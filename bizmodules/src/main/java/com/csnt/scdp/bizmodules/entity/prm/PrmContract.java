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
 * Description:  PrmContract
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-04-08 10:18:18
 */

@javax.persistence.Entity
@Table(name = "PRM_CONTRACT")
public class PrmContract extends BasePojo {
    private String uuid;
    private String operateBusinessBidInfoId;
    private String projectName;
    private String projectManager;
    private String generalEngineer;
    private String contractorOffice;
    private String contractName;
    private String contractNo;
    private String state;
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
    private Date successBidDate;
    private String projectScale;
    private String prmProjectMainId;
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
    private String innerPurchaseReqId;
    private Integer isMajorProject;
    private BigDecimal contractNowMoney;
    private BigDecimal profitRate;
    private Date confirmedDate;
    private Integer isBusinessTax;
    private String prmCodeType;
    private String affiliatedInstitutions;
    private Date examDate;

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

    @Column(name = "EXAM_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    @Column(name = "OPERATE_BUSINESS_BID_INFO_ID", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getOperateBusinessBidInfoId() {
        return operateBusinessBidInfoId;
    }

    public void setOperateBusinessBidInfoId(String operateBusinessBidInfoId) {
        this.operateBusinessBidInfoId = operateBusinessBidInfoId;
    }

    @Column(name = "PROJECT_NAME", nullable = true, insertable = true, updatable = true, length = 200, precision = 0)
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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

    @Column(name = "CONTRACT_NAME", nullable = true, insertable = true, updatable = true, length = 300, precision = 0)
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

    @Column(name = "STATE", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name = "CUSTOMER_ID", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @Column(name = "DESIGNER_ID", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
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

    @Column(name = "CONTRACT_DURATION", nullable = true, insertable = true, updatable = true, length = 4000, precision = 0)
    public String getContractDuration() {
        return contractDuration;
    }

    public void setContractDuration(String contractDuration) {
        this.contractDuration = contractDuration;
    }

    @Column(name = "DEFECTS_LIABILITY_PERIODS", nullable = true, insertable = true, updatable = true, length = 4000, precision = 0)
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

    @Column(name = "COUNTRY_CODE", nullable = true, insertable = true, updatable = true, length = 16, precision = 0)
    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Column(name = "BUILD_REGION", nullable = true, insertable = true, updatable = true, length = 16, precision = 0)
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

    @Column(name = "SUCCESS_BID_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getSuccessBidDate() {
        return successBidDate;
    }

    public void setSuccessBidDate(Date successBidDate) {
        this.successBidDate = successBidDate;
    }

    @Column(name = "PROJECT_SCALE", nullable = true, insertable = true, updatable = true, length = 4000, precision = 0)
    public String getProjectScale() {
        return projectScale;
    }

    public void setProjectScale(String projectScale) {
        this.projectScale = projectScale;
    }

    @Column(name = "PRM_PROJECT_MAIN_ID", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getPrmProjectMainId() {
        return prmProjectMainId;
    }

    public void setPrmProjectMainId(String prmProjectMainId) {
        this.prmProjectMainId = prmProjectMainId;
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

    @Column(name = "SEQ_NO", nullable = true, insertable = true, updatable = true, length = 9, precision = 0)
    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    @Column(name = "INNER_PURCHASE_REQ_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getInnerPurchaseReqId() {
        return innerPurchaseReqId;
    }

    public void setInnerPurchaseReqId(String innerPurchaseReqId) {
        this.innerPurchaseReqId = innerPurchaseReqId;
    }

    @Column(name = "IS_MAJOR_PROJECT", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public Integer getIsMajorProject() {
        return isMajorProject;
    }

    public void setIsMajorProject(Integer isMajorProject) {
        this.isMajorProject = isMajorProject;
    }

    @Column(name = "CONTRACT_NOW_MONEY", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getContractNowMoney() {
        return contractNowMoney;
    }

    public void setContractNowMoney(BigDecimal contractNowMoney) {
        this.contractNowMoney = contractNowMoney;
    }

    @Column(name = "PROFIT_RATE", nullable = true, insertable = true, updatable = true, length = 15, precision = 2)
    public BigDecimal getProfitRate() {
        return profitRate;
    }

    public void setProfitRate(BigDecimal profitRate) {
        this.profitRate = profitRate;
    }

    @Column(name = "CONFIRMED_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getConfirmedDate() {
        return confirmedDate;
    }

    public void setConfirmedDate(Date confirmedDate) {
        this.confirmedDate = confirmedDate;
    }

    @Column(name = "IS_BUSINESS_TAX", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public Integer getIsBusinessTax() {
        return isBusinessTax;
    }

    public void setIsBusinessTax(Integer isBusinessTax) {
        this.isBusinessTax = isBusinessTax;
    }

    @Column(name = "PRM_CODE_TYPE", nullable = true, insertable = true, updatable = true, length = 16, precision = 0)
    public String getPrmCodeType() {
        return prmCodeType;
    }

    public void setPrmCodeType(String prmCodeType) {
        this.prmCodeType = prmCodeType;
    }

    @Column(name = "AFFILIATED_INSTITUTIONS", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getAffiliatedInstitutions() {
        return affiliatedInstitutions;
    }

    public void setAffiliatedInstitutions(String affiliatedInstitutions) {
        this.affiliatedInstitutions = affiliatedInstitutions;
    }
}