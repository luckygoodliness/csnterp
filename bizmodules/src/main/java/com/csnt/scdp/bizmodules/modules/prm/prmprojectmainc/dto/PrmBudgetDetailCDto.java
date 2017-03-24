package com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto;

import com.csnt.scdp.bizmodules.entity.prm.PrmBudgetDetailC;
import com.csnt.scdp.framework.dto.PojoMapping;

import java.math.BigDecimal;


/**
 * Description:  PrmBudgetDetailCDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-27 15:49:04
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.prm.PrmBudgetDetailC")
public class PrmBudgetDetailCDto extends PrmBudgetDetailC {
    private BigDecimal budgetRate;
    private String subjectComment;
    private String changeStatus;
    private String changedFields;
    private Integer isRevised;
    private BigDecimal excludingVatAmount;

    public String getChangeStatus() {
        return changeStatus;
    }

    public void setChangeStatus(String changeStatus) {
        this.changeStatus = changeStatus;
    }

    public String getChangedFields() {
        return changedFields;
    }

    public void setChangedFields(String changedFields) {
        this.changedFields = changedFields;
    }

    public Integer getIsRevised() {
        return isRevised;
    }

    public void setIsRevised(Integer isRevised) {
        this.isRevised = isRevised;
    }

    public BigDecimal getBudgetRate() {
        return budgetRate;
    }

    public void setBudgetRate(BigDecimal budgetRate) {
        this.budgetRate = budgetRate;
    }

    public String getSubjectComment() {
        return subjectComment;
    }

    public void setSubjectComment(String subjectComment) {
        this.subjectComment = subjectComment;
    }

    public BigDecimal getExcludingVatAmount() {
        return excludingVatAmount;
    }

    public void setExcludingVatAmount(BigDecimal excludingVatAmount) {
        this.excludingVatAmount = excludingVatAmount;
    }
}