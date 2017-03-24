package com.csnt.scdp.bizmodules.modules.workflow.prm.purchasereq;

import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.sysmodules.modules.sys.workflow.action.WorkFlowVariableProcessAction;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("prm_purchase_request-complete")
@Transactional

public class PrmPurchaseRequestComplete extends PrmPurchaseRequestStart {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        return super.perform(inMap);
    }
}
