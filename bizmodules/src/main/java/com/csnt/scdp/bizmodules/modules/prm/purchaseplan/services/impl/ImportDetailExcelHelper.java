package com.csnt.scdp.bizmodules.modules.prm.purchaseplan.services.impl;

import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.dto.PrmPurchasePlanDetailDto;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.MapUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.framework.util.XmlUtil;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by yangjinsan on 2015/11/9.
 */
public class ImportDetailExcelHelper {

    private final static String RULE_NAME_NULLABLE = "nullable";
    private final static String PRM_PURCHASEPLAN_DETAIL = "PrmPurchaseplanDetail";
    private final static String PRM_PURCHASEPLAN_DETAIL_DTO = "PrmPurchasePlanDetailDto";
    private final static String EXCEL_STRUCTURE_XML_PATH = "com/csnt/scdp/bizmodules/modules/prm/purchaseplan/template/ExcelStructure.xml";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private PrmPurchasePlanDetailDto detailDto = new PrmPurchasePlanDetailDto();
    private String purchaseplanDetailUuid;
    private Map rsMap;
    private Workbook workBook;
    private Sheet sheet;
    private Map entityMap;
    private Map columnMap;
    private Map ruleMap;
    private Map columnRulesMap;
    private Map columnListMap;
    private List columnList;
    private List<String> errorMsg;// 出错信息
    private String curEntityCode;
    private Map curEntityHeadMap;
    private List listDatas;


    public Map doParseExcel(Map inMap) {
        HttpServletRequest request = (HttpServletRequest) inMap.get(CommonAttribute.HTTP_REQUEST);
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> files = multipartRequest.getFiles("uploadFile");
        List uploadData = (List) inMap.get("uploadMeta");
        rsMap = new HashMap<>();
        errorMsg = new ArrayList<String>();

        purchaseplanDetailUuid = uploadData.get(0).toString();// 获取前台传入的uuid
        rsMap.put("viewData", "保存失败！出错信息保存在C:/temp下");// 回显值，默认显示失败，saveDataToDataBase()方法成功则显示成功
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
        ParseXMLUtil(EXCEL_STRUCTURE_XML_PATH);
        readExcelData();
        if (errorMsg.size() > 0) {
            exportMsg();
        }
        return rsMap;
    }

    // 创建workbook
    private Workbook creat(InputStream in) throws IOException,
            InvalidFormatException {
        if (!in.markSupported()) {
            in = new PushbackInputStream(in, 8);
        }
        if (POIFSFileSystem.hasPOIFSHeader(in)) {
            return new HSSFWorkbook(in);
        }
        if (POIXMLDocument.hasOOXMLHeader(in)) {
            return new XSSFWorkbook(OPCPackage.open(in));
        }
        throw new BizException("你的excel版本目前poi解析不了");
    }

    // 解析保存excel数据表结构信息的xml文件
    private void ParseXMLUtil(String xmlPath) {
        columnMap = new HashMap();
        entityMap = new HashMap();
        ruleMap = new HashMap();
        columnRulesMap = new HashMap();
        columnListMap = new HashMap();
        Document doc = XmlUtil.getDoc(xmlPath);
        if (doc != null) {
            Element root = doc.getRootElement();
            Iterator itEntity = root.elements("entity").iterator();
            while (itEntity.hasNext()) {
                Element entity = (Element) itEntity.next();
                parseEntity(entity);
            }
        }
    }

    // 解析entity
    private void parseEntity(Element entity) {
        if (entity != null) {
            setEntityMap(entity);
            String entityName = entity.attributeValue("name");
            Iterator itColumn = entity.elements("column").iterator();
            while (itColumn.hasNext()) {
                Element column = (Element) itColumn.next();
                setColumnMap(entityName, column);
            }
            columnListMap.put(entityName, columnList);
        }
    }

    // 将entity放入entityMap中
    private void setEntityMap(Element entity) {
        Map ent = new HashMap();
        columnList = new ArrayList();
        String name = entity.attributeValue("name");
        String code = entity.attributeValue("code");
        ent.put("name", name);
        ent.put("code", code);
        entityMap.put(name, ent);
    }

    // 开始从excel读取数据
    private void readExcelData() {
        int sheetSize = workBook.getNumberOfSheets();
        int flag = -1;
        for (int i = 0; i < sheetSize; i++) {
            sheet = workBook.getSheetAt(i);
            String entityName = workBook.getSheetName(i);
            if (entityMap.get(entityName) == null) {
                continue;
            }
            flag = 1;
            readSheetData(sheet);
        }
        if (flag == -1) {
            throw new BizException("未读取到相关数据，请检查文件！");
        }
//        }
    }


    // 输出出错信息到excel，暂时保存在本地目录
    private void exportMsg() {
        // 创建Workbook
        HSSFWorkbook wb = new HSSFWorkbook();

        // 创建Excel的工作sheet,对应到一个excel文档的tab
        HSSFSheet sheet = wb.createSheet("导入结果");

        // 设置excel每列宽度
        sheet.setColumnWidth(0, 25000);

        // 创建字体样式
        HSSFFont font = wb.createFont();
        font.setFontName("Verdana");
        font.setBoldweight((short) 100);
        font.setFontHeight((short) 300);
        font.setColor(HSSFColor.RED.index);

        // 创建单元格样式
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        // 设置边框
        style.setBottomBorderColor(HSSFColor.RED.index);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);

        style.setFont(font);// 设置字体

        for (int i = 0; i < errorMsg.size(); i++) {
            HSSFRow row = sheet.createRow(i);// 创建Excel的sheet的一行
            row.setHeight((short) 500);// 设定行的高度
            HSSFCell cell = row.createCell(0);// 创建一个Excel的单元格
            cell.setCellStyle(style);// 给Excel的单元格设置样式
            cell.setCellValue(errorMsg.get(i));// 给Excel的单元格赋值
        }

        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
            String date = sdf.format(cal.getTime());
            String folderPath = "C:\\temp\\";
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdir();
            }
            FileOutputStream os = new FileOutputStream(folderPath + date + ".xls");
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 将column放入columnMap中
    private void setColumnMap(String entityName, Element column) {
        if (column != null) {
            Map col = new HashMap();
            String name = column.attributeValue("name");
            String code = column.attributeValue("code");
            String type = column.attributeValue("type");
            col.put("name", name);
            col.put("code", code);
            col.put("type", type);
            String columnMapKey = entityName + "_" + name;    // eg:立项主信息_合同名称
            columnMap.put(columnMapKey, col);
            columnList.add(col);
            Iterator ruleIt = column.elements("rules").iterator();  // 获得rules
            while (ruleIt.hasNext()) {
                Element rules = (Element) ruleIt.next();
                Iterator rule = rules.elements("rule").iterator();   // 获得rule
                while (rule.hasNext()) {
                    Element ruleValid = (Element) rule.next();     // 获得每一行rule
                    setRuleMap(entityName, name, ruleValid);
                }
            }
        }
    }


    // 将rule验证规则放入ruleMap中
    private void setRuleMap(String entityName, String columnName, Element ruleValid) {
        if (ruleValid != null) {
            String ruleName = ruleValid.attributeValue("name");
            String ruleMsg = ruleValid.attributeValue("message");
            Map ruleValidMap = new HashMap();
            ruleValidMap.put("name", ruleName);
            ruleValidMap.put("message", ruleMsg);
            String ruleStrKey = entityName + "_" + columnName + "_" + ruleName;
            String colStrKey = entityName + "_" + columnName;
            if (this.getColumnRulesMap().containsKey(colStrKey)) {
                List valids = (List) this.getColumnRulesMap().get(colStrKey);
                valids.add(ruleValidMap);
            } else {
                List valids = new ArrayList();
                valids.add(ruleValidMap);
                this.columnRulesMap.put(colStrKey, valids);  //将每个column下的所有rules存入该map中
            }
            ruleMap.put(ruleStrKey, ruleValidMap); //将每个column下的一条rule存入该map中
        }
    }

    // 读每个sheet页的数据
    public void readSheetData(Sheet sheet) {
        String entityName = sheet.getSheetName();
        int rowNumbers = sheet.getPhysicalNumberOfRows();
        if (rowNumbers == 0) {
            errorMsg.add("表：" + entityName + "，数据为空");
        } else {
            Map ent = (Map) this.getEntityMap().get(entityName);
            if (MapUtil.isEmpty(ent)) {
//                errorMsg.add("表：" + entityName + "，名称不对");
            } else {
                this.setCurEntityCode((String) ent.get("code"));
                List colList = (List) this.getColumnListMap().get(entityName);
                int xmlRowNum = colList.size();
                Row excelRow = sheet.getRow(0);
                int excelFirstRow = excelRow.getFirstCellNum();
                int excelLastRow = excelRow.getLastCellNum();
                if (xmlRowNum != (excelLastRow - excelFirstRow)) {
                    errorMsg.add("表：" + entityName + "，列数应为：" + xmlRowNum + "，请核对!");
                } else {
                    readSheetHeadData(sheet);
                    readSheetColumnData(sheet, entityName);
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

    // 跟据cell的类型输出相应的String值
    private String getStringCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        DecimalFormat df = new DecimalFormat("#.##");
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                cellValue = cell.getRichStringCellValue().getString().trim();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    cellValue = sdf.format(cell.getDateCellValue());
                } else {
                    cellValue = df.format(cell.getNumericCellValue()).toString();
                }
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue()).trim();
                break;
            case HSSFCell.CELL_TYPE_FORMULA:
                try {
                    cellValue = String.valueOf(cell.getNumericCellValue());
                } catch (IllegalStateException e) {
                    cellValue = cell.getStringCellValue();
                }
                break;
            default:
                cellValue = "";
        }
        return cellValue;
    }

    // 读取sheet页里面的数据
    public void readSheetColumnData(Sheet sheet, String entityName) {
        Row excelheadRow = sheet.getRow(0);
        int excelLastcell = excelheadRow.getLastCellNum();   //excel总列数
        int excelRowNum = sheet.getLastRowNum();  //excel总行数
        Map headMap = (Map) this.getCurEntityHeadMap().get(this.getCurEntityCode());
        Map colMap = this.getColumnMap();
        listDatas = new ArrayList();

        boolean isFirstCellNull = true;//当行的第一个单元格为空时跳出
        for (int r = 1; r < excelRowNum + 1 && isFirstCellNull; r++) {//行循环
            Row columnRow = sheet.getRow(r);
            if (columnRow != null) {
                Map curRowCellMap = new HashMap();
                for (int c = 0; c < excelLastcell; c++) { //列循环
                    String headTitle = "";
                    headTitle = headMap.get(c).toString();
                    Map curColMap = (Map) colMap.get(entityName + "_" + headTitle);
                    if (MapUtil.isEmpty(curColMap)) {
                        errorMsg.add("表：" + entityName + "，列：" + headTitle + "，名称不对");
                    } else {
                        String curColCode = (String) curColMap.get("code");
                        String curColType = (String) curColMap.get("type");
                        Cell colCell = columnRow.getCell(c);
                        String value = this.getStringCellValue(colCell);
                        if(c == 0 && StringUtil.isEmpty(value)){
                            isFirstCellNull = false;
                            break;
                        }
                        if (value != null) {
                            value = value.trim();
                        }
                        String xmlColType = (String) curColMap.get("type");
                        switch (xmlColType) {
                            case "String":
                                try {
                                    String intVal = new String(value);
                                    curRowCellMap.put(curColCode, intVal);
                                } catch (NumberFormatException e) {
                                    errorMsg.add("表：" + entityName + "，行：" + r + "，列：" + headTitle + "，格式错误");
                                }
                                break;
                            case "BigDecimal":
                                try {
                                    BigDecimal bigVal = null;
                                    if (value == "") {
                                        bigVal = new BigDecimal(0);
                                    } else {
                                        bigVal = new BigDecimal(value);
                                    }
                                    curRowCellMap.put(curColCode, bigVal);
                                } catch (NumberFormatException e) {
                                    errorMsg.add("表：" + entityName + "，行：" + r + "，列：" + headTitle + "，格式错误");
                                }
                                break;
                            case "Date":
                                try {
                                    Date dateVal = null;
                                    if (value == "") {
                                        //如果是空数据则不需要赋值

                                    } else {
                                        dateVal = sdf.parse(value);
                                    }
                                    curRowCellMap.put(curColCode, dateVal);
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
                //如果存在不为0的单价和数量，并且采购预算没有数据，就重新计算采购预算
                if (curRowCellMap.get("amount") != null && curRowCellMap.get("budgetPrice") != null) {
                    BigDecimal bigAmount = (BigDecimal) curRowCellMap.get("amount");
                    BigDecimal bigMoney = (BigDecimal) curRowCellMap.get("budgetPrice");
                    Double amount = bigAmount.doubleValue();
                    double budgetPrice = bigMoney.doubleValue();
                    if (amount > 0 && budgetPrice > 0) {
                        double budgetMoney = 0d;
                        if (curRowCellMap.get("purchaseBudgetMoney") != null) {
                            BigDecimal purchaseBudgetMoney = (BigDecimal) curRowCellMap.get("purchaseBudgetMoney");
                            budgetMoney = purchaseBudgetMoney.doubleValue();
                        }
                        if (budgetMoney == 0) {
                            curRowCellMap.put("purchaseBudgetMoney", amount * budgetPrice);
                        }
                    }
                }
                if(curRowCellMap.size()>0){
                    Map m = new HashMap<>();
                    m.put("data", curRowCellMap);
                    listDatas.add(m);
                }
            }
        }
        if (errorMsg.size() == 0 && listDatas.size() > 0) {
//            convertMapToDto();
            rsMap.put("listDatas", listDatas);
            rsMap.remove("viewData");
        }
    }

    // 转换map对象为Dto
    private void convertMapToDto() {
        try {
            String currentEntity = this.getCurEntityCode();
            switch (currentEntity) {
                case PRM_PURCHASEPLAN_DETAIL:
                    Map excelData = (Map) this.getListDatas().get(0);
                    if (StringUtil.isEmpty(purchaseplanDetailUuid)) {
                        detailDto = (PrmPurchasePlanDetailDto) DtoHelper.getDtoFromMap(excelData, PRM_PURCHASEPLAN_DETAIL_DTO);
                    } else {
                        detailDto = PersistenceFactory.getInstance().findByPK(PrmPurchasePlanDetailDto.class, purchaseplanDetailUuid);
                    }

                    break;
            }
        } catch (Exception e) {
            throw new BizException("转换map对象为pojo对象失败");
        }
    }

    // 验证单元格数据
    public void validateCellData(int curRow, Cell colCell, String entityName, String headTitle, String curColType) {
        List rulList = (List) this.getColumnRulesMap().get(entityName + "_" + headTitle);
        if (rulList != null && rulList.size() > 0) {
            for (int i = 0; i < rulList.size(); i++) {
                Map rulM = (Map) rulList.get(i);
                String rulName = (String) rulM.get("name");
                String rulMsg = (String) rulM.get("message");
                String cellValue = this.getStringCellValue(colCell).trim();
                if (rulName.equals(RULE_NAME_NULLABLE)) {
                    if (cellValue.equals("")) {
                        errorMsg.add("表：" + entityName + "，行：" + curRow + "，列：" + headTitle + "，" + rulMsg);
                    }
                } else {
                    // 这里写其他的验证规则
                }
            }
        }
    }

    //setter and getter

    public String getPurchaseplanDetailUuid() {
        return purchaseplanDetailUuid;
    }

    public void setPurchaseplanDetailUuid(String purchaseplanDetailUuid) {
        this.purchaseplanDetailUuid = purchaseplanDetailUuid;
    }

    public Map getRsMap() {
        return rsMap;
    }

    public void setRsMap(Map rsMap) {
        this.rsMap = rsMap;
    }

    public Workbook getWorkBook() {
        return workBook;
    }

    public void setWorkBook(Workbook workBook) {
        this.workBook = workBook;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    public Map getEntityMap() {
        return entityMap;
    }

    public void setEntityMap(Map entityMap) {
        this.entityMap = entityMap;
    }

    public Map getColumnMap() {
        return columnMap;
    }

    public void setColumnMap(Map columnMap) {
        this.columnMap = columnMap;
    }

    public Map getRuleMap() {
        return ruleMap;
    }

    public void setRuleMap(Map ruleMap) {
        this.ruleMap = ruleMap;
    }

    public Map getColumnRulesMap() {
        return columnRulesMap;
    }

    public void setColumnRulesMap(Map columnRulesMap) {
        this.columnRulesMap = columnRulesMap;
    }

    public Map getColumnListMap() {
        return columnListMap;
    }

    public void setColumnListMap(Map columnListMap) {
        this.columnListMap = columnListMap;
    }

    public List getColumnList() {
        return columnList;
    }

    public void setColumnList(List columnList) {
        this.columnList = columnList;
    }

    public List<String> getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(List<String> errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getCurEntityCode() {
        return curEntityCode;
    }

    public void setCurEntityCode(String curEntityCode) {
        this.curEntityCode = curEntityCode;
    }

    public Map getCurEntityHeadMap() {
        return curEntityHeadMap;
    }

    public void setCurEntityHeadMap(Map curEntityHeadMap) {
        this.curEntityHeadMap = curEntityHeadMap;
    }

    public List getListDatas() {
        return listDatas;
    }

    public void setListDatas(List listDatas) {
        this.listDatas = listDatas;
    }
}
