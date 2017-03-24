package com.csnt.scdp.bizmodules.modules.nonprm.budget.services.impl;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.entity.nonprm.*;
import com.csnt.scdp.bizmodules.entityattributes.nonprm.*;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.services.intf.BudgetManager;
import com.csnt.scdp.framework.bo.QueryCondition;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpOrg;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.entityattributes.ScdpOrgAttribute;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;
import junit.framework.Test;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:  BudgethManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-24 13:22:12
 */

@Scope("singleton")
@Service("budget-manager")
public class BudgetManagerImpl implements BudgetManager {

    @Override
    public List<NonProjectBudgetH> getObjByOrgCodeAndYear(String orgCode, String year) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(NonProjectBudgetHAttribute.OFFICE_ID, orgCode);
        mapConditions.put(NonProjectBudgetHAttribute.YEAR, year);
        List<NonProjectBudgetH> lstBudgetH = PersistenceFactory.getInstance().
                findByAnyFields(NonProjectBudgetH.class, mapConditions, null);
        return lstBudgetH;
    }

    @Override
    public List<NonProjectBudgetC> getNonProjectBudgetCDetailsByHid(String hid) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(NonProjectBudgetCAttribute.HID, hid);
        List<NonProjectBudgetC> lstBudgetC = PersistenceFactory.getInstance().
                findByAnyFields(NonProjectBudgetC.class, mapConditions, null);
        return lstBudgetC;
    }

    @Override
    public List<NonProjectBudgetC> getPassedNonProjectBudgetC(String orgCode, String year) {

        List<NonProjectBudgetH> budgetHList = getObjByOrgCodeAndYear(orgCode, year);
        List<NonProjectBudgetC> rtn = new ArrayList<NonProjectBudgetC>();
        if (ListUtil.isNotEmpty(budgetHList)) {
            List hids = budgetHList.stream().
                    filter(t -> BillStateAttribute.CMD_BILL_STATE_APPROVED.equals(t.getState())).
                    map(t -> t.getUuid()).distinct().collect(Collectors.toList());
            if (ListUtil.isNotEmpty(hids)) {
                QueryCondition condition1 = new QueryCondition();
                condition1.setField(NonProjectBudgetCAttribute.HID);
                condition1.setOperator("in");
                condition1.setValueList(hids);
                List<QueryCondition> lstCondition = new ArrayList<QueryCondition>();
                lstCondition.add(condition1);
                rtn = PersistenceFactory.getInstance().findByAnyFields(NonProjectBudgetC.class, lstCondition, null);
            }
        }
        return rtn;
    }

    @Override
    public NonProjectBudgetH getPassedNonProjectBudgetH(String orgCode, String year) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(NonProjectBudgetHAttribute.OFFICE_ID, orgCode);
        mapConditions.put(NonProjectBudgetHAttribute.YEAR, year);
        mapConditions.put(NonProjectBudgetHAttribute.STATE, BillStateAttribute.CMD_BILL_STATE_APPROVED);
        List<NonProjectBudgetH> lstBudgetH = PersistenceFactory.getInstance().
                findByAnyFields(NonProjectBudgetH.class, mapConditions, null);
        if (ListUtil.isEmpty(lstBudgetH)) {
            return null;
        } else {
            return lstBudgetH.get(0);
        }
    }

    @Override
    public List<ScdpOrg> getObjsByOrgCode(String orgCode) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(ScdpOrgAttribute.ORG_CODE, orgCode);
        List<ScdpOrg> lstScdpOrg = PersistenceFactory.getInstance().
                findByAnyFields(ScdpOrg.class, mapConditions, null);
        return lstScdpOrg;
    }

    @Override
    public List<NonProjectBudgetCD> getObjsByCid(String cid) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(NonProjectBudgetCDAttribute.CID, cid);
        List<NonProjectBudgetCD> lstBudgetCD = PersistenceFactory.getInstance().
                findByAnyFields(NonProjectBudgetCD.class, mapConditions, null);
        return lstBudgetCD;
    }

    @Override
    public List<NonProjectBudgetAppro> getObjByCid(String cid) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(NonProjectBudgetApproAttribute.BUDGET_CUUID, cid);
        List<NonProjectBudgetAppro> lstBudgetAppro = PersistenceFactory.getInstance().
                findByAnyFields(NonProjectBudgetAppro.class, mapConditions, null);
        return lstBudgetAppro;
    }

    @Override
    public Object save(NonProjectBudgetCD budgetCD) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        return pcm.insert(budgetCD);
    }

    @Override
    public boolean save(NonProjectBudgetAppro budgetAppro) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        try {
            //
            pcm.insert(budgetAppro);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean update(NonProjectBudgetCD budgetCD) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        try {
            //
            pcm.update(budgetCD);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void updateNonProjectBudgetC(NonProjectBudgetC budgetC) {
        PersistenceFactory.getInstance().update(budgetC);
    }

    @Override
    public Object saveNonProjectBudgetC(NonProjectBudgetC budgetC) {
        return PersistenceFactory.getInstance().insert(budgetC);
    }

    @Override
    public boolean update(NonProjectBudgetAppro budgetAppro) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        try {
            //
            pcm.update(budgetAppro);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(NonProjectBudgetCD budgetCD) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        try {
            //
            pcm.delete(budgetCD);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void remove(String budgetCDUUid) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        NonProjectBudgetCD cd = pcm.findByPK(NonProjectBudgetCD.class, budgetCDUUid);
        pcm.delete(cd);
    }

    @Override
    public Map getNonProjectBudgetExecuteInfoByOfficeIdAndYear(String officeId, String year) {
        Map<String, Map> rtnMap = new HashMap<String, Map>();
        DAOMeta daoMeta = new DAOMeta();
        String sql = "select *\n" +
                " from   vw_non_budget_execute t\n" +
                " where  t.office_id='" + officeId + "' and t.year='" + year + "'";
        daoMeta.setStrSql(sql);
        List info = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(info)) {
            info.forEach(t -> {
                String key = (String) ((Map) t).get("uuid");
                rtnMap.put(key, (Map) t);
            });
        }
        return rtnMap;
    }
}