package com.csnt.scdp.bizmodules.modules.common.helper;

import com.csnt.scdp.framework.bo.QueryCondition;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.MessageHelper;
import com.csnt.scdp.framework.util.MapUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.framework.util.XmlUtil;
import com.csnt.scdp.sysmodules.entity.ScdpRole;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.entityattributes.ScdpRoleAttribute;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by huangming on 2015-11-04.
 */
public abstract class ImportExcelHelper {
    private final static String RULE_NAME_NULLABLE = "nullable";
    public Map rsMap;
    public Workbook workBook;
    public Sheet sheet;
    public Map entityMap;
    public Map columnMap;
    public Map ruleMap;
    public Map columnRulesMap;
    public Map columnListMap;
    public List columnList;
    public List<String> errorMsg;// 出错信息
    public String curEntityCode;
    public Map curEntityHeadMap;
    public List listDatas;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private Map<String, String> projectStageMap;
    private Map<String, String> userNameMap;
    private Map<String, String> roleNameMap;
    private HashMap infoMap = new HashMap();

    /**
     * 转换EXCELS
     *
     * @param inMap
     * @return
     */
    public abstract Map doParseExcel(Map inMap);

    // 创建workbook
    public Workbook creat(InputStream in) throws IOException,
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
        throw new BizException(MessageHelper.getMessage("MSG_PRM_IMP_EXCEL_VERSION_ERROR"));
    }

    // 开始从excel读取数据
    public void readExcelData() {
        int sheetSize = this.workBook.getNumberOfSheets();
        int xmlSheetNum = this.getEntityMap().size();
        if (sheetSize != xmlSheetNum) {
            infoMap.put("xmlSheetNum", xmlSheetNum);
            errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_SHEET_NUM_ERROR", infoMap));
        } else {
            for (int i = 0; i < sheetSize; i++) {
                infoMap.clear();
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
            infoMap.put("entityName", entityName);
            errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_SHEET_NO_DATA_ERROR", infoMap));
        } else {
            Map ent = (Map) this.getEntityMap().get(entityName);
            if (MapUtil.isEmpty(ent)) {
                infoMap.put("entityName", entityName);
                errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_SHEET_NAME_ERROR", infoMap));
            } else {
                this.setCurEntityCode((String) ent.get("code"));
                List colList = (List) this.getColumnListMap().get(entityName);
                int xmlRowNum = colList.size();
                Row excelRow = sheet.getRow(0);
                int excelFirstRow = excelRow.getFirstCellNum();
                int excelLastRow = excelRow.getLastCellNum();
                if (xmlRowNum != (excelLastRow - excelFirstRow)) {
                    infoMap.put("entityName", entityName);
                    infoMap.put("xmlRowNum", xmlRowNum);
                    errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_SHEET_COLUMN_NUMBER_ERROR", infoMap));
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

    // 读取sheet页里面的数据
    public void readSheetColumnData(Sheet sheet, String entityName) {
        Row excelheadRow = sheet.getRow(0);
        int excelLastcell = excelheadRow.getLastCellNum();   //excel总列数
        int excelRowNum = sheet.getLastRowNum();  //excel总行数
        Map headMap = (Map) this.getCurEntityHeadMap().get(this.getCurEntityCode());
        Map colMap = this.getColumnMap();
        listDatas = new ArrayList();

        outer:
        for (int row = 1; row < excelRowNum + 1; row++) {//行循环
            Row columnRow = sheet.getRow(row);
            if (columnRow != null) {
                Map curRowCellMap = new HashMap();
                for (int c = 0; c < excelLastcell; c++) { //列循环
                    String headTitle = "";
                    headTitle = headMap.get(c).toString();
                    Map curColMap = (Map) colMap.get(entityName + "_" + headTitle);
                    if (MapUtil.isEmpty(curColMap)) {
                        infoMap.put("entityName", entityName);
                        infoMap.put("headTitle", headTitle);
                        errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_SHEET_COLUMN_NAME_ERROR", infoMap));
                    } else {
                        String curColCode = (String) curColMap.get("code");
                        Cell colCell = columnRow.getCell(c);
                        String value = this.getStringCellValue(colCell);
                        value = value.trim();
                        if (c == 0 && value.equals("")) {
                            break outer;
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
                                    infoMap.put("entityName", entityName);
                                    infoMap.put("row", row);
                                    infoMap.put("headTitle", headTitle);
                                    errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_SHEET_DATA_FORMAT_ERROR", infoMap));
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
                                    infoMap.put("entityName", entityName);
                                    infoMap.put("row", row);
                                    infoMap.put("headTitle", headTitle);
                                    errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_SHEET_DATA_FORMAT_ERROR", infoMap));
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
                                    infoMap.put("entityName", entityName);
                                    infoMap.put("row", row);
                                    infoMap.put("headTitle", headTitle);
                                    errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_SHEET_DATA_FORMAT_ERROR", infoMap));
                                }
                                break;
                            default:
                                curRowCellMap.put(curColCode, value);
                        }
                        validateCellData(row + 1, colCell, entityName, headTitle, curColType);
                    }
                }
                listDatas.add(curRowCellMap);
            } else {
                break outer;
            }
        }
        if (errorMsg.size() == 0) {
            convertMapToPojo();
        }
    }

    // 转换map对象为pojo
    public abstract void convertMapToPojo();

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
                        infoMap.put("entityName", entityName);
                        infoMap.put("curRow", curRow);
                        infoMap.put("headTitle", headTitle);
                        infoMap.put("rulMsg", rulMsg);
                        errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_SHEET_DATA_RULE_BREAK", infoMap));
                    }
                } else {
                    // 这里写其他的验证规则
                }
            }
        }
    }

    // 解析保存excel数据表结构信息的xml文件
    public void parseXMLUtil(String xmlPath) {
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
    public void parseEntity(Element entity) {
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
    public void setEntityMap(Element entity) {
        Map ent = new HashMap();
        columnList = new ArrayList();
        String name = entity.attributeValue("name");
        String code = entity.attributeValue("code");
        ent.put("name", name);
        ent.put("code", code);
        entityMap.put(name, ent);
    }

    // 将column放入columnMap中
    public void setColumnMap(String entityName, Element column) {
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
    public void setRuleMap(String entityName, String columnName, Element ruleValid) {
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

    // 跟据cell的类型输出相应的String值
    public String getStringCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        DecimalFormat df = new DecimalFormat("#.###");
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

    // 输出出错信息到excel，暂时保存在本地目录
    public abstract void exportMsg();

    // 数据逻辑验证
    public void LogicVerification() {

    }

    // 从数据库查出所有需要转换的字典值
    private void getDictionaryDataFromDatabase(Set userNames, Set roleNames) {
        List lstUserNames = new ArrayList<>(userNames);
        QueryCondition queryCondition = new QueryCondition();
        queryCondition.setField(ScdpUserAttribute.USER_NAME);
        queryCondition.setOperator("in");
        queryCondition.setValueList(lstUserNames);
        List<QueryCondition> lstCondition = new ArrayList<>();
        lstCondition.add(queryCondition);
        List<ScdpUser> scdpUsers = PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, lstCondition, null);
        List lstRoleNames = new ArrayList<>(roleNames);
        queryCondition.setField(ScdpRoleAttribute.ROLE_NAME);
        queryCondition.setOperator("in");
        queryCondition.setValueList(lstRoleNames);
        lstCondition.clear();
        lstCondition.add(queryCondition);
        List<ScdpRole> scdpRoles = PersistenceFactory.getInstance().findByAnyFields(ScdpRole.class, lstCondition, null);
        List lstScdpCodeOfProjectStage = ErpCodeHelper.findByCodeType("CDM_PROJECT_STAGE");
        userNameMap = new HashMap<String, String>();
        roleNameMap = new HashMap<String, String>();
        projectStageMap = new HashMap<String, String>();
        for (ScdpUser scdpUser : scdpUsers) {
            userNameMap.put(scdpUser.getUserName(), scdpUser.getUserId());
        }
        for (ScdpRole scdpRole : scdpRoles) {
            roleNameMap.put(scdpRole.getRoleName(), scdpRole.getUuid());
        }
        for (Iterator iter = lstScdpCodeOfProjectStage.iterator(); iter.hasNext(); ) {
            Map projectStage = (Map) iter.next();
            String codeDesc = (String) projectStage.get("codeDesc");
            String sysCode = (String) projectStage.get("sysCode");
            projectStageMap.put(codeDesc, sysCode);
        }
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
