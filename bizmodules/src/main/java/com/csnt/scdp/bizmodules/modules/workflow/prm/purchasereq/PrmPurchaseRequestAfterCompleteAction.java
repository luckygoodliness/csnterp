package com.csnt.scdp.bizmodules.modules.workflow.prm.purchasereq;

import com.csnt.scdp.bizmodules.attributes.OriginalFormTypeAttribute;
import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.services.intf.PrmpurchasereqManager;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.helper.UserHelper;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("prm_purchase_request-after-fixed")
@Transactional

public class PrmPurchaseRequestAfterCompleteAction extends ERPWorkFlowBaseVariableCollectionAction {

    @Resource(name = "prmpurchasereq-manager")
    private PrmpurchasereqManager prmpurchasereqManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map dataInfo = super.perform(inMap);
        Map mapResult = new HashMap();
        String uuid = (String) inMap.get("businessKey");
        Integer isInner = (Integer) inMap.get("isInner");
        if (isInner == null || isInner != 1) {
            prmpurchasereqManager.audit(uuid);
        }
        //如果运管部主任提交流程，发消息，运管部主任是usertask3，修改流程图要注意
        String taskDefKey = (String) inMap.get("taskDefKey");
        if ("usertask3".equals(taskDefKey)) {
            String officeId = dataInfo.get("officeId") == null ? "" : (String) dataInfo.get("officeId");
            String purchaseReqNo = dataInfo.get("purchaseReqNo") == null ? "" : (String) dataInfo.get("purchaseReqNo");
            if (StringUtil.isNotEmpty(officeId) && StringUtil.isNotEmpty(purchaseReqNo)) {
                prmpurchasereqManager.sendMsg(uuid, purchaseReqNo, officeId);
            }
        }
        return mapResult;
    }
}
