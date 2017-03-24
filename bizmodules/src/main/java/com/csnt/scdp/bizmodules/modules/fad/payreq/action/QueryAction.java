package com.csnt.scdp.bizmodules.modules.fad.payreq.action;

import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.MapUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.fad.payreq.services.intf.PayreqManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Map;

/**
 * Description:  QueryAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 17:42:24
 */

@Scope("singleton")
@Controller("payreq-query")
@Transactional
public class QueryAction extends CommonQueryAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(QueryAction.class);

    @Resource(name = "payreq-manager")
    private PayreqManager payreqManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {

        Map condMap = (Map) inMap.get("queryConditions");
        if (MapUtil.isNotEmpty(condMap) && condMap.containsKey("yearQ")) {
            //前台JCombo对返回年份是Long类型，底层强转报错，将Long转为String传入
            Object year = condMap.get("yearQ");
            condMap.put("yearQ", year.toString());
        }
        //Do before
        Map out = super.perform(inMap);
        //Do After
        return out;
    }
}
