package com.csnt.scdp.bizmodules.modules.nonprm.monitorm.dto;

import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectIncomeMon;

import java.math.BigDecimal;
import java.util.List;


/**
 * Description:  NonProjectIncomeMonDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-11-16 14:11:04
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.nonprm.NonProjectIncomeMon")
public class NonProjectIncomeMonDto extends NonProjectIncomeMon {
    private BigDecimal assignedValue;

    public BigDecimal getAssignedValue() {
        return assignedValue;
    }

    public void setAssignedValue(BigDecimal assignedValue) {
        this.assignedValue = assignedValue;
    }
}