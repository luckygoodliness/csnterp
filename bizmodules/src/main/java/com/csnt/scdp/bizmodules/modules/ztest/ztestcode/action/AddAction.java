package com.csnt.scdp.bizmodules.modules.ztest.ztestcode.action;

import com.csnt.scdp.bizmodules.entity.ztest.ZtrainMain;
import com.csnt.scdp.bizmodules.entityattributes.ztest.ZtrainMainAttribute;
import com.csnt.scdp.bizmodules.modules.ztest.ztestcode.dto.ZtrainMainDto;
import com.csnt.scdp.bizmodules.modules.ztrain.ztraincode.dto.TestTableDto;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.ListUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.ztest.ztestcode.services.intf.ZtestcodeManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  AddAction
 * Copyright: © 2017 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * 2017-02-24 09:51:18
 */

@Scope("singleton")
@Controller("ztestcode-add")
@Transactional
public class AddAction extends CommonAddAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);

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
	protected void	beforeAction(BasePojo dtoObj) {
		ZtrainMainDto tableDto =(ZtrainMainDto) dtoObj;
		Map<String, Object> ConditionsMap = new HashMap<String, Object>();
		ConditionsMap.put(ZtrainMainAttribute.CODE, tableDto.getCode());
//		前台录入的code，查出后台数据库是否已经存在相同的数据了：
		List<ZtrainMain> tableList = PersistenceFactory.getInstance().findByAnyFields(ZtrainMain.class, ConditionsMap, null);
		if (ListUtil.isNotEmpty(tableList)){
			throw new BizException("保存失败，编码已经存在");
		}
	}

}
