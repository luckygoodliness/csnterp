package com.csnt.scdp.bizmodules.modules.prm.prmcustomer.dto;

import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.prm.PrmCustomer;

import java.util.List;


/**
 * Description:  PrmCustomerDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-29 10:33:22
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.prm.PrmCustomer")
public class PrmCustomerDto extends PrmCustomer {
	@CascadeChilds(FK = "prmCustomerId|uuid")
	private List<PrmCustomerLinkmanDto> prmCustomerLinkmanDto;

	@CascadeChilds(FK = "prmCustomerId|uuid")
	private List<PrmCustomerBankDto> prmCustomerBankDto;



	public List<PrmCustomerLinkmanDto> getPrmCustomerLinkmanDto() {
		return prmCustomerLinkmanDto;
	}

	public void setPrmCustomerLinkmanDto(List<PrmCustomerLinkmanDto> childDto) {
		this.prmCustomerLinkmanDto = childDto;
	}

	public List<PrmCustomerBankDto> getPrmCustomerBankDto() {
		return prmCustomerBankDto;
	}

	public void setPrmCustomerBankDto(List<PrmCustomerBankDto> childDto) {
		this.prmCustomerBankDto = childDto;
	}

}