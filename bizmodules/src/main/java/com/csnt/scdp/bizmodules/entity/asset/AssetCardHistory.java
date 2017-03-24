package com.csnt.scdp.bizmodules.entity.asset;

import com.csnt.scdp.framework.dto.BasePojo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.math.BigDecimal;

/**
 * Description:  AssetCardHistory
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 *          2016-12-12 16:56:28
 */

@javax.persistence.Entity
@Table(name = "ASSET_CARD_HISTORY")
public class AssetCardHistory extends BasePojo {
    private String uuid;
    private String assetCardUuid;
    private String historyField;
    private String cardCode;
    private String assetCode;
    private String assetName;
    private String assetTypeCode;
    private String deviceType;
    private String specification;
    private String model;
    private String storeplace;
    private String state;
    private String officeId;
    private String endUserCode;
    private String endUserName;
    private String liablePerson;
    private String fromNc;
    private Date purchaseTime;
    private Date discardTime;
    private BigDecimal limitMonth;
    private BigDecimal localValue;
    private BigDecimal monthDepreciation;
    private BigDecimal netValue;
    private String factoryName;
    private Date releaseDate;
    private String identificationNumber;
    private String buildingProperty;
    private BigDecimal area;
    private String chassisNumber;
    private String vehicleNumber;
    private String vehicleType;
    private String authorizationCode;
    private Date checkedDate;
    private Date validDate;
    private Date annualCheckExpiredDate;
    private Date insuranceExpiredDate;
    private String accessory;
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
    private String source;
    private Integer seqNo;
    private String operationUnit;
    private String operator;
    private String operatorTel;
    private String status;
    private String unit;

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

    @Column(name = "ASSET_CARD_UUID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getAssetCardUuid() {
        return assetCardUuid;
    }

    public void setAssetCardUuid(String assetCardUuid) {
        this.assetCardUuid = assetCardUuid;
    }

    @Column(name = "HISTORY_FIELD", nullable = true, insertable = true, updatable = true, length = 25, precision = 0)
    public String getHistoryField() {
        return historyField;
    }

    public void setHistoryField(String historyField) {
        this.historyField = historyField;
    }

    @Column(name = "CARD_CODE", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    @Column(name = "ASSET_CODE", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    @Column(name = "ASSET_NAME", nullable = true, insertable = true, updatable = true, length = 250, precision = 0)
    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    @Column(name = "ASSET_TYPE_CODE", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getAssetTypeCode() {
        return assetTypeCode;
    }

    public void setAssetTypeCode(String assetTypeCode) {
        this.assetTypeCode = assetTypeCode;
    }

    @Column(name = "DEVICE_TYPE", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    @Column(name = "SPECIFICATION", nullable = true, insertable = true, updatable = true, length = 250, precision = 0)
    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    @Column(name = "MODEL", nullable = true, insertable = true, updatable = true, length = 250, precision = 0)
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Column(name = "STOREPLACE", nullable = true, insertable = true, updatable = true, length = 200, precision = 0)
    public String getStoreplace() {
        return storeplace;
    }

    public void setStoreplace(String storeplace) {
        this.storeplace = storeplace;
    }

    @Column(name = "STATE", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name = "OFFICE_ID", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    @Column(name = "END_USER_CODE", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getEndUserCode() {
        return endUserCode;
    }

    public void setEndUserCode(String endUserCode) {
        this.endUserCode = endUserCode;
    }

    @Column(name = "END_USER_NAME", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getEndUserName() {
        return endUserName;
    }

    public void setEndUserName(String endUserName) {
        this.endUserName = endUserName;
    }

    @Column(name = "LIABLE_PERSON", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getLiablePerson() {
        return liablePerson;
    }

    public void setLiablePerson(String liablePerson) {
        this.liablePerson = liablePerson;
    }

    @Column(name = "FROM_NC", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public String getFromNc() {
        return fromNc;
    }

    public void setFromNc(String fromNc) {
        this.fromNc = fromNc;
    }

    @Column(name = "PURCHASE_TIME", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(Date purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    @Column(name = "DISCARD_TIME", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getDiscardTime() {
        return discardTime;
    }

    public void setDiscardTime(Date discardTime) {
        this.discardTime = discardTime;
    }

    @Column(name = "LIMIT_MONTH", nullable = true, insertable = true, updatable = true, length = 0, precision = -127)
    public BigDecimal getLimitMonth() {
        return limitMonth;
    }

    public void setLimitMonth(BigDecimal limitMonth) {
        this.limitMonth = limitMonth;
    }

    @Column(name = "LOCAL_VALUE", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getLocalValue() {
        return localValue;
    }

    public void setLocalValue(BigDecimal localValue) {
        this.localValue = localValue;
    }

    @Column(name = "MONTH_DEPRECIATION", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getMonthDepreciation() {
        return monthDepreciation;
    }

    public void setMonthDepreciation(BigDecimal monthDepreciation) {
        this.monthDepreciation = monthDepreciation;
    }

    @Column(name = "NET_VALUE", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getNetValue() {
        return netValue;
    }

    public void setNetValue(BigDecimal netValue) {
        this.netValue = netValue;
    }

    @Column(name = "FACTORY_NAME", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    @Column(name = "RELEASE_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Column(name = "IDENTIFICATION_NUMBER", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    @Column(name = "BUILDING_PROPERTY", nullable = true, insertable = true, updatable = true, length = 200, precision = 0)
    public String getBuildingProperty() {
        return buildingProperty;
    }

    public void setBuildingProperty(String buildingProperty) {
        this.buildingProperty = buildingProperty;
    }

    @Column(name = "AREA", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    @Column(name = "CHASSIS_NUMBER", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getChassisNumber() {
        return chassisNumber;
    }

    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    @Column(name = "VEHICLE_NUMBER", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    @Column(name = "VEHICLE_TYPE", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    @Column(name = "AUTHORIZATION_CODE", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    @Column(name = "CHECKED_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getCheckedDate() {
        return checkedDate;
    }

    public void setCheckedDate(Date checkedDate) {
        this.checkedDate = checkedDate;
    }

    @Column(name = "VALID_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getValidDate() {
        return validDate;
    }

    public void setValidDate(Date validDate) {
        this.validDate = validDate;
    }

    @Column(name = "ANNUAL_CHECK_EXPIRED_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getAnnualCheckExpiredDate() {
        return annualCheckExpiredDate;
    }

    public void setAnnualCheckExpiredDate(Date annualCheckExpiredDate) {
        this.annualCheckExpiredDate = annualCheckExpiredDate;
    }

    @Column(name = "INSURANCE_EXPIRED_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getInsuranceExpiredDate() {
        return insuranceExpiredDate;
    }

    public void setInsuranceExpiredDate(Date insuranceExpiredDate) {
        this.insuranceExpiredDate = insuranceExpiredDate;
    }

    @Column(name = "ACCESSORY", nullable = true, insertable = true, updatable = true, length = 2000, precision = 0)
    public String getAccessory() {
        return accessory;
    }

    public void setAccessory(String accessory) {
        this.accessory = accessory;
    }

    @Column(name = "DESCP", nullable = true, insertable = true, updatable = true, length = 2000, precision = 0)
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

    @Column(name = "UPDATE_BY", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name = "UPDATE_TIME", nullable = false, insertable = true, updatable = true, length = 0, precision = 0)
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

    @Column(name = "SOURCE", nullable = true, insertable = true, updatable = true, length = 250, precision = 0)
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Column(name = "SEQ_NO", nullable = true, insertable = true, updatable = true, length = 9, precision = 0)
    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    @Column(name = "OPERATION_UNIT", nullable = true, insertable = true, updatable = true, length = 500, precision = 0)
    public String getOperationUnit() {
        return operationUnit;
    }

    public void setOperationUnit(String operationUnit) {
        this.operationUnit = operationUnit;
    }

    @Column(name = "OPERATOR", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Column(name = "OPERATOR_TEL", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getOperatorTel() {
        return operatorTel;
    }

    public void setOperatorTel(String operatorTel) {
        this.operatorTel = operatorTel;
    }

    @Column(name = "STATUS", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "UNIT", nullable = true, insertable = true, updatable = true, length = 250, precision = 0)
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}