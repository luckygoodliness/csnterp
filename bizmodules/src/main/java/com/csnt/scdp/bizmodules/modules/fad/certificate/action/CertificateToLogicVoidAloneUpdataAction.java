package com.csnt.scdp.bizmodules.modules.fad.certificate.action;

import com.csnt.scdp.bizmodules.entity.fad.FadCertifiDeficitRel;
import com.csnt.scdp.bizmodules.entity.fad.FadCertifiMergeSplitRel;
import com.csnt.scdp.bizmodules.entity.fad.FadCertificate;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCertifiDeficitRelAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCertifiMergeSplitRelAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCertificateAttribute;
import com.csnt.scdp.bizmodules.modules.fad.certificate.dto.FadCertificateDto;
import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
import com.csnt.scdp.framework.commonaction.crud.CommonLoadAction;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Reese on 2015/10/14.
 */
@Scope("singleton")
@Controller("certificate-certificateToLogicVoidAloneUpdata")
@Transactional
public class CertificateToLogicVoidAloneUpdataAction extends CommonLoadAction implements IAction {

    private static ILogTracer tracer = LogTracerFactory.getInstance(CertificateToLogicVoidUpdataAction.class);
    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        String fadCertificateUuid = certificateManager.isNullReturnEmpty(inMap.get("fadCertificateUuid"));
        String tblVersion = certificateManager.isNullReturnEmpty(inMap.get("tblVersion"));
        certificateManager.certificateCheckTimeStamp(fadCertificateUuid, tblVersion);

        String makerUuid = certificateManager.isNullReturnEmpty(inMap.get("makerUuid"));
        return certificateManager.certificateToLogicVoidAlone(fadCertificateUuid, makerUuid);
    }
}
