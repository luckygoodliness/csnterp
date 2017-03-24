package com.csnt.scdp.bizmodules.modules.prm.projectmain.action;

import com.csnt.scdp.bizmodules.modules.prm.projectmain.dto.PrmBudgetPrincipalDto;
import com.csnt.scdp.bizmodules.modules.prm.projectmain.dto.PrmProjectMainDto;
import com.csnt.scdp.bizmodules.modules.prm.projectmain.services.intf.ProjectmainManager;
import com.csnt.scdp.framework.commonaction.crud.CommonExportXlsAction;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.helper.I18NHelper;
import com.csnt.scdp.framework.helper.SysconfigHelper;
import com.csnt.scdp.framework.helper.XlsHelper;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.DateUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.context.annotation.Scope;
import org.springframework.expression.spel.ast.FormatHelper;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import com.csnt.scdp.bizmodules.modules.prm.projectmain.action.LoadAction;

/**
 * Created by lijx on 2016/8/26.
 */
@Scope("singleton")
@Controller("projectmain-exportall")
@Transactional
public class ExportAllAction extends XlsHelper implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ExportAllAction.class);

    @Resource(name = "projectmain-manager")
    private ProjectmainManager projectmainManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        HttpServletResponse response = (HttpServletResponse)inMap.get("http_response");
        String fileName = (String)inMap.get("fileName");
        response.setContentType("multipart/form-data");
        boolean hideSeq = inMap.containsKey("hideSeq")?((Boolean)inMap.get("hideSeq")).booleanValue():false;

        try {
            response.setHeader("Content-Disposition", "attachment;fileName=" + new String(fileName.getBytes("gb2312"), "iso8859-1"));
        } catch (UnsupportedEncodingException var5) {
            throw new SysException(var5);
        }

        OutputStream os;
        try {
            os = response.getOutputStream();
        } catch (IOException var7) {
            tracer.error(var7);
            throw new SysException(var7);
        }
        Map out = new LoadAction().perform(inMap);
        Map dto =  (Map)out.get("prmProjectMainDto");
        SXSSFWorkbook workbook = new SXSSFWorkbook(5000);

        super.setDateStyleStr("yyyy-MM-dd");
        super.setTimeStampStyleStr("yyyy-MM-dd");

        //sheet-立项主信息
        Sheet sheet = workbook.createSheet("立项主信息");
        initCellStyle(workbook);
        setHeaderLine(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "name"), hideSeq);
        setColumnWidth(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "width"));

        //sheet-关联单位
        sheet = workbook.createSheet("关联单位");
        super.initCellStyle(workbook);
        super.setHeaderLine(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "name"), hideSeq);
        super.setColumnWidth(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "width"));
        super.setDataLine(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "field"), (List) dto.get("prmAssociatedUnitsDto"), hideSeq, 0);

        //sheet-项目成员表
        sheet = workbook.createSheet("项目成员表");
        super.initCellStyle(workbook);
        super.setHeaderLine(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "name"), hideSeq);
        super.setColumnWidth(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "width"));
        super.setDataLine(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "field"), (List) dto.get("prmMemberDetailPDto"), hideSeq, 0);

        //sheet-进度计划
        sheet = workbook.createSheet("进度计划");
        super.initCellStyle(workbook);
        super.setHeaderLine(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "name"), hideSeq);
        super.setColumnWidth(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "width"));
        super.setDataLine(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "field"), (List) dto.get("prmProgressDetailPDto"), hideSeq, 0);

        //sheet-开支计划
        sheet = workbook.createSheet("开支计划");
        super.initCellStyle(workbook);
        super.setHeaderLine(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "name"), hideSeq);
        super.setColumnWidth(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "width"));
        super.setDataLine(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "field"), (List) dto.get("prmPayDetailPDto"), hideSeq, 0);

        //sheet-结算计划
        sheet = workbook.createSheet("结算计划");
        super.initCellStyle(workbook);
        super.setHeaderLine(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "name"), hideSeq);
        super.setColumnWidth(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "width"));
        super.setDataLine(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "field"), (List) dto.get("prmSquareDetailPDto"), hideSeq, 0);

        //sheet-收款计划
        sheet = workbook.createSheet("收款计划");
        super.initCellStyle(workbook);
        super.setHeaderLine(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "name"), hideSeq);
        super.setColumnWidth(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "width"));
        super.setDataLine(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "field"), (List) dto.get("prmReceiptsDetailPDto"), hideSeq, 0);

        //sheet-质量安全计划
        sheet = workbook.createSheet("质量安全计划");
        super.initCellStyle(workbook);
        super.setHeaderLine(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "name"), hideSeq);
        super.setColumnWidth(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "width"));
        super.setDataLine(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "field"), (List) dto.get("prmQsPDto"), hideSeq, 0);

        //sheet-立项预算汇总
        sheet = workbook.createSheet("立项预算汇总");
        super.initCellStyle(workbook);
        super.setHeaderLine(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "name"), hideSeq);
        super.setColumnWidth(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "width"));
        super.setDataLine(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "field"), (List) dto.get("prmBudgetDetailDto"), hideSeq, 0);

        //sheet-外协
        sheet = workbook.createSheet("外协");
        super.initCellStyle(workbook);
        super.setHeaderLine(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "name"), true);
        super.setColumnWidth(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "width"));
        List prmBudgetOutsourceList = (List) dto.get("prmBudgetOutsourceDto");
        if(ListUtil.isNotEmpty(prmBudgetOutsourceList)) {
            prmBudgetOutsourceList.sort((Object o1, Object o2) -> {
                Map m1 = (Map) o1;
                Map m2 = (Map) o2;
                String seqNo1 = this.serialNoSortFn(m1.get("serialNumber").toString());
                String seqNo2 = this.serialNoSortFn(m2.get("serialNumber").toString());
                return seqNo1.compareTo(seqNo2);
            });
        }

        super.setDataLine(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "field"), prmBudgetOutsourceList, true, 0);

        //sheet-主材
        sheet = workbook.createSheet("主材");
        super.initCellStyle(workbook);
        super.setHeaderLine(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "name"), true);
        super.setColumnWidth(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "width"));
        List prmBudgetPrincipalList = (List) dto.get("prmBudgetPrincipalDto");
        if(ListUtil.isNotEmpty(prmBudgetPrincipalList)) {
            prmBudgetPrincipalList.sort((Object o1, Object o2)-> {
                Map m1 = (Map)o1;
                Map m2 = (Map)o2;
                String seqNo1 = this.serialNoSortFn(m1.get("serialNumber").toString());
                String seqNo2 = this.serialNoSortFn(m2.get("serialNumber").toString());
                return seqNo1.compareTo(seqNo2);
            });
        }

        super.setDataLine(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "field"), prmBudgetPrincipalList, true, 0);

        //sheet-辅材
        sheet = workbook.createSheet("辅材");
        super.initCellStyle(workbook);
        super.setHeaderLine(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "name"), true);
        super.setColumnWidth(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "width"));
        List prmBudgetAccessoryList = (List) dto.get("prmBudgetAccessoryDto");
        if(ListUtil.isNotEmpty(prmBudgetAccessoryList)) {
            prmBudgetAccessoryList.sort((Object o1, Object o2) -> {
                Map m1 = (Map) o1;
                Map m2 = (Map) o2;
                String seqNo1 = this.serialNoSortFn(m1.get("serialNumber").toString());
                String seqNo2 = this.serialNoSortFn(m2.get("serialNumber").toString());
                return seqNo1.compareTo(seqNo2);
            });
        }

        super.setDataLine(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "field"), prmBudgetAccessoryList, true, 0);

        //sheet-运行
        sheet = workbook.createSheet("运行");
        super.initCellStyle(workbook);
        super.setHeaderLine(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "name"), hideSeq);
        super.setColumnWidth(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "width"));
        super.setDataLine(sheet, getColumnFriendlyNameOrWidth(sheet.getSheetName(), "field"), (List) dto.get("prmBudgetRunDto"), hideSeq, 0);

        try {
            workbook.write(os);
            if(os instanceof FileOutputStream) {
                os.close();
            }
            workbook.dispose();
        } catch (IOException var3) {
            throw new BizException("Can\'t close Workbook.", var3);
        }
//        resetTimeStyleStr();
        return null;
    }

    public String serialNoSortFn(String value) {
        if(StringUtil.isNotEmpty(value)) {
            String[] orgNums = value.split("-");
            String convertedNums = "";
            for(String item : orgNums) {
                String newNum = item;
                if (item.length() < 10) {
                    for (int i = item.length(); i < 10; i++) {
                        newNum = "0" + newNum;
                    }
                }
                convertedNums += newNum + "-";
            }
            return convertedNums;
        } else {
            return "0";
        }
    }

    /**
     *
     * @param sheetName
     * @param type  name:表头字段名列表；width:宽值列表；field:字段列表
     * @return
     */
    protected List getColumnFriendlyNameOrWidth(String sheetName,String type){
        List lstName = new ArrayList<>();
        List lstWidth = new ArrayList<>();
        List lstField = new ArrayList<>();
        if("立项主信息".equals(sheetName)){
            lstName.add("合同名称");  lstWidth.add((double)(double)80);
            lstName.add("项目名称");  lstWidth.add((double)80);
            lstName.add("是否预立项");lstWidth.add((double)90);
            lstName.add("合同运行额（元）");lstWidth.add((double)100);
            lstName.add("成本控制额（元）");lstWidth.add((double)100);
            lstName.add("承担部门");lstWidth.add((double)80);
            lstName.add("项目负责人");lstWidth.add((double)90);
            lstName.add("工期");lstWidth.add((double)50);
            lstName.add("计划开始时间");lstWidth.add((double)100);
            lstName.add("计划完成时间");lstWidth.add((double)100);
            lstName.add("代号类型");lstWidth.add((double)80);
            lstName.add("立项经费说明");lstWidth.add((double)120);
            lstName.add("备注");lstWidth.add((double)200);
        }else if("关联单位".equals(sheetName)){lstWidth.add((double)100);
            lstName.add("关联单位名称");lstWidth.add((double)200);lstField.add("associatedType");
            lstName.add("关联单位类型");lstWidth.add((double)250);lstField.add("associatedUnitsName");
            lstName.add("备注");lstWidth.add((double)250);lstField.add("remark");
        }else if("项目成员表".equals(sheetName)){
            //成员姓名	项目角色	专业分工	进入项目日期	离开项目日期	投入百分比（%）
            lstName.add("成员姓名");lstWidth.add((double)100);lstField.add("staffIdDesc");
            lstName.add("项目角色");lstWidth.add((double)100);lstField.add("postDesc");
            lstName.add("专业分工");lstWidth.add((double)150);lstField.add("jobShareDesc");
            lstName.add("进入项目日期");lstWidth.add((double)110);lstField.add("enterDate");
            lstName.add("离开项目日期");lstWidth.add((double)110);lstField.add("outDate");
            lstName.add("投入百分比（%）");lstWidth.add((double)100);lstField.add("percent");
        }else if("进度计划".equals(sheetName)){
            //项目阶段	计划起始时间	计划结束时间	实际起始时间	实际结束时间
            lstName.add("项目阶段");lstWidth.add((double)80);lstField.add("projectStageDesc");
            lstName.add("计划起始时间");lstWidth.add((double)120);lstField.add("schemingBeginDate");
            lstName.add("计划结束时间");lstWidth.add((double)120);lstField.add("schemingEndDate");
            lstName.add("实际起始时间");lstWidth.add((double)120);lstField.add("actualBeginDate");
            lstName.add("实际结束时间");lstWidth.add((double)120);lstField.add("actualEndDate");
        }else if ("开支计划".equals(sheetName)){
            //项目阶段	开支内容	预计开支金额（元）	开始时间	结束时间
            lstName.add("项目阶段");lstWidth.add((double)80);lstField.add("projectStageDesc");
            lstName.add("开支内容");lstWidth.add((double)150);lstField.add("payContent");
            lstName.add("预计开支金额（元）");lstWidth.add((double)120);lstField.add("payMoney");
            lstName.add("开始时间");lstWidth.add((double)100);lstField.add("beginDate");
            lstName.add("结束时间");lstWidth.add((double)100);lstField.add("endDate");
        }else if("结算计划".equals(sheetName)){
            //计划结算时间	计划结算金额（元）	计划结算成本金额（元）	计划结算利润金额（元）	说明
            lstName.add("计划结算时间");lstWidth.add((double)100);lstField.add("schemingSquareDate");
            lstName.add("计划结算金额（元）");lstWidth.add((double)110);lstField.add("schemingSquareMoney");
            lstName.add("计划结算成本金额（元）");lstWidth.add((double)130);lstField.add("schemingSquareCost");
            lstName.add("计划结算利润金额（元）");lstWidth.add((double)130);lstField.add("schemingSquareProfits");
            lstName.add("说明");lstWidth.add((double)200);lstField.add("explanation");
        }else if("收款计划".equals(sheetName)){
            //项目阶段	计划收款时间	计划收款金额（元）	说明
            lstName.add("项目阶段");lstWidth.add((double)100);lstField.add("projectStageDesc");
            lstName.add("计划收款时间");lstWidth.add((double)120);lstField.add("schemingReceiptsDate");
            lstName.add("计划收款金额（元）");lstWidth.add((double)120);lstField.add("schemingReceiptsMoney");
            lstName.add("说明");lstWidth.add((double)200);lstField.add("explanation");
        }else if ("质量安全计划".equals(sheetName)){
            //安全负责人	安全联系人	安全体系文件	安全保障措施	质量负责人	质量联系人	质量体系文件	质量保障措施	外部编号	备注
            lstName.add("安全负责人");lstWidth.add((double)100);lstField.add("safePrincipalDesc");
            lstName.add("安全联系人");lstWidth.add((double)100);lstField.add("safeContactDesc");
            lstName.add("安全体系文件");lstWidth.add((double)120);lstField.add("safeDocument");
            lstName.add("安全保障措施");lstWidth.add((double)120);lstField.add("safeMeasure");
            lstName.add("质量负责人");lstWidth.add((double)100);lstField.add("qualityPrincipalDesc");
            lstName.add("质量联系人");lstWidth.add((double)100);lstField.add("qualityContactDesc");
            lstName.add("质量体系文件");lstWidth.add((double)120);lstField.add("qualityDocument");
            lstName.add("质量保障措施");lstWidth.add((double)120);lstField.add("qualityMeasure");
            lstName.add("外部编号");lstWidth.add((double)100);lstField.add("outerNo");
            lstName.add("备注");lstWidth.add((double)200);lstField.add("remark");
        }else if("立项预算汇总".equals(sheetName)){
            //名称	原合同价（元）	联合设计上报（元）	说明	计划预算金额（元）	备注
            lstName.add("名称");lstWidth.add((double)100);lstField.add("budgetCodeDesc");
            lstName.add("原合同价（元）");lstWidth.add((double)110);lstField.add("contractMoney");
            lstName.add("联合设计上报（元）");lstWidth.add((double)120);lstField.add("jointDesignMoney");
            lstName.add("成本控制额(元)");lstWidth.add((double)200);lstField.add("costControlMoney");
            lstName.add("增值税");lstWidth.add((double)200);lstField.add("vatAmount");
            lstName.add("税前成本");lstWidth.add((double)200);lstField.add("excludingVatAmount");
            lstName.add("说明");lstWidth.add((double)200);lstField.add("explanation");
            //lstName.add("计划预算金额（元）");lstWidth.add((double)120);                  //字段找不到
            lstName.add("备注");lstWidth.add((double)150);lstField.add("remark");
        }else if ("外协".equals(sheetName)){
            //名称	主要外协内容	数量	单位	计划单价（元）	计划总价（元）	备注
            lstName.add("序号");lstWidth.add((double)100);lstField.add("serialNumber");
            lstName.add("名称");lstWidth.add((double)100);lstField.add("supplierCode");
            lstName.add("主要外协内容");lstWidth.add((double)120);lstField.add("content");
            lstName.add("数量");lstWidth.add((double)80);lstField.add("amount");
            lstName.add("单位");lstWidth.add((double)60);lstField.add("unit");
            lstName.add("计划单价（元）");lstWidth.add((double)120);lstField.add("price");
            lstName.add("计划总价（元）");lstWidth.add((double)120);lstField.add("totalValue");
            lstName.add("备注");lstWidth.add((double)200);lstField.add("remark");
        }else if ("主材".equals(sheetName)){
            //设备名称	型号规格	生产厂家	单位	合同数量	合同单价（元）	合同总价（元）	计划数量	计划单价（元）	计划总价（元）	计划毛利（元）	备注
            lstName.add("序号");lstWidth.add((double)100);lstField.add("serialNumber");
            lstName.add("设备名称");lstWidth.add((double)100);lstField.add("equipmentName");
            lstName.add("型号规格");lstWidth.add((double)80);lstField.add("equipmentModel");
            lstName.add("生产厂家");lstWidth.add((double)100);lstField.add("factory");
            lstName.add("单位");lstWidth.add((double)70);lstField.add("unit");
            lstName.add("合同数量");lstWidth.add((double)70);lstField.add("contractAmount");
            lstName.add("合同单价（元）");lstWidth.add((double)100);lstField.add("contractPrice");
            lstName.add("合同总价（元）");lstWidth.add((double)100);lstField.add("contractTotalValue");
            lstName.add("计划数量");lstWidth.add((double)70);lstField.add("amount");
            lstName.add("计划单价（元）");lstWidth.add((double)100);lstField.add("schemingPrice");
            lstName.add("计划总价（元）");lstWidth.add((double)100);lstField.add("schemingTotalValue");
            lstName.add("计划毛利（元）");lstWidth.add((double)100);lstField.add("schemingGrossProfit");
            lstName.add("备注");lstWidth.add((double)200);lstField.add("remark");
        }else if("辅材".equals(sheetName)){
            //材料名称	规格	数量	计划单价（元）	计划总价（元）	备注
            lstName.add("序号");lstWidth.add((double)100);lstField.add("serialNumber");
            lstName.add("材料名称");lstWidth.add((double)100);lstField.add("accessoryName");
            lstName.add("规格");lstWidth.add((double)80);lstField.add("accessoryModel");
            lstName.add("数量");lstWidth.add((double)80);lstField.add("amount");
            lstName.add("计划单价（元）");lstWidth.add((double)110);lstField.add("price");
            lstName.add("计划总价（元）");lstWidth.add((double)110);lstField.add("totalValue");
            lstName.add("备注");lstWidth.add((double)200);lstField.add("remark");
        }else if("运行".equals(sheetName)){
            //名称	单位	数量	计划单价（元）	计划总价（元）	备注
            lstName.add("名称");lstWidth.add((double)100);lstField.add("financialSubjectCodeDesc");
            lstName.add("单位");lstWidth.add((double)80);lstField.add("unit");
            lstName.add("数量");lstWidth.add((double)80);lstField.add("amount");
            lstName.add("计划单价（元）");lstWidth.add((double)120);lstField.add("price");
            lstName.add("计划总价（元）");lstWidth.add((double)120);lstField.add("totalValue");
            lstName.add("备注");lstWidth.add((double)200);lstField.add("remark");
        }
        if("name".equals(type))
            return lstName;
        else if("width".equals(type))
            return lstWidth;
        else if("field".equals(type))
            return lstField;
        return null;
    }

}
