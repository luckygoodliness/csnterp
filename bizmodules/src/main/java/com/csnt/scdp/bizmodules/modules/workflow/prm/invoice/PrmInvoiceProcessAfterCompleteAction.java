package com.csnt.scdp.bizmodules.modules.workflow.prm.invoice;

import com.csnt.scdp.bizmodules.attributes.MessageTemplateNo;
import com.csnt.scdp.bizmodules.attributes.OriginalFormTypeAttribute;
import com.csnt.scdp.bizmodules.entity.prm.PrmBilling;
import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
import com.csnt.scdp.bizmodules.modules.prm.prmbilling.services.intf.PrmbillingManager;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.UserHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entityattributes.ScdpMsgAttribute;
import com.csnt.scdp.sysmodules.helper.MsgSendHelper;
import com.csnt.scdp.sysmodules.modules.sys.msgcenter.dto.ReceiptsMeta;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("prm_invoice-after-fixed")
@Transactional

public class PrmInvoiceProcessAfterCompleteAction extends ERPWorkFlowBaseVariableCollectionAction {

    @Resource(name = "prmbilling-manager")
    private PrmbillingManager prmbillingManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapResult = new HashMap();
        String uuid = (String) inMap.get("businessKey");
         /*String userId = UserHelper.getUserId();
        String uuidStrReturn = certificateManager.javaBeanToCertificate(uuid, OriginalFormTypeAttribute.PRM_BILLING);
        if(StringUtil.isEmpty(uuidStrReturn)){
            throw new BizException("凭证生成失败");
        }*/
        //todo
        prmbillingManager.updateCustomerAfterBilling(uuid);
        return mapResult;
    }


}
