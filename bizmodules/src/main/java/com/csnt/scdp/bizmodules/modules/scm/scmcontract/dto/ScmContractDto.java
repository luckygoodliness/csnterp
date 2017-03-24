package com.csnt.scdp.bizmodules.modules.scm.scmcontract.dto;

import com.csnt.scdp.bizmodules.modules.cdm.cdmfile.dto.CdmFileRelationDto;
import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.scm.ScmContract;
import com.csnt.scdp.framework.util.ListUtil;

import java.math.BigDecimal;
import java.util.List;


/**
 * Description:  ScmContractDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-26 15:32:42
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.scm.ScmContract")
public class ScmContractDto extends ScmContract {
    @CascadeChilds(FK = "scmContractId|uuid")
    private List<ScmContractPaytypeDto> scmContractPaytypeDto;

    @CascadeChilds(FK = "scmContractId|uuid", cascadeDelete = false)
    private List<PrmPurchaseReqDetailDto> prmPurchaseReqDetailDto;

    @CascadeChilds(FK = "scmContractId|uuid")
    private List<ScmContractDetailDto> scmContractDetailDto;

    @CascadeChilds(FK = "scmContractId|uuid")
    private List<ScmContractChangeDto> scmContractChangeDto;

    @CascadeChilds(FK = "dataId|uuid")
    private List<CdmFileRelationDto> cdmFileRelationDto;
    //收款部门
    private String debterDepartment;    //收款部门
    private String projectName;//项目名称
    private String contractNatureName;//合同性质名称
    private String contractStateName;//合同状态名称
    private String orgName;//合同部门名称
    private String projectCode;//项目代号
    private BigDecimal fadInvoiceMoney;// 发票入账金额--state=4
    private BigDecimal fadInvoiceMoneyL;// 发票录入金额--state!=3
    private BigDecimal fadInvoiceMoneyU;// 合同未到发票额
    private BigDecimal scmPaidMoney;// 实际支付金额
    private BigDecimal scmUnpaidMoney;// 合同未付金额
    private BigDecimal scmLockedMoney;// 占用金额
    private BigDecimal scmUnlockedMoney;// 合同占用剩余金额
    private BigDecimal scmNeedPayMoney;// 应付账款
    private BigDecimal checkedMoney;// 合同到货确认金额
    private String supplierBlack;// 该供应商是否在黑名单中
    private String subjectCodeQ;// 课题代号
    private String subjectCode;
    private String subjectName;// 课题代号
    private BigDecimal budgetAmount;
    private BigDecimal expectedAmount;
    private String fadSubjectCode;
    private String taxTypes;
    private String levelTypes;
    private String supplierGenre;
    private String stateDesc;

    private String stampProjectCode;

    public String getStampProjectCode() {
        return stampProjectCode;
    }

    public void setStampProjectCode(String stampProjectCode) {
        this.stampProjectCode = stampProjectCode;
    }

    public void setTaxTypes(String taxTypes) {
        this.taxTypes = taxTypes;
    }

    public void setLevelTypes(String levelTypes) {
        this.levelTypes = levelTypes;
    }

    public void setSupplierGenre(String supplierGenre) {
        this.supplierGenre = supplierGenre;
    }


    public String getSupplierGenre() {
        return supplierGenre;
    }

    public String getTaxTypes() {
        return taxTypes;
    }

    public String getLevelTypes() {
        return levelTypes;
    }


    public BigDecimal getExpectedAmount() {
        return expectedAmount;
    }

    public void setExpectedAmount(BigDecimal expectedAmount) {
        this.expectedAmount = expectedAmount;
    }

    public void setBudgetAmount(BigDecimal budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public BigDecimal getBudgetAmount() {
        return budgetAmount;
    }

    public List<ScmContractPaytypeDto> getScmContractPaytypeDto() {
        return scmContractPaytypeDto;
    }

    public void setScmContractPaytypeDto(List<ScmContractPaytypeDto> childDto) {
        this.scmContractPaytypeDto = childDto;
    }

    public List<PrmPurchaseReqDetailDto> getPrmPurchaseReqDetailDto() {
        return prmPurchaseReqDetailDto;
    }

    public void setPrmPurchaseReqDetailDto(List<PrmPurchaseReqDetailDto> childDto) {
        this.prmPurchaseReqDetailDto = childDto;
    }

    public List<ScmContractDetailDto> getScmContractDetailDto() {
        return scmContractDetailDto;
    }

    public void setScmContractDetailDto(List<ScmContractDetailDto> childDto) {
        this.scmContractDetailDto = childDto;
    }

    public List<ScmContractChangeDto> getScmContractChangeDto() {
        return scmContractChangeDto;
    }

    public void setScmContractChangeDto(List<ScmContractChangeDto> childDto) {
        this.scmContractChangeDto = childDto;
    }

    public List<CdmFileRelationDto> getCdmFileRelationDto() {
        return cdmFileRelationDto;
    }

    public void setCdmFileRelationDto(List<CdmFileRelationDto> childDto) {
        this.cdmFileRelationDto = childDto;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public String getDebterDepartment() {
        return debterDepartment;
    }

    @Override
    public void setDebterDepartment(String debterDepartment) {
        this.debterDepartment = debterDepartment;
    }

    public String getContractNatureName() {
        return contractNatureName;
    }

    public void setContractNatureName(String contractNatureName) {
        this.contractNatureName = contractNatureName;
    }

    public String getContractStateName() {
        return contractStateName;
    }

    public void setContractStateName(String contractStateName) {
        this.contractStateName = contractStateName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public BigDecimal getFadInvoiceMoney() {
        return fadInvoiceMoney;
    }

    public void setFadInvoiceMoney(BigDecimal fadInvoiceMoney) {
        this.fadInvoiceMoney = fadInvoiceMoney;
    }

    public BigDecimal getFadInvoiceMoneyL() {
        return fadInvoiceMoneyL;
    }

    public void setFadInvoiceMoneyL(BigDecimal fadInvoiceMoneyL) {
        this.fadInvoiceMoneyL = fadInvoiceMoneyL;
    }

    public BigDecimal getFadInvoiceMoneyU() {
        return fadInvoiceMoneyU;
    }

    public void setFadInvoiceMoneyU(BigDecimal fadInvoiceMoneyU) {
        this.fadInvoiceMoneyU = fadInvoiceMoneyU;
    }

    public BigDecimal getScmPaidMoney() {
        return scmPaidMoney;
    }

    public void setScmPaidMoney(BigDecimal scmPaidMoney) {
        this.scmPaidMoney = scmPaidMoney;
    }

    public BigDecimal getScmUnpaidMoney() {
        return scmUnpaidMoney;
    }

    public void setScmUnpaidMoney(BigDecimal scmUnpaidMoney) {
        this.scmUnpaidMoney = scmUnpaidMoney;
    }

    public BigDecimal getScmLockedMoney() {
        return scmLockedMoney;
    }

    public void setScmLockedMoney(BigDecimal scmLockedMoney) {
        this.scmLockedMoney = scmLockedMoney;
    }

    public BigDecimal getScmUnlockedMoney() {
        return scmUnlockedMoney;
    }

    public void setScmUnlockedMoney(BigDecimal scmUnlockedMoney) {
        this.scmUnlockedMoney = scmUnlockedMoney;
    }

    public BigDecimal getScmNeedPayMoney() {
        return scmNeedPayMoney;
    }

    public void setScmNeedPayMoney(BigDecimal scmNeedPayMoney) {
        this.scmNeedPayMoney = scmNeedPayMoney;
    }

    public BigDecimal getCheckedMoney() {
        return checkedMoney;
    }

    public void setCheckedMoney(BigDecimal checkedMoney) {
        this.checkedMoney = checkedMoney;
    }

    public String getSupplierBlack() {
        return supplierBlack;
    }

    public void setSupplierBlack(String supplierBlack) {
        this.supplierBlack = supplierBlack;
    }

    public String getSubjectCodeQ() {
        return subjectCodeQ;
    }

    public void setSubjectCodeQ(String subjectCodeQ) {
        this.subjectCodeQ = subjectCodeQ;
    }


    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }


    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getFadSubjectCode() {
        return fadSubjectCode;
    }

    public void setFadSubjectCode(String fadSubjectCode) {
        this.fadSubjectCode = fadSubjectCode;
    }

    public String getStateDesc() {
        return stateDesc;
    }

    public void setStateDesc(String stateDesc) {
        this.stateDesc = stateDesc;
    }
}