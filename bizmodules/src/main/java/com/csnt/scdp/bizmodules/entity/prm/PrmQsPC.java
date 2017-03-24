package com.csnt.scdp.bizmodules.entity.prm;

import com.csnt.scdp.framework.dto.BasePojo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Description:  PrmQsPC
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-10-21 20:24:42
 */

@javax.persistence.Entity
@Table(name = "PRM_QS_P_C")
public class PrmQsPC extends BasePojo {
    private String prmProjectMainCId;
    private String lastUuid;
    private String uuid;
    private String state;
    private String safePrincipal;
    private String safeContact;
    private String safeDocument;
    private String safeMeasure;
    private String qualityPrincipal;
    private String qualityContact;
    private String qualityDocument;
    private String qualityMeasure;
    private String outerNo;
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

    @Column(name = "STATE", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name = "SAFE_PRINCIPAL", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getSafePrincipal() {
        return safePrincipal;
    }

    public void setSafePrincipal(String safePrincipal) {
        this.safePrincipal = safePrincipal;
    }

    @Column(name = "SAFE_CONTACT", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getSafeContact() {
        return safeContact;
    }

    public void setSafeContact(String safeContact) {
        this.safeContact = safeContact;
    }

    @Column(name = "SAFE_DOCUMENT", nullable = true, insertable = true, updatable = true, length = 200, precision = 0)
    public String getSafeDocument() {
        return safeDocument;
    }

    public void setSafeDocument(String safeDocument) {
        this.safeDocument = safeDocument;
    }

    @Column(name = "SAFE_MEASURE", nullable = true, insertable = true, updatable = true, length = 500, precision = 0)
    public String getSafeMeasure() {
        return safeMeasure;
    }

    public void setSafeMeasure(String safeMeasure) {
        this.safeMeasure = safeMeasure;
    }

    @Column(name = "QUALITY_PRINCIPAL", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getQualityPrincipal() {
        return qualityPrincipal;
    }

    public void setQualityPrincipal(String qualityPrincipal) {
        this.qualityPrincipal = qualityPrincipal;
    }

    @Column(name = "QUALITY_CONTACT", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getQualityContact() {
        return qualityContact;
    }

    public void setQualityContact(String qualityContact) {
        this.qualityContact = qualityContact;
    }

    @Column(name = "QUALITY_DOCUMENT", nullable = true, insertable = true, updatable = true, length = 200, precision = 0)
    public String getQualityDocument() {
        return qualityDocument;
    }

    public void setQualityDocument(String qualityDocument) {
        this.qualityDocument = qualityDocument;
    }

    @Column(name = "QUALITY_MEASURE", nullable = true, insertable = true, updatable = true, length = 500, precision = 0)
    public String getQualityMeasure() {
        return qualityMeasure;
    }

    public void setQualityMeasure(String qualityMeasure) {
        this.qualityMeasure = qualityMeasure;
    }

    @Column(name = "OUTER_NO", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getOuterNo() {
        return outerNo;
    }

    public void setOuterNo(String outerNo) {
        this.outerNo = outerNo;
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

    @Column(name = "PRM_PROJECT_MAIN_C_ID", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getPrmProjectMainCId() {
        return prmProjectMainCId;
    }

    public void setPrmProjectMainCId(String prmProjectMainCId) {
        this.prmProjectMainCId = prmProjectMainCId;
    }

    @Column(name = "LAST_UUID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getLastUuid() {
        return lastUuid;
    }

    public void setLastUuid(String lastUuid) {
        this.lastUuid = lastUuid;
    }
}