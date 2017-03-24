package com.csnt.scdp.bizmodules.modules.asset.apply.services.impl;

import com.csnt.scdp.bizmodules.entity.asset.AssetCard;
import com.csnt.scdp.bizmodules.entity.asset.AssetDiscardApply;
import com.csnt.scdp.bizmodules.entity.asset.AssetTransfer;
import com.csnt.scdp.bizmodules.modules.asset.apply.dto.AssetDiscardApplyDto;
import com.csnt.scdp.bizmodules.modules.asset.apply.services.intf.ApplyManager;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:  ApplyManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 20:06:32
 */

@Scope("singleton")
@Service("apply-manager")
public class ApplyManagerImpl implements ApplyManager {
    @Override
    public void validateCardUnique(AssetDiscardApplyDto assetDiscardApplyDto) {
        String daoType = "apply-dao";
        String daoKeyAdd = "validate_card_add";
        String daoKeyModify = "validate_card_modify";
        if ("+".equals(assetDiscardApplyDto.getEditflag())) {
            List lstParams = new ArrayList();
            lstParams.add(assetDiscardApplyDto.getCardUuid());

            DAOMeta daoMeta = DAOHelper.getDAO(daoType, daoKeyAdd, lstParams);
            Integer count = PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta);
            if (count > 0) {
                String info = StringUtil.isEmpty(assetDiscardApplyDto.getAssetCode()) ? "资产名称为" + assetDiscardApplyDto.getAssetName() : "资产编号为 " + assetDiscardApplyDto.getAssetCode();
                throw new BizException(info + "的报废申请已存在！");
            }
        } else if ("*".equals(assetDiscardApplyDto.getEditflag())) {
            AssetDiscardApply assetDiscardApplyDB = PersistenceFactory.getInstance().findByPK(AssetDiscardApply.class, assetDiscardApplyDto.getUuid());
            if (assetDiscardApplyDB.getCardUuid().equals(assetDiscardApplyDto.getCardUuid())) {
                return;
            }

            List lstParams = new ArrayList();
            lstParams.add(assetDiscardApplyDto.getCardUuid());
            lstParams.add(assetDiscardApplyDto.getUuid());

            DAOMeta daoMeta = DAOHelper.getDAO(daoType, daoKeyModify, lstParams);
            Integer count = PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta);
            if (count > 0) {
                String info = StringUtil.isEmpty(assetDiscardApplyDto.getAssetCode()) ? "资产名称为" + assetDiscardApplyDto.getAssetName() : "资产编号为 " + assetDiscardApplyDto.getAssetCode();
                throw new BizException(info + "的报废申请已存在！");
            }
        }

    }

    @Override
    public void updateAssetCardState(String cardUuid) {
        AssetCard assetCard = PersistenceFactory.getInstance().findByPK(AssetCard.class, cardUuid);
        assetCard.setStatus("P");
        PersistenceFactory.getInstance().update(assetCard);
    }
}