package com.csnt.scdp.bizmodules.modules.workflow.nonprm.budget;

import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetAjustC;
import com.csnt.scdp.bizmodules.entity.nonprm.SubjectSubject;
import com.csnt.scdp.bizmodules.entityattributes.nonprm.NonProjectBudgetAjustCAttribute;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.bo.QueryCondition;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.StringUtil;
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
@Controller("non_prm_budget_plan_apply-complete")
@Transactional

public class NonPrmBudgetComplete extends ERPWorkFlowBaseVariableCollectionAction {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map roledata = ErpWorkFlowHelper.loadUserInfoByUserID(inMap);
//        String role1 = "公司领导";
//        List assigneeList = new ArrayList();
//        if (StringUtil.isNotEmpty(roledata.get(role1))) {
//            assigneeList = StringUtil.splitAsList((String) roledata.get(role1), ",");
//        }
//        String assignees = (String) inMap.get(WorkFlowAttribute.ASSIGNEE_LOWER);
//        if(StringUtil.isNotEmpty(assignees)){
//            assigneeList= StringUtil.splitAsList(assignees, ";");
//        }
        Map mapVar = new HashMap();
//        mapVar.put("assigneeList", assigneeList);
        mapVar.putAll(ErpWorkFlowHelper.filterRoles(inMap, roledata));
        return mapVar;
    }
}
