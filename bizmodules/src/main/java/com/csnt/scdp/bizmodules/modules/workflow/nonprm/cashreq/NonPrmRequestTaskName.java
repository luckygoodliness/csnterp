package com.csnt.scdp.bizmodules.modules.workflow.nonprm.cashreq;

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
@Controller("non_prm_request-process-taskname")
@Transactional

public class NonPrmRequestTaskName extends ERPWorkFlowBaseVariableCollectionAction {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        inMap.put("dto", "com.csnt.scdp.bizmodules.modules.fad.cashreq.dto.FadCashReqDto");
        Map dataInfo = super.perform(inMap);
        String runningNo = (String) dataInfo.get("runningNo");
        String subjectCode = (String) dataInfo.get("subjectCode");
        String officeId = (String) dataInfo.get("officeId");
        String orgName = (String) dataInfo.get("officeId");
        if (StringUtil.isNotEmpty(officeId)) {
            orgName = OrgHelper.getOrgNameByCode(officeId);
        }
        String userName = (String) inMap.get("start_user");
        Map mapResult = new HashMap();
        String state = (String) dataInfo.get("state");
        String stateDesc = ErpWorkFlowHelper.stateFlag(state);
        mapResult.put("name", stateDesc + "部门：" + orgName + "；提交人：" + userName + "；流水号：" + runningNo + "；费用科目：" + subjectCode);
        return mapResult;
    }
}
