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
 * Description:  ScmContractChangeD
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-08-29 11:21:14
 */

@javax.persistence.Entity
@Table(name = "SCM_CONTRACT_CHANGE_D")
public class ScmContractChangeD extends BasePojo {
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
    private String prmPurchaseReqDetailId;
    private String prmPurchaseReqId;
    private String prmPurchasePlanDetailId;
    private String purchaseType;
    private BigDecimal purchaseBudgetMoney;
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
    private String prmBudgetRefId;
    private String prmBudgetType;
    private String scmPurchaseReqId;
    private String scmContractId;
    private BigDecimal handleAmount;
    private String fallbackReason;
    private String puuid;
    private String serialNumber;
    private BigDecimal expectedPrice;
    private String scmContractCode;
    private String purchasePackageId;
    private String unit;
    private Integer isfallback;
    private String changeState;
    private String scmContractChangeId;

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

    @Column(name = "SCM_CONTRACT_CHANGE_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getScmContractChangeId() {
        return scmContractChangeId;
    }

    public void setScmContractChangeId(String scmContractChangeId) {
        this.scmContractChangeId = scmContractChangeId;
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

    @Column(name = "PRM_PURCHASE_REQ_DETAIL_ID", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getPrmPurchaseReqDetailId() {
        return prmPurchaseReqDetailId;
    }

    public void setPrmPurchaseReqDetailId(String prmPurchaseReqDetailId) {
        this.prmPurchaseReqDetailId = prmPurchaseReqDetailId;
    }

    @Column(name = "PRM_PURCHASE_REQ_ID", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getPrmPurchaseReqId() {
        return prmPurchaseReqId;
    }

    public void setPrmPurchaseReqId(String prmPurchaseReqId) {
        this.prmPurchaseReqId = prmPurchaseReqId;
    }

    @Column(name = "PRM_PURCHASE_PLAN_DETAIL_ID", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getPrmPurchasePlanDetailId() {
        return prmPurchasePlanDetailId;
    }

    public void setPrmPurchasePlanDetailId(String prmPurchasePlanDetailId) {
        this.prmPurchasePlanDetailId = prmPurchasePlanDetailId;
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

    @Column(name = "NAME", nullable = true, insertable = true, updatable = true, length = 600, precision = 0)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "MODEL", nullable = true, insertable = true, updatable = true, length = 600, precision = 0)
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

    @Column(name = "SUPPLIER_ID", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
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

    @Column(name = "REMARK", nullable = true, insertable = true, updatable = true, length = 1000, precision = 0)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    @Column(name = "SCM_PURCHASE_REQ_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getScmPurchaseReqId() {
        return scmPurchaseReqId;
    }

    public void setScmPurchaseReqId(String scmPurchaseReqId) {
        this.scmPurchaseReqId = scmPurchaseReqId;
    }

    @Column(name = "SCM_CONTRACT_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getScmContractId() {
        return scmContractId;
    }

    public void setScmContractId(String scmContractId) {
        this.scmContractId = scmContractId;
    }

    @Column(name = "HANDLE_AMOUNT", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getHandleAmount() {
        return handleAmount;
    }

    public void setHandleAmount(BigDecimal handleAmount) {
        this.handleAmount = handleAmount;
    }

    @Column(name = "FALLBACK_REASON", nullable = true, insertable = true, updatable = true, length = 500, precision = 0)
    public String getFallbackReason() {
        return fallbackReason;
    }

    public void setFallbackReason(String fallbackReason) {
        this.fallbackReason = fallbackReason;
    }

    @Column(name = "PUUID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getPuuid() {
        return puuid;
    }

    public void setPuuid(String puuid) {
        this.puuid = puuid;
    }

    @Column(name = "SERIAL_NUMBER", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Column(name = "EXPECTED_PRICE", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getExpectedPrice() {
        return expectedPrice;
    }

    public void setExpectedPrice(BigDecimal expectedPrice) {
        this.expectedPrice = expectedPrice;
    }

    @Column(name = "SCM_CONTRACT_CODE", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getScmContractCode() {
        return scmContractCode;
    }

    public void setScmContractCode(String scmContractCode) {
        this.scmContractCode = scmContractCode;
    }

    @Column(name = "PURCHASE_PACKAGE_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getPurchasePackageId() {
        return purchasePackageId;
    }

    public void setPurchasePackageId(String purchasePackageId) {
        this.purchasePackageId = purchasePackageId;
    }

    @Column(name = "UNIT", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Column(name = "ISFALLBACK", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public Integer getIsfallback() {
        return isfallback;
    }

    public void setIsfallback(Integer isfallback) {
        this.isfallback = isfallback;
    }

    @Column(name = "CHANGE_STATE", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public String getChangeState() {
        return changeState;
    }

    public void setChangeState(String changeState) {
        this.changeState = changeState;
    }

}