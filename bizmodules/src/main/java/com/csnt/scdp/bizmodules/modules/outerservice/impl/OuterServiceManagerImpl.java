package com.csnt.scdp.bizmodules.modules.outerservice.impl;

import com.csnt.scdp.bizmodules.entity.outersystem.ErpOuterSystemConfiguration;
import com.csnt.scdp.bizmodules.entityattributes.outersystem.ErpOuterSystemConfigurationAttribute;
import com.csnt.scdp.bizmodules.modules.outerservice.intf.OuterServiceManager;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.util.ListUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Scope("singleton")
@Service("outerservice-manager")
public class OuterServiceManagerImpl implements OuterServiceManager {
    public ErpOuterSystemConfiguration getErpOuterSystemConfiguration(String systemCode) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        Map conditionMap = new HashMap<>();
        conditionMap.put(ErpOuterSystemConfigurationAttribute.SYSTEM_CODE, systemCode);
        List<ErpOuterSystemConfiguration> erpOuterSystemConfiguration = pcm.findByAnyFields(ErpOuterSystemConfiguration.class, conditionMap, null);
        if (ListUtil.isNotEmpty(erpOuterSystemConfiguration)) {
            return erpOuterSystemConfiguration.get(0);
        } else {
            return null;
        }
    }

    public String getOutterSysUrl(String systemCode) {
        ErpOuterSystemConfiguration erpOuterSystemConfiguration = getErpOuterSystemConfiguration(systemCode);
        if (erpOuterSystemConfiguration != null) {
            return erpOuterSystemConfiguration.getUrl();
        } else {
            return null;
        }
    }

}