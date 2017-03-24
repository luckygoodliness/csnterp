package com.csnt.scdp.bizmodules.modules.fad.certificatesetrule.action;

import com.csnt.scdp.bizmodules.entityattributes.nonprm.NonProjectSubjectSubjectAttribute;
import com.csnt.scdp.bizmodules.modules.common.action.ErpLoadAction;
import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
import com.csnt.scdp.bizmodules.modules.nonprm.subjectsubject.dto.NonProjectSubjectSubjectDto;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.helper.DtoHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.fad.certificatesetrule.services.intf.CertificatesetruleManager;
import com.csnt.scdp.sysmodules.entity.ScdpOrg;
import com.csnt.scdp.bizmodules.entity.fad.FadRtfreevalueView;
import com.csnt.scdp.bizmodules.entity.fad.FadAccsubjRtfreevalue;
import com.csnt.scdp.sysmodules.entityattributes.ScdpOrgAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadRtfreevalueViewAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadAccsubjRtfreevalueAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadProjectSetRuleAttribute;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import com.csnt.scdp.framework.core.persistence.PersistenceFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  LoadAction
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-09-23 21:16:46
 */

@Scope("singleton")
@Controller("certificatesetrule-load")
@Transactional
public class LoadAction extends ErpLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

    @Resource(name = "certificatesetrule-manager")
    private CertificatesetruleManager certificatesetruleManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        //Do After
        return out;
    }
}
