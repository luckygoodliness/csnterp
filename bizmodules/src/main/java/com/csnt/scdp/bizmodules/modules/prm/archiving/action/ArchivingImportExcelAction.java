package com.csnt.scdp.bizmodules.modules.prm.archiving.action;

import com.csnt.scdp.bizmodules.modules.fad.invoice.services.impl.ImportInvoicesExcel;
import com.csnt.scdp.bizmodules.modules.prm.archiving.services.impl.ImportArchivingExcel;
import com.csnt.scdp.framework.commonaction.CommonFileUploadAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.Map;

/**
 * M3_C23_F1_归档明细导入
 * Created by xiezf on 2016/8/29.
 */
@Scope("singleton")
@Controller("archiving-importexcel")
@Transactional
public class ArchivingImportExcelAction extends CommonFileUploadAction {
    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map resultMap = new ImportArchivingExcel().doParseExcel(inMap);
        return resultMap;
    }
}
