package com.csnt.scdp.bizmodules.modules.scm.supplierinfor.action;

import com.csnt.scdp.bizmodules.modules.scm.supplierinfochange.services.intf.SupplierinfochangeManager;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.dto.ScmSupplierBankDto;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.dto.ScmSupplierDto;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.helper.NumberingHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.services.intf.SupplierinforManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-24 16:02:00
 */

@Scope("singleton")
@Controller("supplierinfor-add")
@Transactional
public class AddAction extends CommonAddAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);

	@Resource(name = "supplierinfor-manager")
	private SupplierinforManager supplierinforManager;
	@Resource(name = "supplierinfochange-manager")
	private SupplierinfochangeManager supplierinfochangeManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {

		Map viewMap = (Map) inMap.get(CommonAttribute.VIEW_DATA);
		Map scmSupplierDto = (Map) viewMap.get("scmSupplierDto");
		String reqNo = NumberingHelper.getNumbering("SCM_SUPPLIER_CODE", null);
		scmSupplierDto.put("supplierCode", reqNo);
		Map out = super.perform(inMap);
		out.put("supplierCode", reqNo);
		return out;
	}
	@Override
	protected void beforeAction(BasePojo dtoObj) {
		ScmSupplierDto scmSupplierDto = (ScmSupplierDto) dtoObj;
		supplierinforManager.supplierNameValidate(scmSupplierDto.getCompleteName(),scmSupplierDto.getSimpleName(),scmSupplierDto.getSupplierCode());
		Map mapSupplierCode = new HashMap();
		List<String> bankId = new ArrayList<String>();
		List<ScmSupplierBankDto> scmSupplierBankCDtoList = scmSupplierDto.getScmSupplierBankDto();
		if (ListUtil.isNotEmpty(scmSupplierBankCDtoList)) {
			for (int i = 0; i < scmSupplierBankCDtoList.size(); i++) {
				if (StringUtil.isNotEmpty(scmSupplierBankCDtoList.get(i).getAccountNo())) {
					if (!(mapSupplierCode.containsKey(scmSupplierBankCDtoList.get(i).getAccountNo().toString()))) {
						mapSupplierCode.put(scmSupplierBankCDtoList.get(i).getAccountNo().toString(), scmSupplierBankCDtoList.get(i).getAccountNo().toString());
						bankId.add(scmSupplierBankCDtoList.get(i).getAccountNo().toString());

					} else {
						throw new BizException("银行账号不能重复！");
					}
				}
			}
			if (bankId.size()>0) {
				supplierinfochangeManager.bankValidate(bankId, scmSupplierDto.getUuid());
			}
		}

	}
}
