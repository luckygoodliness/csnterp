package com.csnt.scdp.bizmodules.entity.nonprm;

import com.csnt.scdp.framework.dto.BasePojo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.math.BigDecimal;

/**
 * Description:  NonBudgetMonitorY
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-28 09:06:36
 */

@javax.persistence.Entity
@Table(name = "NON_BUDGET_MONITOR_Y")
public class NonBudgetMonitorY extends BasePojo {
    private String uuid;
    private String year;
    private String budgetOfficeId;
    private String inOutType;
    private String budgetType;
    private BigDecimal assignedMoney;
    private BigDecimal occuredMoney;
    private String companyCode;
    private String companyName;
    private String departmentCode;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    private Integer isVoid;
    private String locTimezone;
    private String tblVersion;
    private Integer seqNo;

    @Column(name = "SEQ_NO", nullable = true, insertable = true, updatable = true, length = 9, precision = 0)
    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }
    @Column(name = "UUID", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Column(name = "YEAR", nullable = false, insertable = true, updatable = true, length = 4, precision = 0)
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Column(name = "BUDGET_OFFICE_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getBudgetOfficeId() {
        return budgetOfficeId;
    }

    public void setBudgetOfficeId(String budgetOfficeId) {
        this.budgetOfficeId = budgetOfficeId;
    }

    @Column(name = "IN_OUT_TYPE", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public String getInOutType() {
        return inOutType;
    }

    public void setInOutType(String inOutType) {
        this.inOutType = inOutType;
    }

    @Column(name = "BUDGET_TYPE", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getBudgetType() {
        return budgetType;
    }

    public void setBudgetType(String budgetType) {
        this.budgetType = budgetType;
    }

    @Column(name = "ASSIGNED_MONEY", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getAssignedMoney() {
        return assignedMoney;
    }

    public void setAssignedMoney(BigDecimal assignedMoney) {
        this.assignedMoney = assignedMoney;
    }

    @Column(name = "OCCURED_MONEY", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getOccuredMoney() {
        return occuredMoney;
    }

    public void setOccuredMoney(BigDecimal occuredMoney) {
        this.occuredMoney = occuredMoney;
    }

    @Column(name = "COMPANY_CODE", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    @Column(name = "COMPANY_NAME", nullable = true, insertable = true, updatable = true, length = 200, precision = 0)
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Column(name = "DEPARTMENT_CODE", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    @Column(name = "CREATE_BY", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name = "CREATE_TIME", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "UPDATE_BY", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name = "UPDATE_TIME", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "IS_VOID", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public Integer getIsVoid() {
        return isVoid;
    }

    public void setIsVoid(Integer isVoid) {
        this.isVoid = isVoid;
    }

    @Column(name = "LOC_TIMEZONE", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    public String getLocTimezone() {
        return locTimezone;
    }

    public void setLocTimezone(String locTimezone) {
        this.locTimezone = locTimezone;
    }

    @Column(name = "TBL_VERSION", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getTblVersion() {
        return tblVersion;
    }

    public void setTblVersion(String tblVersion) {
        this.tblVersion = tblVersion;
    }

}