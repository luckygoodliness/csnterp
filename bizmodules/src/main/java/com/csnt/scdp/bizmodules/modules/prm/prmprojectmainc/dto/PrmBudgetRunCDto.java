package com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto;

import com.csnt.scdp.bizmodules.entity.prm.PrmBudgetRunC;
import com.csnt.scdp.framework.dto.PojoMapping;

import java.math.BigDecimal;


/**
 * Description:  PrmBudgetRunCDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-27 15:49:04
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.prm.PrmBudgetRunC")
public class PrmBudgetRunCDto extends PrmBudgetRunC {
    private String changeStatus;
    private String changedFields;
    private Integer isRevised;
    private BigDecimal lockedMoney;

    public BigDecimal getLockedMoney() {
        return lockedMoney;
    }

    public void setLockedMoney(BigDecimal lockedMoney) {
        this.lockedMoney = lockedMoney;
    }

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

}