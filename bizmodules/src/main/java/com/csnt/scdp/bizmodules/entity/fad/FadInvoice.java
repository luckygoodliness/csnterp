package com.csnt.scdp.bizmodules.entity.fad;

import com.csnt.scdp.framework.dto.BasePojo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.math.BigDecimal;

/**
 * Description:  FadInvoice
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-12-29 16:01:22
 */

@javax.persistence.Entity
@Table(name = "FAD_INVOICE")
public class FadInvoice extends BasePojo {
    private String uuid;
    private String prmProjectMainId;
    private String invoiceCode;
    private String expensesType;
    private String subjectId;
    private String subjectName;
    private String invoiceNo;
    private Integer invoiceNum;
    private BigDecimal invoiceTotalValue;
    private BigDecimal expensesMoney;
    private String supplierId;
    private Date buildTime;
    private String supplierName;
    private String taxRegistrationNo;
    private String invoiceReqNo;
    private String invoiceType;
    private String payStyle;
    private BigDecimal taxRate;
    private String renderPerson;
    private String officeId;
    private String tripLocation;
    private Date tripBeginDate;
    private Date tripEndDate;
    private Integer tripDays;
    private String tripReason;
    private String state;
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
    private String subjectCode;
    private Date certificateCreateTime;
    private String fadSubjectCode;
    private String scmContractCode;
    private String bank;
    private String bankId;
    private Integer cashClearance;
    private Integer directPayment;
    private String fadYear;
    private String fadMonth;
    private String officeName;
    private String payee;
    private Integer isWriteOff;

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

    @Column(name = "IS_WRITE_OFF", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public Integer getIsWriteOff() {
        return isWriteOff;
    }

    public void setIsWriteOff(Integer isWriteOff) {
        this.isWriteOff = isWriteOff;
    }

    @Column(name = "PRM_PROJECT_MAIN_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getPrmProjectMainId() {
        return prmProjectMainId;
    }

    public void setPrmProjectMainId(String prmProjectMainId) {
        this.prmProjectMainId = prmProjectMainId;
    }

    @Column(name = "INVOICE_CODE", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    @Column(name = "EXPENSES_TYPE", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public String getExpensesType() {
        return expensesType;
    }

    public void setExpensesType(String expensesType) {
        this.expensesType = expensesType;
    }

    @Column(name = "SUBJECT_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    @Column(name = "SUBJECT_NAME", nullable = true, insertable = true, updatable = true, length = 500, precision = 0)
    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    @Column(name = "INVOICE_NO", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    @Column(name = "INVOICE_NUM", nullable = true, insertable = true, updatable = true, length = 4, precision = 0)
    public Integer getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(Integer invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    @Column(name = "INVOICE_TOTAL_VALUE", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getInvoiceTotalValue() {
        return invoiceTotalValue;
    }

    public void setInvoiceTotalValue(BigDecimal invoiceTotalValue) {
        this.invoiceTotalValue = invoiceTotalValue;
    }

    @Column(name = "EXPENSES_MONEY", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getExpensesMoney() {
        return expensesMoney;
    }

    public void setExpensesMoney(BigDecimal expensesMoney) {
        this.expensesMoney = expensesMoney;
    }

    @Column(name = "SUPPLIER_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    @Column(name = "BUILD_TIME", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(Date buildTime) {
        this.buildTime = buildTime;
    }

    @Column(name = "SUPPLIER_NAME", nullable = true, insertable = true, updatable = true, length = 250, precision = 0)
    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    @Column(name = "TAX_REGISTRATION_NO", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getTaxRegistrationNo() {
        return taxRegistrationNo;
    }

    public void setTaxRegistrationNo(String taxRegistrationNo) {
        this.taxRegistrationNo = taxRegistrationNo;
    }

    @Column(name = "INVOICE_REQ_NO", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getInvoiceReqNo() {
        return invoiceReqNo;
    }

    public void setInvoiceReqNo(String invoiceReqNo) {
        this.invoiceReqNo = invoiceReqNo;
    }

    @Column(name = "INVOICE_TYPE", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    @Column(name = "PAY_STYLE", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getPayStyle() {
        return payStyle;
    }

    public void setPayStyle(String payStyle) {
        this.payStyle = payStyle;
    }

    @Column(name = "TAX_RATE", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    @Column(name = "RENDER_PERSON", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getRenderPerson() {
        return renderPerson;
    }

    public void setRenderPerson(String renderPerson) {
        this.renderPerson = renderPerson;
    }

    @Column(name = "OFFICE_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    @Column(name = "TRIP_LOCATION", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getTripLocation() {
        return tripLocation;
    }

    public void setTripLocation(String tripLocation) {
        this.tripLocation = tripLocation;
    }

    @Column(name = "TRIP_BEGIN_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getTripBeginDate() {
        return tripBeginDate;
    }

    public void setTripBeginDate(Date tripBeginDate) {
        this.tripBeginDate = tripBeginDate;
    }

    @Column(name = "TRIP_END_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getTripEndDate() {
        return tripEndDate;
    }

    public void setTripEndDate(Date tripEndDate) {
        this.tripEndDate = tripEndDate;
    }

    @Column(name = "TRIP_DAYS", nullable = true, insertable = true, updatable = true, length = 5, precision = 0)
    public Integer getTripDays() {
        return tripDays;
    }

    public void setTripDays(Integer tripDays) {
        this.tripDays = tripDays;
    }

    @Column(name = "TRIP_REASON", nullable = true, insertable = true, updatable = true, length = 200, precision = 0)
    public String getTripReason() {
        return tripReason;
    }

    public void setTripReason(String tripReason) {
        this.tripReason = tripReason;
    }

    @Column(name = "STATE", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    @Column(name = "SUBJECT_CODE", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    @Column(name = "CERTIFICATE_CREATE_TIME", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getCertificateCreateTime() {
        return certificateCreateTime;
    }

    public void setCertificateCreateTime(Date certificateCreateTime) {
        this.certificateCreateTime = certificateCreateTime;
    }

    @Column(name = "FAD_SUBJECT_CODE", nullable = true, insertable = true, updatable = true, length = 500, precision = 0)
    public String getFadSubjectCode() {
        return fadSubjectCode;
    }

    public void setFadSubjectCode(String fadSubjectCode) {
        this.fadSubjectCode = fadSubjectCode;
    }

    @Column(name = "SCM_CONTRACT_CODE", nullable = true, insertable = true, updatable = true, length = 500, precision = 0)
    public String getScmContractCode() {
        return scmContractCode;
    }

    public void setScmContractCode(String scmContractCode) {
        this.scmContractCode = scmContractCode;
    }

    @Column(name = "BANK", nullable = true, insertable = true, updatable = true, length = 200, precision = 0)
    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    @Column(name = "BANK_ID", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    @Column(name = "CASH_CLEARANCE", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public Integer getCashClearance() {
        return cashClearance;
    }

    public void setCashClearance(Integer cashClearance) {
        this.cashClearance = cashClearance;
    }

    @Column(name = "DIRECT_PAYMENT", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public Integer getDirectPayment() {
        return directPayment;
    }

    public void setDirectPayment(Integer directPayment) {
        this.directPayment = directPayment;
    }

    @Column(name = "FAD_YEAR", nullable = true, insertable = true, updatable = true, length = 4, precision = 0)
    public String getFadYear() {
        return fadYear;
    }

    public void setFadYear(String fadYear) {
        this.fadYear = fadYear;
    }

    @Column(name = "FAD_MONTH", nullable = true, insertable = true, updatable = true, length = 2, precision = 0)
    public String getFadMonth() {
        return fadMonth;
    }

    public void setFadMonth(String fadMonth) {
        this.fadMonth = fadMonth;
    }

    @Column(name = "OFFICE_NAME", nullable = true, insertable = true, updatable = true, length = 500, precision = 0)
    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    @Column(name = "PAYEE", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

}