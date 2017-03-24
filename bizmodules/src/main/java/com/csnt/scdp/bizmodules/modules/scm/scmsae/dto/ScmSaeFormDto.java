package com.csnt.scdp.bizmodules.modules.scm.scmsae.dto;

import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.scm.ScmSaeForm;

import java.util.List;


/**
 * Description:  ScmSaeFormDto
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2016-09-01 20:48:53
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.scm.ScmSaeForm")
public class ScmSaeFormDto extends ScmSaeForm {
    private String userCodeDesc;
    private String state;
    private String officeIdDesc;

    public String getUserCodeDesc() {
        return userCodeDesc;
    }

    public void setUserCodeDesc(String userCodeDesc) {
        this.userCodeDesc = userCodeDesc;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOfficeIdDesc() {
        return officeIdDesc;
    }

    public void setOfficeIdDesc(String officeIdDesc) {
        this.officeIdDesc = officeIdDesc;
    }
}