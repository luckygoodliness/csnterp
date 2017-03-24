package com.csnt.scdp.bizmodules.modules.workflow.fad.payreq;

import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Wuhd on 2015/11/9.
 */
@Scope("singleton")
@Controller("scm_purchasepaymentrequestmonthpayment-complete")
@Transactional

public class PurchasePaymentRequestMonthPaymentComplete extends ERPWorkFlowBaseVariableCollectionAction {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapVar = ErpWorkFlowHelper.loadUserInfoByUserID(inMap);
        //todo 发布新流程要注意，老流程都已经审批完结，不然会有影响
//        String role1 = "公司领导";
//        List assigneeList = new ArrayList();
//        if (StringUtil.isNotEmpty(mapVar.get(role1))) {
//            assigneeList = StringUtil.splitAsList((String) mapVar.get(role1), ",");
//        }
//        String assignees = (String) inMap.get(WorkFlowAttribute.ASSIGNEE_LOWER);
//        if (StringUtil.isNotEmpty(assignees)) {
//            assigneeList = StringUtil.splitAsList(assignees, ";");
//        }
//        Map result = new HashMap();
//        result.put("assigneeList", assigneeList);
//        result.putAll(ErpWorkFlowHelper.filterRoles(inMap, mapVar));
        return mapVar;
    }
}
