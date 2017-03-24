package com.csnt.scdp.bizmodules.modules.scm.supplierinfor.action;

import com.csnt.scdp.bizmodules.modules.scm.scmsupplierlimitchange.services.intf.ScmsupplierlimitchangeManager;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.services.intf.SupplierinforManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.StringUtil;
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
 * @timestamp 2015-09-26 15:32:42
 */

@Scope("singleton")
@Controller("supplierinfor-supplierNameValidate")
@Transactional
public class SupplierNameValidate implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(SupplierNameValidate.class);

    @Resource(name = "supplierinfor-manager")
    private SupplierinforManager supplierinforManager;
    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map rsMap = new HashMap();
        String completeName = (String) inMap.get("completeName");
        String simpleName = (String) inMap.get("simpleName");
        String supplierCode = (String) inMap.get("supplierCode");
        if (StringUtil.isEmpty(completeName)){
         completeName="";
        }
        if (StringUtil.isEmpty(supplierCode)){
            supplierCode="";
        }
        if (StringUtil.isEmpty(simpleName)){
            simpleName="";
        }
        supplierinforManager.supplierNameValidateMouseLeave(completeName, simpleName, supplierCode);
        return null;
    }
}
