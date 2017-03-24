package com.csnt.scdp.bizmodules.modules.nonprm.income.services.intf;

import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetC;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectExtraItem;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectIncome;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectIncomeIn;
import com.csnt.scdp.bizmodules.modules.nonprm.income.dto.NonProjectIncomeDto;
import com.csnt.scdp.bizmodules.modules.nonprm.income.dto.NonProjectIncomeInDto;
import com.csnt.scdp.sysmodules.entity.ScdpOrg;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Description:  IncomeManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-24 17:37:28
 */
public interface IncomeManager {

    /**
     * 根据类型查找摘要名称
     *
     * @param type 类型
     * @return 摘要名称集合对象
     */
    public List<NonProjectExtraItem> getNamesByType(String type);

    /**
     * 查找当年所有部门的各种费用总值
     *
     * @param year  年份为当前年份
     * @param state 申请状态为“2” 为通过
     * @return
     */
    public List getObjectsByYearAndState(String year, String state);

    /**
     * 查找当年所有部门的各种费用
     *
     * @param year  年份为当前年份
     * @param state 申请状态为“2” 为通过
     * @return
     */
    public List getObjsByYearAndState(String year, String state);

    /**
     * 查前一年发生的所有部门总值
     * @param year
     * @param type
     * @return
     */
//    public List getLastOrrByYearAndType(String year,String type);

    /**
     * 当年计划收款金额
     *
     * @param year
     * @return
     */
    public List getSchemingReceiptsByYear(String year);

    /**
     * 当年结算计划金额
     *
     * @param year
     * @return
     */
    public List getSchemingSquareByYear(String year);

    /**
     * @param obj
     * @return
     */
    public boolean save(NonProjectIncome obj);

    NonProjectIncome getNonProjectIncomeByUUid(String UUid);

    NonProjectIncomeIn getNonProjectIncomeInByUUid(String UUid);

    /**
     * @param obj
     * @return
     */
    public boolean save(NonProjectIncomeIn obj);

    /**
     * 根据年份和费用科目查找当年的各项费用
     *
     * @param subject
     * @return
     */
    public List<NonProjectIncome> getObjsBySubjectAndYear(String subject, String year);

    /**
     * 根据年份查找NonProjectIncomeIn表里面有没有当年数据
     *
     * @param year
     * @return
     */
    public List<NonProjectIncomeIn> getObjsByYear(String year);

    public List<NonProjectIncome> getObjByYear(String year);

    /**
     * 根据部门编号和年份查找当年各种费用的总和
     *
     * @param orgCode
     * @param year
     * @return
     */
    public List getObjsByOrgCodeAndYear(String orgCode, String year);

    /**
     * @param year
     * @return
     */
    public List getObjectsByYear(String year);

    public List getObjectByYear(Map inMap);

    public List getObjectByYear(String year);

    /**
     * 根据puuid查找对象
     *
     * @return
     */
    public List<ScdpOrg> getAllBizDept();

    /**
     * @param income
     * @return
     */
    public boolean update(NonProjectIncome income);

    public boolean update(NonProjectIncomeIn incomeIn);
}