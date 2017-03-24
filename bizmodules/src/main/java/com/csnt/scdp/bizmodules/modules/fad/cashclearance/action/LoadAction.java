package com.csnt.scdp.bizmodules.modules.fad.cashclearance.action;

import com.csnt.scdp.bizmodules.entity.fad.FadCashReq;
import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.modules.common.action.ErpLoadAction;
import com.csnt.scdp.bizmodules.modules.common.intf.CommonServiceManager;
import com.csnt.scdp.bizmodules.modules.fad.cashclearance.services.intf.CashclearanceManager;
import com.csnt.scdp.bizmodules.modules.fad.cashreq.services.intf.CashreqManager;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * Description:  LoadAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-11-08 18:01:30
 */

@Scope("singleton")
@Controller("cashclearance-load")
@Transactional
public class LoadAction extends ErpLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

	@Resource(name = "cashclearance-manager")
	private CashclearanceManager cashclearanceManager;

    @Resource(name = "cashreq-manager")
    private CashreqManager cashReqManager;

    @Resource(name = "erp-common-service-manager")
    private CommonServiceManager commonServiceManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
        Map out = super.perform(inMap);
        //相关合同grid合同号与合同总额赋值
        Map fadCashClearanceMap = (Map) out.get("fadCashClearanceDto");

        if(fadCashClearanceMap.get("prmProjectMainId")!=null&&fadCashClearanceMap.get("prmProjectMainId")!=""){
            String prmProjectMainId = (String) fadCashClearanceMap.get("prmProjectMainId");
            PrmProjectMain prmProjectMain = PersistenceFactory.getInstance().findByPK(PrmProjectMain.class,prmProjectMainId);
            fadCashClearanceMap.put("projectName", prmProjectMain.getProjectName());
        }

        if (fadCashClearanceMap != null) {
            String userId = (String) fadCashClearanceMap.get("clearancePerson");
            ScdpUser scdpUser = commonServiceManager.findUserByUserId(userId).get(0);
            fadCashClearanceMap.put("clearancePersonName", scdpUser.getUserName());

            String orgCode = (String) fadCashClearanceMap.get("officeId");
            if(orgCode!=null){
                String orgName = OrgHelper.getOrgNameByCode(orgCode);
                fadCashClearanceMap.put("officeIdDesc", orgName);
            }
        }
        if (fadCashClearanceMap.get("fadCashReqInvoiceDto") != null) {
            List<Map> scmContractInvoiceList = (List) fadCashClearanceMap.get("fadCashReqInvoiceDto");
            for (Map s : scmContractInvoiceList) {
                FadCashReq fadCashReq = cashReqManager.getFadCashReqbyUuid((String) s.get("fadCashReqId"));
                s.put("runningNo", fadCashReq.getRunningNo());//对应请款流水号
                s.put("reqMoney", fadCashReq.getMoney());//对应请款金额
                s.put("cashVoucher", "待完善");//对应请款凭证
            }
        }
		return out;
	}
}
