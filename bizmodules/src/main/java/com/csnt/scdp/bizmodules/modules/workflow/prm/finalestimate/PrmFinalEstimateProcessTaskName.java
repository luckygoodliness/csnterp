package com.csnt.scdp.bizmodules.modules.workflow.prm.finalestimate;

import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("prm_final_estimates-process-taskname")
@Transactional

public class PrmFinalEstimateProcessTaskName extends ERPWorkFlowBaseVariableCollectionAction {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapResult = new HashMap();
        String businessKey = (String) inMap.get(WorkFlowAttribute.BUSINESS_KEY);
        String sql = "select a.state,a.department_code,c.project_name From prm_final_estimate a inner join prm_project_main c on a.prm_project_main_id = c.uuid where a.uuid=?";
        List lstParam = new ArrayList();
        lstParam.add(businessKey);
        DAOMeta daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setLstParams(lstParam);
        daoMeta.setNeedFilter(false);
        List lstResult = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(lstResult)) {
            Map temp = (Map) lstResult.get(0);
            String officeId = (String) temp.get("departmentCode");
            String orgName = null;
            if (StringUtil.isNotEmpty(officeId)) {
                orgName = OrgHelper.getOrgNameByCode(officeId);
            }
            String state = (String) temp.get("state");
            String stateDesc = ErpWorkFlowHelper.stateFlag(state);
            mapResult.put("name", stateDesc + "部门：" + orgName + "；项目名称：" + temp.get("projectName"));
        }
        return mapResult;
    }
}
