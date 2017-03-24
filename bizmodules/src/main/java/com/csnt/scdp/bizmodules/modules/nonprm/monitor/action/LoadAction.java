package com.csnt.scdp.bizmodules.modules.nonprm.monitor.action;

import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectExtraItem;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectIncome;
import com.csnt.scdp.bizmodules.modules.nonprm.income.services.intf.IncomeManager;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.nonprm.monitor.services.intf.MonitorManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  LoadAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-27 15:12:59
 */

@Scope("singleton")
@Controller("monitor-load")
@Transactional
public class LoadAction extends CommonLoadAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

	@Resource(name = "monitor-manager")
	private MonitorManager monitorManager;
    @Resource(name = "income-manager")
    private IncomeManager incomeManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
//        Map out = super.perform(inMap);//主
        Map out = new HashMap<>();
        Map incomeMap = new HashMap<>();//装预算支出
        String year = (String)inMap.get("uuid");//选取的年份
//        String year = "2015";
        String state = "2";

        //根据类型查找摘要名称
        String typeBudgetIn = "0";
        String typeBudgetOut = "1";
        List<NonProjectExtraItem> lstBudgetIn= incomeManager.getNamesByType(typeBudgetIn);
        List<NonProjectExtraItem> lstBudgetOut= incomeManager.getNamesByType(typeBudgetOut);
        out.put("year",year);
        List lstIncome2 = new ArrayList<>();
        if (lstBudgetIn != null){
            for ( int i = 0; i < lstBudgetIn.size() ; i++ ){
                String subjectName = lstBudgetIn.get(i).getName();
                Map<String,Object> income2 = new HashMap<>();
//                if ("收款计划".equals(subjectName)){
//                    //根据年份查找当年计划收款金额
//                    List lstSchemingReceiptMoney = incomeManager.getSchemingReceiptsByYear(year);
//                    Map planCollection = (Map)lstSchemingReceiptMoney.get(0);
//                    BigDecimal srmoney = (BigDecimal)planCollection.get("srmoney");
//                    income2.put("appliedValue",srmoney);
//                }
//                if("结题计划".equals(subjectName)){
//                    //根据年份查找当年结题计划 SCHEMING_SQUARE_MONEY
//                    List lstSchemingSquareMoney = incomeManager.getSchemingSquareByYear(year);
//                    Map planCollection = (Map)lstSchemingSquareMoney.get(0);
//                    BigDecimal ssmoney = (BigDecimal)planCollection.get("ssmoney");
//                    income2.put("appliedValue",ssmoney);
//                }
                income2.put("subject", subjectName);
                lstIncome2.add(income2);
            }
            incomeMap.put("nonProjectIncome2Dto",lstIncome2);
        }

        List lstIncomes = new ArrayList<>();//创建集合装行对象
        //第一个行对象
        if (lstBudgetOut != null){
            //根据年份查找部门非项目支出的值
//            List lstDeptPayOut = incomeManager.getObjectsByYearAndType(year, state);
//            Map budgetC = (Map)lstDeptPayOut.get(0);
//            BigDecimal lastOcc = (BigDecimal)budgetC.get("lastoccured");
//            BigDecimal applyed = (BigDecimal)budgetC.get("applyed");
//            BigDecimal firstInstance = (BigDecimal)budgetC.get("instanced");
//            BigDecimal assigned = (BigDecimal)budgetC.get("assigned");
            String subName = lstBudgetOut.get(0).getName();

            Map<String,Object> nonBudgetC =  new HashMap<>();
//            nonBudgetC.put("occurredValue",lastOcc);
//            nonBudgetC.put("appliedValue",applyed);
//            nonBudgetC.put("firstInstance",firstInstance);
//            nonBudgetC.put("assignedValue",assigned);
            nonBudgetC.put("subject",subName);
            lstIncomes.add(nonBudgetC);
            //第二行开始 firstInstance appliedValue assignedValue occurredValue
            for (int i= 1; i<lstBudgetOut.size(); i++){
                String subjectName = lstBudgetOut.get(i).getName();
                Map<String,Object> income = new HashMap<>();
                //根据费用名称和年份查找其他费用的值
                List<NonProjectIncome> lstValue  = incomeManager.getObjsBySubjectAndYear(subjectName,year);

                if(!("部门非项目支出".equals(subjectName))){
//                    BigDecimal applyedValue = lstValue.get(0).getAppliedValue();
//                    BigDecimal instanceValue = lstValue.get(0).getFirstInstance();
//                    BigDecimal assignedValue = lstValue.get(0).getAssignedValue();
//                    BigDecimal occurredValue = lstValue.get(0).getOccurredValue();
//                    income.put("appliedValue",applyedValue);
//                    income.put("firstInstance",instanceValue);
//                    income.put("assignedValue",assignedValue);
//                    income.put("occurredValue",occurredValue);
                    income.put("subject",subjectName);
                }
                lstIncomes.add(income);
            }
            incomeMap.put("nonProjectIncomeDto",lstIncomes);
        }
        //Do before
        out.put("nonProjectIncomeBalanceDto",incomeMap);
        //Do After
        return out;
	}
}
