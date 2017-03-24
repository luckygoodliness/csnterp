package com.csnt.scdp.bizmodules.modules.operate.operatekeyprojectsinfo.action;

import com.csnt.scdp.bizmodules.modules.common.action.ErpDeleteAction;
import com.csnt.scdp.bizmodules.modules.operate.operatekeyprojectsinfo.services.intf.OperatekeyprojectsinfoManager;
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
 * Description:  DeleteAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-10-27 17:35:53
 */

@Scope("singleton")
@Controller("operatekeyprojectsinfo-delete")
@Transactional
public class DeleteAction extends ErpDeleteAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(DeleteAction.class);

	@Resource(name = "operatekeyprojectsinfo-manager")
	private OperatekeyprojectsinfoManager operatekeyprojectsinfoManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
		Map out = super.perform(inMap);
		//Do After
		return out;
	}
}
