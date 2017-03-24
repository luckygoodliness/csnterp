package com.csnt.scdp.bizmodules.modules.scm.requestallot.dto;

import com.csnt.scdp.bizmodules.entity.scm.ScmPurchaseReq;
import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.dto.PrmPurchaseReqDetailDto;
import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;

import java.util.List;


/**
 * Description:  ScmPurchaseReqDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-10-22 22:18:10
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.scm.ScmPurchaseReq")
public class ScmPurchaseReqDto extends ScmPurchaseReq {
    @CascadeChilds(FK = "scmPurchaseReqId|uuid")
    private List<PrmPurchaseReqDetailDto> prmPurchaseReqDetailDto;

    public List<PrmPurchaseReqDetailDto> getPrmPurchaseReqDetailDto() {
        return prmPurchaseReqDetailDto;
    }

    public void setPrmPurchaseReqDetailDto(List<PrmPurchaseReqDetailDto> prmPurchaseReqDetailDto) {
        this.prmPurchaseReqDetailDto = prmPurchaseReqDetailDto;
    }
}