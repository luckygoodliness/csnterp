package com.csnt.scdp.bizmodules.modules.workflow.nonprm.cashdepositreq;

import com.csnt.scdp.bizmodules.attributes.OriginalFormTypeAttribute;
import com.csnt.scdp.bizmodules.entity.fad.FadCashReq;
import com.csnt.scdp.bizmodules.entity.fad.FadRtfreevalue;
import com.csnt.scdp.bizmodules.modules.fad.cashreq.services.intf.CashreqManager;
import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.services.intf.SupplierinforManager;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.UserHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("non_prm_cashreq_deposit-after-fixed")
@Transactional

public class NonPrmCashreqDepositAfterCompleteAction extends ERPWorkFlowBaseVariableCollectionAction {

    @Resource(name = "supplierinfor-manager")
    private SupplierinforManager supplierinforManager;

    @Resource(name = "cashreq-manager")
    private CashreqManager cashreqManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapResult = new HashMap();
        Map mapData = super.perform(inMap);
        String payeeName = (String) mapData.get("payeeName");
        String uuid = (String) inMap.get("businessKey");
        FadCashReq fadCashReq = PersistenceFactory.getInstance().findByPK(FadCashReq.class, uuid);
        if (!("0".equals(fadCashReq.getPayStyle()) || "2".equals(fadCashReq.getPayStyle()))) {
            supplierinforManager.generateSupplierByFlag(fadCashReq.getPayeeName(), fadCashReq.getPayeeBankName(), fadCashReq.getPayeeAccount(), "4");
        }
        Map accountCondition = new HashMap<>();
        accountCondition.put("accountInfoName", payeeName);
        List<FadRtfreevalue> fadRtfreevalues = PersistenceFactory.getInstance().findByAnyFields(FadRtfreevalue.class, accountCondition, null);
        if (ListUtil.isEmpty(fadRtfreevalues)) {
            //nc表没有业主
            cashreqManager.sendMsg(uuid, payeeName, "noDepositCustomer");
        }

        return mapResult;
    }
}
