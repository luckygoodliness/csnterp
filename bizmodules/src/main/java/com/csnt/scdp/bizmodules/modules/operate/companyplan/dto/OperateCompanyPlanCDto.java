package com.csnt.scdp.bizmodules.modules.operate.companyplan.dto;

import com.csnt.scdp.bizmodules.entity.operate.OperateCompanyPlanC;
import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;

import java.util.List;


/**
 * Description:  OperateCompanyPlanDDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-11-19 20:30:35
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.operate.OperateCompanyPlanC")
public class OperateCompanyPlanCDto extends OperateCompanyPlanC {
    @CascadeChilds(FK = "operateCompanyPlanCId|uuid")
    private List<OperateCompanyPlanDDto> operateCompanyPlanDDto;


    public List<OperateCompanyPlanDDto> getOperateCompanyPlanDDto() {
        return operateCompanyPlanDDto;
    }

    public void setOperateCompanyPlanDDto(List<OperateCompanyPlanDDto> childDto) {
        this.operateCompanyPlanDDto = childDto;
    }

}