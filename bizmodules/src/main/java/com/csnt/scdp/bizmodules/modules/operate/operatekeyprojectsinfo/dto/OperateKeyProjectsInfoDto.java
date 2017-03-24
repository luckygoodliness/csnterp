package com.csnt.scdp.bizmodules.modules.operate.operatekeyprojectsinfo.dto;

import com.csnt.scdp.bizmodules.entity.operate.OperateKeyProjectsInfo;
import com.csnt.scdp.framework.dto.PojoMapping;


/**
 * Description:  OperateKeyProjectsInfoDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-10-27 17:35:53
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.operate.OperateKeyProjectsInfo")
public class OperateKeyProjectsInfoDto extends OperateKeyProjectsInfo {
    private String proprietorUnitDesc;
    private String projectShowName;

    public String getProprietorUnitDesc() {
        return proprietorUnitDesc;
    }

    public void setProprietorUnitDesc(String proprietorUnitDesc) {
        this.proprietorUnitDesc = proprietorUnitDesc;
    }

    public String getProjectShowName() {
        return projectShowName;
    }

    public void setProjectShowName(String projectShowName) {
        this.projectShowName = projectShowName;
    }


}