package com.csnt.scdp.bizmodules.modules.scm.scmsupplierlimitchange.services.intf;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Description:  ScmsupplierlimitchangeManager
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-07-22 16:17:18
 */
public interface ScmsupplierlimitchangeManager {
    List<Map<String, Object>> selectCurAmount(String uuid) ;
    List<Map<String, Object>> selectContract(String uuid);
    void replaceMxxAmount(String uuid);
}