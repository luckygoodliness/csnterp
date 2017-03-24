package com.csnt.scdp.bizmodules.modules.scm.materialclass.action;

import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.scm.materialclass.services.intf.MaterialclassManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Map;

/**
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 11:15:17
 */

@Scope("singleton")
@Controller("materialclass-add")
@Transactional
public class AddAction extends CommonAddAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);

	@Resource(name = "materialclass-manager")
	private MaterialclassManager materialclassManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
//		String state = inMap.get("");
		Map out = super.perform(inMap);
		//Do After
		return out;
	}
}
