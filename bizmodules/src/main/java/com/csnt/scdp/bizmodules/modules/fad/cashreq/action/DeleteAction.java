package com.csnt.scdp.bizmodules.modules.fad.cashreq.action;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.entity.fad.FadCashReq;
import com.csnt.scdp.bizmodules.modules.common.action.ErpDeleteAction;
import com.csnt.scdp.bizmodules.modules.fad.cashreq.services.intf.CashreqManager;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * Description:  DeleteAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-22 09:40:50
 */

@Scope("singleton")
@Controller("cashreq-delete")
@Transactional
public class DeleteAction extends ErpDeleteAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(DeleteAction.class);

    @Resource(name = "cashreq-manager")
    private CashreqManager cashreqManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {

        //以下校验数据的状态，若不是初始状态，则不能删除，将其从删除列表中移除
        List lstUuids = (List) super.getDeleteUuids(inMap);
        for (int i = 0; i < lstUuids.size(); i++) {
            FadCashReq obj = PersistenceFactory.getInstance().findByPK(FadCashReq.class, lstUuids.get(i).toString());
            if (obj != null) {
                if (!(obj.getState() == null
                        || BillStateAttribute.FAD_BILL_STATE_NEW.equals(obj.getState())
                        || BillStateAttribute.FAD_BILL_STATE_REJECTED.equals(obj.getState())
                        || BillStateAttribute.FAD_BILL_STATE_DISABLED.equals(obj.getState()))) {
                    lstUuids.remove(i);
                }
            }
        }

        //Do before
        Map out = super.perform(inMap);
        //Do After
        return out;
    }
}
