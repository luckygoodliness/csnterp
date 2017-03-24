package com.csnt.scdp.bizmodules.modules.fad.invoice.dto;

import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.fad.FadCashReqInvoice;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * Description:  FadCashReqInvoiceDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-27 14:55:38
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.fad.FadCashReqInvoice")
public class FadCashReqInvoiceDto extends FadCashReqInvoice {

    //发票号
    String invoiceNo;

    private BigDecimal cashReqMoney;// 原请款金额

    private Date ncDate;

    public Date getNcDate() {
        return ncDate;
    }

    public void setNcDate(Date ncDate) {
        this.ncDate = ncDate;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    //类型  0-发票核销，1-现金核销
    String clearanceTypeName;

    public String getClearanceTypeName() {
        return "0".equals(getClearanceType()) ? "发票核销" : "现金核销";
    }

    //发票核销或者现金核销流水号
    public String getInvoiceRunningNo() {
        return invoiceRunningNo;
    }

    public void setInvoiceRunningNo(String invoiceRunningNo) {
        this.invoiceRunningNo = invoiceRunningNo;
    }

    String invoiceRunningNo;

    public BigDecimal getCashReqMoney() {
        return cashReqMoney;
    }

    public void setCashReqMoney(BigDecimal cashReqMoney) {
        this.cashReqMoney = cashReqMoney;
    }
}