package com.csnt.scdp.bizmodules.entity.prm.common;

import com.csnt.scdp.framework.dto.BasePojo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:  PrmProjectMain
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 13:30:43
 */

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class PrmAbstractProjectMain extends BasePojo {
    private String uuid;
    private String prmContractId;
    private String projectName;
    private String projectShortName;
    private Integer isPreProject;
    private BigDecimal projectMoney;
    private BigDecimal contractSignMoney;
    private BigDecimal costControlMoney;
    private String projectSerialNo;
    private String customerId;
    private String contractorOffice;
    private String projectManager;
    private String contractDuration;
    private Date scheduledBeginDate;
    private Date scheduledEndDate;
    private String taxType;
    private String fundsExplanation;
    private String remark;
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
    private String projectCode;
    private Date establishDate;
    private String prmCodeType;
    private Integer isMajorProject;


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

    @Column(name = "PRM_CONTRACT_ID", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getPrmContractId() {
        return prmContractId;
    }

    public void setPrmContractId(String prmContractId) {
        this.prmContractId = prmContractId;
    }

    @Column(name = "PROJECT_NAME", nullable = true, insertable = true, updatable = true, length = 200, precision = 0)
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Column(name = "PROJECT_SHORT_NAME", nullable = true, insertable = true, updatable = true, length = 50, precision
            = 0)
    public String getProjectShortName() {
        return projectShortName;
    }

    public void setProjectShortName(String projectShortName) {
        this.projectShortName = projectShortName;
    }

    @Column(name = "IS_PRE_PROJECT", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public Integer getIsPreProject() {
        return isPreProject;
    }

    public void setIsPreProject(Integer isPreProject) {
        this.isPreProject = isPreProject;
    }

    @Column(name = "PROJECT_MONEY", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getProjectMoney() {
        return projectMoney;
    }

    public void setProjectMoney(BigDecimal projectMoney) {
        this.projectMoney = projectMoney;
    }

    @Column(name = "COST_CONTROL_MONEY", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getCostControlMoney() {
        return costControlMoney;
    }

    public void setCostControlMoney(BigDecimal costControlMoney) {
        this.costControlMoney = costControlMoney;
    }

    @Column(name = "PROJECT_SERIAL_NO", nullable = true, insertable = true, updatable = true, length = 64, precision = 0)
    public String getProjectSerialNo() {
        return projectSerialNo;
    }

    public void setProjectSerialNo(String projectSerialNo) {
        this.projectSerialNo = projectSerialNo;
    }

    @Column(name = "CUSTOMER_ID", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @Column(name = "CONTRACTOR_OFFICE", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getContractorOffice() {
        return contractorOffice;
    }

    public void setContractorOffice(String contractorOffice) {
        this.contractorOffice = contractorOffice;
    }

    @Column(name = "PROJECT_MANAGER", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    @Column(name = "CONTRACT_DURATION", nullable = true, insertable = true, updatable = true, length = 4000, precision = 0)
    public String getContractDuration() {
        return contractDuration;
    }

    public void setContractDuration(String contractDuration) {
        this.contractDuration = contractDuration;
    }

    @Column(name = "SCHEDULED_BEGIN_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getScheduledBeginDate() {
        return scheduledBeginDate;
    }

    public void setScheduledBeginDate(Date scheduledBeginDate) {
        this.scheduledBeginDate = scheduledBeginDate;
    }

    @Column(name = "SCHEDULED_END_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getScheduledEndDate() {
        return scheduledEndDate;
    }

    public void setScheduledEndDate(Date scheduledEndDate) {
        this.scheduledEndDate = scheduledEndDate;
    }

    @Column(name = "TAX_TYPE", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    @Column(name = "FUNDS_EXPLANATION", nullable = true, insertable = true, updatable = true, length = 4000, precision = 0)
    public String getFundsExplanation() {
        return fundsExplanation;
    }

    public void setFundsExplanation(String fundsExplanation) {
        this.fundsExplanation = fundsExplanation;
    }

    @Column(name = "REMARK", nullable = true, insertable = true, updatable = true, length = 4000, precision = 0)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    @Column(name = "PROJECT_CODE", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    @Column(name = "ESTABLISH_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getEstablishDate() {
        return establishDate;
    }

    public void setEstablishDate(Date establishDate) {
        this.establishDate = establishDate;
    }

    @Column(name = "PRM_CODE_TYPE", nullable = true, insertable = true, updatable = true, length = 16, precision = 0)
    public String getPrmCodeType() {
        return prmCodeType;
    }

    public void setPrmCodeType(String prmCodeType) {
        this.prmCodeType = prmCodeType;
    }

    @Column(name = "IS_MAJOR_PROJECT", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public Integer getIsMajorProject() {
        return isMajorProject;
    }

    public void setIsMajorProject(Integer isMajorProject) {
        this.isMajorProject = isMajorProject;
    }

    @Column(name = "CONTRACT_SIGN_MONEY", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getContractSignMoney() {
        return contractSignMoney;
    }

    public void setContractSignMoney(BigDecimal contractSignMoney) {
        this.contractSignMoney = contractSignMoney;
    }
}