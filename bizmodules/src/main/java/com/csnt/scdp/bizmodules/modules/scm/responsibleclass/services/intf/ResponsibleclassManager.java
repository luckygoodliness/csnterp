package com.csnt.scdp.bizmodules.modules.scm.responsibleclass.services.intf;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Description:  ResponsibleclassManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-22 15:57:13
 */
public interface ResponsibleclassManager {
    //1.额外设置需要返回的数据
    void setExtraData(Map outMap);
}