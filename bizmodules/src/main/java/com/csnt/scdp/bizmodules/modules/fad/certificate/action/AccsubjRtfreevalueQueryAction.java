package com.csnt.scdp.bizmodules.modules.fad.certificate.action;

import com.csnt.scdp.bizmodules.entity.fad.FadCertificateDetail;
import com.csnt.scdp.bizmodules.entity.fad.FadRtfreevalueView;
import com.csnt.scdp.bizmodules.entity.fad.*;
import com.csnt.scdp.bizmodules.entity.prm.PrmCustomer;
import com.csnt.scdp.bizmodules.entity.prm.PrmReceipts;
import com.csnt.scdp.bizmodules.entity.prm.PrmBilling;
import com.csnt.scdp.bizmodules.entity.scm.ScmSupplier;
import com.csnt.scdp.bizmodules.entity.scm.ScmContract;
import com.csnt.scdp.bizmodules.entity.scm.ScmContractInvoice;
import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadRtfreevalueViewAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmCustomerAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmSupplierAttribute;
import com.csnt.scdp.bizmodules.modules.fad.certificate.dto.FadCertificateDetailDto;
import com.csnt.scdp.bizmodules.modules.fad.certificate.dto.FadCertificateDto;
import com.csnt.scdp.bizmodules.modules.nonprm.subjectsubject.dto.NonProjectSubjectSubjectDto;
import com.csnt.scdp.framework.helper.DtoHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.entity.ScdpOrg;
import com.csnt.scdp.bizmodules.entity.fad.FadAccsubjRtfreevalue;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCertificateAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCertificateDetailAttribute;
import com.csnt.scdp.sysmodules.entityattributes.ScdpOrgAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.*;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmSupplierAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmContractInvoiceAttribute;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;
import com.csnt.scdp.bizmodules.entityattributes.nonprm.NonProjectSubjectSubjectAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadAccsubjRtfreevalueAttribute;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Reese on 2015/10/14.
 */
@Scope("singleton")
@Controller("certificate-accsubjRtfreevalueQuery")
@Transactional
public class AccsubjRtfreevalueQueryAction extends CommonLoadAction implements IAction {

    private static ILogTracer tracer = LogTracerFactory.getInstance(AccsubjRtfreevalueQueryAction.class);
    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map result = new HashMap<>();

        //取得科目
        //String fadCertificateUuid = certificateManager.isNullReturnEmpty(inMap.get("fadCertificateUuid"));
        //String fadCertificateDetailUuid = certificateManager.isNullReturnEmpty(inMap.get("fadCertificateDetailUuid"));
        String FinanceSubjectCode = certificateManager.isNullReturnEmpty(inMap.get(FadCertificateDetailAttribute.SUBJECT_CODE));
        String receiverOrPayerType = certificateManager.isNullReturnEmpty(inMap.get(FadCertificateAttribute.RECEIVER_OR_PAYER_TYPE));
        String receiverOrPayerId = certificateManager.isNullReturnEmpty(inMap.get(FadCertificateAttribute.RECEIVER_OR_PAYER_ID));
        String receiverOrPayerCode = certificateManager.isNullReturnEmpty(inMap.get(FadCertificateAttribute.RECEIVER_OR_PAYER_CODE));
        String receiverOrPayerName = certificateManager.isNullReturnEmpty(inMap.get(FadCertificateAttribute.RECEIVER_OR_PAYER_NAME));
        String businessId = certificateManager.isNullReturnEmpty(inMap.get(FadCertificateAttribute.BUSINESS_ID));
        String originalFormCode = certificateManager.isNullReturnEmpty(inMap.get(FadCertificateAttribute.ORIGINAL_FORM_CODE));
        String originalFormType = certificateManager.isNullReturnEmpty(inMap.get(FadCertificateAttribute.ORIGINAL_FORM_TYPE));
        String originalFormTypeForeign = certificateManager.getOriginalFormTypeForeign(originalFormType);

        if (certificateManager.isNullReturnEmpty(FinanceSubjectCode).length() > 0) {

            //全局信息
            String UserId = "";
            String projectCodeOrSubjectCode = "";
            String projectDepartmentCodeOrSubjectDepartmentCode = "";

            //根据凭证来源设定分录信息
            if (originalFormTypeForeign.equals("0")) {

                //0.FAD_INVOICE(发票)
                FadInvoice fadInvoice = certificateManager.isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadInvoice.class, businessId) : null;
                if (fadInvoice != null) {

                    //4.无请款报销
                    //5.有请款报销
                    if (originalFormType.equals("4") || originalFormType.equals("5") || originalFormType.equals("5.1")) {

                        //【报销人】取得
                        UserId = certificateManager.isNullReturnEmpty(fadInvoice.getRenderPerson());
                        String userName = "";
                        Map<String, Object> scdpUserConditionsMap = new HashMap<String, Object>();
                        scdpUserConditionsMap.put(ScdpUserAttribute.USER_ID, UserId);
                        List<ScdpUser> scdpUserList = certificateManager.isNullReturnEmpty(UserId).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, scdpUserConditionsMap, null) : new ArrayList<>();
                        if (scdpUserList.size() > 0) {
                            ScdpUser scdpUser = (ScdpUser) scdpUserList.get(0);
                            userName = certificateManager.isNullReturnEmpty(scdpUser.getUserName());
                        }

                        //【项目代号】或【费用科目代号】取得
                        //【项目部门代号】或【费用科目部门代号】取得
                        //String projectCodeOrSubjectCode = "";
                        //String projectDepartmentCodeOrSubjectDepartmentCode = "";
                        if (certificateManager.isNullReturnEmpty(fadInvoice.getPrmProjectMainId()).length() > 0) {
                            PrmProjectMain prmProjectMain = certificateManager.isNullReturnEmpty(fadInvoice.getPrmProjectMainId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, certificateManager.isNullReturnEmpty(fadInvoice.getPrmProjectMainId())) : null;
                            if (prmProjectMain != null) {
                                projectCodeOrSubjectCode = certificateManager.isNullReturnEmpty(prmProjectMain.getProjectCode());
                                projectDepartmentCodeOrSubjectDepartmentCode = certificateManager.isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                            }
                        } else {
                            projectCodeOrSubjectCode = certificateManager.isNullReturnEmpty(fadInvoice.getSubjectCode());

                            Map<String, Object> nonProjectSubjectSubjectConditionsMap = new HashMap<String, Object>();
                            nonProjectSubjectSubjectConditionsMap.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, projectCodeOrSubjectCode);
                            List nonProjectSubjectSubjectList = certificateManager.isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0 ? DtoHelper.findByAnyFields(NonProjectSubjectSubjectDto.class, nonProjectSubjectSubjectConditionsMap, null) : new ArrayList<>();
                            if (nonProjectSubjectSubjectList.size() > 0) {
                                NonProjectSubjectSubjectDto nonProjectSubjectSubject = (NonProjectSubjectSubjectDto) nonProjectSubjectSubjectList.get(0);
                                projectDepartmentCodeOrSubjectDepartmentCode = certificateManager.isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                            }
                        }

                        //【项目部门名称】或【费用科目部门名称】取得
                        String projectDepartmentCodeOrSubjectDepartmentName = "";
                        Map<String, Object> scdpOrgConditionsMap = new HashMap<String, Object>();
                        scdpOrgConditionsMap.put(ScdpOrgAttribute.ORG_CODE, projectDepartmentCodeOrSubjectDepartmentCode);
                        List<ScdpOrg> scdpOrgList = certificateManager.isNullReturnEmpty(projectDepartmentCodeOrSubjectDepartmentCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpOrg.class, scdpOrgConditionsMap, null) : new ArrayList<>();
                        if (scdpOrgList.size() > 0) {
                            ScdpOrg scdpOrg = (ScdpOrg) scdpOrgList.get(0);
                            projectDepartmentCodeOrSubjectDepartmentName = certificateManager.isNullReturnEmpty(scdpOrg.getOrgName());
                        }
                    }

                    //6.无请款采购合同
                    //7.有请款采购合同应付、有请款采购合同预付
                    else if (originalFormType.equals("6") || originalFormType.equals("7")) {

                        //【报销人】取得
                        UserId = certificateManager.isNullReturnEmpty(fadInvoice.getRenderPerson());

                        //【项目代号】或【费用科目代号】取得
                        //【项目部门代号】或【费用科目部门代号】取得
                        //String projectCodeOrSubjectCode = "";
                        //String projectDepartmentCodeOrSubjectDepartmentCode = "";
                        Map<String, Object> scmContractInvoiceConditionsMap = new HashMap<String, Object>();
                        scmContractInvoiceConditionsMap.put(ScmContractInvoiceAttribute.FAD_INVOICE_ID, certificateManager.isNullReturnEmpty(fadInvoice.getUuid()));
                        List<ScmContractInvoice> scmContractInvoiceList = certificateManager.isNullReturnEmpty(fadInvoice.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScmContractInvoice.class, scmContractInvoiceConditionsMap, null) : new ArrayList<>();
                        if (scmContractInvoiceList.size() > 0) {
                            for (int yy = 0; yy < scmContractInvoiceList.size(); yy++) {
                                ScmContractInvoice scmContractInvoice = scmContractInvoiceList.get(yy);
                                ScmContract scmContract = certificateManager.isNullReturnEmpty(scmContractInvoice.getScmContractId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmContract.class, certificateManager.isNullReturnEmpty(scmContractInvoice.getScmContractId())) : null;
                                if (scmContract != null) {
                                    if ((certificateManager.isNullReturnEmpty(scmContract.getScmContractCode()).length() > 0) && (certificateManager.isNullReturnEmpty(originalFormCode).indexOf(certificateManager.isNullReturnEmpty(scmContract.getScmContractCode())) != -1)) {
                                        if (certificateManager.isNullReturnEmpty(scmContract.getIsProject()).equals("1")) {
                                            PrmProjectMain prmProjectMain = certificateManager.isNullReturnEmpty(scmContract.getProjectId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, certificateManager.isNullReturnEmpty(scmContract.getProjectId())) : null;
                                            if (prmProjectMain != null) {
                                                if (!(certificateManager.isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0)) {
                                                    projectCodeOrSubjectCode = certificateManager.isNullReturnEmpty(prmProjectMain.getProjectCode());
                                                } else {
                                                    projectCodeOrSubjectCode = certificateManager.isNullReturnEmpty(projectCodeOrSubjectCode) + certificateManager.MergeSplitSymbol + certificateManager.isNullReturnEmpty(prmProjectMain.getProjectCode());
                                                }
                                                if (!(certificateManager.isNullReturnEmpty(projectDepartmentCodeOrSubjectDepartmentCode).length() > 0)) {
                                                    projectDepartmentCodeOrSubjectDepartmentCode = certificateManager.isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                                } else {
                                                    projectDepartmentCodeOrSubjectDepartmentCode = certificateManager.isNullReturnEmpty(projectDepartmentCodeOrSubjectDepartmentCode) + certificateManager.MergeSplitSymbol + certificateManager.isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                                }
                                            }
                                        } else {
                                            if (!(certificateManager.isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0)) {
                                                projectCodeOrSubjectCode = certificateManager.isNullReturnEmpty(scmContract.getSubjectCode());
                                            } else {
                                                projectCodeOrSubjectCode = certificateManager.isNullReturnEmpty(projectCodeOrSubjectCode) + certificateManager.MergeSplitSymbol + certificateManager.isNullReturnEmpty(scmContract.getSubjectCode());
                                            }

                                            Map<String, Object> nonProjectSubjectSubjectConditionsMap = new HashMap<String, Object>();
                                            nonProjectSubjectSubjectConditionsMap.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, certificateManager.isNullReturnEmpty(scmContract.getSubjectCode()));
                                            List nonProjectSubjectSubjectList = certificateManager.isNullReturnEmpty(scmContract.getSubjectCode()).length() > 0 ? DtoHelper.findByAnyFields(NonProjectSubjectSubjectDto.class, nonProjectSubjectSubjectConditionsMap, null) : new ArrayList<>();
                                            if (nonProjectSubjectSubjectList.size() > 0) {
                                                NonProjectSubjectSubjectDto nonProjectSubjectSubject = (NonProjectSubjectSubjectDto) nonProjectSubjectSubjectList.get(0);
                                                if (!(certificateManager.isNullReturnEmpty(projectDepartmentCodeOrSubjectDepartmentCode).length() > 0)) {
                                                    projectDepartmentCodeOrSubjectDepartmentCode = certificateManager.isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                                } else {
                                                    projectDepartmentCodeOrSubjectDepartmentCode = projectDepartmentCodeOrSubjectDepartmentCode + certificateManager.MergeSplitSymbol + certificateManager.isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            if (certificateManager.isNullReturnEmpty(fadInvoice.getPrmProjectMainId()).length() > 0) {
                                PrmProjectMain prmProjectMain = certificateManager.isNullReturnEmpty(fadInvoice.getPrmProjectMainId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, certificateManager.isNullReturnEmpty(fadInvoice.getPrmProjectMainId())) : null;
                                if (prmProjectMain != null) {
                                    projectCodeOrSubjectCode = certificateManager.isNullReturnEmpty(prmProjectMain.getProjectCode());
                                    projectDepartmentCodeOrSubjectDepartmentCode = certificateManager.isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                }
                            } else {
                                projectCodeOrSubjectCode = certificateManager.isNullReturnEmpty(fadInvoice.getSubjectCode());

                                Map<String, Object> nonProjectSubjectSubjectConditionsMap = new HashMap<String, Object>();
                                nonProjectSubjectSubjectConditionsMap.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, projectCodeOrSubjectCode);
                                List nonProjectSubjectSubjectList = certificateManager.isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0 ? DtoHelper.findByAnyFields(NonProjectSubjectSubjectDto.class, nonProjectSubjectSubjectConditionsMap, null) : new ArrayList<>();
                                if (nonProjectSubjectSubjectList.size() > 0) {
                                    NonProjectSubjectSubjectDto nonProjectSubjectSubject = (NonProjectSubjectSubjectDto) nonProjectSubjectSubjectList.get(0);
                                    projectDepartmentCodeOrSubjectDepartmentCode = certificateManager.isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                }
                            }
                        }
                    }

                    //8.采购合同成本确认
                    else if (originalFormType.equals("8")) {

                        //【报销人】取得
                        UserId = certificateManager.isNullReturnEmpty(fadInvoice.getRenderPerson());

                        //【项目代号】或【费用科目代号】取得
                        //【项目部门代号】或【费用科目部门代号】取得
                        //String projectCodeOrSubjectCode = "";
                        //String projectDepartmentCodeOrSubjectDepartmentCode = "";
                        Map<String, Object> scmContractInvoiceConditionsMap = new HashMap<String, Object>();
                        scmContractInvoiceConditionsMap.put(ScmContractInvoiceAttribute.FAD_INVOICE_ID, certificateManager.isNullReturnEmpty(fadInvoice.getUuid()));
                        List<ScmContractInvoice> scmContractInvoiceList = certificateManager.isNullReturnEmpty(fadInvoice.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScmContractInvoice.class, scmContractInvoiceConditionsMap, null) : new ArrayList<>();
                        if (scmContractInvoiceList.size() > 0) {
                            for (int yy = 0; yy < scmContractInvoiceList.size(); yy++) {
                                ScmContractInvoice scmContractInvoice = scmContractInvoiceList.get(yy);
                                ScmContract scmContract = certificateManager.isNullReturnEmpty(scmContractInvoice.getScmContractId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmContract.class, certificateManager.isNullReturnEmpty(scmContractInvoice.getScmContractId())) : null;
                                if (scmContract != null) {
                                    if ((certificateManager.isNullReturnEmpty(scmContract.getScmContractCode()).length() > 0) && (certificateManager.isNullReturnEmpty(originalFormCode).indexOf(certificateManager.isNullReturnEmpty(scmContract.getScmContractCode())) != -1)) {
                                        if (certificateManager.isNullReturnEmpty(scmContract.getIsProject()).equals("1")) {
                                            PrmProjectMain prmProjectMain = certificateManager.isNullReturnEmpty(scmContract.getProjectId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, certificateManager.isNullReturnEmpty(scmContract.getProjectId())) : null;
                                            if (prmProjectMain != null) {
                                                if (!(certificateManager.isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0)) {
                                                    projectCodeOrSubjectCode = certificateManager.isNullReturnEmpty(prmProjectMain.getProjectCode());
                                                } else {
                                                    projectCodeOrSubjectCode = certificateManager.isNullReturnEmpty(projectCodeOrSubjectCode) + certificateManager.MergeSplitSymbol + certificateManager.isNullReturnEmpty(prmProjectMain.getProjectCode());
                                                }
                                                if (!(certificateManager.isNullReturnEmpty(projectDepartmentCodeOrSubjectDepartmentCode).length() > 0)) {
                                                    projectDepartmentCodeOrSubjectDepartmentCode = certificateManager.isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                                } else {
                                                    projectDepartmentCodeOrSubjectDepartmentCode = certificateManager.isNullReturnEmpty(projectDepartmentCodeOrSubjectDepartmentCode) + certificateManager.MergeSplitSymbol + certificateManager.isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                                }
                                            }
                                        } else {
                                            if (!(certificateManager.isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0)) {
                                                projectCodeOrSubjectCode = certificateManager.isNullReturnEmpty(scmContract.getSubjectCode());
                                            } else {
                                                projectCodeOrSubjectCode = certificateManager.isNullReturnEmpty(projectCodeOrSubjectCode) + certificateManager.MergeSplitSymbol + certificateManager.isNullReturnEmpty(scmContract.getSubjectCode());
                                            }

                                            Map<String, Object> nonProjectSubjectSubjectConditionsMap = new HashMap<String, Object>();
                                            nonProjectSubjectSubjectConditionsMap.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, certificateManager.isNullReturnEmpty(scmContract.getSubjectCode()));
                                            List nonProjectSubjectSubjectList = certificateManager.isNullReturnEmpty(scmContract.getSubjectCode()).length() > 0 ? DtoHelper.findByAnyFields(NonProjectSubjectSubjectDto.class, nonProjectSubjectSubjectConditionsMap, null) : new ArrayList<>();
                                            if (nonProjectSubjectSubjectList.size() > 0) {
                                                NonProjectSubjectSubjectDto nonProjectSubjectSubject = (NonProjectSubjectSubjectDto) nonProjectSubjectSubjectList.get(0);
                                                if (!(certificateManager.isNullReturnEmpty(projectDepartmentCodeOrSubjectDepartmentCode).length() > 0)) {
                                                    projectDepartmentCodeOrSubjectDepartmentCode = certificateManager.isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                                } else {
                                                    projectDepartmentCodeOrSubjectDepartmentCode = projectDepartmentCodeOrSubjectDepartmentCode + certificateManager.MergeSplitSymbol + certificateManager.isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            if (certificateManager.isNullReturnEmpty(fadInvoice.getPrmProjectMainId()).length() > 0) {
                                PrmProjectMain prmProjectMain = certificateManager.isNullReturnEmpty(fadInvoice.getPrmProjectMainId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, certificateManager.isNullReturnEmpty(fadInvoice.getPrmProjectMainId())) : null;
                                if (prmProjectMain != null) {
                                    projectCodeOrSubjectCode = certificateManager.isNullReturnEmpty(prmProjectMain.getProjectCode());
                                    projectDepartmentCodeOrSubjectDepartmentCode = certificateManager.isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                }
                            } else {
                                projectCodeOrSubjectCode = certificateManager.isNullReturnEmpty(fadInvoice.getSubjectCode());

                                Map<String, Object> nonProjectSubjectSubjectConditionsMap = new HashMap<String, Object>();
                                nonProjectSubjectSubjectConditionsMap.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, projectCodeOrSubjectCode);
                                List nonProjectSubjectSubjectList = certificateManager.isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0 ? DtoHelper.findByAnyFields(NonProjectSubjectSubjectDto.class, nonProjectSubjectSubjectConditionsMap, null) : new ArrayList<>();
                                if (nonProjectSubjectSubjectList.size() > 0) {
                                    NonProjectSubjectSubjectDto nonProjectSubjectSubject = (NonProjectSubjectSubjectDto) nonProjectSubjectSubjectList.get(0);
                                    projectDepartmentCodeOrSubjectDepartmentCode = certificateManager.isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                }
                            }
                        }
                    }

                    //9.采购合同支付直接支付
                    else if (originalFormType.equals("9")) {

                        //【报销人】取得
                        UserId = certificateManager.isNullReturnEmpty(fadInvoice.getRenderPerson());

                        //【项目代号】或【费用科目代号】取得
                        //【项目部门代号】或【费用科目部门代号】取得
                        //String projectCodeOrSubjectCode = "";
                        //String projectDepartmentCodeOrSubjectDepartmentCode = "";
                        Map<String, Object> scmContractInvoiceConditionsMap = new HashMap<String, Object>();
                        scmContractInvoiceConditionsMap.put(ScmContractInvoiceAttribute.FAD_INVOICE_ID, certificateManager.isNullReturnEmpty(fadInvoice.getUuid()));
                        List<ScmContractInvoice> scmContractInvoiceList = certificateManager.isNullReturnEmpty(fadInvoice.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScmContractInvoice.class, scmContractInvoiceConditionsMap, null) : new ArrayList<>();
                        if (scmContractInvoiceList.size() > 0) {
                            for (int yy = 0; yy < scmContractInvoiceList.size(); yy++) {
                                ScmContractInvoice scmContractInvoice = scmContractInvoiceList.get(yy);
                                ScmContract scmContract = certificateManager.isNullReturnEmpty(scmContractInvoice.getScmContractId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmContract.class, certificateManager.isNullReturnEmpty(scmContractInvoice.getScmContractId())) : null;
                                if (scmContract != null) {
                                    if ((certificateManager.isNullReturnEmpty(scmContract.getScmContractCode()).length() > 0) && (certificateManager.isNullReturnEmpty(originalFormCode).indexOf(certificateManager.isNullReturnEmpty(scmContract.getScmContractCode())) != -1)) {
                                        if (certificateManager.isNullReturnEmpty(scmContract.getIsProject()).equals("1")) {
                                            PrmProjectMain prmProjectMain = certificateManager.isNullReturnEmpty(scmContract.getProjectId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, certificateManager.isNullReturnEmpty(scmContract.getProjectId())) : null;
                                            if (prmProjectMain != null) {
                                                if (!(certificateManager.isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0)) {
                                                    projectCodeOrSubjectCode = certificateManager.isNullReturnEmpty(prmProjectMain.getProjectCode());
                                                } else {
                                                    projectCodeOrSubjectCode = certificateManager.isNullReturnEmpty(projectCodeOrSubjectCode) + certificateManager.MergeSplitSymbol + certificateManager.isNullReturnEmpty(prmProjectMain.getProjectCode());
                                                }
                                                if (!(certificateManager.isNullReturnEmpty(projectDepartmentCodeOrSubjectDepartmentCode).length() > 0)) {
                                                    projectDepartmentCodeOrSubjectDepartmentCode = certificateManager.isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                                } else {
                                                    projectDepartmentCodeOrSubjectDepartmentCode = certificateManager.isNullReturnEmpty(projectDepartmentCodeOrSubjectDepartmentCode) + certificateManager.MergeSplitSymbol + certificateManager.isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                                }
                                            }
                                        } else {
                                            if (!(certificateManager.isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0)) {
                                                projectCodeOrSubjectCode = certificateManager.isNullReturnEmpty(scmContract.getSubjectCode());
                                            } else {
                                                projectCodeOrSubjectCode = certificateManager.isNullReturnEmpty(projectCodeOrSubjectCode) + certificateManager.MergeSplitSymbol + certificateManager.isNullReturnEmpty(scmContract.getSubjectCode());
                                            }

                                            Map<String, Object> nonProjectSubjectSubjectConditionsMap = new HashMap<String, Object>();
                                            nonProjectSubjectSubjectConditionsMap.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, certificateManager.isNullReturnEmpty(scmContract.getSubjectCode()));
                                            List nonProjectSubjectSubjectList = certificateManager.isNullReturnEmpty(scmContract.getSubjectCode()).length() > 0 ? DtoHelper.findByAnyFields(NonProjectSubjectSubjectDto.class, nonProjectSubjectSubjectConditionsMap, null) : new ArrayList<>();
                                            if (nonProjectSubjectSubjectList.size() > 0) {
                                                NonProjectSubjectSubjectDto nonProjectSubjectSubject = (NonProjectSubjectSubjectDto) nonProjectSubjectSubjectList.get(0);
                                                if (!(certificateManager.isNullReturnEmpty(projectDepartmentCodeOrSubjectDepartmentCode).length() > 0)) {
                                                    projectDepartmentCodeOrSubjectDepartmentCode = certificateManager.isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                                } else {
                                                    projectDepartmentCodeOrSubjectDepartmentCode = projectDepartmentCodeOrSubjectDepartmentCode + certificateManager.MergeSplitSymbol + certificateManager.isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            if (certificateManager.isNullReturnEmpty(fadInvoice.getPrmProjectMainId()).length() > 0) {
                                PrmProjectMain prmProjectMain = certificateManager.isNullReturnEmpty(fadInvoice.getPrmProjectMainId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, certificateManager.isNullReturnEmpty(fadInvoice.getPrmProjectMainId())) : null;
                                if (prmProjectMain != null) {
                                    projectCodeOrSubjectCode = certificateManager.isNullReturnEmpty(prmProjectMain.getProjectCode());
                                    projectDepartmentCodeOrSubjectDepartmentCode = certificateManager.isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                }
                            } else {
                                projectCodeOrSubjectCode = certificateManager.isNullReturnEmpty(fadInvoice.getSubjectCode());

                                Map<String, Object> nonProjectSubjectSubjectConditionsMap = new HashMap<String, Object>();
                                nonProjectSubjectSubjectConditionsMap.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, projectCodeOrSubjectCode);
                                List nonProjectSubjectSubjectList = certificateManager.isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0 ? DtoHelper.findByAnyFields(NonProjectSubjectSubjectDto.class, nonProjectSubjectSubjectConditionsMap, null) : new ArrayList<>();
                                if (nonProjectSubjectSubjectList.size() > 0) {
                                    NonProjectSubjectSubjectDto nonProjectSubjectSubject = (NonProjectSubjectSubjectDto) nonProjectSubjectSubjectList.get(0);
                                    projectDepartmentCodeOrSubjectDepartmentCode = certificateManager.isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                }
                            }
                        }
                    }
                }
            } else if (originalFormTypeForeign.equals("1")) {

                //1.FAD_CASH_REQ(请款)
                FadCashReq fadCashReq = certificateManager.isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadCashReq.class, businessId) : null;
                if (fadCashReq != null) {

                    //1.个人请款(1日常2差旅)
                    if (originalFormType.equals("1")) {

                        //【请款人】取得
                        UserId = certificateManager.isNullReturnEmpty(fadCashReq.getStaffId());
                        String userName = "";
                        Map<String, Object> scdpUserConditionsMap = new HashMap<String, Object>();
                        scdpUserConditionsMap.put(ScdpUserAttribute.USER_ID, UserId);
                        List<ScdpUser> scdpUserList = certificateManager.isNullReturnEmpty(UserId).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, scdpUserConditionsMap, null) : new ArrayList<>();
                        if (scdpUserList.size() > 0) {
                            ScdpUser scdpUser = (ScdpUser) scdpUserList.get(0);
                            userName = certificateManager.isNullReturnEmpty(scdpUser.getUserName());
                        }

                        //【项目代号】或【费用科目代号】取得
                        //【项目部门代号】或【费用科目部门代号】取得
                        //String projectCodeOrSubjectCode = "";
                        //String projectDepartmentCodeOrSubjectDepartmentCode = "";
                        if (certificateManager.isNullReturnEmpty(fadCashReq.getIsProject()).equals("1")) {
                            PrmProjectMain prmProjectMain = certificateManager.isNullReturnEmpty(fadCashReq.getProjectId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, certificateManager.isNullReturnEmpty(fadCashReq.getProjectId())) : null;
                            if (prmProjectMain != null) {
                                projectCodeOrSubjectCode = certificateManager.isNullReturnEmpty(prmProjectMain.getProjectCode());
                                projectDepartmentCodeOrSubjectDepartmentCode = certificateManager.isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                            }
                        } else {
                            projectCodeOrSubjectCode = certificateManager.isNullReturnEmpty(fadCashReq.getSubjectCode());

                            Map<String, Object> nonProjectSubjectSubjectConditionsMap = new HashMap<String, Object>();
                            nonProjectSubjectSubjectConditionsMap.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, projectCodeOrSubjectCode);
                            List nonProjectSubjectSubjectList = certificateManager.isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0 ? DtoHelper.findByAnyFields(NonProjectSubjectSubjectDto.class, nonProjectSubjectSubjectConditionsMap, null) : new ArrayList<>();
                            if (nonProjectSubjectSubjectList.size() > 0) {
                                NonProjectSubjectSubjectDto nonProjectSubjectSubject = (NonProjectSubjectSubjectDto) nonProjectSubjectSubjectList.get(0);
                                projectDepartmentCodeOrSubjectDepartmentCode = certificateManager.isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                            }
                        }

                        //【项目部门名称】或【费用科目部门名称】取得
                        String projectDepartmentCodeOrSubjectDepartmentName = "";
                        Map<String, Object> scdpOrgConditionsMap = new HashMap<String, Object>();
                        scdpOrgConditionsMap.put(ScdpOrgAttribute.ORG_CODE, projectDepartmentCodeOrSubjectDepartmentCode);
                        List<ScdpOrg> scdpOrgList = certificateManager.isNullReturnEmpty(projectDepartmentCodeOrSubjectDepartmentCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpOrg.class, scdpOrgConditionsMap, null) : new ArrayList<>();
                        if (scdpOrgList.size() > 0) {
                            ScdpOrg scdpOrg = (ScdpOrg) scdpOrgList.get(0);
                            projectDepartmentCodeOrSubjectDepartmentName = certificateManager.isNullReturnEmpty(scdpOrg.getOrgName());
                        }
                    }

                    //2.材料费或外包商请款(0采购合同)
                    else if (originalFormType.equals("2")) {

                        //【请款人】取得
                        UserId = certificateManager.isNullReturnEmpty(fadCashReq.getStaffId());

                        //【项目代号】或【费用科目代号】取得
                        //【项目部门代号】或【费用科目部门代号】取得
                        //【费用科目内容】取得
                        //String projectCodeOrSubjectCode = "";
                        //String projectDepartmentCodeOrSubjectDepartmentCode = "";
                        ScmContract scmContract = certificateManager.isNullReturnEmpty(fadCashReq.getPurchaseContractId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmContract.class, certificateManager.isNullReturnEmpty(fadCashReq.getPurchaseContractId())) : null;
                        if (scmContract != null) {
                            if (certificateManager.isNullReturnEmpty(scmContract.getIsProject()).equals("1")) {
                                PrmProjectMain prmProjectMain = certificateManager.isNullReturnEmpty(scmContract.getProjectId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, certificateManager.isNullReturnEmpty(scmContract.getProjectId())) : null;
                                if (prmProjectMain != null) {
                                    projectCodeOrSubjectCode = certificateManager.isNullReturnEmpty(prmProjectMain.getProjectCode());
                                    projectDepartmentCodeOrSubjectDepartmentCode = certificateManager.isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                }

                                String sql = "SELECT SUBJECT_NAME FROM FAD_BASE_SUBJECT WHERE SUBJECT_NO = '" + certificateManager.isNullReturnEmpty(scmContract.getSubjectCode()) + "'";
                                DAOMeta daoMeta = new DAOMeta();
                                daoMeta.setStrSql(sql);
                                daoMeta.setNeedFilter(false);
                                List fadBaseSubjectList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                            } else {
                                projectCodeOrSubjectCode = certificateManager.isNullReturnEmpty(scmContract.getSubjectCode());

                                Map<String, Object> nonProjectSubjectSubjectConditionsMap = new HashMap<String, Object>();
                                nonProjectSubjectSubjectConditionsMap.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, projectCodeOrSubjectCode);
                                List nonProjectSubjectSubjectList = certificateManager.isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0 ? DtoHelper.findByAnyFields(NonProjectSubjectSubjectDto.class, nonProjectSubjectSubjectConditionsMap, null) : new ArrayList<>();
                                if (nonProjectSubjectSubjectList.size() > 0) {
                                    NonProjectSubjectSubjectDto nonProjectSubjectSubject = (NonProjectSubjectSubjectDto) nonProjectSubjectSubjectList.get(0);
                                    projectDepartmentCodeOrSubjectDepartmentCode = certificateManager.isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                }
                            }
                        }
                    }

                    //3.保证金请款(3保证金)
                    else if (originalFormType.equals("3")) {

                        //【请款人】取得
                        UserId = certificateManager.isNullReturnEmpty(fadCashReq.getStaffId());
                    }
                    //周转金请款
                    else if (originalFormType.equals("14")) {

                        //【请款人】取得
                        UserId = certificateManager.isNullReturnEmpty(fadCashReq.getStaffId());
                    }
                }
            } else if (originalFormTypeForeign.equals("2")) {

                //2.FAD_PAY_REQ_C(支付)【提示:请用王兆刚的ERP账号登录测试(支付)的生成凭证!】
                FadPayReqC fadPayReqC = certificateManager.isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadPayReqC.class, businessId) : null;
                if (fadPayReqC != null) {

                    //10.采购合同支付
                    if (originalFormType.equals("10")) {

                        //【项目代号】或【费用科目代号】取得
                        //String projectCodeOrSubjectCode = "";
                        //String projectDepartmentCodeOrSubjectDepartmentCode = "";
                        ScmContract scmContract = certificateManager.isNullReturnEmpty(fadPayReqC.getScmContractId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmContract.class, certificateManager.isNullReturnEmpty(fadPayReqC.getScmContractId())) : null;
                        if (scmContract != null) {
                            if (certificateManager.isNullReturnEmpty(scmContract.getIsProject()).equals("1")) {
                                PrmProjectMain prmProjectMain = certificateManager.isNullReturnEmpty(scmContract.getProjectId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, certificateManager.isNullReturnEmpty(scmContract.getProjectId())) : null;
                                if (prmProjectMain != null) {
                                    projectCodeOrSubjectCode = certificateManager.isNullReturnEmpty(prmProjectMain.getProjectCode());
                                    projectDepartmentCodeOrSubjectDepartmentCode = certificateManager.isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                }
                            } else {
                                projectCodeOrSubjectCode = certificateManager.isNullReturnEmpty(scmContract.getSubjectCode());

                                Map<String, Object> nonProjectSubjectSubjectConditionsMap = new HashMap<String, Object>();
                                nonProjectSubjectSubjectConditionsMap.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, projectCodeOrSubjectCode);
                                List nonProjectSubjectSubjectList = certificateManager.isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0 ? DtoHelper.findByAnyFields(NonProjectSubjectSubjectDto.class, nonProjectSubjectSubjectConditionsMap, null) : new ArrayList<>();
                                if (nonProjectSubjectSubjectList.size() > 0) {
                                    NonProjectSubjectSubjectDto nonProjectSubjectSubject = (NonProjectSubjectSubjectDto) nonProjectSubjectSubjectList.get(0);
                                    projectDepartmentCodeOrSubjectDepartmentCode = certificateManager.isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                }
                            }
                        }
                    }
                }
            } else if (originalFormTypeForeign.equals("3")) {

                //3.FAD_CASH_CLEARANCE(还款)
                FadCashClearance fadCashClearance = certificateManager.isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadCashClearance.class, businessId) : null;
                if (fadCashClearance != null) {

                    //11.还款
                    if (originalFormType.equals("11")) {

                        //【还款人】取得
                        //【还款人部门名称】取得
                        UserId = certificateManager.isNullReturnEmpty(fadCashClearance.getClearancePerson());
                        String userName = "";
                        String DepartmentName = "";
                        Map<String, Object> scdpUserConditionsMap = new HashMap<String, Object>();
                        scdpUserConditionsMap.put(ScdpUserAttribute.USER_ID, UserId);
                        List<ScdpUser> scdpUserList = certificateManager.isNullReturnEmpty(UserId).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, scdpUserConditionsMap, null) : new ArrayList<>();
                        if (scdpUserList.size() > 0) {
                            ScdpUser scdpUser = (ScdpUser) scdpUserList.get(0);
                            userName = certificateManager.isNullReturnEmpty(scdpUser.getUserName());

                            ScdpOrg scdpOrg = certificateManager.isNullReturnEmpty(scdpUser.getOrgUuid()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScdpOrg.class, certificateManager.isNullReturnEmpty(scdpUser.getOrgUuid())) : null;
                            if (scdpOrg != null) {
                                DepartmentName = certificateManager.isNullReturnEmpty(scdpOrg.getOrgName());
                            }
                        }

                        //【项目代号】或【费用科目代号】取得
                        //【项目部门代号】或【费用科目部门代号】取得
                        //【费用科目内容】取得
                        //String projectCodeOrSubjectCode = "";
                        //String projectDepartmentCodeOrSubjectDepartmentCode = "";
                        Map<String, Object> fadCashReqInvoiceConditionsMap = new HashMap<String, Object>();
                        fadCashReqInvoiceConditionsMap.put(FadCashReqInvoiceAttribute.FAD_INVOICE_ID, certificateManager.isNullReturnEmpty(fadCashClearance.getUuid()));
                        List<FadCashReqInvoice> fadCashReqInvoiceList = certificateManager.isNullReturnEmpty(fadCashClearance.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCashReqInvoice.class, fadCashReqInvoiceConditionsMap, null) : new ArrayList<>();
                        if (fadCashReqInvoiceList.size() > 0) {
                            FadCashReqInvoice fadCashReqInvoice = fadCashReqInvoiceList.get(0);
                            FadCashReq fadCashReq = certificateManager.isNullReturnEmpty(fadCashReqInvoice.getFadCashReqId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadCashReq.class, certificateManager.isNullReturnEmpty(fadCashReqInvoice.getFadCashReqId())) : null;
                            if (fadCashReq != null) {

                                //采购还款
                                if (certificateManager.isNullReturnEmpty(fadCashReq.getReqType()).equals("0")) {

                                    //【项目代号】或【费用科目代号】取得
                                    //【费用科目内容】取得
                                    ScmContract scmContract = certificateManager.isNullReturnEmpty(fadCashReq.getPurchaseContractId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmContract.class, certificateManager.isNullReturnEmpty(fadCashReq.getPurchaseContractId())) : null;
                                    if (scmContract != null) {
                                        if (certificateManager.isNullReturnEmpty(scmContract.getIsProject()).equals("1")) {
                                            PrmProjectMain prmProjectMain = certificateManager.isNullReturnEmpty(scmContract.getProjectId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, certificateManager.isNullReturnEmpty(scmContract.getProjectId())) : null;
                                            if (prmProjectMain != null) {
                                                projectCodeOrSubjectCode = certificateManager.isNullReturnEmpty(prmProjectMain.getProjectCode());
                                                //projectDepartmentCodeOrSubjectDepartmentCode = certificateManager.isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                            }

                                            String sql = "SELECT SUBJECT_NAME FROM FAD_BASE_SUBJECT WHERE SUBJECT_NO = '" + certificateManager.isNullReturnEmpty(scmContract.getSubjectCode()) + "'";
                                            DAOMeta daoMeta = new DAOMeta();
                                            daoMeta.setStrSql(sql);
                                            daoMeta.setNeedFilter(false);
                                            List fadBaseSubjectList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                                        } else {
                                            projectCodeOrSubjectCode = certificateManager.isNullReturnEmpty(scmContract.getSubjectCode());

                                            Map<String, Object> nonProjectSubjectSubjectConditionsMap = new HashMap<String, Object>();
                                            nonProjectSubjectSubjectConditionsMap.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, projectCodeOrSubjectCode);
                                            List nonProjectSubjectSubjectList = certificateManager.isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0 ? DtoHelper.findByAnyFields(NonProjectSubjectSubjectDto.class, nonProjectSubjectSubjectConditionsMap, null) : new ArrayList<>();
                                            if (nonProjectSubjectSubjectList.size() > 0) {
                                                NonProjectSubjectSubjectDto nonProjectSubjectSubject = (NonProjectSubjectSubjectDto) nonProjectSubjectSubjectList.get(0);
                                                //projectDepartmentCodeOrSubjectDepartmentCode = certificateManager.isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                            }
                                        }
                                    }
                                }

                                //其他还款
                                else {
                                    //【项目代号】或【费用科目代号】取得
                                    //【项目部门代号】或【费用科目部门代号】取得
                                    if (certificateManager.isNullReturnEmpty(fadCashReq.getIsProject()).equals("1")) {
                                        PrmProjectMain prmProjectMain = certificateManager.isNullReturnEmpty(fadCashReq.getProjectId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, certificateManager.isNullReturnEmpty(fadCashReq.getProjectId())) : null;
                                        if (prmProjectMain != null) {
                                            projectCodeOrSubjectCode = certificateManager.isNullReturnEmpty(prmProjectMain.getProjectCode());
                                            //projectDepartmentCodeOrSubjectDepartmentCode = certificateManager.isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                        }
                                    } else {
                                        projectCodeOrSubjectCode = certificateManager.isNullReturnEmpty(fadCashReq.getSubjectCode());

                                        Map<String, Object> nonProjectSubjectSubjectConditionsMap = new HashMap<String, Object>();
                                        nonProjectSubjectSubjectConditionsMap.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, projectCodeOrSubjectCode);
                                        List nonProjectSubjectSubjectList = certificateManager.isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0 ? DtoHelper.findByAnyFields(NonProjectSubjectSubjectDto.class, nonProjectSubjectSubjectConditionsMap, null) : new ArrayList<>();
                                        if (nonProjectSubjectSubjectList.size() > 0) {
                                            NonProjectSubjectSubjectDto nonProjectSubjectSubject = (NonProjectSubjectSubjectDto) nonProjectSubjectSubjectList.get(0);
                                            //projectDepartmentCodeOrSubjectDepartmentCode = certificateManager.isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (originalFormTypeForeign.equals("4")) {

                //4.PRM_RECEIPTS(收款)
                PrmReceipts prmReceipts = certificateManager.isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmReceipts.class, businessId) : null;
                if (prmReceipts != null) {

                    //12.收款
                    if (originalFormType.equals("12") || originalFormType.equals("12.1") || originalFormType.equals("12.2")) {

                        //【项目代号】或【费用科目代号】取得
                        //【项目部门代号】或【费用科目部门代号】取得
                        //String projectCodeOrSubjectCode = "";
                        //String projectDepartmentCodeOrSubjectDepartmentCode = "";
                        if (certificateManager.isNullReturnEmpty(prmReceipts.getPrmProjectMainId()).length() > 0) {
                            PrmProjectMain prmProjectMain = certificateManager.isNullReturnEmpty(prmReceipts.getPrmProjectMainId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, certificateManager.isNullReturnEmpty(prmReceipts.getPrmProjectMainId())) : null;
                            if (prmProjectMain != null) {
                                projectCodeOrSubjectCode = certificateManager.isNullReturnEmpty(prmProjectMain.getProjectCode());
                                projectDepartmentCodeOrSubjectDepartmentCode = certificateManager.isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                            }
                        }
                    }
                }
            } else if (originalFormTypeForeign.equals("5")) {

                //5.PRM_BILLING(开票)
                PrmBilling prmBilling = certificateManager.isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmBilling.class, businessId) : null;
                if (prmBilling != null) {

                    //13.开票
                    if (originalFormType.equals("13")) {

                        //【项目代号】或【费用科目代号】取得
                        //【项目部门代号】或【费用科目部门代号】取得
                        //String projectCodeOrSubjectCode = "";
                        //String projectDepartmentCodeOrSubjectDepartmentCode = "";
                        if (certificateManager.isNullReturnEmpty(prmBilling.getPrmProjectMainId()).length() > 0) {
                            PrmProjectMain prmProjectMain = certificateManager.isNullReturnEmpty(prmBilling.getPrmProjectMainId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, certificateManager.isNullReturnEmpty(prmBilling.getPrmProjectMainId())) : null;
                            if (prmProjectMain != null) {
                                projectCodeOrSubjectCode = certificateManager.isNullReturnEmpty(prmProjectMain.getProjectCode());
                                projectDepartmentCodeOrSubjectDepartmentCode = certificateManager.isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                            }
                        }
                    }
                }
            }

            //根据科目编码取得相关核算项
            Map<String, Object> fadAccsubjRtfreevalueConditionsMap = new HashMap<String, Object>();
            fadAccsubjRtfreevalueConditionsMap.put(FadAccsubjRtfreevalueAttribute.SUBJECT_CODE, certificateManager.isNullReturnEmpty(FinanceSubjectCode));
            List<FadAccsubjRtfreevalue> fadAccsubjRtfreevalueList = certificateManager.isNullReturnEmpty(FinanceSubjectCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadAccsubjRtfreevalue.class, fadAccsubjRtfreevalueConditionsMap, "to_number(uuid) asc") : new ArrayList<>();
            for (int yy = 0; yy < fadAccsubjRtfreevalueList.size(); yy++) {
                FadAccsubjRtfreevalue fadAccsubjRtfreevalue = (FadAccsubjRtfreevalue) fadAccsubjRtfreevalueList.get(yy);
                String accountNo = certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getAccountNo());

                if (certificateManager.isNullReturnEmpty(accountNo).length() > 0) {

                    //对方类型为SCDP_USER用户
                    if (certificateManager.isNullReturnEmpty(receiverOrPayerType).equals("SCDP_USER")) {

                        //辅助核算类型为用户
                        if (certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getAccountNo()).equals("1")) {
                            if (certificateManager.isUuidBelongNcCode(receiverOrPayerId)) {
                                Map<String, Object> fadNcPersonConditionsMap = new HashMap<String, Object>();
                                fadNcPersonConditionsMap.put(FadNcPersonAttribute.NC_PERSON_CODE, receiverOrPayerCode);
                                List<FadNcPerson> fadNcPersonList = certificateManager.isNullReturnEmpty(receiverOrPayerCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadNcPerson.class, fadNcPersonConditionsMap, null) : new ArrayList<>();
                                if (fadNcPersonList.size() > 0) {
                                    FadNcPerson fadNcPerson = (FadNcPerson) fadNcPersonList.get(0);
                                    fadAccsubjRtfreevalue.setAccountInfoId(receiverOrPayerId);
                                    Map<String, Object> fadNcOrgConditionsMap = new HashMap<String, Object>();
                                    fadNcOrgConditionsMap.put(FadNcOrgAttribute.NC_ORG_CODE, certificateManager.isNullReturnEmpty(fadNcPerson.getNcOrgCode()));
                                    List<FadNcOrg> fadNcOrgList = certificateManager.isNullReturnEmpty(fadNcPerson.getNcOrgCode()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadNcOrg.class, fadNcOrgConditionsMap, null) : new ArrayList<>();
                                    if (fadNcOrgList.size() > 0) {
                                        FadNcOrg fadNcOrg = (FadNcOrg) fadNcOrgList.get(0);
                                        if (certificateManager.isNullReturnEmpty(fadNcOrg.getNcOrgCode()).length() > 0) {
                                            fadAccsubjRtfreevalue.setAccountInfoCode(certificateManager.isNullReturnEmpty(fadNcPerson.getNcPersonCode()) + "-" + certificateManager.isNullReturnEmpty(fadNcOrg.getNcOrgCode()));
                                        } else {
                                            fadAccsubjRtfreevalue.setAccountInfoCode(certificateManager.isNullReturnEmpty(fadNcPerson.getNcPersonCode()));
                                        }
                                        if (certificateManager.isNullReturnEmpty(fadNcOrg.getNcOrgName()).length() > 0) {
                                            fadAccsubjRtfreevalue.setAccountInfoName(certificateManager.isNullReturnEmpty(fadNcPerson.getNcPersonName()) + "-" + certificateManager.isNullReturnEmpty(fadNcOrg.getNcOrgName()));
                                        } else {
                                            fadAccsubjRtfreevalue.setAccountInfoName(certificateManager.isNullReturnEmpty(fadNcPerson.getNcPersonName()));
                                        }
                                    } else {
                                        fadAccsubjRtfreevalue.setAccountInfoCode(certificateManager.isNullReturnEmpty(fadNcPerson.getNcPersonCode()));
                                        fadAccsubjRtfreevalue.setAccountInfoName(certificateManager.isNullReturnEmpty(fadNcPerson.getNcPersonName()));
                                    }
                                }
                            } else {
                                Map<String, Object> scdpUserConditionsMap = new HashMap<String, Object>();
                                scdpUserConditionsMap.put(ScdpUserAttribute.USER_ID, receiverOrPayerCode);
                                List<ScdpUser> scdpUserList = certificateManager.isNullReturnEmpty(receiverOrPayerCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, scdpUserConditionsMap, null) : new ArrayList<>();
                                if (scdpUserList.size() > 0) {
                                    ScdpUser scdpUser = (ScdpUser) scdpUserList.get(0);
                                    fadAccsubjRtfreevalue.setAccountInfoId(certificateManager.isNullReturnEmpty(scdpUser.getUuid()));
                                    ScdpOrg scdpOrg = certificateManager.isNullReturnEmpty(scdpUser.getOrgUuid()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScdpOrg.class, certificateManager.isNullReturnEmpty(scdpUser.getOrgUuid())) : null;
                                    if (scdpOrg != null) {
                                        if (certificateManager.isNullReturnEmpty(scdpOrg.getOrgCode()).length() > 0) {
                                            fadAccsubjRtfreevalue.setAccountInfoCode(certificateManager.isNullReturnEmpty(scdpUser.getUserId()) + "-" + certificateManager.isNullReturnEmpty(scdpOrg.getOrgCode()));
                                        } else {
                                            fadAccsubjRtfreevalue.setAccountInfoCode(certificateManager.isNullReturnEmpty(scdpUser.getUserId()));
                                        }
                                        if (certificateManager.isNullReturnEmpty(scdpOrg.getOrgName()).length() > 0) {
                                            fadAccsubjRtfreevalue.setAccountInfoName(certificateManager.isNullReturnEmpty(scdpUser.getUserName()) + "-" + certificateManager.isNullReturnEmpty(scdpOrg.getOrgName()));
                                        } else {
                                            fadAccsubjRtfreevalue.setAccountInfoName(certificateManager.isNullReturnEmpty(scdpUser.getUserName()));
                                        }
                                    } else {
                                        fadAccsubjRtfreevalue.setAccountInfoCode(certificateManager.isNullReturnEmpty(scdpUser.getUserId()));
                                        fadAccsubjRtfreevalue.setAccountInfoName(certificateManager.isNullReturnEmpty(scdpUser.getUserName()));
                                    }
                                }
                            }
                        }

                        //辅助核算类型为部门
                        else if (certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getAccountNo()).equals("2")) {
                            if (certificateManager.isUuidBelongNcCode(receiverOrPayerId)) {
                                Map<String, Object> fadNcPersonConditionsMap = new HashMap<String, Object>();
                                fadNcPersonConditionsMap.put(FadNcPersonAttribute.NC_PERSON_CODE, receiverOrPayerCode);
                                List<FadNcPerson> fadNcPersonList = certificateManager.isNullReturnEmpty(receiverOrPayerCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadNcPerson.class, fadNcPersonConditionsMap, null) : new ArrayList<>();
                                if (fadNcPersonList.size() > 0) {
                                    FadNcPerson fadNcPerson = (FadNcPerson) fadNcPersonList.get(0);
                                    Map<String, Object> fadNcOrgConditionsMap = new HashMap<String, Object>();
                                    fadNcOrgConditionsMap.put(FadNcOrgAttribute.NC_ORG_CODE, certificateManager.isNullReturnEmpty(fadNcPerson.getNcOrgCode()));
                                    List<FadNcOrg> fadNcOrgList = certificateManager.isNullReturnEmpty(fadNcPerson.getNcOrgCode()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadNcOrg.class, fadNcOrgConditionsMap, null) : new ArrayList<>();
                                    if (fadNcOrgList.size() > 0) {
                                        FadNcOrg fadNcOrg = (FadNcOrg) fadNcOrgList.get(0);
                                        fadAccsubjRtfreevalue.setAccountInfoId(receiverOrPayerId);
                                        fadAccsubjRtfreevalue.setAccountInfoCode(certificateManager.isNullReturnEmpty(fadNcOrg.getNcOrgCode()));
                                        fadAccsubjRtfreevalue.setAccountInfoName(certificateManager.isNullReturnEmpty(fadNcOrg.getNcOrgName()));
                                    }
                                }
                            } else {
                                Map<String, Object> scdpUserConditionsMap = new HashMap<String, Object>();
                                scdpUserConditionsMap.put(ScdpUserAttribute.USER_ID, receiverOrPayerCode);
                                List<ScdpUser> scdpUserList = certificateManager.isNullReturnEmpty(receiverOrPayerCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, scdpUserConditionsMap, null) : new ArrayList<>();
                                if (scdpUserList.size() > 0) {
                                    ScdpUser scdpUser = (ScdpUser) scdpUserList.get(0);
                                    ScdpOrg scdpOrg = certificateManager.isNullReturnEmpty(scdpUser.getOrgUuid()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScdpOrg.class, certificateManager.isNullReturnEmpty(scdpUser.getOrgUuid())) : null;
                                    if (scdpOrg != null) {
                                        fadAccsubjRtfreevalue.setAccountInfoId(certificateManager.isNullReturnEmpty(scdpOrg.getUuid()));
                                        fadAccsubjRtfreevalue.setAccountInfoCode(certificateManager.isNullReturnEmpty(scdpOrg.getOrgCode()));
                                        fadAccsubjRtfreevalue.setAccountInfoName(certificateManager.isNullReturnEmpty(scdpOrg.getOrgName()));
                                    }
                                }
                            }
                        }
                    }

                    //对方类型为SCM_SUPPLIER供应商
                    else if (certificateManager.isNullReturnEmpty(receiverOrPayerType).equals("SCM_SUPPLIER")) {

                        //辅助核算类型为客商、内部客商、外部客商
                        if (
                                certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getAccountNo()).equals("73")
                                        || certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getAccountNo()).equals("NEIBU")
                                        || certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getAccountNo()).equals("WAIBU")
                                ) {
                            if (certificateManager.isUuidBelongNcCode(receiverOrPayerId)) {
                                fadAccsubjRtfreevalue.setAccountInfoId(receiverOrPayerId);
                                fadAccsubjRtfreevalue.setAccountInfoCode(receiverOrPayerCode);
                                fadAccsubjRtfreevalue.setAccountInfoName(receiverOrPayerName);
                            } else {
                                ScmSupplier scmSupplier = certificateManager.isNullReturnEmpty(receiverOrPayerId).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmSupplier.class, receiverOrPayerId) : null;
                                if (scmSupplier != null) {
                                    fadAccsubjRtfreevalue.setAccountInfoId(certificateManager.isNullReturnEmpty(scmSupplier.getUuid()));
                                    fadAccsubjRtfreevalue.setAccountInfoCode(certificateManager.isNullReturnEmpty(scmSupplier.getSupplierCode()));
                                    fadAccsubjRtfreevalue.setAccountInfoName(certificateManager.isNullReturnEmpty(scmSupplier.getCompleteName()));
                                } else {
                                    fadAccsubjRtfreevalue.setAccountInfoName(receiverOrPayerName);
                                }
                            }
                        }
                    }

                    //对方类型为PRM_CUSTOMER客户
                    else if (certificateManager.isNullReturnEmpty(receiverOrPayerType).equals("PRM_CUSTOMER")) {

                        //辅助核算类型为客商、内部客商、外部客商
                        if (
                                certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getAccountNo()).equals("73")
                                        || certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getAccountNo()).equals("NEIBU")
                                        || certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getAccountNo()).equals("WAIBU")
                                ) {
                            if (certificateManager.isUuidBelongNcCode(receiverOrPayerId)) {
                                fadAccsubjRtfreevalue.setAccountInfoId(receiverOrPayerId);
                                fadAccsubjRtfreevalue.setAccountInfoCode(receiverOrPayerCode);
                                fadAccsubjRtfreevalue.setAccountInfoName(receiverOrPayerName);
                            } else {
                                PrmCustomer prmCustomer = certificateManager.isNullReturnEmpty(receiverOrPayerId).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmCustomer.class, receiverOrPayerId) : null;
                                if (prmCustomer != null) {
                                    fadAccsubjRtfreevalue.setAccountInfoId(certificateManager.isNullReturnEmpty(prmCustomer.getUuid()));
                                    fadAccsubjRtfreevalue.setAccountInfoCode(certificateManager.isNullReturnEmpty(prmCustomer.getCustomerCode()));
                                    fadAccsubjRtfreevalue.setAccountInfoName(certificateManager.isNullReturnEmpty(prmCustomer.getCustomerName()));
                                }
                            }
                        }
                    }

                    //收款(调整)代入指定辅助核算类型:收支类型、客商
                    if (certificateManager.isNullReturnEmpty(originalFormType).equals("12.2")) {

                        //收支类型
                        if (certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getAccountNo()).equals("JGG_CASS")) {
                            fadAccsubjRtfreevalue.setAccountInfoId(certificateManager.NCCodeDataSymbol);
                            fadAccsubjRtfreevalue.setAccountInfoCode("CLKJ98");
                            fadAccsubjRtfreevalue.setAccountInfoName("中海科技无辅助");
                        }

                        //客商
                        else if (
                                certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getAccountNo()).equals("73")
                                        || certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getAccountNo()).equals("NEIBU")
                                        || certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getAccountNo()).equals("WAIBU")
                                ) {
                            fadAccsubjRtfreevalue.setAccountInfoId(certificateManager.NCCodeDataSymbol);
                            fadAccsubjRtfreevalue.setAccountInfoCode("2LKW002416");
                            fadAccsubjRtfreevalue.setAccountInfoName("零星采购商");
                        }
                    }

                    //存在【项目代号】或【费用科目代号】
                    if (projectCodeOrSubjectCode.indexOf(certificateManager.MergeSplitSymbol) != -1) {
                        projectCodeOrSubjectCode = projectCodeOrSubjectCode.split(certificateManager.MergeSplitSymbol)[0];
                    }
                    if (certificateManager.isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0) {

                        //辅助核算类型为科技项目
                        if (certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getAccountNo()).equals("JXX_JASS")) {
                            String accountInfoName = projectCodeOrSubjectCode;
                            if (certificateManager.isNullReturnEmpty(accountInfoName).length() > 0) {
                                Map<String, Object> fadRtfreevalueViewConditionsMap = new HashMap<String, Object>();
                                fadRtfreevalueViewConditionsMap.put(FadRtfreevalueViewAttribute.ACCOUNT_NO, certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getAccountNo()));
                                fadRtfreevalueViewConditionsMap.put(FadRtfreevalueViewAttribute.ACCOUNT_INFO_NAME, accountInfoName);
                                List<FadRtfreevalueView> fadRtfreevalueViewList = ((certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getAccountNo()).length() > 0) && (certificateManager.isNullReturnEmpty(accountInfoName).length() > 0)) ? PersistenceFactory.getInstance().findByAnyFields(FadRtfreevalueView.class, fadRtfreevalueViewConditionsMap, null) : new ArrayList<>();
                                if (fadRtfreevalueViewList.size() > 0) {
                                    FadRtfreevalueView fadRtfreevalueView = (FadRtfreevalueView) fadRtfreevalueViewList.get(0);
                                    fadAccsubjRtfreevalue.setAccountInfoId(certificateManager.isNullReturnEmpty(fadRtfreevalueView.getUuid()));
                                    fadAccsubjRtfreevalue.setAccountInfoCode(certificateManager.isNullReturnEmpty(fadRtfreevalueView.getAccountInfoCode()));
                                }
                                fadAccsubjRtfreevalue.setAccountInfoName(accountInfoName);
                            }
                        }
                    }

                    //存在【项目部门代号】或【费用科目部门代号】
                    if (projectDepartmentCodeOrSubjectDepartmentCode.indexOf(certificateManager.MergeSplitSymbol) != -1) {
                        projectDepartmentCodeOrSubjectDepartmentCode = projectDepartmentCodeOrSubjectDepartmentCode.split(certificateManager.MergeSplitSymbol)[0];
                    }
                    if (certificateManager.isNullReturnEmpty(projectDepartmentCodeOrSubjectDepartmentCode).length() > 0) {

                        //辅助核算类型为部门
                        if (certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getAccountNo()).equals("2")) {
                            Map<String, Object> scdpOrgConditionsMap = new HashMap<String, Object>();
                            scdpOrgConditionsMap.put(ScdpOrgAttribute.ORG_CODE, projectDepartmentCodeOrSubjectDepartmentCode);
                            List<ScdpOrg> scdpOrgList = certificateManager.isNullReturnEmpty(projectDepartmentCodeOrSubjectDepartmentCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpOrg.class, scdpOrgConditionsMap, null) : new ArrayList<>();
                            if (scdpOrgList.size() > 0) {
                                ScdpOrg scdpOrg = (ScdpOrg) scdpOrgList.get(0);
                                fadAccsubjRtfreevalue.setAccountInfoId(certificateManager.isNullReturnEmpty(scdpOrg.getUuid()));
                                fadAccsubjRtfreevalue.setAccountInfoCode(certificateManager.isNullReturnEmpty(scdpOrg.getOrgCode()));
                                fadAccsubjRtfreevalue.setAccountInfoName(certificateManager.isNullReturnEmpty(scdpOrg.getOrgName()));
                            }
                        }
                    }

                    //存在【人员】
                    if (certificateManager.isNullReturnEmpty(UserId).length() > 0) {

                        //辅助核算类型为用户
                        if (certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getAccountNo()).equals("1")) {
                            if (!(certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getAccountInfoName()).length() > 0)) {
                                Map<String, Object> scdpUserConditionsMap = new HashMap<String, Object>();
                                scdpUserConditionsMap.put(ScdpUserAttribute.USER_ID, UserId);
                                List<ScdpUser> scdpUserList = certificateManager.isNullReturnEmpty(UserId).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, scdpUserConditionsMap, null) : new ArrayList<>();
                                if (scdpUserList.size() > 0) {
                                    ScdpUser scdpUser = (ScdpUser) scdpUserList.get(0);
                                    fadAccsubjRtfreevalue.setAccountInfoId(certificateManager.isNullReturnEmpty(scdpUser.getUuid()));
                                    ScdpOrg scdpOrg = certificateManager.isNullReturnEmpty(scdpUser.getOrgUuid()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScdpOrg.class, certificateManager.isNullReturnEmpty(scdpUser.getOrgUuid())) : null;
                                    if (scdpOrg != null) {
                                        if (certificateManager.isNullReturnEmpty(scdpOrg.getOrgCode()).length() > 0) {
                                            fadAccsubjRtfreevalue.setAccountInfoCode(certificateManager.isNullReturnEmpty(scdpUser.getUserId()) + "-" + certificateManager.isNullReturnEmpty(scdpOrg.getOrgCode()));
                                        } else {
                                            fadAccsubjRtfreevalue.setAccountInfoCode(certificateManager.isNullReturnEmpty(scdpUser.getUserId()));
                                        }
                                        if (certificateManager.isNullReturnEmpty(scdpOrg.getOrgName()).length() > 0) {
                                            fadAccsubjRtfreevalue.setAccountInfoName(certificateManager.isNullReturnEmpty(scdpUser.getUserName()) + "-" + certificateManager.isNullReturnEmpty(scdpOrg.getOrgName()));
                                        } else {
                                            fadAccsubjRtfreevalue.setAccountInfoName(certificateManager.isNullReturnEmpty(scdpUser.getUserName()));
                                        }
                                    } else {
                                        fadAccsubjRtfreevalue.setAccountInfoCode(certificateManager.isNullReturnEmpty(scdpUser.getUserId()));
                                        fadAccsubjRtfreevalue.setAccountInfoName(certificateManager.isNullReturnEmpty(scdpUser.getUserName()));
                                    }
                                }
                            }
                        }

                        //辅助核算类型为部门
                        else if (certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getAccountNo()).equals("2")) {
                            if (!(certificateManager.isNullReturnEmpty(fadAccsubjRtfreevalue.getAccountInfoName()).length() > 0)) {
                                Map<String, Object> scdpUserConditionsMap = new HashMap<String, Object>();
                                scdpUserConditionsMap.put(ScdpUserAttribute.USER_ID, UserId);
                                List<ScdpUser> scdpUserList = certificateManager.isNullReturnEmpty(UserId).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, scdpUserConditionsMap, null) : new ArrayList<>();
                                if (scdpUserList.size() > 0) {
                                    ScdpUser scdpUser = (ScdpUser) scdpUserList.get(0);
                                    ScdpOrg scdpOrg = certificateManager.isNullReturnEmpty(scdpUser.getOrgUuid()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScdpOrg.class, certificateManager.isNullReturnEmpty(scdpUser.getOrgUuid())) : null;
                                    if (scdpOrg != null) {
                                        fadAccsubjRtfreevalue.setAccountInfoId(certificateManager.isNullReturnEmpty(scdpOrg.getUuid()));
                                        fadAccsubjRtfreevalue.setAccountInfoCode(certificateManager.isNullReturnEmpty(scdpOrg.getOrgCode()));
                                        fadAccsubjRtfreevalue.setAccountInfoName(certificateManager.isNullReturnEmpty(scdpOrg.getOrgName()));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            result.put("fadAccsubjRtfreevalueList", fadAccsubjRtfreevalueList);
        }

        return result;
    }
}