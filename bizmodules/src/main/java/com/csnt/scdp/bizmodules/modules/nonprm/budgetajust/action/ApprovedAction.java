package com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.action;

import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetAjustC;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetC;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetH;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.services.intf.BudgetManager;
import com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.services.intf.BudgetajustManager;
import com.csnt.scdp.framework.commonaction.crud.CommonAddAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.sysmodules.entity.ScdpOrg;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-25 16:43:12
 */

@Scope("singleton")
@Controller("budgetajust-approvedAction")
@Transactional
public class ApprovedAction extends CommonAddAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(ApprovedAction.class);

	@Resource(name = "budgetajust-manager")
	private BudgetajustManager budgetajustManager;

    @Resource(name = "budget-manager")
    private BudgetManager budgetManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {

        List orgNamelst = (List) inMap.get("lstOrgName");//定义数组接收选中部门名称
        List yearlst = (List) inMap.get("lstYear");//定义数组接收选中年份
        String orgName = orgNamelst.get(0).toString();
        //根据部门名称查找部门编号
        List<ScdpOrg> obj = budgetajustManager.getOfficeIdByOrgName(orgName);
//        ScdpOrg obj = PersistenceFactory.getInstance().findByPK(ScdpOrg.class,orgName);
        String orgCode = obj.get(0).getUuid();
        String year = yearlst.get(0).toString();
        if(year != null && orgCode != null){
            List lstUuid = (List) inMap.get("lstUuid");//定义数组接收选中uuid
            if (lstUuid.size() > 0) {
                //获取uuid
                String uuid = lstUuid.get(0).toString();
                //根据hid查找变更预算
                List<NonProjectBudgetAjustC> lstBudgetChanged = budgetajustManager.getBudgetChangedByHid(uuid);
                //根据部门id和年份查找对象
                List<NonProjectBudgetH> budgetCs = budgetajustManager.getObjByOrgCodeAndYear(orgCode,year);
                String hid = budgetCs.get(0).getUuid();
                List<NonProjectBudgetC> lstBudgetC =  budgetManager.getNonProjectBudgetCDetailsByHid(hid);
//                    NonProjectBudgetC budgetC = new NonProjectBudgetC();
                for (int i = 0; i < lstBudgetChanged.size(); i++) {
                    if(lstBudgetChanged.get(i).getBudgetChanged() != null){
//                        String budgetChanged = lstBudgetChanged.get(i).getBudgetChanged().toString();
                        BigDecimal bd = lstBudgetChanged.get(i).getBudgetChanged();
//                        BigDecimal bd = new BigDecimal(budgetChanged);
                        String budgetAjustCFsCode = lstBudgetChanged.get(i).getFinancialSubjectCode();
                        String budgetCFsCode = lstBudgetC.get(i).getFinancialSubjectCode();
                        if (budgetAjustCFsCode != null){
                            if ( budgetAjustCFsCode.equals(budgetCFsCode) ){
                                lstBudgetC.get(i).setThisYearChanged(bd);
                            }
                        }
                    }
                }
            }
        }
        return null;
//		Map out = super.perform(inMap);
//		return out;
	}
}
