package com.csnt.scdp.bizmodules.modules.prm.progress.services.intf;

import java.util.List;
import java.util.Map;

/**
 * Description:  ProgressManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 19:16:19
 */
public interface ProgressManager {
    //1.额外设置需要返回的数据
    void setExtraData(Map outMap);
    //根据项目名称uuid带出监理单位
    public List getManagementIdByUUID(String uuid);

}