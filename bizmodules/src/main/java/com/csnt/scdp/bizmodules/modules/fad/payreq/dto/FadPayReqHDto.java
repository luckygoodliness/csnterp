package com.csnt.scdp.bizmodules.modules.fad.payreq.dto;

import com.csnt.scdp.bizmodules.modules.cdm.cdmfile.dto.CdmFileRelationDto;
import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.fad.FadPayReqH;

import java.util.ArrayList;
import java.util.List;


/**
 * Description:  FadPayReqHDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-26 17:42:25
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.fad.FadPayReqH")
public class FadPayReqHDto extends FadPayReqH {
    @CascadeChilds(FK = "puuid|uuid")
    private List<FadPayReqCDto> fadPayReqCDto;

    public List<FadPayReqCDto> getFadPayReqCDto() {
        return fadPayReqCDto;
    }

    public void setFadPayReqCDto(List<FadPayReqCDto> childDto) {
        this.fadPayReqCDto = childDto;
    }

    @CascadeChilds(FK = "dataId|uuid")
    private List<CdmFileRelationDto> cdmFileRelationDto;

    public List<CdmFileRelationDto> getCdmFileRelationDto() {
        return cdmFileRelationDto;
    }

    public void setCdmFileRelationDto(List<CdmFileRelationDto> cdmFileRelationDto) {
        this.cdmFileRelationDto = cdmFileRelationDto;
    }

}