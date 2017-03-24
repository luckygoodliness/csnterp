package com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.services.impl;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.attributes.ErpCommonAttribute;
import com.csnt.scdp.bizmodules.attributes.ExpandFieldName;
import com.csnt.scdp.bizmodules.attributes.ExpandFieldType;
import com.csnt.scdp.bizmodules.attributes.PrmBudgetCodes;
import com.csnt.scdp.bizmodules.attributes.TaxType;
import com.csnt.scdp.bizmodules.entity.prm.PrmAssociatedUnitsC;
import com.csnt.scdp.bizmodules.entity.prm.PrmBudgetAccessoryC;
import com.csnt.scdp.bizmodules.entity.prm.PrmBudgetDetailC;
import com.csnt.scdp.bizmodules.entity.prm.PrmBudgetOutsourceC;
import com.csnt.scdp.bizmodules.entity.prm.PrmBudgetPrincipalC;
import com.csnt.scdp.bizmodules.entity.prm.PrmBudgetRunC;
import com.csnt.scdp.bizmodules.entity.prm.PrmContract;
import com.csnt.scdp.bizmodules.entity.prm.PrmContractDetailC;
import com.csnt.scdp.bizmodules.entity.prm.PrmMemberDetailPC;
import com.csnt.scdp.bizmodules.entity.prm.PrmPayDetailPC;
import com.csnt.scdp.bizmodules.entity.prm.PrmProgressDetailPC;
import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMainC;
import com.csnt.scdp.bizmodules.entity.prm.PrmQsPC;
import com.csnt.scdp.bizmodules.entity.prm.PrmReceiptsDetailPC;
import com.csnt.scdp.bizmodules.entity.prm.PrmSquareDetailPC;
import com.csnt.scdp.bizmodules.entityattributes.nonprm.FinancialSubjectAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmAssociatedUnitsCAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmBudgetAccessoryCAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmBudgetDetailCAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmBudgetOutsourceCAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmBudgetPrincipalCAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmBudgetRunCAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmMemberDetailPCAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmPayDetailPCAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProgressDetailPCAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainCAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmQsPCAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmReceiptsDetailPCAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmSquareDetailPCAttribute;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpCodeHelper;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpExpandFieldHelper;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmProjectMainCDto;
import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.services.intf.PurchaseplanManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.bo.QueryCondition;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.helper.MessageHelper;
import com.csnt.scdp.framework.helper.UserHelper;
import com.csnt.scdp.framework.util.ArrayUtil;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MapUtil;
import com.csnt.scdp.framework.util.ObjectUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.framework.util.XmlUtil;
import com.csnt.scdp.sysmodules.entity.ScdpOrg;
import com.csnt.scdp.sysmodules.entity.ScdpRole;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.entityattributes.ScdpCodeAttribute;
import com.csnt.scdp.sysmodules.entityattributes.ScdpOrgAttribute;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by huangming on 2015-11-04.
 */
public class ImportProjectExcelHelper {
    private final static String RULE_NAME_NULLABLE = "nullable";
    private final static String PRM_PROJECT_MAIN_C = "PrmProjectMainC";
    private final static String PRM_ASSOCIATED_UNITS_C = "PrmAssociatedUnitsC";
    private final static String PRM_MEMBER_DETAILP_C = "PrmMemberDetailPC";
    private final static String PRM_PAY_DETAILP_C = "PrmPayDetailPC";
    private final static String PRM_PROGRESS_DETAILP_C = "PrmProgressDetailPC";
    private final static String PRM_SQUARE_DETAILP_C = "PrmSquareDetailPC";
    private final static String PRM_RECEIPTS_DETAILP_C = "PrmReceiptsDetailPC";
    private final static String PRM_QSP_C = "PrmQsPC";
    private final static String PRM_BUDGET_DETAIL_C = "PrmBudgetDetailC";
    private final static String PRM_BUDGET_OUTSOURCE_C = "PrmBudgetOutsourceC";
    private final static String PRM_BUDGET_PRINCIPAL_C = "PrmBudgetPrincipalC";
    private final static String PRM_BUDGET_ACCESSORY_C = "PrmBudgetAccessoryC";
    private final static String PRM_BUDGET_RUN_C = "PrmBudgetRunC";
    private final static String EXCEL_STRUCTURE_XML_PATH = "com/csnt/scdp/bizmodules/modules/prm/prmprojectmainc/template/projectmain_import_excel_template.xml";
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
    PrmProjectMainCDto projectMainC;
    List<PrmAssociatedUnitsC> associatedUnitsCs;
    List<PrmMemberDetailPC> memberDetailPCs;
    List<PrmPayDetailPC> payDetailPCs;
    List<PrmProgressDetailPC> progressDetailPCs;
    List<PrmSquareDetailPC> squareDetailPCs;
    List<PrmReceiptsDetailPC> receiptsDetailPCs;
    List<PrmQsPC> qsPCs;
    List<PrmBudgetDetailC> budgetDetailCs;
    List<PrmBudgetOutsourceC> budgetOutsourceCs;
    List<PrmBudgetPrincipalC> budgetPrincipalCs;
    List<PrmBudgetAccessoryC> budgetAccessoryCs;
    List<PrmBudgetRunC> budgetRunCs;
    String projectMainCUuid;
    String prmContractId;
    String contractPrmCodeType;
    String type;
    boolean isInsert;
    private Map<String, String> projectStageMap;
    private Map<String, String> jobShareMap;
    private Map<String, String> prmCodeTypeMap;
    private Map<String, String> userNameMap;
    private Map<String, String> roleNameMap;
    private HashMap infoMap;

    private String[] revisedSheetCode = new String[]{PRM_PROJECT_MAIN_C, PRM_BUDGET_PRINCIPAL_C};

    public Map doParseExcel(Map inMap) {
        HttpServletRequest request = (HttpServletRequest) inMap.get(CommonAttribute.HTTP_REQUEST);
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> files = multipartRequest.getFiles("uploadFile");
        List uploadData = (List) inMap.get("uploadMeta");
        rsMap = new HashMap<>();
        errorMsg = new ArrayList<String>();
        associatedUnitsCs = new ArrayList<PrmAssociatedUnitsC>();
        memberDetailPCs = new ArrayList<PrmMemberDetailPC>();
        payDetailPCs = new ArrayList<PrmPayDetailPC>();
        progressDetailPCs = new ArrayList<PrmProgressDetailPC>();
        squareDetailPCs = new ArrayList<PrmSquareDetailPC>();
        receiptsDetailPCs = new ArrayList<PrmReceiptsDetailPC>();
        qsPCs = new ArrayList<PrmQsPC>();
        budgetDetailCs = new ArrayList<PrmBudgetDetailC>();
        budgetOutsourceCs = new ArrayList<PrmBudgetOutsourceC>();
        budgetPrincipalCs = new ArrayList<PrmBudgetPrincipalC>();
        budgetAccessoryCs = new ArrayList<PrmBudgetAccessoryC>();
        budgetRunCs = new ArrayList<PrmBudgetRunC>();
        projectMainCUuid = uploadData.get(0).toString();// 获取前台传入的PrmProjectMainC的uuid
        type = uploadData.get(1).toString();// 立项新增或者立项变更
        isInsert = false;
        infoMap = new HashMap();

        try {
            for (Iterator<MultipartFile> iterator = files.iterator(); iterator.hasNext(); ) {
                MultipartFile file = iterator.next();
                workBook = creat(file.getInputStream());
            }
        } catch (InvalidFormatException e) {
            throw new BizException(MessageHelper.getMessage("MSG_PRM_IMP_FILE_PARSING_ERROR"));
        } catch (IOException e) {
            throw new BizException(MessageHelper.getMessage("MSG_PRM_IMP_FILE_PARSING_ERROR"));
        }
        parseXMLUtil(EXCEL_STRUCTURE_XML_PATH);
        readExcelData();

        if (errorMsg.size() == 0) {
            //根据excel数据拿到数据字典
            getDictionaryDataFromDatabase();
            //将名称替换成数据字典code
            changeDataFromDictionary();
            //数据逻辑校验
            LogicVerification();
            if (errorMsg.size() == 0) {
                saveDataToDataBase();
            }
        }
        exportMsg();

        return rsMap;
    }

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
                //
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
                String entityCode = (String) ent.get("code");
                if ("new".equals(this.type) || ArrayUtil.contains(this.revisedSheetCode, entityCode)) {
                    //变更导入时，只读取2个sheet的内容
                    this.setCurEntityCode(entityCode);
                    List colList = (List) this.getColumnListMap().get(entityName);
                    int xmlColumnNum = colList.size();
                    Row excelRow = sheet.getRow(0);
                    int excelFirstColumn = excelRow.getFirstCellNum();
                    int excelLastColumn = excelRow.getLastCellNum();
                    if (xmlColumnNum != (excelLastColumn - excelFirstColumn)) {
                        infoMap.put("entityName", entityName);
                        infoMap.put("xmlColumnNum", xmlColumnNum);
                        errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_SHEET_COLUMN_NUMBER_ERROR", infoMap));
                    } else {
                        readSheetHeadData(sheet);
                        readSheetRowData(sheet, entityName);
                    }
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
    public void readSheetRowData(Sheet sheet, String entityName) {
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
                                    infoMap.put("row", row + 1);
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
                                    infoMap.put("row", row + 1);
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
                                    infoMap.put("row", row + 1);
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
    public void convertMapToPojo() {
        try {
            String currentEntity = this.getCurEntityCode();
            switch (currentEntity) {
                case PRM_PROJECT_MAIN_C:
                    Map excelData = (Map) this.getListDatas().get(0);
                    String isPreProject = (String) excelData.get(PrmProjectMainAttribute.IS_PRE_PROJECT);
                    if ("是".equals(isPreProject) || "1".equals(isPreProject)) {
                        excelData.put(PrmProjectMainAttribute.IS_PRE_PROJECT, Integer.valueOf(1));
                    } else {
                        excelData.put(PrmProjectMainAttribute.IS_PRE_PROJECT, Integer.valueOf(0));
                    }
                    projectMainC = BeanFactory.getObject(PrmProjectMainCDto.class);
                    BeanUtil.bean2Bean(excelData, projectMainC);
                    projectMainC.setDetailType(PrmProjectMainAttribute.PRM_DETAIL_NEW);
                    projectMainC.setState(BillStateAttribute.CMD_BILL_STATE_NEW);
                    break;
                case PRM_ASSOCIATED_UNITS_C:
                    for (int i = 0; i < this.getListDatas().size(); i++) {
                        PrmAssociatedUnitsC prmAssociatedUnitsC = BeanFactory.getObject(PrmAssociatedUnitsC.class);
                        BeanUtil.bean2Bean((Map) this.getListDatas().get(i), prmAssociatedUnitsC);
                        prmAssociatedUnitsC.setSeqNo(new Integer(i + 1));
                        associatedUnitsCs.add(prmAssociatedUnitsC);
                    }
                    break;
                case PRM_MEMBER_DETAILP_C:
                    for (int i = 0; i < this.getListDatas().size(); i++) {
                        PrmMemberDetailPC prmMemberDetailPC = BeanFactory.getObject(PrmMemberDetailPC.class);
                        BeanUtil.bean2Bean((Map) this.getListDatas().get(i), prmMemberDetailPC);
                        prmMemberDetailPC.setSeqNo(new Integer(i + 1));
                        memberDetailPCs.add(prmMemberDetailPC);
                    }
                    break;
                case PRM_PAY_DETAILP_C:
                    for (int i = 0; i < this.getListDatas().size(); i++) {
                        PrmPayDetailPC prmPayDetailPC = BeanFactory.getObject(PrmPayDetailPC.class);
                        BeanUtil.bean2Bean((Map) this.getListDatas().get(i), prmPayDetailPC);
                        prmPayDetailPC.setSeqNo(new Integer(i + 1));
                        payDetailPCs.add(prmPayDetailPC);
                    }
                    break;
                case PRM_PROGRESS_DETAILP_C:
                    for (int i = 0; i < this.getListDatas().size(); i++) {
                        PrmProgressDetailPC prmProgressDetailPC = BeanFactory.getObject(PrmProgressDetailPC.class);
                        BeanUtil.bean2Bean((Map) this.getListDatas().get(i), prmProgressDetailPC);
                        prmProgressDetailPC.setSeqNo(new Integer(i + 1));
                        progressDetailPCs.add(prmProgressDetailPC);
                    }
                    break;
                case PRM_SQUARE_DETAILP_C:
                    for (int i = 0; i < this.getListDatas().size(); i++) {
                        PrmSquareDetailPC prmSquareDetailPC = BeanFactory.getObject(PrmSquareDetailPC.class);
                        BeanUtil.bean2Bean((Map) this.getListDatas().get(i), prmSquareDetailPC);
                        prmSquareDetailPC.setSeqNo(new Integer(i + 1));
                        squareDetailPCs.add(prmSquareDetailPC);
                    }
                    break;
                case PRM_RECEIPTS_DETAILP_C:
                    for (int i = 0; i < this.getListDatas().size(); i++) {
                        PrmReceiptsDetailPC prmReceiptsDetailPC = BeanFactory.getObject(PrmReceiptsDetailPC.class);
                        BeanUtil.bean2Bean((Map) this.getListDatas().get(i), prmReceiptsDetailPC);
                        prmReceiptsDetailPC.setSeqNo(new Integer(i + 1));
                        receiptsDetailPCs.add(prmReceiptsDetailPC);
                    }
                    break;
                case PRM_QSP_C:
                    for (int i = 0; i < this.getListDatas().size(); i++) {
                        PrmQsPC prmQsPC = BeanFactory.getObject(PrmQsPC.class);
                        BeanUtil.bean2Bean((Map) this.getListDatas().get(i), prmQsPC);
                        prmQsPC.setSeqNo(new Integer(i + 1));
                        qsPCs.add(prmQsPC);
                    }
                    break;
                case PRM_BUDGET_DETAIL_C:
                    for (int i = 0; i < this.getListDatas().size(); i++) {
                        PrmBudgetDetailC prmBudgetDetailC = BeanFactory.getObject(PrmBudgetDetailC.class);
                        BeanUtil.bean2Bean((Map) this.getListDatas().get(i), prmBudgetDetailC);
                        prmBudgetDetailC.setSeqNo(new Integer(i + 1));
                        budgetDetailCs.add(prmBudgetDetailC);
                    }
                    break;
                case PRM_BUDGET_OUTSOURCE_C:
                    for (int i = 0; i < this.getListDatas().size(); i++) {
                        PrmBudgetOutsourceC prmBudgetOutsourceC = BeanFactory.getObject(PrmBudgetOutsourceC.class);
                        BeanUtil.bean2Bean((Map) this.getListDatas().get(i), prmBudgetOutsourceC);
                        prmBudgetOutsourceC.setSeqNo(new Integer(i + 1));
                        budgetOutsourceCs.add(prmBudgetOutsourceC);
                    }
                    break;
                case PRM_BUDGET_PRINCIPAL_C:
                    for (int i = 0; i < this.getListDatas().size(); i++) {
                        PrmBudgetPrincipalC prmBudgetPrincipalC = BeanFactory.getObject(PrmBudgetPrincipalC.class);
                        BeanUtil.bean2Bean((Map) this.getListDatas().get(i), prmBudgetPrincipalC);
                        prmBudgetPrincipalC.setSeqNo(new Integer(i + 1));
                        budgetPrincipalCs.add(prmBudgetPrincipalC);
                    }
                    break;
                case PRM_BUDGET_ACCESSORY_C:
                    for (int i = 0; i < this.getListDatas().size(); i++) {
                        PrmBudgetAccessoryC prmBudgetAccessoryC = BeanFactory.getObject(PrmBudgetAccessoryC.class);
                        BeanUtil.bean2Bean((Map) this.getListDatas().get(i), prmBudgetAccessoryC);
                        prmBudgetAccessoryC.setSeqNo(new Integer(i + 1));
                        budgetAccessoryCs.add(prmBudgetAccessoryC);
                    }
                    break;
                case PRM_BUDGET_RUN_C:
                    for (int i = 0; i < this.getListDatas().size(); i++) {
                        PrmBudgetRunC prmBudgetRunC = BeanFactory.getObject(PrmBudgetRunC.class);
                        BeanUtil.bean2Bean((Map) this.getListDatas().get(i), prmBudgetRunC);
                        prmBudgetRunC.setSeqNo(new Integer(i + 1));
                        budgetRunCs.add(prmBudgetRunC);
                    }
                    break;
            }
        } catch (Exception e) {
            throw new BizException(MessageHelper.getMessage("MSG_PRM_IMP_MAP_TO_POJO_ERROR"));
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
        DecimalFormat df = new DecimalFormat("#.####");
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
    public void exportMsg() {
        if (ListUtil.isNotEmpty(errorMsg)) {
            rsMap.put("saveFlag", Boolean.FALSE);
            rsMap.put("errorMsgLog", StringUtil.joinList(errorMsg, ErpCommonAttribute.BREAK_LINE));
            rsMap.put("projectMainCUuid", projectMainCUuid);
        }
    }

    // 数据逻辑验证
    public void LogicVerification() {
        if (StringUtil.isNotEmpty(type) && type.equals("new")) {
            if (projectMainC.getScheduledBeginDate() != null && projectMainC.getScheduledEndDate() != null) {
                if (projectMainC.getScheduledBeginDate().after(projectMainC.getScheduledEndDate())) {
                    errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_PROJECT_MAIN_C_SCHEDULED_DATE_ERROR"));
                }
            }

            BigDecimal costControlMoney = BigDecimal.valueOf(0);
            for (PrmBudgetDetailC pb : budgetDetailCs) {
                if (pb.getBudgetCode().equals("ESTIMATED_COST")) {
                    costControlMoney = pb.getCostControlMoney();
                }
            }
//            if (costControlMoney.compareTo(projectMainC.getCostControlMoney()) != 0) {
//                errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_PROJECT_MAIN_C_COST_CONTROL_MONEY_ERROR"));
//            }

            List<String> lstDupDetailNo = budgetDetailCs.stream().filter(x -> StringUtil.isNotEmpty(x
                    .getSerialNumber())
                    && budgetDetailCs.stream
                    ().anyMatch(y -> y != x && x.getSerialNumber().equals(y.getSerialNumber())))
                    .map(x -> x.getSerialNumber()).distinct().collect(Collectors.toList());
            if (ListUtil.isNotEmpty(lstDupDetailNo)) {
                errorMsg.add("立项预算汇总->序号重复：" + StringUtil.joinList(lstDupDetailNo, ", "));
            }
            for (PrmBudgetDetailC pb : budgetDetailCs) {
                if (ArrayUtil.contains(PrmBudgetCodes.EMPTY_AMOUNT1_ITEMS, pb.getBudgetCode())) {
                    pb.setContractMoney(null);
                    pb.setJointDesignMoney(null);
                }
                if (ArrayUtil.contains(PrmBudgetCodes.EMPTY_AMOUNT3_ITEMS, pb.getBudgetCode())) {
                    pb.setCostControlMoney(null);
                }
                BigDecimal totalCMoney = BigDecimal.valueOf(0);
                if (pb.getBudgetCode().equals("PRINCIPAL")) {
                    for (PrmBudgetPrincipalC pbp : budgetPrincipalCs) {
                        totalCMoney = totalCMoney.add(pbp.getSchemingTotalValue());
                    }
                    if (totalCMoney.compareTo(pb.getCostControlMoney()) != 0) {
                        errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_BUDGET_DETAIL_COST_CONTROL_MONEY_PRINCIPAL_ERROR"));
                    }
                }
                if (pb.getBudgetCode().equals("ACCESSORY")) {
                    for (PrmBudgetAccessoryC pba : budgetAccessoryCs) {
                        totalCMoney = totalCMoney.add(pba.getTotalValue());
                    }
                    if (totalCMoney.compareTo(pb.getCostControlMoney()) != 0) {
                        errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_BUDGET_DETAIL_COST_CONTROL_MONEY_ACCESSORY_ERROR"));
                    }
                }
                if (pb.getBudgetCode().equals("OUTSOURCE")) {
                    for (PrmBudgetOutsourceC pbo : budgetOutsourceCs) {
                        totalCMoney = totalCMoney.add(pbo.getTotalValue());
                    }
                    if (totalCMoney.compareTo(pb.getCostControlMoney()) != 0) {
                        errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_BUDGET_DETAIL_COST_CONTROL_MONEY_OUTSOURCE_ERROR"));
                    }
                }
                if (pb.getBudgetCode().equals("RUN")) {
                    for (PrmBudgetRunC pbr : budgetRunCs) {
                        totalCMoney = totalCMoney.add(pbr.getTotalValue());
                    }
                    if (totalCMoney.compareTo(pb.getCostControlMoney()) != 0) {
                        errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_BUDGET_DETAIL_COST_CONTROL_MONEY_RUN_ERROR"));
                    }
                }
            }

            List<String> lstDupAccessoryNo = budgetAccessoryCs.stream().filter(x -> StringUtil.isNotEmpty(x
                    .getSerialNumber())
                    && budgetAccessoryCs.stream
                    ().anyMatch(y -> y != x && x.getSerialNumber().equals(y.getSerialNumber())))
                    .map(x -> x.getSerialNumber()).distinct().collect(Collectors.toList());
            if (ListUtil.isNotEmpty(lstDupAccessoryNo)) {
                errorMsg.add("辅材->序号重复：" + StringUtil.joinList(lstDupAccessoryNo, ", "));
            }
            int j = 2;
            for (PrmBudgetAccessoryC pba : budgetAccessoryCs) {
                BigDecimal total = pba.getPrice().multiply(pba.getAmount()).setScale(2, BigDecimal.ROUND_HALF_UP);
                if (total.compareTo(pba.getTotalValue()) != 0) {
                    infoMap.put("j", j);
                    errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_BUDGET_ACCESSORY_TOTAL_VALUE_ERROR", infoMap));
                }
                j++;
            }


            List<String> lstDupOutsourceNo = budgetOutsourceCs.stream().filter(x -> StringUtil.isNotEmpty(x
                    .getSerialNumber())
                    && budgetOutsourceCs.stream
                    ().anyMatch(y -> y != x && x.getSerialNumber().equals(y.getSerialNumber())))
                    .map(x -> x.getSerialNumber()).distinct().collect(Collectors.toList());
            if (ListUtil.isNotEmpty(lstDupOutsourceNo)) {
                errorMsg.add("外协->序号重复：" + StringUtil.joinList(lstDupOutsourceNo, ", "));
            }
            int k = 2;
            for (PrmBudgetOutsourceC pbo : budgetOutsourceCs) {
                BigDecimal total = pbo.getPrice().multiply(pbo.getAmount()).setScale(2, BigDecimal.ROUND_HALF_UP);
                if (total.compareTo(pbo.getTotalValue()) != 0) {
                    infoMap.put("k", k);
                    errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_BUDGET_OUTSOURCE_TOTAL_VALUE_ERROR", infoMap));
                }
                k++;
            }

            List<String> lstDupRunNo = budgetRunCs.stream().filter(x -> StringUtil.isNotEmpty(x
                    .getSerialNumber())
                    && budgetRunCs.stream
                    ().anyMatch(y -> y != x && x.getSerialNumber().equals(y.getSerialNumber())))
                    .map(x -> x.getSerialNumber()).distinct().collect(Collectors.toList());
            if (ListUtil.isNotEmpty(lstDupRunNo)) {
                errorMsg.add("运行->序号重复：" + StringUtil.joinList(lstDupRunNo, ", "));
            }
            int l = 2;
            for (PrmBudgetRunC pbr : budgetRunCs) {
                BigDecimal total = pbr.getPrice().multiply(pbr.getAmount()).setScale(2, BigDecimal.ROUND_HALF_UP);
                if (total.compareTo(pbr.getTotalValue()) != 0) {
                    infoMap.put("l", l);
                    errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_BUDGET_RUN_TOTAL_VALUE_ERROR", infoMap));
                }
                l++;
            }
        }

        List<String> lstDupPrincipalNo = budgetPrincipalCs.stream().filter(x -> StringUtil.isNotEmpty(x
                .getSerialNumber())
                && budgetPrincipalCs.stream
                ().anyMatch(y -> y != x && x.getSerialNumber().equals(y.getSerialNumber())))
                .map(x -> x.getSerialNumber()).distinct().collect(Collectors.toList());
        if (ListUtil.isNotEmpty(lstDupPrincipalNo)) {
            errorMsg.add("主材->序号重复：" + StringUtil.joinList(lstDupPrincipalNo, ", "));
        }
        int i = 2;
        for (PrmBudgetPrincipalC pbp : budgetPrincipalCs) {
            BigDecimal total1 = pbp.getSchemingPrice().multiply(pbp.getAmount()).setScale(2, BigDecimal.ROUND_HALF_UP);
            if (total1.compareTo(pbp.getSchemingTotalValue()) != 0) {
                infoMap.put("i", i);
                errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_BUDGET_PRINCIPAL_TOTAL_VALUE_ERROR_1", infoMap));
            }
            BigDecimal total2 = pbp.getContractPrice().multiply(pbp.getContractAmount()).setScale(2, BigDecimal.ROUND_HALF_UP);
            if (total2.compareTo(pbp.getContractTotalValue()) != 0) {
                infoMap.put("i", i);
                errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_BUDGET_PRINCIPAL_TOTAL_VALUE_ERROR_2", infoMap));
            }
            i++;
        }
    }

    // 根据数据字典转换对象属性值
    public void changeDataFromDictionary() {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        Map map = new HashMap<>();

        {
            String prmContractName = projectMainC.getPrmContractIdDesc();
            if (StringUtil.isNotEmpty(prmContractName)) {
                map.clear();
                map.put("contractName", prmContractName);
                List<PrmContract> lstContract = pcm.findByAnyFields(PrmContract.class, map, null);
                if (ListUtil.isNotEmpty(lstContract) && lstContract.size() == 1) {
                    this.prmContractId = lstContract.get(0).getUuid();
                    if ("new".equals(this.type)) {
                        if (Integer.valueOf(1).equals(lstContract.get(0).getIsMajorProject())) {
                            projectMainC.setIsMajorProject(Integer.valueOf(1));
                        } else {
                            projectMainC.setIsMajorProject(Integer.valueOf(0));
                        }
                        projectMainC.setTaxType(lstContract.get(0).getTaxType());
                        if (StringUtil.isNotEmpty(lstContract.get(0).getPrmCodeType())) {
                            contractPrmCodeType = lstContract.get(0).getPrmCodeType();
                            projectMainC.setPrmCodeType(lstContract.get(0).getPrmCodeType());
                        }

                        map.clear();
                        String sql = "select t1.project_name,t1.is_void,t1.detail_type from prm_project_main_c t1 " +
                                "inner join prm_contract_detail_c t2 on t2.prm_project_main_c_id=t1.uuid " +
                                "where t2.prm_contract_id=? and (t1.detail_type='NEW' " +
                                " or (t1.detail_type='*' and (t1.state is null or t1.state!='2'))) " +
                                "union " +
                                "select t1.project_name, t1.is_void, '' as detail_type from prm_project_main t1 " +
                                "inner join prm_contract_detail t2 on t2.prm_project_main_id= t1.uuid " +
                                "where t2.prm_contract_id=? ";
                        List lstParam = new ArrayList<>();
                        lstParam.add(prmContractId);
                        lstParam.add(prmContractId);
                        List<Map<String, Object>> lstProject = pcm.findByNativeSQL(new DAOMeta(sql, lstParam, false));
                        if (ListUtil.isNotEmpty(lstProject)) {
                            for (Map<String, Object> project : lstProject) {
                                String detailType = (String) project.get("detailType");
                                String projectName = (String) project.get("projectName");
                                if (!Integer.valueOf(1).equals(project.get("isVoid"))
                                        && ((PrmProjectMainAttribute.PRM_DETAIL_NEW.equals(detailType)
                                        && !projectName.equals(projectMainC.getProjectName()))
                                        || !PrmProjectMainAttribute.PRM_DETAIL_NEW.equals(detailType))) {
                                    errorMsg.add("合同对应的项目名称不一致！");
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_PROJECT_MAIN_C_CONTRACT_NAME_ERROR"));
                }
            }

/*            String customerId = projectMainC.getCustomerId();
            if (StringUtil.isNotEmpty(customerId)) {
                map.clear();
                map.put(PrmCustomerAttribute.CUSTOMER_NAME, customerId);
                List<PrmCustomer> lstCustomer = pcm.findByAnyFields(PrmCustomer.class, map, null);
                if (ListUtil.isNotEmpty(lstCustomer) && lstCustomer.size() == 1) {
                    String customerUuid = lstCustomer.get(0).getUuid();
                    projectMainC.setCustomerId(customerUuid);
                } else {
                    errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_PROJECT_MAIN_C_CUSTOMER_NAME_ERROR"));
                }
            }*/

            String contractorOffice = projectMainC.getContractorOffice();
            if (StringUtil.isNotEmpty(contractorOffice)) {
                map.clear();
                map.put(ScdpOrgAttribute.ORG_NAME, contractorOffice);
                List<ScdpOrg> lstOrg = pcm.findByAnyFields(ScdpOrg.class, map, null);
                if (ListUtil.isNotEmpty(lstOrg) && lstOrg.size() == 1) {
                    String orgCode = lstOrg.get(0).getOrgCode();
                    projectMainC.setContractorOffice(orgCode);
                } else {
                    errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_PROJECT_MAIN_C_CONTRACTOR_OFFICE_ERROR"));
                }
            }

            String projectManager = projectMainC.getProjectManager();
            if (StringUtil.isNotEmpty(projectManager)) {
                map.clear();
                map.put(ScdpUserAttribute.USER_NAME, projectManager);
                List<ScdpUser> lstUser = pcm.findByAnyFields(ScdpUser.class, map, null);
                if (ListUtil.isNotEmpty(lstUser)) {
                    String userId = lstUser.get(0).getUserId();
                    projectMainC.setProjectManager(userId);
                } else {
                    errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_PROJECT_MAIN_C_PROJECT_MANAGER_ERROR"));
                }
            }

//            String taxType = projectMainC.getTaxType();
//            if (StringUtil.isNotEmpty(taxType)) {
//                List lstScdpCode = ErpCodeHelper.findByCodeTypeAndCodeDesc("CDM_TAX_TYPE", taxType);
//                if (ListUtil.isNotEmpty(lstScdpCode) && lstScdpCode.size() == 1) {
//                    map = (Map) (lstScdpCode.get(0));
//                    projectMainC.setTaxType((String) map.get("sysCode"));
//                } else {
//                    errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_PROJECT_MAIN_C_TAX_TYPE_ERROR"));
//                }
//            }
        }

        if (StringUtil.isNotEmpty(type) && type.equals("new")) {
            {
                String prmCodeTypeDesc = projectMainC.getPrmCodeType();
                if (StringUtil.isEmpty(prmCodeTypeDesc)) {
                    errorMsg.add("立项主信息->代号类型,不能为空");
                } else {
                    if (StringUtil.isEmpty(contractPrmCodeType)) {
                        String prmCodeType = prmCodeTypeMap.get(prmCodeTypeDesc.toString());
                        projectMainC.setPrmCodeType(prmCodeType);
                        if (StringUtil.isEmpty(prmCodeType)) {
                            errorMsg.add("立项主信息->代号类型,不存在");
                        }
                    }
                }
                int i = 2;
                for (PrmMemberDetailPC member : memberDetailPCs) {

                    String memberStaffId = member.getStaffId();
                    if (StringUtil.isNotEmpty(memberStaffId) && MapUtil.isNotEmpty(userNameMap) && userNameMap.containsKey(memberStaffId)) {
                        member.setStaffId(userNameMap.get(memberStaffId));
                    } else {
                        infoMap.put("i", i);
                        errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_MEMBER_DETAIL_STAFF_ID_ERROR", infoMap));
                    }

                    String post = member.getPost();
                    if (StringUtil.isNotEmpty(post) && MapUtil.isNotEmpty(roleNameMap) && roleNameMap.containsKey(post)) {
                        member.setPost(roleNameMap.get(post));
                    } else {
                        infoMap.put("i", i);
                        errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_MEMBER_DETAIL_POST_ERROR", infoMap));
                    }

                    String jobShare = member.getJobShare();
                    if (StringUtil.isNotEmpty(jobShare) && MapUtil.isNotEmpty(jobShareMap) && jobShareMap.containsKey(jobShare)) {
                        member.setJobShare(jobShareMap.get(jobShare));
                    } else {
                        infoMap.put("i", i);
                        errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_MEMBER_DETAIL_JOBSHARE_ERROR", infoMap));
                    }

                    i++;
                }
            }

            {
                int i = 2;
                for (PrmPayDetailPC pay : payDetailPCs) {
                    String projectStage = pay.getProjectStage();
                    if (StringUtil.isNotEmpty(projectStage) && MapUtil.isNotEmpty(projectStageMap) && projectStageMap.containsKey(projectStage)) {
                        pay.setProjectStage(projectStageMap.get(projectStage));
                    } else {
                        infoMap.put("i", i);
                        errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_PAY_DETAIL_PROJECT_STAGE_ERROR", infoMap));
                    }
                    i++;
                }
            }

            {
                int i = 2;
                for (PrmProgressDetailPC progress : progressDetailPCs) {
                    String projectStage = progress.getProjectStage();
                    if (StringUtil.isNotEmpty(projectStage) && MapUtil.isNotEmpty(projectStageMap) && projectStageMap.containsKey(projectStage)) {
                        progress.setProjectStage(projectStageMap.get(projectStage));
                    } else {
                        infoMap.put("i", i);
                        errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_PROGRESS_DETAIL_PROJECT_STAGE_ERROR", infoMap));
                    }
                    i++;
                }
            }

            {
                int i = 2;
                for (PrmReceiptsDetailPC receipts : receiptsDetailPCs) {
                    String projectStage = receipts.getProjectStage();
                    if (StringUtil.isNotEmpty(projectStage) && MapUtil.isNotEmpty(projectStageMap) && projectStageMap.containsKey(projectStage)) {
                        receipts.setProjectStage(projectStageMap.get(projectStage));
                    } else {
                        infoMap.put("i", i);
                        errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_RECEIPTS_DETAIL_PROJECT_STAGE_ERROR", infoMap));
                    }
                    i++;
                }
            }

            {
                int i = 2;
                for (PrmQsPC qs : qsPCs) {

                    String safePrincipal = qs.getSafePrincipal();
                    if (StringUtil.isNotEmpty(safePrincipal) && MapUtil.isNotEmpty(userNameMap) && userNameMap.containsKey(safePrincipal)) {
                        qs.setSafePrincipal(userNameMap.get(safePrincipal));
                    } else {
                        infoMap.put("i", i);
                        errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_QS_SAFE_PRINCIPAL_ERROR", infoMap));
                    }

                    String safeContact = qs.getSafeContact();
                    if (StringUtil.isNotEmpty(safeContact) && MapUtil.isNotEmpty(userNameMap) && userNameMap.containsKey(safeContact)) {
                        qs.setSafeContact(userNameMap.get(safeContact));
                    } else {
                        infoMap.put("i", i);
                        errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_QS_SAFE_CONTRACT_ERROR", infoMap));
                    }

                    String qualityPrincipal = qs.getQualityPrincipal();
                    if (StringUtil.isNotEmpty(qualityPrincipal) && MapUtil.isNotEmpty(userNameMap) && userNameMap.containsKey(qualityPrincipal)) {
                        qs.setQualityPrincipal(userNameMap.get(qualityPrincipal));
                    } else {
                        infoMap.put("i", i);
                        errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_QS_QUALITY_PRINCIPAL_ERROR", infoMap));
                    }

                    String qualityContact = qs.getQualityContact();
                    if (StringUtil.isNotEmpty(qualityContact) && MapUtil.isNotEmpty(userNameMap) && userNameMap.containsKey(qualityContact)) {
                        qs.setQualityContact(userNameMap.get(qualityContact));
                    } else {
                        infoMap.put("i", i);
                        errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_QS_QUALITY_CONTRACT_ERROR", infoMap));
                    }

                    i++;
                }
            }

            {
                int i = 2;
                DAOMeta daoMeta = DAOHelper.getDAO("prmprojectmainc-dao", "financial_subject_descp_null", null);
                List<Map<String, Object>> lstFinancialSubject = pcm.findByNativeSQL(daoMeta);
                for (PrmBudgetDetailC budgetDetail : budgetDetailCs) {
                    String budgetCode = budgetDetail.getBudgetCode();
                    for (Map financialSubjectMap : lstFinancialSubject) {
                        String financialSubject = (String) financialSubjectMap.get(FinancialSubjectAttribute.SUBJECT_NAME);
                        if (financialSubject.equals(budgetCode)) {
                            budgetDetail.setBudgetCode((String) financialSubjectMap.get(FinancialSubjectAttribute.SUBJECT_NO));
                            break;
                        } else {
                            budgetDetail.setBudgetCode(null);
                        }
                    }
                    if (budgetDetail.getBudgetCode() == null) {
                        infoMap.put("i", i);
                        errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_BUDGET_DETAIL_BUDGET_CODE_ERROR", infoMap));
                    }
                    i++;
                }
            }

            {
                int i = 2;
                DAOMeta daoMeta = DAOHelper.getDAO("prmprojectmainc-dao", "financial_subject_descp_run", null);
                List<Map<String, Object>> lstFinancialSubject = pcm.findByNativeSQL(daoMeta);
                for (PrmBudgetRunC budgetRun : budgetRunCs) {
                    String financialSubjectCode = budgetRun.getFinancialSubjectCode();
                    for (Map financialSubjectMap : lstFinancialSubject) {
                        String financialSubject = (String) financialSubjectMap.get(FinancialSubjectAttribute.SUBJECT_NAME);
                        if (financialSubject.equals(financialSubjectCode)) {
                            budgetRun.setFinancialSubjectCode((String) financialSubjectMap.get
                                    (FinancialSubjectAttribute.SUBJECT_NO));
                            break;
                        } else {
                            budgetRun.setFinancialSubjectCode(null);
                        }
                    }
                    if (budgetRun.getFinancialSubjectCode() == null) {
                        infoMap.put("i", i);
                        errorMsg.add(MessageHelper.getMessage("MSG_PRM_IMP_BUDGET_RUN_FINANCIAL_SUBJECT_CODE_ERROR", infoMap));
                    }
                    i++;
                }
            }
        }
    }

    // 保存数据到数据库
    public void saveDataToDataBase() {
        if (StringUtil.isNotEmpty(type) && type.equals("new")) {
            saveDataToDataBaseForNew();
        } else {
            saveDataToDataBaseForModify();
        }
        if (ListUtil.isEmpty(errorMsg)) {
            rsMap.put("saveFlag", Boolean.TRUE);
            rsMap.put("projectMainCUuid", projectMainCUuid);
        }

    }

    // 保存数据到数据库-立项新增
    public void saveDataToDataBaseForNew() {
        boolean success = saveOrUpdateProjectMainCForNew();
        if (success) {
            insertContractDetailC();
            insertAssociatedUnitsC();
            insertMemberDetailPC();
            insertPayDetailPC();
            insertProgressDetailPC();
            insertSquareDetailPC();
            insertReceiptsDetailPC();
            insertQsPC();
            insertBudgetDetailC();
            insertBudgetOutsourceC();
            insertBudgetAccessoryC();
            insertBudgetPrincipalC();
            insertBudgetRunC();
        }
    }

    private void insertContractDetailC() {
        if (StringUtil.isEmpty(this.prmContractId)) {
            return;
        }
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        Map condition = new HashMap();
        condition.put(PrmBudgetDetailCAttribute.PRM_PROJECT_MAIN_C_ID, projectMainCUuid);
        List<PrmContractDetailC> lstContract = pcm.findByAnyFields(PrmContractDetailC.class, condition, null);
        if (ListUtil.isNotEmpty(lstContract)) {
            PrmContractDetailC existedContract = lstContract.stream().filter(x -> this.prmContractId.equals(x
                    .getPrmContractId())).findAny().orElse(null);
            if (existedContract == null) {
                lstContract.forEach(x -> pcm.delete(x));
            } else {
                pcm.delete(existedContract);
            }
        }
        PrmContract contract = pcm.findByPK(PrmContract.class, this.prmContractId);
        if (contract != null) {
            PrmContractDetailC contractDetail = new PrmContractDetailC();
            contractDetail.setPrmProjectMainCId(this.projectMainCUuid);
            contractDetail.setPrmContractId(this.prmContractId);
            contractDetail.setCustomerId(contract.getCustomerId());
            contractDetail.setContractNowMoney(contract.getContractNowMoney());
            pcm.insert(contractDetail);
        }
    }

    // 保存数据到数据库-立项变更
    public void saveDataToDataBaseForModify() {
        saveOrUpdateProjectMainCForModify();
        insertNotExistBudgetPrincipalC();
    }

    // 插入立项主信息-立项新增
    public boolean saveOrUpdateProjectMainCForNew() {
        boolean success = true;
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        String pName = projectMainC.getProjectName();
        if (StringUtil.isNotEmpty(pName)) {
            List lstParam = new ArrayList();
            lstParam.add(pName);
            lstParam.add(PrmProjectMainAttribute.PRM_DETAIL_NEW);
            DAOMeta daoMeta = DAOHelper.getDAO("prmprojectmainc-dao", "common_query3", lstParam);
            daoMeta.setNeedFilter(false);
            List<Map<String, Object>> lstProjectMainC = pcm.findByNativeSQL(daoMeta);
            if (lstProjectMainC.size() != 0) {
                Map prmProjectMainCMap = lstProjectMainC.get(0);
                String state = (String) prmProjectMainCMap.get("state");
                if (StringUtil.isNotEmpty(state) && !(state.equals(BillStateAttribute.CMD_BILL_STATE_NEW) || state.equals(BillStateAttribute.CMD_BILL_STATE_REJECTED))) {
                    List lstScdpCode = ErpCodeHelper.findByCodeTypeAndSysCode("CDM_BILL_STATE", state);
                    String stateInfo = "";
                    if (ListUtil.isNotEmpty(lstScdpCode) && lstScdpCode.size() == 1) {
                        Map map = (Map) (lstScdpCode.get(0));
                        stateInfo = (String) map.get("codeDesc");
                    }
                    infoMap.put("stateInfo", stateInfo);
                    throw new BizException(MessageHelper.getMessage("MSG_PRM_IMP_PROJECT_MAIN_C_STATE_NOT_NEW_ERROR", infoMap));
                }
                PrmProjectMainC prmProjectMainCToUpdate = pcm.findByPK(PrmProjectMainC.class, (String) prmProjectMainCMap.get("uuid"));
                projectMainCUuid = prmProjectMainCToUpdate.getUuid();
//                String tblVersion = prmProjectMainCToUpdate.getTblVersion();
//                String updateBy = prmProjectMainCToUpdate.getUpdateBy();
//                Date updateTime = prmProjectMainCToUpdate.getUpdateTime();
//                BeanUtil.bean2Bean(projectMainC, prmProjectMainCToUpdate);
//                prmProjectMainCToUpdate.setUuid(projectMainCUuid);
//                prmProjectMainCToUpdate.setTblVersion(tblVersion);
//                prmProjectMainCToUpdate.setUpdateBy(updateBy);
//                prmProjectMainCToUpdate.setUpdateTime(updateTime);
//                Integer isPreProject = prmProjectMainCToUpdate.getIsPreProject();
//                if ((isPreProject != null) && (isPreProject.equals(new Integer(1)))) {
//                    if (StringUtil.isNotEmpty(projectMainC.getPrmContractId())) {
//                        prmProjectMainCToUpdate.setIsMajorProject(projectMainC.getIsMajorProject());
//                    }
//                }
                if (StringUtil.isNotEmpty(projectMainC.getProjectManager())) {
                    prmProjectMainCToUpdate.setProjectManager(projectMainC.getProjectManager());
                }
                if (StringUtil.isNotEmpty(projectMainC.getScheduledBeginDate())) {
                    prmProjectMainCToUpdate.setScheduledBeginDate(projectMainC.getScheduledBeginDate());
                }
                if (StringUtil.isNotEmpty(projectMainC.getScheduledEndDate())) {
                    prmProjectMainCToUpdate.setScheduledEndDate(projectMainC.getScheduledEndDate());
                }
                if (StringUtil.isNotEmpty(projectMainC.getTaxType())) {
                    prmProjectMainCToUpdate.setTaxType(projectMainC.getTaxType());
                }
                if (StringUtil.isNotEmpty(projectMainC.getPrmCodeType())) {
                    prmProjectMainCToUpdate.setPrmCodeType(projectMainC.getPrmCodeType());
                }
                if (StringUtil.isNotEmpty(projectMainC.getContractDuration())) {
                    prmProjectMainCToUpdate.setContractDuration(projectMainC.getContractDuration());
                }
                if (StringUtil.isNotEmpty(projectMainC.getFundsExplanation())) {
                    prmProjectMainCToUpdate.setFundsExplanation(projectMainC.getFundsExplanation());
                }
                if (StringUtil.isNotEmpty(projectMainC.getRemark())) {
                    prmProjectMainCToUpdate.setRemark(projectMainC.getRemark());
                }
                if (validateHeader(prmProjectMainCToUpdate)) {
                    pcm.update(prmProjectMainCToUpdate);
                    isInsert = false;
                } else {
                    success = false;
                }
            } else {
                if (StringUtil.isEmpty(projectMainC.getContractorOffice())) {
                    projectMainC.setContractorOffice(UserHelper.getOrgCode());
                }
                if (validateHeader(projectMainC)) {
//                    projectMainC.setPrmContractId(null);// 这个字段现在不用了，现在暂时删掉
                    PrmProjectMainC prmProjectMainC = new PrmProjectMainC();
                    BeanUtil.bean2Bean(projectMainC, prmProjectMainC);
//                    BeanUtil.setProperty();
//                    DtoHelper.
                    pcm.insert(prmProjectMainC);
                    projectMainCUuid = prmProjectMainC.getUuid();
                    isInsert = true;
                } else {
                    success = false;
                }
            }
        }
        return success;
    }

    private boolean validateHeader(PrmProjectMainC projectMainC) {
        boolean valid = true;
        String taxType = projectMainC.getTaxType();
        if (StringUtil.isEmpty(taxType)) {
            taxType = TaxType.PP;
        }
        List<Map<String, Object>> lstCodeMap = ErpCodeHelper.findByCodeType("PRM_CODE_TYPE_" + taxType);
        if (ListUtil.isEmpty(lstCodeMap) || lstCodeMap.stream().noneMatch(
                x -> projectMainC.getPrmCodeType().equals(x.get(ScdpCodeAttribute.SYS_CODE)))) {
            valid = false;
            errorMsg.add("立项主信息->税款类别与代号类型不匹配！");
        }
        return valid;
    }


    // 插入立项主信息-立项变更
    public void saveOrUpdateProjectMainCForModify() {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        String pName = projectMainC.getProjectName();
        if (StringUtil.isNotEmpty(pName)) {
            List lstParam = new ArrayList<>();
            lstParam.add(pName);
            DAOMeta daoMeta = DAOHelper.getDAO("prmprojectmainc-dao", "common_query4", lstParam);
            daoMeta.setNeedFilter(false);
            List<Map<String, Object>> lstProjectMainC = pcm.findByNativeSQL(daoMeta);
            if (ListUtil.isNotEmpty(lstProjectMainC)) {
                for (Iterator<Map<String, Object>> iterator = lstProjectMainC.iterator(); iterator.hasNext(); ) {
                    Map projectMainCMap = iterator.next();
                    String state = (String) projectMainCMap.get("state");
                    if (StringUtil.isNotEmpty(state) && !state.equals(BillStateAttribute.CMD_BILL_STATE_NEW)) {
                        iterator.remove();
                    }
                }
                if (ListUtil.isEmpty(lstProjectMainC)) {
                    throw new BizException(MessageHelper.getMessage("MSG_PRM_IMP_PROJECT_MAIN_C_STATE_ALL_NOT_NEW_ERROR"));
                }
                Map mapProjectMainC = lstProjectMainC.stream().sorted((x, y) -> {
                            long defaultTime = 0l;
                            Date dateX = (Date) (x.get(CommonAttribute.CREATE_TIME) != null ? x.get(CommonAttribute
                                    .CREATE_TIME) : new Date(defaultTime));
                            Date dateY = (Date) (y.get(CommonAttribute.CREATE_TIME) != null ? y.get(CommonAttribute
                                    .CREATE_TIME) : new Date(defaultTime));
                            return dateY.compareTo(dateX);
                        }
                ).findFirst().get();
                projectMainCUuid = (String) mapProjectMainC.get("uuid");
                projectMainC.setUuid(projectMainCUuid);
                projectMainC.setPrmProjectMainId((String) mapProjectMainC.get(PrmProjectMainCAttribute.PRM_PROJECT_MAIN_ID));


            } else {
                throw new BizException(MessageHelper.getMessage("MSG_PRM_IMP_PROJECT_MAIN_C_NOT_EXIST_ERROR"));
            }
        }
    }

    // 插入关联单位信息
    public void insertAssociatedUnitsC() {
        if (ListUtil.isNotEmpty(associatedUnitsCs)) {
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            if (!isInsert) {
                Map mapCondition = new HashMap();
                mapCondition.put(PrmAssociatedUnitsCAttribute.PRM_PROJECT_MAIN_C_ID, projectMainCUuid);
                List<PrmAssociatedUnitsC> lstPrmAssociatedUnitsC = pcm.findByAnyFields(PrmAssociatedUnitsC.class, mapCondition, null);
                if (ListUtil.isNotEmpty(lstPrmAssociatedUnitsC)) {
                    pcm.batchDelete(lstPrmAssociatedUnitsC);
                }
            }
            for (PrmAssociatedUnitsC prmAssociatedUnitsC : associatedUnitsCs) {
                if (StringUtil.isEmpty(prmAssociatedUnitsC.getPrmProjectMainCId())) {
                    prmAssociatedUnitsC.setPrmProjectMainCId(projectMainCUuid);
                }
            }
            pcm.batchInsert(associatedUnitsCs);
        }
    }

    // 插入项目成员信息
    public void insertMemberDetailPC() {
        if (ListUtil.isNotEmpty(memberDetailPCs)) {
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            if (!isInsert) {
                Map mapCondition = new HashMap();
                mapCondition.put(PrmMemberDetailPCAttribute.PRM_PROJECT_MAIN_C_ID, projectMainCUuid);
                List<PrmMemberDetailPC> lstPrmMemberDetailPC = pcm.findByAnyFields(PrmMemberDetailPC.class, mapCondition, null);
                if (ListUtil.isNotEmpty(lstPrmMemberDetailPC)) {
                    pcm.batchDelete(lstPrmMemberDetailPC);
                }
            }
            for (PrmMemberDetailPC prmMemberDetailPC : memberDetailPCs) {
                if (StringUtil.isEmpty(prmMemberDetailPC.getPrmProjectMainCId())) {
                    prmMemberDetailPC.setPrmProjectMainCId(projectMainCUuid);
                }
            }
            pcm.batchInsert(memberDetailPCs);
        }
    }

    // 插入开支计划信息
    public void insertPayDetailPC() {
        if (ListUtil.isNotEmpty(payDetailPCs)) {
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            if (!isInsert) {
                Map mapCondition = new HashMap();
                mapCondition.put(PrmPayDetailPCAttribute.PRM_PROJECT_MAIN_C_ID, projectMainCUuid);
                List<PrmPayDetailPC> lstPrmPayDetailPC = pcm.findByAnyFields(PrmPayDetailPC.class, mapCondition, null);
                if (ListUtil.isNotEmpty(lstPrmPayDetailPC)) {
                    pcm.batchDelete(lstPrmPayDetailPC);
                }
            }
            for (PrmPayDetailPC prmPayDetailPC : payDetailPCs) {
                if (StringUtil.isEmpty(prmPayDetailPC.getPrmProjectMainCId())) {
                    prmPayDetailPC.setPrmProjectMainCId(projectMainCUuid);
                }
            }
            pcm.batchInsert(payDetailPCs);
        }
    }

    // 插入进度计划信息
    public void insertProgressDetailPC() {
        if (ListUtil.isNotEmpty(progressDetailPCs)) {
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            if (!isInsert) {
                Map mapCondition = new HashMap();
                mapCondition.put(PrmProgressDetailPCAttribute.PRM_PROJECT_MAIN_C_ID, projectMainCUuid);
                List<PrmProgressDetailPC> lstPrmProgressDetailPC = pcm.findByAnyFields(PrmProgressDetailPC.class, mapCondition, null);
                if (ListUtil.isNotEmpty(lstPrmProgressDetailPC)) {
                    pcm.batchDelete(lstPrmProgressDetailPC);
                }
            }
            for (PrmProgressDetailPC prmProgressDetailPC : progressDetailPCs) {
                if (StringUtil.isEmpty(prmProgressDetailPC.getPrmProjectMainCId())) {
                    prmProgressDetailPC.setPrmProjectMainCId(projectMainCUuid);
                }
            }
            pcm.batchInsert(progressDetailPCs);
        }
    }

    // 插入结算计划信息
    public void insertSquareDetailPC() {
        if (ListUtil.isNotEmpty(squareDetailPCs)) {
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            if (!isInsert) {
                Map mapCondition = new HashMap();
                mapCondition.put(PrmSquareDetailPCAttribute.PRM_PROJECT_MAIN_C_ID, projectMainCUuid);
                List<PrmSquareDetailPC> lstPrmSquareDetailPC = pcm.findByAnyFields(PrmSquareDetailPC.class, mapCondition, null);
                if (ListUtil.isNotEmpty(lstPrmSquareDetailPC)) {
                    pcm.batchDelete(lstPrmSquareDetailPC);
                }
            }
            for (PrmSquareDetailPC prmSquareDetailPC : squareDetailPCs) {
                if (StringUtil.isEmpty(prmSquareDetailPC.getPrmProjectMainCId())) {
                    prmSquareDetailPC.setPrmProjectMainCId(projectMainCUuid);
                }
            }
            pcm.batchInsert(squareDetailPCs);
        }
    }

    // 插入收款计划信息
    public void insertReceiptsDetailPC() {
        if (ListUtil.isNotEmpty(receiptsDetailPCs)) {
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            if (!isInsert) {
                Map mapCondition = new HashMap();
                mapCondition.put(PrmReceiptsDetailPCAttribute.PRM_PROJECT_MAIN_C_ID, projectMainCUuid);
                List<PrmReceiptsDetailPC> lstPrmReceiptsDetailPC = pcm.findByAnyFields(PrmReceiptsDetailPC.class, mapCondition, null);
                if (ListUtil.isNotEmpty(lstPrmReceiptsDetailPC)) {
                    pcm.batchDelete(lstPrmReceiptsDetailPC);
                }
            }
            for (PrmReceiptsDetailPC prmReceiptsDetailPC : receiptsDetailPCs) {
                if (StringUtil.isEmpty(prmReceiptsDetailPC.getPrmProjectMainCId())) {
                    prmReceiptsDetailPC.setPrmProjectMainCId(projectMainCUuid);
                }
            }
            pcm.batchInsert(receiptsDetailPCs);
        }
    }

    // 插入质量安全计划信息
    public void insertQsPC() {
        if (ListUtil.isNotEmpty(qsPCs)) {
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            if (!isInsert) {
                Map mapCondition = new HashMap();
                mapCondition.put(PrmQsPCAttribute.PRM_PROJECT_MAIN_C_ID, projectMainCUuid);
                List<PrmQsPC> lstPrmQsPC = pcm.findByAnyFields(PrmQsPC.class, mapCondition, null);
                if (ListUtil.isNotEmpty(lstPrmQsPC)) {
                    pcm.batchDelete(lstPrmQsPC);
                }
            }
            for (PrmQsPC prmQsPC : qsPCs) {
                if (StringUtil.isEmpty(prmQsPC.getPrmProjectMainCId())) {
                    prmQsPC.setPrmProjectMainCId(projectMainCUuid);
                }
            }
            pcm.batchInsert(qsPCs);
        }
    }

    // 插入立项预算汇总信息
    public void insertBudgetDetailC() {
        if (ListUtil.isNotEmpty(budgetDetailCs)) {
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            if (!isInsert) {
                Map mapCondition = new HashMap();
                mapCondition.put(PrmBudgetDetailCAttribute.PRM_PROJECT_MAIN_C_ID, projectMainCUuid);
                List<PrmBudgetDetailC> lstPrmBudgetDetailC = pcm.findByAnyFields(PrmBudgetDetailC.class, mapCondition, null);
                if (ListUtil.isNotEmpty(lstPrmBudgetDetailC)) {
                    pcm.batchDelete(lstPrmBudgetDetailC);
                }
            }
            for (PrmBudgetDetailC prmBudgetDetailC : budgetDetailCs) {
                if (StringUtil.isEmpty(prmBudgetDetailC.getPrmProjectMainCId())) {
                    prmBudgetDetailC.setPrmProjectMainCId(projectMainCUuid);
                }
            }
            pcm.batchInsert(budgetDetailCs);
        }
    }

    // 插入外协信息
    public void insertBudgetOutsourceC() {
        if (ListUtil.isNotEmpty(budgetOutsourceCs)) {
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            if (!isInsert) {
                Map mapCondition = new HashMap();
                mapCondition.put(PrmBudgetOutsourceCAttribute.PRM_PROJECT_MAIN_C_ID, projectMainCUuid);
                List<PrmBudgetOutsourceC> lstPrmBudgetOutsourceC = pcm.findByAnyFields(PrmBudgetOutsourceC.class, mapCondition, null);
                if (ListUtil.isNotEmpty(lstPrmBudgetOutsourceC)) {
                    pcm.batchDelete(lstPrmBudgetOutsourceC);
                }
            }
            for (PrmBudgetOutsourceC prmBudgetOutsourceC : budgetOutsourceCs) {
                if (StringUtil.isEmpty(prmBudgetOutsourceC.getPrmProjectMainCId())) {
                    prmBudgetOutsourceC.setPrmProjectMainCId(projectMainCUuid);
                }
            }
            pcm.batchInsert(budgetOutsourceCs);
        }
    }

    // 插入辅材信息
    public void insertBudgetAccessoryC() {
        if (ListUtil.isNotEmpty(budgetAccessoryCs)) {
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            if (!isInsert) {
                Map mapCondition = new HashMap();
                mapCondition.put(PrmBudgetAccessoryCAttribute.PRM_PROJECT_MAIN_C_ID, projectMainCUuid);
                List<PrmBudgetAccessoryC> lstPrmBudgetAccessoryC = pcm.findByAnyFields(PrmBudgetAccessoryC.class, mapCondition, null);
                if (ListUtil.isNotEmpty(lstPrmBudgetAccessoryC)) {
                    pcm.batchDelete(lstPrmBudgetAccessoryC);
                }
            }
            for (PrmBudgetAccessoryC prmBudgetAccessoryC : budgetAccessoryCs) {
                if (StringUtil.isEmpty(prmBudgetAccessoryC.getPrmProjectMainCId())) {
                    prmBudgetAccessoryC.setPrmProjectMainCId(projectMainCUuid);
                }
            }
            pcm.batchInsert(budgetAccessoryCs);
        }
    }

    // 插入主材信息
    public void insertBudgetPrincipalC() {
        if (ListUtil.isNotEmpty(budgetPrincipalCs)) {
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            if (!isInsert) {
                Map mapCondition = new HashMap();
                mapCondition.put(PrmBudgetPrincipalCAttribute.PRM_PROJECT_MAIN_C_ID, projectMainCUuid);
                List<PrmBudgetPrincipalC> lstPrmBudgetPrincipalC = pcm.findByAnyFields(PrmBudgetPrincipalC.class, mapCondition, null);
                if (ListUtil.isNotEmpty(lstPrmBudgetPrincipalC)) {
                    pcm.batchDelete(lstPrmBudgetPrincipalC);
                }
            }
            for (PrmBudgetPrincipalC prmBudgetPrincipalC : budgetPrincipalCs) {
                if (StringUtil.isEmpty(prmBudgetPrincipalC.getPrmProjectMainCId())) {
                    prmBudgetPrincipalC.setPrmProjectMainCId(projectMainCUuid);
                }
            }
            pcm.batchInsert(budgetPrincipalCs);
        }
    }

    // 插入运行信息
    public void insertBudgetRunC() {
        if (ListUtil.isNotEmpty(budgetRunCs)) {
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            if (!isInsert) {
                Map mapCondition = new HashMap();
                mapCondition.put(PrmBudgetRunCAttribute.PRM_PROJECT_MAIN_C_ID, projectMainCUuid);
                List<PrmBudgetRunC> lstPrmBudgetRunC = pcm.findByAnyFields(PrmBudgetRunC.class, mapCondition, null);
                if (ListUtil.isNotEmpty(lstPrmBudgetRunC)) {
                    pcm.batchDelete(lstPrmBudgetRunC);
                }
            }
            for (PrmBudgetRunC prmBudgetRunC : budgetRunCs) {
                if (StringUtil.isEmpty(prmBudgetRunC.getPrmProjectMainCId())) {
                    prmBudgetRunC.setPrmProjectMainCId(projectMainCUuid);
                }
            }
            pcm.batchInsert(budgetRunCs);
        }
    }

    // 立项变更-插入表中序号不存在的数据
    public void insertNotExistBudgetPrincipalC() {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        List<PrmBudgetPrincipalC> lstToRemove = new ArrayList<PrmBudgetPrincipalC>();
        Map mapCondition = new HashMap();
        mapCondition.put(PrmBudgetPrincipalCAttribute.PRM_PROJECT_MAIN_C_ID, projectMainCUuid);
        List<PrmBudgetPrincipalC> lstBudgetPrincipalC = pcm.findByAnyFields(PrmBudgetPrincipalC.class, mapCondition, null);
        List<String> lstExistedSerialNum = lstBudgetPrincipalC.stream().filter(x -> budgetPrincipalCs.stream()
                .anyMatch(y -> ObjectUtil.beSame(x.getSerialNumber(), y.getSerialNumber())))
                .map(x -> x.getSerialNumber()).collect(Collectors.toList());
        if (ListUtil.isNotEmpty(lstExistedSerialNum)) {
            errorMsg.add("主材->序号在项目中已存在：" + StringUtil.joinList(lstExistedSerialNum, ", "));
            return;
        }
        if (budgetPrincipalCs.size() != 0) {
            List<String> lstNewSerialNo = budgetPrincipalCs.stream().map(x -> x.getSerialNumber()).collect(Collectors
                    .toList());
            PurchaseplanManager purchaseplanManager = BeanFactory.getBean("purchaseplan-manager");
            List<String> lstInvalidSerialNo = purchaseplanManager.queryExistedSerialNo(projectMainC.getPrmProjectMainId()
                    , lstNewSerialNo, PrmBudgetCodes.PRINCIPAL);
            if (ListUtil.isNotEmpty(lstInvalidSerialNo)) {
                errorMsg.add("主材->序号已在采购计划中存在：" + StringUtil.joinList(lstInvalidSerialNo, ", "));
                return;
            }
            for (PrmBudgetPrincipalC prmBudgetPrincipalC : budgetPrincipalCs) {
                if (StringUtil.isEmpty(prmBudgetPrincipalC.getPrmProjectMainCId())) {
                    prmBudgetPrincipalC.setPrmProjectMainCId(projectMainCUuid);
                }
            }
            pcm.batchInsert(budgetPrincipalCs);
        }
    }

    // 从数据库查出所有需要转换的字典值
    private void getDictionaryDataFromDatabase() {
        Set userNames = new HashSet<>();
        Set roleNames = new HashSet<>();
        for (PrmMemberDetailPC prmMemberDetailPC : memberDetailPCs) {
            if (StringUtil.isNotEmpty(prmMemberDetailPC.getStaffId())) {
                userNames.add(prmMemberDetailPC.getStaffId());
            }
            if (StringUtil.isNotEmpty(prmMemberDetailPC.getPost())) {
                roleNames.add(prmMemberDetailPC.getPost());
            }
        }
        for (PrmQsPC prmQsPC : qsPCs) {
            if (StringUtil.isNotEmpty(prmQsPC.getSafePrincipal())) {
                userNames.add(prmQsPC.getSafePrincipal());
            }
            if (StringUtil.isNotEmpty(prmQsPC.getSafeContact())) {
                userNames.add(prmQsPC.getSafeContact());
            }
            if (StringUtil.isNotEmpty(prmQsPC.getQualityPrincipal())) {
                userNames.add(prmQsPC.getQualityPrincipal());
            }
            if (StringUtil.isNotEmpty(prmQsPC.getQualityContact())) {
                userNames.add(prmQsPC.getQualityContact());
            }
        }
        List<QueryCondition> lstCondition = new ArrayList<QueryCondition>();
        QueryCondition queryCondition = new QueryCondition();
        if (userNames.size() > 0) {
            List lstUserNames = new ArrayList<>(userNames);
            queryCondition.setField(ScdpUserAttribute.USER_NAME);
            queryCondition.setOperator("in");
            queryCondition.setValueList(lstUserNames);
            lstCondition.add(queryCondition);
            List<ScdpUser> scdpUsers = PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, lstCondition, null);
            userNameMap = new HashMap<String, String>();
            for (ScdpUser scdpUser : scdpUsers) {
                userNameMap.put(scdpUser.getUserName(), scdpUser.getUserId());
            }
        }

        if (roleNames.size() > 0) {
            List lstRoleNames = new ArrayList<>(roleNames);
            queryCondition.setField(ScdpRoleAttribute.ROLE_NAME);
            queryCondition.setOperator("in");
            queryCondition.setValueList(lstRoleNames);
            lstCondition.clear();
            lstCondition.add(queryCondition);
            List<ScdpRole> scdpRoles = PersistenceFactory.getInstance().findByAnyFields(ScdpRole.class, lstCondition, null);
            roleNameMap = new HashMap<String, String>();
            for (ScdpRole scdpRole : scdpRoles) {
                if ("Y".equals(ErpExpandFieldHelper.getExpandFieldValue(ExpandFieldType.ROLE, scdpRole.getUuid(),
                        ExpandFieldName.IS_PROJECT_ROLE))) {
                    roleNameMap.put(scdpRole.getRoleName(), scdpRole.getUuid());
                }
            }
        }

        List lstScdpCodeOfProjectStage = ErpCodeHelper.findByCodeType("CDM_PROJECT_STAGE");
        projectStageMap = new HashMap<String, String>();
        for (Iterator iter = lstScdpCodeOfProjectStage.iterator(); iter.hasNext(); ) {
            Map projectStage = (Map) iter.next();
            String codeDesc = (String) projectStage.get("codeDesc");
            String sysCode = (String) projectStage.get("sysCode");
            projectStageMap.put(codeDesc, sysCode);
        }

        List lstScdpCodeOfJobShare = ErpCodeHelper.findByCodeType("JOB_SHARE");
        jobShareMap = new HashMap<String, String>();
        for (Iterator iter = lstScdpCodeOfJobShare.iterator(); iter.hasNext(); ) {
            Map mapCode = (Map) iter.next();
            String codeDesc = (String) mapCode.get("codeDesc");
            String sysCode = (String) mapCode.get("sysCode");
            jobShareMap.put(codeDesc, sysCode);
        }

        List lstPrmCodeType = ErpCodeHelper.findByCodeType("PRM_CODE_TYPE_PP");
        prmCodeTypeMap = new HashMap<String, String>();
        for (Iterator iter = lstPrmCodeType.iterator(); iter.hasNext(); ) {
            Map mapCode = (Map) iter.next();
            String codeDesc = (String) mapCode.get("codeDesc");
            String sysCode = (String) mapCode.get("sysCode");
            prmCodeTypeMap.put(codeDesc, sysCode);
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
