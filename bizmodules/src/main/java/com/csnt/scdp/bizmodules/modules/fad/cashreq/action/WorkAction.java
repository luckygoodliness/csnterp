package com.csnt.scdp.bizmodules.modules.fad.cashreq.action;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.entity.fad.FadCashReq;
import com.csnt.scdp.bizmodules.modules.fad.cashreq.services.intf.CashreqManager;
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
 * Created by fisher on 2015/11/10.
 */
@Scope("singleton")
@Controller("cashreq-work")
@Transactional
public class WorkAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(WorkAction.class);

    @Resource(name = "cashreq-manager")
    private CashreqManager cashreqManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map rsMap = new HashMap();
        String uuid = (String) inMap.get("uuid");
        String actionType = (String) inMap.get("actionType");
        FadCashReq req;
        switch (actionType) {
            case "trash":
                req = cashreqManager.getFadCashReqbyUuid(uuid);
                if (BillStateAttribute.FAD_BILL_STATE_APPROVED.equals(req.getState())) {
                    req.setState(BillStateAttribute.FAD_BILL_STATE_DISABLED);
                    cashreqManager.updateFadCashReq(req);
                } else {
                    throw new BizException("操作失败！");
                }
                break;
        }
        rsMap.put("result", true);
        return rsMap;
    }
}
