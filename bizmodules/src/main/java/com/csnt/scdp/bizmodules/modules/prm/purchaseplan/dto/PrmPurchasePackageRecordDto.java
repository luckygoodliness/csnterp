package com.csnt.scdp.bizmodules.modules.prm.purchaseplan.dto;

import com.csnt.scdp.bizmodules.entity.prm.PrmPurchasePackageRecord;
import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
//import com.csnt.scdp.sysmodules.entity.PrmPurchasePackageRecord;

import java.util.List;


/**
 * Description:  PrmPurchasePackageRecordDto
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2016-09-08 09:43:22
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.prm.PrmPurchasePackageRecord")
public class PrmPurchasePackageRecordDto extends PrmPurchasePackageRecord {

    private String revisedByDesc;
    private String materialClassCodeDesc;

    public String getMaterialClassCodeDesc() {
        return materialClassCodeDesc;
    }

    public void setMaterialClassCodeDesc(String materialClassCodeDesc) {
        this.materialClassCodeDesc = materialClassCodeDesc;
    }

    public String getRevisedByDesc() {
        return revisedByDesc;
    }

    public void setRevisedByDesc(String revisedByDesc) {
        this.revisedByDesc = revisedByDesc;
    }

}