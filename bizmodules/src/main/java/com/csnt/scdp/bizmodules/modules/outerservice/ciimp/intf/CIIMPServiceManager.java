package com.csnt.scdp.bizmodules.modules.outerservice.ciimp.intf;

import com.csnt.scdp.bizmodules.entity.outersystem.ErpOuterSystemConfiguration;

import java.util.List;
import java.util.Map;

public interface CIIMPServiceManager {
    /**
     * @return authStr
     */
    String login();

    /**
     * @param actionUrl http://172.10.50.133:8080/scdp/services/api/ 之后的路径
     * @param dataMap 发送数据
     * @return result
     */
    String sendFinInfo(String actionUrl, Map dataMap);

    String sendFinInfo(String actionUrl, List dataLst);
}