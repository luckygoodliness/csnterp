package com.csnt.scdp.bizmodules.modules.fad.invoice.action;

import com.csnt.scdp.bizmodules.modules.fad.invoice.services.impl.ImportInvoicesExcel;
import com.csnt.scdp.framework.commonaction.CommonFileUploadAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.Map;

/**
 * M3_C12_F1_发票内容导入
 */
@Scope("singleton")
@Controller("prmfailyclaims-importexcel")
@Transactional
public class PrmFailyClaimsImportExcelAction extends CommonFileUploadAction{
    private ILogTracer tracer = LogTracerFactory.getInstance(this.getClass());

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map resultMap = new ImportInvoicesExcel().doParseExcel(inMap);
        return resultMap;
    }
}
