package com.csnt.scdp.bizmodules.modules.asset.maintain.services.intf;

import com.csnt.scdp.bizmodules.entity.asset.AssetMaintain;

import java.util.Map;

/**
 * Description:  MaintainManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-22 20:02:12
 */
public interface MaintainManager {
    public AssetMaintain getAssetMaintainForUUID(String uuid);

    public void deleteAssetMaintain(Map outMap);

}