package com.csnt.scdp.bizmodules.modules.mobileservice.scm.scmcontract;

import com.csnt.scdp.bizmodules.attributes.ErpMobileModulePathAttribute;
import com.csnt.scdp.bizmodules.attributes.ErpMobileTerminalAttribute;
import com.csnt.scdp.bizmodules.entity.operate.OperateBusinessBidInfo;
import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entity.scm.ScmContract;
import com.csnt.scdp.bizmodules.entity.scm.ScmSupplier;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCashReqInvoiceAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmPurchaseReqDetailAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmContractAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmContractChangeAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmContractDetailAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmContractPaytypeAttribute;
import com.csnt.scdp.bizmodules.modules.cdm.cdmfile.dto.CdmFileRelationDto;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpI18NHelper;
import com.csnt.scdp.bizmodules.modules.fad.cashreq.dto.FadCashReqDto;
import com.csnt.scdp.bizmodules.modules.fad.invoice.dto.FadCashReqInvoiceDto;
import com.csnt.scdp.bizmodules.modules.mobileservice.ERPMobileTerminalBaseVariableCollectionAction;
import com.csnt.scdp.bizmodules.modules.mobileservice.intf.MobileTerminalQuery;
import com.csnt.scdp.bizmodules.modules.scm.scmcontract.dto.*;
import com.csnt.scdp.bizmodules.modules.workflow.message.UnHandledTaskReminderJob;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.helper.SysconfigHelper;
import com.csnt.scdp.framework.util.DateUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MathUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.helper.FMCodeHelper;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by lenovo on 2016/8/30.
 */
@Scope("singleton")
@Controller("mobile-terminal-query-scm_contract_prepare")
@Transactional
public class ScmContractQuery extends ERPMobileTerminalBaseVariableCollectionAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(UnHandledTaskReminderJob.class);

    @Resource(name = "mobile-terminal-query")
    private MobileTerminalQuery mobileTerminalQuery;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map out = new LinkedHashMap<>();//返回结果
        Map itemMap = new LinkedHashMap<>();//主子表数据
        Map extraGridMap = new LinkedHashMap<>();//子表数据
        Map basePojoMap = super.perform(inMap);
        String workFlowDefinitionKey = (String) inMap.get(WorkFlowAttribute.WORKFLOW_DEFINITION_KEY);
        BasePojo basePojo = (BasePojo) basePojoMap.get(ErpMobileTerminalAttribute.BASE_POJO);
        ScmContractDto scmContractDto = (ScmContractDto) basePojo;

        String orgCode = scmContractDto.getOfficeId();
        if (orgCode != null) {
            String orgName = OrgHelper.getOrgNameByCode(orgCode);
            scmContractDto.setOrgName(orgName);
        }
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        String projectId = scmContractDto.getProjectId();
        String subjectCode = scmContractDto.getSubjectCode();
        String supplierId = scmContractDto.getSupplierCode();
        if (StringUtil.isNotEmpty(projectId)) {
            PrmProjectMain prmProjectMain = pcm.findByPK(PrmProjectMain.class, projectId);
            if (prmProjectMain != null) {
                scmContractDto.setProjectName(prmProjectMain.getProjectName());
                scmContractDto.setFadSubjectCode(prmProjectMain.getProjectCode());
            }
        } else if (StringUtil.isNotEmpty(subjectCode)) {
            DAOMeta daoMeta = new DAOMeta();
            String sql = "SELECT N.FINANCIAL_SUBJECT FROM NON_PROJECT_SUBJECT_SUBJECT N WHERE N.FINANCIAL_SUBJECT_CODE= '" + subjectCode + "'";
            daoMeta.setStrSql(sql);
            List<Map<String, Object>> lis = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            if (ListUtil.isNotEmpty(lis)) {
                Map m = lis.get(0);
                scmContractDto.setProjectName((String) m.get("financialSubject"));
            }
            scmContractDto.setFadSubjectCode(subjectCode);
        }

        List<PrmPurchaseReqDetailDto> prmPurchaseReqDetailDtos = scmContractDto.getPrmPurchaseReqDetailDto();
        if (ListUtil.isNotEmpty(prmPurchaseReqDetailDtos)) {

            BigDecimal budgetAmount = new BigDecimal(0);
            BigDecimal expectedAmount = new BigDecimal(0);
            for (PrmPurchaseReqDetailDto prmPurchaseReqDetailDto : prmPurchaseReqDetailDtos) {
                BigDecimal totalBudgetMoney = new BigDecimal(0);
                BigDecimal totalExpectedMoney = new BigDecimal(0);
                BigDecimal budgetPrice = prmPurchaseReqDetailDto.getBudgetPrice();
                BigDecimal expectedPrice = prmPurchaseReqDetailDto.getExpectedPrice();
                BigDecimal amount = prmPurchaseReqDetailDto.getAmount();
                if (budgetPrice != null && amount != null) {
                    totalBudgetMoney = budgetPrice.multiply(amount);
                    prmPurchaseReqDetailDto.setTotalBudgetMoney(totalBudgetMoney);
                }
                if (expectedPrice != null && amount != null) {
                    totalExpectedMoney = expectedPrice.multiply(amount);
                    prmPurchaseReqDetailDto.setTotalExpectedMoney(totalExpectedMoney);
                }
                budgetAmount = totalBudgetMoney.add(budgetAmount);
                expectedAmount = totalExpectedMoney.add(expectedAmount);
            }
            scmContractDto.setBudgetAmount(budgetAmount);
            scmContractDto.setExpectedAmount(expectedAmount);
        }

        String contractNature = FMCodeHelper.getDescByCode(scmContractDto.getContractNature(), "SCM_CONTRACT_NATURE");
        String purchaseTypes = FMCodeHelper.getDescByCode(scmContractDto.getPurchaseTypes(), "SCM_PURCHASE_TYPE");
        String contractPayType = FMCodeHelper.getDescByCode(scmContractDto.getContractPayType(), "SCM_CONTRACT_PAY_TYPE");
        String contractState = FMCodeHelper.getDescByCode(scmContractDto.getContractState(), "SCM_CONTRACT_STATE");
        String isClosed = FMCodeHelper.getDescByCode(scmContractDto.getIsClosed(), "SCM_CONTRACT_CLOSE_STATE");
        String payType = FMCodeHelper.getDescByCode(scmContractDto.getPayType(), "FAD_PAYWAY");

        if (StringUtil.isNotEmpty(supplierId)) {
            ScmSupplier scmSupplier = pcm.findByPK(ScmSupplier.class, supplierId);
            if (scmSupplier != null) {
                String taxTypes = scmSupplier.getTaxTypes();
                String levelTypes = scmSupplier.getLevelCode();
                String supplierGenre = scmSupplier.getSupplierGenre();
                if (StringUtil.isNotEmpty(taxTypes)) {
                    if ("0".equals(taxTypes)) {
                        taxTypes = "一般纳税人";
                    } else {
                        taxTypes = "小规模纳税人";
                    }
                } else {
                    taxTypes = "未知";
                }
                if (StringUtil.isNotEmpty(supplierGenre)) {
                    switch (supplierGenre) {
                        case "0":
                            supplierGenre = "合格供方";
                            break;
                        case "1":
                            supplierGenre = "普通供方";
                            break;
                        case "2":
                            supplierGenre = "零星供方";
                            break;
                        case "3":
                            supplierGenre = "报销供方";
                            break;
                        default:
                            supplierGenre = "其它";
                    }
                } else {
                    supplierGenre = "其它";
                }
                scmContractDto.setTaxTypes(taxTypes);
                scmContractDto.setLevelTypes(levelTypes);
                scmContractDto.setSupplierGenre(supplierGenre);
            }
        }

        String stateDesc = FMCodeHelper.getDescByCode(scmContractDto.getState(), "CDM_BILL_STATE");
        scmContractDto.setStateDesc(stateDesc);
        //主表
        Map hostMap = new LinkedHashMap<>();
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractAttribute.SCM_CONTRACT_CODE, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), scmContractDto.getScmContractCode());
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractAttribute.OFFICE_ID, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), scmContractDto.getOrgName());
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractAttribute.FAD_SUBJECT_CODE, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), scmContractDto.getFadSubjectCode());
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractAttribute.PROJECT_NAME, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), scmContractDto.getProjectName());
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractAttribute.BUDGET_AMOUNT, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), MathUtil.formatDecimalToString(scmContractDto.getBudgetAmount(), "##,##0.00"));
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractAttribute.EXPECTED_AMOUNT, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), MathUtil.formatDecimalToString(scmContractDto.getExpectedAmount(), "##,##0.00"));
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractAttribute.CONTRACT_NATURE, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), contractNature);
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractAttribute.SUPPLIER_NAME, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), scmContractDto.getSupplierName());
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractAttribute.QUANTITY, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), MathUtil.formatDecimalToString(scmContractDto.getQuantity(), "##,##0.00"));
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractAttribute.AMOUNT, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), MathUtil.formatDecimalToString(scmContractDto.getAmount(), "##,##0.00"));
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractAttribute.PURCHASE_TYPES, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), purchaseTypes);
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractAttribute.CONTRACT_PAY_TYPE, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), contractPayType);
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractAttribute.CONTRACT_STATE, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), contractState);
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractAttribute.IS_CLOSED, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), isClosed);
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractAttribute.TAX_TYPES, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), scmContractDto.getTaxTypes());
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractAttribute.SUPPLIER_GENRE, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), scmContractDto.getSupplierGenre());
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractAttribute.LEVEL_TYPES, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), scmContractDto.getLevelTypes());
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractAttribute.ETA, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), DateUtil.formatDate(scmContractDto.getEta(), "yyyy-MM-dd"));
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractAttribute.DEBTER_DEPARTMENT, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), scmContractDto.getDebterDepartment());
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractAttribute.DEBTER, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), scmContractDto.getDebter());
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractAttribute.PAYEE_NAME, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), scmContractDto.getPayeeName());
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractAttribute.BANK, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), scmContractDto.getBank());
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractAttribute.BANK_ID, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), scmContractDto.getBankId());
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractAttribute.PAY_TYPE, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), payType);
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractAttribute.TOTAL_VALUE, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), MathUtil.formatDecimalToString(scmContractDto.getTotalValue(), "##,##0.00"));
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractAttribute.IS_URGENT, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), "1".equals(scmContractDto.getIsUrgent()) ? "是" : "否");
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractAttribute.DEBT_DATE, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), DateUtil.formatDate(scmContractDto.getDebtDate(), "yyyy-MM-dd"));
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractAttribute.OTHER_DES, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), scmContractDto.getOtherDes());
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractAttribute.ELECTRIC_COMMERCIAL_STORE, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), scmContractDto.getElectricCommercialStore());
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractAttribute.JD_NAME, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), scmContractDto.getJdName());
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractAttribute.JD_PASSWORD, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), scmContractDto.getJdPassword());
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractAttribute.JD_ORDER_NO, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), scmContractDto.getJdOrderNo());
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractAttribute.JD_ORDER_DATE, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), DateUtil.formatDate(scmContractDto.getJdOrderDate(), "yyyy-MM-dd"));
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractAttribute.JD_LAST_DATE, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), DateUtil.formatDate(scmContractDto.getJdLastDate(), "yyyy-MM-dd"));

        itemMap.putAll(hostMap);


        extraGridMap.put("extraGrid", this.collectInvoiceVariable(scmContractDto));
        //核销信息子表
        itemMap.putAll(extraGridMap);


        out.put(ErpMobileTerminalAttribute.ITEM, itemMap);


        //附件表
        out.putAll(this.collectAttachemtVariable(scmContractDto));

        //报表
        out.putAll(collectReports(scmContractDto));

        //部门code，如果工作流不是自定义的code，则不需要。。。
//        out.putAll(this.getProcessDeptCode(fadCashReqDto, workFlowDefinitionKey));
        return out;
    }


    private Map collectInvoiceVariable(ScmContractDto scmContractDto) {
        Map resultMap = new LinkedHashMap<>();
        List<Map> payTypeInfo = new ArrayList<>();

        List<ScmContractPaytypeDto> scmContractPaytypeDtoList = scmContractDto.getScmContractPaytypeDto();
        if (ListUtil.isNotEmpty(scmContractPaytypeDtoList)) {
            for (ScmContractPaytypeDto scmContractPaytypeDto : scmContractPaytypeDtoList) {
                Map map = new LinkedHashMap<>();
                map.put(ErpI18NHelper.englishToChinese(ScmContractPaytypeAttribute.SEQ_NO, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), scmContractPaytypeDto.getSeqNo());
                map.put(ErpI18NHelper.englishToChinese(ScmContractPaytypeAttribute.TITLE, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), scmContractPaytypeDto.getTitle());
                map.put(ErpI18NHelper.englishToChinese(ScmContractPaytypeAttribute.VALUE, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), scmContractPaytypeDto.getValue());
                map.put(ErpI18NHelper.englishToChinese(ScmContractPaytypeAttribute.ACTUALLY_PAID, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), MathUtil.formatDecimalToString(scmContractPaytypeDto.getActuallyPaid(), "##,##0.00"));
                payTypeInfo.add(map);
            }

        }
        resultMap.put("支付明细", payTypeInfo);

        //
        List<Map> purchaseInfo = new ArrayList<>();
        List<PrmPurchaseReqDetailDto> prmPurchaseReqDetailDtoList = scmContractDto.getPrmPurchaseReqDetailDto();
        if (ListUtil.isNotEmpty(prmPurchaseReqDetailDtoList)) {
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            List purchaseReqIdList = new ArrayList<>();
            List purchaseReqNoList = new ArrayList<>();
            List packageNameList = new ArrayList<>();
            for (PrmPurchaseReqDetailDto prmPurchaseReqDetailDto : prmPurchaseReqDetailDtoList) {
                purchaseReqIdList.add(prmPurchaseReqDetailDto.getPrmPurchaseReqId());
            }
            //申请单号
            DAOMeta daoMeta = new DAOMeta();
            if (ListUtil.isNotEmpty(purchaseReqIdList)) {
                String sql = "SELECT P.UUID,P.PURCHASE_REQ_NO,P.IS_PROJECT FROM PRM_PURCHASE_REQ P WHERE P.UUID IN (" + StringUtil.joinForSqlIn(purchaseReqIdList, ",") + ")";
                daoMeta.setStrSql(sql);
                purchaseReqNoList = pcm.findByNativeSQL(daoMeta);
            }
            List purchasePackageIdList = new ArrayList<>();
            for (PrmPurchaseReqDetailDto prmPurchaseReqDetailDto : prmPurchaseReqDetailDtoList) {

                String purchasePackageId = prmPurchaseReqDetailDto.getPurchasePackageId();
                if (StringUtil.isNotEmpty(purchasePackageId)) {
                    purchasePackageIdList.add(purchasePackageId);
                }
            }
            if (ListUtil.isNotEmpty(purchasePackageIdList)) {
                String selfCondition = " T.PURCHASE_PACKAGE_ID IN (" + StringUtil.joinForSqlIn(purchasePackageIdList, ",") + ")";
                daoMeta = DAOHelper.getDAO("scmcontract-dao", "find_package_name_by_package_id", null);
                daoMeta.setStrSql(daoMeta.getStrSql().replace("${selfconditions}", selfCondition));
                packageNameList = pcm.findByNativeSQL(daoMeta);

            }
            for (PrmPurchaseReqDetailDto prmPurchaseReqDetailDto : prmPurchaseReqDetailDtoList) {
                Map map = new LinkedHashMap<>();
                map.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqDetailAttribute.SERIAL_NUMBER, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), prmPurchaseReqDetailDto.getSerialNumber());
                if (ListUtil.isNotEmpty(packageNameList)) {
                    String purchasePackageId = prmPurchaseReqDetailDto.getPurchasePackageId();
                    for (Object l : packageNameList) {
                        Map m = (Map) l;
                        String mId = (String) m.get("purchasePackageId");
                        if (mId.equals(purchasePackageId)) {
                            String packageName = (String) m.get("packageName");
                            map.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqDetailAttribute.SUB_PACKAGE_NAME, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), packageName);
                            break;
                        }
                    }

                }
                map.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqDetailAttribute.NAME, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), prmPurchaseReqDetailDto.getName());
                map.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqDetailAttribute.MODEL, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), prmPurchaseReqDetailDto.getModel());
                map.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqDetailAttribute.FACTORY, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), prmPurchaseReqDetailDto.getFactory());
                map.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqDetailAttribute.BUDGET_PRICE, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), MathUtil.formatDecimalToString(prmPurchaseReqDetailDto.getBudgetPrice(), "##,##0.00"));
                map.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqDetailAttribute.EXPECTED_PRICE, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), MathUtil.formatDecimalToString(prmPurchaseReqDetailDto.getExpectedPrice(), "##,##0.00"));
                map.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqDetailAttribute.HANDLE_AMOUNT, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), MathUtil.formatDecimalToString(prmPurchaseReqDetailDto.getHandleAmount(), "##,##0.00"));
                BigDecimal totalBudgetMoney = new BigDecimal(0);
                if (prmPurchaseReqDetailDto.getBudgetPrice() != null && prmPurchaseReqDetailDto.getAmount() != null) {
                    totalBudgetMoney = prmPurchaseReqDetailDto.getBudgetPrice().multiply(prmPurchaseReqDetailDto.getAmount());
                }
                map.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqDetailAttribute.TOTAL_BUDGET_MONEY, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), MathUtil.formatDecimalToString(totalBudgetMoney, "##,##0.00"));
                BigDecimal totalExpectedMoney = new BigDecimal(0);
                if (prmPurchaseReqDetailDto.getExpectedPrice() != null && prmPurchaseReqDetailDto.getAmount() != null) {
                    totalExpectedMoney = prmPurchaseReqDetailDto.getExpectedPrice().multiply(prmPurchaseReqDetailDto.getAmount());
                }
                map.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqDetailAttribute.TOTAL_EXPECTED_MONEY, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), MathUtil.formatDecimalToString(totalExpectedMoney, "##,##0.00"));
                map.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqDetailAttribute.ARRIVE_DATE, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), DateUtil.formatDate(prmPurchaseReqDetailDto.getArriveDate(), "yyyy-MM-dd"));
                map.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqDetailAttribute.PRM_PURCHASE_REQ_ID, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), prmPurchaseReqDetailDto.getPrmPurchaseReqId());
                for (Object l : purchaseReqNoList) {
                    Map m = (Map) l;
                    String mId = (String) m.get("uuid");
                    if (mId.equals(prmPurchaseReqDetailDto.getPrmPurchaseReqId())) {
                        map.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqDetailAttribute.PRM_PURCHASE_REQ_ID, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), m.get("purchaseReqNo"));
                        break;
                    }
                }
                map.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqDetailAttribute.REMARK, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), prmPurchaseReqDetailDto.getRemark());
                purchaseInfo.add(map);
            }

        }
        resultMap.put("询价单明细", purchaseInfo);

        List<Map> contractDetailInfo = new ArrayList<>();
        List<ScmContractDetailDto> scmContractDetailDtoList = scmContractDto.getScmContractDetailDto();
        if (ListUtil.isNotEmpty(scmContractDetailDtoList)) {
            for (ScmContractDetailDto scmContractDetailDto : scmContractDetailDtoList) {
                Map map = new LinkedHashMap<>();
                map.put(ErpI18NHelper.englishToChinese(ScmContractDetailAttribute.SEQ_NO, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), scmContractDetailDto.getSeqNo());
                map.put(ErpI18NHelper.englishToChinese(ScmContractDetailAttribute.MATERIAL_NAME, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), scmContractDetailDto.getMaterialName());
                map.put(ErpI18NHelper.englishToChinese(ScmContractDetailAttribute.MODEL, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), scmContractDetailDto.getModel());
                map.put(ErpI18NHelper.englishToChinese(ScmContractDetailAttribute.AMOUNT, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), MathUtil.formatDecimalToString(scmContractDetailDto.getAmount(), "##,##0.00"));
                map.put(ErpI18NHelper.englishToChinese(ScmContractDetailAttribute.UNITS, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), scmContractDetailDto.getUnits());
                map.put(ErpI18NHelper.englishToChinese(ScmContractDetailAttribute.UNIT_PRICE_TALK, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), MathUtil.formatDecimalToString(scmContractDetailDto.getUnitPriceTalk(), "##,##0.00"));
                map.put(ErpI18NHelper.englishToChinese(ScmContractDetailAttribute.UNIT_PRICE_TRUE, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), MathUtil.formatDecimalToString(scmContractDetailDto.getUnitPriceTrue(), "##,##0.00"));
                BigDecimal totalPriceTalk = new BigDecimal(0);
                if (scmContractDetailDto.getUnitPriceTalk() != null && scmContractDetailDto.getAmount() != null) {
                    totalPriceTalk = scmContractDetailDto.getUnitPriceTalk().multiply(scmContractDetailDto.getAmount());
                }
                BigDecimal totalPriceTrue = new BigDecimal(0);
                if (scmContractDetailDto.getUnitPriceTrue() != null && scmContractDetailDto.getAmount() != null) {
                    totalPriceTrue = scmContractDetailDto.getUnitPriceTrue().multiply(scmContractDetailDto.getAmount());
                }
                map.put(ErpI18NHelper.englishToChinese(ScmContractDetailAttribute.TOTAL_PRICE_TALK, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), MathUtil.formatDecimalToString(totalPriceTalk, "##,##0.00"));
                map.put(ErpI18NHelper.englishToChinese(ScmContractDetailAttribute.TOTAL_PRICE_TRUE, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), MathUtil.formatDecimalToString(totalPriceTrue, "##,##0.00"));
                map.put(ErpI18NHelper.englishToChinese(ScmContractDetailAttribute.FACTORY, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), scmContractDetailDto.getFactory());
                map.put(ErpI18NHelper.englishToChinese(ScmContractDetailAttribute.REMARK, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), scmContractDetailDto.getRemark());
                contractDetailInfo.add(map);
            }
        }
        resultMap.put("合同明细", contractDetailInfo);

        //todo 手机端看不到，先不做
//        List<Map> scmChangeDetil = new ArrayList<>();
//        List<ScmContractChangeDto> scmContractChangeDtos = scmContractDto.getScmContractChangeDto();
//        if (ListUtil.isNotEmpty(scmContractChangeDtos)) {
//            for (ScmContractChangeDto scmContractChangeDto : scmContractChangeDtos) {
//                Map map = new LinkedHashMap<>();
//                map.put(ErpI18NHelper.englishToChinese(ScmContractChangeAttribute.RUNNING_NO, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), scmContractChangeDto.getRunningNo());
//                map.put(ErpI18NHelper.englishToChinese(ScmContractChangeAttribute.ORIGINAL_VALUE, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), scmContractChangeDto.getOriginalValue());
//                map.put(ErpI18NHelper.englishToChinese(ScmContractChangeAttribute.NEW_VALUE, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), scmContractChangeDto.getNewValue());
//                map.put(ErpI18NHelper.englishToChinese(ScmContractChangeAttribute.CLOSE_CHANGE, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), scmContractChangeDto.getCloseChange());
//                map.put(ErpI18NHelper.englishToChinese(ScmContractChangeAttribute.STATE, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), scmContractChangeDto.getState());
//                map.put(ErpI18NHelper.englishToChinese(ScmContractChangeAttribute.CHANGE_REASON, ErpMobileModulePathAttribute.SCM_CONTRACT_PREPARE_MODULEPATH), scmContractChangeDto.getChangeReason());
//                scmChangeDetil.add(map);
//            }
//        }
//        resultMap.put("变更记录", scmChangeDetil);

        return resultMap;
    }

    private Map collectAttachemtVariable(ScmContractDto scmContractDto) {

        Map resultMap = new HashMap<>();
        List<Map> attachmentInfo = new ArrayList<Map>();
        List<CdmFileRelationDto> cdmFileRelationDtoList = scmContractDto.getCdmFileRelationDto();
        resultMap.putAll(mobileTerminalQuery.getAttachmentsInfo(cdmFileRelationDtoList));
        return resultMap;
    }

    private Map collectReports(ScmContractDto scmContractDto) {
        Map resultMap = new HashMap<>();
        String previewUrl = SysconfigHelper.getProperty("mobile_fineReport_preview_url");
        Map projectReports = new LinkedHashMap<>();
        List lstResult = new ArrayList<>();
        projectReports.put(ErpMobileTerminalAttribute.IS_DIRECT, true);
        projectReports.put("title", "项目情况明细表");
        projectReports.put("url", previewUrl + "ReportServer?reportlet=" + "YG009_APP" + ".cpt" + "&project_id=" + scmContractDto.getProjectId());
        lstResult.add(projectReports);
        resultMap.put("reports", lstResult);
        return resultMap;
    }
}
