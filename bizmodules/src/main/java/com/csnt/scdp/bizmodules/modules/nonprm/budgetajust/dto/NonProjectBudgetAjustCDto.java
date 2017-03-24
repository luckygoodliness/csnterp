package com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.dto;

import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetAjustC;
import com.csnt.scdp.framework.util.MathUtil;

import java.math.BigDecimal;
import java.util.List;


/**
 * Description:  NonProjectBudgetAjustCDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-25 16:43:12
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetAjustC")
public class NonProjectBudgetAjustCDto extends NonProjectBudgetAjustC {

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

    public BigDecimal getAfterAdjustMoney() {
        return MathUtil.add(this.getOrrigalBudgetAssigned(), this.getBudgetChanged());
    }

    private BigDecimal afterAdjustMoney;
}