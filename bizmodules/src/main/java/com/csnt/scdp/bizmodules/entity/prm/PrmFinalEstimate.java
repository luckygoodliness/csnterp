package com.csnt.scdp.bizmodules.entity.prm;

import com.csnt.scdp.framework.dto.BasePojo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:  PrmFinalEstimate
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 20:02:44
 */

@javax.persistence.Entity
@Table(name = "PRM_FINAL_ESTIMATE")
public class PrmFinalEstimate extends BasePojo {
    private String uuid;
    private String prmProjectMainId;
    private BigDecimal squareProjectMoney;
    private BigDecimal squareGrossProfit;
    private BigDecimal manageMoney;
    private BigDecimal rax;
    private BigDecimal squareCost;
    private String state;
    private Integer isArchiving;
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
    private String squareType;
    private Date  squareDate;
    private Date reviseTaxDate;
    private BigDecimal taxCorrection;
    private BigDecimal costCorrection;
    private Date examDate;



    private Date examRTaxDate;

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

    @Column(name = "PRM_PROJECT_MAIN_ID", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getPrmProjectMainId() {
        return prmProjectMainId;
    }

    public void setPrmProjectMainId(String prmProjectMainId) {
        this.prmProjectMainId = prmProjectMainId;
    }

    @Column(name = "SQUARE_PROJECT_MONEY", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getSquareProjectMoney() {
        return squareProjectMoney;
    }

    public void setSquareProjectMoney(BigDecimal squareProjectMoney) {
        this.squareProjectMoney = squareProjectMoney;
    }

    @Column(name = "SQUARE_GROSS_PROFIT", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getSquareGrossProfit() {
        return squareGrossProfit;
    }

    public void setSquareGrossProfit(BigDecimal squareGrossProfit) {
        this.squareGrossProfit = squareGrossProfit;
    }

    @Column(name = "MANAGE_MONEY", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getManageMoney() {
        return manageMoney;
    }

    public void setManageMoney(BigDecimal manageMoney) {
        this.manageMoney = manageMoney;
    }

    @Column(name = "RAX", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getRax() {
        return rax;
    }

    public void setRax(BigDecimal rax) {
        this.rax = rax;
    }

    @Column(name = "STATE", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name = "IS_ARCHIVING", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public Integer getIsArchiving() {
        return isArchiving;
    }

    public void setIsArchiving(Integer isArchiving) {
        this.isArchiving = isArchiving;
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

    @Column(name = "SQUARE_TYPE", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    public String getSquareType() {
        return squareType;
    }

    public void setSquareType(String squareType) {
        this.squareType = squareType;
    }

    @Column(name = "SQUARE_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getSquareDate() { return squareDate;
    }

    public void setSquareDate(Date squareDate) {
        this.squareDate = squareDate;
    }

    @Column(name = "SQUARE_COST", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getSquareCost() {
        return squareCost;
    }

    public void setSquareCost(BigDecimal squareCost) {
        this.squareCost = squareCost;
    }

    @Column(name = "TAX_CORRECTION", nullable = true, insertable = true, updatable = true, length = 18, precision = 2)
    public BigDecimal getTaxCorrection() {
        return taxCorrection;
    }

    public void setTaxCorrection(BigDecimal taxCorrection) {
        this.taxCorrection = taxCorrection;
    }

    @Column(name = "COST_CORRECTION", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getCostCorrection() {
        return costCorrection;
    }

    public void setCostCorrection(BigDecimal costCorrection) {
        this.costCorrection = costCorrection;
    }

    @Column(name = "REVISE_TAX_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getReviseTaxDate() {
        return reviseTaxDate;
    }

    public void setReviseTaxDate(Date reviseTaxDate) {
        this.reviseTaxDate = reviseTaxDate;
    }

    @Column(name = "EXAM_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    @Column(name = "EXAM_R_TAX_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getExamRTaxDate() {
        return examRTaxDate;
    }

    public void setExamRTaxDate(Date examRTaxDate) {
        this.examRTaxDate = examRTaxDate;
    }
}