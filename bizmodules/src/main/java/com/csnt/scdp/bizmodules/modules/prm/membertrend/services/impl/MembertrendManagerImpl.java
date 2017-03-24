package com.csnt.scdp.bizmodules.modules.prm.membertrend.services.impl;

import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmMemberTrendAttribute;
import com.csnt.scdp.bizmodules.modules.prm.membertrend.services.intf.MembertrendManager;
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
 * Description:  MembertrendManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-22 13:49:25
 */

@Scope("singleton")
@Service("membertrend-manager")
public class MembertrendManagerImpl implements MembertrendManager {
    @Override
    public void setExtraData(Map outMap) {
        Map dtoMap = (Map) outMap.get("prmMemberTrendDto");
        if (dtoMap != null) {
            //1.设置项目名的显示
            String projectName = "";
            String prmProjectMainId = (String) dtoMap.get("prmProjectMainId");
            if (StringUtil.isEmpty(prmProjectMainId)) {

            }
            PrmProjectMain prmProjectMain = PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, prmProjectMainId);
            if (prmProjectMain != null) {
                projectName = prmProjectMain.getProjectName();
            }
            dtoMap.put(PrmMemberTrendAttribute.PROJECT_NAME, projectName);
            //设置姓名的显示
            String staffName = "";
            String staffId = (String) dtoMap.get("staffId");
            if (staffId != null) {
                Map paramMap = new HashMap<>();
                paramMap.put(ScdpUserAttribute.USER_ID, staffId);
                List<ScdpUser> list = PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, paramMap, null);
                if (ListUtil.isNotEmpty(list)) {
                    staffName = list.get(0).getUserName();
                }
            }
            //2.设置部门
            String departmentCode = (String) dtoMap.get("departmentCode");
            if (StringUtil.isNotEmpty(departmentCode)) {
                dtoMap.put("departmentCodeDesc", OrgHelper.getOrgNameByCode(departmentCode));
            }

            dtoMap.put(PrmMemberTrendAttribute.STAFF_NAME, staffName);
        }
    }

}