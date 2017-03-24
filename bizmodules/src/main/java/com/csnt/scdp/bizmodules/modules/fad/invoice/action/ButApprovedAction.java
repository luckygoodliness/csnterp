package com.csnt.scdp.bizmodules.modules.fad.invoice.action;

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
@Controller("invoice-approved")
@Transactional
public class ButApprovedAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ButApprovedAction.class);

    @Resource(name = "invoice-manager")
    private InvoiceManager invoiceManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map rsMap = new HashMap();
        String uuid = (String) inMap.get("uuid");
        if (StringUtil.isNotEmpty(uuid)) {
            FadInvoice fadInvoice = PersistenceFactory.getInstance().findByPK(FadInvoice.class, uuid);

            boolean isPrm = false;
            String budgetId =fadInvoice.getSubjectId();
            if(fadInvoice.getPrmProjectMainId()!=null){
                isPrm=true;
            }
            BigDecimal expensesMoney =fadInvoice.getExpensesMoney();
            invoiceManager.checkBudgetIsEnough(isPrm,budgetId,expensesMoney);
            invoiceManager.changeStateToApproved(uuid);
            rsMap.put(FadInvoiceAttribute.STATE, uuid);
        }
        return rsMap;
    }
}
