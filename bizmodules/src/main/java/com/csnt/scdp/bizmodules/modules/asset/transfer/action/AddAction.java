package com.csnt.scdp.bizmodules.modules.asset.transfer.action;

import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.helper.NumberingHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.asset.transfer.services.intf.TransferManager;

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
 * @timestamp 2015-09-23 18:08:32
 */

@Scope("singleton")
@Controller("transfer-add")
@Transactional
public class AddAction extends CommonAddAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);

	@Resource(name = "transfer-manager")
	private TransferManager transferManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		String dtoClass = (String) inMap.get(CommonAttribute.DTO_CLASS_NAME);
		Map viewMap = (Map) inMap.get(CommonAttribute.VIEW_DATA);

		String mainTabName = StringUtil.capFirst(BeanFactory.getClass(dtoClass).getSimpleName(), false);

		Map mapInput = (Map) viewMap.get(mainTabName);
		String reqNo = NumberingHelper.getNumbering("ASSET_TRANSFER_NO", null);
		mapInput.put("ransferApplyId", reqNo);

		Map out = super.perform(inMap);
		//Do After
		out.put("ransferApplyId", reqNo);
		return out;
	}
}
