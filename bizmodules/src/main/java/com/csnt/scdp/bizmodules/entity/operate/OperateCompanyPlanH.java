package com.csnt.scdp.bizmodules.entity.operate;

import com.csnt.scdp.framework.dto.BasePojo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.math.BigDecimal;

/**
 * Description:  OperateCompanyPlanH
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-11-19 20:30:35
 */

@javax.persistence.Entity
@Table(name = "OPERATE_COMPANY_PLAN_H")
public class OperateCompanyPlanH extends BasePojo {
    private String uuid;
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
    private Integer seqNo;
    private Integer startYear;
    private String planName;
    private BigDecimal jhsr;
    private BigDecimal jhjslr;
    private BigDecimal jhlxlr;
    private BigDecimal jhjssr;
    private BigDecimal jhhte;
    private String remark;

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

    @Column(name = "SEQ_NO", nullable = true, insertable = true, updatable = true, length = 9, precision = 0)
    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    @Column(name = "START_YEAR", nullable = true, insertable = true, updatable = true, length = 4, precision = 0)
    public Integer getStartYear() {
        return startYear;
    }

    public void setStartYear(Integer startYear) {
        this.startYear = startYear;
    }

    @Column(name = "PLAN_NAME", nullable = true, insertable = true, updatable = true, length = 200, precision = 0)
    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    @Column(name = "JHSR", nullable = true, insertable = true, updatable = true, length = 18, precision = 4)
    public BigDecimal getJhsr() {
        return jhsr;
    }

    public void setJhsr(BigDecimal jhsr) {
        this.jhsr = jhsr;
    }

    @Column(name = "JHJSLR", nullable = true, insertable = true, updatable = true, length = 18, precision = 4)
    public BigDecimal getJhjslr() {
        return jhjslr;
    }

    public void setJhjslr(BigDecimal jhjslr) {
        this.jhjslr = jhjslr;
    }

    @Column(name = "JHLXLR", nullable = true, insertable = true, updatable = true, length = 18, precision = 4)
    public BigDecimal getJhlxlr() {
        return jhlxlr;
    }

    public void setJhlxlr(BigDecimal jhlxlr) {
        this.jhlxlr = jhlxlr;
    }

    @Column(name = "JHJSSR", nullable = true, insertable = true, updatable = true, length = 18, precision = 4)
    public BigDecimal getJhjssr() {
        return jhjssr;
    }

    public void setJhjssr(BigDecimal jhjssr) {
        this.jhjssr = jhjssr;
    }

    @Column(name = "JHHTE", nullable = true, insertable = true, updatable = true, length = 18, precision = 4)
    public BigDecimal getJhhte() {
        return jhhte;
    }

    public void setJhhte(BigDecimal jhhte) {
        this.jhhte = jhhte;
    }

    @Column(name = "REMARK", nullable = true, insertable = true, updatable = true, length = 1000, precision = 0)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}