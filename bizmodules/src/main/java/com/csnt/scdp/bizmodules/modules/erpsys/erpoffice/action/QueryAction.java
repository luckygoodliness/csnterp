package com.csnt.scdp.bizmodules.modules.erpsys.erpoffice.action;

/**
 * Created by fisher on 2015/11/4.
 */

import com.csnt.scdp.bizmodules.modules.erpsys.erpoffice.services.intf.ErpOfficeManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.CommonQueryAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;


@Scope("singleton")
@Controller("erpoffice-query")
@Transactional
public class QueryAction extends CommonQueryAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(QueryAction.class);

    @Resource(name = "erpoffice-manager")
    private ErpOfficeManager erpOfficeManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        StringBuffer selfSql = new StringBuffer();
        selfSql.append(" 1=1 ");
        //Do before
        Map condMap = (Map) inMap.get("queryConditions");
        Object orgCodes = condMap.get("orgCodeS");
        condMap.remove("orgCodeS");

        if (StringUtil.isNotEmpty(orgCodes)) {
            selfSql.append(" and t.org_code not in (" + orgCodes + ")");
        }
        Map map = new HashMap();
        map.put("selfconditions", selfSql.toString());
        inMap.put(CommonAttribute.QUERY_EXTRA_PARAMS, map);

        //Do before
        Map out = super.perform(inMap);
        //Do After
        return out;
    }
}