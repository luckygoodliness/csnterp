package com.csnt.scdp.bizmodules.modules.fad.invoice.action;

import com.csnt.scdp.bizmodules.entity.scm.ScmContract;
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
@Controller("invoice-getpurchasedetailstampprojectcode")
@Transactional
public class GetPurchaseDetailStampProjectCode implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(GetPurchaseDetailStampProjectCode.class);

    @Resource(name = "invoice-manager")
    private InvoiceManager invoiceManager;
    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map rsMap = new HashMap();
        List scmContractMapList = new ArrayList();
        //发票录入id和合同id
        String fadInvoiceUuid = (String)inMap.get("fadInvoiceUuid");
        String scmContractCode = (String)inMap.get("scmContractCode");
        if (StringUtil.isNotEmpty(scmContractCode)) {
            // 根据根据前台传进来的合同代号，逐条取出来对应的采购明细的标记位，有标记位的要用标记源的课题代号覆盖原来的课题代号
            String strSql = "SELECT distinct SC.SCM_CONTRACT_CODE, \n" +
                    "       PPRD.IS_STAMP,\n" +
                    "       PPRD.STAMP_PROJECT_UUID,\n" +
                    "       SC.UUID as Scuuid,\n" +
                    "       M.PROJECT_CODE\n" +
                    "  from  SCM_CONTRACT SC \n" +
                    "  left join PRM_PURCHASE_REQ_DETAIL PPRD \n" +
                    "  on SC.UUID = PPRD.SCM_CONTRACT_ID\n" +
                    "  LEFT JOIN PRM_PROJECT_MAIN M \n" +
                    "  ON PPRD.STAMP_PROJECT_UUID = M.UUID\n" +
                    "  WHERE SC.SCM_CONTRACT_CODE = '" + scmContractCode + "'";
            DAOMeta daoMeta = new DAOMeta(strSql);
            List resultList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            if (ListUtil.isNotEmpty(resultList)) {
               for (int i = 0; i < resultList.size(); i++) {
                    Map result = (Map) resultList.get(i);
                    String scmContractId = (String) result.get("scmContractId");
                    String isStamp = (String) result.get("isStamp");
                    String projectCode = (String) result.get("projectCode");
                    Map scmContractMap = new HashMap();
                    scmContractMap.put("scmContractId", scmContractId);
                    scmContractMap.put("isStamp", isStamp);
                    scmContractMap.put("projectCode", projectCode);
                    scmContractMapList.add(scmContractMap);
                }
                rsMap.put("scmContractMapList", scmContractMapList);
            }
        }
        return rsMap;
    }
}
