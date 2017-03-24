package com.csnt.scdp.bizmodules.modules.asset.card.services.intf;

import com.csnt.scdp.bizmodules.modules.asset.transfer.dto.AssetTransferDto;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Description:  CardManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-22 10:18:39
 */
public interface CardManager {

    public void doTransferCard(AssetTransferDto assetTransferDto);

}