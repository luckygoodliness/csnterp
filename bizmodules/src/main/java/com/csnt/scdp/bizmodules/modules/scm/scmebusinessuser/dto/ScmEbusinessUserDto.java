package com.csnt.scdp.bizmodules.modules.scm.scmebusinessuser.dto;

import com.csnt.scdp.bizmodules.entity.scm.ScmSupplier;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.scm.ScmEbusinessUser;
import com.csnt.scdp.sysmodules.helper.OrgHelper;


/**
 * Description:  ScmEbusinessUserDto
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2016-08-17 16:35:37
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.scm.ScmEbusinessUser")
public class ScmEbusinessUserDto extends ScmEbusinessUser {
    //部门名称
    private String officeIdDesc;
    //供应商名称
    private String scmSupplierName;

    public String getOfficeIdDesc() {
        return OrgHelper.getOrgNameByCode(this.getOfficeId());
    }

    public void setOfficeIdDesc(String officeIdDesc) {
        this.officeIdDesc = officeIdDesc;
    }

    public String getScmSupplierName() {
//        ScmSupplier scmSupplier = PersistenceFactory.getInstance().findByPK(ScmSupplier.class, this.getScmSupplierId());
//        return scmSupplier.getCompleteName();
        return this.scmSupplierName;
    }

    public void setScmSupplierName(String scmSupplierName) {
        this.scmSupplierName = scmSupplierName;
    }
}