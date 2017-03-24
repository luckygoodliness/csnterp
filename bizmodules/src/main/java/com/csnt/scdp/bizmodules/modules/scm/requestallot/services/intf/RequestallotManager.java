package com.csnt.scdp.bizmodules.modules.scm.requestallot.services.intf;

import java.util.Map;

/**
 * Description:  RequestallotManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-10-22 22:18:10
 */
public interface RequestallotManager {
    /**
     * 1.额外设置需要返回的数据
     *
     * @param outMap
     */
    void setExtraData(Map outMap);

    /**
     * 为申请单分配查询页面设置额外的查询条件（默认查询未分配的数据）
     */
    void setExtraQueryConditions(Map inMap);
}