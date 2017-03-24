package com.csnt.scdp.bizmodules.modules.workflow.scm.scmcontractchange;

import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("scm_contract_revise-complete")
@Transactional

public class ScmContractChangeComplete extends ScmContractChangeStart {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapVar = super.perform(inMap);
        BigDecimal newValue = (BigDecimal) mapVar.get("newValue");
        BigDecimal originalValue = (BigDecimal) mapVar.get("originalValue");
        boolean condition1 = null == originalValue
                || originalValue.compareTo(new BigDecimal(0)) == 0
                || newValue.subtract(originalValue).compareTo(originalValue.multiply(new BigDecimal(0.05))) < 0
                || newValue.subtract(originalValue).compareTo(new BigDecimal(100000)) < 0;
        boolean condition2 = newValue.subtract(originalValue).abs().compareTo(originalValue.multiply(new BigDecimal(0.1))) > 0;
        mapVar.put("condition1", condition1);
        mapVar.put("condition2", condition2);
        return mapVar;
    }
}
