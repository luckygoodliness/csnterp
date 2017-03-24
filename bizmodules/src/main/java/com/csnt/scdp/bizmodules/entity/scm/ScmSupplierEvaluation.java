package com.csnt.scdp.bizmodules.entity.scm;

import com.csnt.scdp.framework.dto.BasePojo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.math.BigDecimal;

/**
 * Description:  ScmSupplierEvaluation
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-08-17 13:47:51
 */

@javax.persistence.Entity
@Table(name = "SCM_SUPPLIER_EVALUATION")
public class ScmSupplierEvaluation extends BasePojo {
    private String uuid;
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
    private String scmSupplierId;
    private String evaluateFrom;
    private Integer price;
    private Integer business;
    private Integer personQuality;
    private Integer organizingCapability;
    private Integer compliance;
    private Integer securityManagement;
    private Integer finalEstimate;
    private Integer arrivalTime;
    private Integer equipmentQuality;
    private Integer service;
    private Integer comprehensive;
    private String remark;
    private String scmContractId;

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

    @Column(name = "SCM_SUPPLIER_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getScmSupplierId() {
        return scmSupplierId;
    }

    public void setScmSupplierId(String scmSupplierId) {
        this.scmSupplierId = scmSupplierId;
    }

    @Column(name = "EVALUATE_FROM", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public String getEvaluateFrom() {
        return evaluateFrom;
    }

    public void setEvaluateFrom(String evaluateFrom) {
        this.evaluateFrom = evaluateFrom;
    }

    @Column(name = "PRICE", nullable = true, insertable = true, updatable = true, length = 3, precision = 0)
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Column(name = "BUSINESS", nullable = true, insertable = true, updatable = true, length = 3, precision = 0)
    public Integer getBusiness() {
        return business;
    }

    public void setBusiness(Integer business) {
        this.business = business;
    }

    @Column(name = "PERSON_QUALITY", nullable = true, insertable = true, updatable = true, length = 3, precision = 0)
    public Integer getPersonQuality() {
        return personQuality;
    }

    public void setPersonQuality(Integer personQuality) {
        this.personQuality = personQuality;
    }

    @Column(name = "ORGANIZING_CAPABILITY", nullable = true, insertable = true, updatable = true, length = 3, precision = 0)
    public Integer getOrganizingCapability() {
        return organizingCapability;
    }

    public void setOrganizingCapability(Integer organizingCapability) {
        this.organizingCapability = organizingCapability;
    }

    @Column(name = "COMPLIANCE", nullable = true, insertable = true, updatable = true, length = 3, precision = 0)
    public Integer getCompliance() {
        return compliance;
    }

    public void setCompliance(Integer compliance) {
        this.compliance = compliance;
    }

    @Column(name = "SECURITY_MANAGEMENT", nullable = true, insertable = true, updatable = true, length = 3, precision = 0)
    public Integer getSecurityManagement() {
        return securityManagement;
    }

    public void setSecurityManagement(Integer securityManagement) {
        this.securityManagement = securityManagement;
    }

    @Column(name = "FINAL_ESTIMATE", nullable = true, insertable = true, updatable = true, length = 3, precision = 0)
    public Integer getFinalEstimate() {
        return finalEstimate;
    }

    public void setFinalEstimate(Integer finalEstimate) {
        this.finalEstimate = finalEstimate;
    }

    @Column(name = "ARRIVAL_TIME", nullable = true, insertable = true, updatable = true, length = 3, precision = 0)
    public Integer getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Integer arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @Column(name = "EQUIPMENT_QUALITY", nullable = true, insertable = true, updatable = true, length = 3, precision = 0)
    public Integer getEquipmentQuality() {
        return equipmentQuality;
    }

    public void setEquipmentQuality(Integer equipmentQuality) {
        this.equipmentQuality = equipmentQuality;
    }

    @Column(name = "SERVICE", nullable = true, insertable = true, updatable = true, length = 3, precision = 0)
    public Integer getService() {
        return service;
    }

    public void setService(Integer service) {
        this.service = service;
    }

    @Column(name = "COMPREHENSIVE", nullable = true, insertable = true, updatable = true, length = 3, precision = 0)
    public Integer getComprehensive() {
        return comprehensive;
    }

    public void setComprehensive(Integer comprehensive) {
        this.comprehensive = comprehensive;
    }

    @Column(name = "REMARK", nullable = true, insertable = true, updatable = true, length = 1000, precision = 0)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    @Column(name = "SCM_CONTRACT_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getScmContractId() {
        return scmContractId;
    }
    public void setScmContractId(String scmContractId) {
        this.scmContractId = scmContractId;
    }

}