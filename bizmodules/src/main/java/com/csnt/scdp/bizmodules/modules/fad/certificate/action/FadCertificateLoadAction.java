package com.csnt.scdp.bizmodules.modules.fad.certificate.action;

import com.csnt.scdp.bizmodules.entity.fad.FadCashReq;
import com.csnt.scdp.bizmodules.entity.fad.FadInvoice;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.DAOHelper;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Reese on 2015/10/14.
 */
@Scope("singleton")
@Controller("certificate-fadCertificateLoad")
@Transactional
public class FadCertificateLoadAction extends CommonLoadAction implements IAction {

    private static ILogTracer tracer = LogTracerFactory.getInstance(FadCertificateLoadAction.class);
    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map result = new HashMap<>();
        String fadCertificateUuid = certificateManager.isNullReturnEmpty(inMap.get("fadCertificateUuid"));

        String ncState = "";
        String deficitCertifiState = "";
        String copyAddState = "";
        String mergeSplitState = "";

        //common_query_load
        List<String> lstParam = new ArrayList<>();
        lstParam.add(fadCertificateUuid);
        DAOMeta daoMeta = DAOHelper.getDAO("certificate-dao", "common_query_load", lstParam);
        daoMeta.setNeedFilter(false);
        List fadCertificateList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (fadCertificateList.size() > 0) {
            Map fadCertificate = (Map) fadCertificateList.get(0);
            ncState = certificateManager.isNullReturnEmpty(fadCertificate.get("ncState"));
            deficitCertifiState = certificateManager.isNullReturnEmpty(fadCertificate.get("deficitCertifiState"));
            copyAddState = certificateManager.isNullReturnEmpty(fadCertificate.get("copyAddState"));
            mergeSplitState = certificateManager.isNullReturnEmpty(fadCertificate.get("mergeSplitState"));
        }

        result.put("ncState", ncState);
        result.put("deficitCertifiState", deficitCertifiState);
        result.put("copyAddState", copyAddState);
        result.put("mergeSplitState", mergeSplitState);
        return result;
    }
}
