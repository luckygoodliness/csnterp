package com.csnt.scdp.bizmodules.modules.scm.scmsaecase.action;

import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.sysmodules.helper.FMCodeHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.scm.scmsaecase.services.intf.ScmsaecaseManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Map;

/**
 * Description:  LoadAction
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-08-16 15:17:56
 */

@Scope("singleton")
@Controller("scmsaecase-load")
@Transactional
public class LoadAction extends CommonLoadAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

	@Resource(name = "scmsaecase-manager")
	private ScmsaecaseManager scmsaecaseManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
		Map out = super.perform(inMap);
		Map scmSaeCaseMap = (Map) out.get("scmSaeCaseDto");

		// 打分项类型
		Object caseType = scmSaeCaseMap.get("caseType");
		if (caseType != null) {
			String caseTypeDesc = FMCodeHelper.getDescByCode(caseType.toString(), "SCM_SAE_CASE_TYPE");
			scmSaeCaseMap.put("caseType", caseTypeDesc);
		}

		//Do After
		return out;
	}
}
