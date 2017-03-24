package com.csnt.scdp.bizmodules.modules.workflow.scm.scmcontractchange;

import com.csnt.scdp.bizmodules.entity.asset.AssetDiscardApply;
import com.csnt.scdp.bizmodules.entity.scm.ScmContractChange;
import com.csnt.scdp.bizmodules.modules.asset.apply.services.intf.ApplyManager;
import com.csnt.scdp.bizmodules.modules.scm.scmcontract.services.intf.ScmcontractManager;
import com.csnt.scdp.bizmodules.modules.scm.scmcontractchange.services.intf.ScmcontractchangeManager;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("scm_contract_revise-after-fixed")
@Transactional

public class ScmContractChangeAfterCompleteAction extends ERPWorkFlowBaseVariableCollectionAction {

    @Resource(name = "scmcontractchange-manager")
    private ScmcontractchangeManager scmcontractchangeManager;

    @Resource(name = "scmcontract-manager")
    private ScmcontractManager scmcontractManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapResult = new HashMap();
        String uuid = (String) inMap.get("businessKey");
        ScmContractChange scmContractChange = PersistenceFactory.getInstance().findByPK(ScmContractChange.class, uuid);
        String scmContractId = scmContractChange.getScmContractId();
        if (StringUtil.isNotEmpty(scmContractId)) {
            scmcontractchangeManager.updateStateToApproved(uuid,scmContractId);
            scmcontractManager.allotContractMoney(scmContractId);
        }
        return mapResult;
    }
}
