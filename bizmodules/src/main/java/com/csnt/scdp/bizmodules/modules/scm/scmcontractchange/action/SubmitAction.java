package com.csnt.scdp.bizmodules.modules.scm.scmcontractchange.action;

import com.csnt.scdp.bizmodules.modules.scm.scmcontractchange.services.intf.ScmcontractchangeManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 18:01:19
 */

@Scope("singleton")
@Controller("scmcontractchange-submit")
@Transactional
public class SubmitAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(SubmitAction.class);

    @Resource(name = "scmcontractchange-manager")
    private ScmcontractchangeManager scmcontractchangeManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //3.一切条件通过，执行新增语句
        Map rsMap = new HashMap();
        String uuid = (String) inMap.get("uuid");
        if (StringUtil.isNotEmpty(uuid)) {
            scmcontractchangeManager.updateStateToSubmit(uuid);
            rsMap.put("result", "OK");
        }
        return rsMap;
    }
}
