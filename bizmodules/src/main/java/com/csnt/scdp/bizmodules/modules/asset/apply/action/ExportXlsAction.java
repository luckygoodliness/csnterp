package com.csnt.scdp.bizmodules.modules.asset.apply.action;

import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.asset.apply.services.intf.ApplyManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Map;

/**
 * Description:  ExportXlsAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 20:06:31
 */

@Scope("singleton")
@Controller("apply-exportxls")
@Transactional
public class ExportXlsAction extends CommonExportXlsAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(ExportXlsAction.class);

	@Resource(name = "apply-manager")
	private ApplyManager applyManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
		Map out = super.perform(inMap);
		//Do After
		return out;
	}
}
