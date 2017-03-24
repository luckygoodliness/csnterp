package com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.action;

import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.services.intf.PrmpurchasereqManager;

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
 * @timestamp 2015-09-26 15:30:52
 */

@Scope("singleton")
@Controller("prmpurchasereq-exportxls")
@Transactional
public class ExportXlsAction extends CommonExportXlsAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(ExportXlsAction.class);

	@Resource(name = "prmpurchasereq-manager")
	private PrmpurchasereqManager prmpurchasereqManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {

		StringBuffer selfSql = new StringBuffer();
		selfSql.append(" 1=1 ");
		Map condMap = (Map) inMap.get("queryConditions");
		Object departmentCode = condMap.get("departmentCode1");
		Object projectId = condMap.get("qryProjectId");
		Object state = condMap.get("state");
		Object purchaseReqNoQ = condMap.get("purchaseReqNoQ");

		if (StringUtil.isNotEmpty(departmentCode)) {
			selfSql.append(" AND REQ.DEPARTMENT_CODE = '" + departmentCode + "'");
		}
		if (StringUtil.isNotEmpty(projectId)) {
			selfSql.append(" AND REQ.PRM_PROJECT_MAIN_ID = '" + projectId + "'");
		}
		if (StringUtil.isNotEmpty(state)) {
			selfSql.append(" AND REQ.STATE = '" + state + "'");
		}
		if (StringUtil.isNotEmpty(purchaseReqNoQ)) {
			selfSql.append(" AND REQ.PURCHASE_REQ_NO LIKE '%" + purchaseReqNoQ + "%'");
		}
		Map map = new HashMap();
		map.put("selfconditions", selfSql.toString());
		inMap.put(CommonAttribute.QUERY_EXTRA_PARAMS, map);
		Map out = super.perform(inMap);

		//Do After
		return out;
	}
}
