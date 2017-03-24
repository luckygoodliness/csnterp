package com.csnt.scdp.bizmodules.modules.nonprm.nonprmpurchasereq.dto;

import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetCD;
import com.csnt.scdp.bizmodules.entity.prm.PrmPurchaseReq;
import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;

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
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetCD")
public class NonprmBudgetCDDto extends NonProjectBudgetCD {
    @CascadeChilds(FK = "prmPurchaseReqId|uuid")
	private List<PrmPurchaseReqDetailDto> prmPurchaseReqDetailDto;

	public List<PrmPurchaseReqDetailDto> getPrmPurchaseReqDetailDto() {
		return prmPurchaseReqDetailDto;
	}

	public void setPrmPurchaseReqDetailDto(List<PrmPurchaseReqDetailDto> childDto) {
		this.prmPurchaseReqDetailDto = childDto;
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

}