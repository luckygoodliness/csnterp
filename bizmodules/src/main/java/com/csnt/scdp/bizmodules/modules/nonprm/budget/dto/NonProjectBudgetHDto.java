package com.csnt.scdp.bizmodules.modules.nonprm.budget.dto;

import com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.dto.NonProjectBudgetAjustCDto;
import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetH;

import java.util.List;


/**
 * Description:  NonProjectBudgetHDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-24 13:22:12
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetH")
public class NonProjectBudgetHDto extends NonProjectBudgetH {
    @CascadeChilds(FK = "hid|uuid")
    private List<NonProjectBudgetCDto> nonProjectBudgetCDto;

    public List<NonProjectBudgetCDto> getNonProjectBudgetCDto() {
        return nonProjectBudgetCDto;
    }

    public void setNonProjectBudgetCDto(List<NonProjectBudgetCDto> childDto) {
        this.nonProjectBudgetCDto = childDto;
    }

    private List<NonProjectBudgetCDDto> nonProjectBudgetCDDto;

    public List<NonProjectBudgetCDDto> getNonProjectBudgetCDDto() {
        return nonProjectBudgetCDDto;
    }

    public void setNonProjectBudgetCDDto(List<NonProjectBudgetCDDto> nonProjectBudgetCDDto) {
        this.nonProjectBudgetCDDto = nonProjectBudgetCDDto;
    }

    private List<NonProjectBudgetCDDto> nonProjectBudgetCD2Dto;

    public List<NonProjectBudgetCDDto> getNonProjectBudgetCD2Dto() {
        return nonProjectBudgetCD2Dto;
    }

    public void setNonProjectBudgetCD2Dto(List<NonProjectBudgetCDDto> nonProjectBudgetCD2Dto) {
        this.nonProjectBudgetCD2Dto = nonProjectBudgetCD2Dto;
    }
}