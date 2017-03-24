package com.csnt.scdp.bizmodules.modules.scm.scmsaeappraisercase.dto;

import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.scm.ScmSaeAppraiserCaseD;

import java.util.List;


/**
 * Description:  ScmSaeAppraiserCaseDDto
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2016-11-02 09:56:31
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.scm.ScmSaeAppraiserCaseD")
public class ScmSaeAppraiserCaseDDto extends ScmSaeAppraiserCaseD {
    private String appraiserDesc;
    private String officeIdDesc;

    public String getAppraiserDesc() {
        return appraiserDesc;
    }

    public void setAppraiserDesc(String appraiserDesc) {
        this.appraiserDesc = appraiserDesc;
    }

    public String getOfficeIdDesc() {
        return officeIdDesc;
    }

    public void setOfficeIdDesc(String officeIdDesc) {
        this.officeIdDesc = officeIdDesc;
    }
}