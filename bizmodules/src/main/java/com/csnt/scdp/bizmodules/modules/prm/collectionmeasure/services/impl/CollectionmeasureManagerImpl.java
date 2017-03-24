package com.csnt.scdp.bizmodules.modules.prm.collectionmeasure.services.impl;

import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmCollectionMeasureAttribute;
import com.csnt.scdp.bizmodules.modules.prm.collectionmeasure.services.intf.CollectionmeasureManager;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
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
 * Description:  CollectionmeasureManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 17:28:12
 */

@Scope("singleton")
@Service("collectionmeasure-manager")
public class CollectionmeasureManagerImpl implements CollectionmeasureManager {
    @Override
    public void setExtraData(Map outMap) {
        Map dtoMap = (Map) outMap.get("prmCollectionMeasureDto");
        if (dtoMap != null) {
            //1.设置项目名的显示
            String projectName = "";
            String prmProjectMainId = (String)dtoMap.get("prmProjectMainId");
            PrmProjectMain prmProjectMain = PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, prmProjectMainId);
            if(prmProjectMain!=null){
                projectName = prmProjectMain.getProjectName();
            }
            dtoMap.put(PrmCollectionMeasureAttribute.PROJECT_NAME, projectName);
            //设置负责人的显示
            String principalName = "";
            String principal = (String)dtoMap.get("principal");
            if(principal!=null){
                Map paramMap = new HashMap<>();
                paramMap.put(ScdpUserAttribute.USER_ID, principal);
                List<ScdpUser> list = PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, paramMap, null);
                if (ListUtil.isNotEmpty(list)) {
                    principalName = list.get(0).getUserName();
                }
            }
            dtoMap.put(PrmCollectionMeasureAttribute.PRINCIPAL_NAME, principalName);
            //2.设置部门
            String departmentCode = (String) dtoMap.get("departmentCode");
            if (StringUtil.isNotEmpty(departmentCode)) {
                dtoMap.put("departmentCodeDesc", OrgHelper.getOrgNameByCode(departmentCode));
            }
        }
    }

}