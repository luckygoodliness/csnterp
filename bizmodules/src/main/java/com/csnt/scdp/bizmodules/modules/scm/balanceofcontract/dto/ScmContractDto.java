package com.csnt.scdp.bizmodules.modules.scm.balanceofcontract.dto;

import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.scm.ScmContract;

import java.util.List;


/**
 * Description:  ScmContractDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-29 09:17:26
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.scm.ScmContract")
public class ScmContractDto extends ScmContract {
	@CascadeChilds(FK = "scmContractId|uuid")
	private List<ScmContractPayDto> scmContractPayDto;

	@CascadeChilds(FK = "scmContractId|uuid")
	private List<ScmContractInvoiceDto> scmContractInvoiceDto;



	public List<ScmContractPayDto> getScmContractPayDto() {
		return scmContractPayDto;
	}

	public void setScmContractPayDto(List<ScmContractPayDto> childDto) {
		this.scmContractPayDto = childDto;
	}

	public List<ScmContractInvoiceDto> getScmContractInvoiceDto() {
		return scmContractInvoiceDto;
	}

	public void setScmContractInvoiceDto(List<ScmContractInvoiceDto> childDto) {
		this.scmContractInvoiceDto = childDto;
	}

}