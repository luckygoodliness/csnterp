package com.csnt.scdp.bizmodules.modules.workflow.fad.cashclearance;

import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
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
@Controller("fin_cash_offset-process-taskname")
@Transactional

public class CashClearanceTaskName extends ERPWorkFlowBaseVariableCollectionAction {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        inMap.put("dto", "com.csnt.scdp.bizmodules.modules.fad.cashclearance.dto.FadCashClearanceDto");
        Map dataInfo = super.perform(inMap);
        String name = "";
        String runningNo = (String) dataInfo.get("runningNo");
        if (StringUtil.isNotEmpty(runningNo)) {
            name = name + "  流水号：" + runningNo;
        }
        String state = (String) dataInfo.get("state");
        String stateDesc = ErpWorkFlowHelper.stateFlag(state);
        Map mapResult = new HashMap();
        mapResult.put("name", stateDesc + name);
        return mapResult;
    }
}
