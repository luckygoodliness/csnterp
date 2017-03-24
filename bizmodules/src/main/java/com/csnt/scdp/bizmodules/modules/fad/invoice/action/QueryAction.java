package com.csnt.scdp.bizmodules.modules.fad.invoice.action;

import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.fad.invoice.services.intf.InvoiceManager;

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
 * @timestamp 2015-09-25 14:13:12
 */

@Scope("singleton")
@Controller("invoice-query")
@Transactional
public class QueryAction extends CommonQueryAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(QueryAction.class);

    @Resource(name = "invoice-manager")
    private InvoiceManager invoiceManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        StringBuffer selfSql = new StringBuffer();
        selfSql.append(" 1=1 ");
        Map condMap = (Map) inMap.get("queryConditions");
        Object projectId = condMap.remove("isProject");
        Object officeId = condMap.remove("officeId_Q");

        if (StringUtil.isNotEmpty(projectId)) {
            if ("1".equals(projectId)) {
                selfSql.append(" and prm_project_main_id is not null");
            } else {
                selfSql.append(" and prm_project_main_id is null");
            }
        }
        if (StringUtil.isNotEmpty(officeId)) {
            selfSql.append(" and office_Id like '%"+officeId+"%' ");

        }
        Map map = new HashMap();
        map.put("selfconditions", selfSql.toString());
        inMap.put(CommonAttribute.QUERY_EXTRA_PARAMS, map);
        Map out = super.perform(inMap);
        //Do After
        return out;
    }
}
