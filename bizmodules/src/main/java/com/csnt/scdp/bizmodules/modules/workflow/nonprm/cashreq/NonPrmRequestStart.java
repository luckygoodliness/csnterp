package com.csnt.scdp.bizmodules.modules.workflow.nonprm.cashreq;

import com.csnt.scdp.bizmodules.attributes.ErpWorkFlowAttribute;
import com.csnt.scdp.bizmodules.entity.nonprm.FinancialSubject;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCashReqAttribute;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpOrgHelper;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.UserHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.modules.sys.workflow.action.WorkFlowVariableProcessAction;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Wuhd on 2015/11/9.
 */
@Scope("singleton")
@Controller("non_prm_request-start")
@Transactional

public class NonPrmRequestStart extends ERPWorkFlowBaseVariableCollectionAction {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapData = super.perform(inMap);
        Map mapVar = new HashMap();
//        mapVar.put("subjectCode", mapData.get(FadCashReqAttribute.SUBJECT_CODE));
        String deptCode=(String) inMap.get(ErpWorkFlowAttribute.PROCESS_DEPT_CODE);
        mapVar.put("deptCode", deptCode);
        mapVar.put("money",mapData.get(FadCashReqAttribute.MONEY));
        String subjectCode=(String) mapData.get(FadCashReqAttribute.SUBJECT_CODE);
        String subjectNo= subjectCode.substring(subjectCode.indexOf("-")+1, subjectCode.length());
        Map  condition = new HashMap();
        condition.put("subjectNo",subjectNo);
        List<FinancialSubject> financialSubjectList = PersistenceFactory.getInstance().findByAnyFields(FinancialSubject.class,condition,null);
        String feeType=null;
        if(ListUtil.isNotEmpty(financialSubjectList)){
            feeType=financialSubjectList.get(0).getSubjectCategory();
        }

        mapVar.put("feeType",feeType);

        boolean isManagerDept= !ErpOrgHelper.isBizDivision2(deptCode);
        mapVar.put("isManagerDept",isManagerDept);
        mapVar.putAll(ErpWorkFlowHelper.filterRoles(inMap, ErpWorkFlowHelper.loadUserInfoByUserID(inMap)));
        return mapVar;
    }
}
