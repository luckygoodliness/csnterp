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
 * Description:  NonProjectIncomeIn
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-11-16 18:40:31
 */

@javax.persistence.Entity
@Table(name = "NON_PROJECT_INCOME_IN")
public class NonProjectIncomeIn extends BasePojo {
    private String uuid;
    private String year;
    private String subject;
    private String subjectOfficeId;
    private String subjectOfficeName;
    private BigDecimal appliedValue;
    private BigDecimal firstInstance;
    private BigDecimal assignedValue;
    private BigDecimal occurredValue;
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
    private BigDecimal changedValue;

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

    @Column(name = "SUBJECT", nullable = true, insertable = true, updatable = true, length = 200, precision = 0)
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Column(name = "SUBJECT_OFFICE_ID", nullable = true, insertable = true, updatable = true, length = 200, precision = 0)
    public String getSubjectOfficeId() {
        return subjectOfficeId;
    }

    public void setSubjectOfficeId(String subjectOfficeId) {
        this.subjectOfficeId = subjectOfficeId;
    }

    @Column(name = "SUBJECT_OFFICE_NAME", nullable = true, insertable = true, updatable = true, length = 200, precision = 0)
    public String getSubjectOfficeName() {
        return subjectOfficeName;
    }

    public void setSubjectOfficeName(String subjectOfficeName) {
        this.subjectOfficeName = subjectOfficeName;
    }

    @Column(name = "APPLIED_VALUE", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getAppliedValue() {
        return appliedValue;
    }

    public void setAppliedValue(BigDecimal appliedValue) {
        this.appliedValue = appliedValue;
    }

    @Column(name = "FIRST_INSTANCE", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getFirstInstance() {
        return firstInstance;
    }

    public void setFirstInstance(BigDecimal firstInstance) {
        this.firstInstance = firstInstance;
    }

    @Column(name = "ASSIGNED_VALUE", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getAssignedValue() {
        return assignedValue;
    }

    public void setAssignedValue(BigDecimal assignedValue) {
        this.assignedValue = assignedValue;
    }

    @Column(name = "OCCURRED_VALUE", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getOccurredValue() {
        return occurredValue;
    }

    public void setOccurredValue(BigDecimal occurredValue) {
        this.occurredValue = occurredValue;
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

    @Column(name = "SEQ_NO", nullable = true, insertable = true, updatable = true, length = 9, precision = 0)
    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    @Column(name = "CHANGED_VALUE", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getChangedValue() {
        return changedValue;
    }

    public void setChangedValue(BigDecimal changedValue) {
        this.changedValue = changedValue;
    }

}