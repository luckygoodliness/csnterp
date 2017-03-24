package com.csnt.scdp.bizmodules.modules.asset.check.dto;

import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.asset.AssetAnnualCheck;

import java.util.List;


/**
 * Description:  AssetAnnualCheckDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-24 09:59:12
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.asset.AssetAnnualCheck")
public class AssetAnnualCheckDto extends AssetAnnualCheck {

    private Integer alreadyUse;

    public Integer getAlreadyUse() {
        return alreadyUse;
    }

    public void setAlreadyUse(Integer alreadyUse) {
        this.alreadyUse = alreadyUse;
    }
}