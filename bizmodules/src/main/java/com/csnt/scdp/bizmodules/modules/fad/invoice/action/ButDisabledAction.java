package com.csnt.scdp.bizmodules.modules.fad.invoice.action;

import com.csnt.scdp.bizmodules.entityattributes.fad.FadInvoiceAttribute;
import com.csnt.scdp.bizmodules.modules.fad.invoice.services.intf.InvoiceManager;
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
 * @timestamp 2015-09-26 15:32:42
 */

@Scope("singleton")
@Controller("invoice-disable")
@Transactional
public class ButDisabledAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ButDisabledAction.class);

    @Resource(name = "invoice-manager")
    private InvoiceManager invoiceManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map rsMap = new HashMap();
        String uuid = (String) inMap.get("uuid");
        if (StringUtil.isNotEmpty(uuid)) {
            invoiceManager.changeStateToDisabled(uuid);
            rsMap.put(FadInvoiceAttribute.STATE, uuid);
        }
        return rsMap;
    }
}
