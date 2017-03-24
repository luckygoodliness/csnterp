package com.csnt.scdp.bizmodules.modules.prm.prmbilling.action;

import com.csnt.scdp.bizmodules.entity.prm.PrmContract;
import com.csnt.scdp.bizmodules.modules.common.action.ErpLoadAction;
import com.csnt.scdp.bizmodules.modules.prm.prmbilling.services.intf.PrmbillingManager;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Map;

/**
 * Description:  LoadAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 17:35:07
 */

@Scope("singleton")
@Controller("prmbilling-load")
@Transactional
public class LoadAction extends ErpLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

	@Resource(name = "prmbilling-manager")
	private PrmbillingManager prmbillingManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
		Map out = super.perform(inMap);
		//Do After
        //设置额外需要显示的字段
        prmbillingManager.setExtraData(out);
        Map prmBillDtoDetail = (Map)out.get("prmBillingDto");
        //load税率时，加0匹配数据字典
        if(prmBillDtoDetail!=null) {
            if(prmBillDtoDetail.get("taxRate")!=null) {
                String taxRate = prmBillDtoDetail.get("taxRate").toString();
                if (taxRate != null && taxRate.length() == 1) {
                    taxRate = taxRate + ".00";
                } else if (taxRate != null && taxRate.length() == 4) {
                    taxRate = taxRate + "";
                }
                prmBillDtoDetail.put("taxRateName", taxRate);
            }
            if(prmBillDtoDetail.get("prmContractId")!=null){

                String prmContractId = (String) prmBillDtoDetail.get("prmContractId");
                PrmContract prmContract = PersistenceFactory.getInstance().findByPK(PrmContract.class, prmContractId);
                prmBillDtoDetail.put("prmContractIdDesc", prmContract.getContractName());
            }
        }
		return out;
	}
}
