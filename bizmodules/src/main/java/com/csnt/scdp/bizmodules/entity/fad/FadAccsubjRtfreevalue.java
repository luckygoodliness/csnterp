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
 * Description:  FadAccsubjRtfreevalue
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-10-21 17:29:52
 */

@javax.persistence.Entity
@Table(name = "FAD_ACCSUBJ_RTFREEVALUE")
public class FadAccsubjRtfreevalue extends BasePojo {
    private String uuid;
    private String subjectCode;
    private String subjectName;
    private Integer subjectLev;
    private String accountNo;
    private String accountType;
    private String accountInfoId;
    private String accountInfoCode;
    private String accountInfoName;
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

    @Column(name = "UUID", nullable = false, insertable = false, updatable = false, length = 32, precision = 0)
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Column(name = "SUBJECT_CODE", nullable = true, insertable = false, updatable = false, length = 50, precision = 0)
    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    @Column(name = "SUBJECT_NAME", nullable = true, insertable = false, updatable = false, length = 100, precision = 0)
    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    @Column(name = "SUBJECT_LEV", nullable = true, insertable = false, updatable = false, length = 38, precision = 0)
    public Integer getSubjectLev() {
        return subjectLev;
    }

    public void setSubjectLev(Integer subjectLev) {
        this.subjectLev = subjectLev;
    }

    @Column(name = "ACCOUNT_NO", nullable = true, insertable = false, updatable = false, length = 50, precision = 0)
    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    @Column(name = "ACCOUNT_TYPE", nullable = true, insertable = false, updatable = false, length = 50, precision = 0)
    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Column(name = "ACCOUNT_INFO_ID", nullable = true, insertable = false, updatable = false, length = 50, precision = 0)
    public String getAccountInfoId() {
        return accountInfoId;
    }

    public void setAccountInfoId(String accountInfoId) {
        this.accountInfoId = accountInfoId;
    }

    @Column(name = "ACCOUNT_INFO_CODE", nullable = true, insertable = false, updatable = false, length = 50, precision = 0)
    public String getAccountInfoCode() {
        return accountInfoCode;
    }

    public void setAccountInfoCode(String accountInfoCode) {
        this.accountInfoCode = accountInfoCode;
    }

    @Column(name = "ACCOUNT_INFO_NAME", nullable = true, insertable = false, updatable = false, length = 200, precision = 0)
    public String getAccountInfoName() {
        return accountInfoName;
    }

    public void setAccountInfoName(String accountInfoName) {
        this.accountInfoName = accountInfoName;
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

    @Column(name = "CREATE_BY", nullable = true, insertable = false, updatable = false, length = 32, precision = 0)
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name = "CREATE_TIME", nullable = true, insertable = false, updatable = false, length = 0, precision = 0)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "UPDATE_BY", nullable = true, insertable = false, updatable = false, length = 32, precision = 0)
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name = "UPDATE_TIME", nullable = true, insertable = false, updatable = false, length = 0, precision = 0)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "LOC_TIMEZONE", nullable = true, insertable = false, updatable = false, length = 32, precision = 0)
    public String getLocTimezone() {
        return locTimezone;
    }

    public void setLocTimezone(String locTimezone) {
        this.locTimezone = locTimezone;
    }

    @Column(name = "TBL_VERSION", nullable = true, insertable = false, updatable = false, length = 32, precision = 0)
    public String getTblVersion() {
        return tblVersion;
    }

    public void setTblVersion(String tblVersion) {
        this.tblVersion = tblVersion;
    }

    @Column(name = "IS_VOID", nullable = true, insertable = false, updatable = false, length = 1, precision = 0)
    public Integer getIsVoid() {
        return isVoid;
    }

    public void setIsVoid(Integer isVoid) {
        this.isVoid = isVoid;
    }
}