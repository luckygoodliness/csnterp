package com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.action;

import com.csnt.scdp.bizmodules.entityattributes.prm.PrmPurchaseReqAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmContractAttribute;
import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.services.intf.PrmpurchasereqManager;
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
import java.util.List;
import java.util.Map;

/**
 * Created by Feng on 2015/11/5.
 */
@Scope("singleton")
@Controller("prmpurchasereq-submit")
@Transactional
public class SubmitAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(SubmitAction.class);

    @Resource(name = "prmpurchasereq-manager")
    private PrmpurchasereqManager prmpurchasereqManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map rsMap = new HashMap();
        List uuidlst = (List) inMap.get("uuidlst");

        if (uuidlst == null || uuidlst.size() == 0) {
            return rsMap;
        }
        for (int i = 0; i < uuidlst.size(); i++) {
            String uuid = uuidlst.get(i).toString();
            if (StringUtil.isNotEmpty(uuid)) {
                prmpurchasereqManager.submit(uuid);
                rsMap.put(PrmPurchaseReqAttribute.STATE, uuid);
            }
        }
        return rsMap;
    }
}
