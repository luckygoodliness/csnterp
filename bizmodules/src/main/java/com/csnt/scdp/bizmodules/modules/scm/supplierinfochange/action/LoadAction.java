package com.csnt.scdp.bizmodules.modules.scm.supplierinfochange.action;

import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.ListUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfochange.services.intf.SupplierinfochangeManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * Description:  LoadAction
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-07-28 17:10:13
 */

@Scope("singleton")
@Controller("supplierinfochange-load")
@Transactional
public class LoadAction extends CommonLoadAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

	@Resource(name = "supplierinfochange-manager")
	private SupplierinfochangeManager supplierinfochangeManager;


	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
		Map out = super.perform(inMap);
		Map scmSupplierCDtotMap = (Map) out.get("scmSupplierCDto");
		List<Map> cdmFileRelationDtoList = (List) scmSupplierCDtotMap.get("cdmFileRelationDto");
		List<Map> cdmFileRelationCDtoList = (List) scmSupplierCDtotMap.get("cdmFileRelationCDto");
		if (ListUtil.isNotEmpty(cdmFileRelationDtoList)) {
			for (int i = cdmFileRelationDtoList.size()-1; i >=0; i--) {
				if ("SCM_SUPPLIER_CHANGE".equals(cdmFileRelationDtoList.get(i).get("fileClassify"))) {
					cdmFileRelationDtoList.remove(i);
				}
			}
		}
		if (ListUtil.isNotEmpty(cdmFileRelationCDtoList)) {
			for (int i = cdmFileRelationCDtoList.size()-1; i >= 0; i--) {
				if (!("SCM_SUPPLIER_CHANGE".equals(cdmFileRelationCDtoList.get(i).get("fileClassify")))) {
					cdmFileRelationCDtoList.remove(i);
				}
			}
		}
		//Do After
		supplierinfochangeManager.setExtraData(out);
		return out;
	}
}
