package com.csnt.scdp.bizmodules.modules.prm.contract.action;

import com.csnt.scdp.bizmodules.entityattributes.prm.PrmContractAttribute;
import com.csnt.scdp.bizmodules.modules.prm.contract.services.intf.ContractManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.CommonAddAction;
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
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 09:57:20
 */

@Scope("singleton")
@Controller("contract-add")
@Transactional
public class AddAction extends CommonAddAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);

	@Resource(name = "contract-manager")
	private ContractManager contractManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
		Map viewData = (Map) inMap.get(CommonAttribute.VIEW_DATA);
		Map headerMap = (Map) viewData.get(PrmContractAttribute.PRM_CONTRACT_DTO);
		String contractNo = (String) headerMap.get(PrmContractAttribute.CONTRACT_NO);
		String contractName = (String) headerMap.get(PrmContractAttribute.CONTRACT_NAME);
		contractManager.checkContractUnique(contractNo, contractName);

		Map out = super.perform(inMap);
		//Do After
		return out;
	}
}
