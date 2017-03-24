package com.csnt.scdp.bizmodules.modules.scm.scmpricedatabase.action;

import com.csnt.scdp.bizmodules.modules.scm.supplierlimit.services.intf.SupplierlimitManager;
import com.csnt.scdp.framework.commonaction.crud.CommonQueryAction;
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
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-07-19 15:14:12
 */

@Scope("singleton")
@Controller("scmpricedatabase-scmpricedatabasequery")
@Transactional
public class ScmPriceDatabaseQueryAction extends CommonQueryAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(ScmPriceDatabaseQueryAction.class);

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
		Map out = super.perform(inMap);
		//Do After
		return out;
	}
}
