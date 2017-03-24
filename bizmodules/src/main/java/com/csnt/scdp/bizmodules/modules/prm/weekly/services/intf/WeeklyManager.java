package com.csnt.scdp.bizmodules.modules.prm.weekly.services.intf;

import com.csnt.scdp.bizmodules.entity.prm.*;

import java.util.List;
import java.util.Map;

/**
 * Description:  WeeklyManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-25 14:01:29
 */
public interface WeeklyManager {
    //项目周报
    public PrmWeekly getPrmProjectMainId(String uuid);
    //项目简报
    public List<PrmBrief> getPrmBriefWeeklyId(String prmProjectMainId);
    public PrmBrief getPrmBriefWeeklyIdForUUID(String uuid);
    //人员动向
    public List<PrmMemberTrend> getPrmMemberTrendWeeklyId(String prmProjectMainId);
    public PrmMemberTrend getPrmMemberTrendWeeklyIdForUUID(String uuid);
    //到货确认
    public List<PrmGoodsArrival> getPrmGoodsArrivalWeeklyId(String prmProjectMainId);
    public PrmGoodsArrival getPrmGoodsArrivalWeeklyIdForUUID(String uuid);
    //收款计量
    public List<PrmCollectionMeasure> getPrmCollectionMeasureWeeklyId(String prmProjectMainId);
    public PrmCollectionMeasure getPrmCollectionMeasureWeeklyIdForUUID(String uuid);
    //工期进度
    public List<PrmProgress> getPrmProgressWeeklyId(String prmProjectMainId);
    public PrmProgress getPrmProgressWeeklyIdForUUID(String uuid);
    //问题申报
    public List<PrmProblem> getPrmProblemWeeklyId(String prmProjectMainId);
    public PrmProblem getPrmProblemWeeklyIdForUUID(String uuid);
    //会议纪要
    public List<PrmMeetingSummary> getPrmMeetingSummaryWeeklyId(String prmProjectMainId);
    public PrmMeetingSummary getPrmMeetingSummaryWeeklyIdForUUID(String uuid);
    //项目收款
    public List<PrmWeeklyReceipt> insertPrmWeeklyReceipt(String prmProjectMainId);
    //支付情况
    public List<PrmWeeklyPay> insertPrmWeeklyPay(String prmProjectMainId);
    //1.额外设置需要返回的数据
    void setExtraData(Map outMap);
}