package com.csnt.scdp.bizmodules.modules.fad.invoice.action;

import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.modules.common.action.ErpLoadAction;
import com.csnt.scdp.bizmodules.modules.common.intf.CommonServiceManager;
import com.csnt.scdp.bizmodules.modules.fad.invoice.services.intf.InvoiceManager;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.helper.FMCodeHelper;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
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
 * @timestamp 2015-09-25 14:13:12
 */

@Scope("singleton")
@Controller("failyinvoice-load")
@Transactional
public class LoadFailyAction extends ErpLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadFailyAction.class);

    @Resource(name = "invoice-manager")
    private InvoiceManager invoiceManager;

    @Resource(name = "erp-common-service-manager")
    private CommonServiceManager commonServiceManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        //相关合同grid合同号与合同总额赋值
        Map fadInvoiceMap = (Map) out.get("fadInvoiceDto");
        String userId = (String) fadInvoiceMap.get("renderPerson");
        if(userId!=null){
            List<ScdpUser> userList = commonServiceManager.findUserByUserId(userId);
            ScdpUser scdpUser=new ScdpUser();
            if(!ListUtil.isEmpty(userList)){
                scdpUser= userList.get(0);
                fadInvoiceMap.put("renderPersonDesc", scdpUser.getUserName());
            }
        }
        if(fadInvoiceMap.get("prmProjectMainId")!=null&&fadInvoiceMap.get("prmProjectMainId")!=""){
            String prmProjectMainId = (String) fadInvoiceMap.get("prmProjectMainId");
            PrmProjectMain prmProjectMain = PersistenceFactory.getInstance().findByPK(PrmProjectMain.class,prmProjectMainId);
            fadInvoiceMap.put("prmProjectName", prmProjectMain.getProjectName());
            fadInvoiceMap.put("projectCode", prmProjectMain.getProjectCode());
        }

        if (fadInvoiceMap != null) {
            String orgCode = (String) fadInvoiceMap.get("officeId");
            if(orgCode!=null){
                String orgName = OrgHelper.getOrgNameByCode(orgCode);
                fadInvoiceMap.put("officeIdDesc", orgName);
            }
        }
        List<Map> fadCashReqInvoiceDtoList = (List)fadInvoiceMap.get("fadCashReqInvoiceDto");
        List fadCashReqId = new ArrayList();
        if(fadCashReqInvoiceDtoList!=null){
            for(Map m :fadCashReqInvoiceDtoList){
                fadCashReqId.add(m.get("fadCashReqId"));
            }
        }

        // 税率
        Object taxRate = fadInvoiceMap.get("taxRate");
        if (taxRate != null) {
            String taxRateDesc = FMCodeHelper.getDescByCode(taxRate.toString(), "FAD_TAX_RATE");
            fadInvoiceMap.put("taxRateDesc", taxRateDesc);
        }

        List<Map> fadCashReqList = invoiceManager.getCashReqInfo(fadCashReqId);
        Map<String, Map> fadCashReq = new HashMap();
        if (ListUtil.isNotEmpty(fadCashReqList)) {
            fadCashReqList.forEach(t -> fadCashReq.put(t.get("uuid").toString(), t));
            fadCashReqInvoiceDtoList.forEach(s -> {
                if (fadCashReq.containsKey(s.get("fadCashReqId"))) {
                    Map fm = new HashMap(fadCashReq.get(s.get("fadCashReqId")));
                    s.put("runningNo", fm.get("runningNo"));//对应请款流水号
                    s.put("reqMoney", fm.get("money"));//对应请款金额
//                    s.put("cashContract", fm.get("cashContract"));//对应请款合同
                    s.put("cashVoucher", fm.get("cashVoucher"));//对应请款凭证
                }
            });
        }

        return out;
    }
}
