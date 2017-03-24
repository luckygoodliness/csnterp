package com.csnt.scdp.bizmodules.modules.prm.progress.services.impl;

import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProgressAttribute;
import com.csnt.scdp.bizmodules.modules.prm.progress.services.intf.ProgressManager;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Description:  ProgressManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 19:16:19
 */

@Scope("singleton")
@Service("progress-manager")
public class ProgressManagerImpl implements ProgressManager {
    @Override
    public void setExtraData(Map outMap) {
        Map dtoMap = (Map) outMap.get("prmProgressDto");
        if (dtoMap != null) {
            //1.设置项目名的显示
            String projectName = "";
            String prmProjectMainId = (String) dtoMap.get("prmProjectMainId");
            PrmProjectMain prmProjectMain = PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, prmProjectMainId);
            if (prmProjectMain != null) {
                projectName = prmProjectMain.getProjectName();
            }
            dtoMap.put(PrmProgressAttribute.PROJECT_NAME, projectName);
            //2.设置部门
            String departmentCode = (String) dtoMap.get("departmentCode");
            if (StringUtil.isNotEmpty(departmentCode)) {
                dtoMap.put("departmentCodeDesc", OrgHelper.getOrgNameByCode((String) dtoMap.get("departmentCode")));
            }
        }
    }

    @Override
    //根据项目名称uuid带出监理单位
    public List getManagementIdByUUID(String uuid) {
        DAOMeta daoMeta = new DAOMeta();
        String sql = "select t2.management_id  \n" +
                "from prm_project_main t1 \n" +
                "left join prm_contract t2   \n" +
                "on t2.uuid=t1.prm_contract_id \n" +
                "where 1=1 and t1.uuid='" + uuid + "'";
        daoMeta.setStrSql(sql);
        List lstObjInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        return lstObjInfo;
    }

}