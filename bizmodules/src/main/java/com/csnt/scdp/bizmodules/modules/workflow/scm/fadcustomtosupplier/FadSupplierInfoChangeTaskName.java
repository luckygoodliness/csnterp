package com.csnt.scdp.bizmodules.modules.workflow.scm.fadcustomtosupplier;

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
 * Created by kangyb on 2016/8/8.
 */

@Scope("singleton")
@Controller("fad_supplier_in_fo_change-process-taskname")
@Transactional
public class FadSupplierInfoChangeTaskName extends ERPWorkFlowBaseVariableCollectionAction {
    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        inMap.put("dto", "com.csnt.scdp.bizmodules.modules.scm.supplierinfochange.dto.ScmSupplierCDto");
        Map dataInfo = super.perform(inMap);
        String supplierCode = (String) dataInfo.get("supplierCode");
        Map mapResult = new HashMap();
        String state = (String) dataInfo.get("state");
        String stateDesc = ErpWorkFlowHelper.stateFlag(state);
        mapResult.put("name", stateDesc + "供应商编码" + supplierCode);
        return mapResult;
    }


}
