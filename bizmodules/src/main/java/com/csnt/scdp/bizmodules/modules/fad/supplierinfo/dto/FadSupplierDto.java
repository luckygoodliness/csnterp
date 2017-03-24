package com.csnt.scdp.bizmodules.modules.fad.supplierinfo.dto;

import com.csnt.scdp.bizmodules.entity.scm.ScmSupplier;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.dto.ScmSupplierBankDto;
import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;

import java.util.List;


/**
 * Description:  FadSupplierDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-12-10 13:50:35
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.scm.ScmSupplier")
public class FadSupplierDto extends ScmSupplier {
	@CascadeChilds(FK = "scmSupplierId|uuid")
	private List<ScmSupplierBankDto> scmSupplierBankDto;

	public List<ScmSupplierBankDto> getScmSupplierBankDto() {
		return scmSupplierBankDto;
	}

	public void setScmSupplierBankDto(List<ScmSupplierBankDto> childDto) {
		this.scmSupplierBankDto = childDto;
	}

}