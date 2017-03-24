package com.csnt.scdp.bizmodules.modules.nonprm.nonprmpurchasereq.action;

import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.nonprm.nonprmpurchasereq.services.intf.NonprmpurchasereqManager;

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
 * @timestamp 2015-11-19 14:00:14
 */

@Scope("singleton")
@Controller("nonprmpurchasereq-query")
@Transactional
public class QueryAction extends CommonQueryAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(QueryAction.class);

    @Resource(name = "nonprmpurchasereq-manager")
    private NonprmpurchasereqManager nonprmpurchasereqManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        StringBuffer selfSql = new StringBuffer();
        selfSql.append(" 1=1 ");

        Map condMap = (Map) inMap.get("queryConditions");
        Object departmentCode = condMap.remove("departmentCode1");
        Object subjectUuid = condMap.get("subjectUuid");
        Object state = condMap.get("state");
        Object subjectName = condMap.get("subjectName");
        Object purchaseReqNoQ = condMap.get("purchaseReqNoQ");

        if (StringUtil.isNotEmpty(departmentCode)) {
            selfSql.append(" AND REQ.DEPARTMENT_CODE = '" + departmentCode + "'");
        }
        if (StringUtil.isNotEmpty(subjectUuid)) {
            selfSql.append(" AND REQ.BUGDET_ID = '" + subjectUuid + "'");
        }
        if (StringUtil.isNotEmpty(state)) {
            selfSql.append(" AND REQ.STATE = '" + state + "'");
        }
        if (StringUtil.isNotEmpty(purchaseReqNoQ)) {
            selfSql.append(" AND REQ.PURCHASE_REQ_NO LIKE '%" + purchaseReqNoQ + "%'");
        }

        Map map = new HashMap();
        map.put("selfconditions", selfSql.toString());

        selfSql = new StringBuffer();
        selfSql.append(" 1=1 ");
        if (StringUtil.isNotEmpty(subjectName)) {
            selfSql.append(" AND SUB.FINANCIAL_SUBJECT LIKE '%" + subjectName + "%'");
        }
        map.put("selfconditions2", selfSql.toString());

        inMap.put(CommonAttribute.QUERY_EXTRA_PARAMS, map);
        Map out = super.perform(inMap);
        //Do After
        return out;
    }
}
