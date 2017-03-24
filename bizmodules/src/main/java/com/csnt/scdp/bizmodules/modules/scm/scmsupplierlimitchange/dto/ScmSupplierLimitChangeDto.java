package com.csnt.scdp.bizmodules.modules.scm.scmsupplierlimitchange.dto;

import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.scm.ScmSupplierLimitChange;

import java.util.List;


/**
 * Description:  ScmSupplierLimitChangeDto
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2016-07-22 16:17:07
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.scm.ScmSupplierLimitChange")
public class ScmSupplierLimitChangeDto extends ScmSupplierLimitChange {
	@CascadeChilds(FK = "scmSupplierLimitChangeId|uuid")
	private List<ScmSupplierLimitChangeDDto> scmSupplierLimitChangeDDto;



	public List<ScmSupplierLimitChangeDDto> getScmSupplierLimitChangeDDto() {
		return scmSupplierLimitChangeDDto;
	}

	public void setScmSupplierLimitChangeDDto(List<ScmSupplierLimitChangeDDto> childDto) {
		this.scmSupplierLimitChangeDDto = childDto;
	}

}