package com.csnt.scdp.bizmodules.modules.scm.scmcontractchange.dto;

import com.csnt.scdp.bizmodules.modules.cdm.cdmfile.dto.CdmFileRelationDto;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.scm.ScmContractChange;

import java.util.ArrayList;
import java.util.List;


/**
 * Description:  ScmContractChangeDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-26 18:01:19
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.scm.ScmContractChange")
public class ScmContractChangeDto extends ScmContractChange {
    public String getContractname() {
        return contractname;
    }

    public void setContractname(String contractname) {
        this.contractname = contractname;
    }

    private String contractname;
    @CascadeChilds(FK = "dataId|uuid")
    private List<CdmFileRelationDto> cdmFileRelationDto;

    //M3_NC3_采购变更申请（项目）
    @CascadeChilds(FK = "scmContractChangeId|uuid")
    private List<ScmContractChangeDDto> scmContractChangeDDto;

    public List<CdmFileRelationDto> getCdmFileRelationDto() {
        return cdmFileRelationDto;
    }

    public void setCdmFileRelationDto(List<CdmFileRelationDto> childDto) {
        this.cdmFileRelationDto = childDto;
    }

    public List<ScmContractChangeDDto> getScmContractChangeDDto() {
        return scmContractChangeDDto;
    }

    public void setScmContractChangeDDto(List<ScmContractChangeDDto> scmContractChangeDDto) {
        this.scmContractChangeDDto = scmContractChangeDDto;
    }
}