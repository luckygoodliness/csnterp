package com.csnt.scdp.bizmodules.modules.prm.prmbilling.action;

import com.csnt.scdp.bizmodules.entityattributes.prm.PrmBillingDetailAttribute;
import com.csnt.scdp.bizmodules.modules.prm.prmbilling.services.intf.PrmbillingManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.CommonExportXlsAction;
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
 * Description:  ExportXlsAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 17:35:07
 */

@Scope("singleton")
@Controller("prmbilling-exportxls")
@Transactional
public class ExportXlsAction extends CommonExportXlsAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(ExportXlsAction.class);

	@Resource(name = "prmbilling-manager")
	private PrmbillingManager prmbillingManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		Map queryExtraMap = (Map) inMap.get(CommonAttribute.QUERY_EXTRA_PARAMS);
		Map queryConditions = (Map) inMap.get("queryConditions");
		if (queryExtraMap == null) {
			queryExtraMap = new HashMap();
			inMap.put(CommonAttribute.QUERY_EXTRA_PARAMS, queryExtraMap);
		}
		StringBuffer sb = new StringBuffer(" 1=1 ");
		String detailContent = (String) queryConditions.remove(PrmBillingDetailAttribute.CONTENT);
		if (StringUtil.isNotEmpty(detailContent)) {
			//结项的项目要过滤掉
			sb.append(" and exists (select 1 from prm_billing_detail pbd where pb.uuid=pbd.prm_billing_id and pbd" +
					".content like '%" + StringUtil.escapeQueueStr(detailContent.trim()) + "%')");
		}
		queryExtraMap.put("selfconditions", sb.toString());
		Map out = super.perform(inMap);
		//Do After
		return out;
	}
}
