package com.csnt.scdp.bizmodules.modules.prm.progress.action;

import com.csnt.scdp.bizmodules.modules.common.action.ErpLoadAction;
import com.csnt.scdp.bizmodules.modules.prm.progress.services.intf.ProgressManager;
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
 * Description:  LoadAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 19:16:19
 */

@Scope("singleton")
@Controller("progress-load")
@Transactional
public class LoadAction extends ErpLoadAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

	@Resource(name = "progress-manager")
	private ProgressManager progressManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
		Map out = super.perform(inMap);
		//Do After
        //1.设置额外需要显示的字段
        progressManager.setExtraData(out);
		return out;
	}
}
