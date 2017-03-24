package com.csnt.scdp.bizmodules.modules.scm.scmsaecase.dto;

import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.scm.ScmSaeCase;

import java.util.List;


/**
 * Description:  ScmSaeCaseDto
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2016-08-16 15:17:56
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.scm.ScmSaeCase")
public class ScmSaeCaseDto extends ScmSaeCase {
	@CascadeChilds(FK = "scmSaeCaseId|uuid")
	private List<ScmSaeCaseDDto> scmSaeCaseDDto;



	public List<ScmSaeCaseDDto> getScmSaeCaseDDto() {
		return scmSaeCaseDDto;
	}

	public void setScmSaeCaseDDto(List<ScmSaeCaseDDto> childDto) {
		this.scmSaeCaseDDto = childDto;
	}

}