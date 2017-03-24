package com.csnt.scdp.bizmodules.modules.prm.weekly.action;

import com.csnt.scdp.bizmodules.entity.prm.*;
import com.csnt.scdp.bizmodules.modules.prm.weekly.dto.*;
import com.csnt.scdp.bizmodules.modules.prm.weekly.services.intf.WeeklyManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.CommonModifyAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description:  ModifyAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-25 14:01:26
 */

@Scope("singleton")
@Controller("weekly-modify")
@Transactional
public class ModifyAction extends CommonModifyAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(ModifyAction.class);

	@Resource(name = "weekly-manager")
	private WeeklyManager weeklyManager;

	@Override
    //"-"是删除关联关系(prm_weekly_id设为空)
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
        List<String>lstBriefUuid=new ArrayList<>();
        List<String>lstMemberTrendUuid=new ArrayList<>();
        List<String>lstGoodsArrivalUuid=new ArrayList<>();
        List<String>lstCollectionMeasureUuid=new ArrayList<>();
        List<String>lstProcessUuid=new ArrayList<>();
        List<String>lstProblemUuid=new ArrayList<>();
        List<String>lstMeetingSummaryUuid=new ArrayList<>();



        String dtoClass = (String) inMap.get(CommonAttribute.DTO_CLASS_NAME);
        Map viewMap = (Map) inMap.get(CommonAttribute.VIEW_DATA);
        BasePojo dtoObj = DtoHelper.getDtoFromMap(viewMap, dtoClass);
        PrmWeeklyDto prmWeekly=(PrmWeeklyDto)dtoObj;

//项目简报
        List<PrmBriefDto> lstPrmBriefDto=prmWeekly.getPrmBriefDto();
        if(ListUtil.isNotEmpty(lstPrmBriefDto)){
            for(PrmBriefDto prmBriefDto:lstPrmBriefDto){
                if(StringUtil.isEmpty(prmBriefDto.getPrmWeeklyId()) ){
                    lstBriefUuid.add(prmBriefDto.getUuid());
                }
            }
        }
//人员动向
        List<PrmMemberTrendDto> lstPrmMemberTrendDto=prmWeekly.getPrmMemberTrendDto();
        if(ListUtil.isNotEmpty(lstPrmMemberTrendDto)){
            for(PrmMemberTrendDto prmMemberTrendDto:lstPrmMemberTrendDto){
                if(StringUtil.isEmpty(prmMemberTrendDto.getPrmWeeklyId()) ){
                    lstMemberTrendUuid.add(prmMemberTrendDto.getUuid());
                }
            }
        }
//到货确认
        List<PrmGoodsArrivalDto> lstPrmGoodsArrivalDto=prmWeekly.getPrmGoodsArrivalDto();
        if(ListUtil.isNotEmpty(lstPrmGoodsArrivalDto)){
            for(PrmGoodsArrivalDto prmGoodsArrivalDto:lstPrmGoodsArrivalDto){
                if(StringUtil.isEmpty(prmGoodsArrivalDto.getPrmWeeklyId()) ){
                    lstGoodsArrivalUuid.add(prmGoodsArrivalDto.getUuid());
                }
            }
        }
//收款计量
        List<PrmCollectionMeasureDto> lstPrmCollectionMeasureDto=prmWeekly.getPrmCollectionMeasureDto();
        if(ListUtil.isNotEmpty(lstPrmCollectionMeasureDto)){
            for(PrmCollectionMeasureDto prmCollectionMeasureDto:lstPrmCollectionMeasureDto){
                if(StringUtil.isEmpty(prmCollectionMeasureDto.getPrmWeeklyId()) ){
                    lstCollectionMeasureUuid.add(prmCollectionMeasureDto.getUuid());
                }
            }
        }
//工期进度
        List<PrmProgressDto> lstPrmProgressDto=prmWeekly.getPrmProgressDto();
        if(ListUtil.isNotEmpty(lstPrmProgressDto)){
            for(PrmProgressDto prmProgressDto:lstPrmProgressDto){
                if(StringUtil.isEmpty(prmProgressDto.getPrmWeeklyId()) ){
                    lstProcessUuid.add(prmProgressDto.getUuid());
                }
            }
        }
//问题申报
        List<PrmProblemDto> lstPrmProblemDto=prmWeekly.getPrmProblemDto();
        if(ListUtil.isNotEmpty(lstPrmProblemDto)){
            for(PrmProblemDto prmProblemDto:lstPrmProblemDto){
                if(StringUtil.isEmpty(prmProblemDto.getPrmWeeklyId()) ){
                    lstProblemUuid.add(prmProblemDto.getUuid());
                }
            }
        }
//会议纪要
        List<PrmMeetingSummaryDto> lstPrmMeetingSummaryDto=prmWeekly.getPrmMeetingSummaryDto();
        if(ListUtil.isNotEmpty(lstPrmMeetingSummaryDto)){
            for(PrmMeetingSummaryDto prmMeetingSummaryDto:lstPrmMeetingSummaryDto){
                if(StringUtil.isEmpty(prmMeetingSummaryDto.getPrmWeeklyId()) ){
                    lstMeetingSummaryUuid.add(prmMeetingSummaryDto.getUuid());
                }
            }
        }


		Map out = super.perform(inMap);
		//Do After
        //项目简报
        if(ListUtil.isNotEmpty(lstBriefUuid)){
            for(String uuid:lstBriefUuid){
                PrmBrief prmBrief = weeklyManager.getPrmBriefWeeklyIdForUUID(uuid);
                if (prmBrief !=null && StringUtil.isNotEmpty(prmBrief.getPrmWeeklyId())) {
                prmBrief.setPrmWeeklyId(null);

              }
            }
        }
        //人员动向
        if(ListUtil.isNotEmpty(lstMemberTrendUuid)){
            for(String uuid:lstMemberTrendUuid){
                PrmMemberTrend prmMemberTrend = weeklyManager.getPrmMemberTrendWeeklyIdForUUID(uuid);
                if (prmMemberTrend != null && StringUtil.isNotEmpty(prmMemberTrend.getPrmWeeklyId())) {
                    prmMemberTrend.setPrmWeeklyId(null);

                }
            }
        }
        //到货确认
        if(ListUtil.isNotEmpty(lstGoodsArrivalUuid)){
            for(String uuid:lstGoodsArrivalUuid){
               PrmGoodsArrival prmGoodsArrival = weeklyManager.getPrmGoodsArrivalWeeklyIdForUUID(uuid);
                if (prmGoodsArrival != null && StringUtil.isNotEmpty(prmGoodsArrival.getPrmWeeklyId())) {
                    prmGoodsArrival.setPrmWeeklyId(null);

                }
            }
        }
        //收款计量
        if(ListUtil.isNotEmpty(lstCollectionMeasureUuid)){
            for(String uuid:lstCollectionMeasureUuid){
                PrmCollectionMeasure prmCollectionMeasure = weeklyManager.getPrmCollectionMeasureWeeklyIdForUUID(uuid);
                if (prmCollectionMeasure != null && StringUtil.isNotEmpty(prmCollectionMeasure.getPrmWeeklyId())) {
                    prmCollectionMeasure.setPrmWeeklyId(null);

                }
            }
        }
        //工期进度
        if(ListUtil.isNotEmpty(lstProcessUuid)){
            for(String uuid:lstProcessUuid){
                PrmProgress prmProgress = weeklyManager.getPrmProgressWeeklyIdForUUID(uuid);
                if (prmProgress != null && StringUtil.isNotEmpty(prmProgress.getPrmWeeklyId())) {
                    prmProgress.setPrmWeeklyId(null);

                }
            }
        }
        //问题申报
        if(ListUtil.isNotEmpty(lstProblemUuid)){
            for(String uuid:lstProblemUuid){
                PrmProblem prmProblem = weeklyManager.getPrmProblemWeeklyIdForUUID(uuid);
                if (prmProblem != null && StringUtil.isNotEmpty(prmProblem.getPrmWeeklyId())) {
                    prmProblem.setPrmWeeklyId(null);

                }
            }
        }
        //会议纪要
        if(ListUtil.isNotEmpty(lstMeetingSummaryUuid)){
            for(String uuid:lstMeetingSummaryUuid){
                PrmMeetingSummary prmMeetingSummary = weeklyManager.getPrmMeetingSummaryWeeklyIdForUUID(uuid);
                if (prmMeetingSummary != null && StringUtil.isNotEmpty(prmMeetingSummary.getPrmWeeklyId())) {
                    prmMeetingSummary.setPrmWeeklyId(null);

                }
            }
        }

		return out;
	}

}
