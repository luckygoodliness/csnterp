package com.csnt.scdp.bizmodules.modules.mobileservice.fad.cashreq;

import com.csnt.scdp.bizmodules.modules.fad.cashreq.services.intf.CashreqManager;
import com.csnt.scdp.bizmodules.modules.mobileservice.ERPMobileTerminalBaseVariableCollectionAction;
import com.csnt.scdp.bizmodules.modules.nonprm.subjectsubject.services.intf.SubjectsubjectManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lenovo on 2016/8/23.
 */
@Scope("singleton")
@Controller("mobile-terminal-start-non_prm_cashreq_deposit")
@Transactional
public class FadCashReqStart extends ERPMobileTerminalBaseVariableCollectionAction {
    @Resource(name = "cashreq-manager")
    private CashreqManager cashreqManager;

    @Override
    public Map perform(Map map) throws BizException, SysException {
        Map resultMap = new HashMap<>();
        resultMap.put("checkType","start");
        String uuid = (String) map.get(WorkFlowAttribute.BUSINESS_KEY);
        Map result = cashreqManager.validateCashReqDepositBeforeWorkFlowCommitOfStart(uuid);
        boolean sucessFlag = (boolean) result.get(CommonAttribute.SUCCESS);
        if (sucessFlag) {
            resultMap.put("result", true);
            resultMap.put("message", "提交成功");
        } else {
            resultMap.put("result", false);
            resultMap.put("message", result.get("message"));
        }
        return resultMap;
    }
}
