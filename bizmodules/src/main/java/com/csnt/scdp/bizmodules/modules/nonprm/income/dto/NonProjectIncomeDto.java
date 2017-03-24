package com.csnt.scdp.bizmodules.modules.nonprm.income.dto;

import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectIncome;

import java.math.BigDecimal;
import java.util.List;


/**
 * Description:  NonProjectIncomeDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-24 17:37:27
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.nonprm.NonProjectIncome")
public class NonProjectIncomeDto extends NonProjectIncome {

    BigDecimal lastOccurredValue;

    public BigDecimal getLastOccurredValue() {
        return lastOccurredValue;
    }

    public void setLastOccurredValue(BigDecimal lastOccurredValue) {
        this.lastOccurredValue = lastOccurredValue;
    }
}