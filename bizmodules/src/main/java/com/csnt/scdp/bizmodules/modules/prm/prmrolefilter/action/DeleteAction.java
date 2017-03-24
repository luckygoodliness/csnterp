package com.csnt.scdp.bizmodules.modules.prm.prmrolefilter.action;

import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.prm.prmrolefilter.services.intf.PrmrolefilterManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Map;

/**
 * Description:  DeleteAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-11-16 13:30:29
 */

@Scope("singleton")
@Controller("prmrolefilter-delete")
@Transactional
public class DeleteAction extends CommonDeleteAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(DeleteAction.class);

	@Resource(name = "prmrolefilter-manager")
	private PrmrolefilterManager prmrolefilterManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
		Map out = super.perform(inMap);
		//Do After
		return out;
	}
}
