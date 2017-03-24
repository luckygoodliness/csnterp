package com.csnt.scdp.bizmodules.modules.workflow.nonprm.purchasereq;

import com.csnt.scdp.bizmodules.modules.common.helper.ErpUserHelper;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("non_prm_purchase_request-process-taskname")
@Transactional

public class NonPrmPruchaseRequestTaskName extends ERPWorkFlowBaseVariableCollectionAction {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        inMap.put("dto", "com.csnt.scdp.bizmodules.modules.nonprm.nonprmpurchasereq.dto.PrmPurchaseReqDto");
        Map dataInfo = super.perform(inMap);
        String purchaseReqNo = (String) dataInfo.get("purchaseReqNo");
        String orgCode = (String) dataInfo.get("departmentCode");
        String userName = (String) inMap.get("start_user");
        String orgName = null;
        String subjectCode = (String) dataInfo.get("subjectCode");
        if (StringUtil.isNotEmpty(orgCode)) {
            orgName = OrgHelper.getOrgNameByCode(orgCode);
        }
        Map mapResult = new HashMap();
        String state = (String) dataInfo.get("state");
        String stateDesc = ErpWorkFlowHelper.stateFlag(state);
        mapResult.put("name", stateDesc + "部门：" + orgName + "；提交人：" + userName + "；申请单号：" + purchaseReqNo + "；费用科目：" + subjectCode);
        dataInfo.putAll(mapResult);
        return dataInfo;
    }
}
