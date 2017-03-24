package com.csnt.scdp.bizmodules.modules.scm.materialclass.services.impl;

import com.csnt.scdp.bizmodules.entity.scm.ScmMaterialItem;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmMaterialItemAttribute;
import com.csnt.scdp.bizmodules.modules.scm.materialclass.services.intf.MaterialclassManager;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Description:  MaterialclassManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 11:15:19
 */

@Scope("singleton")
@Service("materialclass-manager")
public class MaterialclassManagerImpl implements MaterialclassManager {
    @Override
    public void setExtraData(Map outMap) {
        Map dtoMap = (Map) outMap.get("scmMaterialClassDto");
        if (dtoMap != null) {
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            String itemClass = (String) dtoMap.get("itemClass");
            if (StringUtil.isNotEmpty(itemClass)) {
                ScmMaterialItem m = pcm.findByPK(ScmMaterialItem.class, itemClass);
                if (m != null) {
                    dtoMap.put(ScmMaterialItemAttribute.ITEM_CLASS_NAME, m.getName());
                }
            }
        }
    }
}