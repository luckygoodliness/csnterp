package com.csnt.scdp.bizmodules.modules.workflow.nonprm.budget;

import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.ListUtil;
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
@Controller("non_prm_budget_plan_apply-process-taskname")
@Transactional

public class NonPrmBudgetTaskName extends ERPWorkFlowBaseVariableCollectionAction {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapResult = new HashMap();
        String businessKey = (String) inMap.get(WorkFlowAttribute.BUSINESS_KEY);
        String sql = "select distinct so.org_name,h.year,h.state from non_project_budget_h h inner join scdp_org so on h.office_id = so.org_code where h.uuid=?";
        List lstParam = new ArrayList();
        lstParam.add(businessKey);
        DAOMeta daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setLstParams(lstParam);
        daoMeta.setNeedFilter(false);
        List lstResult = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(lstResult)) {
            Map temp = (Map) lstResult.get(0);
            String state = (String) temp.get("state");
            String stateDesc = ErpWorkFlowHelper.stateFlag(state);
            mapResult.put("name", stateDesc + "部门：" + temp.get("orgName") + "；年度：" + temp.get("year"));
        }
        return mapResult;
    }
}
