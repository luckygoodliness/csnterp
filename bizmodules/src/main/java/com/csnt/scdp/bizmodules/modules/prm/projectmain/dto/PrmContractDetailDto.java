package com.csnt.scdp.bizmodules.modules.prm.projectmain.dto;

import com.csnt.scdp.bizmodules.entity.prm.PrmContractDetail;
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
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.prm.PrmContractDetail")
public class PrmContractDetailDto extends PrmContractDetail {

    private String contractName;
    private String customerName;
    private BigDecimal contractSignMoney;
    private Integer isBusinessTax;
    private String taxType;
    private String prmCodeType;
    private String innerProjectCode;

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

    public String getInnerProjectCode() {
        return innerProjectCode;
    }

    public void setInnerProjectCode(String innerProjectCode) {
        this.innerProjectCode = innerProjectCode;
    }
}