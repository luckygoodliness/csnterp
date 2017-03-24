package com.csnt.scdp.bizmodules.modules.nonprm.nonprmpurchasereq.services.intf;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Description:  NonprmpurchasereqManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-11-19 14:00:26
 */
public interface NonprmpurchasereqManager {

    /**
     * 1.提交
     *
     * @param uuid
     */
    void submit(String uuid);
    /**
     * 1.审核
     *
     * @param isPro 0非项目、项目1
     * @param uuid
     */
    void audit(String isPro, String uuid);

    boolean checkExcessBudget(Map inMap);
}