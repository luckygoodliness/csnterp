package com.csnt.scdp.bizmodules.modules.fad.certificatesetrule.action;

import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.fad.certificatesetrule.services.intf.CertificatesetruleManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:  QueryAction
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-09-23 21:16:46
 */

@Scope("singleton")
@Controller("projectsetrule-query")
@Transactional
public class ProjectSetRuleQueryAction extends CommonQueryAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ProjectSetRuleQueryAction.class);

    @Resource(name = "certificatesetrule-manager")
    private CertificatesetruleManager certificatesetruleManager;

    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        StringBuffer condSql = new StringBuffer();
        condSql.append(" 1=1 ");
        Map condMap = (Map) inMap.get("queryConditions");
        String offices = certificateManager.isNullReturnEmpty(condMap.get("office"));
        String projectCodes = certificateManager.isNullReturnEmpty(condMap.get("projectCode"));
        condMap.remove("office");
        condMap.remove("projectCode");

        if (offices.length() > 0) {
            condSql.append(" and (");
            String[] officeArr = offices.split("\\|");
            for (int i = 0; i < officeArr.length; i++) {
                String office = certificateManager.isNullReturnEmpty(officeArr[i]);
                if (office.length() > 0) {
                    if (condSql.indexOf("office like '%") == -1) {
                        condSql.append("office like '%" + office + "%'");
                    } else {
                        condSql.append(" or office like '%" + office + "%'");
                    }
                }
            }
            condSql.append(")");
        }

        if (projectCodes.length() > 0) {
            condSql.append(" and (");
            String[] projectCodeArr = projectCodes.split("\\|");
            for (int i = 0; i < projectCodeArr.length; i++) {
                String projectCode = certificateManager.isNullReturnEmpty(projectCodeArr[i]);
                if (projectCode.length() > 0) {
                    if (condSql.indexOf("project_code like '%") == -1) {
                        condSql.append("project_code like '%" + projectCode + "%'");
                    } else {
                        condSql.append(" or project_code like '%" + projectCode + "%'");
                    }
                }
            }
            condSql.append(")");
        }

        Map condmap = new HashMap();
        condmap.put("selfconditions", condSql.toString().replace("and ()", ""));
        inMap.put(CommonAttribute.QUERY_EXTRA_PARAMS, condmap);

        Map out = super.perform(inMap);
        //Do After
        return out;
    }
}
