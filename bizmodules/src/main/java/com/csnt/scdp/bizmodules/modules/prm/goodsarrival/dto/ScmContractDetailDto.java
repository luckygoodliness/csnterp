package com.csnt.scdp.bizmodules.modules.prm.goodsarrival.dto;

import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.scm.ScmContractDetail;

import java.util.List;


/**
 * Description:  ScmContractDetailDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-10-15 12:30:05
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.scm.ScmContractDetail")
public class ScmContractDetailDto extends ScmContractDetail {
	@CascadeChilds(FK = "scmContractDetailId|uuid")
	private List<PrmGoodsArrivalDto> prmGoodsArrivalDto;



	public List<PrmGoodsArrivalDto> getPrmGoodsArrivalDto() {
		return prmGoodsArrivalDto;
	}

	public void setPrmGoodsArrivalDto(List<PrmGoodsArrivalDto> childDto) {
		this.prmGoodsArrivalDto = childDto;
	}

}