package com.csnt.scdp.bizmodules.modules.scm.scmcontract.action;

import com.csnt.scdp.bizmodules.modules.scm.scmcontract.services.impl.ImportScmContractDetailExcelHelper;
import com.csnt.scdp.bizmodules.modules.scm.scmcontract.services.intf.ScmcontractManager;
import com.csnt.scdp.framework.commonaction.CommonFileUploadAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.Map;

@Scope("singleton")
@Controller("scmcontract-importexcel")
@Transactional
public class ImportExcelAction extends CommonFileUploadAction {
    private ILogTracer tracer = LogTracerFactory.getInstance(this.getClass());
    private ScmcontractManager scmcontractManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map resultMap = new ImportScmContractDetailExcelHelper().doParseExcel(inMap);
        return resultMap;
    }
}

