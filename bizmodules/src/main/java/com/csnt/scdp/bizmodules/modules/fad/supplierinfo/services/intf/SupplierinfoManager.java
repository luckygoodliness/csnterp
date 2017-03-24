package com.csnt.scdp.bizmodules.modules.fad.supplierinfo.services.intf;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Description:  SupplierinfoManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-12-10 13:50:46
 */
public interface SupplierinfoManager {

    void validateUnique(String uuid, String completeName, String editFlag);

}