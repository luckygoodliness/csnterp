package com.csnt.scdp.bizmodules.modules.scm.responsibleclass.services.impl;

import com.csnt.scdp.bizmodules.entity.scm.ScmMaterialClass;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmResponsibleClassAttribute;
import com.csnt.scdp.bizmodules.modules.scm.responsibleclass.services.intf.ResponsibleclassManager;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  ResponsibleclassManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-22 15:57:13
 */

@Scope("singleton")
@Service("responsibleclass-manager")
public class ResponsibleclassManagerImpl implements ResponsibleclassManager {

    @Override
    public void setExtraData(Map outMap) {
        Map dtoMap = (Map) outMap.get("scmResponsibleClassDto");
        if (dtoMap != null) {
            //1.设置物料名的显示
            String responsibleClassName = "";
            String responsibleClass = (String) dtoMap.get(ScmResponsibleClassAttribute.RESPONSIBLE_CLASS);
            if (StringUtil.isNotEmpty(responsibleClass)) {
                Map paramMap = new HashMap<>();
                paramMap.put("code", responsibleClass);
                List<ScmMaterialClass> list = PersistenceFactory.getInstance().findByAnyFields(ScmMaterialClass.class, paramMap, null);
                if (ListUtil.isNotEmpty(list)) {
                    responsibleClassName = list.get(0).getName();
                }
            }
            dtoMap.put(ScmResponsibleClassAttribute.RESPONSIBLE_CLASS_NAME, responsibleClassName);
            //2.设置负责人的显示
            String principalName = "";
            String principal = (String) dtoMap.get(ScmResponsibleClassAttribute.PRINCIPAL);
            if (StringUtil.isNotEmpty(principal)) {
                Map paramMap = new HashMap<>();
                paramMap.put(ScdpUserAttribute.USER_ID, principal);
                List<ScdpUser> list = PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, paramMap, null);
                if (ListUtil.isNotEmpty(list)) {
                    principalName = list.get(0).getUserName();
                }
            }
            dtoMap.put(ScmResponsibleClassAttribute.PRINCIPAL_NAME, principalName);
        }
    }
}