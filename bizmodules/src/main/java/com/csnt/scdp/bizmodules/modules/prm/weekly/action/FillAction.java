package com.csnt.scdp.bizmodules.modules.prm.weekly.action;

import com.csnt.scdp.bizmodules.entity.prm.*;
import com.csnt.scdp.bizmodules.modules.prm.weekly.services.intf.WeeklyManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/4.
 */
@Scope("singleton")
@Controller("weekly-fill")
@Transactional
public class FillAction implements IAction{
    private static ILogTracer tracer = LogTracerFactory.getInstance(FillAction.class);

    @Resource(name = "weekly-manager")
    private WeeklyManager weeklyManager;

    //给相应的子表加上关联关系(prm_weekly_id)
    public Map perform(Map inMap) {
        PersistenceCrudManager pcm= PersistenceFactory.getInstance();
        String uuid=(String)inMap.get("uuid");
        if(StringUtil.isNotEmpty(uuid)){
            PrmWeekly prmWeekly=weeklyManager.getPrmProjectMainId(uuid);
            String projectId=prmWeekly.getPrmProjectMainId();
            String Uuid=prmWeekly.getUuid();

            //项目简报
            List<PrmBrief> prmBriefList=weeklyManager.getPrmBriefWeeklyId(projectId);
            if(ListUtil.isNotEmpty(prmBriefList)){
                for(int i=0;i<prmBriefList.size();i++){
                    if(StringUtil.isEmpty(prmBriefList.get(i).getPrmWeeklyId())){
                        prmBriefList.get(i).setPrmWeeklyId(uuid);
                    }
                }
                pcm.batchUpdate(prmBriefList);
            }
            //人员动向
            List<PrmMemberTrend> prmMemberTrendList=weeklyManager.getPrmMemberTrendWeeklyId(projectId);
            if(ListUtil.isNotEmpty(prmMemberTrendList)){
                for(PrmMemberTrend prmMemberTrend:prmMemberTrendList){
                    if(StringUtil.isEmpty(prmMemberTrend.getPrmWeeklyId())){
                        prmMemberTrend.setPrmWeeklyId(uuid);
                    }
                }
                pcm.batchUpdate(prmMemberTrendList);
            }

            //到货确认
            List<PrmGoodsArrival> prmGoodsArrivalList=weeklyManager.getPrmGoodsArrivalWeeklyId(projectId);
            if(ListUtil.isNotEmpty(prmGoodsArrivalList)){
                for(PrmGoodsArrival prmGoodsArrival:prmGoodsArrivalList){
                    if(StringUtil.isEmpty(prmGoodsArrival.getPrmWeeklyId())){
                        prmGoodsArrival.setPrmWeeklyId(uuid);
                        pcm.update(prmGoodsArrival);
                    }
                }
                pcm.batchUpdate(prmGoodsArrivalList);
            }
            //收款计量
            List<PrmCollectionMeasure> prmCollectionMeasureList=weeklyManager.getPrmCollectionMeasureWeeklyId(projectId);
            if(ListUtil.isNotEmpty(prmCollectionMeasureList)){
                for(PrmCollectionMeasure prmCollectionMeasure:prmCollectionMeasureList){
                    if(StringUtil.isEmpty(prmCollectionMeasure.getPrmWeeklyId())){
                        prmCollectionMeasure.setPrmWeeklyId(uuid);
                        pcm.update(prmCollectionMeasure);
                    }
                }
                pcm.batchUpdate(prmCollectionMeasureList);
            }
            //工期进度
            List<PrmProgress> prmProgressList=weeklyManager.getPrmProgressWeeklyId(projectId);
            if(ListUtil.isNotEmpty(prmProgressList)){
                for(PrmProgress prmProgress:prmProgressList){
                    if(StringUtil.isEmpty(prmProgress.getPrmWeeklyId())){
                        prmProgress.setPrmWeeklyId(uuid);
                        pcm.update(prmProgress);
                    }
                }
                pcm.batchUpdate(prmProgressList);
            }
            //问题申报
            List<PrmProblem> prmProblemList=weeklyManager.getPrmProblemWeeklyId(projectId);
            if(ListUtil.isNotEmpty(prmProblemList)){
                for(PrmProblem prmProblem:prmProblemList){
                    if(StringUtil.isEmpty(prmProblem.getPrmWeeklyId())){
                        prmProblem.setPrmWeeklyId(uuid);
                        pcm.update(prmProblem);
                    }
                }
                pcm.batchUpdate(prmProblemList);
            }
            //会议纪要
            List<PrmMeetingSummary> prmMeetingSummaryList=weeklyManager.getPrmMeetingSummaryWeeklyId(projectId);
            if(ListUtil.isNotEmpty(prmMeetingSummaryList)){
                for(PrmMeetingSummary prmMeetingSummary:prmMeetingSummaryList){
                    if(StringUtil.isEmpty(prmMeetingSummary.getPrmWeeklyId())){
                        prmMeetingSummary.setPrmWeeklyId(uuid);
                        pcm.update(prmMeetingSummary);
                    }
                }
                pcm.batchUpdate(prmMeetingSummaryList);
            }
            //收款情况

            List<PrmWeeklyReceipt> prmWeeklyReceiptList=weeklyManager.insertPrmWeeklyReceipt(projectId);
            if(ListUtil.isNotEmpty(prmWeeklyReceiptList)){
                for(int i=0;i<prmWeeklyReceiptList.size();i++){
                    prmWeeklyReceiptList.get(i).setPrmWeeklyId(uuid);
                }
                pcm.batchInsert(prmWeeklyReceiptList);
            }
            //支付情况
            List<PrmWeeklyPay> prmWeeklyPayList=weeklyManager.insertPrmWeeklyPay(projectId);
            if(ListUtil.isNotEmpty(prmWeeklyPayList)){
                for(int i=0;i<prmWeeklyPayList.size();i++){
                    prmWeeklyPayList.get(i).setPrmWeeklyId(uuid);
                }
                pcm.batchInsert(prmWeeklyReceiptList);
            }

        }
        return  null;
    }
}
