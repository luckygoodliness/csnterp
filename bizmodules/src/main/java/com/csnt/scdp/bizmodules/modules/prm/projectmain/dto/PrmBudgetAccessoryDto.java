package com.csnt.scdp.bizmodules.modules.prm.projectmain.dto;

import com.csnt.scdp.bizmodules.entity.prm.PrmBudgetAccessory;
import com.csnt.scdp.bizmodules.modules.prm.projectmain.helper.ProjectMainHelper;
import com.csnt.scdp.framework.dto.PojoMapping;

import java.math.BigDecimal;
import java.util.Date;


/**
 * Description:  PrmBudgetAccessoryDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-10-21 20:28:09
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.prm.PrmBudgetAccessory")
public class PrmBudgetAccessoryDto extends PrmBudgetAccessory {
    private Date latestUpdateTime;
    private String changeStatus;
    private Integer changeVersion;
    private String changeVersionDesc;
    private String latestUpdateBy;
    private String latestUpdateByDesc;
    private BigDecimal purchaseLimitLeft;

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

    public BigDecimal getPurchaseLimitLeft() {
        return purchaseLimitLeft;
    }

    public void setPurchaseLimitLeft(BigDecimal purchaseLimitLeft) {
        this.purchaseLimitLeft = purchaseLimitLeft;
    }
}