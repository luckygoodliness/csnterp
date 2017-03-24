package com.csnt.scdp.bizmodules.modules.scm.purchasereq.dto;

import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.scm.ScmPurchaseReq;
import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.dto.PrmPurchaseReqDetailDto;

import java.util.List;


/**
 * Description:  ScmPurchaseReqDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-10-14 13:46:18
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.scm.ScmPurchaseReq")
public class ScmPurchaseReqDto extends ScmPurchaseReq {
    @CascadeChilds(FK = "scmPurchaseReqId|uuid")
    private List<PrmPurchaseReqDetailDto> prmPurchaseReqDetailDto;
    private String orgName;
    private String projectName;
    private String stampCount;

    public List<PrmPurchaseReqDetailDto> getPrmPurchaseReqDetailDto() {
        return prmPurchaseReqDetailDto;
    }

    public void setPrmPurchaseReqDetailDto(List<PrmPurchaseReqDetailDto> childDto) {
        this.prmPurchaseReqDetailDto = childDto;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getStampCount() {
        return stampCount;
    }

    public void setStampCount(String stampCount) {
        this.stampCount = stampCount;
    }
}