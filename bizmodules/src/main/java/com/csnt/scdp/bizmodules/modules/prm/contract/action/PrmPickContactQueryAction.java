package com.csnt.scdp.bizmodules.modules.prm.contract.action;

import com.csnt.scdp.bizmodules.modules.prm.contract.services.intf.ContractManager;
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
 * @timestamp 2015-09-23 09:57:20
 */

@Scope("singleton")
@Controller("prm-pickcontract-query")
@Transactional
public class PrmPickContactQueryAction extends CommonQueryAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(PrmPickContactQueryAction.class);

    @Resource(name = "contract-manager")
    private ContractManager contractManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map queryExtraParam = (Map) inMap.get(CommonAttribute.QUERY_EXTRA_PARAMS);
        if (queryExtraParam == null) {
            queryExtraParam = new HashMap();
            inMap.put(CommonAttribute.QUERY_EXTRA_PARAMS, queryExtraParam);
        }
        StringBuffer selfSql = new StringBuffer();
        selfSql.append(" 1=1 ");
        String pickFn = (String) queryExtraParam.get("pickModule");
        if ("pickForProject".equals(pickFn)) {
            String mainCUuid = (String) queryExtraParam.get("mainCUuid");
            String mainUuid = (String) queryExtraParam.get("mainUuid");
            String exclusion = (String) queryExtraParam.get("exclusion");

            String tmpM = " select distinct  d.prm_contract_id\n" +
                    "  from prm_project_main m, prm_contract_detail d\n" +
                    " where m.uuid = d.prm_project_main_id and (m.is_void is null or m.is_void = '0')";

            String tmpC = " select distinct dc.prm_contract_id\n" +
                    "  from prm_project_main_c mc, prm_contract_detail_c dc\n" +
                    " where mc.uuid = dc.prm_project_main_c_id\n" +
                    "   and mc.state in ('0', '1','5', '9')" +
                    " and (mc.is_void is null or mc.is_void = '0') ";
            if (StringUtil.isNotEmpty(mainUuid)) {
                tmpM = tmpM + " and m.uuid<>'" + mainUuid + "'";
                tmpC = tmpC + " and mc.prm_project_main_id<>'" + mainUuid + "'";
            }
            if (StringUtil.isNotEmpty(mainCUuid)) {
                tmpC = tmpC + " and mc.uuid<>'" + mainCUuid + "'";
            }
            selfSql.append("  and not exists(select 1 from (" + tmpM + " union " + tmpC + ") t12 " +
                    "where t12.prm_contract_id=t1.uuid)\n");

            selfSql.append("  and not exists(select 1 from prm_contract_c t13 " +
                    "where  t13.state in ('0', '1', '5','9') and t13.prm_contract_id is not null and t13.prm_contract_id=t1.uuid)\n");

            if (StringUtil.isNotEmpty(exclusion)) {
                selfSql.append(" and t1.uuid not in (" + exclusion + ")");
            }
        } else if ("pickForContractC".equals(pickFn)) {
            selfSql.append(" and not exists( select 1 from (select d.prm_contract_id\n" +
                    "  from prm_project_main_c c, prm_contract_detail_c d\n" +
                    " where c.uuid = d.prm_project_main_c_id\n" +
                    "   and c.state in ('0', '1', '5','9')) mc where mc.prm_contract_id = t1.uuid)" +
                    " and not exists(select 1 from prm_contract_c r\n" +
                    "   where r.state in ('0','1','5','9') and r.prm_contract_id = t1.uuid)" +
                    " and not exists (select 1  from prm_contract_detail cd, prm_project_main m\n" +
                    "         where cd.prm_project_main_id = m.uuid\n" +
                    "           and m.state = '1' and cd.prm_contract_id = t1.uuid) ");
        } else if ("pickForContractRemove".equals(pickFn)) {
            selfSql.append("  and not exists (select 1\n" +
                    "          from prm_contract_c c\n" +
                    "         where t1. uuid = c.prm_contract_id\n" +
                    "           and c.state in ('0', '1','5','9'))\n" +
                    "           and  not exists(select 1  from  prm_contract_detail d where d.prm_contract_id =t1.uuid)" +
                    "       and not exists (select 1\n" +
                    "          from (select dc.prm_contract_id\n" +
                    "                  from prm_project_main_c mc, prm_contract_detail_c dc\n" +
                    "                 where mc.uuid = dc.prm_project_main_c_id and mc.state in ('0', '1', '5','9')) md\n" +
                    "         where t1.uuid = md.prm_contract_id) ");
        }
        queryExtraParam.put("selfconditions", selfSql.toString());
        Map out = super.perform(inMap);
        return out;
    }
}
