package com.csnt.scdp.bizmodules.modules.prm.projectmain.action;

import com.csnt.scdp.bizmodules.modules.prm.projectmain.services.intf.ProjectmainManager;
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
import java.util.Map;

/**
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 13:30:43
 */

@Scope("singleton")
@Controller("projectmain-show-budget-history")
@Transactional
public class ShowBudgetHistoryAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ShowBudgetHistoryAction.class);

    @Resource(name = "projectmain-manager")
    private ProjectmainManager projectmainManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        String uuid = (String) inMap.get(CommonAttribute.UUID);


        Map out = projectmainManager.loadProjectBudgetHistory(uuid);
        //Do After
        return out;
    }
}
