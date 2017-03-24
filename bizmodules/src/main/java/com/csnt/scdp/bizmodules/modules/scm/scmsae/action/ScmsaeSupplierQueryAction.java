package com.csnt.scdp.bizmodules.modules.scm.scmsae.action;

import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.scm.scmsae.services.intf.ScmsaeManager;

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
 * @timestamp 2016-08-23 19:38:15
 */

@Scope("singleton")
@Controller("pick-scmsaesupplier-query")
@Transactional
public class ScmsaeSupplierQueryAction extends CommonQueryAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(QueryAction.class);

//    @Resource(name = "scmsae-manager")
//    private ScmsaeManager scmsaeManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        StringBuffer selfSql = new StringBuffer();
        selfSql.append(" 1=1 ");
        Map condMap = (Map) inMap.get("queryConditions");
        Object connStr = condMap.remove("connStr");
        Object accumulativeRatioFrom = condMap.remove("accumulativeRatioFrom");
        Object accumulativeRatioTo = condMap.remove("accumulativeRatioTo");
        Object quantity = condMap.remove("quantity");
        Object comprehensive = condMap.remove("comprehensive");
        Object effectiveDateFrom = condMap.remove("effectiveDatefieldFrom");
        Object effectiveDateTo = condMap.remove("effectiveDatefieldTo");
        StringBuffer effectiveDateSql = new StringBuffer();
        effectiveDateSql.append(" 1=1 ");
        if (StringUtil.isNotEmpty(effectiveDateFrom)){
            effectiveDateSql.append(" and effective_Date >= to_date (replace('"+effectiveDateFrom.toString()+"','T',' '),'yyyy-mm-dd hh24:mi:ss')");
        }
        if (StringUtil.isNotEmpty(effectiveDateTo)){
            effectiveDateSql.append(" and effective_Date <= to_date (replace('"+effectiveDateTo.toString()+"','T',' '),'yyyy-mm-dd hh24:mi:ss')");
        }
        if (StringUtil.isNotEmpty(connStr)){
            selfSql.append(" and not exists (select 1 from dual where supplier_id in ("+connStr.toString()+") )");
        }
        if (StringUtil.isNotEmpty(accumulativeRatioFrom)){
            selfSql.append(" and accumulative_ratio >= '"+accumulativeRatioFrom+"' ");
        }
        if (StringUtil.isNotEmpty(accumulativeRatioTo)){
            selfSql.append(" and accumulative_ratio <= '"+accumulativeRatioTo+"' ");
        }
        if (StringUtil.isNotEmpty(quantity)){
            selfSql.append(" and quantity >= '"+quantity+"' ");
        }
        if (StringUtil.isNotEmpty(comprehensive)){
            selfSql.append(" and nvl(comprehensive,0) >= '"+comprehensive+"' ");
        }
        Map map = new HashMap();
        map.put("effectivedate", effectiveDateSql.toString());
        map.put("selfconditions", selfSql.toString());
        inMap.put(CommonAttribute.QUERY_EXTRA_PARAMS, map);

        Map out = super.perform(inMap);
        //Do After
        return out;
    }
}
