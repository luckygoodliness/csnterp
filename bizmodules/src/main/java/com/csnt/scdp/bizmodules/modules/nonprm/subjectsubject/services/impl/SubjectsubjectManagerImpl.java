package com.csnt.scdp.bizmodules.modules.nonprm.subjectsubject.services.impl;

import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetC;
import com.csnt.scdp.bizmodules.entity.nonprm.SubjectSubject;
import com.csnt.scdp.bizmodules.entityattributes.nonprm.NonProjectBudgetCAttribute;
import com.csnt.scdp.bizmodules.entityattributes.nonprm.NonProjectSubjectSubjectAttribute;
import com.csnt.scdp.bizmodules.modules.nonprm.subjectsubject.services.intf.SubjectsubjectManager;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.sysmodules.entity.ScdpOrg;
import com.csnt.scdp.sysmodules.entityattributes.ScdpOrgAttribute;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  SubjectsubjectManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 18:59:15
 */

@Scope("singleton")
@Service("subjectsubject-manager")
public class SubjectsubjectManagerImpl implements SubjectsubjectManager {

    @Override
    public boolean checkDelete(String uuid) {
        boolean rtnFlag = true;
        //  判断 数据库中是否存在该费用名称是否使用 是则返false  否则true
//        SubjectSubject obj = PersistenceFactory.getInstance().findByPK(SubjectSubject.class, uuid);
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(NonProjectBudgetCAttribute.FINANCIAL_SUBJECT_CODE, uuid);
        List<NonProjectBudgetC> subjectCodes = PersistenceFactory.getInstance().findByAnyFields(NonProjectBudgetC.class, mapConditions, null);
        if (ListUtil.isNotEmpty(subjectCodes)) {
            rtnFlag = false;
        }
        return rtnFlag;
    }

    @Override
    public boolean checkDataUnique(String officeId, String financialSubject) {
        boolean rtnFlag = true;
        //  判断 数据库中是否存在该费用名称和部门id 是则返false  否则true
//        SubjectSubject obj = PersistenceFactory.getInstance().findByPK(SubjectSubject.class, uuid);
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT, financialSubject);
        mapConditions.put(NonProjectSubjectSubjectAttribute.OFFICE_ID, officeId);
        List<SubjectSubject> lstSubjectSubject = PersistenceFactory.getInstance().
                findByAnyFields(SubjectSubject.class, mapConditions, null);
        if (ListUtil.isNotEmpty(lstSubjectSubject)) {
            rtnFlag = false;
        }
        return rtnFlag;
    }

    @Override
    public List<SubjectSubject> getObjByFSCode(String fsCode) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, fsCode);
        List<SubjectSubject> lstSubject = PersistenceFactory.getInstance().
                findByAnyFields(SubjectSubject.class, mapConditions, null);
        return lstSubject;
    }

    @Override
    public List<ScdpOrg> getObjsByOrgCode(String orgCode) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(ScdpOrgAttribute.ORG_CODE, orgCode);
        List<ScdpOrg> lstOrg = PersistenceFactory.getInstance().
                findByAnyFields(ScdpOrg.class, mapConditions, null);
        return lstOrg;
    }

    //获取组织代码
    @Override
    public String getOrgCode(String uuid) {
        ScdpOrg obj = PersistenceFactory.getInstance().findByPK(ScdpOrg.class, uuid);
        String orgCode = obj.getOrgCode();
        return orgCode;
    }

    @Override
    public List<SubjectSubject> getObjects(Map<String, Object> mapCondition) {
        //  判断 数据库中是否存在该费用名称和部门id 是则return  否则保存
        List<SubjectSubject> lstSubjectSubject = PersistenceFactory.getInstance().
                findByAnyFields(SubjectSubject.class, mapCondition, null);
        return lstSubjectSubject;
    }

    /**
     * 根据部门ID查找对象
     *
     * @param officeId 部门ID
     * @return subjectSubject对象
     */
    @Override
    public List<SubjectSubject> getObjsByOfficeId(String officeId) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(NonProjectSubjectSubjectAttribute.OFFICE_ID, officeId);
        List<SubjectSubject> lstSubjectSubject = PersistenceFactory.getInstance().
                findByAnyFields(SubjectSubject.class, mapConditions, null);
        return lstSubjectSubject;
    }

    @Override
    public List<SubjectSubject> getObjsBySubjectName(String subjectName) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT, subjectName);
        List<SubjectSubject> lstSubjectSubject = PersistenceFactory.getInstance().
                findByAnyFields(SubjectSubject.class, mapConditions, null);
        return lstSubjectSubject;
    }

    @Override
    public List<SubjectSubject> getOfficeFixedSubject(String officeId, String fixedKey, String value) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(NonProjectSubjectSubjectAttribute.OFFICE_ID, officeId);
        mapConditions.put(fixedKey, value);
        List<SubjectSubject> lstSubjectSubject = PersistenceFactory.getInstance().
                findByAnyFields(SubjectSubject.class, mapConditions, null);
        return lstSubjectSubject;
    }
}