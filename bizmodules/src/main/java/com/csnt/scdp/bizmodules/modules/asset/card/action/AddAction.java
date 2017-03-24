package com.csnt.scdp.bizmodules.modules.asset.card.action;

import com.csnt.scdp.bizmodules.entity.asset.AssetTransfer;
import com.csnt.scdp.bizmodules.modules.asset.card.dto.AssetCardDto;
import com.csnt.scdp.bizmodules.modules.asset.card.dto.AssetHandoverDto;
import com.csnt.scdp.bizmodules.modules.asset.card.dto.AssetCardTransferDto;
import com.csnt.scdp.bizmodules.entityattributes.asset.AssetCardAttribute;
import com.csnt.scdp.bizmodules.modules.asset.transfer.dto.AssetTransferDto;
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
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.helper.NumberingHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.asset.card.services.intf.CardManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-22 10:18:39
 */

@Scope("singleton")
@Controller("card-add")
@Transactional
public class AddAction extends CommonAddAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);

    @Resource(name = "card-manager")
    private CardManager cardManager;

    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {

        //Do before
        if (
                certificateManager.isNullReturnEmpty(inMap.get("menuCode")).equals("ASSETCARD")
                        ||
                        certificateManager.isNullReturnEmpty(inMap.get("menuCode")).equals("ASSETADDCARD")
                ) {

            String cardCode = "";
            String assetCode = "";
            String handoverNo = "";

            if (certificateManager.isNullReturnEmpty(inMap.get("menuCode")).equals("ASSETCARD")) {
                AssetCardDto assetCardDto = (AssetCardDto) BeanUtil.map2Dto((Map) ((Map) inMap.get(CommonAttribute.VIEW_DATA)).get("assetCardDto"), AssetCardDto.class);
                cardCode = certificateManager.isNullReturnEmpty(assetCardDto.getCardCode());
                assetCode = certificateManager.isNullReturnEmpty(assetCardDto.getAssetCode());
            } else {
                AssetHandoverDto assetHandoverDto = (AssetHandoverDto) BeanUtil.map2Dto((Map) ((Map) inMap.get(CommonAttribute.VIEW_DATA)).get("assetHandoverDto"), AssetHandoverDto.class);
                cardCode = certificateManager.isNullReturnEmpty(assetHandoverDto.getCardCode());
                assetCode = certificateManager.isNullReturnEmpty(assetHandoverDto.getAssetCode());

                //要赋值这里必须用MAP才是传引用!
                Map assetHandoverMap = ((Map) ((Map) inMap.get(CommonAttribute.VIEW_DATA)).get("assetHandoverDto"));
                handoverNo = certificateManager.isNullReturnEmpty(NumberingHelper.getNumbering("HANDOVER_NO", null));
                assetHandoverMap.put("handoverNo", handoverNo);

                //是【固定资产验收交接单】则验证所选采购【采购申请明细】预算数量是否已用光
                {
                    int amount = Integer.parseInt(certificateManager.isNullReturnEmpty(assetHandoverMap.get("amount")));
                    String purchaseReqDetailUuid = certificateManager.isNullReturnEmpty(assetHandoverMap.get("purchaseReqDetailUuid"));

                    String sql = "SELECT 1 FROM ASSET_HANDOVER WHERE (IS_VOID = 0 OR IS_VOID IS NULL) AND PURCHASE_REQ_DETAIL_UUID = '" + purchaseReqDetailUuid + "'";
                    DAOMeta daoMeta = new DAOMeta();
                    daoMeta.setStrSql(sql);
                    daoMeta.setNeedFilter(false);
                    if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) >= amount) {
                        throw new BizException("该【采购申请明细】已做【验收交接单】");
                    }
                }
            }

            //验证【卡片编号】、【资产编号】存在否
            {
                String sql = "SELECT 1 FROM ASSET_CARD WHERE (IS_VOID = 0 OR IS_VOID IS NULL) AND CARD_CODE = '" + cardCode + "'";
                DAOMeta daoMeta = new DAOMeta();
                daoMeta.setStrSql(sql);
                daoMeta.setNeedFilter(false);
                if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                    throw new BizException("【卡片编号】已在【固定资产卡片】中存在!");
                }

                sql = "SELECT 1 FROM ASSET_CARD WHERE (IS_VOID = 0 OR IS_VOID IS NULL) AND ASSET_CODE = '" + assetCode + "'";
                daoMeta = new DAOMeta();
                daoMeta.setStrSql(sql);
                daoMeta.setNeedFilter(false);
                if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                    throw new BizException("【资产编号】已在【固定资产卡片】中存在!");
                }

                sql = "SELECT 1 FROM ASSET_HANDOVER WHERE (IS_VOID = 0 OR IS_VOID IS NULL) AND CARD_CODE = '" + cardCode + "'";
                daoMeta = new DAOMeta();
                daoMeta.setStrSql(sql);
                daoMeta.setNeedFilter(false);
                if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                    throw new BizException("【卡片编号】已在【固定资产验收交接单】中存在!");
                }

                sql = "SELECT 1 FROM ASSET_HANDOVER WHERE (IS_VOID = 0 OR IS_VOID IS NULL) AND ASSET_CODE = '" + assetCode + "'";
                daoMeta = new DAOMeta();
                daoMeta.setStrSql(sql);
                daoMeta.setNeedFilter(false);
                if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                    throw new BizException("【资产编号】已在【固定资产验收交接单】中存在!");
                }
            }

            Map out = super.perform(inMap);
            if (certificateManager.isNullReturnEmpty(handoverNo).length() > 0) {
                out.put("handoverNo", handoverNo);
            }
            return out;
        } else if (certificateManager.isNullReturnEmpty(inMap.get("menuCode")).equals("ASSETTRANSFERCARD")) {

            //要赋值这里必须用MAP才是传引用!
            Map assetCardTransferDtoMap = ((Map) ((Map) inMap.get(CommonAttribute.VIEW_DATA)).get("assetCardTransferDto"));

            //验证本张【资产编号】已发起【工作流】并处于【未审核】状态否
            {
                String sql = "SELECT 1 FROM ASSET_TRANSFER WHERE (IS_VOID = 0 OR IS_VOID IS NULL) AND CARD_UUID = '" + certificateManager.isNullReturnEmpty(assetCardTransferDtoMap.get("cardUuid")) + "' AND STATE <> '2'";
                DAOMeta daoMeta = new DAOMeta();
                daoMeta.setStrSql(sql);
                daoMeta.setNeedFilter(false);
                if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                    throw new BizException("本张【资产编号】已发起【工作流】并处于【未审核】状态!");
                }
            }

            assetCardTransferDtoMap.put("ransferApplyId", certificateManager.isNullReturnEmpty(NumberingHelper.getNumbering("ASSET_TRANSFER_NO", null)));
            assetCardTransferDtoMap.put("operatePerson", certificateManager.isNullReturnEmpty(assetCardTransferDtoMap.get("createBy")));
            assetCardTransferDtoMap.put("applyDate", assetCardTransferDtoMap.get("createTime"));

            String sql = "select" +
                    " nvl(t.in_person_code,a.end_user_code) end_user_code," +
                    " nvl(t.in_person_name,a.end_user_name) end_user_name," +
                    " nvl(t.in_office_id,a.office_id) office_id," +
                    " nvl(t.in_liable_person,a.liable_person) liable_person" +
                    " from asset_transfer t" +
                    " right join asset_card a" +
                    " on t.card_uuid = a.uuid" +
                    " where a.uuid='" + certificateManager.isNullReturnEmpty(assetCardTransferDtoMap.get("cardUuid")) + "'" +
                    " and (t.ransfer_apply_id = (select max(t1.ransfer_apply_id) from asset_transfer t1 right join asset_card a1 on t1.card_Uuid = a1.uuid where a1.uuid = a.uuid and (t1.is_void = 0 or t1.is_void is null) and (a1.is_void = 0 or a1.is_void is null)) or t.ransfer_apply_id is null)" +
                    " and (t.is_void = 0 or t.is_void is null)" +
                    " and (a.is_void = 0 or a.is_void is null)";

            DAOMeta daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            List assetCardTransferList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            Map assetCardTransferMap = (Map) assetCardTransferList.get(0);

            assetCardTransferDtoMap.put("outPersonCode", certificateManager.isNullReturnEmpty(assetCardTransferMap.get("endUserCode")));
            assetCardTransferDtoMap.put("outPersonName", certificateManager.isNullReturnEmpty(assetCardTransferMap.get("endUserName")));
            assetCardTransferDtoMap.put("outOfficeId", certificateManager.isNullReturnEmpty(assetCardTransferMap.get("officeId")));
            assetCardTransferDtoMap.put("outLiablePerson", certificateManager.isNullReturnEmpty(assetCardTransferMap.get("liablePerson")));
        }

        //Do After
        Map out = super.perform(inMap);
        return out;
    }
}
