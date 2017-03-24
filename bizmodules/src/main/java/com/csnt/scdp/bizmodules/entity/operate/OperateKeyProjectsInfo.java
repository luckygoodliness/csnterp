package com.csnt.scdp.bizmodules.entity.operate;

import com.csnt.scdp.framework.dto.BasePojo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:  OperateKeyProjectsInfo
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-10-27 17:35:53
 */

@javax.persistence.Entity
@Table(name = "OPERATE_KEY_PROJECTS_INFO")
public class OperateKeyProjectsInfo extends BasePojo {
    private String uuid;
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
    private Date period;
    private Date recordDate;
    private String projectName;
    private Integer isKey;
    private String proprietorUnit;
    private String decisionMaker;
    private String projectProfile;
    private Date bidTime;
    private Date signedTime;
    private String competitor;
    private String projectOc;
    private String questions;
    private BigDecimal plannedCa;
    private BigDecimal projectMoney;
    private String operationState;
    private String operationSummary;
    private String contractStatus;
    private String remark;
    private String isWin;
    private String comBidUnit;
    private String comBidNumber;
    private Date bidingDocStart;
    private Date bidingDocEnd;
    private BigDecimal bidingDocPrice;
    private BigDecimal bidBond;
    private String eotm;
    private Date bod;
    private Integer seqNo;
    private String officeId;


    private String area;

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

    @Column(name = "PERIOD", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getPeriod() {
        return period;
    }

    public void setPeriod(Date period) {
        this.period = period;
    }

    @Column(name = "RECORD_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    @Column(name = "PROJECT_NAME", nullable = true, insertable = true, updatable = true, length = 200, precision = 0)
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Column(name = "IS_KEY", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public Integer getIsKey() {
        return isKey;
    }

    public void setIsKey(Integer isKey) {
        this.isKey = isKey;
    }

    @Column(name = "PROPRIETOR_UNIT", nullable = true, insertable = true, updatable = true, length = 200, precision = 0)
    public String getProprietorUnit() {
        return proprietorUnit;
    }

    public void setProprietorUnit(String proprietorUnit) {
        this.proprietorUnit = proprietorUnit;
    }

    @Column(name = "DECISION_MAKER", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getDecisionMaker() {
        return decisionMaker;
    }

    public void setDecisionMaker(String decisionMaker) {
        this.decisionMaker = decisionMaker;
    }

    @Column(name = "PROJECT_PROFILE", nullable = true, insertable = true, updatable = true, length = 2000, precision = 0)
    public String getProjectProfile() {
        return projectProfile;
    }

    public void setProjectProfile(String projectProfile) {
        this.projectProfile = projectProfile;
    }

    @Column(name = "BID_TIME", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getBidTime() {
        return bidTime;
    }

    public void setBidTime(Date bidTime) {
        this.bidTime = bidTime;
    }

    @Column(name = "SIGNED_TIME", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getSignedTime() {
        return signedTime;
    }

    public void setSignedTime(Date signedTime) {
        this.signedTime = signedTime;
    }

    @Column(name = "COMPETITOR", nullable = true, insertable = true, updatable = true, length = 2000, precision = 0)
    public String getCompetitor() {
        return competitor;
    }

    public void setCompetitor(String competitor) {
        this.competitor = competitor;
    }

    @Column(name = "PROJECT_OC", nullable = true, insertable = true, updatable = true, length = 2000, precision = 0)
    public String getProjectOc() {
        return projectOc;
    }

    public void setProjectOc(String projectOc) {
        this.projectOc = projectOc;
    }

    @Column(name = "QUESTIONS", nullable = true, insertable = true, updatable = true, length = 2000, precision = 0)
    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    @Column(name = "PLANNED_CA", nullable = true, insertable = true, updatable = true, length = 19, precision = 3)
    public BigDecimal getPlannedCa() {
        return plannedCa;
    }

    public void setPlannedCa(BigDecimal plannedCa) {
        this.plannedCa = plannedCa;
    }

    @Column(name = "PROJECT_MONEY", nullable = true, insertable = true, updatable = true, length = 19, precision = 3)
    public BigDecimal getProjectMoney() {
        return projectMoney;
    }

    public void setProjectMoney(BigDecimal projectMoney) {
        this.projectMoney = projectMoney;
    }

    @Column(name = "OPERATION_STATE", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getOperationState() {
        return operationState;
    }

    public void setOperationState(String operationState) {
        this.operationState = operationState;
    }

    @Column(name = "OPERATION_SUMMARY", nullable = true, insertable = true, updatable = true, length = 2000, precision = 0)
    public String getOperationSummary() {
        return operationSummary;
    }

    public void setOperationSummary(String operationSummary) {
        this.operationSummary = operationSummary;
    }

    @Column(name = "CONTRACT_STATUS", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }

    @Column(name = "REMARK", nullable = true, insertable = true, updatable = true, length = 2000, precision = 0)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "IS_WIN", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getIsWin() {
        return isWin;
    }

    public void setIsWin(String isWin) {
        this.isWin = isWin;
    }

    @Column(name = "COM_BID_UNIT", nullable = true, insertable = true, updatable = true, length = 200, precision = 0)
    public String getComBidUnit() {
        return comBidUnit;
    }

    public void setComBidUnit(String comBidUnit) {
        this.comBidUnit = comBidUnit;
    }

    @Column(name = "COM_BID_NUMBER", nullable = true, insertable = true, updatable = true, length = 200, precision = 0)
    public String getComBidNumber() {
        return comBidNumber;
    }

    public void setComBidNumber(String comBidNumber) {
        this.comBidNumber = comBidNumber;
    }

    @Column(name = "BIDING_DOC_START", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getBidingDocStart() {
        return bidingDocStart;
    }

    public void setBidingDocStart(Date bidingDocStart) {
        this.bidingDocStart = bidingDocStart;
    }

    @Column(name = "BIDING_DOC_END", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getBidingDocEnd() {
        return bidingDocEnd;
    }

    public void setBidingDocEnd(Date bidingDocEnd) {
        this.bidingDocEnd = bidingDocEnd;
    }

    @Column(name = "BIDING_DOC_PRICE", nullable = true, insertable = true, updatable = true, length = 19, precision = 3)
    public BigDecimal getBidingDocPrice() {
        return bidingDocPrice;
    }

    public void setBidingDocPrice(BigDecimal bidingDocPrice) {
        this.bidingDocPrice = bidingDocPrice;
    }

    @Column(name = "BID_BOND", nullable = true, insertable = true, updatable = true, length = 19, precision = 3)
    public BigDecimal getBidBond() {
        return bidBond;
    }

    public void setBidBond(BigDecimal bidBond) {
        this.bidBond = bidBond;
    }

    @Column(name = "EOTM", nullable = true, insertable = true, updatable = true, length = 500, precision = 0)
    public String getEotm() {
        return eotm;
    }

    public void setEotm(String eotm) {
        this.eotm = eotm;
    }

    @Column(name = "BOD", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getBod() {
        return bod;
    }

    public void setBod(Date bod) {
        this.bod = bod;
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

    @Column(name = "AREA", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

}