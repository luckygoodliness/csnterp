package com.csnt.scdp.bizmodules.modules.nonprm.nonprmpurchasereq.dto;

import com.csnt.scdp.bizmodules.modules.cdm.cdmfile.dto.CdmFileRelationDto;
import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.prm.PrmPurchaseReq;

import java.math.BigDecimal;
import java.util.List;


/**
 * Description:  PrmPurchaseReqDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-11-19 14:00:14
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.prm.PrmPurchaseReq")
public class PrmPurchaseReqDto extends PrmPurchaseReq {
    @CascadeChilds(FK = "prmPurchaseReqId|uuid")
	private List<PrmPurchaseReqDetailDto> prmPurchaseReqDetailDto;
    @CascadeChilds(FK = "dataId|uuid")
    private List<CdmFileRelationDto> cdmFileRelationDto;

	public List<PrmPurchaseReqDetailDto> getPrmPurchaseReqDetailDto() {
		return prmPurchaseReqDetailDto;
	}

	public void setPrmPurchaseReqDetailDto(List<PrmPurchaseReqDetailDto> childDto) {
		this.prmPurchaseReqDetailDto = childDto;
	}
    public List<CdmFileRelationDto> getCdmFileRelationDto() {
        return cdmFileRelationDto;
    }

    public void setCdmFileRelationDto(List<CdmFileRelationDto> childDto) {
        this.cdmFileRelationDto = childDto;
    }

    private String orgName = "";

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getStateNm() {
        return stateNm;
    }

    public void setStateNm(String stateNm) {
        this.stateNm = stateNm;
    }

    private String stateNm="";


    public String getFinancialSubject() {
        return financialSubject;
    }

    public void setFinancialSubject(String financialSubject) {
        this.financialSubject = financialSubject;
    }

    private String financialSubject="";

    public String getFinancialSubjectCode() {
        return financialSubjectCode;
    }

    public void setFinancialSubjectCode(String financialSubjectCode) {
        this.financialSubjectCode = financialSubjectCode;
    }

    private String financialSubjectCode="";


    private BigDecimal checkRemainMoney;

    public BigDecimal getCheckRemainMoney() {
        return checkRemainMoney;
    }

    public void setCheckRemainMoney(BigDecimal checkRemainMoney) {
        this.checkRemainMoney = checkRemainMoney;
    }
}