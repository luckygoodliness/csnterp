package com.csnt.scdp.bizmodules.modules.workflow.prm.contract;

import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("prm_contract_management-process-taskname")
@Transactional

public class PrmContractManagementProcessTaskName extends ERPWorkFlowBaseVariableCollectionAction {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        inMap.put("dto", "com.csnt.scdp.bizmodules.modules.prm.contractc.dto.PrmContractCDto");
        Map dataInfo = super.perform(inMap);
        String contractName = (String) dataInfo.get("contractName");
        Map mapVar = new HashMap();
        String state = (String) dataInfo.get("state");
        String stateDesc = ErpWorkFlowHelper.stateFlag(state);
        mapVar.put("name", stateDesc + "合同名称：" + contractName);
        return mapVar;
    }
}
