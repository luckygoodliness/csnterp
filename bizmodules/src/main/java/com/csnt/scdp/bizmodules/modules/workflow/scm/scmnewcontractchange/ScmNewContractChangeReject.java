package com.csnt.scdp.bizmodules.modules.workflow.scm.scmnewcontractchange;

import com.csnt.scdp.bizmodules.modules.workflow.scm.scmcontractchange.ScmContractChangeComplete;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("scm_purchase_change-reject")
@Transactional

public class ScmNewContractChangeReject extends ScmContractChangeComplete {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapVar = super.perform(inMap);
        return mapVar;
    }
}
