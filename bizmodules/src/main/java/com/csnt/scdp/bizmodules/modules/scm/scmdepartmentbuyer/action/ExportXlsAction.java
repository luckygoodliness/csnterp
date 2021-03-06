package com.csnt.scdp.bizmodules.modules.scm.scmdepartmentbuyer.action;

import com.csnt.scdp.bizmodules.modules.scm.scmdepartmentbuyer.services.intf.ScmdepartmentbuyerManager;
import com.csnt.scdp.framework.commonaction.crud.CommonExportXlsAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Map;

/**
 * Description:  ExportXlsAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-11-23 11:06:51
 */

@Scope("singleton")
@Controller("scmdepartmentbuyer-exportxls")
@Transactional
public class ExportXlsAction extends CommonExportXlsAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ExportXlsAction.class);

    @Resource(name = "scmdepartmentbuyer-manager")
    private ScmdepartmentbuyerManager scmdepartmentbuyerManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        //Do After
        return out;
    }
}
