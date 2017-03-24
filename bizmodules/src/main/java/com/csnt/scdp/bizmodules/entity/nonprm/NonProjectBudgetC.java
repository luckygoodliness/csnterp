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
 * Description:  NonProjectBudgetC
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-24 13:22:12
 */

@javax.persistence.Entity
@Table(name = "NON_PROJECT_BUDGET_C")
public class NonProjectBudgetC extends BasePojo {
    private String uuid;
    private String hid;
    private String financialSubjectCode;
    private BigDecimal lastYearAssigned;
    private BigDecimal lastYearChanged;
    private BigDecimal lastYearOccured;
    private BigDecimal thisYearApplyed;
    private BigDecimal thisYearFirstInstance;
    private BigDecimal thisYearAssigned;
    private BigDecimal thisYearOccured;
    private BigDecimal thisYearChanged;
    private BigDecimal thisYearAppropriation;
    private BigDecimal thisYearPreappropriation;
    private String descp;
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

    @Column(name = "HID", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    @Column(name = "FINANCIAL_SUBJECT_CODE", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getFinancialSubjectCode() {
        return financialSubjectCode;
    }

    public void setFinancialSubjectCode(String financialSubjectCode) {
        this.financialSubjectCode = financialSubjectCode;
    }

    @Column(name = "LAST_YEAR_ASSIGNED", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getLastYearAssigned() {
        return lastYearAssigned;
    }

    public void setLastYearAssigned(BigDecimal lastYearAssigned) {
        this.lastYearAssigned = lastYearAssigned;
    }

    @Column(name = "LAST_YEAR_CHANGED", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getLastYearChanged() {
        return lastYearChanged;
    }

    public void setLastYearChanged(BigDecimal lastYearChanged) {
        this.lastYearChanged = lastYearChanged;
    }

    @Column(name = "LAST_YEAR_OCCURED", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getLastYearOccured() {
        return lastYearOccured;
    }

    public void setLastYearOccured(BigDecimal lastYearOccured) {
        this.lastYearOccured = lastYearOccured;
    }

    @Column(name = "THIS_YEAR_APPLYED", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getThisYearApplyed() {
        return thisYearApplyed;
    }

    public void setThisYearApplyed(BigDecimal thisYearApplyed) {
        this.thisYearApplyed = thisYearApplyed;
    }

    @Column(name = "THIS_YEAR_FIRST_INSTANCE", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getThisYearFirstInstance() {
        return thisYearFirstInstance;
    }

    public void setThisYearFirstInstance(BigDecimal thisYearFirstInstance) {
        this.thisYearFirstInstance = thisYearFirstInstance;
    }

    @Column(name = "THIS_YEAR_ASSIGNED", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getThisYearAssigned() {
        return thisYearAssigned;
    }

    public void setThisYearAssigned(BigDecimal thisYearAssigned) {
        this.thisYearAssigned = thisYearAssigned;
    }

    @Column(name = "THIS_YEAR_OCCURED", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getThisYearOccured() {
        return thisYearOccured;
    }

    public void setThisYearOccured(BigDecimal thisYearOccured) {
        this.thisYearOccured = thisYearOccured;
    }

    @Column(name = "THIS_YEAR_CHANGED", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getThisYearChanged() {
        return thisYearChanged;
    }

    public void setThisYearChanged(BigDecimal thisYearChanged) {
        this.thisYearChanged = thisYearChanged;
    }

    @Column(name = "THIS_YEAR_APPROPRIATION", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getThisYearAppropriation() {
        return thisYearAppropriation;
    }

    public void setThisYearAppropriation(BigDecimal thisYearAppropriation) {
        this.thisYearAppropriation = thisYearAppropriation;
    }

    @Column(name = "THIS_YEAR_PREAPPROPRIATION", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getThisYearPreappropriation() {
        return thisYearPreappropriation;
    }

    public void setThisYearPreappropriation(BigDecimal thisYearPreappropriation) {
        this.thisYearPreappropriation = thisYearPreappropriation;
    }

    @Column(name = "DESCP", nullable = true, insertable = true, updatable = true, length = 500, precision = 0)
    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp;
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

    @Column(name = "DEPARTMENT_CODE", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
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

    @Column(name = "LOC_TIMEZONE", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
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