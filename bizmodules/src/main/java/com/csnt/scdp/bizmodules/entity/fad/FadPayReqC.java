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
 * Description:  FadPayReqC
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-10-19 20:52:40
 */

@javax.persistence.Entity
@Table(name = "FAD_PAY_REQ_C")
public class FadPayReqC extends BasePojo {
    private String uuid;
    private String puuid;
    private String projectMainId;
    private String supplierId;
    private String scmContractId;
    private BigDecimal accountsPayable;
    private BigDecimal paidMoney;
    private BigDecimal reqMoney;
    private BigDecimal approveMoney;
    private BigDecimal purchaseManagerMoney;
    private BigDecimal purchaseChiefMoney;
    private BigDecimal auditMoney;
    private String payReason;
    private String state;
    private String remark;
    private String payType;
    private Date expectDate;
    private String companyCode;
    private String companyName;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    private Integer isVoid;
    private String locTimezone;
    private BigDecimal tblVersion;
    private Integer seqNo;
    private String runningNo;
    private BigDecimal cashReqValue;
    private BigDecimal scmContractExpectPaid;
    private BigDecimal scmContractThenPaid;

    private Date certificateCreateTime;
    private String departmentCode;

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

    @Column(name = "PUUID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getPuuid() {
        return puuid;
    }

    public void setPuuid(String puuid) {
        this.puuid = puuid;
    }

    @Column(name = "RUNNING_NO", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getRunningNo() {
        return runningNo;
    }

    public void setRunningNo(String runningNo) {
        this.runningNo = runningNo;
    }

    @Column(name = "PROJECT_MAIN_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getProjectMainId() {
        return projectMainId;
    }

    public void setProjectMainId(String projectMainId) {
        this.projectMainId = projectMainId;
    }

    @Column(name = "SUPPLIER_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    @Column(name = "SCM_CONTRACT_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getScmContractId() {
        return scmContractId;
    }

    public void setScmContractId(String scmContractId) {
        this.scmContractId = scmContractId;
    }

    @Column(name = "ACCOUNTS_PAYABLE", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getAccountsPayable() {
        return accountsPayable;
    }

    public void setAccountsPayable(BigDecimal accountsPayable) {
        this.accountsPayable = accountsPayable;
    }

    @Column(name = "PAID_MONEY", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getPaidMoney() {
        return paidMoney;
    }

    public void setPaidMoney(BigDecimal paidMoney) {
        this.paidMoney = paidMoney;
    }

    @Column(name = "REQ_MONEY", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getReqMoney() {
        return reqMoney;
    }

    public void setReqMoney(BigDecimal reqMoney) {
        this.reqMoney = reqMoney;
    }

    @Column(name = "APPROVE_MONEY", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getApproveMoney() {
        return approveMoney;
    }

    public void setApproveMoney(BigDecimal approveMoney) {
        this.approveMoney = approveMoney;
    }

    @Column(name = "PURCHASE_MANAGER_MONEY", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getPurchaseManagerMoney() {
        return purchaseManagerMoney;
    }

    public void setPurchaseManagerMoney(BigDecimal purchaseManagerMoney) {
        this.purchaseManagerMoney = purchaseManagerMoney;
    }

    @Column(name = "PURCHASE_CHIEF_MONEY", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getPurchaseChiefMoney() {
        return purchaseChiefMoney;
    }

    public void setPurchaseChiefMoney(BigDecimal purchaseChiefMoney) {
        this.purchaseChiefMoney = purchaseChiefMoney;
    }

    @Column(name = "AUDIT_MONEY", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getAuditMoney() {
        return auditMoney;
    }

    public void setAuditMoney(BigDecimal auditMoney) {
        this.auditMoney = auditMoney;
    }

    @Column(name = "PAY_REASON", nullable = true, insertable = true, updatable = true, length = 200, precision = 0)
    public String getPayReason() {
        return payReason;
    }

    public void setPayReason(String payReason) {
        this.payReason = payReason;
    }

    @Column(name = "STATE", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
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

    @Column(name = "PAY_TYPE", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    @Column(name = "EXPECT_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getExpectDate() {
        return expectDate;
    }

    public void setExpectDate(Date expectDate) {
        this.expectDate = expectDate;
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

    @Column(name = "CASH_REQ_VALUE", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getCashReqValue() {
        return cashReqValue;
    }


    public void setCashReqValue(BigDecimal cashReqValue) {
        this.cashReqValue = cashReqValue;
    }

    @Column(name = "SCM_CONTRACT_EXPECT_PAID", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getScmContractExpectPaid() {
        return scmContractExpectPaid;
    }

    public void setScmContractExpectPaid(BigDecimal scmContractExpectPaid) {
        this.scmContractExpectPaid = scmContractExpectPaid;
    }

    @Column(name = "SCM_CONTRACT_THEN_PAID", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getScmContractThenPaid() {
        return scmContractThenPaid;
    }

    public void setScmContractThenPaid(BigDecimal scmContractThenPaid) {
        this.scmContractThenPaid = scmContractThenPaid;
    }

    @Column(name = "TBL_VERSION", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public BigDecimal getRecordVersion() {
        return tblVersion;
    }

    public void setRecordVersion(BigDecimal recordVersion) {
        this.tblVersion = recordVersion;
    }

    @Column(name = "CERTIFICATE_CREATE_TIME", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getCertificateCreateTime() {
        return certificateCreateTime;
    }

    public void setCertificateCreateTime(Date certificateCreateTime) {
        this.certificateCreateTime = certificateCreateTime;
    }

    @Column(name = "DEPARTMENT_CODE", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }
}