package com.csnt.scdp.bizmodules.modules.prm.archiving.dto;

import com.csnt.scdp.bizmodules.modules.cdm.cdmfile.dto.CdmFileRelationDto;
import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.prm.PrmArchiving;

import java.util.List;


/**
 * Description:  PrmArchivingDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-24 15:45:09
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.prm.PrmArchiving")
public class PrmArchivingDto extends PrmArchiving {
	@CascadeChilds(FK = "prmArchivingId|uuid")
	private List<PrmArchivingDetailDto> prmArchivingDetailDto;

    @CascadeChilds(FK = "dataId|uuid")
    private List<CdmFileRelationDto> cdmFileRelationDto;

    public List<CdmFileRelationDto> getCdmFileRelationDto() {
        return cdmFileRelationDto;
    }

    public void setCdmFileRelationDto(List<CdmFileRelationDto> cdmFileRelationDto) {
        this.cdmFileRelationDto = cdmFileRelationDto;
    }

	public List<PrmArchivingDetailDto> getPrmArchivingDetailDto() {
		return prmArchivingDetailDto;
	}

	public void setPrmArchivingDetailDto(List<PrmArchivingDetailDto> childDto) {
		this.prmArchivingDetailDto = childDto;
	}

}