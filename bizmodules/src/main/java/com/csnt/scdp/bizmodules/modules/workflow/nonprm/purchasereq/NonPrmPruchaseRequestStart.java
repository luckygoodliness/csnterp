package com.csnt.scdp.bizmodules.modules.workflow.nonprm.purchasereq;

import com.csnt.scdp.bizmodules.entityattributes.fad.FadCashReqAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmPurchaseReqAttribute;
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
 * Created by Wuhd on 2015/11/9.
 */
@Scope("singleton")
@Controller("non_prm_purchase_request-start")
@Transactional

public class NonPrmPruchaseRequestStart extends ERPWorkFlowBaseVariableCollectionAction {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapData = super.perform(inMap);
        Map mapVar = new HashMap();

        mapVar.put(PrmPurchaseReqAttribute.FINANCIAL_SUBJECT_CODE, mapData.get(FadCashReqAttribute.SUBJECT_CODE));
//        mapVar.put("money",mapData.get(FadCashReqAttribute.MONEY));
        mapVar.put("uuid",mapData.get("uuid"));
        mapVar.put("deptCode", UserHelper.getOrgCode());
        mapVar.putAll(ErpWorkFlowHelper.filterRoles(inMap, ErpWorkFlowHelper.loadUserInfoByUserID(inMap)));
        return mapVar;
    }
}
