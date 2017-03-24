package com.csnt.scdp.bizmodules.modules.fad.certificate.action;

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
@Controller("certificate-originalToCertificate")
@Transactional
public class OriginalToCertificateAction extends CommonLoadAction implements IAction {

    private static ILogTracer tracer = LogTracerFactory.getInstance(OriginalToCertificateAction.class);
    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map result = new HashMap<>();
        String businessId = certificateManager.isNullReturnEmpty(inMap.get("businessId"));
        String originalFormTypeForeign = certificateManager.isNullReturnEmpty(inMap.get("originalFormTypeForeign"));
        String fadCertificateUuid = certificateManager.javaBeanToCertificate(businessId, originalFormTypeForeign);
        result.put("fadCertificateUuid", fadCertificateUuid);
        return result;
    }
}
