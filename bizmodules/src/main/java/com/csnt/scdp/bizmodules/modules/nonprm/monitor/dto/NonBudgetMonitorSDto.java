package com.csnt.scdp.bizmodules.modules.nonprm.monitor.dto;

import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.nonprm.NonBudgetMonitorS;

import java.util.List;


/**
 * Description:  NonBudgetMonitorSDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-27 15:12:59
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.nonprm.NonBudgetMonitorS")
public class NonBudgetMonitorSDto extends NonBudgetMonitorS {

     String tit;

    public String getTitle() {
        return tit;
    }

    public void setTitle(String title) {
        this.tit = title;
    }
}