package com.csnt.scdp.bizmodules.modules.operate.businessbidinfo.action;

import com.csnt.scdp.bizmodules.modules.nonprm.budget.services.intf.BudgetManager;
import com.csnt.scdp.bizmodules.modules.operate.businessbidinfo.services.intf.BusinessbidinfoManager;
import com.csnt.scdp.framework.commonaction.crud.CommonAddAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpOrg;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Reese on 2015/12/28.
 */
@Scope("singleton")
@Controller("businessbidinfo-get")
@Transactional
public class GetDeptNameAction extends CommonAddAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);

    @Resource(name = "businessbidinfo-manager")
    private BusinessbidinfoManager businessbidinfoManager;

    @Resource(name = "budget-manager")
    private BudgetManager budgetManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map map = new HashMap<>();
        String deptCode = (String) inMap.get("deptCode");
        if (StringUtil.isNotEmpty(deptCode)) {
            List<ScdpOrg> lstObj = budgetManager.getObjsByOrgCode(deptCode);
            if (ListUtil.isNotEmpty(lstObj)) {
                String orgName = lstObj.get(0).getOrgName();
                map.put("orgName", orgName);
                return map;
            }
        }
        return null;
    }
}


