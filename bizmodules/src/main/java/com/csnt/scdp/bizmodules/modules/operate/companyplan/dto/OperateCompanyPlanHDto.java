package com.csnt.scdp.bizmodules.modules.operate.companyplan.dto;

import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.operate.OperateCompanyPlanH;

import java.util.List;


/**
 * Description:  OperateCompanyPlanHDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-11-19 20:30:35
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.operate.OperateCompanyPlanH")
public class OperateCompanyPlanHDto extends OperateCompanyPlanH {
	@CascadeChilds(FK = "operateCompanyPlanHId|uuid")
	private List<OperateCompanyPlanCDto> operateCompanyPlanCDto;



	public List<OperateCompanyPlanCDto> getOperateCompanyPlanCDto() {
		return operateCompanyPlanCDto;
	}

	public void setOperateCompanyPlanCDto(List<OperateCompanyPlanCDto> childDto) {
		this.operateCompanyPlanCDto = childDto;
	}

}