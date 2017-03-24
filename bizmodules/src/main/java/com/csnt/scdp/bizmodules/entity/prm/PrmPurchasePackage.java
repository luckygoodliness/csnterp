package com.csnt.scdp.bizmodules.entity.prm;

import com.csnt.scdp.framework.dto.BasePojo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:  PrmPurchasePackage
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-10-27 18:04:00
 */

@javax.persistence.Entity
@Table(name = "PRM_PURCHASE_PACKAGE")
public class PrmPurchasePackage extends BasePojo {
    private String uuid;
    private String prmProjectMainId;
    private Integer packageNo;
    private BigDecimal packageBudgetMoney;
    private String packageState;
    private String state;
    private String remark;
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
    private String purchaseLevel;
    private String subjectProperty;
    private String description;
    private Integer seqNo;
    private String packageName;
    private BigDecimal pendingMoney;
    private String materialClassCode;
    private Date arriveDate;

    @Column(name = "MATERIAL_CLASS_CODE", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getMaterialClassCode() {
        return materialClassCode;
    }

    public void setMaterialClassCode(String materialClassCode) {
        this.materialClassCode = materialClassCode;
    }

    @Column(name = "UUID", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "assigned")
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Column(name = "PRM_PROJECT_MAIN_ID", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getPrmProjectMainId() {
        return prmProjectMainId;
    }

    public void setPrmProjectMainId(String prmProjectMainId) {
        this.prmProjectMainId = prmProjectMainId;
    }

    @Column(name = "PACKAGE_NO", nullable = true, insertable = true, updatable = true, length = 5, precision = 0)
    public Integer getPackageNo() {
        return packageNo;
    }

    public void setPackageNo(Integer packageNo) {
        this.packageNo = packageNo;
    }

    @Column(name = "PACKAGE_BUDGET_MONEY", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getPackageBudgetMoney() {
        return packageBudgetMoney;
    }

    public void setPackageBudgetMoney(BigDecimal packageBudgetMoney) {
        this.packageBudgetMoney = packageBudgetMoney;
    }

    @Column(name = "PACKAGE_STATE", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getPackageState() {
        return packageState;
    }

    public void setPackageState(String packageState) {
        this.packageState = packageState;
    }

    @Column(name = "STATE", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name = "REMARK", nullable = true, insertable = true, updatable = true, length = 500, precision = 0)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    @Column(name = "TBL_VERSION", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
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

    @Column(name = "PURCHASE_LEVEL", nullable = true, insertable = true, updatable = true, length = 20, precision = 0)
    public String getPurchaseLevel() {
        return purchaseLevel;
    }

    public void setPurchaseLevel(String purchaseLevel) {
        this.purchaseLevel = purchaseLevel;
    }

    @Column(name = "SUBJECT_PROPERTY", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getSubjectProperty() {
        return subjectProperty;
    }

    public void setSubjectProperty(String subjectProperty) {
        this.subjectProperty = subjectProperty;
    }

    @Column(name = "DESCRIPTION", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "SEQ_NO", nullable = true, insertable = true, updatable = true, length = 9, precision = 0)
    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    @Column(name = "PACKAGE_NAME", nullable = true, insertable = true, updatable = true, length = 128, precision = 0)
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Column(name = "PENDING_MONEY", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getPendingMoney() {
        return pendingMoney;
    }

    public void setPendingMoney(BigDecimal pendingMoney) {
        this.pendingMoney = pendingMoney;
    }

    @Column(name = "ARRIVE_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getArriveDate() {
        return arriveDate;
    }

    public void setArriveDate(Date arriveDate) {
        this.arriveDate = arriveDate;
    }
}