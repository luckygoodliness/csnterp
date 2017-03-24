package com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.services.intf;

import com.csnt.scdp.bizmodules.entity.nonprm.*;
import com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.dto.NonProjectBudgetAjustCDDto;
import com.csnt.scdp.sysmodules.entity.ScdpOrg;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Description:  BudgetajustManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-25 16:43:13
 */
@Scope("singleton")
@Service("budgetajust-budgetajustmanager")
@Transactional
public interface BudgetajustManager {
    /**
     * 根据年度和部门获取非项目预算执行情况
     *
     * @param year     年度信息
     * @param officeId 部门的代码
     * @return 返回一个Map，以预算代码为Key
     */
    Map<String, Map> getNonBudgetExecute(String year, String officeId);

    /**
     * 根据hid查找对象
     *
     * @param hid
     * @return
     */
    public List<NonProjectBudgetAjustC> getObjByHid(String hid);

    /**
     * 根据cid获取对象
     *
     * @param cid
     * @return
     */
    public List<NonProjectBudgetCD> getObjByCid(String cid);

    /**
     * 根据cid获取对象
     *
     * @param cid
     * @return
     */
    List<NonProjectBudgetAjustCD> getAjustCDByCid(String cid);

    /**
     * 根据费用代码获取uuid
     *
     * @param fsCode
     * @return
     */
    List<NonProjectBudgetC> getUuidByFsCode(String fsCode);

    /**
     * 根据费用代码获取当年申请额
     *
     * @param fsCode
     * @return
     */
    List<NonProjectBudgetC> getApplyedByFSCode(String fsCode);

    /**
     * 根据费用代码获取原预算金额
     *
     * @param fsCode
     * @return
     */
    List<NonProjectBudgetAjustC> getObjByFSCode(String fsCode);

    /**
     * 根据费用代码获取费用名称
     *
     * @param fsCode 费用代码
     * @return 费用名称集合对象
     */
    List<SubjectSubject> getSubjectNameByFSCode(String fsCode);

    /**
     * 根据uuid查找部门编号
     *
     * @param uuid
     * @return
     */
    String getFSCodeByUuid(String uuid);

    /**
     * 根据部门id查找费用名称
     *
     * @param officeId
     * @return
     */
    List<SubjectSubject> getSubjectNameByOfficeId(String officeId);

    /**
     * 根据部门id和年份查找对象
     *
     * @param officeId
     * @param year
     * @return
     */
    List<NonProjectBudgetH> getObjByOrgCodeAndYear(String officeId, String year);


    /**
     * 根据部门id和年份查找对象
     *
     * @param officeId
     * @param year
     * @return
     */
    List<NonProjectBudgetAjustH> getObjeByOrgCodeAndYear(String officeId, String year,String state);

    NonProjectBudgetAjustH getNonProjectBudgetAjustHById(String uuid);

    /**
     * 根据部门名称查找部门id
     *
     * @param orgName
     * @return
     */
    List<ScdpOrg> getOfficeIdByOrgName(String orgName);

    /**
     * 根据hid查找变更预算
     *
     * @param hid
     * @return
     */
    List<NonProjectBudgetAjustC> getBudgetChangedByHid(String hid);

    /**
     * 根据cid查找对象
     *
     * @param cid
     * @return
     */
    List<NonProjectBudgetAjustCD> getObjsByCid(String cid);

    List<NonProjectBudgetAjustCDDto> getObjectByCid(String cid);


    /**
     * 保存数据到数据库
     *
     * @param ajustC NonProjectBudgetAjustC对象
     * @return
     */
    boolean save(NonProjectBudgetAjustC ajustC);

    boolean save(NonProjectBudgetAjustH budgetAjustH);

    boolean save(NonProjectBudgetC budgetC);

    boolean save(NonProjectBudgetAjustCD ajustCD);

    boolean update(NonProjectBudgetAjustCD ajustCD);

    //根据hid查找对象
    List<NonProjectBudgetAjustC> getObjeByHid(String hid);

    /**
     * 根据费用科目代号获取uuid
     *
     * @param fsCode
     * @return
     */
    List<NonProjectBudgetAjustC> getUuidByFsCode1(String fsCode);

    List<NonProjectBudgetAjustC> getObjByFsCodeAndHid(String fsCode, String hid);

    List<NonProjectBudgetAjustCD> getObjByItem(String item);

    /**
     * 删除对象
     *
     * @param budgetAjustCD
     * @return
     */
    boolean delete(NonProjectBudgetAjustCD budgetAjustCD);

    /**
     * 根据hid和费用代码查找对象
     *
     * @param hid
     * @param fsCode
     * @return
     */
    List<NonProjectBudgetAjustC> getObjByHidAndFsCode(String hid, String fsCode);
}