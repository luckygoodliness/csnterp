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
 * Description:  NonProjectBudgetAppro
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-12-15 09:40:00
 */

@javax.persistence.Entity
@Table(name = "NON_PROJECT_BUDGET_APPRO")
public class NonProjectBudgetAppro extends BasePojo {
    private String uuid;
    private String budgetCuuid;
    private String budgetSubjectCode;
    private String budgetSubjectName;
    private BigDecimal appropriationBefore;
    private BigDecimal appropriationAfter;
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

    @Column(name = "UUID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Column(name = "BUDGET_CUUID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getBudgetCuuid() {
        return budgetCuuid;
    }

    public void setBudgetCuuid(String budgetCuuid) {
        this.budgetCuuid = budgetCuuid;
    }

    @Column(name = "BUDGET_SUBJECT_CODE", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
    public String getBudgetSubjectCode() {
        return budgetSubjectCode;
    }

    public void setBudgetSubjectCode(String budgetSubjectCode) {
        this.budgetSubjectCode = budgetSubjectCode;
    }

    @Column(name = "BUDGET_SUBJECT_NAME", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
    public String getBudgetSubjectName() {
        return budgetSubjectName;
    }

    public void setBudgetSubjectName(String budgetSubjectName) {
        this.budgetSubjectName = budgetSubjectName;
    }

    @Column(name = "APPROPRIATION_BEFORE", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getAppropriationBefore() {
        return appropriationBefore;
    }

    public void setAppropriationBefore(BigDecimal appropriationBefore) {
        this.appropriationBefore = appropriationBefore;
    }

    @Column(name = "APPROPRIATION_AFTER", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getAppropriationAfter() {
        return appropriationAfter;
    }

    public void setAppropriationAfter(BigDecimal appropriationAfter) {
        this.appropriationAfter = appropriationAfter;
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

    @Column(name = "SEQ_NO", nullable = true, insertable = true, updatable = true, length = 9, precision = 0)
    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

}