package com.csnt.scdp.bizmodules.modules.prm.purchaseplan.dto;

import com.csnt.scdp.bizmodules.entity.prm.PrmPurchasePlanDetail;
import com.csnt.scdp.framework.dto.PojoMapping;

import java.math.BigDecimal;


/**
 * Description:  PrmPurchasePlanDetailDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-25 14:45:15
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.prm.PrmPurchasePlanDetail")
public class PrmPurchasePlanDetailDto extends PrmPurchasePlanDetail {
    /**
     * 可申请数量
     */
    private BigDecimal availableAmount;
    /**
     * 是否立项预算
     */
    private Integer isBudget;
    /**
     * 是否存在采购申请
     */
    private Integer isReq;
    /**
     * 包状态是否已关闭
     */
    private Integer isClose;

    /**
     * 运行费是否已进行日常报销
     */
    private Integer isRunApply;


    private BigDecimal originalAmount;
    private BigDecimal appliedAmount;
    private BigDecimal oriBudgetAmount;
    private BigDecimal oriBudgetPrice;
    private BigDecimal oriBudgetTotalValue;

    public BigDecimal getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(BigDecimal availableAmount) {
        this.availableAmount = availableAmount;
    }


    public Integer getIsClose() {
        return isClose;
    }

    public void setIsClose(Integer isClose) {
        this.isClose = isClose;
    }

    public Integer getIsReq() {
        return isReq;
    }

    public void setIsReq(Integer isReq) {
        this.isReq = isReq;
    }

    public Integer getIsBudget() {
        return isBudget;
    }

    public void setIsBudget(Integer isBudget) {
        this.isBudget = isBudget;
    }

    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(BigDecimal originalAmount) {
        this.originalAmount = originalAmount;
    }

    public BigDecimal getAppliedAmount() {
        return appliedAmount;
    }

    public void setAppliedAmount(BigDecimal appliedAmount) {
        this.appliedAmount = appliedAmount;
    }

    public BigDecimal getOriBudgetAmount() {
        return oriBudgetAmount;
    }

    public void setOriBudgetAmount(BigDecimal oriBudgetAmount) {
        this.oriBudgetAmount = oriBudgetAmount;
    }

    public BigDecimal getOriBudgetPrice() {
        return oriBudgetPrice;
    }

    public void setOriBudgetPrice(BigDecimal oriBudgetPrice) {
        this.oriBudgetPrice = oriBudgetPrice;
    }

    public BigDecimal getOriBudgetTotalValue() {
        return oriBudgetTotalValue;
    }

    public void setOriBudgetTotalValue(BigDecimal oriBudgetTotalValue) {
        this.oriBudgetTotalValue = oriBudgetTotalValue;
    }

    public Integer getIsRunApply() {
        return isRunApply;
    }

    public void setIsRunApply(Integer isRunApply) {
        this.isRunApply = isRunApply;
    }

}