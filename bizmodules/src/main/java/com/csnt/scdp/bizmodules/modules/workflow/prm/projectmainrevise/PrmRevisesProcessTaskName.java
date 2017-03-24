package com.csnt.scdp.bizmodules.modules.workflow.prm.projectmainrevise;

import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainCAttribute;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("prm_revise-process-taskname")
@Transactional

public class PrmRevisesProcessTaskName extends ERPWorkFlowBaseVariableCollectionAction {
    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        inMap.put("dto", "com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmProjectMainCDto");
        Map dataInfo = super.perform(inMap);
        String projectName = (String) dataInfo.get("projectName");
        String officeId = (String) dataInfo.get("contractorOffice");
        String orgName = null;
        if (StringUtil.isNotEmpty(officeId)) {
            orgName = OrgHelper.getOrgNameByCode(officeId);
        }
        Map mapResult = new HashMap();
        String state = (String) dataInfo.get("state");
        String stateDesc = ErpWorkFlowHelper.stateFlag(state);
        mapResult.put("name", stateDesc + "部门：" + orgName + "；项目名称：" + projectName);

        String projectMainUuid = (String) dataInfo.get(PrmProjectMainCAttribute.PRM_PROJECT_MAIN_ID);
        if (StringUtil.isNotEmpty(projectMainUuid)) {
            PrmProjectMain projectMain = PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, projectMainUuid);
            if (projectMain != null && Integer.valueOf(1).equals(projectMain.getIsPreProject())) {
                mapResult.put(WorkFlowAttribute.TITLE, "项目预立项变更");
            }
        }

        return mapResult;
    }
}
