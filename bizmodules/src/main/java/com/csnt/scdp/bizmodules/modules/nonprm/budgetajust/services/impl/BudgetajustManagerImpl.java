package com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.services.impl;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.entity.nonprm.*;
import com.csnt.scdp.bizmodules.entityattributes.nonprm.*;
import com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.dto.NonProjectBudgetAjustCDDto;
import com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.services.intf.BudgetajustManager;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpOrg;
import com.csnt.scdp.sysmodules.entityattributes.ScdpOrgAttribute;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:  BudgetajustManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-25 16:43:13
 */

@Scope("singleton")
@Service("budgetajust-manager")
public class BudgetajustManagerImpl implements BudgetajustManager {
    @Override
    public Map<String, Map> getNonBudgetExecute(String year, String officeId) {
        Map<String, Map> rtnMap = new HashMap<String, Map>();
        DAOMeta daoMeta = new DAOMeta();
        String sql = "select t.* from VW_NON_BUDGET_EXECUTE t WHERE  t.year = '" + year + "' and office_id='" + officeId + "'";
        daoMeta.setStrSql(sql);
        List lstObjInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(lstObjInfo)) {
            lstObjInfo.forEach(n -> rtnMap.put(((Map) n).get("subjectCode").toString(), (Map) n));
        }
        return rtnMap;
    }

    @Override
    public List<NonProjectBudgetAjustC> getObjByHid(String hid) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(NonProjectBudgetAjustCAttribute.HID, hid);
        List<NonProjectBudgetAjustC> lstBudgetAjustC = PersistenceFactory.getInstance().
                findByAnyFields(NonProjectBudgetAjustC.class, mapConditions, null);
        return lstBudgetAjustC;
    }

    /**
     * 根据cid获取对象
     *
     * @param cid
     * @return
     */
    @Override
    public List<NonProjectBudgetCD> getObjByCid(String cid) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(NonProjectBudgetCDAttribute.CID, cid);
        List<NonProjectBudgetCD> lstBudgetCD = PersistenceFactory.getInstance().
                findByAnyFields(NonProjectBudgetCD.class, mapConditions, null);
        return lstBudgetCD;
    }

    /**
     * 根据cid获取对象
     *
     * @param cid
     * @return
     */
    @Override
    public List<NonProjectBudgetAjustCD> getAjustCDByCid(String cid) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(NonProjectBudgetAjustCDAttribute.CID, cid);
        List<NonProjectBudgetAjustCD> lstBudgetCD = PersistenceFactory.getInstance().
                findByAnyFields(NonProjectBudgetAjustCD.class, mapConditions, null);
        return lstBudgetCD;
    }

    /**
     * 根据费用科目代号获取uuid
     *
     * @param fsCode
     * @return
     */
    @Override
    public List<NonProjectBudgetC> getUuidByFsCode(String fsCode) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(NonProjectBudgetCAttribute.FINANCIAL_SUBJECT_CODE, fsCode);
        List<NonProjectBudgetC> lstUuid = PersistenceFactory.getInstance().
                findByAnyFields(NonProjectBudgetC.class, mapConditions, null);
        return lstUuid;
    }

    /**
     * 根据费用科目代号获取uuid
     *
     * @param fsCode
     * @return
     */
    @Override
    public List<NonProjectBudgetAjustC> getUuidByFsCode1(String fsCode) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(NonProjectBudgetAjustCAttribute.FINANCIAL_SUBJECT_CODE, fsCode);
        List<NonProjectBudgetAjustC> lstUuid = PersistenceFactory.getInstance().
                findByAnyFields(NonProjectBudgetAjustC.class, mapConditions, null);
        return lstUuid;
    }

    @Override
    public List<NonProjectBudgetAjustC> getObjByFsCodeAndHid(String fsCode, String hid) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(NonProjectBudgetAjustCAttribute.FINANCIAL_SUBJECT_CODE, fsCode);
        mapConditions.put(NonProjectBudgetAjustCAttribute.HID, hid);
        List<NonProjectBudgetAjustC> lstObj = PersistenceFactory.getInstance().
                findByAnyFields(NonProjectBudgetAjustC.class, mapConditions, null);
        return lstObj;
    }

    @Override
    public List<NonProjectBudgetAjustCD> getObjByItem(String item) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(NonProjectBudgetAjustCDAttribute.ITEM, item);
        List<NonProjectBudgetAjustCD> lstObj = PersistenceFactory.getInstance().
                findByAnyFields(NonProjectBudgetAjustCD.class, mapConditions, null);
        return lstObj;
    }


    /**
     * 根据费用代码查找当年申请
     *
     * @param fsCode 费用代码
     * @return
     */
    @Override
    public List<NonProjectBudgetC> getApplyedByFSCode(String fsCode) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(NonProjectBudgetCAttribute.FINANCIAL_SUBJECT_CODE, fsCode);
        List<NonProjectBudgetC> lstApplyed = PersistenceFactory.getInstance().
                findByAnyFields(NonProjectBudgetC.class, mapConditions, null);
//        String aa = lstApplyed.get(0).getThisYearApplyed();
        return lstApplyed;
    }

    /**
     * 根据费用代码查找原预算金额
     *
     * @param fsCode 费用代码
     * @return
     */
    @Override
    public List<NonProjectBudgetAjustC> getObjByFSCode(String fsCode) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(NonProjectBudgetAjustCAttribute.FINANCIAL_SUBJECT_CODE, fsCode);
        List<NonProjectBudgetAjustC> lstApplyed = PersistenceFactory.getInstance().
                findByAnyFields(NonProjectBudgetAjustC.class, mapConditions, null);
//        String aa = lstApplyed.get(0).getThisYearApplyed();
        return lstApplyed;
    }

    /**
     * 根据费用代码查找费用名称
     *
     * @param fsCode 费用代码
     * @return
     */
    @Override
    public List<SubjectSubject> getSubjectNameByFSCode(String fsCode) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, fsCode);
        List<SubjectSubject> lstSubjectNames = PersistenceFactory.getInstance().
                findByAnyFields(SubjectSubject.class, mapConditions, null);
        return lstSubjectNames;
    }

    /**
     * 根据uuid查找部门编号
     *
     * @param uuid
     * @return
     */
    @Override
    public String getFSCodeByUuid(String uuid) {
        NonProjectBudgetH obj = PersistenceFactory.getInstance().findByPK(NonProjectBudgetH.class, uuid);
        String officeId = obj.getOfficeId();
        return officeId;
    }

    /**
     * 根据部门id查找费用名称
     *
     * @param officeId
     * @return
     */
    @Override
    public List<SubjectSubject> getSubjectNameByOfficeId(String officeId) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(NonProjectSubjectSubjectAttribute.OFFICE_ID, officeId);
        List<SubjectSubject> lstSubjectName = PersistenceFactory.getInstance().
                findByAnyFields(SubjectSubject.class, mapConditions, null);
        return lstSubjectName;
    }

    /**
     * 根据部门id和年份查找对象
     *
     * @param officeId
     * @param year
     * @return
     */
    @Override
    public List<NonProjectBudgetH> getObjByOrgCodeAndYear(String officeId, String year) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(NonProjectBudgetHAttribute.OFFICE_ID, officeId);
        mapConditions.put(NonProjectBudgetHAttribute.YEAR, year);
        List<NonProjectBudgetH> lstBudgetAjust = PersistenceFactory.getInstance().
                findByAnyFields(NonProjectBudgetH.class, mapConditions, null);
        return lstBudgetAjust;
    }

    /**
     * 根据部门id和年份查找对象
     *
     * @param officeId
     * @param year
     * @return
     */
    @Override
    public List<NonProjectBudgetAjustH> getObjeByOrgCodeAndYear(String officeId, String year, String state) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(NonProjectBudgetAjustHAttribute.OFFICE_ID, officeId);
        mapConditions.put(NonProjectBudgetAjustHAttribute.YEAR, year);
        if (StringUtil.isNotEmpty(state)) {
            mapConditions.put(NonProjectBudgetAjustHAttribute.STATE, BillStateAttribute.FAD_BILL_STATE_APPROVED);
        }
        List<NonProjectBudgetAjustH> lstBudgetAjustH = PersistenceFactory.getInstance().
                findByAnyFields(NonProjectBudgetAjustH.class, mapConditions, null);
        return lstBudgetAjustH;
    }

    @Override
    public NonProjectBudgetAjustH getNonProjectBudgetAjustHById(String uuid) {
        return PersistenceFactory.getInstance().findByPK(NonProjectBudgetAjustH.class, uuid);
    }

    /**
     * 根据hid查找变更预算
     *
     * @param hid
     * @return
     */
    @Override
    public List<NonProjectBudgetAjustC> getBudgetChangedByHid(String hid) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(NonProjectBudgetCAttribute.HID, hid);
        List<NonProjectBudgetAjustC> lstBudgetChanged = PersistenceFactory.getInstance().
                findByAnyFields(NonProjectBudgetAjustC.class, mapConditions, null);
        return lstBudgetChanged;
    }

    @Override
    public List<NonProjectBudgetAjustCD> getObjsByCid(String cid) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(NonProjectBudgetAjustCDAttribute.CID, cid);
        List<NonProjectBudgetAjustCD> lstBudgetChanged = PersistenceFactory.getInstance().
                findByAnyFields(NonProjectBudgetAjustCD.class, mapConditions, null);
        return lstBudgetChanged;
    }

    @Override
    public List<NonProjectBudgetAjustCDDto> getObjectByCid(String cid) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(NonProjectBudgetAjustCDAttribute.CID, cid);
        List<NonProjectBudgetAjustCDDto> lstBudgetChanged = PersistenceFactory.getInstance().
                findByAnyFields(NonProjectBudgetAjustCDDto.class, mapConditions, null);
        return lstBudgetChanged;
    }

    /**
     * 根据部门名称查找部门id
     *
     * @param orgName
     * @return
     */
    @Override
    public List<ScdpOrg> getOfficeIdByOrgName(String orgName) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(ScdpOrgAttribute.ORG_NAME, orgName);
        List<ScdpOrg> lstOfficeId = PersistenceFactory.getInstance().
                findByAnyFields(ScdpOrg.class, mapConditions, null);
        return lstOfficeId;
    }


    @Override
    public boolean save(NonProjectBudgetC budgetC) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        try {
            //
            Object rtn = pcm.insert(budgetC);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean save(NonProjectBudgetAjustCD ajustCD) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        try {
            //
            Object rtn = pcm.insert(ajustCD);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean update(NonProjectBudgetAjustCD ajustCD) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        try {
            //
            pcm.update(ajustCD);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean save(NonProjectBudgetAjustC ajustC) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        try {
            //
            Object rtn = pcm.insert(ajustC);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean save(NonProjectBudgetAjustH budgetAjustH) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        try {
            //
            Object rtn = pcm.insert(budgetAjustH);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<NonProjectBudgetAjustC> getObjeByHid(String hid) {
        List<NonProjectBudgetAjustC> lstBudgetAjustC1 = new ArrayList<NonProjectBudgetAjustC>();
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(NonProjectBudgetAjustCAttribute.HID, hid);
        lstBudgetAjustC1 = PersistenceFactory.getInstance().
                findByAnyFields(NonProjectBudgetAjustC.class, mapConditions, null);
        return lstBudgetAjustC1;
    }

    @Override
    public boolean delete(NonProjectBudgetAjustCD budgetAjustCD) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        try {
            //
            pcm.delete(budgetAjustCD);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<NonProjectBudgetAjustC> getObjByHidAndFsCode(String hid, String fsCode) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(NonProjectBudgetCAttribute.HID, hid);
        mapConditions.put(NonProjectBudgetCAttribute.FINANCIAL_SUBJECT_CODE, fsCode);
        List<NonProjectBudgetAjustC> lstBudgetAjustC = PersistenceFactory.getInstance().
                findByAnyFields(NonProjectBudgetAjustC.class, mapConditions, null);
        return lstBudgetAjustC;
    }
}