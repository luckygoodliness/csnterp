package com.csnt.scdp.bizmodules.modules.asset.apply.action;

import com.csnt.scdp.bizmodules.entity.asset.AssetDiscardApply;
import com.csnt.scdp.bizmodules.modules.asset.apply.dto.AssetDiscardApplyDto;
import com.csnt.scdp.bizmodules.modules.asset.transfer.dto.AssetTransferDto;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.helper.NumberingHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.asset.apply.services.intf.ApplyManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Map;

/**
 * Description:  ModifyAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 20:06:31
 */

@Scope("singleton")
@Controller("apply-modify")
@Transactional
public class ModifyAction extends CommonModifyAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(ModifyAction.class);

	@Resource(name = "apply-manager")
	private ApplyManager applyManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
        String dtoClass = (String) inMap.get(CommonAttribute.DTO_CLASS_NAME);
        Map viewMap = (Map) inMap.get(CommonAttribute.VIEW_DATA);

        String mainTabName = StringUtil.capFirst(BeanFactory.getClass(dtoClass).getSimpleName(), false);

        Map mapInput = (Map) viewMap.get(mainTabName);

        AssetDiscardApplyDto assetDiscardApplyDto = (AssetDiscardApplyDto) BeanUtil.map2Dto(mapInput, BeanFactory.getClass(dtoClass));
        applyManager.validateCardUnique(assetDiscardApplyDto);

		Map out = super.perform(inMap);
		//Do After
		return out;
	}
}
