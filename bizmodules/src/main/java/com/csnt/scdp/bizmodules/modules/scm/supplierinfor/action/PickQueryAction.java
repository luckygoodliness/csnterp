package com.csnt.scdp.bizmodules.modules.scm.supplierinfor.action;

import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.services.intf.SupplierinforManager;
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

/**
 * Description:  QueryAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-24 16:02:00
 */

@Scope("singleton")
@Controller("picksupplierinfor-query")
@Transactional
public class PickQueryAction extends CommonQueryAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(PickQueryAction.class);

    @Resource(name = "supplierinfor-manager")
    private SupplierinforManager supplierinforManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        StringBuffer selfSql = new StringBuffer();
      selfSql.append(" 1=1 ");
        //Do before
        Map condMap = (Map) inMap.get("queryConditions");
        Object printFalg = condMap.remove("uuid");
        Object changeType = condMap.remove("changeType");
        if (StringUtil.isNotEmpty(printFalg)){
            selfSql.append(" and t.uuid not in (" + printFalg + ")");
        }
        if (StringUtil.isNotEmpty(changeType)){
            if (changeType.equals("0")) {
                selfSql.append(" and t.supplier_Genre <> 4" );
            }
            else if(changeType.equals("1")){
                selfSql.append(" and t.supplier_Genre = 4" );
            }
        }
        Map map = new HashMap();
        map.put("selfconditions", selfSql.toString());
        inMap.put(CommonAttribute.QUERY_EXTRA_PARAMS, map);
        Map out = super.perform(inMap);
        //Do After
        return out;
    }
}
