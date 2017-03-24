package com.csnt.scdp.bizmodules.modules.prm.projectmain.action;

import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainAttribute;
import com.csnt.scdp.bizmodules.modules.prm.projectmain.services.intf.ProjectmainManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.CommonQueryAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.MapUtil;
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
 * @timestamp 2015-09-26 13:30:43
 */

@Scope("singleton")
@Controller("projectmain-query")
@Transactional
public class QueryAction extends CommonQueryAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(QueryAction.class);

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
        Object filterFinishedProject = queryExtraMap.get(PrmProjectMainAttribute.FILTER_FINISHED_PROJECT);
        if (Boolean.TRUE.equals(filterFinishedProject)) {
            //结项的项目要过滤掉
            sb.append(" and not exists(select 1 from prm_final_estimate t21 " +
                    "where (t21.is_void is null or t21.is_void=0) " +
                    "and (t21.square_type='1' or t21.square_type='3')" +
                    "and t21.prm_project_main_id=t1.uuid) ");
        }
        String customerId = (String) MapUtil.getByKeyPath(inMap, "queryConditions.customerId");
        if (StringUtil.isNotEmpty(customerId)) {
            sb.append(" and exists(select 1 from prm_contract_detail t22 " +
                    "where t22.prm_project_main_id=t1.uuid " +
                    "and t22.customer_id='" + customerId + "') ");
        }
        Map condMap = (Map) inMap.get("queryConditions");
        String prmContractId = (String) condMap.get("prmContractId");
        condMap.remove("prmContractId");
        if (StringUtil.isNotEmpty(prmContractId) && prmContractId.trim().length() > 0) {
            sb.append(" and  exists (select 1 from (" +
                    " select c.contract_name, d.prm_project_main_id" +
                    "  from prm_contract c, prm_contract_detail d" +
                    " where c.uuid = d.prm_contract_id" +
                    "   and c.contract_name like '%" + prmContractId + "%') cd" +
                    "   where cd.prm_project_main_id = t1.uuid)");
        }
        queryExtraMap.put("selfconditions", sb.toString());
        Map out = super.perform(inMap);
        //Do After
        return out;
    }
}
