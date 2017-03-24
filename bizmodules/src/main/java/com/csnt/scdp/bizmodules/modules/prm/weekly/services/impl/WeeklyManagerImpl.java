package com.csnt.scdp.bizmodules.modules.prm.weekly.services.impl;

import com.csnt.scdp.bizmodules.entity.fad.FadPayReqC;
import com.csnt.scdp.bizmodules.entity.prm.*;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadPayReqCAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.*;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpCodeHelper;
import com.csnt.scdp.bizmodules.modules.prm.weekly.services.intf.WeeklyManager;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpCode;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.entityattributes.ScdpCodeAttribute;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  WeeklyManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-25 14:01:29
 */

@Scope("singleton")
@Service("weekly-manager")
public class WeeklyManagerImpl implements WeeklyManager {
    @Override
    public PrmWeekly getPrmProjectMainId(String uuid) {
//        Map<String,Object> mapConditions = new HashMap<String,Object>();
//        mapConditions.put(PrmWeeklyAttribute., uuid);
        PrmWeekly prmWeekly = PersistenceFactory.getInstance().findByPK(PrmWeekly.class, uuid);
        if (StringUtil.isNotEmpty(prmWeekly)) {
            return prmWeekly;
        }
        return null;
    }

    //获取项目简报周报id
    @Override
    public List<PrmBrief> getPrmBriefWeeklyId(String prmProjectMainId) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(PrmBriefAttribute.PRM_PROJECT_MAIN_ID, prmProjectMainId);
        List<PrmBrief> lstPrmBrief = PersistenceFactory.getInstance().findByAnyFields(PrmBrief.class, mapConditions, null);
        if (ListUtil.isNotEmpty(lstPrmBrief)) {
            return lstPrmBrief;
        }
        return null;
    }

    @Override
    public PrmBrief getPrmBriefWeeklyIdForUUID(String uuid) {
//        Map<String,Object> mapConditions = new HashMap<String,Object>();
//        mapConditions.put(PrmWeeklyAttribute., uuid);
        PrmBrief prmBrief = PersistenceFactory.getInstance().findByPK(PrmBrief.class, uuid);
        if (StringUtil.isNotEmpty(prmBrief)) {
            return prmBrief;
        }
        return null;
    }

    //获取人员动向周报id
    @Override
    public List<PrmMemberTrend> getPrmMemberTrendWeeklyId(String prmProjectMainId) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(PrmMemberTrendAttribute.PRM_PROJECT_MAIN_ID, prmProjectMainId);
        List<PrmMemberTrend> lstPrmMemberTrend = PersistenceFactory.getInstance().findByAnyFields(PrmMemberTrend.class, mapConditions, null);
        if (ListUtil.isNotEmpty(lstPrmMemberTrend)) {
            return lstPrmMemberTrend;
        }
        return null;
    }

    @Override
    public PrmMemberTrend getPrmMemberTrendWeeklyIdForUUID(String uuid) {
//        Map<String,Object> mapConditions = new HashMap<String,Object>();
//        mapConditions.put(PrmWeeklyAttribute., uuid);
        PrmMemberTrend prmMemberTrend = PersistenceFactory.getInstance().findByPK(PrmMemberTrend.class, uuid);
        if (StringUtil.isNotEmpty(prmMemberTrend)) {
            return prmMemberTrend;
        }
        return null;
    }

    //获取到货确认周报id
    @Override
    public List<PrmGoodsArrival> getPrmGoodsArrivalWeeklyId(String prmProjectMainId) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(PrmGoodsArrivalAttribute.PRM_PROJECT_MAIN_ID, prmProjectMainId);
        List<PrmGoodsArrival> lstPrmGoodsArrival = PersistenceFactory.getInstance().findByAnyFields(PrmGoodsArrival.class, mapConditions, null);
        if (ListUtil.isNotEmpty(lstPrmGoodsArrival)) {
            return lstPrmGoodsArrival;
        }
        return null;
    }

    @Override
    public PrmGoodsArrival getPrmGoodsArrivalWeeklyIdForUUID(String uuid) {
//        Map<String,Object> mapConditions = new HashMap<String,Object>();
//        mapConditions.put(PrmWeeklyAttribute., uuid);
        PrmGoodsArrival prmGoodsArrival = PersistenceFactory.getInstance().findByPK(PrmGoodsArrival.class, uuid);
        if (StringUtil.isNotEmpty(prmGoodsArrival)) {
            return prmGoodsArrival;
        }
        return null;
    }

    //获取收款计量周报id
    @Override
    public List<PrmCollectionMeasure> getPrmCollectionMeasureWeeklyId(String prmProjectMainId) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(PrmCollectionMeasureAttribute.PRM_PROJECT_MAIN_ID, prmProjectMainId);
        List<PrmCollectionMeasure> lstPrmCollectionMeasure = PersistenceFactory.getInstance().findByAnyFields(PrmCollectionMeasure.class, mapConditions, null);
        if (ListUtil.isNotEmpty(lstPrmCollectionMeasure)) {
            return lstPrmCollectionMeasure;
        }
        return null;
    }

    @Override
    public PrmCollectionMeasure getPrmCollectionMeasureWeeklyIdForUUID(String uuid) {
//        Map<String,Object> mapConditions = new HashMap<String,Object>();
//        mapConditions.put(PrmWeeklyAttribute., uuid);
        PrmCollectionMeasure prmCollectionMeasure = PersistenceFactory.getInstance().findByPK(PrmCollectionMeasure.class, uuid);
        if (StringUtil.isNotEmpty(prmCollectionMeasure)) {
            return prmCollectionMeasure;
        }
        return null;
    }

    //获取工期进度周报id
    @Override
    public List<PrmProgress> getPrmProgressWeeklyId(String prmProjectMainId) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(PrmProgressAttribute.PRM_PROJECT_MAIN_ID, prmProjectMainId);
        List<PrmProgress> lstPrmProgress = PersistenceFactory.getInstance().findByAnyFields(PrmProgress.class, mapConditions, null);
        if (ListUtil.isNotEmpty(lstPrmProgress)) {
            return lstPrmProgress;
        }
        return null;
    }

    @Override
    public PrmProgress getPrmProgressWeeklyIdForUUID(String uuid) {
//        Map<String,Object> mapConditions = new HashMap<String,Object>();
//        mapConditions.put(PrmWeeklyAttribute., uuid);
        PrmProgress prmProgress = PersistenceFactory.getInstance().findByPK(PrmProgress.class, uuid);
        if (StringUtil.isNotEmpty(prmProgress)) {
            return prmProgress;
        }
        return null;
    }

    //获取问题申报周报id
    @Override
    public List<PrmProblem> getPrmProblemWeeklyId(String prmProjectMainId) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(PrmProblemAttribute.PRM_PROJECT_MAIN_ID, prmProjectMainId);
        List<PrmProblem> lstPrmProblem = PersistenceFactory.getInstance().findByAnyFields(PrmProblem.class, mapConditions, null);
        if (ListUtil.isNotEmpty(lstPrmProblem)) {
            return lstPrmProblem;
        }
        return null;
    }

    @Override
    public PrmProblem getPrmProblemWeeklyIdForUUID(String uuid) {
//        Map<String,Object> mapConditions = new HashMap<String,Object>();
//        mapConditions.put(PrmWeeklyAttribute., uuid);
        PrmProblem prmProblem = PersistenceFactory.getInstance().findByPK(PrmProblem.class, uuid);
        if (StringUtil.isNotEmpty(prmProblem)) {
            return prmProblem;
        }
        return null;
    }

    //获取会议纪要周报id
    @Override
    public List<PrmMeetingSummary> getPrmMeetingSummaryWeeklyId(String prmProjectMainId) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(PrmMeetingSummaryAttribute.PRM_PROJECT_MAIN_ID, prmProjectMainId);
        List<PrmMeetingSummary> lstPrmMeetingSummary = PersistenceFactory.getInstance().findByAnyFields(PrmMeetingSummary.class, mapConditions, null);
        if (ListUtil.isNotEmpty(lstPrmMeetingSummary)) {
            return lstPrmMeetingSummary;
        }
        return null;
    }

    @Override
    public PrmMeetingSummary getPrmMeetingSummaryWeeklyIdForUUID(String uuid) {
//        Map<String,Object> mapConditions = new HashMap<String,Object>();
//        mapConditions.put(PrmWeeklyAttribute., uuid);
        PrmMeetingSummary prmMeetingSummary = PersistenceFactory.getInstance().findByPK(PrmMeetingSummary.class, uuid);
        if (StringUtil.isNotEmpty(prmMeetingSummary)) {
            return prmMeetingSummary;
        }
        return null;
    }

    //    public boolean insertPrmWeeklyReceipt(String uuid) {
//        boolean result = false;
////
////    String sql =  "INSERT INTO PRM_WEEKLY_RECEIPT('"+PrmWeeklyReceiptAttribute.PAYER +"','"+ PrmWeeklyReceiptAttribute.CUSTOMER_ID+"','"
////            +PrmWeeklyReceiptAttribute.ESTIMATE_MONEY+"','"+PrmWeeklyReceiptAttribute.ACTUAL_MONEY+"','"+PrmWeeklyReceiptAttribute.MONEY_TYPE+
////            "','"+PrmWeeklyReceiptAttribute.ESTIMATE_DATE+"','"+PrmWeeklyReceiptAttribute.ACTUAL_DATE+"','"+PrmWeeklyReceiptAttribute.PRM_UNKNOWN_RECEIPTS_ID+
////            "','"+PrmWeeklyReceiptAttribute.STATE+"')'"+"'VALUES('"+
//        PrmWeeklyReceipt pwr = new PrmWeeklyReceipt();
//        pwr.setEditflag("fd");
//        pwr.setPrmWeeklyId("fds");
//        PersistenceFactory.getInstance().insert(pwr);
//        if(count==1){
//            result=true;
//        }
//        return result;
//    }
    //收款情况
    @Override
    public List<PrmWeeklyReceipt> insertPrmWeeklyReceipt(String prmProjectMainId) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(PrmReceiptsAttribute.PRM_PROJECT_MAIN_ID, prmProjectMainId);
        List<PrmReceipts> lstPrmReceipts = PersistenceFactory.getInstance().findByAnyFields(PrmReceipts.class, mapConditions, null);
        if (ListUtil.isNotEmpty(lstPrmReceipts)) {
            List<PrmWeeklyReceipt> pwrList = new ArrayList<PrmWeeklyReceipt>();
            for (int i = 0; i < lstPrmReceipts.size(); i++) {
                PrmWeeklyReceipt pwr = new PrmWeeklyReceipt();
                pwr.setPrmProjectMainId(lstPrmReceipts.get(i).getPrmProjectMainId());
                pwr.setPrmReceiptsId(lstPrmReceipts.get(i).getUuid());
                pwr.setPayer(lstPrmReceipts.get(i).getPayer());
                pwr.setCustomerId(lstPrmReceipts.get(i).getCustomerId());
                pwr.setEstimateMoney(lstPrmReceipts.get(i).getEstimateMoney());
                pwr.setActualMoney(lstPrmReceipts.get(i).getActualMoney());
                pwr.setMoneyType(lstPrmReceipts.get(i).getMoneyType());
                pwr.setEstimateDate(lstPrmReceipts.get(i).getEstimateDate());
                pwr.setActualDate(lstPrmReceipts.get(i).getActualDate());
                pwr.setPrmUnknownReceiptsId(lstPrmReceipts.get(i).getPrmUnknownReceiptsId());
                pwr.setState(lstPrmReceipts.get(i).getState());
                pwrList.add(pwr);
            }
            return pwrList;
        }
        return null;

    }

    //支付情况
    @Override
    public List<PrmWeeklyPay> insertPrmWeeklyPay(String prmProjectMainId) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(FadPayReqCAttribute.PROJECT_MAIN_ID, prmProjectMainId);
        List<FadPayReqC> lstFadPayReqC = PersistenceFactory.getInstance().findByAnyFields(FadPayReqC.class, mapConditions, null);
        if (ListUtil.isNotEmpty(lstFadPayReqC)) {
            List<PrmWeeklyPay> pwpList = new ArrayList<PrmWeeklyPay>();
            for (int i = 0; i < lstFadPayReqC.size(); i++) {
                PrmWeeklyPay pwp = new PrmWeeklyPay();
                pwp.setPrmProjectMainId(lstFadPayReqC.get(i).getProjectMainId());
                pwp.setPrmPayReqId(lstFadPayReqC.get(i).getUuid());
                pwp.setScmContractId(lstFadPayReqC.get(i).getScmContractId());
//                pwp.setAccountsPayable(lstFadPayReqC.get(i).getAccountsPayable());
//                pwp.setPaidMoney(lstFadPayReqC.get(i).getPaidMoney());
                pwp.setReqMoney(lstFadPayReqC.get(i).getReqMoney());
                pwp.setApproveMoney(lstFadPayReqC.get(i).getApproveMoney());
                pwp.setState(lstFadPayReqC.get(i).getState());
                pwpList.add(pwp);
            }

            return pwpList;
        }
        return null;

    }

    @Override
    public void setExtraData(Map outMap) {
        Map dtoMap = (Map) outMap.get("prmWeeklyDto");
        if (dtoMap != null) {
            //1.设置项目名的显示
            String projectName = "";
            String prmProjectMainId = dtoMap.get("prmProjectMainId") + "";
            PrmProjectMain prmProjectMain = PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, prmProjectMainId);
            if (prmProjectMain != null) {
                projectName = prmProjectMain.getProjectName();
            }
            dtoMap.put(PrmWeeklyAttribute.PROJECT_NAME, projectName);
            //2.设置部门
            if (StringUtil.isNotEmpty((String) dtoMap.get("departmentCode"))) {
                dtoMap.put("departmentCodeDesc", OrgHelper.getOrgNameByCode((String) dtoMap.get("departmentCode")));
            }

        }

        List<Map> prmMemberTrendList = (List) dtoMap.get("prmMemberTrendDto");        //人员动向
        List<Map> prmGoodsArrivalList = (List) dtoMap.get("prmGoodsArrivalDto");        //到货确认
        List<Map> prmCollectionMeasureList = (List) dtoMap.get("prmCollectionMeasureDto");//        收款计量
        List<Map> prmProgressList = (List) dtoMap.get("prmProgressDto");//        工期进度
        List<Map> prmProblemList = (List) dtoMap.get("prmProblemDto");//        问题申报
        List staffId = new ArrayList();//user
        List signId = new ArrayList();//签字状态
        List projectProgressId = new ArrayList();//坤进度

        //收集需要转码字段值
        if (prmMemberTrendList != null) {
            for (Map s : prmMemberTrendList) {
                if (StringUtil.isNotEmpty(s.get(PrmMemberTrendAttribute.STAFF_ID)))
                    staffId.add(s.get(PrmMemberTrendAttribute.STAFF_ID));
            }
        }
        if (prmGoodsArrivalList != null) {
            for (Map s : prmGoodsArrivalList) {
                if (StringUtil.isNotEmpty(s.get(PrmGoodsArrivalAttribute.REGISTRANT)))
                    staffId.add(s.get(PrmGoodsArrivalAttribute.REGISTRANT));
            }
        }
        if (prmCollectionMeasureList != null) {
            for (Map s : prmCollectionMeasureList) {
                if (StringUtil.isNotEmpty(s.get(PrmCollectionMeasureAttribute.PRINCIPAL)))
                    staffId.add(s.get(PrmCollectionMeasureAttribute.PRINCIPAL));
                if (StringUtil.isNotEmpty(s.get(PrmCollectionMeasureAttribute.SIGN)))
                    signId.add(s.get(PrmCollectionMeasureAttribute.SIGN));
            }
        }
        if (prmProgressList != null) {
            for (Map s : prmProgressList) {
                if (StringUtil.isNotEmpty(s.get("projectProgress")))
                    projectProgressId.add(s.get("projectProgress"));
            }
        }
        if (prmProblemList != null) {
            for (Map s : prmProblemList) {
                if (StringUtil.isNotEmpty(s.get(PrmProgressAttribute.PROJECT_PROGRESS)))
                    staffId.add(s.get(PrmProgressAttribute.PROJECT_PROGRESS));
                if (StringUtil.isNotEmpty(s.get(PrmProblemAttribute.PROVIDER)))
                    staffId.add(s.get(PrmProblemAttribute.PROVIDER));
                if (StringUtil.isNotEmpty(s.get(PrmProblemAttribute.POST_PERSON)))
                    staffId.add(s.get(PrmProblemAttribute.POST_PERSON));
            }
        }

        //人员code to name
        if (staffId.size() > 0) {
            Map staffIdMap = new HashMap();
            staffIdMap.put(ScdpUserAttribute.USER_ID, staffId);
            List<ScdpUser> userList = PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, staffIdMap, null);
            if (ListUtil.isNotEmpty(userList)) {
                for (ScdpUser user : userList) {
                    if (prmMemberTrendList != null) {
                        for (Map s : prmMemberTrendList) {
                            if (user.getUserId().equals(s.get(PrmMemberTrendAttribute.STAFF_ID))) {
                                s.put(PrmMemberTrendAttribute.STAFF_NAME, user.getUserName());
                            }
                        }
                    }
                    if (prmGoodsArrivalList != null) {
                        for (Map s : prmGoodsArrivalList) {
                            if (user.getUserId().equals(s.get(PrmGoodsArrivalAttribute.REGISTRANT))) {
                                s.put(PrmGoodsArrivalAttribute.REGISTRANT_NAME, user.getUserName());
                            }
                        }
                    }

                    if (prmCollectionMeasureList != null) {
                        for (Map s : prmCollectionMeasureList) {
                            if (user.getUserId().equals(s.get(PrmCollectionMeasureAttribute.PRINCIPAL))) {
                                s.put(PrmCollectionMeasureAttribute.PRINCIPAL_NAME, user.getUserName());
                            }
                        }
                    }

                    if (prmProblemList != null) {
                        for (Map s : prmProblemList) {
                            if (user.getUserId().equals(s.get(PrmProblemAttribute.POST_PERSON))) {
                                s.put(PrmProblemAttribute.POST_PERSON_NAME, user.getUserName());
                            }
                            if (user.getUserId().equals(s.get(PrmProblemAttribute.PROVIDER))) {
                                s.put(PrmProblemAttribute.PROVIDER_NAME, user.getUserName());
                            }
                        }
                    }
                }
            }
        }

        //签字状态code to name
        if (signId.size() > 0) {
//            Map signIdMap = new HashMap();
//            signIdMap.put(ScdpCodeAttribute.SYS_CODE, signId);
            List<Map> scdpCodeList = ErpCodeHelper.findByCodeType("PRM_MEASURE_SIGN");
            if (ListUtil.isNotEmpty(scdpCodeList)) {
                for (Map scdpCode : scdpCodeList) {
                    if (prmCollectionMeasureList != null) {
                        for (Map s : prmCollectionMeasureList) {
                            if (s.get(PrmCollectionMeasureAttribute.SIGN).equals(scdpCode.get(ScdpCodeAttribute.SYS_CODE))) {
                                s.put(PrmCollectionMeasureAttribute.SIGN, scdpCode.get(ScdpCodeAttribute.CODE_DESC));
                            }
                        }
                    }
                }
            }
        }

        //项目进度code to name
        if (projectProgressId.size() > 0) {
            List<Map> scdpCodeList = ErpCodeHelper.findByCodeType("PRM_PROGRESS");
            if (ListUtil.isNotEmpty(scdpCodeList)) {
                for (Map scdpCode : scdpCodeList) {
                    if (prmProgressList != null) {
                        for (Map s : prmProgressList) {
                            if (s.get(PrmProgressAttribute.PROJECT_PROGRESS).equals(scdpCode.get(ScdpCodeAttribute.SYS_CODE))) {
                                s.put(PrmProgressAttribute.PROJECT_PROGRESS, scdpCode.get(ScdpCodeAttribute.CODE_DESC));
                            }
                        }
                    }
                }
            }
        }
    }
}