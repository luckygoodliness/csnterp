package com.csnt.scdp.bizmodules.modules.fad.invoice.action;

import com.csnt.scdp.bizmodules.modules.fad.invoice.services.intf.InvoiceManager;
import com.csnt.scdp.framework.commonaction.crud.CommonQueryAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
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
@Controller("invoice-Validation")
@Transactional
public class Validation extends CommonQueryAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(Validation.class);

    @Resource(name = "invoice-manager")
    private InvoiceManager invoiceManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
//        String isPrm = (String) inMap.get("isPrm");
//        String expensesType = (String) inMap.get("expensesType");
        String subjectId = (String) inMap.get("subjectId");
        String userCode = (String) inMap.get("userCode");
        String notInRow = (String) inMap.get("notInRow");
        int num = 0;
//        List budgetlist = new ArrayList();
        Map out = new HashMap();
        if (StringUtil.isNotEmpty(subjectId) && StringUtil.isNotEmpty(userCode)) {
            DAOMeta daoMeta = new DAOMeta();
            String reqSql = "SELECT 1 " +
                    "FROM (SELECT (SELECT SUM(FCRI.CLEARANCE_MONEY) " +
                    "FROM FAD_CASH_REQ_INVOICE FCRI " +
                    "WHERE T.UUID = FCRI.FAD_CASH_REQ_ID " +
                    "AND (EXISTS (SELECT 1 " +
                    "FROM FAD_INVOICE F " +
                    "WHERE F.UUID = FCRI.FAD_INVOICE_ID " +
                    "AND F.STATE <> '3') OR EXISTS " +
                    "(SELECT 1 " +
                    "FROM FAD_CASH_CLEARANCE F " +
                    "WHERE F.UUID = FCRI.FAD_INVOICE_ID " +
                    "AND F.STATE <> '3'))) AS CLEARANCED_MONEY, " +
                    "T.MONEY " +
                    "FROM FAD_CASH_REQ T " +
                    "WHERE STATE = '4' " +
                    "AND T.STAFF_ID ='" + userCode + "' " +
                    "AND BUDGET_C_UUID = '" + subjectId + "' " +
                    "AND UUID NOT IN (" + notInRow + ")) T " +
                    "WHERE(NVL(T.MONEY, 0) - NVL(T.CLEARANCED_MONEY, 0)) > 0";
            daoMeta.setStrSql(reqSql);
            List<Map<String, Object>> reqList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            if (ListUtil.isNotEmpty(reqList)) {
                num = reqList.size();
            }
        }

        out.put("rownum", num);
        return out;
    }
}
