package com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.action;

import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainCAttribute;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmProjectMainCDto;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.services.intf.PrmprojectmaincManager;
import com.csnt.scdp.bizmodules.modules.prm.projectmain.services.intf.ProjectmainManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  LoadAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-27 15:28:19
 */

@Scope("singleton")
@Controller("wrapper-projectmain-to-change")
@Transactional
public class WrapperProjectMainToChange implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(WrapperProjectMainToChange.class);

    @Resource(name = "prmprojectmainc-manager")
    private PrmprojectmaincManager prmprojectmaincManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        String uuid = (String) inMap.get("uuid");
        String wrapperItem = (String) inMap.get(PrmProjectMainAttribute.PRM_PROJECT_DETAIL_TYPE);
        Map result = new HashMap<>();
        PrmProjectMainCDto ppmcd = prmprojectmaincManager.wrapperProjectMainToChange(uuid, wrapperItem);
        result.put(PrmProjectMainCAttribute.PRM_PROJECT_MAIN_C_DTO, ppmcd);

        ProjectmainManager projectmainManager = BeanFactory.getBean("projectmain-manager");
        List<String> uuids = projectmainManager.getRelatedContractUuidsFromMain(uuid);
        result.put("mainContractUuids", uuids);

        return result;
    }
}
