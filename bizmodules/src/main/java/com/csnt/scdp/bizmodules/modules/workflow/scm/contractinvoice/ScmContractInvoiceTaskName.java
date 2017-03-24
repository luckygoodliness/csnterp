package com.csnt.scdp.bizmodules.modules.workflow.scm.contractinvoice;

import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("scm_contract_invoice-process-taskname")
@Transactional

public class ScmContractInvoiceTaskName extends ERPWorkFlowBaseVariableCollectionAction {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        inMap.put("dto", "com.csnt.scdp.bizmodules.modules.fad.invoice.dto.FadInvoiceDto");
        Map dataInfo = super.perform(inMap);
        String fadSubjectCode = (String) dataInfo.get("fadSubjectCode");
        String supplierName = (String) dataInfo.get("supplierName");
        BigDecimal expensesMoney = (BigDecimal) dataInfo.get("expensesMoney");
        Map mapResult = new HashMap();
        String state = (String) dataInfo.get("state");
        String stateDesc = ErpWorkFlowHelper.stateFlag(state);
        mapResult.put("name", stateDesc + "课题代号：" + fadSubjectCode + "；开票单位：" + supplierName + "；报销总金额：" + expensesMoney);
        dataInfo.putAll(mapResult);
        return dataInfo;
    }
}
