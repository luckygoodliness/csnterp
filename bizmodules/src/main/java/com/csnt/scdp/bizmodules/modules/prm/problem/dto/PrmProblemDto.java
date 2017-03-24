package com.csnt.scdp.bizmodules.modules.prm.problem.dto;

import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.prm.PrmProblem;

import java.util.List;


/**
 * Description:  PrmProblemDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-11-06 16:30:48
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.prm.PrmProblem")
public class PrmProblemDto extends PrmProblem {
	@CascadeChilds(FK = "prmProblemId|uuid")
	private List<PrmProblemFeedbackDto> prmProblemFeedbackDto;



	public List<PrmProblemFeedbackDto> getPrmProblemFeedbackDto() {
		return prmProblemFeedbackDto;
	}

	public void setPrmProblemFeedbackDto(List<PrmProblemFeedbackDto> childDto) {
		this.prmProblemFeedbackDto = childDto;
	}

}