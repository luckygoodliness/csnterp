package com.csnt.scdp.bizmodules.modules.scm.notesplan.dto;

import com.csnt.scdp.bizmodules.entity.scm.ScmNotesPlan;
import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;

import java.util.List;


/**
 * Description:  ScmNotesPlanDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-11-04 15:02:50
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.scm.ScmNotesPlan")
public class ScmNotesPlanDto extends ScmNotesPlan {
    @CascadeChilds(FK = "scmNotesPlanId|uuid")
    private List<ScmNotesPlanDetailDto> scmNotesPlanDetailDto;


    public List<ScmNotesPlanDetailDto> getScmNotesPlanDetailDto() {
        return scmNotesPlanDetailDto;
    }

    public void setScmNotesPlanDetailDto(List<ScmNotesPlanDetailDto> childDto) {
        this.scmNotesPlanDetailDto = childDto;
    }

}