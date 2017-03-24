package com.csnt.scdp.bizmodules.modules.workflow.scm.payreq;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.entity.fad.FadPayReqC;
import com.csnt.scdp.bizmodules.entity.fad.FadPayReqH;
import com.csnt.scdp.bizmodules.modules.fad.payreq.services.intf.PayreqManager;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.services.intf.SupplierinforManager;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.util.ListUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Wuhd on 2015/11/9.
 */
@Scope("singleton")
@Controller("scm_purchasepaymentrequestadjustinterimpayment-after-fixed")
@Transactional

public class ScmPurchaseInterimpaymentAfterComplete extends ERPWorkFlowBaseVariableCollectionAction {
    @Resource(name = "payreq-manager")
    private PayreqManager payreqManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapResult = new HashMap();
        String uuid = (String) inMap.get("businessKey");
        List<FadPayReqC> FadPayReqCLst = payreqManager.getFadPayReqCbyPuuid(uuid);
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        if (ListUtil.isNotEmpty(FadPayReqCLst)) {
            Calendar ca = Calendar.getInstance();
            FadPayReqCLst.stream().forEach(t -> {
                t.setCertificateCreateTime(ca.getTime());
                t.setState(BillStateAttribute.FAD_BILL_STATE_SENT);
                t.setAuditMoney(t.getAccountsPayable());
            });
            pcm.batchUpdate(FadPayReqCLst);
        }
        return mapResult;
    }
}
