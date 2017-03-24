package com.csnt.scdp.bizmodules.entity.scm;

import com.csnt.scdp.framework.dto.BasePojo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:  ScmContract
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-12-23 11:16:13
 */

@javax.persistence.Entity
@Table(name = "SCM_CONTRACT")
public class ScmContract extends BasePojo {
    private String uuid;
    private String scmContractCode;
    private String contractNature;
    private String supplierCode;
    private String supplierName;
    private BigDecimal amount;
    private BigDecimal quantity;
    private String purchaseTypes;
    private String contractPayType;
    private String otherDes;
    private String debter;
    private String debterDepartment;
    private String bank;
    private String bankId;
    private BigDecimal totalValue;
    private String payType;
    private Integer isUrgent;
    private Date debtDate;
    private String state;
    private Date effectiveDate;
    private Date stampDate;
    private String contractState;
    private Integer isVirtual;
    private BigDecimal paid;
    private BigDecimal invoiceAmount;
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
    private String subjectCode;
    private Integer seqNo;
    private String officeId;
    private String payeeName;
    private String purchasePackageId;
    private Integer isProject;
    private String operatorId;
    private String operatorName;
    private String bugdetId;
    private String isClosed;
    private String electricCommercialStore;
    private String jdName;
    private String jdPassword;
    private String jdOrderNo;
    private Date jdOrderDate;
    private Date jdLastDate;
    private Date eta;

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

    @Column(name = "SCM_CONTRACT_CODE", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getScmContractCode() {
        return scmContractCode;
    }

    public void setScmContractCode(String scmContractCode) {
        this.scmContractCode = scmContractCode;
    }

    @Column(name = "CONTRACT_NATURE", nullable = true, insertable = true, updatable = true, length = 16, precision = 0)
    public String getContractNature() {
        return contractNature;
    }

    public void setContractNature(String contractNature) {
        this.contractNature = contractNature;
    }

    @Column(name = "SUPPLIER_CODE", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    @Column(name = "SUPPLIER_NAME", nullable = true, insertable = true, updatable = true, length = 200, precision = 0)
    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    @Column(name = "AMOUNT", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Column(name = "QUANTITY", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    @Column(name = "PURCHASE_TYPES", nullable = true, insertable = true, updatable = true, length = 2, precision = 0)
    public String getPurchaseTypes() {
        return purchaseTypes;
    }

    public void setPurchaseTypes(String purchaseTypes) {
        this.purchaseTypes = purchaseTypes;
    }

    @Column(name = "CONTRACT_PAY_TYPE", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public String getContractPayType() {
        return contractPayType;
    }

    public void setContractPayType(String contractPayType) {
        this.contractPayType = contractPayType;
    }

    @Column(name = "OTHER_DES", nullable = true, insertable = true, updatable = true, length = 250, precision = 0)
    public String getOtherDes() {
        return otherDes;
    }

    public void setOtherDes(String otherDes) {
        this.otherDes = otherDes;
    }

    @Column(name = "DEBTER", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getDebter() {
        return debter;
    }

    public void setDebter(String debter) {
        this.debter = debter;
    }

    @Column(name = "DEBTER_DEPARTMENT", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getDebterDepartment() {
        return debterDepartment;
    }

    public void setDebterDepartment(String debterDepartment) {
        this.debterDepartment = debterDepartment;
    }

    @Column(name = "BANK", nullable = true, insertable = true, updatable = true, length = 600, precision = 0)
    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    @Column(name = "BANK_ID", nullable = true, insertable = true, updatable = true, length = 600, precision = 0)
    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    @Column(name = "TOTAL_VALUE", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    @Column(name = "PAY_TYPE", nullable = true, insertable = true, updatable = true, length = 16, precision = 0)
    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    @Column(name = "IS_URGENT", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public Integer getIsUrgent() {
        return isUrgent;
    }

    public void setIsUrgent(Integer isUrgent) {
        this.isUrgent = isUrgent;
    }

    @Column(name = "DEBT_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getDebtDate() {
        return debtDate;
    }

    public void setDebtDate(Date debtDate) {
        this.debtDate = debtDate;
    }

    @Column(name = "STATE", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name = "EFFECTIVE_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    @Column(name = "STAMP_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getStampDate() {
        return stampDate;
    }

    public void setStampDate(Date stampDate) {
        this.stampDate = stampDate;
    }

    @Column(name = "CONTRACT_STATE", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public String getContractState() {
        return contractState;
    }

    public void setContractState(String contractState) {
        this.contractState = contractState;
    }

    @Column(name = "IS_VIRTUAL", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public Integer getIsVirtual() {
        return isVirtual;
    }

    public void setIsVirtual(Integer isVirtual) {
        this.isVirtual = isVirtual;
    }

    @Column(name = "PAID", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getPaid() {
        return paid;
    }

    public void setPaid(BigDecimal paid) {
        this.paid = paid;
    }

    @Column(name = "INVOICE_AMOUNT", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    @Column(name = "REMARK", nullable = true, insertable = true, updatable = true, length = 200, precision = 0)
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

    @Column(name = "SUBJECT_CODE", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    @Column(name = "SEQ_NO", nullable = true, insertable = true, updatable = true, length = 9, precision = 0)
    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    @Column(name = "OFFICE_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    @Column(name = "PAYEE_NAME", nullable = true, insertable = true, updatable = true, length = 200, precision = 0)
    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    @Column(name = "PURCHASE_PACKAGE_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getPurchasePackageId() {
        return purchasePackageId;
    }

    public void setPurchasePackageId(String purchasePackageId) {
        this.purchasePackageId = purchasePackageId;
    }

    @Column(name = "IS_PROJECT", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public Integer getIsProject() {
        return isProject;
    }

    public void setIsProject(Integer isProject) {
        this.isProject = isProject;
    }

    @Column(name = "OPERATOR_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    @Column(name = "OPERATOR_NAME", nullable = true, insertable = true, updatable = true, length = 200, precision = 0)
    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    @Column(name = "BUGDET_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getBugdetId() {
        return bugdetId;
    }

    public void setBugdetId(String bugdetId) {
        this.bugdetId = bugdetId;
    }

    @Column(name = "IS_CLOSED", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public String getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(String isClosed) {
        this.isClosed = isClosed;
    }

    @Column(name = "ELECTRIC_COMMERCIAL_STORE", nullable = true, insertable = true, updatable = true, length = 500, precision = 0)
    public String getElectricCommercialStore() {
        return electricCommercialStore;
    }

    public void setElectricCommercialStore(String electricCommercialStore) {
        this.electricCommercialStore = electricCommercialStore;
    }

    @Column(name = "JD_NAME", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getJdName() {
        return jdName;
    }

    public void setJdName(String jdName) {
        this.jdName = jdName;
    }

    @Column(name = "JD_PASSWORD", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getJdPassword() {
        return jdPassword;
    }

    public void setJdPassword(String jdPassword) {
        this.jdPassword = jdPassword;
    }

    @Column(name = "JD_ORDER_NO", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getJdOrderNo() {
        return jdOrderNo;
    }

    public void setJdOrderNo(String jdOrderNo) {
        this.jdOrderNo = jdOrderNo;
    }

    @Column(name = "JD_ORDER_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getJdOrderDate() {
        return jdOrderDate;
    }

    public void setJdOrderDate(Date jdOrderDate) {
        this.jdOrderDate = jdOrderDate;
    }

    @Column(name = "JD_LAST_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getJdLastDate() {
        return jdLastDate;
    }

    public void setJdLastDate(Date jdLastDate) {
        this.jdLastDate = jdLastDate;
    }

    @Column(name = "ETA", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getEta() {
        return eta;
    }

    public void setEta(Date eta) {
        this.eta = eta;
    }
}