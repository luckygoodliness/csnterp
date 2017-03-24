package com.csnt.scdp.bizmodules.modules.workflow;

import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wuhd on 2015/11/9.
 */
@Scope("singleton")
@Controller("work-flow-collect-common-variable")
@Transactional

public class WorkFlowCollectCommonVariable implements IAction {

    //取角色对应的userid
    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map roleMap = ErpWorkFlowHelper.loadUserInfoByUserID(inMap);
        return ErpWorkFlowHelper.filterRoles(inMap, roleMap);
    }
}
