package com.csnt.scdp.bizmodules.modules.fad.certificate.action;

import com.csnt.scdp.bizmodules.modules.common.action.ErpLoadAction;
import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description:  LoadAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-10-16 10:07:48
 */

@Scope("singleton")
@Controller("certificate-load")
@Transactional
public class LoadAction extends ErpLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        //Do After
        Map fadCertificateMap = (Map) out.get("fadCertificateDto");
        List<Map> fadCertificateDetailList = new ArrayList<Map>();
        if (fadCertificateMap.get("fadCertificateDetailDto") != null) {
            fadCertificateDetailList = (List) fadCertificateMap.get("fadCertificateDetailDto");
        }
        if (fadCertificateDetailList != null) {
            for (int i = 0; i < fadCertificateDetailList.size(); i++) {
                Map fadCertificateDetailMap = (Map) fadCertificateDetailList.get(i);
                if (certificateManager.isNullReturnEmpty(fadCertificateDetailMap.get("debtorOrCreditor")).equals("0")) {
                    fadCertificateDetailMap.put("debtorOrCreditor", "借");
                } else if (certificateManager.isNullReturnEmpty(fadCertificateDetailMap.get("debtorOrCreditor")).equals("1")) {
                    fadCertificateDetailMap.put("debtorOrCreditor", "贷");
                }
            }
        }
        certificateManager.setExtraData(out);
        return out;
    }
}
