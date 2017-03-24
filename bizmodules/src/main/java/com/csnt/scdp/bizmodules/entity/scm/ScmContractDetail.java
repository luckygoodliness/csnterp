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
 * Description:  ScmContractDetail
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-11-25 21:07:45
 */

@javax.persistence.Entity
@Table(name = "SCM_CONTRACT_DETAIL")
public class ScmContractDetail extends BasePojo {
    private String uuid;
    private String scmContractId;
    private String materialName;
    private String model;
    private String units;
    private BigDecimal amount;
    private BigDecimal unitPriceTalk;
    private BigDecimal unitPriceTrue;
    private String factory;
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
    private Integer seqNo;
    private String materialNumber;
    private Integer isRepair;

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

    @Column(name = "SCM_CONTRACT_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getScmContractId() {
        return scmContractId;
    }

    public void setScmContractId(String scmContractId) {
        this.scmContractId = scmContractId;
    }

    @Column(name = "MATERIAL_NAME", nullable = true, insertable = true, updatable = true, length = 1000, precision = 0)
    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    @Column(name = "MODEL", nullable = true, insertable = true, updatable = true, length = 4000, precision = 0)
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Column(name = "UNITS", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    @Column(name = "AMOUNT", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Column(name = "UNIT_PRICE_TALK", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getUnitPriceTalk() {
        return unitPriceTalk;
    }

    public void setUnitPriceTalk(BigDecimal unitPriceTalk) {
        this.unitPriceTalk = unitPriceTalk;
    }

    @Column(name = "UNIT_PRICE_TRUE", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getUnitPriceTrue() {
        return unitPriceTrue;
    }

    public void setUnitPriceTrue(BigDecimal unitPriceTrue) {
        this.unitPriceTrue = unitPriceTrue;
    }

    @Column(name = "FACTORY", nullable = true, insertable = true, updatable = true, length = 400, precision = 0)
    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    @Column(name = "REMARK", nullable = true, insertable = true, updatable = true, length = 400, precision = 0)
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

    @Column(name = "SEQ_NO", nullable = true, insertable = true, updatable = true, length = 9, precision = 0)
    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    @Column(name = "MATERIAL_NUMBER", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
    public String getMaterialNumber() {
        return materialNumber;
    }

    public void setMaterialNumber(String materialNumber) {
        this.materialNumber = materialNumber;
    }

    @Column(name = "IS_REPAIR", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public Integer getIsRepair() {
        return isRepair;
    }

    public void setIsRepair(Integer isRepair) {
        this.isRepair = isRepair;
    }
}