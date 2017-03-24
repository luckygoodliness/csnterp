package com.csnt.scdp.bizmodules.modules.asset.card.action;

import com.csnt.scdp.bizmodules.entity.asset.AssetCard;
import com.csnt.scdp.bizmodules.entity.asset.AssetCardHistory;
import com.csnt.scdp.bizmodules.modules.asset.card.services.intf.CardManager;
import com.csnt.scdp.bizmodules.modules.asset.card.dto.AssetCardDto;
import com.csnt.scdp.bizmodules.modules.asset.card.dto.AssetCardHistoryDto;
import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.helper.DtoHelper;
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
@Controller("asset-card")
@Transactional
public class CarYearCheckRemindAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(TransferCardAction.class);

    @Resource(name = "card-manager")
    private CardManager cardManager;

    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Override
    public Map perform(Map map) throws BizException, SysException {
        String historyField = certificateManager.isNullReturnEmpty(map.get("historyField"));
        Map mapInput = (Map) map.get("assetCardDto");
        AssetCardDto assetCardDto = (AssetCardDto) BeanUtil.map2Dto(mapInput, AssetCardDto.class);

        DtoHelper.CascadeSave(assetCardDto);

        String uuid = certificateManager.isNullReturnEmpty(assetCardDto.getUuid());
        AssetCard assetCard = certificateManager.isNullReturnEmpty(uuid).length() > 0 ? PersistenceFactory.getInstance().findByPK(AssetCard.class, uuid) : null;
        if (assetCard != null) {
            AssetCardHistory assetCardHistory = BeanFactory.getObject(AssetCardHistory.class);
            BeanUtil.bean2Bean(assetCard, assetCardHistory);
            assetCardHistory.setUuid(null);
            PersistenceFactory.getInstance().insert(assetCardHistory);

            assetCardHistory.setAssetCardUuid(uuid);
            assetCardHistory.setHistoryField(historyField);
            PersistenceFactory.getInstance().update(assetCardHistory);
        }

        Map outMap = new HashMap();

        outMap.put("assetCardDto", assetCardDto);

        return outMap;
    }
}
