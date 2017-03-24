package com.csnt.scdp.bizmodules.modules.scm.scmsae.dto;

import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.scm.ScmSaeObject;

import java.math.BigDecimal;
import java.util.List;


/**
 * Description:  ScmSaeObjectDto
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2016-09-01 20:48:53
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.scm.ScmSaeObject")
public class ScmSaeObjectDto extends ScmSaeObject {
	@CascadeChilds(FK = "scmSaeObjectId|uuid")
	private List<ScmSaeFormDto> scmSaeFormDto;
	private String materialClassName;
	private String supplierName;
	private String materialTypeName;
	private String operativeSegments;
	private BigDecimal amount;
	private BigDecimal contractNum;

	public List<ScmSaeFormDto> getScmSaeFormDto() {
		return scmSaeFormDto;
	}

	public void setScmSaeFormDto(List<ScmSaeFormDto> childDto) {
		this.scmSaeFormDto = childDto;
	}

	public String getMaterialClassName() {
		return materialClassName;
	}

	public void setMaterialClassName(String materialClassName) {
		this.materialClassName = materialClassName;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getMaterialTypeName() {
		return materialTypeName;
	}

	public void setMaterialTypeName(String materialTypeName) {
		this.materialTypeName = materialTypeName;
	}

	public String getOperativeSegments() {
		return operativeSegments;
	}

	public void setOperativeSegments(String operativeSegments) {
		this.operativeSegments = operativeSegments;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getContractNum() {
		return contractNum;
	}

	public void setContractNum(BigDecimal contractNum) {
		this.contractNum = contractNum;
	}
}