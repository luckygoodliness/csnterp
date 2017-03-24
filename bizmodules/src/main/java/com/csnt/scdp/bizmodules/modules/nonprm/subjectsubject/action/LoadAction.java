package com.csnt.scdp.bizmodules.modules.nonprm.subjectsubject.action;

import com.csnt.scdp.bizmodules.modules.common.action.ErpLoadAction;
import com.csnt.scdp.bizmodules.modules.nonprm.subjectsubject.services.intf.SubjectsubjectManager;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Map;

/**
 * Description:  LoadAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 18:59:14
 */

@Scope("singleton")
@Controller("subjectsubject-load")
@Transactional
public class LoadAction extends ErpLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

    @Resource(name = "subjectsubject-manager")
    private SubjectsubjectManager subjectsubjectManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        //Do After
        Map dtoMap = (Map) out.get("nonProjectSubjectSubjectDto");
        if (dtoMap != null) {
            //2.设置部门
            String officeId = (String) dtoMap.get("officeId");
            if (StringUtil.isNotEmpty(officeId)) {
                dtoMap.put("officeIdDesc", OrgHelper.getOrgNameByCode(officeId));
            }
        }
        return out;
    }
}
