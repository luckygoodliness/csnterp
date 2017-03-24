package com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto;

import com.csnt.scdp.bizmodules.entity.prm.PrmQsPC;
import com.csnt.scdp.framework.dto.PojoMapping;


/**
 * Description:  PrmQsPCDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-10-21 20:24:42
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.prm.PrmQsPC")
public class PrmQsPCDto extends PrmQsPC {
    private String safePrincipalDesc;
    private String safeContactDesc;
    private String qualityPrincipalDesc;
    private String qualityContactDesc;
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

    public String getSafePrincipalDesc() {
        return safePrincipalDesc;
    }

    public void setSafePrincipalDesc(String safePrincipalDesc) {
        this.safePrincipalDesc = safePrincipalDesc;
    }

    public String getSafeContactDesc() {
        return safeContactDesc;
    }

    public void setSafeContactDesc(String safeContactDesc) {
        this.safeContactDesc = safeContactDesc;
    }

    public String getQualityPrincipalDesc() {
        return qualityPrincipalDesc;
    }

    public void setQualityPrincipalDesc(String qualityPrincipalDesc) {
        this.qualityPrincipalDesc = qualityPrincipalDesc;
    }

    public String getQualityContactDesc() {
        return qualityContactDesc;
    }

    public void setQualityContactDesc(String qualityContactDesc) {
        this.qualityContactDesc = qualityContactDesc;
    }
}