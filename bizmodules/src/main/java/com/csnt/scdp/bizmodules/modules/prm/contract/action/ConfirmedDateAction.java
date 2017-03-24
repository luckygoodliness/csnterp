package com.csnt.scdp.bizmodules.modules.prm.contract.action;


import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.attributes.OriginalFormTypeAttribute;
import com.csnt.scdp.bizmodules.entity.prm.PrmContract;
import com.csnt.scdp.bizmodules.entity.prm.PrmReceipts;
import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
import com.csnt.scdp.bizmodules.modules.prm.prmexamperiod.services.intf.PrmExamPeriodManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Scope("singleton")
@Controller("confirm-date")
@Transactional
public class ConfirmedDateAction implements IAction {

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map out = new HashMap();
        String uuid = (String) inMap.get("uuid");
        PrmContract prmContract = PersistenceFactory.getInstance().findByPK(PrmContract.class, uuid);
        if (prmContract != null) {
            Date curDate = new Date();
            prmContract.setConfirmedDate(curDate);
            if (prmContract.getExamDate() == null) {
                PrmExamPeriodManager prmExamPeriodManager = BeanFactory.getBean("prmexamperiod-manager".toLowerCase());
                Date examDate = prmExamPeriodManager.getExamDateFromAppointedDate(curDate);
                prmContract.setExamDate(examDate);
            }
            PersistenceFactory.getInstance().update(prmContract);
            out.put("result", true);
        }
        return out;
    }
}
