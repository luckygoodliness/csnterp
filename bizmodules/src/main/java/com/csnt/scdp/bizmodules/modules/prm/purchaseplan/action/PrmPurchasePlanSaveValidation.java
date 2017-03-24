package com.csnt.scdp.bizmodules.modules.prm.purchaseplan.action;

import com.csnt.scdp.bizmodules.attributes.ErpCommonAttribute;
import com.csnt.scdp.bizmodules.attributes.PrmBudgetCodes;
import com.csnt.scdp.bizmodules.entity.prm.PrmBudgetAccessory;
import com.csnt.scdp.bizmodules.entity.prm.PrmBudgetOutsource;
import com.csnt.scdp.bizmodules.entity.prm.PrmBudgetPrincipal;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmPurchasePackageAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmPurchasePlanDetailAttribute;
import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.services.intf.PurchaseplanManager;
import com.csnt.scdp.framework.commonaction.crud.CommonQueryAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by asas on 2015/10/21.
 * <p/>
 * 查询相关立项类型
 */
@Scope("singleton")
@Controller("prm-purchaseplan-save-validation")
@Transactional
public class PrmPurchasePlanSaveValidation extends CommonQueryAction {
    @Resource(name = "purchaseplan-manager")
    private PurchaseplanManager purchaseplanManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //before
        Map out = new HashMap();
        StringBuffer errorMsg = new StringBuffer();
        String prmProjectMainId = (String) inMap.get("prmProjectMainId");
        List<Map<String, Object>> lstPurchasePrincipal = (List<Map<String, Object>>) inMap.get(PrmBudgetCodes.PRINCIPAL);
        List<Map<String, Object>> lstPurchaseAccessory = (List<Map<String, Object>>) inMap.get(PrmBudgetCodes.ACCESSORY);
        List<Map<String, Object>> lstPurchaseOutsource = (List<Map<String, Object>>) inMap.get(PrmBudgetCodes.OUTSOURCE);
        Map condition = new HashMap();
        condition.put(PrmPurchasePlanDetailAttribute.PRM_PROJECT_MAIN_ID, prmProjectMainId);
        if (lstPurchasePrincipal != null) {
            List<PrmBudgetPrincipal> lstBudgetPrincipal = PersistenceFactory.getInstance().findByAnyFields(PrmBudgetPrincipal.class, condition,
                    null);
            for (int i = 0; i < lstPurchasePrincipal.size(); i++) {
                Map purchaseItem = lstPurchasePrincipal.get(i);
                String budgetRefId = (String) purchaseItem.get(PrmPurchasePlanDetailAttribute.PRM_BUDGET_REF_ID);
                double purchaseMoney = 0;
                if (StringUtil.isNotEmpty(purchaseItem.get(PrmPurchasePlanDetailAttribute.PURCHASE_BUDGET_MONEY))) {
                    purchaseMoney = new BigDecimal(String.valueOf(purchaseItem.get
                            (PrmPurchasePlanDetailAttribute.PURCHASE_BUDGET_MONEY))).doubleValue();
                }
                for (Iterator<PrmBudgetPrincipal> iterator = lstBudgetPrincipal.iterator(); iterator.hasNext(); ) {
                    PrmBudgetPrincipal budgetPrincipal = iterator.next();
                    if (budgetRefId.equals(budgetPrincipal.getUuid())) {
                        double budgetMoney = budgetPrincipal.getSchemingTotalValue() == null ? 0 : budgetPrincipal
                                .getSchemingTotalValue().doubleValue();
                        if (purchaseMoney > budgetMoney) {
                            //大于预算金额
                            String serialNumber = budgetPrincipal.getSerialNumber();
                            errorMsg.append(ErpCommonAttribute.BREAK_LINE + "拆项合计不能大于立项预算金额(" + budgetMoney + ")。预算类别：主材，序号：" + serialNumber);
                        }
                        if ("1".equals(purchaseItem.get(PrmPurchasePackageAttribute.SUBJECT_PROPERTY))) {
                            List<Map> lstRigidAmount = (List<Map>) purchaseItem.get("rigidAmounts");
                            if (ListUtil.isNotEmpty(lstBudgetPrincipal)) {
                                for (Map rigidItem : lstRigidAmount) {
                                    BigDecimal purchaseAmount = new BigDecimal(String.valueOf(rigidItem.get
                                            (PrmPurchasePlanDetailAttribute
                                                    .AMOUNT)));
                                    BigDecimal budgetAmount = budgetPrincipal.getAmount() == null ? BigDecimal.ZERO : budgetPrincipal
                                            .getAmount();
                                    if (purchaseAmount.compareTo(budgetAmount) > 0) {
                                        errorMsg.append(ErpCommonAttribute.BREAK_LINE + "刚性采购数量不能超过原始预算数量(" + budgetAmount + ")。预算类别：主材，序号：" +
                                                rigidItem.get(PrmPurchasePlanDetailAttribute.SERIAL_NUMBER));
                                    }
                                }
                            }
                        }
                        iterator.remove();
                    }
                }
            }
        }
        if (lstPurchaseAccessory != null) {
            List<PrmBudgetAccessory> lstBudgetAccessory = PersistenceFactory.getInstance().findByAnyFields(PrmBudgetAccessory.class, condition,
                    null);
            for (int i = 0; i < lstPurchaseAccessory.size(); i++) {
                Map purchaseItem = lstPurchaseAccessory.get(i);
                String budgetRefId = (String) purchaseItem.get(PrmPurchasePlanDetailAttribute.PRM_BUDGET_REF_ID);
                double purchaseMoney = 0;
                if (StringUtil.isNotEmpty(purchaseItem.get(PrmPurchasePlanDetailAttribute.PURCHASE_BUDGET_MONEY))) {
                    purchaseMoney = new BigDecimal(String.valueOf(purchaseItem.get
                            (PrmPurchasePlanDetailAttribute.PURCHASE_BUDGET_MONEY))).doubleValue();
                }
                for (Iterator<PrmBudgetAccessory> iterator = lstBudgetAccessory.iterator(); iterator.hasNext(); ) {
                    PrmBudgetAccessory budgetAccessory = iterator.next();
                    if (budgetRefId.equals(budgetAccessory.getUuid())) {
                        double budgetMoney = budgetAccessory.getTotalValue() == null ? 0 : budgetAccessory.getTotalValue()
                                .doubleValue();
                        if (purchaseMoney > budgetMoney) {
                            //大于预算金额
                            String serialNumber = budgetAccessory.getSerialNumber();
                            errorMsg.append(ErpCommonAttribute.BREAK_LINE + "拆项合计不能大于立项预算金额(" + budgetMoney + ")" +
                                    "。预算类别：辅材，序号：" +
                                    serialNumber);
                        }
                        if ("1".equals(purchaseItem.get(PrmPurchasePackageAttribute.SUBJECT_PROPERTY))) {
                            List<Map> lstRigidAmount = (List<Map>) purchaseItem.get("rigidAmounts");
                            if (ListUtil.isNotEmpty(lstBudgetAccessory)) {
                                for (Map rigidItem : lstRigidAmount) {
                                    BigDecimal purchaseAmount = new BigDecimal(String.valueOf(rigidItem.get
                                            (PrmPurchasePlanDetailAttribute
                                                    .AMOUNT)));
                                    BigDecimal budgetAmount = budgetAccessory.getAmount() == null ? BigDecimal.ZERO :
                                            budgetAccessory.getAmount();
                                    if (purchaseAmount.compareTo(budgetAmount) > 0) {
                                        errorMsg.append(ErpCommonAttribute.BREAK_LINE + "刚性采购数量不能超过原始预算数量(" + budgetAmount + ")。预算类别：辅材，序号：" +
                                                rigidItem.get(PrmPurchasePlanDetailAttribute.SERIAL_NUMBER));
                                    }
                                }
                            }
                        }
                        iterator.remove();
                    }
                }
            }
        }
        if (lstPurchaseOutsource != null) {
            List<PrmBudgetOutsource> lstBudgetOutsource = PersistenceFactory.getInstance().findByAnyFields(PrmBudgetOutsource.class, condition,
                    null);
            for (int i = 0; i < lstPurchaseOutsource.size(); i++) {
                Map purchaseItem = lstPurchaseOutsource.get(i);
                String budgetRefId = (String) purchaseItem.get(PrmPurchasePlanDetailAttribute.PRM_BUDGET_REF_ID);
                double purchaseMoney = 0;
                if (StringUtil.isNotEmpty(purchaseItem.get(PrmPurchasePlanDetailAttribute.PURCHASE_BUDGET_MONEY))) {
                    purchaseMoney = new BigDecimal(String.valueOf(purchaseItem.get
                            (PrmPurchasePlanDetailAttribute.PURCHASE_BUDGET_MONEY))).doubleValue();
                }
                for (Iterator<PrmBudgetOutsource> iterator = lstBudgetOutsource.iterator(); iterator.hasNext(); ) {
                    PrmBudgetOutsource budgetOutsource = iterator.next();
                    if (budgetRefId.equals(budgetOutsource.getUuid())) {
                        double budgetMoney = budgetOutsource.getTotalValue() == null ? 0 : budgetOutsource.getTotalValue()
                                .doubleValue();
                        if (purchaseMoney > budgetMoney) {
                            //大于预算金额
                            String serialNumber = budgetOutsource.getSerialNumber();
                            errorMsg.append(ErpCommonAttribute.BREAK_LINE + "拆项合计不能大于立项预算金额(" + budgetMoney + ")。 预算类别：外协，序号：" + serialNumber);
                        }
                        if ("1".equals(purchaseItem.get(PrmPurchasePackageAttribute.SUBJECT_PROPERTY))) {
                            List<Map> lstRigidAmount = (List<Map>) purchaseItem.get("rigidAmounts");
                            if (ListUtil.isNotEmpty(lstBudgetOutsource)) {
                                for (Map rigidItem : lstRigidAmount) {
                                    BigDecimal purchaseAmount = new BigDecimal(String.valueOf(rigidItem.get
                                            (PrmPurchasePlanDetailAttribute.AMOUNT)));
                                    BigDecimal budgetAmount = budgetOutsource.getAmount() == null ? BigDecimal.ZERO :
                                            budgetOutsource.getAmount();
                                    if (purchaseAmount.compareTo(budgetAmount) > 0) {
                                        errorMsg.append(ErpCommonAttribute.BREAK_LINE + "刚性采购数量不能超过原始预算数量(" + budgetAmount + ")。预算类别：外协，序号：" +
                                                rigidItem.get(PrmPurchasePlanDetailAttribute.SERIAL_NUMBER));
                                    }
                                }
                            }
                        }
                        iterator.remove();
                    }
                }
            }
        }
        out.put("errorMsg", errorMsg.toString());
        return out;
    }
}
