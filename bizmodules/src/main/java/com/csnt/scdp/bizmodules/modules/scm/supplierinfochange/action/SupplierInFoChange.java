package com.csnt.scdp.bizmodules.modules.scm.supplierinfochange.action;

import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainCAttribute;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmProjectMainCDto;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.services.intf.PrmprojectmaincManager;
import com.csnt.scdp.bizmodules.modules.prm.projectmain.services.intf.ProjectmainManager;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfochange.dto.ScmSupplierCDto;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfochange.services.intf.SupplierinfochangeManager;
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
@Controller("supplierinfochange-supplierinfochange")
@Transactional
public class SupplierInFoChange implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(SupplierInFoChange.class);

    @Resource(name = "supplierinfochange-manager")
    private SupplierinfochangeManager supplierinfochangeManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        String uuid = (String) inMap.get("uuid");
        Map result = new HashMap<>();
        ScmSupplierCDto ppmcd = supplierinfochangeManager.scmSupplierCChange(uuid);
        result.put("scmSupplierCDto", ppmcd);
        return result;
    }
}
