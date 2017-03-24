package com.csnt.scdp.bizmodules.modules.prm.prmbilling.action;


import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.entity.prm.PrmBilling;
import com.csnt.scdp.bizmodules.modules.prm.prmbilling.services.intf.PrmbillingManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Scope("singleton")
@Controller("prmbilling-getinvoiceno")
@Transactional
public class getInvoiceNoAction implements IAction {

    @Resource(name = "prmbilling-manager")
    private PrmbillingManager prmbillingManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map out = new HashMap();
        String uuid = prmbillingManager.isNullReturnEmpty(inMap.get("uuid"));

        PrmBilling prmBilling = PersistenceFactory.getInstance().findByPK(PrmBilling.class, uuid);
        if (prmBilling != null&& StringUtil.isNotEmpty(prmBilling.getInvoiceNo())) {
            out.put("invoiceNo", prmBilling.getInvoiceNo());
        } else {
            out.put("invoiceNo", "NULL");
        }
        return out;
    }
}
