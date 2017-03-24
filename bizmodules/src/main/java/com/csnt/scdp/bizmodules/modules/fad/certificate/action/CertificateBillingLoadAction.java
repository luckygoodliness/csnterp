package com.csnt.scdp.bizmodules.modules.fad.certificate.action;

import com.csnt.scdp.bizmodules.entity.fad.FadInvoice;
import com.csnt.scdp.bizmodules.entity.prm.PrmBilling;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Reese on 2015/10/14.
 */
@Scope("singleton")
@Controller("certificate-certificateBillingLoad")
@Transactional
public class CertificateBillingLoadAction extends CommonLoadAction implements IAction {

    private static ILogTracer tracer = LogTracerFactory.getInstance(CertificateBillingLoadAction.class);
    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map result = new HashMap<>();
        String businessId = certificateManager.isNullReturnEmpty(inMap.get("businessId"));

        //5.PRM_BILLING(开票)
        PrmBilling prmBilling = certificateManager.isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmBilling.class, businessId) : null;
        if (prmBilling != null) {
            result.put("prmBilling", prmBilling);
        }

        return result;
    }
}
