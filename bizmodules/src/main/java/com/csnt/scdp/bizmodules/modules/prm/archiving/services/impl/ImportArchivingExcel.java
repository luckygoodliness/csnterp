package com.csnt.scdp.bizmodules.modules.prm.archiving.services.impl;

import com.csnt.scdp.bizmodules.modules.common.helper.ImportExcelHelper;
import com.csnt.scdp.bizmodules.modules.prm.archiving.dto.PrmArchivingDetailDto;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.MapUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * M3_C23_F1_归档明细导入
 * Created by xiezf on 2016/8/29.
 */
public class ImportArchivingExcel extends ImportExcelHelper {
//    D:\project\ProZH\01_Source\bizmodules\src\main\java\com\csnt\scdp\bizmodules\modules\prm\archiving\template\ExcelStructure.xml
    private final static String EXCEL_STRUCTURE_XML_PATH = "com/csnt/scdp/bizmodules/modules/prm/archiving/template/ExcelStructure.xml";
    List<PrmArchivingDetailDto> prmArchivingDetailList;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    List lastColumnValueList = new ArrayList<>();//存放最后一列的数据

    @Override
    public Map doParseExcel(Map inMap) {
        HttpServletRequest request = (HttpServletRequest) inMap.get(CommonAttribute.HTTP_REQUEST);
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> files = multipartRequest.getFiles("uploadFile");
        rsMap = new HashMap<>();
        errorMsg = new ArrayList<String>();
        prmArchivingDetailList = new ArrayList<PrmArchivingDetailDto>();
        try {
            for (Iterator<MultipartFile> iterator = files.iterator(); iterator.hasNext(); ) {
                MultipartFile file = iterator.next();
                workBook = creat(file.getInputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        parseXMLUtil(EXCEL_STRUCTURE_XML_PATH);
        readExcelData();
        exportMsg();

        rsMap.put("resData", prmArchivingDetailList);
        return rsMap;
    }

    @Override
    public void convertMapToPojo() {
        List excelData = this.getListDatas();
        try {
            for (int i = 0; i < excelData.size(); i++) {
                PrmArchivingDetailDto prmArchivingDetail = (PrmArchivingDetailDto)BeanUtil.map2Dto((HashMap) this.getListDatas().get(i), PrmArchivingDetailDto.class);
                prmArchivingDetailList.add(prmArchivingDetail);
            }
        } catch (Exception e) {
            throw new BizException("转换map对象为pojo对象失败");
        }
    }

    @Override
    public void exportMsg() {
        String errorMsgLog = "";
        for (int i = 0; i < errorMsg.size(); i++) {
            errorMsgLog += "\r\n" + errorMsg.get(i);
        }
        if (StringUtil.isEmpty(errorMsgLog)) {
            rsMap.put("succ",true);
        } else {
            rsMap.put("succ",false);
            rsMap.put("errorMsgLog", errorMsgLog);
        }
    }

    //    // 开始从excel读取数据
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
        for (int r = 1; r < excelRowNum + 1; r++) {
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
        for (int r = 1; r < excelRowNum + 1; r++) {//行循环
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
                        if (StringUtil.isNotEmpty(value)) {
                            value = value.trim();
                        } else if (StringUtil.isEmpty(value)) {
                            breakFlag = true;
                            break;
                        }

                        String curColType = (String) curColMap.get("type");
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
        Row excelheadRow = sheet.getRow(0);
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
