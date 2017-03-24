package com.csnt.scdp.bizmodules.modules.prm.finalestimate.action;

import com.csnt.scdp.bizmodules.modules.prm.finalestimate.services.intf.FinalestimateManager;
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
 * @timestamp 2015-09-23 20:02:44
 */

@Scope("singleton")
@Controller("finalestimate-query")
@Transactional
public class QueryAction extends CommonQueryAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(QueryAction.class);

	@Resource(name = "finalestimate-manager")
	private FinalestimateManager finalestimateManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
		Map queryExtraMap = (Map) inMap.get(CommonAttribute.QUERY_EXTRA_PARAMS);
		if (queryExtraMap == null) {
			queryExtraMap = new HashMap();
			inMap.put(CommonAttribute.QUERY_EXTRA_PARAMS, queryExtraMap);
		}

		Map queryConditions = (Map) inMap.get("queryConditions");
		if (queryConditions == null) {
			queryConditions = new HashMap();
			inMap.put("queryConditions", queryConditions);
		}

		Object taxCorrected = queryConditions.get("isCorrected");
		StringBuffer sb = new StringBuffer(" 1=1 ");
		if (StringUtil.isNotEmpty(taxCorrected)) {
			queryConditions.put("squareType", "1");
			if ("1".equals(taxCorrected.toString())) {
				sb.append(" and (f.tax_correction is not null) ");
			} else {
				sb.append(" and (f.tax_correction is null) ");

			}
		}

		queryExtraMap.put("selfconditions", sb.toString());
		Map out = super.perform(inMap);
		//Do After
		return out;
	}
}
