package com.csnt.scdp.bizmodules.modules.scm.supplierinfor.dto;

import com.csnt.scdp.bizmodules.entity.scm.ScmSupplier;
import com.csnt.scdp.bizmodules.modules.cdm.cdmfile.dto.CdmFileRelationDto;
import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;

import java.util.List;


/**
 * Description:  ScmSupplierDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-24 16:02:00
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.scm.ScmSupplier")
public class ScmSupplierDto extends ScmSupplier {
	@CascadeChilds(FK = "scmSupplierId|uuid")
	private List<ScmSupplierBankDto> scmSupplierBankDto;

	@CascadeChilds(FK = "scmSupplierId|uuid")
	private List<ScmSupplierContactsDto> scmSupplierContactsDto;

	@CascadeChilds(FK = "scmSupplierId|uuid")
	private List<ScmSukpplierKeyDto> scmSukpplierKeyDto;

	@CascadeChilds(FK = "scmSupplierId|uuid")
	private List<PrmBlacklistMonthDto> prmBlacklistMonthDto;

	@CascadeChilds(FK = "dataId|uuid")
	private List<CdmFileRelationDto> cdmFileRelationDto;

	@CascadeChilds(FK = "scmSupplierId|uuid")
	private List<ScmSupplierEvaluationDto> scmSupplierEvaluationDto;

	public List<ScmSupplierEvaluationDto> getScmSupplierEvaluationDto() {
		return scmSupplierEvaluationDto;
	}

	public void setScmSupplierEvaluationDto(List<ScmSupplierEvaluationDto> childDto) {
		this.scmSupplierEvaluationDto = childDto;
	}
	public List<CdmFileRelationDto> getCdmFileRelationDto() {
		return cdmFileRelationDto;
	}

	public void setCdmFileRelationDto(List<CdmFileRelationDto> childDto) {
		this.cdmFileRelationDto = childDto;
	}

	public List<ScmSupplierBankDto> getScmSupplierBankDto() {
		return scmSupplierBankDto;
	}

	public void setScmSupplierBankDto(List<ScmSupplierBankDto> childDto) {
		this.scmSupplierBankDto = childDto;
	}

	public List<ScmSupplierContactsDto> getScmSupplierContactsDto() {
		return scmSupplierContactsDto;
	}

	public void setScmSupplierContactsDto(List<ScmSupplierContactsDto> childDto) {
		this.scmSupplierContactsDto = childDto;
	}

	public List<ScmSukpplierKeyDto> getScmSukpplierKeyDto() {
		return scmSukpplierKeyDto;
	}

	public void setScmSukpplierKeyDto(List<ScmSukpplierKeyDto> childDto) {
		this.scmSukpplierKeyDto = childDto;
	}

	public List<PrmBlacklistMonthDto> getPrmBlacklistMonthDto() {
		return prmBlacklistMonthDto;
	}

	public void setPrmBlacklistMonthDto(List<PrmBlacklistMonthDto> childDto) {
		this.prmBlacklistMonthDto = childDto;
	}

}