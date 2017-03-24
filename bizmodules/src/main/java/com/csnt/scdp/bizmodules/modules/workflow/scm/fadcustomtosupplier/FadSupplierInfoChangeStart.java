package com.csnt.scdp.bizmodules.modules.workflow.scm.fadcustomtosupplier;

import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
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
@Controller("fad_supplier_in_fo_change-start")
@Transactional

public class FadSupplierInfoChangeStart extends ERPWorkFlowBaseVariableCollectionAction {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map roleMap = ErpWorkFlowHelper.filterRoles(inMap, ErpWorkFlowHelper.loadUserInfoByUserID(inMap));
        Map dataInfo = super.perform(inMap);
        String supplierGenre = (String) dataInfo.get("supplierGenre");
        roleMap.put("supplierGenre", supplierGenre);
        return roleMap;
    }
}
