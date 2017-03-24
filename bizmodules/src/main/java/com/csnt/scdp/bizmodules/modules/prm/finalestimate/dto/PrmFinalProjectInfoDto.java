package com.csnt.scdp.bizmodules.modules.prm.finalestimate.dto;

import com.csnt.scdp.framework.dto.BasePojo;

import java.math.BigDecimal;

/**
 * Description:  PrmProjectFinalDto
 * Copyright: © 2015 Aardwolf Studio. All rights reserved.
 * Company:CSNT中海网络科技股份有限公司
 *
 * @author liujingyuan
 * @version 1.0
 */
public class PrmFinalProjectInfoDto extends BasePojo {
    private BigDecimal totalSquareMoney;
    private BigDecimal plannedCost;
    private BigDecimal plannedProfit;
    private BigDecimal plannedTax;
    private BigDecimal projectActualMoneySum;

    private BigDecimal squareMoneySum;
    private BigDecimal squareCostSum;
    private BigDecimal squareGrossProfitSum;
    private BigDecimal raxSum;

    private BigDecimal registeredMoney;
    private BigDecimal registeredCost;
    private BigDecimal registeredProfit;
    private BigDecimal registeredReceiveMoney;

    private BigDecimal plannedProfitPercent;
    private BigDecimal lockedBudgetSum;
    private BigDecimal billedCostSum;
    private BigDecimal costCorrection;
    private BigDecimal taxMoneySum;


    public BigDecimal getTotalSquareMoney() {
        return totalSquareMoney;
    }

    public void setTotalSquareMoney(BigDecimal totalSquareMoney) {
        this.totalSquareMoney = totalSquareMoney;
    }

    public BigDecimal getPlannedCost() {
        return plannedCost;
    }

    public void setPlannedCost(BigDecimal plannedCost) {
        this.plannedCost = plannedCost;
    }

    public BigDecimal getPlannedProfit() {
        return plannedProfit;
    }

    public void setPlannedProfit(BigDecimal plannedProfit) {
        this.plannedProfit = plannedProfit;
    }

    public BigDecimal getPlannedTax() {
        return plannedTax;
    }

    public void setPlannedTax(BigDecimal plannedTax) {
        this.plannedTax = plannedTax;
    }

    public BigDecimal getProjectActualMoneySum() {
        return projectActualMoneySum;
    }

    public void setProjectActualMoneySum(BigDecimal projectActualMoneySum) {
        this.projectActualMoneySum = projectActualMoneySum;
    }

    public BigDecimal getSquareMoneySum() {
        return squareMoneySum;
    }

    public void setSquareMoneySum(BigDecimal squareMoneySum) {
        this.squareMoneySum = squareMoneySum;
    }

    public BigDecimal getSquareCostSum() {
        return squareCostSum;
    }

    public void setSquareCostSum(BigDecimal squareCostSum) {
        this.squareCostSum = squareCostSum;
    }

    public BigDecimal getSquareGrossProfitSum() {
        return squareGrossProfitSum;
    }

    public void setSquareGrossProfitSum(BigDecimal squareGrossProfitSum) {
        this.squareGrossProfitSum = squareGrossProfitSum;
    }

    public BigDecimal getRaxSum() {
        return raxSum;
    }

    public void setRaxSum(BigDecimal raxSum) {
        this.raxSum = raxSum;
    }

    public BigDecimal getRegisteredMoney() {
        return registeredMoney;
    }

    public void setRegisteredMoney(BigDecimal registeredMoney) {
        this.registeredMoney = registeredMoney;
    }

    public BigDecimal getRegisteredCost() {
        return registeredCost;
    }

    public void setRegisteredCost(BigDecimal registeredCost) {
        this.registeredCost = registeredCost;
    }

    public BigDecimal getRegisteredProfit() {
        return registeredProfit;
    }

    public void setRegisteredProfit(BigDecimal registeredProfit) {
        this.registeredProfit = registeredProfit;
    }

    public BigDecimal getRegisteredReceiveMoney() {
        return registeredReceiveMoney;
    }

    public void setRegisteredReceiveMoney(BigDecimal registeredReceiveMoney) {
        this.registeredReceiveMoney = registeredReceiveMoney;
    }

    public BigDecimal getPlannedProfitPercent() {
        return plannedProfitPercent;
    }

    public void setPlannedProfitPercent(BigDecimal plannedProfitPercent) {
        this.plannedProfitPercent = plannedProfitPercent;
    }

    public BigDecimal getLockedBudgetSum() {
        return lockedBudgetSum;
    }

    public void setLockedBudgetSum(BigDecimal lockedBudgetSum) {
        this.lockedBudgetSum = lockedBudgetSum;
    }

    public BigDecimal getBilledCostSum() {
        return billedCostSum;
    }

    public void setBilledCostSum(BigDecimal billedCostSum) {
        this.billedCostSum = billedCostSum;
    }

    public BigDecimal getCostCorrection() {
        return costCorrection;
    }

    public void setCostCorrection(BigDecimal costCorrection) {
        this.costCorrection = costCorrection;
    }

    public BigDecimal getTaxMoneySum() {
        return taxMoneySum;
    }

    public void setTaxMoneySum(BigDecimal taxMoneySum) {
        this.taxMoneySum = taxMoneySum;
    }
}

