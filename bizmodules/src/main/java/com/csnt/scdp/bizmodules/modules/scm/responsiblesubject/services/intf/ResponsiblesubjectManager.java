package com.csnt.scdp.bizmodules.modules.scm.responsiblesubject.services.intf;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Description:  ResponsiblesubjectManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-22 13:18:08
 */
public interface ResponsiblesubjectManager {
    //1.额外设置需要返回的数据
    void setExtraData(Map outMap);
}