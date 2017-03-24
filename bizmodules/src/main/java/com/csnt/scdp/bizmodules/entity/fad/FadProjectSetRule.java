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
 * Description:  FadProjectSetRule
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-09-30 19:07:31
 */

@javax.persistence.Entity
@Table(name = "FAD_PROJECT_SET_RULE")
public class FadProjectSetRule extends BasePojo {
    private String uuid;
    private String office;
    private String subject;
    private String projectCode;
    private String prmCodeType;
    private String debtorSubject6a7;
    private String creditorSubject6a7;
    private String debtorSubject8;
    private String creditorSubject8;
    private String debtorSubject9a10;
    private String creditorSubject9a10;
    private String creditorRtfree9a10;
    private String debtorSubject1;
    private String debtorSubject2;
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
    private Integer seqNo;

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

    @Column(name = "OFFICE", nullable = false, insertable = true, updatable = true, length = 4000, precision = 0)
    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    @Column(name = "SUBJECT", nullable = false, insertable = true, updatable = true, length = 50, precision = 0)
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Column(name = "PROJECT_CODE", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    @Column(name = "PRM_CODE_TYPE", nullable = false, insertable = true, updatable = true, length = 16, precision = 0)
    public String getPrmCodeType() {
        return prmCodeType;
    }

    public void setPrmCodeType(String prmCodeType) {
        this.prmCodeType = prmCodeType;
    }

    @Column(name = "DEBTOR_SUBJECT6A7", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
    public String getDebtorSubject6a7() {
        return debtorSubject6a7;
    }

    public void setDebtorSubject6a7(String debtorSubject6a7) {
        this.debtorSubject6a7 = debtorSubject6a7;
    }

    @Column(name = "CREDITOR_SUBJECT6A7", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
    public String getCreditorSubject6a7() {
        return creditorSubject6a7;
    }

    public void setCreditorSubject6a7(String creditorSubject6a7) {
        this.creditorSubject6a7 = creditorSubject6a7;
    }

    @Column(name = "DEBTOR_SUBJECT8", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
    public String getDebtorSubject8() {
        return debtorSubject8;
    }

    public void setDebtorSubject8(String debtorSubject8) {
        this.debtorSubject8 = debtorSubject8;
    }

    @Column(name = "CREDITOR_SUBJECT8", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
    public String getCreditorSubject8() {
        return creditorSubject8;
    }

    public void setCreditorSubject8(String creditorSubject8) {
        this.creditorSubject8 = creditorSubject8;
    }

    @Column(name = "DEBTOR_SUBJECT9A10", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
    public String getDebtorSubject9a10() {
        return debtorSubject9a10;
    }

    public void setDebtorSubject9a10(String debtorSubject9a10) {
        this.debtorSubject9a10 = debtorSubject9a10;
    }

    @Column(name = "CREDITOR_SUBJECT9A10", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
    public String getCreditorSubject9a10() {
        return creditorSubject9a10;
    }

    public void setCreditorSubject9a10(String creditorSubject9a10) {
        this.creditorSubject9a10 = creditorSubject9a10;
    }

    @Column(name = "CREDITOR_RTFREE9A10", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
    public String getCreditorRtfree9a10() {
        return creditorRtfree9a10;
    }

    public void setCreditorRtfree9a10(String creditorRtfree9a10) {
        this.creditorRtfree9a10 = creditorRtfree9a10;
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

    @Column(name = "SEQ_NO", nullable = true, insertable = true, updatable = true, length = 9, precision = 0)
    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

}