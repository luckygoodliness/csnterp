package com.csnt.scdp.bizmodules.modules.fad.invoice.action;

import com.csnt.scdp.bizmodules.modules.fad.invoice.services.intf.InvoiceManager;
import com.csnt.scdp.framework.commonaction.crud.CommonQueryAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  QueryAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-25 14:13:12
 */

@Scope("singleton")
@Controller("invoice-getremainbudget")
@Transactional
public class GetRemainBudget extends CommonQueryAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(GetRemainBudget.class);

    @Resource(name = "invoice-manager")
    private InvoiceManager invoiceManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        String isPrm = (String)inMap.get("isPrm");
        String officeCode = (String) inMap.get("officeId");
        String fadYear = (String) inMap.get("fadYear");
        String projectId = (String) inMap.get("projectId");
        BigDecimal balance = new BigDecimal("0");
        String subjectId = "";
        List budgetlist = new ArrayList();
        if("1".equals(isPrm)){//项目
            budgetlist= invoiceManager.getPrmTripBudget(projectId);
            if (!budgetlist.isEmpty() && budgetlist.size() != 0) {
                Map budgetMap = new HashMap((Map) budgetlist.get(0));
                balance = new BigDecimal((budgetMap.get("remainBudget")).toString());//可用额度
                subjectId= budgetMap.get("budgetId").toString();//预算ID
            }
        }else {//非项目
            budgetlist= invoiceManager.getNonTripBudget(officeCode,fadYear);
            if (!budgetlist.isEmpty() && budgetlist.size() != 0) {
                Map budgetMap = new HashMap((Map) budgetlist.get(0));
                balance = new BigDecimal((budgetMap.get("remainBudget")).toString());//可用额度
                subjectId= budgetMap.get("uuid").toString();//预算ID
            }
        }
//        if (!budgetlist.isEmpty() && budgetlist.size() != 0) {
//            Map budgetMap = new HashMap((Map) budgetlist.get(0));
//            balance = new BigDecimal((budgetMap.get("clearanceMoneyU")).toString());//可用额度
//        }
        Map out = new HashMap();
        out.put("remainBudget", balance);
        out.put("subjectId", subjectId);
        //Do After
        return out;
    }
}
