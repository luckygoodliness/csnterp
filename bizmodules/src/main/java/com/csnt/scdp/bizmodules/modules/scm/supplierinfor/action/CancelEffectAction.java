package com.csnt.scdp.bizmodules.modules.scm.supplierinfor.action;

import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.services.intf.SupplierinforManager;
import com.csnt.scdp.framework.commonaction.crud.CommonModifyAction;
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
import java.util.Map;

/**
 * Description:  ModifyAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-24 16:02:00
 */

@Scope("singleton")
@Controller("supplierinfor-canceleffect")
@Transactional
public class CancelEffectAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(CancelEffectAction.class);

    @Resource(name = "supplierinfor-manager")
    private SupplierinforManager supplierinforManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        supplierinforManager.cancelEffectSupplier(inMap);
        Map out = new HashMap<>();
        return out;
    }
}