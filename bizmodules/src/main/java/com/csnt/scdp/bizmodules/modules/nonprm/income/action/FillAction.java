package com.csnt.scdp.bizmodules.modules.nonprm.income.action;

import com.csnt.scdp.bizmodules.entity.nonprm.*;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.dto.NonProjectBudgetCDto;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.dto.NonProjectBudgetHDto;
import com.csnt.scdp.bizmodules.modules.nonprm.income.services.intf.IncomeManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.CommonLoadAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.sysmodules.entity.ScdpOrg;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description:  LoadAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-24 17:37:27
 */

@Scope("singleton")
@Controller("income-filldataAction")
@Transactional
public class FillAction extends CommonLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(FillAction.class);

    @Resource(name = "income-manager")
    private IncomeManager incomeManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map out = new HashMap<>();
        //根据类型查找摘要名称
        String typeBudgetIn = "0";
        String typeBudgetOut = "1";
        List<String> needDeptIn = new ArrayList<String>();
        needDeptIn.add("经营合同计划");
        needDeptIn.add("收款计划");
        needDeptIn.add("结题计划");
        List<NonProjectExtraItem> lstBudgetInC = incomeManager.getNamesByType(typeBudgetIn);
        List<NonProjectExtraItem> lstBudgetOut = incomeManager.getNamesByType(typeBudgetOut);
        List<NonProjectIncomeIn> incomeInLst = new ArrayList<NonProjectIncomeIn>();
        List<ScdpOrg> orgs = incomeManager.getAllBizDept();
        lstBudgetInC.forEach(t -> {
            NonProjectIncomeIn in = new NonProjectIncomeIn();
            in.setSubject(t.getName());
            incomeInLst.add(in);

            if (needDeptIn.contains(t.getName())) {
                for (ScdpOrg org : orgs) {
                    NonProjectIncomeIn in2 = new NonProjectIncomeIn();
                    in2.setSubject(t.getName());
                    in2.setSubjectOfficeId(org.getOrgCode());
                    in2.setSubjectOfficeName(org.getOrgName());
                    incomeInLst.add(in2);
                }
            }
        });

        String state = "2";
        // 创建 Calendar 对象
        Calendar calendar = Calendar.getInstance();
        // 显示年份
        int y = calendar.get(Calendar.YEAR);
        String year = Integer.toString(y);
        //部门非项目支出各种金额的总值
        List lstObj = incomeManager.getObjectsByYearAndState(year, state);
        //查找当年上报值，初审值，终审值
        List<Object> lstValues = new ArrayList<Object>();
        if (ListUtil.isNotEmpty(lstBudgetOut)){
            for (int i = 0; i < lstBudgetOut.size(); i++) {
                String subject = lstBudgetOut.get(i).getName();
                List<NonProjectIncome> lstValue = incomeManager.getObjsBySubjectAndYear(subject, year);
                if (ListUtil.isNotEmpty(lstValue)){
                    lstValues.add(lstValue);
                }
            }
        }
        //获取当年计划收款金额
        List schemingReceiptMoney = incomeManager.getSchemingReceiptsByYear(year);
        //获取当年结题计划金额
        List schemingSquare= incomeManager.getSchemingSquareByYear(year);
        out.put("sumValue", lstObj);//部门非项目支出所有费用
        out.put("lstValues", lstValues);//其他各项费用
        out.put("schemingReceiptMoney", schemingReceiptMoney);//当年计划收款
        out.put("schemingSquare",schemingSquare);//当年结题计划
        out.put("namesIn", incomeInLst);//预算收入
        out.put("namesOut", lstBudgetOut);//预算支出
        //Do After
        return out;
    }
}