package com.csnt.scdp.bizmodules.modules.workflow.scm.supplierinfo;

import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.sysmodules.modules.sys.workflow.action.WorkFlowVariableProcessAction;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("scm_supplier_apporve-start")
@Transactional

public class ScmSupplierApporveStart extends ERPWorkFlowBaseVariableCollectionAction {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map roleMap = ErpWorkFlowHelper.filterRoles(inMap, ErpWorkFlowHelper.loadUserInfoByUserID(inMap));
        roleMap.put("isSupplier",true);
        return roleMap;
    }
}
