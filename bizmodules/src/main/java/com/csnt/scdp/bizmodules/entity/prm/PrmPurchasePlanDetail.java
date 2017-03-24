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
 * Description:  PrmPurchasePlanDetail
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-25 14:45:16
 */

@javax.persistence.Entity
@Table(name = "PRM_PURCHASE_PLAN_DETAIL")
public class PrmPurchasePlanDetail extends BasePojo {
    private String uuid;
    private String prmBudgetRefId;
    private String prmBudgetType;
    private BigDecimal subPackageNo;
    private String purchaseType;
    private BigDecimal purchaseBudgetMoney;
    private String purchaseLevel;
    private String subjectCode;
    private String subjectProperty;
    private String name;
    private String model;
    private String factory;
    private BigDecimal amount;
    private BigDecimal budgetPrice;
    private String supplierId;
    private String supplierProperty;
    private Date arriveDate;
    private String technicalDrawing;
    private String arriveLocation;
    private String consignee;
    private String contactWay;
    private String remark;
    private String prmContractId;
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
    private String serialNumber;
    private Integer seqNo;
    private String unit;

    private String prmProjectMainId;
    private String purchasePackageId;
    private String isStamp;

    @Column(name = "PURCHASE_PACKAGE_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getPurchasePackageId() {
        return purchasePackageId;
    }

    public void setPurchasePackageId(String purchasePackageId) {
        this.purchasePackageId = purchasePackageId;
    }

    @Column(name = "PRM_PROJECT_MAIN_ID", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getPrmProjectMainId() {
        return prmProjectMainId;
    }

    public void setPrmProjectMainId(String prmProjectMainId) {
        this.prmProjectMainId = prmProjectMainId;
    }

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

    @Column(name = "PRM_BUDGET_REF_ID", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getPrmBudgetRefId() {
        return prmBudgetRefId;
    }

    public void setPrmBudgetRefId(String prmBudgetRefId) {
        this.prmBudgetRefId = prmBudgetRefId;
    }

    @Column(name = "PRM_BUDGET_TYPE", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getPrmBudgetType() {
        return prmBudgetType;
    }

    public void setPrmBudgetType(String prmBudgetType) {
        this.prmBudgetType = prmBudgetType;
    }

    @Column(name = "SUB_PACKAGE_NO", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getSubPackageNo() {
        return subPackageNo;
    }

    public void setSubPackageNo(BigDecimal subPackageNo) {
        this.subPackageNo = subPackageNo;
    }

    @Column(name = "PURCHASE_TYPE", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getPurchaseType() {
        return purchaseType;
    }

    public void setPurchaseType(String purchaseType) {
        this.purchaseType = purchaseType;
    }

    @Column(name = "PURCHASE_BUDGET_MONEY", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getPurchaseBudgetMoney() {
        return purchaseBudgetMoney;
    }

    public void setPurchaseBudgetMoney(BigDecimal purchaseBudgetMoney) {
        this.purchaseBudgetMoney = purchaseBudgetMoney;
    }

    @Column(name = "PURCHASE_LEVEL", nullable = true, insertable = true, updatable = true, length = 20, precision = 0)
    public String getPurchaseLevel() {
        return purchaseLevel;
    }

    public void setPurchaseLevel(String purchaseLevel) {
        this.purchaseLevel = purchaseLevel;
    }

    @Column(name = "SUBJECT_CODE", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    @Column(name = "SUBJECT_PROPERTY", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getSubjectProperty() {
        return subjectProperty;
    }

    public void setSubjectProperty(String subjectProperty) {
        this.subjectProperty = subjectProperty;
    }

    @Column(name = "NAME", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "MODEL", nullable = true, insertable = true, updatable = true, length = 4000, precision = 0)
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Column(name = "FACTORY", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    @Column(name = "AMOUNT", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Column(name = "BUDGET_PRICE", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getBudgetPrice() {
        return budgetPrice;
    }

    public void setBudgetPrice(BigDecimal budgetPrice) {
        this.budgetPrice = budgetPrice;
    }

    @Column(name = "SUPPLIER_ID", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    @Column(name = "SUPPLIER_PROPERTY", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getSupplierProperty() {
        return supplierProperty;
    }

    public void setSupplierProperty(String supplierProperty) {
        this.supplierProperty = supplierProperty;
    }

    @Column(name = "ARRIVE_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getArriveDate() {
        return arriveDate;
    }

    public void setArriveDate(Date arriveDate) {
        this.arriveDate = arriveDate;
    }

    @Column(name = "TECHNICAL_DRAWING", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getTechnicalDrawing() {
        return technicalDrawing;
    }

    public void setTechnicalDrawing(String technicalDrawing) {
        this.technicalDrawing = technicalDrawing;
    }

    @Column(name = "ARRIVE_LOCATION", nullable = true, insertable = true, updatable = true, length = 200, precision = 0)
    public String getArriveLocation() {
        return arriveLocation;
    }

    public void setArriveLocation(String arriveLocation) {
        this.arriveLocation = arriveLocation;
    }

    @Column(name = "CONSIGNEE", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    @Column(name = "CONTACT_WAY", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
    public String getContactWay() {
        return contactWay;
    }

    public void setContactWay(String contactWay) {
        this.contactWay = contactWay;
    }

    @Column(name = "REMARK", nullable = true, insertable = true, updatable = true, length = 500, precision = 0)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "PRM_CONTRACT_ID", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getPrmContractId() {
        return prmContractId;
    }

    public void setPrmContractId(String prmContractId) {
        this.prmContractId = prmContractId;
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

    @Column(name = "SERIAL_NUMBER", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Column(name = "UNIT", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Column(name = "IS_STAMP", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public String getIsStamp() {
        return isStamp;
    }

    public void setIsStamp(String isStamp) {
        this.isStamp = isStamp;
    }
}