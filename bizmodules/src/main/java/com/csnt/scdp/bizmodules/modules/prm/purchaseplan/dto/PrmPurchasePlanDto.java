package com.csnt.scdp.bizmodules.modules.prm.purchaseplan.dto;

import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.modules.cdm.cdmfile.dto.CdmFileRelationDto;
import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;

import java.util.List;


/**
 * Description:  PrmPurchasePlanDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-23 19:04:01
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain")
public class PrmPurchasePlanDto extends PrmProjectMain {

    private String manager;
    private String customer;
    private String stateNm;
    private String prmProjectMainId;
    private String contractorOfficeDesc;
    @CascadeChilds(FK = "prmProjectMainId|uuid")
    private List<PrmPurchasePackageDto> prmPurchasePackageDto;
    @CascadeChilds(FK = "prmProjectMainId|uuid")
    private List<PrmPurchasePlanDetailDto> prmPurchasePlanDetailDto;
    @CascadeChilds(FK = "dataId|uuid")
    private List<CdmFileRelationDto> cdmFileRelationDto;

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public List<PrmPurchasePackageDto> getPrmPurchasePackageDto() {
        return prmPurchasePackageDto;
    }

    public void setPrmPurchasePackageDto(List<PrmPurchasePackageDto> prmPurchasePackageDto) {
        this.prmPurchasePackageDto = prmPurchasePackageDto;
    }

    public List<PrmPurchasePlanDetailDto> getPrmPurchasePlanDetailDto() {
        return prmPurchasePlanDetailDto;
    }

    public void setPrmPurchasePlanDetailDto(List<PrmPurchasePlanDetailDto> childDto) {
        this.prmPurchasePlanDetailDto = childDto;
    }
    public List<CdmFileRelationDto> getCdmFileRelationDto() {
        return cdmFileRelationDto;
    }

    public void setCdmFileRelationDto(List<CdmFileRelationDto> childDto) {
        this.cdmFileRelationDto = childDto;
    }

    public String getStateNm() {
        return stateNm;
    }

    public void setStateNm(String stateNm) {
        this.stateNm = stateNm;
    }
    public String getPrmProjectMainId() {
        return prmProjectMainId;
    }

    public void setPrmProjectMainId(String prmProjectMainId) {
        this.prmProjectMainId = prmProjectMainId;
    }

    public String getContractorOfficeDesc() {
        return contractorOfficeDesc;
    }

    public void setContractorOfficeDesc(String contractorOfficeDesc) {
        this.contractorOfficeDesc = contractorOfficeDesc;
    }
}