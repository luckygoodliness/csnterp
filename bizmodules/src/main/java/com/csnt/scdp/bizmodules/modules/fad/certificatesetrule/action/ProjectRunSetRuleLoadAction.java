package com.csnt.scdp.bizmodules.modules.fad.certificatesetrule.action;

import com.csnt.scdp.bizmodules.entityattributes.fad.*;
import com.csnt.scdp.bizmodules.entityattributes.nonprm.NonProjectSubjectSubjectAttribute;
import com.csnt.scdp.bizmodules.modules.common.action.ErpLoadAction;
import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
import com.csnt.scdp.bizmodules.modules.nonprm.subjectsubject.dto.NonProjectSubjectSubjectDto;
import com.csnt.scdp.framework.core.dao.DAOMeta;
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
import com.csnt.scdp.bizmodules.entityattributes.fad.FadProjectRunSetRuleAttribute;

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
@Controller("projectrunsetrule-load")
@Transactional
public class ProjectRunSetRuleLoadAction extends ErpLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ProjectRunSetRuleLoadAction.class);

    @Resource(name = "certificatesetrule-manager")
    private CertificatesetruleManager certificatesetruleManager;

    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        //Do After

        Map fadProjectRunSetRuleMap = (Map) out.get("fadProjectRunSetRuleDto");

        //项目费用科目名称显示
        if (certificateManager.isNullReturnEmpty(fadProjectRunSetRuleMap.get(FadProjectRunSetRuleAttribute.RUN_SUBJECT)).length() > 0) {
            String subjectCode = certificateManager.isNullReturnEmpty(fadProjectRunSetRuleMap.get(FadProjectRunSetRuleAttribute.RUN_SUBJECT));
            String subjectName = "";
            String sql = "SELECT SUBJECT_NAME FROM FAD_BASE_SUBJECT WHERE SUBJECT_NO = '" + subjectCode + "'";
            DAOMeta daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            List fadBaseSubjectList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            if (fadBaseSubjectList.size() > 0) {
                Map fadBaseSubject = (Map) fadBaseSubjectList.get(0);
                subjectName = certificateManager.isNullReturnEmpty(fadBaseSubject.get("subjectName"));
            }
            fadProjectRunSetRuleMap.put("subjectName", subjectCode + "[" + subjectName + "]");
        }

        //成本确认凭证借方科目名称显示
        if (certificateManager.isNullReturnEmpty(fadProjectRunSetRuleMap.get(FadProjectRunSetRuleAttribute.DEBTOR_SUBJECT801)).length() > 0) {
            String subjectCode = certificateManager.isNullReturnEmpty(fadProjectRunSetRuleMap.get(FadProjectRunSetRuleAttribute.DEBTOR_SUBJECT801));
            String subjectName = "";
            Map<String, Object> fadAccsubjRtfreevalueConditionsMap = new HashMap<String, Object>();
            fadAccsubjRtfreevalueConditionsMap.put(FadAccsubjRtfreevalueAttribute.SUBJECT_CODE, subjectCode);
            List<FadAccsubjRtfreevalue> fadAccsubjRtfreevalueList = certificateManager.isNullReturnEmpty(subjectCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadAccsubjRtfreevalue.class, fadAccsubjRtfreevalueConditionsMap, null) : new ArrayList<>();
            if (fadAccsubjRtfreevalueList.size() > 0) {
                FadAccsubjRtfreevalue fadAccsubjRtfreevalue = (FadAccsubjRtfreevalue) fadAccsubjRtfreevalueList.get(0);
                subjectName = certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getSubjectName());
            }
            fadProjectRunSetRuleMap.put("debtorSubject801Name", subjectName);
        }
        if (certificateManager.isNullReturnEmpty(fadProjectRunSetRuleMap.get(FadProjectRunSetRuleAttribute.DEBTOR_SUBJECT80201)).length() > 0) {
            String subjectCode = certificateManager.isNullReturnEmpty(fadProjectRunSetRuleMap.get(FadProjectRunSetRuleAttribute.DEBTOR_SUBJECT80201));
            String subjectName = "";
            Map<String, Object> fadAccsubjRtfreevalueConditionsMap = new HashMap<String, Object>();
            fadAccsubjRtfreevalueConditionsMap.put(FadAccsubjRtfreevalueAttribute.SUBJECT_CODE, subjectCode);
            List<FadAccsubjRtfreevalue> fadAccsubjRtfreevalueList = certificateManager.isNullReturnEmpty(subjectCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadAccsubjRtfreevalue.class, fadAccsubjRtfreevalueConditionsMap, null) : new ArrayList<>();
            if (fadAccsubjRtfreevalueList.size() > 0) {
                FadAccsubjRtfreevalue fadAccsubjRtfreevalue = (FadAccsubjRtfreevalue) fadAccsubjRtfreevalueList.get(0);
                subjectName = certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getSubjectName());
            }
            fadProjectRunSetRuleMap.put("debtorSubject80201Name", subjectName);
        }
        if (certificateManager.isNullReturnEmpty(fadProjectRunSetRuleMap.get(FadProjectRunSetRuleAttribute.DEBTOR_SUBJECT80202)).length() > 0) {
            String subjectCode = certificateManager.isNullReturnEmpty(fadProjectRunSetRuleMap.get(FadProjectRunSetRuleAttribute.DEBTOR_SUBJECT80202));
            String subjectName = "";
            Map<String, Object> fadAccsubjRtfreevalueConditionsMap = new HashMap<String, Object>();
            fadAccsubjRtfreevalueConditionsMap.put(FadAccsubjRtfreevalueAttribute.SUBJECT_CODE, subjectCode);
            List<FadAccsubjRtfreevalue> fadAccsubjRtfreevalueList = certificateManager.isNullReturnEmpty(subjectCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadAccsubjRtfreevalue.class, fadAccsubjRtfreevalueConditionsMap, null) : new ArrayList<>();
            if (fadAccsubjRtfreevalueList.size() > 0) {
                FadAccsubjRtfreevalue fadAccsubjRtfreevalue = (FadAccsubjRtfreevalue) fadAccsubjRtfreevalueList.get(0);
                subjectName = certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getSubjectName());
            }
            fadProjectRunSetRuleMap.put("debtorSubject80202Name", subjectName);
        }
        if (certificateManager.isNullReturnEmpty(fadProjectRunSetRuleMap.get(FadProjectRunSetRuleAttribute.DEBTOR_SUBJECT80203)).length() > 0) {
            String subjectCode = certificateManager.isNullReturnEmpty(fadProjectRunSetRuleMap.get(FadProjectRunSetRuleAttribute.DEBTOR_SUBJECT80203));
            String subjectName = "";
            Map<String, Object> fadAccsubjRtfreevalueConditionsMap = new HashMap<String, Object>();
            fadAccsubjRtfreevalueConditionsMap.put(FadAccsubjRtfreevalueAttribute.SUBJECT_CODE, subjectCode);
            List<FadAccsubjRtfreevalue> fadAccsubjRtfreevalueList = certificateManager.isNullReturnEmpty(subjectCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadAccsubjRtfreevalue.class, fadAccsubjRtfreevalueConditionsMap, null) : new ArrayList<>();
            if (fadAccsubjRtfreevalueList.size() > 0) {
                FadAccsubjRtfreevalue fadAccsubjRtfreevalue = (FadAccsubjRtfreevalue) fadAccsubjRtfreevalueList.get(0);
                subjectName = certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getSubjectName());
            }
            fadProjectRunSetRuleMap.put("debtorSubject80203Name", subjectName);
        }
        if (certificateManager.isNullReturnEmpty(fadProjectRunSetRuleMap.get(FadProjectRunSetRuleAttribute.DEBTOR_SUBJECT803)).length() > 0) {
            String subjectCode = certificateManager.isNullReturnEmpty(fadProjectRunSetRuleMap.get(FadProjectRunSetRuleAttribute.DEBTOR_SUBJECT803));
            String subjectName = "";
            Map<String, Object> fadAccsubjRtfreevalueConditionsMap = new HashMap<String, Object>();
            fadAccsubjRtfreevalueConditionsMap.put(FadAccsubjRtfreevalueAttribute.SUBJECT_CODE, subjectCode);
            List<FadAccsubjRtfreevalue> fadAccsubjRtfreevalueList = certificateManager.isNullReturnEmpty(subjectCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadAccsubjRtfreevalue.class, fadAccsubjRtfreevalueConditionsMap, null) : new ArrayList<>();
            if (fadAccsubjRtfreevalueList.size() > 0) {
                FadAccsubjRtfreevalue fadAccsubjRtfreevalue = (FadAccsubjRtfreevalue) fadAccsubjRtfreevalueList.get(0);
                subjectName = certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getSubjectName());
            }
            fadProjectRunSetRuleMap.put("debtorSubject803Name", subjectName);
        }
        if (certificateManager.isNullReturnEmpty(fadProjectRunSetRuleMap.get(FadProjectRunSetRuleAttribute.DEBTOR_SUBJECT804)).length() > 0) {
            String subjectCode = certificateManager.isNullReturnEmpty(fadProjectRunSetRuleMap.get(FadProjectRunSetRuleAttribute.DEBTOR_SUBJECT804));
            String subjectName = "";
            Map<String, Object> fadAccsubjRtfreevalueConditionsMap = new HashMap<String, Object>();
            fadAccsubjRtfreevalueConditionsMap.put(FadAccsubjRtfreevalueAttribute.SUBJECT_CODE, subjectCode);
            List<FadAccsubjRtfreevalue> fadAccsubjRtfreevalueList = certificateManager.isNullReturnEmpty(subjectCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadAccsubjRtfreevalue.class, fadAccsubjRtfreevalueConditionsMap, null) : new ArrayList<>();
            if (fadAccsubjRtfreevalueList.size() > 0) {
                FadAccsubjRtfreevalue fadAccsubjRtfreevalue = (FadAccsubjRtfreevalue) fadAccsubjRtfreevalueList.get(0);
                subjectName = certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getSubjectName());
            }
            fadProjectRunSetRuleMap.put("debtorSubject804Name", subjectName);
        }
        if (certificateManager.isNullReturnEmpty(fadProjectRunSetRuleMap.get(FadProjectRunSetRuleAttribute.DEBTOR_SUBJECT805)).length() > 0) {
            String subjectCode = certificateManager.isNullReturnEmpty(fadProjectRunSetRuleMap.get(FadProjectRunSetRuleAttribute.DEBTOR_SUBJECT805));
            String subjectName = "";
            Map<String, Object> fadAccsubjRtfreevalueConditionsMap = new HashMap<String, Object>();
            fadAccsubjRtfreevalueConditionsMap.put(FadAccsubjRtfreevalueAttribute.SUBJECT_CODE, subjectCode);
            List<FadAccsubjRtfreevalue> fadAccsubjRtfreevalueList = certificateManager.isNullReturnEmpty(subjectCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadAccsubjRtfreevalue.class, fadAccsubjRtfreevalueConditionsMap, null) : new ArrayList<>();
            if (fadAccsubjRtfreevalueList.size() > 0) {
                FadAccsubjRtfreevalue fadAccsubjRtfreevalue = (FadAccsubjRtfreevalue) fadAccsubjRtfreevalueList.get(0);
                subjectName = certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getSubjectName());
            }
            fadProjectRunSetRuleMap.put("debtorSubject805Name", subjectName);
        }

        //成本确认凭证贷方科目名称显示
        if (certificateManager.isNullReturnEmpty(fadProjectRunSetRuleMap.get(FadProjectRunSetRuleAttribute.CREDITOR_SUBJECT8)).length() > 0) {
            String subjectCode = certificateManager.isNullReturnEmpty(fadProjectRunSetRuleMap.get(FadProjectRunSetRuleAttribute.CREDITOR_SUBJECT8));
            String subjectName = "";
            Map<String, Object> fadAccsubjRtfreevalueConditionsMap = new HashMap<String, Object>();
            fadAccsubjRtfreevalueConditionsMap.put(FadAccsubjRtfreevalueAttribute.SUBJECT_CODE, subjectCode);
            List<FadAccsubjRtfreevalue> fadAccsubjRtfreevalueList = certificateManager.isNullReturnEmpty(subjectCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadAccsubjRtfreevalue.class, fadAccsubjRtfreevalueConditionsMap, null) : new ArrayList<>();
            if (fadAccsubjRtfreevalueList.size() > 0) {
                FadAccsubjRtfreevalue fadAccsubjRtfreevalue = (FadAccsubjRtfreevalue) fadAccsubjRtfreevalueList.get(0);
                subjectName = certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getSubjectName());
            }
            fadProjectRunSetRuleMap.put("creditorSubject8Name", subjectName);
        }

        //采购凭证借方科目名称显示
        if (certificateManager.isNullReturnEmpty(fadProjectRunSetRuleMap.get(FadProjectRunSetRuleAttribute.DEBTOR_SUBJECT6A7)).length() > 0) {
            String subjectCode = certificateManager.isNullReturnEmpty(fadProjectRunSetRuleMap.get(FadProjectRunSetRuleAttribute.DEBTOR_SUBJECT6A7));
            String subjectName = "";
            Map<String, Object> fadAccsubjRtfreevalueConditionsMap = new HashMap<String, Object>();
            fadAccsubjRtfreevalueConditionsMap.put(FadAccsubjRtfreevalueAttribute.SUBJECT_CODE, subjectCode);
            List<FadAccsubjRtfreevalue> fadAccsubjRtfreevalueList = certificateManager.isNullReturnEmpty(subjectCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadAccsubjRtfreevalue.class, fadAccsubjRtfreevalueConditionsMap, null) : new ArrayList<>();
            if (fadAccsubjRtfreevalueList.size() > 0) {
                FadAccsubjRtfreevalue fadAccsubjRtfreevalue = (FadAccsubjRtfreevalue) fadAccsubjRtfreevalueList.get(0);
                subjectName = certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getSubjectName());
            }
            fadProjectRunSetRuleMap.put("debtorSubject6a7Name", subjectName);
        }

        //采购凭证贷方科目名称显示
        if (certificateManager.isNullReturnEmpty(fadProjectRunSetRuleMap.get(FadProjectRunSetRuleAttribute.CREDITOR_SUBJECT6A7)).length() > 0) {
            String subjectCode = certificateManager.isNullReturnEmpty(fadProjectRunSetRuleMap.get(FadProjectRunSetRuleAttribute.CREDITOR_SUBJECT6A7));
            String subjectName = "";
            Map<String, Object> fadAccsubjRtfreevalueConditionsMap = new HashMap<String, Object>();
            fadAccsubjRtfreevalueConditionsMap.put(FadAccsubjRtfreevalueAttribute.SUBJECT_CODE, subjectCode);
            List<FadAccsubjRtfreevalue> fadAccsubjRtfreevalueList = certificateManager.isNullReturnEmpty(subjectCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadAccsubjRtfreevalue.class, fadAccsubjRtfreevalueConditionsMap, null) : new ArrayList<>();
            if (fadAccsubjRtfreevalueList.size() > 0) {
                FadAccsubjRtfreevalue fadAccsubjRtfreevalue = (FadAccsubjRtfreevalue) fadAccsubjRtfreevalueList.get(0);
                subjectName = certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getSubjectName());
            }
            fadProjectRunSetRuleMap.put("creditorSubject6a7Name", subjectName);
        }

        //支付凭证借方科目名称显示
        if (certificateManager.isNullReturnEmpty(fadProjectRunSetRuleMap.get(FadProjectRunSetRuleAttribute.DEBTOR_SUBJECT9A10)).length() > 0) {
            String subjectCode = certificateManager.isNullReturnEmpty(fadProjectRunSetRuleMap.get(FadProjectRunSetRuleAttribute.DEBTOR_SUBJECT9A10));
            String subjectName = "";
            Map<String, Object> fadAccsubjRtfreevalueConditionsMap = new HashMap<String, Object>();
            fadAccsubjRtfreevalueConditionsMap.put(FadAccsubjRtfreevalueAttribute.SUBJECT_CODE, subjectCode);
            List<FadAccsubjRtfreevalue> fadAccsubjRtfreevalueList = certificateManager.isNullReturnEmpty(subjectCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadAccsubjRtfreevalue.class, fadAccsubjRtfreevalueConditionsMap, null) : new ArrayList<>();
            if (fadAccsubjRtfreevalueList.size() > 0) {
                FadAccsubjRtfreevalue fadAccsubjRtfreevalue = (FadAccsubjRtfreevalue) fadAccsubjRtfreevalueList.get(0);
                subjectName = certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getSubjectName());
            }
            fadProjectRunSetRuleMap.put("debtorSubject9a10Name", subjectName);
        }

        //支付凭证贷方科目名称显示
        if (certificateManager.isNullReturnEmpty(fadProjectRunSetRuleMap.get(FadProjectRunSetRuleAttribute.CREDITOR_SUBJECT9A10)).length() > 0) {
            String subjectCode = certificateManager.isNullReturnEmpty(fadProjectRunSetRuleMap.get(FadProjectRunSetRuleAttribute.CREDITOR_SUBJECT9A10));
            String subjectName = "";
            Map<String, Object> fadAccsubjRtfreevalueConditionsMap = new HashMap<String, Object>();
            fadAccsubjRtfreevalueConditionsMap.put(FadAccsubjRtfreevalueAttribute.SUBJECT_CODE, subjectCode);
            List<FadAccsubjRtfreevalue> fadAccsubjRtfreevalueList = certificateManager.isNullReturnEmpty(subjectCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadAccsubjRtfreevalue.class, fadAccsubjRtfreevalueConditionsMap, null) : new ArrayList<>();
            if (fadAccsubjRtfreevalueList.size() > 0) {
                FadAccsubjRtfreevalue fadAccsubjRtfreevalue = (FadAccsubjRtfreevalue) fadAccsubjRtfreevalueList.get(0);
                subjectName = certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getSubjectName());
            }
            fadProjectRunSetRuleMap.put("creditorSubject9a10Name", subjectName);
        }

        //支付凭证贷方辅助核算项名称显示
        if (certificateManager.isNullReturnEmpty(fadProjectRunSetRuleMap.get(FadProjectSetRuleAttribute.CREDITOR_RTFREE9A10)).length() > 0) {
            String accountInfoCode = certificateManager.isNullReturnEmpty(fadProjectRunSetRuleMap.get(FadProjectSetRuleAttribute.CREDITOR_RTFREE9A10));
            String accountInfoName = "";
            Map<String, Object> fadRtfreevalueViewConditionsMap = new HashMap<String, Object>();
            fadRtfreevalueViewConditionsMap.put(FadRtfreevalueViewAttribute.ACCOUNT_INFO_CODE, accountInfoCode);
            List<FadRtfreevalueView> fadRtfreevalueViewList = certificateManager.isNullReturnEmpty(accountInfoCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadRtfreevalueView.class, fadRtfreevalueViewConditionsMap, null) : new ArrayList<>();
            if (fadRtfreevalueViewList.size() > 0) {
                FadRtfreevalueView fadRtfreevalueView = (FadRtfreevalueView) fadRtfreevalueViewList.get(0);
                accountInfoName = certificateManager.isNullReturnEmpty(fadRtfreevalueView.getAccountInfoName());
            }
            fadProjectRunSetRuleMap.put("creditorRtfree9a10Name", accountInfoName);
        }

        //请款凭证借方科目(电汇/现金)名称显示
        if (certificateManager.isNullReturnEmpty(fadProjectRunSetRuleMap.get(FadProjectRunSetRuleAttribute.DEBTOR_SUBJECT1)).length() > 0) {
            String subjectCode = certificateManager.isNullReturnEmpty(fadProjectRunSetRuleMap.get(FadProjectRunSetRuleAttribute.DEBTOR_SUBJECT1));
            String subjectName = "";
            Map<String, Object> fadAccsubjRtfreevalueConditionsMap = new HashMap<String, Object>();
            fadAccsubjRtfreevalueConditionsMap.put(FadAccsubjRtfreevalueAttribute.SUBJECT_CODE, subjectCode);
            List<FadAccsubjRtfreevalue> fadAccsubjRtfreevalueList = certificateManager.isNullReturnEmpty(subjectCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadAccsubjRtfreevalue.class, fadAccsubjRtfreevalueConditionsMap, null) : new ArrayList<>();
            if (fadAccsubjRtfreevalueList.size() > 0) {
                FadAccsubjRtfreevalue fadAccsubjRtfreevalue = (FadAccsubjRtfreevalue) fadAccsubjRtfreevalueList.get(0);
                subjectName = certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getSubjectName());
            }
            fadProjectRunSetRuleMap.put("debtorSubject1Name", subjectName);
        }

        //请款凭证借方科目(付汇等)名称显示
        if (certificateManager.isNullReturnEmpty(fadProjectRunSetRuleMap.get(FadProjectRunSetRuleAttribute.DEBTOR_SUBJECT2)).length() > 0) {
            String subjectCode = certificateManager.isNullReturnEmpty(fadProjectRunSetRuleMap.get(FadProjectRunSetRuleAttribute.DEBTOR_SUBJECT2));
            String subjectName = "";
            Map<String, Object> fadAccsubjRtfreevalueConditionsMap = new HashMap<String, Object>();
            fadAccsubjRtfreevalueConditionsMap.put(FadAccsubjRtfreevalueAttribute.SUBJECT_CODE, subjectCode);
            List<FadAccsubjRtfreevalue> fadAccsubjRtfreevalueList = certificateManager.isNullReturnEmpty(subjectCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadAccsubjRtfreevalue.class, fadAccsubjRtfreevalueConditionsMap, null) : new ArrayList<>();
            if (fadAccsubjRtfreevalueList.size() > 0) {
                FadAccsubjRtfreevalue fadAccsubjRtfreevalue = (FadAccsubjRtfreevalue) fadAccsubjRtfreevalueList.get(0);
                subjectName = certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getSubjectName());
            }
            fadProjectRunSetRuleMap.put("debtorSubject2Name", subjectName);
        }

        return out;
    }
}
