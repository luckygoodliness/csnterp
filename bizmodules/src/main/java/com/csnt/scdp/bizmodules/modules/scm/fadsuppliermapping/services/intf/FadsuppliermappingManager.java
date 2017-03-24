package com.csnt.scdp.bizmodules.modules.scm.fadsuppliermapping.services.intf;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Description:  FadsuppliermappingManager
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-08-04 15:22:39
 */
public interface FadsuppliermappingManager {
    void  fadSupplierMappingChange(String supplierFromName, String supplierToUuid);

}