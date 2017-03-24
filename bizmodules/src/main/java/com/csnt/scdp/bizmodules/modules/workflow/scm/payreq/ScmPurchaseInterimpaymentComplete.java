package com.csnt.scdp.bizmodules.modules.workflow.scm.payreq;

import com.csnt.scdp.bizmodules.entity.fad.FadPayReqC;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("scm_purchasepaymentrequestinterimpayment-complete")
@Transactional

public class ScmPurchaseInterimpaymentComplete extends ScmPurchaseInterimpaymentStart {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapVar = super.perform(inMap);
        String puuid = (String) mapVar.get("uuid");
        Map condition = new HashMap<>();
        condition.put("puuid", puuid);
        List<FadPayReqC> fadPayReqCList = PersistenceFactory.getInstance().findByAnyFields(FadPayReqC.class, condition, null);
        BigDecimal amount = new BigDecimal(0);
        if (null != fadPayReqCList) {
            for (int i = 0; i < fadPayReqCList.size(); i++) {
                BigDecimal money = fadPayReqCList.get(i).getAuditMoney()==null?new BigDecimal(0):fadPayReqCList.get(i).getAuditMoney();
                amount = amount.add(money);
            }
        }
        mapVar.put("amount",amount);
        return mapVar;
    }
}
