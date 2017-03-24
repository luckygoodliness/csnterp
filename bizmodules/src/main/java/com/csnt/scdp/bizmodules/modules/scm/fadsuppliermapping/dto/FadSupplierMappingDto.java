package com.csnt.scdp.bizmodules.modules.scm.fadsuppliermapping.dto;

import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.scm.FadSupplierMapping;

import java.util.List;


/**
 * Description:  FadSupplierMappingDto
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2016-08-04 15:22:38
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.scm.FadSupplierMapping")
public class FadSupplierMappingDto extends FadSupplierMapping {
    public String supplierToUuidDesc;
    public String getSupplierToUuidDesc() {
        return supplierToUuidDesc;
    }

    public void setSupplierToUuidDesc(String supplierToUuidDesc) {
        this.supplierToUuidDesc = supplierToUuidDesc;
    }
}