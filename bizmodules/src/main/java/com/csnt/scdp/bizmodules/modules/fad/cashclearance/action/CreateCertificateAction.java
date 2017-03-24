package com.csnt.scdp.bizmodules.modules.fad.cashclearance.action;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.attributes.OriginalFormTypeAttribute;
import com.csnt.scdp.bizmodules.entity.fad.FadCashClearance;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCashClearanceAttribute;
import com.csnt.scdp.bizmodules.modules.fad.cashclearance.services.intf.CashclearanceManager;
import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
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
import java.util.Date;
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
@Controller("cashclearance-createcertificate")
@Transactional
public class CreateCertificateAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(CreateCertificateAction.class);

    @Resource(name = "cashclearance-manager")
    private CashclearanceManager cashclearanceManager;

    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map rsMap = new HashMap();
        String uuid = (String) inMap.get("uuid");
        String fadCertificateUuid = certificateManager.javaBeanToCertificate(uuid, OriginalFormTypeAttribute.FAD_CASH_REQ_INVOICE);
        if (StringUtil.isEmpty(fadCertificateUuid)) {
            throw new BizException("凭证生成失败");
        }
        FadCashClearance fadCashClearance = PersistenceFactory.getInstance().findByPK(FadCashClearance.class, uuid);
        if (fadCashClearance != null) {
            fadCashClearance.setState(BillStateAttribute.FAD_BILL_STATE_CERTIFICATED);
            fadCashClearance.setCertificateCreateTime(new Date());
            PersistenceFactory.getInstance().update(fadCashClearance);
            rsMap.put(FadCashClearanceAttribute.RUNNING_NO, fadCashClearance.getRunningNo());
            rsMap.put("fadCertificateUuid", fadCertificateUuid);
        }

        return rsMap;
    }
}
