package com.csnt.scdp.bizmodules.modules.ztrain.ztraincode.dto;
import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.ztrain.TestTable;

import java.util.List;


/**
 * Description:  TestTableDto
 * Copyright: © 2017 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * 2017-02-21 15:11:17
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.ztrain.TestTable")
public class TestTableDto extends TestTable {
    @CascadeChilds(FK = "puuid|uuid")
    private List<TestTableContentDto> testTableContentDto;

    public List<TestTableContentDto> getTestTableContentDto() {
        return testTableContentDto;
    }

    public void setTestTableContentDto(List<TestTableContentDto> childDto) {
        this.testTableContentDto = childDto;
    }

    private String projectCode;

    private String uuid;

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }


}