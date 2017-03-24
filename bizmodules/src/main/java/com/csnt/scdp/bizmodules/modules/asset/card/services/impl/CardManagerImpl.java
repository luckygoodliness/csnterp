package com.csnt.scdp.bizmodules.modules.asset.card.services.impl;

import com.csnt.scdp.bizmodules.entity.asset.AssetCard;
import com.csnt.scdp.bizmodules.entity.asset.AssetTransfer;
import com.csnt.scdp.bizmodules.modules.asset.card.services.intf.CardManager;
import com.csnt.scdp.bizmodules.modules.asset.transfer.dto.AssetTransferDto;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.DtoHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Description:  CardManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-22 10:18:39
 */

@Scope("singleton")
@Service("card-manager")
public class CardManagerImpl implements CardManager {

    @Override
    public void doTransferCard(AssetTransferDto assetTransferDto) {
        AssetCard assetCard = PersistenceFactory.getInstance().findByPK(AssetCard.class, assetTransferDto.getCardUuid());

        assetTransferDto.setCardCode(assetCard.getCardCode());
        assetTransferDto.setEditflag("+");
        DtoHelper.CascadeSave(assetTransferDto);

        String endUserName = assetTransferDto.getInPersonName();
        String officeId = assetTransferDto.getInOfficeId();
        String endUserCode = assetTransferDto.getInPersonCode();

        assetCard.setLiablePerson(assetTransferDto.getInLiablePerson());
        assetCard.setEndUserCode(endUserCode);
        assetCard.setEndUserName(endUserName);
        assetCard.setOfficeId(officeId);

        PersistenceFactory.getInstance().update(assetCard);
    }
}