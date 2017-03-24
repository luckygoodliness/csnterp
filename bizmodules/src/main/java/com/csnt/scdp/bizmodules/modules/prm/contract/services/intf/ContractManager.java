package com.csnt.scdp.bizmodules.modules.prm.contract.services.intf;

import com.csnt.scdp.bizmodules.entity.prm.PrmContract;
import com.csnt.scdp.bizmodules.modules.prm.contract.dto.PrmContractDto;

/**
 * Description:  ContractManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 09:57:20
 */
public interface ContractManager {

    PrmContract findContractHeaderOnly(String uuid);

    void checkContractUnique(String contractNo, String contractName);

    void loadExtraDescField(PrmContractDto contractDto);
}