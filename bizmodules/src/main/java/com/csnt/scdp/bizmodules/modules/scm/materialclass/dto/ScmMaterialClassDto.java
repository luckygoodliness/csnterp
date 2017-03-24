package com.csnt.scdp.bizmodules.modules.scm.materialclass.dto;

import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.scm.ScmMaterialClass;

import java.util.List;


/**
 * Description:  ScmMaterialClassDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-23 11:15:17
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.scm.ScmMaterialClass")
public class ScmMaterialClassDto extends ScmMaterialClass {
	@CascadeChilds(FK = "scmMaterialClassId|uuid")
	private List<ScmMaterialKeyDto> scmMaterialKeyDto;



	public List<ScmMaterialKeyDto> getScmMaterialKeyDto() {
		return scmMaterialKeyDto;
	}

	public void setScmMaterialKeyDto(List<ScmMaterialKeyDto> childDto) {
		this.scmMaterialKeyDto = childDto;
	}

}