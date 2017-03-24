package com.csnt.scdp.bizmodules.modules.workflow.fad.cashreq;

import com.csnt.scdp.bizmodules.entity.fad.FadCashReq;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.services.intf.SupplierinforManager;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wuhd on 2015/11/9.
 */
@Scope("singleton")
@Controller("scm_purchasepaymentrequestcontract-after-fixed")
@Transactional

public class PurchasePaymentRequestAfterComplete extends ERPWorkFlowBaseVariableCollectionAction {
    @Resource(name = "supplierinfor-manager")
    private SupplierinforManager supplierinforManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapResult = new HashMap();

        String uuid = (String) inMap.get("businessKey");
        FadCashReq fadCashReq = PersistenceFactory.getInstance().findByPK(FadCashReq.class, uuid);
        if ( !("0".equals(fadCashReq.getPayStyle()) || "2".equals(fadCashReq.getPayStyle()))) {
            supplierinforManager.generateSupplierByFlag(fadCashReq.getPayeeName(), fadCashReq.getPayeeBankName(), fadCashReq.getPayeeAccount(),"3");
        }
        return mapResult;
    }
}
