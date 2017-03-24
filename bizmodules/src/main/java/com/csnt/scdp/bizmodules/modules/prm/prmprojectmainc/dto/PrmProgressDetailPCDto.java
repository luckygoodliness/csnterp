package com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto;

import com.csnt.scdp.bizmodules.entity.prm.PrmProgressDetailPC;
import com.csnt.scdp.framework.dto.PojoMapping;


/**
 * Description:  PrmProgressDetailPCDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-24 15:53:10
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.prm.PrmProgressDetailPC")
public class PrmProgressDetailPCDto extends PrmProgressDetailPC {
    private String changeStatus;
    private String changedFields;
    private Integer isRevised;

    public String getChangeStatus() {
        return changeStatus;
    }

    public void setChangeStatus(String changeStatus) {
        this.changeStatus = changeStatus;
    }

    public String getChangedFields() {
        return changedFields;
    }

    public void setChangedFields(String changedFields) {
        this.changedFields = changedFields;
    }

    public Integer getIsRevised() {
        return isRevised;
    }

    public void setIsRevised(Integer isRevised) {
        this.isRevised = isRevised;
    }

}