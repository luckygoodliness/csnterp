package com.csnt.scdp.bizmodules.modules.nonprm.income.dto;

import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectIncome;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.dto.NonProjectBudgetCDto;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;

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
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.modules.nonprm.income.dto.NonProjectIncomeBalanceDto")
public class NonProjectIncomeBalanceDto extends BasePojo {
    String year;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    private String uuid;
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    String tit;

    public String getTit() {
        return tit;
    }

    public void setTit(String tit) {
        this.tit = tit;
    }

    BigDecimal lastAppliedValue;

    public BigDecimal getLastAppliedValue() {
        return lastAppliedValue;
    }

    public void setLastAppliedValue(BigDecimal lastAppliedValue) {
        this.lastAppliedValue = lastAppliedValue;
    }

    BigDecimal firstInstance;

    public BigDecimal getFirstInstance() {
        return firstInstance;
    }

    public void setFirstInstance(BigDecimal firstInstance) {
        this.firstInstance = firstInstance;
    }

    BigDecimal appliedValue;

    public BigDecimal getAppliedValue() {
        return appliedValue;
    }

    public void setAppliedValue(BigDecimal appliedValue) {
        this.appliedValue = appliedValue;
    }

    BigDecimal assignedValue;

    public BigDecimal getAssignedValue() {
        return assignedValue;
    }

    public void setAssignedValue(BigDecimal assignedValue) {
        this.assignedValue = assignedValue;
    }

    BigDecimal occurredValue;

    public BigDecimal getOccurredValue() {
        return occurredValue;
    }
    public void setOccurredValue(BigDecimal occurredValue) {
        this.occurredValue = occurredValue;
    }

    @CascadeChilds(FK = "year|year")
    private List<NonProjectIncomeDto> NonProjectIncomeDto;

    public List<NonProjectIncomeDto> getNonProjectIncomeDto() {
        return NonProjectIncomeDto;
    }

    public void setNonProjectIncomeDto(List<NonProjectIncomeDto> nonProjectIncomeDto) {
        NonProjectIncomeDto = nonProjectIncomeDto;
    }

    @CascadeChilds(FK = "year|year")
    private List<NonProjectIncome2Dto> NonProjectIncome2Dto;

    public List<NonProjectIncome2Dto> getNonProjectIncome2Dto() {
        return NonProjectIncome2Dto;
    }

    public void setNonProjectIncome2Dto(List<NonProjectIncome2Dto> nonProjectIncome2Dto) {
        NonProjectIncome2Dto = nonProjectIncome2Dto;
    }

    @CascadeChilds(FK = "year|year")
    private List<NonProjectIncomeInDto> nonProjectIncomeInDto;

    public List<NonProjectIncomeInDto> getNonProjectIncomeInDto() {
        return nonProjectIncomeInDto;
    }

    public void setNonProjectIncomeInDto(List<NonProjectIncomeInDto> nonProjectIncomeInDto) {
        this.nonProjectIncomeInDto = nonProjectIncomeInDto;
    }
}