package com.csnt.scdp.bizmodules.modules.prm.prmorgrolefilter.dto;

import com.csnt.scdp.bizmodules.entity.prm.PrmBilling;
import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.sysmodules.entity.ScdpOrg;
//import com.csnt.scdp.bizmodules.entity.ScdpOrg;

import java.util.List;


/**
 * Description:  ScdpOrgDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-12-07 15:33:14
 */
@PojoMapping(PojoClass = "com.csnt.scdp.sysmodules.entity.ScdpOrg")
public class ScdpOrgDto extends ScdpOrg {

    @CascadeChilds(FK = "roleId|uuid")
    private List<PrmOrgRoleFilterDto> prmOrgRoleFilterDto;


    public List<PrmOrgRoleFilterDto> getPrmOrgRoleFilterDto() {
        return prmOrgRoleFilterDto;
    }

    public void setPrmOrgRoleFilterDto(List<PrmOrgRoleFilterDto> prmOrgRoleFilterDto) {
        this.prmOrgRoleFilterDto = prmOrgRoleFilterDto;
    }
}

