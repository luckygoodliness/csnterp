package com.csnt.scdp.bizmodules.modules.workflow.nonprm.budgetajust;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.entity.nonprm.*;
import com.csnt.scdp.bizmodules.entityattributes.nonprm.NonProjectSubjectSubjectAttribute;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.dto.NonProjectBudgetHDto;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.services.intf.BudgetManager;
import com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.services.intf.BudgetajustManager;
import com.csnt.scdp.bizmodules.modules.nonprm.subjectsubject.services.intf.SubjectsubjectManager;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
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
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("non_prm_budget_adjustment-after-fixed")
@Transactional

public class NonPrmBudgetAdjustAfterComplete extends ERPWorkFlowBaseVariableCollectionAction {

    @Resource(name = "budgetajust-manager")
    private BudgetajustManager budgetajustManager;

    @Resource(name = "subjectsubject-manager")
    private SubjectsubjectManager subjectsubjectManager;

    @Resource(name = "budget-manager")
    private BudgetManager budgetManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapResult = new HashMap();
        String uuid = (String) inMap.get("businessKey");
        NonProjectBudgetAjustH ajustH = budgetajustManager.getNonProjectBudgetAjustHById(uuid);
        List<NonProjectBudgetAjustC> ajustCList = budgetajustManager.getBudgetChangedByHid(uuid);

        NonProjectBudgetH budgetH = budgetManager.getPassedNonProjectBudgetH(ajustH.getOfficeId(), ajustH.getYear());

        Map<String, Map> BudgetView = budgetManager.getNonProjectBudgetExecuteInfoByOfficeIdAndYear(ajustH.getOfficeId(), ajustH.getYear());

        if (budgetH != null && ListUtil.isNotEmpty(ajustCList)) {
            Map<String, NonProjectBudgetC> nonProjectBudgetCList = budgetManager.getPassedNonProjectBudgetC(ajustH.getOfficeId(), ajustH.getYear())
                    .stream().collect(Collectors.toMap(NonProjectBudgetC::getFinancialSubjectCode, t -> (t)));

            List<SubjectSubject> subjectAsset = subjectsubjectManager.getOfficeFixedSubject(ajustH.getOfficeId(), NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT, "固定资产添置");
            List<SubjectSubject> subjectManage = subjectsubjectManager.getOfficeFixedSubject(ajustH.getOfficeId(), NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT, "管理费用");
            String assetCode = ListUtil.isNotEmpty(subjectAsset) ? subjectAsset.get(0).getUuid() : "";
            String manageCode = ListUtil.isNotEmpty(subjectManage) ? subjectManage.get(0).getUuid() : "";


            ajustCList.forEach(t -> {
                if (nonProjectBudgetCList.containsKey(t.getFinancialSubjectCode())) {
                    //若该部门当年存在改科目的预算计划，直接修改当年变更金额及其明细
                    NonProjectBudgetC budgetC = nonProjectBudgetCList.get(t.getFinancialSubjectCode());
                    BigDecimal thisYearChanged = budgetC.getThisYearChanged() == null ? BigDecimal.ZERO : budgetC.getThisYearChanged();
                    BigDecimal thisYearAssigned = budgetC.getThisYearAssigned() == null ? BigDecimal.ZERO : budgetC.getThisYearAssigned();

                    BigDecimal changedBudget = t.getBudgetChanged() == null ? BigDecimal.ZERO : t.getBudgetChanged();
                    if (BigDecimal.ZERO.compareTo(changedBudget) != 0) {
                        budgetC.setThisYearChanged(thisYearChanged.add(changedBudget));
                        BigDecimal appropriation = budgetC.getThisYearAppropriation() == null ? BigDecimal.ZERO : budgetC.getThisYearAppropriation();
                        BigDecimal thisYearFinal = thisYearAssigned.add(budgetC.getThisYearChanged());

                        if (appropriation.compareTo(thisYearFinal) > 0) {
                            budgetC.setThisYearAppropriation(thisYearFinal);
                            //若预算变更后，拨款金额大于当年下发金额，自动调整拨款金额为当年下发金额
                            NonProjectBudgetAppro approDto = new NonProjectBudgetAppro();
                            approDto.setAppropriationBefore(budgetC.getThisYearAppropriation());
                            approDto.setAppropriationAfter(thisYearFinal);
                            approDto.setBudgetCuuid(budgetC.getUuid());
                            approDto.setDescp("预算变更后，拨款金额大于最终下发金额，自动调整拨款金额。");

                            if (BudgetView.containsKey(budgetC.getUuid())) {
                                approDto.setBudgetSubjectName((String) BudgetView.get(budgetC.getUuid()).get("financialSubject"));
                                approDto.setBudgetSubjectCode((String) BudgetView.get(budgetC.getUuid()).get("subjectCode"));
                            }
                            budgetManager.save(approDto);
                        }
                        budgetManager.updateNonProjectBudgetC(budgetC);
                    }
                } else {
                    //若该部门当年不存在改科目的预算计划，插入打牌预算计划及其明细
                    NonProjectBudgetC budgetC = new NonProjectBudgetC();
                    BigDecimal changedBudget = t.getBudgetChanged() == null ? BigDecimal.ZERO : t.getBudgetChanged();
                    budgetC.setSeqNo(t.getSeqNo());
                    budgetC.setFinancialSubjectCode(t.getFinancialSubjectCode());
                    budgetC.setThisYearChanged(changedBudget);
                    budgetC.setHid(budgetH.getUuid());
                    budgetManager.saveNonProjectBudgetC(budgetC);
                    nonProjectBudgetCList.put(t.getFinancialSubjectCode(), budgetC);
                }
                if (StringUtil.isNotEmpty(assetCode) && assetCode.equals(t.getFinancialSubjectCode())) {
                    String CUuid = nonProjectBudgetCList.containsKey(assetCode) ? nonProjectBudgetCList.get(assetCode).getUuid() : "";
                    saveAdjustDetail2BudgetDetail(t.getUuid(), CUuid);
                }
                if (StringUtil.isNotEmpty(manageCode) && manageCode.equals(t.getFinancialSubjectCode())) {
                    String CUuid = nonProjectBudgetCList.containsKey(manageCode) ? nonProjectBudgetCList.get(manageCode).getUuid() : "";
                    saveAdjustDetail2BudgetDetail(t.getUuid(), CUuid);
                }
            });
        }
        return mapResult;
    }

    private void saveAdjustDetail2BudgetDetail(String adjustCUuid, String budgetCUuid) {
        List<NonProjectBudgetAjustCD> adjust = budgetajustManager.getAjustCDByCid(adjustCUuid);
        Map<String, NonProjectBudgetCD> budgetC = budgetManager.getObjsByCid(budgetCUuid)
                .stream().collect(Collectors.toMap(NonProjectBudgetCD::getUuid, t -> (t)));
        adjust.forEach(t -> {
            if (StringUtil.isNotEmpty(t.getBudgetCdUuid()) && budgetC.containsKey(t.getBudgetCdUuid())) {
                NonProjectBudgetCD cd2 = budgetC.get(t.getBudgetCdUuid());
                cd2.setItem(t.getItem());
                cd2.setPrice(t.getPrice());
                cd2.setAmount(t.getAppliedAmount());
                BigDecimal price = t.getPrice() == null ? BigDecimal.ZERO : t.getPrice();
                BigDecimal amount = t.getAppliedAmount() == null ? BigDecimal.ZERO : t.getAppliedAmount();
                cd2.setTotalValue(price.multiply(amount));
                cd2.setDescp(t.getDescp());
                budgetManager.update(cd2);
            } else {
                NonProjectBudgetCD cd = new NonProjectBudgetCD();
                cd.setCid(budgetCUuid);
                cd.setSeqNo(t.getSeqNo());
                cd.setItem(t.getItem());
                cd.setPrice(t.getPrice());
                cd.setAmount(t.getAppliedAmount());
                BigDecimal price = t.getPrice() == null ? BigDecimal.ZERO : t.getPrice();
                BigDecimal amount = t.getAppliedAmount() == null ? BigDecimal.ZERO : t.getAppliedAmount();
                cd.setTotalValue(price.multiply(amount));
                cd.setDescp(t.getDescp());
                Object rid = budgetManager.save(cd);
                t.setBudgetCdUuid((String) rid);
                budgetajustManager.save(t);
            }
        });
    }
}
