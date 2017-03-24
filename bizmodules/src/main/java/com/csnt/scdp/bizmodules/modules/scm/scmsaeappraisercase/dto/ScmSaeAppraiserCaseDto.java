package com.csnt.scdp.bizmodules.modules.scm.scmsaeappraisercase.dto;

import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.scm.ScmSaeAppraiserCase;

import java.util.List;


/**
 * Description:  ScmSaeAppraiserCaseDto
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2016-11-02 09:56:31
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.scm.ScmSaeAppraiserCase")
public class ScmSaeAppraiserCaseDto extends ScmSaeAppraiserCase {
	@CascadeChilds(FK = "scmSaeAppraiserCaseId|uuid")
	private List<ScmSaeAppraiserCaseDDto> scmSaeAppraiserCaseDDto;

	public List<ScmSaeAppraiserCaseDDto> getScmSaeAppraiserCaseDDto() {
		return scmSaeAppraiserCaseDDto;
	}

	public void setScmSaeAppraiserCaseDDto(List<ScmSaeAppraiserCaseDDto> childDto) {
		this.scmSaeAppraiserCaseDDto = childDto;
	}

}