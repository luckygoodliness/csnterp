package com.csnt.scdp.bizmodules.modules.scm.scmsaeapproval.dto;

import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.scm.ScmSaeApproval;

import java.util.List;


/**
 * Description:  ScmSaeApprovalDto
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2016-08-30 19:12:22
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.scm.ScmSaeApproval")
public class ScmSaeApprovalDto extends ScmSaeApproval {
	@CascadeChilds(FK = "scmSaeApprovalId|uuid")
	private List<ScmSaeApprovalDDto> scmSaeApprovalDDto;



	public List<ScmSaeApprovalDDto> getScmSaeApprovalDDto() {
		return scmSaeApprovalDDto;
	}

	public void setScmSaeApprovalDDto(List<ScmSaeApprovalDDto> childDto) {
		this.scmSaeApprovalDDto = childDto;
	}

}