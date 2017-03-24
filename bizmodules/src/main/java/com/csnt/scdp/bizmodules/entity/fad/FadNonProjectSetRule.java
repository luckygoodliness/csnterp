package com.csnt.scdp.bizmodules.entity.fad;

import com.csnt.scdp.framework.dto.BasePojo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.math.BigDecimal;

/**
 * Description:  FadNonProjectSetRule
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-09-23 21:16:46
 */

@javax.persistence.Entity
@Table(name = "FAD_NON_PROJECT_SET_RULE")
public class FadNonProjectSetRule extends BasePojo {
    private String uuid;
    private String office;
    private String financialSubject;
    private String debtorSubject4a51;
    private String debtorSubject1;
    private String debtorSubject2;
    private String creditorSubject4a51a1a2;
    private String creditorRtfree4a51a1a2;
    private String companyCode;
    private String projectId;
    private String departmentCode;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    private String locTimezone;
    private String tblVersion;
    private Integer isVoid;

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

    @Column(name = "OFFICE", nullable = false, insertable = true, updatable = true, length = 100, precision = 0)
    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    @Column(name = "FINANCIAL_SUBJECT", nullable = false, insertable = true, updatable = true, length = 100, precision = 0)
    public String getFinancialSubject() {
        return financialSubject;
    }

    public void setFinancialSubject(String financialSubject) {
        this.financialSubject = financialSubject;
    }

    @Column(name = "DEBTOR_SUBJECT4A51", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
    public String getDebtorSubject4a51() {
        return debtorSubject4a51;
    }

    public void setDebtorSubject4a51(String debtorSubject4a51) {
        this.debtorSubject4a51 = debtorSubject4a51;
    }

    @Column(name = "DEBTOR_SUBJECT1", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
    public String getDebtorSubject1() {
        return debtorSubject1;
    }

    public void setDebtorSubject1(String debtorSubject1) {
        this.debtorSubject1 = debtorSubject1;
    }

    @Column(name = "DEBTOR_SUBJECT2", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
    public String getDebtorSubject2() {
        return debtorSubject2;
    }

    public void setDebtorSubject2(String debtorSubject2) {
        this.debtorSubject2 = debtorSubject2;
    }

    @Column(name = "CREDITOR_SUBJECT4A51A1A2", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
    public String getCreditorSubject4a51a1a2() {
        return creditorSubject4a51a1a2;
    }

    public void setCreditorSubject4a51a1a2(String creditorSubject4a51a1a2) {
        this.creditorSubject4a51a1a2 = creditorSubject4a51a1a2;
    }

    @Column(name = "CREDITOR_RTFREE4A51A1A2", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
    public String getCreditorRtfree4a51a1a2() {
        return creditorRtfree4a51a1a2;
    }

    public void setCreditorRtfree4a51a1a2(String creditorRtfree4a51a1a2) {
        this.creditorRtfree4a51a1a2 = creditorRtfree4a51a1a2;
    }

    @Column(name = "COMPANY_CODE", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    @Column(name = "PROJECT_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
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

    @Column(name = "IS_VOID", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public Integer getIsVoid() {
        return isVoid;
    }

    public void setIsVoid(Integer isVoid) {
        this.isVoid = isVoid;
    }
}