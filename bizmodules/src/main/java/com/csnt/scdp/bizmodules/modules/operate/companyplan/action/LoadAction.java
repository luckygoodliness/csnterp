package com.csnt.scdp.bizmodules.modules.operate.companyplan.action;

import com.csnt.scdp.bizmodules.entityattributes.operate.OperateCompanyPlanCAttribute;
import com.csnt.scdp.bizmodules.entityattributes.operate.OperateCompanyPlanHAttribute;
import com.csnt.scdp.bizmodules.modules.common.action.ErpLoadAction;
import com.csnt.scdp.bizmodules.modules.operate.companyplan.services.intf.CompanyplanManager;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.sysmodules.entity.ScdpOrg;
import com.csnt.scdp.sysmodules.entityattributes.ScdpOrgAttribute;
import com.csnt.scdp.sysmodules.helper.FMCodeHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  LoadAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-11-19 20:30:35
 */

@Scope("singleton")
@Controller("companyplan-load")
@Transactional
public class LoadAction extends ErpLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

	@Resource(name = "companyplan-manager")
	private CompanyplanManager companyplanManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
		Map out = super.perform(inMap);
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        Map operateCompanyPlanHMap = (Map) out.get(OperateCompanyPlanHAttribute.OPERATE_COMPANY_PLAN_H_Dto);
        List<Map> operateCompanyPlanCDtoList = (List) operateCompanyPlanHMap.get(OperateCompanyPlanCAttribute.OPERATE_COMPANY_PLAN_C_Dto);
        List officeCode = new ArrayList();
        if (operateCompanyPlanCDtoList != null) {
            for (Map m : operateCompanyPlanCDtoList) {
                officeCode.add(m.get(OperateCompanyPlanCAttribute.OBJECT_ID));
            }
        }

        List<ScdpOrg>  scdpOrgList = new ArrayList();
        if (ListUtil.isNotEmpty(officeCode)) {
            Map mapContractId = new HashMap();
            mapContractId.put(ScdpOrgAttribute.ORG_CODE, officeCode);
            scdpOrgList = pcm.findByAnyFields(ScdpOrg.class, mapContractId, null);
        }
        Map<String, ScdpOrg> scdpOrgMap = new HashMap();
        if (ListUtil.isNotEmpty(scdpOrgList)) {
            scdpOrgList.forEach(t -> scdpOrgMap.put(t.getOrgCode(), t));
            operateCompanyPlanCDtoList.forEach(s -> {
                if (scdpOrgMap.containsKey(s.get(OperateCompanyPlanCAttribute.OBJECT_ID))) {
                    ScdpOrg so = scdpOrgMap.get(s.get(OperateCompanyPlanCAttribute.OBJECT_ID));
                    s.put(OperateCompanyPlanCAttribute.OFFICE_NAME, so.getOrgName());
                }
            });
        }

        String taxRate = operateCompanyPlanHMap.get("startYear").toString();
        if (taxRate != null) {
                String taxRateDesc = FMCodeHelper.getDescByCode(taxRate, "CDM_YEAR");
            operateCompanyPlanHMap.put("startYearDesc", taxRateDesc);
        }
		//Do After
		return out;
	}
}
