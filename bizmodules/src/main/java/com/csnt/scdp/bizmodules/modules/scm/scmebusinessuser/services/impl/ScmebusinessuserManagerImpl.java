package com.csnt.scdp.bizmodules.modules.scm.scmebusinessuser.services.impl;

import com.csnt.scdp.bizmodules.entity.scm.ScmEbusinessUser;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmEbusinessUserAttribute;
import com.csnt.scdp.bizmodules.modules.scm.scmebusinessuser.services.intf.ScmebusinessuserManager;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.StringUtil;
import com.fr.stable.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  ScmebusinessuserManagerImpl
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-08-17 16:35:38
 */

@Scope("singleton")
@Service("scmebusinessuser-manager")
public class ScmebusinessuserManagerImpl implements ScmebusinessuserManager {

    public List getScmebusinessuserByEbusinessName(String ebusinessName) {
        return getScmebusinessuserByEbusinessNameAndUserCode(ebusinessName, StringUtils.EMPTY);
    }

    public List getScmebusinessuserByEbusinessNameAndUserCode(String ebusinessName, String userCode) {
        List rtn = Collections.EMPTY_LIST;
        if (StringUtil.isNotEmpty(ebusinessName)) {
            Map conditionMap = new HashMap<>();
            conditionMap.put(ScmEbusinessUserAttribute.EBUSINESS_NAME, ebusinessName);
            if (StringUtil.isNotEmpty(userCode)) {
                conditionMap.put(ScmEbusinessUserAttribute.USER_CODE, userCode);
            }
            rtn = PersistenceFactory.getInstance().findByAnyFields(ScmEbusinessUser.class, conditionMap, null);
        }
        return rtn;
    }
}