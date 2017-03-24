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
import com.csnt.scdp.bizmodules.entityattributes.fad.FadProjectSetRuleAttribute;

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
@Controller("projectsetrule-load")
@Transactional
public class ProjectSetRuleLoadAction extends ErpLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ProjectSetRuleLoadAction.class);

    @Resource(name = "certificatesetrule-manager")
    private CertificatesetruleManager certificatesetruleManager;

    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        //Do After

        Map fadProjectSetRuleMap = (Map) out.get("fadProjectSetRuleDto");

        //项目费用科目名称显示
        if (certificateManager.isNullReturnEmpty(fadProjectSetRuleMap.get(FadProjectSetRuleAttribute.SUBJECT)).length() > 0) {
            String subjectCode = certificateManager.isNullReturnEmpty(fadProjectSetRuleMap.get(FadProjectSetRuleAttribute.SUBJECT));
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
            fadProjectSetRuleMap.put("subjectName", subjectCode + "[" + subjectName + "]");
        }

        //项目代号类型名称显示
        if (certificateManager.isNullReturnEmpty(fadProjectSetRuleMap.get(FadProjectSetRuleAttribute.PRM_CODE_TYPE)).length() > 0) {
            String prmCodeType = certificateManager.isNullReturnEmpty(fadProjectSetRuleMap.get(FadProjectSetRuleAttribute.PRM_CODE_TYPE));
            String prmCodeTypeName = "";
            String sql = "select sys_code,case when length(substr(code_desc,1,case when code_desc like '%（%' then instr(code_desc,'（',1,1) - 1 else length(code_desc) end )) = 2 then substr(code_desc,1,case when code_desc like '%（%' then instr(code_desc,'（',1,1) - 1 else length(code_desc) end ) || '类' else substr(code_desc,1,case when code_desc like '%（%' then instr(code_desc,'（',1,1) - 1 else length(code_desc) end ) end code_desc from scdp_code where code_type = 'PRM_CODE_TYPE_PP' and sys_code = '" + prmCodeType + "'";
            DAOMeta daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            List scdpCodeList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            if (scdpCodeList.size() > 0) {
                Map scdpCode = (Map) scdpCodeList.get(0);
                prmCodeTypeName = certificateManager.isNullReturnEmpty(scdpCode.get("codeDesc"));
            }
            fadProjectSetRuleMap.put("prmCodeTypeName", prmCodeTypeName);
        }

        //采购凭证借方科目名称显示
        if (certificateManager.isNullReturnEmpty(fadProjectSetRuleMap.get(FadProjectSetRuleAttribute.DEBTOR_SUBJECT6A7)).length() > 0) {
            String subjectCode = certificateManager.isNullReturnEmpty(fadProjectSetRuleMap.get(FadProjectSetRuleAttribute.DEBTOR_SUBJECT6A7));
            String subjectName = "";
            Map<String, Object> fadAccsubjRtfreevalueConditionsMap = new HashMap<String, Object>();
            fadAccsubjRtfreevalueConditionsMap.put(FadAccsubjRtfreevalueAttribute.SUBJECT_CODE, subjectCode);
            List<FadAccsubjRtfreevalue> fadAccsubjRtfreevalueList = certificateManager.isNullReturnEmpty(subjectCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadAccsubjRtfreevalue.class, fadAccsubjRtfreevalueConditionsMap, null) : new ArrayList<>();
            if (fadAccsubjRtfreevalueList.size() > 0) {
                FadAccsubjRtfreevalue fadAccsubjRtfreevalue = (FadAccsubjRtfreevalue) fadAccsubjRtfreevalueList.get(0);
                subjectName = certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getSubjectName());
            }
            fadProjectSetRuleMap.put("debtorSubject6a7Name", subjectName);
        }

        //采购凭证贷方科目名称显示
        if (certificateManager.isNullReturnEmpty(fadProjectSetRuleMap.get(FadProjectSetRuleAttribute.CREDITOR_SUBJECT6A7)).length() > 0) {
            String subjectCode = certificateManager.isNullReturnEmpty(fadProjectSetRuleMap.get(FadProjectSetRuleAttribute.CREDITOR_SUBJECT6A7));
            String subjectName = "";
            Map<String, Object> fadAccsubjRtfreevalueConditionsMap = new HashMap<String, Object>();
            fadAccsubjRtfreevalueConditionsMap.put(FadAccsubjRtfreevalueAttribute.SUBJECT_CODE, subjectCode);
            List<FadAccsubjRtfreevalue> fadAccsubjRtfreevalueList = certificateManager.isNullReturnEmpty(subjectCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadAccsubjRtfreevalue.class, fadAccsubjRtfreevalueConditionsMap, null) : new ArrayList<>();
            if (fadAccsubjRtfreevalueList.size() > 0) {
                FadAccsubjRtfreevalue fadAccsubjRtfreevalue = (FadAccsubjRtfreevalue) fadAccsubjRtfreevalueList.get(0);
                subjectName = certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getSubjectName());
            }
            fadProjectSetRuleMap.put("creditorSubject6a7Name", subjectName);
        }

        //成本确认凭证借方科目名称显示
        if (certificateManager.isNullReturnEmpty(fadProjectSetRuleMap.get(FadProjectSetRuleAttribute.DEBTOR_SUBJECT8)).length() > 0) {
            String subjectCode = certificateManager.isNullReturnEmpty(fadProjectSetRuleMap.get(FadProjectSetRuleAttribute.DEBTOR_SUBJECT8));
            String subjectName = "";
            Map<String, Object> fadAccsubjRtfreevalueConditionsMap = new HashMap<String, Object>();
            fadAccsubjRtfreevalueConditionsMap.put(FadAccsubjRtfreevalueAttribute.SUBJECT_CODE, subjectCode);
            List<FadAccsubjRtfreevalue> fadAccsubjRtfreevalueList = certificateManager.isNullReturnEmpty(subjectCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadAccsubjRtfreevalue.class, fadAccsubjRtfreevalueConditionsMap, null) : new ArrayList<>();
            if (fadAccsubjRtfreevalueList.size() > 0) {
                FadAccsubjRtfreevalue fadAccsubjRtfreevalue = (FadAccsubjRtfreevalue) fadAccsubjRtfreevalueList.get(0);
                subjectName = certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getSubjectName());
            }
            fadProjectSetRuleMap.put("debtorSubject8Name", subjectName);
        }

        //成本确认凭证贷方科目名称显示
        if (certificateManager.isNullReturnEmpty(fadProjectSetRuleMap.get(FadProjectSetRuleAttribute.CREDITOR_SUBJECT8)).length() > 0) {
            String subjectCode = certificateManager.isNullReturnEmpty(fadProjectSetRuleMap.get(FadProjectSetRuleAttribute.CREDITOR_SUBJECT8));
            String subjectName = "";
            Map<String, Object> fadAccsubjRtfreevalueConditionsMap = new HashMap<String, Object>();
            fadAccsubjRtfreevalueConditionsMap.put(FadAccsubjRtfreevalueAttribute.SUBJECT_CODE, subjectCode);
            List<FadAccsubjRtfreevalue> fadAccsubjRtfreevalueList = certificateManager.isNullReturnEmpty(subjectCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadAccsubjRtfreevalue.class, fadAccsubjRtfreevalueConditionsMap, null) : new ArrayList<>();
            if (fadAccsubjRtfreevalueList.size() > 0) {
                FadAccsubjRtfreevalue fadAccsubjRtfreevalue = (FadAccsubjRtfreevalue) fadAccsubjRtfreevalueList.get(0);
                subjectName = certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getSubjectName());
            }
            fadProjectSetRuleMap.put("creditorSubject8Name", subjectName);
        }

        //支付凭证借方科目名称显示
        if (certificateManager.isNullReturnEmpty(fadProjectSetRuleMap.get(FadProjectSetRuleAttribute.DEBTOR_SUBJECT9A10)).length() > 0) {
            String subjectCode = certificateManager.isNullReturnEmpty(fadProjectSetRuleMap.get(FadProjectSetRuleAttribute.DEBTOR_SUBJECT9A10));
            String subjectName = "";
            Map<String, Object> fadAccsubjRtfreevalueConditionsMap = new HashMap<String, Object>();
            fadAccsubjRtfreevalueConditionsMap.put(FadAccsubjRtfreevalueAttribute.SUBJECT_CODE, subjectCode);
            List<FadAccsubjRtfreevalue> fadAccsubjRtfreevalueList = certificateManager.isNullReturnEmpty(subjectCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadAccsubjRtfreevalue.class, fadAccsubjRtfreevalueConditionsMap, null) : new ArrayList<>();
            if (fadAccsubjRtfreevalueList.size() > 0) {
                FadAccsubjRtfreevalue fadAccsubjRtfreevalue = (FadAccsubjRtfreevalue) fadAccsubjRtfreevalueList.get(0);
                subjectName = certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getSubjectName());
            }
            fadProjectSetRuleMap.put("debtorSubject9a10Name", subjectName);
        }

        //支付凭证贷方科目名称显示
        if (certificateManager.isNullReturnEmpty(fadProjectSetRuleMap.get(FadProjectSetRuleAttribute.CREDITOR_SUBJECT9A10)).length() > 0) {
            String subjectCode = certificateManager.isNullReturnEmpty(fadProjectSetRuleMap.get(FadProjectSetRuleAttribute.CREDITOR_SUBJECT9A10));
            String subjectName = "";
            Map<String, Object> fadAccsubjRtfreevalueConditionsMap = new HashMap<String, Object>();
            fadAccsubjRtfreevalueConditionsMap.put(FadAccsubjRtfreevalueAttribute.SUBJECT_CODE, subjectCode);
            List<FadAccsubjRtfreevalue> fadAccsubjRtfreevalueList = certificateManager.isNullReturnEmpty(subjectCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadAccsubjRtfreevalue.class, fadAccsubjRtfreevalueConditionsMap, null) : new ArrayList<>();
            if (fadAccsubjRtfreevalueList.size() > 0) {
                FadAccsubjRtfreevalue fadAccsubjRtfreevalue = (FadAccsubjRtfreevalue) fadAccsubjRtfreevalueList.get(0);
                subjectName = certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getSubjectName());
            }
            fadProjectSetRuleMap.put("creditorSubject9a10Name", subjectName);
        }

        //支付凭证贷方辅助核算项名称显示
        if (certificateManager.isNullReturnEmpty(fadProjectSetRuleMap.get(FadProjectSetRuleAttribute.CREDITOR_RTFREE9A10)).length() > 0) {
            String accountInfoCode = certificateManager.isNullReturnEmpty(fadProjectSetRuleMap.get(FadProjectSetRuleAttribute.CREDITOR_RTFREE9A10));
            String accountInfoName = "";
            Map<String, Object> fadRtfreevalueViewConditionsMap = new HashMap<String, Object>();
            fadRtfreevalueViewConditionsMap.put(FadRtfreevalueViewAttribute.ACCOUNT_INFO_CODE, accountInfoCode);
            List<FadRtfreevalueView> fadRtfreevalueViewList = certificateManager.isNullReturnEmpty(accountInfoCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadRtfreevalueView.class, fadRtfreevalueViewConditionsMap, null) : new ArrayList<>();
            if (fadRtfreevalueViewList.size() > 0) {
                FadRtfreevalueView fadRtfreevalueView = (FadRtfreevalueView) fadRtfreevalueViewList.get(0);
                accountInfoName = certificateManager.isNullReturnEmpty(fadRtfreevalueView.getAccountInfoName());
            }
            fadProjectSetRuleMap.put("creditorRtfree9a10Name", accountInfoName);
        }

        //请款凭证借方科目(电汇/现金)名称显示
        if (certificateManager.isNullReturnEmpty(fadProjectSetRuleMap.get(FadProjectSetRuleAttribute.DEBTOR_SUBJECT1)).length() > 0) {
            String subjectCode = certificateManager.isNullReturnEmpty(fadProjectSetRuleMap.get(FadProjectSetRuleAttribute.DEBTOR_SUBJECT1));
            String subjectName = "";
            Map<String, Object> fadAccsubjRtfreevalueConditionsMap = new HashMap<String, Object>();
            fadAccsubjRtfreevalueConditionsMap.put(FadAccsubjRtfreevalueAttribute.SUBJECT_CODE, subjectCode);
            List<FadAccsubjRtfreevalue> fadAccsubjRtfreevalueList = certificateManager.isNullReturnEmpty(subjectCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadAccsubjRtfreevalue.class, fadAccsubjRtfreevalueConditionsMap, null) : new ArrayList<>();
            if (fadAccsubjRtfreevalueList.size() > 0) {
                FadAccsubjRtfreevalue fadAccsubjRtfreevalue = (FadAccsubjRtfreevalue) fadAccsubjRtfreevalueList.get(0);
                subjectName = certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getSubjectName());
            }
            fadProjectSetRuleMap.put("debtorSubject1Name", subjectName);
        }

        //请款凭证借方科目(付汇等)名称显示
        if (certificateManager.isNullReturnEmpty(fadProjectSetRuleMap.get(FadProjectSetRuleAttribute.DEBTOR_SUBJECT2)).length() > 0) {
            String subjectCode = certificateManager.isNullReturnEmpty(fadProjectSetRuleMap.get(FadProjectSetRuleAttribute.DEBTOR_SUBJECT2));
            String subjectName = "";
            Map<String, Object> fadAccsubjRtfreevalueConditionsMap = new HashMap<String, Object>();
            fadAccsubjRtfreevalueConditionsMap.put(FadAccsubjRtfreevalueAttribute.SUBJECT_CODE, subjectCode);
            List<FadAccsubjRtfreevalue> fadAccsubjRtfreevalueList = certificateManager.isNullReturnEmpty(subjectCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadAccsubjRtfreevalue.class, fadAccsubjRtfreevalueConditionsMap, null) : new ArrayList<>();
            if (fadAccsubjRtfreevalueList.size() > 0) {
                FadAccsubjRtfreevalue fadAccsubjRtfreevalue = (FadAccsubjRtfreevalue) fadAccsubjRtfreevalueList.get(0);
                subjectName = certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getSubjectName());
            }
            fadProjectSetRuleMap.put("debtorSubject2Name", subjectName);
        }

        //部门名称显示
        if (certificateManager.isNullReturnEmpty(fadProjectSetRuleMap.get(FadProjectSetRuleAttribute.OFFICE)).length() > 0) {
            String OrgCodeArr[] = certificateManager.isNullReturnEmpty(fadProjectSetRuleMap.get(FadProjectSetRuleAttribute.OFFICE)).split("\\|");
            String OrgNames = "";
            for (int i = 0; i < OrgCodeArr.length; i++) {
                String OrgCode = certificateManager.isNullReturnEmpty(OrgCodeArr[i]);
                if (OrgCode.length() > 0) {
                    String OrgName = "";
                    Map<String, Object> scdpOrgConditionsMap = new HashMap<String, Object>();
                    scdpOrgConditionsMap.put(ScdpOrgAttribute.ORG_CODE, OrgCode);
                    List<ScdpOrg> scdpOrgList = certificateManager.isNullReturnEmpty(OrgCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpOrg.class, scdpOrgConditionsMap, null) : new ArrayList<>();
                    if (scdpOrgList.size() > 0) {
                        ScdpOrg scdpOrg = (ScdpOrg) scdpOrgList.get(0);
                        OrgName = certificateManager.isNullReturnEmpty(scdpOrg.getOrgName());
                        if (OrgName.length() > 0) {
                            if (!(OrgNames.length() > 0)) {
                                OrgNames = OrgName;
                            } else {
                                OrgNames = OrgNames + "|" + OrgName;
                            }
                        }
                    }
                }
            }
            fadProjectSetRuleMap.put("officeDesc", "|" + OrgNames + "|");
        }

        return out;
    }
}
