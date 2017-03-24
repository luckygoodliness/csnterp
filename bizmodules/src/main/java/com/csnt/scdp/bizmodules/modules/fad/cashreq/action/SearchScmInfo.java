package com.csnt.scdp.bizmodules.modules.fad.cashreq.action;

import com.csnt.scdp.bizmodules.entity.scm.ScmContract;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCashReqAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmContractAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmSupplierAttribute;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.services.intf.SupplierinforManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/1.
 */
@Scope("singleton")
@Controller("scminfo-search")
@Transactional
public class SearchScmInfo implements IAction {
    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map result = new HashedMap();
        String scmContractId = (String) inMap.get(FadCashReqAttribute.PURCHASE_CONTRACT_ID);
        if (StringUtil.isNotEmpty(scmContractId)) {
            ScmContract scmContract = PersistenceFactory.getInstance().findByPK(ScmContract.class,scmContractId);
            result.put("scmContract", scmContract);
        }
        return result;
    }
}
