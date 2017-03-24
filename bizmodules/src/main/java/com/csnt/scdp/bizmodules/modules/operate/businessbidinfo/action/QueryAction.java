package com.csnt.scdp.bizmodules.modules.operate.businessbidinfo.action;

import com.csnt.scdp.bizmodules.modules.operate.businessbidinfo.services.intf.BusinessbidinfoManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.CommonQueryAction;
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
 * Description:  QueryAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-28 18:18:49
 */

@Scope("singleton")
@Controller("businessbidinfo-query")
@Transactional
public class QueryAction extends CommonQueryAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(QueryAction.class);

	@Resource(name = "businessbidinfo-manager")
	private BusinessbidinfoManager businessbidinfoManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
		Map queryExtraMap = (Map) inMap.get(CommonAttribute.QUERY_EXTRA_PARAMS);
		if (queryExtraMap == null) {
			queryExtraMap = new HashMap();
			inMap.put(CommonAttribute.QUERY_EXTRA_PARAMS, queryExtraMap);
		}
		StringBuffer sb = new StringBuffer(" 1=1 ");
		String exclude = (String) queryExtraMap.get("exclude");
		if ("1".equals(exclude)) {
            sb.append(" and bid_result in ('1','2','3') and  not exists (select pc.operate_business_bid_info_id  from prm_contract_c pc  " +
                    "where pc.operate_business_bid_info_id =  bbi.uuid   and (pc.is_void = 0 or  pc.is_void is null)) ");
		}
		queryExtraMap.put("selfconditions", sb.toString());
		Map out = super.perform(inMap);
		//Do After
		return out;
	}
}
