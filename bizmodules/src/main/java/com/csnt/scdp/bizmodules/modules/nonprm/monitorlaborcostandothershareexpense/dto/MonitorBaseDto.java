package com.csnt.scdp.bizmodules.modules.nonprm.monitorlaborcostandothershareexpense.dto;

import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.nonprm.MonitorBase;

import java.util.List;


/**
 * Description:  MonitorBaseDto
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2016-04-08 16:32:06
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.nonprm.MonitorBase")
public class MonitorBaseDto extends MonitorBase {
    @CascadeChilds(FK = "monitorBaseId|uuid")
    private List<MonitorLaborCostDto> monitorLaborCostDto;

    @CascadeChilds(FK = "monitorBaseId|uuid")
    private List<MonitorOtherShareDto> monitorOtherShareDto;

    public List<MonitorLaborCostDto> getMonitorLaborCostDto() {
        return monitorLaborCostDto;
    }

    public void setMonitorLaborCostDto(List<MonitorLaborCostDto> childDto) {
        this.monitorLaborCostDto = childDto;
    }

    public List<MonitorOtherShareDto> getMonitorOtherShareDto() {
        return monitorOtherShareDto;
    }

    public void setMonitorOtherShareDto(List<MonitorOtherShareDto> childDto) {
        this.monitorOtherShareDto = childDto;
    }
}