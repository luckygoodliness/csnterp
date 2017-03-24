package com.csnt.scdp.bizmodules.modules.workflow.asset.card;

import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.sysmodules.modules.sys.workflow.action.WorkFlowVariableProcessAction;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by Wuhd on 2015/11/9.
 */
@Scope("singleton")
@Controller("asset_register-start")
@Transactional

public class AssetRegisterStart extends ERPWorkFlowBaseVariableCollectionAction {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map dataInfo = super.perform(inMap);
        String deviceType = (String) dataInfo.get("deviceType");
        String endUserCode = (String) dataInfo.get("endUserCode");
        Map mapVar = ErpWorkFlowHelper.filterRoles(inMap, ErpWorkFlowHelper.loadUserInfoByUserID(inMap));
        mapVar.put("deviceType", deviceType);
        mapVar.put("endUserCode", endUserCode);
        mapVar.put("isEasyConsumed", ("DZYH-BG").equals(deviceType) || ("DZYH-SC").equals(deviceType) ? 1 : 0);
        return mapVar;
    }
}
