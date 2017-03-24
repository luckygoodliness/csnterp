package com.csnt.scdp.bizmodules.modules.prm.projectmain.action;

import com.csnt.scdp.bizmodules.modules.prm.projectmain.services.intf.ProjectmainManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.CommonQueryAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.helper.UserHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:  QueryAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 13:30:43
 */

@Scope("singleton")
@Controller("inner-project-query")
@Transactional
public class InnerProjectQueryAction extends CommonQueryAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(InnerProjectQueryAction.class);

    @Resource(name = "projectmain-manager")
    private ProjectmainManager projectmainManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map queryExtraMap = (Map) inMap.get(CommonAttribute.QUERY_EXTRA_PARAMS);
        if (queryExtraMap == null) {
            queryExtraMap = new HashMap();
            inMap.put(CommonAttribute.QUERY_EXTRA_PARAMS, queryExtraMap);
        }
        StringBuffer sb = new StringBuffer(" 1=1 ");
        if ("1".equals(queryExtraMap.get("filterCurrentDept"))) {
            sb.append(" and t1.inner_supplier ='" + UserHelper.getOrgCode() + "' ");
        }
        queryExtraMap.put("selfconditions", sb.toString());
        Map out = super.perform(inMap);
        //Do After
        return out;
    }
}
