package com.csnt.scdp.bizmodules.modules.scm.prmblacklistmonth.services.intf;

import java.util.List;
import java.util.Map;

/**
 * Description:  PrmblacklistmonthManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-10-14 15:47:35
 */
public interface PrmblacklistmonthManager {
    //1.额外设置需要返回的数据
    void setExtraData(Map outMap);

    /**
     * @return处于黑名单的供应商列表
     */
    List getCurrentBlacklist();

}