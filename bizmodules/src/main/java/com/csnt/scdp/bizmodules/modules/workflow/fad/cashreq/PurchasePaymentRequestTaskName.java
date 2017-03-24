package com.csnt.scdp.bizmodules.modules.workflow.fad.cashreq;

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
@Controller("scm_purchasepaymentrequestcontract-process-taskname")
@Transactional

public class PurchasePaymentRequestTaskName extends ERPWorkFlowBaseVariableCollectionAction {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapResult = new HashMap();
        String businessKey = (String) inMap.get(WorkFlowAttribute.BUSINESS_KEY);
        String sql = "select f.state,f.office_id,f.project_name,s.scm_contract_code,f.supplier_name from fad_cash_req f inner join scm_contract s on  f.purchase_contract_id=s.uuid where f.uuid=?";
        List lstParam = new ArrayList();
        lstParam.add(businessKey);
        DAOMeta daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setLstParams(lstParam);
        daoMeta.setNeedFilter(false);
        List lstResult = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(lstResult)) {
            Map temp = (Map) lstResult.get(0);
            String orgCode = (String) temp.get("officeId");
            String orgName = null;
            if (StringUtil.isNotEmpty(orgCode)) {
                orgName = OrgHelper.getOrgNameByCode(orgCode);
            }
            String scmContractCode = (String) temp.get("scmContractCode");
            String supplierName = (String) temp.get("supplierName");
            String projectName = (String) temp.get("projectName");
            String state = (String) temp.get("state");
            String stateDesc = ErpWorkFlowHelper.stateFlag(state);
            mapResult.put("name", stateDesc + "部门：" + orgName + "；采购合同：" + scmContractCode + "；合同供应商：" + supplierName + "；所属项目：" + projectName);
        }
        return mapResult;
    }
}

