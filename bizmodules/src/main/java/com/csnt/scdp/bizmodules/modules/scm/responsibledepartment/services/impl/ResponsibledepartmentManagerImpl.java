package com.csnt.scdp.bizmodules.modules.scm.responsibledepartment.services.impl;

import com.csnt.scdp.bizmodules.entity.scm.ScmResponsibleDepartment;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmResponsibleDepartmentAttribute;
import com.csnt.scdp.bizmodules.modules.common.intf.CommonServiceManager;
import com.csnt.scdp.bizmodules.modules.scm.responsibledepartment.services.intf.ResponsibledepartmentManager;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.helper.UserHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  ResponsibledepartmentManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-22 20:32:51
 */

@Scope("singleton")
@Service("responsibledepartment-manager")
public class ResponsibledepartmentManagerImpl implements ResponsibledepartmentManager {

    @Resource(name = "erp-common-service-manager")
    private CommonServiceManager commonServiceManager;

    @Override
    public void setExtraData(Map outMap) {
        Map dtoMap = (Map) outMap.get("scmResponsibleDepartmentDto");
        if (dtoMap != null) {
            //1.设置负责人的显示
            String principalName = "";
            String attacheName = "";
            String principal = (String) dtoMap.get(ScmResponsibleDepartmentAttribute.PRINCIPAL);
            String attache = (String) dtoMap.get(ScmResponsibleDepartmentAttribute.ATTACHE);
//            if (StringUtil.isNotEmpty(principal)) {
//                Map paramMap = new HashMap<>();
//                paramMap.put(ScdpUserAttribute.USER_ID, principal);
////                paramMap.put(ScdpUserAttribute.USER_ID, attache);
//                List<ScdpUser> list = PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, paramMap, null);
//                if (ListUtil.isNotEmpty(list)) {
//                    principalName = list.get(0).getUserName();
//                }
//            }
            List userLit = new ArrayList();
            if (StringUtil.isNotEmpty(principal)) {
                userLit.add(principal);
            }
            if (StringUtil.isNotEmpty(attache)) {
                userLit.add(attache);
            }
            if (ListUtil.isNotEmpty(userLit)) {
                List<ScdpUser> userList = commonServiceManager.findUserByUserIds(userLit);
                if (!ListUtil.isEmpty(userList)) {
                    for (ScdpUser s : userList) {
                        if (s.getUserId().equals(principal)) {
                            principalName = s.getUserName();
                        }
                        if (s.getUserId().equals(attache)) {
                            attacheName = s.getUserName();
                        }
                    }
                }
            }
            dtoMap.put(ScmResponsibleDepartmentAttribute.PRINCIPAL_NAME, principalName);
            dtoMap.put(ScmResponsibleDepartmentAttribute.ATTACHE_NAME, attacheName);
            //2.设置负责部门的显示
            if (StringUtil.isNotEmpty((String) dtoMap.get("responsibleDepartment"))) {
                dtoMap.put("responsibleDepartmentDesc", OrgHelper.getOrgNameByCode((String) dtoMap.get("responsibleDepartment")));
            }
        }
    }


    @Override
    public List<ScmResponsibleDepartment> loadAllInfo() {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        Map<String, Object> queryMap = new HashMap();
        return PersistenceFactory.getInstance().findByAnyFields(ScmResponsibleDepartment.class, queryMap, null);
    }
}
