package com.csnt.scdp.bizmodules.modules.scm.balanceofcontract.action;

import com.csnt.scdp.bizmodules.modules.scm.balanceofcontract.services.intf.BalanceofcontractManager;
import com.csnt.scdp.framework.commonaction.CommonReportQueryAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Map;

/**
 * Description:  QueryAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-29 09:17:26
 */

@Scope("singleton")
@Controller("purchaseprogress-query")
@Transactional
public class QueryProgressAction extends CommonReportQueryAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(QueryProgressAction.class);

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
		Map out = super.perform(inMap);
		//Do After
		return out;
	}
}
