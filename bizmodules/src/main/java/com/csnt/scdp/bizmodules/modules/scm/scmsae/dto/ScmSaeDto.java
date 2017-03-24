package com.csnt.scdp.bizmodules.modules.scm.scmsae.dto;

import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.scm.ScmSae;

import java.util.List;


/**
 * Description:  ScmSaeDto
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2016-09-01 20:48:53
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.scm.ScmSae")
public class ScmSaeDto extends ScmSae {
	@CascadeChilds(FK = "scmSaeId|uuid")
	private List<ScmSaeObjectDto> scmSaeObjectDto;
	private String saiCaseDesc;
	private String waiCaseDesc;
	private String appraiserCaseDesc;

	public List<ScmSaeObjectDto> getScmSaeObjectDto() {
		return scmSaeObjectDto;
	}

	public void setScmSaeObjectDto(List<ScmSaeObjectDto> childDto) {
		this.scmSaeObjectDto = childDto;
	}

	public String getSaiCaseDesc() {
		return saiCaseDesc;
	}

	public void setSaiCaseDesc(String saiCaseDesc) {
		this.saiCaseDesc = saiCaseDesc;
	}

	public String getWaiCaseDesc() {
		return waiCaseDesc;
	}

	public void setWaiCaseDesc(String waiCaseDesc) {
		this.waiCaseDesc = waiCaseDesc;
	}

	public String getAppraiserCaseDesc() {
		return appraiserCaseDesc;
	}

	public void setAppraiserCaseDesc(String appraiserCaseDesc) {
		this.appraiserCaseDesc = appraiserCaseDesc;
	}
}