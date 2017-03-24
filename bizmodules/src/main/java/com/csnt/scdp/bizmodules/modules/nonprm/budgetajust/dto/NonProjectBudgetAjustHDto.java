package com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.dto;

import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetAjustH;

import java.util.List;


/**
 * Description:  NonProjectBudgetAjustHDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-25 16:43:12
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetAjustH")
public class NonProjectBudgetAjustHDto extends NonProjectBudgetAjustH {


	@CascadeChilds(FK = "hid|uuid")
	private List<NonProjectBudgetAjustCDto> nonProjectBudgetAjustCDto;

	public List<NonProjectBudgetAjustCDto> getNonProjectBudgetAjustCDto() {
		return nonProjectBudgetAjustCDto;
	}

	public void setNonProjectBudgetAjustCDto(List<NonProjectBudgetAjustCDto> childDto) {
		this.nonProjectBudgetAjustCDto = childDto;
	}

    private List<NonProjectBudgetAjustCDDto> nonProjectBudgetAjustCDDto;

    public List<NonProjectBudgetAjustCDDto> getNonProjectBudgetAjustCDDto() {
        return nonProjectBudgetAjustCDDto;
    }

    public void setNonProjectBudgetAjustCDDto(List<NonProjectBudgetAjustCDDto> nonProjectBudgetAjustCDDto) {
        this.nonProjectBudgetAjustCDDto = nonProjectBudgetAjustCDDto;
    }

    private List<NonProjectBudgetAjustCDDto> nonProjectBudgetAjustCD2Dto;

    public List<NonProjectBudgetAjustCDDto> getNonProjectBudgetAjustCD2Dto() {
        return nonProjectBudgetAjustCD2Dto;
    }

    public void setNonProjectBudgetAjustCD2Dto(List<NonProjectBudgetAjustCDDto> nonProjectBudgetAjustCD2Dto) {
        this.nonProjectBudgetAjustCD2Dto = nonProjectBudgetAjustCD2Dto;
    }

}