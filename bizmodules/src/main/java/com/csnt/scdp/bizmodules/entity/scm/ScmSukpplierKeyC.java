package com.csnt.scdp.bizmodules.entity.scm;

import com.csnt.scdp.framework.dto.BasePojo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.math.BigDecimal;

/**
 * Description:  ScmSukpplierKeyC
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-07-28 17:10:24
 */

@javax.persistence.Entity
@Table(name = "SCM_SUKPPLIER_KEY_C")
public class ScmSukpplierKeyC extends BasePojo {
    private String uuid;
    private String scmSupplierId;
    private String keyNaem;
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
    private String puuid;

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

    @Column(name = "SCM_SUPPLIER_ID", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getScmSupplierId() {
        return scmSupplierId;
    }

    public void setScmSupplierId(String scmSupplierId) {
        this.scmSupplierId = scmSupplierId;
    }

    @Column(name = "KEY_NAEM", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
    public String getKeyNaem() {
        return keyNaem;
    }

    public void setKeyNaem(String keyNaem) {
        this.keyNaem = keyNaem;
    }

    @Column(name = "REMARK", nullable = true, insertable = true, updatable = true, length = 200, precision = 0)
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

    @Column(name = "PUUID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getPuuid() { return puuid;}

    public void setPuuid(String puuid) {this.puuid = puuid;}

}