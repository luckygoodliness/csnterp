package com.csnt.scdp.bizmodules.modules.ztrain.ztraincode.action;

import com.csnt.scdp.bizmodules.entity.ztrain.TestTable;
import com.csnt.scdp.bizmodules.entityattributes.ztrain.TestTableAttribute;
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
import com.csnt.scdp.bizmodules.modules.ztrain.ztraincode.services.intf.ZtraincodeManager;

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
 * 2017-02-21 15:11:17
 */

@Scope("singleton")
@Controller("ztraincode-add")
@Transactional
public class AddAction extends CommonAddAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);

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
	protected void	beforeAction(BasePojo dtoObj) {
		TestTableDto tableDto =(TestTableDto) dtoObj;
		Map<String, Object> ConditionsMap = new HashMap<String, Object>();
		ConditionsMap.put(TestTableAttribute.CODE, tableDto.getCode());
//		前台录入的code，查出后台数据库是否已经存在相同的数据了：
		List<TestTable> tableList = PersistenceFactory.getInstance().findByAnyFields(TestTable.class, ConditionsMap, null);
		if (ListUtil.isNotEmpty(tableList)){
			throw new BizException("保存失败，学号已经存在");
		}
	}
}
