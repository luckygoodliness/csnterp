package com.csnt.scdp.bizmodules.modules.scm.supplierinfochange.dto;

import com.csnt.scdp.bizmodules.modules.cdm.cdmfile.dto.CdmFileRelationDto;
import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.scm.ScmSupplierC;

import java.util.List;


/**
 * Description:  ScmSupplierCDto
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2016-07-28 17:10:13
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.scm.ScmSupplierC")
public class ScmSupplierCDto extends ScmSupplierC {
	@CascadeChilds(FK = "scmSupplierId|uuid")
	private List<ScmSupplierBankCDto> scmSupplierBankCDto;

	@CascadeChilds(FK = "scmSupplierId|uuid")
	private List<ScmSupplierContactsCDto> scmSupplierContactsCDto;

	@CascadeChilds(FK = "scmSupplierId|uuid")
	private List<ScmSukpplierKeyCDto> scmSukpplierKeyCDto;

	@CascadeChilds(FK = "dataId|uuid")
	private List<CdmFileRelationDto> cdmFileRelationDto;

	@CascadeChilds(FK = "dataId|uuid")
	private List<CdmFileRelationDto> cdmFileRelationCDto;

	public List<CdmFileRelationDto> getCdmFileRelationDto() {
		return cdmFileRelationDto;
	}

	public List<CdmFileRelationDto> getCdmFileRelationCDto() {
		return cdmFileRelationCDto;
	}

	public void setCdmFileRelationCDto(List<CdmFileRelationDto> cdmFileRelationCDto) {
		this.cdmFileRelationCDto = cdmFileRelationCDto;
	}

	public void setCdmFileRelationDto(List<CdmFileRelationDto> childDto) {
		this.cdmFileRelationDto = childDto;
	}

	public List<ScmSupplierBankCDto> getScmSupplierBankCDto() {
		return scmSupplierBankCDto;
	}

	public void setScmSupplierBankCDto(List<ScmSupplierBankCDto> childDto) {
		this.scmSupplierBankCDto = childDto;
	}

	public List<ScmSupplierContactsCDto> getScmSupplierContactsCDto() {
		return scmSupplierContactsCDto;
	}

	public void setScmSupplierContactsCDto(List<ScmSupplierContactsCDto> childDto) {
		this.scmSupplierContactsCDto = childDto;
	}

	public List<ScmSukpplierKeyCDto> getScmSukpplierKeyCDto() {
		return scmSukpplierKeyCDto;
	}

	public void setScmSukpplierKeyCDto(List<ScmSukpplierKeyCDto> childDto) {
		this.scmSukpplierKeyCDto = childDto;
	}

}