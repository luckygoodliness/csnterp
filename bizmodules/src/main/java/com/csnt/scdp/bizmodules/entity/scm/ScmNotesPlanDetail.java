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
 * Description:  ScmNotesPlanDetail
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-11-14 22:12:06
 */

@javax.persistence.Entity
@Table(name = "SCM_NOTES_PLAN_DETAIL")
public class ScmNotesPlanDetail extends BasePojo {
    private String uuid;
    private String scmNotesPlanId;
    private String scmContractCode;
    private String supplierId;
    private String supplierName;
    private String prmProjectMainId;
    private BigDecimal conclusionLineIn;
    private BigDecimal conclusionLineOut;
    private BigDecimal conclusionLineContract;
    private BigDecimal conclusionLineFinancial;
    private BigDecimal conclusionRateIn;
    private BigDecimal conclusionRateOut;
    private BigDecimal conclusionRateContract;
    private BigDecimal conclusionRateReceipt;
    private BigDecimal scmContractAmount;
    private BigDecimal scmContractInvoice;
    private BigDecimal paid;
    private String remark;
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
    private Date markTime;
    private String createByContract;

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

    @Column(name = "SCM_NOTES_PLAN_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getScmNotesPlanId() {
        return scmNotesPlanId;
    }

    public void setScmNotesPlanId(String scmNotesPlanId) {
        this.scmNotesPlanId = scmNotesPlanId;
    }

    @Column(name = "SCM_CONTRACT_CODE", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getScmContractCode() {
        return scmContractCode;
    }

    public void setScmContractCode(String scmContractCode) {
        this.scmContractCode = scmContractCode;
    }

    @Column(name = "SUPPLIER_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    @Column(name = "SUPPLIER_NAME", nullable = true, insertable = true, updatable = true, length = 200, precision = 0)
    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    @Column(name = "PRM_PROJECT_MAIN_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getPrmProjectMainId() {
        return prmProjectMainId;
    }

    public void setPrmProjectMainId(String prmProjectMainId) {
        this.prmProjectMainId = prmProjectMainId;
    }

    @Column(name = "CONCLUSION_LINE_IN", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getConclusionLineIn() {
        return conclusionLineIn;
    }

    public void setConclusionLineIn(BigDecimal conclusionLineIn) {
        this.conclusionLineIn = conclusionLineIn;
    }

    @Column(name = "CONCLUSION_LINE_OUT", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getConclusionLineOut() {
        return conclusionLineOut;
    }

    public void setConclusionLineOut(BigDecimal conclusionLineOut) {
        this.conclusionLineOut = conclusionLineOut;
    }

    @Column(name = "CONCLUSION_LINE_CONTRACT", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getConclusionLineContract() {
        return conclusionLineContract;
    }

    public void setConclusionLineContract(BigDecimal conclusionLineContract) {
        this.conclusionLineContract = conclusionLineContract;
    }

    @Column(name = "CONCLUSION_LINE_FINANCIAL", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getConclusionLineFinancial() {
        return conclusionLineFinancial;
    }

    public void setConclusionLineFinancial(BigDecimal conclusionLineFinancial) {
        this.conclusionLineFinancial = conclusionLineFinancial;
    }

    @Column(name = "CONCLUSION_RATE_IN", nullable = true, insertable = true, updatable = true, length = 6, precision = 3)
    public BigDecimal getConclusionRateIn() {
        return conclusionRateIn;
    }

    public void setConclusionRateIn(BigDecimal conclusionRateIn) {
        this.conclusionRateIn = conclusionRateIn;
    }

    @Column(name = "CONCLUSION_RATE_OUT", nullable = true, insertable = true, updatable = true, length = 6, precision = 3)
    public BigDecimal getConclusionRateOut() {
        return conclusionRateOut;
    }

    public void setConclusionRateOut(BigDecimal conclusionRateOut) {
        this.conclusionRateOut = conclusionRateOut;
    }

    @Column(name = "CONCLUSION_RATE_CONTRACT", nullable = true, insertable = true, updatable = true, length = 6, precision = 3)
    public BigDecimal getConclusionRateContract() {
        return conclusionRateContract;
    }

    public void setConclusionRateContract(BigDecimal conclusionRateContract) {
        this.conclusionRateContract = conclusionRateContract;
    }

    @Column(name = "CONCLUSION_RATE_RECEIPT", nullable = true, insertable = true, updatable = true, length = 6, precision = 3)
    public BigDecimal getConclusionRateReceipt() {
        return conclusionRateReceipt;
    }

    public void setConclusionRateReceipt(BigDecimal conclusionRateReceipt) {
        this.conclusionRateReceipt = conclusionRateReceipt;
    }

    @Column(name = "SCM_CONTRACT_AMOUNT", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getScmContractAmount() {
        return scmContractAmount;
    }

    public void setScmContractAmount(BigDecimal scmContractAmount) {
        this.scmContractAmount = scmContractAmount;
    }

    @Column(name = "SCM_CONTRACT_INVOICE", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getScmContractInvoice() {
        return scmContractInvoice;
    }

    public void setScmContractInvoice(BigDecimal scmContractInvoice) {
        this.scmContractInvoice = scmContractInvoice;
    }

    @Column(name = "PAID", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getPaid() {
        return paid;
    }

    public void setPaid(BigDecimal paid) {
        this.paid = paid;
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

    @Column(name = "TBL_VERSION", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public BigDecimal getTblVersion() {
        return tblVersion;
    }

    public void setTblVersion(BigDecimal tblVersion) {
        this.tblVersion = tblVersion;
    }

    @Column(name = "SEQ_NO", nullable = true, insertable = true, updatable = true, length = 9, precision = 0)
    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    @Column(name = "MARK_TIME", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getMarkTime() {
        return markTime;
    }

    public void setMarkTime(Date markTime) {
        this.markTime = markTime;
    }

    @Column(name = "CREATE_BY_CONTRACT", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getCreateByContract() {
        return createByContract;
    }

    public void setCreateByContract(String createByContract) {
        this.createByContract = createByContract;
    }

}