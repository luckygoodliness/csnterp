package com.csnt.scdp.bizmodules.modules.prm.purchaseplan.action;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.attributes.PrmBudgetCodes;
import com.csnt.scdp.bizmodules.entity.prm.*;
import com.csnt.scdp.bizmodules.entity.scm.ScmMaterialClass;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmBudgetPrincipalAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmMaterialClassAttribute;
import com.csnt.scdp.bizmodules.modules.common.action.ErpLoadAction;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpUserHelper;
import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.dto.PrmPurchasePackageDto;
import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.dto.PrmPurchasePlanDetailDto;
import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.dto.PrmPurchasePlanDto;
import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.services.intf.PurchaseplanManager;
import com.csnt.scdp.bizmodules.modules.scm.purchasereq.services.intf.PurchasereqManager;
import com.csnt.scdp.framework.bo.QueryCondition;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MathUtil;
import com.csnt.scdp.framework.util.ObjectUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpFileManager;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:  LoadAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 19:04:01
 */

@Scope("singleton")
@Controller("purchaseplan-load")
@Transactional
public class LoadAction extends ErpLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

    @Resource(name = "purchaseplan-manager")
    private PurchaseplanManager purchaseplanManager;

    @Resource(name = "purchasereq-manager")
    private PurchasereqManager purchasereqManager;


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        //Do After
        return out;
    }

    @Override
    protected void afterAction(BasePojo dtoObj) {
        PrmPurchasePlanDto prmProjectMain = (PrmPurchasePlanDto) dtoObj;
        prmProjectMain.setPrmProjectMainId(prmProjectMain.getUuid());
        if (prmProjectMain == null) {
            return;
        }
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        //******set project information start
        if (StringUtil.isNotEmpty(prmProjectMain.getProjectManager())) {
            ScdpUser managerUser = ErpUserHelper.getScdpUserByUserId(prmProjectMain.getProjectManager());
            if (managerUser != null) {
                prmProjectMain.setManager(managerUser.getUserName());
            }
        }
        if (StringUtil.isNotEmpty(prmProjectMain.getContractorOffice())) {
            prmProjectMain.setContractorOfficeDesc(OrgHelper.getOrgNameByCode(prmProjectMain.getContractorOffice()));
        }
        if (StringUtil.isNotEmpty(prmProjectMain.getCustomerId())) {
            String custName = purchaseplanManager.getCustmerNameByUuid(prmProjectMain.getCustomerId());
            prmProjectMain.setCustomer(custName);
        }
        //****set project information end

        List<PrmPurchasePackageDto> lstPurchasePackage = prmProjectMain.getPrmPurchasePackageDto();
        List<PrmPurchasePlanDetailDto> lstPurchasePlanDetail = prmProjectMain.getPrmPurchasePlanDetailDto();
        if (ListUtil.isEmpty(lstPurchasePlanDetail)) {
            return;
        }

        if (ListUtil.isNotEmpty(lstPurchasePackage)) {
            List<String> lstMaterialClassCode = lstPurchasePackage.stream().filter(x -> StringUtil.isNotEmpty(x
                    .getMaterialClassCode())).map
                    (x -> x.getMaterialClassCode()).distinct().collect(Collectors.toList());
            if (ListUtil.isNotEmpty(lstMaterialClassCode)) {
                Map condition = new HashMap<>();
                condition.put(ScmMaterialClassAttribute.CODE, lstMaterialClassCode);
                List<ScmMaterialClass> lstMaterialClass = pcm.findByAnyFields(ScmMaterialClass.class, condition, null);
                if (ListUtil.isNotEmpty(lstMaterialClass)) {
                    for (ScmMaterialClass materialClass : lstMaterialClass) {
                        for (PrmPurchasePackageDto purchasePackage : lstPurchasePackage) {
                            if (materialClass.getCode().equals(purchasePackage.getMaterialClassCode())) {
                                purchasePackage.setMaterialClassCodeDesc(materialClass.getName());
                            }
                        }
                    }
                }
            }
            List<String> lstPkgId = lstPurchasePackage.stream().filter(x -> StringUtil.isNotEmpty(x
                    .getUuid())).map
                    (x -> x.getUuid()).distinct().collect(Collectors.toList());
            if (ListUtil.isNotEmpty(lstPkgId)) {
                DAOMeta daoMeta = new DAOMeta();
                String sql = "select package_id,remain_budget from vw_prm_budget v where v.package_id in (" + StringUtil.joinForSqlIn(lstPkgId, ",") + ")";
                daoMeta.setStrSql(sql);
                List<Map<String, Object>> reqList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                if (ListUtil.isNotEmpty(reqList)) {
                    for (Map<String, Object> m : reqList) {
                        for (PrmPurchasePackageDto purchasePackage : lstPurchasePackage) {
                            if (m.get("packageId").equals(purchasePackage.getUuid())) {
                                purchasePackage.setRemainBudget(new BigDecimal(StringUtil.replaceNull(m.get("remainBudget"), "0")));
                            }
                        }
                    }
                }
            }
            StringBuilder qryCondition = new StringBuilder();
            for (PrmPurchasePackageDto prmPurchasePackageDto : lstPurchasePackage) {
                qryCondition.append("'" + prmPurchasePackageDto.getUuid() + "'");
                qryCondition.append(",");
            }
            qryCondition.deleteCharAt(qryCondition.length() - 1);
            DAOMeta daoMeta = DAOHelper.getDAO("purchaseplan-dao", "get_req_info", null);
            daoMeta.setNeedFilter(false);
            Map mapCond = new HashMap();
            mapCond.put("packageIds", qryCondition);
            daoMeta.setStrSql(StringUtil.replaceParams(daoMeta.getStrSql(), mapCond));
            List<Map<String, Object>> lstChange = pcm.findByNativeSQL(daoMeta);
            if (ListUtil.isNotEmpty(lstChange)) {
                for (PrmPurchasePackageDto prmPurchasePackageDto : lstPurchasePackage) {
                    for (Map tmp : lstChange) {
                        String dataUUid = StringUtil.replaceNull(tmp.get(("uuid")));
                        if (dataUUid.equals(prmPurchasePackageDto.getUuid())) {
                            prmPurchasePackageDto.setCompletePercent(StringUtil.replaceNull(tmp.get("completePercent"), "0"));
                            prmPurchasePackageDto.setAppliedMoney(new BigDecimal(StringUtil.replaceNull(tmp.get("appliedMoney"), "0")));
                            prmPurchasePackageDto.setPackageBalance(new BigDecimal(StringUtil.replaceNull(tmp.get("packageBalance"), "0")));
                            prmPurchasePackageDto.setRemainBudget(new BigDecimal(StringUtil.replaceNull(prmPurchasePackageDto.getRemainBudget(), "0")));
                            break;
                        }
                    }
                }
            }
        }

        Map mapCondition = new HashMap();
        mapCondition.put(PrmBudgetPrincipalAttribute.PRM_PROJECT_MAIN_ID, prmProjectMain.getUuid());

        List<PrmBudgetPrincipal> lstBudgetPrincipal = pcm.findByAnyFields(PrmBudgetPrincipal.class, mapCondition, null);
        List<PrmBudgetAccessory> lstBudgetAccessory = pcm.findByAnyFields(PrmBudgetAccessory.class, mapCondition, null);
        List<PrmBudgetOutsource> lstBudgetOutsource = pcm.findByAnyFields(PrmBudgetOutsource.class, mapCondition, null);
        List<PrmBudgetRun> lstBudgetRun = pcm.findByAnyFields(PrmBudgetRun.class, mapCondition, null);

        List<Map<String, Object>> lstPurchaseReqLock = purchasereqManager.getPurchaseReqDetailByProjectId(prmProjectMain.getUuid());
        Map fadInvoiceInfo = new HashMap<>();
        fadInvoiceInfo.putAll(purchaseplanManager.getInvoiceInfoByMainId(prmProjectMain.getUuid()));
        lstPurchasePlanDetail.forEach(x -> {
            //set the purchase request status
            if (ListUtil.isNotEmpty(lstPurchaseReqLock)) {
                List<Map<String, Object>> lstPurchaseReqLockScreen = lstPurchaseReqLock.parallelStream().filter(y -> x.getUuid()
                        .equals(y.get("prmPurchasePlanDetailId"))).collect(Collectors.toList());
                if (ListUtil.isNotEmpty(lstPurchaseReqLockScreen)) {
                    x.setIsReq(Integer.valueOf(1));
                    BigDecimal handleAmount = BigDecimal.ZERO;
                    for (Map m : lstPurchaseReqLockScreen) {
                        handleAmount = MathUtil.add(handleAmount, new BigDecimal("" + m.get("lockAmount")));
                    }
                    x.setAppliedAmount(handleAmount);
                }
            }

            //set package state and information
            if (ListUtil.isNotEmpty(lstPurchasePackage)) {
                PrmPurchasePackageDto purchasePackage = lstPurchasePackage.parallelStream().filter(y -> y.getUuid
                        ().equals(x.getPurchasePackageId())).findAny().orElse(null);
                if (purchasePackage != null) {
                    String purchaseState = purchasePackage.getPackageState();
                    if (BillStateAttribute.CMD_BILL_STATE_CLOSED.equals(purchaseState)) {
                        x.setIsClose(Integer.valueOf(1));
                    }
                }
            }

            //set whether is original budget record
            String budgetCode = x.getPrmBudgetType();
            if (PrmBudgetCodes.PRINCIPAL.equals(budgetCode)) {
                if (ListUtil.isNotEmpty(lstBudgetPrincipal)) {
                    PrmBudgetPrincipal budget = lstBudgetPrincipal.parallelStream().filter(y -> y.getUuid
                            ().equals(x.getPrmBudgetRefId())).findAny().orElse(null);
                    if (budget != null) {
                        x.setOriginalAmount(budget.getAmount());
                        if (StringUtil.isEmpty(x.getSerialNumber())
                                || ObjectUtil.beSame(budget.getSerialNumber(), x.getSerialNumber())) {
                            x.setIsBudget(Integer.valueOf(1));
                            x.setOriBudgetAmount(budget.getAmount());
                            x.setOriBudgetPrice(budget.getSchemingPrice());
                            x.setOriBudgetTotalValue(budget.getSchemingTotalValue());
                        }
                    }
                }
            } else if (PrmBudgetCodes.ACCESSORY.equals(budgetCode)) {
                if (ListUtil.isNotEmpty(lstBudgetAccessory)) {
                    PrmBudgetAccessory budget = lstBudgetAccessory.parallelStream().filter(y -> y.getUuid
                            ().equals(x.getPrmBudgetRefId())).findAny().orElse(null);
                    if (budget != null) {
                        x.setOriginalAmount(budget.getAmount());
                        if (StringUtil.isEmpty(x.getSerialNumber())
                                || ObjectUtil.beSame(budget.getSerialNumber(), x.getSerialNumber())) {
                            x.setIsBudget(Integer.valueOf(1));
                            x.setOriBudgetAmount(budget.getAmount());
                            x.setOriBudgetPrice(budget.getPrice());
                            x.setOriBudgetTotalValue(budget.getTotalValue());
                        }
                    }
                }
            } else if (PrmBudgetCodes.OUTSOURCE.equals(budgetCode)) {
                if (ListUtil.isNotEmpty(lstBudgetOutsource)) {
                    PrmBudgetOutsource budget = lstBudgetOutsource.parallelStream().filter(y -> y.getUuid
                            ().equals(x.getPrmBudgetRefId())).findAny().orElse(null);
                    if (budget != null) {
                        x.setOriginalAmount(budget.getAmount());
                        if (StringUtil.isEmpty(x.getSerialNumber())
                                || ObjectUtil.beSame(budget.getSerialNumber(), x.getSerialNumber())) {
                            x.setIsBudget(Integer.valueOf(1));
                            x.setOriBudgetAmount(budget.getAmount());
                            x.setOriBudgetPrice(budget.getPrice());
                            x.setOriBudgetTotalValue(budget.getTotalValue());
                        }
                    }
                }
            } else if (PrmBudgetCodes.RUN.equals(budgetCode)) {
                if (ListUtil.isNotEmpty(lstBudgetRun)) {
                    PrmBudgetRun budget = lstBudgetRun.parallelStream().filter(y -> y.getUuid
                            ().equals(x.getPrmBudgetRefId())).findAny().orElse(null);
                    if (budget != null) {
                        if (fadInvoiceInfo.get(budget.getFinancialSubjectCode()) != null) {
                            x.setIsRunApply(Integer.valueOf(1));
                        }
                        x.setOriginalAmount(budget.getAmount());
                        if (StringUtil.isEmpty(x.getSerialNumber())
                                || ObjectUtil.beSame(budget.getSerialNumber(), x.getSerialNumber())) {
                            x.setIsBudget(Integer.valueOf(1));
                            x.setOriBudgetAmount(budget.getAmount());
                            x.setOriBudgetPrice(budget.getPrice());
                            x.setOriBudgetTotalValue(budget.getTotalValue());

                        }
                    }
                }
            }

        });

        if (ListUtil.isEmpty(lstPurchasePlanDetail)) {
            return;
        } else {
            //设置可申请数量
            for (int i = 0; i < lstPurchasePlanDetail.size(); i++) {
                BigDecimal amount = lstPurchasePlanDetail.get(i).getAmount() == null ? BigDecimal.valueOf(0) : lstPurchasePlanDetail.get(i).getAmount();
                BigDecimal appliedAmount = lstPurchasePlanDetail.get(i).getAppliedAmount() == null ? BigDecimal.valueOf(0) : lstPurchasePlanDetail.get(i).getAppliedAmount();
                long avaAmount = amount.longValue() - appliedAmount.longValue();
                lstPurchasePlanDetail.get(i).setAvailableAmount(BigDecimal.valueOf(avaAmount));
            }
        }
    }
}
