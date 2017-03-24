package com.csnt.scdp.bizmodules.modules.asset.card.action;

import com.csnt.scdp.bizmodules.entity.asset.AssetCard;
import com.csnt.scdp.bizmodules.entity.asset.AssetHandover;
import com.csnt.scdp.bizmodules.entity.asset.AssetCardTransfer;
import com.csnt.scdp.bizmodules.modules.asset.card.services.intf.CardManager;
import com.csnt.scdp.bizmodules.modules.common.action.ErpDeleteAction;
import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * Description:  DeleteAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-22 10:18:39
 */

@Scope("singleton")
@Controller("card-delete")
@Transactional
public class DeleteAction extends ErpDeleteAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(DeleteAction.class);

    @Resource(name = "card-manager")
    private CardManager cardManager;

    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {

        //Do before
        if (certificateManager.isNullReturnEmpty(inMap.get("menuCode")).equals("ASSETCARD")) {
            List lst = (List) super.getDeleteUuids(inMap);
            if (ListUtil.isNotEmpty(lst)) {
                for (Object obj : lst) {
                    String uuid = StringUtil.replaceNull(obj);
                    AssetCard assetCard = PersistenceFactory.getInstance().findByPK(AssetCard.class, uuid);
                    if (certificateManager.isNullReturnEmpty(assetCard.getAssetHandoverUuid()).length() > 0) {
                        throw new BizException("您不能删除由【固定资产验收交接单】产生的【固定资产卡片】");
                    }

                    String sql = "SELECT 1 FROM ASSET_TRANSFER WHERE CARD_UUID = '" + certificateManager.isNullReturnEmpty(assetCard.getUuid()) + "'";
                    DAOMeta daoMeta = new DAOMeta();
                    daoMeta.setStrSql(sql);
                    daoMeta.setNeedFilter(false);
                    if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                        throw new BizException("您不能删除已存在【固定资产变动申请单】的【固定资产卡片】");
                    }

                    sql = "SELECT 1 FROM ASSET_MAINTAIN WHERE CARD_UUID = '" + certificateManager.isNullReturnEmpty(assetCard.getUuid()) + "'";
                    daoMeta = new DAOMeta();
                    daoMeta.setStrSql(sql);
                    daoMeta.setNeedFilter(false);
                    if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                        throw new BizException("您不能删除已存在【固定资产设备维修申请单】的【固定资产卡片】");
                    }

                    sql = "SELECT 1 FROM ASSET_DISCARD_APPLY WHERE CARD_UUID = '" + certificateManager.isNullReturnEmpty(assetCard.getUuid()) + "'";
                    daoMeta = new DAOMeta();
                    daoMeta.setStrSql(sql);
                    daoMeta.setNeedFilter(false);
                    if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                        throw new BizException("您不能删除已存在【资产设备报废申请单】的【固定资产卡片】");
                    }
                }
            }
        } else if (certificateManager.isNullReturnEmpty(inMap.get("menuCode")).equals("ASSETADDCARD")) {
            List lst = (List) super.getDeleteUuids(inMap);
            if (ListUtil.isNotEmpty(lst)) {
                for (Object obj : lst) {
                    String uuid = StringUtil.replaceNull(obj);
                    AssetHandover assetHandover = PersistenceFactory.getInstance().findByPK(AssetHandover.class, uuid);
                    if (!"0".equals(assetHandover.getState())) {
                        throw new BizException("您不能删除非新增状态的【固定资产验收交接单】");
                    }
                }
            }
        } else if (certificateManager.isNullReturnEmpty(inMap.get("menuCode")).equals("ASSETTRANSFERCARD")) {
            List lst = (List) super.getDeleteUuids(inMap);
            if (ListUtil.isNotEmpty(lst)) {
                for (Object obj : lst) {
                    String uuid = StringUtil.replaceNull(obj);
                    AssetCardTransfer assetCardTransfer = PersistenceFactory.getInstance().findByPK(AssetCardTransfer.class, uuid);
                    if (!"0".equals(assetCardTransfer.getState())) {
                        throw new BizException("您不能删除非新增状态的【固定资产变动申请单】");
                    }
                }
            }
        }

        //Do After
        Map out = super.perform(inMap);
        return out;
    }
}
