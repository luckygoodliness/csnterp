package com.csnt.scdp.bizmodules.modules.prm.projectmain.dto;

import com.csnt.scdp.bizmodules.entity.prm.PrmReceiptsDetailP;
import com.csnt.scdp.bizmodules.modules.prm.projectmain.helper.ProjectMainHelper;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.sysmodules.helper.FMCodeHelper;

import java.util.Date;


/**
 * Description:  PrmReceiptsDetailPDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-10-21 20:28:09
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.prm.PrmReceiptsDetailP")
public class PrmReceiptsDetailPDto extends PrmReceiptsDetailP {

    private Date latestUpdateTime;
    private String changeStatus;
    private Integer changeVersion;
    private String changeVersionDesc;
    private String latestUpdateBy;
    private String latestUpdateByDesc;
    private String projectStageDesc;

    public Date getLatestUpdateTime() {
        return latestUpdateTime;
    }

    public void setLatestUpdateTime(Date latestUpdateTime) {
        this.latestUpdateTime = latestUpdateTime;
    }

    public String getChangeStatus() {
        return changeStatus;
    }

    public void setChangeStatus(String changeStatus) {
        this.changeStatus = changeStatus;
    }

    public Integer getChangeVersion() {
        return changeVersion;
    }

    public void setChangeVersion(Integer changeVersion) {
        this.changeVersion = changeVersion;
    }

    public String getChangeVersionDesc() {
        return ProjectMainHelper.getChangeVersionDesc(this.getChangeVersion());
    }

    public String getLatestUpdateBy() {
        return latestUpdateBy;
    }

    public void setLatestUpdateBy(String latestUpdateBy) {
        this.latestUpdateBy = latestUpdateBy;
    }

    public String getLatestUpdateByDesc() {
        return latestUpdateByDesc;
    }

    public void setLatestUpdateByDesc(String latestUpdateByDesc) {
        this.latestUpdateByDesc = latestUpdateByDesc;
    }

    public String getProjectStageDesc() {
        return  FMCodeHelper.getDescByCode(this.getProjectStage(), "CDM_PROJECT_STAGE");
    }

    public void setProjectStageDesc(String projectStageDesc) {
        this.projectStageDesc = projectStageDesc;
    }
}