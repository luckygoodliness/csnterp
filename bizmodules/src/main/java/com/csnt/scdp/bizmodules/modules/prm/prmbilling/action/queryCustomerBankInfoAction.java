package com.csnt.scdp.bizmodules.modules.prm.prmbilling.action;

import com.csnt.scdp.bizmodules.modules.prm.prmbilling.services.intf.PrmbillingManager;
import com.csnt.scdp.framework.commonaction.crud.CommonQueryAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Map;

/**
 * Description:  QueryAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 15:32:42
 */

@Scope("singleton")
@Controller("prmbilling-querycustomerbankinfo")
@Transactional
public class queryCustomerBankInfoAction extends CommonQueryAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(queryCustomerBankInfoAction.class);

    @Resource(name = "prmbilling-manager")
    private PrmbillingManager prmbillingManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //1.返回某供应商的最新有效的银行信息
        return prmbillingManager.getCustomerLatestBankInfo(inMap);
    }
}
