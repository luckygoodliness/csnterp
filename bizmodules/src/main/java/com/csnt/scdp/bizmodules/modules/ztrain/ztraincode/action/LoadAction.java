package com.csnt.scdp.bizmodules.modules.ztrain.ztraincode.action;

import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.modules.ztrain.ztraincode.dto.TestTableDto;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.ztrain.ztraincode.services.intf.ZtraincodeManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Map;

/**
 * Description:  LoadAction
 * Copyright: © 2017 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * 2017-02-21 15:11:17
 */

@Scope("singleton")
@Controller("ztraincode-load")
@Transactional
public class LoadAction extends CommonLoadAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

	@Resource(name = "ztraincode-manager")
	private ZtraincodeManager ztraincodeManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
		Map out = super.perform(inMap);
		//Do After
		return out;
	}

	@Override
	protected void afterAction(BasePojo dtoObj) {
		TestTableDto tableDto = (TestTableDto) dtoObj;
		String projectId = tableDto.getProjectId();

		if (StringUtil.isNotEmpty(projectId)) {
			PrmProjectMain obj = PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, projectId);
			if (obj != null) {
				tableDto.setProjectCode(obj.getProjectCode());
			}

		}
	}
}
