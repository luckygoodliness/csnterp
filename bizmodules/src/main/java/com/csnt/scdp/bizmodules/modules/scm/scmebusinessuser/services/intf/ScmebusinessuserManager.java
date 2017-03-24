package com.csnt.scdp.bizmodules.modules.scm.scmebusinessuser.services.intf;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:  ScmebusinessuserManager
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-08-17 16:35:38
 */
public interface ScmebusinessuserManager {

    List getScmebusinessuserByEbusinessName(String ebusinessName);

    List getScmebusinessuserByEbusinessNameAndUserCode(String ebusinessName, String UserCode);
}