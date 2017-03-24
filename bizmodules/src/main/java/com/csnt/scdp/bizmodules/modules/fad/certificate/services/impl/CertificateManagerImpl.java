package com.csnt.scdp.bizmodules.modules.fad.certificate.services.impl;

import com.csnt.scdp.bizmodules.entity.fad.FadAccsubjRtfreevalue;
import com.csnt.scdp.bizmodules.entity.fad.FadCashClearance;
import com.csnt.scdp.bizmodules.entity.fad.FadCashReq;
import com.csnt.scdp.bizmodules.entity.fad.FadCashReqInvoice;
import com.csnt.scdp.bizmodules.entity.fad.FadCertifiDeficitRel;
import com.csnt.scdp.bizmodules.entity.fad.FadCertifiMergeSplitRel;
import com.csnt.scdp.bizmodules.entity.fad.FadCertificate;
import com.csnt.scdp.bizmodules.entity.fad.FadCertificateAccount;
import com.csnt.scdp.bizmodules.entity.fad.FadCertificateDetail;
import com.csnt.scdp.bizmodules.entity.fad.FadInvoice;
import com.csnt.scdp.bizmodules.entity.fad.FadPayReqC;
import com.csnt.scdp.bizmodules.entity.fad.FadRtfreevalueView;
import com.csnt.scdp.bizmodules.entity.fad.ScdpWebservices;
import com.csnt.scdp.bizmodules.entity.fad.ScdpWebservicesDesc;
import com.csnt.scdp.bizmodules.entity.prm.PrmBilling;
import com.csnt.scdp.bizmodules.entity.prm.PrmCustomer;
import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entity.prm.PrmReceipts;
import com.csnt.scdp.bizmodules.entity.scm.ScmContract;
import com.csnt.scdp.bizmodules.entity.scm.ScmContractInvoice;
import com.csnt.scdp.bizmodules.entity.scm.ScmSupplier;
import com.csnt.scdp.bizmodules.entity.fad.FadNonProjectSetRule;
import com.csnt.scdp.bizmodules.entity.fad.FadProjectSetRule;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadAccsubjRtfreevalueAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCashReqInvoiceAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCertifiDeficitRelAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCertifiMergeSplitRelAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCertificateAccountAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCertificateAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCertificateDetailAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadRtfreevalueViewAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.ScdpWebservicesDescAttribute;
import com.csnt.scdp.bizmodules.entityattributes.nonprm.NonProjectSubjectSubjectAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmContractInvoiceAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmSupplierAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadNonProjectSetRuleAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadProjectSetRuleAttribute;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpExpandFieldHelper;
import com.csnt.scdp.bizmodules.modules.fad.certificate.dto.FadCertificateAccountDto;
import com.csnt.scdp.bizmodules.modules.fad.certificate.dto.FadCertificateDetailDto;
import com.csnt.scdp.bizmodules.modules.fad.certificate.dto.FadCertificateDto;
import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
import com.csnt.scdp.bizmodules.modules.nonprm.subjectsubject.dto.NonProjectSubjectSubjectDto;
import com.csnt.scdp.framework.bo.QueryCondition;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.UUIDUtil;
import com.csnt.scdp.framework.util.XmlUtil;
import com.csnt.scdp.sysmodules.entity.FmCurrency;
import com.csnt.scdp.sysmodules.entity.ScdpOrg;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.entityattributes.ScdpOrgAttribute;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  CertificateManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-10-16 10:07:48
 */

@Scope("singleton")
@Service("certificate-manager")
public class CertificateManagerImpl implements CertificateManager {

    @Override
    public String isEmptyReturnZero(String value) {
        if (value.trim().length() == 0) {
            return "0";
        } else {
            return value.trim();
        }
    }

    @Override
    public String isZeroReturnEmpty(String value) {
        try {
            if (new BigDecimal(value).compareTo(new BigDecimal("0")) == 0) {
                return "";
            } else {
                return value.trim();
            }
        } catch (Exception e) {
            return value.trim();
        }
    }

    @Override
    public String isNullReturnEmpty(Object value) {
        if (value == null || value.equals("null")) {
            return "";
        } else {
            return value.toString().trim();
        }
    }

    @Override
    public String isNullReturnEmpty(Integer value) {
        if (value == null) {
            return "";
        } else {
            return value.toString().trim();
        }
    }

    @Override
    public void certificateCheckToDayValidDate(String dateStr, String dateFormatStr, String state) {
        if (state.equals("0")) {
            try {
                SimpleDateFormat yyyyDateFormat = new SimpleDateFormat("yyyy");
                SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatStr);
                dateFormat.setLenient(false);
                Date date = dateFormat.parse(dateStr);

                if (dateStr.length() != dateFormatStr.length()) {
                    throw new ParseException(null, 0);
                }

                Date toDay = new Date();
                String toDayS = dateFormat.format(toDay);

                if (Integer.parseInt(toDayS) < Integer.parseInt(dateStr)) {
                    throw new BizException("【会计年月】不许晚于【当前日期】的【年月】");
                } else if (Integer.parseInt(yyyyDateFormat.format(toDay)) - 1 > Integer.parseInt(yyyyDateFormat.format(date))) {
                    throw new BizException("【会计年】只能是【今年】或【去年】");
                }
            } catch (ParseException e) {
                throw new BizException("【会计年月】格式错误!");
            }
        }
    }

    @Override
    public String nvl(Object value1, Object value2) {
        if (isNullReturnEmpty(value1).length() == 0) {
            return isNullReturnEmpty(value2);
        } else {
            return isNullReturnEmpty(value1);
        }
    }

    /*@Override
    public Boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            if (isNullReturnEmpty(value).length() <= 9) {
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }*/

    @Override
    public Boolean isUuidBelongNcCode(String value) {
        if (isNullReturnEmpty(value).indexOf(NCCodeDataSymbol) == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String StringDistinct(String values, String SP) {
        String ReturnValues = "";
        String[] valuesArr = isNullReturnEmpty(values).split(SP);
        for (int i = 0; i < valuesArr.length; i++) {
            if (isNullReturnEmpty(valuesArr[i]).length() > 0) {
                if (i == 0) {
                    ReturnValues = isNullReturnEmpty(valuesArr[i]);
                } else if (ReturnValues.indexOf(SP) == -1) {
                    if (!(ReturnValues.equals(isNullReturnEmpty(valuesArr[i])))) {
                        ReturnValues = ReturnValues + SP + isNullReturnEmpty(valuesArr[i]);
                    }
                } else {
                    if (
                            (ReturnValues.indexOf(SP + isNullReturnEmpty(valuesArr[i]) + SP) == -1)
                                    &&
                                    (ReturnValues.indexOf(isNullReturnEmpty(valuesArr[i]) + SP) != 0)
                                    &&
                                    (
                                            (ReturnValues.indexOf(SP + isNullReturnEmpty(valuesArr[i])) != (ReturnValues.length() - (SP + isNullReturnEmpty(valuesArr[i])).length()))
                                                    ||
                                                    (ReturnValues.indexOf(SP + isNullReturnEmpty(valuesArr[i])) == -1)
                                    )
                            ) {
                        ReturnValues = ReturnValues + SP + isNullReturnEmpty(valuesArr[i]);
                    }
                }
            } else {
                continue;
            }
        }
        return isNullReturnEmpty(ReturnValues);
    }

    @Override
    public String sendNcStringFilter(String value) {
        value = isNullReturnEmpty(value).replace("&", " ");
        return isNullReturnEmpty(value);
    }

    @Override
    public String AccountNoNCSet(String AccountNo) {
        AccountNo = isNullReturnEmpty(AccountNo);
        if (AccountNo.equals("JGG_CASS")) {
            return "JGG_CAss";
        } else if (AccountNo.equals("73")) {
            return "cust_id";
        } else if (AccountNo.equals("93")) {
            return "banktype_id";
        } else if (AccountNo.equals("96")) {
            return "acc_code";
        } else if (AccountNo.equals("DZGG-K")) {
            return "deposit_id";
        } else if (AccountNo.equals("NEIBU")) {
            return "cust_id_i";
        } else if (AccountNo.equals("WAIBU")) {
            return "cust_id_o";
        } else if (AccountNo.equals("2")) {
            return "deptcode";
        } else if (AccountNo.equals("1")) {
            return "psn_code";
        } else if (AccountNo.equals("22")) {
            return "asset_class";
        } else if (AccountNo.equals("DZCY-A")) {
            return "DZCY_A";
        } else if (AccountNo.equals("DZGG-A")) {
            return "DZGG_A";
        } else if (AccountNo.equals("DZGG-B")) {
            return "DZGG_B";
        } else if (AccountNo.equals("DZGG-C")) {
            return "DZGG_C";
        } else if (AccountNo.equals("DZGG-D")) {
            return "DZGG_D";
        } else if (AccountNo.equals("DZGG-G")) {
            return "DZGG_G";
        } else if (AccountNo.equals("DZGG-J")) {
            return "DZGG_J";
        } else if (AccountNo.equals("INACCASS")) {
            return "INACCASS";
        } else if (AccountNo.equals("JGG_BASS")) {
            return "JGG_BASS";
        } else if (AccountNo.equals("JGG_DASS")) {
            return "JGG_DASS";
        } else if (AccountNo.equals("JGG_EASS")) {
            return "JGG_EASS";
        } else if (AccountNo.equals("JGG_FASS")) {
            return "JGG_FASS";
        } else if (AccountNo.equals("JGG_HASS")) {
            return "JGG_HASS";
        } else if (AccountNo.equals("JGG_KASS")) {
            return "JGG_KASS";
        } else if (AccountNo.equals("JXX_JASS")) {
            return "JXX_JASS";
        } else {
            return isNullReturnEmpty(AccountNo);
        }
    }

    @Override
    public String SplitAccountInfoCodeForScdpUser(String accountInfoCode) {
        accountInfoCode = isNullReturnEmpty(accountInfoCode);
        if (accountInfoCode.indexOf("-") != -1) {
            accountInfoCode = accountInfoCode.substring(0, accountInfoCode.indexOf("-"));
        }
        return isNullReturnEmpty(accountInfoCode);
    }

    @Override
    public void setExtraData(Map outMap) {

        //1.设置主表凭证制单人名称显示
        Map fadCertificateMap = (Map) outMap.get("fadCertificateDto");
        if (fadCertificateMap != null) {
            String makerName = "";
            if (isNullReturnEmpty(fadCertificateMap.get(FadCertificateAttribute.MAKER)).length() > 0) {
                String maker = isNullReturnEmpty(fadCertificateMap.get(FadCertificateAttribute.MAKER));
                Map<String, Object> scdpUserConditionsMap = new HashMap<String, Object>();
                scdpUserConditionsMap.put(ScdpUserAttribute.USER_ID, maker);
                List<ScdpUser> scdpUserList = isNullReturnEmpty(maker).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, scdpUserConditionsMap, null) : new ArrayList<>();
                if (scdpUserList.size() > 0) {
                    ScdpUser scdpUser = (ScdpUser) scdpUserList.get(0);
                    makerName = isNullReturnEmpty(scdpUser.getUserName());
                    if (makerName.length() > 0 && (!(isNullReturnEmpty(fadCertificateMap.get(FadCertificateAttribute.MAKER_NAME)).length() > 0))) {
                        fadCertificateMap.put(FadCertificateAttribute.MAKER_NAME, makerName);
                    }
                }
            }
        }

        //2.设置子表凭证摘要各类名称显示
        List<Map> fadCertificateDetailList = new ArrayList<Map>();
        if (fadCertificateMap.get("fadCertificateDetailDto") != null) {
            fadCertificateDetailList = (List) fadCertificateMap.get("fadCertificateDetailDto");
        }
        if (fadCertificateDetailList != null) {
            for (int y = 0; y < fadCertificateDetailList.size(); y++) {
                Map fadCertificateDetailMap = (Map) fadCertificateDetailList.get(y);
                if (fadCertificateDetailMap != null) {

                    //币种名称显示
                    String currtypename = "";
                    if (isNullReturnEmpty(fadCertificateDetailMap.get(FadCertificateDetailAttribute.CURRENCY)).length() > 0) {
                        String currency = isNullReturnEmpty(fadCertificateDetailMap.get(FadCertificateDetailAttribute.CURRENCY));
                        Map<String, Object> fmCurrencyConditionsMap = new HashMap<String, Object>();
                        fmCurrencyConditionsMap.put("currencyCode", currency);
                        List<FmCurrency> fmCurrencyList = isNullReturnEmpty(currency).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FmCurrency.class, fmCurrencyConditionsMap, null) : new ArrayList<>();
                        if (fmCurrencyList.size() > 0) {
                            FmCurrency fmCurrency = (FmCurrency) fmCurrencyList.get(0);
                            currtypename = isNullReturnEmpty(fmCurrency.getCurrencyDesc());
                            if (currtypename.length() > 0 && (!(isNullReturnEmpty(fadCertificateDetailMap.get(FadCertificateDetailAttribute.CURRTYPENAME)).length() > 0))) {
                                fadCertificateDetailMap.put(FadCertificateDetailAttribute.CURRTYPENAME, currtypename);
                            }
                        }
                    }

                    //科目名称显示
                    String subjectName = "";
                    if (isNullReturnEmpty(fadCertificateDetailMap.get(FadCertificateDetailAttribute.SUBJECT_CODE)).length() > 0) {
                        String subjectCode = isNullReturnEmpty(fadCertificateDetailMap.get(FadCertificateDetailAttribute.SUBJECT_CODE));
                        Map<String, Object> fadAccsubjRtfreevalueConditionsMap = new HashMap<String, Object>();
                        fadAccsubjRtfreevalueConditionsMap.put(FadAccsubjRtfreevalueAttribute.SUBJECT_CODE, subjectCode);
                        List<FadAccsubjRtfreevalue> fadAccsubjRtfreevalueList = isNullReturnEmpty(subjectCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadAccsubjRtfreevalue.class, fadAccsubjRtfreevalueConditionsMap, null) : new ArrayList<>();
                        if (fadAccsubjRtfreevalueList.size() > 0) {
                            FadAccsubjRtfreevalue fadAccsubjRtfreevalue = (FadAccsubjRtfreevalue) fadAccsubjRtfreevalueList.get(0);
                            subjectName = isNullReturnEmpty(fadAccsubjRtfreevalue.getSubjectName());
                            if (subjectName.length() > 0 && (!(isNullReturnEmpty(fadCertificateDetailMap.get(FadCertificateDetailAttribute.SUBJECT_NAME)).length() > 0))) {
                                fadCertificateDetailMap.put(FadCertificateDetailAttribute.SUBJECT_NAME, subjectName);
                            }
                        }
                    }

                    //核算内容名称显示
                    List<Map> fadCertificateAccountList = new ArrayList<Map>();
                    if (fadCertificateDetailMap.get("fadCertificateAccountDto") != null) {
                        fadCertificateAccountList = (List) fadCertificateDetailMap.get("fadCertificateAccountDto");
                    }
                    if (fadCertificateAccountList != null) {
                        for (int z = 0; z < fadCertificateAccountList.size(); z++) {
                            Map fadCertificateAccountMap = (Map) fadCertificateAccountList.get(z);
                            if (fadCertificateAccountMap != null) {

                                //核算内容名称显示
                                String accountInfoName = "";
                                if (isNullReturnEmpty(fadCertificateAccountMap.get(FadCertificateAccountAttribute.ACCOUNT_INFO_CODE)).length() > 0) {
                                    String accountInfoCode = isNullReturnEmpty(fadCertificateAccountMap.get(FadCertificateAccountAttribute.ACCOUNT_INFO_CODE));
                                    Map<String, Object> fadRtfreevalueViewConditionsMap = new HashMap<String, Object>();
                                    fadRtfreevalueViewConditionsMap.put(FadRtfreevalueViewAttribute.ACCOUNT_INFO_CODE, accountInfoCode);
                                    List<FadRtfreevalueView> fadRtfreevalueViewList = isNullReturnEmpty(accountInfoCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadRtfreevalueView.class, fadRtfreevalueViewConditionsMap, null) : new ArrayList<>();
                                    if (fadRtfreevalueViewList.size() > 0) {
                                        FadRtfreevalueView fadRtfreevalueView = (FadRtfreevalueView) fadRtfreevalueViewList.get(0);
                                        accountInfoName = isNullReturnEmpty(fadRtfreevalueView.getAccountInfoName());
                                        if (accountInfoName.length() > 0 && (!(isNullReturnEmpty(fadCertificateAccountMap.get(FadCertificateAccountAttribute.ACCOUNT_INFO_NAME)).length() > 0))) {
                                            fadCertificateAccountMap.put(FadCertificateAccountAttribute.ACCOUNT_INFO_NAME, accountInfoName);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public Boolean updateStateToSent(String fadCertificateUuid, String businessId, String originalFormType, String state, String certifiBusinessState, String BusinessState, Map mResult) {
        Boolean result = false;

        Boolean IsDeficitCertificate = false;

        //凭证红冲关系取得
        Map<String, Object> fadCertifiDeficitRelConditionsMap = new HashMap<String, Object>();
        fadCertifiDeficitRelConditionsMap.put(FadCertifiDeficitRelAttribute.DEFICIT_ID, fadCertificateUuid);
        List<FadCertifiDeficitRel> fadCertifiDeficitRelList = isNullReturnEmpty(fadCertificateUuid).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiDeficitRel.class, fadCertifiDeficitRelConditionsMap, "seq_no asc") : new ArrayList<>();

        //凭证合并关系取得
        Map<String, Object> fadCertifiMergeRelConditionsMap = new HashMap<String, Object>();
        fadCertifiMergeRelConditionsMap.put(FadCertifiMergeSplitRelAttribute.MERGE_ID, fadCertificateUuid);
        List<FadCertifiMergeSplitRel> fadCertifiMergeRelList = isNullReturnEmpty(fadCertificateUuid).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiMergeSplitRel.class, fadCertifiMergeRelConditionsMap, "seq_no asc") : new ArrayList<>();

        //凭证拆分关系取得
        Map<String, Object> fadCertifiSplitRelConditionsMap = new HashMap<String, Object>();
        fadCertifiSplitRelConditionsMap.put(FadCertifiMergeSplitRelAttribute.SPLIT_ID, fadCertificateUuid);
        List<FadCertifiMergeSplitRel> fadCertifiSplitRelList = isNullReturnEmpty(fadCertificateUuid).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiMergeSplitRel.class, fadCertifiSplitRelConditionsMap, "seq_no asc") : new ArrayList<>();

        if (fadCertifiDeficitRelList.size() > 0) {
            IsDeficitCertificate = true;
            String certifiId = isNullReturnEmpty(((FadCertifiDeficitRel) fadCertifiDeficitRelList.get(0)).getCertifiId());

            //凭证合并拆分关系取得
            fadCertifiMergeRelConditionsMap = new HashMap<String, Object>();
            fadCertifiMergeRelConditionsMap.put(FadCertifiMergeSplitRelAttribute.MERGE_ID, certifiId);
            fadCertifiMergeRelList = isNullReturnEmpty(certifiId).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiMergeSplitRel.class, fadCertifiMergeRelConditionsMap, "seq_no asc") : new ArrayList<>();

            //凭证合并拆分关系取得
            fadCertifiSplitRelConditionsMap = new HashMap<String, Object>();
            fadCertifiSplitRelConditionsMap.put(FadCertifiMergeSplitRelAttribute.SPLIT_ID, certifiId);
            fadCertifiSplitRelList = isNullReturnEmpty(certifiId).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiMergeSplitRel.class, fadCertifiSplitRelConditionsMap, "seq_no asc") : new ArrayList<>();

            if (fadCertifiMergeRelList.size() > 0) {
                result = updateStateToSentForFadCertifiMerge(fadCertifiMergeRelConditionsMap, fadCertifiMergeRelList, certifiId, result, originalFormType, certifiBusinessState, state, mResult, fadCertificateUuid, IsDeficitCertificate);
            } else if (fadCertifiSplitRelList.size() > 0) {
                result = updateStateToSentForFadCertifiSplit(fadCertifiMergeRelConditionsMap, fadCertifiMergeRelList, fadCertifiSplitRelList, businessId, result, originalFormType, certifiBusinessState, state, mResult, fadCertificateUuid, IsDeficitCertificate);
            } else {
                result = updateStateToSentForFadCertificate(businessId, result, originalFormType, certifiBusinessState, state, true, fadCertificateUuid, IsDeficitCertificate);
            }
        } else if (fadCertifiMergeRelList.size() > 0) {
            result = updateStateToSentForFadCertifiMerge(fadCertifiMergeRelConditionsMap, fadCertifiMergeRelList, fadCertificateUuid, result, originalFormType, BusinessState, state, mResult, fadCertificateUuid, IsDeficitCertificate);
        } else if (fadCertifiSplitRelList.size() > 0) {
            result = updateStateToSentForFadCertifiSplit(fadCertifiMergeRelConditionsMap, fadCertifiMergeRelList, fadCertifiSplitRelList, businessId, result, originalFormType, BusinessState, state, mResult, fadCertificateUuid, IsDeficitCertificate);
        } else {
            result = updateStateToSentForFadCertificate(businessId, result, originalFormType, BusinessState, state, true, fadCertificateUuid, IsDeficitCertificate);
        }

        return result;
    }

    public Boolean updateStateToSentForFadCertifiMerge(Map<String, Object> fadCertifiMergeRelConditionsMap, List<FadCertifiMergeSplitRel> fadCertifiMergeRelList, String certifiId, Boolean result, String originalFormType, String BusinessState, String state, Map mResult, String fadCertificateUuid, Boolean IsDeficitCertificate) {
        List fadCertificateLst1 = new ArrayList<>();
        for (int i = 0; i < fadCertifiMergeRelList.size(); i++) {
            FadCertificateDto fadCertificate = isNullReturnEmpty(((FadCertifiMergeSplitRel) fadCertifiMergeRelList.get(i)).getSplitId()).length() > 0 ? (FadCertificateDto) DtoHelper.findDtoByPK(FadCertificateDto.class, isNullReturnEmpty(((FadCertifiMergeSplitRel) fadCertifiMergeRelList.get(i)).getSplitId())) : null;
            if (fadCertificate != null) {
                result = updateStateToSentForFadCertificate(fadCertificate.getBusinessId(), result, fadCertificate.getOriginalFormType(), BusinessState, state, true, fadCertificate.getUuid(), IsDeficitCertificate);
                fadCertificate.setState(state);
                fadCertificate.setCertificateNo(isNullReturnEmpty(mResult.get("certificateNo")));
                fadCertificate.setMaker(isNullReturnEmpty(mResult.get("maker")));
                fadCertificate.setMakeDate((Date) mResult.get("makeDate"));
                fadCertificateLst1.add(fadCertificate);

                //凭证合并拆分关系取得
                String mergeId = isNullReturnEmpty(((FadCertifiMergeSplitRel) fadCertifiMergeRelList.get(i)).getMergeId());
                fadCertifiMergeRelConditionsMap = new HashMap<String, Object>();
                fadCertifiMergeRelConditionsMap.put(FadCertifiMergeSplitRelAttribute.MERGE_ID, isNullReturnEmpty(((FadCertifiMergeSplitRel) fadCertifiMergeRelList.get(i)).getSplitId()));
                fadCertifiMergeRelList = isNullReturnEmpty(((FadCertifiMergeSplitRel) fadCertifiMergeRelList.get(i)).getSplitId()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiMergeSplitRel.class, fadCertifiMergeRelConditionsMap, "seq_no asc") : new ArrayList<>();
                if (fadCertifiMergeRelList.size() > 0) {
                    i = -1;
                    continue;
                } else {
                    fadCertifiMergeRelConditionsMap = new HashMap<String, Object>();
                    fadCertifiMergeRelConditionsMap.put(FadCertifiMergeSplitRelAttribute.MERGE_ID, mergeId);
                    fadCertifiMergeRelList = isNullReturnEmpty(mergeId).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiMergeSplitRel.class, fadCertifiMergeRelConditionsMap, "seq_no asc") : new ArrayList<>();
                    if ((i == (fadCertifiMergeRelList.size() - 1)) && (!(mergeId.equals(certifiId)))) {
                        fadCertifiMergeRelConditionsMap = new HashMap<String, Object>();
                        fadCertifiMergeRelConditionsMap.put(FadCertifiMergeSplitRelAttribute.SPLIT_ID, mergeId);
                        fadCertifiMergeRelList = isNullReturnEmpty(mergeId).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiMergeSplitRel.class, fadCertifiMergeRelConditionsMap, "seq_no asc") : new ArrayList<>();

                        fadCertifiMergeRelConditionsMap = new HashMap<String, Object>();
                        fadCertifiMergeRelConditionsMap.put(FadCertifiMergeSplitRelAttribute.MERGE_ID, isNullReturnEmpty(((FadCertifiMergeSplitRel) fadCertifiMergeRelList.get(0)).getMergeId()));
                        fadCertifiMergeRelList = isNullReturnEmpty(((FadCertifiMergeSplitRel) fadCertifiMergeRelList.get(0)).getMergeId()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiMergeSplitRel.class, fadCertifiMergeRelConditionsMap, "seq_no asc") : new ArrayList<>();
                        for (int y = 0; y < fadCertifiMergeRelList.size(); y++) {
                            if (((FadCertifiMergeSplitRel) fadCertifiMergeRelList.get(y)).getSplitId().equals(mergeId)) {
                                i = y;
                                break;
                            }
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
        DtoHelper.batchUpdate(fadCertificateLst1, FadCertificate.class);
        return result;
    }

    public Boolean updateStateToSentForFadCertifiSplit(Map<String, Object> fadCertifiMergeRelConditionsMap, List<FadCertifiMergeSplitRel> fadCertifiMergeRelList, List<FadCertifiMergeSplitRel> fadCertifiSplitRelList, String businessId, Boolean result, String originalFormType, String BusinessState, String state, Map mResult, String fadCertificateUuid, Boolean IsDeficitCertificate) {
        fadCertifiMergeRelConditionsMap = new HashMap<String, Object>();
        fadCertifiMergeRelConditionsMap.put(FadCertifiMergeSplitRelAttribute.MERGE_ID, isNullReturnEmpty(((FadCertifiMergeSplitRel) fadCertifiSplitRelList.get(0)).getMergeId()));
        fadCertifiMergeRelList = isNullReturnEmpty(((FadCertifiMergeSplitRel) fadCertifiSplitRelList.get(0)).getMergeId()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiMergeSplitRel.class, fadCertifiMergeRelConditionsMap, "seq_no asc") : new ArrayList<>();
        List fadCertificateLst1 = new ArrayList<>();
        String SplitState = state;
        result = true;
        for (int i = 0; i < fadCertifiMergeRelList.size(); i++) {
            FadCertificate fadCertificate = isNullReturnEmpty(((FadCertifiMergeSplitRel) fadCertifiMergeRelList.get(i)).getSplitId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadCertificate.class, isNullReturnEmpty(((FadCertifiMergeSplitRel) fadCertifiMergeRelList.get(i)).getSplitId())) : null;
            if (fadCertificate != null) {
                //result = updateStateToSentForFadCertificate(fadCertificate.getBusinessId(), result, fadCertificate.getOriginalFormType(), BusinessState, state, false, fadCertificate.getUuid(), IsDeficitCertificate);
                if (SplitState.equals(state) || i == 0) {
                    SplitState = isNullReturnEmpty(fadCertificate.getState());
                }
                if (i == (fadCertifiMergeRelList.size() - 1)) {
                    if (SplitState.equals(state)) {
                        fadCertificate = isNullReturnEmpty(((FadCertifiMergeSplitRel) fadCertifiMergeRelList.get(i)).getMergeId()).length() > 0 ? (FadCertificateDto) DtoHelper.findDtoByPK(FadCertificateDto.class, isNullReturnEmpty(((FadCertifiMergeSplitRel) fadCertifiMergeRelList.get(i)).getMergeId())) : null;
                        fadCertificate.setState(state);
                        fadCertificate.setMaker(isNullReturnEmpty(mResult.get("maker")));
                        fadCertificate.setMakeDate((Date) mResult.get("makeDate"));
                        fadCertificateLst1.add(fadCertificate);
                    } else {
                        result = false;
                    }
                    fadCertifiMergeRelConditionsMap = new HashMap<String, Object>();
                    fadCertifiMergeRelConditionsMap.put(FadCertifiMergeSplitRelAttribute.SPLIT_ID, isNullReturnEmpty(((FadCertifiMergeSplitRel) fadCertifiMergeRelList.get(i)).getMergeId()));
                    fadCertifiMergeRelList = isNullReturnEmpty(((FadCertifiMergeSplitRel) fadCertifiMergeRelList.get(i)).getMergeId()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiMergeSplitRel.class, fadCertifiMergeRelConditionsMap, "seq_no asc") : new ArrayList<>();

                    if (fadCertifiMergeRelList.size() > 0) {
                        fadCertifiMergeRelConditionsMap = new HashMap<String, Object>();
                        fadCertifiMergeRelConditionsMap.put(FadCertifiMergeSplitRelAttribute.MERGE_ID, isNullReturnEmpty(((FadCertifiMergeSplitRel) fadCertifiMergeRelList.get(0)).getMergeId()));
                        fadCertifiMergeRelList = isNullReturnEmpty(((FadCertifiMergeSplitRel) fadCertifiMergeRelList.get(0)).getMergeId()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiMergeSplitRel.class, fadCertifiMergeRelConditionsMap, "seq_no asc") : new ArrayList<>();
                        i = -1;
                        continue;
                    }
                }
            }
        }
        DtoHelper.batchUpdate(fadCertificateLst1, FadCertificate.class);
        if (result) {
            result = updateStateToSentForFadCertificate(businessId, result, originalFormType, BusinessState, state, true, fadCertificateUuid, IsDeficitCertificate);
        }
        return result;
    }

    public Boolean updateStateToSentForFadCertificate(String businessId, Boolean result, String originalFormType, String BusinessState, String state, Boolean isUpdate, String fadCertificateUuid, Boolean IsDeficitCertificate) {
        if (
                originalFormType.equals("1")
                        || originalFormType.equals("2")
                        || originalFormType.equals("3")) {

            //1.FAD_CASH_REQ(请款)
            FadCashReq fadCashReq = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadCashReq.class, businessId) : null;
            if (fadCashReq != null) {
                if (isUpdate) {
                    fadCashReq.setState(BusinessState);
                    PersistenceFactory.getInstance().update(fadCashReq);
                }
                result = true;
            } else {
                result = false;
                //throw new BizException("【当前凭证】已发送,但未能更新【原始单据】状态到【已发送】!");
            }
        } else if (
                originalFormType.equals("4")
                        || originalFormType.equals("5")
                        || originalFormType.equals("5.1")
                        || originalFormType.equals("6")
                        || originalFormType.equals("7")
                        || originalFormType.equals("8")
                        || originalFormType.equals("9")
                ) {

            //0.FAD_INVOICE(发票)
            FadInvoice fadInvoice = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadInvoice.class, businessId) : null;
            if (fadInvoice != null) {
                if (isUpdate) {
                    if (
                            (
                                    originalFormType.equals("5")
                                            || originalFormType.equals("5.1")
                                            || originalFormType.equals("6")
                                            || originalFormType.equals("7")
                                            || originalFormType.equals("8")
                                            || originalFormType.equals("9")
                            )
                                    &&
                                    (!IsDeficitCertificate)
                            ) {
                        //主表凭证取得
                        FadCertificateDto fadCertificate = isNullReturnEmpty(fadCertificateUuid).length() > 0 ? (FadCertificateDto) DtoHelper.findDtoByPK(FadCertificateDto.class, fadCertificateUuid) : null;
                        if (fadCertificate != null) {
                            String[] originalFormCodes = isNullReturnEmpty(fadCertificate.getOriginalFormCode()).split(MergeSplitSymbol);
                            for (int ii = 0; ii < originalFormCodes.length; ii++) {
                                String originalFormCode = originalFormCodes[ii];
                                if (originalFormCode.indexOf("(发票黏贴单号)") != -1) {
                                    String sql = "SELECT 1 FROM FAD_CERTIFICATE WHERE ORIGINAL_FORM_CODE LIKE '%" + originalFormCode + "%' AND ORIGINAL_FORM_TYPE IN ('5','5.1','6','7','8','9') AND STATE <> '" + state + "' AND STATE <> '2' AND NVL(IS_VOID, 0) <> 1";
                                    DAOMeta daoMeta = new DAOMeta();
                                    daoMeta.setStrSql(sql);
                                    daoMeta.setNeedFilter(false);
                                    if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                                        isUpdate = false;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (isUpdate) {
                        fadInvoice.setState(BusinessState);
                        PersistenceFactory.getInstance().update(fadInvoice);
                    }
                }
                result = true;
            } else {
                result = false;
                //throw new BizException("【当前凭证】已发送,但未能更新【原始单据】状态到【已发送】!");
            }
        } else if (originalFormType.equals("10")) {

            //2.FAD_PAY_REQ_C(支付)
            FadPayReqC fadPayReqC = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadPayReqC.class, businessId) : null;
            if (fadPayReqC != null) {
                if (isUpdate) {
                    fadPayReqC.setState(BusinessState);
                    PersistenceFactory.getInstance().update(fadPayReqC);
                }
                result = true;
            } else {
                result = false;
                //throw new BizException("【当前凭证】已发送,但未能更新【原始单据】状态到【已发送】!");
            }
        } else if (originalFormType.equals("11")
                ) {

            //3.FAD_CASH_CLEARANCE(还款)
            FadCashClearance fadCashClearance = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadCashClearance.class, businessId) : null;
            if (fadCashClearance != null) {
                if (isUpdate) {
                    fadCashClearance.setState(BusinessState);
                    PersistenceFactory.getInstance().update(fadCashClearance);
                }
                result = true;
            } else {
                result = false;
                //throw new BizException("【当前凭证】已发送,但未能更新【原始单据】状态到【已发送】!");
            }
        } else if (originalFormType.equals("12")
                ) {

            //4.PRM_RECEIPTS(收款)
            PrmReceipts prmReceipts = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmReceipts.class, businessId) : null;
            if (prmReceipts != null) {
                if (isUpdate) {
                    prmReceipts.setState(BusinessState);
                    PersistenceFactory.getInstance().update(prmReceipts);
                }
                result = true;
            } else {
                result = false;
                //throw new BizException("【当前凭证】已发送,但未能更新【原始单据】状态到【已发送】!");
            }
        } else if (originalFormType.equals("13")
                ) {

            //5.PRM_BILLING(开票)
            PrmBilling prmBilling = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmBilling.class, businessId) : null;
            if (prmBilling != null) {
                if (isUpdate) {
                    prmBilling.setState(BusinessState);
                    PersistenceFactory.getInstance().update(prmBilling);
                }
                result = true;
            } else {
                result = false;
                //throw new BizException("【当前凭证】已发送,但未能更新【原始单据】状态到【已发送】!");
            }
        } else {
            result = false;
            //throw new BizException("【当前凭证】已发送,但未能更新【原始单据】状态到【已发送】!");
        }
        return result;
    }

    @Override
    public String javaBeanToCertificate(String businessId, String originalFormTypeForeign) {
        if (
                (isNullReturnEmpty(businessId).length() == 0)
                        || (isNullReturnEmpty(originalFormTypeForeign).length() == 0)
                ) {
            throw new BizException("【原始单据UUID】生成凭证失败,请检查您输入的【原始单据UUID】、【原始单据类型】是否正确!");
        }

        String originalFormType = getOriginalFormType(businessId, originalFormTypeForeign);

        if (isNullReturnEmpty(originalFormType).length() == 0) {
            throw new BizException("【原始单据UUID】生成凭证失败,请检查您输入的【原始单据UUID】、【原始单据类型】是否正确!");
        }

        ifCertificateAlreadyExistThrowException(businessId);

        //1.个人请款(1日常2差旅)
        if (originalFormType.equals("1")) {
            createCertificateFromBusinessPersonalReq(businessId, originalFormType);
        }

        //2.材料费或外包商请款(0采购合同)
        else if (originalFormType.equals("2")) {
            createCertificateFromBusinessPurchaseContractReq(businessId, originalFormType);
        }

        //3.保证金请款(3保证金)
        else if (originalFormType.equals("3")) {
            createCertificateFromBusinessBondReq(businessId, originalFormType);
        }

        //4.无请款报销
        else if (originalFormType.equals("4")) {
            createCertificateFromBusinessNoPaymentReimbursementInvoice(businessId, originalFormType);
        } else if (originalFormType.equals("4.1")) {

            //6.无请款采购合同
            createCertificateFromBusinessNoPaymentPurchaseContractInvoiceMaterial(businessId, "6");

            //8.采购合同成本确认
            createCertificateFromBusinessPurchaseContractCostConfirmationInvoiceMaterial(businessId, "8");

            //9.采购合同支付直接支付
            createCertificateFromBusinessPurchaseContractPaymentLessThan20000InvoiceMaterial(businessId, "9");
        }

        //5.有请款报销
        else if (originalFormType.equals("5")) {
            createCertificateFromBusinessPaymentReimbursementInvoice(businessId, originalFormType);
        } else if (originalFormType.equals("5.1")) {

            //7.有请款采购合同应付
            createCertificateFromBusinessPaymentPurchaseContractPayableInvoiceMaterial(businessId, "7");

            //8.采购合同成本确认
            createCertificateFromBusinessPurchaseContractCostConfirmationInvoiceMaterial(businessId, "8");

            //9.采购合同支付直接支付
            createCertificateFromBusinessPurchaseContractPaymentLessThan20000InvoiceMaterial(businessId, "9");
        } else if (originalFormType.equals("5.2")) {

            //7.1.有请款采购合同预付
            createCertificateFromBusinessPaymentPurchaseContractPaidInvoiceMaterial(businessId, "7");

            //8.采购合同成本确认
            createCertificateFromBusinessPurchaseContractCostConfirmationInvoiceMaterial(businessId, "8");

            //9.采购合同支付直接支付
            //createCertificateFromBusinessPurchaseContractPaymentLessThan20000InvoiceMaterial(businessId, "9");
        }

        //6.无请款采购合同
        else if (originalFormType.equals("6")) {
            if (isContractWaixie(businessId)) {
                createCertificateFromBusinessNoPaymentPurchaseContractInvoiceWaixie(businessId, originalFormType);
            } else {
                createCertificateFromBusinessNoPaymentPurchaseContractInvoice(businessId, originalFormType);

                //8.采购合同成本确认
                createCertificateFromBusinessPurchaseContractCostConfirmationInvoice(businessId, "8");
            }

            //9.采购合同支付直接支付
            createCertificateFromBusinessPurchaseContractPaymentLessThan20000Invoice(businessId, "9");
        }

        //7.有请款采购合同应付
        else if (originalFormType.equals("7")) {
            if (isContractWaixie(businessId)) {
                createCertificateFromBusinessPaymentPurchaseContractPayableInvoiceWaixie(businessId, originalFormType);
            } else {
                createCertificateFromBusinessPaymentPurchaseContractPayableInvoice(businessId, originalFormType);

                //8.采购合同成本确认
                createCertificateFromBusinessPurchaseContractCostConfirmationInvoice(businessId, "8");
            }

            //9.采购合同支付直接支付
            createCertificateFromBusinessPurchaseContractPaymentLessThan20000Invoice(businessId, "9");
        }

        //7.1.有请款采购合同预付
        else if (originalFormType.equals("7.1")) {
            if (isContractWaixie(businessId)) {
                createCertificateFromBusinessPaymentPurchaseContractPaidInvoiceWaixie(businessId, "7");
            } else {
                createCertificateFromBusinessPaymentPurchaseContractPaidInvoice(businessId, "7");

                //8.采购合同成本确认
                createCertificateFromBusinessPurchaseContractCostConfirmationInvoice(businessId, "8");
            }
        }

        //10.采购合同支付
        else if (originalFormType.equals("10")) {
            createCertificateFromBusinessPurchaseContractPayment(businessId, originalFormType);
        }

        //11.还款
        else if (originalFormType.equals("11")) {
            createCertificateFromBusinessPayBackTheMoney(businessId, originalFormType);
        }

        //12.收款
        else if (originalFormType.equals("12")) {
            createCertificateFromBusinessReceivables(businessId, originalFormType);
        } else if (originalFormType.equals("12.1")) {
            createCertificateFromBusinessReceivablesPendingConfirmation(businessId, originalFormType);
        } else if (originalFormType.equals("12.2")) {
            createCertificateFromBusinessReceivablesAdjustment(businessId, originalFormType);
        }

        //13.开票
        else if (originalFormType.equals("13")) {
            createCertificateFromBusinessBill(businessId, originalFormType);
        }

        //14.周转金
        else if (originalFormType.equals("14")) {
            createCertificateFromRevolvingReq(businessId, originalFormType);
        }

        //按凭证设定规则设定凭证开始
        Map<String, Object> fadCertificateConditionsMap = new HashMap<String, Object>();
        fadCertificateConditionsMap.put(FadCertificateAttribute.BUSINESS_ID, businessId);
        fadCertificateConditionsMap.put(FadCertificateAttribute.STATE, "0");
        List fadCertificateList = isNullReturnEmpty(businessId).length() > 0 ? DtoHelper.findByAnyFields(FadCertificateDto.class, fadCertificateConditionsMap, "seq_no asc") : new ArrayList<>();

        //UpdateBy系统信息生成
        certificateSetSystemUpdataField(businessId, fadCertificateList);
        fadCertificateList = isNullReturnEmpty(businessId).length() > 0 ? DtoHelper.findByAnyFields(FadCertificateDto.class, fadCertificateConditionsMap, "seq_no asc") : new ArrayList<>();

        certificateDetailSetDetailForBusinessId(businessId, fadCertificateList);
        certificateMergeOriginalFormCodeAbstractsFromCertificateDetail(businessId, fadCertificateList);

        nonProjectSetRuleToUpdateCertificateSetDetailSubject(businessId, originalFormTypeForeign, originalFormType, fadCertificateList);
        projectSetRuleToUpdateCertificateSetDetailSubject(businessId, originalFormTypeForeign, originalFormType, fadCertificateList);
        projectRunSetRuleToUpdateCertificateSetDetailSubject(businessId, originalFormTypeForeign, originalFormType, fadCertificateList);

        certificateDetailSetAccountForBusinessId(businessId, fadCertificateList);
        fadCertificateList = isNullReturnEmpty(businessId).length() > 0 ? DtoHelper.findByAnyFields(FadCertificateDto.class, fadCertificateConditionsMap, "seq_no asc") : new ArrayList<>();

        nonProjectSetRuleToUpdateCertificateSetAccountRtfree(businessId, originalFormTypeForeign, originalFormType, fadCertificateList);
        projectSetRuleToUpdateCertificateSetAccountRtfree(businessId, originalFormTypeForeign, originalFormType, fadCertificateList);
        projectRunSetRuleToUpdateCertificateSetAccountRtfree(businessId, originalFormTypeForeign, originalFormType, fadCertificateList);
        //按凭证设定规则设定凭证结束

        certificateSetSystemUpdataField(businessId, fadCertificateList);
        ifCertificateLocalZeroThrowException(businessId);

        /*if (true) {
            throw new BizException("测试弹出");
        }*/

        return isNullReturnEmpty(((FadCertificateDto) (fadCertificateList.get(0))).getUuid());
    }

    public String getFadCertificateUuidByBusinessId(String businessId) {

        //主表凭证取得
        List<QueryCondition> lstQueryCondition = new ArrayList<>();

        QueryCondition queryCondition1 = new QueryCondition();
        queryCondition1.setField(FadCertificateAttribute.BUSINESS_ID);
        queryCondition1.setOperator("=");
        queryCondition1.addValue(businessId);
        lstQueryCondition.add(queryCondition1);

        QueryCondition queryCondition2 = new QueryCondition();
        queryCondition2.setField(FadCertificateAttribute.STATE);
        queryCondition2.setOperator("IN");
        List valueConditionList = new ArrayList<>();
        valueConditionList.add("0");
        valueConditionList.add("1");
        queryCondition2.setValueList(valueConditionList);
        lstQueryCondition.add(queryCondition2);

        List fadCertificateList = isNullReturnEmpty(businessId).length() > 0 ? DtoHelper.findByAnyFields(FadCertificateDto.class, lstQueryCondition, "seq_no asc") : new ArrayList<>();
        if (fadCertificateList.size() > 0) {
            FadCertificateDto fadCertificate = (FadCertificateDto) fadCertificateList.get(0);
            return isNullReturnEmpty(fadCertificate.getUuid());
        } else {
            return "";
        }
    }

    @Override
    public String getOriginalFormType(String businessId, String originalFormTypeForeign) {
        String originalFormType = "";
        String sql = "";
        DAOMeta daoMeta = null;

        if (originalFormTypeForeign.equals("0")) {
            //4.无请款报销
            sql = "SELECT 1\n" +
                    "      FROM FAD_INVOICE\n" +
                    "     WHERE UUID = '" + businessId + "'\n" +
                    "       AND NOT EXISTS\n" +
                    "     (SELECT 1\n" +
                    "              FROM FAD_CASH_REQ_INVOICE\n" +
                    "             WHERE FAD_CASH_REQ_INVOICE.FAD_INVOICE_ID = FAD_INVOICE.UUID)\n" +
                    "       AND EXPENSES_TYPE <> '0'";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                sql = "SELECT 1 FROM FAD_INVOICE_MATERIAL WHERE FAD_INVOICE_ID = '" + businessId + "'";
                daoMeta = new DAOMeta();
                daoMeta.setStrSql(sql);
                daoMeta.setNeedFilter(false);
                if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                    originalFormType = "4.1";
                } else {
                    originalFormType = "4";
                }
                return originalFormType;
            }

            //5.有请款报销
            sql = "SELECT 1\n" +
                    "      FROM FAD_INVOICE\n" +
                    "     WHERE UUID = '" + businessId + "'\n" +
                    "       AND EXISTS\n" +
                    "     (SELECT 1\n" +
                    "              FROM FAD_CASH_REQ_INVOICE\n" +
                    "             WHERE FAD_CASH_REQ_INVOICE.FAD_INVOICE_ID = FAD_INVOICE.UUID)\n" +
                    "       AND EXPENSES_TYPE <> '0'";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                sql = "SELECT 1 FROM FAD_INVOICE_MATERIAL WHERE FAD_INVOICE_ID = '" + businessId + "'";
                daoMeta = new DAOMeta();
                daoMeta.setStrSql(sql);
                daoMeta.setNeedFilter(false);
                if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                    sql = "SELECT 1\n" +
                            "      FROM (SELECT INVOICE_TYPE,\n" +
                            "                   INVOICE_REQ_NO,\n" +
                            "                   CREATE_TIME,\n" +
                            "                   TAX_RATE,\n" +
                            "                   DEBTOR,\n" +
                            "                   CREDITOR,\n" +
                            "                   SUM(CLEARANCE_MONEY) AS SUM_CLEARANCE_MONEY\n" +
                            "              FROM (SELECT FAD_INVOICE.INVOICE_TYPE,\n" +
                            "                           FAD_INVOICE.INVOICE_REQ_NO,\n" +
                            "                           FAD_INVOICE.CREATE_TIME,\n" +
                            "                           NVL(FAD_INVOICE.TAX_RATE, 0) AS TAX_RATE,\n" +
                            "                           NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS DEBTOR,\n" +
                            "                           NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS CREDITOR,\n" +
                            "                           NVL(FAD_CASH_REQ_INVOICE.CLEARANCE_MONEY, 0) AS CLEARANCE_MONEY\n" +
                            "                      FROM FAD_INVOICE\n" +
                            "                      LEFT JOIN FAD_CASH_REQ_INVOICE ON FAD_INVOICE.UUID =\n" +
                            "                                                        FAD_CASH_REQ_INVOICE.FAD_INVOICE_ID\n" +
                            "                     WHERE FAD_INVOICE.UUID = '" + businessId + "'\n" +
                            "                       AND EXISTS (SELECT 1\n" +
                            "                              FROM FAD_CASH_REQ_INVOICE\n" +
                            "                             WHERE FAD_CASH_REQ_INVOICE.FAD_INVOICE_ID =\n" +
                            "                                   FAD_INVOICE.UUID)\n" +
                            "                       AND EXPENSES_TYPE <> '0') A\n" +
                            "             GROUP BY INVOICE_TYPE,\n" +
                            "                      INVOICE_REQ_NO,\n" +
                            "                      CREATE_TIME,\n" +
                            "                      TAX_RATE,\n" +
                            "                      DEBTOR,\n" +
                            "                      CREDITOR) A\n" +
                            "     WHERE (SUM_CLEARANCE_MONEY > 0)\n" +
                            "       AND (DEBTOR - SUM_CLEARANCE_MONEY) > 0";
                    daoMeta = new DAOMeta();
                    daoMeta.setStrSql(sql);
                    daoMeta.setNeedFilter(false);
                    if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                        originalFormType = "5.1";
                    } else {
                        originalFormType = "5.2";
                    }
                } else {
                    originalFormType = "5";
                }
                return originalFormType;
            }

            //6.无请款采购合同
            sql = "SELECT 1\n" +
                    "      FROM FAD_INVOICE\n" +
                    "     WHERE UUID = '" + businessId + "'\n" +
                    "       AND NOT EXISTS\n" +
                    "     (SELECT 1\n" +
                    "              FROM FAD_CASH_REQ_INVOICE\n" +
                    "             WHERE FAD_CASH_REQ_INVOICE.FAD_INVOICE_ID = FAD_INVOICE.UUID)\n" +
                    "       AND EXPENSES_TYPE = '0'";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                originalFormType = "6";
                return originalFormType;
            }

            //7.有请款采购合同应付
            sql = "SELECT 1\n" +
                    "      FROM (SELECT INVOICE_TYPE,\n" +
                    "                   INVOICE_REQ_NO,\n" +
                    "                   CREATE_TIME,\n" +
                    "                   TAX_RATE,\n" +
                    "                   DEBTOR,\n" +
                    "                   CREDITOR,\n" +
                    "                   SUM(CLEARANCE_MONEY) AS SUM_CLEARANCE_MONEY\n" +
                    "              FROM (SELECT FAD_INVOICE.INVOICE_TYPE,\n" +
                    "                           FAD_INVOICE.INVOICE_REQ_NO,\n" +
                    "                           FAD_INVOICE.CREATE_TIME,\n" +
                    "                           NVL(FAD_INVOICE.TAX_RATE, 0) AS TAX_RATE,\n" +
                    "                           NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS DEBTOR,\n" +
                    "                           NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS CREDITOR,\n" +
                    "                           NVL(FAD_CASH_REQ_INVOICE.CLEARANCE_MONEY, 0) AS CLEARANCE_MONEY\n" +
                    "                      FROM FAD_INVOICE\n" +
                    "                      LEFT JOIN FAD_CASH_REQ_INVOICE ON FAD_INVOICE.UUID =\n" +
                    "                                                        FAD_CASH_REQ_INVOICE.FAD_INVOICE_ID\n" +
                    "                     WHERE FAD_INVOICE.UUID = '" + businessId + "'\n" +
                    "                       AND EXISTS (SELECT 1\n" +
                    "                              FROM FAD_CASH_REQ_INVOICE\n" +
                    "                             WHERE FAD_CASH_REQ_INVOICE.FAD_INVOICE_ID =\n" +
                    "                                   FAD_INVOICE.UUID)\n" +
                    "                       AND EXPENSES_TYPE = '0') A\n" +
                    "             GROUP BY INVOICE_TYPE,\n" +
                    "                      INVOICE_REQ_NO,\n" +
                    "                      CREATE_TIME,\n" +
                    "                      TAX_RATE,\n" +
                    "                      DEBTOR,\n" +
                    "                      CREDITOR) A\n" +
                    "     WHERE (SUM_CLEARANCE_MONEY > 0)\n" +
                    "       AND (DEBTOR - SUM_CLEARANCE_MONEY) > 0";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                originalFormType = "7";
                return originalFormType;
            }

            //7.1.有请款采购合同预付
            sql = "SELECT 1\n" +
                    "      FROM (SELECT INVOICE_TYPE,\n" +
                    "                   INVOICE_REQ_NO,\n" +
                    "                   CREATE_TIME,\n" +
                    "                   TAX_RATE,\n" +
                    "                   DEBTOR,\n" +
                    "                   CREDITOR,\n" +
                    "                   SUM(CLEARANCE_MONEY) AS SUM_CLEARANCE_MONEY\n" +
                    "              FROM (SELECT FAD_INVOICE.INVOICE_TYPE,\n" +
                    "                           FAD_INVOICE.INVOICE_REQ_NO,\n" +
                    "                           FAD_INVOICE.CREATE_TIME,\n" +
                    "                           NVL(FAD_INVOICE.TAX_RATE, 0) AS TAX_RATE,\n" +
                    "                           NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS DEBTOR,\n" +
                    "                           NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS CREDITOR,\n" +
                    "                           NVL(FAD_CASH_REQ_INVOICE.CLEARANCE_MONEY, 0) AS CLEARANCE_MONEY\n" +
                    "                      FROM FAD_INVOICE\n" +
                    "                      LEFT JOIN FAD_CASH_REQ_INVOICE ON FAD_INVOICE.UUID =\n" +
                    "                                                        FAD_CASH_REQ_INVOICE.FAD_INVOICE_ID\n" +
                    "                     WHERE FAD_INVOICE.UUID = '" + businessId + "'\n" +
                    "                       AND EXISTS (SELECT 1\n" +
                    "                              FROM FAD_CASH_REQ_INVOICE\n" +
                    "                             WHERE FAD_CASH_REQ_INVOICE.FAD_INVOICE_ID =\n" +
                    "                                   FAD_INVOICE.UUID)\n" +
                    "                       AND EXPENSES_TYPE = '0') A\n" +
                    "             GROUP BY INVOICE_TYPE,\n" +
                    "                      INVOICE_REQ_NO,\n" +
                    "                      CREATE_TIME,\n" +
                    "                      TAX_RATE,\n" +
                    "                      DEBTOR,\n" +
                    "                      CREDITOR) A\n" +
                    "     WHERE SUM_CLEARANCE_MONEY > 0";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                originalFormType = "7.1";
                return originalFormType;
            }
        } else if (originalFormTypeForeign.equals("1")) {
            //1.个人请款(1日常2差旅)
            sql = "SELECT 1\n" +
                    "      FROM FAD_CASH_REQ\n" +
                    "     WHERE UUID = '" + businessId + "'\n" +
                    "       AND (REQ_TYPE = '1' OR REQ_TYPE = '2')";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                originalFormType = "1";
                return originalFormType;
            }

            //2.材料费或外包商请款(0采购合同)
            sql = "SELECT 1\n" +
                    "      FROM FAD_CASH_REQ\n" +
                    "     WHERE UUID = '" + businessId + "'\n" +
                    "       AND REQ_TYPE = '0'";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                originalFormType = "2";
                return originalFormType;
            }

            //3.保证金请款(3保证金)
            sql = "SELECT 1\n" +
                    "      FROM FAD_CASH_REQ\n" +
                    "     WHERE UUID = '" + businessId + "'\n" +
                    "       AND REQ_TYPE = '3'";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                originalFormType = "3";
                return originalFormType;
            }
            //14.周转金请款(14.周转金)
            sql = "SELECT 1\n" +
                    "      FROM FAD_CASH_REQ\n" +
                    "     WHERE UUID = '" + businessId + "'\n" +
                    "       AND REQ_TYPE = '4'";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                originalFormType = "14";
                return originalFormType;
            }
        } else if (originalFormTypeForeign.equals("2")) {
            //10.采购合同支付
            sql = "SELECT 1\n" +
                    "      FROM FAD_PAY_REQ_C\n" +
                    "     WHERE UUID = '" + businessId + "'";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                originalFormType = "10";
                return originalFormType;
            }
        } else if (originalFormTypeForeign.equals("3")) {
            //11.还款
            sql = "SELECT 1\n" +
                    "      FROM FAD_CASH_CLEARANCE\n" +
                    "     WHERE UUID = '" + businessId + "'";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                originalFormType = "11";
                return originalFormType;
            }
        } else if (originalFormTypeForeign.equals("4")) {
            //12.收款
            sql = "SELECT 1\n" +
                    "      FROM PRM_RECEIPTS\n" +
                    "     WHERE UUID = '" + businessId + "' AND PRM_UNKNOWN_RECEIPTS_ID IS NULL";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                originalFormType = "12";
                return originalFormType;
            }

            sql = "SELECT 1\n" +
                    "      FROM PRM_UNKNOWN_RECEIPTS\n" +
                    "     WHERE UUID = '" + businessId + "'";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                originalFormType = "12.1";
                return originalFormType;
            }

            sql = "SELECT 1\n" +
                    "      FROM PRM_RECEIPTS\n" +
                    "     WHERE UUID = '" + businessId + "'";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                originalFormType = "12.2";
                return originalFormType;
            }
        } else if (originalFormTypeForeign.equals("5")) {
            //13.开票
            sql = "SELECT 1\n" +
                    "      FROM PRM_BILLING\n" +
                    "     WHERE UUID = '" + businessId + "'";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                originalFormType = "13";
                return originalFormType;
            }
        }

        return originalFormType;
    }

    @Override
    public String getOriginalFormTypeForeign(String originalFormType) {
        if (
                originalFormType.equals("4")
                        ||
                        originalFormType.equals("5")
                        ||
                        originalFormType.equals("5.1")
                        ||
                        originalFormType.equals("6")
                        ||
                        originalFormType.equals("7")
                        ||
                        originalFormType.equals("8")
                        ||
                        originalFormType.equals("9")
                ) {
            return "0";
        } else if (
                originalFormType.equals("1")
                        ||
                        originalFormType.equals("2")
                        ||
                        originalFormType.equals("3")
                ) {
            return "1";
        } else if (originalFormType.equals("10")) {
            return "2";
        } else if (originalFormType.equals("11")) {
            return "3";
        } else if (
                originalFormType.equals("12")
                        ||
                        originalFormType.equals("12.1")
                        ||
                        originalFormType.equals("12.2")
                ) {
            return "4";
        } else if (originalFormType.equals("13")) {
            return "5";
        } else {
            return "无法识别凭证的外部类型!";
        }
    }

    @Override
    public Boolean isContractWaixie(String businessId) {
        String sql = "";
        DAOMeta daoMeta = null;

        //判断发票相关采购合同是否外协
        sql = "SELECT 1\n" +
                "          FROM FAD_INVOICE\n" +
                "          LEFT JOIN SCM_CONTRACT_INVOICE ON FAD_INVOICE.UUID =\n" +
                "                                            SCM_CONTRACT_INVOICE.FAD_INVOICE_ID\n" +
                "          LEFT JOIN SCM_CONTRACT ON SCM_CONTRACT_INVOICE.SCM_CONTRACT_ID =\n" +
                "                                    SCM_CONTRACT.UUID\n" +
                "          WHERE (FAD_INVOICE.UUID = '" + businessId + "') AND (SCM_CONTRACT.SCM_CONTRACT_CODE LIKE 'W%' OR FAD_INVOICE.TAX_RATE = 0.06)";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);

        //发票相关采购合同是外协
        if ((PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void certificateDetailSetDetailForBusinessId(String businessId, List fadCertificateList) {
        List fadCertificateDetailLst2 = new ArrayList<>();

        //主表凭证取得
        //Map<String, Object> fadCertificateConditionsMap = new HashMap<String, Object>();
        //fadCertificateConditionsMap.put(FadCertificateAttribute.BUSINESS_ID, businessId);
        //fadCertificateConditionsMap.put(FadCertificateAttribute.STATE, "0");
        //List fadCertificateList = isNullReturnEmpty(businessId).length() > 0 ? DtoHelper.findByAnyFields(FadCertificateDto.class, fadCertificateConditionsMap, "seq_no asc") : new ArrayList<>();
        for (int i = 0; i < fadCertificateList.size(); i++) {
            FadCertificateDto fadCertificate = (FadCertificateDto) fadCertificateList.get(i);
            String originalFormType = isNullReturnEmpty(fadCertificate.getOriginalFormType());
            String originalFormTypeForeign = getOriginalFormTypeForeign(originalFormType);

            //子表凭证摘要取得
            List<FadCertificateDetailDto> fadCertificateDetailList = fadCertificate.getFadCertificateDetailDto() != null ? fadCertificate.getFadCertificateDetailDto() : new ArrayList<>();
            for (int y = 0; y < fadCertificateDetailList.size(); y++) {

                //凭证分录取得
                FadCertificateDetailDto fadCertificateDetail = fadCertificateDetailList.get(y);
                String FinanceSubjectCode = isNullReturnEmpty(fadCertificateDetail.getSubjectCode());

                if (isNullReturnEmpty(FinanceSubjectCode).length() > 0) {

                    //全局信息
                    String debtorOrCreditor = isNullReturnEmpty(fadCertificateDetail.getDebtorOrCreditor());
                    String UserId = "";
                    String projectCodeOrSubjectCode = "";
                    String projectDepartmentCodeOrSubjectDepartmentCode = "";

                    //根据凭证来源设定分录信息
                    if (originalFormTypeForeign.equals("0")) {

                        //0.FAD_INVOICE(发票)
                        FadInvoice fadInvoice = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadInvoice.class, businessId) : null;
                        if (fadInvoice != null) {

                            //4.无请款报销
                            //5.有请款报销
                            if (originalFormType.equals("4") || originalFormType.equals("5") || originalFormType.equals("5.1")) {

                                //【报销人】取得
                                UserId = isNullReturnEmpty(fadInvoice.getRenderPerson());
                                String userName = "";
                                Map<String, Object> scdpUserConditionsMap = new HashMap<String, Object>();
                                scdpUserConditionsMap.put(ScdpUserAttribute.USER_ID, UserId);
                                List<ScdpUser> scdpUserList = isNullReturnEmpty(UserId).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, scdpUserConditionsMap, null) : new ArrayList<>();
                                if (scdpUserList.size() > 0) {
                                    ScdpUser scdpUser = (ScdpUser) scdpUserList.get(0);
                                    userName = isNullReturnEmpty(scdpUser.getUserName());
                                }

                                //【项目代号】或【费用科目代号】取得
                                //【项目部门代号】或【费用科目部门代号】取得
                                //String projectCodeOrSubjectCode = "";
                                //String projectDepartmentCodeOrSubjectDepartmentCode = "";
                                if (isNullReturnEmpty(fadInvoice.getPrmProjectMainId()).length() > 0) {
                                    PrmProjectMain prmProjectMain = isNullReturnEmpty(fadInvoice.getPrmProjectMainId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(fadInvoice.getPrmProjectMainId())) : null;
                                    if (prmProjectMain != null) {
                                        projectCodeOrSubjectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                                        projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                    }
                                } else {
                                    projectCodeOrSubjectCode = isNullReturnEmpty(fadInvoice.getSubjectCode());

                                    Map<String, Object> nonProjectSubjectSubjectConditionsMap = new HashMap<String, Object>();
                                    nonProjectSubjectSubjectConditionsMap.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, projectCodeOrSubjectCode);
                                    List nonProjectSubjectSubjectList = isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0 ? DtoHelper.findByAnyFields(NonProjectSubjectSubjectDto.class, nonProjectSubjectSubjectConditionsMap, null) : new ArrayList<>();
                                    if (nonProjectSubjectSubjectList.size() > 0) {
                                        NonProjectSubjectSubjectDto nonProjectSubjectSubject = (NonProjectSubjectSubjectDto) nonProjectSubjectSubjectList.get(0);
                                        projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                    }
                                }

                                //【项目部门名称】或【费用科目部门名称】取得
                                String projectDepartmentCodeOrSubjectDepartmentName = "";
                                Map<String, Object> scdpOrgConditionsMap = new HashMap<String, Object>();
                                scdpOrgConditionsMap.put(ScdpOrgAttribute.ORG_CODE, projectDepartmentCodeOrSubjectDepartmentCode);
                                List<ScdpOrg> scdpOrgList = isNullReturnEmpty(projectDepartmentCodeOrSubjectDepartmentCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpOrg.class, scdpOrgConditionsMap, null) : new ArrayList<>();
                                if (scdpOrgList.size() > 0) {
                                    ScdpOrg scdpOrg = (ScdpOrg) scdpOrgList.get(0);
                                    projectDepartmentCodeOrSubjectDepartmentName = isNullReturnEmpty(scdpOrg.getOrgName());
                                }

                                //【费用科目内容】取得
                                String subjectName = isNullReturnEmpty(fadInvoice.getSubjectName());

                                fadCertificateDetail.setFree1(projectCodeOrSubjectCode);
                                if (originalFormType.equals("4")) {
                                    if (isNullReturnEmpty(debtorOrCreditor).equals("0")) {
                                        if (FinanceSubjectCode.equals("540101")) {
                                            fadCertificateDetail.setAbstracts(projectDepartmentCodeOrSubjectDepartmentName + userName + subjectName + "报销");
                                        }
                                    } else if (isNullReturnEmpty(debtorOrCreditor).equals("1")) {
                                        if (FinanceSubjectCode.equals("1002") || FinanceSubjectCode.equals("1001")) {
                                            fadCertificateDetail.setAbstracts("付" + projectDepartmentCodeOrSubjectDepartmentName + userName + subjectName);
                                        }
                                    }
                                } else if (originalFormType.equals("5") || originalFormType.equals("5.1")) {
                                    if (isNullReturnEmpty(debtorOrCreditor).equals("0")) {
                                        if (FinanceSubjectCode.equals("540101") || FinanceSubjectCode.equals("2241")) {
                                            fadCertificateDetail.setAbstracts(projectDepartmentCodeOrSubjectDepartmentName + userName + subjectName + "报销");
                                        }
                                    } else if (isNullReturnEmpty(debtorOrCreditor).equals("1")) {
                                        if (FinanceSubjectCode.equals("2241")) {
                                            fadCertificateDetail.setAbstracts(projectDepartmentCodeOrSubjectDepartmentName + userName + subjectName + "报销");
                                        } else if (FinanceSubjectCode.equals("122102") || FinanceSubjectCode.equals("122106") || FinanceSubjectCode.equals("122166") || FinanceSubjectCode.equals("1002") || FinanceSubjectCode.equals("1001")) {
                                            fadCertificateDetail.setAbstracts("核销" + projectDepartmentCodeOrSubjectDepartmentName + userName + subjectName + "请款");
                                        }
                                    }
                                }
                            }

                            //6.无请款采购合同
                            //7.有请款采购合同应付、有请款采购合同预付
                            else if (originalFormType.equals("6") || originalFormType.equals("7")) {

                                //【报销人】取得
                                UserId = isNullReturnEmpty(fadInvoice.getRenderPerson());

                                //【供应商名称】取得
                                String CompleteName = "";
                                ScmSupplier scmSupplier = isNullReturnEmpty(fadInvoice.getSupplierId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmSupplier.class, isNullReturnEmpty(fadInvoice.getSupplierId())) : null;
                                if (scmSupplier != null) {
                                    CompleteName = isNullReturnEmpty(scmSupplier.getCompleteName());
                                } else {
                                    CompleteName = isNullReturnEmpty(fadInvoice.getSupplierName());
                                }

                                //【项目代号】或【费用科目代号】取得
                                //【项目部门代号】或【费用科目部门代号】取得
                                //String projectCodeOrSubjectCode = "";
                                //String projectDepartmentCodeOrSubjectDepartmentCode = "";
                                Map<String, Object> scmContractInvoiceConditionsMap = new HashMap<String, Object>();
                                scmContractInvoiceConditionsMap.put(ScmContractInvoiceAttribute.FAD_INVOICE_ID, isNullReturnEmpty(fadInvoice.getUuid()));
                                List<ScmContractInvoice> scmContractInvoiceList = isNullReturnEmpty(fadInvoice.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScmContractInvoice.class, scmContractInvoiceConditionsMap, null) : new ArrayList<>();
                                if (scmContractInvoiceList.size() > 0) {
                                    for (int yy = 0; yy < scmContractInvoiceList.size(); yy++) {
                                        ScmContractInvoice scmContractInvoice = scmContractInvoiceList.get(yy);
                                        ScmContract scmContract = isNullReturnEmpty(scmContractInvoice.getScmContractId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmContract.class, isNullReturnEmpty(scmContractInvoice.getScmContractId())) : null;
                                        if (scmContract != null) {
                                            if ((isNullReturnEmpty(scmContract.getScmContractCode()).length() > 0) && (isNullReturnEmpty(fadCertificateDetail.getOriginalFormCode()).indexOf(isNullReturnEmpty(scmContract.getScmContractCode())) != -1)) {
                                                if (isNullReturnEmpty(scmContract.getIsProject()).equals("1")) {
                                                    PrmProjectMain prmProjectMain = isNullReturnEmpty(scmContract.getProjectId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(scmContract.getProjectId())) : null;
                                                    if (prmProjectMain != null) {
                                                        if (!(isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0)) {
                                                            projectCodeOrSubjectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                                                        } else {
                                                            projectCodeOrSubjectCode = isNullReturnEmpty(projectCodeOrSubjectCode) + MergeSplitSymbol + isNullReturnEmpty(prmProjectMain.getProjectCode());
                                                        }
                                                        if (!(isNullReturnEmpty(projectDepartmentCodeOrSubjectDepartmentCode).length() > 0)) {
                                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                                        } else {
                                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(projectDepartmentCodeOrSubjectDepartmentCode) + MergeSplitSymbol + isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                                        }
                                                    }
                                                } else {
                                                    if (!(isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0)) {
                                                        projectCodeOrSubjectCode = isNullReturnEmpty(scmContract.getSubjectCode());
                                                    } else {
                                                        projectCodeOrSubjectCode = isNullReturnEmpty(projectCodeOrSubjectCode) + MergeSplitSymbol + isNullReturnEmpty(scmContract.getSubjectCode());
                                                    }

                                                    Map<String, Object> nonProjectSubjectSubjectConditionsMap = new HashMap<String, Object>();
                                                    nonProjectSubjectSubjectConditionsMap.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, isNullReturnEmpty(scmContract.getSubjectCode()));
                                                    List nonProjectSubjectSubjectList = isNullReturnEmpty(scmContract.getSubjectCode()).length() > 0 ? DtoHelper.findByAnyFields(NonProjectSubjectSubjectDto.class, nonProjectSubjectSubjectConditionsMap, null) : new ArrayList<>();
                                                    if (nonProjectSubjectSubjectList.size() > 0) {
                                                        NonProjectSubjectSubjectDto nonProjectSubjectSubject = (NonProjectSubjectSubjectDto) nonProjectSubjectSubjectList.get(0);
                                                        if (!(isNullReturnEmpty(projectDepartmentCodeOrSubjectDepartmentCode).length() > 0)) {
                                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                                        } else {
                                                            projectDepartmentCodeOrSubjectDepartmentCode = projectDepartmentCodeOrSubjectDepartmentCode + MergeSplitSymbol + isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if (isNullReturnEmpty(fadInvoice.getPrmProjectMainId()).length() > 0) {
                                        PrmProjectMain prmProjectMain = isNullReturnEmpty(fadInvoice.getPrmProjectMainId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(fadInvoice.getPrmProjectMainId())) : null;
                                        if (prmProjectMain != null) {
                                            projectCodeOrSubjectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                        }
                                    } else {
                                        projectCodeOrSubjectCode = isNullReturnEmpty(fadInvoice.getSubjectCode());

                                        Map<String, Object> nonProjectSubjectSubjectConditionsMap = new HashMap<String, Object>();
                                        nonProjectSubjectSubjectConditionsMap.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, projectCodeOrSubjectCode);
                                        List nonProjectSubjectSubjectList = isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0 ? DtoHelper.findByAnyFields(NonProjectSubjectSubjectDto.class, nonProjectSubjectSubjectConditionsMap, null) : new ArrayList<>();
                                        if (nonProjectSubjectSubjectList.size() > 0) {
                                            NonProjectSubjectSubjectDto nonProjectSubjectSubject = (NonProjectSubjectSubjectDto) nonProjectSubjectSubjectList.get(0);
                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                        }
                                    }
                                }

                                fadCertificateDetail.setFree1(projectCodeOrSubjectCode);
                                if (isNullReturnEmpty(debtorOrCreditor).equals("0")) {
                                    if (FinanceSubjectCode.equals("2221010101")) {
                                        fadCertificateDetail.setAbstracts(CompleteName + "购材料进项税" + isNullReturnEmpty(fadInvoice.getInvoiceReqNo()));
                                    } else if (FinanceSubjectCode.equals("140302") || FinanceSubjectCode.equals("5401")) {
                                        fadCertificateDetail.setAbstracts(CompleteName + "购材料");
                                    }
                                } else if (isNullReturnEmpty(debtorOrCreditor).equals("1")) {
                                    if (FinanceSubjectCode.equals("2221010104")) {
                                        fadCertificateDetail.setAbstracts(CompleteName + "购材料进项税" + isNullReturnEmpty(fadInvoice.getInvoiceReqNo()));
                                    } else if (FinanceSubjectCode.equals("1123")) {
                                        fadCertificateDetail.setAbstracts("核销" + CompleteName + "材料款请款");
                                    } else if (FinanceSubjectCode.equals("2202")) {
                                        fadCertificateDetail.setAbstracts("应付" + CompleteName + "材料款");
                                    }
                                }
                            }

                            //8.采购合同成本确认
                            else if (originalFormType.equals("8")) {

                                //【报销人】取得
                                UserId = isNullReturnEmpty(fadInvoice.getRenderPerson());

                                //【项目代号】或【费用科目代号】取得
                                //【项目部门代号】或【费用科目部门代号】取得
                                //String projectCodeOrSubjectCode = "";
                                //String projectDepartmentCodeOrSubjectDepartmentCode = "";
                                Map<String, Object> scmContractInvoiceConditionsMap = new HashMap<String, Object>();
                                scmContractInvoiceConditionsMap.put(ScmContractInvoiceAttribute.FAD_INVOICE_ID, isNullReturnEmpty(fadInvoice.getUuid()));
                                List<ScmContractInvoice> scmContractInvoiceList = isNullReturnEmpty(fadInvoice.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScmContractInvoice.class, scmContractInvoiceConditionsMap, null) : new ArrayList<>();
                                if (scmContractInvoiceList.size() > 0) {
                                    for (int yy = 0; yy < scmContractInvoiceList.size(); yy++) {
                                        ScmContractInvoice scmContractInvoice = scmContractInvoiceList.get(yy);
                                        ScmContract scmContract = isNullReturnEmpty(scmContractInvoice.getScmContractId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmContract.class, isNullReturnEmpty(scmContractInvoice.getScmContractId())) : null;
                                        if (scmContract != null) {
                                            if ((isNullReturnEmpty(scmContract.getScmContractCode()).length() > 0) && (isNullReturnEmpty(fadCertificateDetail.getOriginalFormCode()).indexOf(isNullReturnEmpty(scmContract.getScmContractCode())) != -1)) {
                                                if (isNullReturnEmpty(scmContract.getIsProject()).equals("1")) {
                                                    PrmProjectMain prmProjectMain = isNullReturnEmpty(scmContract.getProjectId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(scmContract.getProjectId())) : null;
                                                    if (prmProjectMain != null) {
                                                        if (!(isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0)) {
                                                            projectCodeOrSubjectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                                                        } else {
                                                            projectCodeOrSubjectCode = isNullReturnEmpty(projectCodeOrSubjectCode) + MergeSplitSymbol + isNullReturnEmpty(prmProjectMain.getProjectCode());
                                                        }
                                                        if (!(isNullReturnEmpty(projectDepartmentCodeOrSubjectDepartmentCode).length() > 0)) {
                                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                                        } else {
                                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(projectDepartmentCodeOrSubjectDepartmentCode) + MergeSplitSymbol + isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                                        }
                                                    }
                                                } else {
                                                    if (!(isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0)) {
                                                        projectCodeOrSubjectCode = isNullReturnEmpty(scmContract.getSubjectCode());
                                                    } else {
                                                        projectCodeOrSubjectCode = isNullReturnEmpty(projectCodeOrSubjectCode) + MergeSplitSymbol + isNullReturnEmpty(scmContract.getSubjectCode());
                                                    }

                                                    Map<String, Object> nonProjectSubjectSubjectConditionsMap = new HashMap<String, Object>();
                                                    nonProjectSubjectSubjectConditionsMap.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, isNullReturnEmpty(scmContract.getSubjectCode()));
                                                    List nonProjectSubjectSubjectList = isNullReturnEmpty(scmContract.getSubjectCode()).length() > 0 ? DtoHelper.findByAnyFields(NonProjectSubjectSubjectDto.class, nonProjectSubjectSubjectConditionsMap, null) : new ArrayList<>();
                                                    if (nonProjectSubjectSubjectList.size() > 0) {
                                                        NonProjectSubjectSubjectDto nonProjectSubjectSubject = (NonProjectSubjectSubjectDto) nonProjectSubjectSubjectList.get(0);
                                                        if (!(isNullReturnEmpty(projectDepartmentCodeOrSubjectDepartmentCode).length() > 0)) {
                                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                                        } else {
                                                            projectDepartmentCodeOrSubjectDepartmentCode = projectDepartmentCodeOrSubjectDepartmentCode + MergeSplitSymbol + isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if (isNullReturnEmpty(fadInvoice.getPrmProjectMainId()).length() > 0) {
                                        PrmProjectMain prmProjectMain = isNullReturnEmpty(fadInvoice.getPrmProjectMainId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(fadInvoice.getPrmProjectMainId())) : null;
                                        if (prmProjectMain != null) {
                                            projectCodeOrSubjectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                        }
                                    } else {
                                        projectCodeOrSubjectCode = isNullReturnEmpty(fadInvoice.getSubjectCode());

                                        Map<String, Object> nonProjectSubjectSubjectConditionsMap = new HashMap<String, Object>();
                                        nonProjectSubjectSubjectConditionsMap.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, projectCodeOrSubjectCode);
                                        List nonProjectSubjectSubjectList = isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0 ? DtoHelper.findByAnyFields(NonProjectSubjectSubjectDto.class, nonProjectSubjectSubjectConditionsMap, null) : new ArrayList<>();
                                        if (nonProjectSubjectSubjectList.size() > 0) {
                                            NonProjectSubjectSubjectDto nonProjectSubjectSubject = (NonProjectSubjectSubjectDto) nonProjectSubjectSubjectList.get(0);
                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                        }
                                    }
                                }

                                fadCertificateDetail.setFree1(projectCodeOrSubjectCode);
                                if (isNullReturnEmpty(debtorOrCreditor).equals("0")) {
                                    if (FinanceSubjectCode.equals("5401")) {
                                        fadCertificateDetail.setAbstracts("领用材料");
                                    }
                                } else if (isNullReturnEmpty(debtorOrCreditor).equals("1")) {
                                    if (FinanceSubjectCode.equals("140302")) {
                                        fadCertificateDetail.setAbstracts("结转材料");
                                    }
                                }
                            }

                            //9.采购合同支付直接支付
                            else if (originalFormType.equals("9")) {

                                //【报销人】取得
                                UserId = isNullReturnEmpty(fadInvoice.getRenderPerson());

                                //【供应商名称】取得
                                String CompleteName = "";
                                ScmSupplier scmSupplier = isNullReturnEmpty(fadInvoice.getSupplierId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmSupplier.class, isNullReturnEmpty(fadInvoice.getSupplierId())) : null;
                                if (scmSupplier != null) {
                                    CompleteName = isNullReturnEmpty(scmSupplier.getCompleteName());
                                } else {
                                    CompleteName = isNullReturnEmpty(fadInvoice.getSupplierName());
                                }

                                //【项目代号】或【费用科目代号】取得
                                //【项目部门代号】或【费用科目部门代号】取得
                                //String projectCodeOrSubjectCode = "";
                                //String projectDepartmentCodeOrSubjectDepartmentCode = "";
                                Map<String, Object> scmContractInvoiceConditionsMap = new HashMap<String, Object>();
                                scmContractInvoiceConditionsMap.put(ScmContractInvoiceAttribute.FAD_INVOICE_ID, isNullReturnEmpty(fadInvoice.getUuid()));
                                List<ScmContractInvoice> scmContractInvoiceList = isNullReturnEmpty(fadInvoice.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScmContractInvoice.class, scmContractInvoiceConditionsMap, null) : new ArrayList<>();
                                if (scmContractInvoiceList.size() > 0) {
                                    for (int yy = 0; yy < scmContractInvoiceList.size(); yy++) {
                                        ScmContractInvoice scmContractInvoice = scmContractInvoiceList.get(yy);
                                        ScmContract scmContract = isNullReturnEmpty(scmContractInvoice.getScmContractId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmContract.class, isNullReturnEmpty(scmContractInvoice.getScmContractId())) : null;
                                        if (scmContract != null) {
                                            if ((isNullReturnEmpty(scmContract.getScmContractCode()).length() > 0) && (isNullReturnEmpty(fadCertificateDetail.getOriginalFormCode()).indexOf(isNullReturnEmpty(scmContract.getScmContractCode())) != -1)) {
                                                if (isNullReturnEmpty(scmContract.getIsProject()).equals("1")) {
                                                    PrmProjectMain prmProjectMain = isNullReturnEmpty(scmContract.getProjectId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(scmContract.getProjectId())) : null;
                                                    if (prmProjectMain != null) {
                                                        if (!(isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0)) {
                                                            projectCodeOrSubjectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                                                        } else {
                                                            projectCodeOrSubjectCode = isNullReturnEmpty(projectCodeOrSubjectCode) + MergeSplitSymbol + isNullReturnEmpty(prmProjectMain.getProjectCode());
                                                        }
                                                        if (!(isNullReturnEmpty(projectDepartmentCodeOrSubjectDepartmentCode).length() > 0)) {
                                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                                        } else {
                                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(projectDepartmentCodeOrSubjectDepartmentCode) + MergeSplitSymbol + isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                                        }
                                                    }
                                                } else {
                                                    if (!(isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0)) {
                                                        projectCodeOrSubjectCode = isNullReturnEmpty(scmContract.getSubjectCode());
                                                    } else {
                                                        projectCodeOrSubjectCode = isNullReturnEmpty(projectCodeOrSubjectCode) + MergeSplitSymbol + isNullReturnEmpty(scmContract.getSubjectCode());
                                                    }

                                                    Map<String, Object> nonProjectSubjectSubjectConditionsMap = new HashMap<String, Object>();
                                                    nonProjectSubjectSubjectConditionsMap.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, isNullReturnEmpty(scmContract.getSubjectCode()));
                                                    List nonProjectSubjectSubjectList = isNullReturnEmpty(scmContract.getSubjectCode()).length() > 0 ? DtoHelper.findByAnyFields(NonProjectSubjectSubjectDto.class, nonProjectSubjectSubjectConditionsMap, null) : new ArrayList<>();
                                                    if (nonProjectSubjectSubjectList.size() > 0) {
                                                        NonProjectSubjectSubjectDto nonProjectSubjectSubject = (NonProjectSubjectSubjectDto) nonProjectSubjectSubjectList.get(0);
                                                        if (!(isNullReturnEmpty(projectDepartmentCodeOrSubjectDepartmentCode).length() > 0)) {
                                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                                        } else {
                                                            projectDepartmentCodeOrSubjectDepartmentCode = projectDepartmentCodeOrSubjectDepartmentCode + MergeSplitSymbol + isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if (isNullReturnEmpty(fadInvoice.getPrmProjectMainId()).length() > 0) {
                                        PrmProjectMain prmProjectMain = isNullReturnEmpty(fadInvoice.getPrmProjectMainId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(fadInvoice.getPrmProjectMainId())) : null;
                                        if (prmProjectMain != null) {
                                            projectCodeOrSubjectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                        }
                                    } else {
                                        projectCodeOrSubjectCode = isNullReturnEmpty(fadInvoice.getSubjectCode());

                                        Map<String, Object> nonProjectSubjectSubjectConditionsMap = new HashMap<String, Object>();
                                        nonProjectSubjectSubjectConditionsMap.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, projectCodeOrSubjectCode);
                                        List nonProjectSubjectSubjectList = isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0 ? DtoHelper.findByAnyFields(NonProjectSubjectSubjectDto.class, nonProjectSubjectSubjectConditionsMap, null) : new ArrayList<>();
                                        if (nonProjectSubjectSubjectList.size() > 0) {
                                            NonProjectSubjectSubjectDto nonProjectSubjectSubject = (NonProjectSubjectSubjectDto) nonProjectSubjectSubjectList.get(0);
                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                        }
                                    }
                                }

                                fadCertificateDetail.setFree1(projectCodeOrSubjectCode);
                                if (isNullReturnEmpty(debtorOrCreditor).equals("0")) {
                                    if (FinanceSubjectCode.equals("2202")) {
                                        fadCertificateDetail.setAbstracts("核销应付款");
                                    }
                                } else if (isNullReturnEmpty(debtorOrCreditor).equals("1")) {
                                    if (FinanceSubjectCode.equals("1002") || FinanceSubjectCode.equals("1001")) {
                                        fadCertificateDetail.setAbstracts("付" + CompleteName + "材料款");
                                    }
                                }
                            }
                        }
                    } else if (originalFormTypeForeign.equals("1")) {

                        //1.FAD_CASH_REQ(请款)
                        FadCashReq fadCashReq = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadCashReq.class, businessId) : null;
                        if (fadCashReq != null) {

                            //1.个人请款(1日常2差旅)
                            if (originalFormType.equals("1")) {

                                //【请款人】取得
                                UserId = isNullReturnEmpty(fadCashReq.getStaffId());
                                String userName = "";
                                Map<String, Object> scdpUserConditionsMap = new HashMap<String, Object>();
                                scdpUserConditionsMap.put(ScdpUserAttribute.USER_ID, UserId);
                                List<ScdpUser> scdpUserList = isNullReturnEmpty(UserId).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, scdpUserConditionsMap, null) : new ArrayList<>();
                                if (scdpUserList.size() > 0) {
                                    ScdpUser scdpUser = (ScdpUser) scdpUserList.get(0);
                                    userName = isNullReturnEmpty(scdpUser.getUserName());
                                }

                                //【项目代号】或【费用科目代号】取得
                                //【项目部门代号】或【费用科目部门代号】取得
                                //String projectCodeOrSubjectCode = "";
                                //String projectDepartmentCodeOrSubjectDepartmentCode = "";
                                if (isNullReturnEmpty(fadCashReq.getIsProject()).equals("1")) {
                                    PrmProjectMain prmProjectMain = isNullReturnEmpty(fadCashReq.getProjectId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(fadCashReq.getProjectId())) : null;
                                    if (prmProjectMain != null) {
                                        projectCodeOrSubjectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                                        projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                    }
                                } else {
                                    projectCodeOrSubjectCode = isNullReturnEmpty(fadCashReq.getSubjectCode());

                                    Map<String, Object> nonProjectSubjectSubjectConditionsMap = new HashMap<String, Object>();
                                    nonProjectSubjectSubjectConditionsMap.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, projectCodeOrSubjectCode);
                                    List nonProjectSubjectSubjectList = isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0 ? DtoHelper.findByAnyFields(NonProjectSubjectSubjectDto.class, nonProjectSubjectSubjectConditionsMap, null) : new ArrayList<>();
                                    if (nonProjectSubjectSubjectList.size() > 0) {
                                        NonProjectSubjectSubjectDto nonProjectSubjectSubject = (NonProjectSubjectSubjectDto) nonProjectSubjectSubjectList.get(0);
                                        projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                    }
                                }

                                //【项目部门名称】或【费用科目部门名称】取得
                                String projectDepartmentCodeOrSubjectDepartmentName = "";
                                Map<String, Object> scdpOrgConditionsMap = new HashMap<String, Object>();
                                scdpOrgConditionsMap.put(ScdpOrgAttribute.ORG_CODE, projectDepartmentCodeOrSubjectDepartmentCode);
                                List<ScdpOrg> scdpOrgList = isNullReturnEmpty(projectDepartmentCodeOrSubjectDepartmentCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpOrg.class, scdpOrgConditionsMap, null) : new ArrayList<>();
                                if (scdpOrgList.size() > 0) {
                                    ScdpOrg scdpOrg = (ScdpOrg) scdpOrgList.get(0);
                                    projectDepartmentCodeOrSubjectDepartmentName = isNullReturnEmpty(scdpOrg.getOrgName());
                                }

                                //【费用科目内容】取得
                                String subjectName = isNullReturnEmpty(fadCashReq.getSubjectName());

                                fadCertificateDetail.setFree1(projectCodeOrSubjectCode);
                                if (isNullReturnEmpty(debtorOrCreditor).equals("0")) {
                                    if (FinanceSubjectCode.equals("122102")) {
                                        fadCertificateDetail.setAbstracts(projectDepartmentCodeOrSubjectDepartmentName + userName + subjectName + "请款");
                                    }
                                } else if (isNullReturnEmpty(debtorOrCreditor).equals("1")) {
                                    if (FinanceSubjectCode.equals("1002")) {
                                        fadCertificateDetail.setAbstracts("付" + projectDepartmentCodeOrSubjectDepartmentName + userName + subjectName);
                                    }
                                }
                            }

                            //2.材料费或外包商请款(0采购合同)
                            else if (originalFormType.equals("2")) {

                                //【请款人】取得
                                UserId = isNullReturnEmpty(fadCashReq.getStaffId());

                                //【供应商名称】取得
                                String CompleteName = "";
                                ScmSupplier scmSupplier = isNullReturnEmpty(fadCashReq.getSupplierId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmSupplier.class, isNullReturnEmpty(fadCashReq.getSupplierId())) : null;
                                if (scmSupplier != null) {
                                    CompleteName = isNullReturnEmpty(scmSupplier.getCompleteName());
                                } else {
                                    CompleteName = isNullReturnEmpty(fadCashReq.getPayeeName());
                                }

                                //【项目代号】或【费用科目代号】取得
                                //【项目部门代号】或【费用科目部门代号】取得
                                //【费用科目内容】取得
                                //String projectCodeOrSubjectCode = "";
                                //String projectDepartmentCodeOrSubjectDepartmentCode = "";
                                String subjectName = "";
                                ScmContract scmContract = isNullReturnEmpty(fadCashReq.getPurchaseContractId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmContract.class, isNullReturnEmpty(fadCashReq.getPurchaseContractId())) : null;
                                if (scmContract != null) {
                                    if (isNullReturnEmpty(scmContract.getIsProject()).equals("1")) {
                                        PrmProjectMain prmProjectMain = isNullReturnEmpty(scmContract.getProjectId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(scmContract.getProjectId())) : null;
                                        if (prmProjectMain != null) {
                                            projectCodeOrSubjectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                        }

                                        String sql = "SELECT SUBJECT_NAME FROM FAD_BASE_SUBJECT WHERE SUBJECT_NO = '" + isNullReturnEmpty(scmContract.getSubjectCode()) + "'";
                                        DAOMeta daoMeta = new DAOMeta();
                                        daoMeta.setStrSql(sql);
                                        daoMeta.setNeedFilter(false);
                                        List fadBaseSubjectList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                                        if (fadBaseSubjectList.size() > 0) {
                                            Map fadBaseSubject = (Map) fadBaseSubjectList.get(0);
                                            subjectName = isNullReturnEmpty(fadBaseSubject.get("subjectName"));
                                        }
                                    } else {
                                        projectCodeOrSubjectCode = isNullReturnEmpty(scmContract.getSubjectCode());

                                        Map<String, Object> nonProjectSubjectSubjectConditionsMap = new HashMap<String, Object>();
                                        nonProjectSubjectSubjectConditionsMap.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, projectCodeOrSubjectCode);
                                        List nonProjectSubjectSubjectList = isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0 ? DtoHelper.findByAnyFields(NonProjectSubjectSubjectDto.class, nonProjectSubjectSubjectConditionsMap, null) : new ArrayList<>();
                                        if (nonProjectSubjectSubjectList.size() > 0) {
                                            NonProjectSubjectSubjectDto nonProjectSubjectSubject = (NonProjectSubjectSubjectDto) nonProjectSubjectSubjectList.get(0);
                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                            subjectName = isNullReturnEmpty(nonProjectSubjectSubject.getFinancialSubject());
                                        }
                                    }
                                }

                                fadCertificateDetail.setFree1(projectCodeOrSubjectCode);
                                if (isNullReturnEmpty(debtorOrCreditor).equals("0")) {
                                    if (FinanceSubjectCode.equals("1123")) {
                                        fadCertificateDetail.setAbstracts(CompleteName + "购" + subjectName + "预付款");
                                    }
                                } else if (isNullReturnEmpty(debtorOrCreditor).equals("1")) {
                                    if (FinanceSubjectCode.equals("1002")) {
                                        fadCertificateDetail.setAbstracts("付" + CompleteName + "购" + subjectName + "预付款");
                                    }
                                }
                            }

                            //3.保证金请款(3保证金)
                            else if (originalFormType.equals("3")) {

                                //【请款人】取得
                                UserId = isNullReturnEmpty(fadCashReq.getStaffId());

                                //【供应商名称】取得
                                String CompleteName = "";
                                ScmSupplier scmSupplier = isNullReturnEmpty(fadCashReq.getSupplierId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmSupplier.class, isNullReturnEmpty(fadCashReq.getSupplierId())) : null;
                                if (scmSupplier != null) {
                                    CompleteName = isNullReturnEmpty(scmSupplier.getCompleteName());
                                } else {
                                    CompleteName = isNullReturnEmpty(fadCashReq.getPayeeName());
                                }

                                if (isNullReturnEmpty(debtorOrCreditor).equals("0")) {
                                    if (FinanceSubjectCode.equals("122106")) {
                                        SimpleDateFormat makeDateToString = new SimpleDateFormat("yyyy-MM-dd");
                                        fadCertificateDetail.setAbstracts(CompleteName + "保证金请款" + makeDateToString.format(fadCashReq.getSquareDate()));
                                    }
                                } else if (isNullReturnEmpty(debtorOrCreditor).equals("1")) {
                                    if (FinanceSubjectCode.equals("1002")) {
                                        fadCertificateDetail.setAbstracts("付" + CompleteName + "保证金请款");
                                    }
                                }
                            }
                            //周转金请款
                            else if (originalFormType.equals("14")) {

                                //【请款人】取得
                                UserId = isNullReturnEmpty(fadCashReq.getStaffId());

                                //【供应商名称】取得
                                String CompleteName = "";
                                ScmSupplier scmSupplier = isNullReturnEmpty(fadCashReq.getSupplierId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmSupplier.class, isNullReturnEmpty(fadCashReq.getSupplierId())) : null;
                                if (scmSupplier != null) {
                                    CompleteName = isNullReturnEmpty(scmSupplier.getCompleteName());
                                } else {
                                    CompleteName = isNullReturnEmpty(fadCashReq.getPayeeName());
                                }

                                if (isNullReturnEmpty(debtorOrCreditor).equals("0")) {
                                    if (FinanceSubjectCode.equals("122166")) {
                                        SimpleDateFormat makeDateToString = new SimpleDateFormat("yyyy-MM-dd");
                                        fadCertificateDetail.setAbstracts(CompleteName + "周转金请款" + makeDateToString.format(fadCashReq.getSquareDate()));
                                    }
                                } else if (isNullReturnEmpty(debtorOrCreditor).equals("1")) {
                                    if (FinanceSubjectCode.equals("1002")) {
                                        fadCertificateDetail.setAbstracts("付" + CompleteName + "周转金请款");
                                    }
                                }
                            }
                        }
                    } else if (originalFormTypeForeign.equals("2")) {

                        //2.FAD_PAY_REQ_C(支付)【提示:请用王兆刚的ERP账号登录测试(支付)的生成凭证!】
                        FadPayReqC fadPayReqC = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadPayReqC.class, businessId) : null;
                        if (fadPayReqC != null) {

                            //10.采购合同支付
                            if (originalFormType.equals("10")) {

                                //【供应商名称】取得
                                String CompleteName = "";
                                ScmSupplier scmSupplier = isNullReturnEmpty(fadPayReqC.getSupplierId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmSupplier.class, isNullReturnEmpty(fadPayReqC.getSupplierId())) : null;
                                if (scmSupplier != null) {
                                    CompleteName = isNullReturnEmpty(scmSupplier.getCompleteName());
                                }

                                //【项目代号】或【费用科目代号】取得
                                //String projectCodeOrSubjectCode = "";
                                //String projectDepartmentCodeOrSubjectDepartmentCode = "";
                                ScmContract scmContract = isNullReturnEmpty(fadPayReqC.getScmContractId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmContract.class, isNullReturnEmpty(fadPayReqC.getScmContractId())) : null;
                                if (scmContract != null) {
                                    if (isNullReturnEmpty(scmContract.getIsProject()).equals("1")) {
                                        PrmProjectMain prmProjectMain = isNullReturnEmpty(scmContract.getProjectId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(scmContract.getProjectId())) : null;
                                        if (prmProjectMain != null) {
                                            projectCodeOrSubjectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                        }
                                    } else {
                                        projectCodeOrSubjectCode = isNullReturnEmpty(scmContract.getSubjectCode());

                                        Map<String, Object> nonProjectSubjectSubjectConditionsMap = new HashMap<String, Object>();
                                        nonProjectSubjectSubjectConditionsMap.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, projectCodeOrSubjectCode);
                                        List nonProjectSubjectSubjectList = isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0 ? DtoHelper.findByAnyFields(NonProjectSubjectSubjectDto.class, nonProjectSubjectSubjectConditionsMap, null) : new ArrayList<>();
                                        if (nonProjectSubjectSubjectList.size() > 0) {
                                            NonProjectSubjectSubjectDto nonProjectSubjectSubject = (NonProjectSubjectSubjectDto) nonProjectSubjectSubjectList.get(0);
                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                        }
                                    }
                                }

                                fadCertificateDetail.setFree1(projectCodeOrSubjectCode);
                                if (isNullReturnEmpty(debtorOrCreditor).equals("0")) {
                                    if (FinanceSubjectCode.equals("2202")) {
                                        fadCertificateDetail.setAbstracts("核销应付款");
                                    }
                                } else if (isNullReturnEmpty(debtorOrCreditor).equals("1")) {
                                    if (FinanceSubjectCode.equals("1002")) {
                                        fadCertificateDetail.setAbstracts("付" + CompleteName + "材料款");
                                    }
                                }
                            }
                        }
                    } else if (originalFormTypeForeign.equals("3")) {

                        //3.FAD_CASH_CLEARANCE(还款)
                        FadCashClearance fadCashClearance = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadCashClearance.class, businessId) : null;
                        if (fadCashClearance != null) {

                            //11.还款
                            if (originalFormType.equals("11")) {

                                //【还款人】取得
                                //【还款人部门名称】取得
                                UserId = isNullReturnEmpty(fadCashClearance.getClearancePerson());
                                String userName = "";
                                String DepartmentName = "";
                                Map<String, Object> scdpUserConditionsMap = new HashMap<String, Object>();
                                scdpUserConditionsMap.put(ScdpUserAttribute.USER_ID, UserId);
                                List<ScdpUser> scdpUserList = isNullReturnEmpty(UserId).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, scdpUserConditionsMap, null) : new ArrayList<>();
                                if (scdpUserList.size() > 0) {
                                    ScdpUser scdpUser = (ScdpUser) scdpUserList.get(0);
                                    userName = isNullReturnEmpty(scdpUser.getUserName());

                                    ScdpOrg scdpOrg = isNullReturnEmpty(scdpUser.getOrgUuid()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScdpOrg.class, isNullReturnEmpty(scdpUser.getOrgUuid())) : null;
                                    if (scdpOrg != null) {
                                        DepartmentName = isNullReturnEmpty(scdpOrg.getOrgName());
                                    }
                                }

                                //【项目代号】或【费用科目代号】取得
                                //【项目部门代号】或【费用科目部门代号】取得
                                //【费用科目内容】取得
                                //String projectCodeOrSubjectCode = "";
                                //String projectDepartmentCodeOrSubjectDepartmentCode = "";
                                String subjectName = "";
                                Map<String, Object> fadCashReqInvoiceConditionsMap = new HashMap<String, Object>();
                                fadCashReqInvoiceConditionsMap.put(FadCashReqInvoiceAttribute.FAD_INVOICE_ID, isNullReturnEmpty(fadCashClearance.getUuid()));
                                List<FadCashReqInvoice> fadCashReqInvoiceList = isNullReturnEmpty(fadCashClearance.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCashReqInvoice.class, fadCashReqInvoiceConditionsMap, null) : new ArrayList<>();
                                if (fadCashReqInvoiceList.size() > 0) {
                                    FadCashReqInvoice fadCashReqInvoice = fadCashReqInvoiceList.get(0);
                                    FadCashReq fadCashReq = isNullReturnEmpty(fadCashReqInvoice.getFadCashReqId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadCashReq.class, isNullReturnEmpty(fadCashReqInvoice.getFadCashReqId())) : null;
                                    if (fadCashReq != null) {

                                        //采购还款
                                        if (isNullReturnEmpty(fadCashReq.getReqType()).equals("0")) {

                                            //【项目代号】或【费用科目代号】取得
                                            //【费用科目内容】取得
                                            ScmContract scmContract = isNullReturnEmpty(fadCashReq.getPurchaseContractId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmContract.class, isNullReturnEmpty(fadCashReq.getPurchaseContractId())) : null;
                                            if (scmContract != null) {
                                                if (isNullReturnEmpty(scmContract.getIsProject()).equals("1")) {
                                                    PrmProjectMain prmProjectMain = isNullReturnEmpty(scmContract.getProjectId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(scmContract.getProjectId())) : null;
                                                    if (prmProjectMain != null) {
                                                        projectCodeOrSubjectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                                                        //projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                                    }

                                                    String sql = "SELECT SUBJECT_NAME FROM FAD_BASE_SUBJECT WHERE SUBJECT_NO = '" + isNullReturnEmpty(scmContract.getSubjectCode()) + "'";
                                                    DAOMeta daoMeta = new DAOMeta();
                                                    daoMeta.setStrSql(sql);
                                                    daoMeta.setNeedFilter(false);
                                                    List fadBaseSubjectList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                                                    if (fadBaseSubjectList.size() > 0) {
                                                        Map fadBaseSubject = (Map) fadBaseSubjectList.get(0);
                                                        subjectName = isNullReturnEmpty(fadBaseSubject.get("subjectName"));
                                                    }
                                                } else {
                                                    projectCodeOrSubjectCode = isNullReturnEmpty(scmContract.getSubjectCode());

                                                    Map<String, Object> nonProjectSubjectSubjectConditionsMap = new HashMap<String, Object>();
                                                    nonProjectSubjectSubjectConditionsMap.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, projectCodeOrSubjectCode);
                                                    List nonProjectSubjectSubjectList = isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0 ? DtoHelper.findByAnyFields(NonProjectSubjectSubjectDto.class, nonProjectSubjectSubjectConditionsMap, null) : new ArrayList<>();
                                                    if (nonProjectSubjectSubjectList.size() > 0) {
                                                        NonProjectSubjectSubjectDto nonProjectSubjectSubject = (NonProjectSubjectSubjectDto) nonProjectSubjectSubjectList.get(0);
                                                        //projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                                        subjectName = isNullReturnEmpty(nonProjectSubjectSubject.getFinancialSubject());
                                                    }
                                                }
                                            }
                                        }

                                        //其他还款
                                        else {
                                            //【项目代号】或【费用科目代号】取得
                                            //【项目部门代号】或【费用科目部门代号】取得
                                            if (isNullReturnEmpty(fadCashReq.getIsProject()).equals("1")) {
                                                PrmProjectMain prmProjectMain = isNullReturnEmpty(fadCashReq.getProjectId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(fadCashReq.getProjectId())) : null;
                                                if (prmProjectMain != null) {
                                                    projectCodeOrSubjectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                                                    //projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                                }
                                            } else {
                                                projectCodeOrSubjectCode = isNullReturnEmpty(fadCashReq.getSubjectCode());

                                                Map<String, Object> nonProjectSubjectSubjectConditionsMap = new HashMap<String, Object>();
                                                nonProjectSubjectSubjectConditionsMap.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, projectCodeOrSubjectCode);
                                                List nonProjectSubjectSubjectList = isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0 ? DtoHelper.findByAnyFields(NonProjectSubjectSubjectDto.class, nonProjectSubjectSubjectConditionsMap, null) : new ArrayList<>();
                                                if (nonProjectSubjectSubjectList.size() > 0) {
                                                    NonProjectSubjectSubjectDto nonProjectSubjectSubject = (NonProjectSubjectSubjectDto) nonProjectSubjectSubjectList.get(0);
                                                    //projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                                }
                                            }

                                            //【费用科目内容】取得
                                            subjectName = isNullReturnEmpty(fadCashReq.getSubjectName());
                                        }
                                    }
                                }

                                fadCertificateDetail.setFree1(projectCodeOrSubjectCode);
                                if (isNullReturnEmpty(debtorOrCreditor).equals("0")) {
                                    if (FinanceSubjectCode.equals("1001")) {
                                        fadCertificateDetail.setAbstracts("收" + DepartmentName + userName + subjectName + "还款");
                                    }
                                } else if (isNullReturnEmpty(debtorOrCreditor).equals("1")) {
                                    if (FinanceSubjectCode.equals("122102") || FinanceSubjectCode.equals("122106") || FinanceSubjectCode.equals("122166") || FinanceSubjectCode.equals("1123")) {
                                        fadCertificateDetail.setAbstracts("核销" + DepartmentName + userName + subjectName + "请款");
                                    }
                                }
                            }
                        }
                    } else if (originalFormTypeForeign.equals("4")) {

                        //4.PRM_RECEIPTS(收款)
                        PrmReceipts prmReceipts = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmReceipts.class, businessId) : null;
                        if (prmReceipts != null) {

                            //12.收款
                            if (originalFormType.equals("12") || originalFormType.equals("12.1") || originalFormType.equals("12.2")) {

                                //【项目代号】或【费用科目代号】取得
                                //【项目部门代号】或【费用科目部门代号】取得
                                //String projectCodeOrSubjectCode = "";
                                //String projectDepartmentCodeOrSubjectDepartmentCode = "";
                                if (isNullReturnEmpty(prmReceipts.getPrmProjectMainId()).length() > 0) {
                                    PrmProjectMain prmProjectMain = isNullReturnEmpty(prmReceipts.getPrmProjectMainId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(prmReceipts.getPrmProjectMainId())) : null;
                                    if (prmProjectMain != null) {
                                        projectCodeOrSubjectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                                        projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                    }
                                }

                                String customerName = "";
                                PrmCustomer prmCustomer = isNullReturnEmpty(prmReceipts.getCustomerId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmCustomer.class, isNullReturnEmpty(prmReceipts.getCustomerId())) : null;
                                if (prmCustomer != null) {
                                    customerName = prmCustomer.getCustomerName();
                                }

                                fadCertificateDetail.setFree1(projectCodeOrSubjectCode);
                                if (isNullReturnEmpty(debtorOrCreditor).equals("0")) {
                                    if (FinanceSubjectCode.equals("224198")) {
                                        fadCertificateDetail.setAbstracts(customerName);
                                    }
                                } else if (isNullReturnEmpty(debtorOrCreditor).equals("1")) {
                                    if (FinanceSubjectCode.equals("1122") || FinanceSubjectCode.equals("224198")) {
                                        fadCertificateDetail.setAbstracts(customerName);
                                    }
                                }
                            }
                        }
                    } else if (originalFormTypeForeign.equals("5")) {

                        //5.PRM_BILLING(开票)
                        PrmBilling prmBilling = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmBilling.class, businessId) : null;
                        if (prmBilling != null) {

                            //13.开票
                            if (originalFormType.equals("13")) {

                                //【项目代号】或【费用科目代号】取得
                                //【项目部门代号】或【费用科目部门代号】取得
                                //String projectCodeOrSubjectCode = "";
                                //String projectDepartmentCodeOrSubjectDepartmentCode = "";
                                if (isNullReturnEmpty(prmBilling.getPrmProjectMainId()).length() > 0) {
                                    PrmProjectMain prmProjectMain = isNullReturnEmpty(prmBilling.getPrmProjectMainId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(prmBilling.getPrmProjectMainId())) : null;
                                    if (prmProjectMain != null) {
                                        projectCodeOrSubjectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                                        projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                    }
                                }

                                String customerName = "";
                                PrmCustomer prmCustomer = isNullReturnEmpty(prmBilling.getCustomerId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmCustomer.class, isNullReturnEmpty(prmBilling.getCustomerId())) : null;
                                if (prmCustomer != null) {
                                    customerName = prmCustomer.getCustomerName();
                                }

                                fadCertificateDetail.setFree1(projectCodeOrSubjectCode);
                                if (isNullReturnEmpty(debtorOrCreditor).equals("0")) {
                                } else if (isNullReturnEmpty(debtorOrCreditor).equals("1")) {
                                    if (FinanceSubjectCode.equals("6001")) {
                                        fadCertificateDetail.setAbstracts(customerName);
                                    }
                                }
                            }
                        }
                    }
                    fadCertificateDetailLst2.add(fadCertificateDetail);
                }
            }
        }
        DtoHelper.batchUpdate(fadCertificateDetailLst2, FadCertificateDetail.class);
    }

    @Override
    public void certificateDetailSetAccountForBusinessId(String businessId, List fadCertificateList) {
        List<FadCertificateAccount> fadCertificateAccountLst3 = new ArrayList<>();

        //主表凭证取得
        //Map<String, Object> fadCertificateConditionsMap = new HashMap<String, Object>();
        //fadCertificateConditionsMap.put(FadCertificateAttribute.BUSINESS_ID, businessId);
        //fadCertificateConditionsMap.put(FadCertificateAttribute.STATE, "0");
        //List fadCertificateList = isNullReturnEmpty(businessId).length() > 0 ? DtoHelper.findByAnyFields(FadCertificateDto.class, fadCertificateConditionsMap, "seq_no asc") : new ArrayList<>();
        for (int i = 0; i < fadCertificateList.size(); i++) {
            FadCertificateDto fadCertificate = (FadCertificateDto) fadCertificateList.get(i);
            String originalFormType = isNullReturnEmpty(fadCertificate.getOriginalFormType());
            String originalFormTypeForeign = getOriginalFormTypeForeign(originalFormType);

            //子表凭证摘要取得
            List<FadCertificateDetailDto> fadCertificateDetailList = fadCertificate.getFadCertificateDetailDto() != null ? fadCertificate.getFadCertificateDetailDto() : new ArrayList<>();
            for (int y = 0; y < fadCertificateDetailList.size(); y++) {

                //凭证分录取得
                FadCertificateDetailDto fadCertificateDetail = fadCertificateDetailList.get(y);
                String FinanceSubjectCode = isNullReturnEmpty(fadCertificateDetail.getSubjectCode());

                if (isNullReturnEmpty(FinanceSubjectCode).length() > 0) {

                    //全局信息
                    String UserId = "";
                    String projectCodeOrSubjectCode = "";
                    String projectDepartmentCodeOrSubjectDepartmentCode = "";

                    //根据凭证来源设定分录信息
                    if (originalFormTypeForeign.equals("0")) {

                        //0.FAD_INVOICE(发票)
                        FadInvoice fadInvoice = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadInvoice.class, businessId) : null;
                        if (fadInvoice != null) {

                            //4.无请款报销
                            //5.有请款报销
                            if (originalFormType.equals("4") || originalFormType.equals("5") || originalFormType.equals("5.1")) {

                                //【报销人】取得
                                UserId = isNullReturnEmpty(fadInvoice.getRenderPerson());
                                String userName = "";
                                Map<String, Object> scdpUserConditionsMap = new HashMap<String, Object>();
                                scdpUserConditionsMap.put(ScdpUserAttribute.USER_ID, UserId);
                                List<ScdpUser> scdpUserList = isNullReturnEmpty(UserId).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, scdpUserConditionsMap, null) : new ArrayList<>();
                                if (scdpUserList.size() > 0) {
                                    ScdpUser scdpUser = (ScdpUser) scdpUserList.get(0);
                                    userName = isNullReturnEmpty(scdpUser.getUserName());
                                }

                                //【项目代号】或【费用科目代号】取得
                                //【项目部门代号】或【费用科目部门代号】取得
                                //String projectCodeOrSubjectCode = "";
                                //String projectDepartmentCodeOrSubjectDepartmentCode = "";
                                if (isNullReturnEmpty(fadInvoice.getPrmProjectMainId()).length() > 0) {
                                    PrmProjectMain prmProjectMain = isNullReturnEmpty(fadInvoice.getPrmProjectMainId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(fadInvoice.getPrmProjectMainId())) : null;
                                    if (prmProjectMain != null) {
                                        projectCodeOrSubjectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                                        projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                    }
                                } else {
                                    projectCodeOrSubjectCode = isNullReturnEmpty(fadInvoice.getSubjectCode());

                                    Map<String, Object> nonProjectSubjectSubjectConditionsMap = new HashMap<String, Object>();
                                    nonProjectSubjectSubjectConditionsMap.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, projectCodeOrSubjectCode);
                                    List nonProjectSubjectSubjectList = isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0 ? DtoHelper.findByAnyFields(NonProjectSubjectSubjectDto.class, nonProjectSubjectSubjectConditionsMap, null) : new ArrayList<>();
                                    if (nonProjectSubjectSubjectList.size() > 0) {
                                        NonProjectSubjectSubjectDto nonProjectSubjectSubject = (NonProjectSubjectSubjectDto) nonProjectSubjectSubjectList.get(0);
                                        projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                    }
                                }

                                //【项目部门名称】或【费用科目部门名称】取得
                                String projectDepartmentCodeOrSubjectDepartmentName = "";
                                Map<String, Object> scdpOrgConditionsMap = new HashMap<String, Object>();
                                scdpOrgConditionsMap.put(ScdpOrgAttribute.ORG_CODE, projectDepartmentCodeOrSubjectDepartmentCode);
                                List<ScdpOrg> scdpOrgList = isNullReturnEmpty(projectDepartmentCodeOrSubjectDepartmentCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpOrg.class, scdpOrgConditionsMap, null) : new ArrayList<>();
                                if (scdpOrgList.size() > 0) {
                                    ScdpOrg scdpOrg = (ScdpOrg) scdpOrgList.get(0);
                                    projectDepartmentCodeOrSubjectDepartmentName = isNullReturnEmpty(scdpOrg.getOrgName());
                                }
                            }

                            //6.无请款采购合同
                            //7.有请款采购合同应付、有请款采购合同预付
                            else if (originalFormType.equals("6") || originalFormType.equals("7")) {

                                //【报销人】取得
                                UserId = isNullReturnEmpty(fadInvoice.getRenderPerson());

                                //【项目代号】或【费用科目代号】取得
                                //【项目部门代号】或【费用科目部门代号】取得
                                //String projectCodeOrSubjectCode = "";
                                //String projectDepartmentCodeOrSubjectDepartmentCode = "";
                                Map<String, Object> scmContractInvoiceConditionsMap = new HashMap<String, Object>();
                                scmContractInvoiceConditionsMap.put(ScmContractInvoiceAttribute.FAD_INVOICE_ID, isNullReturnEmpty(fadInvoice.getUuid()));
                                List<ScmContractInvoice> scmContractInvoiceList = isNullReturnEmpty(fadInvoice.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScmContractInvoice.class, scmContractInvoiceConditionsMap, null) : new ArrayList<>();
                                if (scmContractInvoiceList.size() > 0) {
                                    for (int yy = 0; yy < scmContractInvoiceList.size(); yy++) {
                                        ScmContractInvoice scmContractInvoice = scmContractInvoiceList.get(yy);
                                        ScmContract scmContract = isNullReturnEmpty(scmContractInvoice.getScmContractId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmContract.class, isNullReturnEmpty(scmContractInvoice.getScmContractId())) : null;
                                        if (scmContract != null) {
                                            if ((isNullReturnEmpty(scmContract.getScmContractCode()).length() > 0) && (isNullReturnEmpty(fadCertificateDetail.getOriginalFormCode()).indexOf(isNullReturnEmpty(scmContract.getScmContractCode())) != -1)) {
                                                if (isNullReturnEmpty(scmContract.getIsProject()).equals("1")) {
                                                    PrmProjectMain prmProjectMain = isNullReturnEmpty(scmContract.getProjectId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(scmContract.getProjectId())) : null;
                                                    if (prmProjectMain != null) {
                                                        if (!(isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0)) {
                                                            projectCodeOrSubjectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                                                        } else {
                                                            projectCodeOrSubjectCode = isNullReturnEmpty(projectCodeOrSubjectCode) + MergeSplitSymbol + isNullReturnEmpty(prmProjectMain.getProjectCode());
                                                        }
                                                        if (!(isNullReturnEmpty(projectDepartmentCodeOrSubjectDepartmentCode).length() > 0)) {
                                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                                        } else {
                                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(projectDepartmentCodeOrSubjectDepartmentCode) + MergeSplitSymbol + isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                                        }
                                                    }
                                                } else {
                                                    if (!(isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0)) {
                                                        projectCodeOrSubjectCode = isNullReturnEmpty(scmContract.getSubjectCode());
                                                    } else {
                                                        projectCodeOrSubjectCode = isNullReturnEmpty(projectCodeOrSubjectCode) + MergeSplitSymbol + isNullReturnEmpty(scmContract.getSubjectCode());
                                                    }

                                                    Map<String, Object> nonProjectSubjectSubjectConditionsMap = new HashMap<String, Object>();
                                                    nonProjectSubjectSubjectConditionsMap.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, isNullReturnEmpty(scmContract.getSubjectCode()));
                                                    List nonProjectSubjectSubjectList = isNullReturnEmpty(scmContract.getSubjectCode()).length() > 0 ? DtoHelper.findByAnyFields(NonProjectSubjectSubjectDto.class, nonProjectSubjectSubjectConditionsMap, null) : new ArrayList<>();
                                                    if (nonProjectSubjectSubjectList.size() > 0) {
                                                        NonProjectSubjectSubjectDto nonProjectSubjectSubject = (NonProjectSubjectSubjectDto) nonProjectSubjectSubjectList.get(0);
                                                        if (!(isNullReturnEmpty(projectDepartmentCodeOrSubjectDepartmentCode).length() > 0)) {
                                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                                        } else {
                                                            projectDepartmentCodeOrSubjectDepartmentCode = projectDepartmentCodeOrSubjectDepartmentCode + MergeSplitSymbol + isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if (isNullReturnEmpty(fadInvoice.getPrmProjectMainId()).length() > 0) {
                                        PrmProjectMain prmProjectMain = isNullReturnEmpty(fadInvoice.getPrmProjectMainId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(fadInvoice.getPrmProjectMainId())) : null;
                                        if (prmProjectMain != null) {
                                            projectCodeOrSubjectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                        }
                                    } else {
                                        projectCodeOrSubjectCode = isNullReturnEmpty(fadInvoice.getSubjectCode());

                                        Map<String, Object> nonProjectSubjectSubjectConditionsMap = new HashMap<String, Object>();
                                        nonProjectSubjectSubjectConditionsMap.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, projectCodeOrSubjectCode);
                                        List nonProjectSubjectSubjectList = isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0 ? DtoHelper.findByAnyFields(NonProjectSubjectSubjectDto.class, nonProjectSubjectSubjectConditionsMap, null) : new ArrayList<>();
                                        if (nonProjectSubjectSubjectList.size() > 0) {
                                            NonProjectSubjectSubjectDto nonProjectSubjectSubject = (NonProjectSubjectSubjectDto) nonProjectSubjectSubjectList.get(0);
                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                        }
                                    }
                                }
                            }

                            //8.采购合同成本确认
                            else if (originalFormType.equals("8")) {

                                //【报销人】取得
                                UserId = isNullReturnEmpty(fadInvoice.getRenderPerson());

                                //【项目代号】或【费用科目代号】取得
                                //【项目部门代号】或【费用科目部门代号】取得
                                //String projectCodeOrSubjectCode = "";
                                //String projectDepartmentCodeOrSubjectDepartmentCode = "";
                                Map<String, Object> scmContractInvoiceConditionsMap = new HashMap<String, Object>();
                                scmContractInvoiceConditionsMap.put(ScmContractInvoiceAttribute.FAD_INVOICE_ID, isNullReturnEmpty(fadInvoice.getUuid()));
                                List<ScmContractInvoice> scmContractInvoiceList = isNullReturnEmpty(fadInvoice.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScmContractInvoice.class, scmContractInvoiceConditionsMap, null) : new ArrayList<>();
                                if (scmContractInvoiceList.size() > 0) {
                                    for (int yy = 0; yy < scmContractInvoiceList.size(); yy++) {
                                        ScmContractInvoice scmContractInvoice = scmContractInvoiceList.get(yy);
                                        ScmContract scmContract = isNullReturnEmpty(scmContractInvoice.getScmContractId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmContract.class, isNullReturnEmpty(scmContractInvoice.getScmContractId())) : null;
                                        if (scmContract != null) {
                                            if ((isNullReturnEmpty(scmContract.getScmContractCode()).length() > 0) && (isNullReturnEmpty(fadCertificateDetail.getOriginalFormCode()).indexOf(isNullReturnEmpty(scmContract.getScmContractCode())) != -1)) {
                                                if (isNullReturnEmpty(scmContract.getIsProject()).equals("1")) {
                                                    PrmProjectMain prmProjectMain = isNullReturnEmpty(scmContract.getProjectId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(scmContract.getProjectId())) : null;
                                                    if (prmProjectMain != null) {
                                                        if (!(isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0)) {
                                                            projectCodeOrSubjectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                                                        } else {
                                                            projectCodeOrSubjectCode = isNullReturnEmpty(projectCodeOrSubjectCode) + MergeSplitSymbol + isNullReturnEmpty(prmProjectMain.getProjectCode());
                                                        }
                                                        if (!(isNullReturnEmpty(projectDepartmentCodeOrSubjectDepartmentCode).length() > 0)) {
                                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                                        } else {
                                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(projectDepartmentCodeOrSubjectDepartmentCode) + MergeSplitSymbol + isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                                        }
                                                    }
                                                } else {
                                                    if (!(isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0)) {
                                                        projectCodeOrSubjectCode = isNullReturnEmpty(scmContract.getSubjectCode());
                                                    } else {
                                                        projectCodeOrSubjectCode = isNullReturnEmpty(projectCodeOrSubjectCode) + MergeSplitSymbol + isNullReturnEmpty(scmContract.getSubjectCode());
                                                    }

                                                    Map<String, Object> nonProjectSubjectSubjectConditionsMap = new HashMap<String, Object>();
                                                    nonProjectSubjectSubjectConditionsMap.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, isNullReturnEmpty(scmContract.getSubjectCode()));
                                                    List nonProjectSubjectSubjectList = isNullReturnEmpty(scmContract.getSubjectCode()).length() > 0 ? DtoHelper.findByAnyFields(NonProjectSubjectSubjectDto.class, nonProjectSubjectSubjectConditionsMap, null) : new ArrayList<>();
                                                    if (nonProjectSubjectSubjectList.size() > 0) {
                                                        NonProjectSubjectSubjectDto nonProjectSubjectSubject = (NonProjectSubjectSubjectDto) nonProjectSubjectSubjectList.get(0);
                                                        if (!(isNullReturnEmpty(projectDepartmentCodeOrSubjectDepartmentCode).length() > 0)) {
                                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                                        } else {
                                                            projectDepartmentCodeOrSubjectDepartmentCode = projectDepartmentCodeOrSubjectDepartmentCode + MergeSplitSymbol + isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if (isNullReturnEmpty(fadInvoice.getPrmProjectMainId()).length() > 0) {
                                        PrmProjectMain prmProjectMain = isNullReturnEmpty(fadInvoice.getPrmProjectMainId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(fadInvoice.getPrmProjectMainId())) : null;
                                        if (prmProjectMain != null) {
                                            projectCodeOrSubjectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                        }
                                    } else {
                                        projectCodeOrSubjectCode = isNullReturnEmpty(fadInvoice.getSubjectCode());

                                        Map<String, Object> nonProjectSubjectSubjectConditionsMap = new HashMap<String, Object>();
                                        nonProjectSubjectSubjectConditionsMap.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, projectCodeOrSubjectCode);
                                        List nonProjectSubjectSubjectList = isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0 ? DtoHelper.findByAnyFields(NonProjectSubjectSubjectDto.class, nonProjectSubjectSubjectConditionsMap, null) : new ArrayList<>();
                                        if (nonProjectSubjectSubjectList.size() > 0) {
                                            NonProjectSubjectSubjectDto nonProjectSubjectSubject = (NonProjectSubjectSubjectDto) nonProjectSubjectSubjectList.get(0);
                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                        }
                                    }
                                }
                            }

                            //9.采购合同支付直接支付
                            else if (originalFormType.equals("9")) {

                                //【报销人】取得
                                UserId = isNullReturnEmpty(fadInvoice.getRenderPerson());

                                //【项目代号】或【费用科目代号】取得
                                //【项目部门代号】或【费用科目部门代号】取得
                                //String projectCodeOrSubjectCode = "";
                                //String projectDepartmentCodeOrSubjectDepartmentCode = "";
                                Map<String, Object> scmContractInvoiceConditionsMap = new HashMap<String, Object>();
                                scmContractInvoiceConditionsMap.put(ScmContractInvoiceAttribute.FAD_INVOICE_ID, isNullReturnEmpty(fadInvoice.getUuid()));
                                List<ScmContractInvoice> scmContractInvoiceList = isNullReturnEmpty(fadInvoice.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScmContractInvoice.class, scmContractInvoiceConditionsMap, null) : new ArrayList<>();
                                if (scmContractInvoiceList.size() > 0) {
                                    for (int yy = 0; yy < scmContractInvoiceList.size(); yy++) {
                                        ScmContractInvoice scmContractInvoice = scmContractInvoiceList.get(yy);
                                        ScmContract scmContract = isNullReturnEmpty(scmContractInvoice.getScmContractId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmContract.class, isNullReturnEmpty(scmContractInvoice.getScmContractId())) : null;
                                        if (scmContract != null) {
                                            if ((isNullReturnEmpty(scmContract.getScmContractCode()).length() > 0) && (isNullReturnEmpty(fadCertificateDetail.getOriginalFormCode()).indexOf(isNullReturnEmpty(scmContract.getScmContractCode())) != -1)) {
                                                if (isNullReturnEmpty(scmContract.getIsProject()).equals("1")) {
                                                    PrmProjectMain prmProjectMain = isNullReturnEmpty(scmContract.getProjectId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(scmContract.getProjectId())) : null;
                                                    if (prmProjectMain != null) {
                                                        if (!(isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0)) {
                                                            projectCodeOrSubjectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                                                        } else {
                                                            projectCodeOrSubjectCode = isNullReturnEmpty(projectCodeOrSubjectCode) + MergeSplitSymbol + isNullReturnEmpty(prmProjectMain.getProjectCode());
                                                        }
                                                        if (!(isNullReturnEmpty(projectDepartmentCodeOrSubjectDepartmentCode).length() > 0)) {
                                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                                        } else {
                                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(projectDepartmentCodeOrSubjectDepartmentCode) + MergeSplitSymbol + isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                                        }
                                                    }
                                                } else {
                                                    if (!(isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0)) {
                                                        projectCodeOrSubjectCode = isNullReturnEmpty(scmContract.getSubjectCode());
                                                    } else {
                                                        projectCodeOrSubjectCode = isNullReturnEmpty(projectCodeOrSubjectCode) + MergeSplitSymbol + isNullReturnEmpty(scmContract.getSubjectCode());
                                                    }

                                                    Map<String, Object> nonProjectSubjectSubjectConditionsMap = new HashMap<String, Object>();
                                                    nonProjectSubjectSubjectConditionsMap.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, isNullReturnEmpty(scmContract.getSubjectCode()));
                                                    List nonProjectSubjectSubjectList = isNullReturnEmpty(scmContract.getSubjectCode()).length() > 0 ? DtoHelper.findByAnyFields(NonProjectSubjectSubjectDto.class, nonProjectSubjectSubjectConditionsMap, null) : new ArrayList<>();
                                                    if (nonProjectSubjectSubjectList.size() > 0) {
                                                        NonProjectSubjectSubjectDto nonProjectSubjectSubject = (NonProjectSubjectSubjectDto) nonProjectSubjectSubjectList.get(0);
                                                        if (!(isNullReturnEmpty(projectDepartmentCodeOrSubjectDepartmentCode).length() > 0)) {
                                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                                        } else {
                                                            projectDepartmentCodeOrSubjectDepartmentCode = projectDepartmentCodeOrSubjectDepartmentCode + MergeSplitSymbol + isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if (isNullReturnEmpty(fadInvoice.getPrmProjectMainId()).length() > 0) {
                                        PrmProjectMain prmProjectMain = isNullReturnEmpty(fadInvoice.getPrmProjectMainId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(fadInvoice.getPrmProjectMainId())) : null;
                                        if (prmProjectMain != null) {
                                            projectCodeOrSubjectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                        }
                                    } else {
                                        projectCodeOrSubjectCode = isNullReturnEmpty(fadInvoice.getSubjectCode());

                                        Map<String, Object> nonProjectSubjectSubjectConditionsMap = new HashMap<String, Object>();
                                        nonProjectSubjectSubjectConditionsMap.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, projectCodeOrSubjectCode);
                                        List nonProjectSubjectSubjectList = isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0 ? DtoHelper.findByAnyFields(NonProjectSubjectSubjectDto.class, nonProjectSubjectSubjectConditionsMap, null) : new ArrayList<>();
                                        if (nonProjectSubjectSubjectList.size() > 0) {
                                            NonProjectSubjectSubjectDto nonProjectSubjectSubject = (NonProjectSubjectSubjectDto) nonProjectSubjectSubjectList.get(0);
                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                        }
                                    }
                                }
                            }
                        }
                    } else if (originalFormTypeForeign.equals("1")) {

                        //1.FAD_CASH_REQ(请款)
                        FadCashReq fadCashReq = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadCashReq.class, businessId) : null;
                        if (fadCashReq != null) {

                            //1.个人请款(1日常2差旅)
                            if (originalFormType.equals("1")) {

                                //【请款人】取得
                                UserId = isNullReturnEmpty(fadCashReq.getStaffId());
                                String userName = "";
                                Map<String, Object> scdpUserConditionsMap = new HashMap<String, Object>();
                                scdpUserConditionsMap.put(ScdpUserAttribute.USER_ID, UserId);
                                List<ScdpUser> scdpUserList = isNullReturnEmpty(UserId).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, scdpUserConditionsMap, null) : new ArrayList<>();
                                if (scdpUserList.size() > 0) {
                                    ScdpUser scdpUser = (ScdpUser) scdpUserList.get(0);
                                    userName = isNullReturnEmpty(scdpUser.getUserName());
                                }

                                //【项目代号】或【费用科目代号】取得
                                //【项目部门代号】或【费用科目部门代号】取得
                                //String projectCodeOrSubjectCode = "";
                                //String projectDepartmentCodeOrSubjectDepartmentCode = "";
                                if (isNullReturnEmpty(fadCashReq.getIsProject()).equals("1")) {
                                    PrmProjectMain prmProjectMain = isNullReturnEmpty(fadCashReq.getProjectId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(fadCashReq.getProjectId())) : null;
                                    if (prmProjectMain != null) {
                                        projectCodeOrSubjectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                                        projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                    }
                                } else {
                                    projectCodeOrSubjectCode = isNullReturnEmpty(fadCashReq.getSubjectCode());

                                    Map<String, Object> nonProjectSubjectSubjectConditionsMap = new HashMap<String, Object>();
                                    nonProjectSubjectSubjectConditionsMap.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, projectCodeOrSubjectCode);
                                    List nonProjectSubjectSubjectList = isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0 ? DtoHelper.findByAnyFields(NonProjectSubjectSubjectDto.class, nonProjectSubjectSubjectConditionsMap, null) : new ArrayList<>();
                                    if (nonProjectSubjectSubjectList.size() > 0) {
                                        NonProjectSubjectSubjectDto nonProjectSubjectSubject = (NonProjectSubjectSubjectDto) nonProjectSubjectSubjectList.get(0);
                                        projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                    }
                                }

                                //【项目部门名称】或【费用科目部门名称】取得
                                String projectDepartmentCodeOrSubjectDepartmentName = "";
                                Map<String, Object> scdpOrgConditionsMap = new HashMap<String, Object>();
                                scdpOrgConditionsMap.put(ScdpOrgAttribute.ORG_CODE, projectDepartmentCodeOrSubjectDepartmentCode);
                                List<ScdpOrg> scdpOrgList = isNullReturnEmpty(projectDepartmentCodeOrSubjectDepartmentCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpOrg.class, scdpOrgConditionsMap, null) : new ArrayList<>();
                                if (scdpOrgList.size() > 0) {
                                    ScdpOrg scdpOrg = (ScdpOrg) scdpOrgList.get(0);
                                    projectDepartmentCodeOrSubjectDepartmentName = isNullReturnEmpty(scdpOrg.getOrgName());
                                }
                            }

                            //2.材料费或外包商请款(0采购合同)
                            else if (originalFormType.equals("2")) {

                                //【请款人】取得
                                UserId = isNullReturnEmpty(fadCashReq.getStaffId());

                                //【项目代号】或【费用科目代号】取得
                                //【项目部门代号】或【费用科目部门代号】取得
                                //【费用科目内容】取得
                                //String projectCodeOrSubjectCode = "";
                                //String projectDepartmentCodeOrSubjectDepartmentCode = "";
                                ScmContract scmContract = isNullReturnEmpty(fadCashReq.getPurchaseContractId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmContract.class, isNullReturnEmpty(fadCashReq.getPurchaseContractId())) : null;
                                if (scmContract != null) {
                                    if (isNullReturnEmpty(scmContract.getIsProject()).equals("1")) {
                                        PrmProjectMain prmProjectMain = isNullReturnEmpty(scmContract.getProjectId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(scmContract.getProjectId())) : null;
                                        if (prmProjectMain != null) {
                                            projectCodeOrSubjectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                        }

                                        String sql = "SELECT SUBJECT_NAME FROM FAD_BASE_SUBJECT WHERE SUBJECT_NO = '" + isNullReturnEmpty(scmContract.getSubjectCode()) + "'";
                                        DAOMeta daoMeta = new DAOMeta();
                                        daoMeta.setStrSql(sql);
                                        daoMeta.setNeedFilter(false);
                                        List fadBaseSubjectList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                                    } else {
                                        projectCodeOrSubjectCode = isNullReturnEmpty(scmContract.getSubjectCode());

                                        Map<String, Object> nonProjectSubjectSubjectConditionsMap = new HashMap<String, Object>();
                                        nonProjectSubjectSubjectConditionsMap.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, projectCodeOrSubjectCode);
                                        List nonProjectSubjectSubjectList = isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0 ? DtoHelper.findByAnyFields(NonProjectSubjectSubjectDto.class, nonProjectSubjectSubjectConditionsMap, null) : new ArrayList<>();
                                        if (nonProjectSubjectSubjectList.size() > 0) {
                                            NonProjectSubjectSubjectDto nonProjectSubjectSubject = (NonProjectSubjectSubjectDto) nonProjectSubjectSubjectList.get(0);
                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                        }
                                    }
                                }
                            }

                            //3.保证金请款(3保证金)
                            else if (originalFormType.equals("3")) {

                                //【请款人】取得
                                UserId = isNullReturnEmpty(fadCashReq.getStaffId());
                            }
                            //周转金请款
                            else if (originalFormType.equals("14")) {

                                //【请款人】取得
                                UserId = isNullReturnEmpty(fadCashReq.getStaffId());
                            }
                        }
                    } else if (originalFormTypeForeign.equals("2")) {

                        //2.FAD_PAY_REQ_C(支付)【提示:请用王兆刚的ERP账号登录测试(支付)的生成凭证!】
                        FadPayReqC fadPayReqC = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadPayReqC.class, businessId) : null;
                        if (fadPayReqC != null) {

                            //10.采购合同支付
                            if (originalFormType.equals("10")) {

                                //【项目代号】或【费用科目代号】取得
                                //String projectCodeOrSubjectCode = "";
                                //String projectDepartmentCodeOrSubjectDepartmentCode = "";
                                ScmContract scmContract = isNullReturnEmpty(fadPayReqC.getScmContractId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmContract.class, isNullReturnEmpty(fadPayReqC.getScmContractId())) : null;
                                if (scmContract != null) {
                                    if (isNullReturnEmpty(scmContract.getIsProject()).equals("1")) {
                                        PrmProjectMain prmProjectMain = isNullReturnEmpty(scmContract.getProjectId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(scmContract.getProjectId())) : null;
                                        if (prmProjectMain != null) {
                                            projectCodeOrSubjectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                        }
                                    } else {
                                        projectCodeOrSubjectCode = isNullReturnEmpty(scmContract.getSubjectCode());

                                        Map<String, Object> nonProjectSubjectSubjectConditionsMap = new HashMap<String, Object>();
                                        nonProjectSubjectSubjectConditionsMap.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, projectCodeOrSubjectCode);
                                        List nonProjectSubjectSubjectList = isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0 ? DtoHelper.findByAnyFields(NonProjectSubjectSubjectDto.class, nonProjectSubjectSubjectConditionsMap, null) : new ArrayList<>();
                                        if (nonProjectSubjectSubjectList.size() > 0) {
                                            NonProjectSubjectSubjectDto nonProjectSubjectSubject = (NonProjectSubjectSubjectDto) nonProjectSubjectSubjectList.get(0);
                                            projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                        }
                                    }
                                }
                            }
                        }
                    } else if (originalFormTypeForeign.equals("3")) {

                        //3.FAD_CASH_CLEARANCE(还款)
                        FadCashClearance fadCashClearance = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadCashClearance.class, businessId) : null;
                        if (fadCashClearance != null) {

                            //11.还款
                            if (originalFormType.equals("11")) {

                                //【还款人】取得
                                //【还款人部门名称】取得
                                UserId = isNullReturnEmpty(fadCashClearance.getClearancePerson());
                                String userName = "";
                                String DepartmentName = "";
                                Map<String, Object> scdpUserConditionsMap = new HashMap<String, Object>();
                                scdpUserConditionsMap.put(ScdpUserAttribute.USER_ID, UserId);
                                List<ScdpUser> scdpUserList = isNullReturnEmpty(UserId).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, scdpUserConditionsMap, null) : new ArrayList<>();
                                if (scdpUserList.size() > 0) {
                                    ScdpUser scdpUser = (ScdpUser) scdpUserList.get(0);
                                    userName = isNullReturnEmpty(scdpUser.getUserName());

                                    ScdpOrg scdpOrg = isNullReturnEmpty(scdpUser.getOrgUuid()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScdpOrg.class, isNullReturnEmpty(scdpUser.getOrgUuid())) : null;
                                    if (scdpOrg != null) {
                                        DepartmentName = isNullReturnEmpty(scdpOrg.getOrgName());
                                    }
                                }

                                //【项目代号】或【费用科目代号】取得
                                //【项目部门代号】或【费用科目部门代号】取得
                                //【费用科目内容】取得
                                //String projectCodeOrSubjectCode = "";
                                //String projectDepartmentCodeOrSubjectDepartmentCode = "";
                                Map<String, Object> fadCashReqInvoiceConditionsMap = new HashMap<String, Object>();
                                fadCashReqInvoiceConditionsMap.put(FadCashReqInvoiceAttribute.FAD_INVOICE_ID, isNullReturnEmpty(fadCashClearance.getUuid()));
                                List<FadCashReqInvoice> fadCashReqInvoiceList = isNullReturnEmpty(fadCashClearance.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCashReqInvoice.class, fadCashReqInvoiceConditionsMap, null) : new ArrayList<>();
                                if (fadCashReqInvoiceList.size() > 0) {
                                    FadCashReqInvoice fadCashReqInvoice = fadCashReqInvoiceList.get(0);
                                    FadCashReq fadCashReq = isNullReturnEmpty(fadCashReqInvoice.getFadCashReqId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadCashReq.class, isNullReturnEmpty(fadCashReqInvoice.getFadCashReqId())) : null;
                                    if (fadCashReq != null) {

                                        //采购还款
                                        if (isNullReturnEmpty(fadCashReq.getReqType()).equals("0")) {

                                            //【项目代号】或【费用科目代号】取得
                                            //【费用科目内容】取得
                                            ScmContract scmContract = isNullReturnEmpty(fadCashReq.getPurchaseContractId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmContract.class, isNullReturnEmpty(fadCashReq.getPurchaseContractId())) : null;
                                            if (scmContract != null) {
                                                if (isNullReturnEmpty(scmContract.getIsProject()).equals("1")) {
                                                    PrmProjectMain prmProjectMain = isNullReturnEmpty(scmContract.getProjectId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(scmContract.getProjectId())) : null;
                                                    if (prmProjectMain != null) {
                                                        projectCodeOrSubjectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                                                        //projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                                    }

                                                    String sql = "SELECT SUBJECT_NAME FROM FAD_BASE_SUBJECT WHERE SUBJECT_NO = '" + isNullReturnEmpty(scmContract.getSubjectCode()) + "'";
                                                    DAOMeta daoMeta = new DAOMeta();
                                                    daoMeta.setStrSql(sql);
                                                    daoMeta.setNeedFilter(false);
                                                    List fadBaseSubjectList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                                                } else {
                                                    projectCodeOrSubjectCode = isNullReturnEmpty(scmContract.getSubjectCode());

                                                    Map<String, Object> nonProjectSubjectSubjectConditionsMap = new HashMap<String, Object>();
                                                    nonProjectSubjectSubjectConditionsMap.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, projectCodeOrSubjectCode);
                                                    List nonProjectSubjectSubjectList = isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0 ? DtoHelper.findByAnyFields(NonProjectSubjectSubjectDto.class, nonProjectSubjectSubjectConditionsMap, null) : new ArrayList<>();
                                                    if (nonProjectSubjectSubjectList.size() > 0) {
                                                        NonProjectSubjectSubjectDto nonProjectSubjectSubject = (NonProjectSubjectSubjectDto) nonProjectSubjectSubjectList.get(0);
                                                        //projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                                    }
                                                }
                                            }
                                        }

                                        //其他还款
                                        else {
                                            //【项目代号】或【费用科目代号】取得
                                            //【项目部门代号】或【费用科目部门代号】取得
                                            if (isNullReturnEmpty(fadCashReq.getIsProject()).equals("1")) {
                                                PrmProjectMain prmProjectMain = isNullReturnEmpty(fadCashReq.getProjectId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(fadCashReq.getProjectId())) : null;
                                                if (prmProjectMain != null) {
                                                    projectCodeOrSubjectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                                                    //projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                                }
                                            } else {
                                                projectCodeOrSubjectCode = isNullReturnEmpty(fadCashReq.getSubjectCode());

                                                Map<String, Object> nonProjectSubjectSubjectConditionsMap = new HashMap<String, Object>();
                                                nonProjectSubjectSubjectConditionsMap.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, projectCodeOrSubjectCode);
                                                List nonProjectSubjectSubjectList = isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0 ? DtoHelper.findByAnyFields(NonProjectSubjectSubjectDto.class, nonProjectSubjectSubjectConditionsMap, null) : new ArrayList<>();
                                                if (nonProjectSubjectSubjectList.size() > 0) {
                                                    NonProjectSubjectSubjectDto nonProjectSubjectSubject = (NonProjectSubjectSubjectDto) nonProjectSubjectSubjectList.get(0);
                                                    //projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(nonProjectSubjectSubject.getOfficeId());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else if (originalFormTypeForeign.equals("4")) {

                        //4.PRM_RECEIPTS(收款)
                        PrmReceipts prmReceipts = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmReceipts.class, businessId) : null;
                        if (prmReceipts != null) {

                            //12.收款
                            if (originalFormType.equals("12") || originalFormType.equals("12.1") || originalFormType.equals("12.2")) {

                                //【项目代号】或【费用科目代号】取得
                                //【项目部门代号】或【费用科目部门代号】取得
                                //String projectCodeOrSubjectCode = "";
                                //String projectDepartmentCodeOrSubjectDepartmentCode = "";
                                if (isNullReturnEmpty(prmReceipts.getPrmProjectMainId()).length() > 0) {
                                    PrmProjectMain prmProjectMain = isNullReturnEmpty(prmReceipts.getPrmProjectMainId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(prmReceipts.getPrmProjectMainId())) : null;
                                    if (prmProjectMain != null) {
                                        projectCodeOrSubjectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                                        projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                    }
                                }
                            }
                        }
                    } else if (originalFormTypeForeign.equals("5")) {

                        //5.PRM_BILLING(开票)
                        PrmBilling prmBilling = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmBilling.class, businessId) : null;
                        if (prmBilling != null) {

                            //13.开票
                            if (originalFormType.equals("13")) {

                                //【项目代号】或【费用科目代号】取得
                                //【项目部门代号】或【费用科目部门代号】取得
                                //String projectCodeOrSubjectCode = "";
                                //String projectDepartmentCodeOrSubjectDepartmentCode = "";
                                if (isNullReturnEmpty(prmBilling.getPrmProjectMainId()).length() > 0) {
                                    PrmProjectMain prmProjectMain = isNullReturnEmpty(prmBilling.getPrmProjectMainId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(prmBilling.getPrmProjectMainId())) : null;
                                    if (prmProjectMain != null) {
                                        projectCodeOrSubjectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                                        projectDepartmentCodeOrSubjectDepartmentCode = isNullReturnEmpty(prmProjectMain.getDepartmentCode());
                                    }
                                }
                            }
                        }
                    }

                    //根据科目编码取得相关核算项
                    Map<String, Object> fadAccsubjRtfreevalueConditionsMap = new HashMap<String, Object>();
                    fadAccsubjRtfreevalueConditionsMap.put(FadAccsubjRtfreevalueAttribute.SUBJECT_CODE, isNullReturnEmpty(FinanceSubjectCode));
                    List<FadAccsubjRtfreevalue> fadAccsubjRtfreevalueList = isNullReturnEmpty(FinanceSubjectCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadAccsubjRtfreevalue.class, fadAccsubjRtfreevalueConditionsMap, "to_number(uuid) asc") : new ArrayList<>();
                    for (int yy = 0; yy < fadAccsubjRtfreevalueList.size(); yy++) {
                        FadAccsubjRtfreevalue fadAccsubjRtfreevalue = (FadAccsubjRtfreevalue) fadAccsubjRtfreevalueList.get(yy);
                        String accountNo = isNullReturnEmpty(fadAccsubjRtfreevalue.getAccountNo());

                        if (isNullReturnEmpty(accountNo).length() > 0) {
                            FadCertificateAccount fadCertificateAccount = BeanFactory.getObject(FadCertificateAccount.class);
                            fadCertificateAccount.setUuid(null);
                            fadCertificateAccount.setFadCertificateDetailId(isNullReturnEmpty(fadCertificateDetail.getUuid()));
                            fadCertificateAccount.setAccountNo(isNullReturnEmpty(accountNo));
                            fadCertificateAccount.setAccountType(isNullReturnEmpty(fadAccsubjRtfreevalue.getAccountType()));

                            //对方类型为SCDP_USER用户
                            if (isNullReturnEmpty(fadCertificate.getReceiverOrPayerType()).equals("SCDP_USER")) {

                                //辅助核算类型为用户
                                if (isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("1")) {
                                    Map<String, Object> scdpUserConditionsMap = new HashMap<String, Object>();
                                    scdpUserConditionsMap.put(ScdpUserAttribute.USER_ID, isNullReturnEmpty(fadCertificate.getReceiverOrPayerCode()));
                                    List<ScdpUser> scdpUserList = isNullReturnEmpty(fadCertificate.getReceiverOrPayerCode()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, scdpUserConditionsMap, null) : new ArrayList<>();
                                    if (scdpUserList.size() > 0) {
                                        ScdpUser scdpUser = (ScdpUser) scdpUserList.get(0);
                                        fadCertificateAccount.setAccountInfoId(isNullReturnEmpty(scdpUser.getUuid()));
                                        ScdpOrg scdpOrg = isNullReturnEmpty(scdpUser.getOrgUuid()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScdpOrg.class, isNullReturnEmpty(scdpUser.getOrgUuid())) : null;
                                        if (scdpOrg != null) {
                                            if (isNullReturnEmpty(scdpOrg.getOrgCode()).length() > 0) {
                                                fadCertificateAccount.setAccountInfoCode(isNullReturnEmpty(scdpUser.getUserId()) + "-" + isNullReturnEmpty(scdpOrg.getOrgCode()));
                                            } else {
                                                fadCertificateAccount.setAccountInfoCode(isNullReturnEmpty(scdpUser.getUserId()));
                                            }
                                            if (isNullReturnEmpty(scdpOrg.getOrgName()).length() > 0) {
                                                fadCertificateAccount.setAccountInfoName(isNullReturnEmpty(scdpUser.getUserName()) + "-" + isNullReturnEmpty(scdpOrg.getOrgName()));
                                            } else {
                                                fadCertificateAccount.setAccountInfoName(isNullReturnEmpty(scdpUser.getUserName()));
                                            }
                                        } else {
                                            fadCertificateAccount.setAccountInfoCode(isNullReturnEmpty(scdpUser.getUserId()));
                                            fadCertificateAccount.setAccountInfoName(isNullReturnEmpty(scdpUser.getUserName()));
                                        }
                                    }
                                }

                                //辅助核算类型为部门
                                else if (isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("2")) {
                                    Map<String, Object> scdpUserConditionsMap = new HashMap<String, Object>();
                                    scdpUserConditionsMap.put(ScdpUserAttribute.USER_ID, isNullReturnEmpty(fadCertificate.getReceiverOrPayerCode()));
                                    List<ScdpUser> scdpUserList = isNullReturnEmpty(fadCertificate.getReceiverOrPayerCode()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, scdpUserConditionsMap, null) : new ArrayList<>();
                                    if (scdpUserList.size() > 0) {
                                        ScdpUser scdpUser = (ScdpUser) scdpUserList.get(0);
                                        ScdpOrg scdpOrg = isNullReturnEmpty(scdpUser.getOrgUuid()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScdpOrg.class, isNullReturnEmpty(scdpUser.getOrgUuid())) : null;
                                        if (scdpOrg != null) {
                                            fadCertificateAccount.setAccountInfoId(isNullReturnEmpty(scdpOrg.getUuid()));
                                            fadCertificateAccount.setAccountInfoCode(isNullReturnEmpty(scdpOrg.getOrgCode()));
                                            fadCertificateAccount.setAccountInfoName(isNullReturnEmpty(scdpOrg.getOrgName()));
                                        }
                                    }
                                }
                            }

                            //对方类型为SCM_SUPPLIER供应商
                            else if (isNullReturnEmpty(fadCertificate.getReceiverOrPayerType()).equals("SCM_SUPPLIER")) {

                                //辅助核算类型为客商、内部客商、外部客商
                                if (
                                        isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("73")
                                                || isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("NEIBU")
                                                || isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("WAIBU")
                                        ) {
                                    ScmSupplier scmSupplier = isNullReturnEmpty(fadCertificate.getReceiverOrPayerId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmSupplier.class, isNullReturnEmpty(fadCertificate.getReceiverOrPayerId())) : null;
                                    if (scmSupplier != null) {
                                        fadCertificateAccount.setAccountInfoId(isNullReturnEmpty(scmSupplier.getUuid()));
                                        fadCertificateAccount.setAccountInfoCode(isNullReturnEmpty(scmSupplier.getSupplierCode()));
                                        fadCertificateAccount.setAccountInfoName(isNullReturnEmpty(scmSupplier.getCompleteName()));
                                    } else {
                                        fadCertificateAccount.setAccountInfoName(isNullReturnEmpty(fadCertificate.getReceiverOrPayerName()));
                                    }
                                }
                            }

                            //对方类型为PRM_CUSTOMER客户
                            else if (isNullReturnEmpty(fadCertificate.getReceiverOrPayerType()).equals("PRM_CUSTOMER")) {

                                //辅助核算类型为客商、内部客商、外部客商
                                if (
                                        isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("73")
                                                || isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("NEIBU")
                                                || isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("WAIBU")
                                        ) {
                                    PrmCustomer prmCustomer = isNullReturnEmpty(fadCertificate.getReceiverOrPayerId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmCustomer.class, isNullReturnEmpty(fadCertificate.getReceiverOrPayerId())) : null;
                                    if (prmCustomer != null) {
                                        fadCertificateAccount.setAccountInfoId(isNullReturnEmpty(prmCustomer.getUuid()));
                                        fadCertificateAccount.setAccountInfoCode(isNullReturnEmpty(prmCustomer.getCustomerCode()));
                                        fadCertificateAccount.setAccountInfoName(isNullReturnEmpty(prmCustomer.getCustomerName()));
                                    }
                                }
                            }

                            //收款(调整)代入指定辅助核算类型:收支类型、客商
                            if (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("12.2")) {

                                //收支类型
                                if (isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("JGG_CASS")) {
                                    fadCertificateAccount.setAccountInfoId(NCCodeDataSymbol);
                                    fadCertificateAccount.setAccountInfoCode("CLKJ98");
                                    fadCertificateAccount.setAccountInfoName("中海科技无辅助");
                                }

                                //客商
                                else if (
                                        isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("73")
                                                || isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("NEIBU")
                                                || isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("WAIBU")
                                        ) {
                                    fadCertificateAccount.setAccountInfoId(NCCodeDataSymbol);
                                    fadCertificateAccount.setAccountInfoCode("2LKW002416");
                                    fadCertificateAccount.setAccountInfoName("零星采购商");
                                }
                            }

                            //存在【项目代号】或【费用科目代号】
                            if (projectCodeOrSubjectCode.indexOf(MergeSplitSymbol) != -1) {
                                projectCodeOrSubjectCode = projectCodeOrSubjectCode.split(MergeSplitSymbol)[0];
                            }
                            if (isNullReturnEmpty(projectCodeOrSubjectCode).length() > 0) {

                                //辅助核算类型为科技项目
                                if (isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("JXX_JASS")) {
                                    String accountInfoName = projectCodeOrSubjectCode;
                                    if (isNullReturnEmpty(accountInfoName).length() > 0) {
                                        Map<String, Object> fadRtfreevalueViewConditionsMap = new HashMap<String, Object>();
                                        fadRtfreevalueViewConditionsMap.put(FadRtfreevalueViewAttribute.ACCOUNT_NO, isNullReturnEmpty(fadCertificateAccount.getAccountNo()));
                                        fadRtfreevalueViewConditionsMap.put(FadRtfreevalueViewAttribute.ACCOUNT_INFO_NAME, accountInfoName);
                                        List<FadRtfreevalueView> fadRtfreevalueViewList = ((isNullReturnEmpty(fadCertificateAccount.getAccountNo()).length() > 0) && (isNullReturnEmpty(accountInfoName).length() > 0)) ? PersistenceFactory.getInstance().findByAnyFields(FadRtfreevalueView.class, fadRtfreevalueViewConditionsMap, null) : new ArrayList<>();
                                        if (fadRtfreevalueViewList.size() > 0) {
                                            FadRtfreevalueView fadRtfreevalueView = (FadRtfreevalueView) fadRtfreevalueViewList.get(0);
                                            fadCertificateAccount.setAccountInfoId(isNullReturnEmpty(fadRtfreevalueView.getUuid()));
                                            fadCertificateAccount.setAccountInfoCode(isNullReturnEmpty(fadRtfreevalueView.getAccountInfoCode()));
                                        }
                                        fadCertificateAccount.setAccountInfoName(accountInfoName);
                                    }
                                }
                            }

                            //存在【项目部门代号】或【费用科目部门代号】
                            if (projectDepartmentCodeOrSubjectDepartmentCode.indexOf(MergeSplitSymbol) != -1) {
                                projectDepartmentCodeOrSubjectDepartmentCode = projectDepartmentCodeOrSubjectDepartmentCode.split(MergeSplitSymbol)[0];
                            }
                            if (isNullReturnEmpty(projectDepartmentCodeOrSubjectDepartmentCode).length() > 0) {

                                //辅助核算类型为部门
                                if (isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("2")) {
                                    Map<String, Object> scdpOrgConditionsMap = new HashMap<String, Object>();
                                    scdpOrgConditionsMap.put(ScdpOrgAttribute.ORG_CODE, projectDepartmentCodeOrSubjectDepartmentCode);
                                    List<ScdpOrg> scdpOrgList = isNullReturnEmpty(projectDepartmentCodeOrSubjectDepartmentCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpOrg.class, scdpOrgConditionsMap, null) : new ArrayList<>();
                                    if (scdpOrgList.size() > 0) {
                                        ScdpOrg scdpOrg = (ScdpOrg) scdpOrgList.get(0);
                                        fadCertificateAccount.setAccountInfoId(isNullReturnEmpty(scdpOrg.getUuid()));
                                        fadCertificateAccount.setAccountInfoCode(isNullReturnEmpty(scdpOrg.getOrgCode()));
                                        fadCertificateAccount.setAccountInfoName(isNullReturnEmpty(scdpOrg.getOrgName()));
                                    }
                                }
                            }

                            //存在【人员】
                            if (isNullReturnEmpty(UserId).length() > 0) {

                                //辅助核算类型为用户
                                if (isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("1")) {
                                    if (!(isNullReturnEmpty(fadCertificateAccount.getAccountInfoName()).length() > 0)) {
                                        Map<String, Object> scdpUserConditionsMap = new HashMap<String, Object>();
                                        scdpUserConditionsMap.put(ScdpUserAttribute.USER_ID, UserId);
                                        List<ScdpUser> scdpUserList = isNullReturnEmpty(UserId).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, scdpUserConditionsMap, null) : new ArrayList<>();
                                        if (scdpUserList.size() > 0) {
                                            ScdpUser scdpUser = (ScdpUser) scdpUserList.get(0);
                                            fadCertificateAccount.setAccountInfoId(isNullReturnEmpty(scdpUser.getUuid()));
                                            ScdpOrg scdpOrg = isNullReturnEmpty(scdpUser.getOrgUuid()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScdpOrg.class, isNullReturnEmpty(scdpUser.getOrgUuid())) : null;
                                            if (scdpOrg != null) {
                                                if (isNullReturnEmpty(scdpOrg.getOrgCode()).length() > 0) {
                                                    fadCertificateAccount.setAccountInfoCode(isNullReturnEmpty(scdpUser.getUserId()) + "-" + isNullReturnEmpty(scdpOrg.getOrgCode()));
                                                } else {
                                                    fadCertificateAccount.setAccountInfoCode(isNullReturnEmpty(scdpUser.getUserId()));
                                                }
                                                if (isNullReturnEmpty(scdpOrg.getOrgName()).length() > 0) {
                                                    fadCertificateAccount.setAccountInfoName(isNullReturnEmpty(scdpUser.getUserName()) + "-" + isNullReturnEmpty(scdpOrg.getOrgName()));
                                                } else {
                                                    fadCertificateAccount.setAccountInfoName(isNullReturnEmpty(scdpUser.getUserName()));
                                                }
                                            } else {
                                                fadCertificateAccount.setAccountInfoCode(isNullReturnEmpty(scdpUser.getUserId()));
                                                fadCertificateAccount.setAccountInfoName(isNullReturnEmpty(scdpUser.getUserName()));
                                            }
                                        }
                                    }
                                }

                                //辅助核算类型为部门
                                else if (isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("2")) {
                                    if (!(isNullReturnEmpty(fadCertificateAccount.getAccountInfoName()).length() > 0)) {
                                        Map<String, Object> scdpUserConditionsMap = new HashMap<String, Object>();
                                        scdpUserConditionsMap.put(ScdpUserAttribute.USER_ID, UserId);
                                        List<ScdpUser> scdpUserList = isNullReturnEmpty(UserId).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, scdpUserConditionsMap, null) : new ArrayList<>();
                                        if (scdpUserList.size() > 0) {
                                            ScdpUser scdpUser = (ScdpUser) scdpUserList.get(0);
                                            ScdpOrg scdpOrg = isNullReturnEmpty(scdpUser.getOrgUuid()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScdpOrg.class, isNullReturnEmpty(scdpUser.getOrgUuid())) : null;
                                            if (scdpOrg != null) {
                                                fadCertificateAccount.setAccountInfoId(isNullReturnEmpty(scdpOrg.getUuid()));
                                                fadCertificateAccount.setAccountInfoCode(isNullReturnEmpty(scdpOrg.getOrgCode()));
                                                fadCertificateAccount.setAccountInfoName(isNullReturnEmpty(scdpOrg.getOrgName()));
                                            }
                                        }
                                    }
                                }
                            }
                            fadCertificateAccountLst3.add(fadCertificateAccount);
                        }
                    }
                }
            }
        }
        PersistenceFactory.getInstance().batchInsert(fadCertificateAccountLst3);
    }

    @Override
    public void certificateMergeOriginalFormCodeAbstractsFromCertificateDetail(String businessId, List fadCertificateList) {

        //主表凭证取得
        //Map<String, Object> fadCertificateConditionsMap = new HashMap<String, Object>();
        //fadCertificateConditionsMap.put(FadCertificateAttribute.BUSINESS_ID, businessId);
        //fadCertificateConditionsMap.put(FadCertificateAttribute.STATE, "0");
        //List fadCertificateList = isNullReturnEmpty(businessId).length() > 0 ? DtoHelper.findByAnyFields(FadCertificateDto.class, fadCertificateConditionsMap, "seq_no asc") : new ArrayList<>();
        for (int i = 0; i < fadCertificateList.size(); i++) {
            FadCertificateDto fadCertificate = (FadCertificateDto) fadCertificateList.get(i);

            //子表凭证摘要取得
            List<FadCertificateDetailDto> fadCertificateDetailList = fadCertificate.getFadCertificateDetailDto() != null ? fadCertificate.getFadCertificateDetailDto() : new ArrayList<>();
            String originalFormCodes = "";
            String abstractss = "";
            BigDecimal debtor = new BigDecimal("0");
            BigDecimal creditor = new BigDecimal("0");
            for (int y = 0; y < fadCertificateDetailList.size(); y++) {
                FadCertificateDetailDto fadCertificateDetail = fadCertificateDetailList.get(y);

                String originalFormCode = isNullReturnEmpty(fadCertificateDetail.getOriginalFormCode());
                if (isNullReturnEmpty(originalFormCode).length() > 0) {
                    if (isNullReturnEmpty(originalFormCodes).length() > 0) {
                        originalFormCodes = originalFormCodes + MergeSplitSymbol + isNullReturnEmpty(originalFormCode);
                    } else {
                        originalFormCodes = isNullReturnEmpty(originalFormCode);
                    }
                }

                String abstracts = isNullReturnEmpty(fadCertificateDetail.getAbstracts());
                if (isNullReturnEmpty(abstracts).length() > 0) {
                    if (isNullReturnEmpty(abstractss).length() > 0) {
                        abstractss = abstractss + MergeSplitSymbol + isNullReturnEmpty(abstracts);
                    } else {
                        abstractss = isNullReturnEmpty(abstracts);
                    }
                }

                if (isNullReturnEmpty(fadCertificateDetail.getDebtorOrCreditor()).equals("0")) {
                    debtor = debtor.add(new BigDecimal(isEmptyReturnZero(isNullReturnEmpty(fadCertificateDetail.getLocal()))));
                } else if (isNullReturnEmpty(fadCertificateDetail.getDebtorOrCreditor()).equals("1")) {
                    creditor = creditor.add(new BigDecimal(isEmptyReturnZero(isNullReturnEmpty(fadCertificateDetail.getLocal()))));
                }
            }
            fadCertificate.setOriginalFormCode(StringDistinct(originalFormCodes, MergeSplitSymbol));
            fadCertificate.setAbstracts(StringDistinct(abstractss, MergeSplitSymbol));
            fadCertificate.setDebtor(debtor);
            fadCertificate.setCreditor(creditor);
        }
        DtoHelper.batchUpdate(fadCertificateList, FadCertificate.class);
    }

    @Override
    public void certificateMergeOriginalFormCodeAbstractsFromCertificateDetailByFadCertificateUuid(String fadCertificateUuid) {

        //主表凭证取得
        FadCertificateDto fadCertificate = isNullReturnEmpty(fadCertificateUuid).length() > 0 ? (FadCertificateDto) DtoHelper.findDtoByPK(FadCertificateDto.class, fadCertificateUuid) : null;
        if (fadCertificate != null) {

            //子表凭证摘要取得
            List<FadCertificateDetailDto> fadCertificateDetailList = fadCertificate.getFadCertificateDetailDto() != null ? fadCertificate.getFadCertificateDetailDto() : new ArrayList<>();
            //String originalFormCodes = "";
            String abstractss = "";
            for (int y = 0; y < fadCertificateDetailList.size(); y++) {
                FadCertificateDetailDto fadCertificateDetail = fadCertificateDetailList.get(y);

                //String originalFormCode = isNullReturnEmpty(fadCertificateDetail.getOriginalFormCode());
                /*if (isNullReturnEmpty(originalFormCode).length() > 0) {
                    if (isNullReturnEmpty(originalFormCodes).length() > 0) {
                        originalFormCodes = originalFormCodes + MergeSplitSymbol + isNullReturnEmpty(originalFormCode);
                    } else {
                        originalFormCodes = isNullReturnEmpty(originalFormCode);
                    }
                }*/

                String abstracts = isNullReturnEmpty(fadCertificateDetail.getAbstracts());
                if (isNullReturnEmpty(abstracts).length() > 0) {
                    if (isNullReturnEmpty(abstractss).length() > 0) {
                        abstractss = abstractss + MergeSplitSymbol + isNullReturnEmpty(abstracts);
                    } else {
                        abstractss = isNullReturnEmpty(abstracts);
                    }
                }
            }
            //fadCertificate.setOriginalFormCode(StringDistinct(originalFormCodes, MergeSplitSymbol));
            fadCertificate.setAbstracts(StringDistinct(abstractss, MergeSplitSymbol));

            List fadCertificateList = new ArrayList<>();
            fadCertificateList.add(fadCertificate);
            DtoHelper.batchUpdate(fadCertificateList, FadCertificate.class);
        }
    }

    @Override
    public void nonProjectSetRuleToUpdateCertificateSetDetailSubject(String businessId, String originalFormTypeForeign, String originalFormType, List fadCertificateList) {

        /*
        【非项目凭证】涉及的【原始单据类型】取值范围
        1.个人请款(日常/差旅)
        2.材料费或外包商请款(采购合同)
        4.1 无请款报销(采购)
        4 无请款报销(日常/差旅)
        5 有请款报销(日常/差旅)
        5.1 有请款报销(采购有应付)
        5.2 有请款报销(采购有预付)
        6 无请款采购合同
        7 有请款采购合同有应付
        7.1.有请款采购合同有预付
        */

        if (originalFormType.equals("1") || originalFormType.equals("2")) {

            //1.FAD_CASH_REQ(请款)
            FadCashReq fadCashReq = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadCashReq.class, businessId) : null;
            if (fadCashReq != null) {
                if (!(isNullReturnEmpty(fadCashReq.getIsProject()).equals("1"))) {

                    //非项目凭证生成规则设定取得
                    Map<String, Object> fadNonProjectSetRuleConditionsMap = new HashMap<String, Object>();
                    fadNonProjectSetRuleConditionsMap.put(FadNonProjectSetRuleAttribute.OFFICE, isNullReturnEmpty(fadCashReq.getOfficeId()));
                    fadNonProjectSetRuleConditionsMap.put(FadNonProjectSetRuleAttribute.FINANCIAL_SUBJECT, isNullReturnEmpty(fadCashReq.getSubjectCode()));
                    List<FadNonProjectSetRule> fadNonProjectSetRuleList = isNullReturnEmpty(fadCashReq.getOfficeId()).length() > 0 && isNullReturnEmpty(fadCashReq.getSubjectCode()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadNonProjectSetRule.class, fadNonProjectSetRuleConditionsMap, "seq_no desc") : new ArrayList<>();
                    if (fadNonProjectSetRuleList.size() > 0) {
                        FadNonProjectSetRule fadNonProjectSetRule = (FadNonProjectSetRule) fadNonProjectSetRuleList.get(0);
                        for (int i = 0; i < fadCertificateList.size(); i++) {
                            FadCertificateDto fadCertificate = (FadCertificateDto) fadCertificateList.get(i);

                            List fadCertificateDetailLst2 = new ArrayList<>();
                            List<FadCertificateDetailDto> fadCertificateDetailList = fadCertificate.getFadCertificateDetailDto() != null ? fadCertificate.getFadCertificateDetailDto() : new ArrayList<>();
                            for (int y = 0; y < fadCertificateDetailList.size(); y++) {
                                FadCertificateDetailDto fadCertificateDetail = (FadCertificateDetailDto) fadCertificateDetailList.get(y);
                                if ((isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("1") || isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("2")) && (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("122102") || isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("1123"))) {
                                    if (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("1") && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("122102")) {

                                        //日常、差旅
                                        if (isNullReturnEmpty(fadNonProjectSetRule.getDebtorSubject1()).length() > 0) {
                                            fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadNonProjectSetRule.getDebtorSubject1()));
                                            fadCertificateDetail.setSubjectName(null);
                                        }
                                    } else if (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("2") && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("1123")) {

                                        //采购
                                        if (isNullReturnEmpty(fadNonProjectSetRule.getDebtorSubject2()).length() > 0) {
                                            fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadNonProjectSetRule.getDebtorSubject2()));
                                            fadCertificateDetail.setSubjectName(null);
                                        }
                                    }
                                } else if (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("1002")) {
                                    if (isNullReturnEmpty(fadNonProjectSetRule.getCreditorSubject4a51a1a2()).length() > 0) {
                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadNonProjectSetRule.getCreditorSubject4a51a1a2()));
                                        fadCertificateDetail.setSubjectName(null);
                                    }
                                }
                                fadCertificateDetailLst2.add(fadCertificateDetail);
                            }
                            DtoHelper.batchUpdate(fadCertificateDetailLst2, FadCertificateDetail.class);
                        }
                    }
                }
            } else {
                throw new BizException("未能找到【原始单据】凭证生成失败!");
            }
        } else if (originalFormType.equals("4") || originalFormType.equals("4.1") || originalFormType.equals("5") || originalFormType.equals("5.1") || originalFormType.equals("5.2")) {

            //0.FAD_INVOICE(发票)
            FadInvoice fadInvoice = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadInvoice.class, businessId) : null;
            if (fadInvoice != null) {
                if (!(isNullReturnEmpty(fadInvoice.getPrmProjectMainId()).length() > 0)) {

                    //非项目凭证生成规则设定取得
                    List<QueryCondition> fadNonProjectSetRuleConditionsLst = new ArrayList<>();

                    QueryCondition fadNonProjectSetRuleQueryCondition1 = new QueryCondition();
                    fadNonProjectSetRuleQueryCondition1.setField(FadNonProjectSetRuleAttribute.OFFICE);
                    fadNonProjectSetRuleQueryCondition1.setOperator("IN");
                    List fadNonProjectSetRuleValueCondition1List = new ArrayList<>();
                    String[] officeIdArr = isNullReturnEmpty(fadInvoice.getOfficeId()).split(",");
                    for (int i = 0; i < officeIdArr.length; i++) {
                        String officeId = isNullReturnEmpty(officeIdArr[i]);
                        if (officeId.length() > 0) {
                            fadNonProjectSetRuleValueCondition1List.add(officeId);
                        }
                    }
                    fadNonProjectSetRuleQueryCondition1.setValueList(fadNonProjectSetRuleValueCondition1List);
                    fadNonProjectSetRuleConditionsLst.add(fadNonProjectSetRuleQueryCondition1);

                    QueryCondition fadNonProjectSetRuleQueryCondition2 = new QueryCondition();
                    fadNonProjectSetRuleQueryCondition2.setField(FadNonProjectSetRuleAttribute.FINANCIAL_SUBJECT);
                    fadNonProjectSetRuleQueryCondition2.setOperator("=");
                    fadNonProjectSetRuleQueryCondition2.addValue(isNullReturnEmpty(fadInvoice.getSubjectCode()));
                    fadNonProjectSetRuleConditionsLst.add(fadNonProjectSetRuleQueryCondition2);

                    List<FadNonProjectSetRule> fadNonProjectSetRuleList = isNullReturnEmpty(fadInvoice.getOfficeId()).length() > 0 && isNullReturnEmpty(fadInvoice.getSubjectCode()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadNonProjectSetRule.class, fadNonProjectSetRuleConditionsLst, "seq_no desc") : new ArrayList<>();
                    if (fadNonProjectSetRuleList.size() > 0) {
                        FadNonProjectSetRule fadNonProjectSetRule = (FadNonProjectSetRule) fadNonProjectSetRuleList.get(0);
                        for (int i = 0; i < fadCertificateList.size(); i++) {
                            FadCertificateDto fadCertificate = (FadCertificateDto) fadCertificateList.get(i);

                            List fadCertificateDetailLst2 = new ArrayList<>();
                            List<FadCertificateDetailDto> fadCertificateDetailList = fadCertificate.getFadCertificateDetailDto() != null ? fadCertificate.getFadCertificateDetailDto() : new ArrayList<>();
                            for (int y = 0; y < fadCertificateDetailList.size(); y++) {
                                FadCertificateDetailDto fadCertificateDetail = (FadCertificateDetailDto) fadCertificateDetailList.get(y);
                                if (originalFormType.equals("4") || originalFormType.equals("5")) {
                                    if ((isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("4") || isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("5") || isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("6") || isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("7")) && (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("540101") || isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("5401"))) {
                                        if (isNullReturnEmpty(fadNonProjectSetRule.getDebtorSubject4a51()).length() > 0) {
                                            fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadNonProjectSetRule.getDebtorSubject4a51()));
                                            fadCertificateDetail.setSubjectName(null);
                                        }
                                    } else if (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("1002")) {
                                        if (isNullReturnEmpty(fadNonProjectSetRule.getCreditorSubject4a51a1a2()).length() > 0) {
                                            fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadNonProjectSetRule.getCreditorSubject4a51a1a2()));
                                            fadCertificateDetail.setSubjectName(null);
                                        }
                                    } else if (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("2202")) {
                                        fadCertificateDetail.setSubjectCode("220226");
                                        fadCertificateDetail.setSubjectName(null);
                                    }
                                } else {
                                    if (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("8") && (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("540101") || isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("5401"))) {
                                        if (isNullReturnEmpty(fadNonProjectSetRule.getDebtorSubject4a51()).length() > 0) {
                                            fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadNonProjectSetRule.getDebtorSubject4a51()));
                                            fadCertificateDetail.setSubjectName(null);
                                        }
                                    } else if (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("1002")) {
                                        if (isNullReturnEmpty(fadNonProjectSetRule.getCreditorSubject4a51a1a2()).length() > 0) {
                                            fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadNonProjectSetRule.getCreditorSubject4a51a1a2()));
                                            fadCertificateDetail.setSubjectName(null);
                                        }
                                    } else if (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("2202")) {
                                        fadCertificateDetail.setSubjectCode("220226");
                                        fadCertificateDetail.setSubjectName(null);
                                    }
                                }

                                fadCertificateDetailLst2.add(fadCertificateDetail);
                            }
                            DtoHelper.batchUpdate(fadCertificateDetailLst2, FadCertificateDetail.class);
                        }
                    }
                }
            } else {
                throw new BizException("未能找到【原始单据】凭证生成失败!");
            }
        } else if (originalFormType.equals("6") || originalFormType.equals("7") || originalFormType.equals("7.1")) {

            //0.FAD_INVOICE(发票)
            FadInvoice fadInvoice = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadInvoice.class, businessId) : null;
            if (fadInvoice != null) {
                Map<String, Object> scmContractInvoiceConditionsMap = new HashMap<String, Object>();
                scmContractInvoiceConditionsMap.put(ScmContractInvoiceAttribute.FAD_INVOICE_ID, isNullReturnEmpty(fadInvoice.getUuid()));
                List<ScmContractInvoice> scmContractInvoiceList = isNullReturnEmpty(fadInvoice.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScmContractInvoice.class, scmContractInvoiceConditionsMap, null) : new ArrayList<>();
                if (scmContractInvoiceList.size() > 0) {
                    for (int yy = 0; yy < scmContractInvoiceList.size(); yy++) {
                        ScmContractInvoice scmContractInvoice = scmContractInvoiceList.get(yy);
                        ScmContract scmContract = isNullReturnEmpty(scmContractInvoice.getScmContractId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmContract.class, isNullReturnEmpty(scmContractInvoice.getScmContractId())) : null;
                        if (scmContract != null) {
                            if (!(isNullReturnEmpty(scmContract.getIsProject()).equals("1"))) {

                                //非项目凭证生成规则设定取得
                                Map<String, Object> fadNonProjectSetRuleConditionsMap = new HashMap<String, Object>();
                                fadNonProjectSetRuleConditionsMap.put(FadNonProjectSetRuleAttribute.OFFICE, isNullReturnEmpty(scmContract.getOfficeId()));
                                fadNonProjectSetRuleConditionsMap.put(FadNonProjectSetRuleAttribute.FINANCIAL_SUBJECT, isNullReturnEmpty(scmContract.getSubjectCode()));
                                List<FadNonProjectSetRule> fadNonProjectSetRuleList = isNullReturnEmpty(scmContract.getOfficeId()).length() > 0 && isNullReturnEmpty(scmContract.getSubjectCode()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadNonProjectSetRule.class, fadNonProjectSetRuleConditionsMap, "seq_no desc") : new ArrayList<>();
                                if (fadNonProjectSetRuleList.size() > 0) {
                                    FadNonProjectSetRule fadNonProjectSetRule = (FadNonProjectSetRule) fadNonProjectSetRuleList.get(0);
                                    for (int i = 0; i < fadCertificateList.size(); i++) {
                                        FadCertificateDto fadCertificate = (FadCertificateDto) fadCertificateList.get(i);

                                        List fadCertificateDetailLst2 = new ArrayList<>();
                                        List<FadCertificateDetailDto> fadCertificateDetailList = fadCertificate.getFadCertificateDetailDto() != null ? fadCertificate.getFadCertificateDetailDto() : new ArrayList<>();
                                        for (int y = 0; y < fadCertificateDetailList.size(); y++) {
                                            FadCertificateDetailDto fadCertificateDetail = (FadCertificateDetailDto) fadCertificateDetailList.get(y);
                                            if ((isNullReturnEmpty(fadCertificateDetail.getFree1()).equals(isNullReturnEmpty(scmContract.getSubjectCode()))) || (isNullReturnEmpty(fadCertificateDetail.getFree1()).length() == 0)) {
                                                if ((originalFormType.equals("6") || originalFormType.equals("7") || originalFormType.equals("7.1")) && isContractWaixie(businessId)) {
                                                    if ((isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("6") || isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("7")) && (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("540101") || isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("5401"))) {
                                                        if (isNullReturnEmpty(fadNonProjectSetRule.getDebtorSubject4a51()).length() > 0) {
                                                            fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadNonProjectSetRule.getDebtorSubject4a51()));
                                                            fadCertificateDetail.setSubjectName(null);
                                                        }
                                                    } else if (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("1002")) {
                                                        if (isNullReturnEmpty(fadNonProjectSetRule.getCreditorSubject4a51a1a2()).length() > 0) {
                                                            fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadNonProjectSetRule.getCreditorSubject4a51a1a2()));
                                                            fadCertificateDetail.setSubjectName(null);
                                                        }
                                                    } else if (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("2202")) {
                                                        fadCertificateDetail.setSubjectCode("220226");
                                                        fadCertificateDetail.setSubjectName(null);
                                                    }
                                                } else {
                                                    if (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("8") && (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("540101") || isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("5401"))) {
                                                        if (isNullReturnEmpty(fadNonProjectSetRule.getDebtorSubject4a51()).length() > 0) {
                                                            fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadNonProjectSetRule.getDebtorSubject4a51()));
                                                            fadCertificateDetail.setSubjectName(null);
                                                        }
                                                    } else if (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("1002")) {
                                                        if (isNullReturnEmpty(fadNonProjectSetRule.getCreditorSubject4a51a1a2()).length() > 0) {
                                                            fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadNonProjectSetRule.getCreditorSubject4a51a1a2()));
                                                            fadCertificateDetail.setSubjectName(null);
                                                        }
                                                    } else if (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("2202")) {
                                                        fadCertificateDetail.setSubjectCode("220226");
                                                        fadCertificateDetail.setSubjectName(null);
                                                    }
                                                }
                                                fadCertificateDetailLst2.add(fadCertificateDetail);
                                            }
                                        }
                                        DtoHelper.batchUpdate(fadCertificateDetailLst2, FadCertificateDetail.class);
                                    }
                                }
                            }
                        } else {
                            throw new BizException("未能找到【原始单据】凭证生成失败!");
                        }
                    }
                } else {
                    throw new BizException("未能找到【原始单据】凭证生成失败!");
                }
            } else {
                throw new BizException("未能找到【原始单据】凭证生成失败!");
            }
        } else if (originalFormType.equals("10")) {

            //2.FAD_PAY_REQ_C(支付)
            FadPayReqC fadPayReqC = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadPayReqC.class, businessId) : null;
            if (fadPayReqC != null) {
                if ((isNullReturnEmpty(fadPayReqC.getScmContractId()).length() > 0)) {
                    ScmContract scmContract = isNullReturnEmpty(fadPayReqC.getScmContractId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmContract.class, isNullReturnEmpty(fadPayReqC.getScmContractId())) : null;
                    if (scmContract != null) {
                        if (!(isNullReturnEmpty(scmContract.getIsProject()).equals("1"))) {

                            //非项目凭证生成规则设定取得
                            Map<String, Object> fadNonProjectSetRuleConditionsMap = new HashMap<String, Object>();
                            fadNonProjectSetRuleConditionsMap.put(FadNonProjectSetRuleAttribute.OFFICE, isNullReturnEmpty(scmContract.getOfficeId()));
                            fadNonProjectSetRuleConditionsMap.put(FadNonProjectSetRuleAttribute.FINANCIAL_SUBJECT, isNullReturnEmpty(scmContract.getSubjectCode()));
                            List<FadNonProjectSetRule> fadNonProjectSetRuleList = isNullReturnEmpty(scmContract.getOfficeId()).length() > 0 && isNullReturnEmpty(scmContract.getSubjectCode()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadNonProjectSetRule.class, fadNonProjectSetRuleConditionsMap, "seq_no desc") : new ArrayList<>();
                            if (fadNonProjectSetRuleList.size() > 0) {
                                FadNonProjectSetRule fadNonProjectSetRule = (FadNonProjectSetRule) fadNonProjectSetRuleList.get(0);
                                for (int i = 0; i < fadCertificateList.size(); i++) {
                                    FadCertificateDto fadCertificate = (FadCertificateDto) fadCertificateList.get(i);

                                    List fadCertificateDetailLst2 = new ArrayList<>();
                                    List<FadCertificateDetailDto> fadCertificateDetailList = fadCertificate.getFadCertificateDetailDto() != null ? fadCertificate.getFadCertificateDetailDto() : new ArrayList<>();
                                    for (int y = 0; y < fadCertificateDetailList.size(); y++) {
                                        FadCertificateDetailDto fadCertificateDetail = (FadCertificateDetailDto) fadCertificateDetailList.get(y);
                                        if (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("1002")) {
                                            if (isNullReturnEmpty(fadNonProjectSetRule.getCreditorSubject4a51a1a2()).length() > 0) {
                                                fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadNonProjectSetRule.getCreditorSubject4a51a1a2()));
                                                fadCertificateDetail.setSubjectName(null);
                                            }
                                        }
                                        fadCertificateDetailLst2.add(fadCertificateDetail);
                                    }
                                    DtoHelper.batchUpdate(fadCertificateDetailLst2, FadCertificateDetail.class);
                                }
                            }
                        }
                    } else {
                        throw new BizException("未能找到【原始单据】凭证生成失败!");
                    }
                } else {
                    throw new BizException("未能找到【原始单据】凭证生成失败!");
                }
            } else {
                throw new BizException("未能找到【原始单据】凭证生成失败!");
            }
        }
    }

    @Override
    public void projectSetRuleToUpdateCertificateSetDetailSubject(String businessId, String originalFormTypeForeign, String originalFormType, List fadCertificateList) {

        /*
        【项目凭证】涉及的【原始单据类型】取值范围
        1.个人请款(日常/差旅)
        2.材料费或外包商请款(采购合同)
        4.1 无请款报销(采购)
        4 无请款报销(日常/差旅)
        5 有请款报销(日常/差旅)
        5.1 有请款报销(采购有应付)
        5.2 有请款报销(采购有预付)
        6 无请款采购合同
        7 有请款采购合同有应付
        7.1.有请款采购合同有预付
        */

        if (originalFormType.equals("1") || originalFormType.equals("2")) {

            //1.FAD_CASH_REQ(请款)
            FadCashReq fadCashReq = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadCashReq.class, businessId) : null;
            if (fadCashReq != null) {
                if (isNullReturnEmpty(fadCashReq.getIsProject()).equals("1")) {
                    PrmProjectMain prmProjectMain = isNullReturnEmpty(fadCashReq.getProjectId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(fadCashReq.getProjectId())) : null;
                    if (prmProjectMain != null) {

                        //项目凭证生成规则设定取得
                        String sql = "SELECT * FROM FAD_PROJECT_SET_RULE";

                        String officeId = isNullReturnEmpty(fadCashReq.getOfficeId());
                        sql = sql + " WHERE (OFFICE LIKE '%|" + officeId + "|%')";

                        String subject = isNullReturnEmpty(fadCashReq.getSubjectCode());
                        sql = sql + " AND (SUBJECT = '" + subject + "')";

                        String projectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                        sql = sql + " AND (PROJECT_CODE LIKE '%|" + projectCode.substring(5, 7) + "|%' OR PROJECT_CODE LIKE '%|" + projectCode.substring(5, 8) + "|%' OR PROJECT_CODE LIKE '%|" + projectCode.substring(0, 1) + projectCode.substring(5, 7) + "|%')";

                        String prmCodeType = isNullReturnEmpty(prmProjectMain.getPrmCodeType());
                        sql = sql + " AND (PRM_CODE_TYPE = '" + prmCodeType + "')";

                        DAOMeta daoMeta = new DAOMeta();
                        daoMeta.setStrSql(sql);
                        daoMeta.setNeedFilter(false);
                        List fadProjectSetRuleList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                        if (fadProjectSetRuleList.size() > 0) {
                            Map fadProjectSetRule = (Map) fadProjectSetRuleList.get(0);
                            for (int i = 0; i < fadCertificateList.size(); i++) {
                                FadCertificateDto fadCertificate = (FadCertificateDto) fadCertificateList.get(i);

                                List fadCertificateDetailLst2 = new ArrayList<>();
                                List<FadCertificateDetailDto> fadCertificateDetailList = fadCertificate.getFadCertificateDetailDto() != null ? fadCertificate.getFadCertificateDetailDto() : new ArrayList<>();
                                for (int y = 0; y < fadCertificateDetailList.size(); y++) {
                                    FadCertificateDetailDto fadCertificateDetail = (FadCertificateDetailDto) fadCertificateDetailList.get(y);
                                    if ((isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("1") || isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("2")) && (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("122102") || isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("1123"))) {
                                        if (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("1") && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("122102")) {

                                            //日常、差旅
                                            if (isNullReturnEmpty(fadProjectSetRule.get("debtorSubject1")).length() > 0) {
                                                fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectSetRule.get("debtorSubject1")));
                                                fadCertificateDetail.setSubjectName(null);
                                            }
                                        } else if (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("2") && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("1123")) {

                                            //采购
                                            if (isNullReturnEmpty(fadProjectSetRule.get("debtorSubject2")).length() > 0) {
                                                fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectSetRule.get("debtorSubject2")));
                                                fadCertificateDetail.setSubjectName(null);
                                            }
                                        }
                                    } else if (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("1002")) {
                                        if (isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")).length() > 0) {
                                            fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")));
                                            fadCertificateDetail.setSubjectName(null);
                                        }
                                    }
                                    fadCertificateDetailLst2.add(fadCertificateDetail);
                                }
                                DtoHelper.batchUpdate(fadCertificateDetailLst2, FadCertificateDetail.class);
                            }
                        }
                    } else {
                        throw new BizException("未能找到【原始单据】凭证生成失败!");
                    }
                }
            } else {
                throw new BizException("未能找到【原始单据】凭证生成失败!");
            }
        } else if (originalFormType.equals("4") || originalFormType.equals("4.1") || originalFormType.equals("5") || originalFormType.equals("5.1") || originalFormType.equals("5.2")) {

            //0.FAD_INVOICE(发票)
            FadInvoice fadInvoice = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadInvoice.class, businessId) : null;
            if (fadInvoice != null) {
                if (isNullReturnEmpty(fadInvoice.getPrmProjectMainId()).length() > 0) {
                    PrmProjectMain prmProjectMain = isNullReturnEmpty(fadInvoice.getPrmProjectMainId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(fadInvoice.getPrmProjectMainId())) : null;
                    if (prmProjectMain != null) {

                        //项目凭证生成规则设定取得
                        String sql = "SELECT * FROM FAD_PROJECT_SET_RULE";

                        String sql2 = " WHERE (";
                        String[] officeIdArr = isNullReturnEmpty(fadInvoice.getOfficeId()).split(",");
                        for (int i = 0; i < officeIdArr.length; i++) {
                            String officeId = isNullReturnEmpty(officeIdArr[i]);
                            if (officeId.length() > 0) {
                                if (sql2.equals(" WHERE (")) {
                                    sql2 = sql2 + " (OFFICE LIKE '%|" + officeId + "|%')";
                                } else {
                                    sql2 = sql2 + " OR (OFFICE LIKE '%|" + officeId + "|%')";
                                }
                            }
                        }
                        sql2 = sql2 + " )";
                        sql = sql + sql2;

                        String subject = isNullReturnEmpty(fadInvoice.getSubjectCode());
                        sql = sql + " AND (SUBJECT = '" + subject + "')";

                        String projectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                        sql = sql + " AND (PROJECT_CODE LIKE '%|" + projectCode.substring(5, 7) + "|%' OR PROJECT_CODE LIKE '%|" + projectCode.substring(5, 8) + "|%' OR PROJECT_CODE LIKE '%|" + projectCode.substring(0, 1) + projectCode.substring(5, 7) + "|%')";

                        String prmCodeType = isNullReturnEmpty(prmProjectMain.getPrmCodeType());
                        sql = sql + " AND (PRM_CODE_TYPE = '" + prmCodeType + "')";

                        DAOMeta daoMeta = new DAOMeta();
                        daoMeta.setStrSql(sql);
                        daoMeta.setNeedFilter(false);
                        List fadProjectSetRuleList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                        if (fadProjectSetRuleList.size() > 0) {
                            Map fadProjectSetRule = (Map) fadProjectSetRuleList.get(0);
                            for (int i = 0; i < fadCertificateList.size(); i++) {
                                FadCertificateDto fadCertificate = (FadCertificateDto) fadCertificateList.get(i);

                                List fadCertificateDetailLst2 = new ArrayList<>();
                                List<FadCertificateDetailDto> fadCertificateDetailList = fadCertificate.getFadCertificateDetailDto() != null ? fadCertificate.getFadCertificateDetailDto() : new ArrayList<>();
                                for (int y = 0; y < fadCertificateDetailList.size(); y++) {
                                    FadCertificateDetailDto fadCertificateDetail = (FadCertificateDetailDto) fadCertificateDetailList.get(y);
                                    if ((isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("4") || isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("5") || isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("6") || isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("7")) && (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("140302") || isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("540101") || isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("5401"))) {
                                        if (isNullReturnEmpty(fadProjectSetRule.get("debtorSubject6a7")).length() > 0) {
                                            fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectSetRule.get("debtorSubject6a7")));
                                            fadCertificateDetail.setSubjectName(null);
                                        }
                                    } else if ((isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("6") || isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("7")) && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("2202")) {
                                        if (isNullReturnEmpty(fadProjectSetRule.get("creditorSubject6a7")).length() > 0) {
                                            fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectSetRule.get("creditorSubject6a7")));
                                            fadCertificateDetail.setSubjectName(null);
                                        }
                                    } else if (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("9") && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("2202")) {
                                        if (isNullReturnEmpty(fadProjectSetRule.get("debtorSubject9a10")).length() > 0) {
                                            fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectSetRule.get("debtorSubject9a10")));
                                            fadCertificateDetail.setSubjectName(null);
                                        }
                                    } else if (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("8") && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("5401")) {
                                        if (isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).length() > 0) {
                                            fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")));
                                            fadCertificateDetail.setSubjectName(null);
                                        }
                                    } else if (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("8") && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("140302")) {
                                        if (isNullReturnEmpty(fadProjectSetRule.get("creditorSubject8")).length() > 0) {
                                            fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectSetRule.get("creditorSubject8")));
                                            fadCertificateDetail.setSubjectName(null);
                                        }
                                    } else if (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("1002")) {
                                        if (isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")).length() > 0) {
                                            fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")));
                                            fadCertificateDetail.setSubjectName(null);
                                        }
                                    }
                                    fadCertificateDetailLst2.add(fadCertificateDetail);
                                }
                                DtoHelper.batchUpdate(fadCertificateDetailLst2, FadCertificateDetail.class);
                            }
                        }
                    } else {
                        throw new BizException("未能找到【原始单据】凭证生成失败!");
                    }
                }
            } else {
                throw new BizException("未能找到【原始单据】凭证生成失败!");
            }
        } else if (originalFormType.equals("6") || originalFormType.equals("7") || originalFormType.equals("7.1")) {

            //0.FAD_INVOICE(发票)
            FadInvoice fadInvoice = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadInvoice.class, businessId) : null;
            if (fadInvoice != null) {
                Map<String, Object> scmContractInvoiceConditionsMap = new HashMap<String, Object>();
                scmContractInvoiceConditionsMap.put(ScmContractInvoiceAttribute.FAD_INVOICE_ID, isNullReturnEmpty(fadInvoice.getUuid()));
                List<ScmContractInvoice> scmContractInvoiceList = isNullReturnEmpty(fadInvoice.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScmContractInvoice.class, scmContractInvoiceConditionsMap, null) : new ArrayList<>();
                if (scmContractInvoiceList.size() > 0) {
                    for (int yy = 0; yy < scmContractInvoiceList.size(); yy++) {
                        ScmContractInvoice scmContractInvoice = scmContractInvoiceList.get(yy);
                        ScmContract scmContract = isNullReturnEmpty(scmContractInvoice.getScmContractId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmContract.class, isNullReturnEmpty(scmContractInvoice.getScmContractId())) : null;
                        if (scmContract != null) {
                            if (isNullReturnEmpty(scmContract.getIsProject()).equals("1")) {
                                PrmProjectMain prmProjectMain = isNullReturnEmpty(scmContract.getProjectId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(scmContract.getProjectId())) : null;
                                if (prmProjectMain != null) {

                                    //项目凭证生成规则设定取得
                                    String sql = "SELECT * FROM FAD_PROJECT_SET_RULE";

                                    String officeId = isNullReturnEmpty(scmContract.getOfficeId());
                                    sql = sql + " WHERE (OFFICE LIKE '%|" + officeId + "|%')";

                                    String subject = isNullReturnEmpty(scmContract.getSubjectCode());
                                    sql = sql + " AND (SUBJECT = '" + subject + "')";

                                    String projectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                                    sql = sql + " AND (PROJECT_CODE LIKE '%|" + projectCode.substring(5, 7) + "|%' OR PROJECT_CODE LIKE '%|" + projectCode.substring(5, 8) + "|%' OR PROJECT_CODE LIKE '%|" + projectCode.substring(0, 1) + projectCode.substring(5, 7) + "|%')";

                                    String prmCodeType = isNullReturnEmpty(prmProjectMain.getPrmCodeType());
                                    sql = sql + " AND (PRM_CODE_TYPE = '" + prmCodeType + "')";

                                    DAOMeta daoMeta = new DAOMeta();
                                    daoMeta.setStrSql(sql);
                                    daoMeta.setNeedFilter(false);
                                    List fadProjectSetRuleList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                                    if (fadProjectSetRuleList.size() > 0) {
                                        Map fadProjectSetRule = (Map) fadProjectSetRuleList.get(0);
                                        for (int i = 0; i < fadCertificateList.size(); i++) {
                                            FadCertificateDto fadCertificate = (FadCertificateDto) fadCertificateList.get(i);

                                            List fadCertificateDetailLst2 = new ArrayList<>();
                                            List<FadCertificateDetailDto> fadCertificateDetailList = fadCertificate.getFadCertificateDetailDto() != null ? fadCertificate.getFadCertificateDetailDto() : new ArrayList<>();
                                            for (int y = 0; y < fadCertificateDetailList.size(); y++) {
                                                FadCertificateDetailDto fadCertificateDetail = (FadCertificateDetailDto) fadCertificateDetailList.get(y);
                                                if ((isNullReturnEmpty(fadCertificateDetail.getFree1()).equals(isNullReturnEmpty(prmProjectMain.getProjectCode()))) || (isNullReturnEmpty(fadCertificateDetail.getFree1()).length() == 0)) {
                                                    if ((isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("6") || isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("7")) && (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("140302") || isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("540101") || isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("5401"))) {
                                                        if (isNullReturnEmpty(fadProjectSetRule.get("debtorSubject6a7")).length() > 0) {
                                                            fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectSetRule.get("debtorSubject6a7")));
                                                            fadCertificateDetail.setSubjectName(null);
                                                        }
                                                    } else if ((isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("6") || isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("7")) && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("2202")) {
                                                        if (isNullReturnEmpty(fadProjectSetRule.get("creditorSubject6a7")).length() > 0) {
                                                            fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectSetRule.get("creditorSubject6a7")));
                                                            fadCertificateDetail.setSubjectName(null);
                                                        }
                                                    } else if (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("9") && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("2202")) {
                                                        if (isNullReturnEmpty(fadProjectSetRule.get("debtorSubject9a10")).length() > 0) {
                                                            fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectSetRule.get("debtorSubject9a10")));
                                                            fadCertificateDetail.setSubjectName(null);
                                                        }
                                                    } else if (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("8") && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("5401")) {
                                                        if (isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).length() > 0) {
                                                            fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")));
                                                            fadCertificateDetail.setSubjectName(null);
                                                        }
                                                    } else if (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("8") && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("140302")) {
                                                        if (isNullReturnEmpty(fadProjectSetRule.get("creditorSubject8")).length() > 0) {
                                                            fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectSetRule.get("creditorSubject8")));
                                                            fadCertificateDetail.setSubjectName(null);
                                                        }
                                                    } else if (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("1002")) {
                                                        if (isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")).length() > 0) {
                                                            fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")));
                                                            fadCertificateDetail.setSubjectName(null);
                                                        }
                                                    }
                                                    fadCertificateDetailLst2.add(fadCertificateDetail);
                                                }
                                            }
                                            DtoHelper.batchUpdate(fadCertificateDetailLst2, FadCertificateDetail.class);
                                        }
                                    }
                                } else {
                                    throw new BizException("未能找到【原始单据】凭证生成失败!");
                                }
                            }
                        } else {
                            throw new BizException("未能找到【原始单据】凭证生成失败!");
                        }
                    }
                } else {
                    throw new BizException("未能找到【原始单据】凭证生成失败!");
                }
            } else {
                throw new BizException("未能找到【原始单据】凭证生成失败!");
            }
        } else if (originalFormType.equals("10")) {

            //2.FAD_PAY_REQ_C(支付)
            FadPayReqC fadPayReqC = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadPayReqC.class, businessId) : null;
            if (fadPayReqC != null) {
                if ((isNullReturnEmpty(fadPayReqC.getScmContractId()).length() > 0)) {
                    ScmContract scmContract = isNullReturnEmpty(fadPayReqC.getScmContractId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmContract.class, isNullReturnEmpty(fadPayReqC.getScmContractId())) : null;
                    if (scmContract != null) {
                        if ((isNullReturnEmpty(scmContract.getIsProject()).equals("1"))) {
                            PrmProjectMain prmProjectMain = isNullReturnEmpty(scmContract.getProjectId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(scmContract.getProjectId())) : null;
                            if (prmProjectMain != null) {

                                //项目凭证生成规则设定取得
                                String sql = "SELECT * FROM FAD_PROJECT_SET_RULE";

                                String officeId = isNullReturnEmpty(scmContract.getOfficeId());
                                sql = sql + " WHERE (OFFICE LIKE '%|" + officeId + "|%')";

                                String subject = isNullReturnEmpty(scmContract.getSubjectCode());
                                sql = sql + " AND (SUBJECT = '" + subject + "')";

                                String projectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                                sql = sql + " AND (PROJECT_CODE LIKE '%|" + projectCode.substring(5, 7) + "|%' OR PROJECT_CODE LIKE '%|" + projectCode.substring(5, 8) + "|%' OR PROJECT_CODE LIKE '%|" + projectCode.substring(0, 1) + projectCode.substring(5, 7) + "|%')";

                                String prmCodeType = isNullReturnEmpty(prmProjectMain.getPrmCodeType());
                                sql = sql + " AND (PRM_CODE_TYPE = '" + prmCodeType + "')";

                                DAOMeta daoMeta = new DAOMeta();
                                daoMeta.setStrSql(sql);
                                daoMeta.setNeedFilter(false);
                                List fadProjectSetRuleList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                                if (fadProjectSetRuleList.size() > 0) {
                                    Map fadProjectSetRule = (Map) fadProjectSetRuleList.get(0);
                                    for (int i = 0; i < fadCertificateList.size(); i++) {
                                        FadCertificateDto fadCertificate = (FadCertificateDto) fadCertificateList.get(i);

                                        List fadCertificateDetailLst2 = new ArrayList<>();
                                        List<FadCertificateDetailDto> fadCertificateDetailList = fadCertificate.getFadCertificateDetailDto() != null ? fadCertificate.getFadCertificateDetailDto() : new ArrayList<>();
                                        for (int y = 0; y < fadCertificateDetailList.size(); y++) {
                                            FadCertificateDetailDto fadCertificateDetail = (FadCertificateDetailDto) fadCertificateDetailList.get(y);
                                            if (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("2202")) {
                                                if (isNullReturnEmpty(fadProjectSetRule.get("debtorSubject9a10")).length() > 0) {
                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectSetRule.get("debtorSubject9a10")));
                                                    fadCertificateDetail.setSubjectName(null);
                                                }
                                            } else if (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("1002")) {
                                                if (isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")).length() > 0) {
                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")));
                                                    fadCertificateDetail.setSubjectName(null);
                                                }
                                            }
                                            fadCertificateDetailLst2.add(fadCertificateDetail);
                                        }
                                        DtoHelper.batchUpdate(fadCertificateDetailLst2, FadCertificateDetail.class);
                                    }
                                }
                            } else {
                                throw new BizException("未能找到【原始单据】凭证生成失败!");
                            }
                        }
                    } else {
                        throw new BizException("未能找到【原始单据】凭证生成失败!");
                    }
                } else {
                    throw new BizException("未能找到【原始单据】凭证生成失败!");
                }
            } else {
                throw new BizException("未能找到【原始单据】凭证生成失败!");
            }
        }
    }

    @Override
    public void projectRunSetRuleToUpdateCertificateSetDetailSubject(String businessId, String originalFormTypeForeign, String originalFormType, List fadCertificateList) {

        /*
        【项目凭证】涉及的【原始单据类型】取值范围
        1.个人请款(日常/差旅)
        2.材料费或外包商请款(采购合同)
        4.1 无请款报销(采购)
        4 无请款报销(日常/差旅)
        5 有请款报销(日常/差旅)
        5.1 有请款报销(采购有应付)
        5.2 有请款报销(采购有预付)
        6 无请款采购合同
        7 有请款采购合同有应付
        7.1.有请款采购合同有预付
        */

        if (originalFormType.equals("1") || originalFormType.equals("2")) {

            //1.FAD_CASH_REQ(请款)
            FadCashReq fadCashReq = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadCashReq.class, businessId) : null;
            if (fadCashReq != null) {
                if (isNullReturnEmpty(fadCashReq.getIsProject()).equals("1")) {
                    PrmProjectMain prmProjectMain = isNullReturnEmpty(fadCashReq.getProjectId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(fadCashReq.getProjectId())) : null;
                    if (prmProjectMain != null) {

                        //项目凭证运行费生成规则设定取得
                        String sql = "SELECT * FROM FAD_PROJECT_RUN_SET_RULE";

                        String subject = isNullReturnEmpty(fadCashReq.getSubjectCode());
                        sql = sql + " WHERE (RUN_SUBJECT = '" + subject + "')";

                        DAOMeta daoMeta = new DAOMeta();
                        daoMeta.setStrSql(sql);
                        daoMeta.setNeedFilter(false);
                        List fadProjectRunSetRuleList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                        if (fadProjectRunSetRuleList.size() > 0) {
                            Map fadProjectRunSetRule = (Map) fadProjectRunSetRuleList.get(0);

                            //项目凭证生成规则设定取得
                            sql = "SELECT * FROM FAD_PROJECT_SET_RULE";

                            String officeId = isNullReturnEmpty(fadCashReq.getOfficeId());
                            sql = sql + " WHERE (OFFICE LIKE '%|" + officeId + "|%')";

                            String projectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                            sql = sql + " AND (PROJECT_CODE LIKE '%|" + projectCode.substring(5, 7) + "|%' OR PROJECT_CODE LIKE '%|" + projectCode.substring(5, 8) + "|%' OR PROJECT_CODE LIKE '%|" + projectCode.substring(0, 1) + projectCode.substring(5, 7) + "|%')";

                            String prmCodeType = isNullReturnEmpty(prmProjectMain.getPrmCodeType());
                            sql = sql + " AND (PRM_CODE_TYPE = '" + prmCodeType + "')";
                            sql = sql + " ORDER BY SEQ_NO DESC";

                            daoMeta = new DAOMeta();
                            daoMeta.setStrSql(sql);
                            daoMeta.setNeedFilter(false);
                            List fadProjectSetRuleList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                            if (fadProjectSetRuleList.size() > 0) {
                                for (int A = 0; A < fadProjectSetRuleList.size(); A++) {
                                    Map fadProjectSetRule = (Map) fadProjectSetRuleList.get(A);
                                    for (int i = 0; i < fadCertificateList.size(); i++) {
                                        FadCertificateDto fadCertificate = (FadCertificateDto) fadCertificateList.get(i);

                                        List fadCertificateDetailLst2 = new ArrayList<>();
                                        List<FadCertificateDetailDto> fadCertificateDetailList = fadCertificate.getFadCertificateDetailDto() != null ? fadCertificate.getFadCertificateDetailDto() : new ArrayList<>();
                                        for (int y = 0; y < fadCertificateDetailList.size(); y++) {
                                            FadCertificateDetailDto fadCertificateDetail = (FadCertificateDetailDto) fadCertificateDetailList.get(y);
                                            if (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("1")) {

                                                //日常、差旅
                                                if (isNullReturnEmpty(fadProjectSetRule.get("debtorSubject1")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject1")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                    if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject1")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject1")).substring(0, 4).equals(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject1")).substring(0, 4))) {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject1")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    } else {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectSetRule.get("debtorSubject1")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    }
                                                } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject1")).length() > 0 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject1")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject1")));
                                                    fadCertificateDetail.setSubjectName(null);
                                                } else if (isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                    if (isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")).substring(0, 4).equals(isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")).substring(0, 4))) {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    } else {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    }
                                                } else if (isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")).length() > 0 && isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")));
                                                    fadCertificateDetail.setSubjectName(null);
                                                }
                                            } else if (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("2")) {

                                                //采购
                                                if (isNullReturnEmpty(fadProjectSetRule.get("debtorSubject2")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject2")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                    if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject2")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject2")).substring(0, 4).equals(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject2")).substring(0, 4))) {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject2")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    } else {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectSetRule.get("debtorSubject2")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    }
                                                } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject2")).length() > 0 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject2")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject2")));
                                                    fadCertificateDetail.setSubjectName(null);
                                                } else if (isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                    if (isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")).substring(0, 4).equals(isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")).substring(0, 4))) {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    } else {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    }
                                                } else if (isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")).length() > 0 && isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")));
                                                    fadCertificateDetail.setSubjectName(null);
                                                }
                                            }
                                            fadCertificateDetailLst2.add(fadCertificateDetail);
                                        }
                                        DtoHelper.batchUpdate(fadCertificateDetailLst2, FadCertificateDetail.class);
                                    }
                                }
                            }
                        }
                    } else {
                        throw new BizException("未能找到【原始单据】凭证生成失败!");
                    }
                }
            } else {
                throw new BizException("未能找到【原始单据】凭证生成失败!");
            }
        } else if (originalFormType.equals("4") || originalFormType.equals("4.1") || originalFormType.equals("5") || originalFormType.equals("5.1") || originalFormType.equals("5.2")) {

            //0.FAD_INVOICE(发票)
            FadInvoice fadInvoice = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadInvoice.class, businessId) : null;
            if (fadInvoice != null) {
                if (isNullReturnEmpty(fadInvoice.getPrmProjectMainId()).length() > 0) {
                    PrmProjectMain prmProjectMain = isNullReturnEmpty(fadInvoice.getPrmProjectMainId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(fadInvoice.getPrmProjectMainId())) : null;
                    if (prmProjectMain != null) {

                        //项目凭证运行费生成规则设定取得
                        String sql = "SELECT * FROM FAD_PROJECT_RUN_SET_RULE";

                        String subject = isNullReturnEmpty(fadInvoice.getSubjectCode());
                        sql = sql + " WHERE (RUN_SUBJECT = '" + subject + "')";

                        DAOMeta daoMeta = new DAOMeta();
                        daoMeta.setStrSql(sql);
                        daoMeta.setNeedFilter(false);
                        List fadProjectRunSetRuleList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                        if (fadProjectRunSetRuleList.size() > 0) {
                            Map fadProjectRunSetRule = (Map) fadProjectRunSetRuleList.get(0);

                            //项目凭证生成规则设定取得
                            sql = "SELECT * FROM FAD_PROJECT_SET_RULE";

                            String sql2 = " WHERE (";
                            String[] officeIdArr = isNullReturnEmpty(fadInvoice.getOfficeId()).split(",");
                            for (int i = 0; i < officeIdArr.length; i++) {
                                String officeId = isNullReturnEmpty(officeIdArr[i]);
                                if (officeId.length() > 0) {
                                    if (sql2.equals(" WHERE (")) {
                                        sql2 = sql2 + " (OFFICE LIKE '%|" + officeId + "|%')";
                                    } else {
                                        sql2 = sql2 + " OR (OFFICE LIKE '%|" + officeId + "|%')";
                                    }
                                }
                            }
                            sql2 = sql2 + " )";
                            sql = sql + sql2;

                            String projectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                            sql = sql + " AND (PROJECT_CODE LIKE '%|" + projectCode.substring(5, 7) + "|%' OR PROJECT_CODE LIKE '%|" + projectCode.substring(5, 8) + "|%' OR PROJECT_CODE LIKE '%|" + projectCode.substring(0, 1) + projectCode.substring(5, 7) + "|%')";

                            String prmCodeType = isNullReturnEmpty(prmProjectMain.getPrmCodeType());
                            sql = sql + " AND (PRM_CODE_TYPE = '" + prmCodeType + "')";
                            sql = sql + " ORDER BY SEQ_NO DESC";

                            daoMeta = new DAOMeta();
                            daoMeta.setStrSql(sql);
                            daoMeta.setNeedFilter(false);
                            List fadProjectSetRuleList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                            if (fadProjectSetRuleList.size() > 0) {
                                for (int A = 0; A < fadProjectSetRuleList.size(); A++) {
                                    Map fadProjectSetRule = (Map) fadProjectSetRuleList.get(A);
                                    for (int i = 0; i < fadCertificateList.size(); i++) {
                                        FadCertificateDto fadCertificate = (FadCertificateDto) fadCertificateList.get(i);

                                        List fadCertificateDetailLst2 = new ArrayList<>();
                                        List<FadCertificateDetailDto> fadCertificateDetailList = fadCertificate.getFadCertificateDetailDto() != null ? fadCertificate.getFadCertificateDetailDto() : new ArrayList<>();
                                        for (int y = 0; y < fadCertificateDetailList.size(); y++) {
                                            FadCertificateDetailDto fadCertificateDetail = (FadCertificateDetailDto) fadCertificateDetailList.get(y);

                                            if (
                                                    (isNullReturnEmpty(fadProjectSetRule.get("subject")).equals("RUN") && isNullReturnEmpty(fadCertificateDetail.getDebtorOrCreditor()).equals("0") && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).indexOf("5401") == 0)
                                                            /*&&
                                                            (
                                                                    (
                                                                            originalFormType.equals("4") && isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("4") && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("540101")
                                                                    )
                                                                            ||
                                                                            (
                                                                                    originalFormType.equals("4.1") && isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("8") && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("5401")
                                                                            )
                                                                            ||
                                                                            (
                                                                                    originalFormType.equals("5") && isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("5") && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("540101")
                                                                            )
                                                                            ||
                                                                            (
                                                                                    originalFormType.equals("5.1") && isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("8") && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("5401")
                                                                            )
                                                                            ||
                                                                            (
                                                                                    originalFormType.equals("5.2") && isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("8") && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("5401")
                                                                            )
                                                                            ||
                                                                            (
                                                                                    originalFormType.equals("6") && isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("6") && isContractWaixie(businessId) && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("5401")
                                                                            )
                                                                            ||
                                                                            (
                                                                                    originalFormType.equals("6") && isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("8") && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("5401")
                                                                            )
                                                                            ||
                                                                            (
                                                                                    originalFormType.equals("7") && isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("7") && isContractWaixie(businessId) && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("5401")
                                                                            )
                                                                            ||
                                                                            (
                                                                                    originalFormType.equals("7") && isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("8") && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("5401")
                                                                            )
                                                                            ||
                                                                            (
                                                                                    originalFormType.equals("7.1") && isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("7") && isContractWaixie(businessId) && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("5401")
                                                                            )
                                                                            ||
                                                                            (
                                                                                    originalFormType.equals("7.1") && isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("8") && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals("5401")
                                                                            )
                                                            )*/
                                                    ) {
                                                if (isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).length() > 0) {
                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")));
                                                    fadCertificateDetail.setSubjectName(null);
                                                }
                                            }

                                            if (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("8") || isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("4")) {
                                                if (isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                    if (isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).length() >= 6 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject801")).length() >= 6 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).substring(0, 6).equals(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject801")).substring(0, 6))) {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject801")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    } else if (isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).length() >= 6 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80201")).length() >= 6 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).substring(0, 6).equals(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80201")).substring(0, 6))) {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80201")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    } else if (isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).length() >= 6 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80202")).length() >= 6 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).substring(0, 6).equals(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80202")).substring(0, 6))) {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80202")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    } else if (isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).length() >= 6 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80203")).length() >= 6 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).substring(0, 6).equals(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80203")).substring(0, 6))) {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80203")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    } else if (isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).length() >= 6 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject803")).length() >= 6 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).substring(0, 6).equals(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject803")).substring(0, 6))) {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject803")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    } else if (isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).length() >= 6 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject804")).length() >= 6 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).substring(0, 6).equals(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject804")).substring(0, 6))) {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject804")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    } else if (isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).length() >= 6 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject805")).length() >= 6 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).substring(0, 6).equals(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject805")).substring(0, 6))) {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject805")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject801")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).substring(0, 4).equals(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject801")).substring(0, 4))) {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject801")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80201")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).substring(0, 4).equals(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80201")).substring(0, 4))) {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80201")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80202")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).substring(0, 4).equals(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80202")).substring(0, 4))) {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80202")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80203")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).substring(0, 4).equals(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80203")).substring(0, 4))) {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80203")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject803")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).substring(0, 4).equals(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject803")).substring(0, 4))) {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject803")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject804")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).substring(0, 4).equals(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject804")).substring(0, 4))) {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject804")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject805")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).substring(0, 4).equals(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject805")).substring(0, 4))) {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject805")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    } else {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    }
                                                }

                                                //加入6位科目识别开始
                                                else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject801")).length() >= 6 && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).length() >= 6 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject801")).substring(0, 6).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 6))) {
                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject801")));
                                                    fadCertificateDetail.setSubjectName(null);
                                                } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80201")).length() >= 6 && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).length() >= 6 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80201")).substring(0, 6).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 6))) {
                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80201")));
                                                    fadCertificateDetail.setSubjectName(null);
                                                } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80202")).length() >= 6 && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).length() >= 6 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80202")).substring(0, 6).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 6))) {
                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80202")));
                                                    fadCertificateDetail.setSubjectName(null);
                                                } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80203")).length() >= 6 && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).length() >= 6 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80203")).substring(0, 6).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 6))) {
                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80203")));
                                                    fadCertificateDetail.setSubjectName(null);
                                                } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject803")).length() >= 6 && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).length() >= 6 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject803")).substring(0, 6).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 6))) {
                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject803")));
                                                    fadCertificateDetail.setSubjectName(null);
                                                } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject804")).length() >= 6 && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).length() >= 6 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject804")).substring(0, 6).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 6))) {
                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject804")));
                                                    fadCertificateDetail.setSubjectName(null);
                                                } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject805")).length() >= 6 && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).length() >= 6 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject805")).substring(0, 6).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 6))) {
                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject805")));
                                                    fadCertificateDetail.setSubjectName(null);
                                                } else if (isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")).length() >= 6 && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).length() >= 6 && isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")).substring(0, 6).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 6))) {
                                                    if (isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")).length() >= 6 && isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")).length() >= 6 && isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")).substring(0, 6).equals(isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")).substring(0, 6))) {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    } else {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    }
                                                } else if (isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")).length() >= 6 && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).length() >= 6 && isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")).substring(0, 6).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 6))) {
                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")));
                                                    fadCertificateDetail.setSubjectName(null);
                                                }
                                                //加入6位科目识别结束

                                                else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject801")).length() > 0 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject801")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject801")));
                                                    fadCertificateDetail.setSubjectName(null);
                                                } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80201")).length() > 0 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80201")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80201")));
                                                    fadCertificateDetail.setSubjectName(null);
                                                } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80202")).length() > 0 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80202")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80202")));
                                                    fadCertificateDetail.setSubjectName(null);
                                                } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80203")).length() > 0 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80203")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80203")));
                                                    fadCertificateDetail.setSubjectName(null);
                                                } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject803")).length() > 0 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject803")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject803")));
                                                    fadCertificateDetail.setSubjectName(null);
                                                } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject804")).length() > 0 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject804")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject804")));
                                                    fadCertificateDetail.setSubjectName(null);
                                                } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject805")).length() > 0 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject805")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject805")));
                                                    fadCertificateDetail.setSubjectName(null);
                                                } else if (isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                    if (isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")).substring(0, 4).equals(isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")).substring(0, 4))) {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    } else {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    }
                                                } else if (isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")).length() > 0 && isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")));
                                                    fadCertificateDetail.setSubjectName(null);
                                                }
                                            } else if (/*isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("4") ||*/ isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("5") || isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("6") || isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("7")) {
                                                if (isNullReturnEmpty(fadProjectSetRule.get("debtorSubject6a7")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject6a7")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                    if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject6a7")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject6a7")).substring(0, 4).equals(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject6a7")).substring(0, 4))) {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject6a7")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    } else {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectSetRule.get("debtorSubject6a7")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    }
                                                } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject6a7")).length() > 0 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject6a7")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject6a7")));
                                                    fadCertificateDetail.setSubjectName(null);
                                                } else if (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("6") || isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("7")) {
                                                    if (isNullReturnEmpty(fadProjectSetRule.get("creditorSubject6a7")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("creditorSubject6a7")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                        if (isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject6a7")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("creditorSubject6a7")).substring(0, 4).equals(isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject6a7")).substring(0, 4))) {
                                                            fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject6a7")));
                                                            fadCertificateDetail.setSubjectName(null);
                                                        } else {
                                                            fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectSetRule.get("creditorSubject6a7")));
                                                            fadCertificateDetail.setSubjectName(null);
                                                        }
                                                    } else if (isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject6a7")).length() > 0 && isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject6a7")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject6a7")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    }
                                                }
                                            } else if (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("9")) {
                                                if (isNullReturnEmpty(fadProjectSetRule.get("debtorSubject9a10")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject9a10")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                    if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject9a10")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject9a10")).substring(0, 4).equals(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject9a10")).substring(0, 4))) {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject9a10")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    } else {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectSetRule.get("debtorSubject9a10")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    }
                                                } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject9a10")).length() > 0 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject9a10")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject9a10")));
                                                    fadCertificateDetail.setSubjectName(null);
                                                } else if (isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                    if (isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")).substring(0, 4).equals(isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")).substring(0, 4))) {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    } else {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    }
                                                } else if (isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")).length() > 0 && isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")));
                                                    fadCertificateDetail.setSubjectName(null);
                                                }
                                            } else if (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("5.1")) {
                                                if (isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                    if (isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")).substring(0, 4).equals(isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")).substring(0, 4))) {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    } else {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    }
                                                } else if (isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")).length() > 0 && isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")));
                                                    fadCertificateDetail.setSubjectName(null);
                                                }
                                            }
                                            fadCertificateDetailLst2.add(fadCertificateDetail);
                                        }
                                        DtoHelper.batchUpdate(fadCertificateDetailLst2, FadCertificateDetail.class);
                                    }
                                }
                            }
                        }
                    } else {
                        throw new BizException("未能找到【原始单据】凭证生成失败!");
                    }
                }
            } else {
                throw new BizException("未能找到【原始单据】凭证生成失败!");
            }
        } else if (originalFormType.equals("6") || originalFormType.equals("7") || originalFormType.equals("7.1")) {

            //0.FAD_INVOICE(发票)
            FadInvoice fadInvoice = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadInvoice.class, businessId) : null;
            if (fadInvoice != null) {
                Map<String, Object> scmContractInvoiceConditionsMap = new HashMap<String, Object>();
                scmContractInvoiceConditionsMap.put(ScmContractInvoiceAttribute.FAD_INVOICE_ID, isNullReturnEmpty(fadInvoice.getUuid()));
                List<ScmContractInvoice> scmContractInvoiceList = isNullReturnEmpty(fadInvoice.getUuid()).length() > 0 ? PersistenceFactory.getInstance
                        ().findByAnyFields(ScmContractInvoice.class, scmContractInvoiceConditionsMap, null) : new ArrayList<>();
                if (scmContractInvoiceList.size() > 0) {
                    for (int yy = 0; yy < scmContractInvoiceList.size(); yy++) {
                        ScmContractInvoice scmContractInvoice = scmContractInvoiceList.get(yy);
                        ScmContract scmContract = isNullReturnEmpty(scmContractInvoice.getScmContractId()).length() > 0 ? PersistenceFactory.getInstance
                                ().findByPK(ScmContract.class, isNullReturnEmpty(scmContractInvoice.getScmContractId())) : null;
                        if (scmContract != null) {
                            if (isNullReturnEmpty(scmContract.getIsProject()).equals("1")) {
                                PrmProjectMain prmProjectMain = isNullReturnEmpty(scmContract.getProjectId()).length() > 0 ? PersistenceFactory.getInstance
                                        ().findByPK(PrmProjectMain.class, isNullReturnEmpty(scmContract.getProjectId())) : null;
                                if (prmProjectMain != null) {

                                    //项目凭证运行费生成规则设定取得
                                    String sql = "SELECT * FROM FAD_PROJECT_RUN_SET_RULE";

                                    String subject = isNullReturnEmpty(scmContract.getSubjectCode());
                                    sql = sql + " WHERE (RUN_SUBJECT = '" + subject + "')";

                                    DAOMeta daoMeta = new DAOMeta();
                                    daoMeta.setStrSql(sql);
                                    daoMeta.setNeedFilter(false);
                                    List fadProjectRunSetRuleList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                                    if (fadProjectRunSetRuleList.size() > 0) {
                                        Map fadProjectRunSetRule = (Map) fadProjectRunSetRuleList.get(0);

                                        //项目凭证生成规则设定取得
                                        sql = "SELECT * FROM FAD_PROJECT_SET_RULE";

                                        String officeId = isNullReturnEmpty(scmContract.getOfficeId());
                                        sql = sql + " WHERE (OFFICE LIKE '%|" + officeId + "|%')";

                                        String projectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                                        sql = sql + " AND (PROJECT_CODE LIKE '%|" + projectCode.substring(5, 7) + "|%' OR PROJECT_CODE LIKE '%|" +
                                                projectCode.substring(5, 8) + "|%' OR PROJECT_CODE LIKE '%|" + projectCode.substring(0, 1) + projectCode.substring(5, 7) + "|%')";

                                        String prmCodeType = isNullReturnEmpty(prmProjectMain.getPrmCodeType());
                                        sql = sql + " AND (PRM_CODE_TYPE = '" + prmCodeType + "')";
                                        sql = sql + " ORDER BY SEQ_NO DESC";

                                        daoMeta = new DAOMeta();
                                        daoMeta.setStrSql(sql);
                                        daoMeta.setNeedFilter(false);
                                        List fadProjectSetRuleList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                                        if (fadProjectSetRuleList.size() > 0) {
                                            for (int A = 0; A < fadProjectSetRuleList.size(); A++) {
                                                Map fadProjectSetRule = (Map) fadProjectSetRuleList.get(A);
                                                for (int i = 0; i < fadCertificateList.size(); i++) {
                                                    FadCertificateDto fadCertificate = (FadCertificateDto) fadCertificateList.get(i);

                                                    List fadCertificateDetailLst2 = new ArrayList<>();
                                                    List<FadCertificateDetailDto> fadCertificateDetailList = fadCertificate.getFadCertificateDetailDto() != null ? fadCertificate.getFadCertificateDetailDto() : new ArrayList<>();
                                                    for (int y = 0; y < fadCertificateDetailList.size(); y++) {
                                                        FadCertificateDetailDto fadCertificateDetail = (FadCertificateDetailDto) fadCertificateDetailList.get(y);
                                                        if ((isNullReturnEmpty(fadCertificateDetail.getFree1()).equals(isNullReturnEmpty(prmProjectMain.getProjectCode()))) || (isNullReturnEmpty(fadCertificateDetail.getFree1()).length() == 0)) {
                                                            if (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("8")) {
                                                                if (isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                                    if (isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).length() >= 6 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject801")).length() >= 6 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).substring(0, 6).equals(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject801")).substring(0, 6))) {
                                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject801")));
                                                                        fadCertificateDetail.setSubjectName(null);
                                                                    } else if (isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).length() >= 6 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80201")).length() >= 6 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).substring(0, 6).equals(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80201")).substring(0, 6))) {
                                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80201")));
                                                                        fadCertificateDetail.setSubjectName(null);
                                                                    } else if (isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).length() >= 6 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80202")).length() >= 6 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).substring(0, 6).equals(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80202")).substring(0, 6))) {
                                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80202")));
                                                                        fadCertificateDetail.setSubjectName(null);
                                                                    } else if (isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).length() >= 6 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80203")).length() >= 6 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).substring(0, 6).equals(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80203")).substring(0, 6))) {
                                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80203")));
                                                                        fadCertificateDetail.setSubjectName(null);
                                                                    } else if (isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).length() >= 6 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject803")).length() >= 6 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).substring(0, 6).equals(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject803")).substring(0, 6))) {
                                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject803")));
                                                                        fadCertificateDetail.setSubjectName(null);
                                                                    } else if (isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).length() >= 6 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject804")).length() >= 6 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).substring(0, 6).equals(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject804")).substring(0, 6))) {
                                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject804")));
                                                                        fadCertificateDetail.setSubjectName(null);
                                                                    } else if (isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).length() >= 6 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject805")).length() >= 6 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).substring(0, 6).equals(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject805")).substring(0, 6))) {
                                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject805")));
                                                                        fadCertificateDetail.setSubjectName(null);
                                                                    } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject801")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).substring(0, 4).equals(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject801")).substring(0, 4))) {
                                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject801")));
                                                                        fadCertificateDetail.setSubjectName(null);
                                                                    } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80201")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).substring(0, 4).equals(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80201")).substring(0, 4))) {
                                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80201")));
                                                                        fadCertificateDetail.setSubjectName(null);
                                                                    } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80202")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).substring(0, 4).equals(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80202")).substring(0, 4))) {
                                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80202")));
                                                                        fadCertificateDetail.setSubjectName(null);
                                                                    } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80203")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).substring(0, 4).equals(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80203")).substring(0, 4))) {
                                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80203")));
                                                                        fadCertificateDetail.setSubjectName(null);
                                                                    } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject803")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).substring(0, 4).equals(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject803")).substring(0, 4))) {
                                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject803")));
                                                                        fadCertificateDetail.setSubjectName(null);
                                                                    } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject804")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).substring(0, 4).equals(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject804")).substring(0, 4))) {
                                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject804")));
                                                                        fadCertificateDetail.setSubjectName(null);
                                                                    } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject805")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")).substring(0, 4).equals(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject805")).substring(0, 4))) {
                                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject805")));
                                                                        fadCertificateDetail.setSubjectName(null);
                                                                    } else {
                                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectSetRule.get("debtorSubject8")));
                                                                        fadCertificateDetail.setSubjectName(null);
                                                                    }
                                                                }

                                                                //加入6位科目识别开始
                                                                else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject801")).length() >= 6 && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).length() >= 6 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject801")).substring(0, 6).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 6))) {
                                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject801")));
                                                                    fadCertificateDetail.setSubjectName(null);
                                                                } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80201")).length() >= 6 && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).length() >= 6 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80201")).substring(0, 6).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 6))) {
                                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80201")));
                                                                    fadCertificateDetail.setSubjectName(null);
                                                                } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80202")).length() >= 6 && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).length() >= 6 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80202")).substring(0, 6).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 6))) {
                                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80202")));
                                                                    fadCertificateDetail.setSubjectName(null);
                                                                } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80203")).length() >= 6 && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).length() >= 6 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80203")).substring(0, 6).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 6))) {
                                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80203")));
                                                                    fadCertificateDetail.setSubjectName(null);
                                                                } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject803")).length() >= 6 && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).length() >= 6 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject803")).substring(0, 6).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 6))) {
                                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject803")));
                                                                    fadCertificateDetail.setSubjectName(null);
                                                                } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject804")).length() >= 6 && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).length() >= 6 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject804")).substring(0, 6).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 6))) {
                                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject804")));
                                                                    fadCertificateDetail.setSubjectName(null);
                                                                } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject805")).length() >= 6 && isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).length() >= 6 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject805")).substring(0, 6).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 6))) {
                                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject805")));
                                                                    fadCertificateDetail.setSubjectName(null);
                                                                }
                                                                //加入6位科目识别结束

                                                                else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject801")).length() > 0 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject801")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject801")));
                                                                    fadCertificateDetail.setSubjectName(null);
                                                                } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80201")).length() > 0 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80201")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80201")));
                                                                    fadCertificateDetail.setSubjectName(null);
                                                                } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80202")).length() > 0 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80202")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80202")));
                                                                    fadCertificateDetail.setSubjectName(null);
                                                                } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80203")).length() > 0 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80203")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject80203")));
                                                                    fadCertificateDetail.setSubjectName(null);
                                                                } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject803")).length() > 0 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject803")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject803")));
                                                                    fadCertificateDetail.setSubjectName(null);
                                                                } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject804")).length() > 0 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject804")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject804")));
                                                                    fadCertificateDetail.setSubjectName(null);
                                                                } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject805")).length() > 0 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject805")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject805")));
                                                                    fadCertificateDetail.setSubjectName(null);
                                                                }
                                                            } else if (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("5") || isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("6") || isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("7")) {
                                                                if (isNullReturnEmpty(fadProjectSetRule.get("debtorSubject6a7")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject6a7")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                                    if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject6a7")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject6a7")).substring(0, 4).equals(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject6a7")).substring(0, 4))) {
                                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject6a7")));
                                                                        fadCertificateDetail.setSubjectName(null);
                                                                    } else {
                                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectSetRule.get("debtorSubject6a7")));
                                                                        fadCertificateDetail.setSubjectName(null);
                                                                    }
                                                                } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject6a7")).length() > 0 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject6a7")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject6a7")));
                                                                    fadCertificateDetail.setSubjectName(null);
                                                                } else if (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("6") || isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("7")) {
                                                                    if (isNullReturnEmpty(fadProjectSetRule.get("creditorSubject6a7")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("creditorSubject6a7")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                                        if (isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject6a7")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("creditorSubject6a7")).substring(0, 4).equals(isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject6a7")).substring(0, 4))) {
                                                                            fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject6a7")));
                                                                            fadCertificateDetail.setSubjectName(null);
                                                                        } else {
                                                                            fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectSetRule.get("creditorSubject6a7")));
                                                                            fadCertificateDetail.setSubjectName(null);
                                                                        }
                                                                    } else if (isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject6a7")).length() > 0 && isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject6a7")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject6a7")));
                                                                        fadCertificateDetail.setSubjectName(null);
                                                                    }
                                                                }
                                                            } else if (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("9")) {
                                                                if (isNullReturnEmpty(fadProjectSetRule.get("debtorSubject9a10")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject9a10")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                                    if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject9a10")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject9a10")).substring(0, 4).equals(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject9a10")).substring(0, 4))) {
                                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject9a10")));
                                                                        fadCertificateDetail.setSubjectName(null);
                                                                    } else {
                                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectSetRule.get("debtorSubject9a10")));
                                                                        fadCertificateDetail.setSubjectName(null);
                                                                    }
                                                                } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject9a10")).length() > 0 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject9a10")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject9a10")));
                                                                    fadCertificateDetail.setSubjectName(null);
                                                                } else if (isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                                    if (isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")).substring(0, 4).equals(isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")).substring(0, 4))) {
                                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")));
                                                                        fadCertificateDetail.setSubjectName(null);
                                                                    } else {
                                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")));
                                                                        fadCertificateDetail.setSubjectName(null);
                                                                    }
                                                                } else if (isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")).length() > 0 && isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                                    fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")));
                                                                    fadCertificateDetail.setSubjectName(null);
                                                                }
                                                            }
                                                            fadCertificateDetailLst2.add(fadCertificateDetail);
                                                        }
                                                    }
                                                    DtoHelper.batchUpdate(fadCertificateDetailLst2, FadCertificateDetail.class);
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    throw new BizException("未能找到【原始单据】凭证生成失败!");
                                }
                            }
                        } else {
                            throw new BizException("未能找到【原始单据】凭证生成失败!");
                        }
                    }
                } else {
                    throw new BizException("未能找到【原始单据】凭证生成失败!");
                }
            } else {
                throw new BizException("未能找到【原始单据】凭证生成失败!");
            }
        } else if (originalFormType.equals("10")) {

            //2.FAD_PAY_REQ_C(支付)
            FadPayReqC fadPayReqC = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadPayReqC.class, businessId) : null;
            if (fadPayReqC != null) {
                if ((isNullReturnEmpty(fadPayReqC.getScmContractId()).length() > 0)) {
                    ScmContract scmContract = isNullReturnEmpty(fadPayReqC.getScmContractId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmContract.class, isNullReturnEmpty(fadPayReqC.getScmContractId())) : null;
                    if (scmContract != null) {
                        if ((isNullReturnEmpty(scmContract.getIsProject()).equals("1"))) {
                            PrmProjectMain prmProjectMain = isNullReturnEmpty(scmContract.getProjectId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(scmContract.getProjectId())) : null;
                            if (prmProjectMain != null) {

                                //项目凭证运行费生成规则设定取得
                                String sql = "SELECT * FROM FAD_PROJECT_RUN_SET_RULE";

                                String subject = isNullReturnEmpty(scmContract.getSubjectCode());
                                sql = sql + " WHERE (RUN_SUBJECT = '" + subject + "')";

                                DAOMeta daoMeta = new DAOMeta();
                                daoMeta.setStrSql(sql);
                                daoMeta.setNeedFilter(false);
                                List fadProjectRunSetRuleList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                                if (fadProjectRunSetRuleList.size() > 0) {
                                    Map fadProjectRunSetRule = (Map) fadProjectRunSetRuleList.get(0);

                                    //项目凭证生成规则设定取得
                                    sql = "SELECT * FROM FAD_PROJECT_SET_RULE";

                                    String officeId = isNullReturnEmpty(scmContract.getOfficeId());
                                    sql = sql + " WHERE (OFFICE LIKE '%|" + officeId + "|%')";

                                    String projectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                                    sql = sql + " AND (PROJECT_CODE LIKE '%|" + projectCode.substring(5, 7) + "|%' OR PROJECT_CODE LIKE '%|" + projectCode.substring(5, 8) + "|%' OR PROJECT_CODE LIKE '%|" + projectCode.substring(0, 1) + projectCode.substring(5, 7) + "|%')";

                                    String prmCodeType = isNullReturnEmpty(prmProjectMain.getPrmCodeType());
                                    sql = sql + " AND (PRM_CODE_TYPE = '" + prmCodeType + "')";
                                    sql = sql + " ORDER BY SEQ_NO DESC";

                                    daoMeta = new DAOMeta();
                                    daoMeta.setStrSql(sql);
                                    daoMeta.setNeedFilter(false);
                                    List fadProjectSetRuleList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                                    if (fadProjectSetRuleList.size() > 0) {
                                        for (int A = 0; A < fadProjectSetRuleList.size(); A++) {
                                            Map fadProjectSetRule = (Map) fadProjectSetRuleList.get(A);
                                            for (int i = 0; i < fadCertificateList.size(); i++) {
                                                FadCertificateDto fadCertificate = (FadCertificateDto) fadCertificateList.get(i);

                                                List fadCertificateDetailLst2 = new ArrayList<>();
                                                List<FadCertificateDetailDto> fadCertificateDetailList = fadCertificate.getFadCertificateDetailDto() != null ? fadCertificate.getFadCertificateDetailDto() : new ArrayList<>();
                                                for (int y = 0; y < fadCertificateDetailList.size(); y++) {
                                                    FadCertificateDetailDto fadCertificateDetail = (FadCertificateDetailDto) fadCertificateDetailList.get(y);
                                                    if (isNullReturnEmpty(fadProjectSetRule.get("debtorSubject9a10")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject9a10")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                        if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject9a10")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("debtorSubject9a10")).substring(0, 4).equals(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject9a10")).substring(0, 4))) {
                                                            fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject9a10")));
                                                            fadCertificateDetail.setSubjectName(null);
                                                        } else {
                                                            fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectSetRule.get("debtorSubject9a10")));
                                                            fadCertificateDetail.setSubjectName(null);
                                                        }
                                                    } else if (isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject9a10")).length() > 0 && isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject9a10")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("debtorSubject9a10")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    } else if (isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                        if (isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")).length() > 0 && isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")).substring(0, 4).equals(isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")).substring(0, 4))) {
                                                            fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")));
                                                            fadCertificateDetail.setSubjectName(null);
                                                        } else {
                                                            fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")));
                                                            fadCertificateDetail.setSubjectName(null);
                                                        }
                                                    } else if (isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")).length() > 0 && isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")).substring(0, 4).equals(isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).substring(0, 4))) {
                                                        fadCertificateDetail.setSubjectCode(isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")));
                                                        fadCertificateDetail.setSubjectName(null);
                                                    }
                                                    fadCertificateDetailLst2.add(fadCertificateDetail);
                                                }
                                                DtoHelper.batchUpdate(fadCertificateDetailLst2, FadCertificateDetail.class);
                                            }
                                        }
                                    }
                                }
                            } else {
                                throw new BizException("未能找到【原始单据】凭证生成失败!");
                            }
                        }
                    } else {
                        throw new BizException("未能找到【原始单据】凭证生成失败!");
                    }
                } else {
                    throw new BizException("未能找到【原始单据】凭证生成失败!");
                }
            } else {
                throw new BizException("未能找到【原始单据】凭证生成失败!");
            }
        }
    }

    @Override
    public void nonProjectSetRuleToUpdateCertificateSetAccountRtfree(String businessId, String originalFormTypeForeign, String originalFormType, List fadCertificateList) {

        /*
        【非项目凭证】涉及的【原始单据类型】取值范围
        1.个人请款(日常/差旅)
        2.材料费或外包商请款(采购合同)
        4.1 无请款报销(采购)
        4 无请款报销(日常/差旅)
        5 有请款报销(日常/差旅)
        5.1 有请款报销(采购有应付)
        5.2 有请款报销(采购有预付)
        6 无请款采购合同
        7 有请款采购合同有应付
        7.1.有请款采购合同有预付
        */

        if (originalFormType.equals("1") || originalFormType.equals("2")) {

            //1.FAD_CASH_REQ(请款)
            FadCashReq fadCashReq = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadCashReq.class, businessId) : null;
            if (fadCashReq != null) {
                if (!(isNullReturnEmpty(fadCashReq.getIsProject()).equals("1"))) {

                    //非项目凭证生成规则设定取得
                    Map<String, Object> fadNonProjectSetRuleConditionsMap = new HashMap<String, Object>();
                    fadNonProjectSetRuleConditionsMap.put(FadNonProjectSetRuleAttribute.OFFICE, isNullReturnEmpty(fadCashReq.getOfficeId()));
                    fadNonProjectSetRuleConditionsMap.put(FadNonProjectSetRuleAttribute.FINANCIAL_SUBJECT, isNullReturnEmpty(fadCashReq.getSubjectCode()));
                    List<FadNonProjectSetRule> fadNonProjectSetRuleList = isNullReturnEmpty(fadCashReq.getOfficeId()).length() > 0 && isNullReturnEmpty(fadCashReq.getSubjectCode()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadNonProjectSetRule.class, fadNonProjectSetRuleConditionsMap, "seq_no desc") : new ArrayList<>();
                    if (fadNonProjectSetRuleList.size() > 0) {
                        FadNonProjectSetRule fadNonProjectSetRule = (FadNonProjectSetRule) fadNonProjectSetRuleList.get(0);
                        for (int i = 0; i < fadCertificateList.size(); i++) {
                            FadCertificateDto fadCertificate = (FadCertificateDto) fadCertificateList.get(i);
                            List<FadCertificateDetailDto> fadCertificateDetailList = fadCertificate.getFadCertificateDetailDto() != null ? fadCertificate.getFadCertificateDetailDto() : new ArrayList<>();
                            for (int y = 0; y < fadCertificateDetailList.size(); y++) {
                                FadCertificateDetailDto fadCertificateDetail = (FadCertificateDetailDto) fadCertificateDetailList.get(y);
                                if (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals(isNullReturnEmpty(fadNonProjectSetRule.getCreditorSubject4a51a1a2()))) {
                                    List fadCertificateAccountLst3 = new ArrayList<>();
                                    List<FadCertificateAccountDto> fadCertificateAccountList = fadCertificateDetail.getFadCertificateAccountDto() != null ? fadCertificateDetail.getFadCertificateAccountDto() : new ArrayList<>();
                                    for (int z = 0; z < fadCertificateAccountList.size(); z++) {
                                        FadCertificateAccountDto fadCertificateAccount = (FadCertificateAccountDto) fadCertificateAccountList.get(z);
                                        if (isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("96")) {
                                            if (isNullReturnEmpty(fadNonProjectSetRule.getCreditorRtfree4a51a1a2()).length() > 0) {
                                                fadCertificateAccount.setAccountInfoId(NCCodeDataSymbol);
                                                fadCertificateAccount.setAccountInfoCode(isNullReturnEmpty(fadNonProjectSetRule.getCreditorRtfree4a51a1a2()));
                                                fadCertificateAccount.setAccountInfoName(null);
                                                fadCertificateAccountLst3.add(fadCertificateAccount);
                                            }
                                        }
                                    }
                                    DtoHelper.batchUpdate(fadCertificateAccountLst3, FadCertificateAccount.class);
                                }
                            }
                        }
                    }
                }
            } else {
                throw new BizException("未能找到【原始单据】凭证生成失败!");
            }
        } else if (originalFormType.equals("4") || originalFormType.equals("4.1") || originalFormType.equals("5") || originalFormType.equals("5.1") || originalFormType.equals("5.2")) {

            //0.FAD_INVOICE(发票)
            FadInvoice fadInvoice = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadInvoice.class, businessId) : null;
            if (fadInvoice != null) {
                if (!(isNullReturnEmpty(fadInvoice.getPrmProjectMainId()).length() > 0)) {

                    //非项目凭证生成规则设定取得
                    List<QueryCondition> fadNonProjectSetRuleConditionsLst = new ArrayList<>();

                    QueryCondition fadNonProjectSetRuleQueryCondition1 = new QueryCondition();
                    fadNonProjectSetRuleQueryCondition1.setField(FadNonProjectSetRuleAttribute.OFFICE);
                    fadNonProjectSetRuleQueryCondition1.setOperator("IN");
                    List fadNonProjectSetRuleValueCondition1List = new ArrayList<>();
                    String[] officeIdArr = isNullReturnEmpty(fadInvoice.getOfficeId()).split(",");
                    for (int i = 0; i < officeIdArr.length; i++) {
                        String officeId = isNullReturnEmpty(officeIdArr[i]);
                        if (officeId.length() > 0) {
                            fadNonProjectSetRuleValueCondition1List.add(officeId);
                        }
                    }
                    fadNonProjectSetRuleQueryCondition1.setValueList(fadNonProjectSetRuleValueCondition1List);
                    fadNonProjectSetRuleConditionsLst.add(fadNonProjectSetRuleQueryCondition1);

                    QueryCondition fadNonProjectSetRuleQueryCondition2 = new QueryCondition();
                    fadNonProjectSetRuleQueryCondition2.setField(FadNonProjectSetRuleAttribute.FINANCIAL_SUBJECT);
                    fadNonProjectSetRuleQueryCondition2.setOperator("=");
                    fadNonProjectSetRuleQueryCondition2.addValue(isNullReturnEmpty(fadInvoice.getSubjectCode()));
                    fadNonProjectSetRuleConditionsLst.add(fadNonProjectSetRuleQueryCondition2);

                    List<FadNonProjectSetRule> fadNonProjectSetRuleList = isNullReturnEmpty(fadInvoice.getOfficeId()).length() > 0 && isNullReturnEmpty(fadInvoice.getSubjectCode()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadNonProjectSetRule.class, fadNonProjectSetRuleConditionsLst, "seq_no desc") : new ArrayList<>();
                    if (fadNonProjectSetRuleList.size() > 0) {
                        FadNonProjectSetRule fadNonProjectSetRule = (FadNonProjectSetRule) fadNonProjectSetRuleList.get(0);
                        for (int i = 0; i < fadCertificateList.size(); i++) {
                            FadCertificateDto fadCertificate = (FadCertificateDto) fadCertificateList.get(i);
                            List<FadCertificateDetailDto> fadCertificateDetailList = fadCertificate.getFadCertificateDetailDto() != null ? fadCertificate.getFadCertificateDetailDto() : new ArrayList<>();
                            for (int y = 0; y < fadCertificateDetailList.size(); y++) {
                                FadCertificateDetailDto fadCertificateDetail = (FadCertificateDetailDto) fadCertificateDetailList.get(y);
                                if (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals(isNullReturnEmpty(fadNonProjectSetRule.getCreditorSubject4a51a1a2()))) {
                                    List fadCertificateAccountLst3 = new ArrayList<>();
                                    List<FadCertificateAccountDto> fadCertificateAccountList = fadCertificateDetail.getFadCertificateAccountDto() != null ? fadCertificateDetail.getFadCertificateAccountDto() : new ArrayList<>();
                                    for (int z = 0; z < fadCertificateAccountList.size(); z++) {
                                        FadCertificateAccountDto fadCertificateAccount = (FadCertificateAccountDto) fadCertificateAccountList.get(z);
                                        if (isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("96")) {
                                            if (isNullReturnEmpty(fadNonProjectSetRule.getCreditorRtfree4a51a1a2()).length() > 0) {
                                                fadCertificateAccount.setAccountInfoId(NCCodeDataSymbol);
                                                fadCertificateAccount.setAccountInfoCode(isNullReturnEmpty(fadNonProjectSetRule.getCreditorRtfree4a51a1a2()));
                                                fadCertificateAccount.setAccountInfoName(null);
                                                fadCertificateAccountLst3.add(fadCertificateAccount);
                                            }
                                        }
                                    }
                                    DtoHelper.batchUpdate(fadCertificateAccountLst3, FadCertificateAccount.class);
                                }
                            }
                        }
                    }
                }
            } else {
                throw new BizException("未能找到【原始单据】凭证生成失败!");
            }
        } else if (originalFormType.equals("6") || originalFormType.equals("7") || originalFormType.equals("7.1")) {

            //0.FAD_INVOICE(发票)
            FadInvoice fadInvoice = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadInvoice.class, businessId) : null;
            if (fadInvoice != null) {
                Map<String, Object> scmContractInvoiceConditionsMap = new HashMap<String, Object>();
                scmContractInvoiceConditionsMap.put(ScmContractInvoiceAttribute.FAD_INVOICE_ID, isNullReturnEmpty(fadInvoice.getUuid()));
                List<ScmContractInvoice> scmContractInvoiceList = isNullReturnEmpty(fadInvoice.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScmContractInvoice.class, scmContractInvoiceConditionsMap, null) : new ArrayList<>();
                if (scmContractInvoiceList.size() > 0) {
                    for (int yy = 0; yy < scmContractInvoiceList.size(); yy++) {
                        ScmContractInvoice scmContractInvoice = scmContractInvoiceList.get(yy);
                        ScmContract scmContract = isNullReturnEmpty(scmContractInvoice.getScmContractId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmContract.class, isNullReturnEmpty(scmContractInvoice.getScmContractId())) : null;
                        if (scmContract != null) {
                            if (!(isNullReturnEmpty(scmContract.getIsProject()).equals("1"))) {

                                //非项目凭证生成规则设定取得
                                Map<String, Object> fadNonProjectSetRuleConditionsMap = new HashMap<String, Object>();
                                fadNonProjectSetRuleConditionsMap.put(FadNonProjectSetRuleAttribute.OFFICE, isNullReturnEmpty(scmContract.getOfficeId()));
                                fadNonProjectSetRuleConditionsMap.put(FadNonProjectSetRuleAttribute.FINANCIAL_SUBJECT, isNullReturnEmpty(scmContract.getSubjectCode()));
                                List<FadNonProjectSetRule> fadNonProjectSetRuleList = isNullReturnEmpty(scmContract.getOfficeId()).length() > 0 && isNullReturnEmpty(scmContract.getSubjectCode()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadNonProjectSetRule.class, fadNonProjectSetRuleConditionsMap, "seq_no desc") : new ArrayList<>();
                                if (fadNonProjectSetRuleList.size() > 0) {
                                    FadNonProjectSetRule fadNonProjectSetRule = (FadNonProjectSetRule) fadNonProjectSetRuleList.get(0);
                                    for (int i = 0; i < fadCertificateList.size(); i++) {
                                        FadCertificateDto fadCertificate = (FadCertificateDto) fadCertificateList.get(i);
                                        List<FadCertificateDetailDto> fadCertificateDetailList = fadCertificate.getFadCertificateDetailDto() != null ? fadCertificate.getFadCertificateDetailDto() : new ArrayList<>();
                                        for (int y = 0; y < fadCertificateDetailList.size(); y++) {
                                            FadCertificateDetailDto fadCertificateDetail = (FadCertificateDetailDto) fadCertificateDetailList.get(y);
                                            if ((isNullReturnEmpty(fadCertificateDetail.getFree1()).equals(isNullReturnEmpty(scmContract.getSubjectCode()))) || (isNullReturnEmpty(fadCertificateDetail.getFree1()).length() == 0)) {
                                                if (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals(isNullReturnEmpty(fadNonProjectSetRule.getCreditorSubject4a51a1a2()))) {
                                                    List fadCertificateAccountLst3 = new ArrayList<>();
                                                    List<FadCertificateAccountDto> fadCertificateAccountList = fadCertificateDetail.getFadCertificateAccountDto() != null ? fadCertificateDetail.getFadCertificateAccountDto() : new ArrayList<>();
                                                    for (int z = 0; z < fadCertificateAccountList.size(); z++) {
                                                        FadCertificateAccountDto fadCertificateAccount = (FadCertificateAccountDto) fadCertificateAccountList.get(z);
                                                        if (isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("96")) {
                                                            if (isNullReturnEmpty(fadNonProjectSetRule.getCreditorRtfree4a51a1a2()).length() > 0) {
                                                                fadCertificateAccount.setAccountInfoId(NCCodeDataSymbol);
                                                                fadCertificateAccount.setAccountInfoCode(isNullReturnEmpty(fadNonProjectSetRule.getCreditorRtfree4a51a1a2()));
                                                                fadCertificateAccount.setAccountInfoName(null);
                                                                fadCertificateAccountLst3.add(fadCertificateAccount);
                                                            }
                                                        }
                                                    }
                                                    DtoHelper.batchUpdate(fadCertificateAccountLst3, FadCertificateAccount.class);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            throw new BizException("未能找到【原始单据】凭证生成失败!");
                        }
                    }
                } else {
                    throw new BizException("未能找到【原始单据】凭证生成失败!");
                }
            } else {
                throw new BizException("未能找到【原始单据】凭证生成失败!");
            }
        } else if (originalFormType.equals("10")) {

            //2.FAD_PAY_REQ_C(支付)
            FadPayReqC fadPayReqC = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadPayReqC.class, businessId) : null;
            if (fadPayReqC != null) {
                if ((isNullReturnEmpty(fadPayReqC.getScmContractId()).length() > 0)) {
                    ScmContract scmContract = isNullReturnEmpty(fadPayReqC.getScmContractId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmContract.class, isNullReturnEmpty(fadPayReqC.getScmContractId())) : null;
                    if (scmContract != null) {
                        if (!(isNullReturnEmpty(scmContract.getIsProject()).equals("1"))) {

                            //非项目凭证生成规则设定取得
                            Map<String, Object> fadNonProjectSetRuleConditionsMap = new HashMap<String, Object>();
                            fadNonProjectSetRuleConditionsMap.put(FadNonProjectSetRuleAttribute.OFFICE, isNullReturnEmpty(scmContract.getOfficeId()));
                            fadNonProjectSetRuleConditionsMap.put(FadNonProjectSetRuleAttribute.FINANCIAL_SUBJECT, isNullReturnEmpty(scmContract.getSubjectCode()));
                            List<FadNonProjectSetRule> fadNonProjectSetRuleList = isNullReturnEmpty(scmContract.getOfficeId()).length() > 0 && isNullReturnEmpty(scmContract.getSubjectCode()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadNonProjectSetRule.class, fadNonProjectSetRuleConditionsMap, "seq_no desc") : new ArrayList<>();
                            if (fadNonProjectSetRuleList.size() > 0) {
                                FadNonProjectSetRule fadNonProjectSetRule = (FadNonProjectSetRule) fadNonProjectSetRuleList.get(0);
                                for (int i = 0; i < fadCertificateList.size(); i++) {
                                    FadCertificateDto fadCertificate = (FadCertificateDto) fadCertificateList.get(i);
                                    List<FadCertificateDetailDto> fadCertificateDetailList = fadCertificate.getFadCertificateDetailDto() != null ? fadCertificate.getFadCertificateDetailDto() : new ArrayList<>();
                                    for (int y = 0; y < fadCertificateDetailList.size(); y++) {
                                        FadCertificateDetailDto fadCertificateDetail = (FadCertificateDetailDto) fadCertificateDetailList.get(y);
                                        if (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals(isNullReturnEmpty(fadNonProjectSetRule.getCreditorSubject4a51a1a2()))) {
                                            List fadCertificateAccountLst3 = new ArrayList<>();
                                            List<FadCertificateAccountDto> fadCertificateAccountList = fadCertificateDetail.getFadCertificateAccountDto() != null ? fadCertificateDetail.getFadCertificateAccountDto() : new ArrayList<>();
                                            for (int z = 0; z < fadCertificateAccountList.size(); z++) {
                                                FadCertificateAccountDto fadCertificateAccount = (FadCertificateAccountDto) fadCertificateAccountList.get(z);
                                                if (isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("96")) {
                                                    if (isNullReturnEmpty(fadNonProjectSetRule.getCreditorRtfree4a51a1a2()).length() > 0) {
                                                        fadCertificateAccount.setAccountInfoId(NCCodeDataSymbol);
                                                        fadCertificateAccount.setAccountInfoCode(isNullReturnEmpty(fadNonProjectSetRule.getCreditorRtfree4a51a1a2()));
                                                        fadCertificateAccount.setAccountInfoName(null);
                                                        fadCertificateAccountLst3.add(fadCertificateAccount);
                                                    }
                                                }
                                            }
                                            DtoHelper.batchUpdate(fadCertificateAccountLst3, FadCertificateAccount.class);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        throw new BizException("未能找到【原始单据】凭证生成失败!");
                    }
                } else {
                    throw new BizException("未能找到【原始单据】凭证生成失败!");
                }
            } else {
                throw new BizException("未能找到【原始单据】凭证生成失败!");
            }
        }
    }

    @Override
    public void projectSetRuleToUpdateCertificateSetAccountRtfree(String businessId, String originalFormTypeForeign, String originalFormType, List fadCertificateList) {

        /*
        【项目凭证】涉及的【原始单据类型】取值范围
        1.个人请款(日常/差旅)
        2.材料费或外包商请款(采购合同)
        4.1 无请款报销(采购)
        4 无请款报销(日常/差旅)
        5 有请款报销(日常/差旅)
        5.1 有请款报销(采购有应付)
        5.2 有请款报销(采购有预付)
        6 无请款采购合同
        7 有请款采购合同有应付
        7.1.有请款采购合同有预付
        */

        if (originalFormType.equals("1") || originalFormType.equals("2")) {

            //1.FAD_CASH_REQ(请款)
            FadCashReq fadCashReq = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadCashReq.class, businessId) : null;
            if (fadCashReq != null) {
                if (isNullReturnEmpty(fadCashReq.getIsProject()).equals("1")) {
                    PrmProjectMain prmProjectMain = isNullReturnEmpty(fadCashReq.getProjectId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(fadCashReq.getProjectId())) : null;
                    if (prmProjectMain != null) {

                        //项目凭证生成规则设定取得
                        String sql = "SELECT * FROM FAD_PROJECT_SET_RULE";

                        String officeId = isNullReturnEmpty(fadCashReq.getOfficeId());
                        sql = sql + " WHERE (OFFICE LIKE '%|" + officeId + "|%')";

                        String subject = isNullReturnEmpty(fadCashReq.getSubjectCode());
                        sql = sql + " AND (SUBJECT = '" + subject + "')";

                        String projectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                        sql = sql + " AND (PROJECT_CODE LIKE '%|" + projectCode.substring(5, 7) + "|%' OR PROJECT_CODE LIKE '%|" + projectCode.substring(5, 8) + "|%' OR PROJECT_CODE LIKE '%|" + projectCode.substring(0, 1) + projectCode.substring(5, 7) + "|%')";

                        String prmCodeType = isNullReturnEmpty(prmProjectMain.getPrmCodeType());
                        sql = sql + " AND (PRM_CODE_TYPE = '" + prmCodeType + "')";

                        DAOMeta daoMeta = new DAOMeta();
                        daoMeta.setStrSql(sql);
                        daoMeta.setNeedFilter(false);
                        List fadProjectSetRuleList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                        if (fadProjectSetRuleList.size() > 0) {
                            Map fadProjectSetRule = (Map) fadProjectSetRuleList.get(0);
                            for (int i = 0; i < fadCertificateList.size(); i++) {
                                FadCertificateDto fadCertificate = (FadCertificateDto) fadCertificateList.get(i);
                                List<FadCertificateDetailDto> fadCertificateDetailList = fadCertificate.getFadCertificateDetailDto() != null ? fadCertificate.getFadCertificateDetailDto() : new ArrayList<>();
                                for (int y = 0; y < fadCertificateDetailList.size(); y++) {
                                    FadCertificateDetailDto fadCertificateDetail = (FadCertificateDetailDto) fadCertificateDetailList.get(y);
                                    if (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals(isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")))) {
                                        List fadCertificateAccountLst3 = new ArrayList<>();
                                        List<FadCertificateAccountDto> fadCertificateAccountList = fadCertificateDetail.getFadCertificateAccountDto() != null ? fadCertificateDetail.getFadCertificateAccountDto() : new ArrayList<>();
                                        for (int z = 0; z < fadCertificateAccountList.size(); z++) {
                                            FadCertificateAccountDto fadCertificateAccount = (FadCertificateAccountDto) fadCertificateAccountList.get(z);
                                            if (isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("96")) {
                                                if (isNullReturnEmpty(fadProjectSetRule.get("creditorRtfree9a10")).length() > 0) {
                                                    fadCertificateAccount.setAccountInfoId(NCCodeDataSymbol);
                                                    fadCertificateAccount.setAccountInfoCode(isNullReturnEmpty(fadProjectSetRule.get("creditorRtfree9a10")));
                                                    fadCertificateAccount.setAccountInfoName(null);
                                                    fadCertificateAccountLst3.add(fadCertificateAccount);
                                                }
                                            }
                                        }
                                        DtoHelper.batchUpdate(fadCertificateAccountLst3, FadCertificateAccount.class);
                                    }
                                }
                            }
                        }
                    } else {
                        throw new BizException("未能找到【原始单据】凭证生成失败!");
                    }
                }
            } else {
                throw new BizException("未能找到【原始单据】凭证生成失败!");
            }
        } else if (originalFormType.equals("4") || originalFormType.equals("4.1") || originalFormType.equals("5") || originalFormType.equals("5.1") || originalFormType.equals("5.2")) {

            //0.FAD_INVOICE(发票)
            FadInvoice fadInvoice = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadInvoice.class, businessId) : null;
            if (fadInvoice != null) {
                if (isNullReturnEmpty(fadInvoice.getPrmProjectMainId()).length() > 0) {
                    PrmProjectMain prmProjectMain = isNullReturnEmpty(fadInvoice.getPrmProjectMainId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(fadInvoice.getPrmProjectMainId())) : null;
                    if (prmProjectMain != null) {

                        //项目凭证生成规则设定取得
                        String sql = "SELECT * FROM FAD_PROJECT_SET_RULE";

                        String sql2 = " WHERE (";
                        String[] officeIdArr = isNullReturnEmpty(fadInvoice.getOfficeId()).split(",");
                        for (int i = 0; i < officeIdArr.length; i++) {
                            String officeId = isNullReturnEmpty(officeIdArr[i]);
                            if (officeId.length() > 0) {
                                if (sql2.equals(" WHERE (")) {
                                    sql2 = sql2 + " (OFFICE LIKE '%|" + officeId + "|%')";
                                } else {
                                    sql2 = sql2 + " OR (OFFICE LIKE '%|" + officeId + "|%')";
                                }
                            }
                        }
                        sql2 = sql2 + " )";
                        sql = sql + sql2;

                        String subject = isNullReturnEmpty(fadInvoice.getSubjectCode());
                        sql = sql + " AND (SUBJECT = '" + subject + "')";

                        String projectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                        sql = sql + " AND (PROJECT_CODE LIKE '%|" + projectCode.substring(5, 7) + "|%' OR PROJECT_CODE LIKE '%|" + projectCode.substring(5, 8) + "|%' OR PROJECT_CODE LIKE '%|" + projectCode.substring(0, 1) + projectCode.substring(5, 7) + "|%')";

                        String prmCodeType = isNullReturnEmpty(prmProjectMain.getPrmCodeType());
                        sql = sql + " AND (PRM_CODE_TYPE = '" + prmCodeType + "')";

                        DAOMeta daoMeta = new DAOMeta();
                        daoMeta.setStrSql(sql);
                        daoMeta.setNeedFilter(false);
                        List fadProjectSetRuleList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                        if (fadProjectSetRuleList.size() > 0) {
                            Map fadProjectSetRule = (Map) fadProjectSetRuleList.get(0);
                            for (int i = 0; i < fadCertificateList.size(); i++) {
                                FadCertificateDto fadCertificate = (FadCertificateDto) fadCertificateList.get(i);
                                List<FadCertificateDetailDto> fadCertificateDetailList = fadCertificate.getFadCertificateDetailDto() != null ? fadCertificate.getFadCertificateDetailDto() : new ArrayList<>();
                                for (int y = 0; y < fadCertificateDetailList.size(); y++) {
                                    FadCertificateDetailDto fadCertificateDetail = (FadCertificateDetailDto) fadCertificateDetailList.get(y);
                                    if (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals(isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")))) {
                                        List fadCertificateAccountLst3 = new ArrayList<>();
                                        List<FadCertificateAccountDto> fadCertificateAccountList = fadCertificateDetail.getFadCertificateAccountDto() != null ? fadCertificateDetail.getFadCertificateAccountDto() : new ArrayList<>();
                                        for (int z = 0; z < fadCertificateAccountList.size(); z++) {
                                            FadCertificateAccountDto fadCertificateAccount = (FadCertificateAccountDto) fadCertificateAccountList.get(z);
                                            if (isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("96")) {
                                                if (isNullReturnEmpty(fadProjectSetRule.get("creditorRtfree9a10")).length() > 0) {
                                                    fadCertificateAccount.setAccountInfoId(NCCodeDataSymbol);
                                                    fadCertificateAccount.setAccountInfoCode(isNullReturnEmpty(fadProjectSetRule.get("creditorRtfree9a10")));
                                                    fadCertificateAccount.setAccountInfoName(null);
                                                    fadCertificateAccountLst3.add(fadCertificateAccount);
                                                }
                                            }
                                        }
                                        DtoHelper.batchUpdate(fadCertificateAccountLst3, FadCertificateAccount.class);
                                    }
                                }
                            }
                        }
                    } else {
                        throw new BizException("未能找到【原始单据】凭证生成失败!");
                    }
                }
            } else {
                throw new BizException("未能找到【原始单据】凭证生成失败!");
            }
        } else if (originalFormType.equals("6") || originalFormType.equals("7") || originalFormType.equals("7.1")) {

            //0.FAD_INVOICE(发票)
            FadInvoice fadInvoice = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadInvoice.class, businessId) : null;
            if (fadInvoice != null) {
                Map<String, Object> scmContractInvoiceConditionsMap = new HashMap<String, Object>();
                scmContractInvoiceConditionsMap.put(ScmContractInvoiceAttribute.FAD_INVOICE_ID, isNullReturnEmpty(fadInvoice.getUuid()));
                List<ScmContractInvoice> scmContractInvoiceList = isNullReturnEmpty(fadInvoice.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScmContractInvoice.class, scmContractInvoiceConditionsMap, null) : new ArrayList<>();
                if (scmContractInvoiceList.size() > 0) {
                    for (int yy = 0; yy < scmContractInvoiceList.size(); yy++) {
                        ScmContractInvoice scmContractInvoice = scmContractInvoiceList.get(yy);
                        ScmContract scmContract = isNullReturnEmpty(scmContractInvoice.getScmContractId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmContract.class, isNullReturnEmpty(scmContractInvoice.getScmContractId())) : null;
                        if (scmContract != null) {
                            if (isNullReturnEmpty(scmContract.getIsProject()).equals("1")) {
                                PrmProjectMain prmProjectMain = isNullReturnEmpty(scmContract.getProjectId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(scmContract.getProjectId())) : null;
                                if (prmProjectMain != null) {

                                    //项目凭证生成规则设定取得
                                    String sql = "SELECT * FROM FAD_PROJECT_SET_RULE";

                                    String officeId = isNullReturnEmpty(scmContract.getOfficeId());
                                    sql = sql + " WHERE (OFFICE LIKE '%|" + officeId + "|%')";

                                    String subject = isNullReturnEmpty(scmContract.getSubjectCode());
                                    sql = sql + " AND (SUBJECT = '" + subject + "')";

                                    String projectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                                    sql = sql + " AND (PROJECT_CODE LIKE '%|" + projectCode.substring(5, 7) + "|%' OR PROJECT_CODE LIKE '%|" + projectCode.substring(5, 8) + "|%' OR PROJECT_CODE LIKE '%|" + projectCode.substring(0, 1) + projectCode.substring(5, 7) + "|%')";

                                    String prmCodeType = isNullReturnEmpty(prmProjectMain.getPrmCodeType());
                                    sql = sql + " AND (PRM_CODE_TYPE = '" + prmCodeType + "')";

                                    DAOMeta daoMeta = new DAOMeta();
                                    daoMeta.setStrSql(sql);
                                    daoMeta.setNeedFilter(false);
                                    List fadProjectSetRuleList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                                    if (fadProjectSetRuleList.size() > 0) {
                                        Map fadProjectSetRule = (Map) fadProjectSetRuleList.get(0);
                                        for (int i = 0; i < fadCertificateList.size(); i++) {
                                            FadCertificateDto fadCertificate = (FadCertificateDto) fadCertificateList.get(i);
                                            List<FadCertificateDetailDto> fadCertificateDetailList = fadCertificate.getFadCertificateDetailDto() != null ? fadCertificate.getFadCertificateDetailDto() : new ArrayList<>();
                                            for (int y = 0; y < fadCertificateDetailList.size(); y++) {
                                                FadCertificateDetailDto fadCertificateDetail = (FadCertificateDetailDto) fadCertificateDetailList.get(y);
                                                if ((isNullReturnEmpty(fadCertificateDetail.getFree1()).equals(isNullReturnEmpty(prmProjectMain.getProjectCode()))) || (isNullReturnEmpty(fadCertificateDetail.getFree1()).length() == 0)) {
                                                    if (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals(isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")))) {
                                                        List fadCertificateAccountLst3 = new ArrayList<>();
                                                        List<FadCertificateAccountDto> fadCertificateAccountList = fadCertificateDetail.getFadCertificateAccountDto() != null ? fadCertificateDetail.getFadCertificateAccountDto() : new ArrayList<>();
                                                        for (int z = 0; z < fadCertificateAccountList.size(); z++) {
                                                            FadCertificateAccountDto fadCertificateAccount = (FadCertificateAccountDto) fadCertificateAccountList.get(z);
                                                            if (isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("96")) {
                                                                if (isNullReturnEmpty(fadProjectSetRule.get("creditorRtfree9a10")).length() > 0) {
                                                                    fadCertificateAccount.setAccountInfoId(NCCodeDataSymbol);
                                                                    fadCertificateAccount.setAccountInfoCode(isNullReturnEmpty(fadProjectSetRule.get("creditorRtfree9a10")));
                                                                    fadCertificateAccount.setAccountInfoName(null);
                                                                    fadCertificateAccountLst3.add(fadCertificateAccount);
                                                                }
                                                            }
                                                        }
                                                        DtoHelper.batchUpdate(fadCertificateAccountLst3, FadCertificateAccount.class);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    throw new BizException("未能找到【原始单据】凭证生成失败!");
                                }
                            }
                        } else {
                            throw new BizException("未能找到【原始单据】凭证生成失败!");
                        }
                    }
                } else {
                    throw new BizException("未能找到【原始单据】凭证生成失败!");
                }
            } else {
                throw new BizException("未能找到【原始单据】凭证生成失败!");
            }
        } else if (originalFormType.equals("10")) {

            //2.FAD_PAY_REQ_C(支付)
            FadPayReqC fadPayReqC = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadPayReqC.class, businessId) : null;
            if (fadPayReqC != null) {
                if ((isNullReturnEmpty(fadPayReqC.getScmContractId()).length() > 0)) {
                    ScmContract scmContract = isNullReturnEmpty(fadPayReqC.getScmContractId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmContract.class, isNullReturnEmpty(fadPayReqC.getScmContractId())) : null;
                    if (scmContract != null) {
                        if ((isNullReturnEmpty(scmContract.getIsProject()).equals("1"))) {
                            PrmProjectMain prmProjectMain = isNullReturnEmpty(scmContract.getProjectId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(scmContract.getProjectId())) : null;
                            if (prmProjectMain != null) {

                                //项目凭证生成规则设定取得
                                String sql = "SELECT * FROM FAD_PROJECT_SET_RULE";

                                String officeId = isNullReturnEmpty(scmContract.getOfficeId());
                                sql = sql + " WHERE (OFFICE LIKE '%|" + officeId + "|%')";

                                String subject = isNullReturnEmpty(scmContract.getSubjectCode());
                                sql = sql + " AND (SUBJECT = '" + subject + "')";

                                String projectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                                sql = sql + " AND (PROJECT_CODE LIKE '%|" + projectCode.substring(5, 7) + "|%' OR PROJECT_CODE LIKE '%|" + projectCode.substring(5, 8) + "|%' OR PROJECT_CODE LIKE '%|" + projectCode.substring(0, 1) + projectCode.substring(5, 7) + "|%')";

                                String prmCodeType = isNullReturnEmpty(prmProjectMain.getPrmCodeType());
                                sql = sql + " AND (PRM_CODE_TYPE = '" + prmCodeType + "')";

                                DAOMeta daoMeta = new DAOMeta();
                                daoMeta.setStrSql(sql);
                                daoMeta.setNeedFilter(false);
                                List fadProjectSetRuleList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                                if (fadProjectSetRuleList.size() > 0) {
                                    Map fadProjectSetRule = (Map) fadProjectSetRuleList.get(0);
                                    for (int i = 0; i < fadCertificateList.size(); i++) {
                                        FadCertificateDto fadCertificate = (FadCertificateDto) fadCertificateList.get(i);
                                        List<FadCertificateDetailDto> fadCertificateDetailList = fadCertificate.getFadCertificateDetailDto() != null ? fadCertificate.getFadCertificateDetailDto() : new ArrayList<>();
                                        for (int y = 0; y < fadCertificateDetailList.size(); y++) {
                                            FadCertificateDetailDto fadCertificateDetail = (FadCertificateDetailDto) fadCertificateDetailList.get(y);
                                            if (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals(isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")))) {
                                                List fadCertificateAccountLst3 = new ArrayList<>();
                                                List<FadCertificateAccountDto> fadCertificateAccountList = fadCertificateDetail.getFadCertificateAccountDto() != null ? fadCertificateDetail.getFadCertificateAccountDto() : new ArrayList<>();
                                                for (int z = 0; z < fadCertificateAccountList.size(); z++) {
                                                    FadCertificateAccountDto fadCertificateAccount = (FadCertificateAccountDto) fadCertificateAccountList.get(z);
                                                    if (isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("96")) {
                                                        if (isNullReturnEmpty(fadProjectSetRule.get("creditorRtfree9a10")).length() > 0) {
                                                            fadCertificateAccount.setAccountInfoId(NCCodeDataSymbol);
                                                            fadCertificateAccount.setAccountInfoCode(isNullReturnEmpty(fadProjectSetRule.get("creditorRtfree9a10")));
                                                            fadCertificateAccount.setAccountInfoName(null);
                                                            fadCertificateAccountLst3.add(fadCertificateAccount);
                                                        }
                                                    }
                                                }
                                                DtoHelper.batchUpdate(fadCertificateAccountLst3, FadCertificateAccount.class);
                                            }
                                        }
                                    }
                                }
                            } else {
                                throw new BizException("未能找到【原始单据】凭证生成失败!");
                            }
                        }
                    } else {
                        throw new BizException("未能找到【原始单据】凭证生成失败!");
                    }
                } else {
                    throw new BizException("未能找到【原始单据】凭证生成失败!");
                }
            } else {
                throw new BizException("未能找到【原始单据】凭证生成失败!");
            }
        }
    }

    @Override
    public void projectRunSetRuleToUpdateCertificateSetAccountRtfree(String businessId, String originalFormTypeForeign, String originalFormType, List fadCertificateList) {

        /*
        【项目凭证】涉及的【原始单据类型】取值范围
        1.个人请款(日常/差旅)
        2.材料费或外包商请款(采购合同)
        4.1 无请款报销(采购)
        4 无请款报销(日常/差旅)
        5 有请款报销(日常/差旅)
        5.1 有请款报销(采购有应付)
        5.2 有请款报销(采购有预付)
        6 无请款采购合同
        7 有请款采购合同有应付
        7.1.有请款采购合同有预付
        */

        if (originalFormType.equals("1") || originalFormType.equals("2")) {

            //1.FAD_CASH_REQ(请款)
            FadCashReq fadCashReq = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadCashReq.class, businessId) : null;
            if (fadCashReq != null) {
                if (isNullReturnEmpty(fadCashReq.getIsProject()).equals("1")) {
                    PrmProjectMain prmProjectMain = isNullReturnEmpty(fadCashReq.getProjectId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(fadCashReq.getProjectId())) : null;
                    if (prmProjectMain != null) {

                        //项目凭证运行费生成规则设定取得
                        String sql = "SELECT * FROM FAD_PROJECT_RUN_SET_RULE";

                        String subject = isNullReturnEmpty(fadCashReq.getSubjectCode());
                        sql = sql + " WHERE (RUN_SUBJECT = '" + subject + "')";

                        DAOMeta daoMeta = new DAOMeta();
                        daoMeta.setStrSql(sql);
                        daoMeta.setNeedFilter(false);
                        List fadProjectRunSetRuleList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                        if (fadProjectRunSetRuleList.size() > 0) {
                            Map fadProjectRunSetRule = (Map) fadProjectRunSetRuleList.get(0);

                            //项目凭证生成规则设定取得
                            sql = "SELECT * FROM FAD_PROJECT_SET_RULE";

                            String officeId = isNullReturnEmpty(fadCashReq.getOfficeId());
                            sql = sql + " WHERE (OFFICE LIKE '%|" + officeId + "|%')";

                            String projectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                            sql = sql + " AND (PROJECT_CODE LIKE '%|" + projectCode.substring(5, 7) + "|%' OR PROJECT_CODE LIKE '%|" + projectCode.substring(5, 8) + "|%' OR PROJECT_CODE LIKE '%|" + projectCode.substring(0, 1) + projectCode.substring(5, 7) + "|%')";

                            String prmCodeType = isNullReturnEmpty(prmProjectMain.getPrmCodeType());
                            sql = sql + " AND (PRM_CODE_TYPE = '" + prmCodeType + "')";
                            sql = sql + " ORDER BY SEQ_NO DESC";

                            daoMeta = new DAOMeta();
                            daoMeta.setStrSql(sql);
                            daoMeta.setNeedFilter(false);
                            List fadProjectSetRuleList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                            if (fadProjectSetRuleList.size() > 0) {
                                for (int A = 0; A < fadProjectSetRuleList.size(); A++) {
                                    Map fadProjectSetRule = (Map) fadProjectSetRuleList.get(A);
                                    for (int i = 0; i < fadCertificateList.size(); i++) {
                                        FadCertificateDto fadCertificate = (FadCertificateDto) fadCertificateList.get(i);
                                        List<FadCertificateDetailDto> fadCertificateDetailList = fadCertificate.getFadCertificateDetailDto() != null ? fadCertificate.getFadCertificateDetailDto() : new ArrayList<>();
                                        for (int y = 0; y < fadCertificateDetailList.size(); y++) {
                                            FadCertificateDetailDto fadCertificateDetail = (FadCertificateDetailDto) fadCertificateDetailList.get(y);
                                            if (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals(isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")))) {
                                                List fadCertificateAccountLst3 = new ArrayList<>();
                                                List<FadCertificateAccountDto> fadCertificateAccountList = fadCertificateDetail.getFadCertificateAccountDto() != null ? fadCertificateDetail.getFadCertificateAccountDto() : new ArrayList<>();
                                                for (int z = 0; z < fadCertificateAccountList.size(); z++) {
                                                    FadCertificateAccountDto fadCertificateAccount = (FadCertificateAccountDto) fadCertificateAccountList.get(z);
                                                    if (isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("96")) {
                                                        if (isNullReturnEmpty(fadProjectRunSetRule.get("creditorRtfree9a10")).length() > 0) {
                                                            fadCertificateAccount.setAccountInfoId(NCCodeDataSymbol);
                                                            fadCertificateAccount.setAccountInfoCode(isNullReturnEmpty(fadProjectRunSetRule.get("creditorRtfree9a10")));
                                                            fadCertificateAccount.setAccountInfoName(null);
                                                            fadCertificateAccountLst3.add(fadCertificateAccount);
                                                        }
                                                    }
                                                }
                                                DtoHelper.batchUpdate(fadCertificateAccountLst3, FadCertificateAccount.class);
                                            } else if (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals(isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")))) {
                                                List fadCertificateAccountLst3 = new ArrayList<>();
                                                List<FadCertificateAccountDto> fadCertificateAccountList = fadCertificateDetail.getFadCertificateAccountDto() != null ? fadCertificateDetail.getFadCertificateAccountDto() : new ArrayList<>();
                                                for (int z = 0; z < fadCertificateAccountList.size(); z++) {
                                                    FadCertificateAccountDto fadCertificateAccount = (FadCertificateAccountDto) fadCertificateAccountList.get(z);
                                                    if (isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("96")) {
                                                        if (isNullReturnEmpty(fadProjectSetRule.get("creditorRtfree9a10")).length() > 0) {
                                                            fadCertificateAccount.setAccountInfoId(NCCodeDataSymbol);
                                                            fadCertificateAccount.setAccountInfoCode(isNullReturnEmpty(fadProjectSetRule.get("creditorRtfree9a10")));
                                                            fadCertificateAccount.setAccountInfoName(null);
                                                            fadCertificateAccountLst3.add(fadCertificateAccount);
                                                        }
                                                    }
                                                }
                                                DtoHelper.batchUpdate(fadCertificateAccountLst3, FadCertificateAccount.class);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        throw new BizException("未能找到【原始单据】凭证生成失败!");
                    }
                }
            } else {
                throw new BizException("未能找到【原始单据】凭证生成失败!");
            }
        } else if (originalFormType.equals("4") || originalFormType.equals("4.1") || originalFormType.equals("5") || originalFormType.equals("5.1") || originalFormType.equals("5.2")) {

            //0.FAD_INVOICE(发票)
            FadInvoice fadInvoice = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadInvoice.class, businessId) : null;
            if (fadInvoice != null) {
                if (isNullReturnEmpty(fadInvoice.getPrmProjectMainId()).length() > 0) {
                    PrmProjectMain prmProjectMain = isNullReturnEmpty(fadInvoice.getPrmProjectMainId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(fadInvoice.getPrmProjectMainId())) : null;
                    if (prmProjectMain != null) {

                        //项目凭证运行费生成规则设定取得
                        String sql = "SELECT * FROM FAD_PROJECT_RUN_SET_RULE";

                        String subject = isNullReturnEmpty(fadInvoice.getSubjectCode());
                        sql = sql + " WHERE (RUN_SUBJECT = '" + subject + "')";

                        DAOMeta daoMeta = new DAOMeta();
                        daoMeta.setStrSql(sql);
                        daoMeta.setNeedFilter(false);
                        List fadProjectRunSetRuleList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                        if (fadProjectRunSetRuleList.size() > 0) {
                            Map fadProjectRunSetRule = (Map) fadProjectRunSetRuleList.get(0);

                            //项目凭证生成规则设定取得
                            sql = "SELECT * FROM FAD_PROJECT_SET_RULE";

                            String sql2 = " WHERE (";
                            String[] officeIdArr = isNullReturnEmpty(fadInvoice.getOfficeId()).split(",");
                            for (int i = 0; i < officeIdArr.length; i++) {
                                String officeId = isNullReturnEmpty(officeIdArr[i]);
                                if (officeId.length() > 0) {
                                    if (sql2.equals(" WHERE (")) {
                                        sql2 = sql2 + " (OFFICE LIKE '%|" + officeId + "|%')";
                                    } else {
                                        sql2 = sql2 + " OR (OFFICE LIKE '%|" + officeId + "|%')";
                                    }
                                }
                            }
                            sql2 = sql2 + " )";
                            sql = sql + sql2;

                            String projectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                            sql = sql + " AND (PROJECT_CODE LIKE '%|" + projectCode.substring(5, 7) + "|%' OR PROJECT_CODE LIKE '%|" + projectCode.substring(5, 8) + "|%' OR PROJECT_CODE LIKE '%|" + projectCode.substring(0, 1) + projectCode.substring(5, 7) + "|%')";

                            String prmCodeType = isNullReturnEmpty(prmProjectMain.getPrmCodeType());
                            sql = sql + " AND (PRM_CODE_TYPE = '" + prmCodeType + "')";
                            sql = sql + " ORDER BY SEQ_NO DESC";

                            daoMeta = new DAOMeta();
                            daoMeta.setStrSql(sql);
                            daoMeta.setNeedFilter(false);
                            List fadProjectSetRuleList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                            if (fadProjectSetRuleList.size() > 0) {
                                for (int A = 0; A < fadProjectSetRuleList.size(); A++) {
                                    Map fadProjectSetRule = (Map) fadProjectSetRuleList.get(A);
                                    for (int i = 0; i < fadCertificateList.size(); i++) {
                                        FadCertificateDto fadCertificate = (FadCertificateDto) fadCertificateList.get(i);
                                        List<FadCertificateDetailDto> fadCertificateDetailList = fadCertificate.getFadCertificateDetailDto() != null ? fadCertificate.getFadCertificateDetailDto() : new ArrayList<>();
                                        for (int y = 0; y < fadCertificateDetailList.size(); y++) {
                                            FadCertificateDetailDto fadCertificateDetail = (FadCertificateDetailDto) fadCertificateDetailList.get(y);
                                            if (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals(isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")))) {
                                                List fadCertificateAccountLst3 = new ArrayList<>();
                                                List<FadCertificateAccountDto> fadCertificateAccountList = fadCertificateDetail.getFadCertificateAccountDto() != null ? fadCertificateDetail.getFadCertificateAccountDto() : new ArrayList<>();
                                                for (int z = 0; z < fadCertificateAccountList.size(); z++) {
                                                    FadCertificateAccountDto fadCertificateAccount = (FadCertificateAccountDto) fadCertificateAccountList.get(z);
                                                    if (isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("96")) {
                                                        if (isNullReturnEmpty(fadProjectRunSetRule.get("creditorRtfree9a10")).length() > 0) {
                                                            fadCertificateAccount.setAccountInfoId(NCCodeDataSymbol);
                                                            fadCertificateAccount.setAccountInfoCode(isNullReturnEmpty(fadProjectRunSetRule.get("creditorRtfree9a10")));
                                                            fadCertificateAccount.setAccountInfoName(null);
                                                            fadCertificateAccountLst3.add(fadCertificateAccount);
                                                        }
                                                    }
                                                }
                                                DtoHelper.batchUpdate(fadCertificateAccountLst3, FadCertificateAccount.class);
                                            } else if (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals(isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")))) {
                                                List fadCertificateAccountLst3 = new ArrayList<>();
                                                List<FadCertificateAccountDto> fadCertificateAccountList = fadCertificateDetail.getFadCertificateAccountDto() != null ? fadCertificateDetail.getFadCertificateAccountDto() : new ArrayList<>();
                                                for (int z = 0; z < fadCertificateAccountList.size(); z++) {
                                                    FadCertificateAccountDto fadCertificateAccount = (FadCertificateAccountDto) fadCertificateAccountList.get(z);
                                                    if (isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("96")) {
                                                        if (isNullReturnEmpty(fadProjectSetRule.get("creditorRtfree9a10")).length() > 0) {
                                                            fadCertificateAccount.setAccountInfoId(NCCodeDataSymbol);
                                                            fadCertificateAccount.setAccountInfoCode(isNullReturnEmpty(fadProjectSetRule.get("creditorRtfree9a10")));
                                                            fadCertificateAccount.setAccountInfoName(null);
                                                            fadCertificateAccountLst3.add(fadCertificateAccount);
                                                        }
                                                    }
                                                }
                                                DtoHelper.batchUpdate(fadCertificateAccountLst3, FadCertificateAccount.class);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        throw new BizException("未能找到【原始单据】凭证生成失败!");
                    }
                }
            } else {
                throw new BizException("未能找到【原始单据】凭证生成失败!");
            }
        } else if (originalFormType.equals("6") || originalFormType.equals("7") || originalFormType.equals("7.1")) {

            //0.FAD_INVOICE(发票)
            FadInvoice fadInvoice = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadInvoice.class, businessId) : null;
            if (fadInvoice != null) {
                Map<String, Object> scmContractInvoiceConditionsMap = new HashMap<String, Object>();
                scmContractInvoiceConditionsMap.put(ScmContractInvoiceAttribute.FAD_INVOICE_ID, isNullReturnEmpty(fadInvoice.getUuid()));
                List<ScmContractInvoice> scmContractInvoiceList = isNullReturnEmpty(fadInvoice.getUuid()).length() > 0 ? PersistenceFactory.getInstance
                        ().findByAnyFields(ScmContractInvoice.class, scmContractInvoiceConditionsMap, null) : new ArrayList<>();
                if (scmContractInvoiceList.size() > 0) {
                    for (int yy = 0; yy < scmContractInvoiceList.size(); yy++) {
                        ScmContractInvoice scmContractInvoice = scmContractInvoiceList.get(yy);
                        ScmContract scmContract = isNullReturnEmpty(scmContractInvoice.getScmContractId()).length() > 0 ? PersistenceFactory.getInstance
                                ().findByPK(ScmContract.class, isNullReturnEmpty(scmContractInvoice.getScmContractId())) : null;
                        if (scmContract != null) {
                            if (isNullReturnEmpty(scmContract.getIsProject()).equals("1")) {
                                PrmProjectMain prmProjectMain = isNullReturnEmpty(scmContract.getProjectId()).length() > 0 ? PersistenceFactory.getInstance
                                        ().findByPK(PrmProjectMain.class, isNullReturnEmpty(scmContract.getProjectId())) : null;
                                if (prmProjectMain != null) {

                                    //项目凭证运行费生成规则设定取得
                                    String sql = "SELECT * FROM FAD_PROJECT_RUN_SET_RULE";

                                    String subject = isNullReturnEmpty(scmContract.getSubjectCode());
                                    sql = sql + " WHERE (RUN_SUBJECT = '" + subject + "')";

                                    DAOMeta daoMeta = new DAOMeta();
                                    daoMeta.setStrSql(sql);
                                    daoMeta.setNeedFilter(false);
                                    List fadProjectRunSetRuleList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                                    if (fadProjectRunSetRuleList.size() > 0) {
                                        Map fadProjectRunSetRule = (Map) fadProjectRunSetRuleList.get(0);

                                        //项目凭证生成规则设定取得
                                        sql = "SELECT * FROM FAD_PROJECT_SET_RULE";

                                        String officeId = isNullReturnEmpty(scmContract.getOfficeId());
                                        sql = sql + " WHERE (OFFICE LIKE '%|" + officeId + "|%')";

                                        String projectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                                        sql = sql + " AND (PROJECT_CODE LIKE '%|" + projectCode.substring(5, 7) + "|%' OR PROJECT_CODE LIKE '%|" +
                                                projectCode.substring(5, 8) + "|%' OR PROJECT_CODE LIKE '%|" + projectCode.substring(0, 1) + projectCode.substring(5, 7) + "|%')";

                                        String prmCodeType = isNullReturnEmpty(prmProjectMain.getPrmCodeType());
                                        sql = sql + " AND (PRM_CODE_TYPE = '" + prmCodeType + "')";
                                        sql = sql + " ORDER BY SEQ_NO DESC";

                                        daoMeta = new DAOMeta();
                                        daoMeta.setStrSql(sql);
                                        daoMeta.setNeedFilter(false);
                                        List fadProjectSetRuleList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                                        if (fadProjectSetRuleList.size() > 0) {
                                            for (int A = 0; A < fadProjectSetRuleList.size(); A++) {
                                                Map fadProjectSetRule = (Map) fadProjectSetRuleList.get(A);
                                                for (int i = 0; i < fadCertificateList.size(); i++) {
                                                    FadCertificateDto fadCertificate = (FadCertificateDto) fadCertificateList.get(i);
                                                    List<FadCertificateDetailDto> fadCertificateDetailList = fadCertificate.getFadCertificateDetailDto() != null ? fadCertificate.getFadCertificateDetailDto() : new ArrayList<>();
                                                    for (int y = 0; y < fadCertificateDetailList.size(); y++) {
                                                        FadCertificateDetailDto fadCertificateDetail = (FadCertificateDetailDto) fadCertificateDetailList.get(y);
                                                        if ((isNullReturnEmpty(fadCertificateDetail.getFree1()).equals(isNullReturnEmpty(prmProjectMain.getProjectCode()))) || (isNullReturnEmpty(fadCertificateDetail.getFree1()).length() == 0)) {
                                                            if (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals(isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")))) {
                                                                List fadCertificateAccountLst3 = new ArrayList<>();
                                                                List<FadCertificateAccountDto> fadCertificateAccountList = fadCertificateDetail.getFadCertificateAccountDto() != null ? fadCertificateDetail.getFadCertificateAccountDto() : new ArrayList<>();
                                                                for (int z = 0; z < fadCertificateAccountList.size(); z++) {
                                                                    FadCertificateAccountDto fadCertificateAccount = (FadCertificateAccountDto) fadCertificateAccountList.get(z);
                                                                    if (isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("96")) {
                                                                        if (isNullReturnEmpty(fadProjectRunSetRule.get("creditorRtfree9a10")).length() > 0) {
                                                                            fadCertificateAccount.setAccountInfoId(NCCodeDataSymbol);
                                                                            fadCertificateAccount.setAccountInfoCode(isNullReturnEmpty(fadProjectRunSetRule.get("creditorRtfree9a10")));
                                                                            fadCertificateAccount.setAccountInfoName(null);
                                                                            fadCertificateAccountLst3.add(fadCertificateAccount);
                                                                        }
                                                                    }
                                                                }
                                                                DtoHelper.batchUpdate(fadCertificateAccountLst3, FadCertificateAccount.class);
                                                            } else if (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals(isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")))) {
                                                                List fadCertificateAccountLst3 = new ArrayList<>();
                                                                List<FadCertificateAccountDto> fadCertificateAccountList = fadCertificateDetail.getFadCertificateAccountDto() != null ? fadCertificateDetail.getFadCertificateAccountDto() : new ArrayList<>();
                                                                for (int z = 0; z < fadCertificateAccountList.size(); z++) {
                                                                    FadCertificateAccountDto fadCertificateAccount = (FadCertificateAccountDto) fadCertificateAccountList.get(z);
                                                                    if (isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("96")) {
                                                                        if (isNullReturnEmpty(fadProjectSetRule.get("creditorRtfree9a10")).length() > 0) {
                                                                            fadCertificateAccount.setAccountInfoId(NCCodeDataSymbol);
                                                                            fadCertificateAccount.setAccountInfoCode(isNullReturnEmpty(fadProjectSetRule.get("creditorRtfree9a10")));
                                                                            fadCertificateAccount.setAccountInfoName(null);
                                                                            fadCertificateAccountLst3.add(fadCertificateAccount);
                                                                        }
                                                                    }
                                                                }
                                                                DtoHelper.batchUpdate(fadCertificateAccountLst3, FadCertificateAccount.class);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    throw new BizException("未能找到【原始单据】凭证生成失败!");
                                }
                            }
                        } else {
                            throw new BizException("未能找到【原始单据】凭证生成失败!");
                        }
                    }
                } else {
                    throw new BizException("未能找到【原始单据】凭证生成失败!");
                }
            } else {
                throw new BizException("未能找到【原始单据】凭证生成失败!");
            }
        } else if (originalFormType.equals("10")) {

            //2.FAD_PAY_REQ_C(支付)
            FadPayReqC fadPayReqC = isNullReturnEmpty(businessId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadPayReqC.class, businessId) : null;
            if (fadPayReqC != null) {
                if ((isNullReturnEmpty(fadPayReqC.getScmContractId()).length() > 0)) {
                    ScmContract scmContract = isNullReturnEmpty(fadPayReqC.getScmContractId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmContract.class, isNullReturnEmpty(fadPayReqC.getScmContractId())) : null;
                    if (scmContract != null) {
                        if ((isNullReturnEmpty(scmContract.getIsProject()).equals("1"))) {
                            PrmProjectMain prmProjectMain = isNullReturnEmpty(scmContract.getProjectId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, isNullReturnEmpty(scmContract.getProjectId())) : null;
                            if (prmProjectMain != null) {

                                //项目凭证运行费生成规则设定取得
                                String sql = "SELECT * FROM FAD_PROJECT_RUN_SET_RULE";

                                String subject = isNullReturnEmpty(scmContract.getSubjectCode());
                                sql = sql + " WHERE (RUN_SUBJECT = '" + subject + "')";

                                DAOMeta daoMeta = new DAOMeta();
                                daoMeta.setStrSql(sql);
                                daoMeta.setNeedFilter(false);
                                List fadProjectRunSetRuleList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                                if (fadProjectRunSetRuleList.size() > 0) {
                                    Map fadProjectRunSetRule = (Map) fadProjectRunSetRuleList.get(0);

                                    //项目凭证生成规则设定取得
                                    sql = "SELECT * FROM FAD_PROJECT_SET_RULE";

                                    String officeId = isNullReturnEmpty(scmContract.getOfficeId());
                                    sql = sql + " WHERE (OFFICE LIKE '%|" + officeId + "|%')";

                                    String projectCode = isNullReturnEmpty(prmProjectMain.getProjectCode());
                                    sql = sql + " AND (PROJECT_CODE LIKE '%|" + projectCode.substring(5, 7) + "|%' OR PROJECT_CODE LIKE '%|" + projectCode.substring(5, 8) + "|%' OR PROJECT_CODE LIKE '%|" + projectCode.substring(0, 1) + projectCode.substring(5, 7) + "|%')";

                                    String prmCodeType = isNullReturnEmpty(prmProjectMain.getPrmCodeType());
                                    sql = sql + " AND (PRM_CODE_TYPE = '" + prmCodeType + "')";
                                    sql = sql + " ORDER BY SEQ_NO DESC";

                                    daoMeta = new DAOMeta();
                                    daoMeta.setStrSql(sql);
                                    daoMeta.setNeedFilter(false);
                                    List fadProjectSetRuleList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                                    if (fadProjectSetRuleList.size() > 0) {
                                        for (int A = 0; A < fadProjectSetRuleList.size(); A++) {
                                            Map fadProjectSetRule = (Map) fadProjectSetRuleList.get(A);
                                            for (int i = 0; i < fadCertificateList.size(); i++) {
                                                FadCertificateDto fadCertificate = (FadCertificateDto) fadCertificateList.get(i);
                                                List<FadCertificateDetailDto> fadCertificateDetailList = fadCertificate.getFadCertificateDetailDto() != null ? fadCertificate.getFadCertificateDetailDto() : new ArrayList<>();
                                                for (int y = 0; y < fadCertificateDetailList.size(); y++) {
                                                    FadCertificateDetailDto fadCertificateDetail = (FadCertificateDetailDto) fadCertificateDetailList.get(y);
                                                    if (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals(isNullReturnEmpty(fadProjectRunSetRule.get("creditorSubject9a10")))) {
                                                        List fadCertificateAccountLst3 = new ArrayList<>();
                                                        List<FadCertificateAccountDto> fadCertificateAccountList = fadCertificateDetail.getFadCertificateAccountDto() != null ? fadCertificateDetail.getFadCertificateAccountDto() : new ArrayList<>();
                                                        for (int z = 0; z < fadCertificateAccountList.size(); z++) {
                                                            FadCertificateAccountDto fadCertificateAccount = (FadCertificateAccountDto) fadCertificateAccountList.get(z);
                                                            if (isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("96")) {
                                                                if (isNullReturnEmpty(fadProjectRunSetRule.get("creditorRtfree9a10")).length() > 0) {
                                                                    fadCertificateAccount.setAccountInfoId(NCCodeDataSymbol);
                                                                    fadCertificateAccount.setAccountInfoCode(isNullReturnEmpty(fadProjectRunSetRule.get("creditorRtfree9a10")));
                                                                    fadCertificateAccount.setAccountInfoName(null);
                                                                    fadCertificateAccountLst3.add(fadCertificateAccount);
                                                                }
                                                            }
                                                        }
                                                        DtoHelper.batchUpdate(fadCertificateAccountLst3, FadCertificateAccount.class);
                                                    } else if (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals(isNullReturnEmpty(fadProjectSetRule.get("creditorSubject9a10")))) {
                                                        List fadCertificateAccountLst3 = new ArrayList<>();
                                                        List<FadCertificateAccountDto> fadCertificateAccountList = fadCertificateDetail.getFadCertificateAccountDto() != null ? fadCertificateDetail.getFadCertificateAccountDto() : new ArrayList<>();
                                                        for (int z = 0; z < fadCertificateAccountList.size(); z++) {
                                                            FadCertificateAccountDto fadCertificateAccount = (FadCertificateAccountDto) fadCertificateAccountList.get(z);
                                                            if (isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("96")) {
                                                                if (isNullReturnEmpty(fadProjectSetRule.get("creditorRtfree9a10")).length() > 0) {
                                                                    fadCertificateAccount.setAccountInfoId(NCCodeDataSymbol);
                                                                    fadCertificateAccount.setAccountInfoCode(isNullReturnEmpty(fadProjectSetRule.get("creditorRtfree9a10")));
                                                                    fadCertificateAccount.setAccountInfoName(null);
                                                                    fadCertificateAccountLst3.add(fadCertificateAccount);
                                                                }
                                                            }
                                                        }
                                                        DtoHelper.batchUpdate(fadCertificateAccountLst3, FadCertificateAccount.class);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                throw new BizException("未能找到【原始单据】凭证生成失败!");
                            }
                        }
                    } else {
                        throw new BizException("未能找到【原始单据】凭证生成失败!");
                    }
                } else {
                    throw new BizException("未能找到【原始单据】凭证生成失败!");
                }
            } else {
                throw new BizException("未能找到【原始单据】凭证生成失败!");
            }
        }
    }

    @Override
    public void certificateSetSystemUpdataField(String businessId, List fadCertificateList) {

        //主表凭证取得
        List fadCertificateDetailLst2 = new ArrayList<>();
        List fadCertificateAccountLst3 = new ArrayList<>();
        //Map<String, Object> fadCertificateConditionsMap = new HashMap<String, Object>();
        //fadCertificateConditionsMap.put(FadCertificateAttribute.BUSINESS_ID, businessId);
        //fadCertificateConditionsMap.put(FadCertificateAttribute.STATE, "0");
        //List fadCertificateList = isNullReturnEmpty(businessId).length() > 0 ? DtoHelper.findByAnyFields(FadCertificateDto.class, fadCertificateConditionsMap, "seq_no asc") : new ArrayList<>();
        for (int i = 0; i < fadCertificateList.size(); i++) {
            FadCertificateDto fadCertificate = (FadCertificateDto) fadCertificateList.get(i);
            fadCertificate.setCreateTime(fadCertificate.getUpdateTime());
            fadCertificate.setCreateBy(fadCertificate.getUpdateBy());

            //子表凭证摘要取得
            List<FadCertificateDetailDto> fadCertificateDetailList = fadCertificate.getFadCertificateDetailDto() != null ? fadCertificate.getFadCertificateDetailDto() : new ArrayList<>();
            for (int y = 0; y < fadCertificateDetailList.size(); y++) {
                FadCertificateDetailDto fadCertificateDetail = (FadCertificateDetailDto) fadCertificateDetailList.get(y);
                fadCertificateDetail.setCreateTime(fadCertificate.getUpdateTime());
                fadCertificateDetail.setCreateBy(fadCertificate.getUpdateBy());
                fadCertificateDetailLst2.add(fadCertificateDetail);

                //分录辅助核算取得
                List<FadCertificateAccountDto> fadCertificateAccountList = fadCertificateDetail.getFadCertificateAccountDto() != null ? fadCertificateDetail.getFadCertificateAccountDto() : new ArrayList<>();
                for (int z = 0; z < fadCertificateAccountList.size(); z++) {
                    FadCertificateAccountDto fadCertificateAccount = (FadCertificateAccountDto) fadCertificateAccountList.get(z);
                    fadCertificateAccountLst3.add(fadCertificateAccount);
                }
            }
        }
        DtoHelper.batchUpdate(fadCertificateList, FadCertificate.class);
        DtoHelper.batchUpdate(fadCertificateDetailLst2, FadCertificateDetail.class);
        DtoHelper.batchUpdate(fadCertificateAccountLst3, FadCertificateAccount.class);
    }

    @Override
    public void ifCertificateLocalZeroThrowException(String businessId) {
        String sql = "";
        DAOMeta daoMeta = null;

        sql = "SELECT 1\n" +
                "      FROM FAD_CERTIFICATE\n" +
                "   WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "     AND ((NVL(DEBTOR, 0) <= 0) AND (NOT((NVL(DEBTOR, 0) < 0) AND ((ORIGINAL_FORM_TYPE = '12') OR (ORIGINAL_FORM_TYPE = '12.2'))))) AND (NVL(IS_VOID, 0) <> 1)";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
            throw new BizException("原凭证的借贷金额不能小于等于0");
        }

        sql = "SELECT 1\n" +
                "      FROM FAD_CERTIFICATE_DETAIL\n" +
                "   WHERE BUSINESS_ID = '" + businessId + "' AND (SELECT STATE FROM FAD_CERTIFICATE WHERE FAD_CERTIFICATE.UUID=FAD_CERTIFICATE_DETAIL.FAD_CERTIFICATE_ID) <> '2'\n" +
                "     AND ((NVL(LOCAL, 0) <= 0) AND (NOT((NVL(LOCAL, 0) < 0) AND ((ORIGINAL_FORM_TYPE = '12') OR (ORIGINAL_FORM_TYPE = '12.2'))))) AND (NVL(IS_VOID, 0) <> 1)";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
            throw new BizException("原凭证的借贷金额不能小于等于0");
        }
    }

    @Override
    public void ifCertificateAlreadyExistThrowException(String businessId) {
        String sql = "";
        DAOMeta daoMeta = null;

        sql = "SELECT 1\n" +
                "      FROM FAD_CERTIFICATE\n" +
                "   WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
            throw new BizException("生成凭证失败,【业务单据】不可重复生成凭证!");
        }
    }

    @Override
    public void createCertificateFromBusinessReqHead(String businessId, String originalFormType) {
        String sql = "";
        DAOMeta daoMeta = null;

        //主表凭证新增
        sql = "INSERT INTO FAD_CERTIFICATE\n" +
                "        (UUID,\n" +
                "         BUSINESS_ID,\n" +
                "         DEBTOR,\n" +
                "         CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE,\n" +
                "         RECEIVER_OR_PAYER_TYPE,\n" +
                "         RECEIVER_OR_PAYER_ID,\n" +
                "         RECEIVER_OR_PAYER_CODE,\n" +
                "         RECEIVER_OR_PAYER_NAME)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               '" + businessId + "',\n" +
                "               NVL(MONEY, 0),\n" +
                "               NVL(MONEY, 0),\n" +
                "               '" + originalFormType + "',\n" +
                "               RUNNING_NO || '(请款流水号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME,\n" +
                "               CASE\n" +
                "                WHEN ((NVL(PAY_STYLE, '0') IN ('1','3','4','5')) AND (SUPPLIER_ID IS NOT NULL OR PAYEE_NAME IS NOT NULL)) OR (STAFF_ID IS NULL) THEN\n" +
                "                 'SCM_SUPPLIER'\n" +
                "                ELSE\n" +
                "                 'SCDP_USER'\n" +
                "               END,\n" +
                "               CASE\n" +
                "                WHEN ((NVL(PAY_STYLE, '0') IN ('1','3','4','5')) AND (SUPPLIER_ID IS NOT NULL OR PAYEE_NAME IS NOT NULL)) OR (STAFF_ID IS NULL) THEN\n" +
                "                 SUPPLIER_ID\n" +
                "                ELSE\n" +
                "                 (SELECT SCDP_USER.UUID FROM SCDP_USER WHERE SCDP_USER.USER_ID = FAD_CASH_REQ.STAFF_ID)\n" +
                "               END,\n" +
                "               CASE\n" +
                "                WHEN ((NVL(PAY_STYLE, '0') IN ('1','3','4','5')) AND (SUPPLIER_ID IS NOT NULL OR PAYEE_NAME IS NOT NULL)) OR (STAFF_ID IS NULL) THEN\n" +
                "                 (SELECT SCM_SUPPLIER.SUPPLIER_CODE FROM SCM_SUPPLIER WHERE SCM_SUPPLIER.UUID = FAD_CASH_REQ.SUPPLIER_ID)\n" +
                "                ELSE\n" +
                "                 STAFF_ID\n" +
                "               END,\n" +
                "               CASE\n" +
                "                WHEN ((NVL(PAY_STYLE, '0') IN ('1','3','4','5')) AND (SUPPLIER_ID IS NOT NULL OR PAYEE_NAME IS NOT NULL)) OR (STAFF_ID IS NULL) THEN\n" +
                "                 NVL((SELECT SCM_SUPPLIER.COMPLETE_NAME FROM SCM_SUPPLIER WHERE SCM_SUPPLIER.UUID = FAD_CASH_REQ.SUPPLIER_ID), FAD_CASH_REQ.PAYEE_NAME)\n" +
                "                ELSE\n" +
                "                 (SELECT SCDP_USER.USER_NAME FROM SCDP_USER WHERE SCDP_USER.USER_ID = FAD_CASH_REQ.STAFF_ID)\n" +
                "               END\n" +
                "          FROM FAD_CASH_REQ\n" +
                "         WHERE UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
    }

    @Override
    public void createCertificateFromBusinessPersonalReq(String businessId, String originalFormType) {
        String sql = "";
        DAOMeta daoMeta = null;

        //请款主表凭证
        createCertificateFromBusinessReqHead(businessId, originalFormType);

        //子表凭证摘要新增
        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               1,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               NVL(MONEY, 0),\n" +
                "               '122102',\n" +
                "               '其他应收款-职工借支',\n" +
                "               '0',\n" +
                "               '" + originalFormType + "', --请款->个人\n" +
                "               RUNNING_NO || '(请款流水号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME\n" +
                "          FROM FAD_CASH_REQ\n" +
                "         WHERE UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               2,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               NVL(MONEY, 0),\n" +
                "               '1002',\n" +
                "               '银行存款',\n" +
                "               '1',\n" +
                "               '" + originalFormType + "', --请款->个人\n" +
                "               RUNNING_NO || '(请款流水号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME\n" +
                "          FROM FAD_CASH_REQ\n" +
                "         WHERE UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
    }

    @Override
    public void createCertificateFromBusinessPurchaseContractReq(String businessId, String originalFormType) {
        String sql = "";
        DAOMeta daoMeta = null;

        //请款主表凭证
        createCertificateFromBusinessReqHead(businessId, originalFormType);

        //子表凭证摘要新增
        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               1,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               NVL(FAD_CASH_REQ.MONEY, 0),\n" +
                "               '1123',\n" +
                "               '预付账款',\n" +
                "               '0',\n" +
                "               '" + originalFormType + "', --请款->材料费或外包商\n" +
                "               SCM_CONTRACT.SCM_CONTRACT_CODE || '(采购合同编号)' || '(' || TO_CHAR(SCM_CONTRACT.CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               SCM_CONTRACT.CREATE_TIME\n" +
                "          FROM FAD_CASH_REQ\n" +
                "          LEFT JOIN SCM_CONTRACT ON FAD_CASH_REQ.PURCHASE_CONTRACT_ID =\n" +
                "                                    SCM_CONTRACT.UUID\n" +
                "         WHERE FAD_CASH_REQ.UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               2,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               NVL(FAD_CASH_REQ.MONEY, 0),\n" +
                "               '1002',\n" +
                "               '银行存款',\n" +
                "               '1',\n" +
                "               '" + originalFormType + "', --请款->材料费或外包商\n" +
                "               FAD_CASH_REQ.RUNNING_NO || '(请款流水号)' || '(' || TO_CHAR(FAD_CASH_REQ.CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               FAD_CASH_REQ.CREATE_TIME\n" +
                "          FROM FAD_CASH_REQ\n" +
                "          LEFT JOIN SCM_CONTRACT ON FAD_CASH_REQ.PURCHASE_CONTRACT_ID =\n" +
                "                                    SCM_CONTRACT.UUID\n" +
                "         WHERE FAD_CASH_REQ.UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
    }

    @Override
    public void createCertificateFromBusinessBondReq(String businessId, String originalFormType) {
        String sql = "";
        DAOMeta daoMeta = null;

        //请款主表凭证
        createCertificateFromBusinessReqHead(businessId, originalFormType);

        //子表凭证摘要新增
        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               1,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               NVL(MONEY, 0),\n" +
                "               '122106',\n" +
                "               '其他应收款-保证金及押金',\n" +
                "               '0',\n" +
                "               '" + originalFormType + "', --请款->保证金\n" +
                "               RUNNING_NO || '(请款流水号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME\n" +
                "          FROM FAD_CASH_REQ\n" +
                "         WHERE UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               2,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               NVL(MONEY, 0),\n" +
                "               '1002',\n" +
                "               '银行存款',\n" +
                "               '1',\n" +
                "               '" + originalFormType + "', --请款->保证金\n" +
                "               RUNNING_NO || '(请款流水号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME\n" +
                "          FROM FAD_CASH_REQ\n" +
                "         WHERE UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
    }

    @Override
    public void createCertificateFromRevolvingReq(String businessId, String originalFormType) {
        String sql = "";
        DAOMeta daoMeta = null;

        //请款主表凭证
        createCertificateFromBusinessReqHead(businessId, originalFormType);

        //子表凭证摘要新增
        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               1,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               NVL(MONEY, 0),\n" +
                "               '122166',\n" +
                "               '其他应收款-周转金',\n" +
                "               '0',\n" +
                "               '" + originalFormType + "', --请款->周转金\n" +
                "               RUNNING_NO || '(请款流水号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME\n" +
                "          FROM FAD_CASH_REQ\n" +
                "         WHERE UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               2,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               NVL(MONEY, 0),\n" +
                "               '1002',\n" +
                "               '银行存款',\n" +
                "               '1',\n" +
                "               '" + originalFormType + "', --请款->周转金\n" +
                "               RUNNING_NO || '(请款流水号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME\n" +
                "          FROM FAD_CASH_REQ\n" +
                "         WHERE UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
    }

    @Override
    public void createCertificateFromBusinessInvoiceHead(String businessId, String originalFormType) {
        String sql = "";
        DAOMeta daoMeta = null;

        //主表凭证新增
        sql = "INSERT INTO FAD_CERTIFICATE\n" +
                "        (UUID,\n" +
                "         BUSINESS_ID,\n" +
                "         DEBTOR,\n" +
                "         CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE,\n" +
                "         RECEIVER_OR_PAYER_TYPE,\n" +
                "         RECEIVER_OR_PAYER_ID,\n" +
                "         RECEIVER_OR_PAYER_CODE,\n" +
                "         RECEIVER_OR_PAYER_NAME)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               '" + businessId + "',\n" +
                "               NVL(EXPENSES_MONEY, 0),\n" +
                "               NVL(EXPENSES_MONEY, 0),\n" +
                "               '" + originalFormType + "',\n" +
                "               INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME,\n" +
                "               CASE\n" +
                "                WHEN NVL(PAY_STYLE, '0') IN ('1','3','4','5') THEN\n" +
                "                 'SCM_SUPPLIER'\n" +
                "                ELSE\n" +
                "                 'SCDP_USER'\n" +
                "               END,\n" +
                "               CASE\n" +
                "                WHEN NVL(PAY_STYLE, '0') IN ('1','3','4','5') THEN\n" +
                "                 SUPPLIER_ID\n" +
                "                ELSE\n" +
                "                 (SELECT SCDP_USER.UUID FROM SCDP_USER WHERE SCDP_USER.USER_ID = FAD_INVOICE.RENDER_PERSON)\n" +
                "               END,\n" +
                "               CASE\n" +
                "                WHEN NVL(PAY_STYLE, '0') IN ('1','3','4','5') THEN\n" +
                "                 (SELECT SCM_SUPPLIER.SUPPLIER_CODE FROM SCM_SUPPLIER WHERE SCM_SUPPLIER.UUID = FAD_INVOICE.SUPPLIER_ID)\n" +
                "                ELSE\n" +
                "                 RENDER_PERSON\n" +
                "               END,\n" +
                "               CASE\n" +
                "                WHEN NVL(PAY_STYLE, '0') IN ('1','3','4','5') THEN\n" +
                "                 NVL((SELECT SCM_SUPPLIER.COMPLETE_NAME FROM SCM_SUPPLIER WHERE SCM_SUPPLIER.UUID = FAD_INVOICE.SUPPLIER_ID), FAD_INVOICE.SUPPLIER_NAME)\n" +
                "                ELSE\n" +
                "                 CASE WHEN PAYEE IS NULL THEN (SELECT SCDP_USER.USER_NAME FROM SCDP_USER WHERE SCDP_USER.USER_ID = FAD_INVOICE.RENDER_PERSON) ELSE PAYEE END\n" +
                "               END\n" +
                "          FROM FAD_INVOICE\n" +
                "         WHERE UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
    }

    @Override
    public void createCertificateFromBusinessNoPaymentReimbursementInvoice(String businessId, String originalFormType) {
        String sql = "";
        DAOMeta daoMeta = null;

        //报销与采购合同发票主表凭证
        createCertificateFromBusinessInvoiceHead(businessId, originalFormType);

        //子表凭证摘要新增
        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               1,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               NVL(EXPENSES_MONEY, 0),\n" +
                "               '540101',\n" +
                "               '工程施工-合同成本',\n" +
                "               '0',\n" +
                "               '" + originalFormType + "', --报销->无请款\n" +
                "               INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME\n" +
                "          FROM FAD_INVOICE\n" +
                "         WHERE UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               2,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               NVL(EXPENSES_MONEY, 0),\n" +
                "               CASE WHEN PAY_STYLE = '0' THEN '1001' ELSE '1002' END,\n" +
                "               CASE WHEN PAY_STYLE = '0' THEN '库存现金' ELSE '银行存款' END,\n" +
                "               '1',\n" +
                "               '" + originalFormType + "', --报销->无请款\n" +
                "               INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME\n" +
                "          FROM FAD_INVOICE\n" +
                "         WHERE UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
    }

    @Override
    public void createCertificateFromBusinessPaymentReimbursementInvoice(String businessId, String originalFormType) {
        String sql = "";
        DAOMeta daoMeta = null;

        //判断报销金额是否大于请款金额
        sql = "SELECT 1\n" +
                "      FROM (SELECT INVOICE_TYPE,\n" +
                "                   EXPENSES_TYPE,\n" +
                "                   INVOICE_REQ_NO,\n" +
                "                   CREATE_TIME,\n" +
                "                   TAX_RATE,\n" +
                "                   DEBTOR,\n" +
                "                   CREDITOR,\n" +
                "                   SUM(CLEARANCE_MONEY) AS SUM_CLEARANCE_MONEY\n" +
                "              FROM (SELECT FAD_INVOICE.INVOICE_TYPE,\n" +
                "                           FAD_INVOICE.EXPENSES_TYPE,\n" +
                "                           FAD_INVOICE.INVOICE_REQ_NO,\n" +
                "                           FAD_INVOICE.CREATE_TIME,\n" +
                "                           NVL(FAD_INVOICE.TAX_RATE, 0) AS TAX_RATE,\n" +
                "                           NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS DEBTOR,\n" +
                "                           NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS CREDITOR,\n" +
                "                           NVL(FAD_CASH_REQ_INVOICE.CLEARANCE_MONEY, 0) AS CLEARANCE_MONEY\n" +
                "                      FROM FAD_INVOICE\n" +
                "                      LEFT JOIN FAD_CASH_REQ_INVOICE ON FAD_INVOICE.UUID =\n" +
                "                                                        FAD_CASH_REQ_INVOICE.FAD_INVOICE_ID\n" +
                "                     WHERE FAD_INVOICE.UUID = '" + businessId + "') A\n" +
                "             GROUP BY INVOICE_TYPE,\n" +
                "                      EXPENSES_TYPE,\n" +
                "                      INVOICE_REQ_NO,\n" +
                "                      CREATE_TIME,\n" +
                "                      TAX_RATE,\n" +
                "                      DEBTOR,\n" +
                "                      CREDITOR) A\n" +
                "     WHERE (CREDITOR - SUM_CLEARANCE_MONEY) > 0";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);

        //报销金额不大于请款金额
        if ((!(PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0))) {

            //报销与采购合同发票主表凭证
            createCertificateFromBusinessInvoiceHead(businessId, originalFormType);

            //子表凭证摘要新增
            sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                    "        (UUID,\n" +
                    "         FAD_CERTIFICATE_ID,\n" +
                    "         BUSINESS_ID,\n" +
                    "         ORDER_NO,\n" +
                    "         CURRENCY,\n" +
                    "         CURRTYPENAME,\n" +
                    "         LOCAL,\n" +
                    "         SUBJECT_CODE,\n" +
                    "         SUBJECT_NAME,\n" +
                    "         DEBTOR_OR_CREDITOR,\n" +
                    "         ORIGINAL_FORM_TYPE,\n" +
                    "         ORIGINAL_FORM_CODE,\n" +
                    "         ORIGINAL_FORM_DATE)\n" +
                    "        SELECT SYS_GUID(),\n" +
                    "               (SELECT UUID\n" +
                    "                  FROM FAD_CERTIFICATE\n" +
                    "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                    "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                    "               '" + businessId + "',\n" +
                    "               1,\n" +
                    "               'CNY',\n" +
                    "               '人民币元',\n" +
                    "               NVL(EXPENSES_MONEY, 0),\n" +
                    "               '540101',\n" +
                    "               '工程施工-合同成本',\n" +
                    "               '0',\n" +
                    "               '" + originalFormType + "', --报销->有请款\n" +
                    "               INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                    "               CREATE_TIME\n" +
                    "          FROM FAD_INVOICE\n" +
                    "         WHERE UUID = '" + businessId + "'";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

            sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                    "        (UUID,\n" +
                    "         FAD_CERTIFICATE_ID,\n" +
                    "         BUSINESS_ID,\n" +
                    "         ORDER_NO,\n" +
                    "         CURRENCY,\n" +
                    "         CURRTYPENAME,\n" +
                    "         LOCAL,\n" +
                    "         SUBJECT_CODE,\n" +
                    "         SUBJECT_NAME,\n" +
                    "         DEBTOR_OR_CREDITOR,\n" +
                    "         ORIGINAL_FORM_TYPE,\n" +
                    "         ORIGINAL_FORM_CODE,\n" +
                    "         ORIGINAL_FORM_DATE)\n" +
                    "        SELECT SYS_GUID(),\n" +
                    "               (SELECT UUID\n" +
                    "                  FROM FAD_CERTIFICATE\n" +
                    "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                    "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                    "               '" + businessId + "',\n" +
                    "               (SELECT NVL(MAX(ORDER_NO),0)\n" +
                    "                  FROM FAD_CERTIFICATE_DETAIL\n" +
                    "                 WHERE FAD_CERTIFICATE_ID =\n" +
                    "                       (SELECT UUID\n" +
                    "                          FROM FAD_CERTIFICATE\n" +
                    "                         WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                    "                           AND ORIGINAL_FORM_TYPE = '" + originalFormType + "')) +\n" +
                    "               ROWNUM,\n" +
                    "               'CNY',\n" +
                    "               '人民币元',\n" +
                    "               NVL(FAD_CASH_REQ_INVOICE.CLEARANCE_MONEY, 0),\n" +
                    "               CASE\n" +
                    "                 WHEN (REQ_TYPE = '1' OR REQ_TYPE = '2') THEN\n" +
                    "                  '122102'\n" +
                    "                 WHEN REQ_TYPE = '3' THEN\n" +
                    "                  '122106'\n" +
                    "                 WHEN REQ_TYPE = '4' THEN\n" +
                    "                  '122166'\n" +
                    "                 ELSE\n" +
                    "                  ''\n" +
                    "               END,\n" +
                    "               CASE\n" +
                    "                 WHEN (REQ_TYPE = '1' OR REQ_TYPE = '2') THEN\n" +
                    "                  '其他应收款-职工借支'\n" +
                    "                 WHEN REQ_TYPE = '3' THEN\n" +
                    "                  '其他应收款-保证金及押金'\n" +
                    "                 WHEN REQ_TYPE = '4' THEN\n" +
                    "                  '其他应收款-周转金'\n" +
                    "                 ELSE\n" +
                    "                  ''\n" +
                    "               END,\n" +
                    "               '1',\n" +
                    "               '" + originalFormType + "', --报销->有请款\n" +
                    "               FAD_CASH_REQ.RUNNING_NO || '(请款流水号)' || '(' || TO_CHAR(FAD_CASH_REQ_INVOICE.CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                    "               FAD_CASH_REQ_INVOICE.CREATE_TIME\n" +
                    "          FROM FAD_INVOICE\n" +
                    "          LEFT JOIN FAD_CASH_REQ_INVOICE ON FAD_INVOICE.UUID =\n" +
                    "                                            FAD_CASH_REQ_INVOICE.FAD_INVOICE_ID\n" +
                    "          LEFT JOIN FAD_CASH_REQ ON FAD_CASH_REQ_INVOICE.FAD_CASH_REQ_ID =\n" +
                    "                                    FAD_CASH_REQ.UUID\n" +
                    "         WHERE FAD_INVOICE.UUID = '" + businessId + "'";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
        }

        //报销金额大于请款金额
        else {

            //第一张成本凭证
            //报销与采购合同发票主表凭证
            createCertificateFromBusinessInvoiceHead(businessId, originalFormType);

            //子表凭证摘要新增
            sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                    "        (UUID,\n" +
                    "         FAD_CERTIFICATE_ID,\n" +
                    "         BUSINESS_ID,\n" +
                    "         ORDER_NO,\n" +
                    "         CURRENCY,\n" +
                    "         CURRTYPENAME,\n" +
                    "         LOCAL,\n" +
                    "         SUBJECT_CODE,\n" +
                    "         SUBJECT_NAME,\n" +
                    "         DEBTOR_OR_CREDITOR,\n" +
                    "         ORIGINAL_FORM_TYPE,\n" +
                    "         ORIGINAL_FORM_CODE,\n" +
                    "         ORIGINAL_FORM_DATE)\n" +
                    "        SELECT SYS_GUID(),\n" +
                    "               (SELECT UUID\n" +
                    "                  FROM FAD_CERTIFICATE\n" +
                    "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                    "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                    "               '" + businessId + "',\n" +
                    "               1,\n" +
                    "               'CNY',\n" +
                    "               '人民币元',\n" +
                    "               NVL(EXPENSES_MONEY, 0),\n" +
                    "               '540101',\n" +
                    "               '工程施工-合同成本',\n" +
                    "               '0',\n" +
                    "               '" + originalFormType + "', --报销->有请款\n" +
                    "               INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                    "               CREATE_TIME\n" +
                    "          FROM FAD_INVOICE\n" +
                    "         WHERE UUID = '" + businessId + "'";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

            sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                    "        (UUID,\n" +
                    "         FAD_CERTIFICATE_ID,\n" +
                    "         BUSINESS_ID,\n" +
                    "         ORDER_NO,\n" +
                    "         CURRENCY,\n" +
                    "         CURRTYPENAME,\n" +
                    "         LOCAL,\n" +
                    "         SUBJECT_CODE,\n" +
                    "         SUBJECT_NAME,\n" +
                    "         DEBTOR_OR_CREDITOR,\n" +
                    "         ORIGINAL_FORM_TYPE,\n" +
                    "         ORIGINAL_FORM_CODE,\n" +
                    "         ORIGINAL_FORM_DATE)\n" +
                    "        SELECT SYS_GUID(),\n" +
                    "               (SELECT UUID\n" +
                    "                  FROM FAD_CERTIFICATE\n" +
                    "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                    "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                    "               '" + businessId + "',\n" +
                    "               (SELECT NVL(MAX(ORDER_NO),0)\n" +
                    "                  FROM FAD_CERTIFICATE_DETAIL\n" +
                    "                 WHERE FAD_CERTIFICATE_ID =\n" +
                    "                       (SELECT UUID\n" +
                    "                          FROM FAD_CERTIFICATE\n" +
                    "                         WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                    "                           AND ORIGINAL_FORM_TYPE = '" + originalFormType + "')) +\n" +
                    "               ROWNUM,\n" +
                    "               'CNY',\n" +
                    "               '人民币元',\n" +
                    "               NVL(FAD_CASH_REQ_INVOICE.CLEARANCE_MONEY, 0),\n" +
                    "               CASE\n" +
                    "                 WHEN (REQ_TYPE = '1' OR REQ_TYPE = '2') THEN\n" +
                    "                  '122102'\n" +
                    "                 WHEN REQ_TYPE = '3' THEN\n" +
                    "                  '122106'\n" +
                    "                 WHEN REQ_TYPE = '4' THEN\n" +
                    "                  '122166'\n" +
                    "                 ELSE\n" +
                    "                  ''\n" +
                    "               END,\n" +
                    "               CASE\n" +
                    "                 WHEN (REQ_TYPE = '1' OR REQ_TYPE = '2') THEN\n" +
                    "                  '其他应收款-职工借支'\n" +
                    "                 WHEN REQ_TYPE = '3' THEN\n" +
                    "                  '其他应收款-保证金及押金'\n" +
                    "                 WHEN REQ_TYPE = '4' THEN\n" +
                    "                  '其他应收款-周转金'\n" +
                    "                 ELSE\n" +
                    "                  ''\n" +
                    "               END,\n" +
                    "               '1',\n" +
                    "               '" + originalFormType + "', --报销->有请款\n" +
                    "               FAD_CASH_REQ.RUNNING_NO || '(请款流水号)' || '(' || TO_CHAR(FAD_CASH_REQ_INVOICE.CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                    "               FAD_CASH_REQ_INVOICE.CREATE_TIME\n" +
                    "          FROM FAD_INVOICE\n" +
                    "          LEFT JOIN FAD_CASH_REQ_INVOICE ON FAD_INVOICE.UUID =\n" +
                    "                                            FAD_CASH_REQ_INVOICE.FAD_INVOICE_ID\n" +
                    "          LEFT JOIN FAD_CASH_REQ ON FAD_CASH_REQ_INVOICE.FAD_CASH_REQ_ID =\n" +
                    "                                    FAD_CASH_REQ.UUID\n" +
                    "         WHERE FAD_INVOICE.UUID = '" + businessId + "'";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

            sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                    "        (UUID,\n" +
                    "         FAD_CERTIFICATE_ID,\n" +
                    "         BUSINESS_ID,\n" +
                    "         ORDER_NO,\n" +
                    "         CURRENCY,\n" +
                    "         CURRTYPENAME,\n" +
                    "         LOCAL,\n" +
                    "         SUBJECT_CODE,\n" +
                    "         SUBJECT_NAME,\n" +
                    "         DEBTOR_OR_CREDITOR,\n" +
                    "         ORIGINAL_FORM_TYPE,\n" +
                    "         ORIGINAL_FORM_CODE,\n" +
                    "         ORIGINAL_FORM_DATE,\n" +
                    "         SEQ_NO)\n" +
                    "        SELECT SYS_GUID(),\n" +
                    "               (SELECT UUID\n" +
                    "                  FROM FAD_CERTIFICATE\n" +
                    "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                    "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                    "               '" + businessId + "',\n" +
                    "               (SELECT NVL(MAX(ORDER_NO),0)\n" +
                    "                  FROM FAD_CERTIFICATE_DETAIL\n" +
                    "                 WHERE FAD_CERTIFICATE_ID =\n" +
                    "                       (SELECT UUID\n" +
                    "                          FROM FAD_CERTIFICATE\n" +
                    "                         WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                    "                           AND ORIGINAL_FORM_TYPE = '" + originalFormType + "')) +\n" +
                    "               ROWNUM,\n" +
                    "               'CNY',\n" +
                    "               '人民币元',\n" +
                    "               CREDITOR - SUM_CLEARANCE_MONEY,\n" +
                    "               '2241',\n" +
                    "               '其他应付款',\n" +
                    "               '1',\n" +
                    "               '" + originalFormType + "', --报销->有请款\n" +
                    "               INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                    "               CREATE_TIME,\n" +
                    "               FAD_CERTIFICATE_SEQ_NO.NEXTVAL\n" +
                    "          FROM (SELECT INVOICE_TYPE,\n" +
                    "                       INVOICE_REQ_NO,\n" +
                    "                       PAY_STYLE,\n" +
                    "                       PRM_PROJECT_MAIN_ID,\n" +
                    "                       SUBJECT_CODE,\n" +
                    "                       SUBJECT_NAME,\n" +
                    "                       OFFICE_ID,\n" +
                    "                       OFFICE_NAME,\n" +
                    "                       PAYEE,\n" +
                    "                       RENDER_PERSON,\n" +
                    "                       SUPPLIER_ID,\n" +
                    "                       SUPPLIER_NAME,\n" +
                    "                       CREATE_TIME,\n" +
                    "                       DEBTOR,\n" +
                    "                       CREDITOR,\n" +
                    "                       SUM(CLEARANCE_MONEY) AS SUM_CLEARANCE_MONEY\n" +
                    "                  FROM (SELECT FAD_INVOICE.INVOICE_TYPE,\n" +
                    "                               FAD_INVOICE.INVOICE_REQ_NO,\n" +
                    "                               FAD_INVOICE.PAY_STYLE,\n" +
                    "                               FAD_INVOICE.PRM_PROJECT_MAIN_ID,\n" +
                    "                               FAD_INVOICE.SUBJECT_CODE,\n" +
                    "                               FAD_INVOICE.SUBJECT_NAME,\n" +
                    "                               FAD_INVOICE.OFFICE_ID,\n" +
                    "                               FAD_INVOICE.OFFICE_NAME,\n" +
                    "                               FAD_INVOICE.PAYEE,\n" +
                    "                               FAD_INVOICE.RENDER_PERSON,\n" +
                    "                               FAD_INVOICE.SUPPLIER_ID,\n" +
                    "                               FAD_INVOICE.SUPPLIER_NAME,\n" +
                    "                               FAD_INVOICE.CREATE_TIME,\n" +
                    "                               NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS DEBTOR,\n" +
                    "                               NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS CREDITOR,\n" +
                    "                               NVL(FAD_CASH_REQ_INVOICE.CLEARANCE_MONEY, 0) AS CLEARANCE_MONEY\n" +
                    "                          FROM FAD_INVOICE\n" +
                    "                          LEFT JOIN FAD_CASH_REQ_INVOICE ON FAD_INVOICE.UUID =\n" +
                    "                                                            FAD_CASH_REQ_INVOICE.FAD_INVOICE_ID\n" +
                    "                         WHERE FAD_INVOICE.UUID = '" + businessId + "') A\n" +
                    "                 GROUP BY INVOICE_TYPE,\n" +
                    "                          INVOICE_REQ_NO,\n" +
                    "                          PAY_STYLE,\n" +
                    "                          PRM_PROJECT_MAIN_ID,\n" +
                    "                          SUBJECT_CODE,\n" +
                    "                          SUBJECT_NAME,\n" +
                    "                          OFFICE_ID,\n" +
                    "                          OFFICE_NAME,\n" +
                    "                          PAYEE,\n" +
                    "                          RENDER_PERSON,\n" +
                    "                          SUPPLIER_ID,\n" +
                    "                          SUPPLIER_NAME,\n" +
                    "                          CREATE_TIME,\n" +
                    "                          DEBTOR,\n" +
                    "                          CREDITOR) A";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

            //第二张其他应付冲银行存款凭证
            //主表凭证新增
            sql = "INSERT INTO FAD_CERTIFICATE\n" +
                    "        (UUID,\n" +
                    "         BUSINESS_ID,\n" +
                    "         DEBTOR,\n" +
                    "         CREDITOR,\n" +
                    "         ORIGINAL_FORM_TYPE,\n" +
                    "         ORIGINAL_FORM_CODE,\n" +
                    "         ORIGINAL_FORM_DATE,\n" +
                    "         RECEIVER_OR_PAYER_TYPE,\n" +
                    "         RECEIVER_OR_PAYER_ID,\n" +
                    "         RECEIVER_OR_PAYER_CODE,\n" +
                    "         RECEIVER_OR_PAYER_NAME,\n" +
                    "         SEQ_NO)\n" +
                    "        SELECT SYS_GUID(),\n" +
                    "               '" + businessId + "',\n" +
                    "               DEBTOR - SUM_CLEARANCE_MONEY,\n" +
                    "               CREDITOR - SUM_CLEARANCE_MONEY,\n" +
                    "               '" + originalFormType + ".1',\n" +
                    "               INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                    "               CREATE_TIME,\n" +
                    "               CASE\n" +
                    "                WHEN NVL(PAY_STYLE, '0') IN ('1','3','4','5') THEN\n" +
                    "                 'SCM_SUPPLIER'\n" +
                    "                ELSE\n" +
                    "                 'SCDP_USER'\n" +
                    "               END,\n" +
                    "               CASE\n" +
                    "                WHEN NVL(PAY_STYLE, '0') IN ('1','3','4','5') THEN\n" +
                    "                 SUPPLIER_ID\n" +
                    "                ELSE\n" +
                    "                 (SELECT SCDP_USER.UUID FROM SCDP_USER WHERE SCDP_USER.USER_ID = A.RENDER_PERSON)\n" +
                    "               END,\n" +
                    "               CASE\n" +
                    "                WHEN NVL(PAY_STYLE, '0') IN ('1','3','4','5') THEN\n" +
                    "                 (SELECT SCM_SUPPLIER.SUPPLIER_CODE FROM SCM_SUPPLIER WHERE SCM_SUPPLIER.UUID = A.SUPPLIER_ID)\n" +
                    "                ELSE\n" +
                    "                 RENDER_PERSON\n" +
                    "               END,\n" +
                    "               CASE\n" +
                    "                WHEN NVL(PAY_STYLE, '0') IN ('1','3','4','5') THEN\n" +
                    "                 NVL((SELECT SCM_SUPPLIER.COMPLETE_NAME FROM SCM_SUPPLIER WHERE SCM_SUPPLIER.UUID = A.SUPPLIER_ID), A.SUPPLIER_NAME)\n" +
                    "                ELSE\n" +
                    "                 CASE WHEN PAYEE IS NULL THEN (SELECT SCDP_USER.USER_NAME FROM SCDP_USER WHERE SCDP_USER.USER_ID = A.RENDER_PERSON) ELSE PAYEE END\n" +
                    "               END,\n" +
                    "               FAD_CERTIFICATE_SEQ_NO.NEXTVAL\n" +
                    "          FROM (SELECT INVOICE_TYPE,\n" +
                    "                       INVOICE_REQ_NO,\n" +
                    "                       PAY_STYLE,\n" +
                    "                       PRM_PROJECT_MAIN_ID,\n" +
                    "                       SUBJECT_CODE,\n" +
                    "                       SUBJECT_NAME,\n" +
                    "                       OFFICE_ID,\n" +
                    "                       OFFICE_NAME,\n" +
                    "                       PAYEE,\n" +
                    "                       RENDER_PERSON,\n" +
                    "                       SUPPLIER_ID,\n" +
                    "                       SUPPLIER_NAME,\n" +
                    "                       CREATE_TIME,\n" +
                    "                       DEBTOR,\n" +
                    "                       CREDITOR,\n" +
                    "                       SUM(CLEARANCE_MONEY) AS SUM_CLEARANCE_MONEY\n" +
                    "                  FROM (SELECT FAD_INVOICE.INVOICE_TYPE,\n" +
                    "                               FAD_INVOICE.INVOICE_REQ_NO,\n" +
                    "                               FAD_INVOICE.PAY_STYLE,\n" +
                    "                               FAD_INVOICE.PRM_PROJECT_MAIN_ID,\n" +
                    "                               FAD_INVOICE.SUBJECT_CODE,\n" +
                    "                               FAD_INVOICE.SUBJECT_NAME,\n" +
                    "                               FAD_INVOICE.OFFICE_ID,\n" +
                    "                               FAD_INVOICE.OFFICE_NAME,\n" +
                    "                               FAD_INVOICE.PAYEE,\n" +
                    "                               FAD_INVOICE.RENDER_PERSON,\n" +
                    "                               FAD_INVOICE.SUPPLIER_ID,\n" +
                    "                               FAD_INVOICE.SUPPLIER_NAME,\n" +
                    "                               FAD_INVOICE.CREATE_TIME,\n" +
                    "                               NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS DEBTOR,\n" +
                    "                               NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS CREDITOR,\n" +
                    "                               NVL(FAD_CASH_REQ_INVOICE.CLEARANCE_MONEY, 0) AS CLEARANCE_MONEY\n" +
                    "                          FROM FAD_INVOICE\n" +
                    "                          LEFT JOIN FAD_CASH_REQ_INVOICE ON FAD_INVOICE.UUID =\n" +
                    "                                                            FAD_CASH_REQ_INVOICE.FAD_INVOICE_ID\n" +
                    "                         WHERE FAD_INVOICE.UUID = '" + businessId + "') A\n" +
                    "                 GROUP BY INVOICE_TYPE,\n" +
                    "                          INVOICE_REQ_NO,\n" +
                    "                          PAY_STYLE,\n" +
                    "                          PRM_PROJECT_MAIN_ID,\n" +
                    "                          SUBJECT_CODE,\n" +
                    "                          SUBJECT_NAME,\n" +
                    "                          OFFICE_ID,\n" +
                    "                          OFFICE_NAME,\n" +
                    "                          PAYEE,\n" +
                    "                          RENDER_PERSON,\n" +
                    "                          SUPPLIER_ID,\n" +
                    "                          SUPPLIER_NAME,\n" +
                    "                          CREATE_TIME,\n" +
                    "                          DEBTOR,\n" +
                    "                          CREDITOR) A";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

            //子表凭证摘要新增
            sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                    "        (UUID,\n" +
                    "         FAD_CERTIFICATE_ID,\n" +
                    "         BUSINESS_ID,\n" +
                    "         ORDER_NO,\n" +
                    "         CURRENCY,\n" +
                    "         CURRTYPENAME,\n" +
                    "         LOCAL,\n" +
                    "         SUBJECT_CODE,\n" +
                    "         SUBJECT_NAME,\n" +
                    "         DEBTOR_OR_CREDITOR,\n" +
                    "         ORIGINAL_FORM_TYPE,\n" +
                    "         ORIGINAL_FORM_CODE,\n" +
                    "         ORIGINAL_FORM_DATE,\n" +
                    "         SEQ_NO)\n" +
                    "        SELECT SYS_GUID(),\n" +
                    "               (SELECT A.UUID FROM(SELECT UUID\n" +
                    "                  FROM FAD_CERTIFICATE\n" +
                    "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                    "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + ".1' \n" +
                    "                   ORDER BY SEQ_NO DESC) A WHERE ROWNUM = 1),\n" +
                    "               '" + businessId + "',\n" +
                    "               1,\n" +
                    "               'CNY',\n" +
                    "               '人民币元',\n" +
                    "               DEBTOR - SUM_CLEARANCE_MONEY,\n" +
                    "               '2241',\n" +
                    "               '其他应付款',\n" +
                    "               '0',\n" +
                    "               '" + originalFormType + ".1', --报销->有请款\n" +
                    "               INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                    "               CREATE_TIME,\n" +
                    "               FAD_CERTIFICATE_SEQ_NO.NEXTVAL\n" +
                    "          FROM (SELECT INVOICE_TYPE,\n" +
                    "                       INVOICE_REQ_NO,\n" +
                    "                       PAY_STYLE,\n" +
                    "                       PRM_PROJECT_MAIN_ID,\n" +
                    "                       SUBJECT_CODE,\n" +
                    "                       SUBJECT_NAME,\n" +
                    "                       OFFICE_ID,\n" +
                    "                       OFFICE_NAME,\n" +
                    "                       PAYEE,\n" +
                    "                       RENDER_PERSON,\n" +
                    "                       SUPPLIER_ID,\n" +
                    "                       SUPPLIER_NAME,\n" +
                    "                       CREATE_TIME,\n" +
                    "                       DEBTOR,\n" +
                    "                       CREDITOR,\n" +
                    "                       SUM(CLEARANCE_MONEY) AS SUM_CLEARANCE_MONEY\n" +
                    "                  FROM (SELECT FAD_INVOICE.INVOICE_TYPE,\n" +
                    "                               FAD_INVOICE.INVOICE_REQ_NO,\n" +
                    "                               FAD_INVOICE.PAY_STYLE,\n" +
                    "                               FAD_INVOICE.PRM_PROJECT_MAIN_ID,\n" +
                    "                               FAD_INVOICE.SUBJECT_CODE,\n" +
                    "                               FAD_INVOICE.SUBJECT_NAME,\n" +
                    "                               FAD_INVOICE.OFFICE_ID,\n" +
                    "                               FAD_INVOICE.OFFICE_NAME,\n" +
                    "                               FAD_INVOICE.PAYEE,\n" +
                    "                               FAD_INVOICE.RENDER_PERSON,\n" +
                    "                               FAD_INVOICE.SUPPLIER_ID,\n" +
                    "                               FAD_INVOICE.SUPPLIER_NAME,\n" +
                    "                               FAD_INVOICE.CREATE_TIME,\n" +
                    "                               NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS DEBTOR,\n" +
                    "                               NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS CREDITOR,\n" +
                    "                               NVL(FAD_CASH_REQ_INVOICE.CLEARANCE_MONEY, 0) AS CLEARANCE_MONEY\n" +
                    "                          FROM FAD_INVOICE\n" +
                    "                          LEFT JOIN FAD_CASH_REQ_INVOICE ON FAD_INVOICE.UUID =\n" +
                    "                                                            FAD_CASH_REQ_INVOICE.FAD_INVOICE_ID\n" +
                    "                         WHERE FAD_INVOICE.UUID = '" + businessId + "') A\n" +
                    "                 GROUP BY INVOICE_TYPE,\n" +
                    "                          INVOICE_REQ_NO,\n" +
                    "                          PAY_STYLE,\n" +
                    "                          PRM_PROJECT_MAIN_ID,\n" +
                    "                          SUBJECT_CODE,\n" +
                    "                          SUBJECT_NAME,\n" +
                    "                          OFFICE_ID,\n" +
                    "                          OFFICE_NAME,\n" +
                    "                          PAYEE,\n" +
                    "                          RENDER_PERSON,\n" +
                    "                          SUPPLIER_ID,\n" +
                    "                          SUPPLIER_NAME,\n" +
                    "                          CREATE_TIME,\n" +
                    "                          DEBTOR,\n" +
                    "                          CREDITOR) A";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

            sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                    "        (UUID,\n" +
                    "         FAD_CERTIFICATE_ID,\n" +
                    "         BUSINESS_ID,\n" +
                    "         ORDER_NO,\n" +
                    "         CURRENCY,\n" +
                    "         CURRTYPENAME,\n" +
                    "         LOCAL,\n" +
                    "         SUBJECT_CODE,\n" +
                    "         SUBJECT_NAME,\n" +
                    "         DEBTOR_OR_CREDITOR,\n" +
                    "         ORIGINAL_FORM_TYPE,\n" +
                    "         ORIGINAL_FORM_CODE,\n" +
                    "         ORIGINAL_FORM_DATE,\n" +
                    "         SEQ_NO)\n" +
                    "        SELECT SYS_GUID(),\n" +
                    "               (SELECT A.UUID FROM(SELECT UUID\n" +
                    "                  FROM FAD_CERTIFICATE\n" +
                    "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                    "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + ".1' \n" +
                    "                   ORDER BY SEQ_NO DESC) A WHERE ROWNUM = 1),\n" +
                    "               '" + businessId + "',\n" +
                    "               2,\n" +
                    "               'CNY',\n" +
                    "               '人民币元',\n" +
                    "               CREDITOR - SUM_CLEARANCE_MONEY,\n" +
                    "               CASE WHEN PAY_STYLE = '0' THEN '1001' ELSE '1002' END,\n" +
                    "               CASE WHEN PAY_STYLE = '0' THEN '库存现金' ELSE '银行存款' END,\n" +
                    "               '1',\n" +
                    "               '" + originalFormType + ".1', --报销->有请款\n" +
                    "               INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                    "               CREATE_TIME,\n" +
                    "               FAD_CERTIFICATE_SEQ_NO.NEXTVAL\n" +
                    "          FROM (SELECT INVOICE_TYPE,\n" +
                    "                       INVOICE_REQ_NO,\n" +
                    "                       PAY_STYLE,\n" +
                    "                       PRM_PROJECT_MAIN_ID,\n" +
                    "                       SUBJECT_CODE,\n" +
                    "                       SUBJECT_NAME,\n" +
                    "                       OFFICE_ID,\n" +
                    "                       OFFICE_NAME,\n" +
                    "                       PAYEE,\n" +
                    "                       RENDER_PERSON,\n" +
                    "                       SUPPLIER_ID,\n" +
                    "                       SUPPLIER_NAME,\n" +
                    "                       CREATE_TIME,\n" +
                    "                       DEBTOR,\n" +
                    "                       CREDITOR,\n" +
                    "                       SUM(CLEARANCE_MONEY) AS SUM_CLEARANCE_MONEY\n" +
                    "                  FROM (SELECT FAD_INVOICE.INVOICE_TYPE,\n" +
                    "                               FAD_INVOICE.INVOICE_REQ_NO,\n" +
                    "                               FAD_INVOICE.PAY_STYLE,\n" +
                    "                               FAD_INVOICE.PRM_PROJECT_MAIN_ID,\n" +
                    "                               FAD_INVOICE.SUBJECT_CODE,\n" +
                    "                               FAD_INVOICE.SUBJECT_NAME,\n" +
                    "                               FAD_INVOICE.OFFICE_ID,\n" +
                    "                               FAD_INVOICE.OFFICE_NAME,\n" +
                    "                               FAD_INVOICE.PAYEE,\n" +
                    "                               FAD_INVOICE.RENDER_PERSON,\n" +
                    "                               FAD_INVOICE.SUPPLIER_ID,\n" +
                    "                               FAD_INVOICE.SUPPLIER_NAME,\n" +
                    "                               FAD_INVOICE.CREATE_TIME,\n" +
                    "                               NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS DEBTOR,\n" +
                    "                               NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS CREDITOR,\n" +
                    "                               NVL(FAD_CASH_REQ_INVOICE.CLEARANCE_MONEY, 0) AS CLEARANCE_MONEY\n" +
                    "                          FROM FAD_INVOICE\n" +
                    "                          LEFT JOIN FAD_CASH_REQ_INVOICE ON FAD_INVOICE.UUID =\n" +
                    "                                                            FAD_CASH_REQ_INVOICE.FAD_INVOICE_ID\n" +
                    "                         WHERE FAD_INVOICE.UUID = '" + businessId + "') A\n" +
                    "                 GROUP BY INVOICE_TYPE,\n" +
                    "                          INVOICE_REQ_NO,\n" +
                    "                          PAY_STYLE,\n" +
                    "                          PRM_PROJECT_MAIN_ID,\n" +
                    "                          SUBJECT_CODE,\n" +
                    "                          SUBJECT_NAME,\n" +
                    "                          OFFICE_ID,\n" +
                    "                          OFFICE_NAME,\n" +
                    "                          PAYEE,\n" +
                    "                          RENDER_PERSON,\n" +
                    "                          SUPPLIER_ID,\n" +
                    "                          SUPPLIER_NAME,\n" +
                    "                          CREATE_TIME,\n" +
                    "                          DEBTOR,\n" +
                    "                          CREDITOR) A";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
        }
    }

    @Override
    public void createCertificateFromBusinessNoPaymentPurchaseContractInvoice(String businessId, String originalFormType) {
        String sql = "";
        DAOMeta daoMeta = null;

        //报销与采购合同发票主表凭证
        createCertificateFromBusinessInvoiceHead(businessId, originalFormType);

        //子表凭证摘要新增
        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               1,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               CASE\n" +
                "                 WHEN FAD_INVOICE.INVOICE_TYPE = '0' THEN\n" +
                "                  NVL(SCM_CONTRACT_INVOICE.AMOUNT, 0) - (NVL(SCM_CONTRACT_INVOICE.AMOUNT, 0) / (1 + NVL(FAD_INVOICE.TAX_RATE, 0)) * NVL(FAD_INVOICE.TAX_RATE, 0))\n" +
                "                 ELSE\n" +
                "                  NVL(SCM_CONTRACT_INVOICE.AMOUNT, 0)\n" +
                "               END,\n" +
                "               '140302',\n" +
                "               '原材料-原料',\n" +
                "               '0',\n" +
                "               '" + originalFormType + "', --采购->无请款应付\n" +
                "               FAD_INVOICE.INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(FAD_INVOICE.CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               FAD_INVOICE.CREATE_TIME\n" +
                "          FROM FAD_INVOICE\n" +
                "          LEFT JOIN SCM_CONTRACT_INVOICE ON FAD_INVOICE.UUID =\n" +
                "                                            SCM_CONTRACT_INVOICE.FAD_INVOICE_ID\n" +
                "          LEFT JOIN SCM_CONTRACT ON SCM_CONTRACT_INVOICE.SCM_CONTRACT_ID =\n" +
                "                                    SCM_CONTRACT.UUID\n" +
                "         WHERE FAD_INVOICE.UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               2,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               CASE\n" +
                "                 WHEN FAD_INVOICE.INVOICE_TYPE = '0' THEN\n" +
                "                  (NVL(SCM_CONTRACT_INVOICE.AMOUNT, 0) / (1 + NVL(FAD_INVOICE.TAX_RATE, 0)) * NVL(FAD_INVOICE.TAX_RATE, 0))\n" +
                "                 ELSE\n" +
                "                  0\n" +
                "               END,\n" +
                "               '2221010101',\n" +
                "               '应交税费-应交税金-应交增值税-进项税额',\n" +
                "               '0',\n" +
                "               '" + originalFormType + "', --采购->无请款应付\n" +
                "               FAD_INVOICE.INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(FAD_INVOICE.CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               FAD_INVOICE.CREATE_TIME\n" +
                "          FROM FAD_INVOICE\n" +
                "          LEFT JOIN SCM_CONTRACT_INVOICE ON FAD_INVOICE.UUID =\n" +
                "                                            SCM_CONTRACT_INVOICE.FAD_INVOICE_ID\n" +
                "          LEFT JOIN SCM_CONTRACT ON SCM_CONTRACT_INVOICE.SCM_CONTRACT_ID =\n" +
                "                                    SCM_CONTRACT.UUID\n" +
                "         WHERE FAD_INVOICE.UUID = '" + businessId + "'\n" +
                "           AND NVL(FAD_INVOICE.TAX_RATE, 0) > 0\n" +
                "           AND FAD_INVOICE.INVOICE_TYPE = '0'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               (SELECT NVL(MAX(ORDER_NO),0)\n" +
                "                  FROM FAD_CERTIFICATE_DETAIL\n" +
                "                 WHERE FAD_CERTIFICATE_ID =\n" +
                "                       (SELECT UUID\n" +
                "                          FROM FAD_CERTIFICATE\n" +
                "                         WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                           AND ORIGINAL_FORM_TYPE = '" + originalFormType + "')) +\n" +
                "               ROWNUM,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               NVL(SCM_CONTRACT_INVOICE.AMOUNT, 0),\n" +
                "               '2202',\n" +
                "               '应付账款',\n" +
                "               '1',\n" +
                "               '" + originalFormType + "', --采购->无请款应付\n" +
                "               SCM_CONTRACT.SCM_CONTRACT_CODE || '(采购合同编号)' || '(' || TO_CHAR(SCM_CONTRACT_INVOICE.CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               SCM_CONTRACT_INVOICE.CREATE_TIME\n" +
                "          FROM FAD_INVOICE\n" +
                "          LEFT JOIN SCM_CONTRACT_INVOICE ON FAD_INVOICE.UUID =\n" +
                "                                            SCM_CONTRACT_INVOICE.FAD_INVOICE_ID\n" +
                "          LEFT JOIN SCM_CONTRACT ON SCM_CONTRACT_INVOICE.SCM_CONTRACT_ID =\n" +
                "                                    SCM_CONTRACT.UUID\n" +
                "         WHERE FAD_INVOICE.UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
    }

    @Override
    public void createCertificateFromBusinessNoPaymentPurchaseContractInvoiceWaixie(String businessId, String originalFormType) {
        String sql = "";
        DAOMeta daoMeta = null;

        //报销与采购合同发票主表凭证
        createCertificateFromBusinessInvoiceHead(businessId, originalFormType);

        //子表凭证摘要新增
        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               1,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               CASE\n" +
                "                 WHEN FAD_INVOICE.INVOICE_TYPE = '0' THEN\n" +
                "                  NVL(SCM_CONTRACT_INVOICE.AMOUNT, 0) - (NVL(SCM_CONTRACT_INVOICE.AMOUNT, 0) / (1 + NVL(FAD_INVOICE.TAX_RATE, 0)) * NVL(FAD_INVOICE.TAX_RATE, 0))\n" +
                "                 ELSE\n" +
                "                  NVL(SCM_CONTRACT_INVOICE.AMOUNT, 0)\n" +
                "               END,\n" +
                "               '5401',\n" +
                "               '工程施工',\n" +
                "               '0',\n" +
                "               '" + originalFormType + "', --采购->无请款应付\n" +
                "               FAD_INVOICE.INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(FAD_INVOICE.CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               FAD_INVOICE.CREATE_TIME\n" +
                "          FROM FAD_INVOICE\n" +
                "          LEFT JOIN SCM_CONTRACT_INVOICE ON FAD_INVOICE.UUID =\n" +
                "                                            SCM_CONTRACT_INVOICE.FAD_INVOICE_ID\n" +
                "          LEFT JOIN SCM_CONTRACT ON SCM_CONTRACT_INVOICE.SCM_CONTRACT_ID =\n" +
                "                                    SCM_CONTRACT.UUID\n" +
                "         WHERE FAD_INVOICE.UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               2,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               CASE\n" +
                "                 WHEN FAD_INVOICE.INVOICE_TYPE = '0' THEN\n" +
                "                  (NVL(SCM_CONTRACT_INVOICE.AMOUNT, 0) / (1 + NVL(FAD_INVOICE.TAX_RATE, 0)) * NVL(FAD_INVOICE.TAX_RATE, 0))\n" +
                "                 ELSE\n" +
                "                  0\n" +
                "               END,\n" +
                "               '2221010101',\n" +
                "               '应交税费-应交税金-应交增值税-进项税额',\n" +
                "               '0',\n" +
                "               '" + originalFormType + "', --采购->无请款应付\n" +
                "               FAD_INVOICE.INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(FAD_INVOICE.CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               FAD_INVOICE.CREATE_TIME\n" +
                "          FROM FAD_INVOICE\n" +
                "          LEFT JOIN SCM_CONTRACT_INVOICE ON FAD_INVOICE.UUID =\n" +
                "                                            SCM_CONTRACT_INVOICE.FAD_INVOICE_ID\n" +
                "          LEFT JOIN SCM_CONTRACT ON SCM_CONTRACT_INVOICE.SCM_CONTRACT_ID =\n" +
                "                                    SCM_CONTRACT.UUID\n" +
                "         WHERE FAD_INVOICE.UUID = '" + businessId + "'\n" +
                "           AND NVL(FAD_INVOICE.TAX_RATE, 0) > 0\n" +
                "           AND FAD_INVOICE.INVOICE_TYPE = '0'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               (SELECT NVL(MAX(ORDER_NO),0)\n" +
                "                  FROM FAD_CERTIFICATE_DETAIL\n" +
                "                 WHERE FAD_CERTIFICATE_ID =\n" +
                "                       (SELECT UUID\n" +
                "                          FROM FAD_CERTIFICATE\n" +
                "                         WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                           AND ORIGINAL_FORM_TYPE = '" + originalFormType + "')) +\n" +
                "               ROWNUM,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               NVL(SCM_CONTRACT_INVOICE.AMOUNT, 0),\n" +
                "               '2202',\n" +
                "               '应付账款',\n" +
                "               '1',\n" +
                "               '" + originalFormType + "', --采购->无请款应付\n" +
                "               SCM_CONTRACT.SCM_CONTRACT_CODE || '(采购合同编号)' || '(' || TO_CHAR(SCM_CONTRACT_INVOICE.CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               SCM_CONTRACT_INVOICE.CREATE_TIME\n" +
                "          FROM FAD_INVOICE\n" +
                "          LEFT JOIN SCM_CONTRACT_INVOICE ON FAD_INVOICE.UUID =\n" +
                "                                            SCM_CONTRACT_INVOICE.FAD_INVOICE_ID\n" +
                "          LEFT JOIN SCM_CONTRACT ON SCM_CONTRACT_INVOICE.SCM_CONTRACT_ID =\n" +
                "                                    SCM_CONTRACT.UUID\n" +
                "         WHERE FAD_INVOICE.UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
    }

    @Override
    public void createCertificateFromBusinessNoPaymentPurchaseContractInvoiceMaterial(String businessId, String originalFormType) {
        String sql = "";
        DAOMeta daoMeta = null;

        //报销与采购合同发票主表凭证
        createCertificateFromBusinessInvoiceHead(businessId, originalFormType);

        //子表凭证摘要新增
        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               1,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               CASE\n" +
                "                 WHEN INVOICE_TYPE = '0' THEN\n" +
                "                  NVL(EXPENSES_MONEY, 0) - (NVL(EXPENSES_MONEY, 0) / (1 + NVL(TAX_RATE, 0)) * NVL(TAX_RATE, 0))\n" +
                "                 ELSE\n" +
                "                  NVL(EXPENSES_MONEY, 0)\n" +
                "               END,\n" +
                "               '140302',\n" +
                "               '原材料-原料',\n" +
                "               '0',\n" +
                "               '" + originalFormType + "', --采购->无请款应付\n" +
                "               INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME\n" +
                "          FROM FAD_INVOICE\n" +
                "         WHERE UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               2,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               CASE\n" +
                "                 WHEN INVOICE_TYPE = '0' THEN\n" +
                "                  (NVL(EXPENSES_MONEY, 0) / (1 + NVL(TAX_RATE, 0)) * NVL(TAX_RATE, 0))\n" +
                "                 ELSE\n" +
                "                  0\n" +
                "               END,\n" +
                "               '2221010101',\n" +
                "               '应交税费-应交税金-应交增值税-进项税额',\n" +
                "               '0',\n" +
                "               '" + originalFormType + "', --采购->无请款应付\n" +
                "               INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME\n" +
                "          FROM FAD_INVOICE\n" +
                "         WHERE UUID = '" + businessId + "'\n" +
                "           AND NVL(TAX_RATE, 0) > 0\n" +
                "           AND INVOICE_TYPE = '0'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               (SELECT NVL(MAX(ORDER_NO),0)\n" +
                "                  FROM FAD_CERTIFICATE_DETAIL\n" +
                "                 WHERE FAD_CERTIFICATE_ID =\n" +
                "                       (SELECT UUID\n" +
                "                          FROM FAD_CERTIFICATE\n" +
                "                         WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                           AND ORIGINAL_FORM_TYPE = '" + originalFormType + "')) +\n" +
                "               ROWNUM,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               NVL(FAD_INVOICE.EXPENSES_MONEY, 0),\n" +
                "               '2202',\n" +
                "               '应付账款',\n" +
                "               '1',\n" +
                "               '" + originalFormType + "', --采购->无请款应付\n" +
                "               FAD_INVOICE.INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(FAD_INVOICE.CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               FAD_INVOICE.CREATE_TIME\n" +
                "          FROM FAD_INVOICE\n" +
                //"          LEFT JOIN FAD_INVOICE_MATERIAL ON FAD_INVOICE.UUID =\n" +
                //"                                            FAD_INVOICE_MATERIAL.FAD_INVOICE_ID\n" +
                "         WHERE FAD_INVOICE.UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
    }

    @Override
    public void createCertificateFromBusinessPaymentPurchaseContractPayableInvoice(String businessId, String originalFormType) {
        String sql = "";
        DAOMeta daoMeta = null;

        //报销与采购合同发票主表凭证
        createCertificateFromBusinessInvoiceHead(businessId, originalFormType);

        //子表凭证摘要新增
        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               (SELECT NVL(MAX(ORDER_NO),0)\n" +
                "                  FROM FAD_CERTIFICATE_DETAIL\n" +
                "                 WHERE FAD_CERTIFICATE_ID =\n" +
                "                       (SELECT UUID\n" +
                "                          FROM FAD_CERTIFICATE\n" +
                "                         WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                           AND ORIGINAL_FORM_TYPE = '" + originalFormType + "')) +\n" +
                "               ROWNUM,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               CASE\n" +
                "                 WHEN FAD_INVOICE.INVOICE_TYPE = '0' THEN\n" +
                "                  NVL(SCM_CONTRACT_INVOICE.AMOUNT, 0) - (NVL(SCM_CONTRACT_INVOICE.AMOUNT, 0) / (1 + NVL(FAD_INVOICE.TAX_RATE, 0)) * NVL(FAD_INVOICE.TAX_RATE, 0))\n" +
                "                 ELSE\n" +
                "                  NVL(SCM_CONTRACT_INVOICE.AMOUNT, 0)\n" +
                "               END,\n" +
                "               '140302',\n" +
                "               '原材料-原料',\n" +
                "               '0',\n" +
                "               '" + originalFormType + "', --采购->有请款应付\n" +
                "               FAD_INVOICE.INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(FAD_INVOICE.CREATE_TIME,'YYYY-MM-DD') || ')'\n" +
                "               || '、' || SCM_CONTRACT.SCM_CONTRACT_CODE || '(采购合同编号)' || '(' || TO_CHAR(SCM_CONTRACT_INVOICE.CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               FAD_INVOICE.CREATE_TIME\n" +
                "          FROM FAD_INVOICE\n" +
                "          LEFT JOIN SCM_CONTRACT_INVOICE ON FAD_INVOICE.UUID =\n" +
                "                                            SCM_CONTRACT_INVOICE.FAD_INVOICE_ID\n" +
                "          LEFT JOIN SCM_CONTRACT ON SCM_CONTRACT_INVOICE.SCM_CONTRACT_ID =\n" +
                "                                    SCM_CONTRACT.UUID\n" +
                "         WHERE FAD_INVOICE.UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               (SELECT NVL(MAX(ORDER_NO),0)\n" +
                "                  FROM FAD_CERTIFICATE_DETAIL\n" +
                "                 WHERE FAD_CERTIFICATE_ID =\n" +
                "                       (SELECT UUID\n" +
                "                          FROM FAD_CERTIFICATE\n" +
                "                         WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                           AND ORIGINAL_FORM_TYPE = '" + originalFormType + "')) +\n" +
                "               ROWNUM,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               CASE\n" +
                "                 WHEN FAD_INVOICE.INVOICE_TYPE = '0' THEN\n" +
                "                  (NVL(SCM_CONTRACT_INVOICE.AMOUNT, 0) / (1 + NVL(FAD_INVOICE.TAX_RATE, 0)) * NVL(FAD_INVOICE.TAX_RATE, 0))\n" +
                "                 ELSE\n" +
                "                  0\n" +
                "               END,\n" +
                "               '2221010101',\n" +
                "               '应交税费-应交税金-应交增值税-进项税额',\n" +
                "               '0',\n" +
                "               '" + originalFormType + "', --采购->有请款应付\n" +
                "               SCM_CONTRACT.SCM_CONTRACT_CODE || '(采购合同编号)' || '(' || TO_CHAR(SCM_CONTRACT_INVOICE.CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               SCM_CONTRACT_INVOICE.CREATE_TIME\n" +
                "          FROM FAD_INVOICE\n" +
                "          LEFT JOIN SCM_CONTRACT_INVOICE ON FAD_INVOICE.UUID =\n" +
                "                                            SCM_CONTRACT_INVOICE.FAD_INVOICE_ID\n" +
                "          LEFT JOIN SCM_CONTRACT ON SCM_CONTRACT_INVOICE.SCM_CONTRACT_ID =\n" +
                "                                    SCM_CONTRACT.UUID\n" +
                "         WHERE FAD_INVOICE.UUID = '" + businessId + "'\n" +
                "           AND NVL(FAD_INVOICE.TAX_RATE, 0) > 0\n" +
                "           AND FAD_INVOICE.INVOICE_TYPE = '0'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE,\n" +
                "         SEQ_NO)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               (SELECT NVL(MAX(ORDER_NO),0)\n" +
                "                  FROM FAD_CERTIFICATE_DETAIL\n" +
                "                 WHERE FAD_CERTIFICATE_ID =\n" +
                "                       (SELECT UUID\n" +
                "                          FROM FAD_CERTIFICATE\n" +
                "                         WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                           AND ORIGINAL_FORM_TYPE = '" + originalFormType + "')) +\n" +
                "               ROW_NUMBER() OVER(ORDER BY SCM_CONTRACT_CODE DESC),\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               CREDITOR - SUM_CLEARANCE_MONEY,\n" +
                "               '2202',\n" +
                "               '应付账款',\n" +
                "               '1',\n" +
                "               '" + originalFormType + "', --采购->有请款应付\n" +
                "               SCM_CONTRACT_CODE || '(采购合同编号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME,\n" +
                "               FAD_CERTIFICATE_SEQ_NO.NEXTVAL\n" +
                "          FROM (SELECT INVOICE_TYPE,\n" +
                "                       PAY_STYLE,\n" +
                "                       PROJECT_ID,\n" +
                "                       SUBJECT_CODE,\n" +
                "                       PURCHASE_PACKAGE_ID,\n" +
                "                       OFFICE_ID,\n" +
                "                       OFFICE_NAME,\n" +
                "                       PAYEE,\n" +
                "                       RENDER_PERSON,\n" +
                "                       SUPPLIER_ID,\n" +
                "                       SUPPLIER_NAME,\n" +
                "                       SCM_CONTRACT_CODE,\n" +
                "                       CREATE_TIME,\n" +
                "                       TAX_RATE,\n" +
                "                       DEBTOR,\n" +
                "                       CREDITOR,\n" +
                "                       SUM(CLEARANCE_MONEY) AS SUM_CLEARANCE_MONEY\n" +
                "                  FROM (SELECT FAD_INVOICE.INVOICE_TYPE,\n" +
                "                               FAD_INVOICE.PAY_STYLE,\n" +
                "                               SCM_CONTRACT.PROJECT_ID,\n" +
                "                               SCM_CONTRACT.SUBJECT_CODE,\n" +
                "                               SCM_CONTRACT.PURCHASE_PACKAGE_ID,\n" +
                "                               FAD_INVOICE.OFFICE_ID,\n" +
                "                               FAD_INVOICE.OFFICE_NAME,\n" +
                "                               FAD_INVOICE.PAYEE,\n" +
                "                               FAD_INVOICE.RENDER_PERSON,\n" +
                "                               FAD_INVOICE.SUPPLIER_ID,\n" +
                "                               FAD_INVOICE.SUPPLIER_NAME,\n" +
                "                               SCM_CONTRACT.SCM_CONTRACT_CODE,\n" +
                "                               SCM_CONTRACT_INVOICE.CREATE_TIME,\n" +
                "                               NVL(FAD_INVOICE.TAX_RATE, 0) AS TAX_RATE,\n" +
                "                               NVL(SCM_CONTRACT_INVOICE.AMOUNT, 0) AS DEBTOR,\n" +
                "                               NVL(SCM_CONTRACT_INVOICE.AMOUNT, 0) AS CREDITOR,\n" +
                "                               NVL(FAD_CASH_REQ_INVOICE.CLEARANCE_MONEY, 0) AS CLEARANCE_MONEY\n" +
                "                          FROM FAD_INVOICE\n" +
                "                          LEFT JOIN SCM_CONTRACT_INVOICE ON FAD_INVOICE.UUID =\n" +
                "                                                            SCM_CONTRACT_INVOICE.FAD_INVOICE_ID\n" +
                "                          LEFT JOIN SCM_CONTRACT ON SCM_CONTRACT_INVOICE.SCM_CONTRACT_ID =\n" +
                "                                                    SCM_CONTRACT.UUID\n" +
                "                          LEFT JOIN FAD_CASH_REQ ON SCM_CONTRACT.UUID =\n" +
                "                                                    FAD_CASH_REQ.PURCHASE_CONTRACT_ID\n" +
                "                          LEFT JOIN FAD_CASH_REQ_INVOICE ON FAD_CASH_REQ.UUID =\n" +
                "                                                            FAD_CASH_REQ_INVOICE.FAD_CASH_REQ_ID\n" +
                "                                                        AND FAD_INVOICE.UUID =\n" +
                "                                                            FAD_CASH_REQ_INVOICE.FAD_INVOICE_ID\n" +
                "                         WHERE FAD_INVOICE.UUID = '" + businessId + "') A\n" +
                "                 GROUP BY INVOICE_TYPE,\n" +
                "                          PAY_STYLE,\n" +
                "                          PROJECT_ID,\n" +
                "                          SUBJECT_CODE,\n" +
                "                          PURCHASE_PACKAGE_ID,\n" +
                "                          OFFICE_ID,\n" +
                "                          OFFICE_NAME,\n" +
                "                          PAYEE,\n" +
                "                          RENDER_PERSON,\n" +
                "                          SUPPLIER_ID,\n" +
                "                          SUPPLIER_NAME,\n" +
                "                          SCM_CONTRACT_CODE,\n" +
                "                          CREATE_TIME,\n" +
                "                          TAX_RATE,\n" +
                "                          DEBTOR,\n" +
                "                          CREDITOR) A WHERE (CREDITOR - SUM_CLEARANCE_MONEY) <> 0";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               (SELECT NVL(MAX(ORDER_NO),0)\n" +
                "                  FROM FAD_CERTIFICATE_DETAIL\n" +
                "                 WHERE FAD_CERTIFICATE_ID =\n" +
                "                       (SELECT UUID\n" +
                "                          FROM FAD_CERTIFICATE\n" +
                "                         WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                           AND ORIGINAL_FORM_TYPE = '" + originalFormType + "')) +\n" +
                "               ROW_NUMBER() OVER(ORDER BY SCM_CONTRACT.SCM_CONTRACT_CODE DESC),\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               NVL(VW_REQ_NO_MERGE.CLEARANCE_MONEY, 0),\n" +
                "               '1123',\n" +
                "               '预付账款',\n" +
                "               '1',\n" +
                "               '" + originalFormType + "', --采购->有请款应付\n" +
                "               CASE WHEN VW_REQ_NO_MERGE.RUNNING_NO IS NULL\n" +
                "               THEN\n" +
                "                       SCM_CONTRACT.SCM_CONTRACT_CODE || '(采购合同编号)' || '(' || TO_CHAR(SCM_CONTRACT_INVOICE.CREATE_TIME,'YYYY-MM-DD') || ')'\n" +
                "               ELSE\n" +
                "                       SCM_CONTRACT.SCM_CONTRACT_CODE || '(采购合同编号)' || '(' || TO_CHAR(SCM_CONTRACT_INVOICE.CREATE_TIME,'YYYY-MM-DD') || ')' || '、' || VW_REQ_NO_MERGE.RUNNING_NO || '(请款流水号)' || '(' || TO_CHAR(VW_REQ_NO_MERGE.CREATE_TIME,'YYYY-MM-DD') || ')'\n" +
                "               END,\n" +
                "               SCM_CONTRACT_INVOICE.CREATE_TIME\n" +
                "          FROM FAD_INVOICE\n" +
                "          LEFT JOIN SCM_CONTRACT_INVOICE ON FAD_INVOICE.UUID =\n" +
                "                                            SCM_CONTRACT_INVOICE.FAD_INVOICE_ID\n" +
                "          LEFT JOIN SCM_CONTRACT ON SCM_CONTRACT_INVOICE.SCM_CONTRACT_ID =\n" +
                "                                    SCM_CONTRACT.UUID\n" +
                "          LEFT JOIN VW_REQ_NO_MERGE ON SCM_CONTRACT.UUID =\n" +
                "                                       VW_REQ_NO_MERGE.PURCHASE_CONTRACT_ID\n" +
                "                                   AND FAD_INVOICE.UUID =\n" +
                "                                       VW_REQ_NO_MERGE.FAD_INVOICE_ID\n" +
                "         WHERE FAD_INVOICE.UUID = '" + businessId + "' AND VW_REQ_NO_MERGE.CLEARANCE_MONEY IS NOT NULL";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
    }

    @Override
    public void createCertificateFromBusinessPaymentPurchaseContractPayableInvoiceWaixie(String businessId, String originalFormType) {
        String sql = "";
        DAOMeta daoMeta = null;

        //报销与采购合同发票主表凭证
        createCertificateFromBusinessInvoiceHead(businessId, originalFormType);

        //子表凭证摘要新增
        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               (SELECT NVL(MAX(ORDER_NO),0)\n" +
                "                  FROM FAD_CERTIFICATE_DETAIL\n" +
                "                 WHERE FAD_CERTIFICATE_ID =\n" +
                "                       (SELECT UUID\n" +
                "                          FROM FAD_CERTIFICATE\n" +
                "                         WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                           AND ORIGINAL_FORM_TYPE = '" + originalFormType + "')) +\n" +
                "               ROWNUM,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               CASE\n" +
                "                 WHEN FAD_INVOICE.INVOICE_TYPE = '0' THEN\n" +
                "                  NVL(SCM_CONTRACT_INVOICE.AMOUNT, 0) - (NVL(SCM_CONTRACT_INVOICE.AMOUNT, 0) / (1 + NVL(FAD_INVOICE.TAX_RATE, 0)) * NVL(FAD_INVOICE.TAX_RATE, 0))\n" +
                "                 ELSE\n" +
                "                  NVL(SCM_CONTRACT_INVOICE.AMOUNT, 0)\n" +
                "               END,\n" +
                "               '5401',\n" +
                "               '工程施工',\n" +
                "               '0',\n" +
                "               '" + originalFormType + "', --采购->有请款应付\n" +
                "               FAD_INVOICE.INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(FAD_INVOICE.CREATE_TIME,'YYYY-MM-DD') || ')'\n" +
                "               || '、' || SCM_CONTRACT.SCM_CONTRACT_CODE || '(采购合同编号)' || '(' || TO_CHAR(SCM_CONTRACT_INVOICE.CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               FAD_INVOICE.CREATE_TIME\n" +
                "          FROM FAD_INVOICE\n" +
                "          LEFT JOIN SCM_CONTRACT_INVOICE ON FAD_INVOICE.UUID =\n" +
                "                                            SCM_CONTRACT_INVOICE.FAD_INVOICE_ID\n" +
                "          LEFT JOIN SCM_CONTRACT ON SCM_CONTRACT_INVOICE.SCM_CONTRACT_ID =\n" +
                "                                    SCM_CONTRACT.UUID\n" +
                "         WHERE FAD_INVOICE.UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               (SELECT NVL(MAX(ORDER_NO),0)\n" +
                "                  FROM FAD_CERTIFICATE_DETAIL\n" +
                "                 WHERE FAD_CERTIFICATE_ID =\n" +
                "                       (SELECT UUID\n" +
                "                          FROM FAD_CERTIFICATE\n" +
                "                         WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                           AND ORIGINAL_FORM_TYPE = '" + originalFormType + "')) +\n" +
                "               ROWNUM,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               CASE\n" +
                "                 WHEN FAD_INVOICE.INVOICE_TYPE = '0' THEN\n" +
                "                  (NVL(SCM_CONTRACT_INVOICE.AMOUNT, 0) / (1 + NVL(FAD_INVOICE.TAX_RATE, 0)) * NVL(FAD_INVOICE.TAX_RATE, 0))\n" +
                "                 ELSE\n" +
                "                  0\n" +
                "               END,\n" +
                "               '2221010101',\n" +
                "               '应交税费-应交税金-应交增值税-进项税额',\n" +
                "               '0',\n" +
                "               '" + originalFormType + "', --采购->有请款应付\n" +
                "               SCM_CONTRACT.SCM_CONTRACT_CODE || '(采购合同编号)' || '(' || TO_CHAR(SCM_CONTRACT_INVOICE.CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               SCM_CONTRACT_INVOICE.CREATE_TIME\n" +
                "          FROM FAD_INVOICE\n" +
                "          LEFT JOIN SCM_CONTRACT_INVOICE ON FAD_INVOICE.UUID =\n" +
                "                                            SCM_CONTRACT_INVOICE.FAD_INVOICE_ID\n" +
                "          LEFT JOIN SCM_CONTRACT ON SCM_CONTRACT_INVOICE.SCM_CONTRACT_ID =\n" +
                "                                    SCM_CONTRACT.UUID\n" +
                "         WHERE FAD_INVOICE.UUID = '" + businessId + "'\n" +
                "           AND NVL(FAD_INVOICE.TAX_RATE, 0) > 0\n" +
                "           AND FAD_INVOICE.INVOICE_TYPE = '0'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE,\n" +
                "         SEQ_NO)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               (SELECT NVL(MAX(ORDER_NO),0)\n" +
                "                  FROM FAD_CERTIFICATE_DETAIL\n" +
                "                 WHERE FAD_CERTIFICATE_ID =\n" +
                "                       (SELECT UUID\n" +
                "                          FROM FAD_CERTIFICATE\n" +
                "                         WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                           AND ORIGINAL_FORM_TYPE = '" + originalFormType + "')) +\n" +
                "               ROW_NUMBER() OVER(ORDER BY SCM_CONTRACT_CODE DESC),\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               CREDITOR - SUM_CLEARANCE_MONEY,\n" +
                "               '2202',\n" +
                "               '应付账款',\n" +
                "               '1',\n" +
                "               '" + originalFormType + "', --采购->有请款应付\n" +
                "               SCM_CONTRACT_CODE || '(采购合同编号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME,\n" +
                "               FAD_CERTIFICATE_SEQ_NO.NEXTVAL\n" +
                "          FROM (SELECT INVOICE_TYPE,\n" +
                "                       PAY_STYLE,\n" +
                "                       PROJECT_ID,\n" +
                "                       SUBJECT_CODE,\n" +
                "                       PURCHASE_PACKAGE_ID,\n" +
                "                       OFFICE_ID,\n" +
                "                       OFFICE_NAME,\n" +
                "                       PAYEE,\n" +
                "                       RENDER_PERSON,\n" +
                "                       SUPPLIER_ID,\n" +
                "                       SUPPLIER_NAME,\n" +
                "                       SCM_CONTRACT_CODE,\n" +
                "                       CREATE_TIME,\n" +
                "                       TAX_RATE,\n" +
                "                       DEBTOR,\n" +
                "                       CREDITOR,\n" +
                "                       SUM(CLEARANCE_MONEY) AS SUM_CLEARANCE_MONEY\n" +
                "                  FROM (SELECT FAD_INVOICE.INVOICE_TYPE,\n" +
                "                               FAD_INVOICE.PAY_STYLE,\n" +
                "                               SCM_CONTRACT.PROJECT_ID,\n" +
                "                               SCM_CONTRACT.SUBJECT_CODE,\n" +
                "                               SCM_CONTRACT.PURCHASE_PACKAGE_ID,\n" +
                "                               FAD_INVOICE.OFFICE_ID,\n" +
                "                               FAD_INVOICE.OFFICE_NAME,\n" +
                "                               FAD_INVOICE.PAYEE,\n" +
                "                               FAD_INVOICE.RENDER_PERSON,\n" +
                "                               FAD_INVOICE.SUPPLIER_ID,\n" +
                "                               FAD_INVOICE.SUPPLIER_NAME,\n" +
                "                               SCM_CONTRACT.SCM_CONTRACT_CODE,\n" +
                "                               SCM_CONTRACT_INVOICE.CREATE_TIME,\n" +
                "                               NVL(FAD_INVOICE.TAX_RATE, 0) AS TAX_RATE,\n" +
                "                               NVL(SCM_CONTRACT_INVOICE.AMOUNT, 0) AS DEBTOR,\n" +
                "                               NVL(SCM_CONTRACT_INVOICE.AMOUNT, 0) AS CREDITOR,\n" +
                "                               NVL(FAD_CASH_REQ_INVOICE.CLEARANCE_MONEY, 0) AS CLEARANCE_MONEY\n" +
                "                          FROM FAD_INVOICE\n" +
                "                          LEFT JOIN SCM_CONTRACT_INVOICE ON FAD_INVOICE.UUID =\n" +
                "                                                            SCM_CONTRACT_INVOICE.FAD_INVOICE_ID\n" +
                "                          LEFT JOIN SCM_CONTRACT ON SCM_CONTRACT_INVOICE.SCM_CONTRACT_ID =\n" +
                "                                                    SCM_CONTRACT.UUID\n" +
                "                          LEFT JOIN FAD_CASH_REQ ON SCM_CONTRACT.UUID =\n" +
                "                                                    FAD_CASH_REQ.PURCHASE_CONTRACT_ID\n" +
                "                          LEFT JOIN FAD_CASH_REQ_INVOICE ON FAD_CASH_REQ.UUID =\n" +
                "                                                            FAD_CASH_REQ_INVOICE.FAD_CASH_REQ_ID\n" +
                "                                                        AND FAD_INVOICE.UUID =\n" +
                "                                                            FAD_CASH_REQ_INVOICE.FAD_INVOICE_ID\n" +
                "                         WHERE FAD_INVOICE.UUID = '" + businessId + "') A\n" +
                "                 GROUP BY INVOICE_TYPE,\n" +
                "                          PAY_STYLE,\n" +
                "                          PROJECT_ID,\n" +
                "                          SUBJECT_CODE,\n" +
                "                          PURCHASE_PACKAGE_ID,\n" +
                "                          OFFICE_ID,\n" +
                "                          OFFICE_NAME,\n" +
                "                          PAYEE,\n" +
                "                          RENDER_PERSON,\n" +
                "                          SUPPLIER_ID,\n" +
                "                          SUPPLIER_NAME,\n" +
                "                          SCM_CONTRACT_CODE,\n" +
                "                          CREATE_TIME,\n" +
                "                          TAX_RATE,\n" +
                "                          DEBTOR,\n" +
                "                          CREDITOR) A WHERE (CREDITOR - SUM_CLEARANCE_MONEY) <> 0";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               (SELECT NVL(MAX(ORDER_NO),0)\n" +
                "                  FROM FAD_CERTIFICATE_DETAIL\n" +
                "                 WHERE FAD_CERTIFICATE_ID =\n" +
                "                       (SELECT UUID\n" +
                "                          FROM FAD_CERTIFICATE\n" +
                "                         WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                           AND ORIGINAL_FORM_TYPE = '" + originalFormType + "')) +\n" +
                "               ROW_NUMBER() OVER(ORDER BY SCM_CONTRACT.SCM_CONTRACT_CODE DESC),\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               NVL(VW_REQ_NO_MERGE.CLEARANCE_MONEY, 0),\n" +
                "               '1123',\n" +
                "               '预付账款',\n" +
                "               '1',\n" +
                "               '" + originalFormType + "', --采购->有请款应付\n" +
                "               CASE WHEN VW_REQ_NO_MERGE.RUNNING_NO IS NULL\n" +
                "               THEN\n" +
                "                       SCM_CONTRACT.SCM_CONTRACT_CODE || '(采购合同编号)' || '(' || TO_CHAR(SCM_CONTRACT_INVOICE.CREATE_TIME,'YYYY-MM-DD') || ')'\n" +
                "               ELSE\n" +
                "                       SCM_CONTRACT.SCM_CONTRACT_CODE || '(采购合同编号)' || '(' || TO_CHAR(SCM_CONTRACT_INVOICE.CREATE_TIME,'YYYY-MM-DD') || ')' || '、' || VW_REQ_NO_MERGE.RUNNING_NO || '(请款流水号)' || '(' || TO_CHAR(VW_REQ_NO_MERGE.CREATE_TIME,'YYYY-MM-DD') || ')'\n" +
                "               END,\n" +
                "               SCM_CONTRACT_INVOICE.CREATE_TIME\n" +
                "          FROM FAD_INVOICE\n" +
                "          LEFT JOIN SCM_CONTRACT_INVOICE ON FAD_INVOICE.UUID =\n" +
                "                                            SCM_CONTRACT_INVOICE.FAD_INVOICE_ID\n" +
                "          LEFT JOIN SCM_CONTRACT ON SCM_CONTRACT_INVOICE.SCM_CONTRACT_ID =\n" +
                "                                    SCM_CONTRACT.UUID\n" +
                "          LEFT JOIN VW_REQ_NO_MERGE ON SCM_CONTRACT.UUID =\n" +
                "                                       VW_REQ_NO_MERGE.PURCHASE_CONTRACT_ID\n" +
                "                                   AND FAD_INVOICE.UUID =\n" +
                "                                       VW_REQ_NO_MERGE.FAD_INVOICE_ID\n" +
                "         WHERE FAD_INVOICE.UUID = '" + businessId + "' AND VW_REQ_NO_MERGE.CLEARANCE_MONEY IS NOT NULL";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
    }

    @Override
    public void createCertificateFromBusinessPaymentPurchaseContractPayableInvoiceMaterial(String businessId, String originalFormType) {
        String sql = "";
        DAOMeta daoMeta = null;

        //报销与采购合同发票主表凭证
        createCertificateFromBusinessInvoiceHead(businessId, originalFormType);

        //子表凭证摘要新增
        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               1,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               CASE\n" +
                "                 WHEN INVOICE_TYPE = '0' THEN\n" +
                "                  NVL(EXPENSES_MONEY, 0) - (NVL(EXPENSES_MONEY, 0) / (1 + NVL(TAX_RATE, 0)) * NVL(TAX_RATE, 0))\n" +
                "                 ELSE\n" +
                "                  NVL(EXPENSES_MONEY, 0)\n" +
                "               END,\n" +
                "               '140302',\n" +
                "               '原材料-原料',\n" +
                "               '0',\n" +
                "               '" + originalFormType + "', --采购->有请款应付\n" +
                "               INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME\n" +
                "          FROM FAD_INVOICE\n" +
                "         WHERE UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               2,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               CASE\n" +
                "                 WHEN INVOICE_TYPE = '0' THEN\n" +
                "                  (NVL(EXPENSES_MONEY, 0) / (1 + NVL(TAX_RATE, 0)) * NVL(TAX_RATE, 0))\n" +
                "                 ELSE\n" +
                "                  0\n" +
                "               END,\n" +
                "               '2221010101',\n" +
                "               '应交税费-应交税金-应交增值税-进项税额',\n" +
                "               '0',\n" +
                "               '" + originalFormType + "', --采购->有请款应付\n" +
                "               INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME\n" +
                "          FROM FAD_INVOICE\n" +
                "         WHERE UUID = '" + businessId + "'\n" +
                "           AND NVL(TAX_RATE, 0) > 0\n" +
                "           AND INVOICE_TYPE = '0'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE,\n" +
                "         SEQ_NO)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               (SELECT NVL(MAX(ORDER_NO),0)\n" +
                "                  FROM FAD_CERTIFICATE_DETAIL\n" +
                "                 WHERE FAD_CERTIFICATE_ID =\n" +
                "                       (SELECT UUID\n" +
                "                          FROM FAD_CERTIFICATE\n" +
                "                         WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                           AND ORIGINAL_FORM_TYPE = '" + originalFormType + "')) +\n" +
                "               ROWNUM,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               CREDITOR - SUM_CLEARANCE_MONEY,\n" +
                "               '2202',\n" +
                "               '应付账款',\n" +
                "               '1',\n" +
                "               '" + originalFormType + "', --采购->有请款应付\n" +
                "               INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME,\n" +
                "               FAD_CERTIFICATE_SEQ_NO.NEXTVAL\n" +
                "          FROM (SELECT INVOICE_TYPE,\n" +
                "                       PAY_STYLE,\n" +
                "                       PRM_PROJECT_MAIN_ID,\n" +
                "                       SUBJECT_CODE,\n" +
                "                       SUBJECT_NAME,\n" +
                "                       OFFICE_ID,\n" +
                "                       OFFICE_NAME,\n" +
                "                       PAYEE,\n" +
                "                       RENDER_PERSON,\n" +
                "                       SUPPLIER_ID,\n" +
                "                       SUPPLIER_NAME,\n" +
                "                       INVOICE_REQ_NO,\n" +
                "                       CREATE_TIME,\n" +
                "                       TAX_RATE,\n" +
                "                       DEBTOR,\n" +
                "                       CREDITOR,\n" +
                "                       SUM(CLEARANCE_MONEY) AS SUM_CLEARANCE_MONEY\n" +
                "                  FROM (SELECT FAD_INVOICE.INVOICE_TYPE,\n" +
                "                               FAD_INVOICE.PAY_STYLE,\n" +
                "                               FAD_INVOICE.PRM_PROJECT_MAIN_ID,\n" +
                "                               FAD_INVOICE.SUBJECT_CODE,\n" +
                "                               FAD_INVOICE.SUBJECT_NAME,\n" +
                "                               FAD_INVOICE.OFFICE_ID,\n" +
                "                               FAD_INVOICE.OFFICE_NAME,\n" +
                "                               FAD_INVOICE.PAYEE,\n" +
                "                               FAD_INVOICE.RENDER_PERSON,\n" +
                "                               FAD_INVOICE.SUPPLIER_ID,\n" +
                "                               FAD_INVOICE.SUPPLIER_NAME,\n" +
                "                               FAD_INVOICE.INVOICE_REQ_NO,\n" +
                "                               FAD_INVOICE.CREATE_TIME,\n" +
                "                               NVL(FAD_INVOICE.TAX_RATE, 0) AS TAX_RATE,\n" +
                "                               NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS DEBTOR,\n" +
                "                               NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS CREDITOR,\n" +
                "                               NVL(FAD_CASH_REQ_INVOICE.CLEARANCE_MONEY, 0) AS CLEARANCE_MONEY\n" +
                "                          FROM FAD_INVOICE\n" +
                //"                          LEFT JOIN FAD_INVOICE_MATERIAL ON FAD_INVOICE.UUID =\n" +
                //"                                            FAD_INVOICE_MATERIAL.FAD_INVOICE_ID\n" +
                "                          LEFT JOIN FAD_CASH_REQ_INVOICE ON FAD_INVOICE.UUID =\n" +
                "                                            FAD_CASH_REQ_INVOICE.FAD_INVOICE_ID\n" +
                "                          LEFT JOIN FAD_CASH_REQ ON FAD_CASH_REQ_INVOICE.FAD_CASH_REQ_ID =\n" +
                "                                    FAD_CASH_REQ.UUID\n" +
                "                         WHERE FAD_INVOICE.UUID = '" + businessId + "') A\n" +
                "                 GROUP BY INVOICE_TYPE,\n" +
                "                          PAY_STYLE,\n" +
                "                          PRM_PROJECT_MAIN_ID,\n" +
                "                          SUBJECT_CODE,\n" +
                "                          SUBJECT_NAME,\n" +
                "                          OFFICE_ID,\n" +
                "                          OFFICE_NAME,\n" +
                "                          PAYEE,\n" +
                "                          RENDER_PERSON,\n" +
                "                          SUPPLIER_ID,\n" +
                "                          SUPPLIER_NAME,\n" +
                "                          INVOICE_REQ_NO,\n" +
                "                          CREATE_TIME,\n" +
                "                          TAX_RATE,\n" +
                "                          DEBTOR,\n" +
                "                          CREDITOR) A WHERE (CREDITOR - SUM_CLEARANCE_MONEY) <> 0";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               (SELECT NVL(MAX(ORDER_NO),0)\n" +
                "                  FROM FAD_CERTIFICATE_DETAIL\n" +
                "                 WHERE FAD_CERTIFICATE_ID =\n" +
                "                       (SELECT UUID\n" +
                "                          FROM FAD_CERTIFICATE\n" +
                "                         WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                           AND ORIGINAL_FORM_TYPE = '" + originalFormType + "')) +\n" +
                "               ROWNUM,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               NVL(FAD_CASH_REQ_INVOICE.CLEARANCE_MONEY, 0),\n" +
                "               '1123',\n" +
                "               '预付账款',\n" +
                "               '1',\n" +
                "               '" + originalFormType + "', --采购->有请款应付\n" +
                "               FAD_INVOICE.INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(FAD_INVOICE.CREATE_TIME,'YYYY-MM-DD') || ')' || '、' || FAD_CASH_REQ.RUNNING_NO || '(请款流水号)' || '(' || TO_CHAR(FAD_CASH_REQ_INVOICE.CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               FAD_INVOICE.CREATE_TIME\n" +
                "          FROM FAD_INVOICE\n" +
                //"          LEFT JOIN FAD_INVOICE_MATERIAL ON FAD_INVOICE.UUID =\n" +
                //"                                            FAD_INVOICE_MATERIAL.FAD_INVOICE_ID\n" +
                "          LEFT JOIN FAD_CASH_REQ_INVOICE ON FAD_INVOICE.UUID =\n" +
                "                                            FAD_CASH_REQ_INVOICE.FAD_INVOICE_ID\n" +
                "          LEFT JOIN FAD_CASH_REQ ON FAD_CASH_REQ_INVOICE.FAD_CASH_REQ_ID =\n" +
                "                                    FAD_CASH_REQ.UUID\n" +
                "         WHERE FAD_INVOICE.UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
    }

    @Override
    public void createCertificateFromBusinessPaymentPurchaseContractPaidInvoice(String businessId, String originalFormType) {
        String sql = "";
        DAOMeta daoMeta = null;

        //主表凭证新增
        sql = "INSERT INTO FAD_CERTIFICATE\n" +
                "        (UUID,\n" +
                "         BUSINESS_ID,\n" +
                "         DEBTOR,\n" +
                "         CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE,\n" +
                "         RECEIVER_OR_PAYER_TYPE,\n" +
                "         RECEIVER_OR_PAYER_ID,\n" +
                "         RECEIVER_OR_PAYER_CODE,\n" +
                "         RECEIVER_OR_PAYER_NAME,\n" +
                "         SEQ_NO)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               '" + businessId + "',\n" +
                "               CASE\n" +
                "                 WHEN DEBTOR >= SUM_CLEARANCE_MONEY THEN\n" +
                "                  SUM_CLEARANCE_MONEY\n" +
                "                 ELSE\n" +
                "                  DEBTOR\n" +
                "               END,\n" +
                "               CASE\n" +
                "                 WHEN CREDITOR >= SUM_CLEARANCE_MONEY THEN\n" +
                "                  SUM_CLEARANCE_MONEY\n" +
                "                 ELSE\n" +
                "                  CREDITOR\n" +
                "               END,\n" +
                "               '" + originalFormType + "',\n" +
                "               INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME,\n" +
                "               CASE\n" +
                "                WHEN NVL(PAY_STYLE, '0') IN ('1','3','4','5') THEN\n" +
                "                 'SCM_SUPPLIER'\n" +
                "                ELSE\n" +
                "                 'SCDP_USER'\n" +
                "               END,\n" +
                "               CASE\n" +
                "                WHEN NVL(PAY_STYLE, '0') IN ('1','3','4','5') THEN\n" +
                "                 SUPPLIER_ID\n" +
                "                ELSE\n" +
                "                 (SELECT SCDP_USER.UUID FROM SCDP_USER WHERE SCDP_USER.USER_ID = A.RENDER_PERSON)\n" +
                "               END,\n" +
                "               CASE\n" +
                "                WHEN NVL(PAY_STYLE, '0') IN ('1','3','4','5') THEN\n" +
                "                 (SELECT SCM_SUPPLIER.SUPPLIER_CODE FROM SCM_SUPPLIER WHERE SCM_SUPPLIER.UUID = A.SUPPLIER_ID)\n" +
                "                ELSE\n" +
                "                 RENDER_PERSON\n" +
                "               END,\n" +
                "               CASE\n" +
                "                WHEN NVL(PAY_STYLE, '0') IN ('1','3','4','5') THEN\n" +
                "                 NVL((SELECT SCM_SUPPLIER.COMPLETE_NAME FROM SCM_SUPPLIER WHERE SCM_SUPPLIER.UUID = A.SUPPLIER_ID), A.SUPPLIER_NAME)\n" +
                "                ELSE\n" +
                "                 CASE WHEN PAYEE IS NULL THEN (SELECT SCDP_USER.USER_NAME FROM SCDP_USER WHERE SCDP_USER.USER_ID = A.RENDER_PERSON) ELSE PAYEE END\n" +
                "               END,\n" +
                "               FAD_CERTIFICATE_SEQ_NO.NEXTVAL\n" +
                "          FROM (SELECT INVOICE_REQ_NO,\n" +
                "                       PAY_STYLE,\n" +
                "                       PRM_PROJECT_MAIN_ID,\n" +
                "                       SUBJECT_CODE,\n" +
                "                       SUBJECT_NAME,\n" +
                "                       OFFICE_ID,\n" +
                "                       OFFICE_NAME,\n" +
                "                       PAYEE,\n" +
                "                       RENDER_PERSON,\n" +
                "                       SUPPLIER_ID,\n" +
                "                       SUPPLIER_NAME,\n" +
                "                       CREATE_TIME,\n" +
                "                       DEBTOR,\n" +
                "                       CREDITOR,\n" +
                "                       SUM(CLEARANCE_MONEY) AS SUM_CLEARANCE_MONEY\n" +
                "                  FROM (SELECT FAD_INVOICE.INVOICE_REQ_NO,\n" +
                "                               FAD_INVOICE.PAY_STYLE,\n" +
                "                               FAD_INVOICE.PRM_PROJECT_MAIN_ID,\n" +
                "                               FAD_INVOICE.SUBJECT_CODE,\n" +
                "                               FAD_INVOICE.SUBJECT_NAME,\n" +
                "                               FAD_INVOICE.OFFICE_ID,\n" +
                "                               FAD_INVOICE.OFFICE_NAME,\n" +
                "                               FAD_INVOICE.PAYEE,\n" +
                "                               FAD_INVOICE.RENDER_PERSON,\n" +
                "                               FAD_INVOICE.SUPPLIER_ID,\n" +
                "                               FAD_INVOICE.SUPPLIER_NAME,\n" +
                "                               FAD_INVOICE.CREATE_TIME,\n" +
                "                               NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS DEBTOR,\n" +
                "                               NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS CREDITOR,\n" +
                "                               NVL(FAD_CASH_REQ_INVOICE.CLEARANCE_MONEY, 0) AS CLEARANCE_MONEY\n" +
                "                          FROM FAD_INVOICE\n" +
                "                          LEFT JOIN FAD_CASH_REQ_INVOICE ON FAD_INVOICE.UUID =\n" +
                "                                                            FAD_CASH_REQ_INVOICE.FAD_INVOICE_ID\n" +
                "                         WHERE FAD_INVOICE.UUID = '" + businessId + "') A\n" +
                "                 GROUP BY INVOICE_REQ_NO,\n" +
                "                          PAY_STYLE,\n" +
                "                          PRM_PROJECT_MAIN_ID,\n" +
                "                          SUBJECT_CODE,\n" +
                "                          SUBJECT_NAME,\n" +
                "                          OFFICE_ID,\n" +
                "                          OFFICE_NAME,\n" +
                "                          PAYEE,\n" +
                "                          RENDER_PERSON,\n" +
                "                          SUPPLIER_ID,\n" +
                "                          SUPPLIER_NAME,\n" +
                "                          CREATE_TIME,\n" +
                "                          DEBTOR,\n" +
                "                          CREDITOR) A";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        //子表凭证摘要新增
        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE,\n" +
                "         SEQ_NO)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               1,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               CASE\n" +
                "                  WHEN INVOICE_TYPE = '0' THEN\n" +
                "                   (CASE\n" +
                "                  WHEN DEBTOR >= SUM_CLEARANCE_MONEY THEN\n" +
                "                   SUM_CLEARANCE_MONEY - (SUM_CLEARANCE_MONEY / (1 + TAX_RATE) * TAX_RATE)\n" +
                "                  ELSE\n" +
                "                   DEBTOR - (DEBTOR / (1 + TAX_RATE) * TAX_RATE)\n" +
                "                END) ELSE(CASE\n" +
                "                 WHEN DEBTOR >= SUM_CLEARANCE_MONEY THEN\n" +
                "                  SUM_CLEARANCE_MONEY\n" +
                "                 ELSE\n" +
                "                  DEBTOR\n" +
                "               END) END,\n" +
                "               '140302',\n" +
                "               '原材料-原料',\n" +
                "               '0',\n" +
                "               '" + originalFormType + "', --采购->有请款预付\n" +
                "               INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME,\n" +
                "               FAD_CERTIFICATE_SEQ_NO.NEXTVAL\n" +
                "          FROM (SELECT INVOICE_TYPE,\n" +
                "                       INVOICE_REQ_NO,\n" +
                "                       PAY_STYLE,\n" +
                "                       PRM_PROJECT_MAIN_ID,\n" +
                "                       SUBJECT_CODE,\n" +
                "                       SUBJECT_NAME,\n" +
                "                       OFFICE_ID,\n" +
                "                       OFFICE_NAME,\n" +
                "                       PAYEE,\n" +
                "                       RENDER_PERSON,\n" +
                "                       SUPPLIER_ID,\n" +
                "                       SUPPLIER_NAME,\n" +
                "                       CREATE_TIME,\n" +
                "                       TAX_RATE,\n" +
                "                       DEBTOR,\n" +
                "                       CREDITOR,\n" +
                "                       SUM(CLEARANCE_MONEY) AS SUM_CLEARANCE_MONEY\n" +
                "                  FROM (SELECT FAD_INVOICE.INVOICE_TYPE,\n" +
                "                               FAD_INVOICE.INVOICE_REQ_NO,\n" +
                "                               FAD_INVOICE.PAY_STYLE,\n" +
                "                               FAD_INVOICE.PRM_PROJECT_MAIN_ID,\n" +
                "                               FAD_INVOICE.SUBJECT_CODE,\n" +
                "                               FAD_INVOICE.SUBJECT_NAME,\n" +
                "                               FAD_INVOICE.OFFICE_ID,\n" +
                "                               FAD_INVOICE.OFFICE_NAME,\n" +
                "                               FAD_INVOICE.PAYEE,\n" +
                "                               FAD_INVOICE.RENDER_PERSON,\n" +
                "                               FAD_INVOICE.SUPPLIER_ID,\n" +
                "                               FAD_INVOICE.SUPPLIER_NAME,\n" +
                "                               FAD_INVOICE.CREATE_TIME,\n" +
                "                               NVL(FAD_INVOICE.TAX_RATE, 0) AS TAX_RATE,\n" +
                "                               NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS DEBTOR,\n" +
                "                               NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS CREDITOR,\n" +
                "                               NVL(FAD_CASH_REQ_INVOICE.CLEARANCE_MONEY, 0) AS CLEARANCE_MONEY\n" +
                "                          FROM FAD_INVOICE\n" +
                "                          LEFT JOIN FAD_CASH_REQ_INVOICE ON FAD_INVOICE.UUID =\n" +
                "                                                            FAD_CASH_REQ_INVOICE.FAD_INVOICE_ID\n" +
                "                         WHERE FAD_INVOICE.UUID = '" + businessId + "') A\n" +
                "                 GROUP BY INVOICE_TYPE,\n" +
                "                          INVOICE_REQ_NO,\n" +
                "                          PAY_STYLE,\n" +
                "                          PRM_PROJECT_MAIN_ID,\n" +
                "                          SUBJECT_CODE,\n" +
                "                          SUBJECT_NAME,\n" +
                "                          OFFICE_ID,\n" +
                "                          OFFICE_NAME,\n" +
                "                          PAYEE,\n" +
                "                          RENDER_PERSON,\n" +
                "                          SUPPLIER_ID,\n" +
                "                          SUPPLIER_NAME,\n" +
                "                          CREATE_TIME,\n" +
                "                          TAX_RATE,\n" +
                "                          DEBTOR,\n" +
                "                          CREDITOR) A";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE,\n" +
                "         SEQ_NO)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               2,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               CASE\n" +
                "                 WHEN INVOICE_TYPE = '0' THEN\n" +
                "                  CASE\n" +
                "                 WHEN DEBTOR >= SUM_CLEARANCE_MONEY THEN\n" +
                "                  (SUM_CLEARANCE_MONEY / (1 + TAX_RATE) * TAX_RATE)\n" +
                "                 ELSE\n" +
                "                  (DEBTOR / (1 + TAX_RATE) * TAX_RATE)\n" +
                "               END ELSE 0 END,\n" +
                "               '2221010101',\n" +
                "               '应交税费-应交税金-应交增值税-进项税额',\n" +
                "               '0',\n" +
                "               '" + originalFormType + "', --采购->有请款预付\n" +
                "               INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME,\n" +
                "               FAD_CERTIFICATE_SEQ_NO.NEXTVAL\n" +
                "          FROM (SELECT INVOICE_TYPE,\n" +
                "                       INVOICE_REQ_NO,\n" +
                "                       PAY_STYLE,\n" +
                "                       PRM_PROJECT_MAIN_ID,\n" +
                "                       SUBJECT_CODE,\n" +
                "                       SUBJECT_NAME,\n" +
                "                       OFFICE_ID,\n" +
                "                       OFFICE_NAME,\n" +
                "                       PAYEE,\n" +
                "                       RENDER_PERSON,\n" +
                "                       SUPPLIER_ID,\n" +
                "                       SUPPLIER_NAME,\n" +
                "                       CREATE_TIME,\n" +
                "                       TAX_RATE,\n" +
                "                       DEBTOR,\n" +
                "                       CREDITOR,\n" +
                "                       SUM(CLEARANCE_MONEY) AS SUM_CLEARANCE_MONEY\n" +
                "                  FROM (SELECT FAD_INVOICE.INVOICE_TYPE,\n" +
                "                               FAD_INVOICE.INVOICE_REQ_NO,\n" +
                "                               FAD_INVOICE.PAY_STYLE,\n" +
                "                               FAD_INVOICE.PRM_PROJECT_MAIN_ID,\n" +
                "                               FAD_INVOICE.SUBJECT_CODE,\n" +
                "                               FAD_INVOICE.SUBJECT_NAME,\n" +
                "                               FAD_INVOICE.OFFICE_ID,\n" +
                "                               FAD_INVOICE.OFFICE_NAME,\n" +
                "                               FAD_INVOICE.PAYEE,\n" +
                "                               FAD_INVOICE.RENDER_PERSON,\n" +
                "                               FAD_INVOICE.SUPPLIER_ID,\n" +
                "                               FAD_INVOICE.SUPPLIER_NAME,\n" +
                "                               FAD_INVOICE.CREATE_TIME,\n" +
                "                               NVL(FAD_INVOICE.TAX_RATE, 0) AS TAX_RATE,\n" +
                "                               NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS DEBTOR,\n" +
                "                               NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS CREDITOR,\n" +
                "                               NVL(FAD_CASH_REQ_INVOICE.CLEARANCE_MONEY, 0) AS CLEARANCE_MONEY\n" +
                "                          FROM FAD_INVOICE\n" +
                "                          LEFT JOIN FAD_CASH_REQ_INVOICE ON FAD_INVOICE.UUID =\n" +
                "                                                            FAD_CASH_REQ_INVOICE.FAD_INVOICE_ID\n" +
                "                         WHERE FAD_INVOICE.UUID = '" + businessId + "') A\n" +
                "                 GROUP BY INVOICE_TYPE,\n" +
                "                          INVOICE_REQ_NO,\n" +
                "                          PAY_STYLE,\n" +
                "                          PRM_PROJECT_MAIN_ID,\n" +
                "                          SUBJECT_CODE,\n" +
                "                          SUBJECT_NAME,\n" +
                "                          OFFICE_ID,\n" +
                "                          OFFICE_NAME,\n" +
                "                          PAYEE,\n" +
                "                          RENDER_PERSON,\n" +
                "                          SUPPLIER_ID,\n" +
                "                          SUPPLIER_NAME,\n" +
                "                          CREATE_TIME,\n" +
                "                          TAX_RATE,\n" +
                "                          DEBTOR,\n" +
                "                          CREDITOR) A\n" +
                "         WHERE TAX_RATE > 0\n" +
                "           AND INVOICE_TYPE = '0'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               (SELECT NVL(MAX(ORDER_NO),0)\n" +
                "                  FROM FAD_CERTIFICATE_DETAIL\n" +
                "                 WHERE FAD_CERTIFICATE_ID =\n" +
                "                       (SELECT UUID\n" +
                "                          FROM FAD_CERTIFICATE\n" +
                "                         WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                           AND ORIGINAL_FORM_TYPE = '" + originalFormType + "')) +\n" +
                "               ROW_NUMBER() OVER(ORDER BY SCM_CONTRACT.SCM_CONTRACT_CODE DESC),\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               NVL(SCM_CONTRACT_INVOICE.AMOUNT, 0),\n" +
                "               '1123',\n" +
                "               '预付账款',\n" +
                "               '1',\n" +
                "               '" + originalFormType + "', --采购->有请款预付\n" +
                "               CASE WHEN VW_REQ_NO_MERGE.RUNNING_NO IS NULL\n" +
                "               THEN\n" +
                "                       SCM_CONTRACT.SCM_CONTRACT_CODE || '(采购合同编号)' || '(' || TO_CHAR(SCM_CONTRACT_INVOICE.CREATE_TIME,'YYYY-MM-DD') || ')'\n" +
                "               ELSE\n" +
                "                       SCM_CONTRACT.SCM_CONTRACT_CODE || '(采购合同编号)' || '(' || TO_CHAR(SCM_CONTRACT_INVOICE.CREATE_TIME,'YYYY-MM-DD') || ')' || '、' || VW_REQ_NO_MERGE.RUNNING_NO || '(请款流水号)' || '(' || TO_CHAR(VW_REQ_NO_MERGE.CREATE_TIME,'YYYY-MM-DD') || ')'\n" +
                "               END,\n" +
                "               SCM_CONTRACT_INVOICE.CREATE_TIME\n" +
                "          FROM FAD_INVOICE\n" +
                "          LEFT JOIN SCM_CONTRACT_INVOICE ON FAD_INVOICE.UUID =\n" +
                "                                            SCM_CONTRACT_INVOICE.FAD_INVOICE_ID\n" +
                "          LEFT JOIN SCM_CONTRACT ON SCM_CONTRACT_INVOICE.SCM_CONTRACT_ID =\n" +
                "                                    SCM_CONTRACT.UUID\n" +
                "          LEFT JOIN VW_REQ_NO_MERGE ON SCM_CONTRACT.UUID =\n" +
                "                                       VW_REQ_NO_MERGE.PURCHASE_CONTRACT_ID\n" +
                "                                   AND FAD_INVOICE.UUID =\n" +
                "                                       VW_REQ_NO_MERGE.FAD_INVOICE_ID\n" +
                "         WHERE FAD_INVOICE.UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
    }

    @Override
    public void createCertificateFromBusinessPaymentPurchaseContractPaidInvoiceWaixie(String businessId, String originalFormType) {
        String sql = "";
        DAOMeta daoMeta = null;

        //主表凭证新增
        sql = "INSERT INTO FAD_CERTIFICATE\n" +
                "        (UUID,\n" +
                "         BUSINESS_ID,\n" +
                "         DEBTOR,\n" +
                "         CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE,\n" +
                "         RECEIVER_OR_PAYER_TYPE,\n" +
                "         RECEIVER_OR_PAYER_ID,\n" +
                "         RECEIVER_OR_PAYER_CODE,\n" +
                "         RECEIVER_OR_PAYER_NAME,\n" +
                "         SEQ_NO)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               '" + businessId + "',\n" +
                "               CASE\n" +
                "                 WHEN DEBTOR >= SUM_CLEARANCE_MONEY THEN\n" +
                "                  SUM_CLEARANCE_MONEY\n" +
                "                 ELSE\n" +
                "                  DEBTOR\n" +
                "               END,\n" +
                "               CASE\n" +
                "                 WHEN CREDITOR >= SUM_CLEARANCE_MONEY THEN\n" +
                "                  SUM_CLEARANCE_MONEY\n" +
                "                 ELSE\n" +
                "                  CREDITOR\n" +
                "               END,\n" +
                "               '" + originalFormType + "',\n" +
                "               INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME,\n" +
                "               CASE\n" +
                "                WHEN NVL(PAY_STYLE, '0') IN ('1','3','4','5') THEN\n" +
                "                 'SCM_SUPPLIER'\n" +
                "                ELSE\n" +
                "                 'SCDP_USER'\n" +
                "               END,\n" +
                "               CASE\n" +
                "                WHEN NVL(PAY_STYLE, '0') IN ('1','3','4','5') THEN\n" +
                "                 SUPPLIER_ID\n" +
                "                ELSE\n" +
                "                 (SELECT SCDP_USER.UUID FROM SCDP_USER WHERE SCDP_USER.USER_ID = A.RENDER_PERSON)\n" +
                "               END,\n" +
                "               CASE\n" +
                "                WHEN NVL(PAY_STYLE, '0') IN ('1','3','4','5') THEN\n" +
                "                 (SELECT SCM_SUPPLIER.SUPPLIER_CODE FROM SCM_SUPPLIER WHERE SCM_SUPPLIER.UUID = A.SUPPLIER_ID)\n" +
                "                ELSE\n" +
                "                 RENDER_PERSON\n" +
                "               END,\n" +
                "               CASE\n" +
                "                WHEN NVL(PAY_STYLE, '0') IN ('1','3','4','5') THEN\n" +
                "                 NVL((SELECT SCM_SUPPLIER.COMPLETE_NAME FROM SCM_SUPPLIER WHERE SCM_SUPPLIER.UUID = A.SUPPLIER_ID), A.SUPPLIER_NAME)\n" +
                "                ELSE\n" +
                "                 CASE WHEN PAYEE IS NULL THEN (SELECT SCDP_USER.USER_NAME FROM SCDP_USER WHERE SCDP_USER.USER_ID = A.RENDER_PERSON) ELSE PAYEE END\n" +
                "               END,\n" +
                "               FAD_CERTIFICATE_SEQ_NO.NEXTVAL\n" +
                "          FROM (SELECT INVOICE_REQ_NO,\n" +
                "                       PAY_STYLE,\n" +
                "                       PRM_PROJECT_MAIN_ID,\n" +
                "                       SUBJECT_CODE,\n" +
                "                       SUBJECT_NAME,\n" +
                "                       OFFICE_ID,\n" +
                "                       OFFICE_NAME,\n" +
                "                       PAYEE,\n" +
                "                       RENDER_PERSON,\n" +
                "                       SUPPLIER_ID,\n" +
                "                       SUPPLIER_NAME,\n" +
                "                       CREATE_TIME,\n" +
                "                       DEBTOR,\n" +
                "                       CREDITOR,\n" +
                "                       SUM(CLEARANCE_MONEY) AS SUM_CLEARANCE_MONEY\n" +
                "                  FROM (SELECT FAD_INVOICE.INVOICE_REQ_NO,\n" +
                "                               FAD_INVOICE.PAY_STYLE,\n" +
                "                               FAD_INVOICE.PRM_PROJECT_MAIN_ID,\n" +
                "                               FAD_INVOICE.SUBJECT_CODE,\n" +
                "                               FAD_INVOICE.SUBJECT_NAME,\n" +
                "                               FAD_INVOICE.OFFICE_ID,\n" +
                "                               FAD_INVOICE.OFFICE_NAME,\n" +
                "                               FAD_INVOICE.PAYEE,\n" +
                "                               FAD_INVOICE.RENDER_PERSON,\n" +
                "                               FAD_INVOICE.SUPPLIER_ID,\n" +
                "                               FAD_INVOICE.SUPPLIER_NAME,\n" +
                "                               FAD_INVOICE.CREATE_TIME,\n" +
                "                               NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS DEBTOR,\n" +
                "                               NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS CREDITOR,\n" +
                "                               NVL(FAD_CASH_REQ_INVOICE.CLEARANCE_MONEY, 0) AS CLEARANCE_MONEY\n" +
                "                          FROM FAD_INVOICE\n" +
                "                          LEFT JOIN FAD_CASH_REQ_INVOICE ON FAD_INVOICE.UUID =\n" +
                "                                                            FAD_CASH_REQ_INVOICE.FAD_INVOICE_ID\n" +
                "                         WHERE FAD_INVOICE.UUID = '" + businessId + "') A\n" +
                "                 GROUP BY INVOICE_REQ_NO,\n" +
                "                          PAY_STYLE,\n" +
                "                          PRM_PROJECT_MAIN_ID,\n" +
                "                          SUBJECT_CODE,\n" +
                "                          SUBJECT_NAME,\n" +
                "                          OFFICE_ID,\n" +
                "                          OFFICE_NAME,\n" +
                "                          PAYEE,\n" +
                "                          RENDER_PERSON,\n" +
                "                          SUPPLIER_ID,\n" +
                "                          SUPPLIER_NAME,\n" +
                "                          CREATE_TIME,\n" +
                "                          DEBTOR,\n" +
                "                          CREDITOR) A";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        //子表凭证摘要新增
        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE,\n" +
                "         SEQ_NO)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               1,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               CASE\n" +
                "                  WHEN INVOICE_TYPE = '0' THEN\n" +
                "                   (CASE\n" +
                "                  WHEN DEBTOR >= SUM_CLEARANCE_MONEY THEN\n" +
                "                   SUM_CLEARANCE_MONEY - (SUM_CLEARANCE_MONEY / (1 + TAX_RATE) * TAX_RATE)\n" +
                "                  ELSE\n" +
                "                   DEBTOR - (DEBTOR / (1 + TAX_RATE) * TAX_RATE)\n" +
                "                END) ELSE(CASE\n" +
                "                 WHEN DEBTOR >= SUM_CLEARANCE_MONEY THEN\n" +
                "                  SUM_CLEARANCE_MONEY\n" +
                "                 ELSE\n" +
                "                  DEBTOR\n" +
                "               END) END,\n" +
                "               '5401',\n" +
                "               '工程施工',\n" +
                "               '0',\n" +
                "               '" + originalFormType + "', --采购->有请款预付\n" +
                "               INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME,\n" +
                "               FAD_CERTIFICATE_SEQ_NO.NEXTVAL\n" +
                "          FROM (SELECT INVOICE_TYPE,\n" +
                "                       INVOICE_REQ_NO,\n" +
                "                       PAY_STYLE,\n" +
                "                       PRM_PROJECT_MAIN_ID,\n" +
                "                       SUBJECT_CODE,\n" +
                "                       SUBJECT_NAME,\n" +
                "                       OFFICE_ID,\n" +
                "                       OFFICE_NAME,\n" +
                "                       PAYEE,\n" +
                "                       RENDER_PERSON,\n" +
                "                       SUPPLIER_ID,\n" +
                "                       SUPPLIER_NAME,\n" +
                "                       CREATE_TIME,\n" +
                "                       TAX_RATE,\n" +
                "                       DEBTOR,\n" +
                "                       CREDITOR,\n" +
                "                       SUM(CLEARANCE_MONEY) AS SUM_CLEARANCE_MONEY\n" +
                "                  FROM (SELECT FAD_INVOICE.INVOICE_TYPE,\n" +
                "                               FAD_INVOICE.INVOICE_REQ_NO,\n" +
                "                               FAD_INVOICE.PAY_STYLE,\n" +
                "                               FAD_INVOICE.PRM_PROJECT_MAIN_ID,\n" +
                "                               FAD_INVOICE.SUBJECT_CODE,\n" +
                "                               FAD_INVOICE.SUBJECT_NAME,\n" +
                "                               FAD_INVOICE.OFFICE_ID,\n" +
                "                               FAD_INVOICE.OFFICE_NAME,\n" +
                "                               FAD_INVOICE.PAYEE,\n" +
                "                               FAD_INVOICE.RENDER_PERSON,\n" +
                "                               FAD_INVOICE.SUPPLIER_ID,\n" +
                "                               FAD_INVOICE.SUPPLIER_NAME,\n" +
                "                               FAD_INVOICE.CREATE_TIME,\n" +
                "                               NVL(FAD_INVOICE.TAX_RATE, 0) AS TAX_RATE,\n" +
                "                               NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS DEBTOR,\n" +
                "                               NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS CREDITOR,\n" +
                "                               NVL(FAD_CASH_REQ_INVOICE.CLEARANCE_MONEY, 0) AS CLEARANCE_MONEY\n" +
                "                          FROM FAD_INVOICE\n" +
                "                          LEFT JOIN FAD_CASH_REQ_INVOICE ON FAD_INVOICE.UUID =\n" +
                "                                                            FAD_CASH_REQ_INVOICE.FAD_INVOICE_ID\n" +
                "                         WHERE FAD_INVOICE.UUID = '" + businessId + "') A\n" +
                "                 GROUP BY INVOICE_TYPE,\n" +
                "                          INVOICE_REQ_NO,\n" +
                "                          PAY_STYLE,\n" +
                "                          PRM_PROJECT_MAIN_ID,\n" +
                "                          SUBJECT_CODE,\n" +
                "                          SUBJECT_NAME,\n" +
                "                          OFFICE_ID,\n" +
                "                          OFFICE_NAME,\n" +
                "                          PAYEE,\n" +
                "                          RENDER_PERSON,\n" +
                "                          SUPPLIER_ID,\n" +
                "                          SUPPLIER_NAME,\n" +
                "                          CREATE_TIME,\n" +
                "                          TAX_RATE,\n" +
                "                          DEBTOR,\n" +
                "                          CREDITOR) A";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE,\n" +
                "         SEQ_NO)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               2,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               CASE\n" +
                "                 WHEN INVOICE_TYPE = '0' THEN\n" +
                "                  CASE\n" +
                "                 WHEN DEBTOR >= SUM_CLEARANCE_MONEY THEN\n" +
                "                  (SUM_CLEARANCE_MONEY / (1 + TAX_RATE) * TAX_RATE)\n" +
                "                 ELSE\n" +
                "                  (DEBTOR / (1 + TAX_RATE) * TAX_RATE)\n" +
                "               END ELSE 0 END,\n" +
                "               '2221010101',\n" +
                "               '应交税费-应交税金-应交增值税-进项税额',\n" +
                "               '0',\n" +
                "               '" + originalFormType + "', --采购->有请款预付\n" +
                "               INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME,\n" +
                "               FAD_CERTIFICATE_SEQ_NO.NEXTVAL\n" +
                "          FROM (SELECT INVOICE_TYPE,\n" +
                "                       INVOICE_REQ_NO,\n" +
                "                       PAY_STYLE,\n" +
                "                       PRM_PROJECT_MAIN_ID,\n" +
                "                       SUBJECT_CODE,\n" +
                "                       SUBJECT_NAME,\n" +
                "                       OFFICE_ID,\n" +
                "                       OFFICE_NAME,\n" +
                "                       PAYEE,\n" +
                "                       RENDER_PERSON,\n" +
                "                       SUPPLIER_ID,\n" +
                "                       SUPPLIER_NAME,\n" +
                "                       CREATE_TIME,\n" +
                "                       TAX_RATE,\n" +
                "                       DEBTOR,\n" +
                "                       CREDITOR,\n" +
                "                       SUM(CLEARANCE_MONEY) AS SUM_CLEARANCE_MONEY\n" +
                "                  FROM (SELECT FAD_INVOICE.INVOICE_TYPE,\n" +
                "                               FAD_INVOICE.INVOICE_REQ_NO,\n" +
                "                               FAD_INVOICE.PAY_STYLE,\n" +
                "                               FAD_INVOICE.PRM_PROJECT_MAIN_ID,\n" +
                "                               FAD_INVOICE.SUBJECT_CODE,\n" +
                "                               FAD_INVOICE.SUBJECT_NAME,\n" +
                "                               FAD_INVOICE.OFFICE_ID,\n" +
                "                               FAD_INVOICE.OFFICE_NAME,\n" +
                "                               FAD_INVOICE.PAYEE,\n" +
                "                               FAD_INVOICE.RENDER_PERSON,\n" +
                "                               FAD_INVOICE.SUPPLIER_ID,\n" +
                "                               FAD_INVOICE.SUPPLIER_NAME,\n" +
                "                               FAD_INVOICE.CREATE_TIME,\n" +
                "                               NVL(FAD_INVOICE.TAX_RATE, 0) AS TAX_RATE,\n" +
                "                               NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS DEBTOR,\n" +
                "                               NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS CREDITOR,\n" +
                "                               NVL(FAD_CASH_REQ_INVOICE.CLEARANCE_MONEY, 0) AS CLEARANCE_MONEY\n" +
                "                          FROM FAD_INVOICE\n" +
                "                          LEFT JOIN FAD_CASH_REQ_INVOICE ON FAD_INVOICE.UUID =\n" +
                "                                                            FAD_CASH_REQ_INVOICE.FAD_INVOICE_ID\n" +
                "                         WHERE FAD_INVOICE.UUID = '" + businessId + "') A\n" +
                "                 GROUP BY INVOICE_TYPE,\n" +
                "                          INVOICE_REQ_NO,\n" +
                "                          PAY_STYLE,\n" +
                "                          PRM_PROJECT_MAIN_ID,\n" +
                "                          SUBJECT_CODE,\n" +
                "                          SUBJECT_NAME,\n" +
                "                          OFFICE_ID,\n" +
                "                          OFFICE_NAME,\n" +
                "                          PAYEE,\n" +
                "                          RENDER_PERSON,\n" +
                "                          SUPPLIER_ID,\n" +
                "                          SUPPLIER_NAME,\n" +
                "                          CREATE_TIME,\n" +
                "                          TAX_RATE,\n" +
                "                          DEBTOR,\n" +
                "                          CREDITOR) A\n" +
                "         WHERE TAX_RATE > 0\n" +
                "           AND INVOICE_TYPE = '0'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               (SELECT NVL(MAX(ORDER_NO),0)\n" +
                "                  FROM FAD_CERTIFICATE_DETAIL\n" +
                "                 WHERE FAD_CERTIFICATE_ID =\n" +
                "                       (SELECT UUID\n" +
                "                          FROM FAD_CERTIFICATE\n" +
                "                         WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                           AND ORIGINAL_FORM_TYPE = '" + originalFormType + "')) +\n" +
                "               ROW_NUMBER() OVER(ORDER BY SCM_CONTRACT.SCM_CONTRACT_CODE DESC),\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               NVL(SCM_CONTRACT_INVOICE.AMOUNT, 0),\n" +
                "               '1123',\n" +
                "               '预付账款',\n" +
                "               '1',\n" +
                "               '" + originalFormType + "', --采购->有请款预付\n" +
                "               CASE WHEN VW_REQ_NO_MERGE.RUNNING_NO IS NULL\n" +
                "               THEN\n" +
                "                       SCM_CONTRACT.SCM_CONTRACT_CODE || '(采购合同编号)' || '(' || TO_CHAR(SCM_CONTRACT_INVOICE.CREATE_TIME,'YYYY-MM-DD') || ')'\n" +
                "               ELSE\n" +
                "                       SCM_CONTRACT.SCM_CONTRACT_CODE || '(采购合同编号)' || '(' || TO_CHAR(SCM_CONTRACT_INVOICE.CREATE_TIME,'YYYY-MM-DD') || ')' || '、' || VW_REQ_NO_MERGE.RUNNING_NO || '(请款流水号)' || '(' || TO_CHAR(VW_REQ_NO_MERGE.CREATE_TIME,'YYYY-MM-DD') || ')'\n" +
                "               END,\n" +
                "               SCM_CONTRACT_INVOICE.CREATE_TIME\n" +
                "          FROM FAD_INVOICE\n" +
                "          LEFT JOIN SCM_CONTRACT_INVOICE ON FAD_INVOICE.UUID =\n" +
                "                                            SCM_CONTRACT_INVOICE.FAD_INVOICE_ID\n" +
                "          LEFT JOIN SCM_CONTRACT ON SCM_CONTRACT_INVOICE.SCM_CONTRACT_ID =\n" +
                "                                    SCM_CONTRACT.UUID\n" +
                "          LEFT JOIN VW_REQ_NO_MERGE ON SCM_CONTRACT.UUID =\n" +
                "                                       VW_REQ_NO_MERGE.PURCHASE_CONTRACT_ID\n" +
                "                                   AND FAD_INVOICE.UUID =\n" +
                "                                       VW_REQ_NO_MERGE.FAD_INVOICE_ID\n" +
                "         WHERE FAD_INVOICE.UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
    }

    @Override
    public void createCertificateFromBusinessPaymentPurchaseContractPaidInvoiceMaterial(String businessId, String originalFormType) {
        String sql = "";
        DAOMeta daoMeta = null;

        //主表凭证新增
        sql = "INSERT INTO FAD_CERTIFICATE\n" +
                "        (UUID,\n" +
                "         BUSINESS_ID,\n" +
                "         DEBTOR,\n" +
                "         CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE,\n" +
                "         RECEIVER_OR_PAYER_TYPE,\n" +
                "         RECEIVER_OR_PAYER_ID,\n" +
                "         RECEIVER_OR_PAYER_CODE,\n" +
                "         RECEIVER_OR_PAYER_NAME,\n" +
                "         SEQ_NO)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               '" + businessId + "',\n" +
                "               CASE\n" +
                "                 WHEN DEBTOR >= SUM_CLEARANCE_MONEY THEN\n" +
                "                  SUM_CLEARANCE_MONEY\n" +
                "                 ELSE\n" +
                "                  DEBTOR\n" +
                "               END,\n" +
                "               CASE\n" +
                "                 WHEN CREDITOR >= SUM_CLEARANCE_MONEY THEN\n" +
                "                  SUM_CLEARANCE_MONEY\n" +
                "                 ELSE\n" +
                "                  CREDITOR\n" +
                "               END,\n" +
                "               '" + originalFormType + "',\n" +
                "               INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME,\n" +
                "               CASE\n" +
                "                WHEN NVL(PAY_STYLE, '0') IN ('1','3','4','5') THEN\n" +
                "                 'SCM_SUPPLIER'\n" +
                "                ELSE\n" +
                "                 'SCDP_USER'\n" +
                "               END,\n" +
                "               CASE\n" +
                "                WHEN NVL(PAY_STYLE, '0') IN ('1','3','4','5') THEN\n" +
                "                 SUPPLIER_ID\n" +
                "                ELSE\n" +
                "                 (SELECT SCDP_USER.UUID FROM SCDP_USER WHERE SCDP_USER.USER_ID = A.RENDER_PERSON)\n" +
                "               END,\n" +
                "               CASE\n" +
                "                WHEN NVL(PAY_STYLE, '0') IN ('1','3','4','5') THEN\n" +
                "                 (SELECT SCM_SUPPLIER.SUPPLIER_CODE FROM SCM_SUPPLIER WHERE SCM_SUPPLIER.UUID = A.SUPPLIER_ID)\n" +
                "                ELSE\n" +
                "                 RENDER_PERSON\n" +
                "               END,\n" +
                "               CASE\n" +
                "                WHEN NVL(PAY_STYLE, '0') IN ('1','3','4','5') THEN\n" +
                "                 NVL((SELECT SCM_SUPPLIER.COMPLETE_NAME FROM SCM_SUPPLIER WHERE SCM_SUPPLIER.UUID = A.SUPPLIER_ID), A.SUPPLIER_NAME)\n" +
                "                ELSE\n" +
                "                 CASE WHEN PAYEE IS NULL THEN (SELECT SCDP_USER.USER_NAME FROM SCDP_USER WHERE SCDP_USER.USER_ID = A.RENDER_PERSON) ELSE PAYEE END\n" +
                "               END,\n" +
                "               FAD_CERTIFICATE_SEQ_NO.NEXTVAL\n" +
                "          FROM (SELECT INVOICE_REQ_NO,\n" +
                "                       PAY_STYLE,\n" +
                "                       PRM_PROJECT_MAIN_ID,\n" +
                "                       SUBJECT_CODE,\n" +
                "                       SUBJECT_NAME,\n" +
                "                       OFFICE_ID,\n" +
                "                       OFFICE_NAME,\n" +
                "                       PAYEE,\n" +
                "                       RENDER_PERSON,\n" +
                "                       SUPPLIER_ID,\n" +
                "                       SUPPLIER_NAME,\n" +
                "                       CREATE_TIME,\n" +
                "                       DEBTOR,\n" +
                "                       CREDITOR,\n" +
                "                       SUM(CLEARANCE_MONEY) AS SUM_CLEARANCE_MONEY\n" +
                "                  FROM (SELECT FAD_INVOICE.INVOICE_REQ_NO,\n" +
                "                               FAD_INVOICE.PAY_STYLE,\n" +
                "                               FAD_INVOICE.PRM_PROJECT_MAIN_ID,\n" +
                "                               FAD_INVOICE.SUBJECT_CODE,\n" +
                "                               FAD_INVOICE.SUBJECT_NAME,\n" +
                "                               FAD_INVOICE.OFFICE_ID,\n" +
                "                               FAD_INVOICE.OFFICE_NAME,\n" +
                "                               FAD_INVOICE.PAYEE,\n" +
                "                               FAD_INVOICE.RENDER_PERSON,\n" +
                "                               FAD_INVOICE.SUPPLIER_ID,\n" +
                "                               FAD_INVOICE.SUPPLIER_NAME,\n" +
                "                               FAD_INVOICE.CREATE_TIME,\n" +
                "                               NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS DEBTOR,\n" +
                "                               NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS CREDITOR,\n" +
                "                               NVL(FAD_CASH_REQ_INVOICE.CLEARANCE_MONEY, 0) AS CLEARANCE_MONEY\n" +
                "                          FROM FAD_INVOICE\n" +
                "                          LEFT JOIN FAD_CASH_REQ_INVOICE ON FAD_INVOICE.UUID =\n" +
                "                                                            FAD_CASH_REQ_INVOICE.FAD_INVOICE_ID\n" +
                "                         WHERE FAD_INVOICE.UUID = '" + businessId + "') A\n" +
                "                 GROUP BY INVOICE_REQ_NO,\n" +
                "                          PAY_STYLE,\n" +
                "                          PRM_PROJECT_MAIN_ID,\n" +
                "                          SUBJECT_CODE,\n" +
                "                          SUBJECT_NAME,\n" +
                "                          OFFICE_ID,\n" +
                "                          OFFICE_NAME,\n" +
                "                          PAYEE,\n" +
                "                          RENDER_PERSON,\n" +
                "                          SUPPLIER_ID,\n" +
                "                          SUPPLIER_NAME,\n" +
                "                          CREATE_TIME,\n" +
                "                          DEBTOR,\n" +
                "                          CREDITOR) A";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        //子表凭证摘要新增
        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE,\n" +
                "         SEQ_NO)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               1,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               CASE\n" +
                "                  WHEN INVOICE_TYPE = '0' THEN\n" +
                "                   (CASE\n" +
                "                  WHEN DEBTOR >= SUM_CLEARANCE_MONEY THEN\n" +
                "                   SUM_CLEARANCE_MONEY - (SUM_CLEARANCE_MONEY / (1 + TAX_RATE) * TAX_RATE)\n" +
                "                  ELSE\n" +
                "                   DEBTOR - (DEBTOR / (1 + TAX_RATE) * TAX_RATE)\n" +
                "                END) ELSE(CASE\n" +
                "                 WHEN DEBTOR >= SUM_CLEARANCE_MONEY THEN\n" +
                "                  SUM_CLEARANCE_MONEY\n" +
                "                 ELSE\n" +
                "                  DEBTOR\n" +
                "               END) END,\n" +
                "               '140302',\n" +
                "               '原材料-原料',\n" +
                "               '0',\n" +
                "               '" + originalFormType + "', --采购->有请款预付\n" +
                "               INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME,\n" +
                "               FAD_CERTIFICATE_SEQ_NO.NEXTVAL\n" +
                "          FROM (SELECT INVOICE_TYPE,\n" +
                "                       INVOICE_REQ_NO,\n" +
                "                       PAY_STYLE,\n" +
                "                       PRM_PROJECT_MAIN_ID,\n" +
                "                       SUBJECT_CODE,\n" +
                "                       SUBJECT_NAME,\n" +
                "                       OFFICE_ID,\n" +
                "                       OFFICE_NAME,\n" +
                "                       PAYEE,\n" +
                "                       RENDER_PERSON,\n" +
                "                       SUPPLIER_ID,\n" +
                "                       SUPPLIER_NAME,\n" +
                "                       CREATE_TIME,\n" +
                "                       TAX_RATE,\n" +
                "                       DEBTOR,\n" +
                "                       CREDITOR,\n" +
                "                       SUM(CLEARANCE_MONEY) AS SUM_CLEARANCE_MONEY\n" +
                "                  FROM (SELECT FAD_INVOICE.INVOICE_TYPE,\n" +
                "                               FAD_INVOICE.INVOICE_REQ_NO,\n" +
                "                               FAD_INVOICE.PAY_STYLE,\n" +
                "                               FAD_INVOICE.PRM_PROJECT_MAIN_ID,\n" +
                "                               FAD_INVOICE.SUBJECT_CODE,\n" +
                "                               FAD_INVOICE.SUBJECT_NAME,\n" +
                "                               FAD_INVOICE.OFFICE_ID,\n" +
                "                               FAD_INVOICE.OFFICE_NAME,\n" +
                "                               FAD_INVOICE.PAYEE,\n" +
                "                               FAD_INVOICE.RENDER_PERSON,\n" +
                "                               FAD_INVOICE.SUPPLIER_ID,\n" +
                "                               FAD_INVOICE.SUPPLIER_NAME,\n" +
                "                               FAD_INVOICE.CREATE_TIME,\n" +
                "                               NVL(FAD_INVOICE.TAX_RATE, 0) AS TAX_RATE,\n" +
                "                               NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS DEBTOR,\n" +
                "                               NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS CREDITOR,\n" +
                "                               NVL(FAD_CASH_REQ_INVOICE.CLEARANCE_MONEY, 0) AS CLEARANCE_MONEY\n" +
                "                          FROM FAD_INVOICE\n" +
                "                          LEFT JOIN FAD_CASH_REQ_INVOICE ON FAD_INVOICE.UUID =\n" +
                "                                                            FAD_CASH_REQ_INVOICE.FAD_INVOICE_ID\n" +
                "                         WHERE FAD_INVOICE.UUID = '" + businessId + "') A\n" +
                "                 GROUP BY INVOICE_TYPE,\n" +
                "                          INVOICE_REQ_NO,\n" +
                "                          PAY_STYLE,\n" +
                "                          PRM_PROJECT_MAIN_ID,\n" +
                "                          SUBJECT_CODE,\n" +
                "                          SUBJECT_NAME,\n" +
                "                          OFFICE_ID,\n" +
                "                          OFFICE_NAME,\n" +
                "                          PAYEE,\n" +
                "                          RENDER_PERSON,\n" +
                "                          SUPPLIER_ID,\n" +
                "                          SUPPLIER_NAME,\n" +
                "                          CREATE_TIME,\n" +
                "                          TAX_RATE,\n" +
                "                          DEBTOR,\n" +
                "                          CREDITOR) A";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE,\n" +
                "         SEQ_NO)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               2,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               CASE\n" +
                "                 WHEN INVOICE_TYPE = '0' THEN\n" +
                "                  CASE\n" +
                "                 WHEN DEBTOR >= SUM_CLEARANCE_MONEY THEN\n" +
                "                  (SUM_CLEARANCE_MONEY / (1 + TAX_RATE) * TAX_RATE)\n" +
                "                 ELSE\n" +
                "                  (DEBTOR / (1 + TAX_RATE) * TAX_RATE)\n" +
                "               END ELSE 0 END,\n" +
                "               '2221010101',\n" +
                "               '应交税费-应交税金-应交增值税-进项税额',\n" +
                "               '0',\n" +
                "               '" + originalFormType + "', --采购->有请款预付\n" +
                "               INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME,\n" +
                "               FAD_CERTIFICATE_SEQ_NO.NEXTVAL\n" +
                "          FROM (SELECT INVOICE_TYPE,\n" +
                "                       INVOICE_REQ_NO,\n" +
                "                       PAY_STYLE,\n" +
                "                       PRM_PROJECT_MAIN_ID,\n" +
                "                       SUBJECT_CODE,\n" +
                "                       SUBJECT_NAME,\n" +
                "                       OFFICE_ID,\n" +
                "                       OFFICE_NAME,\n" +
                "                       PAYEE,\n" +
                "                       RENDER_PERSON,\n" +
                "                       SUPPLIER_ID,\n" +
                "                       SUPPLIER_NAME,\n" +
                "                       CREATE_TIME,\n" +
                "                       TAX_RATE,\n" +
                "                       DEBTOR,\n" +
                "                       CREDITOR,\n" +
                "                       SUM(CLEARANCE_MONEY) AS SUM_CLEARANCE_MONEY\n" +
                "                  FROM (SELECT FAD_INVOICE.INVOICE_TYPE,\n" +
                "                               FAD_INVOICE.INVOICE_REQ_NO,\n" +
                "                               FAD_INVOICE.PAY_STYLE,\n" +
                "                               FAD_INVOICE.PRM_PROJECT_MAIN_ID,\n" +
                "                               FAD_INVOICE.SUBJECT_CODE,\n" +
                "                               FAD_INVOICE.SUBJECT_NAME,\n" +
                "                               FAD_INVOICE.OFFICE_ID,\n" +
                "                               FAD_INVOICE.OFFICE_NAME,\n" +
                "                               FAD_INVOICE.PAYEE,\n" +
                "                               FAD_INVOICE.RENDER_PERSON,\n" +
                "                               FAD_INVOICE.SUPPLIER_ID,\n" +
                "                               FAD_INVOICE.SUPPLIER_NAME,\n" +
                "                               FAD_INVOICE.CREATE_TIME,\n" +
                "                               NVL(FAD_INVOICE.TAX_RATE, 0) AS TAX_RATE,\n" +
                "                               NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS DEBTOR,\n" +
                "                               NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS CREDITOR,\n" +
                "                               NVL(FAD_CASH_REQ_INVOICE.CLEARANCE_MONEY, 0) AS CLEARANCE_MONEY\n" +
                "                          FROM FAD_INVOICE\n" +
                "                          LEFT JOIN FAD_CASH_REQ_INVOICE ON FAD_INVOICE.UUID =\n" +
                "                                                            FAD_CASH_REQ_INVOICE.FAD_INVOICE_ID\n" +
                "                         WHERE FAD_INVOICE.UUID = '" + businessId + "') A\n" +
                "                 GROUP BY INVOICE_TYPE,\n" +
                "                          INVOICE_REQ_NO,\n" +
                "                          PAY_STYLE,\n" +
                "                          PRM_PROJECT_MAIN_ID,\n" +
                "                          SUBJECT_CODE,\n" +
                "                          SUBJECT_NAME,\n" +
                "                          OFFICE_ID,\n" +
                "                          OFFICE_NAME,\n" +
                "                          PAYEE,\n" +
                "                          RENDER_PERSON,\n" +
                "                          SUPPLIER_ID,\n" +
                "                          SUPPLIER_NAME,\n" +
                "                          CREATE_TIME,\n" +
                "                          TAX_RATE,\n" +
                "                          DEBTOR,\n" +
                "                          CREDITOR) A\n" +
                "         WHERE TAX_RATE > 0\n" +
                "           AND INVOICE_TYPE = '0'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               (SELECT NVL(MAX(ORDER_NO),0)\n" +
                "                  FROM FAD_CERTIFICATE_DETAIL\n" +
                "                 WHERE FAD_CERTIFICATE_ID =\n" +
                "                       (SELECT UUID\n" +
                "                          FROM FAD_CERTIFICATE\n" +
                "                         WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                           AND ORIGINAL_FORM_TYPE = '" + originalFormType + "')) +\n" +
                "               ROWNUM,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               NVL(FAD_CASH_REQ_INVOICE.CLEARANCE_MONEY, 0),\n" +
                "               '1123',\n" +
                "               '预付账款',\n" +
                "               '1',\n" +
                "               '" + originalFormType + "', --采购->有请款预付\n" +
                "               FAD_INVOICE.INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(FAD_INVOICE.CREATE_TIME,'YYYY-MM-DD') || ')' || '、' || FAD_CASH_REQ.RUNNING_NO || '(请款流水号)' || '(' || TO_CHAR(FAD_CASH_REQ_INVOICE.CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               FAD_INVOICE.CREATE_TIME\n" +
                "          FROM FAD_INVOICE\n" +
                //"          LEFT JOIN FAD_INVOICE_MATERIAL ON FAD_INVOICE.UUID =\n" +
                //"                                            FAD_INVOICE_MATERIAL.FAD_INVOICE_ID\n" +
                "          LEFT JOIN FAD_CASH_REQ_INVOICE ON FAD_INVOICE.UUID =\n" +
                "                                            FAD_CASH_REQ_INVOICE.FAD_INVOICE_ID\n" +
                "          LEFT JOIN FAD_CASH_REQ ON FAD_CASH_REQ_INVOICE.FAD_CASH_REQ_ID =\n" +
                "                                    FAD_CASH_REQ.UUID\n" +
                "         WHERE FAD_INVOICE.UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
    }

    @Override
    public void createCertificateFromBusinessPurchaseContractCostConfirmationInvoice(String businessId, String originalFormType) {
        String sql = "";
        DAOMeta daoMeta = null;

        //报销与采购合同发票主表凭证
        createCertificateFromBusinessInvoiceHead(businessId, originalFormType);

        //子表凭证摘要新增
        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               (SELECT NVL(MAX(ORDER_NO),0)\n" +
                "                  FROM FAD_CERTIFICATE_DETAIL\n" +
                "                 WHERE FAD_CERTIFICATE_ID =\n" +
                "                       (SELECT UUID\n" +
                "                          FROM FAD_CERTIFICATE\n" +
                "                         WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                           AND ORIGINAL_FORM_TYPE = '" + originalFormType + "')) +\n" +
                "               ROWNUM,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               NVL(SCM_CONTRACT_INVOICE.AMOUNT, 0),\n" +
                "               '5401',\n" +
                "               '工程施工',\n" +
                "               '0',\n" +
                "               '" + originalFormType + "', --采购->成本确认\n" +
                "               SCM_CONTRACT.SCM_CONTRACT_CODE || '(采购合同编号)' || '(' || TO_CHAR(SCM_CONTRACT_INVOICE.CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               SCM_CONTRACT_INVOICE.CREATE_TIME\n" +
                "          FROM FAD_INVOICE\n" +
                "          LEFT JOIN SCM_CONTRACT_INVOICE ON FAD_INVOICE.UUID =\n" +
                "                                            SCM_CONTRACT_INVOICE.FAD_INVOICE_ID\n" +
                "          LEFT JOIN SCM_CONTRACT ON SCM_CONTRACT_INVOICE.SCM_CONTRACT_ID =\n" +
                "                                    SCM_CONTRACT.UUID\n" +
                "         WHERE FAD_INVOICE.UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               (SELECT NVL(MAX(ORDER_NO),0)\n" +
                "                  FROM FAD_CERTIFICATE_DETAIL\n" +
                "                 WHERE FAD_CERTIFICATE_ID =\n" +
                "                       (SELECT UUID\n" +
                "                          FROM FAD_CERTIFICATE\n" +
                "                         WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                           AND ORIGINAL_FORM_TYPE = '" + originalFormType + "')) +\n" +
                "               ROWNUM,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               CASE\n" +
                "                 WHEN INVOICE_TYPE = '0' THEN\n" +
                "                  NVL(EXPENSES_MONEY, 0) - (NVL(EXPENSES_MONEY, 0) / (1 + NVL(TAX_RATE, 0)) * NVL(TAX_RATE, 0))\n" +
                "                 ELSE\n" +
                "                  NVL(EXPENSES_MONEY, 0)\n" +
                "               END,\n" +
                "               '140302',\n" +
                "               '原材料-原料',\n" +
                "               '1',\n" +
                "               '" + originalFormType + "', --采购->成本确认\n" +
                "               INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME\n" +
                "          FROM FAD_INVOICE\n" +
                "         WHERE UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               (SELECT NVL(MAX(ORDER_NO),0)\n" +
                "                  FROM FAD_CERTIFICATE_DETAIL\n" +
                "                 WHERE FAD_CERTIFICATE_ID =\n" +
                "                       (SELECT UUID\n" +
                "                          FROM FAD_CERTIFICATE\n" +
                "                         WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                           AND ORIGINAL_FORM_TYPE = '" + originalFormType + "')) +\n" +
                "               ROWNUM,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               CASE\n" +
                "                 WHEN INVOICE_TYPE = '0' THEN\n" +
                "                  (NVL(EXPENSES_MONEY, 0) / (1 + NVL(TAX_RATE, 0)) * NVL(TAX_RATE, 0))\n" +
                "                 ELSE\n" +
                "                  0\n" +
                "               END,\n" +
                "               '2221010104',\n" +
                "               '应交税费-应交税金-应交增值税-进项税额转出',\n" +
                "               '1',\n" +
                "               '" + originalFormType + "', --采购->成本确认\n" +
                "               INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME\n" +
                "          FROM FAD_INVOICE\n" +
                "         WHERE UUID = '" + businessId + "'\n" +
                "           AND NVL(TAX_RATE, 0) > 0\n" +
                "           AND INVOICE_TYPE = '0'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
    }

    @Override
    public void createCertificateFromBusinessPurchaseContractCostConfirmationInvoiceMaterial(String businessId, String originalFormType) {
        String sql = "";
        DAOMeta daoMeta = null;

        //报销与采购合同发票主表凭证
        createCertificateFromBusinessInvoiceHead(businessId, originalFormType);

        //子表凭证摘要新增
        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               1,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               NVL(FAD_INVOICE.EXPENSES_MONEY, 0),\n" +
                "               '5401',\n" +
                "               '工程施工',\n" +
                "               '0',\n" +
                "               '" + originalFormType + "', --采购->成本确认\n" +
                "               FAD_INVOICE.INVOICE_REQ_NO || '(采购合同编号)' || '(' || TO_CHAR(FAD_INVOICE.CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               FAD_INVOICE.CREATE_TIME\n" +
                "          FROM FAD_INVOICE\n" +
                //"          LEFT JOIN FAD_INVOICE_MATERIAL ON FAD_INVOICE.UUID =\n" +
                //"                                            FAD_INVOICE_MATERIAL.FAD_INVOICE_ID\n" +
                "         WHERE FAD_INVOICE.UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               2,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               CASE\n" +
                "                 WHEN INVOICE_TYPE = '0' THEN\n" +
                "                  NVL(EXPENSES_MONEY, 0) - (NVL(EXPENSES_MONEY, 0) / (1 + NVL(TAX_RATE, 0)) * NVL(TAX_RATE, 0))\n" +
                "                 ELSE\n" +
                "                  NVL(EXPENSES_MONEY, 0)\n" +
                "               END,\n" +
                "               '140302',\n" +
                "               '原材料-原料',\n" +
                "               '1',\n" +
                "               '" + originalFormType + "', --采购->成本确认\n" +
                "               INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME\n" +
                "          FROM FAD_INVOICE\n" +
                "         WHERE UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               3,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               CASE\n" +
                "                 WHEN INVOICE_TYPE = '0' THEN\n" +
                "                  (NVL(EXPENSES_MONEY, 0) / (1 + NVL(TAX_RATE, 0)) * NVL(TAX_RATE, 0))\n" +
                "                 ELSE\n" +
                "                  0\n" +
                "               END,\n" +
                "               '2221010104',\n" +
                "               '应交税费-应交税金-应交增值税-进项税额转出',\n" +
                "               '1',\n" +
                "               '" + originalFormType + "', --采购->成本确认\n" +
                "               INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME\n" +
                "          FROM FAD_INVOICE\n" +
                "         WHERE UUID = '" + businessId + "'\n" +
                "           AND NVL(TAX_RATE, 0) > 0\n" +
                "           AND INVOICE_TYPE = '0'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
    }

    @Override
    public void createCertificateFromBusinessPurchaseContractPaymentLessThan20000Invoice(String businessId, String originalFormType) {
        String sql = "";
        DAOMeta daoMeta = null;

        //判断采购合同支付是否直接支付
        sql = "SELECT 1 FROM SCM_CONTRACT_INVOICE WHERE FAD_INVOICE_ID = '" + businessId + "' AND DIRECT_PAYMENT = 1";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);

        //采购合同支付直接支付
        if ((PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0)) {

            //报销与采购合同发票主表凭证
            createCertificateFromBusinessInvoiceHead(businessId, originalFormType);

            //子表凭证摘要新增
            sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                    "        (UUID,\n" +
                    "         FAD_CERTIFICATE_ID,\n" +
                    "         BUSINESS_ID,\n" +
                    "         ORDER_NO,\n" +
                    "         CURRENCY,\n" +
                    "         CURRTYPENAME,\n" +
                    "         LOCAL,\n" +
                    "         SUBJECT_CODE,\n" +
                    "         SUBJECT_NAME,\n" +
                    "         DEBTOR_OR_CREDITOR,\n" +
                    "         ORIGINAL_FORM_TYPE,\n" +
                    "         ORIGINAL_FORM_CODE,\n" +
                    "         ORIGINAL_FORM_DATE,\n" +
                    "         SEQ_NO)\n" +
                    "        SELECT SYS_GUID(),\n" +
                    "               (SELECT UUID\n" +
                    "                  FROM FAD_CERTIFICATE\n" +
                    "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                    "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                    "               '" + businessId + "',\n" +
                    "               (SELECT NVL(MAX(ORDER_NO),0)\n" +
                    "                  FROM FAD_CERTIFICATE_DETAIL\n" +
                    "                 WHERE FAD_CERTIFICATE_ID =\n" +
                    "                       (SELECT UUID\n" +
                    "                          FROM FAD_CERTIFICATE\n" +
                    "                         WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                    "                           AND ORIGINAL_FORM_TYPE = '" + originalFormType + "')) +\n" +
                    "               ROW_NUMBER() OVER(ORDER BY SCM_CONTRACT_CODE DESC),\n" +
                    "               'CNY',\n" +
                    "               '人民币元',\n" +
                    "               DEBTOR - SUM_CLEARANCE_MONEY,\n" +
                    "               '2202',\n" +
                    "               '应付账款',\n" +
                    "               '0',\n" +
                    "               '" + originalFormType + "', --采购->支付(直接支付)\n" +
                    "               SCM_CONTRACT_CODE || '(采购合同编号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                    "               CREATE_TIME,\n" +
                    "               FAD_CERTIFICATE_SEQ_NO.NEXTVAL\n" +
                    "          FROM (SELECT INVOICE_TYPE,\n" +
                    "                       PAY_STYLE,\n" +
                    "                       PROJECT_ID,\n" +
                    "                       SUBJECT_CODE,\n" +
                    "                       PURCHASE_PACKAGE_ID,\n" +
                    "                       OFFICE_ID,\n" +
                    "                       OFFICE_NAME,\n" +
                    "                       PAYEE,\n" +
                    "                       RENDER_PERSON,\n" +
                    "                       SUPPLIER_ID,\n" +
                    "                       SUPPLIER_NAME,\n" +
                    "                       SCM_CONTRACT_CODE,\n" +
                    "                       CREATE_TIME,\n" +
                    "                       TAX_RATE,\n" +
                    "                       DEBTOR,\n" +
                    "                       CREDITOR,\n" +
                    "                       SUM(CLEARANCE_MONEY) AS SUM_CLEARANCE_MONEY\n" +
                    "                  FROM (SELECT FAD_INVOICE.INVOICE_TYPE,\n" +
                    "                               FAD_INVOICE.PAY_STYLE,\n" +
                    "                               SCM_CONTRACT.PROJECT_ID,\n" +
                    "                               SCM_CONTRACT.SUBJECT_CODE,\n" +
                    "                               SCM_CONTRACT.PURCHASE_PACKAGE_ID,\n" +
                    "                               FAD_INVOICE.OFFICE_ID,\n" +
                    "                               FAD_INVOICE.OFFICE_NAME,\n" +
                    "                               FAD_INVOICE.PAYEE,\n" +
                    "                               FAD_INVOICE.RENDER_PERSON,\n" +
                    "                               FAD_INVOICE.SUPPLIER_ID,\n" +
                    "                               FAD_INVOICE.SUPPLIER_NAME,\n" +
                    "                               SCM_CONTRACT.SCM_CONTRACT_CODE,\n" +
                    "                               SCM_CONTRACT_INVOICE.CREATE_TIME,\n" +
                    "                               NVL(FAD_INVOICE.TAX_RATE, 0) AS TAX_RATE,\n" +
                    "                               NVL(SCM_CONTRACT_INVOICE.AMOUNT, 0) AS DEBTOR,\n" +
                    "                               NVL(SCM_CONTRACT_INVOICE.AMOUNT, 0) AS CREDITOR,\n" +
                    "                               NVL(FAD_CASH_REQ_INVOICE.CLEARANCE_MONEY, 0) AS CLEARANCE_MONEY\n" +
                    "                          FROM FAD_INVOICE\n" +
                    "                          LEFT JOIN (SELECT * FROM SCM_CONTRACT_INVOICE WHERE DIRECT_PAYMENT = 1) SCM_CONTRACT_INVOICE ON FAD_INVOICE.UUID =\n" +
                    "                                                            SCM_CONTRACT_INVOICE.FAD_INVOICE_ID\n" +
                    "                          LEFT JOIN SCM_CONTRACT ON SCM_CONTRACT_INVOICE.SCM_CONTRACT_ID =\n" +
                    "                                                    SCM_CONTRACT.UUID\n" +
                    "                          LEFT JOIN FAD_CASH_REQ ON SCM_CONTRACT.UUID =\n" +
                    "                                                    FAD_CASH_REQ.PURCHASE_CONTRACT_ID\n" +
                    "                          LEFT JOIN FAD_CASH_REQ_INVOICE ON FAD_CASH_REQ.UUID =\n" +
                    "                                                            FAD_CASH_REQ_INVOICE.FAD_CASH_REQ_ID\n" +
                    "                                                        AND FAD_INVOICE.UUID =\n" +
                    "                                                            FAD_CASH_REQ_INVOICE.FAD_INVOICE_ID\n" +
                    "                         WHERE FAD_INVOICE.UUID = '" + businessId + "') A\n" +
                    "                 GROUP BY INVOICE_TYPE,\n" +
                    "                          PAY_STYLE,\n" +
                    "                          PROJECT_ID,\n" +
                    "                          SUBJECT_CODE,\n" +
                    "                          PURCHASE_PACKAGE_ID,\n" +
                    "                          OFFICE_ID,\n" +
                    "                          OFFICE_NAME,\n" +
                    "                          PAYEE,\n" +
                    "                          RENDER_PERSON,\n" +
                    "                          SUPPLIER_ID,\n" +
                    "                          SUPPLIER_NAME,\n" +
                    "                          SCM_CONTRACT_CODE,\n" +
                    "                          CREATE_TIME,\n" +
                    "                          TAX_RATE,\n" +
                    "                          DEBTOR,\n" +
                    "                          CREDITOR) A WHERE (DEBTOR - SUM_CLEARANCE_MONEY) <> 0";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

            sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                    "        (UUID,\n" +
                    "         FAD_CERTIFICATE_ID,\n" +
                    "         BUSINESS_ID,\n" +
                    "         ORDER_NO,\n" +
                    "         CURRENCY,\n" +
                    "         CURRTYPENAME,\n" +
                    "         LOCAL,\n" +
                    "         SUBJECT_CODE,\n" +
                    "         SUBJECT_NAME,\n" +
                    "         DEBTOR_OR_CREDITOR,\n" +
                    "         ORIGINAL_FORM_TYPE,\n" +
                    "         ORIGINAL_FORM_CODE,\n" +
                    "         ORIGINAL_FORM_DATE,\n" +
                    "         SEQ_NO)\n" +
                    "        SELECT SYS_GUID(),\n" +
                    "               (SELECT UUID\n" +
                    "                  FROM FAD_CERTIFICATE\n" +
                    "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                    "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                    "               '" + businessId + "',\n" +
                    "               (SELECT NVL(MAX(ORDER_NO),0)\n" +
                    "                  FROM FAD_CERTIFICATE_DETAIL\n" +
                    "                 WHERE FAD_CERTIFICATE_ID =\n" +
                    "                       (SELECT UUID\n" +
                    "                          FROM FAD_CERTIFICATE\n" +
                    "                         WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                    "                           AND ORIGINAL_FORM_TYPE = '" + originalFormType + "')) +\n" +
                    "               ROWNUM,\n" +
                    "               'CNY',\n" +
                    "               '人民币元',\n" +
                    "               CREDITOR - SUM_CLEARANCE_MONEY,\n" +
                    "               CASE WHEN PAY_STYLE = '0' THEN '1001' ELSE '1002' END,\n" +
                    "               CASE WHEN PAY_STYLE = '0' THEN '库存现金' ELSE '银行存款' END,\n" +
                    "               '1',\n" +
                    "               '" + originalFormType + "', --采购->支付(直接支付)\n" +
                    "               INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                    "               CREATE_TIME,\n" +
                    "               FAD_CERTIFICATE_SEQ_NO.NEXTVAL\n" +
                    "          FROM (SELECT INVOICE_TYPE,\n" +
                    "                       PAY_STYLE,\n" +
                    "                       PROJECT_ID,\n" +
                    "                       SUBJECT_CODE,\n" +
                    "                       PURCHASE_PACKAGE_ID,\n" +
                    "                       OFFICE_ID,\n" +
                    "                       OFFICE_NAME,\n" +
                    "                       PAYEE,\n" +
                    "                       RENDER_PERSON,\n" +
                    "                       SUPPLIER_ID,\n" +
                    "                       SUPPLIER_NAME,\n" +
                    "                       INVOICE_REQ_NO,\n" +
                    "                       CREATE_TIME,\n" +
                    "                       TAX_RATE,\n" +
                    "                       DEBTOR,\n" +
                    "                       CREDITOR,\n" +
                    "                       SUM(CLEARANCE_MONEY) AS SUM_CLEARANCE_MONEY\n" +
                    "                  FROM (SELECT FAD_INVOICE.INVOICE_TYPE,\n" +
                    "                               FAD_INVOICE.PAY_STYLE,\n" +
                    "                               SCM_CONTRACT.PROJECT_ID,\n" +
                    "                               SCM_CONTRACT.SUBJECT_CODE,\n" +
                    "                               SCM_CONTRACT.PURCHASE_PACKAGE_ID,\n" +
                    "                               FAD_INVOICE.OFFICE_ID,\n" +
                    "                               FAD_INVOICE.OFFICE_NAME,\n" +
                    "                               FAD_INVOICE.PAYEE,\n" +
                    "                               FAD_INVOICE.RENDER_PERSON,\n" +
                    "                               FAD_INVOICE.SUPPLIER_ID,\n" +
                    "                               FAD_INVOICE.SUPPLIER_NAME,\n" +
                    "                               FAD_INVOICE.INVOICE_REQ_NO,\n" +
                    "                               FAD_INVOICE.CREATE_TIME,\n" +
                    "                               NVL(FAD_INVOICE.TAX_RATE, 0) AS TAX_RATE,\n" +
                    "                               NVL(SCM_CONTRACT_INVOICE.AMOUNT, 0) AS DEBTOR,\n" +
                    "                               NVL(SCM_CONTRACT_INVOICE.AMOUNT, 0) AS CREDITOR,\n" +
                    "                               NVL(FAD_CASH_REQ_INVOICE.CLEARANCE_MONEY, 0) AS CLEARANCE_MONEY\n" +
                    "                          FROM FAD_INVOICE\n" +
                    "                          LEFT JOIN (SELECT * FROM SCM_CONTRACT_INVOICE WHERE DIRECT_PAYMENT = 1) SCM_CONTRACT_INVOICE ON FAD_INVOICE.UUID =\n" +
                    "                                                            SCM_CONTRACT_INVOICE.FAD_INVOICE_ID\n" +
                    "                          LEFT JOIN SCM_CONTRACT ON SCM_CONTRACT_INVOICE.SCM_CONTRACT_ID =\n" +
                    "                                                    SCM_CONTRACT.UUID\n" +
                    "                          LEFT JOIN FAD_CASH_REQ ON SCM_CONTRACT.UUID =\n" +
                    "                                                    FAD_CASH_REQ.PURCHASE_CONTRACT_ID\n" +
                    "                          LEFT JOIN FAD_CASH_REQ_INVOICE ON FAD_CASH_REQ.UUID =\n" +
                    "                                                            FAD_CASH_REQ_INVOICE.FAD_CASH_REQ_ID\n" +
                    "                                                        AND FAD_INVOICE.UUID =\n" +
                    "                                                            FAD_CASH_REQ_INVOICE.FAD_INVOICE_ID\n" +
                    "                         WHERE FAD_INVOICE.UUID = '" + businessId + "') A\n" +
                    "                 GROUP BY INVOICE_TYPE,\n" +
                    "                          PAY_STYLE,\n" +
                    "                          PROJECT_ID,\n" +
                    "                          SUBJECT_CODE,\n" +
                    "                          PURCHASE_PACKAGE_ID,\n" +
                    "                          OFFICE_ID,\n" +
                    "                          OFFICE_NAME,\n" +
                    "                          PAYEE,\n" +
                    "                          RENDER_PERSON,\n" +
                    "                          SUPPLIER_ID,\n" +
                    "                          SUPPLIER_NAME,\n" +
                    "                          INVOICE_REQ_NO,\n" +
                    "                          CREATE_TIME,\n" +
                    "                          TAX_RATE,\n" +
                    "                          DEBTOR,\n" +
                    "                          CREDITOR) A WHERE (CREDITOR - SUM_CLEARANCE_MONEY) <> 0";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
        }
    }

    @Override
    public void createCertificateFromBusinessPurchaseContractPaymentLessThan20000InvoiceMaterial(String businessId, String originalFormType) {
        String sql = "";
        DAOMeta daoMeta = null;

        //采购合同支付直接支付
        {

            //报销与采购合同发票主表凭证
            createCertificateFromBusinessInvoiceHead(businessId, originalFormType);

            //子表凭证摘要新增
            sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                    "        (UUID,\n" +
                    "         FAD_CERTIFICATE_ID,\n" +
                    "         BUSINESS_ID,\n" +
                    "         ORDER_NO,\n" +
                    "         CURRENCY,\n" +
                    "         CURRTYPENAME,\n" +
                    "         LOCAL,\n" +
                    "         SUBJECT_CODE,\n" +
                    "         SUBJECT_NAME,\n" +
                    "         DEBTOR_OR_CREDITOR,\n" +
                    "         ORIGINAL_FORM_TYPE,\n" +
                    "         ORIGINAL_FORM_CODE,\n" +
                    "         ORIGINAL_FORM_DATE,\n" +
                    "         SEQ_NO)\n" +
                    "        SELECT SYS_GUID(),\n" +
                    "               (SELECT UUID\n" +
                    "                  FROM FAD_CERTIFICATE\n" +
                    "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                    "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                    "               '" + businessId + "',\n" +
                    "               (SELECT NVL(MAX(ORDER_NO),0)\n" +
                    "                  FROM FAD_CERTIFICATE_DETAIL\n" +
                    "                 WHERE FAD_CERTIFICATE_ID =\n" +
                    "                       (SELECT UUID\n" +
                    "                          FROM FAD_CERTIFICATE\n" +
                    "                         WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                    "                           AND ORIGINAL_FORM_TYPE = '" + originalFormType + "')) +\n" +
                    "               ROWNUM,\n" +
                    "               'CNY',\n" +
                    "               '人民币元',\n" +
                    "               DEBTOR - SUM_CLEARANCE_MONEY,\n" +
                    "               '2202',\n" +
                    "               '应付账款',\n" +
                    "               '0',\n" +
                    "               '" + originalFormType + "', --采购->支付(直接支付)\n" +
                    "               INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                    "               CREATE_TIME,\n" +
                    "               FAD_CERTIFICATE_SEQ_NO.NEXTVAL\n" +
                    "          FROM (SELECT INVOICE_TYPE,\n" +
                    "                       PAY_STYLE,\n" +
                    "                       PRM_PROJECT_MAIN_ID,\n" +
                    "                       SUBJECT_CODE,\n" +
                    "                       SUBJECT_NAME,\n" +
                    "                       OFFICE_ID,\n" +
                    "                       OFFICE_NAME,\n" +
                    "                       PAYEE,\n" +
                    "                       RENDER_PERSON,\n" +
                    "                       SUPPLIER_ID,\n" +
                    "                       SUPPLIER_NAME,\n" +
                    "                       INVOICE_REQ_NO,\n" +
                    "                       CREATE_TIME,\n" +
                    "                       TAX_RATE,\n" +
                    "                       DEBTOR,\n" +
                    "                       CREDITOR,\n" +
                    "                       SUM(CLEARANCE_MONEY) AS SUM_CLEARANCE_MONEY\n" +
                    "                  FROM (SELECT FAD_INVOICE.INVOICE_TYPE,\n" +
                    "                               FAD_INVOICE.PAY_STYLE,\n" +
                    "                               FAD_INVOICE.PRM_PROJECT_MAIN_ID,\n" +
                    "                               FAD_INVOICE.SUBJECT_CODE,\n" +
                    "                               FAD_INVOICE.SUBJECT_NAME,\n" +
                    "                               FAD_INVOICE.OFFICE_ID,\n" +
                    "                               FAD_INVOICE.OFFICE_NAME,\n" +
                    "                               FAD_INVOICE.PAYEE,\n" +
                    "                               FAD_INVOICE.RENDER_PERSON,\n" +
                    "                               FAD_INVOICE.SUPPLIER_ID,\n" +
                    "                               FAD_INVOICE.SUPPLIER_NAME,\n" +
                    "                               FAD_INVOICE.INVOICE_REQ_NO,\n" +
                    "                               FAD_INVOICE.CREATE_TIME,\n" +
                    "                               NVL(FAD_INVOICE.TAX_RATE, 0) AS TAX_RATE,\n" +
                    "                               NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS DEBTOR,\n" +
                    "                               NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS CREDITOR,\n" +
                    "                               NVL(FAD_CASH_REQ_INVOICE.CLEARANCE_MONEY, 0) AS CLEARANCE_MONEY\n" +
                    "                          FROM FAD_INVOICE\n" +
                    //"                          LEFT JOIN FAD_INVOICE_MATERIAL ON FAD_INVOICE.UUID =\n" +
                    //"                                            FAD_INVOICE_MATERIAL.FAD_INVOICE_ID\n" +
                    "                          LEFT JOIN FAD_CASH_REQ_INVOICE ON FAD_INVOICE.UUID =\n" +
                    "                                            FAD_CASH_REQ_INVOICE.FAD_INVOICE_ID\n" +
                    "                          LEFT JOIN FAD_CASH_REQ ON FAD_CASH_REQ_INVOICE.FAD_CASH_REQ_ID =\n" +
                    "                                    FAD_CASH_REQ.UUID\n" +
                    "                         WHERE FAD_INVOICE.UUID = '" + businessId + "') A\n" +
                    "                 GROUP BY INVOICE_TYPE,\n" +
                    "                          PAY_STYLE,\n" +
                    "                          PRM_PROJECT_MAIN_ID,\n" +
                    "                          SUBJECT_CODE,\n" +
                    "                          SUBJECT_NAME,\n" +
                    "                          OFFICE_ID,\n" +
                    "                          OFFICE_NAME,\n" +
                    "                          PAYEE,\n" +
                    "                          RENDER_PERSON,\n" +
                    "                          SUPPLIER_ID,\n" +
                    "                          SUPPLIER_NAME,\n" +
                    "                          INVOICE_REQ_NO,\n" +
                    "                          CREATE_TIME,\n" +
                    "                          TAX_RATE,\n" +
                    "                          DEBTOR,\n" +
                    "                          CREDITOR) A WHERE (DEBTOR - SUM_CLEARANCE_MONEY) <> 0";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

            sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                    "        (UUID,\n" +
                    "         FAD_CERTIFICATE_ID,\n" +
                    "         BUSINESS_ID,\n" +
                    "         ORDER_NO,\n" +
                    "         CURRENCY,\n" +
                    "         CURRTYPENAME,\n" +
                    "         LOCAL,\n" +
                    "         SUBJECT_CODE,\n" +
                    "         SUBJECT_NAME,\n" +
                    "         DEBTOR_OR_CREDITOR,\n" +
                    "         ORIGINAL_FORM_TYPE,\n" +
                    "         ORIGINAL_FORM_CODE,\n" +
                    "         ORIGINAL_FORM_DATE,\n" +
                    "         SEQ_NO)\n" +
                    "        SELECT SYS_GUID(),\n" +
                    "               (SELECT UUID\n" +
                    "                  FROM FAD_CERTIFICATE\n" +
                    "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                    "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                    "               '" + businessId + "',\n" +
                    "               (SELECT NVL(MAX(ORDER_NO),0)\n" +
                    "                  FROM FAD_CERTIFICATE_DETAIL\n" +
                    "                 WHERE FAD_CERTIFICATE_ID =\n" +
                    "                       (SELECT UUID\n" +
                    "                          FROM FAD_CERTIFICATE\n" +
                    "                         WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                    "                           AND ORIGINAL_FORM_TYPE = '" + originalFormType + "')) +\n" +
                    "               ROWNUM,\n" +
                    "               'CNY',\n" +
                    "               '人民币元',\n" +
                    "               CREDITOR - SUM_CLEARANCE_MONEY,\n" +
                    "               CASE WHEN PAY_STYLE = '0' THEN '1001' ELSE '1002' END,\n" +
                    "               CASE WHEN PAY_STYLE = '0' THEN '库存现金' ELSE '银行存款' END,\n" +
                    "               '1',\n" +
                    "               '" + originalFormType + "', --采购->支付(直接支付)\n" +
                    "               INVOICE_REQ_NO || '(发票黏贴单号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                    "               CREATE_TIME,\n" +
                    "               FAD_CERTIFICATE_SEQ_NO.NEXTVAL\n" +
                    "          FROM (SELECT INVOICE_TYPE,\n" +
                    "                       PAY_STYLE,\n" +
                    "                       PRM_PROJECT_MAIN_ID,\n" +
                    "                       SUBJECT_CODE,\n" +
                    "                       SUBJECT_NAME,\n" +
                    "                       OFFICE_ID,\n" +
                    "                       OFFICE_NAME,\n" +
                    "                       PAYEE,\n" +
                    "                       RENDER_PERSON,\n" +
                    "                       SUPPLIER_ID,\n" +
                    "                       SUPPLIER_NAME,\n" +
                    "                       INVOICE_REQ_NO,\n" +
                    "                       CREATE_TIME,\n" +
                    "                       TAX_RATE,\n" +
                    "                       DEBTOR,\n" +
                    "                       CREDITOR,\n" +
                    "                       SUM(CLEARANCE_MONEY) AS SUM_CLEARANCE_MONEY\n" +
                    "                  FROM (SELECT FAD_INVOICE.INVOICE_TYPE,\n" +
                    "                               FAD_INVOICE.PAY_STYLE,\n" +
                    "                               FAD_INVOICE.PRM_PROJECT_MAIN_ID,\n" +
                    "                               FAD_INVOICE.SUBJECT_CODE,\n" +
                    "                               FAD_INVOICE.SUBJECT_NAME,\n" +
                    "                               FAD_INVOICE.OFFICE_ID,\n" +
                    "                               FAD_INVOICE.OFFICE_NAME,\n" +
                    "                               FAD_INVOICE.PAYEE,\n" +
                    "                               FAD_INVOICE.RENDER_PERSON,\n" +
                    "                               FAD_INVOICE.SUPPLIER_ID,\n" +
                    "                               FAD_INVOICE.SUPPLIER_NAME,\n" +
                    "                               FAD_INVOICE.INVOICE_REQ_NO,\n" +
                    "                               FAD_INVOICE.CREATE_TIME,\n" +
                    "                               NVL(FAD_INVOICE.TAX_RATE, 0) AS TAX_RATE,\n" +
                    "                               NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS DEBTOR,\n" +
                    "                               NVL(FAD_INVOICE.EXPENSES_MONEY, 0) AS CREDITOR,\n" +
                    "                               NVL(FAD_CASH_REQ_INVOICE.CLEARANCE_MONEY, 0) AS CLEARANCE_MONEY\n" +
                    "                          FROM FAD_INVOICE\n" +
                    //"                          LEFT JOIN FAD_INVOICE_MATERIAL ON FAD_INVOICE.UUID =\n" +
                    //"                                            FAD_INVOICE_MATERIAL.FAD_INVOICE_ID\n" +
                    "                          LEFT JOIN FAD_CASH_REQ_INVOICE ON FAD_INVOICE.UUID =\n" +
                    "                                            FAD_CASH_REQ_INVOICE.FAD_INVOICE_ID\n" +
                    "                          LEFT JOIN FAD_CASH_REQ ON FAD_CASH_REQ_INVOICE.FAD_CASH_REQ_ID =\n" +
                    "                                    FAD_CASH_REQ.UUID\n" +
                    "                         WHERE FAD_INVOICE.UUID = '" + businessId + "') A\n" +
                    "                 GROUP BY INVOICE_TYPE,\n" +
                    "                          PAY_STYLE,\n" +
                    "                          PRM_PROJECT_MAIN_ID,\n" +
                    "                          SUBJECT_CODE,\n" +
                    "                          SUBJECT_NAME,\n" +
                    "                          OFFICE_ID,\n" +
                    "                          OFFICE_NAME,\n" +
                    "                          PAYEE,\n" +
                    "                          RENDER_PERSON,\n" +
                    "                          SUPPLIER_ID,\n" +
                    "                          SUPPLIER_NAME,\n" +
                    "                          INVOICE_REQ_NO,\n" +
                    "                          CREATE_TIME,\n" +
                    "                          TAX_RATE,\n" +
                    "                          DEBTOR,\n" +
                    "                          CREDITOR) A WHERE (CREDITOR - SUM_CLEARANCE_MONEY) <> 0";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
        }
    }

    @Override
    public void createCertificateFromBusinessPurchaseContractPayment(String businessId, String originalFormType) {
        String sql = "";
        DAOMeta daoMeta = null;

        //主表凭证新增
        sql = "INSERT INTO FAD_CERTIFICATE\n" +
                "        (UUID,\n" +
                "         BUSINESS_ID,\n" +
                "         DEBTOR,\n" +
                "         CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE,\n" +
                "         RECEIVER_OR_PAYER_TYPE,\n" +
                "         RECEIVER_OR_PAYER_ID,\n" +
                "         RECEIVER_OR_PAYER_CODE,\n" +
                "         RECEIVER_OR_PAYER_NAME,\n" +
                "         REMARK)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               '" + businessId + "',\n" +
                "               NVL(ACCOUNTS_PAYABLE, 0),\n" +
                "               NVL(ACCOUNTS_PAYABLE, 0),\n" +
                "               '" + originalFormType + "',\n" +
                "               RUNNING_NO || '(采购合同支付流水号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME,\n" +
                "               'SCM_SUPPLIER',\n" +
                "               SUPPLIER_ID,\n" +
                "               (SELECT SCM_SUPPLIER.SUPPLIER_CODE FROM SCM_SUPPLIER WHERE SCM_SUPPLIER.UUID = FAD_PAY_REQ_C.SUPPLIER_ID),\n" +
                "               (SELECT SCM_SUPPLIER.COMPLETE_NAME FROM SCM_SUPPLIER WHERE SCM_SUPPLIER.UUID = FAD_PAY_REQ_C.SUPPLIER_ID),\n" +
                "               REMARK\n" +
                "          FROM FAD_PAY_REQ_C\n" +
                "         WHERE UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        //子表凭证摘要新增
        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               1,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               NVL(FAD_PAY_REQ_C.ACCOUNTS_PAYABLE, 0),\n" +
                "               '2202',\n" +
                "               '应付账款',\n" +
                "               '0',\n" +
                "               '" + originalFormType + "', --采购->支付\n" +
                "               SCM_CONTRACT.SCM_CONTRACT_CODE || '(采购合同编号)' || '(' || TO_CHAR(SCM_CONTRACT.CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               SCM_CONTRACT.CREATE_TIME\n" +
                "          FROM FAD_PAY_REQ_C\n" +
                "          LEFT JOIN SCM_CONTRACT ON FAD_PAY_REQ_C.SCM_CONTRACT_ID =\n" +
                "                                    SCM_CONTRACT.UUID\n" +
                "         WHERE FAD_PAY_REQ_C.UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               2,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               NVL(ACCOUNTS_PAYABLE, 0),\n" +
                "               '1002',\n" +
                "               '银行存款',\n" +
                "               '1',\n" +
                "               '" + originalFormType + "', --采购->支付\n" +
                "               RUNNING_NO || '(采购合同支付流水号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME\n" +
                "          FROM FAD_PAY_REQ_C\n" +
                "         WHERE UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
    }

    @Override
    public void createCertificateFromBusinessPayBackTheMoney(String businessId, String originalFormType) {
        String sql = "";
        DAOMeta daoMeta = null;

        //主表凭证新增
        sql = "INSERT INTO FAD_CERTIFICATE\n" +
                "        (UUID,\n" +
                "         BUSINESS_ID,\n" +
                "         DEBTOR,\n" +
                "         CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE,\n" +
                "         RECEIVER_OR_PAYER_TYPE,\n" +
                "         RECEIVER_OR_PAYER_ID,\n" +
                "         RECEIVER_OR_PAYER_CODE,\n" +
                "         RECEIVER_OR_PAYER_NAME,\n" +
                "         SEQ_NO)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               '" + businessId + "',\n" +
                "               SUM_CLEARANCE_MONEY,\n" +
                "               SUM_CLEARANCE_MONEY,\n" +
                "               '" + originalFormType + "',\n" +
                "               RUNNING_NO || '(现金核销流水号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME,\n" +
                "               'SCDP_USER',\n" +
                "               (SELECT SCDP_USER.UUID FROM SCDP_USER WHERE SCDP_USER.USER_ID = A.CLEARANCE_PERSON),\n" +
                "               CLEARANCE_PERSON,\n" +
                "               (SELECT SCDP_USER.USER_NAME FROM SCDP_USER WHERE SCDP_USER.USER_ID = A.CLEARANCE_PERSON),\n" +
                "               FAD_CERTIFICATE_SEQ_NO.NEXTVAL\n" +
                "          FROM (SELECT FAD_CASH_CLEARANCE.RUNNING_NO,\n" +
                "                       FAD_CASH_CLEARANCE.CREATE_TIME,\n" +
                "                       FAD_CASH_CLEARANCE.CLEARANCE_PERSON,\n" +
                "                       SUM(NVL(FAD_CASH_REQ_INVOICE.CLEARANCE_MONEY, 0)) SUM_CLEARANCE_MONEY\n" +
                "                  FROM FAD_CASH_CLEARANCE\n" +
                "                  LEFT JOIN FAD_CASH_REQ_INVOICE ON FAD_CASH_CLEARANCE.UUID =\n" +
                "                                                    FAD_CASH_REQ_INVOICE.FAD_INVOICE_ID\n" +
                "                 WHERE FAD_CASH_CLEARANCE.UUID = '" + businessId + "'\n" +
                "                 GROUP BY FAD_CASH_CLEARANCE.UUID,\n" +
                "                          FAD_CASH_CLEARANCE.RUNNING_NO,\n" +
                "                          FAD_CASH_CLEARANCE.CREATE_TIME,\n" +
                "                          FAD_CASH_CLEARANCE.CLEARANCE_PERSON\n" +
                "                \n" +
                "                ) A";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        //子表凭证摘要新增
        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE,\n" +
                "         SEQ_NO)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               1,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               SUM_CLEARANCE_MONEY,\n" +
                "               '1001',\n" +
                "               '库存现金',\n" +
                "               '0',\n" +
                "               '" + originalFormType + "', --还款\n" +
                "               RUNNING_NO || '(现金核销流水号)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME,\n" +
                "               FAD_CERTIFICATE_SEQ_NO.NEXTVAL\n" +
                "          FROM (SELECT FAD_CASH_CLEARANCE.RUNNING_NO,\n" +
                "                       FAD_CASH_CLEARANCE.CREATE_TIME,\n" +
                "                       SUM(NVL(FAD_CASH_REQ_INVOICE.CLEARANCE_MONEY, 0)) SUM_CLEARANCE_MONEY\n" +
                "                  FROM FAD_CASH_CLEARANCE\n" +
                "                  LEFT JOIN FAD_CASH_REQ_INVOICE ON FAD_CASH_CLEARANCE.UUID =\n" +
                "                                                    FAD_CASH_REQ_INVOICE.FAD_INVOICE_ID\n" +
                "                 WHERE FAD_CASH_CLEARANCE.UUID = '" + businessId + "'\n" +
                "                 GROUP BY FAD_CASH_CLEARANCE.UUID,\n" +
                "                          FAD_CASH_CLEARANCE.RUNNING_NO,\n" +
                "                          FAD_CASH_CLEARANCE.CREATE_TIME\n" +
                "                \n" +
                "                ) A";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               ROWNUM + 1,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               NVL(FAD_CASH_REQ_INVOICE.CLEARANCE_MONEY, 0),\n" +
                "               CASE\n" +
                "                 WHEN (REQ_TYPE = '1' OR REQ_TYPE = '2') THEN\n" +
                "                  '122102'\n" +
                "                 WHEN REQ_TYPE = '0' THEN\n" +
                "                  '1123'\n" +
                "                 WHEN REQ_TYPE = '3' THEN\n" +
                "                  '122106'\n" +
                "                 WHEN REQ_TYPE = '4' THEN\n" +
                "                  '122166'\n" +
                "                 ELSE\n" +
                "                  ''\n" +
                "               END,\n" +
                "               CASE\n" +
                "                 WHEN (REQ_TYPE = '1' OR REQ_TYPE = '2') THEN\n" +
                "                  '其他应收款-职工借支'\n" +
                "                 WHEN REQ_TYPE = '0' THEN\n" +
                "                  '预付账款'\n" +
                "                 WHEN REQ_TYPE = '3' THEN\n" +
                "                  '其他应收款-保证金及押金'\n" +
                "                 WHEN REQ_TYPE = '4' THEN\n" +
                "                  '其他应收款-周转金'\n" +
                "                 ELSE\n" +
                "                  ''\n" +
                "               END,\n" +
                "               '1',\n" +
                "               '" + originalFormType + "', --还款\n" +
                "               CASE WHEN REQ_TYPE = '0' THEN\n" +
                "                    SCM_CONTRACT.SCM_CONTRACT_CODE || '(采购合同编号)' || '(' || TO_CHAR(SCM_CONTRACT.CREATE_TIME,'YYYY-MM-DD') || ')' || '、' || FAD_CASH_REQ.RUNNING_NO || '(请款流水号)' || '(' || TO_CHAR(FAD_CASH_REQ_INVOICE.CREATE_TIME,'YYYY-MM-DD') || ')'\n" +
                "               ELSE\n" +
                "                    FAD_CASH_REQ.RUNNING_NO || '(请款流水号)' || '(' || TO_CHAR(FAD_CASH_REQ_INVOICE.CREATE_TIME,'YYYY-MM-DD') || ')'\n" +
                "               END,\n" +
                "               CASE WHEN REQ_TYPE = '0' THEN\n" +
                "                    SCM_CONTRACT.CREATE_TIME\n" +
                "               ELSE\n" +
                "                    FAD_CASH_REQ_INVOICE.CREATE_TIME\n" +
                "               END\n" +
                "          FROM FAD_CASH_CLEARANCE\n" +
                "          LEFT JOIN FAD_CASH_REQ_INVOICE ON FAD_CASH_CLEARANCE.UUID =\n" +
                "                                            FAD_CASH_REQ_INVOICE.FAD_INVOICE_ID\n" +
                "          LEFT JOIN FAD_CASH_REQ ON FAD_CASH_REQ_INVOICE.FAD_CASH_REQ_ID =\n" +
                "                                    FAD_CASH_REQ.UUID\n" +
                "          LEFT JOIN SCM_CONTRACT ON FAD_CASH_REQ.PURCHASE_CONTRACT_ID =\n" +
                "                                    SCM_CONTRACT.UUID\n" +
                "         WHERE FAD_CASH_CLEARANCE.UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
    }

    @Override
    public void createCertificateFromBusinessReceivables(String businessId, String originalFormType) {
        String sql = "";
        DAOMeta daoMeta = null;

        //主表凭证新增
        sql = "INSERT INTO FAD_CERTIFICATE\n" +
                "        (UUID,\n" +
                "         BUSINESS_ID,\n" +
                "         DEBTOR,\n" +
                "         CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE,\n" +
                "         RECEIVER_OR_PAYER_TYPE,\n" +
                "         RECEIVER_OR_PAYER_ID,\n" +
                "         RECEIVER_OR_PAYER_CODE,\n" +
                "         RECEIVER_OR_PAYER_NAME)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               '" + businessId + "',\n" +
                "               NVL(ACTUAL_MONEY, 0),\n" +
                "               NVL(ACTUAL_MONEY, 0),\n" +
                "               '" + originalFormType + "',\n" +
                "               UUID || '(已确认收款)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME,\n" +
                "               'PRM_CUSTOMER',\n" +
                "               PAYER,\n" +
                "               (SELECT PRM_CUSTOMER.CUSTOMER_CODE FROM PRM_CUSTOMER WHERE PRM_CUSTOMER.UUID = PRM_RECEIPTS.PAYER),\n" +
                "               (SELECT PRM_CUSTOMER.CUSTOMER_NAME FROM PRM_CUSTOMER WHERE PRM_CUSTOMER.UUID = PRM_RECEIPTS.PAYER)\n" +
                "          FROM PRM_RECEIPTS\n" +
                "         WHERE UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        //子表凭证摘要新增
        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               1,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               NVL(ACTUAL_MONEY, 0),\n" +
                "               '1002',\n" +
                "               '银行存款',\n" +
                "               '0',\n" +
                "               '" + originalFormType + "', --收款\n" +
                "               UUID || '(已确认收款)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME\n" +
                "          FROM PRM_RECEIPTS\n" +
                "         WHERE UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               2,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               NVL(ACTUAL_MONEY, 0),\n" +
                "               '1122',\n" +
                "               '应收账款',\n" +
                "               '1',\n" +
                "               '" + originalFormType + "', --收款\n" +
                "               UUID || '(已确认收款)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME\n" +
                "          FROM PRM_RECEIPTS\n" +
                "         WHERE UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
    }

    @Override
    public void createCertificateFromBusinessReceivablesPendingConfirmation(String businessId, String originalFormType) {
        String sql = "";
        DAOMeta daoMeta = null;

        //主表凭证新增
        sql = "INSERT INTO FAD_CERTIFICATE\n" +
                "        (UUID,\n" +
                "         BUSINESS_ID,\n" +
                "         DEBTOR,\n" +
                "         CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE,\n" +
                "         RECEIVER_OR_PAYER_TYPE,\n" +
                "         RECEIVER_OR_PAYER_ID,\n" +
                "         RECEIVER_OR_PAYER_CODE,\n" +
                "         RECEIVER_OR_PAYER_NAME)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               '" + businessId + "',\n" +
                "               NVL(MONEY, 0),\n" +
                "               NVL(MONEY, 0),\n" +
                "               '" + originalFormType + "',\n" +
                "               RECEIPT_NO || '(待确认收款)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME,\n" +
                "               'PRM_CUSTOMER',\n" +
                "               PAYER,\n" +
                "               (SELECT PRM_CUSTOMER.CUSTOMER_CODE FROM PRM_CUSTOMER WHERE PRM_CUSTOMER.UUID = PRM_UNKNOWN_RECEIPTS.PAYER),\n" +
                "               (SELECT PRM_CUSTOMER.CUSTOMER_NAME FROM PRM_CUSTOMER WHERE PRM_CUSTOMER.UUID = PRM_UNKNOWN_RECEIPTS.PAYER)\n" +
                "          FROM PRM_UNKNOWN_RECEIPTS\n" +
                "         WHERE UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        //子表凭证摘要新增
        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               1,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               NVL(MONEY, 0),\n" +
                "               '1002',\n" +
                "               '银行存款',\n" +
                "               '0',\n" +
                "               '" + originalFormType + "', --收款\n" +
                "               RECEIPT_NO || '(待确认收款)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME\n" +
                "          FROM PRM_UNKNOWN_RECEIPTS\n" +
                "         WHERE UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               2,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               NVL(MONEY, 0),\n" +
                "               '1122',\n" +
                "               '应收账款',\n" +
                "               '1',\n" +
                "               '" + originalFormType + "', --收款\n" +
                "               RECEIPT_NO || '(待确认收款)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME\n" +
                "          FROM PRM_UNKNOWN_RECEIPTS\n" +
                "         WHERE UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
    }

    @Override
    public void createCertificateFromBusinessReceivablesAdjustment(String businessId, String originalFormType) {
        String sql = "";
        DAOMeta daoMeta = null;

        //主表凭证新增
        sql = "INSERT INTO FAD_CERTIFICATE\n" +
                "        (UUID,\n" +
                "         BUSINESS_ID,\n" +
                "         DEBTOR,\n" +
                "         CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE,\n" +
                "         RECEIVER_OR_PAYER_TYPE,\n" +
                "         RECEIVER_OR_PAYER_ID,\n" +
                "         RECEIVER_OR_PAYER_CODE,\n" +
                "         RECEIVER_OR_PAYER_NAME)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               '" + businessId + "',\n" +
                "               1,\n" +
                "               1,\n" +
                "               '" + originalFormType + "',\n" +
                "               PRM_UNKNOWN_RECEIPTS.RECEIPT_NO || '(待确认收款)' || '(' || TO_CHAR(PRM_RECEIPTS.CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               PRM_RECEIPTS.CREATE_TIME,\n" +
                "               'PRM_CUSTOMER',\n" +
                "               PRM_RECEIPTS.PAYER,\n" +
                "               (SELECT PRM_CUSTOMER.CUSTOMER_CODE FROM PRM_CUSTOMER WHERE PRM_CUSTOMER.UUID = PRM_RECEIPTS.PAYER),\n" +
                "               (SELECT PRM_CUSTOMER.CUSTOMER_NAME FROM PRM_CUSTOMER WHERE PRM_CUSTOMER.UUID = PRM_RECEIPTS.PAYER)\n" +
                "          FROM PRM_RECEIPTS\n" +
                "          LEFT JOIN PRM_UNKNOWN_RECEIPTS ON PRM_RECEIPTS.PRM_UNKNOWN_RECEIPTS_ID = PRM_UNKNOWN_RECEIPTS.UUID\n" +
                "         WHERE PRM_RECEIPTS.UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        //子表凭证摘要新增
        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               1,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               1,\n" +
                "               '224198',\n" +
                "               '其他应付款-其他',\n" +
                "               '0',\n" +
                "               '" + originalFormType + "', --收款\n" +
                "               PRM_UNKNOWN_RECEIPTS.RECEIPT_NO || '(待确认收款)' || '(' || TO_CHAR(PRM_RECEIPTS.CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               PRM_RECEIPTS.CREATE_TIME\n" +
                "          FROM PRM_RECEIPTS\n" +
                "          LEFT JOIN PRM_UNKNOWN_RECEIPTS ON PRM_RECEIPTS.PRM_UNKNOWN_RECEIPTS_ID = PRM_UNKNOWN_RECEIPTS.UUID\n" +
                "         WHERE PRM_RECEIPTS.UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               2,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               1,\n" +
                "               '224198',\n" +
                "               '其他应付款-其他',\n" +
                "               '1',\n" +
                "               '" + originalFormType + "', --收款\n" +
                "               PRM_UNKNOWN_RECEIPTS.RECEIPT_NO || '(待确认收款)' || '(' || TO_CHAR(PRM_RECEIPTS.CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               PRM_RECEIPTS.CREATE_TIME\n" +
                "          FROM PRM_RECEIPTS\n" +
                "          LEFT JOIN PRM_UNKNOWN_RECEIPTS ON PRM_RECEIPTS.PRM_UNKNOWN_RECEIPTS_ID = PRM_UNKNOWN_RECEIPTS.UUID\n" +
                "         WHERE PRM_RECEIPTS.UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               3,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               NVL(PRM_RECEIPTS.ACTUAL_MONEY, 0) * -1,\n" +
                "               '1122',\n" +
                "               '应收账款',\n" +
                "               '1',\n" +
                "               '" + originalFormType + "', --收款\n" +
                "               PRM_UNKNOWN_RECEIPTS.RECEIPT_NO || '(待确认收款)' || '(' || TO_CHAR(PRM_RECEIPTS.CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               PRM_RECEIPTS.CREATE_TIME\n" +
                "          FROM PRM_RECEIPTS\n" +
                "          LEFT JOIN PRM_UNKNOWN_RECEIPTS ON PRM_RECEIPTS.PRM_UNKNOWN_RECEIPTS_ID = PRM_UNKNOWN_RECEIPTS.UUID\n" +
                "         WHERE PRM_RECEIPTS.UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               4,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               NVL(PRM_RECEIPTS.ACTUAL_MONEY, 0),\n" +
                "               '1122',\n" +
                "               '应收账款',\n" +
                "               '1',\n" +
                "               '" + originalFormType + "', --收款\n" +
                "               PRM_UNKNOWN_RECEIPTS.RECEIPT_NO || '(待确认收款)' || '(' || TO_CHAR(PRM_RECEIPTS.CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               PRM_RECEIPTS.CREATE_TIME\n" +
                "          FROM PRM_RECEIPTS\n" +
                "          LEFT JOIN PRM_UNKNOWN_RECEIPTS ON PRM_RECEIPTS.PRM_UNKNOWN_RECEIPTS_ID = PRM_UNKNOWN_RECEIPTS.UUID\n" +
                "         WHERE PRM_RECEIPTS.UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
    }

    @Override
    public void createCertificateFromBusinessBill(String businessId, String originalFormType) {
        String sql = "";
        DAOMeta daoMeta = null;

        //主表凭证新增
        sql = "INSERT INTO FAD_CERTIFICATE\n" +
                "        (UUID,\n" +
                "         BUSINESS_ID,\n" +
                "         DEBTOR,\n" +
                "         CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE,\n" +
                "         RECEIVER_OR_PAYER_TYPE,\n" +
                "         RECEIVER_OR_PAYER_ID,\n" +
                "         RECEIVER_OR_PAYER_CODE,\n" +
                "         RECEIVER_OR_PAYER_NAME)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               '" + businessId + "',\n" +
                "               NVL(NET_MONEY, 0),\n" +
                "               NVL(NET_MONEY, 0),\n" +
                "               '" + originalFormType + "',\n" +
                "               UUID || '(开票)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME,\n" +
                "               'PRM_CUSTOMER',\n" +
                "               CUSTOMER_ID,\n" +
                "               (SELECT PRM_CUSTOMER.CUSTOMER_CODE FROM PRM_CUSTOMER WHERE PRM_CUSTOMER.UUID = PRM_BILLING.CUSTOMER_ID),\n" +
                "               (SELECT PRM_CUSTOMER.CUSTOMER_NAME FROM PRM_CUSTOMER WHERE PRM_CUSTOMER.UUID = PRM_BILLING.CUSTOMER_ID)\n" +
                "          FROM PRM_BILLING\n" +
                "         WHERE UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        //子表凭证摘要新增
        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               1,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               CASE WHEN INVOICE_TYPE = '0' THEN\n" +
                "                   NVL(NET_MONEY, 0) - (NVL(NET_MONEY, 0) / (1 + NVL(TAX_RATE, 0)) * NVL(TAX_RATE, 0))\n" +
                "               ELSE\n" +
                "                   NVL(NET_MONEY, 0)\n" +
                "               END,\n" +
                "               '1122',\n" +
                "               '应收账款',\n" +
                "               '0',\n" +
                "               '" + originalFormType + "', --开票\n" +
                "               UUID || '(开票)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME\n" +
                "          FROM PRM_BILLING\n" +
                "         WHERE UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               1,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               CASE WHEN INVOICE_TYPE = '0' THEN\n" +
                "                   (NVL(NET_MONEY, 0) / (1 + NVL(TAX_RATE, 0)) * NVL(TAX_RATE, 0))\n" +
                "               ELSE\n" +
                "                   0\n" +
                "               END,\n" +
                "               '2221010102',\n" +
                "               '应交税费-应交税金-应交增值税-销项税额',\n" +
                "               '0',\n" +
                "               '" + originalFormType + "', --开票\n" +
                "               UUID || '(开票)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME\n" +
                "          FROM PRM_BILLING\n" +
                "         WHERE UUID = '" + businessId + "'\n" +
                "         AND NVL(TAX_RATE, 0) > 0\n" +
                "         AND INVOICE_TYPE = '0'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "INSERT INTO FAD_CERTIFICATE_DETAIL\n" +
                "        (UUID,\n" +
                "         FAD_CERTIFICATE_ID,\n" +
                "         BUSINESS_ID,\n" +
                "         ORDER_NO,\n" +
                "         CURRENCY,\n" +
                "         CURRTYPENAME,\n" +
                "         LOCAL,\n" +
                "         SUBJECT_CODE,\n" +
                "         SUBJECT_NAME,\n" +
                "         DEBTOR_OR_CREDITOR,\n" +
                "         ORIGINAL_FORM_TYPE,\n" +
                "         ORIGINAL_FORM_CODE,\n" +
                "         ORIGINAL_FORM_DATE)\n" +
                "        SELECT SYS_GUID(),\n" +
                "               (SELECT UUID\n" +
                "                  FROM FAD_CERTIFICATE\n" +
                "                 WHERE BUSINESS_ID = '" + businessId + "' AND STATE <> '2'\n" +
                "                   AND ORIGINAL_FORM_TYPE = '" + originalFormType + "'),\n" +
                "               '" + businessId + "',\n" +
                "               2,\n" +
                "               'CNY',\n" +
                "               '人民币元',\n" +
                "               NVL(NET_MONEY, 0),\n" +
                "               '6001',\n" +
                "               '主营业务收入',\n" +
                "               '1',\n" +
                "               '" + originalFormType + "', --开票\n" +
                "               UUID || '(开票)' || '(' || TO_CHAR(CREATE_TIME,'YYYY-MM-DD') || ')',\n" +
                "               CREATE_TIME\n" +
                "          FROM PRM_BILLING\n" +
                "         WHERE UUID = '" + businessId + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
    }

    @Override
    public String certificateMerge(List fadCertificateGridSelectionUuidList) {
        String returnValue = "OK";

        //Err
        String errMessages = "";

        //主表凭证取得
        List fadCertificateLst1s = new ArrayList<>();
        List fadCertificateDetailLst2s = new ArrayList<>();
        List fadCertificateAccountLst3s = new ArrayList<>();
        FadCertificate fadCertificateInsert = BeanFactory.getObject(FadCertificate.class);
        List fadCertificateDetailLst2 = new ArrayList<>();
        List<FadCertifiMergeSplitRel> fadCertifiMergeSplitRelLst4 = new ArrayList<>();
        String fadCertifiMergeSplitRelBatId = isNullReturnEmpty(UUIDUtil.getUUID());
        String fadCertificateInsertUuid = "";
        int fadCertificateDetailSeqNo = 1;
        BigDecimal debtor = new BigDecimal("0");
        BigDecimal creditor = new BigDecimal("0");
        String originalFormCodes = "";
        String abstractss = "";
        String receiverOrPayerNames = "";
        for (int i = 0; i < fadCertificateGridSelectionUuidList.size(); i++) {

            //主表凭证取得
            String fadCertificateUuid = isNullReturnEmpty(fadCertificateGridSelectionUuidList.get(i));
            FadCertificateDto fadCertificate = isNullReturnEmpty(fadCertificateUuid).length() > 0 ? (FadCertificateDto) DtoHelper.findDtoByPK(FadCertificateDto.class, fadCertificateUuid) : null;
            if (fadCertificate != null) {

                //凭证合并拆分关系取得
                Map<String, Object> fadCertifiMergeRelConditionsMap = new HashMap<String, Object>();
                fadCertifiMergeRelConditionsMap.put(FadCertifiMergeSplitRelAttribute.MERGE_ID, isNullReturnEmpty(fadCertificate.getUuid()));
                List<FadCertifiMergeSplitRel> fadCertifiMergeRelList = isNullReturnEmpty(fadCertificate.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiMergeSplitRel.class, fadCertifiMergeRelConditionsMap, "seq_no asc") : new ArrayList<>();
                if (fadCertifiMergeRelList.size() > 0) {
                    throw new BizException("合并凭证失败!(【合并凭证】【" + isNullReturnEmpty(fadCertificate.getOriginalFormCode()) + "】不能再次参与合并)");
                }

                Map<String, Object> fadCertifiSplitRelConditionsMap = new HashMap<String, Object>();
                fadCertifiSplitRelConditionsMap.put(FadCertifiMergeSplitRelAttribute.SPLIT_ID, isNullReturnEmpty(fadCertificate.getUuid()));
                List<FadCertifiMergeSplitRel> fadCertifiSplitRelList = isNullReturnEmpty(fadCertificate.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiMergeSplitRel.class, fadCertifiSplitRelConditionsMap, "seq_no asc") : new ArrayList<>();
                if (fadCertifiSplitRelList.size() > 0) {
                    throw new BizException("合并凭证失败!(【拆分凭证】【" + isNullReturnEmpty(fadCertificate.getOriginalFormCode()) + "】不能参与合并)");
                }

                String sql = "SELECT 1 FROM FAD_CERTIFICATE WHERE FREE2 = '" + isNullReturnEmpty(fadCertificate.getUuid()) + "' AND STATE IN ('0', '1')";
                DAOMeta daoMeta = new DAOMeta();
                daoMeta.setStrSql(sql);
                daoMeta.setNeedFilter(false);
                if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                    throw new BizException("合并凭证失败!(【已复制凭证】【" + isNullReturnEmpty(fadCertificate.getOriginalFormCode()) + "】不能参与合并)");
                }

                sql = "SELECT 1 FROM FAD_CERTIFICATE WHERE UUID = '" + isNullReturnEmpty(fadCertificate.getUuid()) + "' AND FREE2 IS NOT NULL";
                daoMeta = new DAOMeta();
                daoMeta.setStrSql(sql);
                daoMeta.setNeedFilter(false);
                if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                    throw new BizException("合并凭证失败!(【复制凭证】【" + isNullReturnEmpty(fadCertificate.getOriginalFormCode()) + "】不能参与合并)");
                }

                //凭证冲红关系取得
                Map<String, Object> fadCertifiCertifiRelConditionsMap = new HashMap<String, Object>();
                fadCertifiCertifiRelConditionsMap.put(FadCertifiDeficitRelAttribute.CERTIFI_ID, isNullReturnEmpty(fadCertificate.getUuid()));
                List<FadCertifiDeficitRel> fadCertifiCertifiRelList = isNullReturnEmpty(fadCertificate.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiDeficitRel.class, fadCertifiCertifiRelConditionsMap, "seq_no asc") : new ArrayList<>();
                if (fadCertifiCertifiRelList.size() > 0) {
                    throw new BizException("合并凭证失败!(【已红冲凭证】【" + isNullReturnEmpty(fadCertificate.getOriginalFormCode()) + "】不能参与合并)");
                }

                //凭证红冲关系取得
                Map<String, Object> fadCertifiDeficitRelConditionsMap = new HashMap<String, Object>();
                fadCertifiDeficitRelConditionsMap.put(FadCertifiDeficitRelAttribute.DEFICIT_ID, isNullReturnEmpty(fadCertificate.getUuid()));
                List<FadCertifiDeficitRel> fadCertifiDeficitRelList = isNullReturnEmpty(fadCertificate.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiDeficitRel.class, fadCertifiDeficitRelConditionsMap, "seq_no asc") : new ArrayList<>();
                if (fadCertifiDeficitRelList.size() > 0) {
                    throw new BizException("合并凭证失败!(【红冲凭证】【" + isNullReturnEmpty(fadCertificate.getOriginalFormCode()) + "】不能参与合并)");
                }

                //验证合并拆分凭证
                certificateCheckMoney(fadCertificate);

                certificateSetVoid(fadCertificate, fadCertificateLst1s, fadCertificateDetailLst2s, fadCertificateAccountLst3s, 1);

                debtor = debtor.add(new BigDecimal(isEmptyReturnZero(isNullReturnEmpty(fadCertificate.getDebtor()))));
                creditor = creditor.add(new BigDecimal(isEmptyReturnZero(isNullReturnEmpty(fadCertificate.getCreditor()))));

                String originalFormCode = fadCertificate.getOriginalFormCode();
                if (isNullReturnEmpty(originalFormCode).length() > 0) {
                    if (isNullReturnEmpty(originalFormCodes).length() > 0) {
                        originalFormCodes = originalFormCodes + MergeSplitSymbol + isNullReturnEmpty(originalFormCode);
                    } else {
                        originalFormCodes = isNullReturnEmpty(originalFormCode);
                    }
                }

                String abstracts = fadCertificate.getAbstracts();
                if (isNullReturnEmpty(abstracts).length() > 0) {
                    if (isNullReturnEmpty(abstractss).length() > 0) {
                        abstractss = abstractss + MergeSplitSymbol + isNullReturnEmpty(abstracts);
                    } else {
                        abstractss = isNullReturnEmpty(abstracts);
                    }
                }

                String receiverOrPayerName = fadCertificate.getReceiverOrPayerName();
                if (isNullReturnEmpty(receiverOrPayerName).length() > 0) {
                    if (isNullReturnEmpty(receiverOrPayerNames).length() > 0) {
                        receiverOrPayerNames = receiverOrPayerNames + MergeSplitSymbol + isNullReturnEmpty(receiverOrPayerName);
                    } else {
                        receiverOrPayerNames = isNullReturnEmpty(receiverOrPayerName);
                    }
                }

                if (i == 0) {
                    BeanUtil.bean2Bean(fadCertificate, fadCertificateInsert);
                    fadCertificateInsert.setIsVoid(null);
                    fadCertificateInsert.setUuid(null);
                    fadCertificateInsert.setState("0");
                    fadCertificateInsert.setCertificateNo(null);
                    fadCertificateInsert.setFeedback(null);
                    fadCertificateInsert.setNcrequestUrl(null);
                    fadCertificateInsert.setNcrequestXml(null);
                    fadCertificateInsert.setNcresponseXml(null);
                    fadCertificateInsert.setMaker(null);
                    fadCertificateInsert.setMakeDate(null);
                    PersistenceFactory.getInstance().insert(fadCertificateInsert);
                    fadCertificateInsertUuid = isNullReturnEmpty(fadCertificateInsert.getUuid());
                }

                FadCertifiMergeSplitRel fadCertifiMergeSplitRel = BeanFactory.getObject(FadCertifiMergeSplitRel.class);
                fadCertifiMergeSplitRel.setMergeId(fadCertificateInsertUuid);
                fadCertifiMergeSplitRel.setSplitId(fadCertificateUuid);
                fadCertifiMergeSplitRel.setBatId(fadCertifiMergeSplitRelBatId);
                fadCertifiMergeSplitRelLst4.add(fadCertifiMergeSplitRel);

                //子表凭证摘要取得
                List<FadCertificateDetailDto> fadCertificateDetailList = fadCertificate.getFadCertificateDetailDto() != null ? fadCertificate.getFadCertificateDetailDto() : new ArrayList<>();
                for (int y = 0; y < fadCertificateDetailList.size(); y++) {
                    FadCertificateDetailDto fadCertificateDetail = fadCertificateDetailList.get(y);
                    String fadCertificateDetailUuid = fadCertificateDetail.getUuid();
                    FadCertificateDetailDto fadCertificateDetailInsert = BeanFactory.getObject(FadCertificateDetailDto.class);
                    BeanUtil.bean2Bean(fadCertificateDetail, fadCertificateDetailInsert);
                    fadCertificateDetailInsert.setIsVoid(null);
                    fadCertificateDetailInsert.setUuid(null);
                    fadCertificateDetailInsert.setFadCertificateId(fadCertificateInsertUuid);
                    fadCertificateDetailInsert.setOrderNo(fadCertificateDetailSeqNo);
                    fadCertificateDetailInsert.setSeqNo(fadCertificateDetailSeqNo);
                    fadCertificateDetailLst2.add(fadCertificateDetailInsert);
                    String fadCertificateDetailInsertUuid = isNullReturnEmpty(fadCertificateDetailInsert.getUuid());
                    fadCertificateDetailSeqNo = fadCertificateDetailSeqNo + 1;

                    //分录辅助核算取得
                    List<FadCertificateAccountDto> fadCertificateAccountLst3 = new ArrayList<>();
                    List<FadCertificateAccountDto> fadCertificateAccountList = fadCertificateDetail.getFadCertificateAccountDto() != null ? fadCertificateDetail.getFadCertificateAccountDto() : new ArrayList<>();
                    int fadCertificateAccountSeqNo = 1;
                    for (int z = 0; z < fadCertificateAccountList.size(); z++) {
                        FadCertificateAccountDto fadCertificateAccount = fadCertificateAccountList.get(z);
                        String fadCertificateAccountUuid = fadCertificateAccount.getUuid();
                        FadCertificateAccountDto fadCertificateAccountInsert = BeanFactory.getObject(FadCertificateAccountDto.class);
                        BeanUtil.bean2Bean(fadCertificateAccount, fadCertificateAccountInsert);
                        fadCertificateAccountInsert.setIsVoid(null);
                        fadCertificateAccountInsert.setUuid(null);
                        fadCertificateAccountInsert.setFadCertificateDetailId(fadCertificateDetailInsertUuid);
                        fadCertificateAccountInsert.setSeqNo(fadCertificateAccountSeqNo);
                        fadCertificateAccountLst3.add(fadCertificateAccountInsert);
                        String fadCertificateAccountInsertUuid = isNullReturnEmpty(fadCertificateAccountInsert.getUuid());
                        fadCertificateAccountSeqNo = fadCertificateAccountSeqNo + 1;
                    }
                    fadCertificateDetailInsert.setFadCertificateAccountDto(fadCertificateAccountLst3);
                }
            } else {
                String errMessage = "记录不存在!";
                errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                returnValue = errMessages;
            }
        }
        if (isNullReturnEmpty(fadCertificateInsertUuid).length() > 0) {

            //逻辑删除合并源凭证
            //DtoHelper.batchUpdate(fadCertificateLst1s, FadCertificate.class);
            DtoHelper.batchUpdate(fadCertificateDetailLst2s, FadCertificateDetail.class);
            DtoHelper.batchUpdate(fadCertificateAccountLst3s, FadCertificateAccount.class);

            fadCertificateInsert.setDebtor(debtor);
            fadCertificateInsert.setCreditor(creditor);
            fadCertificateInsert.setOriginalFormCode(StringDistinct(originalFormCodes, MergeSplitSymbol));
            fadCertificateInsert.setAbstracts(StringDistinct(abstractss, MergeSplitSymbol));
            fadCertificateInsert.setReceiverOrPayerName(StringDistinct(receiverOrPayerNames, MergeSplitSymbol));
            PersistenceFactory.getInstance().update(fadCertificateInsert);

            PersistenceFactory.getInstance().batchInsert(fadCertifiMergeSplitRelLst4);
            DtoHelper.batchAdd(fadCertificateDetailLst2, FadCertificateDetail.class);
            certificateDetailSetSon(fadCertificateDetailLst2);
            certificateDetailSetOrder(fadCertificateDetailLst2);
            DtoHelper.batchUpdate(fadCertificateDetailLst2, FadCertificateDetail.class);
            //returnValue = certificateDetailMerge(fadCertificateInsertUuid);
        }

        return returnValue;
    }

    @Override
    public String certificateMergeRestore(String fadCertificateUuid) {
        String returnValue = "OK";

        //Err
        String errMessages = "";

        //主表凭证取得
        FadCertificateDto fadCertificate = isNullReturnEmpty(fadCertificateUuid).length() > 0 ? (FadCertificateDto) DtoHelper.findDtoByPK(FadCertificateDto.class, fadCertificateUuid) : null;
        if (fadCertificate != null) {
            Map<String, Object> fadCertifiMergeRelConditionsMap = new HashMap<String, Object>();
            fadCertifiMergeRelConditionsMap.put(FadCertifiMergeSplitRelAttribute.MERGE_ID, isNullReturnEmpty(fadCertificate.getUuid()));
            List<FadCertifiMergeSplitRel> fadCertifiMergeRelList = isNullReturnEmpty(fadCertificate.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiMergeSplitRel.class, fadCertifiMergeRelConditionsMap, "seq_no asc") : new ArrayList<>();
            if (!(fadCertifiMergeRelList.size() > 0)) {
                throw new BizException("复原【合并凭证】失败!(不存在【合并关系】的【合并凭证】【" + isNullReturnEmpty(fadCertificate.getOriginalFormCode()) + "】不能复原)");
            }

            //凭证合并拆分关系取得
            Map<String, Object> fadCertifiSplitRelConditionsMap = new HashMap<String, Object>();
            fadCertifiSplitRelConditionsMap.put(FadCertifiMergeSplitRelAttribute.SPLIT_ID, isNullReturnEmpty(fadCertificate.getUuid()));
            List<FadCertifiMergeSplitRel> fadCertifiSplitRelList = isNullReturnEmpty(fadCertificate.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiMergeSplitRel.class, fadCertifiSplitRelConditionsMap, "seq_no asc") : new ArrayList<>();
            if (fadCertifiSplitRelList.size() > 0) {
                throw new BizException("复原【合并凭证】失败!(存在【拆分关系】的【合并凭证】【" + isNullReturnEmpty(fadCertificate.getOriginalFormCode()) + "】不能复原)");
            }

            String sql = "SELECT 1 FROM FAD_CERTIFICATE WHERE FREE2 = '" + isNullReturnEmpty(fadCertificate.getUuid()) + "' AND STATE IN ('0', '1')";
            DAOMeta daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                throw new BizException("复原【合并凭证】失败!(【已复制凭证】【" + isNullReturnEmpty(fadCertificate.getOriginalFormCode()) + "】不能复原)");
            }

            sql = "SELECT 1 FROM FAD_CERTIFICATE WHERE UUID = '" + isNullReturnEmpty(fadCertificate.getUuid()) + "' AND FREE2 IS NOT NULL";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                throw new BizException("复原【合并凭证】失败!(【复制凭证】【" + isNullReturnEmpty(fadCertificate.getOriginalFormCode()) + "】不能复原)");
            }

            //凭证冲红关系取得
            Map<String, Object> fadCertifiCertifiRelConditionsMap = new HashMap<String, Object>();
            fadCertifiCertifiRelConditionsMap.put(FadCertifiDeficitRelAttribute.CERTIFI_ID, isNullReturnEmpty(fadCertificate.getUuid()));
            List<FadCertifiDeficitRel> fadCertifiCertifiRelList = isNullReturnEmpty(fadCertificate.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiDeficitRel.class, fadCertifiCertifiRelConditionsMap, "seq_no asc") : new ArrayList<>();
            if (fadCertifiCertifiRelList.size() > 0) {
                throw new BizException("复原【合并凭证】失败!(【已红冲凭证】【" + isNullReturnEmpty(fadCertificate.getOriginalFormCode()) + "】不能复原)");
            }

            //凭证红冲关系取得
            Map<String, Object> fadCertifiDeficitRelConditionsMap = new HashMap<String, Object>();
            fadCertifiDeficitRelConditionsMap.put(FadCertifiDeficitRelAttribute.DEFICIT_ID, isNullReturnEmpty(fadCertificate.getUuid()));
            List<FadCertifiDeficitRel> fadCertifiDeficitRelList = isNullReturnEmpty(fadCertificate.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiDeficitRel.class, fadCertifiDeficitRelConditionsMap, "seq_no asc") : new ArrayList<>();
            if (fadCertifiDeficitRelList.size() > 0) {
                throw new BizException("复原【合并凭证】失败!(【红冲凭证】【" + isNullReturnEmpty(fadCertificate.getOriginalFormCode()) + "】不能复原)");
            }

            //验证合并拆分凭证
            certificateCheckMoney(fadCertificate);

            //复原开始
            //访问跨库视图,检测凭证是否在NC库存在
            /*sql = "SELECT 1 FROM NC_CERTIFICATE WHERE PK_DOCID = '" + isNullReturnEmpty(fadCertificate.getUuid()) + "'";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                throw new BizException("复原【合并凭证】失败!(【在NC库中已存在的凭证】【" + isNullReturnEmpty(fadCertificate.getOriginalFormCode()) + "】不能复原【提示:请先删除NC凭证再尝试复原】)");
            }*/

            for (int i = 0; i < fadCertifiMergeRelList.size(); i++) {
                FadCertifiMergeSplitRel fadCertifiMergeRel = (FadCertifiMergeSplitRel) fadCertifiMergeRelList.get(i);

                //目标【复原凭证】取得
                FadCertificateDto fadCertificateSplit = isNullReturnEmpty(fadCertifiMergeRel.getSplitId()).length() > 0 ? (FadCertificateDto) DtoHelper.findDtoByPK(FadCertificateDto.class, isNullReturnEmpty(fadCertifiMergeRel.getSplitId())) : null;

                //复原【凭证】
                fadCertificateSplit.setState("0");
                fadCertificateSplit.setCertificateNo(null);
                fadCertificateSplit.setFeedback(null);
                fadCertificateSplit.setMaker(null);
                fadCertificateSplit.setMakeDate(null);
                fadCertificateSplit.setNcrequestUrl(null);
                fadCertificateSplit.setNcrequestXml(null);
                fadCertificateSplit.setNcresponseXml(null);
                certificateSetVoid(fadCertificateSplit, null);
                updateStateToSentForFadCertificate(fadCertificateSplit.getBusinessId(), null, fadCertificateSplit.getOriginalFormType(), "8", fadCertificateSplit.getState(), true, fadCertificateSplit.getUuid(), false);
            }

            //【合并凭证】的【合并关系】删除
            PersistenceFactory.getInstance().batchDelete(fadCertifiMergeRelList);

            //如【合并凭证】为已发送凭证,则警告用户登录NC系统同步删除该凭证
            if (isNullReturnEmpty(fadCertificate.getState()).equals("1")) {
                String errMessage = "复原【合并凭证】到【原始凭证】成功!警告:【合并凭证】【" + nvl(fadCertificate.getCertificateNo(), fadCertificate.getUuid()) + "】为【已发送NC凭证】,请您务必登录【NC系统】删除该凭证!";
                errMessages = (errMessages.length() > 0) ? errMessages + "\n\n" + errMessage : "\n" + errMessage;
                returnValue = errMessages;
            }

            //【合并凭证】删除
            List fadCertificateLst1 = new ArrayList<>();
            fadCertificateLst1.add(fadCertificate);
            DtoHelper.batchDel(fadCertificateLst1, FadCertificateDto.class);
            //复原结束
        } else {
            String errMessage = "记录不存在!";
            errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
            returnValue = errMessages;
        }

        return returnValue;
    }

    @Override
    public String certificateSplit(String fadCertificateUuid, Integer certificateSplitCount) {
        String returnValue = "OK";

        //Err
        String errMessages = "";

        List fadCertificateLst1 = new ArrayList<>();
        List<FadCertifiMergeSplitRel> fadCertifiMergeSplitRelLst4 = new ArrayList<>();
        String fadCertifiMergeSplitRelBatId = isNullReturnEmpty(UUIDUtil.getUUID());

        //主表凭证取得
        FadCertificateDto fadCertificate = isNullReturnEmpty(fadCertificateUuid).length() > 0 ? (FadCertificateDto) DtoHelper.findDtoByPK(FadCertificateDto.class, fadCertificateUuid) : null;
        if (fadCertificate != null) {

            //子表凭证摘要取得
            List<FadCertificateDetailDto> fadCertificateDetailList = fadCertificate.getFadCertificateDetailDto() != null ? fadCertificate.getFadCertificateDetailDto() : new ArrayList<>();

            //凭证合并拆分关系取得
            Map<String, Object> fadCertifiSplitRelConditionsMap = new HashMap<String, Object>();
            fadCertifiSplitRelConditionsMap.put(FadCertifiMergeSplitRelAttribute.SPLIT_ID, isNullReturnEmpty(fadCertificate.getUuid()));
            List<FadCertifiMergeSplitRel> fadCertifiSplitRelList = isNullReturnEmpty(fadCertificate.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiMergeSplitRel.class, fadCertifiSplitRelConditionsMap, "seq_no asc") : new ArrayList<>();
            if (fadCertifiSplitRelList.size() > 0) {
                throw new BizException("拆分凭证失败!(【拆分凭证】【" + isNullReturnEmpty(fadCertificate.getOriginalFormCode()) + "】不能再次拆分)");
            }

            Map<String, Object> fadCertifiMergeRelConditionsMap = new HashMap<String, Object>();
            fadCertifiMergeRelConditionsMap.put(FadCertifiMergeSplitRelAttribute.MERGE_ID, isNullReturnEmpty(fadCertificate.getUuid()));
            List<FadCertifiMergeSplitRel> fadCertifiMergeRelList = isNullReturnEmpty(fadCertificate.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiMergeSplitRel.class, fadCertifiMergeRelConditionsMap, "seq_no asc") : new ArrayList<>();
            if (fadCertifiMergeRelList.size() > 0) {
                throw new BizException("拆分凭证失败!(【合并凭证】【" + isNullReturnEmpty(fadCertificate.getOriginalFormCode()) + "】不能拆分)");
            }

            String sql = "SELECT 1 FROM FAD_CERTIFICATE WHERE FREE2 = '" + isNullReturnEmpty(fadCertificate.getUuid()) + "' AND STATE IN ('0', '1')";
            DAOMeta daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                throw new BizException("拆分凭证失败!(【已复制凭证】【" + isNullReturnEmpty(fadCertificate.getOriginalFormCode()) + "】不能拆分)");
            }

            sql = "SELECT 1 FROM FAD_CERTIFICATE WHERE UUID = '" + isNullReturnEmpty(fadCertificate.getUuid()) + "' AND FREE2 IS NOT NULL";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                throw new BizException("拆分凭证失败!(【复制凭证】【" + isNullReturnEmpty(fadCertificate.getOriginalFormCode()) + "】不能拆分)");
            }

            //凭证冲红关系取得
            Map<String, Object> fadCertifiCertifiRelConditionsMap = new HashMap<String, Object>();
            fadCertifiCertifiRelConditionsMap.put(FadCertifiDeficitRelAttribute.CERTIFI_ID, isNullReturnEmpty(fadCertificate.getUuid()));
            List<FadCertifiDeficitRel> fadCertifiCertifiRelList = isNullReturnEmpty(fadCertificate.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiDeficitRel.class, fadCertifiCertifiRelConditionsMap, "seq_no asc") : new ArrayList<>();
            if (fadCertifiCertifiRelList.size() > 0) {
                throw new BizException("拆分凭证失败!(【已红冲凭证】【" + isNullReturnEmpty(fadCertificate.getOriginalFormCode()) + "】不能拆分)");
            }

            //凭证红冲关系取得
            Map<String, Object> fadCertifiDeficitRelConditionsMap = new HashMap<String, Object>();
            fadCertifiDeficitRelConditionsMap.put(FadCertifiDeficitRelAttribute.DEFICIT_ID, isNullReturnEmpty(fadCertificate.getUuid()));
            List<FadCertifiDeficitRel> fadCertifiDeficitRelList = isNullReturnEmpty(fadCertificate.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiDeficitRel.class, fadCertifiDeficitRelConditionsMap, "seq_no asc") : new ArrayList<>();
            if (fadCertifiDeficitRelList.size() > 0) {
                throw new BizException("拆分凭证失败!(【红冲凭证】【" + isNullReturnEmpty(fadCertificate.getOriginalFormCode()) + "】不能拆分)");
            }

            //验证合并拆分凭证
            certificateCheckMoney(fadCertificate);

            for (int i = 0; i < certificateSplitCount; i++) {
                String fadCertificateInsertUuid = "";

                FadCertificateDto fadCertificateInsert = BeanFactory.getObject(FadCertificateDto.class);
                BeanUtil.bean2Bean(fadCertificate, fadCertificateInsert);
                fadCertificateInsert.setUuid(null);
                if (i == certificateSplitCount - 1) {
                    if (new BigDecimal(isEmptyReturnZero(isNullReturnEmpty(fadCertificate.getDebtor()))).compareTo(new BigDecimal("0")) != 0) {
                        fadCertificateInsert.setDebtor(fadCertificate.getDebtor().divideAndRemainder(new BigDecimal(certificateSplitCount.toString()))[0].add(fadCertificate.getDebtor().divideAndRemainder(new BigDecimal(certificateSplitCount.toString()))[1]));
                    }
                    if (new BigDecimal(isEmptyReturnZero(isNullReturnEmpty(fadCertificate.getCreditor()))).compareTo(new BigDecimal("0")) != 0) {
                        fadCertificateInsert.setCreditor(fadCertificate.getCreditor().divideAndRemainder(new BigDecimal(certificateSplitCount.toString()))[0].add(fadCertificate.getCreditor().divideAndRemainder(new BigDecimal(certificateSplitCount.toString()))[1]));
                    }
                } else {
                    if (new BigDecimal(isEmptyReturnZero(isNullReturnEmpty(fadCertificate.getDebtor()))).compareTo(new BigDecimal("0")) != 0) {
                        fadCertificateInsert.setDebtor(fadCertificate.getDebtor().divideAndRemainder(new BigDecimal(certificateSplitCount.toString()))[0]);
                    }
                    if (new BigDecimal(isEmptyReturnZero(isNullReturnEmpty(fadCertificate.getCreditor()))).compareTo(new BigDecimal("0")) != 0) {
                        fadCertificateInsert.setCreditor(fadCertificate.getCreditor().divideAndRemainder(new BigDecimal(certificateSplitCount.toString()))[0]);
                    }
                }
                fadCertificateInsert.setState("0");
                fadCertificateInsert.setCertificateNo(null);
                fadCertificateInsert.setFeedback(null);
                fadCertificateInsert.setNcrequestUrl(null);
                fadCertificateInsert.setNcrequestXml(null);
                fadCertificateInsert.setNcresponseXml(null);
                fadCertificateInsert.setMaker(null);
                fadCertificateInsert.setMakeDate(null);
                fadCertificateLst1.add(fadCertificateInsert);
                fadCertificateInsertUuid = isNullReturnEmpty(fadCertificateInsert.getUuid());

                FadCertifiMergeSplitRel fadCertifiMergeSplitRel = BeanFactory.getObject(FadCertifiMergeSplitRel.class);
                fadCertifiMergeSplitRel.setMergeId(fadCertificateUuid);
                fadCertifiMergeSplitRel.setSplitId(fadCertificateInsertUuid);
                fadCertifiMergeSplitRel.setBatId(fadCertifiMergeSplitRelBatId);
                fadCertifiMergeSplitRelLst4.add(fadCertifiMergeSplitRel);

                List<FadCertificateDetailDto> fadCertificateDetailLst2 = new ArrayList<>();
                for (int y = 0; y < fadCertificateDetailList.size(); y++) {
                    FadCertificateDetailDto fadCertificateDetail = fadCertificateDetailList.get(y);
                    String fadCertificateDetailUuid = isNullReturnEmpty(fadCertificateDetail.getUuid());
                    FadCertificateDetailDto fadCertificateDetailInsert = BeanFactory.getObject(FadCertificateDetailDto.class);
                    BeanUtil.bean2Bean(fadCertificateDetail, fadCertificateDetailInsert);
                    fadCertificateDetailInsert.setUuid(null);
                    fadCertificateDetailInsert.setFadCertificateId(fadCertificateInsertUuid);
                    fadCertificateDetailInsert.setOrderNo(y + 1);
                    fadCertificateDetailInsert.setSeqNo(y + 1);
                    if (i == certificateSplitCount - 1) {
                        if (new BigDecimal(isEmptyReturnZero(isNullReturnEmpty(fadCertificateDetail.getPrimary()))).compareTo(new BigDecimal("0")) != 0) {
                            fadCertificateDetailInsert.setPrimary(fadCertificateDetail.getPrimary().divideAndRemainder(new BigDecimal(certificateSplitCount.toString()))[0].add(fadCertificateDetail.getPrimary().divideAndRemainder(new BigDecimal(certificateSplitCount.toString()))[1]));
                        }
                        if (new BigDecimal(isEmptyReturnZero(isNullReturnEmpty(fadCertificateDetail.getLocal()))).compareTo(new BigDecimal("0")) != 0) {
                            fadCertificateDetailInsert.setLocal(fadCertificateDetail.getLocal().divideAndRemainder(new BigDecimal(certificateSplitCount.toString()))[0].add(fadCertificateDetail.getLocal().divideAndRemainder(new BigDecimal(certificateSplitCount.toString()))[1]));
                        }
                    } else {
                        if (new BigDecimal(isEmptyReturnZero(isNullReturnEmpty(fadCertificateDetail.getPrimary()))).compareTo(new BigDecimal("0")) != 0) {
                            fadCertificateDetailInsert.setPrimary(fadCertificateDetail.getPrimary().divideAndRemainder(new BigDecimal(certificateSplitCount.toString()))[0]);
                        }
                        if (new BigDecimal(isEmptyReturnZero(isNullReturnEmpty(fadCertificateDetail.getLocal()))).compareTo(new BigDecimal("0")) != 0) {
                            fadCertificateDetailInsert.setLocal(fadCertificateDetail.getLocal().divideAndRemainder(new BigDecimal(certificateSplitCount.toString()))[0]);
                        }
                    }
                    fadCertificateDetailLst2.add(fadCertificateDetailInsert);
                    String fadCertificateDetailInsertUuid = isNullReturnEmpty(fadCertificateDetailInsert.getUuid());

                    //分录辅助核算取得
                    List<FadCertificateAccountDto> fadCertificateAccountLst3 = new ArrayList<>();
                    List<FadCertificateAccountDto> fadCertificateAccountList = fadCertificateDetail.getFadCertificateAccountDto() != null ? fadCertificateDetail.getFadCertificateAccountDto() : new ArrayList<>();
                    for (int z = 0; z < fadCertificateAccountList.size(); z++) {
                        FadCertificateAccountDto fadCertificateAccount = fadCertificateAccountList.get(z);
                        String fadCertificateAccountUuid = isNullReturnEmpty(fadCertificateAccount.getUuid());
                        FadCertificateAccountDto fadCertificateAccountInsert = BeanFactory.getObject(FadCertificateAccountDto.class);
                        BeanUtil.bean2Bean(fadCertificateAccount, fadCertificateAccountInsert);
                        fadCertificateAccountInsert.setUuid(null);
                        fadCertificateAccountInsert.setFadCertificateDetailId(fadCertificateDetailInsertUuid);
                        fadCertificateAccountInsert.setSeqNo(z + 1);
                        fadCertificateAccountLst3.add(fadCertificateAccountInsert);
                        String fadCertificateAccountInsertUuid = isNullReturnEmpty(fadCertificateAccountInsert.getUuid());
                    }
                    fadCertificateDetailInsert.setFadCertificateAccountDto(fadCertificateAccountLst3);
                }
                fadCertificateInsert.setFadCertificateDetailDto(fadCertificateDetailLst2);
            }
            DtoHelper.batchAdd(fadCertificateLst1, FadCertificate.class);

            //逻辑删除拆分源凭证
            certificateSetVoid(fadCertificate, 1);

            for (int i = 0; i < fadCertificateLst1.size(); i++) {
                FadCertificate fadCertificateInsert = (FadCertificate) fadCertificateLst1.get(i);
                FadCertifiMergeSplitRel fadCertifiMergeSplitRel = (FadCertifiMergeSplitRel) fadCertifiMergeSplitRelLst4.get(i);
                fadCertifiMergeSplitRel.setSplitId(isNullReturnEmpty(fadCertificateInsert.getUuid()));
            }
            PersistenceFactory.getInstance().batchInsert(fadCertifiMergeSplitRelLst4);
            certificateSetSon(fadCertificateLst1);
        } else {
            String errMessage = "记录不存在!";
            errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
            returnValue = errMessages;
        }

        return returnValue;
    }

    @Override
    public String certificateSplitRestore(String fadCertificateUuid) {
        String returnValue = "OK";

        //Err
        String errMessages = "";

        //主表凭证取得
        FadCertificateDto fadCertificate = isNullReturnEmpty(fadCertificateUuid).length() > 0 ? (FadCertificateDto) DtoHelper.findDtoByPK(FadCertificateDto.class, fadCertificateUuid) : null;
        if (fadCertificate != null) {
            //凭证合并拆分关系取得
            Map<String, Object> fadCertifiSplitRelConditionsMap = new HashMap<String, Object>();
            fadCertifiSplitRelConditionsMap.put(FadCertifiMergeSplitRelAttribute.SPLIT_ID, isNullReturnEmpty(fadCertificate.getUuid()));
            List<FadCertifiMergeSplitRel> fadCertifiSplitRelList = isNullReturnEmpty(fadCertificate.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiMergeSplitRel.class, fadCertifiSplitRelConditionsMap, "seq_no asc") : new ArrayList<>();
            if (!(fadCertifiSplitRelList.size() > 0)) {
                throw new BizException("复原【拆分凭证】失败!(不存在【拆分关系】的【拆分凭证】【" + isNullReturnEmpty(fadCertificate.getOriginalFormCode()) + "】不能复原)");
            }

            Map<String, Object> fadCertifiMergeRelConditionsMap = new HashMap<String, Object>();
            fadCertifiMergeRelConditionsMap.put(FadCertifiMergeSplitRelAttribute.MERGE_ID, isNullReturnEmpty(fadCertificate.getUuid()));
            List<FadCertifiMergeSplitRel> fadCertifiMergeRelList = isNullReturnEmpty(fadCertificate.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiMergeSplitRel.class, fadCertifiMergeRelConditionsMap, "seq_no asc") : new ArrayList<>();
            if (fadCertifiMergeRelList.size() > 0) {
                throw new BizException("复原【拆分凭证】失败!(存在【合并关系】的【拆分凭证】【" + isNullReturnEmpty(fadCertificate.getOriginalFormCode()) + "】不能复原)");
            }

            String sql = "SELECT 1 FROM FAD_CERTIFICATE WHERE FREE2 = '" + isNullReturnEmpty(fadCertificate.getUuid()) + "' AND STATE IN ('0', '1')";
            DAOMeta daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                throw new BizException("复原【拆分凭证】失败!(【已复制凭证】【" + isNullReturnEmpty(fadCertificate.getOriginalFormCode()) + "】不能复原)");
            }

            sql = "SELECT 1 FROM FAD_CERTIFICATE WHERE UUID = '" + isNullReturnEmpty(fadCertificate.getUuid()) + "' AND FREE2 IS NOT NULL";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                throw new BizException("复原【拆分凭证】失败!(【复制凭证】【" + isNullReturnEmpty(fadCertificate.getOriginalFormCode()) + "】不能复原)");
            }

            //凭证冲红关系取得
            Map<String, Object> fadCertifiCertifiRelConditionsMap = new HashMap<String, Object>();
            fadCertifiCertifiRelConditionsMap.put(FadCertifiDeficitRelAttribute.CERTIFI_ID, isNullReturnEmpty(fadCertificate.getUuid()));
            List<FadCertifiDeficitRel> fadCertifiCertifiRelList = isNullReturnEmpty(fadCertificate.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiDeficitRel.class, fadCertifiCertifiRelConditionsMap, "seq_no asc") : new ArrayList<>();
            if (fadCertifiCertifiRelList.size() > 0) {
                throw new BizException("复原【拆分凭证】失败!(【已红冲凭证】【" + isNullReturnEmpty(fadCertificate.getOriginalFormCode()) + "】不能复原)");
            }

            //凭证红冲关系取得
            Map<String, Object> fadCertifiDeficitRelConditionsMap = new HashMap<String, Object>();
            fadCertifiDeficitRelConditionsMap.put(FadCertifiDeficitRelAttribute.DEFICIT_ID, isNullReturnEmpty(fadCertificate.getUuid()));
            List<FadCertifiDeficitRel> fadCertifiDeficitRelList = isNullReturnEmpty(fadCertificate.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiDeficitRel.class, fadCertifiDeficitRelConditionsMap, "seq_no asc") : new ArrayList<>();
            if (fadCertifiDeficitRelList.size() > 0) {
                throw new BizException("复原【拆分凭证】失败!(【红冲凭证】【" + isNullReturnEmpty(fadCertificate.getOriginalFormCode()) + "】不能复原)");
            }

            //验证合并拆分凭证
            certificateCheckMoney(fadCertificate);

            //复原开始
            //目标【复原凭证】取得
            FadCertificateDto fadCertificateMerge = isNullReturnEmpty(((FadCertifiMergeSplitRel) fadCertifiSplitRelList.get(0)).getMergeId()).length() > 0 ? (FadCertificateDto) DtoHelper.findDtoByPK(FadCertificateDto.class, isNullReturnEmpty(((FadCertifiMergeSplitRel) fadCertifiSplitRelList.get(0)).getMergeId())) : null;

            //复原【凭证】
            fadCertificateMerge.setState("0");
            fadCertificateMerge.setCertificateNo(null);
            fadCertificateMerge.setFeedback(null);
            fadCertificateMerge.setMaker(null);
            fadCertificateMerge.setMakeDate(null);
            fadCertificateMerge.setNcrequestUrl(null);
            fadCertificateMerge.setNcrequestXml(null);
            fadCertificateMerge.setNcresponseXml(null);
            certificateSetVoid(fadCertificateMerge, null);
            updateStateToSentForFadCertificate(fadCertificateMerge.getBusinessId(), null, fadCertificateMerge.getOriginalFormType(), "8", fadCertificateMerge.getState(), true, fadCertificateMerge.getUuid(), false);

            //【拆分凭证】的【拆分关系】取得
            fadCertifiMergeRelConditionsMap = new HashMap<String, Object>();
            fadCertifiMergeRelConditionsMap.put(FadCertifiMergeSplitRelAttribute.MERGE_ID, isNullReturnEmpty(((FadCertifiMergeSplitRel) fadCertifiSplitRelList.get(0)).getMergeId()));
            fadCertifiMergeRelList = isNullReturnEmpty(((FadCertifiMergeSplitRel) fadCertifiSplitRelList.get(0)).getMergeId()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiMergeSplitRel.class, fadCertifiMergeRelConditionsMap, "seq_no asc") : new ArrayList<>();
            List fadCertificateLst1 = new ArrayList<>();
            for (int i = 0; i < fadCertifiMergeRelList.size(); i++) {
                FadCertifiMergeSplitRel fadCertifiMergeRel = (FadCertifiMergeSplitRel) fadCertifiMergeRelList.get(i);

                //访问跨库视图,检测凭证是否在NC库存在
                /*sql = "SELECT 1 FROM NC_CERTIFICATE WHERE PK_DOCID = '" + isNullReturnEmpty(fadCertifiMergeRel.getSplitId()) + "'";
                daoMeta = new DAOMeta();
                daoMeta.setStrSql(sql);
                daoMeta.setNeedFilter(false);
                if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                    throw new BizException("复原【拆分凭证】失败!(【在NC库中已存在的凭证】【" + isNullReturnEmpty(fadCertificate.getOriginalFormCode()) + "】不能复原【提示:请先删除NC凭证再尝试复原】)");
                }*/

                //【拆分凭证】取得
                FadCertificateDto fadCertificateSplit = isNullReturnEmpty(fadCertifiMergeRel.getSplitId()).length() > 0 ? (FadCertificateDto) DtoHelper.findDtoByPK(FadCertificateDto.class, isNullReturnEmpty(fadCertifiMergeRel.getSplitId())) : null;
                fadCertificateLst1.add(fadCertificateSplit);

                //如【拆分凭证】为已发送凭证,则警告用户登录NC系统同步删除该凭证
                if (isNullReturnEmpty(fadCertificateSplit.getState()).equals("1")) {
                    String errMessage = "复原【拆分凭证】到【原始凭证】成功!警告:【拆分凭证】【" + nvl(fadCertificateSplit.getCertificateNo(), fadCertificateSplit.getUuid()) + "】为【已发送NC凭证】,请您务必登录【NC系统】删除该凭证!";
                    errMessages = (errMessages.length() > 0) ? errMessages + "\n\n" + errMessage : "\n" + errMessage;
                    returnValue = errMessages;
                }
            }

            //【拆分凭证】的【拆分关系】删除
            PersistenceFactory.getInstance().batchDelete(fadCertifiMergeRelList);

            //【拆分凭证】删除
            DtoHelper.batchDel(fadCertificateLst1, FadCertificateDto.class);
            //复原结束
        } else {
            String errMessage = "记录不存在!";
            errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
            returnValue = errMessages;
        }

        return returnValue;
    }

    @Override
    public void certificateSetVoid(FadCertificateDto fadCertificate, Integer isVoid) {
        if (Integer.parseInt(isEmptyReturnZero(isNullReturnEmpty(isVoid))) == 1) {
            fadCertificate.setIsVoid(isVoid);

            //子表凭证摘要取得
            List fadCertificateDetailLst2 = new ArrayList<>();
            List fadCertificateAccountLst3 = new ArrayList<>();
            List<FadCertificateDetailDto> fadCertificateDetailList = fadCertificate.getFadCertificateDetailDto() != null ? fadCertificate.getFadCertificateDetailDto() : new ArrayList<>();
            for (int y = 0; y < fadCertificateDetailList.size(); y++) {
                FadCertificateDetailDto fadCertificateDetail = fadCertificateDetailList.get(y);
                String fadCertificateDetailUuid = isNullReturnEmpty(fadCertificateDetail.getUuid());
                fadCertificateDetail.setIsVoid(isVoid);
                fadCertificateDetailLst2.add(fadCertificateDetail);

                //分录辅助核算取得
                List<FadCertificateAccountDto> fadCertificateAccountList = fadCertificateDetail.getFadCertificateAccountDto() != null ? fadCertificateDetail.getFadCertificateAccountDto() : new ArrayList<>();
                for (int z = 0; z < fadCertificateAccountList.size(); z++) {
                    FadCertificateAccountDto fadCertificateAccount = fadCertificateAccountList.get(z);
                    String fadCertificateAccountUuid = isNullReturnEmpty(fadCertificateAccount.getUuid());
                    fadCertificateAccount.setIsVoid(isVoid);
                    fadCertificateAccountLst3.add(fadCertificateAccount);
                }
            }
            List fadCertificateList = new ArrayList<>();
            fadCertificateList.add(fadCertificate);
            DtoHelper.batchUpdate(fadCertificateList, FadCertificate.class);
            DtoHelper.batchUpdate(fadCertificateDetailLst2, FadCertificateDetail.class);
            DtoHelper.batchUpdate(fadCertificateAccountLst3, FadCertificateAccount.class);
        } else if (isNullReturnEmpty(isVoid).length() == 0 || Integer.parseInt(isEmptyReturnZero(isNullReturnEmpty(isVoid))) == 0) {
            String sql = "UPDATE FAD_CERTIFICATE SET IS_VOID = NULL, STATE = '0', FEEDBACK = NULL WHERE UUID = '" + isNullReturnEmpty(fadCertificate.getUuid()) + "'";
            DAOMeta daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

            sql = "UPDATE FAD_CERTIFICATE_DETAIL SET IS_VOID = NULL WHERE FAD_CERTIFICATE_ID = '" + isNullReturnEmpty(fadCertificate.getUuid()) + "'";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

            sql = "UPDATE FAD_CERTIFICATE_ACCOUNT SET IS_VOID = NULL WHERE FAD_CERTIFICATE_DETAIL_ID IN (SELECT UUID FROM FAD_CERTIFICATE_DETAIL WHERE FAD_CERTIFICATE_ID = '" + isNullReturnEmpty(fadCertificate.getUuid()) + "')";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
        }
    }

    @Override
    public void certificateSetVoid(FadCertificateDto fadCertificate, List fadCertificateLst1, List fadCertificateDetailLst2, List fadCertificateAccountLst3, Integer isVoid) {
        if (Integer.parseInt(isEmptyReturnZero(isNullReturnEmpty(isVoid))) == 1) {
            fadCertificate.setIsVoid(isVoid);
            fadCertificateLst1.add(fadCertificate);

            //子表凭证摘要取得
            List<FadCertificateDetailDto> fadCertificateDetailList = fadCertificate.getFadCertificateDetailDto() != null ? fadCertificate.getFadCertificateDetailDto() : new ArrayList<>();
            for (int y = 0; y < fadCertificateDetailList.size(); y++) {
                FadCertificateDetailDto fadCertificateDetail = fadCertificateDetailList.get(y);
                String fadCertificateDetailUuid = isNullReturnEmpty(fadCertificateDetail.getUuid());
                fadCertificateDetail.setIsVoid(isVoid);
                fadCertificateDetailLst2.add(fadCertificateDetail);

                //分录辅助核算取得
                List<FadCertificateAccountDto> fadCertificateAccountList = fadCertificateDetail.getFadCertificateAccountDto() != null ? fadCertificateDetail.getFadCertificateAccountDto() : new ArrayList<>();
                for (int z = 0; z < fadCertificateAccountList.size(); z++) {
                    FadCertificateAccountDto fadCertificateAccount = fadCertificateAccountList.get(z);
                    String fadCertificateAccountUuid = isNullReturnEmpty(fadCertificateAccount.getUuid());
                    fadCertificateAccount.setIsVoid(isVoid);
                    fadCertificateAccountLst3.add(fadCertificateAccount);
                }
            }

            List fadCertificateList = new ArrayList<>();
            fadCertificateList.add(fadCertificate);
            DtoHelper.batchUpdate(fadCertificateList, FadCertificate.class);
        } else if (isNullReturnEmpty(isVoid).length() == 0 || Integer.parseInt(isEmptyReturnZero(isNullReturnEmpty(isVoid))) == 0) {
            String sql = "UPDATE FAD_CERTIFICATE SET IS_VOID = NULL, STATE = '0', FEEDBACK = NULL WHERE UUID = '" + isNullReturnEmpty(fadCertificate.getUuid()) + "'";
            DAOMeta daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

            sql = "UPDATE FAD_CERTIFICATE_DETAIL SET IS_VOID = NULL WHERE FAD_CERTIFICATE_ID = '" + isNullReturnEmpty(fadCertificate.getUuid()) + "'";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

            sql = "UPDATE FAD_CERTIFICATE_ACCOUNT SET IS_VOID = NULL WHERE FAD_CERTIFICATE_DETAIL_ID IN (SELECT UUID FROM FAD_CERTIFICATE_DETAIL WHERE FAD_CERTIFICATE_ID = '" + isNullReturnEmpty(fadCertificate.getUuid()) + "')";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

            //主表凭证取得
            fadCertificate = isNullReturnEmpty(fadCertificate.getUuid()).length() > 0 ? (FadCertificateDto) DtoHelper.findDtoByPK(FadCertificateDto.class, isNullReturnEmpty(fadCertificate.getUuid())) : null;
            fadCertificate.setIsVoid(isVoid);
            fadCertificateLst1.add(fadCertificate);

            //子表凭证摘要取得
            List<FadCertificateDetailDto> fadCertificateDetailList = fadCertificate.getFadCertificateDetailDto() != null ? fadCertificate.getFadCertificateDetailDto() : new ArrayList<>();
            for (int y = 0; y < fadCertificateDetailList.size(); y++) {
                FadCertificateDetailDto fadCertificateDetail = fadCertificateDetailList.get(y);
                String fadCertificateDetailUuid = isNullReturnEmpty(fadCertificateDetail.getUuid());
                fadCertificateDetail.setIsVoid(isVoid);
                fadCertificateDetailLst2.add(fadCertificateDetail);

                //分录辅助核算取得
                List<FadCertificateAccountDto> fadCertificateAccountList = fadCertificateDetail.getFadCertificateAccountDto() != null ? fadCertificateDetail.getFadCertificateAccountDto() : new ArrayList<>();
                for (int z = 0; z < fadCertificateAccountList.size(); z++) {
                    FadCertificateAccountDto fadCertificateAccount = fadCertificateAccountList.get(z);
                    String fadCertificateAccountUuid = isNullReturnEmpty(fadCertificateAccount.getUuid());
                    fadCertificateAccount.setIsVoid(isVoid);
                    fadCertificateAccountLst3.add(fadCertificateAccount);
                }
            }

            List fadCertificateList = new ArrayList<>();
            fadCertificateList.add(fadCertificate);
            DtoHelper.batchUpdate(fadCertificateList, FadCertificate.class);
        }
    }

    @Override
    public void certificateSetSon(List fadCertificateLst1) {
        List fadCertificateDetailLst2 = new ArrayList<>();
        for (int i = 0; i < fadCertificateLst1.size(); i++) {
            FadCertificateDto fadCertificateInsert = (FadCertificateDto) fadCertificateLst1.get(i);
            List<FadCertificateDetailDto> fadCertificateDetailInsertList = fadCertificateInsert.getFadCertificateDetailDto() != null ? fadCertificateInsert.getFadCertificateDetailDto() : new ArrayList<>();
            for (int y = 0; y < fadCertificateDetailInsertList.size(); y++) {
                FadCertificateDetailDto fadCertificateDetailInsert = fadCertificateDetailInsertList.get(y);
                fadCertificateDetailInsert.setFadCertificateId(isNullReturnEmpty(fadCertificateInsert.getUuid()));
                fadCertificateDetailLst2.add(fadCertificateDetailInsert);
            }
        }
        DtoHelper.batchAdd(fadCertificateDetailLst2, FadCertificateDetail.class);
        certificateDetailSetSon(fadCertificateDetailLst2);
    }

    @Override
    public void certificateDetailSetVoid(FadCertificateDetailDto fadCertificateDetail, Integer isVoid) {
        if (Integer.parseInt(isEmptyReturnZero(isNullReturnEmpty(isVoid))) == 1) {
            fadCertificateDetail.setIsVoid(isVoid);

            //分录辅助核算取得
            List fadCertificateAccountLst3 = new ArrayList<>();
            List<FadCertificateAccountDto> fadCertificateAccountList = fadCertificateDetail.getFadCertificateAccountDto() != null ? fadCertificateDetail.getFadCertificateAccountDto() : new ArrayList<>();
            for (int z = 0; z < fadCertificateAccountList.size(); z++) {
                FadCertificateAccountDto fadCertificateAccount = fadCertificateAccountList.get(z);
                String fadCertificateAccountUuid = isNullReturnEmpty(fadCertificateAccount.getUuid());
                fadCertificateAccount.setIsVoid(isVoid);
                fadCertificateAccountLst3.add(fadCertificateAccount);
            }

            List fadCertificateDetailList = new ArrayList<>();
            fadCertificateDetailList.add(fadCertificateDetail);
            DtoHelper.batchUpdate(fadCertificateDetailList, FadCertificateDetail.class);
            DtoHelper.batchUpdate(fadCertificateAccountLst3, FadCertificateAccount.class);
        } else if (isNullReturnEmpty(isVoid).length() == 0 || Integer.parseInt(isEmptyReturnZero(isNullReturnEmpty(isVoid))) == 0) {
            String sql = "UPDATE FAD_CERTIFICATE_DETAIL SET IS_VOID = NULL WHERE UUID = '" + isNullReturnEmpty(fadCertificateDetail.getUuid()) + "'";
            DAOMeta daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

            sql = "UPDATE FAD_CERTIFICATE_ACCOUNT SET IS_VOID = NULL WHERE FAD_CERTIFICATE_DETAIL_ID = '" + isNullReturnEmpty(fadCertificateDetail.getUuid()) + "'";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
        }
    }

    @Override
    public void certificateDetailSetVoid(FadCertificateDetailDto fadCertificateDetail, List fadCertificateDetailLst2, List fadCertificateAccountLst3, Integer isVoid) {
        if (Integer.parseInt(isEmptyReturnZero(isNullReturnEmpty(isVoid))) == 1) {
            fadCertificateDetail.setIsVoid(isVoid);
            fadCertificateDetailLst2.add(fadCertificateDetail);

            //分录辅助核算取得
            List<FadCertificateAccountDto> fadCertificateAccountList = fadCertificateDetail.getFadCertificateAccountDto() != null ? fadCertificateDetail.getFadCertificateAccountDto() : new ArrayList<>();
            for (int z = 0; z < fadCertificateAccountList.size(); z++) {
                FadCertificateAccountDto fadCertificateAccount = fadCertificateAccountList.get(z);
                String fadCertificateAccountUuid = isNullReturnEmpty(fadCertificateAccount.getUuid());
                fadCertificateAccount.setIsVoid(isVoid);
                fadCertificateAccountLst3.add(fadCertificateAccount);
            }
        } else if (isNullReturnEmpty(isVoid).length() == 0 || Integer.parseInt(isEmptyReturnZero(isNullReturnEmpty(isVoid))) == 0) {
            String sql = "UPDATE FAD_CERTIFICATE_DETAIL SET IS_VOID = NULL WHERE UUID = '" + isNullReturnEmpty(fadCertificateDetail.getUuid()) + "'";
            DAOMeta daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

            sql = "UPDATE FAD_CERTIFICATE_ACCOUNT SET IS_VOID = NULL WHERE FAD_CERTIFICATE_DETAIL_ID = '" + isNullReturnEmpty(fadCertificateDetail.getUuid()) + "'";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

            //子表凭证摘要取得
            fadCertificateDetail = isNullReturnEmpty(fadCertificateDetail.getUuid()).length() > 0 ? (FadCertificateDetailDto) DtoHelper.findDtoByPK(FadCertificateDetailDto.class, isNullReturnEmpty(fadCertificateDetail.getUuid())) : null;
            fadCertificateDetail.setIsVoid(isVoid);
            fadCertificateDetailLst2.add(fadCertificateDetail);

            //分录辅助核算取得
            List<FadCertificateAccountDto> fadCertificateAccountList = fadCertificateDetail.getFadCertificateAccountDto() != null ? fadCertificateDetail.getFadCertificateAccountDto() : new ArrayList<>();
            for (int z = 0; z < fadCertificateAccountList.size(); z++) {
                FadCertificateAccountDto fadCertificateAccount = fadCertificateAccountList.get(z);
                String fadCertificateAccountUuid = isNullReturnEmpty(fadCertificateAccount.getUuid());
                fadCertificateAccount.setIsVoid(isVoid);
                fadCertificateAccountLst3.add(fadCertificateAccount);
            }
        }
    }

    @Override
    public void certificateDetailSetSon(List fadCertificateDetailLst2) {
        List fadCertificateAccountLst3 = new ArrayList<>();
        for (int y = 0; y < fadCertificateDetailLst2.size(); y++) {
            FadCertificateDetailDto fadCertificateDetailInsert = (FadCertificateDetailDto) fadCertificateDetailLst2.get(y);
            List<FadCertificateAccountDto> fadCertificateAccountInsertList = fadCertificateDetailInsert.getFadCertificateAccountDto() != null ? fadCertificateDetailInsert.getFadCertificateAccountDto() : new ArrayList<>();
            for (int z = 0; z < fadCertificateAccountInsertList.size(); z++) {
                FadCertificateAccountDto fadCertificateAccountInsert = fadCertificateAccountInsertList.get(z);
                fadCertificateAccountInsert.setFadCertificateDetailId(isNullReturnEmpty(fadCertificateDetailInsert.getUuid()));
                fadCertificateAccountLst3.add(fadCertificateAccountInsert);
            }
        }
        DtoHelper.batchAdd(fadCertificateAccountLst3, FadCertificateAccount.class);
    }

    @Override
    public void certificateDetailSetOrder(List fadCertificateDetailLst2) {
        //子表凭证摘要取得
        int debtorCount = 1;
        for (int y = 0; y < fadCertificateDetailLst2.size(); y++) {
            FadCertificateDetailDto fadCertificateDetail = (FadCertificateDetailDto) fadCertificateDetailLst2.get(y);
            if (isNullReturnEmpty(fadCertificateDetail.getDebtorOrCreditor()).equals("0")) {
                fadCertificateDetail.setOrderNo(debtorCount);
                fadCertificateDetail.setSeqNo(debtorCount);
                debtorCount = debtorCount + 1;
            }
        }
        for (int y = 0; y < fadCertificateDetailLst2.size(); y++) {
            FadCertificateDetailDto fadCertificateDetail = (FadCertificateDetailDto) fadCertificateDetailLst2.get(y);
            if (!(isNullReturnEmpty(fadCertificateDetail.getDebtorOrCreditor()).equals("0"))) {
                fadCertificateDetail.setOrderNo(debtorCount);
                fadCertificateDetail.setSeqNo(debtorCount);
                debtorCount = debtorCount + 1;
            }
        }
    }

    @Override
    public String certificateDetailMerge(String fadCertificateUuid) {
        String returnValue = "OK";

        //Err
        String errMessages = "";

        //主表凭证取得
        FadCertificateDto fadCertificate = isNullReturnEmpty(fadCertificateUuid).length() > 0 ? (FadCertificateDto) DtoHelper.findDtoByPK(FadCertificateDto.class, fadCertificateUuid) : null;
        if (fadCertificate != null) {

            //子表凭证摘要取得
            List fadCertificateDetailLst2s = new ArrayList<>();
            List fadCertificateAccountLst3s = new ArrayList<>();
            List fadCertificateDetailLst2 = new ArrayList<>();
            Map<String, Object> fadCertificateDetailConditionsMap = new HashMap<String, Object>();
            fadCertificateDetailConditionsMap.put(FadCertificateDetailAttribute.FAD_CERTIFICATE_ID, fadCertificateUuid);
            //List fadCertificateDetailList = isNullReturnEmpty(fadCertificateUuid).length() > 0 ? DtoHelper.findByAnyFields(FadCertificateDetailDto.class, fadCertificateDetailConditionsMap, "seq_no asc") : new ArrayList<>();
            List<FadCertificateDetailDto> fadCertificateDetailList = fadCertificate.getFadCertificateDetailDto() != null ? fadCertificate.getFadCertificateDetailDto() : new ArrayList<>();
            int fadCertificateDetailListSize = fadCertificateDetailList.size();
            for (int y1 = 0; y1 < fadCertificateDetailListSize; y1++) {
                FadCertificateDetailDto fadCertificateDetail = (FadCertificateDetailDto) fadCertificateDetailList.get(y1);
                String fadCertificateDetailUuid = isNullReturnEmpty(fadCertificateDetail.getUuid());
                BigDecimal primary = new BigDecimal("0");
                BigDecimal local = new BigDecimal("0");
                primary = primary.add(new BigDecimal(isEmptyReturnZero(isNullReturnEmpty(fadCertificateDetail.getPrimary()))));
                local = local.add(new BigDecimal(isEmptyReturnZero(isNullReturnEmpty(fadCertificateDetail.getLocal()))));
                String originalFormCodes = isNullReturnEmpty(fadCertificateDetail.getOriginalFormCode());
                String abstractss = isNullReturnEmpty(fadCertificateDetail.getAbstracts());

                //子表凭证摘要泡沫对象取得
                for (int y2 = 0; y2 < fadCertificateDetailListSize; y2++) {
                    FadCertificateDetailDto fadCertificateDetailBubble = (FadCertificateDetailDto) fadCertificateDetailList.get(y2);
                    String fadCertificateDetailBubbleUuid = isNullReturnEmpty(fadCertificateDetailBubble.getUuid());
                    if (
                            (y1 != y2)
                                    && (isNullReturnEmpty(fadCertificateDetail.getDebtorOrCreditor()).equals(isNullReturnEmpty(fadCertificateDetailBubble.getDebtorOrCreditor())))
                                    && (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals(isNullReturnEmpty(fadCertificateDetailBubble.getSubjectCode())))
                            ) {

                        Boolean isMergeAlow = true;

                        if (
                                (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("12.2"))
                                        && (new BigDecimal(isEmptyReturnZero(isNullReturnEmpty(fadCertificateDetail.getLocal()))).add(new BigDecimal(isEmptyReturnZero(isNullReturnEmpty(fadCertificateDetailBubble.getLocal())))).compareTo(new BigDecimal("0")) == 0)
                                ) {
                            isMergeAlow = false;
                        } else {

                            //分录辅助核算取得
                            List<FadCertificateAccountDto> fadCertificateAccountList = ((FadCertificateDetailDto) fadCertificateDetailList.get(y1)).getFadCertificateAccountDto() != null ? ((FadCertificateDetailDto) fadCertificateDetailList.get(y1)).getFadCertificateAccountDto() : new ArrayList<>();
                            for (int z1 = 0; z1 < fadCertificateAccountList.size(); z1++) {
                                if (isMergeAlow) {
                                    FadCertificateAccountDto fadCertificateAccount = fadCertificateAccountList.get(z1);
                                    String fadCertificateAccountUuid = isNullReturnEmpty(fadCertificateAccount.getUuid());

                                    List<FadCertificateAccountDto> fadCertificateAccountBubbleList = ((FadCertificateDetailDto) fadCertificateDetailList.get(y2)).getFadCertificateAccountDto() != null ? ((FadCertificateDetailDto) fadCertificateDetailList.get(y2)).getFadCertificateAccountDto() : new ArrayList<>();
                                    if (fadCertificateAccountList.size() != fadCertificateAccountBubbleList.size()) {
                                        isMergeAlow = false;
                                        break;
                                    }
                                    for (int z2 = 0; z2 < fadCertificateAccountBubbleList.size(); z2++) {
                                        FadCertificateAccountDto fadCertificateAccountBubble = fadCertificateAccountBubbleList.get(z2);
                                        String fadCertificateAccountBubbleUuid = isNullReturnEmpty(fadCertificateAccountBubble.getUuid());

                                        if (isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals(isNullReturnEmpty(fadCertificateAccountBubble.getAccountNo()))) {
                                            if (!(isNullReturnEmpty(fadCertificateAccount.getAccountInfoCode()).equals(isNullReturnEmpty(fadCertificateAccountBubble.getAccountInfoCode())))) {
                                                isMergeAlow = false;
                                                break;
                                            } else {
                                                isMergeAlow = true;
                                                break;
                                            }
                                        } else {
                                            isMergeAlow = false;
                                        }
                                    }
                                } else {
                                    break;
                                }
                            }

                            if (isMergeAlow) {
                                primary = primary.add(new BigDecimal(isEmptyReturnZero(isNullReturnEmpty(fadCertificateDetailBubble.getPrimary()))));
                                local = local.add(new BigDecimal(isEmptyReturnZero(isNullReturnEmpty(fadCertificateDetailBubble.getLocal()))));

                                String originalFormCode = isNullReturnEmpty(fadCertificateDetailBubble.getOriginalFormCode());
                                if (isNullReturnEmpty(originalFormCode).length() > 0) {
                                    if (isNullReturnEmpty(originalFormCodes).length() > 0) {
                                        originalFormCodes = originalFormCodes + MergeSplitSymbol + isNullReturnEmpty(originalFormCode);
                                    } else {
                                        originalFormCodes = isNullReturnEmpty(originalFormCode);
                                    }
                                }

                                String abstracts = isNullReturnEmpty(fadCertificateDetailBubble.getAbstracts());
                                if (isNullReturnEmpty(abstracts).length() > 0) {
                                    if (isNullReturnEmpty(abstractss).length() > 0) {
                                        abstractss = abstractss + MergeSplitSymbol + isNullReturnEmpty(abstracts);
                                    } else {
                                        abstractss = isNullReturnEmpty(abstracts);
                                    }
                                }

                                FadCertificateDetailDto fadCertificateDetailInsert = BeanFactory.getObject(FadCertificateDetailDto.class);
                                BeanUtil.bean2Bean(fadCertificateDetail, fadCertificateDetailInsert);
                                fadCertificateDetailInsert.setUuid(null);
                                fadCertificateDetailInsert.setPrimary(primary);
                                fadCertificateDetailInsert.setLocal(local);
                                fadCertificateDetailInsert.setOriginalFormCode(StringDistinct(originalFormCodes, MergeSplitSymbol));
                                fadCertificateDetailInsert.setAbstracts(StringDistinct(abstractss, MergeSplitSymbol));
                                //fadCertificateDetailInsert.setBusinessId(isNullReturnEmpty(fadCertificate.getBusinessId()));
                                fadCertificateDetailLst2.add(fadCertificateDetailInsert);
                                String fadCertificateDetailInsertUuid = isNullReturnEmpty(fadCertificateDetailInsert.getUuid());

                                //分录辅助核算取得
                                List<FadCertificateAccountDto> fadCertificateAccountLst3 = new ArrayList<>();
                                for (int z = 0; z < fadCertificateAccountList.size(); z++) {
                                    FadCertificateAccountDto fadCertificateAccount = fadCertificateAccountList.get(z);
                                    String fadCertificateAccountUuid = isNullReturnEmpty(fadCertificateAccount.getUuid());
                                    FadCertificateAccountDto fadCertificateAccountInsert = BeanFactory.getObject(FadCertificateAccountDto.class);
                                    BeanUtil.bean2Bean(fadCertificateAccount, fadCertificateAccountInsert);
                                    fadCertificateAccountInsert.setUuid(null);
                                    fadCertificateAccountInsert.setFadCertificateDetailId(fadCertificateDetailInsertUuid);
                                    fadCertificateAccountLst3.add(fadCertificateAccountInsert);
                                    String fadCertificateAccountInsertUuid = isNullReturnEmpty(fadCertificateAccountInsert.getUuid());
                                }
                                fadCertificateDetailInsert.setFadCertificateAccountDto(fadCertificateAccountLst3);

                                //逻辑删除合并源凭证子表摘要
                                if (isNullReturnEmpty(fadCertificateDetailUuid).length() > 0) {
                                    certificateDetailSetVoid(fadCertificateDetail, fadCertificateDetailLst2s, fadCertificateAccountLst3s, 1);
                                } else {
                                    for (int y = 0; y < fadCertificateDetailLst2.size(); y++) {
                                        if (
                                                (isNullReturnEmpty(((FadCertificateDetailDto) fadCertificateDetailLst2.get(y)).getDebtorOrCreditor()).equals(isNullReturnEmpty(((FadCertificateDetail) fadCertificateDetailList.get(y1)).getDebtorOrCreditor())))
                                                        && (isNullReturnEmpty(((FadCertificateDetailDto) fadCertificateDetailLst2.get(y)).getSubjectCode()).equals(isNullReturnEmpty(((FadCertificateDetail) fadCertificateDetailList.get(y1)).getSubjectCode())))
                                                ) {
                                            fadCertificateDetailLst2.remove(y);
                                            break;
                                        }
                                    }
                                }
                                if (isNullReturnEmpty(fadCertificateDetailBubbleUuid).length() > 0) {
                                    certificateDetailSetVoid(fadCertificateDetailBubble, fadCertificateDetailLst2s, fadCertificateAccountLst3s, 1);
                                } else {
                                    for (int y = 0; y < fadCertificateDetailLst2.size(); y++) {
                                        if (
                                                (isNullReturnEmpty(((FadCertificateDetailDto) fadCertificateDetailLst2.get(y)).getDebtorOrCreditor()).equals(isNullReturnEmpty(((FadCertificateDetail) fadCertificateDetailList.get(y2)).getDebtorOrCreditor())))
                                                        && (isNullReturnEmpty(((FadCertificateDetailDto) fadCertificateDetailLst2.get(y)).getSubjectCode()).equals(isNullReturnEmpty(((FadCertificateDetail) fadCertificateDetailList.get(y2)).getSubjectCode())))
                                                ) {
                                            fadCertificateDetailLst2.remove(y);
                                            break;
                                        }
                                    }
                                }

                                //子表凭证摘要重新取得
                                //fadCertificateDetailList = isNullReturnEmpty(fadCertificateUuid).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertificateDetail.class, fadCertificateDetailConditionsMap, "seq_no asc") : new ArrayList<>();
                                //fadCertificateDetailListSize = fadCertificateDetailList.size();
                                fadCertificateDetailList.remove(y1);
                                if (y1 > y2) {
                                    fadCertificateDetailList.remove(y2);
                                } else {
                                    fadCertificateDetailList.remove(y2 - 1);
                                }
                                fadCertificateDetailList.add(fadCertificateDetailInsert);
                                fadCertificateDetailListSize = fadCertificateDetailList.size();
                                y1 = -1;
                                break;
                            }
                        }
                    } else if (
                            (y1 != y2)
                                    && (!(isNullReturnEmpty(fadCertificateDetail.getDebtorOrCreditor()).equals(isNullReturnEmpty(fadCertificateDetailBubble.getDebtorOrCreditor()))))
                                    && (isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).equals(isNullReturnEmpty(fadCertificateDetailBubble.getSubjectCode())))
                                    && (!(isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("12.2")))
                            ) {
                        throw new BizException("合并凭证失败!(合并后的凭证存在同一科目同时出现借贷方不能合并)");
                    }
                }
            }
            DtoHelper.batchUpdate(fadCertificateDetailLst2s, FadCertificateDetail.class);
            DtoHelper.batchUpdate(fadCertificateAccountLst3s, FadCertificateAccount.class);
            DtoHelper.batchAdd(fadCertificateDetailLst2, FadCertificateDetail.class);
            certificateDetailSetSon(fadCertificateDetailLst2);

            fadCertificateDetailLst2 = isNullReturnEmpty(fadCertificateUuid).length() > 0 ? DtoHelper.findByAnyFields(FadCertificateDetailDto.class, fadCertificateDetailConditionsMap, "seq_no asc") : new ArrayList<>();
            int debtor = 0;
            int creditor = 0;
            String CertificateType = "转账凭证";
            for (int y = 0; y < fadCertificateDetailLst2.size(); y++) {
                FadCertificateDetail fadCertificateDetail = (FadCertificateDetail) fadCertificateDetailLst2.get(y);
                fadCertificateDetail.setOrderNo(y + 1);
                fadCertificateDetail.setSeqNo(y + 1);
                if (isNullReturnEmpty(fadCertificateDetail.getDebtorOrCreditor()).equals("0")) {
                    debtor = debtor + 1;
                } else {
                    creditor = creditor + 1;
                }
                if ((isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).indexOf("1002")) == 0) {
                    CertificateType = "银行凭证";
                } else if (
                        ((isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).indexOf("1001")) == 0)
                                && (!(CertificateType.equals("银行凭证")))
                        ) {
                    CertificateType = "现金凭证";
                }
            }
            if (
                    ((debtor > 1) && (creditor > 1))
                            && (!(CertificateType.equals("转账凭证")))
                    ) {
                throw new BizException("合并凭证失败!(合并后的【" + CertificateType + "】存在多借多贷不能合并)");
            } else {
                if (!(CertificateType.equals("转账凭证"))) {
                    if (isNullReturnEmpty(fadCertificate.getReceiverOrPayerName()).split(MergeSplitSymbol).length > 1) {
                        throw new BizException("合并凭证失败!(合并后的【" + CertificateType + "】存在多个【对方单位名称】不能合并)");
                    }
                } else {
                    fadCertificate.setReceiverOrPayerName(isNullReturnEmpty(fadCertificate.getReceiverOrPayerName()).split(MergeSplitSymbol)[0]);
                    List fadCertificateLst1 = new ArrayList<>();
                    fadCertificateLst1.add(fadCertificate);
                    DtoHelper.batchUpdate(fadCertificateLst1, FadCertificate.class);
                }
                certificateDetailSetOrder(fadCertificateDetailLst2);
                DtoHelper.batchUpdate(fadCertificateDetailLst2, FadCertificateDetail.class);
            }
        } else {
            String errMessage = "记录不存在!";
            errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
            returnValue = errMessages;
        }
        return returnValue;
    }

    @Override
    public void certificateCheckMoney(FadCertificateDto fadCertificate) {

        //合并原凭证借贷金额总和
        BigDecimal debtor = new BigDecimal("0");
        BigDecimal creditor = new BigDecimal("0");

        //凭证合并拆分关系取得
        Map<String, Object> fadCertifiMergeRelConditionsMap = new HashMap<String, Object>();
        fadCertifiMergeRelConditionsMap.put(FadCertifiMergeSplitRelAttribute.MERGE_ID, isNullReturnEmpty(fadCertificate.getUuid()));
        List<FadCertifiMergeSplitRel> fadCertifiMergeRelList = isNullReturnEmpty(fadCertificate.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiMergeSplitRel.class, fadCertifiMergeRelConditionsMap, "seq_no asc") : new ArrayList<>();
        for (int i = 0; i < fadCertifiMergeRelList.size(); i++) {
            FadCertifiMergeSplitRel fadCertifiMergeRel = (FadCertifiMergeSplitRel) fadCertifiMergeRelList.get(i);
            String fadCertifiMergeRelSplitId = isNullReturnEmpty(fadCertifiMergeRel.getSplitId());

            FadCertificate fadCertificateSplit = isNullReturnEmpty(fadCertifiMergeRelSplitId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadCertificate.class, fadCertifiMergeRelSplitId) : null;
            if (fadCertificateSplit != null) {
                debtor = debtor.add(new BigDecimal(isEmptyReturnZero(isNullReturnEmpty(fadCertificateSplit.getDebtor()))));
                creditor = creditor.add(new BigDecimal(isEmptyReturnZero(isNullReturnEmpty(fadCertificateSplit.getCreditor()))));
            }
        }
        if (fadCertifiMergeRelList.size() > 0) {
            if ((!(fadCertificate.getDebtor().compareTo(debtor) == 0)) || (!(fadCertificate.getCreditor().compareTo(creditor) == 0))) {
                throw new BizException("当前合并凭证金额与原凭证金额总和不符,可参照【原凭证金额总和】对借贷金额做适当调整!(当前合并凭证金额:[借方:" + fadCertificate.getDebtor() + ",贷方:" + fadCertificate.getCreditor() + "],原凭证金额总和:[借方:" + debtor + ",贷方:" + creditor + "])");
            }
        }

        //拆分原凭证借贷金额
        debtor = new BigDecimal("0");
        creditor = new BigDecimal("0");

        //拆分凭证借贷金额总和
        BigDecimal debtorAll = new BigDecimal("0");
        BigDecimal creditorAll = new BigDecimal("0");

        //凭证合并拆分关系取得
        Map<String, Object> fadCertifiSplitRelConditionsMap = new HashMap<String, Object>();
        fadCertifiSplitRelConditionsMap.put(FadCertifiMergeSplitRelAttribute.SPLIT_ID, isNullReturnEmpty(fadCertificate.getUuid()));
        List<FadCertifiMergeSplitRel> fadCertifiSplitRelList = isNullReturnEmpty(fadCertificate.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiMergeSplitRel.class, fadCertifiSplitRelConditionsMap, "seq_no asc") : new ArrayList<>();
        for (int i = 0; i < fadCertifiSplitRelList.size(); i++) {
            FadCertifiMergeSplitRel fadCertifiSplitRel = (FadCertifiMergeSplitRel) fadCertifiSplitRelList.get(i);
            String fadCertifiSplitRelMergeId = isNullReturnEmpty(fadCertifiSplitRel.getMergeId());
            String fadCertifiSplitRelBatId = isNullReturnEmpty(fadCertifiSplitRel.getBatId());

            FadCertificate fadCertificateMerge = isNullReturnEmpty(fadCertifiSplitRelMergeId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadCertificate.class, fadCertifiSplitRelMergeId) : null;
            if (fadCertificateMerge != null) {
                debtor = debtor.add(new BigDecimal(isEmptyReturnZero(isNullReturnEmpty(fadCertificateMerge.getDebtor()))));
                creditor = creditor.add(new BigDecimal(isEmptyReturnZero(isNullReturnEmpty(fadCertificateMerge.getCreditor()))));
            }

            //凭证合并拆分关系取得
            fadCertifiMergeRelConditionsMap = new HashMap<String, Object>();
            fadCertifiMergeRelConditionsMap.put(FadCertifiMergeSplitRelAttribute.MERGE_ID, fadCertifiSplitRelMergeId);
            fadCertifiMergeRelConditionsMap.put(FadCertifiMergeSplitRelAttribute.BAT_ID, fadCertifiSplitRelBatId);
            fadCertifiMergeRelList = ((isNullReturnEmpty(fadCertifiSplitRelMergeId).length() > 0) && (isNullReturnEmpty(fadCertifiSplitRelBatId).length() > 0)) ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiMergeSplitRel.class, fadCertifiMergeRelConditionsMap, "seq_no asc") : new ArrayList<>();
            for (int y = 0; y < fadCertifiMergeRelList.size(); y++) {
                FadCertifiMergeSplitRel fadCertifiMergeRel = (FadCertifiMergeSplitRel) fadCertifiMergeRelList.get(y);
                String fadCertifiMergeRelSplitId = isNullReturnEmpty(fadCertifiMergeRel.getSplitId());

                FadCertificate fadCertificateSplit = isNullReturnEmpty(fadCertifiMergeRelSplitId).length() > 0 ? PersistenceFactory.getInstance().findByPK(FadCertificate.class, fadCertifiMergeRelSplitId) : null;
                if (fadCertificateSplit != null) {
                    debtorAll = debtorAll.add(new BigDecimal(isEmptyReturnZero(isNullReturnEmpty(fadCertificateSplit.getDebtor()))));
                    creditorAll = creditorAll.add(new BigDecimal(isEmptyReturnZero(isNullReturnEmpty(fadCertificateSplit.getCreditor()))));
                }
            }
            break;
        }
        if (fadCertifiSplitRelList.size() > 0) {
            if ((!(debtorAll.compareTo(debtor) == 0)) || (!(creditorAll.compareTo(creditor) == 0))) {
                throw new BizException("当前拆分凭证金额总和与原凭证金额不符,可参照【原凭证金额】对借贷金额做适当调整!(当前拆分凭证金额总和:[借方:" + debtorAll + ",贷方:" + creditorAll + "],原凭证金额:[借方:" + debtor + ",贷方:" + creditor + "])");
            }
        }
    }

    @Override
    public void certificateCheckTimeStamp(String fadCertificateUuid, String tblVersion) {
        try {
            if (isNullReturnEmpty(fadCertificateUuid).length() > 0 && isNullReturnEmpty(tblVersion).length() > 0) {
                String sql = "UPDATE FAD_CERTIFICATE SET TBL_VERSION = '" + UUIDUtil.getUUID() + "' WHERE UUID = '" + fadCertificateUuid + "' AND TBL_VERSION = '" + tblVersion + "'";
                DAOMeta daoMeta = new DAOMeta();
                daoMeta.setStrSql(sql);
                daoMeta.setNeedFilter(false);
                if (!(PersistenceFactory.getInstance().updateByNativeSQL(daoMeta) > 0)) {
                    throw new BizException("凭证时间戳验证:凭证过期(已被其他用户修改过),操作无效,请刷新后重新尝试!");
                }
            } else {
                throw new BizException("凭证时间戳验证:初始条件设定不足!");
            }
        } catch (Exception e) {
            throw new BizException(isNullReturnEmpty(((BizException) e).getExceptionKey()));
        }
    }

    @Override
    public void CheckTimeStamp(String tableName, String businessName, String uuid, String tblVersion) {
        try {
            if (isNullReturnEmpty(tableName).length() > 0 && isNullReturnEmpty(businessName).length() > 0 && isNullReturnEmpty(uuid).length() > 0 && isNullReturnEmpty(tblVersion).length() > 0) {
                String sql = "UPDATE " + tableName + " SET TBL_VERSION = '" + UUIDUtil.getUUID() + "' WHERE UUID = '" + uuid + "' AND TBL_VERSION = '" + tblVersion + "'";
                DAOMeta daoMeta = new DAOMeta();
                daoMeta.setStrSql(sql);
                daoMeta.setNeedFilter(false);
                if (!(PersistenceFactory.getInstance().updateByNativeSQL(daoMeta) > 0)) {
                    throw new BizException(businessName + "时间戳验证:" + businessName + "过期(已被其他用户修改过),操作无效,请刷新后重新尝试!");
                }
            } else {
                throw new BizException(businessName + "时间戳验证:初始条件设定不足!");
            }
        } catch (Exception e) {
            throw new BizException(isNullReturnEmpty(((BizException) e).getExceptionKey()));
        }
    }

    @Override
    public String certificateDeficit(String fadCertificateUuid) {
        String returnValue = "OK";

        //Err
        String errMessages = "";

        //主表凭证取得
        String fadCertificateInsertUuid = "";
        FadCertificateDto fadCertificate = isNullReturnEmpty(fadCertificateUuid).length() > 0 ? (FadCertificateDto) DtoHelper.findDtoByPK(FadCertificateDto.class, fadCertificateUuid) : null;
        if (fadCertificate != null) {

            String sql = "SELECT 1 FROM FAD_CERTIFICATE WHERE FREE2 = '" + isNullReturnEmpty(fadCertificate.getUuid()) + "' AND STATE IN ('0', '1')";
            DAOMeta daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                throw new BizException("【已复制凭证】不能红冲!");
            }

            sql = "SELECT 1 FROM FAD_CERTIFICATE WHERE UUID = '" + isNullReturnEmpty(fadCertificate.getUuid()) + "' AND FREE2 IS NOT NULL";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                throw new BizException("【复制凭证】不能红冲!");
            }

            sql = "SELECT 1 FROM FAD_CERTIFICATE WHERE UUID = '" + isNullReturnEmpty(fadCertificate.getUuid()) + "' AND STATE = '1'";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            if (!(PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0)) {
                throw new BizException("【未发送凭证】不能红冲!");
            }

            //凭证冲红关系取得
            Map<String, Object> fadCertifiCertifiRelConditionsMap = new HashMap<String, Object>();
            fadCertifiCertifiRelConditionsMap.put(FadCertifiDeficitRelAttribute.CERTIFI_ID, isNullReturnEmpty(fadCertificate.getUuid()));
            List<FadCertifiDeficitRel> fadCertifiCertifiRelList = isNullReturnEmpty(fadCertificate.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiDeficitRel.class, fadCertifiCertifiRelConditionsMap, "seq_no asc") : new ArrayList<>();
            if (fadCertifiCertifiRelList.size() > 0) {
                throw new BizException("【已红冲凭证】不能再次红冲!");
            }

            //凭证红冲关系取得
            Map<String, Object> fadCertifiDeficitRelConditionsMap = new HashMap<String, Object>();
            fadCertifiDeficitRelConditionsMap.put(FadCertifiDeficitRelAttribute.DEFICIT_ID, isNullReturnEmpty(fadCertificate.getUuid()));
            List<FadCertifiDeficitRel> fadCertifiDeficitRelList = isNullReturnEmpty(fadCertificate.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiDeficitRel.class, fadCertifiDeficitRelConditionsMap, "seq_no asc") : new ArrayList<>();
            if (fadCertifiDeficitRelList.size() > 0) {
                throw new BizException("【红冲凭证】不能红冲!");
            }

            FadCertificate fadCertificateInsert = BeanFactory.getObject(FadCertificate.class);
            BeanUtil.bean2Bean(fadCertificate, fadCertificateInsert);
            fadCertificateInsert.setUuid(null);
            fadCertificateInsert.setDebtor(new BigDecimal(isEmptyReturnZero(isNullReturnEmpty(fadCertificate.getDebtor()))).multiply(new BigDecimal(-1)));
            fadCertificateInsert.setCreditor(new BigDecimal(isEmptyReturnZero(isNullReturnEmpty(fadCertificate.getCreditor()))).multiply(new BigDecimal(-1)));
            fadCertificateInsert.setState("0");
            fadCertificateInsert.setCertificateNo(null);
            fadCertificateInsert.setFeedback(null);
            fadCertificateInsert.setNcrequestUrl(null);
            fadCertificateInsert.setNcrequestXml(null);
            fadCertificateInsert.setNcresponseXml(null);
            fadCertificateInsert.setMaker(null);
            fadCertificateInsert.setMakeDate(null);
            PersistenceFactory.getInstance().insert(fadCertificateInsert);
            fadCertificateInsertUuid = isNullReturnEmpty(fadCertificateInsert.getUuid());

            FadCertifiDeficitRel fadCertifiDeficitRel = BeanFactory.getObject(FadCertifiDeficitRel.class);
            fadCertifiDeficitRel.setCertifiId(fadCertificateUuid);
            fadCertifiDeficitRel.setDeficitId(fadCertificateInsertUuid);
            PersistenceFactory.getInstance().insert(fadCertifiDeficitRel);

            //子表凭证摘要取得
            List fadCertificateDetailLst2 = new ArrayList<>();
            List<FadCertificateDetailDto> fadCertificateDetailList = fadCertificate.getFadCertificateDetailDto() != null ? fadCertificate.getFadCertificateDetailDto() : new ArrayList<>();
            for (int y = 0; y < fadCertificateDetailList.size(); y++) {
                FadCertificateDetailDto fadCertificateDetail = fadCertificateDetailList.get(y);
                String fadCertificateDetailUuid = fadCertificateDetail.getUuid();
                FadCertificateDetailDto fadCertificateDetailInsert = BeanFactory.getObject(FadCertificateDetailDto.class);
                BeanUtil.bean2Bean(fadCertificateDetail, fadCertificateDetailInsert);
                fadCertificateDetailInsert.setUuid(null);
                fadCertificateDetailInsert.setFadCertificateId(fadCertificateInsertUuid);
                fadCertificateDetailInsert.setPrimary(new BigDecimal(isEmptyReturnZero(isNullReturnEmpty(fadCertificateDetail.getPrimary()))).multiply(new BigDecimal(-1)));
                fadCertificateDetailInsert.setLocal(new BigDecimal(isEmptyReturnZero(isNullReturnEmpty(fadCertificateDetail.getLocal()))).multiply(new BigDecimal(-1)));
                fadCertificateDetailLst2.add(fadCertificateDetailInsert);
                String fadCertificateDetailInsertUuid = isNullReturnEmpty(fadCertificateDetailInsert.getUuid());

                //分录辅助核算取得
                List<FadCertificateAccountDto> fadCertificateAccountLst3 = new ArrayList<>();
                List<FadCertificateAccountDto> fadCertificateAccountList = fadCertificateDetail.getFadCertificateAccountDto() != null ? fadCertificateDetail.getFadCertificateAccountDto() : new ArrayList<>();
                for (int z = 0; z < fadCertificateAccountList.size(); z++) {
                    FadCertificateAccountDto fadCertificateAccount = fadCertificateAccountList.get(z);
                    String fadCertificateAccountUuid = isNullReturnEmpty(fadCertificateAccount.getUuid());
                    FadCertificateAccountDto fadCertificateAccountInsert = BeanFactory.getObject(FadCertificateAccountDto.class);
                    BeanUtil.bean2Bean(fadCertificateAccount, fadCertificateAccountInsert);
                    fadCertificateAccountInsert.setUuid(null);
                    fadCertificateAccountInsert.setFadCertificateDetailId(fadCertificateDetailInsertUuid);
                    fadCertificateAccountLst3.add(fadCertificateAccountInsert);
                    String fadCertificateAccountInsertUuid = isNullReturnEmpty(fadCertificateAccountInsert.getUuid());
                }
                fadCertificateDetailInsert.setFadCertificateAccountDto(fadCertificateAccountLst3);
            }
            DtoHelper.batchAdd(fadCertificateDetailLst2, FadCertificateDetail.class);
            certificateDetailSetSon(fadCertificateDetailLst2);
        } else {
            String errMessage = "记录不存在!";
            errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
            returnValue = errMessages;
        }

        return returnValue;
    }

    @Override
    public Map certificateSendNC(String fadCertificateUuid, String makerUuid) {
        Map result = new HashMap<>();

        //Err
        String errMessages = "";

        Date makeDate = new Date();
        SimpleDateFormat makeDateToString = new SimpleDateFormat("yyyy-MM-dd");
        String makeDateS = makeDateToString.format(makeDate);

        //主表凭证取得
        FadCertificateDto fadCertificate = isNullReturnEmpty(fadCertificateUuid).length() > 0 ? (FadCertificateDto) DtoHelper.findDtoByPK(FadCertificateDto.class, fadCertificateUuid) : null;
        if (fadCertificate != null) {
            if (isNullReturnEmpty(fadCertificate.getState()).equals("1")) {
                makeDate = fadCertificate.getMakeDate();
                makeDateS = makeDateToString.format(makeDate);
            }

            //验证合并拆分凭证
            certificateCheckMoney(fadCertificate);

            //凭证冲红关系取得
            Map<String, Object> fadCertifiCertifiRelConditionsMap = new HashMap<String, Object>();
            fadCertifiCertifiRelConditionsMap.put(FadCertifiDeficitRelAttribute.CERTIFI_ID, isNullReturnEmpty(fadCertificate.getUuid()));
            List<FadCertifiDeficitRel> fadCertifiCertifiRelList = isNullReturnEmpty(fadCertificate.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiDeficitRel.class, fadCertifiCertifiRelConditionsMap, "seq_no asc") : new ArrayList<>();
            if (fadCertifiCertifiRelList.size() > 0) {
                String errMessage = "【已红冲凭证】,不能再次发送保存!";
                errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                result.put("errMessages", errMessages);
                return result;
            }

            //凭证红冲关系取得
            Map<String, Object> fadCertifiDeficitRelConditionsMap = new HashMap<String, Object>();
            fadCertifiDeficitRelConditionsMap.put(FadCertifiDeficitRelAttribute.DEFICIT_ID, isNullReturnEmpty(fadCertificate.getUuid()));
            List<FadCertifiDeficitRel> fadCertifiDeficitRelList = isNullReturnEmpty(fadCertificate.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiDeficitRel.class, fadCertifiDeficitRelConditionsMap, "seq_no asc") : new ArrayList<>();

            if (!(fadCertifiDeficitRelList.size() > 0)) {
                if (isNullReturnEmpty(fadCertificate.getOriginalFormCode()).length() > 0) {
                    String[] originalFormCodes = isNullReturnEmpty(fadCertificate.getOriginalFormCode()).split(MergeSplitSymbol);

                    //验证子表凭证摘要分录的相关请款单是否发送NC
                    if (
                            (isNullReturnEmpty(fadCertificate.getOriginalFormCode()).indexOf("(请款流水号)") != -1)
                                    &&
                                    (
                                            (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("5"))
                                                    ||
                                                    (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("7"))
                                                    ||
                                                    (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("11"))
                                    )
                            ) {
                        for (int ii = 0; ii < originalFormCodes.length; ii++) {
                            String originalFormCode = isNullReturnEmpty(originalFormCodes[ii]);
                            if (originalFormCode.indexOf("(请款流水号)") != -1) {
                                String sql = "SELECT 1 FROM FAD_CERTIFICATE WHERE ORIGINAL_FORM_CODE like '%" + originalFormCode + "%' AND ORIGINAL_FORM_TYPE IN ('1','2','3') AND STATE = '0' AND NVL(IS_VOID, 0) <> 1";
                                DAOMeta daoMeta = new DAOMeta();
                                daoMeta.setStrSql(sql);
                                daoMeta.setNeedFilter(false);
                                if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                                    String errMessage = "【当前凭证】相关的【请款凭证】尚未发送NC,请先将相关【请款凭证】发送NC!";
                                    errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                                }
                            }
                        }
                    } else if (
                            (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("8"))
                                    ||
                                    (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("9"))
                            ) {
                        for (int ii = 0; ii < originalFormCodes.length; ii++) {
                            String originalFormCode = isNullReturnEmpty(originalFormCodes[ii]);
                            if (originalFormCode.indexOf("(发票黏贴单号)") != -1) {
                                String sql = "SELECT 1 FROM FAD_CERTIFICATE WHERE ORIGINAL_FORM_CODE like '%" + originalFormCode + "%' AND ORIGINAL_FORM_TYPE = '6' AND STATE = '0' AND NVL(IS_VOID, 0) <> 1";
                                DAOMeta daoMeta = new DAOMeta();
                                daoMeta.setStrSql(sql);
                                daoMeta.setNeedFilter(false);
                                if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                                    String errMessage = "【当前凭证】相关的【6.无请款采购合同报销】凭证尚未发送NC,请先将相关【6.无请款采购合同报销】凭证发送NC!";
                                    errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                                } else {
                                    sql = "SELECT 1 FROM FAD_CERTIFICATE WHERE ORIGINAL_FORM_CODE like '%" + originalFormCode + "%' AND ORIGINAL_FORM_TYPE = '7' AND STATE = '0' AND NVL(IS_VOID, 0) <> 1";
                                    daoMeta = new DAOMeta();
                                    daoMeta.setStrSql(sql);
                                    daoMeta.setNeedFilter(false);
                                    if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                                        String errMessage = "【当前凭证】相关的【7.有请款采购合同报销】凭证尚未发送NC,请先将相关【7.有请款采购合同报销】凭证发送NC!";
                                        errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                                    }
                                }
                            }
                        }
                    } else if (
                            isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("5.1")
                            ) {
                        for (int ii = 0; ii < originalFormCodes.length; ii++) {
                            String originalFormCode = isNullReturnEmpty(originalFormCodes[ii]);
                            if (originalFormCode.indexOf("(发票黏贴单号)") != -1) {
                                String sql = "SELECT 1 FROM FAD_CERTIFICATE WHERE ORIGINAL_FORM_CODE like '%" + originalFormCode + "%' AND ORIGINAL_FORM_TYPE = '5' AND STATE = '0' AND NVL(IS_VOID, 0) <> 1";
                                DAOMeta daoMeta = new DAOMeta();
                                daoMeta.setStrSql(sql);
                                daoMeta.setNeedFilter(false);
                                if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                                    String errMessage = "【当前凭证】相关的【5.有请款报销】凭证尚未发送NC,请先将相关【5.有请款报销】凭证发送NC!";
                                    errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                                }
                            }
                        }
                    }
                }
            }

            //员工信息取得
            ScdpUser scdpUser = isNullReturnEmpty(makerUuid).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScdpUser.class, makerUuid) : null;
            if (scdpUser != null) {
                String userId = isNullReturnEmpty(scdpUser.getUserId());
                String userName = isNullReturnEmpty(scdpUser.getUserName());

                String xmlFadCertificatePostXMLData = WSFadCertificateXML;
                String xmlFadCertificateDetailPostXMLData = "";
                String xmlFadCertificateAccountPostXMLData = "";

                xmlFadCertificatePostXMLData = xmlFadCertificatePostXMLData.replace("[$fad_certificate_uuid]", isNullReturnEmpty(fadCertificate.getUuid()));
                xmlFadCertificatePostXMLData = xmlFadCertificatePostXMLData.replace("[$fiscal_year]", isNullReturnEmpty(fadCertificate.getYears()));
                xmlFadCertificatePostXMLData = xmlFadCertificatePostXMLData.replace("[$accounting_period]", isNullReturnEmpty(fadCertificate.getMonths()));
                xmlFadCertificatePostXMLData = xmlFadCertificatePostXMLData.replace("[$attachment_number]", isEmptyReturnZero(isNullReturnEmpty(fadCertificate.getAttachmentNumber())));

                if (Integer.parseInt(makeDateS.substring(0, 4) + makeDateS.substring(5, 7)) > Integer.parseInt(fadCertificate.getYears() + fadCertificate.getMonths())) {
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR, Integer.parseInt(fadCertificate.getYears()));
                    cal.set(Calendar.MONTH, Integer.parseInt(fadCertificate.getMonths()) - 1);
                    makeDateS = (fadCertificate.getYears() + "-" + fadCertificate.getMonths() + "-" + String.valueOf(cal.getActualMaximum(Calendar.DAY_OF_MONTH)));
                } else if (Integer.parseInt(makeDateS.substring(0, 4) + makeDateS.substring(5, 7)) < Integer.parseInt(fadCertificate.getYears() + fadCertificate.getMonths())) {
                    String feedback = "【会计年月】不许晚于【制单日期】的【年月】";
                    fadCertificate.setFeedback(feedback);
                    fadCertificate.setMaker(userId);
                    fadCertificate.setMakeDate(makeDate);

                    List fadCertificateList = new ArrayList<>();
                    fadCertificateList.add(fadCertificate);
                    DtoHelper.batchUpdate(fadCertificateList, FadCertificate.class);

                    String errMessage = feedback;
                    errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                    result.put("errMessages", errMessages);

                    result.put("feedback", feedback);
                    result.put("maker", userId);
                    result.put("makerName", userName);
                    result.put("makeDate", makeDate);
                    return result;
                }

                xmlFadCertificatePostXMLData = xmlFadCertificatePostXMLData.replace("[$prepareddate]", makeDateS);

                String enter = isNullReturnEmpty(ErpExpandFieldHelper.getExpandFieldValue("USER", makerUuid, "ncCode"));
                if (!(enter.length() > 0)) {
                    String errMessage = "【凭证抬头】【制单人】的【NCCODE】不能为空!";
                    errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                }

                xmlFadCertificatePostXMLData = xmlFadCertificatePostXMLData.replace("[$enter]", enter);
                xmlFadCertificatePostXMLData = xmlFadCertificatePostXMLData.replace("[$cashier]", "");
                xmlFadCertificatePostXMLData = xmlFadCertificatePostXMLData.replace("[$signature]", "N");
                xmlFadCertificatePostXMLData = xmlFadCertificatePostXMLData.replace("[$checker]", "");
                xmlFadCertificatePostXMLData = xmlFadCertificatePostXMLData.replace("[$posting_date]", "");
                xmlFadCertificatePostXMLData = xmlFadCertificatePostXMLData.replace("[$posting_person]", "");
                //xmlFadCertificatePostXMLData = xmlFadCertificatePostXMLData.replace("[$memo1]", sendNcStringFilter(fadCertificate.getAbstracts()));
                xmlFadCertificatePostXMLData = xmlFadCertificatePostXMLData.replace("[$memo2]", sendNcStringFilter(fadCertificate.getRemark()));

                String receiverOrPayerType = isNullReturnEmpty(fadCertificate.getReceiverOrPayerType());
                String receiverOrPayerId = isNullReturnEmpty(fadCertificate.getReceiverOrPayerId());
                String receiverOrPayerCode = isNullReturnEmpty(fadCertificate.getReceiverOrPayerCode());
                String receiverOrPayerName = isNullReturnEmpty(fadCertificate.getReceiverOrPayerName());
                String reserve1 = "";
                String reserve2 = "";

                //对方类型为SCDP_USER用户
                if (isNullReturnEmpty(receiverOrPayerType).equals("SCDP_USER")) {
                    reserve1 = "3999999999";
                    reserve2 = receiverOrPayerName;
                }

                //对方类型为SCM_SUPPLIER供应商
                else if (isNullReturnEmpty(receiverOrPayerType).equals("SCM_SUPPLIER")) {
                    if (isUuidBelongNcCode(receiverOrPayerId)) {
                        reserve1 = receiverOrPayerCode;
                    } else {
                        ScmSupplier scmSupplier = isNullReturnEmpty(receiverOrPayerId).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmSupplier.class, receiverOrPayerId) : null;
                        if (scmSupplier != null) {
                            reserve1 = isNullReturnEmpty(scmSupplier.getNcCode());
                        } else {
                            Map<String, Object> scmSupplierConditionsMap = new HashMap<String, Object>();
                            scmSupplierConditionsMap.put(ScmSupplierAttribute.COMPLETE_NAME, receiverOrPayerName);
                            List<ScmSupplier> scmSupplierList = isNullReturnEmpty(receiverOrPayerName).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScmSupplier.class, scmSupplierConditionsMap, null) : new ArrayList<>();
                            if (scmSupplierList.size() > 0) {
                                reserve1 = isNullReturnEmpty(((ScmSupplier) scmSupplierList.get(0)).getNcCode());
                            }
                        }
                    }
                    if (reserve1.length() > 0) {
                        reserve2 = receiverOrPayerName;
                    } else {
                        String errMessage = "【凭证抬头】【对方单位名称】【供应商】的【NCCODE】不能为空!";
                        errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                    }
                }

                //对方类型为PRM_CUSTOMER客户
                else if (isNullReturnEmpty(receiverOrPayerType).equals("PRM_CUSTOMER")) {
                    if (isUuidBelongNcCode(receiverOrPayerId)) {
                        reserve1 = receiverOrPayerCode;
                    } else {
                        PrmCustomer prmCustomer = isNullReturnEmpty(receiverOrPayerId).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmCustomer.class, receiverOrPayerId) : null;
                        if (prmCustomer != null) {
                            reserve1 = isNullReturnEmpty(prmCustomer.getNcCode());
                        }
                    }
                    if (reserve1.length() > 0) {
                        reserve2 = receiverOrPayerName;
                    } else {
                        String errMessage = "【凭证抬头】【对方单位名称】【客户】的【NCCODE】不能为空!";
                        errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                    }
                } else {
                    String errMessage = "【凭证抬头】【对方单位名称】不能为空!";
                    errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                }

                //子表凭证摘要取得
                List<FadCertificateDetailDto> fadCertificateDetailList = fadCertificate.getFadCertificateDetailDto() != null ? fadCertificate.getFadCertificateDetailDto() : new ArrayList<>();
                for (int i1 = 0; i1 < fadCertificateDetailList.size(); i1++) {
                    for (int i2 = i1 + 1; i2 < fadCertificateDetailList.size(); i2++) {
                        FadCertificateDetailDto fadCertificateDetail1 = fadCertificateDetailList.get(i1);
                        FadCertificateDetailDto fadCertificateDetail2 = fadCertificateDetailList.get(i2);
                        if (fadCertificateDetail1.getOrderNo() > fadCertificateDetail2.getOrderNo()) {
                            fadCertificateDetailList.set(i1, fadCertificateDetail2);
                            fadCertificateDetailList.set(i2, fadCertificateDetail1);
                        }
                    }
                }
                int debtor = 0;
                int creditor = 0;
                String CertificateType = "转账凭证";
                for (int y = 0; y < fadCertificateDetailList.size(); y++) {
                    FadCertificateDetailDto fadCertificateDetail = fadCertificateDetailList.get(y);
                    if (y == 0) {
                        xmlFadCertificatePostXMLData = xmlFadCertificatePostXMLData.replace("[$memo1]", sendNcStringFilter(fadCertificateDetail.getAbstracts()));
                    }
                    if (isNullReturnEmpty(fadCertificateDetail.getDebtorOrCreditor()).equals("0")) {
                        debtor = debtor + 1;
                    } else {
                        creditor = creditor + 1;
                    }
                    if ((isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).indexOf("1002")) == 0) {
                        CertificateType = "银行凭证";
                    } else if (
                            ((isNullReturnEmpty(fadCertificateDetail.getSubjectCode()).indexOf("1001")) == 0)
                                    && (!(CertificateType.equals("银行凭证")))
                            ) {
                        CertificateType = "现金凭证";
                    }

                    xmlFadCertificateDetailPostXMLData = xmlFadCertificateDetailPostXMLData + WSFadCertificateDetailXML;

                    xmlFadCertificateDetailPostXMLData = xmlFadCertificateDetailPostXMLData.replace("[$entry_id]", isNullReturnEmpty(fadCertificateDetail.getOrderNo()));

                    xmlFadCertificateDetailPostXMLData = xmlFadCertificateDetailPostXMLData.replace("[$account_code]", isNullReturnEmpty(fadCertificateDetail.getSubjectCode()));

                    if (sendNcStringFilter(fadCertificateDetail.getAbstracts()).length() > 0) {
                        xmlFadCertificateDetailPostXMLData = xmlFadCertificateDetailPostXMLData.replace("[$abstract]", sendNcStringFilter(fadCertificateDetail.getAbstracts()));
                    } else {
                        xmlFadCertificateDetailPostXMLData = xmlFadCertificateDetailPostXMLData.replace("[$abstract]", sendNcStringFilter(fadCertificate.getAbstracts()));
                    }

                    xmlFadCertificateDetailPostXMLData = xmlFadCertificateDetailPostXMLData.replace("[$settlement]", isNullReturnEmpty(fadCertificateDetail.getCheckoutMethod()));
                    xmlFadCertificateDetailPostXMLData = xmlFadCertificateDetailPostXMLData.replace("[$document_id]", isNullReturnEmpty(fadCertificateDetail.getFormNo()));
                    xmlFadCertificateDetailPostXMLData = xmlFadCertificateDetailPostXMLData.replace("[$document_date]", isNullReturnEmpty(fadCertificateDetail.getFormDate()));

                    xmlFadCertificateDetailPostXMLData = xmlFadCertificateDetailPostXMLData.replace("[$currency]", isNullReturnEmpty(fadCertificateDetail.getCurrency()));

                    xmlFadCertificateDetailPostXMLData = xmlFadCertificateDetailPostXMLData.replace("[$unit_price]", "");
                    xmlFadCertificateDetailPostXMLData = xmlFadCertificateDetailPostXMLData.replace("[$exchange_rate1]", "");
                    xmlFadCertificateDetailPostXMLData = xmlFadCertificateDetailPostXMLData.replace("[$exchange_rate2]", "");

                    if ((isNullReturnEmpty(fadCertificateDetail.getDebtorOrCreditor()).equals("借")) || (isNullReturnEmpty(fadCertificateDetail.getDebtorOrCreditor()).equals("0"))) {
                        xmlFadCertificateDetailPostXMLData = xmlFadCertificateDetailPostXMLData.replace("[$debit_quantity]", "");
                        xmlFadCertificateDetailPostXMLData = xmlFadCertificateDetailPostXMLData.replace("[$primary_debit_amount]", isEmptyReturnZero(isNullReturnEmpty(fadCertificateDetail.getPrimary())));
                        xmlFadCertificateDetailPostXMLData = xmlFadCertificateDetailPostXMLData.replace("[$secondary_debit_amount]", "");
                        xmlFadCertificateDetailPostXMLData = xmlFadCertificateDetailPostXMLData.replace("[$natural_debit_currency]", isEmptyReturnZero(isNullReturnEmpty(fadCertificateDetail.getLocal())));
                    } else {
                        xmlFadCertificateDetailPostXMLData = xmlFadCertificateDetailPostXMLData.replace("[$debit_quantity]", "");
                        xmlFadCertificateDetailPostXMLData = xmlFadCertificateDetailPostXMLData.replace("[$primary_debit_amount]", "0");
                        xmlFadCertificateDetailPostXMLData = xmlFadCertificateDetailPostXMLData.replace("[$secondary_debit_amount]", "");
                        xmlFadCertificateDetailPostXMLData = xmlFadCertificateDetailPostXMLData.replace("[$natural_debit_currency]", "0");
                    }
                    if ((isNullReturnEmpty(fadCertificateDetail.getDebtorOrCreditor()).equals("贷")) || (isNullReturnEmpty(fadCertificateDetail.getDebtorOrCreditor()).equals("1"))) {
                        xmlFadCertificateDetailPostXMLData = xmlFadCertificateDetailPostXMLData.replace("[$credit_quantity]", "");
                        xmlFadCertificateDetailPostXMLData = xmlFadCertificateDetailPostXMLData.replace("[$primary_credit_amount]", isEmptyReturnZero(isNullReturnEmpty(fadCertificateDetail.getPrimary())));
                        xmlFadCertificateDetailPostXMLData = xmlFadCertificateDetailPostXMLData.replace("[$secondary_credit_amount]", "");
                        xmlFadCertificateDetailPostXMLData = xmlFadCertificateDetailPostXMLData.replace("[$natural_credit_currency]", isEmptyReturnZero(isNullReturnEmpty(fadCertificateDetail.getLocal())));
                    } else {
                        xmlFadCertificateDetailPostXMLData = xmlFadCertificateDetailPostXMLData.replace("[$credit_quantity]", "");
                        xmlFadCertificateDetailPostXMLData = xmlFadCertificateDetailPostXMLData.replace("[$primary_credit_amount]", "0");
                        xmlFadCertificateDetailPostXMLData = xmlFadCertificateDetailPostXMLData.replace("[$secondary_credit_amount]", "");
                        xmlFadCertificateDetailPostXMLData = xmlFadCertificateDetailPostXMLData.replace("[$natural_credit_currency]", "0");
                    }

                    xmlFadCertificateDetailPostXMLData = xmlFadCertificateDetailPostXMLData.replace("[$bill_type]", "");
                    xmlFadCertificateDetailPostXMLData = xmlFadCertificateDetailPostXMLData.replace("[$bill_id]", "");
                    xmlFadCertificateDetailPostXMLData = xmlFadCertificateDetailPostXMLData.replace("[$bill_date]", "");

                    //分录辅助核算取得
                    List<FadCertificateAccountDto> fadCertificateAccountList = fadCertificateDetail.getFadCertificateAccountDto() != null ? fadCertificateDetail.getFadCertificateAccountDto() : new ArrayList<>();
                    if (!(fadCertificateAccountList.size() > 0)) {
                        xmlFadCertificateAccountPostXMLData = "<auxiliary_accounting/>";
                    } else {
                        xmlFadCertificateAccountPostXMLData = "";
                        for (int z = 0; z < fadCertificateAccountList.size(); z++) {
                            String accountInfoCode = "";
                            String WSFadCertificateAccountXMLToSet = WSFadCertificateAccountXML;

                            if ((z == 0) && (z == (fadCertificateAccountList.size() - 1))) {
                            } else if (z == 0) {
                                WSFadCertificateAccountXMLToSet = WSFadCertificateAccountXMLToSet.replace("</auxiliary_accounting>", "");
                            } else if (z == (fadCertificateAccountList.size() - 1)) {
                                WSFadCertificateAccountXMLToSet = WSFadCertificateAccountXMLToSet.replace("<auxiliary_accounting>", "");
                            } else {
                                WSFadCertificateAccountXMLToSet = WSFadCertificateAccountXMLToSet.replace("</auxiliary_accounting>", "");
                                WSFadCertificateAccountXMLToSet = WSFadCertificateAccountXMLToSet.replace("<auxiliary_accounting>", "");
                            }

                            FadCertificateAccountDto fadCertificateAccount = fadCertificateAccountList.get(z);
                            xmlFadCertificateAccountPostXMLData = xmlFadCertificateAccountPostXMLData + WSFadCertificateAccountXMLToSet;

                            xmlFadCertificateAccountPostXMLData = xmlFadCertificateAccountPostXMLData.replace("[$account_no]", AccountNoNCSet(fadCertificateAccount.getAccountNo()));

                            //辅助核算类型为用户
                            if (isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("1")) {
                                if (isNullReturnEmpty(fadCertificateAccount.getAccountInfoCode()).length() > 0) {
                                    if (isUuidBelongNcCode(fadCertificateAccount.getAccountInfoId())) {
                                        accountInfoCode = SplitAccountInfoCodeForScdpUser(fadCertificateAccount.getAccountInfoCode());
                                    } else {
                                        Map<String, Object> scdpUserConditionsMap = new HashMap<String, Object>();
                                        scdpUserConditionsMap.put(ScdpUserAttribute.USER_ID, SplitAccountInfoCodeForScdpUser(fadCertificateAccount.getAccountInfoCode()));
                                        List<ScdpUser> scdpUserList = SplitAccountInfoCodeForScdpUser(fadCertificateAccount.getAccountInfoCode()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, scdpUserConditionsMap, null) : new ArrayList<>();
                                        if (scdpUserList.size() > 0) {
                                            accountInfoCode = isNullReturnEmpty(ErpExpandFieldHelper.getExpandFieldValue("USER", isNullReturnEmpty(((ScdpUser) scdpUserList.get(0)).getUuid()), "ncNumber"));
                                        }

                                        if (accountInfoCode.length() == 0) {
                                            String errMessage = "【辅助核算项】【" + isNullReturnEmpty(fadCertificateAccount.getAccountType()) + "】的【NCCODE】不能为空!";
                                            errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                                        }
                                    }
                                } else {
                                    String errMessage = "【辅助核算项】【" + isNullReturnEmpty(fadCertificateAccount.getAccountType()) + "】不能为空!";
                                    errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                                }
                            }

                            //辅助核算类型为部门
                            else if (isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("2")) {
                                if (isNullReturnEmpty(fadCertificateAccount.getAccountInfoCode()).length() > 0) {
                                    if (isUuidBelongNcCode(fadCertificateAccount.getAccountInfoId())) {
                                        accountInfoCode = isNullReturnEmpty(fadCertificateAccount.getAccountInfoCode());
                                    } else {
                                        Map<String, Object> scdpOrgConditionsMap = new HashMap<String, Object>();
                                        scdpOrgConditionsMap.put(ScdpOrgAttribute.ORG_CODE, isNullReturnEmpty(fadCertificateAccount.getAccountInfoCode()));
                                        List<ScdpOrg> scdpOrgList = isNullReturnEmpty(fadCertificateAccount.getAccountInfoCode()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpOrg.class, scdpOrgConditionsMap, null) : new ArrayList<>();
                                        if (scdpOrgList.size() > 0) {
                                            accountInfoCode = isNullReturnEmpty(ErpExpandFieldHelper.getExpandFieldValue("ORG", ((ScdpOrg) scdpOrgList.get(0)).getUuid(), "ncCode"));
                                        }

                                        if (accountInfoCode.length() == 0) {
                                            String errMessage = "【辅助核算项】【" + isNullReturnEmpty(fadCertificateAccount.getAccountType()) + "】的【NCCODE】不能为空!";
                                            errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                                        }
                                    }
                                } else {
                                    String errMessage = "【辅助核算项】【" + isNullReturnEmpty(fadCertificateAccount.getAccountType()) + "】不能为空!";
                                    errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                                }
                            }

                            //辅助核算类型为客商、内部客商、外部客商
                            else if (
                                    isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("73")
                                            || isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("NEIBU")
                                            || isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("WAIBU")
                                    ) {
                                if ((isNullReturnEmpty(fadCertificateAccount.getAccountInfoId()).length() > 0) || (isNullReturnEmpty(fadCertificateAccount.getAccountInfoName()).length() > 0 && isNullReturnEmpty(fadCertificate.getReceiverOrPayerType()).equals("SCM_SUPPLIER"))) {
                                    if (isUuidBelongNcCode(fadCertificateAccount.getAccountInfoId())) {
                                        accountInfoCode = isNullReturnEmpty(fadCertificateAccount.getAccountInfoCode());
                                    } else {

                                        //对方类型为SCM_SUPPLIER供应商
                                        if (isNullReturnEmpty(fadCertificate.getReceiverOrPayerType()).equals("SCM_SUPPLIER")) {
                                            ScmSupplier scmSupplier = isNullReturnEmpty(fadCertificateAccount.getAccountInfoId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmSupplier.class, isNullReturnEmpty(fadCertificateAccount.getAccountInfoId())) : null;
                                            if (scmSupplier != null) {
                                                accountInfoCode = isNullReturnEmpty(scmSupplier.getNcCode());
                                            } else {
                                                Map<String, Object> scmSupplierConditionsMap = new HashMap<String, Object>();
                                                scmSupplierConditionsMap.put(ScmSupplierAttribute.COMPLETE_NAME, isNullReturnEmpty(fadCertificateAccount.getAccountInfoName()));
                                                List<ScmSupplier> scmSupplierList = isNullReturnEmpty(fadCertificateAccount.getAccountInfoName()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScmSupplier.class, scmSupplierConditionsMap, null) : new ArrayList<>();
                                                if (scmSupplierList.size() > 0) {
                                                    accountInfoCode = isNullReturnEmpty(((ScmSupplier) scmSupplierList.get(0)).getNcCode());
                                                }
                                            }

                                            if (accountInfoCode.length() == 0) {
                                                String errMessage = "【辅助核算项】【" + isNullReturnEmpty(fadCertificateAccount.getAccountType()) + "】【供应商】的【NCCODE】不能为空!";
                                                errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                                            }
                                        }

                                        //对方类型为PRM_CUSTOMER客户
                                        else if (isNullReturnEmpty(fadCertificate.getReceiverOrPayerType()).equals("PRM_CUSTOMER")) {
                                            PrmCustomer prmCustomer = isNullReturnEmpty(fadCertificateAccount.getAccountInfoId()).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmCustomer.class, isNullReturnEmpty(fadCertificateAccount.getAccountInfoId())) : null;
                                            if (prmCustomer != null) {
                                                accountInfoCode = isNullReturnEmpty(prmCustomer.getNcCode());
                                            }

                                            if (accountInfoCode.length() == 0) {
                                                String errMessage = "【辅助核算项】【" + isNullReturnEmpty(fadCertificateAccount.getAccountType()) + "】【客户】的【NCCODE】不能为空!";
                                                errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                                            }
                                        }
                                    }
                                } else {
                                    if (isNullReturnEmpty(fadCertificate.getReceiverOrPayerType()).equals("SCM_SUPPLIER")) {
                                        String errMessage = "【辅助核算项】【" + isNullReturnEmpty(fadCertificateAccount.getAccountType()) + "】【供应商】不能为空!";
                                        errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                                    } else if (isNullReturnEmpty(fadCertificate.getReceiverOrPayerType()).equals("PRM_CUSTOMER")) {
                                        String errMessage = "【辅助核算项】【" + isNullReturnEmpty(fadCertificateAccount.getAccountType()) + "】【客户】不能为空!";
                                        errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                                    } else {
                                        String errMessage = "【辅助核算项】【" + isNullReturnEmpty(fadCertificateAccount.getAccountType()) + "】不能为空!";
                                        errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                                    }
                                }
                            }

                            //辅助核算类型为科技项目
                            else if (isNullReturnEmpty(fadCertificateAccount.getAccountNo()).equals("JXX_JASS")) {
                                if (
                                        (isNullReturnEmpty(fadCertificateAccount.getAccountInfoCode()).length() > 0)
                                                || (isNullReturnEmpty(fadCertificateAccount.getAccountInfoName()).length() > 0)
                                        ) {
                                    if (isUuidBelongNcCode(fadCertificateAccount.getAccountInfoId())) {
                                        accountInfoCode = isNullReturnEmpty(fadCertificateAccount.getAccountInfoCode());
                                    } else {
                                        Map<String, Object> fadRtfreevalueViewConditionsMap = new HashMap<String, Object>();
                                        fadRtfreevalueViewConditionsMap.put(FadRtfreevalueViewAttribute.ACCOUNT_NO, isNullReturnEmpty(fadCertificateAccount.getAccountNo()));
                                        fadRtfreevalueViewConditionsMap.put(FadRtfreevalueViewAttribute.ACCOUNT_INFO_NAME, isNullReturnEmpty(fadCertificateAccount.getAccountInfoName()));
                                        List<FadRtfreevalueView> fadRtfreevalueViewList = isNullReturnEmpty(fadCertificateAccount.getAccountInfoName()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadRtfreevalueView.class, fadRtfreevalueViewConditionsMap, null) : new ArrayList<>();
                                        if (fadRtfreevalueViewList.size() > 0) {
                                            accountInfoCode = isNullReturnEmpty(((FadRtfreevalueView) fadRtfreevalueViewList.get(0)).getAccountInfoCode());
                                        } else {
                                            accountInfoCode = isNullReturnEmpty(fadCertificateAccount.getAccountInfoName());
                                        }
                                    }

                                    if (accountInfoCode.length() == 0) {
                                        String errMessage = "【辅助核算项】【" + isNullReturnEmpty(fadCertificateAccount.getAccountType()) + "】的【NCCODE】不能为空!";
                                        errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                                    }
                                } else {
                                    String errMessage = "【辅助核算项】【" + isNullReturnEmpty(fadCertificateAccount.getAccountType()) + "】不能为空!";
                                    errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                                }
                            }

                            //辅助核算类型为其他
                            else {
                                if (isNullReturnEmpty(fadCertificateAccount.getAccountInfoCode()).length() > 0) {
                                    accountInfoCode = isNullReturnEmpty(fadCertificateAccount.getAccountInfoCode());
                                } else {
                                    String errMessage = "【辅助核算项】【" + isNullReturnEmpty(fadCertificateAccount.getAccountType()) + "】不能为空!";
                                    errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                                }
                            }
                            xmlFadCertificateAccountPostXMLData = xmlFadCertificateAccountPostXMLData.replace("[$account_info_code]", accountInfoCode);
                        }
                    }
                    xmlFadCertificateDetailPostXMLData = xmlFadCertificateDetailPostXMLData.replace("[$auxiliary_accounting]", xmlFadCertificateAccountPostXMLData);
                }

                if (
                        ((debtor > 1) && (creditor > 1))
                                && (!(CertificateType.equals("转账凭证")))
                        ) {
                    String errMessage = "发送凭证失败!(【" + CertificateType + "】存在多借多贷不能发送)";
                    errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                }

                if (CertificateType.equals("转账凭证")) {
                    xmlFadCertificatePostXMLData = xmlFadCertificatePostXMLData.replace("[$reserve1]", "");
                    xmlFadCertificatePostXMLData = xmlFadCertificatePostXMLData.replace("[$reserve2]", "");
                } else {
                    xmlFadCertificatePostXMLData = xmlFadCertificatePostXMLData.replace("[$reserve1]", reserve1);
                    xmlFadCertificatePostXMLData = xmlFadCertificatePostXMLData.replace("[$reserve2]", reserve2);
                }

                xmlFadCertificatePostXMLData = xmlFadCertificatePostXMLData.replace("[$voucher_type]", CertificateType);

                xmlFadCertificatePostXMLData = xmlFadCertificatePostXMLData.replace("[$entry]", xmlFadCertificateDetailPostXMLData);

                //判断是否有异常
                if (errMessages.length() > 0) {
                    result.put("errMessages", "单据【" + nvl(fadCertificate.getOriginalFormCode(), fadCertificate.getUuid()) + "】" + errMessages);
                    return result;
                }

                //WebService取得
                ScdpWebservices scdpWebservices = isNullReturnEmpty("1").length() > 0 ? PersistenceFactory.getInstance().findByPK(ScdpWebservices.class, "1") : null;
                if (scdpWebservices != null) {

                    //WebService取得
                    String Url = "http://" + isNullReturnEmpty(scdpWebservices.getWsType()) + isNullReturnEmpty(scdpWebservices.getWsCode());
                    Map<String, Object> scdpWebservicesDescConditionsMap = new HashMap<String, Object>();
                    scdpWebservicesDescConditionsMap.put(ScdpWebservicesDescAttribute.PUUID, isNullReturnEmpty(scdpWebservices.getPuuid()));
                    List<ScdpWebservicesDesc> scdpWebservicesDescList = isNullReturnEmpty(scdpWebservices.getPuuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpWebservicesDesc.class, scdpWebservicesDescConditionsMap, "seq_no asc") : new ArrayList<>();
                    for (int i = 0; i < scdpWebservicesDescList.size(); i++) {
                        ScdpWebservicesDesc scdpWebservicesDesc = (ScdpWebservicesDesc) scdpWebservicesDescList.get(i);
                        if (i == 0) {
                            Url = Url + "?" + isNullReturnEmpty(scdpWebservicesDesc.getLangId()) + "=" + isNullReturnEmpty(scdpWebservicesDesc.getCodeDesc());
                        } else {
                            Url = Url + "&" + isNullReturnEmpty(scdpWebservicesDesc.getLangId()) + "=" + isNullReturnEmpty(scdpWebservicesDesc.getCodeDesc());
                        }
                    }

                    //发送到WebService
                    xmlFadCertificatePostXMLData = isNullReturnEmpty(xmlFadCertificatePostXMLData);
                    HttpClient httpClient = new HttpClient();
                    PostMethod method = new PostMethod(Url);
                    method.setRequestBody(xmlFadCertificatePostXMLData);
                    method.setRequestHeader("Content-type", "text/xml; charset=GBK");

                    String ResponseBodyAsString = "";

                    //排队占用
                    String sql = "UPDATE SCDP_WEBSERVICES SET IS_ACTIVE = 1 WHERE UUID = '1'";
                    DAOMeta daoMeta = new DAOMeta();
                    daoMeta.setStrSql(sql);
                    daoMeta.setNeedFilter(false);
                    try {
                        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
                    } catch (Exception e) {
                        throw new BizException("凭证排队发送异常:占用超时!");
                    }
                    try {

                        //正式发送
                        httpClient.executeMethod(method);
                        ResponseBodyAsString = isNullReturnEmpty(method.getResponseBodyAsString());

                        //模拟发送
                        /*ResponseBodyAsString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                                + "<ufinterface billtype=\"gl\" filename=\"voucher.xml\" isexchange=\"Y\" proc=\"add\" receiver=\"9137A00@9137A00-0002\" replace=\"Y\" roottag=\"sendresult\" sender=\"ZHKJ\" successful=\"Y\">"
                                + "<sendresult>"
                                + "<billpk>"
                                + "</billpk>"
                                + "<bdocid>ff808081521570c6015215a71f3d0007</bdocid>"
                                + "<filename>voucher.xml</filename>"
                                + "<resultcode>1</resultcode>"
                                + "<resultdescription>单据ff808081521570c6015215a71f3d0007开始处理..."
                                + "单据ff808081521570c6015215a71f3d0007处理完毕!</resultdescription>"
                                + "<content>2016.06-银行凭证-100</content>"
                                + "</sendresult>"
                                + "</ufinterface>";*/
                    } catch (Exception e) {
                        String feedback = "连接【NCService】失败,请联系网络管理员!";
                        fadCertificate.setFeedback(feedback);
                        fadCertificate.setMaker(userId);
                        fadCertificate.setMakeDate(makeDate);

                        List fadCertificateList = new ArrayList<>();
                        fadCertificateList.add(fadCertificate);
                        DtoHelper.batchUpdate(fadCertificateList, FadCertificate.class);

                        String errMessage = feedback;
                        errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                        result.put("errMessages", errMessages);

                        result.put("feedback", feedback);
                        result.put("maker", userId);
                        result.put("makerName", userName);
                        result.put("makeDate", makeDate);
                        return result;
                    }

                    Document doc = XmlUtil.parseDoc(ResponseBodyAsString);
                    Element resultcodeElement = XmlUtil.getNodeByXPath(doc, "/ufinterface/sendresult/resultcode");
                    Element resultdescriptionElement = XmlUtil.getNodeByXPath(doc, "/ufinterface/sendresult/resultdescription");
                    Element contentElement = XmlUtil.getNodeByXPath(doc, "/ufinterface/sendresult/content");
                    String state = "";
                    String certificateNo = "";
                    if (resultcodeElement.getText().equals("1")) {
                        state = "1";
                        certificateNo = isNullReturnEmpty(contentElement.getText());
                    } else {
                        state = "0";
                    }
                    String feedback = isNullReturnEmpty(resultdescriptionElement.getText());
                    String errMessage = feedback.replace(fadCertificate.getUuid(), "【" + nvl(fadCertificate.getOriginalFormCode(), fadCertificate.getUuid()) + "】");
                    errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                    result.put("errMessages", errMessages);

                    //xmlFadCertificatePostXMLData = xmlFadCertificatePostXMLData.replace("'", "\"");
                    //ResponseBodyAsString = ResponseBodyAsString.replace("'", "\"");
                    //feedback = feedback.replace("'", "\"");

                    fadCertificate.setState(state);
                    fadCertificate.setCertificateNo(certificateNo);
                    fadCertificate.setFeedback(feedback);
                    fadCertificate.setMaker(userId);
                    fadCertificate.setMakeDate(makeDate);
                    fadCertificate.setNcrequestUrl(Url);
                    fadCertificate.setNcrequestXml(xmlFadCertificatePostXMLData);
                    fadCertificate.setNcresponseXml(ResponseBodyAsString);

                    List fadCertificateList = new ArrayList<>();
                    fadCertificateList.add(fadCertificate);
                    DtoHelper.batchUpdate(fadCertificateList, FadCertificate.class);

                    result.put("state", state);
                    result.put("certificateNo", certificateNo);
                    result.put("feedback", feedback);
                    result.put("maker", userId);
                    result.put("makerName", userName);
                    result.put("makeDate", makeDate);
                    result.put("ncrequestUrl", Url);
                    result.put("ncrequestXml", xmlFadCertificatePostXMLData);
                    result.put("ncresponseXml", ResponseBodyAsString);
                } else {
                    String errMessage = "【NCWebService】不能为空!";
                    errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                    result.put("errMessages", "单据【" + nvl(fadCertificate.getOriginalFormCode(), fadCertificate.getUuid()) + "】" + errMessages);
                    return result;
                }
            } else {
                String errMessage = "【凭证抬头】【制单人】不能为空!";
                errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                result.put("errMessages", "单据【" + nvl(fadCertificate.getOriginalFormCode(), fadCertificate.getUuid()) + "】" + errMessages);
                return result;
            }
        } else {
            String errMessage = "记录不存在!";
            errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
            result.put("errMessages", errMessages);
            return result;
        }

        //根据NC发送状态设定原始单据状态
        if (isNullReturnEmpty(result.get("state")).equals("1")) {
            updateStateToSent(isNullReturnEmpty(fadCertificate.getUuid()), isNullReturnEmpty(fadCertificate.getBusinessId()), isNullReturnEmpty(fadCertificate.getOriginalFormType()), isNullReturnEmpty(result.get("state")), "2", "4", result);
        }

        return result;
    }

    @Override
    public Map certificateToLogicVoid(String fadCertificateUuid, String makerUuid) {
        Map result = new HashMap<>();

        //Err
        String errMessages = "";

        Date makeDate = new Date();
        SimpleDateFormat makeDateToString = new SimpleDateFormat("yyyy-MM-dd");
        String makeDateS = makeDateToString.format(makeDate);

        //主表凭证取得
        FadCertificateDto fadCertificate = isNullReturnEmpty(fadCertificateUuid).length() > 0 ? (FadCertificateDto) DtoHelper.findDtoByPK(FadCertificateDto.class, fadCertificateUuid) : null;
        if (fadCertificate != null) {

            //验证合并拆分凭证
            certificateCheckMoney(fadCertificate);

            //凭证合并关系取得
            Map<String, Object> fadCertifiMergeRelConditionsMap = new HashMap<String, Object>();
            fadCertifiMergeRelConditionsMap.put(FadCertifiMergeSplitRelAttribute.MERGE_ID, isNullReturnEmpty(fadCertificate.getUuid()));
            List<FadCertifiMergeSplitRel> fadCertifiMergeRelList = isNullReturnEmpty(fadCertificate.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiMergeSplitRel.class, fadCertifiMergeRelConditionsMap, "seq_no asc") : new ArrayList<>();
            if (fadCertifiMergeRelList.size() > 0) {
                String errMessage = "【合并凭证】不能作废!";
                errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                result.put("errMessages", errMessages);
                return result;
            }

            //凭证拆分关系取得
            Map<String, Object> fadCertifiSplitRelConditionsMap = new HashMap<String, Object>();
            fadCertifiSplitRelConditionsMap.put(FadCertifiMergeSplitRelAttribute.SPLIT_ID, isNullReturnEmpty(fadCertificate.getUuid()));
            List<FadCertifiMergeSplitRel> fadCertifiSplitRelList = isNullReturnEmpty(fadCertificate.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiMergeSplitRel.class, fadCertifiSplitRelConditionsMap, "seq_no asc") : new ArrayList<>();
            if (fadCertifiSplitRelList.size() > 0) {
                String errMessage = "【拆分凭证】不能作废!";
                errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                result.put("errMessages", errMessages);
                return result;
            }

            String sql = "SELECT 1 FROM FAD_CERTIFICATE WHERE FREE2 = '" + isNullReturnEmpty(fadCertificate.getUuid()) + "' AND STATE IN ('0', '1')";
            DAOMeta daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                String errMessage = "【已复制凭证】不能作废!";
                errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                result.put("errMessages", errMessages);
                return result;
            }

            sql = "SELECT 1 FROM FAD_CERTIFICATE WHERE UUID = '" + isNullReturnEmpty(fadCertificate.getUuid()) + "' AND FREE2 IS NOT NULL";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                String errMessage = "【复制凭证】不能作废!";
                errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                result.put("errMessages", errMessages);
                return result;
            }

            //凭证冲红关系取得
            Map<String, Object> fadCertifiCertifiRelConditionsMap = new HashMap<String, Object>();
            fadCertifiCertifiRelConditionsMap.put(FadCertifiDeficitRelAttribute.CERTIFI_ID, isNullReturnEmpty(fadCertificate.getUuid()));
            List<FadCertifiDeficitRel> fadCertifiCertifiRelList = isNullReturnEmpty(fadCertificate.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiDeficitRel.class, fadCertifiCertifiRelConditionsMap, "seq_no asc") : new ArrayList<>();
            if (fadCertifiCertifiRelList.size() > 0) {
                String errMessage = "【已红冲凭证】不能作废!";
                errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                result.put("errMessages", errMessages);
                return result;
            }

            //凭证红冲关系取得
            Map<String, Object> fadCertifiDeficitRelConditionsMap = new HashMap<String, Object>();
            fadCertifiDeficitRelConditionsMap.put(FadCertifiDeficitRelAttribute.DEFICIT_ID, isNullReturnEmpty(fadCertificate.getUuid()));
            List<FadCertifiDeficitRel> fadCertifiDeficitRelList = isNullReturnEmpty(fadCertificate.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiDeficitRel.class, fadCertifiDeficitRelConditionsMap, "seq_no asc") : new ArrayList<>();
            if (!(fadCertifiDeficitRelList.size() > 0)) {
                String[] originalFormCodes = isNullReturnEmpty(fadCertificate.getOriginalFormCode()).split(MergeSplitSymbol);

                if (
                        (isNullReturnEmpty(fadCertificate.getOriginalFormCode()).indexOf("(请款流水号)") != -1)
                                &&
                                (
                                        (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("1"))
                                                ||
                                                (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("2"))
                                                ||
                                                (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("3"))
                                )
                        ) {
                    for (int ii = 0; ii < originalFormCodes.length; ii++) {
                        String originalFormCode = originalFormCodes[ii];
                        if (originalFormCode.indexOf("(请款流水号)") != -1) {
                            sql = "SELECT 1 FROM FAD_CERTIFICATE WHERE ORIGINAL_FORM_CODE LIKE '%" + originalFormCode + "%' AND ORIGINAL_FORM_TYPE = '5' AND STATE IN ('0', '1')";
                            daoMeta = new DAOMeta();
                            daoMeta.setStrSql(sql);
                            daoMeta.setNeedFilter(false);
                            if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                                String errMessage = "【当前请款凭证】尚有相关的【5.有请款报销(日常请款)】凭证未作废,请先将相关【5.有请款报销(日常请款)】凭证作废!";
                                errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                            }

                            sql = "SELECT 1 FROM FAD_CERTIFICATE WHERE ORIGINAL_FORM_CODE LIKE '%" + originalFormCode + "%' AND ORIGINAL_FORM_TYPE = '7' AND STATE IN ('0', '1')";
                            daoMeta = new DAOMeta();
                            daoMeta.setStrSql(sql);
                            daoMeta.setNeedFilter(false);
                            if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                                String errMessage = "【当前请款凭证】尚有相关的【7.有请款采购合同】凭证未作废,请先将相关【7.有请款采购合同】凭证作废!";
                                errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                            }

                            sql = "SELECT 1 FROM FAD_CERTIFICATE WHERE ORIGINAL_FORM_CODE LIKE '%" + originalFormCode + "%' AND ORIGINAL_FORM_TYPE = '11' AND STATE IN ('0', '1')";
                            daoMeta = new DAOMeta();
                            daoMeta.setStrSql(sql);
                            daoMeta.setNeedFilter(false);
                            if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                                String errMessage = "【当前请款凭证】尚有相关的【11.还款】凭证未作废,请先将相关【11.还款】凭证作废!";
                                errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                            }
                        }
                    }
                }
            }

            //判断是否有异常
            if (errMessages.length() > 0) {
                result.put("errMessages", errMessages);
                return result;
            }

            //员工信息取得
            ScdpUser scdpUser = isNullReturnEmpty(makerUuid).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScdpUser.class, makerUuid) : null;
            if (scdpUser != null) {
                String userId = isNullReturnEmpty(scdpUser.getUserId());
                String userName = isNullReturnEmpty(scdpUser.getUserName());
                String state = "2";
                fadCertificate.setState(state);
                fadCertificate.setMaker(userId);
                fadCertificate.setMakeDate(makeDate);

                List fadCertificateList = new ArrayList<>();
                fadCertificateList.add(fadCertificate);

                if (!(fadCertifiDeficitRelList.size() > 0)) {
                    if (
                            (
                                    (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("5"))
                                            || (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("5.1"))
                                            || (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("6"))
                                            || (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("7"))
                                            || (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("8"))
                                            || (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("9"))
                            )
                                    && (isNullReturnEmpty(fadCertificate.getBusinessId()).length() > 0)
                            ) {
                        sql = "SELECT * FROM FAD_CERTIFICATE WHERE BUSINESS_ID = '" + isNullReturnEmpty(fadCertificate.getBusinessId()) + "' AND STATE <> '2'";
                        daoMeta = new DAOMeta();
                        daoMeta.setStrSql(sql);
                        daoMeta.setNeedFilter(false);
                        List fadCertificateMapListOther = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                        for (int i = 0; i < fadCertificateMapListOther.size(); i++) {
                            Map fadCertificateMapOther = (Map) fadCertificateMapListOther.get(i);

                            //主表凭证取得
                            FadCertificateDto fadCertificateOther = isNullReturnEmpty(fadCertificateMapOther.get("uuid")).length() > 0 ? (FadCertificateDto) DtoHelper.findDtoByPK(FadCertificateDto.class, isNullReturnEmpty(fadCertificateMapOther.get("uuid"))) : null;
                            if (fadCertificateOther != null) {
                                if (!(isNullReturnEmpty(fadCertificateOther.getUuid()).equals(isNullReturnEmpty(fadCertificate.getUuid())))) {

                                    //凭证红冲关系取得
                                    Map<String, Object> fadCertifiDeficitRelOtherConditionsMap = new HashMap<String, Object>();
                                    fadCertifiDeficitRelOtherConditionsMap.put(FadCertifiDeficitRelAttribute.DEFICIT_ID, isNullReturnEmpty(fadCertificateOther.getUuid()));
                                    List<FadCertifiDeficitRel> fadCertifiDeficitRelOtherList = isNullReturnEmpty(fadCertificateOther.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiDeficitRel.class, fadCertifiDeficitRelOtherConditionsMap, "seq_no asc") : new ArrayList<>();

                                    //凭证冲红关系取得
                                    Map<String, Object> fadCertifiCertifiRelOtherConditionsMap = new HashMap<String, Object>();
                                    fadCertifiCertifiRelOtherConditionsMap.put(FadCertifiDeficitRelAttribute.CERTIFI_ID, isNullReturnEmpty(fadCertificateOther.getUuid()));
                                    List<FadCertifiDeficitRel> fadCertifiCertifiRelOtherList = isNullReturnEmpty(fadCertificateOther.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiDeficitRel.class, fadCertifiCertifiRelOtherConditionsMap, "seq_no asc") : new ArrayList<>();

                                    //凭证合并关系取得
                                    Map<String, Object> fadCertifiMergeRelOtherConditionsMap = new HashMap<String, Object>();
                                    fadCertifiMergeRelOtherConditionsMap.put(FadCertifiMergeSplitRelAttribute.MERGE_ID, isNullReturnEmpty(fadCertificateOther.getUuid()));
                                    List<FadCertifiMergeSplitRel> fadCertifiMergeRelOtherList = isNullReturnEmpty(fadCertificateOther.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiMergeSplitRel.class, fadCertifiMergeRelOtherConditionsMap, "seq_no asc") : new ArrayList<>();

                                    //凭证拆分关系取得
                                    Map<String, Object> fadCertifiSplitRelOtherConditionsMap = new HashMap<String, Object>();
                                    fadCertifiSplitRelOtherConditionsMap.put(FadCertifiMergeSplitRelAttribute.SPLIT_ID, isNullReturnEmpty(fadCertificateOther.getUuid()));
                                    List<FadCertifiMergeSplitRel> fadCertifiSplitRelOtherList = isNullReturnEmpty(fadCertificateOther.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiMergeSplitRel.class, fadCertifiSplitRelOtherConditionsMap, "seq_no asc") : new ArrayList<>();

                                    if (fadCertifiDeficitRelOtherList.size() > 0) {
                                        String errMessage = "相关的【主凭证】或【关联凭证】中包含【已红冲凭证】不能作废!";
                                        errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                                        result.put("errMessages", errMessages);
                                        return result;
                                    } else if (fadCertifiCertifiRelOtherList.size() > 0) {
                                        String errMessage = "相关的【主凭证】或【关联凭证】中包含【已红冲凭证】不能作废!";
                                        errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                                        result.put("errMessages", errMessages);
                                        return result;
                                    } else if (fadCertifiMergeRelOtherList.size() > 0) {
                                        String errMessage = "相关的【主凭证】或【关联凭证】中包含参与过合并或拆分的凭证不能作废!";
                                        errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                                        result.put("errMessages", errMessages);
                                        return result;
                                    } else if (fadCertifiSplitRelOtherList.size() > 0) {
                                        String errMessage = "相关的【主凭证】或【关联凭证】中包含参与过合并或拆分的凭证不能作废!";
                                        errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                                        result.put("errMessages", errMessages);
                                        return result;
                                    }

                                    fadCertificateOther.setState(state);
                                    fadCertificateOther.setMaker(userId);
                                    fadCertificateOther.setMakeDate(makeDate);
                                    List fadCertificateListOther = new ArrayList<>();
                                    fadCertificateListOther.add(fadCertificateOther);
                                    DtoHelper.batchUpdate(fadCertificateListOther, FadCertificate.class);
                                }
                            } else {
                                String errMessage = "记录不存在!";
                                errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                                result.put("errMessages", errMessages);
                                return result;
                            }
                        }
                    }
                } else {
                    for (int iii = 0; iii < fadCertifiDeficitRelList.size(); iii++) {
                        FadCertifiDeficitRel fadCertifiDeficitRel = ((FadCertifiDeficitRel) fadCertifiDeficitRelList.get(iii));

                        sql = "SELECT 1 FROM FAD_CERTIFICATE WHERE FREE2 = '" + isNullReturnEmpty(fadCertifiDeficitRel.getCertifiId()) + "' AND STATE IN ('0', '1')";
                        daoMeta = new DAOMeta();
                        daoMeta.setStrSql(sql);
                        daoMeta.setNeedFilter(false);
                        if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                            throw new BizException("本张【红冲凭证】对应的【已红冲凭证】已复制新增,不能作废!");
                        } else {
                            PersistenceFactory.getInstance().delete(fadCertifiDeficitRel);
                        }
                    }
                }
                DtoHelper.batchUpdate(fadCertificateList, FadCertificate.class);
                result.put("state", state);
                result.put("maker", userId);
                result.put("makerName", userName);
                result.put("makeDate", makeDate);
            } else {
                String errMessage = "【凭证抬头】【制单人】不能为空!";
                errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                result.put("errMessages", errMessages);
                return result;
            }
        } else {
            String errMessage = "记录不存在!";
            errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
            result.put("errMessages", errMessages);
            return result;
        }

        //根据NC发送状态设定原始单据状态
        if (isNullReturnEmpty(result.get("state")).equals("2")) {
            updateStateToSent(isNullReturnEmpty(fadCertificate.getUuid()), isNullReturnEmpty(fadCertificate.getBusinessId()), isNullReturnEmpty(fadCertificate.getOriginalFormType()), isNullReturnEmpty(result.get("state")), "4", "2", result);
        }

        return result;
    }

    @Override
    public Map certificateToLogicVoidAlone(String fadCertificateUuid, String makerUuid) {
        Map result = new HashMap<>();

        //Err
        String errMessages = "";

        Date makeDate = new Date();
        SimpleDateFormat makeDateToString = new SimpleDateFormat("yyyy-MM-dd");
        String makeDateS = makeDateToString.format(makeDate);

        //主表凭证取得
        FadCertificateDto fadCertificate = isNullReturnEmpty(fadCertificateUuid).length() > 0 ? (FadCertificateDto) DtoHelper.findDtoByPK(FadCertificateDto.class, fadCertificateUuid) : null;
        if (fadCertificate != null) {

            //验证合并拆分凭证
            certificateCheckMoney(fadCertificate);

            //凭证合并关系取得
            Map<String, Object> fadCertifiMergeRelConditionsMap = new HashMap<String, Object>();
            fadCertifiMergeRelConditionsMap.put(FadCertifiMergeSplitRelAttribute.MERGE_ID, isNullReturnEmpty(fadCertificate.getUuid()));
            List<FadCertifiMergeSplitRel> fadCertifiMergeRelList = isNullReturnEmpty(fadCertificate.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiMergeSplitRel.class, fadCertifiMergeRelConditionsMap, "seq_no asc") : new ArrayList<>();
            if (fadCertifiMergeRelList.size() > 0) {
                String errMessage = "【合并凭证】不能消退!";
                errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                result.put("errMessages", errMessages);
                return result;
            }

            //凭证拆分关系取得
            Map<String, Object> fadCertifiSplitRelConditionsMap = new HashMap<String, Object>();
            fadCertifiSplitRelConditionsMap.put(FadCertifiMergeSplitRelAttribute.SPLIT_ID, isNullReturnEmpty(fadCertificate.getUuid()));
            List<FadCertifiMergeSplitRel> fadCertifiSplitRelList = isNullReturnEmpty(fadCertificate.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiMergeSplitRel.class, fadCertifiSplitRelConditionsMap, "seq_no asc") : new ArrayList<>();
            if (fadCertifiSplitRelList.size() > 0) {
                String errMessage = "【拆分凭证】不能消退!";
                errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                result.put("errMessages", errMessages);
                return result;
            }

            String sql = "SELECT 1 FROM FAD_CERTIFICATE WHERE FREE2 = '" + isNullReturnEmpty(fadCertificate.getUuid()) + "' AND STATE IN ('0', '1')";
            DAOMeta daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                String errMessage = "【已复制凭证】不能消退!";
                errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                result.put("errMessages", errMessages);
                return result;
            }

            sql = "SELECT 1 FROM FAD_CERTIFICATE WHERE UUID = '" + isNullReturnEmpty(fadCertificate.getUuid()) + "' AND FREE2 IS NOT NULL";
            daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                String errMessage = "【复制凭证】不能消退!";
                errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                result.put("errMessages", errMessages);
                return result;
            }

            //凭证冲红关系取得
            Map<String, Object> fadCertifiCertifiRelConditionsMap = new HashMap<String, Object>();
            fadCertifiCertifiRelConditionsMap.put(FadCertifiDeficitRelAttribute.CERTIFI_ID, isNullReturnEmpty(fadCertificate.getUuid()));
            List<FadCertifiDeficitRel> fadCertifiCertifiRelList = isNullReturnEmpty(fadCertificate.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiDeficitRel.class, fadCertifiCertifiRelConditionsMap, "seq_no asc") : new ArrayList<>();
            if (fadCertifiCertifiRelList.size() > 0) {
                String errMessage = "【已红冲凭证】不能消退!";
                errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                result.put("errMessages", errMessages);
                return result;
            }

            //凭证红冲关系取得
            Map<String, Object> fadCertifiDeficitRelConditionsMap = new HashMap<String, Object>();
            fadCertifiDeficitRelConditionsMap.put(FadCertifiDeficitRelAttribute.DEFICIT_ID, isNullReturnEmpty(fadCertificate.getUuid()));
            List<FadCertifiDeficitRel> fadCertifiDeficitRelList = isNullReturnEmpty(fadCertificate.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiDeficitRel.class, fadCertifiDeficitRelConditionsMap, "seq_no asc") : new ArrayList<>();
            if (fadCertifiDeficitRelList.size() > 0) {
                String errMessage = "【红冲凭证】不能消退!";
                errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                result.put("errMessages", errMessages);
                return result;
            }

            //判断是否有异常
            if (errMessages.length() > 0) {
                result.put("errMessages", errMessages);
                return result;
            }

            //员工信息取得
            ScdpUser scdpUser = isNullReturnEmpty(makerUuid).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScdpUser.class, makerUuid) : null;
            if (scdpUser != null) {
                String userId = isNullReturnEmpty(scdpUser.getUserId());
                String userName = isNullReturnEmpty(scdpUser.getUserName());
                String state = "2";
                fadCertificate.setState(state);
                fadCertificate.setMaker(userId);
                fadCertificate.setMakeDate(makeDate);

                List fadCertificateList = new ArrayList<>();
                fadCertificateList.add(fadCertificate);

                if (
                        (
                                (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("8"))
                                        || (isNullReturnEmpty(fadCertificate.getOriginalFormType()).equals("9"))
                        )
                                && (isNullReturnEmpty(fadCertificate.getBusinessId()).length() > 0)
                        ) {
                    sql = "SELECT * FROM FAD_CERTIFICATE WHERE BUSINESS_ID = '" + isNullReturnEmpty(fadCertificate.getBusinessId()) + "' AND STATE <> '2'";
                    daoMeta = new DAOMeta();
                    daoMeta.setStrSql(sql);
                    daoMeta.setNeedFilter(false);
                    List fadCertificateMapListOther = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                    for (int i = 0; i < fadCertificateMapListOther.size(); i++) {
                        Map fadCertificateMapOther = (Map) fadCertificateMapListOther.get(i);

                        //主表凭证取得
                        FadCertificateDto fadCertificateOther = isNullReturnEmpty(fadCertificateMapOther.get("uuid")).length() > 0 ? (FadCertificateDto) DtoHelper.findDtoByPK(FadCertificateDto.class, isNullReturnEmpty(fadCertificateMapOther.get("uuid"))) : null;
                        if (fadCertificateOther != null) {
                            if (!(isNullReturnEmpty(fadCertificateOther.getUuid()).equals(isNullReturnEmpty(fadCertificate.getUuid())))) {

                                //凭证红冲关系取得
                                Map<String, Object> fadCertifiDeficitRelOtherConditionsMap = new HashMap<String, Object>();
                                fadCertifiDeficitRelOtherConditionsMap.put(FadCertifiDeficitRelAttribute.DEFICIT_ID, isNullReturnEmpty(fadCertificateOther.getUuid()));
                                List<FadCertifiDeficitRel> fadCertifiDeficitRelOtherList = isNullReturnEmpty(fadCertificateOther.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiDeficitRel.class, fadCertifiDeficitRelOtherConditionsMap, "seq_no asc") : new ArrayList<>();

                                //凭证冲红关系取得
                                Map<String, Object> fadCertifiCertifiRelOtherConditionsMap = new HashMap<String, Object>();
                                fadCertifiCertifiRelOtherConditionsMap.put(FadCertifiDeficitRelAttribute.CERTIFI_ID, isNullReturnEmpty(fadCertificateOther.getUuid()));
                                List<FadCertifiDeficitRel> fadCertifiCertifiRelOtherList = isNullReturnEmpty(fadCertificateOther.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiDeficitRel.class, fadCertifiCertifiRelOtherConditionsMap, "seq_no asc") : new ArrayList<>();

                                //凭证合并关系取得
                                Map<String, Object> fadCertifiMergeRelOtherConditionsMap = new HashMap<String, Object>();
                                fadCertifiMergeRelOtherConditionsMap.put(FadCertifiMergeSplitRelAttribute.MERGE_ID, isNullReturnEmpty(fadCertificateOther.getUuid()));
                                List<FadCertifiMergeSplitRel> fadCertifiMergeRelOtherList = isNullReturnEmpty(fadCertificateOther.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiMergeSplitRel.class, fadCertifiMergeRelOtherConditionsMap, "seq_no asc") : new ArrayList<>();

                                //凭证拆分关系取得
                                Map<String, Object> fadCertifiSplitRelOtherConditionsMap = new HashMap<String, Object>();
                                fadCertifiSplitRelOtherConditionsMap.put(FadCertifiMergeSplitRelAttribute.SPLIT_ID, isNullReturnEmpty(fadCertificateOther.getUuid()));
                                List<FadCertifiMergeSplitRel> fadCertifiSplitRelOtherList = isNullReturnEmpty(fadCertificateOther.getUuid()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiMergeSplitRel.class, fadCertifiSplitRelOtherConditionsMap, "seq_no asc") : new ArrayList<>();

                                if (fadCertifiDeficitRelOtherList.size() > 0) {
                                    String errMessage = "相关的【主凭证】或【关联凭证】中包含【已红冲凭证】不能消退!";
                                    errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                                    result.put("errMessages", errMessages);
                                    return result;
                                } else if (fadCertifiCertifiRelOtherList.size() > 0) {
                                    String errMessage = "相关的【主凭证】或【关联凭证】中包含【已红冲凭证】不能消退!";
                                    errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                                    result.put("errMessages", errMessages);
                                    return result;
                                } else if (fadCertifiMergeRelOtherList.size() > 0) {
                                    String errMessage = "相关的【主凭证】或【关联凭证】中包含参与过合并或拆分的凭证不能消退!";
                                    errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                                    result.put("errMessages", errMessages);
                                    return result;
                                } else if (fadCertifiSplitRelOtherList.size() > 0) {
                                    String errMessage = "相关的【主凭证】或【关联凭证】中包含参与过合并或拆分的凭证不能消退!";
                                    errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                                    result.put("errMessages", errMessages);
                                    return result;
                                }
                            }
                        } else {
                            String errMessage = "记录不存在!";
                            errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                            result.put("errMessages", errMessages);
                            return result;
                        }
                    }
                } else {
                    String errMessage = "目前仅支持【8.采购合同成本确认】、【9.采购合同支付(20000以下)】类凭证的消退";
                    errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                    result.put("errMessages", errMessages);
                    return result;
                }

                DtoHelper.batchUpdate(fadCertificateList, FadCertificate.class);
                result.put("state", state);
                result.put("maker", userId);
                result.put("makerName", userName);
                result.put("makeDate", makeDate);
            } else {
                String errMessage = "【凭证抬头】【制单人】不能为空!";
                errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
                result.put("errMessages", errMessages);
                return result;
            }
        } else {
            String errMessage = "记录不存在!";
            errMessages = (errMessages.length() > 0) ? errMessages + "\n" + errMessage : "\n" + errMessage;
            result.put("errMessages", errMessages);
            return result;
        }

        //根据NC发送状态设定原始单据状态
        if (isNullReturnEmpty(result.get("state")).equals("2")) {
            updateStateToSent(isNullReturnEmpty(fadCertificate.getUuid()), isNullReturnEmpty(fadCertificate.getBusinessId()), isNullReturnEmpty(fadCertificate.getOriginalFormType()), "1", "2", "4", result);
        }

        return result;
    }
}