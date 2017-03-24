package com.csnt.scdp.bizmodules.modules.fad.certificatesetrule.action;

import com.csnt.scdp.bizmodules.entityattributes.nonprm.NonProjectSubjectSubjectAttribute;
import com.csnt.scdp.bizmodules.modules.common.action.ErpLoadAction;
import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
import com.csnt.scdp.bizmodules.modules.nonprm.subjectsubject.dto.NonProjectSubjectSubjectDto;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.helper.DtoHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.fad.certificatesetrule.services.intf.CertificatesetruleManager;
import com.csnt.scdp.sysmodules.entity.ScdpOrg;
import com.csnt.scdp.bizmodules.entity.fad.FadRtfreevalueView;
import com.csnt.scdp.bizmodules.entity.fad.FadAccsubjRtfreevalue;
import com.csnt.scdp.sysmodules.entityattributes.ScdpOrgAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadRtfreevalueViewAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadAccsubjRtfreevalueAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadNonProjectSetRuleAttribute;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import com.csnt.scdp.framework.core.persistence.PersistenceFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  LoadAction
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-09-23 21:16:46
 */

@Scope("singleton")
@Controller("nonprojectsetrule-load")
@Transactional
public class NonProjectSetRuleLoadAction extends ErpLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(NonProjectSetRuleLoadAction.class);

    @Resource(name = "certificatesetrule-manager")
    private CertificatesetruleManager certificatesetruleManager;

    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        //Do After

        Map fadNonProjectSetRuleMap = (Map) out.get("fadNonProjectSetRuleDto");

        //非项目费用科目名称显示
        if (certificateManager.isNullReturnEmpty(fadNonProjectSetRuleMap.get(FadNonProjectSetRuleAttribute.FINANCIAL_SUBJECT)).length() > 0) {
            String financialSubjectCode = certificateManager.isNullReturnEmpty(fadNonProjectSetRuleMap.get(FadNonProjectSetRuleAttribute.FINANCIAL_SUBJECT));
            String financialSubject = "";
            Map<String, Object> nonProjectSubjectSubjectConditionsMap = new HashMap<String, Object>();
            nonProjectSubjectSubjectConditionsMap.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, financialSubjectCode);
            List nonProjectSubjectSubjectList = certificateManager.isNullReturnEmpty(financialSubjectCode).length() > 0 ? DtoHelper.findByAnyFields(NonProjectSubjectSubjectDto.class, nonProjectSubjectSubjectConditionsMap, null) : new ArrayList<>();
            if (nonProjectSubjectSubjectList.size() > 0) {
                NonProjectSubjectSubjectDto nonProjectSubjectSubject = (NonProjectSubjectSubjectDto) nonProjectSubjectSubjectList.get(0);
                financialSubject = certificateManager.isNullReturnEmpty(nonProjectSubjectSubject.getFinancialSubject());
            }
            fadNonProjectSetRuleMap.put("financialSubjectName", financialSubjectCode + "[" + financialSubject + "]");
        }

        //报销凭证借方科目名称显示
        if (certificateManager.isNullReturnEmpty(fadNonProjectSetRuleMap.get(FadNonProjectSetRuleAttribute.DEBTOR_SUBJECT4A51)).length() > 0) {
            String subjectCode = certificateManager.isNullReturnEmpty(fadNonProjectSetRuleMap.get(FadNonProjectSetRuleAttribute.DEBTOR_SUBJECT4A51));
            String subjectName = "";
            Map<String, Object> fadAccsubjRtfreevalueConditionsMap = new HashMap<String, Object>();
            fadAccsubjRtfreevalueConditionsMap.put(FadAccsubjRtfreevalueAttribute.SUBJECT_CODE, subjectCode);
            List<FadAccsubjRtfreevalue> fadAccsubjRtfreevalueList = certificateManager.isNullReturnEmpty(subjectCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadAccsubjRtfreevalue.class, fadAccsubjRtfreevalueConditionsMap, null) : new ArrayList<>();
            if (fadAccsubjRtfreevalueList.size() > 0) {
                FadAccsubjRtfreevalue fadAccsubjRtfreevalue = (FadAccsubjRtfreevalue) fadAccsubjRtfreevalueList.get(0);
                subjectName = certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getSubjectName());
            }
            fadNonProjectSetRuleMap.put("debtorSubject4a51Name", subjectName);
        }

        //请款凭证借方科目(电汇/现金)名称显示
        if (certificateManager.isNullReturnEmpty(fadNonProjectSetRuleMap.get(FadNonProjectSetRuleAttribute.DEBTOR_SUBJECT1)).length() > 0) {
            String subjectCode = certificateManager.isNullReturnEmpty(fadNonProjectSetRuleMap.get(FadNonProjectSetRuleAttribute.DEBTOR_SUBJECT1));
            String subjectName = "";
            Map<String, Object> fadAccsubjRtfreevalueConditionsMap = new HashMap<String, Object>();
            fadAccsubjRtfreevalueConditionsMap.put(FadAccsubjRtfreevalueAttribute.SUBJECT_CODE, subjectCode);
            List<FadAccsubjRtfreevalue> fadAccsubjRtfreevalueList = certificateManager.isNullReturnEmpty(subjectCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadAccsubjRtfreevalue.class, fadAccsubjRtfreevalueConditionsMap, null) : new ArrayList<>();
            if (fadAccsubjRtfreevalueList.size() > 0) {
                FadAccsubjRtfreevalue fadAccsubjRtfreevalue = (FadAccsubjRtfreevalue) fadAccsubjRtfreevalueList.get(0);
                subjectName = certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getSubjectName());
            }
            fadNonProjectSetRuleMap.put("debtorSubject1Name", subjectName);
        }

        //请款凭证借方科目(付汇等)名称显示
        if (certificateManager.isNullReturnEmpty(fadNonProjectSetRuleMap.get(FadNonProjectSetRuleAttribute.DEBTOR_SUBJECT2)).length() > 0) {
            String subjectCode = certificateManager.isNullReturnEmpty(fadNonProjectSetRuleMap.get(FadNonProjectSetRuleAttribute.DEBTOR_SUBJECT2));
            String subjectName = "";
            Map<String, Object> fadAccsubjRtfreevalueConditionsMap = new HashMap<String, Object>();
            fadAccsubjRtfreevalueConditionsMap.put(FadAccsubjRtfreevalueAttribute.SUBJECT_CODE, subjectCode);
            List<FadAccsubjRtfreevalue> fadAccsubjRtfreevalueList = certificateManager.isNullReturnEmpty(subjectCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadAccsubjRtfreevalue.class, fadAccsubjRtfreevalueConditionsMap, null) : new ArrayList<>();
            if (fadAccsubjRtfreevalueList.size() > 0) {
                FadAccsubjRtfreevalue fadAccsubjRtfreevalue = (FadAccsubjRtfreevalue) fadAccsubjRtfreevalueList.get(0);
                subjectName = certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getSubjectName());
            }
            fadNonProjectSetRuleMap.put("debtorSubject2Name", subjectName);
        }

        //报销、请款凭证贷方科目名称显示
        if (certificateManager.isNullReturnEmpty(fadNonProjectSetRuleMap.get(FadNonProjectSetRuleAttribute.CREDITOR_SUBJECT4A51A1A2)).length() > 0) {
            String subjectCode = certificateManager.isNullReturnEmpty(fadNonProjectSetRuleMap.get(FadNonProjectSetRuleAttribute.CREDITOR_SUBJECT4A51A1A2));
            String subjectName = "";
            Map<String, Object> fadAccsubjRtfreevalueConditionsMap = new HashMap<String, Object>();
            fadAccsubjRtfreevalueConditionsMap.put(FadAccsubjRtfreevalueAttribute.SUBJECT_CODE, subjectCode);
            List<FadAccsubjRtfreevalue> fadAccsubjRtfreevalueList = certificateManager.isNullReturnEmpty(subjectCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadAccsubjRtfreevalue.class, fadAccsubjRtfreevalueConditionsMap, null) : new ArrayList<>();
            if (fadAccsubjRtfreevalueList.size() > 0) {
                FadAccsubjRtfreevalue fadAccsubjRtfreevalue = (FadAccsubjRtfreevalue) fadAccsubjRtfreevalueList.get(0);
                subjectName = certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getSubjectName());
            }
            fadNonProjectSetRuleMap.put("creditorSubject4a51a1a2Name", subjectName);
        }

        //报销、请款凭证贷方辅助核算项名称显示
        if (certificateManager.isNullReturnEmpty(fadNonProjectSetRuleMap.get(FadNonProjectSetRuleAttribute.CREDITOR_RTFREE4A51A1A2)).length() > 0) {
            String accountInfoCode = certificateManager.isNullReturnEmpty(fadNonProjectSetRuleMap.get(FadNonProjectSetRuleAttribute.CREDITOR_RTFREE4A51A1A2));
            String accountInfoName = "";
            Map<String, Object> fadRtfreevalueViewConditionsMap = new HashMap<String, Object>();
            fadRtfreevalueViewConditionsMap.put(FadRtfreevalueViewAttribute.ACCOUNT_INFO_CODE, accountInfoCode);
            List<FadRtfreevalueView> fadRtfreevalueViewList = certificateManager.isNullReturnEmpty(accountInfoCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadRtfreevalueView.class, fadRtfreevalueViewConditionsMap, null) : new ArrayList<>();
            if (fadRtfreevalueViewList.size() > 0) {
                FadRtfreevalueView fadRtfreevalueView = (FadRtfreevalueView) fadRtfreevalueViewList.get(0);
                accountInfoName = certificateManager.isNullReturnEmpty(fadRtfreevalueView.getAccountInfoName());
            }
            fadNonProjectSetRuleMap.put("creditorRtfree4a51a1a2Name", accountInfoName);
        }

        //部门名称显示
        if (certificateManager.isNullReturnEmpty(fadNonProjectSetRuleMap.get(FadNonProjectSetRuleAttribute.OFFICE)).length() > 0) {
            String OrgCode = certificateManager.isNullReturnEmpty(fadNonProjectSetRuleMap.get(FadNonProjectSetRuleAttribute.OFFICE));
            String OrgName = "";
            Map<String, Object> scdpOrgConditionsMap = new HashMap<String, Object>();
            scdpOrgConditionsMap.put(ScdpOrgAttribute.ORG_CODE, OrgCode);
            List<ScdpOrg> scdpOrgList = certificateManager.isNullReturnEmpty(OrgCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpOrg.class, scdpOrgConditionsMap, null) : new ArrayList<>();
            if (scdpOrgList.size() > 0) {
                ScdpOrg scdpOrg = (ScdpOrg) scdpOrgList.get(0);
                OrgName = certificateManager.isNullReturnEmpty(scdpOrg.getOrgName());
            }
            fadNonProjectSetRuleMap.put("officeDesc", OrgName);
        }

        return out;
    }
}
