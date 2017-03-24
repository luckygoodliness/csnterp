package com.csnt.scdp.bizmodules.modules.fad.supplierinfo.action;

import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.fad.supplierinfo.services.intf.SupplierinfoManager;

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
 * @timestamp 2015-12-10 13:50:35
 */

@Scope("singleton")
@Controller("supplierinfo-query")
@Transactional
public class QueryAction extends CommonQueryAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(QueryAction.class);

	@Resource(name = "supplierinfo-manager")
	private SupplierinfoManager supplierinfoManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
		Map out = super.perform(inMap);
		//Do After
		return out;
	}
}
