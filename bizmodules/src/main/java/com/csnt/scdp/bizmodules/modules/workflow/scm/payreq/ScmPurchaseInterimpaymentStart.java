package com.csnt.scdp.bizmodules.modules.workflow.scm.payreq;

import com.csnt.scdp.bizmodules.modules.common.helper.ErpOrgHelper;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.helper.UserHelper;
import com.csnt.scdp.sysmodules.modules.sys.workflow.action.WorkFlowVariableProcessAction;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("scm_purchasepaymentrequestinterimpayment-start")
@Transactional

public class ScmPurchaseInterimpaymentStart extends ERPWorkFlowBaseVariableCollectionAction {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapData = super.perform(inMap);
        Map mapVar = new HashMap();
        String officeId = UserHelper.getOrgUuid();
        boolean isManagerDept = !ErpOrgHelper.isBizDivision(officeId);
        mapVar.put("isManagerDept", isManagerDept);
        mapVar.put("uuid", mapData.get("uuid"));
        mapVar.put("money", mapData.get("money"));
//        mapVar.put("originalValue", mapData.get("originalValue"));
        mapVar.putAll(ErpWorkFlowHelper.filterRoles(inMap, ErpWorkFlowHelper.loadUserInfoByUserID(inMap)));
        return mapVar;
    }
}
