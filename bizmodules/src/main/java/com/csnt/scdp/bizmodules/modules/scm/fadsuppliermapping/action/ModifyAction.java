package com.csnt.scdp.bizmodules.modules.scm.fadsuppliermapping.action;

import com.csnt.scdp.bizmodules.entity.scm.FadSupplierMapping;
import com.csnt.scdp.bizmodules.entity.scm.ScmSupplier;
import com.csnt.scdp.bizmodules.entityattributes.scm.FadSupplierMappingAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmSupplierAttribute;
import com.csnt.scdp.bizmodules.modules.scm.fadsuppliermapping.dto.FadSupplierMappingDto;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.ListUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.scm.fadsuppliermapping.services.intf.FadsuppliermappingManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  ModifyAction
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-08-04 15:22:38
 */

@Scope("singleton")
@Controller("fadsuppliermapping-modify")
@Transactional
public class ModifyAction extends CommonModifyAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(ModifyAction.class);

	@Resource(name = "fadsuppliermapping-manager")
	private FadsuppliermappingManager fadsuppliermappingManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
		Map out = super.perform(inMap);
		//Do After
		return out;
	}
	@Override
	protected void beforeAction(BasePojo dtoObj) {
		FadSupplierMappingDto fadSupplierMappingDto = (FadSupplierMappingDto) dtoObj;
		PersistenceCrudManager pcm = PersistenceFactory.getInstance();
		ScmSupplier scmSupplierName = pcm.findByPK(ScmSupplier.class, fadSupplierMappingDto.getSupplierToUuid());
		if (fadSupplierMappingDto.getSupplierFromName().equals(scmSupplierName.getCompleteName())) {
			throw new BizException("新名称和旧名称不能相同!");
		}

		ScmSupplier scmSupplier = pcm.findByPK(ScmSupplier.class, fadSupplierMappingDto.getSupplierToUuid());
		Map conditionMap = new HashMap<>();
		conditionMap.put(FadSupplierMappingAttribute.SUPPLIER_FROM_NAME, fadSupplierMappingDto.getSupplierFromName());
		List<FadSupplierMapping> fadSupplierMappingOldList = pcm.findByAnyFields(FadSupplierMapping.class, conditionMap, null);
		if (ListUtil.isNotEmpty(fadSupplierMappingOldList)) {
			for (int i = 0; i < fadSupplierMappingOldList.size(); i++) {
				if (!(fadSupplierMappingDto.getUuid().equals(fadSupplierMappingOldList.get(i).getUuid()))) {
					if (fadSupplierMappingDto.getSupplierToUuid().equals(fadSupplierMappingOldList.get(i).getSupplierToUuid())) {
						throw new BizException("系统已存在该映射关系，保存失败!");
					} else {
						scmSupplier = pcm.findByPK(ScmSupplier.class, fadSupplierMappingOldList.get(i).getSupplierToUuid());
						throw new BizException("原名称已经关联供应商" + scmSupplier.getCompleteName() + "，请先清除后，才能保存!");
					}
				}
			}
		}
		conditionMap.clear();
		conditionMap.put(ScmSupplierAttribute.COMPLETE_NAME, fadSupplierMappingDto.getSupplierFromName());
		List<ScmSupplier> ScmSupplierList = pcm.findByAnyFields(ScmSupplier.class, conditionMap, null);
		if (ListUtil.isNotEmpty(ScmSupplierList)) {
			conditionMap.clear();
			conditionMap.put(FadSupplierMappingAttribute.SUPPLIER_FROM_NAME, scmSupplier.getCompleteName());
			conditionMap.put(FadSupplierMappingAttribute.SUPPLIER_TO_UUID, ScmSupplierList.get(0).getUuid());
			List<FadSupplierMapping> fadSupplierMappingNewList = pcm.findByAnyFields(FadSupplierMapping.class, conditionMap, null);
			if (ListUtil.isNotEmpty(fadSupplierMappingNewList)) {
				throw new BizException("系统存在" + scmSupplier.getCompleteName() + "到" + ScmSupplierList.get(0).getCompleteName() + "的映射，请先清除后，才能保存！");
			}
		}

		fadsuppliermappingManager.fadSupplierMappingChange(fadSupplierMappingDto.getSupplierFromName(), fadSupplierMappingDto.getSupplierToUuid());
	}
}
