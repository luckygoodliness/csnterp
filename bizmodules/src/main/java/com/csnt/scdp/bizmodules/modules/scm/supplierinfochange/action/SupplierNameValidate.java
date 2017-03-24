package com.csnt.scdp.bizmodules.modules.scm.supplierinfochange.action;

import com.csnt.scdp.bizmodules.modules.scm.supplierinfochange.dto.ScmSupplierCDto;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfochange.services.intf.SupplierinfochangeManager;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.services.intf.SupplierinforManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.CommonAddAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.helper.NumberingHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:  AddAction
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-07-28 17:10:13
 */

@Scope("singleton")
@Controller("supplierinfochange-supplierNameValidate")
@Transactional
public class SupplierNameValidate extends CommonAddAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(SupplierNameValidate.class);

	@Resource(name = "supplierinfochange-manager")
	private SupplierinfochangeManager supplierinfochangeManager;

	@Resource(name = "supplierinfor-manager")
	private SupplierinforManager supplierinforManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		Map rsMap = new HashMap();
		String completeName = (String) inMap.get("completeName");
		String simpleName = (String) inMap.get("simpleName");
		String supplierCode = (String) inMap.get("supplierCode");
		if (StringUtil.isEmpty(completeName)) {
			completeName = "";
		}
		if (StringUtil.isEmpty(supplierCode)) {
			supplierCode = "";
		}
		if (StringUtil.isEmpty(simpleName)) {
			simpleName = "";
		}
		supplierinforManager.supplierNameValidateMouseLeave(completeName, simpleName, supplierCode);
		return null;
	}
}