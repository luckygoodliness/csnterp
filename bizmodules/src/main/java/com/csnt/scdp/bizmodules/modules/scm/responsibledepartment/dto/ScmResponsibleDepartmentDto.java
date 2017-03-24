package com.csnt.scdp.bizmodules.modules.scm.responsibledepartment.dto;

import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.scm.ScmResponsibleDepartment;

import java.util.List;


/**
 * Description:  ScmResponsibleDepartmentDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-22 20:32:51
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.scm.ScmResponsibleDepartment")
public class ScmResponsibleDepartmentDto extends ScmResponsibleDepartment {
    //负责部门
    private String responsibleDepartmentDesc;

    public String getResponsibleDepartmentDesc() {
        return responsibleDepartmentDesc;
    }

    public void setResponsibleDepartmentDesc(String responsibleDepartmentDesc) {
        this.responsibleDepartmentDesc = responsibleDepartmentDesc;
    }
}