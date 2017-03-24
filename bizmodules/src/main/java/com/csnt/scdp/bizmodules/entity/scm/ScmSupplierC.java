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
 * Description:  ScmSupplierC
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-07-28 17:10:24
 */

@javax.persistence.Entity
@Table(name = "SCM_SUPPLIER_C")
public class ScmSupplierC extends BasePojo {
    private String uuid;
    private String supplierCode;
    private String supplierCodeForGroup;
    private String inoutType;
    private String completeName;
    private String oldName;
    private String englishName;
    private String simpleName;
    private String supplierOthername;
    private String industryType;
    private String apName;
    private String apId;
    private BigDecimal registeredCapital;
    private String registeredCapitalCurrency;
    private BigDecimal fixedAsset;
    private String fixedAssetCurrency;
    private Date registeredDate;
    private Long employeeNumber;
    private String contactInfo;
    private String url;
    private String country;
    private String province;
    private String city;
    private String registeredAddress;
    private String postcode;
    private String businessScope;
    private String companyIntroduce;
    private String mainClient;
    private String swiftCode;
    private Date startDate;
    private Date endDate;
    private String supplierStatus;
    private String supplierType;
    private String enterpriseScale;
    private String supplierProperty;
    private String organizationType;
    private String enterpriseType;
    private String levelCode;
    private String taxRegistrationNo;
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
    private String state;
    private String ncCode;
    private String taxTypes;
    private String supplierGenre;
    private Integer isEBusiness;
    private String changeType;
    private String puuid;


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

    @Column(name = "SUPPLIER_CODE", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    @Column(name = "SUPPLIER_CODE_FOR_GROUP", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getSupplierCodeForGroup() {
        return supplierCodeForGroup;
    }

    public void setSupplierCodeForGroup(String supplierCodeForGroup) {
        this.supplierCodeForGroup = supplierCodeForGroup;
    }

    @Column(name = "INOUT_TYPE", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getInoutType() {
        return inoutType;
    }

    public void setInoutType(String inoutType) {
        this.inoutType = inoutType;
    }

    @Column(name = "COMPLETE_NAME", nullable = true, insertable = true, updatable = true, length = 250, precision = 0)
    public String getCompleteName() {
        return completeName;
    }

    public void setCompleteName(String completeName) {
        this.completeName = completeName;
    }

    @Column(name = "OLD_NAME", nullable = true, insertable = true, updatable = true, length = 1000, precision = 0)
    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    @Column(name = "ENGLISH_NAME", nullable = true, insertable = true, updatable = true, length = 1000, precision = 0)
    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    @Column(name = "SIMPLE_NAME", nullable = true, insertable = true, updatable = true, length = 512, precision = 0)
    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    @Column(name = "SUPPLIER_OTHERNAME", nullable = true, insertable = true, updatable = true, length = 250, precision = 0)
    public String getSupplierOthername() {
        return supplierOthername;
    }

    public void setSupplierOthername(String supplierOthername) {
        this.supplierOthername = supplierOthername;
    }

    @Column(name = "INDUSTRY_TYPE", nullable = true, insertable = true, updatable = true, length = 200, precision = 0)
    public String getIndustryType() {
        return industryType;
    }

    public void setIndustryType(String industryType) {
        this.industryType = industryType;
    }

    @Column(name = "AP_NAME", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
    public String getApName() {
        return apName;
    }

    public void setApName(String apName) {
        this.apName = apName;
    }

    @Column(name = "AP_ID", nullable = true, insertable = true, updatable = true, length = 20, precision = 0)
    public String getApId() {
        return apId;
    }

    public void setApId(String apId) {
        this.apId = apId;
    }

    @Column(name = "REGISTERED_CAPITAL", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getRegisteredCapital() {
        return registeredCapital;
    }

    public void setRegisteredCapital(BigDecimal registeredCapital) {
        this.registeredCapital = registeredCapital;
    }

    @Column(name = "REGISTERED_CAPITAL_CURRENCY", nullable = true, insertable = true, updatable = true, length = 20, precision = 0)
    public String getRegisteredCapitalCurrency() {
        return registeredCapitalCurrency;
    }

    public void setRegisteredCapitalCurrency(String registeredCapitalCurrency) {
        this.registeredCapitalCurrency = registeredCapitalCurrency;
    }

    @Column(name = "FIXED_ASSET", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getFixedAsset() {
        return fixedAsset;
    }

    public void setFixedAsset(BigDecimal fixedAsset) {
        this.fixedAsset = fixedAsset;
    }

    @Column(name = "FIXED_ASSET_CURRENCY", nullable = true, insertable = true, updatable = true, length = 20, precision = 0)
    public String getFixedAssetCurrency() {
        return fixedAssetCurrency;
    }

    public void setFixedAssetCurrency(String fixedAssetCurrency) {
        this.fixedAssetCurrency = fixedAssetCurrency;
    }

    @Column(name = "REGISTERED_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Date registeredDate) {
        this.registeredDate = registeredDate;
    }

    @Column(name = "EMPLOYEE_NUMBER", nullable = true, insertable = true, updatable = true, length = 18, precision = 0)
    public Long getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(Long employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    @Column(name = "CONTACT_INFO", nullable = true, insertable = true, updatable = true, length = 400, precision = 0)
    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    @Column(name = "URL", nullable = true, insertable = true, updatable = true, length = 400, precision = 0)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(name = "COUNTRY", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Column(name = "PROVINCE", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Column(name = "CITY", nullable = true, insertable = true, updatable = true, length = 400, precision = 0)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name = "REGISTERED_ADDRESS", nullable = true, insertable = true, updatable = true, length = 1000, precision = 0)
    public String getRegisteredAddress() {
        return registeredAddress;
    }

    public void setRegisteredAddress(String registeredAddress) {
        this.registeredAddress = registeredAddress;
    }

    @Column(name = "POSTCODE", nullable = true, insertable = true, updatable = true, length = 200, precision = 0)
    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    @Column(name = "BUSINESS_SCOPE", nullable = true, insertable = true, updatable = true, length = 2000, precision = 0)
    public String getBusinessScope() {
        return businessScope;
    }

    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope;
    }

    @Column(name = "COMPANY_INTRODUCE", nullable = true, insertable = true, updatable = true, length = 4000, precision = 0)
    public String getCompanyIntroduce() {
        return companyIntroduce;
    }

    public void setCompanyIntroduce(String companyIntroduce) {
        this.companyIntroduce = companyIntroduce;
    }

    @Column(name = "MAIN_CLIENT", nullable = true, insertable = true, updatable = true, length = 250, precision = 0)
    public String getMainClient() {
        return mainClient;
    }

    public void setMainClient(String mainClient) {
        this.mainClient = mainClient;
    }

    @Column(name = "SWIFT_CODE", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    @Column(name = "START_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Column(name = "END_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Column(name = "SUPPLIER_STATUS", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
    public String getSupplierStatus() {
        return supplierStatus;
    }

    public void setSupplierStatus(String supplierStatus) {
        this.supplierStatus = supplierStatus;
    }

    @Column(name = "SUPPLIER_TYPE", nullable = true, insertable = true, updatable = true, length = 20, precision = 0)
    public String getSupplierType() {
        return supplierType;
    }

    public void setSupplierType(String supplierType) {
        this.supplierType = supplierType;
    }

    @Column(name = "ENTERPRISE_SCALE", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public String getEnterpriseScale() {
        return enterpriseScale;
    }

    public void setEnterpriseScale(String enterpriseScale) {
        this.enterpriseScale = enterpriseScale;
    }

    @Column(name = "SUPPLIER_PROPERTY", nullable = true, insertable = true, updatable = true, length = 2, precision = 0)
    public String getSupplierProperty() {
        return supplierProperty;
    }

    public void setSupplierProperty(String supplierProperty) {
        this.supplierProperty = supplierProperty;
    }

    @Column(name = "ORGANIZATION_TYPE", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    @Column(name = "ENTERPRISE_TYPE", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
    public String getEnterpriseType() {
        return enterpriseType;
    }

    public void setEnterpriseType(String enterpriseType) {
        this.enterpriseType = enterpriseType;
    }

    @Column(name = "LEVEL_CODE", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    @Column(name = "TAX_REGISTRATION_NO", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getTaxRegistrationNo() {
        return taxRegistrationNo;
    }

    public void setTaxRegistrationNo(String taxRegistrationNo) {
        this.taxRegistrationNo = taxRegistrationNo;
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

    @Column(name = "STATE", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name = "NC_CODE", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getNcCode() {
        return ncCode;
    }

    public void setNcCode(String ncCode) {
        this.ncCode = ncCode;
    }

    @Column(name = "TAX_TYPES", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public String getTaxTypes() {
        return taxTypes;
    }

    public void setTaxTypes(String taxTypes) {
        this.taxTypes = taxTypes;
    }

    @Column(name = "SUPPLIER_GENRE", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public String getSupplierGenre() {
        return supplierGenre;
    }

    public void setSupplierGenre(String supplierGenre) {
        this.supplierGenre = supplierGenre;
    }

    @Column(name = "IS_E_BUSINESS", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public Integer getIsEBusiness() {
        return isEBusiness;
    }

    public void setIsEBusiness(Integer isEBusiness) {
        this.isEBusiness = isEBusiness;
    }

    @Column(name = "CHANGE_TYPE", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    @Column(name = "PUUID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getPuuid() { return puuid;}

    public void setPuuid(String puuid) {this.puuid = puuid;}
}