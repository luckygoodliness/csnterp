package com.csnt.scdp.bizmodules.modules.asset.apply.action;

import com.csnt.scdp.bizmodules.entity.asset.AssetCard;
import com.csnt.scdp.bizmodules.modules.asset.apply.dto.AssetDiscardApplyDto;
import com.csnt.scdp.bizmodules.modules.asset.apply.services.intf.ApplyManager;
import com.csnt.scdp.bizmodules.modules.common.action.ErpLoadAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Map;

/**
 * Description:  LoadAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 20:06:31
 */

@Scope("singleton")
@Controller("apply-load")
@Transactional
public class LoadAction extends ErpLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

    @Resource(name = "apply-manager")
    private ApplyManager applyManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);

        Map dtoMap = (Map) out.get("assetDiscardApplyDto");
        if (dtoMap != null) {
            //1.设置负责人的显示
            String orgCode = (String) dtoMap.get("applyOfficeId");
            if (orgCode != null) {
                String orgName = OrgHelper.getOrgNameByCode(orgCode);
                dtoMap.put("applyOfficeIdDesc", orgName);
            }
        }
        //Do After
        return out;
    }

    @Override
    protected void afterAction(BasePojo dtoObj) {
        AssetDiscardApplyDto assetDiscardApplyDto = (AssetDiscardApplyDto) dtoObj;
        String cardUuid = assetDiscardApplyDto.getCardUuid();
        if (StringUtils.isNoneBlank(cardUuid)) {
            AssetCard assetCard = PersistenceFactory.getInstance().findByPK(AssetCard.class, cardUuid);
            if (assetCard != null) {
                assetDiscardApplyDto.setAssetCode(assetCard.getAssetCode());
                assetDiscardApplyDto.setAssetName(assetCard.getAssetName());
                assetDiscardApplyDto.setDeviceType(assetCard.getDeviceType());
                assetDiscardApplyDto.setFactoryName(assetCard.getFactoryName());
                assetDiscardApplyDto.setIdentificationNumber(assetCard.getIdentificationNumber());
                assetDiscardApplyDto.setPurchaseTime(assetCard.getPurchaseTime());
                assetDiscardApplyDto.setModel(assetCard.getModel());
                assetDiscardApplyDto.setLocalValue(assetCard.getLocalValue());
                assetDiscardApplyDto.setSpecification(assetCard.getSpecification());
            }
        }
    }

}
