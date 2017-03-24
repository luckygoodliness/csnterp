package com.csnt.scdp.bizmodules.modules.workflow.nonprm.cashdepositreq;

import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.helper.FMCodeHelper;
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
@Controller("non_prm_cashreq_deposit-process-taskname")
@Transactional

public class NonPrmCashreqDepositTaskName extends ERPWorkFlowBaseVariableCollectionAction {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        inMap.put("dto", "com.csnt.scdp.bizmodules.modules.fad.cashreq.dto.FadCashReqDto");
        Map dataInfo = super.perform(inMap);
        String projectName = (String) dataInfo.get("projectName");
        String officeId = (String) dataInfo.get("officeId");
        String userName = (String) inMap.get("start_user");
        String runningNo = (String) dataInfo.get("runningNo");
        String depositCharacteristic = (String) dataInfo.get("depositCharacteristic");
//        String depositType = (String)dataInfo.get("depositType");

        String orgName = null;
        if (StringUtil.isNotEmpty(officeId)) {
            orgName = OrgHelper.getOrgNameByCode(officeId);
        }
        String depositCharacteristicName = null;
        if (StringUtil.isNotEmpty(depositCharacteristic)) {
            depositCharacteristicName = FMCodeHelper.getDescByCode(depositCharacteristic, "FAD_DEPOSIT_CHARACTERISTIC");
        }
//        String depositTypeName=null;
//        if (StringUtil.isNotEmpty(depositType)) {
//            depositTypeName = FMCodeHelper.getDescByCode(depositType,"FAD_DEPOSIT_TYPE");
//        }
        String state = (String) dataInfo.get("state");
        String stateDesc = ErpWorkFlowHelper.stateFlag(state);
        Map mapResult = new HashMap();
        mapResult.put("name", stateDesc + "部门：" + orgName + "；提交人：" + userName + "；流水号：" + runningNo + "；保证金性质：" + depositCharacteristicName);


        return mapResult;
    }
}
