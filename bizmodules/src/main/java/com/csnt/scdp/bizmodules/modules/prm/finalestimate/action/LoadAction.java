package com.csnt.scdp.bizmodules.modules.prm.finalestimate.action;

import com.csnt.scdp.bizmodules.modules.common.action.ErpLoadAction;
import com.csnt.scdp.bizmodules.modules.prm.finalestimate.dto.PrmFinalEstimateDto;
import com.csnt.scdp.bizmodules.modules.prm.finalestimate.services.intf.FinalestimateManager;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.dto.BasePojo;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Map;

/**
 * Description:  LoadAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 20:02:44
 */

@Scope("singleton")
@Controller("finalestimate-load")
@Transactional
public class LoadAction extends ErpLoadAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

	@Resource(name = "finalestimate-manager")
	private FinalestimateManager finalestimateManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
		Map out = super.perform(inMap);
		//Do After
		return out;
	}

	@Override
	protected void afterAction(BasePojo dtoObj) {
		PrmFinalEstimateDto headerDto = (PrmFinalEstimateDto) dtoObj;
		if (headerDto == null) {
			return;
		}

		finalestimateManager.setExtraData(headerDto);
	}
}
