package com.csnt.scdp.bizmodules.modules.fad.fadinterestrate.action;

import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.ObjectUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.fad.fadinterestrate.services.intf.FadinterestrateManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:  QueryAction
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-08-30 10:37:36
 */

@Scope("singleton")
@Controller("fadinterestrate-query")
@Transactional
public class QueryAction extends CommonQueryAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(QueryAction.class);

	@Resource(name = "fadinterestrate-manager")
	private FadinterestrateManager fadinterestrateManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		StringBuffer condSql = new StringBuffer();
		condSql.append("1 = 1");
		Map queryConditions = (Map) inMap.get("queryConditions");
		String validityDateFromStr = StringUtil.replaceNull(queryConditions.get("validityDateFrom"));
		String validityDateToStr = StringUtil.replaceNull(queryConditions.get("validityDateTo"));

		String validityDateFrom = validityDateFromStr.replaceAll("T","");
		String validityDateTo = validityDateToStr.replaceAll("T","");
		if (StringUtil.isNotEmpty(validityDateFrom))
			condSql.append(" and validity_date_from >= to_date('" + validityDateFrom + "', 'YYYY/MM/DD HH24:MI:SS')");
		if (StringUtil.isNotEmpty(validityDateTo))
			condSql.append(" and validity_date_to <= to_date('" + validityDateTo + "', 'YYYY/MM/DD HH24:MI:SS')");

		queryConditions.remove("validityDateFrom");
		queryConditions.remove("validityDateTo");

		Map<String, String> extraMap = new HashMap<String, String>();
		extraMap.put("selfconditions", condSql.toString());
		inMap.put(CommonAttribute.QUERY_EXTRA_PARAMS, extraMap);
		//Do before
		Map out = super.perform(inMap);
		//Do After
		return out;
	}
}
