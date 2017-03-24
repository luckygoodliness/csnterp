package com.csnt.scdp.bizmodules.modules.nonprm.budgeth.action;

import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetAppro;
import com.csnt.scdp.bizmodules.entity.nonprm.SubjectSubject;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.services.intf.BudgetManager;
import com.csnt.scdp.bizmodules.modules.nonprm.subjectsubject.services.intf.SubjectsubjectManager;
import com.csnt.scdp.framework.commonaction.crud.CommonLoadAction;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.ListUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Reese on 2015/10/14.
 */
@Scope("singleton")
@Controller("budgeth-approdetail")
@Transactional
public class ApproDetailAction extends CommonLoadAction implements IAction {

    private static ILogTracer tracer = LogTracerFactory.getInstance(ApproDetailAction.class);
    @Resource(name="budget-manager")
    private BudgetManager budgetManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map out = new HashMap<>();
        String budgetCUuid = (String)inMap.get("budgetCUuid");
        List<NonProjectBudgetAppro> lstBudgetAppro = budgetManager.getObjByCid(budgetCUuid);
        out.put("lstBudgetAppro",lstBudgetAppro);
        return  out;
    }

}
