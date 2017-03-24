package com.csnt.scdp.bizmodules.modules.ztest.ztestcode.action;

import com.csnt.scdp.bizmodules.entity.prm.PrmCustomer;
import com.csnt.scdp.bizmodules.modules.ztest.ztestcode.dto.ZtrainMainDto;
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
import com.csnt.scdp.bizmodules.modules.ztest.ztestcode.services.intf.ZtestcodeManager;

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
 * 2017-02-24 09:51:18
 */

@Scope("singleton")
@Controller("ztestcode-load")
@Transactional
public class LoadAction extends CommonLoadAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

	@Resource(name = "ztestcode-manager")
	private ZtestcodeManager ztestcodeManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
		Map out = super.perform(inMap);
		//Do After
		return out;
	}

	@Override
	protected void afterAction(BasePojo dtoObj) {
		ZtrainMainDto tableDto = (ZtrainMainDto) dtoObj;
		String customerId = tableDto.getCusomterUuid();

		if (StringUtil.isNotEmpty(customerId)) {
			PrmCustomer obj = PersistenceFactory.getInstance().findByPK(PrmCustomer.class, customerId);
			if (obj != null) {
				tableDto.setCustomerName(obj.getCustomerName());
				tableDto.setCusomterUuid(obj.getUpdateBy());
			}

		}
	}
}

