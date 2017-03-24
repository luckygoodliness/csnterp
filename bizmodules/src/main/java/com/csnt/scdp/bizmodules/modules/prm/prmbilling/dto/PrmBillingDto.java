package com.csnt.scdp.bizmodules.modules.prm.prmbilling.dto;

import com.csnt.scdp.bizmodules.modules.cdm.cdmfile.dto.CdmFileRelationDto;
import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.prm.PrmBilling;

import java.util.List;


/**
 * Description:  PrmBillingDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-26 17:35:07
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.prm.PrmBilling")
public class PrmBillingDto extends PrmBilling {
	@CascadeChilds(FK = "prmBillingId|uuid")
	private List<PrmBillingDetailDto> prmBillingDetailDto;

	@CascadeChilds(FK = "dataId|uuid")
	private List<CdmFileRelationDto> cdmFileRelationDto;


	public List<CdmFileRelationDto> getCdmFileRelationDto() {
		return cdmFileRelationDto;
	}

	public void setCdmFileRelationDto(List<CdmFileRelationDto> cdmFileRelationDto) {
		this.cdmFileRelationDto = cdmFileRelationDto;
	}

	public List<PrmBillingDetailDto> getPrmBillingDetailDto() {
		return prmBillingDetailDto;
	}

	public void setPrmBillingDetailDto(List<PrmBillingDetailDto> childDto) {
		this.prmBillingDetailDto = childDto;
	}

}