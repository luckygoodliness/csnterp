package com.csnt.scdp.bizmodules.entity.asset;

import com.csnt.scdp.framework.dto.BasePojo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Description:  AssetCard
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-22 10:18:39
 */

@javax.persistence.Entity
@Table(name = "ASSET_TRANSFER")
public class AssetCardTransfer extends BasePojo {
    private String uuid;
    private String cardUuid;
    private String state;
    private String remark;
    private String ransferType;

    private String endUserCode;
    private String endUserName;
    private String officeId;
    private String liablePerson;

    private Integer seqNo;
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

    private String ransferApplyId;
    private String operatePerson;
    private Date applyDate;

    private String outPersonCode;
    private String outPersonName;
    private String outOfficeId;
    private String outLiablePerson;

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

    @Column(name = "CARD_UUID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getCardUuid() {
        return cardUuid;
    }

    public void setCardUuid(String cardUuid) {
        this.cardUuid = cardUuid;
    }

    @Column(name = "STATE", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name = "REMARK", nullable = true, insertable = true, updatable = true, length = 2000, precision = 0)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "RANSFER_TYPE", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getRansferType() {
        return ransferType;
    }

    public void setRansferType(String ransferType) {
        this.ransferType = ransferType;
    }

    @Column(name = "IN_PERSON_CODE", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getEndUserCode() {
        return endUserCode;
    }

    public void setEndUserCode(String endUserCode) {
        this.endUserCode = endUserCode;
    }

    @Column(name = "IN_PERSON_NAME", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getEndUserName() {
        return endUserName;
    }

    public void setEndUserName(String endUserName) {
        this.endUserName = endUserName;
    }

    @Column(name = "IN_OFFICE_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    @Column(name = "IN_LIABLE_PERSON", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getLiablePerson() {
        return liablePerson;
    }

    public void setLiablePerson(String liablePerson) {
        this.liablePerson = liablePerson;
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

    @Column(name = "RANSFER_APPLY_ID", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getRansferApplyId() {
        return ransferApplyId;
    }

    public void setRansferApplyId(String ransferApplyId) {
        this.ransferApplyId = ransferApplyId;
    }

    @Column(name = "OPERATE_PERSON", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getOperatePerson() {
        return operatePerson;
    }

    public void setOperatePerson(String operatePerson) {
        this.operatePerson = operatePerson;
    }

    @Column(name = "APPLY_DATE", nullable = false, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    @Column(name = "OUT_PERSON_CODE", nullable = true, insertable = true, updatable = true, length = 2000, precision = 0)
    public String getOutPersonCode() {
        return outPersonCode;
    }

    public void setOutPersonCode(String outPersonCode) {
        this.outPersonCode = outPersonCode;
    }

    @Column(name = "OUT_PERSON_NAME", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getOutPersonName() {
        return outPersonName;
    }

    public void setOutPersonName(String outPersonName) {
        this.outPersonName = outPersonName;
    }

    @Column(name = "OUT_OFFICE_ID", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public String getOutOfficeId() {
        return outOfficeId;
    }

    public void setOutOfficeId(String outOfficeId) {
        this.outOfficeId = outOfficeId;
    }

    @Column(name = "OUT_LIABLE_PERSON", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getOutLiablePerson() {
        return outLiablePerson;
    }

    public void setOutLiablePerson(String outLiablePerson) {
        this.outLiablePerson = outLiablePerson;
    }
}