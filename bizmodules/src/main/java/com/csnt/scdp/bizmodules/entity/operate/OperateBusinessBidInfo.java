package com.csnt.scdp.bizmodules.entity.operate;

import com.csnt.scdp.framework.dto.BasePojo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:  OperateBusinessBidInfo
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-28 18:18:49
 */

@javax.persistence.Entity
@Table(name = "OPERATE_BUSINESS_BID_INFO")
public class OperateBusinessBidInfo extends BasePojo {
    private String uuid;
    private String state;
    private String regionId;
    private String projectName;
    private String comBidUnit;
    private String comBidNumber;
    private Date bidingDocStart;
    private Date bidingDocEnd;
    private BigDecimal bidingDocPrice;
    private BigDecimal bidBond;
    private String eotm;
    private Date bod;
    private String companyCode;
    private String departmentCode;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    private String operateBy;
    private Date operateTime;

    private String locTimezone;
    private String tblVersion;
    private Integer isVoid;
    private Integer seqNo;
    private String bidResult;
    private BigDecimal priceFixing;
    private String operateKeyProjectsInfoId;

    private String projectType;
    private String projectManager;
    private String generalEngineer;
    private String contractorOffice;
    private String contractName;
    private String contractNo;
    private String customerId;
    private String designerId;
    private String managementId;
    private BigDecimal contractSignMoney;
    private Date contractSignDate;
    private Date contractStartDate;
    private Date contractEndDate;
    private String contractDuration;
    private String defectsLiabilityPeriods;
    private String preoperation;
    private String countryCode;
    private String buildRegion;
    private String projectSourceType;
    private String taxRegion;
    private String taxType;
    private String projectScale;


    @Column(name = "SEQ_NO", nullable = true, insertable = true, updatable = true, length = 9, precision = 0)
    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    @Column(name = "UUID", nullable = false, insertable = true, updatable = true, length = 50, precision = 0)
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


    @Column(name = "REGION_ID", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    @Column(name = "PROJECT_NAME", nullable = true, insertable = true, updatable = true, length = 200, precision = 0)
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }


    @Column(name = "COM_BID_UNIT", nullable = true, insertable = true, updatable = true, length = 200, precision = 0)
    public String getComBidUnit() {
        return comBidUnit;
    }

    public void setComBidUnit(String comBidUnit) {
        this.comBidUnit = comBidUnit;
    }

    @Column(name = "COM_BID_NUMBER", nullable = true, insertable = true, updatable = true, length = 200, precision = 0)
    public String getComBidNumber() {
        return comBidNumber;
    }

    public void setComBidNumber(String comBidNumber) {
        this.comBidNumber = comBidNumber;
    }

    @Column(name = "BIDING_DOC_START", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getBidingDocStart() {
        return bidingDocStart;
    }

    public void setBidingDocStart(Date bidingDocStart) {
        this.bidingDocStart = bidingDocStart;
    }

    @Column(name = "BIDING_DOC_END", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getBidingDocEnd() {
        return bidingDocEnd;
    }

    public void setBidingDocEnd(Date bidingDocEnd) {
        this.bidingDocEnd = bidingDocEnd;
    }

    @Column(name = "BIDING_DOC_PRICE", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getBidingDocPrice() {
        return bidingDocPrice;
    }

    public void setBidingDocPrice(BigDecimal bidingDocPrice) {
        this.bidingDocPrice = bidingDocPrice;
    }

    @Column(name = "BID_BOND", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getBidBond() {
        return bidBond;
    }

    public void setBidBond(BigDecimal bidBond) {
        this.bidBond = bidBond;
    }

    @Column(name = "EOTM", nullable = true, insertable = true, updatable = true, length = 500, precision = 0)
    public String getEotm() {
        return eotm;
    }

    public void setEotm(String eotm) {
        this.eotm = eotm;
    }

    @Column(name = "BOD", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getBod() {
        return bod;
    }

    public void setBod(Date bod) {
        this.bod = bod;
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

    @Column(name = "PROJECT_TYPE", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    @Column(name = "BID_RESULT", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getBidResult() {
        return bidResult;
    }

    public void setBidResult(String bidResult) {
        this.bidResult = bidResult;
    }

    @Column(name = "OPERATE_KEY_PROJECTS_INFO_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getOperateKeyProjectsInfoId() {
        return operateKeyProjectsInfoId;
    }

    public void setOperateKeyProjectsInfoId(String operateKeyProjectsInfoId) {
        this.operateKeyProjectsInfoId = operateKeyProjectsInfoId;
    }

    @Column(name = "PROJECT_MANAGER", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    @Column(name = "GENERAL_ENGINEER", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getGeneralEngineer() {
        return generalEngineer;
    }

    public void setGeneralEngineer(String generalEngineer) {
        this.generalEngineer = generalEngineer;
    }

    @Column(name = "CONTRACTOR_OFFICE", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getContractorOffice() {
        return contractorOffice;
    }

    public void setContractorOffice(String contractorOffice) {
        this.contractorOffice = contractorOffice;
    }

    @Column(name = "CONTRACT_NAME", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    @Column(name = "CONTRACT_NO", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    @Column(name = "CUSTOMER_ID", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @Column(name = "DESIGNER_ID", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getDesignerId() {
        return designerId;
    }

    public void setDesignerId(String designerId) {
        this.designerId = designerId;
    }

    @Column(name = "MANAGEMENT_ID", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getManagementId() {
        return managementId;
    }

    public void setManagementId(String managementId) {
        this.managementId = managementId;
    }

    @Column(name = "CONTRACT_SIGN_MONEY", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getContractSignMoney() {
        return contractSignMoney;
    }

    public void setContractSignMoney(BigDecimal contractSignMoney) {
        this.contractSignMoney = contractSignMoney;
    }

    @Column(name = "CONTRACT_SIGN_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getContractSignDate() {
        return contractSignDate;
    }

    public void setContractSignDate(Date contractSignDate) {
        this.contractSignDate = contractSignDate;
    }

    @Column(name = "CONTRACT_START_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getContractStartDate() {
        return contractStartDate;
    }

    public void setContractStartDate(Date contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    @Column(name = "CONTRACT_END_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(Date contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    @Column(name = "CONTRACT_DURATION", nullable = true, insertable = true, updatable = true, length = 4000, precision
            = 0)
    public String getContractDuration() {
        return contractDuration;
    }

    public void setContractDuration(String contractDuration) {
        this.contractDuration = contractDuration;
    }

    @Column(name = "DEFECTS_LIABILITY_PERIODS", nullable = true, insertable = true, updatable = true, length = 4000,
            precision = 0)
    public String getDefectsLiabilityPeriods() {
        return defectsLiabilityPeriods;
    }

    public void setDefectsLiabilityPeriods(String defectsLiabilityPeriods) {
        this.defectsLiabilityPeriods = defectsLiabilityPeriods;
    }

    @Column(name = "PREOPERATION", nullable = true, insertable = true, updatable = true, length = 4000, precision = 0)
    public String getPreoperation() {
        return preoperation;
    }

    public void setPreoperation(String preoperation) {
        this.preoperation = preoperation;
    }

    @Column(name = "COUNTRY_CODE", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Column(name = "BUILD_REGION", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
    public String getBuildRegion() {
        return buildRegion;
    }

    public void setBuildRegion(String buildRegion) {
        this.buildRegion = buildRegion;
    }

    @Column(name = "PROJECT_SOURCE_TYPE", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
    public String getProjectSourceType() {
        return projectSourceType;
    }

    public void setProjectSourceType(String projectSourceType) {
        this.projectSourceType = projectSourceType;
    }

    @Column(name = "TAX_REGION", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
    public String getTaxRegion() {
        return taxRegion;
    }

    public void setTaxRegion(String taxRegion) {
        this.taxRegion = taxRegion;
    }

    @Column(name = "TAX_TYPE", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    @Column(name = "PROJECT_SCALE", nullable = true, insertable = true, updatable = true, length = 4000, precision = 0)
    public String getProjectScale() {
        return projectScale;
    }

    public void setProjectScale(String projectScale) {
        this.projectScale = projectScale;
    }

    @Column(name = "PRICE_FIXING", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getPriceFixing() {
        return priceFixing;
    }

    public void setPriceFixing(BigDecimal priceFixing) {
        this.priceFixing = priceFixing;
    }

    @Column(name = "OPERATE_BY", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getOperateBy() {
        return operateBy;
    }

    public void setOperateBy(String operateBy) {
        this.operateBy = operateBy;
    }

    @Column(name = "OPERATE_TIME", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }
}