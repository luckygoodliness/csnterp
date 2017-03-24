package com.csnt.scdp.bizmodules.modules.workflow.nonprm.budget;

import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetC;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.services.intf.BudgetManager;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shenyx on 2016/01/06.
 */
@Scope("singleton")
@Controller("non_prm_budget_plan_apply-after-fixed")
@Transactional

public class NonPrmBudgetAfterFixedAction extends ERPWorkFlowBaseVariableCollectionAction {

    @Resource(name = "budget-manager")
    private BudgetManager budgetManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapVar = new HashMap();
        String uuid = (String) inMap.get("businessKey");
        List<NonProjectBudgetC> nonProjectBudgetCList = budgetManager.getNonProjectBudgetCDetailsByHid(uuid);
        for (NonProjectBudgetC budgetC : nonProjectBudgetCList) {
            if (budgetC.getThisYearFirstInstance() == null || budgetC.getThisYearAssigned() == null) {
                throw new BizException("审批失败！存在当年初审或当年下达数据为空的记录！");
            }
        }
        nonProjectBudgetCList.forEach(t -> {
            t.setThisYearAppropriation(t.getThisYearPreappropriation());
            budgetManager.updateNonProjectBudgetC(t);
        });
        return mapVar;
    }
}
