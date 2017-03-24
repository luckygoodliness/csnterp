package com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.action;

import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.services.intf.PrmpurchasereqManager;
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
 * Created by Feng on 2015/11/23.
 */
@Scope("singleton")
@Controller("prmpurchasereq-abolish")
@Transactional
public class AuditAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(AuditAction.class);

    @Resource(name = "prmpurchasereq-manager")
    private PrmpurchasereqManager prmpurchasereqManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map rsMap = new HashMap();
        String uuid = "" + inMap.get("uuid");
        String reason = "" + inMap.get("fallbackReason");
        Map out = new HashMap<>();
        String result = prmpurchasereqManager.abolish(uuid, reason);
        out.put("result", result);
        return out;
    }
}
