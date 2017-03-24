package com.csnt.scdp.bizmodules.modules.prm.prmcustomer.action;

import com.csnt.scdp.bizmodules.entity.fad.FadRtfreevalue;
import com.csnt.scdp.bizmodules.modules.prm.prmcustomer.dto.PrmCustomerDto;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.ListUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.prm.prmcustomer.services.intf.PrmcustomerManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
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
 * @timestamp 2015-09-29 10:33:22
 */

@Scope("singleton")
@Controller("prmcustomer-add")
@Transactional
public class AddAction extends CommonAddAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);

    @Resource(name = "prmcustomer-manager")
    private PrmcustomerManager prmcustomerManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        //Do After
        return out;
    }

    @Override
    protected void afterAction(BasePojo dtoObj) {
        PrmCustomerDto prmCustomerDto = (PrmCustomerDto) dtoObj;
        String customerName = prmCustomerDto.getCustomerName();
        String uuid = prmCustomerDto.getUuid();
        Map accountCondition = new HashMap<>();
        accountCondition.put("accountInfoName", customerName);
        List<FadRtfreevalue> fadRtfreevalues = PersistenceFactory.getInstance().findByAnyFields(FadRtfreevalue.class, accountCondition, null);
        if (ListUtil.isEmpty(fadRtfreevalues)) {
            //nc表没有业主
            prmcustomerManager.sendMsg(uuid, customerName, "noCustomer");
        } else {
            //如果供应商表ncCode为空，发送消息，
            // 此功能暂时不需要
//                if (StringUtil.isEmpty(ncCode)) {
//                    prmcustomerManager.sendMsg(uuid, customerName,"noNcCode");
//                }
        }

    }
}
