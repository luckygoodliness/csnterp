package com.csnt.scdp.bizmodules.modules.fad.cashreq.dto;

import com.csnt.scdp.bizmodules.entity.fad.FadCashReq;
import com.csnt.scdp.bizmodules.modules.cdm.cdmfile.dto.CdmFileRelationDto;
import com.csnt.scdp.bizmodules.modules.fad.invoice.dto.FadCashReqInvoiceDto;
import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;

import java.math.BigDecimal;
import java.util.List;


/**
 * Description:  FadCashReqDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-22 09:40:50
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.fad.FadCashReq")
public class FadCashReqDto extends FadCashReq {

    @CascadeChilds(FK = "fadCashReqId|uuid", cascadeDelete = false)
    private List<FadCashReqInvoiceDto> fadCashReqInvoiceDto;
    @CascadeChilds(FK = "fadCashReqUuid|uuid")
    private List<FadCashReqShareDto> fadCashReqShareDto;
    @CascadeChilds(FK = "fadCashReqUuid|uuid")
    private List<FadCashReqBudgetDto> fadCashReqBudgetDto;
    @CascadeChilds(FK = "dataId|uuid")
    private List<CdmFileRelationDto> cdmFileRelationDto;
    //用于界面显示合同
    private String purchaseContractIdPlus;
    //用于界面显示用户
    private String staffIdDesc;
    private String officeIdDesc;
    private String budgetDesc;
    private String prmContractorOffice;
    private String fadPayReqHUuid;
    private String payStyleDesc;
    private String stateDesc;
    //M7_C5_F2_逾期利息
    private BigDecimal totalOverdueInterest;
    public BigDecimal getTotalOverdueInterest(){
        return totalOverdueInterest;
    }
    public void setTotalOverdueInterest(BigDecimal totalOverdueInterest){
        this.totalOverdueInterest=totalOverdueInterest;
    }
    public String getFadPayReqHUuid() {
        return fadPayReqHUuid;
    }

    public void setFadPayReqHUuid(String fadPayReqHUuid) {
        this.fadPayReqHUuid = fadPayReqHUuid;
    }

    public String getOperateBusinessBidInfoIdDesc() {
        return operateBusinessBidInfoIdDesc;
    }

    private String operateBusinessBidInfoIdDesc;

    public void setOperateBusinessBidInfoIdDesc(String operateBusinessBidInfoIdDesc) {
        this.operateBusinessBidInfoIdDesc = operateBusinessBidInfoIdDesc;
    }

    private String fileId;
    private String attachmentFileName;

    public List<FadCashReqInvoiceDto> getFadCashReqInvoiceDto() {
        return fadCashReqInvoiceDto;
    }

    public void setFadCashReqInvoiceDto(List<FadCashReqInvoiceDto> childDto) {
        this.fadCashReqInvoiceDto = childDto;
    }

    public List<FadCashReqBudgetDto> getFadCashReqBudgetDto() {
        return fadCashReqBudgetDto;
    }

    public void setFadCashReqBudgetDto(List<FadCashReqBudgetDto> childDto) {
        this.fadCashReqBudgetDto = childDto;
    }

    public List<FadCashReqShareDto> getFadCashReqShareDto() {
        return fadCashReqShareDto;
    }

    public void setFadCashReqShareDto(List<FadCashReqShareDto> fadCashReqShareDto) {
        this.fadCashReqShareDto = fadCashReqShareDto;
    }

    public List<CdmFileRelationDto> getCdmFileRelationDto() {
        return cdmFileRelationDto;
    }

    public void setCdmFileRelationDto(List<CdmFileRelationDto> cdmFileRelationDto) {
        this.cdmFileRelationDto = cdmFileRelationDto;
    }

    public String getPurchaseContractIdPlus() {
        return purchaseContractIdPlus;
    }

    public void setPurchaseContractIdPlus(String purchaseContractIdPlus) {
        this.purchaseContractIdPlus = purchaseContractIdPlus;
    }

    public String getStaffIdDesc() {
        return staffIdDesc;
    }

    public void setStaffIdDesc(String staffIdDesc) {
        this.staffIdDesc = staffIdDesc;
    }

    public String getBudgetDesc() {
        return budgetDesc;
    }

    public void setBudgetDesc(String budgetDesc) {
        this.budgetDesc = budgetDesc;
    }

    public String getPrmContractorOffice() {
        return prmContractorOffice;
    }

    public void setPrmContractorOffice(String prmContractorOffice) {
        this.prmContractorOffice = prmContractorOffice;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getAttachmentFileName() {
        return attachmentFileName;
    }

    public void setAttachmentFileName(String attachmentFileName) {
        this.attachmentFileName = attachmentFileName;
    }

    public String getOfficeIdDesc() {
        return officeIdDesc;
    }

    public void setOfficeIdDesc(String officeIdDesc) {
        this.officeIdDesc = officeIdDesc;
    }


    public String getStateDesc() {
        return stateDesc;
    }

    public void setStateDesc(String stateDesc) {
        this.stateDesc = stateDesc;
    }


    public String getPayStyleDesc() {
        return payStyleDesc;
    }

    public void setPayStyleDesc(String payStyleDesc) {
        this.payStyleDesc = payStyleDesc;
    }

}