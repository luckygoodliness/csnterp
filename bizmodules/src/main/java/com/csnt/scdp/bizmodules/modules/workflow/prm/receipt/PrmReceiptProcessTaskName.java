package com.csnt.scdp.bizmodules.modules.workflow.prm.receipt;

import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
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
@Controller("prm_receipt-process-taskname")
@Transactional

public class PrmReceiptProcessTaskName extends ERPWorkFlowBaseVariableCollectionAction {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapResult = new HashMap();
        String businessKey = (String) inMap.get(WorkFlowAttribute.BUSINESS_KEY);
        String sql = "select a.state,c.customer_name, b.project_name From PRM_RECEIPTS a left join prm_customer c on a.customer_id = c.uuid" +
                " left join prm_project_main b on a.prm_project_main_id = b.uuid where a.uuid=?";
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
            if (StringUtil.isNotEmpty(temp.get("customerName"))) {
                mapResult.put("name", stateDesc + "所属项目：" + temp.get("projectName") + "; 业主单位：" + temp.get("customerName"));
            } else {
                mapResult.put("name", stateDesc + "所属项目：" + temp.get("projectName"));
            }
        }
        return mapResult;
    }
}
