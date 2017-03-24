package com.csnt.scdp.bizmodules.modules.scm.responsiblesubject.services.impl;

import com.csnt.scdp.bizmodules.entity.nonprm.FinancialSubject;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmResponsibleSubjectAttribute;
import com.csnt.scdp.bizmodules.modules.scm.responsiblesubject.services.intf.ResponsiblesubjectManager;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  ResponsiblesubjectManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-22 13:18:08
 */

@Scope("singleton")
@Service("responsiblesubject-manager")
public class ResponsiblesubjectManagerImpl implements ResponsiblesubjectManager {
    @Override
    public void setExtraData(Map outMap) {
        Map dtoMap = (Map) outMap.get("scmResponsibleSubjectDto");
        if (dtoMap != null) {
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            //1.设置负责人的显示
            String principalName = "";
            String principal = (String) dtoMap.get(ScmResponsibleSubjectAttribute.PRINCIPAL);
            if (StringUtil.isNotEmpty(principal)) {
                Map paramMap = new HashMap<>();
                paramMap.put(ScdpUserAttribute.USER_ID, principal);
                List<ScdpUser> list = PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, paramMap, null);
                if (ListUtil.isNotEmpty(list)) {
                    principalName = list.get(0).getUserName();
                }
            }
            dtoMap.put(ScmResponsibleSubjectAttribute.PRINCIPAL_NAME, principalName);
            //2.设置科目的显示
            String subjectCodeName = "";
            String subjectCode = (String) dtoMap.get(ScmResponsibleSubjectAttribute.SUBJECT_CODE);
            if (StringUtil.isNotEmpty(subjectCode)) {
                FinancialSubject financialSubject = pcm.findByPK(FinancialSubject.class, subjectCode);
                if (financialSubject != null) {
                    subjectCodeName = financialSubject.getSubjectName();
                }
            }
            dtoMap.put(ScmResponsibleSubjectAttribute.SUBJECT_CODE_NAME, subjectCodeName);
        }
    }
}