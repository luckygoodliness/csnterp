package com.csnt.scdp.bizmodules.modules.fad.certificate.action;

import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
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
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 15:32:42
 */

@Scope("singleton")
@Controller("certificate-getFadCertificateUuid")
@Transactional
public class GetFadCertificateUuidAction implements IAction {

    private static ILogTracer tracer = LogTracerFactory.getInstance(GetFadCertificateUuidAction.class);
    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map result = new HashMap();
        String businessId = certificateManager.isNullReturnEmpty(inMap.get("uuid"));
        String fadCertificateUuid = certificateManager.isNullReturnEmpty(certificateManager.getFadCertificateUuidByBusinessId(businessId));
        result.put("fadCertificateUuid", fadCertificateUuid);
        return result;
    }
}
