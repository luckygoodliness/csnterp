package com.csnt.scdp.bizmodules.modules.workflow.asset.apply;

import com.csnt.scdp.bizmodules.entity.asset.AssetCard;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.sysmodules.modules.sys.workflow.action.WorkFlowVariableProcessAction;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wuhd on 2015/11/9.
 */
@Scope("singleton")
@Controller("asset_retirement-start")
@Transactional

public class AssetRetirementStart extends ERPWorkFlowBaseVariableCollectionAction {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapVar = super.perform(inMap);
        Map mapData = ErpWorkFlowHelper.filterRoles(inMap, ErpWorkFlowHelper.loadUserInfoByUserID(inMap));
        String cardUuid = (String) mapVar.get("cardUuid");
        Map condition = new HashMap<>();
        condition.put("cardUuid", cardUuid);
        AssetCard assetCard = PersistenceFactory.getInstance().findByPK(AssetCard.class, cardUuid);
        if (assetCard != null) {
            String deviceType = assetCard.getDeviceType();
            BigDecimal localValue = null == assetCard.getLocalValue() ? new BigDecimal(0) : assetCard.getLocalValue();
            mapData.put("deviceType", deviceType);
            mapData.put("localValue", localValue);
        }
        return mapData;
    }
}
