package com.csnt.scdp.bizmodules.modules.prm.prmbilling.action;


import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.attributes.OriginalFormTypeAttribute;
import com.csnt.scdp.bizmodules.entity.prm.PrmBilling;
import com.csnt.scdp.bizmodules.entity.prm.PrmReceipts;
import com.csnt.scdp.bizmodules.modules.fad.cashreq.services.intf.CashreqManager;
import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
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
@Controller("prmbilling-invoiceNoDateSave")
@Transactional
public class InvoiceNoDateSaveAction implements IAction {

    @Resource(name = "prmbilling-manager")
    private PrmbillingManager prmbillingManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map out = new HashMap();
        String prmBillingUuid = prmbillingManager.isNullReturnEmpty(inMap.get("prmBillingUuid"));
        String tblVersion = prmbillingManager.isNullReturnEmpty(inMap.get("tblVersion"));
        String invoiceNo = prmbillingManager.isNullReturnEmpty(inMap.get("invoiceNo"));
        Date invoiceDate = null;
        if (prmbillingManager.isNullReturnEmpty(inMap.get("invoiceDate")).length() > 0) {
            invoiceDate = new Date(prmbillingManager.isNullReturnEmpty(inMap.get("invoiceDate")));
        }
        prmbillingManager.billingCheckTimeStamp(prmBillingUuid, tblVersion);
        PrmBilling prmBilling = PersistenceFactory.getInstance().findByPK(PrmBilling.class, prmBillingUuid);
        if (prmBilling != null && BillStateAttribute.CMD_BILL_STATE_APPROVED.equals(prmBilling.getState())) {
            prmBilling.setInvoiceNo(invoiceNo);
            prmBilling.setInvoiceDate(invoiceDate);
            PersistenceFactory.getInstance().update(prmBilling);
        } else {
            out.put("errorMsg", "只有审批通过的单据才能保存开票!");
        }
        return out;
    }
}
