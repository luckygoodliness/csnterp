package com.csnt.scdp.bizmodules.modules.nonprm.income.services.impl;

import com.csnt.scdp.bizmodules.attributes.MessageKeyAttribute;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetC;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectExtraItem;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectIncome;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectIncomeIn;
import com.csnt.scdp.bizmodules.entityattributes.nonprm.NonProjectExtraItemAttribute;
import com.csnt.scdp.bizmodules.entityattributes.nonprm.NonProjectIncome2Attribute;
import com.csnt.scdp.bizmodules.entityattributes.nonprm.NonProjectIncomeAttribute;
import com.csnt.scdp.bizmodules.entityattributes.nonprm.NonProjectIncomeInAttribute;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpOrgHelper;
import com.csnt.scdp.bizmodules.modules.nonprm.income.dto.NonProjectIncomeDto;
import com.csnt.scdp.bizmodules.modules.nonprm.income.dto.NonProjectIncomeInDto;
import com.csnt.scdp.bizmodules.modules.nonprm.income.services.intf.IncomeManager;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.helper.MessageHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpOrg;
import com.csnt.scdp.sysmodules.entityattributes.ScdpOrgAttribute;
import com.fr.stable.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:  IncomeManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-24 17:37:28
 */

@Scope("singleton")
@Service("income-manager")
public class IncomeManagerImpl implements IncomeManager {
    private static ILogTracer tracer = LogTracerFactory.getInstance(IncomeManagerImpl.class);

    /**
     * 根据类型查找摘要名称
     *
     * @param type 类型
     * @return 摘要名称集合对象
     */
    @Override
    public List<NonProjectExtraItem> getNamesByType(String type) {
        String sqlNo = "seq_no asc";
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(NonProjectExtraItemAttribute.TYPE, type);
        List<NonProjectExtraItem> lstIncome = PersistenceFactory.getInstance().
                findByAnyFields(NonProjectExtraItem.class, mapConditions, sqlNo);
        return lstIncome;
    }

    /**
     * @param year  年份为当前年份
     * @param state 申请状态为“2” 为通过
     * @return
     */
    @Override
    public List getObjectsByYearAndState(String year, String state) {
        DAOMeta daoMeta = new DAOMeta();
        String sql = "SELECT SUM(c.this_year_applyed) as applyed,\n" +
                "sum(c.THIS_YEAR_FIRST_INSTANCE) as instanced,\n" +
                "sum(c.THIS_YEAR_ASSIGNED) as assigned,\n" +
                "sum(c.THIS_YEAR_OCCURED) as occured,\n" +
                "sum(c.LAST_YEAR_OCCURED) as lastOccured \n" +
                "FROM   non_project_budget_c c \n" +
                "left join non_project_budget_h h \n" +
                "on  c.hid = h.uuid \n" +
                "where 1=1 and h.year=" + year + " AND h.state = " + state;

        daoMeta.setStrSql(sql);
        List lstObjInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        return lstObjInfo;
    }

    @Override
    public List getObjsByYearAndState(String year, String state) {
        DAOMeta daoMeta = new DAOMeta();
        String sql = "SELECT c.this_year_applyed as applyed,\n" +
                "c.THIS_YEAR_FIRST_INSTANCE as instanced,\n" +
                "c.THIS_YEAR_ASSIGNED as assigned,\n" +
                "c.THIS_YEAR_OCCURED as occured,\n" +
                "c.LAST_YEAR_OCCURED as lastOccured,\n" +
                "c.department_code AS deptCode \n" +
                "FROM   non_project_budget_c c \n" +
                "left join non_project_budget_h h \n" +
                "on  c.hid = h.uuid \n" +
                "where h.year=" + year + " AND h.state = " + state;
        daoMeta.setStrSql(sql);
        List lstObjInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        return lstObjInfo;
    }

    @Override
    public List getObjectByYear(Map inMap) {
        String year = StringUtil.replaceNull(inMap.get("uuid"));
        int y = Integer.parseInt(year);
        int lastY = y - 1;
        String lastYear = Integer.toString(lastY);
        List lstParam = new ArrayList();
        lstParam.add(year);
        lstParam.add(lastYear);
        DAOMeta daoMeta = DAOHelper.getDAO("income-dao", "query_dept_non_project_income_expenses", lstParam);
        List lstValues = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        return lstValues;
    }

    @Override
    public List getObjsByOrgCodeAndYear(String orgCode, String year) {
        List lstParam = new ArrayList();
        lstParam.add(orgCode);
        lstParam.add(year);
        DAOMeta daoMeta = DAOHelper.getDAO("income-dao", "query_total_applied", lstParam);
        List lstValues = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        return lstValues;
    }

    @Override
    public List getObjectsByYear(String y) {
//        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
//        DAOMeta daoMeta = DAOHelper.getDAO("income-dao", "query_lastyear_occurred", null);
        int yy = Integer.parseInt(y);
        int lastY = yy - 1;
        String lastYear = Integer.toString(lastY);
        DAOMeta daoMeta = new DAOMeta();
        String sql = "select t.*, p.occurred occ\n" +
                "from (select * \n" +
                " from non_project_income_in \n" +
                " where year = " + y + ") t \n" +
                " left join (select * \n" +
                " from non_project_income_in income \n" +
                "left join (select sum(m.occurred_value) occurred, m.puuid \n" +
                "from non_project_income_mon m \n" +
                "group by m.puuid) im \n" +
                "on income.uuid = im.puuid \n" +
                "where year = " + lastYear + ") p \n" +
                " on t.subject = p.subject \n" +
                " and nvl(t.subject_office_id, 0) = nvl(p.subject_office_id, 0)";
        daoMeta.setStrSql(sql);
        List lstObjInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        return lstObjInfo;
    }

    @Override
    public List getObjectByYear(String y) {
        int yy = Integer.parseInt(y);
        int lastY = yy - 1;
        String lastYear = Integer.toString(lastY);
        DAOMeta daoMeta = new DAOMeta();
        String sql = "select t.*, p.occurred_value occ \n" +
                "from (select * \n" +
                " from non_project_income \n" +
                " where year = " + y + ") t \n" +
                " left join (select * \n" +
                " from non_project_income income \n" +
                "left join (select sum(m.occurred_value), m.puuid \n" +
                "from non_project_income_mon m \n" +
                "group by m.puuid) im \n" +
                "on income.uuid = im.puuid \n" +
                "where year = " + lastYear + ") p \n" +
                " on t.subject = p.subject \n" +
                " and nvl(t.subject_office_id, 0) = nvl(p.subject_office_id, 0)";
        daoMeta.setStrSql(sql);
        List lstObjInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        return lstObjInfo;
    }


    /**
     * 当年计划收款金额
     *
     * @param year
     * @return
     */
    @Override
    public List getSchemingReceiptsByYear(String year) {
        DAOMeta daoMeta = new DAOMeta();
        String sql = "SELECT SUM(PC.SCHEMING_RECEIPTS_MONEY) as SRMONEY\n" +
                "FROM   PRM_RECEIPTS_DETAIL_P_C PC \n" +
                "where 1=1 and to_char(PC.SCHEMING_RECEIPTS_DATE,'yyyy')=TO_CHAR(" + year + ")";
//                "where 1=1 and to_char(PC.SCHEMING_RECEIPTS_DATE,'yyyy')=TO_CHAR(SYSDATE,'YYYY')";
        daoMeta.setStrSql(sql);
        List lstObjInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        return lstObjInfo;
    }

    //当年结算计划金额
    @Override
    public List getSchemingSquareByYear(String year) {
        DAOMeta daoMeta = new DAOMeta();
        String sql = "SELECT SUM(DP.SCHEMING_SQUARE_MONEY) as SSMONEY \n" +
                "FROM   PRM_SQUARE_DETAIL_P DP \n" +
                "where 1=1 and to_char(DP.SCHEMING_SQUARE_DATE,'yyyy')=TO_CHAR(" + year + ")";
        daoMeta.setStrSql(sql);
        List lstObjInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        return lstObjInfo;
    }


    @Override
    public boolean save(NonProjectIncome obj) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        Object rtn = pcm.insert(obj);
        if (rtn != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean save(NonProjectIncomeIn obj) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        Object rtn = pcm.insert(obj);
        if (rtn != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public NonProjectIncome getNonProjectIncomeByUUid(String UUid) {
        return PersistenceFactory.getInstance().findByPK(NonProjectIncome.class, UUid);
    }

    @Override
    public NonProjectIncomeIn getNonProjectIncomeInByUUid(String UUid) {
        return PersistenceFactory.getInstance().findByPK(NonProjectIncomeIn.class, UUid);
    }

    /**
     * 根据年份和费用科目查找当年的各项费用
     *
     * @param subject
     * @return
     */
    @Override
    public List<NonProjectIncome> getObjsBySubjectAndYear(String subject, String year) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(NonProjectIncomeAttribute.SUBJECT, subject);
        mapConditions.put(NonProjectIncomeAttribute.YEAR, year);
        List<NonProjectIncome> lstIncome = PersistenceFactory.getInstance().
                findByAnyFields(NonProjectIncome.class, mapConditions, null);
        return lstIncome;
    }

    @Override
    public List<NonProjectIncomeIn> getObjsByYear(String year) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(NonProjectIncomeInAttribute.YEAR, year);
        List<NonProjectIncomeIn> lstIncomeIn = PersistenceFactory.getInstance().
                findByAnyFields(NonProjectIncomeIn.class, mapConditions, null);
        return lstIncomeIn;
    }

    @Override
    public List<NonProjectIncome> getObjByYear(String year) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(NonProjectIncomeAttribute.YEAR, year);
        List<NonProjectIncome> lstIncome = PersistenceFactory.getInstance().
                findByAnyFields(NonProjectIncome.class, mapConditions, null);
        return lstIncome;
    }

    @Override
    public List<ScdpOrg> getAllBizDept() {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(ScdpOrgAttribute.ORG_TYPE, "D");
        List<ScdpOrg> lstOrg = PersistenceFactory.getInstance().
                findByAnyFields(ScdpOrg.class, mapConditions, null);
        return lstOrg.stream().filter(t -> ErpOrgHelper.isBizDivision(t.getUuid())).collect(Collectors.toList());
    }

    @Override
    public boolean update(NonProjectIncome income) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        pcm.update(income);
        return true;
    }

    @Override
    public boolean update(NonProjectIncomeIn incomeIn) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        pcm.update(incomeIn);
        return true;
    }
    /**
     *
     * @param year
     * @param type
     * @return
     */
//    @Override
//    public List getLastOrrByYearAndType(String year, String type) {
//        DAOMeta daoMeta = new DAOMeta();
//        String sql = "SELECT sum(c.THIS_YEAR_OCCURED) as occured \n" +
//                "FROM   non_project_budget_c c \n" +
//                "left join non_project_budget_h h \n" +
//                "on  c.hid = h.uuid \n" +
//                "where 1=1 and h.year=" + year +" AND h.state = " + type;
//
//        daoMeta.setStrSql(sql);
//        List lstObjInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
//        return lstObjInfo;
//    }
}