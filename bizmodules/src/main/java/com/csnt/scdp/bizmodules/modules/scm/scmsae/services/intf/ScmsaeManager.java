package com.csnt.scdp.bizmodules.modules.scm.scmsae.services.intf;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Description:  ScmsaeManager
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-08-23 19:38:26
 */
public interface ScmsaeManager {
    public String createScmSaeForm(String scmSaeId);

    public String computeSaeResult(String scmSaeId, double rate);

    public String createScmSaeTask(String scmSaeId, String beginTime, String endTime, String remark);
}