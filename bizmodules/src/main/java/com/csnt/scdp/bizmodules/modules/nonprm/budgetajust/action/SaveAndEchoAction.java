package com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.action;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.entity.nonprm.*;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.services.intf.BudgetManager;
import com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.services.intf.BudgetajustManager;
import com.csnt.scdp.bizmodules.modules.nonprm.subjectsubject.services.intf.SubjectsubjectManager;
import com.csnt.scdp.framework.commonaction.crud.CommonAddAction;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.ObjectUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpOrg;
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
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-25 16:43:12
 */

@Scope("singleton")
@Controller("budgetajust-saveandechoAction")
@Transactional
public class SaveAndEchoAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(SaveAndEchoAction.class);

    @Resource(name = "budgetajust-manager")
    private BudgetajustManager budgetajustManager;

    @Resource(name = "budget-manager")
    private BudgetManager budgetManager;

    @Resource(name = "subjectsubject-manager")
    private SubjectsubjectManager subjectsubjectManager;

    //在Budget表中读取数据过来 保存到budgetAjust表中
    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map result = new HashMap<>();
        //获取部门代号
        String orgCode = (String) inMap.get("orgCode");
//        List<ScdpOrg> objs = subjectsubjectManager.getObjsByOrgCode(orgCode);
//        String officeId = objs.get(0).getUuid();
        if (StringUtil.isEmpty(orgCode)){
            throw new BizException("请重新选择部门", new HashMap());
        }
        String year = (String) inMap.get("year");
        //根据部门id和年份查找对象
        List<NonProjectBudgetAjustH> lstBudgetAjustH = budgetajustManager.getObjeByOrgCodeAndYear(orgCode, year, BillStateAttribute.FAD_BILL_STATE_APPROVED);
        if (ListUtil.isEmpty(lstBudgetAjustH)) {
            List<NonProjectBudgetH> lstBudgetH = budgetManager.getObjByOrgCodeAndYear(orgCode, year);
            if (ListUtil.isEmpty(lstBudgetH)) {
                throw new BizException("该部门未做预算", new HashMap());
            } else {
                NonProjectBudgetAjustH budgetAjustH = new NonProjectBudgetAjustH();
                budgetAjustH.setYear(lstBudgetH.get(0).getYear());
                budgetAjustH.setOfficeId(lstBudgetH.get(0).getOfficeId());
//                budgetAjustH.setUuid(lstBudgetH.get(0).getUuid());
                budgetAjustH.setState(lstBudgetH.get(0).getState());
                budgetajustManager.save(budgetAjustH);
                //获取uuid -->得到BudgetC表的hid
                String hidBudgetC = lstBudgetH.get(0).getUuid();
                List<NonProjectBudgetAjustC> lstBudgetAjustC = budgetajustManager.getObjByHid(hidBudgetC);
                if (ListUtil.isEmpty(lstBudgetAjustC)){
                    List<NonProjectBudgetC> lstBudgetC = budgetManager.getNonProjectBudgetCDetailsByHid(hidBudgetC);
                    if (ListUtil.isNotEmpty(lstBudgetC)) {
                        for (int i = 0; i < lstBudgetC.size(); i++) {
                            //费用代码-->subjectsubject的uuid
                            String fsCode = lstBudgetC.get(i).getFinancialSubjectCode();
                            //根据uuid查找费用名称
                            SubjectSubject subjectObj = PersistenceFactory.getInstance()
                                    .findByPK(SubjectSubject.class, fsCode);
                            String subjectName = subjectObj.getFinancialSubject();
                            //原预算金额
                            BigDecimal orrigalBudgetAssigned = lstBudgetC.get(i).getThisYearApplyed();

                            NonProjectBudgetAjustC budgetAjustC = new NonProjectBudgetAjustC();
                            budgetAjustC.setFinancialSubjectCode(fsCode);
                            budgetAjustC.setOrrigalBudgetAssigned(orrigalBudgetAssigned);
                            budgetAjustC.setHid(hidBudgetC);
//                        budgetAjustC.setUuid(lstBudgetC.get(i).getUuid());
                            budgetajustManager.save(budgetAjustC);

                            if ("管理费用".equals(subjectName) || "固定资产添置".equals(subjectName)) {
                                //查找cid
                                String cidBudgetCD = lstBudgetC.get(i).getUuid();
                                List<NonProjectBudgetAjustCD> lstBudgetAjustCD = budgetajustManager.getObjsByCid(cidBudgetCD);
                                if (ListUtil.isEmpty(lstBudgetAjustCD)){
                                    //根据cid查找对象
                                    List<NonProjectBudgetCD> lstBudgetCD = budgetManager.getObjsByCid(cidBudgetCD);
                                    if (lstBudgetCD.size() > 0) {
                                        for (int j = 0; j < lstBudgetCD.size(); j++) {
                                            String cid = lstBudgetCD.get(j).getCid();
                                            String item = lstBudgetCD.get(j).getItem();
                                            BigDecimal amount = lstBudgetCD.get(j).getAmount();
                                            BigDecimal price = lstBudgetCD.get(j).getPrice();
                                            BigDecimal totalValue = lstBudgetCD.get(j).getTotalValue();

                                            NonProjectBudgetAjustCD budgetAjustCD = new NonProjectBudgetAjustCD();
                                            budgetAjustCD.setCid(cid);
                                            budgetAjustCD.setItem(item);
                                            budgetAjustCD.setPrice(price);
                                            budgetAjustCD.setOrrigalBudgetAssigned(totalValue);
                                            budgetAjustCD.setAppliedAmount(amount);
                                            budgetajustManager.save(budgetAjustCD);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return result;
        } else {
            List<NonProjectBudgetH> lstBudgetH = budgetManager.getObjByOrgCodeAndYear(orgCode, year);
            String hidBudgetC = lstBudgetH.get(0).getUuid();
            List<NonProjectBudgetAjustC> lstBudgetAjustC = budgetajustManager.getObjByHid(hidBudgetC);
            if (ListUtil.isEmpty(lstBudgetAjustC)) {
                List<NonProjectBudgetC> lstBudgetC = budgetManager.getNonProjectBudgetCDetailsByHid(hidBudgetC);
                if (lstBudgetC != null) {
                    for (int i = 0; i < lstBudgetC.size(); i++) {
                        //费用代码-->subjectsubject的uuid
                        String fsCode = lstBudgetC.get(i).getFinancialSubjectCode();
                        //根据uuid查找费用名称
                        SubjectSubject subjectObj = PersistenceFactory.getInstance()
                                .findByPK(SubjectSubject.class, fsCode);
                        String subjectName = subjectObj.getFinancialSubject();
                        //原预算金额
                        BigDecimal orrigalBudgetAssigned = lstBudgetC.get(i).getThisYearApplyed();

                        NonProjectBudgetAjustC budgetAjustC = new NonProjectBudgetAjustC();
                        budgetAjustC.setFinancialSubjectCode(fsCode);
                        budgetAjustC.setOrrigalBudgetAssigned(orrigalBudgetAssigned);
                        budgetAjustC.setHid(hidBudgetC);
//                        budgetAjustC.setUuid(lstBudgetC.get(i).getUuid());
                        budgetajustManager.save(budgetAjustC);

                        if ("管理费用".equals(subjectName) || "固定资产添置".equals(subjectName)) {
                            //查找cid
                            String cidBudgetCD = lstBudgetC.get(i).getUuid();
                            List<NonProjectBudgetAjustCD> lstBudgetAjustCD = budgetajustManager.getObjsByCid(cidBudgetCD);
                            if (ListUtil.isEmpty(lstBudgetAjustCD)){
                                //根据cid查找对象
                                List<NonProjectBudgetCD> lstBudgetCD = budgetManager.getObjsByCid(cidBudgetCD);
                                if (lstBudgetCD.size() > 0) {
                                    for (int j = 0; j < lstBudgetCD.size(); j++) {
                                        String cid = lstBudgetCD.get(j).getCid();
                                        String item = lstBudgetCD.get(j).getItem();
                                        BigDecimal amount = lstBudgetCD.get(j).getAmount();
                                        BigDecimal price = lstBudgetCD.get(j).getPrice();
                                        BigDecimal totalValue = lstBudgetCD.get(j).getTotalValue();

                                        NonProjectBudgetAjustCD budgetAjustCD = new NonProjectBudgetAjustCD();
                                        budgetAjustCD.setCid(cid);
                                        budgetAjustCD.setItem(item);
                                        budgetAjustCD.setPrice(price);
                                        budgetAjustCD.setOrrigalBudgetAssigned(totalValue);
                                        budgetAjustCD.setAppliedAmount(amount);
                                        budgetajustManager.save(budgetAjustCD);
                                    }
                                }
                            }
                        }
                    }
                }
            }else {
                for (int x = 0; x<lstBudgetAjustC.size(); x++){
                    List<NonProjectBudgetC> lstBudgetC = budgetManager.getNonProjectBudgetCDetailsByHid(hidBudgetC);
                    NonProjectBudgetAjustC budgetAjustC= lstBudgetAjustC.get(x);
                    String fsCode = budgetAjustC.getFinancialSubjectCode();
                    //根据uuid查找费用名称
                    SubjectSubject subjectObj = PersistenceFactory.getInstance()
                            .findByPK(SubjectSubject.class, fsCode);
                    String subjectName = subjectObj.getFinancialSubject();
                    if ("管理费用".equals(subjectName) || "固定资产添置".equals(subjectName)) {
                        //查找cid
                        String cidBudgetAjustCD = budgetAjustC.getUuid();
                        List<NonProjectBudgetAjustCD> lstBudgetAjustCD = budgetajustManager.getObjsByCid(cidBudgetAjustCD);
                        if (ListUtil.isEmpty(lstBudgetAjustCD)){
                            List<NonProjectBudgetCD> lstBudgetCD = budgetManager.getObjsByCid(cidBudgetAjustCD);
                            if (lstBudgetCD.size() > 0) {
                                for (int j = 0; j < lstBudgetCD.size(); j++) {
                                    String cid = lstBudgetCD.get(j).getCid();
                                    String item = lstBudgetCD.get(j).getItem();
                                    BigDecimal amount = lstBudgetCD.get(j).getAmount();
                                    BigDecimal price = lstBudgetCD.get(j).getPrice();
                                    BigDecimal totalValue = lstBudgetCD.get(j).getTotalValue();

                                    NonProjectBudgetAjustCD budgetAjustCD = new NonProjectBudgetAjustCD();
                                    budgetAjustCD.setCid(cid);
                                    budgetAjustCD.setItem(item);
                                    budgetAjustCD.setPrice(price);
                                    budgetAjustCD.setOrrigalBudgetAssigned(totalValue);
                                    budgetAjustCD.setAppliedAmount(amount);
                                    budgetajustManager.save(budgetAjustCD);
                                }
                            }
                        }
                    }
                }
            }
            return result;
        }
    }
}