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
 * Description:  FadCertificateAccount
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-10-16 10:07:48
 */

@javax.persistence.Entity
@Table(name = "FAD_CERTIFICATE_ACCOUNT")
public class FadCertificateAccount extends BasePojo {
    private String uuid;
    private String fadCertificateDetailId;
    private String accountNo;
    private String accountType;
    private String accountInfoId;
    private String accountInfoCode;
    private String accountInfoName;
    private Integer orderNo;
    private String free1;
    private String free2;
    private String free3;
    private String free4;
    private String free5;
    private String free6;
    private String free7;
    private String free8;
    private String free9;
    private String free10;
    private String free11;
    private String free12;
    private String free13;
    private String free14;
    private String free15;
    private String free16;
    private String free17;
    private String free18;
    private String free19;
    private String free20;
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

    @Column(name = "FAD_CERTIFICATE_DETAIL_ID", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    public String getFadCertificateDetailId() {
        return fadCertificateDetailId;
    }

    public void setFadCertificateDetailId(String fadCertificateDetailId) {
        this.fadCertificateDetailId = fadCertificateDetailId;
    }

    @Column(name = "ACCOUNT_NO", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    @Column(name = "ACCOUNT_TYPE", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Column(name = "ACCOUNT_INFO_ID", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getAccountInfoId() {
        return accountInfoId;
    }

    public void setAccountInfoId(String accountInfoId) {
        this.accountInfoId = accountInfoId;
    }

    @Column(name = "ACCOUNT_INFO_CODE", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getAccountInfoCode() {
        return accountInfoCode;
    }

    public void setAccountInfoCode(String accountInfoCode) {
        this.accountInfoCode = accountInfoCode;
    }

    @Column(name = "ACCOUNT_INFO_NAME", nullable = true, insertable = true, updatable = true, length = 200, precision = 0)
    public String getAccountInfoName() {
        return accountInfoName;
    }

    public void setAccountInfoName(String accountInfoName) {
        this.accountInfoName = accountInfoName;
    }

    @Column(name = "ORDER_NO", nullable = true, insertable = true, updatable = true, length = 4, precision = 0)
    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    @Column(name = "FREE1", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree1() {
        return free1;
    }

    public void setFree1(String free1) {
        this.free1 = free1;
    }

    @Column(name = "FREE2", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree2() {
        return free2;
    }

    public void setFree2(String free2) {
        this.free2 = free2;
    }

    @Column(name = "FREE3", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree3() {
        return free3;
    }

    public void setFree3(String free3) {
        this.free3 = free3;
    }

    @Column(name = "FREE4", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree4() {
        return free4;
    }

    public void setFree4(String free4) {
        this.free4 = free4;
    }

    @Column(name = "FREE5", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree5() {
        return free5;
    }

    public void setFree5(String free5) {
        this.free5 = free5;
    }

    @Column(name = "FREE6", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree6() {
        return free6;
    }

    public void setFree6(String free6) {
        this.free6 = free6;
    }

    @Column(name = "FREE7", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree7() {
        return free7;
    }

    public void setFree7(String free7) {
        this.free7 = free7;
    }

    @Column(name = "FREE8", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree8() {
        return free8;
    }

    public void setFree8(String free8) {
        this.free8 = free8;
    }

    @Column(name = "FREE9", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree9() {
        return free9;
    }

    public void setFree9(String free9) {
        this.free9 = free9;
    }

    @Column(name = "FREE10", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree10() {
        return free10;
    }

    public void setFree10(String free10) {
        this.free10 = free10;
    }

    @Column(name = "FREE11", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree11() {
        return free11;
    }

    public void setFree11(String free11) {
        this.free11 = free11;
    }

    @Column(name = "FREE12", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree12() {
        return free12;
    }

    public void setFree12(String free12) {
        this.free12 = free12;
    }

    @Column(name = "FREE13", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree13() {
        return free13;
    }

    public void setFree13(String free13) {
        this.free13 = free13;
    }

    @Column(name = "FREE14", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree14() {
        return free14;
    }

    public void setFree14(String free14) {
        this.free14 = free14;
    }

    @Column(name = "FREE15", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree15() {
        return free15;
    }

    public void setFree15(String free15) {
        this.free15 = free15;
    }

    @Column(name = "FREE16", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree16() {
        return free16;
    }

    public void setFree16(String free16) {
        this.free16 = free16;
    }

    @Column(name = "FREE17", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree17() {
        return free17;
    }

    public void setFree17(String free17) {
        this.free17 = free17;
    }

    @Column(name = "FREE18", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree18() {
        return free18;
    }

    public void setFree18(String free18) {
        this.free18 = free18;
    }

    @Column(name = "FREE19", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree19() {
        return free19;
    }

    public void setFree19(String free19) {
        this.free19 = free19;
    }

    @Column(name = "FREE20", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getFree20() {
        return free20;
    }

    public void setFree20(String free20) {
        this.free20 = free20;
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
}