package com.csnt.scdp.bizmodules.modules.workflow.prm.purchaseplan;

import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.ListUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("prm_purchase_plan-process-taskname")
@Transactional

public class PrmPurchasePlanProcessTaskName extends ERPWorkFlowBaseVariableCollectionAction {
    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        inMap.put("dto", "com.csnt.scdp.bizmodules.modules.prm.purchaseplan.dto.PrmPurchasePlanDto");
        Map dataInfo = super.perform(inMap);
        Map mapResult = new HashMap();
        String state=(String)dataInfo.get("purchasePlanState");
        String stateDesc= ErpWorkFlowHelper.stateFlag(state);
        mapResult.put("name",stateDesc+ "所属项目：" + dataInfo.get("projectName"));
        return mapResult;
    }
}
