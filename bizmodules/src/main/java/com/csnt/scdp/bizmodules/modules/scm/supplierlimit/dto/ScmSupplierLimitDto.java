package com.csnt.scdp.bizmodules.modules.scm.supplierlimit.dto;

import com.csnt.scdp.bizmodules.entity.scm.ScmSupplierLimit;
import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;

import java.util.List;


/**
 * Description:  ScmSupplierLimitDto
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2016-07-19 15:14:12
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.scm.ScmSupplierLimit")
public class ScmSupplierLimitDto extends ScmSupplierLimit {
	@CascadeChilds(FK = "scmSupplierLimitId|uuid")
	private List<ScmSupplierLimitDetailDto> scmSupplierLimitDetailDto;



	public List<ScmSupplierLimitDetailDto> getScmSupplierLimitDetailDto() {
		return scmSupplierLimitDetailDto;
	}

	public void setScmSupplierLimitDetailDto(List<ScmSupplierLimitDetailDto> childDto) {
		this.scmSupplierLimitDetailDto = childDto;
	}

}