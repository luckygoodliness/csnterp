package com.csnt.scdp.bizmodules.modules.asset.transfer.action;

import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.asset.transfer.services.intf.TransferManager;

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
 * @timestamp 2015-09-23 18:08:32
 */

@Scope("singleton")
@Controller("transfer-query")
@Transactional
public class QueryAction extends CommonQueryAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(QueryAction.class);

	@Resource(name = "transfer-manager")
	private TransferManager transferManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
		Map out = super.perform(inMap);
		//Do After
		return out;
	}
}
