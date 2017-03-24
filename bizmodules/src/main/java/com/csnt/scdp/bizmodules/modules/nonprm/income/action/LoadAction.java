package com.csnt.scdp.bizmodules.modules.nonprm.income.action;

import com.csnt.scdp.bizmodules.modules.common.action.ErpLoadAction;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.services.intf.BudgetManager;
import com.csnt.scdp.bizmodules.modules.nonprm.income.services.intf.IncomeManager;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Map;

/**
 * Description:  LoadAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-24 17:37:27
 */

@Scope("singleton")
@Controller("income-load")
@Transactional
public class LoadAction extends ErpLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

    @Resource(name = "income-manager")
    private IncomeManager incomeManager;

    @Resource(name = "budget-manager")
    private BudgetManager budgetManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map out = super.perform(inMap);
        //Do After
        return out;
    }
}

