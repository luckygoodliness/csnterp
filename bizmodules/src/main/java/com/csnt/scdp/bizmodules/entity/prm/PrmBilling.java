package com.csnt.scdp.bizmodules.entity.prm;

import com.csnt.scdp.framework.dto.BasePojo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.math.BigDecimal;

/**
 * Description:  PrmBilling
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-11-13 10:20:35
 */

@javax.persistence.Entity
@Table(name = "PRM_BILLING")
public class PrmBilling extends BasePojo {
    private String uuid;
    private String prmProjectMainId;
    private String customerId;
    private String customerName;
    private String customerInvoiceId;
    private String customerInvoiceName;
    private String customerInvoiceNameEn;
    private String customerLocation;
    private BigDecimal exchangeRate;
    private String invoiceCurrency;
    private String originalCurrency;
    private BigDecimal originalMoney;
    private BigDecimal netMoney;
    private BigDecimal taxMoney;
    private String phone;
    private String taxNo;
    private String bankName;
    private String bankAccount;
    private BigDecimal invoiceMoney;
    private String invoiceNo;
    private Date invoiceDate;
    private String reqPerson;
    private String reqOffice;
    private String maker;
    private String invoiceType;
    private BigDecimal taxRate;
    private String state;
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
    private String locationType;
    private String paymentType;
    private String remark;
    private Date expectReceiveDate;
    private String prmContractId;
    private Integer billType;
    private String repealUuid;
    private String reason;

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

    @Column(name = "PRM_PROJECT_MAIN_ID", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getPrmProjectMainId() {
        return prmProjectMainId;
    }

    public void setPrmProjectMainId(String prmProjectMainId) {
        this.prmProjectMainId = prmProjectMainId;
    }

    @Column(name = "CUSTOMER_ID", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @Column(name = "CUSTOMER_NAME", nullable = true, insertable = true, updatable = true, length = 500, precision = 0)
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Column(name = "CUSTOMER_INVOICE_NAME", nullable = true, insertable = true, updatable = true, length = 500, precision = 0)
    public String getCustomerInvoiceName() {
        return customerInvoiceName;
    }

    public void setCustomerInvoiceName(String customerInvoiceName) {
        this.customerInvoiceName = customerInvoiceName;
    }

    @Column(name = "CUSTOMER_INVOICE_ID", nullable = true, insertable = true, updatable = true, length = 500, precision = 0)
    public String getCustomerInvoiceId() {
        return customerInvoiceId;
    }

    public void setCustomerInvoiceId(String customerInvoiceId) {
        this.customerInvoiceId = customerInvoiceId;
    }

    @Column(name = "CUSTOMER_INVOICE_NAME_EN", nullable = true, insertable = true, updatable = true, length = 500, precision = 0)
    public String getCustomerInvoiceNameEn() {
        return customerInvoiceNameEn;
    }

    public void setCustomerInvoiceNameEn(String customerInvoiceNameEn) {
        this.customerInvoiceNameEn = customerInvoiceNameEn;
    }

    @Column(name = "CUSTOMER_LOCATION", nullable = true, insertable = true, updatable = true, length = 500, precision = 0)
    public String getCustomerLocation() {
        return customerLocation;
    }

    public void setCustomerLocation(String customerLocation) {
        this.customerLocation = customerLocation;
    }

    @Column(name = "EXCHANGE_RATE", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    @Column(name = "INVOICE_CURRENCY", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getInvoiceCurrency() {
        return invoiceCurrency;
    }

    public void setInvoiceCurrency(String invoiceCurrency) {
        this.invoiceCurrency = invoiceCurrency;
    }

    @Column(name = "ORIGINAL_CURRENCY", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getOriginalCurrency() {
        return originalCurrency;
    }

    public void setOriginalCurrency(String originalCurrency) {
        this.originalCurrency = originalCurrency;
    }

    @Column(name = "ORIGINAL_MONEY", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getOriginalMoney() {
        return originalMoney;
    }

    public void setOriginalMoney(BigDecimal originalMoney) {
        this.originalMoney = originalMoney;
    }

    @Column(name = "PHONE", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name = "TAX_NO", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getTaxNo() {
        return taxNo;
    }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
    }

    @Column(name = "BANK_NAME", nullable = true, insertable = true, updatable = true, length = 500, precision = 0)
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Column(name = "BANK_ACCOUNT", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    @Column(name = "INVOICE_MONEY", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getInvoiceMoney() {
        return invoiceMoney;
    }

    public void setInvoiceMoney(BigDecimal invoiceMoney) {
        this.invoiceMoney = invoiceMoney;
    }

    @Column(name = "INVOICE_NO", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    @Column(name = "INVOICE_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    @Column(name = "REQ_PERSON", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getReqPerson() {
        return reqPerson;
    }

    public void setReqPerson(String reqPerson) {
        this.reqPerson = reqPerson;
    }

    @Column(name = "REQ_OFFICE", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getReqOffice() {
        return reqOffice;
    }

    public void setReqOffice(String reqOffice) {
        this.reqOffice = reqOffice;
    }

    @Column(name = "MAKER", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    @Column(name = "INVOICE_TYPE", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    @Column(name = "TAX_RATE", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    @Column(name = "STATE", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    @Column(name = "REMARK", nullable = true, insertable = true, updatable = true, length = 512, precision = 0)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    @Column(name = "NET_MONEY", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getNetMoney() {
        return netMoney;
    }

    public void setNetMoney(BigDecimal netMoney) {
        this.netMoney = netMoney;
    }

    @Column(name = "TAX_MONEY", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getTaxMoney() {
        return taxMoney;
    }

    public void setTaxMoney(BigDecimal taxMoney) {
        this.taxMoney = taxMoney;
    }

    @Column(name = "LOCATION_TYPE", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    @Column(name = "PAYMENT_TYPE", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    @Column(name = "EXPECT_RECEIVE_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getExpectReceiveDate() {
        return expectReceiveDate;
    }

    public void setExpectReceiveDate(Date expectReceiveDate) {
        this.expectReceiveDate = expectReceiveDate;
    }

    @Column(name = "PRM_CONTRACT_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getPrmContractId() {
        return prmContractId;
    }

    public void setPrmContractId(String prmContractId) {
        this.prmContractId = prmContractId;
    }

    @Column(name = "BILL_TYPE", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    @Column(name = "REPEAL_UUID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getRepealUuid() {
        return repealUuid;
    }

    public void setRepealUuid(String repealUuid) {
        this.repealUuid = repealUuid;
    }

    @Column(name = "REASON", nullable = true, insertable = true, updatable = true, length = 512, precision = 0)
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}