package com.csnt.scdp.bizmodules.modules.fad.cashclearance.action;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.entity.fad.FadCashClearance;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCashClearanceAttribute;
import com.csnt.scdp.bizmodules.modules.fad.cashclearance.services.intf.CashclearanceManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 15:32:42
 */

@Scope("singleton")
@Controller("cashclearance-annul")
@Transactional
public class BtnAnnulAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(BtnAnnulAction.class);

    @Resource(name = "cashclearance-manager")
    private CashclearanceManager cashclearanceManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map rsMap = new HashMap();
        String uuid = (String) inMap.get("uuid");
        if (StringUtil.isNotEmpty(uuid)) {
            FadCashClearance fadCashClearance = PersistenceFactory.getInstance().findByPK(FadCashClearance.class, uuid);
            if (fadCashClearance != null) {
                if (BillStateAttribute.FAD_BILL_STATE_SENT.equals(fadCashClearance.getState())) {
                    throw new BizException("废止失败，凭证已经发送NC，请到凭证编辑界面做红冲。", new HashMap());
                } else {
                    fadCashClearance.setState(BillStateAttribute.FAD_BILL_STATE_DISABLED);
                    PersistenceFactory.getInstance().update(fadCashClearance);
                    rsMap.put(FadCashClearanceAttribute.RUNNING_NO, fadCashClearance.getRunningNo());
                }
            }
        }
        return rsMap;
    }
}
