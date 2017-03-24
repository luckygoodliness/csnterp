package com.csnt.scdp.bizmodules.modules.asset.card.action;

import com.csnt.scdp.bizmodules.entity.asset.AssetTransfer;
import com.csnt.scdp.bizmodules.modules.asset.card.dto.AssetCardDto;
import com.csnt.scdp.bizmodules.modules.asset.card.dto.AssetCardTransferDto;
import com.csnt.scdp.bizmodules.modules.asset.card.dto.AssetHandoverDto;
import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.sysmodules.helper.NumberingHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.asset.card.services.intf.CardManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * Description:  ModifyAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-22 10:18:39
 */

@Scope("singleton")
@Controller("card-modify")
@Transactional
public class ModifyAction extends CommonModifyAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ModifyAction.class);

    @Resource(name = "card-manager")
    private CardManager cardManager;

    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {

        if (
                certificateManager.isNullReturnEmpty(inMap.get("menuCode")).equals("ASSETCARD")
                        ||
                        certificateManager.isNullReturnEmpty(inMap.get("menuCode")).equals("ASSETADDCARD")
                ) {

            String uuid = "";
            String cardCode = "";
            String assetCode = "";

            if (certificateManager.isNullReturnEmpty(inMap.get("menuCode")).equals("ASSETCARD")) {
                AssetCardDto assetCardDto = (AssetCardDto) BeanUtil.map2Dto((Map) ((Map) inMap.get(CommonAttribute.VIEW_DATA)).get("assetCardDto"), AssetCardDto.class);
                uuid = certificateManager.isNullReturnEmpty(assetCardDto.getUuid());
                cardCode = certificateManager.isNullReturnEmpty(assetCardDto.getCardCode());
                assetCode = certificateManager.isNullReturnEmpty(assetCardDto.getAssetCode());
                if (certificateManager.isNullReturnEmpty(assetCardDto.getAssetHandoverUuid()).length() > 0) {
                    throw new BizException("您不能修改由【固定资产验收交接单】产生的【固定资产卡片】");
                }

                String sql = "SELECT 1 FROM ASSET_TRANSFER WHERE CARD_UUID = '" + certificateManager.isNullReturnEmpty(assetCardDto.getUuid()) + "'";
                DAOMeta daoMeta = new DAOMeta();
                daoMeta.setStrSql(sql);
                daoMeta.setNeedFilter(false);
                if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                    throw new BizException("您不能修改已存在【固定资产变动申请单】的【固定资产卡片】");
                }

                sql = "SELECT 1 FROM ASSET_MAINTAIN WHERE CARD_UUID = '" + certificateManager.isNullReturnEmpty(assetCardDto.getUuid()) + "'";
                daoMeta = new DAOMeta();
                daoMeta.setStrSql(sql);
                daoMeta.setNeedFilter(false);
                if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                    throw new BizException("您不能修改已存在【固定资产设备维修申请单】的【固定资产卡片】");
                }

                sql = "SELECT 1 FROM ASSET_DISCARD_APPLY WHERE CARD_UUID = '" + certificateManager.isNullReturnEmpty(assetCardDto.getUuid()) + "'";
                daoMeta = new DAOMeta();
                daoMeta.setStrSql(sql);
                daoMeta.setNeedFilter(false);
                if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                    throw new BizException("您不能修改已存在【资产设备报废申请单】的【固定资产卡片】");
                }
            } else {
                AssetHandoverDto assetHandoverDto = (AssetHandoverDto) BeanUtil.map2Dto((Map) ((Map) inMap.get(CommonAttribute.VIEW_DATA)).get("assetHandoverDto"), AssetHandoverDto.class);
                uuid = certificateManager.isNullReturnEmpty(assetHandoverDto.getUuid());
                cardCode = certificateManager.isNullReturnEmpty(assetHandoverDto.getCardCode());
                assetCode = certificateManager.isNullReturnEmpty(assetHandoverDto.getAssetCode());
            }

            //验证【卡片编号】、【资产编号】存在否
            {
                String sql = "SELECT 1 FROM ASSET_CARD WHERE (IS_VOID = 0 OR IS_VOID IS NULL) AND UUID <> '" + uuid + "' AND CARD_CODE = '" + cardCode + "'";
                DAOMeta daoMeta = new DAOMeta();
                daoMeta.setStrSql(sql);
                daoMeta.setNeedFilter(false);
                if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                    throw new BizException("【卡片编号】已在【固定资产卡片】中存在!");
                }

                sql = "SELECT 1 FROM ASSET_CARD WHERE (IS_VOID = 0 OR IS_VOID IS NULL) AND UUID <> '" + uuid + "' AND ASSET_CODE = '" + assetCode + "'";
                daoMeta = new DAOMeta();
                daoMeta.setStrSql(sql);
                daoMeta.setNeedFilter(false);
                if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                    throw new BizException("【资产编号】已在【固定资产卡片】中存在!");
                }

                sql = "SELECT 1 FROM ASSET_HANDOVER WHERE (IS_VOID = 0 OR IS_VOID IS NULL) AND UUID <> '" + uuid + "' AND CARD_CODE = '" + cardCode + "'";
                daoMeta = new DAOMeta();
                daoMeta.setStrSql(sql);
                daoMeta.setNeedFilter(false);
                if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                    throw new BizException("【卡片编号】已在【固定资产验收交接单】中存在!");
                }

                sql = "SELECT 1 FROM ASSET_HANDOVER WHERE (IS_VOID = 0 OR IS_VOID IS NULL) AND UUID <> '" + uuid + "' AND ASSET_CODE = '" + assetCode + "'";
                daoMeta = new DAOMeta();
                daoMeta.setStrSql(sql);
                daoMeta.setNeedFilter(false);
                if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                    throw new BizException("【资产编号】已在【固定资产验收交接单】中存在!");
                }
            }
        }

        //Do After
        Map out = super.perform(inMap);
        return out;
    }
}
