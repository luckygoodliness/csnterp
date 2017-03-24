package com.csnt.scdp.bizmodules.modules.workflow.prm.cashrequest;

import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.UserHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("prm_cash_request-start")
@Transactional

public class PrmCashRequestStart extends ERPWorkFlowBaseVariableCollectionAction {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map dataInfo = super.perform(inMap);
        String subjectCode = (String) dataInfo.get("subjectCode");
        String subjectCategory = "";
        if (StringUtil.isNotEmpty(subjectCode)) {
            String sql = "select subject_category from fad_base_subject where subject_no=?";
            List lstParam = new ArrayList();
            lstParam.add(subjectCode);
            DAOMeta daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setLstParams(lstParam);
            List lstResult = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            if (ListUtil.isNotEmpty(lstResult)) {
                subjectCategory = (String) ((Map) lstResult.get(0)).get("subjectCategory");
            }
            if(subjectCategory == null){
                subjectCategory = "";
            }
        }
        Map mapVar = new HashMap();
        mapVar.put("subjectCategory", subjectCategory);
        mapVar.putAll(ErpWorkFlowHelper.filterRoles(inMap, ErpWorkFlowHelper.loadUserInfoByUserID(inMap)));
        String deptCode = UserHelper.getOrgCode();
        boolean isManagerDept = isManagerDept(deptCode);
        mapVar.put("isManagerDept", isManagerDept);
        BigDecimal money = (BigDecimal)dataInfo.get("money");
        if(money == null){
            money = new BigDecimal(0);
        }
        mapVar.put("money", money);
        mapVar.put("isManagerDept", isManagerDept);
        boolean isGeneralManager = UserHelper.getUserId().equals(mapVar.get("公司总经理"));
        mapVar.put("isGeneralManager", isGeneralManager);
        boolean isChairman = UserHelper.getUserId().equals(mapVar.get("董事长"));
        mapVar.put("isChairman", isChairman);
        boolean isSecretary = UserHelper.getUserId().equals(mapVar.get("党委书记"));
        mapVar.put("isSecreP2tary", isSecretary);
        boolean isAccountant = UserHelper.getUserId().equals(mapVar.get("公司总会计师"));
        mapVar.put("isAccountant", isAccountant);
        boolean isManager = UserHelper.getUserId().equals(mapVar.get("部门领导"));
        mapVar.put("isManager", isManager);
        String companyVp = (String)mapVar.get("公司副总经理");
        boolean isCompanyVp = companyVp != null && companyVp.indexOf(UserHelper.getUserId()) != -1;
        mapVar.put("isCompanyVp", isCompanyVp);
        boolean comMember = !isChairman&&!isSecretary&&!isAccountant&&!isCompanyVp&&!isManager&&!isGeneralManager;
        mapVar.put("comMember", comMember);
        boolean isGMApprover = isChairman||isSecretary||isAccountant||isCompanyVp;
        mapVar.put("isGMApprover", isGMApprover);

        return mapVar;
    }

    private boolean isManagerDept(String deptCode) {
        if (deptCode.equals("CSNT_JCB") || deptCode.equals("CSNT_ZCB") || deptCode.equals("CSNT_YGB")
                || deptCode.equals("CSNT_RLZYB") || deptCode.equals("CSNT_GYLB")) {
            return true;
        }
        return false;
    }
}
