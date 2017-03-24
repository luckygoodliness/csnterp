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
 * Description:  FadCashReq
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 16:34:21
 */

@javax.persistence.Entity
@Table(name = "FAD_CASH_REQ")
public class FadCashReq extends BasePojo {
    private String uuid;
    private String projectId;
    private String projectName;
    private String subjectCode;
    private String subjectName;
    private String officeId;
    private String packageUuid;
    private String contractPayType;
    private String budgetCUuid;
    private String reqType;
    private String depositCharacteristic;
    private String depositType;
    private String staffId;
    private String supplierId;
    private String supplierName;
    private String purchaseContractId;
    private String explanation;
    private String model;
    private String other;
    private String tripLocation;
    private Integer tripDays;
    private Integer tripMemberNum;
    private String vehicle;
    private String tripReason;
    private String purpose;
    private BigDecimal money;
    private BigDecimal writeoffMoney;
    private BigDecimal interestRate;
    private Integer isUrgent;
    private String payStyle;
    private String payeeName;
    private String financeNo;
    private String payeeBankName;
    private String payeeLocation;
    private String payeeAccount;
    private String payeePostcode;
    private Date squareDate;
    private String budgetType;
    private String state;
    private String remark;
    private BigDecimal cashBackMoney;
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
    private String jdName;
    private Integer isIndividual;
    private Integer isAdvancePayment;
    private String electricCommercialStore;
    private String jdPassword;
    private String jdOrderNo;
    private Date jdOrderDate;
    private Date jdLastDate;
    private String runningNo;
    private Integer seqNo;
    private String operatorId;
    private String operatorName;
    private Date operateTime;
    private Date certificateCreateTime;
    private String fadSubjectCode;
    private Integer isProject;
    private Date applyDate;
    private Date preclearDate;
    private Date meetingStartTime;
    private Date meetingEndTime;
    private Integer meetingDays;
    private String meetingLocation;
    private Integer meetingInPersonNum;
    private Integer meetingOutPersonNum;
    private String meetingSubject;
    private String fadPayReqCUuid;
    private String operateBusinessBidInfoId;

    @Column(name = "PACKAGE_UUID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getPackageUuid() {
        return packageUuid;
    }

    public void setPackageUuid(String packageUuid) {
        this.packageUuid = packageUuid;
    }

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

    @Column(name = "FAD_PAY_REQ_C_UUID", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getFadPayReqCUuid() {
        return fadPayReqCUuid;
    }

    public void setFadPayReqCUuid(String fadPayReqCUuid) {
        this.fadPayReqCUuid = fadPayReqCUuid;
    }

    @Column(name = "ELECTRIC_COMMERCIAL_STORE", nullable = true, insertable = true, updatable = true, length = 500, precision = 0)
    public String getElectricCommercialStore() {
        return electricCommercialStore;
    }

    public void setElectricCommercialStore(String electricCommercialStore) {
        this.electricCommercialStore = electricCommercialStore;
    }

    @Column(name = "PROJECT_ID", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    @Column(name = "BUDGET_C_UUID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getBudgetCUuid() {
        return budgetCUuid;
    }

    public void setBudgetCUuid(String budgetCUuid) {
        this.budgetCUuid = budgetCUuid;
    }

    @Column(name = "CONTRACT_PAY_TYPE", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public String getContractPayType() {
        return contractPayType;
    }

    public void setContractPayType(String contractPayType) {
        this.contractPayType = contractPayType;
    }

    @Column(name = "RUNNING_NO", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getRunningNo() {
        return runningNo;
    }

    public void setRunningNo(String runningNo) {
        this.runningNo = runningNo;
    }

    @Column(name = "SUBJECT_CODE", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    @Column(name = "FAD_SUBJECT_CODE", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getFadSubjectCode() {
        return fadSubjectCode;
    }

    public void setFadSubjectCode(String fadSubjectCode) {
        this.fadSubjectCode = fadSubjectCode;
    }

    @Column(name = "PROJECT_NAME", nullable = true, insertable = true, updatable = true, length = 200, precision = 0)
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Column(name = "SUBJECT_NAME", nullable = true, insertable = true, updatable = true, length = 200, precision = 0)
    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    @Column(name = "OFFICE_ID", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    @Column(name = "REQ_TYPE", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public String getReqType() {
        return reqType;
    }

    public void setReqType(String reqType) {
        this.reqType = reqType;
    }

    @Column(name = "DEPOSIT_CHARACTERISTIC", nullable = true, insertable = true, updatable = true, length = 250, precision = 0)
    public String getDepositCharacteristic() {
        return depositCharacteristic;
    }

    public void setDepositCharacteristic(String depositCharacteristic) {
        this.depositCharacteristic = depositCharacteristic;
    }

    @Column(name = "DEPOSIT_TYPE", nullable = true, insertable = true, updatable = true, length = 250, precision = 0)
    public String getDepositType() {
        return depositType;
    }

    public void setDepositType(String depositType) {
        this.depositType = depositType;
    }

    @Column(name = "STAFF_ID", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    @Column(name = "SUPPLIER_ID", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    @Column(name = "SUPPLIER_NAME", nullable = true, insertable = true, updatable = true, length = 250, precision = 0)
    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    @Column(name = "PURCHASE_CONTRACT_ID", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getPurchaseContractId() {
        return purchaseContractId;
    }

    public void setPurchaseContractId(String purchaseContractId) {
        this.purchaseContractId = purchaseContractId;
    }

    @Column(name = "EXPLANATION", nullable = true, insertable = true, updatable = true, length = 200, precision = 0)
    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    @Column(name = "MODEL", nullable = true, insertable = true, updatable = true, length = 500, precision = 0)
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Column(name = "OTHER", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    @Column(name = "TRIP_LOCATION", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getTripLocation() {
        return tripLocation;
    }

    public void setTripLocation(String tripLocation) {
        this.tripLocation = tripLocation;
    }

    @Column(name = "TRIP_DAYS", nullable = true, insertable = true, updatable = true, length = 5, precision = 0)
    public Integer getTripDays() {
        return tripDays;
    }

    public void setTripDays(Integer tripDays) {
        this.tripDays = tripDays;
    }

    @Column(name = "TRIP_MEMBER_NUM", nullable = true, insertable = true, updatable = true, length = 5, precision = 0)
    public Integer getTripMemberNum() {
        return tripMemberNum;
    }

    public void setTripMemberNum(Integer tripMemberNum) {
        this.tripMemberNum = tripMemberNum;
    }

    @Column(name = "VEHICLE", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    @Column(name = "TRIP_REASON", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
    public String getTripReason() {
        return tripReason;
    }

    public void setTripReason(String tripReason) {
        this.tripReason = tripReason;
    }

    @Column(name = "PURPOSE", nullable = true, insertable = true, updatable = true, length = 200, precision = 0)
    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    @Column(name = "MONEY", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    @Column(name = "WRITEOFF_MONEY", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getWriteoffMoney() {
        return writeoffMoney;
    }

    public void setWriteoffMoney(BigDecimal writeoffMoney) {
        this.writeoffMoney = writeoffMoney;
    }

    @Column(name = "INTEREST_RATE", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    @Column(name = "PAY_STYLE", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getPayStyle() {
        return payStyle;
    }

    public void setPayStyle(String payStyle) {
        this.payStyle = payStyle;
    }

    @Column(name = "PAYEE_NAME", nullable = true, insertable = true, updatable = true, length = 200, precision = 0)
    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    @Column(name = "FINANCE_NO", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getFinanceNo() {
        return financeNo;
    }

    public void setFinanceNo(String financeNo) {
        this.financeNo = financeNo;
    }

    @Column(name = "PAYEE_BANK_NAME", nullable = true, insertable = true, updatable = true, length = 500, precision = 0)
    public String getPayeeBankName() {
        return payeeBankName;
    }

    public void setPayeeBankName(String payeeBankName) {
        this.payeeBankName = payeeBankName;
    }

    @Column(name = "PAYEE_LOCATION", nullable = true, insertable = true, updatable = true, length = 500, precision = 0)
    public String getPayeeLocation() {
        return payeeLocation;
    }

    public void setPayeeLocation(String payeeLocation) {
        this.payeeLocation = payeeLocation;
    }

    @Column(name = "PAYEE_ACCOUNT", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getPayeeAccount() {
        return payeeAccount;
    }

    public void setPayeeAccount(String payeeAccount) {
        this.payeeAccount = payeeAccount;
    }

    @Column(name = "PAYEE_POSTCODE", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
    public String getPayeePostcode() {
        return payeePostcode;
    }

    public void setPayeePostcode(String payeePostcode) {
        this.payeePostcode = payeePostcode;
    }

    @Column(name = "SQUARE_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getSquareDate() {
        return squareDate;
    }

    public void setSquareDate(Date squareDate) {
        this.squareDate = squareDate;
    }

    @Column(name = "BUDGET_TYPE", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getBudgetType() {
        return budgetType;
    }

    public void setBudgetType(String budgetType) {
        this.budgetType = budgetType;
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

    @Column(name = "CASH_BACK_MONEY", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getCashBackMoney() {
        return cashBackMoney;
    }

    public void setCashBackMoney(BigDecimal cashBackMoney) {
        this.cashBackMoney = cashBackMoney;
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

    @Column(name = "IS_URGENT", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public Integer getIsUrgent() {
        return isUrgent;
    }

    public void setIsUrgent(Integer isUrgent) {
        this.isUrgent = isUrgent;
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

    @Column(name = "IS_INDIVIDUAL", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public Integer getIsIndividual() {
        return isIndividual;
    }

    public void setIsIndividual(Integer isIndividual) {
        this.isIndividual = isIndividual;
    }

    @Column(name = "IS_ADVANCE_PAYMENT", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public Integer getIsAdvancePayment() {
        return isAdvancePayment;
    }

    public void setIsAdvancePayment(Integer isAdvancePayment) {
        this.isAdvancePayment = isAdvancePayment;
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

    @Column(name = "IS_PROJECT", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public Integer getIsProject() {
        return isProject;
    }

    public void setIsProject(Integer isProject) {
        this.isProject = isProject;
    }

    @Column(name = "PRECLEAR_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getPreclearDate() {
        return preclearDate;
    }

    public void setPreclearDate(Date preclearDate) {
        this.preclearDate = preclearDate;
    }

    @Column(name = "APPLY_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    @Column(name = "OPERATE_TIME", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    @Column(name = "CERTIFICATE_CREATE_TIME", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getCertificateCreateTime() {
        return certificateCreateTime;
    }

    public void setCertificateCreateTime(Date certificateCreateTime) {
        this.certificateCreateTime = certificateCreateTime;
    }

    @Column(name = "MEETING_START_TIME", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getMeetingStartTime() {
        return meetingStartTime;
    }

    public void setMeetingStartTime(Date meetingStartTime) {
        this.meetingStartTime = meetingStartTime;
    }

    @Column(name = "MEETING_END_TIME", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getMeetingEndTime() {
        return meetingEndTime;
    }

    public void setMeetingEndTime(Date meetingEndTime) {
        this.meetingEndTime = meetingEndTime;
    }

    @Column(name = "MEETING_DAYS", nullable = true, insertable = true, updatable = true, length = 8, precision = 0)
    public Integer getMeetingDays() {
        return meetingDays;
    }

    public void setMeetingDays(Integer meetingDays) {
        this.meetingDays = meetingDays;
    }

    @Column(name = "MEETING_LOCATION", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
    public String getMeetingLocation() {
        return meetingLocation;
    }

    public void setMeetingLocation(String meetingLocation) {
        this.meetingLocation = meetingLocation;
    }

    @Column(name = "MEETING_IN_PERSON_NUM", nullable = true, insertable = true, updatable = true, length = 8, precision = 0)
    public Integer getMeetingInPersonNum() {
        return meetingInPersonNum;
    }

    public void setMeetingInPersonNum(Integer meetingInPersonNum) {
        this.meetingInPersonNum = meetingInPersonNum;
    }

    @Column(name = "MEETING_OUT_PERSON_NUM", nullable = true, insertable = true, updatable = true, length = 8, precision = 0)
    public Integer getMeetingOutPersonNum() {
        return meetingOutPersonNum;
    }

    public void setMeetingOutPersonNum(Integer meetingOutPersonNum) {
        this.meetingOutPersonNum = meetingOutPersonNum;
    }

    @Column(name = "MEETING_SUBJECT", nullable = true, insertable = true, updatable = true, length = 500, precision = 0)
    public String getMeetingSubject() {
        return meetingSubject;
    }

    public void setMeetingSubject(String meetingSubject) {
        this.meetingSubject = meetingSubject;
    }

    @Column(name = "OPERATE_BUSINESS_BID_INFO_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getOperateBusinessBidInfoId() {
        return operateBusinessBidInfoId;
    }

    public void setOperateBusinessBidInfoId(String operateBusinessBidInfoId) {
        this.operateBusinessBidInfoId = operateBusinessBidInfoId;
    }
}