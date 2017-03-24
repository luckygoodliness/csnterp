package com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto;

import com.csnt.scdp.bizmodules.entity.prm.PrmBudgetPrincipalC;
import com.csnt.scdp.framework.dto.PojoMapping;

import java.math.BigDecimal;


/**
 * Description:  PrmBudgetPrincipalCDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-27 15:49:04
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.prm.PrmBudgetPrincipalC")
public class PrmBudgetPrincipalCDto extends PrmBudgetPrincipalC {
    private String changeStatus;
    private String changedFields;
    private Integer isRevised;
    private String subjectProperty;
    private BigDecimal preTotalValue;
    private String splitFromUuidNo;
    private BigDecimal lockedAmount;
    private BigDecimal lockedMoney;
    private String packageName;
    private BigDecimal packageNo;

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

    public String getSubjectProperty() {
        return subjectProperty;
    }

    public void setSubjectProperty(String subjectProperty) {
        this.subjectProperty = subjectProperty;
    }

    public BigDecimal getPreTotalValue() {
        return preTotalValue;
    }

    public void setPreTotalValue(BigDecimal preTotalValue) {
        this.preTotalValue = preTotalValue;
    }

    public String getSplitFromUuidNo() {
        return splitFromUuidNo;
    }

    public void setSplitFromUuidNo(String splitFromUuidNo) {
        this.splitFromUuidNo = splitFromUuidNo;
    }

    public BigDecimal getLockedAmount() {
        return lockedAmount;
    }

    public void setLockedAmount(BigDecimal lockedAmount) {
        this.lockedAmount = lockedAmount;
    }

    public BigDecimal getLockedMoney() {
        return lockedMoney;
    }

    public void setLockedMoney(BigDecimal lockedMoney) {
        this.lockedMoney = lockedMoney;
    }

    public BigDecimal getPackageNo() {
        return packageNo;
    }

    public void setPackageNo(BigDecimal packageNo) {
        this.packageNo = packageNo;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

}