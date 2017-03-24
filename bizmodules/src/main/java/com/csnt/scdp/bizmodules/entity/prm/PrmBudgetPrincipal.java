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
 * Description:  PrmBudgetPrincipal
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-27 15:03:57
 */

@javax.persistence.Entity
@Table(name = "PRM_BUDGET_PRINCIPAL")
public class PrmBudgetPrincipal extends BasePojo {
    private String prmProjectMainId;
    private String uuid;
    private String serialNumber;
    private Date arrivalDate;
    private String supplerProperty;
    private String equipmentSubject;
    private String equipmentName;
    private String equipmentModel;
    private String factory;
    private String unit;
    private BigDecimal amount; //计划数量
    private BigDecimal contractAmount; //合同数量
    private BigDecimal contractPrice;
    private BigDecimal contractTotalValue;
    private BigDecimal bidPrice;
    private BigDecimal bidTotalValue;
    private BigDecimal schemingPrice;
    private BigDecimal schemingTotalValue;
    private BigDecimal schemingGrossProfit;
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
    private String splitFromUuid;
    private String isStamp;

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
    @GenericGenerator(name = "idGenerator", strategy = "assigned")
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Column(name = "SERIAL_NUMBER", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Column(name = "ARRIVAL_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    @Column(name = "SUPPLER_PROPERTY", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getSupplerProperty() {
        return supplerProperty;
    }

    public void setSupplerProperty(String supplerProperty) {
        this.supplerProperty = supplerProperty;
    }

    @Column(name = "EQUIPMENT_SUBJECT", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getEquipmentSubject() {
        return equipmentSubject;
    }

    public void setEquipmentSubject(String equipmentSubject) {
        this.equipmentSubject = equipmentSubject;
    }

    @Column(name = "EQUIPMENT_NAME", nullable = true, insertable = true, updatable = true, length = 500, precision = 0)
    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    @Column(name = "EQUIPMENT_MODEL", nullable = true, insertable = true, updatable = true, length = 4000, precision = 0)
    public String getEquipmentModel() {
        return equipmentModel;
    }

    public void setEquipmentModel(String equipmentModel) {
        this.equipmentModel = equipmentModel;
    }

    @Column(name = "FACTORY", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    @Column(name = "UNIT", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Column(name = "AMOUNT", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Column(name = "CONTRACT_AMOUNT", nullable = true, insertable = true, updatable = true, length = 18, precision = 2)
    public BigDecimal getContractAmount() {
        return contractAmount;
    }

    public void setContractAmount(BigDecimal contractAmount) {
        this.contractAmount = contractAmount;
    }

    @Column(name = "CONTRACT_PRICE", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getContractPrice() {
        return contractPrice;
    }

    public void setContractPrice(BigDecimal contractPrice) {
        this.contractPrice = contractPrice;
    }

    @Column(name = "CONTRACT_TOTAL_VALUE", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getContractTotalValue() {
        return contractTotalValue;
    }

    public void setContractTotalValue(BigDecimal contractTotalValue) {
        this.contractTotalValue = contractTotalValue;
    }

    @Column(name = "BID_PRICE", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(BigDecimal bidPrice) {
        this.bidPrice = bidPrice;
    }

    @Column(name = "BID_TOTAL_VALUE", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getBidTotalValue() {
        return bidTotalValue;
    }

    public void setBidTotalValue(BigDecimal bidTotalValue) {
        this.bidTotalValue = bidTotalValue;
    }

    @Column(name = "SCHEMING_PRICE", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getSchemingPrice() {
        return schemingPrice;
    }

    public void setSchemingPrice(BigDecimal schemingPrice) {
        this.schemingPrice = schemingPrice;
    }

    @Column(name = "SCHEMING_TOTAL_VALUE", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getSchemingTotalValue() {
        return schemingTotalValue;
    }

    public void setSchemingTotalValue(BigDecimal schemingTotalValue) {
        this.schemingTotalValue = schemingTotalValue;
    }

    @Column(name = "SCHEMING_GROSS_PROFIT", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getSchemingGrossProfit() {
        return schemingGrossProfit;
    }

    public void setSchemingGrossProfit(BigDecimal schemingGrossProfit) {
        this.schemingGrossProfit = schemingGrossProfit;
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

    @Column(name = "PRM_PROJECT_MAIN_ID", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getPrmProjectMainId() {
        return prmProjectMainId;
    }

    public void setPrmProjectMainId(String prmProjectMainId) {
        this.prmProjectMainId = prmProjectMainId;
    }

    @Column(name = "SPLIT_FROM_UUID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getSplitFromUuid() {
        return splitFromUuid;
    }

    public void setSplitFromUuid(String splitFromUuid) {
        this.splitFromUuid = splitFromUuid;
    }

    @Column(name = "IS_STAMP", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public String getIsStamp() {
        return isStamp;
    }

    public void setIsStamp(String isStamp) {
        this.isStamp = isStamp;
    }
}