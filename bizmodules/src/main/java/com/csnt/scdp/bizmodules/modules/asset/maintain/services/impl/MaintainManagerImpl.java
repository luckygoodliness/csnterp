package com.csnt.scdp.bizmodules.modules.asset.maintain.services.impl;

import com.csnt.scdp.bizmodules.entity.asset.AssetMaintain;
import com.csnt.scdp.bizmodules.modules.asset.maintain.services.intf.MaintainManager;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Description:  MaintainManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-22 20:02:12
 */

@Scope("singleton")
@Service("maintain-manager")
public class MaintainManagerImpl implements MaintainManager {
    @Override
    public AssetMaintain getAssetMaintainForUUID(String uuid) {
        AssetMaintain assetMaintain = PersistenceFactory.getInstance().findByPK(AssetMaintain.class, uuid);
        if (StringUtil.isNotEmpty(assetMaintain)) {
            return assetMaintain;
        }
        return null;
    }

    @Override
    public void deleteAssetMaintain(Map outMap) {
        Map dtoMap = (Map) outMap.get("assetMaintainDto");
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        if (dtoMap != null) {
            String uuid = dtoMap.get("assetMaintainDto") + "";
            AssetMaintain assetMaintain = PersistenceFactory.getInstance().findByPK(AssetMaintain.class, uuid);
            if (assetMaintain != null) {
              String state = assetMaintain.getState();
                if(state=="N"){
                    pcm.delete(assetMaintain);
                }
            }
        }

    }

}