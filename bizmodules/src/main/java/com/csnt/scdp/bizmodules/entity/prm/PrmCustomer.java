package com.csnt.scdp.bizmodules.entity.prm;

import com.csnt.scdp.framework.dto.BasePojo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Description:  PrmCustomer
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-29 10:33:22
 */

@javax.persistence.Entity
@Table(name = "PRM_CUSTOMER")
public class PrmCustomer extends BasePojo {
    private String uuid;
    private String customerCode;
    private String customerName;
    private String customerNation;
    private String customerProvince;
    private String customerAddress;
    private String customerPostalcode;
    private String customerTel;
    private String customerLink;
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
    private String  ncCode;
    private String  taxNo;
    private Integer  isSubcompany;

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

    @Column(name = "CUSTOMER_CODE", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    @Column(name = "CUSTOMER_NAME", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Column(name = "CUSTOMER_NATION", nullable = true, insertable = true, updatable = true, length = 30, precision = 0)
    public String getCustomerNation() {
        return customerNation;
    }

    public void setCustomerNation(String customerNation) {
        this.customerNation = customerNation;
    }

    @Column(name = "CUSTOMER_PROVINCE", nullable = true, insertable = true, updatable = true, length = 30, precision = 0)
    public String getCustomerProvince() {
        return customerProvince;
    }

    public void setCustomerProvince(String customerProvince) {
        this.customerProvince = customerProvince;
    }

    @Column(name = "CUSTOMER_ADDRESS", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    @Column(name = "CUSTOMER_POSTALCODE", nullable = true, insertable = true, updatable = true, length = 30, precision = 0)
    public String getCustomerPostalcode() {
        return customerPostalcode;
    }

    public void setCustomerPostalcode(String customerPostalcode) {
        this.customerPostalcode = customerPostalcode;
    }

    @Column(name = "CUSTOMER_TEL", nullable = true, insertable = true, updatable = true, length = 30, precision = 0)
    public String getCustomerTel() {
        return customerTel;
    }

    public void setCustomerTel(String customerTel) {
        this.customerTel = customerTel;
    }

    @Column(name = "CUSTOMER_LINK", nullable = true, insertable = true, updatable = true, length = 30, precision = 0)
    public String getCustomerLink() {
        return customerLink;
    }

    public void setCustomerLink(String customerLink) {
        this.customerLink = customerLink;
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

    @Column(name = "NC_CODE", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getNcCode() {
        return ncCode;
    }

    public void setNcCode(String ncCode) {
        this.ncCode = ncCode;
    }

    @Column(name = "TAX_NO", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getTaxNo() {
        return taxNo;
    }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
    }


    @Column(name = "IS_SUBCOMPANY", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public Integer getIsSubcompany() {
        return isSubcompany;
    }

    public void setIsSubcompany(Integer isSubcompany) {
        this.isSubcompany = isSubcompany;
    }


}