package com.csnt.scdp.bizmodules.modules.scm.requestallot.services.impl;

import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmPurchaseReqAttribute;
import com.csnt.scdp.bizmodules.modules.scm.requestallot.services.intf.RequestallotManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  RequestallotManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-10-22 22:18:10
 */

@Scope("singleton")
@Service("requestallot-manager")
public class RequestallotManagerImpl implements RequestallotManager {
    @Override
    public void setExtraData(Map outMap) {
        Map dtoMap = (Map) outMap.get("scmPurchaseReqDto");
        if (dtoMap != null) {
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            //1.设置项目名的显示
            String prmProjectMainIdName = "";
            String prmProjectCode = "";
            String prmProjectMainId = (String) dtoMap.get(ScmPurchaseReqAttribute.PRM_PROJECT_MAIN_ID);
            if (StringUtil.isNotEmpty(prmProjectMainId)) {
                PrmProjectMain prmProjectMain = pcm.findByPK(PrmProjectMain.class, prmProjectMainId);
                if (prmProjectMain != null) {
                    prmProjectMainIdName = prmProjectMain.getProjectName();
                    prmProjectCode = prmProjectMain.getProjectCode();
                }
            }
            dtoMap.put(ScmPurchaseReqAttribute.PRM_PROJECT_MAIN_ID_NAME, prmProjectMainIdName);
            dtoMap.put(ScmPurchaseReqAttribute.PROJECT_CODE, prmProjectCode);
            //2.设置负责人的显示
            String principalName = "";
            String principal = (String) dtoMap.get(ScmPurchaseReqAttribute.PRINCIPAL);
            if (StringUtil.isNotEmpty(principal)) {
                Map paramMap = new HashMap<>();
                paramMap.put(ScdpUserAttribute.USER_ID, principal);
                List<ScdpUser> list = pcm.findByAnyFields(ScdpUser.class, paramMap, null);
                if (ListUtil.isNotEmpty(list)) {
                    principalName = list.get(0).getUserName();
                }
            }
            dtoMap.put(ScmPurchaseReqAttribute.PRINCIPAL_NAME, principalName);
            //23设置负责部门的显示
            if (StringUtil.isNotEmpty((String) dtoMap.get("officeId"))) {
                dtoMap.put("officeIdDesc", OrgHelper.getOrgNameByCode((String) dtoMap.get("officeId")));
            }
        }
    }

    @Override
    public void setExtraQueryConditions(Map inMap) {
        StringBuffer sql = new StringBuffer();
        sql.append(" 1=1 ");
        Map condMap = (Map) inMap.get("queryConditions");
        if ("N".equals(condMap.get(ScmPurchaseReqAttribute.IS_ALLOT))) {
            sql.append(" and t.principal is null");
        } else if ("Y".equals(condMap.get(ScmPurchaseReqAttribute.IS_ALLOT))) {
            sql.append(" and t.principal is not null");
        }
        Map map = new HashMap();
        map.put("selfconditions", sql.toString());
        inMap.put(CommonAttribute.QUERY_EXTRA_PARAMS, map);
    }
}