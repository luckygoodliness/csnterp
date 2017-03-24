package com.csnt.scdp.bizmodules.modules.scm.scmsaetask.action;

import com.csnt.scdp.bizmodules.entity.scm.ScmMaterialClass;
import com.csnt.scdp.bizmodules.entity.scm.ScmSaeObject;
import com.csnt.scdp.bizmodules.entity.scm.ScmSupplier;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpUserHelper;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.helper.FMCodeHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.scm.scmsaetask.services.intf.ScmsaetaskManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  LoadAction
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-08-26 09:56:50
 */

@Scope("singleton")
@Controller("scmsaetask-load")
@Transactional
public class LoadAction extends CommonLoadAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

	@Resource(name = "scmsaetask-manager")
	private ScmsaetaskManager scmsaetaskManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
		PersistenceCrudManager pcm = PersistenceFactory.getInstance();
		Map out = super.perform(inMap);
		Map scmSaeTaskMap = (Map) out.get("scmSaeTaskDto");

		// 类型
		Object materialType = scmSaeTaskMap.get("materialType");
		if (materialType != null) {
			String materialTypeDesc = FMCodeHelper.getDescByCode(materialType.toString(), "SCM_SAE_CASE_TYPE");
			scmSaeTaskMap.put("materialType", materialTypeDesc);
		}
		// 状态
		Object state = scmSaeTaskMap.get("state");
		if (state != null) {
			String stateDesc = FMCodeHelper.getDescByCode(state.toString(), "SCM_SAE_CASE_STATE");
			scmSaeTaskMap.put("state", stateDesc);
		}
		// 评分人
		Object userCode = scmSaeTaskMap.get("userCode");
		if (userCode != null) {
			ScdpUser user = ErpUserHelper.findUserByUserId(userCode.toString());
			scmSaeTaskMap.put("userCode", user.getUserName());
		}

		List<Map> scmSaeFormList = (List) scmSaeTaskMap.get("scmSaeFormDto");
		if(ListUtil.isNotEmpty(scmSaeFormList)) {
			scmSaeFormList.forEach(record -> {
				ScmSaeObject scmSaeObject = pcm.findByPK(ScmSaeObject.class, record.get("scmSaeObjectId").toString());
				Map param = new HashMap();
				param.put("code", scmSaeObject.getMaterialCode());
				List<ScmMaterialClass> materialClassList = pcm.findByAnyFields(ScmMaterialClass.class, param, null);
				ScmMaterialClass  materialClass = materialClassList.get(0);
				record.put("materialClassName", materialClass.getName());

				ScmSupplier scmSupplier = pcm.findByPK(ScmSupplier.class, scmSaeObject.getSupplierId());
				record.put("supplierName", scmSupplier.getCompleteName());
			});
		}

		//Do After
		return out;
	}
}
