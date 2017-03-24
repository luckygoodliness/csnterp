package com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.dto;

import com.csnt.scdp.bizmodules.modules.cdm.cdmfile.dto.CdmFileRelationDto;
import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.prm.PrmPurchaseReq;

import java.util.List;


/**
 * Description:  PrmPurchaseReqDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-26 15:30:52
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.prm.PrmPurchaseReq")
public class PrmPurchaseReqDto extends PrmPurchaseReq {
    @CascadeChilds(FK = "prmPurchaseReqId|uuid")
    private List<PrmPurchaseReqDetailDto> prmPurchaseReqDetailDto;

    public List<PrmPurchaseReqDetailDto> getPrmPurchaseReqDetailDto() {
        return prmPurchaseReqDetailDto;
    }

    public void setPrmPurchaseReqDetailDto(List<PrmPurchaseReqDetailDto> childDto) {
        this.prmPurchaseReqDetailDto = childDto;
    }

    @CascadeChilds(FK = "dataId|uuid")
    private List<CdmFileRelationDto> cdmFileRelationDto;

    public List<CdmFileRelationDto> getCdmFileRelationDto() {
        return cdmFileRelationDto;
    }

    public void setCdmFileRelationDto(List<CdmFileRelationDto> childDto) {
        this.cdmFileRelationDto = childDto;
    }


    private String projectName = "";

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    private String orgName = "";

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getStateNm() {
        return stateNm;
    }

    public void setStateNm(String stateNm) {
        this.stateNm = stateNm;
    }

    private String stateNm = "";


}