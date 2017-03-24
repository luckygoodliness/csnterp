package com.csnt.scdp.bizmodules.modules.fad.cashclearance.dto;

import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.fad.FadCashClearance;

import java.util.List;


/**
 * Description:  FadCashClearanceDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-11-08 18:01:30
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.fad.FadCashClearance")
public class FadCashClearanceDto extends FadCashClearance {
	@CascadeChilds(FK = "fadInvoiceId|uuid")
	private List<FadCashReqInvoiceDto> fadCashReqInvoiceDto;



	public List<FadCashReqInvoiceDto> getFadCashReqInvoiceDto() {
		return fadCashReqInvoiceDto;
	}

	public void setFadCashReqInvoiceDto(List<FadCashReqInvoiceDto> childDto) {
		this.fadCashReqInvoiceDto = childDto;
	}

}