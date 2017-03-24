package com.csnt.scdp.bizmodules.modules.workflow.scm.adjustinterimpayment;

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
@Controller("scm_purchasepaymentrequestadjustinterimpayment-complete")
@Transactional

public class ScmPurchaseInterimpaymentAdjustComplete extends ScmPurchaseInterimpaymentAdjustStart {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapVar = super.perform(inMap);
        return mapVar;
    }
}
