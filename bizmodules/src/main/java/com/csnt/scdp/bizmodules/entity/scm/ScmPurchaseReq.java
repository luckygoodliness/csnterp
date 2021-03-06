package com.csnt.scdp.bizmodules.entity.scm;

import com.csnt.scdp.framework.dto.BasePojo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:  ScmPurchaseReq
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-11-14 19:52:00
 */

@javax.persistence.Entity
@Table(name = "SCM_PURCHASE_REQ")
public class ScmPurchaseReq extends BasePojo {
    private String uuid;
    private String prmPurchaseReqId;
    private String purchasePackageId;
    private String prmProjectMainId;
    private String purchaseReqNo;
    private String bugdetId;
    private String subjectCode;
    private String state;
    private String principal;
    private String remark;
    private String companyCode;
    private String departmentCode;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    private String locTimezone;
    private String tblVersion;
    private Integer isVoid;
    private Integer seqNo;
    private String officeId;
    private Integer isProject;
    private Integer isRead;
    private BigDecimal totalMoney;
    private String purchaseLevel;

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

    @Column(name = "PRM_PURCHASE_REQ_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getPrmPurchaseReqId() {
        return prmPurchaseReqId;
    }

    public void setPrmPurchaseReqId(String prmPurchaseReqId) {
        this.prmPurchaseReqId = prmPurchaseReqId;
    }

    @Column(name = "PURCHASE_PACKAGE_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getPurchasePackageId() {
        return purchasePackageId;
    }

    public void setPurchasePackageId(String purchasePackageId) {
        this.purchasePackageId = purchasePackageId;
    }

    @Column(name = "PRM_PROJECT_MAIN_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getPrmProjectMainId() {
        return prmProjectMainId;
    }

    public void setPrmProjectMainId(String prmProjectMainId) {
        this.prmProjectMainId = prmProjectMainId;
    }

    @Column(name = "PURCHASE_REQ_NO", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getPurchaseReqNo() {
        return purchaseReqNo;
    }

    public void setPurchaseReqNo(String purchaseReqNo) {
        this.purchaseReqNo = purchaseReqNo;
    }

    @Column(name = "BUGDET_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getBugdetId() {
        return bugdetId;
    }

    public void setBugdetId(String bugdetId) {
        this.bugdetId = bugdetId;
    }

    @Column(name = "SUBJECT_CODE", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    @Column(name = "STATE", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name = "PRINCIPAL", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
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

    @Column(name = "OFFICE_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    @Column(name = "IS_PROJECT", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public Integer getIsProject() {
        return isProject;
    }

    public void setIsProject(Integer isProject) {
        this.isProject = isProject;
    }

    @Column(name = "TOTAL_MONEY", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    @Column(name = "PURCHASE_LEVEL", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public String getPurchaseLevel() {
        return purchaseLevel;
    }

    public void setPurchaseLevel(String purchaseLevel) {
        this.purchaseLevel = purchaseLevel;
    }

    @Column(name = "IS_READ", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }
}