package com.csnt.scdp.bizmodules.modules.asset.maintain.action;

import com.csnt.scdp.bizmodules.entity.asset.AssetCard;
import com.csnt.scdp.bizmodules.modules.asset.maintain.dto.AssetMaintainDto;
import com.csnt.scdp.bizmodules.modules.asset.maintain.services.intf.MaintainManager;
import com.csnt.scdp.bizmodules.modules.common.action.ErpLoadAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.dto.BasePojo;
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
 * @timestamp 2015-09-22 20:02:10
 */

@Scope("singleton")
@Controller("maintain-load")
@Transactional
public class LoadAction extends ErpLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

	@Resource(name = "maintain-manager")
	private MaintainManager maintainManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
		Map out = super.perform(inMap);
		//Do After
		return out;
	}

    @Override
    protected void afterAction(BasePojo dtoObj) {
        AssetMaintainDto assetMaintainDto = (AssetMaintainDto) dtoObj;
        String cardUuid = assetMaintainDto.getCardUuid();
        if(StringUtils.isNoneBlank( cardUuid )){
            AssetCard assetCard = PersistenceFactory.getInstance().findByPK( AssetCard.class , cardUuid );
            if(assetCard != null){
               assetMaintainDto.setAssetCode( assetCard.getAssetCode() );
               assetMaintainDto.setAssetName( assetCard.getAssetName() );
               assetMaintainDto.setCardCode( assetCard.getCardCode() );
               assetMaintainDto.setOfficeId( assetCard.getOfficeId() );
            }
        }
    }

}
