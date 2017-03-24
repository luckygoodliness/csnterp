package com.csnt.scdp.bizmodules.modules.prm.prmcustomer.services.intf;

import com.csnt.scdp.framework.core.exception.BizException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Description:  PrmcustomerManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-29 10:33:22
 */
public interface PrmcustomerManager {

    String createPrmcustomerByName(String name);

    void sendMsg(String uuid, String customerName, String type);
}