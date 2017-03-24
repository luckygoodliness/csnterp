package com.csnt.scdp.bizmodules.modules.prm.purchaseplan.dto;

import com.csnt.scdp.bizmodules.entity.prm.PrmPurchasePackage;
import com.csnt.scdp.framework.dto.PojoMapping;

import java.math.BigDecimal;


/**
 * Description:  PrmPurchasePackageDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-10-27 18:04:00
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.prm.PrmPurchasePackage")
public class PrmPurchasePackageDto extends PrmPurchasePackage {
    private String materialClassCodeDesc;
    private BigDecimal appliedMoney;
    private String completePercent;
    private BigDecimal packageBalance;
    private BigDecimal remainBudget;

    public String getMaterialClassCodeDesc() {
        return materialClassCodeDesc;
    }

    public void setMaterialClassCodeDesc(String materialClassCodeDesc) {
        this.materialClassCodeDesc = materialClassCodeDesc;
    }

    public BigDecimal getAppliedMoney() {
        return appliedMoney;
    }

    public void setAppliedMoney(BigDecimal appliedMoney) {
        this.appliedMoney = appliedMoney;
    }

    public String getCompletePercent() {
        return completePercent;
    }

    public void setCompletePercent(String completePercent) {
        this.completePercent = completePercent;
    }

    public BigDecimal getPackageBalance() {
        return packageBalance;
    }

    public void setPackageBalance(BigDecimal packageBalance) {
        this.packageBalance = packageBalance;
    }

    public BigDecimal getRemainBudget() {
        return remainBudget;
    }

    public void setRemainBudget(BigDecimal remainBudget) {
        this.remainBudget = remainBudget;
    }
}