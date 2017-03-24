package com.csnt.scdp.bizmodules.modules.asset.transfer.action;

import com.csnt.scdp.bizmodules.entity.asset.AssetCard;
import com.csnt.scdp.bizmodules.modules.asset.transfer.dto.AssetTransferDto;
import com.csnt.scdp.bizmodules.modules.asset.transfer.services.intf.TransferManager;
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
 * @timestamp 2015-09-23 18:08:32
 */

@Scope("singleton")
@Controller("transfer-load")
@Transactional
public class LoadAction extends ErpLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

	@Resource(name = "transfer-manager")
	private TransferManager transferManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
		Map out = super.perform(inMap);
		//Do After
		return out;
	}

    @Override
    protected void afterAction(BasePojo dtoObj) {
        AssetTransferDto assetTransferDto = (AssetTransferDto) dtoObj;
        String cardUuid = assetTransferDto.getCardUuid();
        if(StringUtils.isNoneBlank(cardUuid)){
            AssetCard assetCard = PersistenceFactory.getInstance().findByPK( AssetCard.class , cardUuid );
            if(assetCard != null){
                assetTransferDto.setCardCode( assetCard.getCardCode() );
            }
        }
    }
}
