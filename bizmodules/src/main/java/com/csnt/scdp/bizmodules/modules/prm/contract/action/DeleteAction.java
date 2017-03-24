package com.csnt.scdp.bizmodules.modules.prm.contract.action;

import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMainC;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainAttribute;
import com.csnt.scdp.bizmodules.modules.common.action.ErpDeleteAction;
import com.csnt.scdp.bizmodules.modules.prm.contract.services.intf.ContractManager;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  DeleteAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 09:57:20
 */

@Scope("singleton")
@Controller("contract-delete")
@Transactional
public class DeleteAction extends ErpDeleteAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(DeleteAction.class);

	@Resource(name = "contract-manager")
	private ContractManager contractManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
		List<String> lstUuid = (List<String>) getDeleteUuids(inMap);
		Map<String, Object> condition = new HashMap();
		condition.put(PrmProjectMainAttribute.PRM_CONTRACT_ID, lstUuid);
		List<PrmProjectMainC> lstPrm = PersistenceFactory.getInstance().findByAnyFields(PrmProjectMainC.class, condition, null);
		if (ListUtil.isNotEmpty(lstPrm)) {
			throw new BizException("不能删除已经关联了项目的合同！");
		}

		Map out = super.perform(inMap);

		String sql = "delete from prm_contract_c where prm_contract_id in (" + StringUtil.joinForSqlIn(lstUuid, ",") + ")";
		DAOMeta daoMeta = new DAOMeta(sql);
		PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
		//Do After
		return out;
	}
}
