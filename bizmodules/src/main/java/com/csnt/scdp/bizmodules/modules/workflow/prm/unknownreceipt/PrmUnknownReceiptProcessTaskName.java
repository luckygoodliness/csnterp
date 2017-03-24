package com.csnt.scdp.bizmodules.modules.workflow.prm.unknownreceipt;

import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("prm_receipt_confirmation-process-taskname")
@Transactional

public class PrmUnknownReceiptProcessTaskName extends ERPWorkFlowBaseVariableCollectionAction {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapResult = new HashMap();
//        String businessKey = (String) inMap.get(WorkFlowAttribute.BUSINESS_KEY);
//        String sql = "select c.customer_name From prm_unknown_receipts a left join prm_customer c on a.department_code = c.customer_code and a.uuid=?";
//        List lstParam=new ArrayList();
//        lstParam.add(businessKey);
//        DAOMeta daoMeta = new DAOMeta();
//        daoMeta.setStrSql(sql);
//        daoMeta.setLstParams(lstParam);
//        daoMeta.setNeedFilter(false);
//        List lstResult = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
//        if(ListUtil.isNotEmpty(lstResult)){
//            Map temp = (Map)lstResult.get(0);
//            mapResult.put("name", "付款单位：" + temp.get("customerName"));
//        }
        return mapResult;
    }
}
