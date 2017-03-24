package com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.action;

import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.MapUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.services.intf.PrmprojectmaincManager;

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
 * @timestamp 2015-09-27 15:28:19
 */

@Scope("singleton")
@Controller("prmprojectmainc-exportxls")
@Transactional
public class ExportXlsAction extends CommonExportXlsAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(ExportXlsAction.class);

	@Resource(name = "prmprojectmainc-manager")
	private PrmprojectmaincManager prmprojectmaincManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
        Map queryExtraMap = (Map) inMap.get(CommonAttribute.QUERY_EXTRA_PARAMS);
        if (queryExtraMap == null) {
            queryExtraMap = new HashMap();
            inMap.put(CommonAttribute.QUERY_EXTRA_PARAMS, queryExtraMap);
        }
        StringBuffer sb = new StringBuffer(" 1=1 ");
        String customerId = (String) MapUtil.getByKeyPath(inMap, "queryConditions.customerId");
        if (StringUtil.isNotEmpty(customerId)) {
            sb.append(" and exists(select 1 from prm_contract_detail_c t22 " +
                    "where t22.prm_project_main_c_id=t1.uuid " +
                    "and t22.customer_id='" + customerId + "') ");
        }
        queryExtraMap.put("selfconditions", sb.toString());
        Map out = super.perform(inMap);
        //Do After
        return out;
	}
}
