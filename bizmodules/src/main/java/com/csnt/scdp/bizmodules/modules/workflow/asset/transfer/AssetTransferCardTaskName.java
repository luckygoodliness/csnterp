package com.csnt.scdp.bizmodules.modules.workflow.asset.transfer;

import com.csnt.scdp.bizmodules.entity.asset.AssetCard;
import com.csnt.scdp.bizmodules.entity.asset.AssetCardTransfer;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("asset_transfer_card-process-taskname")
@Transactional

public class AssetTransferCardTaskName extends ERPWorkFlowBaseVariableCollectionAction {

    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        inMap.put("dto", "com.csnt.scdp.bizmodules.modules.asset.card.dto.AssetCardTransferDto");
        Map dataInfo = super.perform(inMap);

        String uuid = certificateManager.isNullReturnEmpty(dataInfo.get("uuid"));
        AssetCardTransfer assetCardTransfer = PersistenceFactory.getInstance().findByPK(AssetCardTransfer.class, uuid);
        AssetCard assetCard = PersistenceFactory.getInstance().findByPK(AssetCard.class, certificateManager.isNullReturnEmpty(assetCardTransfer.getCardUuid()));

        String assetName = certificateManager.isNullReturnEmpty(assetCard.getAssetName());
        Map mapResult = new HashMap();
        String state = certificateManager.isNullReturnEmpty(dataInfo.get("state"));
        String stateDesc = ErpWorkFlowHelper.stateFlag(state);
        mapResult.put("name", stateDesc + "资产名称：" + assetName);
        return mapResult;
    }
}
