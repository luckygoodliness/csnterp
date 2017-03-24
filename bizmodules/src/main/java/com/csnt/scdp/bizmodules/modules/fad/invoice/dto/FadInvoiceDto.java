package com.csnt.scdp.bizmodules.modules.fad.invoice.dto;

import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.fad.FadInvoice;

import java.math.BigDecimal;
import java.util.List;


/**
 * Description:  FadInvoiceDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-27 14:55:38
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.fad.FadInvoice")
public class FadInvoiceDto extends FadInvoice {
    @CascadeChilds(FK = "fadInvoiceId|uuid")
    private List<FadCashReqInvoiceDto> fadCashReqInvoiceDto;

    @CascadeChilds(FK = "fadInvoiceId|uuid")
    private List<FadInvoiceSubsidyDto> fadInvoiceSubsidyDto;

    @CascadeChilds(FK = "fadInvoiceId|uuid")
    private List<FadInvoiceTravelDto> fadInvoiceTravelDto;

    @CascadeChilds(FK = "fadInvoiceId|uuid")
    private List<ScmContractInvoiceDto> scmContractInvoiceDto;

    @CascadeChilds(FK = "fadInvoiceId|uuid")
    private List<FadInvoiceMaterialDto> fadInvoiceMaterialDto;

    public List<FadInvoiceMaterialDto> getFadInvoiceMaterialDto() {
        return fadInvoiceMaterialDto;
    }
    private BigDecimal payCash;// 报现金额

    public BigDecimal getPayCash() {
        return payCash;
    }

    public void setPayCash(BigDecimal payCash) {
        this.payCash = payCash;
    }

    public void setFadInvoiceMaterialDto(List<FadInvoiceMaterialDto> childDto) {
        this.fadInvoiceMaterialDto = childDto;
    }
    public List<FadCashReqInvoiceDto> getFadCashReqInvoiceDto() {
        return fadCashReqInvoiceDto;
    }

    public void setFadCashReqInvoiceDto(List<FadCashReqInvoiceDto> childDto) {
        this.fadCashReqInvoiceDto = childDto;
    }

    public List<FadInvoiceSubsidyDto> getFadInvoiceSubsidyDto() {
        return fadInvoiceSubsidyDto;
    }

    public void setFadInvoiceSubsidyDto(List<FadInvoiceSubsidyDto> childDto) {
        this.fadInvoiceSubsidyDto = childDto;
    }

    public List<FadInvoiceTravelDto> getFadInvoiceTravelDto() {
        return fadInvoiceTravelDto;
    }

    public void setFadInvoiceTravelDto(List<FadInvoiceTravelDto> childDto) {
        this.fadInvoiceTravelDto = childDto;
    }

    public List<ScmContractInvoiceDto> getScmContractInvoiceDto() {
        return scmContractInvoiceDto;
    }

    public void setScmContractInvoiceDto(List<ScmContractInvoiceDto> childDto) {
        this.scmContractInvoiceDto = childDto;
    }
}