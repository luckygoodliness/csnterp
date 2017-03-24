package com.csnt.scdp.bizmodules.modules.nonprm.budget.services.intf;

import com.csnt.scdp.bizmodules.entity.nonprm.*;
import com.csnt.scdp.sysmodules.entity.ScdpOrg;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Description:  BudgethManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-24 13:22:12
 */


@Scope("singleton")
@Service("budget-budgetmanager")
@Transactional
public interface BudgetManager {

    /**
     * 根据部门编号和年份查找对象
     *
     * @param orgCode 部门编号
     * @param year    年份
     * @return NonProjectBudgetH对象
     */
    List<NonProjectBudgetH> getObjByOrgCodeAndYear(String orgCode, String year);

    /**
     * 根据hid查找对象
     *
     * @param hid
     * @return
     */
    List<NonProjectBudgetC> getNonProjectBudgetCDetailsByHid(String hid);

    List<NonProjectBudgetC> getPassedNonProjectBudgetC(String orgCode, String year);

    NonProjectBudgetH getPassedNonProjectBudgetH(String orgCode, String year);

    /**
     * 根据orgCode查找对象 拿部门id
     *
     * @param orgCode
     * @return
     */
    List<ScdpOrg> getObjsByOrgCode(String orgCode);

    /**
     * 根据cid查找BudgetCD对象
     *
     * @param cid
     * @return
     */
    List<NonProjectBudgetCD> getObjsByCid(String cid);

    List<NonProjectBudgetAppro> getObjByCid(String cid);

    /**
     * @param budgetCD
     */
    Object save(NonProjectBudgetCD budgetCD);

    boolean save(NonProjectBudgetAppro budgetAppro);

    boolean update(NonProjectBudgetCD budgetCD);

    void updateNonProjectBudgetC(NonProjectBudgetC budgetC);

    Object saveNonProjectBudgetC(NonProjectBudgetC budgetC);

    boolean update(NonProjectBudgetAppro budgetAppro);

    /**
     * 删除对象
     *
     * @param budgetCD
     * @return
     */
    boolean delete(NonProjectBudgetCD budgetCD);

    void remove(String budgetCDUUid);

    Map getNonProjectBudgetExecuteInfoByOfficeIdAndYear(String officeId, String year);
}