package com.csnt.scdp.bizmodules.modules.prm.meetingsummary.services.impl;

import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmMeetingSummaryAttribute;
import com.csnt.scdp.bizmodules.modules.prm.meetingsummary.services.intf.MeetingsummaryManager;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Description:  MeetingsummaryManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 19:49:18
 */

@Scope("singleton")
@Service("meetingsummary-manager")
public class MeetingsummaryManagerImpl implements MeetingsummaryManager {
    @Override
    public void setExtraData(Map outMap) {
        Map dtoMap = (Map) outMap.get("prmMeetingSummaryDto");
        if (dtoMap != null) {
            //1.设置项目名的显示
            String projectName = "";
            String prmProjectMainId = dtoMap.get("prmProjectMainId") + "";
            PrmProjectMain prmProjectMain = PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, prmProjectMainId);
            if (prmProjectMain != null) {
                projectName = prmProjectMain.getProjectName();
            }
            dtoMap.put(PrmMeetingSummaryAttribute.PROJECT_NAME, projectName);
            //2.设置部门
            String departmentCode = (String) dtoMap.get("departmentCode");
            if (StringUtil.isNotEmpty(departmentCode)) {
                dtoMap.put("departmentCodeDesc", OrgHelper.getOrgNameByCode(departmentCode));
            }
        }
    }

}