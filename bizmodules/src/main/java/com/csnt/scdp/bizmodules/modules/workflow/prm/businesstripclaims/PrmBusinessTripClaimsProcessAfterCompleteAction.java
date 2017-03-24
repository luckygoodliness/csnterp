package com.csnt.scdp.bizmodules.modules.workflow.prm.businesstripclaims;

import com.csnt.scdp.bizmodules.entity.fad.FadInvoice;
import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.services.intf.SupplierinforManager;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
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
@Controller("prm_businesstrip_claims-after-fixed")
@Transactional

public class PrmBusinessTripClaimsProcessAfterCompleteAction extends ERPWorkFlowBaseVariableCollectionAction {

    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;
    @Resource(name = "supplierinfor-manager")
    private SupplierinforManager supplierinforManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapResult = new HashMap();
        String uuid = (String) inMap.get("businessKey");
        FadInvoice fadInvoice = PersistenceFactory.getInstance().findByPK(FadInvoice.class, uuid);
        if (!("0".equals(fadInvoice.getPayStyle()) || "2".equals(fadInvoice.getPayStyle()))&&StringUtil.isNotEmpty(fadInvoice.getSupplierName())) {
            supplierinforManager.generateSupplierByFlag(fadInvoice.getSupplierName(), fadInvoice.getBank(), fadInvoice.getBankId(), "3");
        }
        return mapResult;
    }
}
