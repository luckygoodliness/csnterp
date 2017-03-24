package com.csnt.scdp.bizmodules.modules.fad.certificate.action;

import com.csnt.scdp.framework.core.dao.DAOMeta;
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
import java.util.List;
import java.util.Map;

/**
 * Created by Reese on 2015/10/14.
 */
@Scope("singleton")
@Controller("certificate-certificateMerge")
@Transactional
public class CertificateMergeAction extends CommonLoadAction implements IAction {

    private static ILogTracer tracer = LogTracerFactory.getInstance(CertificateMergeAction.class);
    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map result = new HashMap<>();
        List fadCertificateGridSelectionUuidList = (List) inMap.get("fadCertificateGridSelectionUuidList");
        List fadCertificateGridSelectionTblVersionList = (List) inMap.get("fadCertificateGridSelectionTblVersionList");

        for (int i = 0; i < fadCertificateGridSelectionUuidList.size(); i++) {
            String fadCertificateUuid = certificateManager.isNullReturnEmpty(fadCertificateGridSelectionUuidList.get(i));
            String tblVersion = certificateManager.isNullReturnEmpty(fadCertificateGridSelectionTblVersionList.get(i));
            certificateManager.certificateCheckTimeStamp(fadCertificateUuid, tblVersion);
        }
        String returnValue = certificateManager.certificateMerge(fadCertificateGridSelectionUuidList);
        if (!(returnValue.equals("OK"))) {
            result.put("errorMsg", returnValue);
        }
        return result;
    }
}
