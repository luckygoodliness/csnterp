package com.csnt.scdp.bizmodules.modules.workflow.prm.projectmain;

import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMainC;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainCAttribute;
import com.csnt.scdp.bizmodules.modules.prm.prmexamperiod.services.intf.PrmExamPeriodManager;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.services.intf.PrmprojectmaincManager;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-27 15:28:19
 */

@Scope("singleton")
@Controller("prm_main-after-complete")
public class PrmMainApproveAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(PrmMainApproveAction.class);

    @Resource(name = "prmprojectmainc-manager")
    private PrmprojectmaincManager prmprojectmaincManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = new HashMap();
        String uuid = (String) inMap.get(WorkFlowAttribute.BUSINESS_KEY);
        String wfStatus = (String) inMap.get(WorkFlowAttribute.STATUS);
        if (WorkFlowAttribute.STATUS_FIXED.equals(wfStatus)) {
            prmprojectmaincManager.updateApproveFields(uuid);
            PrmProjectMainC prmProjectMainC = PersistenceFactory.getInstance().findByPK(PrmProjectMainC.class, uuid);
            Date auditDate = new Date();
            PrmExamPeriodManager prmExamPeriodManager = BeanFactory.getBean("prmexamperiod-manager".toLowerCase());
            Date examDate = prmExamPeriodManager.getExamDateFromAppointedDate(auditDate);
            prmProjectMainC.setAuditTime(auditDate);
            prmProjectMainC.setExamDate(examDate);
            PersistenceFactory.getInstance().update(prmProjectMainC);
        }
        out.put(PrmProjectMainCAttribute.PRM_PROJECT_MAIN_C_DTO, PersistenceFactory.getInstance().findByPK
                (PrmProjectMainC.class, uuid));
        //Do After
        return out;
    }

}
