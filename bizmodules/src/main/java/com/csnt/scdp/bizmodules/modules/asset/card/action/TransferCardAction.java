package com.csnt.scdp.bizmodules.modules.asset.card.action;

import com.csnt.scdp.bizmodules.modules.asset.card.services.intf.CardManager;
import com.csnt.scdp.bizmodules.modules.asset.transfer.dto.AssetTransferDto;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.sysmodules.entity.ScdpOrg;
import com.csnt.scdp.sysmodules.entityattributes.ScdpOrgAttribute;
import com.csnt.scdp.sysmodules.helper.NumberingHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tao on 2015/10/30.
 */
@Scope("singleton")
@Controller("transfer-card")
@Transactional
public class TransferCardAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(TransferCardAction.class);

    @Resource(name = "card-manager")
    private CardManager cardManager;

    @Override
    public Map perform(Map map) throws BizException, SysException {
        Map mapInput = (Map) map.get("assetTransferDto");
        AssetTransferDto assetTransferDto = (AssetTransferDto) BeanUtil.map2Dto(mapInput, AssetTransferDto.class);
        String ransferApplyId = NumberingHelper.getNumbering("ASSET_TRANSFER_NO", null);
        assetTransferDto.setRansferApplyId(ransferApplyId);

        cardManager.doTransferCard(assetTransferDto);

        Map outMap = new HashMap();
        Map mapCond = new HashMap();
        mapCond.put(ScdpOrgAttribute.ORG_CODE, assetTransferDto.getOutOfficeId());
        List list = PersistenceFactory.getInstance().findByAnyFields(ScdpOrg.class, mapCond, null);
        if(list.size()>0){
            ScdpOrg scdpOrgOut = (ScdpOrg)list.get(0);
            assetTransferDto.setOutOfficeIdDesc(scdpOrgOut.getOrgName());
            outMap.put("assetTransferDto", assetTransferDto);
        }

        return outMap;
    }
}
