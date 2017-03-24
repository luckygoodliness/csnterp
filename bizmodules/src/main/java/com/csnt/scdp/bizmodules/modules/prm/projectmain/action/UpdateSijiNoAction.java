package com.csnt.scdp.bizmodules.modules.prm.projectmain.action;

import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainAttribute;
import com.csnt.scdp.bizmodules.modules.prm.projectmain.services.intf.ProjectmainManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 13:30:43
 */

@Scope("singleton")
@Controller("projectmain-update-sijino")
@Transactional
public class UpdateSijiNoAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(UpdateSijiNoAction.class);

    @Resource(name = "projectmain-manager")
    private ProjectmainManager projectmainManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map out = new HashMap();
        //Do before
        String uuid = (String) inMap.get(CommonAttribute.UUID);
        String sijiNo = (String) inMap.get(PrmProjectMainAttribute.SIJI_NO);
        PrmProjectMain projectMain = PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, uuid);
        projectMain.setSijiNo(sijiNo);
        PersistenceFactory.getInstance().update(projectMain);
        //Do After
        return out;
    }
}
