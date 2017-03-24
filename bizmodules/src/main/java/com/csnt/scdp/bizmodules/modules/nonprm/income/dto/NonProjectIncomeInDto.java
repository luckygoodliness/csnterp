package com.csnt.scdp.bizmodules.modules.nonprm.income.dto;

import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectIncomeIn;

import java.math.BigDecimal;
import java.util.List;


/**
 * Description:  NonProjectIncomeInDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-11-16 18:40:31
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.nonprm.NonProjectIncomeIn")
public class NonProjectIncomeInDto extends NonProjectIncomeIn {
    BigDecimal lastAppliedValue;

    public BigDecimal getLastAppliedValue() {
        return lastAppliedValue;
    }

    public void setLastAppliedValue(BigDecimal lastAppliedValue) {
        this.lastAppliedValue = lastAppliedValue;
    }

    BigDecimal lastOccurredValue;

    public BigDecimal getLastOccurredValue() {
        return lastOccurredValue;
    }

    public void setLastOccurredValue(BigDecimal lastOccurredValue) {
        this.lastOccurredValue = lastOccurredValue;
    }
}