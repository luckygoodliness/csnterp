package com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.action;

import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.services.impl.ImportProjectExcelHelper;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.services.intf.PrmprojectmaincManager;
import com.csnt.scdp.framework.commonaction.CommonFileUploadAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Scope("singleton")
@Controller("requirement-header-fileupload")
@Transactional
public class UploadAction extends CommonFileUploadAction {
    @Resource(name = "prmprojectmainc-manager")
    private PrmprojectmaincManager prmprojectmaincManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map resultMap = new ImportProjectExcelHelper().doParseExcel(inMap);
        List uploadData = (List) inMap.get("uploadMeta");
        String type = uploadData.get(1).toString();
        if (Boolean.TRUE.equals(resultMap.get("saveFlag"))) {
            String uuid = (String) resultMap.get("projectMainCUuid");
            if (StringUtil.isNotEmpty(uuid)) {
                prmprojectmaincManager.updateHeaderAndDetailAmount(uuid);
            }
        }
        return resultMap;
    }


}