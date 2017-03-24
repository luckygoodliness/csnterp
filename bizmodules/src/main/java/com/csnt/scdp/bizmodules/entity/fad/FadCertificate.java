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
 * Description:  FadCertificate
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-10-16 10:07:48
 */

@javax.persistence.Entity
@Table(name = "FAD_CERTIFICATE")
public class FadCertificate extends BasePojo {
    private String uuid;
    private String businessId;
    private String certificateNo;
    private String ncSwiftNumber;
    private Date certificateDate;
    private Date makeDate;
    private String certificateType;
    private String abstracts;
    private BigDecimal debtor;
    private BigDecimal creditor;
    private String maker;
    private String cashier;
    private String auditor;
    private String bookkeeper;
    private String years;
    private String months;
    private Integer attachmentNumber;
    private String receiverOrPayerType;
    private String receiverOrPayerCode;
    private String receiverOrPayerId;
    private String receiverOrPayerName;
    private String originalFormType;
    private String originalFormCode;
    private Date originalFormDate;
    private String state;
    private String feedback;
    private String ncrequestUrl;
    private String ncrequestXml;
    private String ncresponseXml;
    private String remark;
    private String free1;
    private String free2;
    private String free3;
    private Integer free4;
    private String free5;
    private String free6;
    private String free7;
    private String free8;
    private String free9;
    private String free10;
    private String free11;
    private String free12;
    private String free13;
    private String free14;
    private String free15;
    private String free16;
    private String free17;
    private String free18;
    private String free19;
    private String free20;
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

    @Column(name = "BUSINESS_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    @Column(name = "CERTIFICATE_NO", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    @Column(name = "NC_SWIFT_NUMBER", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getNcSwiftNumber() {
        return ncSwiftNumber;
    }

    public void setNcSwiftNumber(String ncSwiftNumber) {
        this.ncSwiftNumber = ncSwiftNumber;
    }

    @Column(name = "CERTIFICATE_DATE", nullable = true, insertable = false, updatable = false, length = 0, precision = 0)
    public Date getCertificateDate() {
        return certificateDate;
    }

    public void setCertificateDate(Date certificateDate) {
        this.certificateDate = certificateDate;
    }

    @Column(name = "MAKE_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getMakeDate() {
        return makeDate;
    }

    public void setMakeDate(Date makeDate) {
        this.makeDate = makeDate;
    }

    @Column(name = "CERTIFICATE_TYPE", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    @Column(name = "ABSTRACTS", nullable = true, insertable = true, updatable = true, length = -1, precision = 0)
    public String getAbstracts() {
        return abstracts;
    }

    public void setAbstracts(String abstracts) {
        this.abstracts = abstracts;
    }

    @Column(name = "DEBTOR", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getDebtor() {
        return debtor;
    }

    public void setDebtor(BigDecimal debtor) {
        this.debtor = debtor;
    }

    @Column(name = "CREDITOR", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getCreditor() {
        return creditor;
    }

    public void setCreditor(BigDecimal creditor) {
        this.creditor = creditor;
    }

    @Column(name = "MAKER", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    @Column(name = "CASHIER", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getCashier() {
        return cashier;
    }

    public void setCashier(String cashier) {
        this.cashier = cashier;
    }

    @Column(name = "AUDITOR", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    @Column(name = "BOOKKEEPER", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getBookkeeper() {
        return bookkeeper;
    }

    public void setBookkeeper(String bookkeeper) {
        this.bookkeeper = bookkeeper;
    }

    @Column(name = "YEARS", nullable = true, insertable = true, updatable = true, length = 4, precision = 0)
    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years;
    }

    @Column(name = "MONTHS", nullable = true, insertable = true, updatable = true, length = 2, precision = 0)
    public String getMonths() {
        return months;
    }

    public void setMonths(String months) {
        this.months = months;
    }

    @Column(name = "ATTACHMENT_NUMBER", nullable = true, insertable = true, updatable = true, length = 4, precision = 0)
    public Integer getAttachmentNumber() {
        return attachmentNumber;
    }

    public void setAttachmentNumber(Integer attachmentNumber) {
        this.attachmentNumber = attachmentNumber;
    }

    @Column(name = "RECEIVER_OR_PAYER_TYPE", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getReceiverOrPayerType() {
        return receiverOrPayerType;
    }

    public void setReceiverOrPayerType(String receiverOrPayerType) {
        this.receiverOrPayerType = receiverOrPayerType;
    }

    @Column(name = "RECEIVER_OR_PAYER_CODE", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getReceiverOrPayerCode() {
        return receiverOrPayerCode;
    }

    public void setReceiverOrPayerCode(String receiverOrPayerCode) {
        this.receiverOrPayerCode = receiverOrPayerCode;
    }

    @Column(name = "RECEIVER_OR_PAYER_ID", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getReceiverOrPayerId() {
        return receiverOrPayerId;
    }

    public void setReceiverOrPayerId(String receiverOrPayerId) {
        this.receiverOrPayerId = receiverOrPayerId;
    }

    @Column(name = "RECEIVER_OR_PAYER_NAME", nullable = true, insertable = true, updatable = true, length = -1, precision = 0)
    public String getReceiverOrPayerName() {
        return receiverOrPayerName;
    }

    public void setReceiverOrPayerName(String receiverOrPayerName) {
        this.receiverOrPayerName = receiverOrPayerName;
    }

    @Column(name = "ORIGINAL_FORM_TYPE", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getOriginalFormType() {
        return originalFormType;
    }

    public void setOriginalFormType(String originalFormType) {
        this.originalFormType = originalFormType;
    }

    @Column(name = "ORIGINAL_FORM_CODE", nullable = true, insertable = true, updatable = true, length = -1, precision = 0)
    public String getOriginalFormCode() {
        return originalFormCode;
    }

    public void setOriginalFormCode(String originalFormCode) {
        this.originalFormCode = originalFormCode;
    }

    @Column(name = "ORIGINAL_FORM_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getOriginalFormDate() {
        return originalFormDate;
    }

    public void setOriginalFormDate(Date originalFormDate) {
        this.originalFormDate = originalFormDate;
    }

    @Column(name = "STATE", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name = "FEEDBACK", nullable = true, insertable = true, updatable = true, length = 4000, precision = 0)
    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    @Column(name = "NCREQUEST_URL", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getNcrequestUrl() {
        return ncrequestUrl;
    }

    public void setNcrequestUrl(String ncrequestUrl) {
        this.ncrequestUrl = ncrequestUrl;
    }

    @Column(name = "NCREQUEST_XML", nullable = true, insertable = true, updatable = true, length = -1, precision = 0)
    public String getNcrequestXml() {
        return ncrequestXml;
    }

    public void setNcrequestXml(String ncrequestXml) {
        this.ncrequestXml = ncrequestXml;
    }

    @Column(name = "NCRESPONSE_XML", nullable = true, insertable = true, updatable = true, length = -1, precision = 0)
    public String getNcresponseXml() {
        return ncresponseXml;
    }

    public void setNcresponseXml(String ncresponseXml) {
        this.ncresponseXml = ncresponseXml;
    }

    @Column(name = "REMARK", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "FREE1", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree1() {
        return free1;
    }

    public void setFree1(String free1) {
        this.free1 = free1;
    }

    @Column(name = "FREE2", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree2() {
        return free2;
    }

    public void setFree2(String free2) {
        this.free2 = free2;
    }

    @Column(name = "FREE3", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree3() {
        return free3;
    }

    public void setFree3(String free3) {
        this.free3 = free3;
    }

    @Column(name = "FREE4", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public Integer getFree4() {
        return free4;
    }

    public void setFree4(Integer free4) {
        this.free4 = free4;
    }

    @Column(name = "FREE5", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree5() {
        return free5;
    }

    public void setFree5(String free5) {
        this.free5 = free5;
    }

    @Column(name = "FREE6", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree6() {
        return free6;
    }

    public void setFree6(String free6) {
        this.free6 = free6;
    }

    @Column(name = "FREE7", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree7() {
        return free7;
    }

    public void setFree7(String free7) {
        this.free7 = free7;
    }

    @Column(name = "FREE8", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree8() {
        return free8;
    }

    public void setFree8(String free8) {
        this.free8 = free8;
    }

    @Column(name = "FREE9", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree9() {
        return free9;
    }

    public void setFree9(String free9) {
        this.free9 = free9;
    }

    @Column(name = "FREE10", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree10() {
        return free10;
    }

    public void setFree10(String free10) {
        this.free10 = free10;
    }

    @Column(name = "FREE11", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree11() {
        return free11;
    }

    public void setFree11(String free11) {
        this.free11 = free11;
    }

    @Column(name = "FREE12", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree12() {
        return free12;
    }

    public void setFree12(String free12) {
        this.free12 = free12;
    }

    @Column(name = "FREE13", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree13() {
        return free13;
    }

    public void setFree13(String free13) {
        this.free13 = free13;
    }

    @Column(name = "FREE14", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree14() {
        return free14;
    }

    public void setFree14(String free14) {
        this.free14 = free14;
    }

    @Column(name = "FREE15", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree15() {
        return free15;
    }

    public void setFree15(String free15) {
        this.free15 = free15;
    }

    @Column(name = "FREE16", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree16() {
        return free16;
    }

    public void setFree16(String free16) {
        this.free16 = free16;
    }

    @Column(name = "FREE17", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree17() {
        return free17;
    }

    public void setFree17(String free17) {
        this.free17 = free17;
    }

    @Column(name = "FREE18", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree18() {
        return free18;
    }

    public void setFree18(String free18) {
        this.free18 = free18;
    }

    @Column(name = "FREE19", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree19() {
        return free19;
    }

    public void setFree19(String free19) {
        this.free19 = free19;
    }

    @Column(name = "FREE20", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree20() {
        return free20;
    }

    public void setFree20(String free20) {
        this.free20 = free20;
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

    @Column(name = "SEQ_NO", nullable = true, insertable = false, updatable = false, length = 9, precision = 0)
    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }
}