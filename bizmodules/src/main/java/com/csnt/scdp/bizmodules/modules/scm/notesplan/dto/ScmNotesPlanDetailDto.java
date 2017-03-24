package com.csnt.scdp.bizmodules.modules.scm.notesplan.dto;

import com.csnt.scdp.bizmodules.entity.scm.ScmNotesPlanDetail;
import com.csnt.scdp.framework.dto.PojoMapping;


/**
 * Description:  ScmNotesPlanDetailDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-11-04 15:02:50
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.scm.ScmNotesPlanDetail")
public class ScmNotesPlanDetailDto extends ScmNotesPlanDetail {

    private String prmProjectMainIdName;

    private String supplierCode;

    private String projectId;

    private String createByContractName;

    private String officeId;

    private String officeName;

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getCreateByContractName() {
        return createByContractName;
    }

    public void setCreateByContractName(String createByContractName) {
        this.createByContractName = createByContractName;
    }

    public String getPrmProjectMainIdName() {
        return prmProjectMainIdName;
    }

    public void setPrmProjectMainIdName(String prmProjectMainIdName) {
        this.prmProjectMainIdName = prmProjectMainIdName;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

}