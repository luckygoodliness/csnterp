package com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto;

import com.csnt.scdp.bizmodules.entity.prm.PrmContractDetailC;
import com.csnt.scdp.framework.dto.PojoMapping;

import java.math.BigDecimal;


/**
 * Description:  PrmMemberDetailPCDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-24 15:08:30
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.prm.PrmContractDetailC")
public class PrmContractDetailCDto extends PrmContractDetailC {
    private String contractName;
    private String customerName;
    private BigDecimal contractSignMoney;
    private Integer isBusinessTax;
    private String taxType;
    private String prmCodeType;
    private String innerPurchaseReqId;
    private String innerProjectCode;

    private String changeStatus;
    private String changedFields;
    private Integer isRevised;

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getContractSignMoney() {
        return contractSignMoney;
    }

    public void setContractSignMoney(BigDecimal contractSignMoney) {
        this.contractSignMoney = contractSignMoney;
    }

    public Integer getIsBusinessTax() {
        return isBusinessTax;
    }

    public void setIsBusinessTax(Integer isBusinessTax) {
        this.isBusinessTax = isBusinessTax;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public String getPrmCodeType() {
        return prmCodeType;
    }

    public void setPrmCodeType(String prmCodeType) {
        this.prmCodeType = prmCodeType;
    }

    public String getChangeStatus() {
        return changeStatus;
    }

    public void setChangeStatus(String changeStatus) {
        this.changeStatus = changeStatus;
    }

    public String getChangedFields() {
        return changedFields;
    }

    public void setChangedFields(String changedFields) {
        this.changedFields = changedFields;
    }

    public Integer getIsRevised() {
        return isRevised;
    }

    public void setIsRevised(Integer isRevised) {
        this.isRevised = isRevised;
    }

    public String getInnerPurchaseReqId() {
        return innerPurchaseReqId;
    }

    public void setInnerPurchaseReqId(String innerPurchaseReqId) {
        this.innerPurchaseReqId = innerPurchaseReqId;
    }
    public String getInnerProjectCode() {
        return innerProjectCode;
    }

    public void setInnerProjectCode(String innerProjectCode) {
        this.innerProjectCode = innerProjectCode;
    }
}