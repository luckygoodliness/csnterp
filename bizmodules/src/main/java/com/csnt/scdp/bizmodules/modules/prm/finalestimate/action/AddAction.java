package com.csnt.scdp.bizmodules.modules.prm.finalestimate.action;

import com.csnt.scdp.bizmodules.modules.prm.finalestimate.services.intf.FinalestimateManager;
import com.csnt.scdp.framework.commonaction.crud.CommonAddAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.ListUtil;
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
 * @timestamp 2015-09-23 20:02:44
 */

@Scope("singleton")
@Controller("finalestimate-add")
@Transactional
public class AddAction extends CommonAddAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);

    @Resource(name = "finalestimate-manager")
    private FinalestimateManager finalestimateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        //Do After
        return out;
    }
}
