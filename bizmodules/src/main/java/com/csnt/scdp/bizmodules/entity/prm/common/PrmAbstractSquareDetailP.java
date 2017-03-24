package com.csnt.scdp.bizmodules.entity.prm.common;

import com.csnt.scdp.framework.dto.BasePojo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:  PrmSquareDetailP
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-25 13:59:41
 */

//@Entity
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class PrmAbstractSquareDetailP extends BasePojo {
    private String uuid;
    private Date schemingSquareDate;
    private BigDecimal schemingSquareMoney;
    private BigDecimal schemingSquareCost;
    private BigDecimal schemingSquareProfits;
    private String explanation;
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
    @GenericGenerator(name = "idGenerator", strategy = "assigned")
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Column(name = "SCHEMING_SQUARE_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getSchemingSquareDate() {
        return schemingSquareDate;
    }

    public void setSchemingSquareDate(Date schemingSquareDate) {
        this.schemingSquareDate = schemingSquareDate;
    }

    @Column(name = "SCHEMING_SQUARE_MONEY", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getSchemingSquareMoney() {
        return schemingSquareMoney;
    }

    public void setSchemingSquareMoney(BigDecimal schemingSquareMoney) {
        this.schemingSquareMoney = schemingSquareMoney;
    }

    @Column(name = "SCHEMING_SQUARE_COST", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getSchemingSquareCost() {
        return schemingSquareCost;
    }

    public void setSchemingSquareCost(BigDecimal schemingSquareCost) {
        this.schemingSquareCost = schemingSquareCost;
    }

    @Column(name = "SCHEMING_SQUARE_PROFITS", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getSchemingSquareProfits() {
        return schemingSquareProfits;
    }

    public void setSchemingSquareProfits(BigDecimal schemingSquareProfits) {
        this.schemingSquareProfits = schemingSquareProfits;
    }

    @Column(name = "EXPLANATION", nullable = true, insertable = true, updatable = true, length = 500, precision = 0)
    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
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

}