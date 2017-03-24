package com.csnt.scdp.bizmodules.modules.asset.apply.services.intf;

import com.csnt.scdp.bizmodules.modules.asset.apply.dto.AssetDiscardApplyDto;

/**
 * Description:  ApplyManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 20:06:32
 */
public interface ApplyManager {

    public void validateCardUnique(AssetDiscardApplyDto assetDiscardApplyDto);

    /**
     * 报废审核通过后将对应资产使用状态更新为报废
     * @param cardUuid
     */
    public void updateAssetCardState(String cardUuid);
}