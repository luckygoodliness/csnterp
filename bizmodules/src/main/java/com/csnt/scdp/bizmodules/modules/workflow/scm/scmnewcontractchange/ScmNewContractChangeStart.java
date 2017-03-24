package com.csnt.scdp.bizmodules.modules.workflow.scm.scmnewcontractchange;

import com.csnt.scdp.bizmodules.modules.common.helper.ErpUserHelper;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.helper.UserHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("scm_purchase_change-start")
@Transactional

public class ScmNewContractChangeStart extends ERPWorkFlowBaseVariableCollectionAction {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapData = super.perform(inMap);
        Map mapVar = new HashMap();
        mapVar.put("newValue", mapData.get("newValue"));
        mapVar.put("originalValue", mapData.get("originalValue"));
        mapVar.putAll(ErpWorkFlowHelper.filterRoles(inMap, ErpWorkFlowHelper.loadUserInfoByUserID(inMap)));
        List<String> roles = ErpUserHelper.getUserRoleInfo(UserHelper.getUserId());
        if (roles.contains("供应链部采购经理")) {
            mapVar.put("isManager", 1);
        } else {
            mapVar.put("isManager", 0);
        }
        return mapVar;
    }
}
