package com.csnt.scdp.bizmodules.modules.prm.prmrolefilter.dto;

import com.csnt.scdp.bizmodules.entity.prm.PrmRoleFilter;
import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.sysmodules.entity.ScdpRole;

import java.util.List;


/**
 * Description:  PrmScdpRoleDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-11-16 13:30:29
 */
@PojoMapping(PojoClass = "com.csnt.scdp.sysmodules.entity.ScdpRole")
public class PrmScdpRoleDto extends ScdpRole {
    public List<PrmRoleFilterDto> getPrmRoleFilterDto() {
        return prmRoleFilterDto;
    }

    public void setPrmRoleFilterDto(List<PrmRoleFilterDto> prmRoleFilterDto) {
        this.prmRoleFilterDto = prmRoleFilterDto;
    }

    @CascadeChilds(FK = "roleId|uuid")
    private List<PrmRoleFilterDto> prmRoleFilterDto;

}