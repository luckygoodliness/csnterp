package com.csnt.scdp.bizmodules.modules.scm.responsibledepartment.services.intf;

import com.csnt.scdp.bizmodules.entity.scm.ScmResponsibleDepartment;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Description:  ResponsibledepartmentManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-22 20:32:51
 */
public interface ResponsibledepartmentManager {
    //1.额外设置需要返回的数据
    void setExtraData(Map outMap);

    List<ScmResponsibleDepartment> loadAllInfo();
}