package com.csnt.scdp.bizmodules.modules.prm.receipts.action;


import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.attributes.OriginalFormTypeAttribute;
import com.csnt.scdp.bizmodules.entity.fad.FadCashReq;
import com.csnt.scdp.bizmodules.entity.prm.PrmReceipts;
import com.csnt.scdp.bizmodules.modules.fad.cashreq.services.intf.CashreqManager;
import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
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


@Scope("singleton")
@Controller("receipts-certificate-create")
@Transactional
public class CertificateAction implements IAction {

    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map out = new HashMap();
        String uuid = (String) inMap.get("uuid");
        PrmReceipts prmReceipts = PersistenceFactory.getInstance().findByPK(PrmReceipts.class, uuid);
        if (prmReceipts != null && BillStateAttribute.CMD_BILL_STATE_APPROVED.equals(prmReceipts.getState())) {
            String fadCertificateUuid = certificateManager.javaBeanToCertificate(uuid, OriginalFormTypeAttribute.PRM_RECEIPT);
            if (StringUtil.isEmpty(fadCertificateUuid)) {
                out.put("result", false);
                out.put("msg", "凭证生成失败！");
            } else {
                out.put("result", true);
                out.put("fadCertificateUuid", fadCertificateUuid);
                prmReceipts.setState(BillStateAttribute.FAD_BILL_STATE_CERTIFICATED);
                PersistenceFactory.getInstance().update(prmReceipts);
            }
        } else {
            out.put("result", false);
            out.put("msg", "只有审批通过的单据才能生成凭证！");
        }
        return out;
    }
}
