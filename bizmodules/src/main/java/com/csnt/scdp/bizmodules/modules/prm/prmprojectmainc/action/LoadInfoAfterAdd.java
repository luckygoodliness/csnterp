package com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.action;

import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.services.intf.PrmprojectmaincManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.DAOHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  QueryAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 10:37:43
 */

@Scope("singleton")
@Controller("prm-project-mainc-new-after-add")
@Transactional
public class LoadInfoAfterAdd implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadInfoAfterAdd.class);

    @Resource(name = "prmprojectmainc-manager")
    private PrmprojectmaincManager prmprojectmaincManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        List lstParam = new ArrayList();
//        String subjectType = (String) inMap.get(FinancialSubjectAttribute.SUBJECT_TYPE);
//        lstParam.add(subjectType);
        DAOMeta daoMeta = DAOHelper.getDAO("financialsubject-dao", "fm_financial_subject_no", lstParam);
        Map out = new HashMap();
        out.put("subjectCodes", PersistenceFactory.getInstance().findByNativeSQL(daoMeta));
        out.put("projectManagerRoleUuid", prmprojectmaincManager.getProjectManagerRoleUuid());
        return out;
    }
}
