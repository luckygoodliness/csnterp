package com.csnt.scdp.bizmodules.modules.scm.scmsaetask.dto;

import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.scm.ScmSaeTask;

import java.util.List;


/**
 * Description:  ScmSaeTaskDto
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2016-08-26 09:56:50
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.scm.ScmSaeTask")
public class ScmSaeTaskDto extends ScmSaeTask {
	@CascadeChilds(FK = "scmSaeTaskId|uuid")
	private List<ScmSaeFormDto> scmSaeFormDto;



	public List<ScmSaeFormDto> getScmSaeFormDto() {
		return scmSaeFormDto;
	}

	public void setScmSaeFormDto(List<ScmSaeFormDto> childDto) {
		this.scmSaeFormDto = childDto;
	}

}