package com.csnt.scdp.bizmodules.modules.operate.bidcontractplan.action;

import com.csnt.scdp.bizmodules.modules.operate.bidcontractplan.services.intf.BidcontractplanManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.CommonExportXlsAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:  ExportXlsAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-11-05 10:26:28
 */

@Scope("singleton")
@Controller("bidcontractplan-exportxls")
@Transactional
public class ExportXlsAction extends CommonExportXlsAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ExportXlsAction.class);

    @Resource(name = "bidcontractplan-manager")
    private BidcontractplanManager bidcontractplanManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map queryExtraMap = (Map) inMap.get(CommonAttribute.QUERY_EXTRA_PARAMS);
        if (queryExtraMap == null) {
            queryExtraMap = new HashMap();
            inMap.put(CommonAttribute.QUERY_EXTRA_PARAMS, queryExtraMap);
        }

        StringBuffer selfSql = new StringBuffer();
        selfSql.append(" 1=1 ");
        Map map = new HashMap();
        String exclude = (String) queryExtraMap.get("exclude");
        if ("1".equals(exclude)) {
            selfSql.append(" and NOT EXISTS (SELECT PC.OPERATE_BUSINESS_BID_INFO_ID  FROM PRM_CONTRACT_C PC  " +
                    "WHERE PC.OPERATE_BUSINESS_BID_INFO_ID =  S.UUID   AND (PC.IS_VOID = 0 OR  PC.IS_VOID IS NULL)) ");
        }
        map.put("selfconditions", selfSql.toString());
        inMap.put(CommonAttribute.QUERY_EXTRA_PARAMS, map);

        Map out = super.perform(inMap);
        //Do After
        return out;
    }
}
