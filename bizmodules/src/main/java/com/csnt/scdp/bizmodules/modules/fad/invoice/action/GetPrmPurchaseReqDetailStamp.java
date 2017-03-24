package com.csnt.scdp.bizmodules.modules.fad.invoice.action;

import com.csnt.scdp.bizmodules.modules.fad.invoice.services.intf.InvoiceManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 15:32:42
 */

@Scope("singleton")
@Controller("invoice-getprmpurchasereqdetailstamp")
@Transactional
public class GetPrmPurchaseReqDetailStamp implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(GetPrmPurchaseReqDetailStamp.class);

    @Resource(name = "invoice-manager")
    private InvoiceManager invoiceManager;
    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map rsMap = new HashMap();
        List scmContractMapList = new ArrayList();
        String fadInvoiceUuid = (String)inMap.get("fadInvoiceUuid");
        if (StringUtil.isNotEmpty(fadInvoiceUuid)) {
            // 根据一组发票号逐条取出来对应的采购明细的标记位
            String strSql = "SELECT distinct SCI.SCM_CONTRACT_ID,PPRD.IS_STAMP\n" +
                    "       from \n" +
                    "       FAD_INVOICE F \n" +
                    "       left join\n" +
                    "       SCM_CONTRACT_INVOICE  SCI\n" +
                    "       on F.UUID = SCI.FAD_INVOICE_ID\n" +
                    "       left join\n" +
                    "       SCM_CONTRACT SC\n" +
                    "       on SC.UUID = SCI.SCM_CONTRACT_ID\n" +
                    "       left join\n" +
                    "       PRM_PURCHASE_REQ_DETAIL PPRD\n" +
                    "       on  SC.UUID = PPRD.SCM_CONTRACT_ID \n" +
                    "  WHERE F.Uuid = '" + fadInvoiceUuid + "'";
            DAOMeta daoMeta = new DAOMeta(strSql);
            List resultList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            if (ListUtil.isNotEmpty(resultList)) {
               for (int i = 0; i < resultList.size(); i++) {
                    Map result = (Map) resultList.get(i);
                    String scmContractId = (String) result.get("scmContractId");
                    String isStamp = (String) result.get("isStamp");
                    Map scmContractMap = new HashMap();
                    scmContractMap.put("scmContractId", scmContractId);
                    scmContractMap.put("isStamp", isStamp);
                    scmContractMapList.add(scmContractMap);
                }
                rsMap.put("scmContractMapList", scmContractMapList);
            }
        }
        return rsMap;
    }
}
