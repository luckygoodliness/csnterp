package com.csnt.scdp.bizmodules.modules.prm.finalestimate.dto;

import com.csnt.scdp.bizmodules.entity.prm.PrmFinalEstimate;
import com.csnt.scdp.framework.dto.PojoMapping;

import java.math.BigDecimal;


/**
 * Description:  PrmFinalEstimateDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-23 20:02:44
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.prm.PrmFinalEstimate")
public class PrmFinalEstimateDto extends PrmFinalEstimate {

    private String prmProjectMainIdDesc;
    private String departmentCodeDesc;
    private BigDecimal squareProportion;
    private String projectCode;

    private PrmFinalProjectInfoDto prmFinalProjectInfoDto;

    public String getPrmProjectMainIdDesc() {
        return prmProjectMainIdDesc;
    }

    public void setPrmProjectMainIdDesc(String prmProjectMainIdDesc) {
        this.prmProjectMainIdDesc = prmProjectMainIdDesc;
    }

    public String getDepartmentCodeDesc() {
        return departmentCodeDesc;
    }

    public void setDepartmentCodeDesc(String departmentCodeDesc) {
        this.departmentCodeDesc = departmentCodeDesc;
    }

    public BigDecimal getSquareProportion() {
        return squareProportion;
    }

    public void setSquareProportion(BigDecimal squareProportion) {
        this.squareProportion = squareProportion;
    }


    public PrmFinalProjectInfoDto getPrmFinalProjectInfoDto() {
        return prmFinalProjectInfoDto;
    }

    public void setPrmFinalProjectInfoDto(PrmFinalProjectInfoDto prmFinalProjectInfoDto) {
        this.prmFinalProjectInfoDto = prmFinalProjectInfoDto;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }
}