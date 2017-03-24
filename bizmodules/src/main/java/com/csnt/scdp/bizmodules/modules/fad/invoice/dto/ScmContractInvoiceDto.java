package com.csnt.scdp.bizmodules.modules.fad.invoice.dto;

import com.csnt.scdp.bizmodules.entity.scm.ScmContractInvoice;
import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;

import java.math.BigDecimal;
import java.util.List;


/**
 * Description:  ScmContractInvoiceDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-26 18:50:47
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.scm.ScmContractInvoice")
public class ScmContractInvoiceDto extends ScmContractInvoice {

//    @CascadeChilds(FK = "scmContractInvoiceId|uuid",hasSub = true)
//    private List<ScmInvoiceMaterialDto> scmInvoiceMaterialDto;

    private String scmContractCode;
    private BigDecimal contractTotalValue;

//    public List<ScmInvoiceMaterialDto> getScmInvoiceMaterialDto() {
//        return scmInvoiceMaterialDto;
//    }
//
//    public void setScmInvoiceMaterialDto(List<ScmInvoiceMaterialDto> childDto) {
//        this.scmInvoiceMaterialDto = childDto;
//    }

    public String getScmContractCode() {
        return scmContractCode;
    }

    public void setScmContractCode(String scmContractCode) {
        this.scmContractCode = scmContractCode;
    }

    public BigDecimal getContractTotalValue() {
        return contractTotalValue;
    }

    public void setContractTotalValue(BigDecimal contractTotalValue) {
        this.contractTotalValue = contractTotalValue;
    }
}