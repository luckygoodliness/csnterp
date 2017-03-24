package com.csnt.scdp.bizmodules.modules.scm.scmcontractchange.action;

import com.csnt.scdp.bizmodules.entityattributes.scm.ScmContractChangeAttribute;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.scm.scmcontractchange.services.intf.ScmcontractchangeManager;

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
 * @timestamp 2015-09-26 18:01:19
 */

@Scope("singleton")
@Controller("scmcontractchange-exportxls")
@Transactional
public class ExportXlsAction extends CommonExportXlsAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(ExportXlsAction.class);

	@Resource(name = "scmcontractchange-manager")
	private ScmcontractchangeManager scmcontractchangeManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
		StringBuffer selfSql = new StringBuffer();
		selfSql.append(" 1=1 ");
		Map condMap = (Map) inMap.get("queryConditions");
		Object scmContractCode = condMap.get("scmContractCode");
		condMap.remove("scmContractCode");
		if(StringUtil.isNotEmpty(scmContractCode)){
			selfSql.append(" and (select sc.scm_contract_code  from scm_contract sc where sc.uuid = scm_contract_id) like '%"+scmContractCode+"%' ");
		}
		Map map = new HashMap();
		map.put("selfconditions", selfSql.toString());
		inMap.put(CommonAttribute.QUERY_EXTRA_PARAMS, map);
		Map out = super.perform(inMap);
		//Do After
		return out;
	}
}
