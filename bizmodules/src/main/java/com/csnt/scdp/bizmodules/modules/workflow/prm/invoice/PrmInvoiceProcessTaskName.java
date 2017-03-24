package com.csnt.scdp.bizmodules.modules.workflow.prm.invoice;

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
@Controller("prm_invoice-process-taskname")
@Transactional

public class PrmInvoiceProcessTaskName extends ERPWorkFlowBaseVariableCollectionAction {
    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapResult = new HashMap();
        String businessKey = (String) inMap.get(WorkFlowAttribute.BUSINESS_KEY);
        String sql = "select a.state,c.customer_name,b.project_name,b.project_code From PRM_BILLING a left join prm_customer c on a.customer_id = c.uuid " +
                "left join prm_project_main b on a.prm_project_main_id = b.uuid where a.uuid = ?";
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
            if(temp.get("customerName")!=null){
                mapResult.put("name", stateDesc + "所属项目：" + temp.get("projectName") +"; 项目代号：" + temp.get("projectCode") + "; 业主单位：" + temp.get("customerName"));
            }else {
                mapResult.put("name", stateDesc + "所属项目：" + temp.get("projectName") +"; 项目代号：" + temp.get("projectCode") + "; 业主单位：(无)");
            }
        }
        return mapResult;
    }
}
