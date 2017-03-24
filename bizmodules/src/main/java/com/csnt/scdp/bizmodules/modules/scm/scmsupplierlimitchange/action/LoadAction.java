package com.csnt.scdp.bizmodules.modules.scm.scmsupplierlimitchange.action;

import com.csnt.scdp.bizmodules.entity.scm.ScmSupplier;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmSupplierLimitAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmSupplierLimitChangeAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmSupplierLimitChangeDAttribute;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.sysmodules.helper.FMCodeHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.scm.scmsupplierlimitchange.services.intf.ScmsupplierlimitchangeManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
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
 * @timestamp 2016-07-22 16:17:07
 */

@Scope("singleton")
@Controller("scmsupplierlimitchange-load")
@Transactional
public class LoadAction extends CommonLoadAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

	@Resource(name = "scmsupplierlimitchange-manager")
	private ScmsupplierlimitchangeManager scmsupplierlimitchangeManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
		Map out = super.perform(inMap);
		PersistenceCrudManager pcm = PersistenceFactory.getInstance();
		Map scmSupplierLimitChangeMap = (Map) out.get(ScmSupplierLimitChangeAttribute.SCM_SUPPLIER_LIMIT_CHANGE_DTO);
		List<Map> scmSupplierLimitChangeDList = (List) scmSupplierLimitChangeMap.get(ScmSupplierLimitChangeDAttribute.SCM_SUPPLIER_LIMIT_CHANGE_D_DTO);
		List<Map<String, Object>> contractList = scmsupplierlimitchangeManager.selectContract(scmSupplierLimitChangeMap.get("uuid").toString());

		if(ListUtil.isNotEmpty(scmSupplierLimitChangeDList)){
			for (int i = 0; i < scmSupplierLimitChangeDList.size(); i++) {
				List list=new ArrayList();
				for (int k = 0; k < contractList.size(); k++)
				if (scmSupplierLimitChangeDList.get(i).get("scmSupplierId").equals(contractList.get(k).get("supplierCode"))){
					list.add(contractList.get(k));
				}
				scmSupplierLimitChangeDList.get(i).put("scmContractDto",list);
			}

		}
		if (null != scmSupplierLimitChangeMap.get("year")) {
			String taxRate = scmSupplierLimitChangeMap.get("year").toString();
			if (taxRate != null) {
				String taxRateDesc = FMCodeHelper.getDescByCode(taxRate, "CDM_YEAR");
				scmSupplierLimitChangeMap.put("year", taxRateDesc);
			}
		}

		if (ListUtil.isNotEmpty(scmSupplierLimitChangeDList)) {
			List<Map<String, Object>> reqList = scmsupplierlimitchangeManager.selectCurAmount(scmSupplierLimitChangeMap.get("uuid").toString());
			Map<String, Map<String, Object>> mapCurAmount = new HashMap<String, Map<String, Object>>();
			if (ListUtil.isNotEmpty(reqList)) {
				for (int i = 0; i < reqList.size(); i++) {
					mapCurAmount.put(reqList.get(i).get("supplierCode").toString(), reqList.get(i));
				}
			}
			if (null != scmSupplierLimitChangeMap.get("state")) {
				String taxRate = scmSupplierLimitChangeMap.get("state").toString();
				if (taxRate != null) {
					String taxRateDesc = FMCodeHelper.getDescByCode(taxRate, "FAD_BILL_STATE");
					scmSupplierLimitChangeMap.put("state", taxRateDesc);
				}
			}
			List supplierId = new ArrayList();
			if (ListUtil.isNotEmpty(scmSupplierLimitChangeDList)) {
				for (Map m : scmSupplierLimitChangeDList) {
					supplierId.add(m.get(ScmSupplierLimitChangeDAttribute.SCM_SUPPLIER_ID));
				}
			}
			List<ScmSupplier> scmSupplierList = new ArrayList<>();
			if (ListUtil.isNotEmpty(supplierId)) {
				Map mapContractId = new HashMap();
				mapContractId.put("uuid", supplierId);
				scmSupplierList = pcm.findByAnyFields(ScmSupplier.class, mapContractId, null);
			}
			Map<String, ScmSupplier> scdpOrgMap = new HashMap();
			if (ListUtil.isNotEmpty(scmSupplierList)) {
				scmSupplierList.forEach(t -> scdpOrgMap.put(t.getUuid(), t));
				scmSupplierLimitChangeDList.forEach(s -> {
					if (scdpOrgMap.containsKey(s.get(ScmSupplierLimitChangeDAttribute.SCM_SUPPLIER_ID))) {
						ScmSupplier so = scdpOrgMap.get(s.get(ScmSupplierLimitChangeDAttribute.SCM_SUPPLIER_ID));
						if(mapCurAmount.containsKey(so.getUuid())) {
							s.put("curVolume", mapCurAmount.get(so.getUuid()).get("curVolume"));
							s.put("curAmount", mapCurAmount.get(so.getUuid()).get("curAmount"));
						}
						s.put("scmSupplierName", so.getCompleteName());
						s.put("supplierType", FMCodeHelper.getDescByCode(so.getSupplierGenre(), "SCM_SUPPLIER_GENRE"));
					}
				});
			}
		}
		//Do After
		return out;
	}
}