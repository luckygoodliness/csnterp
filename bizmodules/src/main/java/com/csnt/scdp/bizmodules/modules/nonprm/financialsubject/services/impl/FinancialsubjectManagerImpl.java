package com.csnt.scdp.bizmodules.modules.nonprm.financialsubject.services.impl;

import com.csnt.scdp.bizmodules.entity.nonprm.FinancialSubject;
import com.csnt.scdp.bizmodules.entity.nonprm.SubjectSubject;
import com.csnt.scdp.bizmodules.entityattributes.nonprm.NonProjectSubjectSubjectAttribute;
import com.csnt.scdp.bizmodules.modules.nonprm.financialsubject.services.intf.FinancialsubjectManager;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.util.ListUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Description:  FinancialsubjectManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 10:37:44
 */

@Scope("singleton")
@Service("financialsubject-manager")
@Transactional
public class FinancialsubjectManagerImpl implements FinancialsubjectManager {
    private FinancialSubject financialSubject;
    private SubjectSubject subjectSubject;

    public void setFinancialSubject(FinancialSubject financialSubject) {
        this.financialSubject = financialSubject;
    }

    public void setSs(SubjectSubject subjectSubject) {
        this.subjectSubject = subjectSubject;
    }

    @Override
    public boolean checkDelete(String uuid) {
        boolean rtnFlag = true;
        //  判断 数据库中是否存在该费用名称是否使用 是则返false  否则true
        FinancialSubject obj = PersistenceFactory.getInstance().findByPK(FinancialSubject.class, uuid);
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT, obj.getSubjectName());
        List<SubjectSubject> subjectNames = PersistenceFactory.getInstance().findByAnyFields(SubjectSubject.class, mapConditions, null);
        if (ListUtil.isNotEmpty(subjectNames)) {
            rtnFlag = false;
        }
        return rtnFlag;
    }

    @Override
    public boolean save(SubjectSubject subject) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        try {
            //
            Object rtn = pcm.insert(subject);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Map<String, Object>> getPrmSubjectCodes() {

        List lstParam = new ArrayList();
//        lstParam.add("1");
        DAOMeta daoMeta = DAOHelper.getDAO("financialsubject-dao", "fm_financial_subject_no", lstParam);
        List<Map<String, Object>> lstSubject = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        return lstSubject;

    }
}