package com.csnt.scdp.bizmodules.modules.fad.invoice.action;

import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.fad.invoice.services.intf.InvoiceManager;

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
 * @timestamp 2015-09-25 14:13:12
 */

@Scope("singleton")
@Controller("invoice-exportxls")
@Transactional
public class ExportXlsAction extends CommonExportXlsAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(ExportXlsAction.class);

	@Resource(name = "invoice-manager")
	private InvoiceManager invoiceManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
		StringBuffer selfSql = new StringBuffer();
		selfSql.append(" 1=1 ");
		Map condMap = (Map) inMap.get("queryConditions");
		Object projectId = condMap.get("isProject");
		condMap.remove("isProject");
		if(StringUtil.isNotEmpty(projectId)){
			if("1".equals(projectId)){
				selfSql.append(" and prm_project_main_id is not null");
			}else{
				selfSql.append(" and prm_project_main_id is null");
			}
		}
		Map map = new HashMap();
		map.put("selfconditions", selfSql.toString());
		inMap.put(CommonAttribute.QUERY_EXTRA_PARAMS, map);
		Map out = super.perform(inMap);
		//Do After
		return out;
	}
}
