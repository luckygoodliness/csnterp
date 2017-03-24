package com.csnt.scdp.bizmodules.modules.prm.prmbilling.action;

import com.csnt.scdp.bizmodules.entity.prm.PrmBilling;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmBillingAttribute;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.ListUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.prm.prmbilling.services.intf.PrmbillingManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 17:35:07
 */

@Scope("singleton")
@Controller("prmbilling-add")
@Transactional
public class AddAction extends CommonAddAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);

    @Resource(name = "prmbilling-manager")
    private PrmbillingManager prmbillingManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        //Do After
        return out;
    }

    protected void beforeAction(BasePojo dtoObj) {
        PrmBilling prmBilling = (PrmBilling) dtoObj;
        if (PrmBillingAttribute.BILL_TYPE_APPLY.compareTo(prmBilling.getBillType()) == 0) {
            prmbillingManager.validaBankNo(prmBilling.getCustomerInvoiceName(), prmBilling.getBankAccount());
        }
    }
}
