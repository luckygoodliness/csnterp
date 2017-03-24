package com.csnt.scdp.bizmodules.modules.asset.card.action;

import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.asset.card.services.intf.CardManager;

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
 * @timestamp 2015-09-22 10:18:39
 */

@Scope("singleton")
@Controller("card-query")
@Transactional
public class QueryAction extends CommonQueryAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(QueryAction.class);

    @Resource(name = "card-manager")
    private CardManager cardManager;

    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {

        //Do before
        if (
                certificateManager.isNullReturnEmpty(inMap.get("layoutFile")).equals("card-query-layout.xml")
                        ||
                        certificateManager.isNullReturnEmpty(inMap.get("layoutFile")).equals("jcard-query-layout.xml")
                        ||
                        certificateManager.isNullReturnEmpty(inMap.get("layoutFile")).equals("jcard2-query-layout.xml")
                        ||
                        certificateManager.isNullReturnEmpty(inMap.get("layoutFile")).equals("jcard3-query-layout.xml")
                ) {

            Map condmap = new HashMap();
            Map queryConditions = (Map) inMap.get("queryConditions");
            {
                String cardNotUse = StringUtil.replaceNull(queryConditions.get("cardNotUse"));
                if ("1".equals(cardNotUse)) {
                    StringBuffer condSql = new StringBuffer();
                    condSql.append(" a.status <> 'P' and a.state = 2 and not exists (select 1 from asset_discard_apply t where t.state <> '3' and t.card_uuid = a.uuid)");
                    condmap.put("cardNotUse", condSql.toString());
                } else {
                    condmap.put("cardNotUse", " 1=1");
                }
                queryConditions.remove("cardNotUse");
            }
            /*{
                String localValueMin = certificateManager.isNullReturnEmpty(queryConditions.get("localValueMin"));
                String localValueMax = certificateManager.isNullReturnEmpty(queryConditions.get("localValueMax"));
                if (localValueMin.length() > 0 || localValueMax.length() > 0) {
                    StringBuffer condSql = new StringBuffer();
                    condSql.append(" local_value between " + certificateManager.isEmptyReturnZero(localValueMin) + " and " + certificateManager.isEmptyReturnZero(localValueMax));
                    condmap.put("selfconditions", condSql.toString());
                } else {
                    condmap.put("selfconditions", " 1=1");
                }
                queryConditions.remove("localValueMin");
                queryConditions.remove("localValueMax");
            }*/
            inMap.put(CommonAttribute.QUERY_EXTRA_PARAMS, condmap);
        }

        //Do After
        Map out = super.perform(inMap);
        return out;
    }
}
