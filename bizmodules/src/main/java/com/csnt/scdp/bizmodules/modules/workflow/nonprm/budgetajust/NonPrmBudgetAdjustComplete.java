package com.csnt.scdp.bizmodules.modules.workflow.nonprm.budgetajust;

import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetAjustC;
import com.csnt.scdp.bizmodules.entity.nonprm.SubjectSubject;
import com.csnt.scdp.bizmodules.entityattributes.nonprm.NonProjectBudgetAjustCAttribute;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.nonprm.subjectsubject.dto.NonProjectSubjectSubjectDto;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.bo.QueryCondition;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("non_prm_budget_adjustment-complete")
@Transactional

public class NonPrmBudgetAdjustComplete extends NonPrmBudgetAdjustStart {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapVar = super.perform(inMap);
        Map mapResult = new HashMap();

//        String uuid = (String) mapVar.get("uuid");
//        Map conditions = new HashMap<>();
//        conditions.put(NonProjectBudgetAjustCAttribute.HID, uuid);
//        List<NonProjectBudgetAjustC> nonProjectBudgetAjustCList = PersistenceFactory.getInstance().findByAnyFields(NonProjectBudgetAjustC.class, conditions, null);
//
//        List financialSubjectCodeLst = new ArrayList<>();
//        if (null != nonProjectBudgetAjustCList) {
//            for (int i = 0; i < nonProjectBudgetAjustCList.size(); i++) {
//                financialSubjectCodeLst.add(nonProjectBudgetAjustCList.get(i).getFinancialSubjectCode());
//            }
//        }
//        QueryCondition condition1 = new QueryCondition();
//        condition1.setField("uuid");
//        condition1.setOperator("in");
//        condition1.setValueList(financialSubjectCodeLst);
//        List<QueryCondition> lstCondition = new ArrayList<QueryCondition>();
//        lstCondition.add(condition1);
//        List<SubjectSubject> subjectSubjects = PersistenceFactory.getInstance().findByAnyFields(SubjectSubject.class, lstCondition, null);
//        List subjectCodeList = new ArrayList<>();
//        if (null != subjectSubjects) {
//            for (int i = 0; i < subjectSubjects.size(); i++) {
//                subjectCodeList.add(subjectSubjects.get(i).getFinancialSubjectCode());
//            }
//        }
//        List relevantDepartmentList = new ArrayList<>();
//        if (null != subjectCodeList) {
//            relevantDepartmentList = ErpWorkFlowHelper.loadHandlerDivisionLeader(subjectCodeList);
//        }
//        String relevantDepartment = (String) inMap.get(WorkFlowAttribute.ASSIGNEE_LOWER);
//        if (StringUtil.isNotEmpty(relevantDepartment)) {
//            relevantDepartmentList = StringUtil.splitAsList(relevantDepartment, ";");
//        }
//        mapResult.put("relevantDepartmentList", relevantDepartmentList);
        return mapResult;
    }
}
