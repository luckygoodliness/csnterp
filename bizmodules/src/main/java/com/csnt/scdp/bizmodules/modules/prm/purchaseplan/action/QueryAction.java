package com.csnt.scdp.bizmodules.modules.prm.purchaseplan.action;

import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.services.intf.PurchaseplanManager;
import com.csnt.scdp.framework.commonaction.crud.CommonQueryAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Map;

/**
 * Description:  QueryAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 19:04:01
 */

@Scope("singleton")
@Controller("purchaseplan-query")
@Transactional
public class QueryAction extends CommonQueryAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(QueryAction.class);

    @Resource(name = "purchaseplan-manager")
    private PurchaseplanManager purchaseplanManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
//        List list = (List) out.get("root");
//        if (ListUtil.isNotEmpty(list)) {
//            for (int i = 0; i < list.size(); i++) {
//                Map prmPurchasePlanMap = null;
//                if (list.get(i) != null) {
//                    prmPurchasePlanMap = (Map) list.get(i);
//                }
//                PrmProjectMain projectMain = purchaseplanManager.findBudgetByProjectMainId(prmPurchasePlanMap.get("prmProjectMainId").toString());
//                prmPurchasePlanMap.put("projectName", projectMain.getProjectName());
//                prmPurchasePlanMap.put("manager", projectMain.getProjectManager());
//                String custName = purchaseplanManager.getCustmerNameByUuid(projectMain.getCustomerId());
//                prmPurchasePlanMap.put("customer", custName);
//                prmPurchasePlanMap.put("projectMoney", projectMain.getProjectMoney());
//                String departmentName = purchaseplanManager.getDepartmentNameByCode(projectMain.getContractorOffice
//                        ());
//                prmPurchasePlanMap.put("department", departmentName);
//            }
//            purchaseplanManager.setExtraData(out);
//        }
        //Do After
        return out;
    }
}
