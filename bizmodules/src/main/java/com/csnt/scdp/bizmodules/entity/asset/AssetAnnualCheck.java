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
 * Description:  AssetAnnualCheck
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-24 09:59:12
 */

@javax.persistence.Entity
@Table(name = "ASSET_ANNUAL_CHECK")
public class AssetAnnualCheck extends BasePojo {
    private String uuid;
    private String cardCode;
    private Date annualCheckTime;
    private String companyDeviceManager;
    private String deptDeviceManager;
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
    private Integer seqNo;
    private String cardUuid;
    private String annualCheckCompany;
    private BigDecimal annualCheckFee;

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

    @Column(name = "CARD_CODE", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    @Column(name = "ANNUAL_CHECK_TIME", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getAnnualCheckTime() {
        return annualCheckTime;
    }

    public void setAnnualCheckTime(Date annualCheckTime) {
        this.annualCheckTime = annualCheckTime;
    }

    @Column(name = "COMPANY_DEVICE_MANAGER", nullable = true, insertable = true, updatable = true, length = 250, precision = 0)
    public String getCompanyDeviceManager() {
        return companyDeviceManager;
    }

    public void setCompanyDeviceManager(String companyDeviceManager) {
        this.companyDeviceManager = companyDeviceManager;
    }

    @Column(name = "DEPT_DEVICE_MANAGER", nullable = true, insertable = true, updatable = true, length = 250, precision = 0)
    public String getDeptDeviceManager() {
        return deptDeviceManager;
    }

    public void setDeptDeviceManager(String deptDeviceManager) {
        this.deptDeviceManager = deptDeviceManager;
    }

    @Column(name = "DESCP", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
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

    @Column(name = "CARD_UUID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getCardUuid() {
        return cardUuid;
    }

    public void setCardUuid(String cardUuid) {
        this.cardUuid = cardUuid;
    }

    @Column(name = "ANNUAL_CHECK_COMPANY", nullable = true, insertable = true, updatable = true, length = 500, precision = 0)
    public String getAnnualCheckCompany() {
        return annualCheckCompany;
    }

    public void setAnnualCheckCompany(String annualCheckCompany) {
        this.annualCheckCompany = annualCheckCompany;
    }

    @Column(name = "ANNUAL_CHECK_FEE", nullable = true, insertable = true, updatable = true, length = 8, precision = 2)
    public BigDecimal getAnnualCheckFee() {
        return annualCheckFee;
    }

    public void setAnnualCheckFee(BigDecimal annualCheckFee) {
        this.annualCheckFee = annualCheckFee;
    }
}