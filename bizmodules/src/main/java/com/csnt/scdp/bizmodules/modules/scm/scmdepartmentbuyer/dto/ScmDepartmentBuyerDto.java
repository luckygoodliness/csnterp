package com.csnt.scdp.bizmodules.modules.scm.scmdepartmentbuyer.dto;

import com.csnt.scdp.bizmodules.entity.scm.ScmDepartmentBuyer;
import com.csnt.scdp.framework.dto.PojoMapping;


/**
 * Description:  ScmDepartmentBuyerDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-11-23 11:06:51
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.scm.ScmDepartmentBuyer")
public class ScmDepartmentBuyerDto extends ScmDepartmentBuyer {
    private String officeCodeDesc;

    public String getOfficeCodeDesc() {
        return officeCodeDesc;
    }

    public void setOfficeCodeDesc(String officeCodeDesc) {
        this.officeCodeDesc = officeCodeDesc;
    }
}