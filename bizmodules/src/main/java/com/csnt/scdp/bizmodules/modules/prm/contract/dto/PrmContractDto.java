package com.csnt.scdp.bizmodules.modules.prm.contract.dto;

import com.csnt.scdp.bizmodules.entity.prm.PrmContract;
import com.csnt.scdp.bizmodules.modules.cdm.cdmfile.dto.CdmFileRelationDto;
import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;

import java.util.List;


/**
 * Description:  PrmContractDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-23 09:57:20
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.prm.PrmContract")
public class PrmContractDto extends PrmContract {
    @CascadeChilds(FK = "dataId|uuid")
    private List<CdmFileRelationDto> cdmFileRelationDto;

    private String operateBusinessBidInfoIdDesc;
    private String projectManagerDesc;
    private String generalEngineerDesc;
    private String customerIdDesc;
    private String designerIdDesc;
    private String managementIdDesc;
    private String innerPurchaseReqIdDesc;
    private String contractorOfficeDesc;

    public List<CdmFileRelationDto> getCdmFileRelationDto() {
        return cdmFileRelationDto;
    }

    public void setCdmFileRelationDto(List<CdmFileRelationDto> cdmFileRelationDto) {
        this.cdmFileRelationDto = cdmFileRelationDto;
    }

    public String getOperateBusinessBidInfoIdDesc() {
        return operateBusinessBidInfoIdDesc;
    }

    public void setOperateBusinessBidInfoIdDesc(String operateBusinessBidInfoIdDesc) {
        this.operateBusinessBidInfoIdDesc = operateBusinessBidInfoIdDesc;
    }

    public String getProjectManagerDesc() {
        return projectManagerDesc;
    }

    public void setProjectManagerDesc(String projectManagerDesc) {
        this.projectManagerDesc = projectManagerDesc;
    }

    public String getGeneralEngineerDesc() {
        return generalEngineerDesc;
    }

    public void setGeneralEngineerDesc(String generalEngineerDesc) {
        this.generalEngineerDesc = generalEngineerDesc;
    }

    public String getCustomerIdDesc() {
        return customerIdDesc;
    }

    public void setCustomerIdDesc(String customerIdDesc) {
        this.customerIdDesc = customerIdDesc;
    }

    public String getManagementIdDesc() {
        return managementIdDesc;
    }

    public void setManagementIdDesc(String managementIdDesc) {
        this.managementIdDesc = managementIdDesc;
    }

    public String getDesignerIdDesc() {
        return designerIdDesc;
    }

    public void setDesignerIdDesc(String designerIdDesc) {
        this.designerIdDesc = designerIdDesc;
    }

    public String getInnerPurchaseReqIdDesc() {
        return innerPurchaseReqIdDesc;
    }

    public void setInnerPurchaseReqIdDesc(String innerPurchaseReqIdDesc) {
        this.innerPurchaseReqIdDesc = innerPurchaseReqIdDesc;
    }

    public String getContractorOfficeDesc() {
        return contractorOfficeDesc;
    }

    public void setContractorOfficeDesc(String contractorOfficeDesc) {
        this.contractorOfficeDesc = contractorOfficeDesc;
    }
}