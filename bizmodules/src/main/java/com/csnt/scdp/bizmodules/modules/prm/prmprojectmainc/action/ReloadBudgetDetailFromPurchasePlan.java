package com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.action;

import com.csnt.scdp.bizmodules.attributes.PrmBudgetCodes;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmBudgetDetailCAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainCAttribute;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.services.intf.PrmprojectmaincManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-27 15:28:19
 */

@Scope("singleton")
@Controller("prmprojectmainc-reload-budget-detail-from-purchase-plan")
@Transactional
public class ReloadBudgetDetailFromPurchasePlan implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ReloadBudgetDetailFromPurchasePlan.class);

    @Resource(name = "prmprojectmainc-manager")
    private PrmprojectmaincManager prmprojectmaincManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = new HashMap();
        List lstReturn = null;
        String projectUuid = (String) inMap.get(PrmProjectMainCAttribute.PRM_PROJECT_MAIN_ID);
        String budgetCode = (String) inMap.get(PrmBudgetDetailCAttribute.BUDGET_CODE);
        List<Map<String, Object>> lstBudget = (List<Map<String, Object>>) inMap.get("details");
        if (PrmBudgetCodes.OUTSOURCE.equals(budgetCode)) {
            lstReturn = prmprojectmaincManager.reloadOutsourceFromPurchasePlan(projectUuid, lstBudget);
        } else if (PrmBudgetCodes.PRINCIPAL.equals(budgetCode)) {
            lstReturn = prmprojectmaincManager.reloadPrincipalFromPurchasePlan(projectUuid, lstBudget);
        } else if (PrmBudgetCodes.ACCESSORY.equals(budgetCode)) {
            lstReturn = prmprojectmaincManager.reloadAccessoryFromPurchasePlan(projectUuid, lstBudget);
        }

        out.put(CommonAttribute.DATAROOT, lstReturn);
        //Do After
        return out;
    }

}
