package com.csnt.scdp.bizmodules.modules.asset.card.dto;

import com.csnt.scdp.bizmodules.modules.cdm.cdmfile.dto.CdmFileRelationDto;
import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.asset.AssetCardTransfer;

import java.util.List;
import java.util.Date;
import java.math.BigDecimal;

/**
 * Description:  AssetCardDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-22 10:18:39
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.asset.AssetCardTransfer")
public class AssetCardTransferDto extends AssetCardTransfer {
    String cardCode;
    String assetCode;
    String assetName;
    String assetTypeCode;
    String deviceType;
    String specification;
    String model;
    String storeplace;
    String status;
    String fromNc;
    String purchaseTime;
    String discardTime;
    String limitMonth;
    String localValue;
    String monthDepreciation;
    String netValue;
    String factoryName;
    String releaseDate;
    String unit;
    String identificationNumber;
    String buildingProperty;
    String area;
    String chassisNumber;
    String vehicleNumber;
    String vehicleType;
    String authorizationCode;
    String checkedDate;
    String validDate;
    String annualCheckExpiredDate;
    String insuranceExpiredDate;
    String accessory;
    String source;
    String operationUnit;
    String operator;
    String operatorTel;
    String assetHandoverUuid;

    @CascadeChilds(FK = "dataId|uuid")
    private List<CdmFileRelationDto> cdmFileRelationDto;

    public List<CdmFileRelationDto> getCdmFileRelationDto() {
        return cdmFileRelationDto;
    }

    public void setCdmFileRelationDto(List<CdmFileRelationDto> childDto) {
        this.cdmFileRelationDto = childDto;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getAssetTypeCode() {
        return assetTypeCode;
    }

    public void setAssetTypeCode(String assetTypeCode) {
        this.assetTypeCode = assetTypeCode;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getStoreplace() {
        return storeplace;
    }

    public void setStoreplace(String storeplace) {
        this.storeplace = storeplace;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFromNc() {
        return fromNc;
    }

    public void setFromNc(String fromNc) {
        this.fromNc = fromNc;
    }

    public String getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(String purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public String getDiscardTime() {
        return discardTime;
    }

    public void setDiscardTime(String discardTime) {
        this.discardTime = discardTime;
    }

    public String getLimitMonth() {
        return limitMonth;
    }

    public void setLimitMonth(String limitMonth) {
        this.limitMonth = limitMonth;
    }

    public String getLocalValue() {
        return localValue;
    }

    public void setLocalValue(String localValue) {
        this.localValue = localValue;
    }

    public String getMonthDepreciation() {
        return monthDepreciation;
    }

    public void setMonthDepreciation(String monthDepreciation) {
        this.monthDepreciation = monthDepreciation;
    }

    public String getNetValue() {
        return netValue;
    }

    public void setNetValue(String netValue) {
        this.netValue = netValue;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getBuildingProperty() {
        return buildingProperty;
    }

    public void setBuildingProperty(String buildingProperty) {
        this.buildingProperty = buildingProperty;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getChassisNumber() {
        return chassisNumber;
    }

    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public String getCheckedDate() {
        return checkedDate;
    }

    public void setCheckedDate(String checkedDate) {
        this.checkedDate = checkedDate;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public String getAnnualCheckExpiredDate() {
        return annualCheckExpiredDate;
    }

    public void setAnnualCheckExpiredDate(String annualCheckExpiredDate) {
        this.annualCheckExpiredDate = annualCheckExpiredDate;
    }

    public String getInsuranceExpiredDate() {
        return insuranceExpiredDate;
    }

    public void setInsuranceExpiredDate(String insuranceExpiredDate) {
        this.insuranceExpiredDate = insuranceExpiredDate;
    }

    public String getAccessory() {
        return accessory;
    }

    public void setAccessory(String accessory) {
        this.accessory = accessory;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getOperationUnit() {
        return operationUnit;
    }

    public void setOperationUnit(String operationUnit) {
        this.operationUnit = operationUnit;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperatorTel() {
        return operatorTel;
    }

    public void setOperatorTel(String operatorTel) {
        this.operatorTel = operatorTel;
    }

    public String getAssetHandoverUuid() {
        return assetHandoverUuid;
    }

    public void setAssetHandoverUuid(String assetHandoverUuid) {
        this.assetHandoverUuid = assetHandoverUuid;
    }
}