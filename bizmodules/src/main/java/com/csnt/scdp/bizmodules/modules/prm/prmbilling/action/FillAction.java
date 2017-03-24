package com.csnt.scdp.bizmodules.modules.prm.prmbilling.action;

import com.csnt.scdp.bizmodules.entity.prm.PrmBilling;
import com.csnt.scdp.bizmodules.modules.prm.prmbilling.services.intf.PrmbillingManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/9.
 */

@Scope("singleton")
@Controller("prmbilling-fill")
@Transactional
public class FillAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(FillAction.class);

    @Resource(name = "prmbilling-manager")
    private PrmbillingManager prmbillingManager;

    @Override
    //根据customerId查询出相对应的数据
    public Map perform(Map inMap) throws BizException, SysException {
        String customerId = (String) inMap.get("customerId");
//        customerId = "8a9183fd50ac61d30150ac6550e60003";
        Map resultMap = new HashMap<>();
        PrmBilling prmBilling = prmbillingManager.getCustomerForUUID(customerId);
        if (prmBilling != null) {
            resultMap.put("customerInvoiceName", prmBilling.getCustomerInvoiceName());
            resultMap.put("customerLocation", prmBilling.getCustomerLocation());
            resultMap.put("taxNo", prmBilling.getTaxNo());
            resultMap.put("bankName", prmBilling.getBankName());
            resultMap.put("bankAccount", prmBilling.getBankAccount());
            resultMap.put("phone", prmBilling.getPhone());
        }
        Map out = new HashMap<>();
        out.put("result", resultMap);
        return out;
    }


}
