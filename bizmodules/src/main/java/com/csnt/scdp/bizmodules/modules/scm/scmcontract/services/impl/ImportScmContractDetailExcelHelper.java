package com.csnt.scdp.bizmodules.modules.scm.scmcontract.services.impl;

import com.csnt.scdp.bizmodules.entity.scm.ScmContract;
import com.csnt.scdp.bizmodules.entity.scm.ScmContractDetail;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmContractDetailAttribute;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpCodeHelper;
import com.csnt.scdp.bizmodules.modules.common.helper.ImportExcelHelper;
import com.csnt.scdp.bizmodules.modules.scm.scmcontract.dto.ScmContractDto;
import com.csnt.scdp.bizmodules.modules.scm.scmcontract.services.intf.ScmcontractManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MapUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * f
 * Created by Little on 2015/11/16.
 */
public class ImportScmContractDetailExcelHelper extends ImportExcelHelper {


    //模板规则存放地址
    private final static String EXCEL_STRUCTURE_XML_PATH = "com/csnt/scdp/bizmodules/modules/scm/scmcontract/template/ExcelStructure.xml";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    List<ScmContractDetail> scmContractDetailList;
    String scmContractUuid;
    List lastColumnValueList = new ArrayList<>();//存放最后一列的数据
    boolean isInsert;

    public Map doParseExcel(Map inMap) {
        HttpServletRequest request = (HttpServletRequest) inMap.get(CommonAttribute.HTTP_REQUEST);
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> files = multipartRequest.getFiles("uploadFile");
        List uploadData = (List) inMap.get("uploadMeta");
        rsMap = new HashMap<>();
        errorMsg = new ArrayList<String>();
        scmContractDetailList = new ArrayList<ScmContractDetail>();
        scmContractUuid = uploadData.get(0).toString();// 获取前台传入的ScmContract的uuid
        isInsert = false;
        try {
            for (Iterator<MultipartFile> iterator = files.iterator(); iterator.hasNext(); ) {
                MultipartFile file = iterator.next();
                workBook = creat(file.getInputStream());
            }
        } catch (InvalidFormatException e) {
            throw new BizException("文件解析异常！");
        } catch (IOException e) {
            throw new BizException("文件解析异常！");
        }
        parseXMLUtil(EXCEL_STRUCTURE_XML_PATH);
        readExcelData();
        if (errorMsg.size() == 0) {
            LogicVerification();
            if (errorMsg.size() == 0) {
                saveDataToDataBase();
                ScmContractDto scmContractDto = (ScmContractDto) DtoHelper.findDtoByPK(ScmContractDto.class, scmContractUuid);
                scmContractDto.setContractState("2");
                scmContractDto.setEditflag("*");
                if(StringUtil.isNotEmpty(scmContractDto.getAmount())){
                    ScmcontractManager scmcontractManager=BeanFactory.getBean("scmcontract-manager");
                    scmcontractManager.clearingBalance(scmContractDto);
                }
//                ScmContractDto scmContractDto = (ScmContractDto) DtoHelper.findDtoByPK(ScmContractDto.class, scmContractUuid);
//                scmContractDto.setContractState("2");
//                scmContractDto.setEditflag("*");
                DtoHelper.CascadeSave(scmContractDto);
            } else {
                exportMsg();
            }
        } else {
            exportMsg();
        }

        return rsMap;
    }

    // 根据数据字典转换对象属性值
    public void changeDataFromDictionary() {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        Map map = new HashMap<>();
        {
            int i = 5;
            for (ScmContractDetail l : scmContractDetailList) {
                String units = l.getUnits();
                List lstScdpCode = ErpCodeHelper.findByCodeTypeAndCodeDesc("CDM_UNIT", units);
                if (ListUtil.isNotEmpty(lstScdpCode) && lstScdpCode.size() == 1) {
                    map = (Map) (lstScdpCode.get(0));
                    l.setUnits((String) map.get("sysCode"));
                } else {
                    errorMsg.add("合同详细第" + i + "行>单位，名称有误！");
                }
                i++;
            }
        }
    }

    // 数据逻辑验证
    public void LogicVerification() {
        BigDecimal rate = new BigDecimal(0);
        if (lastColumnValueList.get(0) != null) {
            rate = new BigDecimal(lastColumnValueList.get(0) + "");
        }
        BigDecimal total1 = new BigDecimal(0);
        BigDecimal total2 = new BigDecimal(0);
        if (lastColumnValueList.get(2) != null) {
            total1 = new BigDecimal(lastColumnValueList.get(2) + "");
        }
        for (int i = 4; i < lastColumnValueList.size(); i++) {
            if (StringUtil.isNotEmpty(lastColumnValueList.get(i))) {
                BigDecimal temp = new BigDecimal(lastColumnValueList.get(i) + "");
                total2 = total2.add(temp);
            }
        }
        if (total1.compareTo(total2) != 0) {
            errorMsg.add("面价总计和所有条款金额之和不相等，请检查Excel内容是否正确！");
        }
        //校验该合同的合同总额是否等于EXCEL的合同实际总额
//        ScmContract scmContract = PersistenceFactory.getInstance().findByPK(ScmContract.class, scmContractUuid);
//        if (scmContract != null) {
//            Object amount = scmContract.getAmount();
//            if (amount != null) {
//                if (total1.compareTo(new BigDecimal(amount + "")) != 0) {
//                    errorMsg.add("Excel面价总计和该合同的合同总额不相等，请调整！");
//                }
//            } else {
//                errorMsg.add("该合同总额未录入，请录入合同总额后再导入！");
//            }
//        } else {
//            throw new BizException("未找到该合同！");
//        }
        for (int i = 0; i < listDatas.size(); i++) {
            BigDecimal temp = (BigDecimal) ((Map) listDatas.get(i)).get("unitPriceTalk");
            BigDecimal truePrice = (temp).multiply(rate);
            ((Map) listDatas.get(i)).put("unitPriceTrue", truePrice);
            scmContractDetailList.get(i).setUnitPriceTrue(truePrice);
        }
    }

    // 保存数据到数据库
    public void saveDataToDataBase() {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        int i=1;
        for (ScmContractDetail scmContractDetail : scmContractDetailList) {
            scmContractDetail.setScmContractId(scmContractUuid);
            scmContractDetail.setIsRepair(0);
            scmContractDetail.setSeqNo(i);
            i+=1;
        }
        //批量删除原有数据
        Map mapCondition = new HashMap();
        mapCondition.put(ScmContractDetailAttribute.SCM_CONTRACT_ID, scmContractUuid);
        List<ScmContractDetail> scmContractDetailListOld = pcm.findByAnyFields(ScmContractDetail.class, mapCondition, null);
        if (ListUtil.isNotEmpty(scmContractDetailListOld)) {
            pcm.batchDelete(scmContractDetailListOld);
        }
        pcm.batchInsert(scmContractDetailList);
        rsMap.put("saveFlag", Boolean.TRUE);
        rsMap.put("scmContractUuid", scmContractUuid);
    }

    // 输出出错信息到excel，暂时保存在本地目录
    public void exportMsg() {
        String errorMsgLog = "";
        for (int i = 0; i < errorMsg.size(); i++) {
            errorMsgLog += "\r\n" + errorMsg.get(i);
        }
        if (StringUtil.isEmpty(errorMsgLog)) {
            rsMap.put("saveFlag", Boolean.TRUE);
            rsMap.put("scmContractUuid", scmContractUuid);
        } else {
            rsMap.put("errorMsgLog", errorMsgLog);
        }
    }

    // 转换map对象为pojo
    public void convertMapToPojo() {
        try {
            for (int i = 0; i < this.getListDatas().size(); i++) {
                ScmContractDetail scmContractDetail = BeanFactory.getObject(ScmContractDetail.class);
                BeanUtil.bean2Bean(this.getListDatas().get(i), scmContractDetail);
                scmContractDetailList.add(scmContractDetail);
            }
        } catch (Exception e) {
            throw new BizException("转换map对象为pojo对象失败");
        }
    }

    // 开始从excel读取数据
    public void readExcelData() {
        int sheetSize = this.workBook.getNumberOfSheets();
        int xmlSheetNum = this.getEntityMap().size();
        if (sheetSize != xmlSheetNum) {
            errorMsg.add("表数目不对，应为：" + xmlSheetNum + "个");
        } else {
            for (int i = 0; i < sheetSize; i++) {
                sheet = workBook.getSheetAt(i);
                String entityName = workBook.getSheetName(i);
                readSheetData(sheet, entityName);
            }
        }
    }

    // 读每个sheet页的数据
    public void readSheetData(Sheet sheet, String entityName) {
        int rowNumbers = sheet.getPhysicalNumberOfRows();
        if (rowNumbers == 0) {
            errorMsg.add("表：" + entityName + "，数据为空");
        } else {
            Map ent = (Map) this.getEntityMap().get(entityName);
            if (MapUtil.isEmpty(ent)) {
                errorMsg.add("表：" + entityName + "，名称不对");
            } else {
                this.setCurEntityCode((String) ent.get("code"));
                List colList = (List) this.getColumnListMap().get(entityName);
                int xmlRowNum = colList.size();
                Row excelRow = sheet.getRow(0);
                int excelFirstRow = excelRow.getFirstCellNum();
                int excelLastRow = excelRow.getLastCellNum();
                if (xmlRowNum != (excelLastRow - excelFirstRow)) {
                    errorMsg.add("表：" + entityName + "，列数应为：" + xmlRowNum + "，请核对");
                } else {
                    readSheetHeadData(sheet);
                    readSheetColumnData(sheet, entityName);
                }
            }
            if (errorMsg.size() == 0) {
                convertMapToPojo();
            }
        }
    }

    // 读取sheet页里面的数据
    public void readSheetColumnData(Sheet sheet, String entityName) {
        Row excelHeadRow = sheet.getRow(0);
        int excelLastCell = excelHeadRow.getLastCellNum();   //excel总列数
        int excelRowNum = sheet.getLastRowNum();  //excel总行数
        Map headMap = (Map) this.getCurEntityHeadMap().get(this.getCurEntityCode());
        Map colMap = this.getColumnMap();
        listDatas = new ArrayList();
        for (int r = 0; r < excelRowNum + 1; r++) {
            Row columnRow = sheet.getRow(r);
            if (columnRow != null) {
                int c = excelLastCell - 1;
                Cell colCell = columnRow.getCell(c);
                String value = this.getStringCellValue(colCell);
                if (value != null) {
                    value = value.trim();
                    lastColumnValueList.add(value);
                }
            }
        }
        boolean breakFlag = false;
        for (int r = 4; r < excelRowNum + 1; r++) {//行循环
            Row columnRow = sheet.getRow(r);
            if (columnRow != null) {
                Map curRowCellMap = new HashMap();
                for (int c = 0; c < excelLastCell; c++) { //列循环
                    String headTitle = headMap.get(c).toString();
                    Map curColMap = (Map) colMap.get(entityName + "_" + headTitle);
                    if (MapUtil.isEmpty(curColMap)) {
                        errorMsg.add("表：" + entityName + "，列：" + headTitle + "，名称不对");
                    } else {
                        String curColCode = (String) curColMap.get("code");
                        Cell colCell = columnRow.getCell(c);
                        String value = this.getStringCellValue(colCell);
                        if (c == 1) {
                            if (StringUtil.isNotEmpty(value)) {
                                value = value.trim();
                            } else if (StringUtil.isEmpty(value)) {
                                breakFlag = true;
                                break;
                            }
                        } else {
                            if (value != null) {
                                value = value.trim();
                            }
                        }

                        String curColType = (String) curColMap.get("type");
                        if (c != excelLastCell - 1) {
                            switch (curColType) {
                                case "Integer":
                                    try {
                                        if (StringUtil.isNotEmpty(value)) {
                                            Integer intVal = new Integer(value);
                                            curRowCellMap.put(curColCode, intVal);
                                        } else {
                                            curRowCellMap.put(curColCode, new Integer(0));
                                        }
                                    } catch (NumberFormatException e) {
                                        errorMsg.add("表：" + entityName + "，行：" + r + "，列：" + headTitle + "，格式错误");
                                    }
                                    break;
                                case "BigDecimal":
                                    try {
                                        if (StringUtil.isNotEmpty(value)) {
                                            BigDecimal bigVal = new BigDecimal(value);
                                            curRowCellMap.put(curColCode, bigVal);
                                        } else {
                                            curRowCellMap.put(curColCode, new BigDecimal(0));
                                        }
                                    } catch (NumberFormatException e) {
                                        errorMsg.add("表：" + entityName + "，行：" + r + "，列：" + headTitle + "，格式错误");
                                    }
                                    break;
                                case "Date":
                                    try {
                                        if (StringUtil.isNotEmpty(value)) {
                                            Date dateVal = sdf.parse(value);
                                            curRowCellMap.put(curColCode, dateVal);
                                        } else {
                                            curRowCellMap.put(curColCode, null);
                                        }
                                    } catch (ParseException e) {
                                        errorMsg.add("表：" + entityName + "，行：" + r + "，列：" + headTitle + "，格式错误");
                                    }
                                    break;
                                default:
                                    curRowCellMap.put(curColCode, value);
                            }
                        }
                        validateCellData(r + 1, colCell, entityName, headTitle, curColType);
                    }
                }
                if (breakFlag) {
                    break;
                } else {
                    listDatas.add(curRowCellMap);
                }
            }
        }
    }

    // 读取sheet页中的表头信息
    public void readSheetHeadData(Sheet sheet) {
        Map headMap = new HashMap();
        curEntityHeadMap = new HashMap();
        Row excelheadRow = sheet.getRow(3);
        int excelLastColumn = excelheadRow.getLastCellNum();
        String headTitle = "";
        for (int i = 0; i < excelLastColumn; i++) {
            Cell cell = excelheadRow.getCell(i);
            headTitle = this.getStringCellValue(cell).trim();
            headMap.put(i, headTitle);
        }
        curEntityHeadMap.put(this.getCurEntityCode(), headMap);
    }
}
