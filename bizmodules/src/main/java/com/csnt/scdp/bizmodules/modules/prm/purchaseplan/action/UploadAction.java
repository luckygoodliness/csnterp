package com.csnt.scdp.bizmodules.modules.prm.purchaseplan.action;

import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.services.impl.ImportDetailExcelHelper;
import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.services.intf.PurchaseplanManager;
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
@Controller("purchaseplan-detail-fileupload")
@Transactional
public class UploadAction extends CommonFileUploadAction {
    @Resource(name = "purchaseplan-manager")
    private PurchaseplanManager prmPurchasePlanManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        List uploadData = (List) inMap.get("uploadMeta");
        String packageUuid = StringUtil.replaceNull(uploadData.get(0));

        Map resultMap = new ImportDetailExcelHelper().doParseExcel(inMap);

        prmPurchasePlanManager.setPackageDetailRefId(resultMap, packageUuid);
        return resultMap;
    }


}