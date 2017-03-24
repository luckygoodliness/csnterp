package com.csnt.scdp.bizmodules.modules.prm.weekly.dto;

import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.prm.PrmWeekly;

import java.util.List;


/**
 * Description:  PrmWeeklyDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-25 14:01:26
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.prm.PrmWeekly")
public class PrmWeeklyDto extends PrmWeekly {
	@CascadeChilds(FK = "prmWeeklyId|uuid",cascadeDelete = false)
	private List<PrmBriefDto> prmBriefDto;

	@CascadeChilds(FK = "prmWeeklyId|uuid",cascadeDelete = false)
	private List<PrmMemberTrendDto> prmMemberTrendDto;

	@CascadeChilds(FK = "prmWeeklyId|uuid",cascadeDelete = false)
	private List<PrmGoodsArrivalDto> prmGoodsArrivalDto;

	@CascadeChilds(FK = "prmWeeklyId|uuid",cascadeDelete = false)
	private List<PrmCollectionMeasureDto> prmCollectionMeasureDto;

	@CascadeChilds(FK = "prmWeeklyId|uuid",cascadeDelete = false)
	private List<PrmProgressDto> prmProgressDto;

	@CascadeChilds(FK = "prmWeeklyId|uuid",cascadeDelete = false)
	private List<PrmProblemDto> prmProblemDto;

	@CascadeChilds(FK = "prmWeeklyId|uuid",cascadeDelete = false)
	private List<PrmMeetingSummaryDto> prmMeetingSummaryDto;

	@CascadeChilds(FK = "prmWeeklyId|uuid")
	private List<PrmWeeklyReceiptDto> prmWeeklyReceiptDto;

	@CascadeChilds(FK = "prmWeeklyId|uuid")
	private List<PrmWeeklyPayDto> prmWeeklyPayDto;



	public List<PrmBriefDto> getPrmBriefDto() {
		return prmBriefDto;
	}

	public void setPrmBriefDto(List<PrmBriefDto> childDto) {
		this.prmBriefDto = childDto;
	}

	public List<PrmMemberTrendDto> getPrmMemberTrendDto() {
		return prmMemberTrendDto;
	}

	public void setPrmMemberTrendDto(List<PrmMemberTrendDto> childDto) {
		this.prmMemberTrendDto = childDto;
	}

	public List<PrmGoodsArrivalDto> getPrmGoodsArrivalDto() {
		return prmGoodsArrivalDto;
	}

	public void setPrmGoodsArrivalDto(List<PrmGoodsArrivalDto> childDto) {
		this.prmGoodsArrivalDto = childDto;
	}

	public List<PrmCollectionMeasureDto> getPrmCollectionMeasureDto() {
		return prmCollectionMeasureDto;
	}

	public void setPrmCollectionMeasureDto(List<PrmCollectionMeasureDto> childDto) {
		this.prmCollectionMeasureDto = childDto;
	}

	public List<PrmProgressDto> getPrmProgressDto() {
		return prmProgressDto;
	}

	public void setPrmProgressDto(List<PrmProgressDto> childDto) {
		this.prmProgressDto = childDto;
	}

	public List<PrmProblemDto> getPrmProblemDto() {
		return prmProblemDto;
	}

	public void setPrmProblemDto(List<PrmProblemDto> childDto) {
		this.prmProblemDto = childDto;
	}

	public List<PrmMeetingSummaryDto> getPrmMeetingSummaryDto() {
		return prmMeetingSummaryDto;
	}

	public void setPrmMeetingSummaryDto(List<PrmMeetingSummaryDto> childDto) {
		this.prmMeetingSummaryDto = childDto;
	}

	public List<PrmWeeklyReceiptDto> getPrmWeeklyReceiptDto() {
		return prmWeeklyReceiptDto;
	}

	public void setPrmWeeklyReceiptDto(List<PrmWeeklyReceiptDto> childDto) {
		this.prmWeeklyReceiptDto = childDto;
	}

	public List<PrmWeeklyPayDto> getPrmWeeklyPayDto() {
		return prmWeeklyPayDto;
	}

	public void setPrmWeeklyPayDto(List<PrmWeeklyPayDto> childDto) {
		this.prmWeeklyPayDto = childDto;
	}

	private String departmentCodeDesc;

	public String getDepartmentCodeDesc() {
		return departmentCodeDesc;
	}

	public void setDepartmentCodeDesc(String departmentCodeDesc) {
		this.departmentCodeDesc = departmentCodeDesc;
	}
}