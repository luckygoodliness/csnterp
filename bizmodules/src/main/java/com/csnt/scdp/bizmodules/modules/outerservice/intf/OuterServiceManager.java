package com.csnt.scdp.bizmodules.modules.outerservice.intf;

import com.csnt.scdp.bizmodules.entity.outersystem.ErpOuterSystemConfiguration;

public interface OuterServiceManager {
    /**
     *
     * @param systemCode
     * @return ErpOuterSystemConfiguration
     */
    ErpOuterSystemConfiguration getErpOuterSystemConfiguration(String systemCode);
    /**
     *
     * @param systemCode
     * @return ErpOuterSystemConfiguration.url
     */
    String getOutterSysUrl(String systemCode);

}