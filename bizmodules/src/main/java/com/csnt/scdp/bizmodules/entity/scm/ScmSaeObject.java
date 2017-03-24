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
 * Description:  ScmSaeObject
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-09-01 20:49:04
 */

@javax.persistence.Entity
@Table(name = "SCM_SAE_OBJECT")
public class ScmSaeObject extends BasePojo {
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
    private String scmSaeId;
    private String supplierId;
    private String materialCode;
    private Integer materialType;
    private BigDecimal comprehensive;
    private Integer item1;
    private Integer item2;
    private Integer item3;
    private Integer item4;
    private Integer item5;
    private Integer item6;
    private Integer item7;
    private Integer item8;
    private Integer item9;
    private Integer item10;
    private Integer item11;
    private Integer item12;
    private Integer item13;
    private Integer item14;
    private Integer item15;
    //private  ScmSaeFormDto;
    //private  ScmSaeFormDto_c;

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

    @Column(name = "SCM_SAE_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getScmSaeId() {
        return scmSaeId;
    }

    public void setScmSaeId(String scmSaeId) {
        this.scmSaeId = scmSaeId;
    }

    @Column(name = "SUPPLIER_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    @Column(name = "MATERIAL_CODE", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    @Column(name = "MATERIAL_TYPE", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public Integer getMaterialType() {
        return materialType;
    }

    public void setMaterialType(Integer materialType) {
        this.materialType = materialType;
    }

    @Column(name = "COMPREHENSIVE", nullable = true, insertable = true, updatable = true, length = 18, precision = 3)
    public BigDecimal getComprehensive() {
        return comprehensive;
    }

    public void setComprehensive(BigDecimal comprehensive) {
        this.comprehensive = comprehensive;
    }

    @Column(name = "ITEM_1", nullable = true, insertable = true, updatable = true, length = 3, precision = 0)
    public Integer getItem1() {
        return item1;
    }

    public void setItem1(Integer item1) {
        this.item1 = item1;
    }

    @Column(name = "ITEM_2", nullable = true, insertable = true, updatable = true, length = 3, precision = 0)
    public Integer getItem2() {
        return item2;
    }

    public void setItem2(Integer item2) {
        this.item2 = item2;
    }

    @Column(name = "ITEM_3", nullable = true, insertable = true, updatable = true, length = 3, precision = 0)
    public Integer getItem3() {
        return item3;
    }

    public void setItem3(Integer item3) {
        this.item3 = item3;
    }

    @Column(name = "ITEM_4", nullable = true, insertable = true, updatable = true, length = 3, precision = 0)
    public Integer getItem4() {
        return item4;
    }

    public void setItem4(Integer item4) {
        this.item4 = item4;
    }

    @Column(name = "ITEM_5", nullable = true, insertable = true, updatable = true, length = 3, precision = 0)
    public Integer getItem5() {
        return item5;
    }

    public void setItem5(Integer item5) {
        this.item5 = item5;
    }

    @Column(name = "ITEM_6", nullable = true, insertable = true, updatable = true, length = 3, precision = 0)
    public Integer getItem6() {
        return item6;
    }

    public void setItem6(Integer item6) {
        this.item6 = item6;
    }

    @Column(name = "ITEM_7", nullable = true, insertable = true, updatable = true, length = 3, precision = 0)
    public Integer getItem7() {
        return item7;
    }

    public void setItem7(Integer item7) {
        this.item7 = item7;
    }

    @Column(name = "ITEM_8", nullable = true, insertable = true, updatable = true, length = 3, precision = 0)
    public Integer getItem8() {
        return item8;
    }

    public void setItem8(Integer item8) {
        this.item8 = item8;
    }

    @Column(name = "ITEM_9", nullable = true, insertable = true, updatable = true, length = 3, precision = 0)
    public Integer getItem9() {
        return item9;
    }

    public void setItem9(Integer item9) {
        this.item9 = item9;
    }

    @Column(name = "ITEM_10", nullable = true, insertable = true, updatable = true, length = 3, precision = 0)
    public Integer getItem10() {
        return item10;
    }

    public void setItem10(Integer item10) {
        this.item10 = item10;
    }

    @Column(name = "ITEM_11", nullable = true, insertable = true, updatable = true, length = 3, precision = 0)
    public Integer getItem11() {
        return item11;
    }

    public void setItem11(Integer item11) {
        this.item11 = item11;
    }

    @Column(name = "ITEM_12", nullable = true, insertable = true, updatable = true, length = 3, precision = 0)
    public Integer getItem12() {
        return item12;
    }

    public void setItem12(Integer item12) {
        this.item12 = item12;
    }

    @Column(name = "ITEM_13", nullable = true, insertable = true, updatable = true, length = 3, precision = 0)
    public Integer getItem13() {
        return item13;
    }

    public void setItem13(Integer item13) {
        this.item13 = item13;
    }

    @Column(name = "ITEM_14", nullable = true, insertable = true, updatable = true, length = 3, precision = 0)
    public Integer getItem14() {
        return item14;
    }

    public void setItem14(Integer item14) {
        this.item14 = item14;
    }

    @Column(name = "ITEM_15", nullable = true, insertable = true, updatable = true, length = 3, precision = 0)
    public Integer getItem15() {
        return item15;
    }

    public void setItem15(Integer item15) {
        this.item15 = item15;
    }

    /*
    @Column(name = "", nullable = false, insertable = true, updatable = true, length = , precision = )
    public  getScmSaeFormDto() {
        return ScmSaeFormDto;
    }

    public void setScmSaeFormDto( ScmSaeFormDto) {
        this.ScmSaeFormDto = ScmSaeFormDto;
    }

    @Column(name = "", nullable = false, insertable = true, updatable = true, length = , precision = )
    public  getScmSaeFormDto_c() {
        return ScmSaeFormDto_c;
    }

    public void setScmSaeFormDto_c( ScmSaeFormDto_c) {
        this.ScmSaeFormDto_c = ScmSaeFormDto_c;
    }*/

}