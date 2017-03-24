package com.csnt.scdp.bizmodules.modules.nonprm.nonprmpurchasereq.action;


import com.csnt.scdp.bizmodules.modules.nonprm.nonprmpurchasereq.services.impl.ImportReqDetailExcelHelper;
import com.csnt.scdp.framework.commonaction.CommonFileUploadAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Map;

@Scope("singleton")
@Controller("nonprmpurchasereq-importexcel")
@Transactional
public class ImportExcelAction extends CommonFileUploadAction {
    private ILogTracer tracer = LogTracerFactory.getInstance(this.getClass());

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map resultMap = new ImportReqDetailExcelHelper().doParseExcel(inMap);
        return resultMap;
    }
}

