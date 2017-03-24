package com.csnt.scdp.bizmodules.modules.nonprm.budget.dto;

import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetC;

import java.math.BigDecimal;
import java.util.List;


/**
 * Description:  NonProjectBudgetCDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-24 13:22:12
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetC")
public class NonProjectBudgetCDto extends NonProjectBudgetC {

    //前端显示费用名称
    private String subjectName;

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    //前端显示费用代码
    private String subjectCode;

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    //预算金额
    private BigDecimal budgetAmount;

    public BigDecimal getBudgetAmount() {
        BigDecimal thisYearAssigned = getThisYearAssigned() == null ? BigDecimal.ZERO : getThisYearAssigned();
        BigDecimal thisYearChanged = getThisYearChanged() == null ? BigDecimal.ZERO : getThisYearChanged();
        budgetAmount = thisYearAssigned.add(thisYearChanged);
        return budgetAmount;
    }

    private BigDecimal thisYearLocked;

    public BigDecimal getThisYearLocked() {
        return thisYearLocked;
    }

    public void setThisYearLocked(BigDecimal thisYearLocked) {
        this.thisYearLocked = thisYearLocked;
    }
}