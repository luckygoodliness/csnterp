package com.csnt.scdp.bizmodules.modules.workflow.prm.finalestimate;

import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("prm_final_estimates-complete")
@Transactional

public class FinalEstimateComplete extends ERPWorkFlowBaseVariableCollectionAction {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map dataMap = ErpWorkFlowHelper.loadUserInfoByUserID(inMap);
        Map dataInfo = super.perform(inMap);
        String squareType=(String) dataInfo.get("squareType");
        Map mapVar = new HashMap();
        mapVar.put("squareType",null==squareType?0:squareType);
        String tempUsers = (String) dataMap.get("运管部运营主管");
        String assignees = (String) inMap.get(WorkFlowAttribute.ASSIGNEE_LOWER);
        if(StringUtil.isNotEmpty(tempUsers)){
            mapVar.put("assigneeList", StringUtil.splitAsList(tempUsers, ","));
        }
        if(StringUtil.isNotEmpty(assignees)){
            mapVar.put("assigneeList", StringUtil.splitAsList(assignees, ";"));
        }
        mapVar.putAll(ErpWorkFlowHelper.filterRoles(inMap, dataMap));
        return mapVar;
    }
}
