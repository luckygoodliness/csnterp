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
@Controller("contract-query")
@Transactional
public class QueryAction extends CommonQueryAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(QueryAction.class);

	@Resource(name = "contract-manager")
	private ContractManager contractManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {

		Map queryExtraParams= (Map) inMap.get(CommonAttribute.QUERY_EXTRA_PARAMS);
		if(queryExtraParams==null){
			queryExtraParams=new HashMap();
			inMap.put(CommonAttribute.QUERY_EXTRA_PARAMS,queryExtraParams);
		}
		StringBuffer sb=new StringBuffer(" 1=1 ");
		queryExtraParams.put("selfconditions",sb.toString());

		Map out = super.perform(inMap);
		return out;
	}
}
