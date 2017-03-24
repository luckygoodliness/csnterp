package com.csnt.scdp.bizmodules.modules.fad.invoice.action;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.entity.fad.FadInvoice;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadInvoiceAttribute;
import com.csnt.scdp.bizmodules.modules.fad.invoice.services.intf.InvoiceManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
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
@Controller("invoice-annul")
@Transactional
public class BtnAnnulAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(BtnAnnulAction.class);

    @Resource(name = "invoice-manager")
    private InvoiceManager invoiceManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map rsMap = new HashMap();
        String uuid = (String) inMap.get("uuid");
        if (StringUtil.isNotEmpty(uuid)) {
            FadInvoice fadInvoice = PersistenceFactory.getInstance().findByPK(FadInvoice.class, uuid);
            if (fadInvoice != null) {
                if (BillStateAttribute.FAD_BILL_STATE_SENT.equals(fadInvoice.getState())) {
                    throw new BizException("废止失败，凭证已经发送NC，请到凭证编辑界面做红冲。", new HashMap());
                } else {
                    fadInvoice.setState(BillStateAttribute.FAD_BILL_STATE_DISABLED);
                    PersistenceFactory.getInstance().update(fadInvoice);
                    rsMap.put(FadInvoiceAttribute.INVOICE_REQ_NO, fadInvoice.getInvoiceReqNo());
                }
            }
        }
        return rsMap;
    }
}
