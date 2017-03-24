package com.csnt.scdp.bizmodules.modules.workflow.scm.scmnewcontractchange;

import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.services.intf.PrmpurchasereqManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Map;

/**
 * Created by Wuhd on 2015/11/9.
 */
@Scope("singleton")
@Controller("scm_purchase_change-after-start")
@Transactional

public class ScmNewContractChangeAfterStartAction implements IAction {
    @Resource(name = "prmpurchasereq-manager")
    private PrmpurchasereqManager prmpurchasereqManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map map = new HashedMap();
        String uuid = (String) inMap.get("businessKey");
        prmpurchasereqManager.checkPurchase(uuid,"1");
        return map;
    }
}
