package com.csnt.scdp.bizmodules.modules.workflow.asset.card;

import com.csnt.scdp.bizmodules.entity.asset.AssetCard;
import com.csnt.scdp.bizmodules.entity.asset.AssetHandover;
import com.csnt.scdp.bizmodules.entity.asset.AssetCardTransfer;
import com.csnt.scdp.bizmodules.entity.prm.PrmContract;
import com.csnt.scdp.bizmodules.entity.prm.PrmContractC;
import com.csnt.scdp.bizmodules.entity.prm.PrmCustomer;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmContractAttribute;
import com.csnt.scdp.bizmodules.modules.asset.card.services.intf.CardManager;
import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
import com.csnt.scdp.bizmodules.modules.prm.prmexamperiod.services.intf.PrmExamPeriodManager;
import com.csnt.scdp.bizmodules.modules.prm.contractc.services.intf.ContractCManager;
import com.csnt.scdp.bizmodules.modules.prm.prmcustomer.services.intf.PrmcustomerManager;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Administrator on 2016/6/19.
 */
@Scope("singleton")
@Controller("asset_register-after-complete")
public class AssetRegisterAfterComplete implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(AssetRegisterAfterComplete.class);

    @Resource(name = "card-manager")
    private CardManager cardManager;

    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {

        //Do before
        Map out = new HashMap();
        String wfStatus = certificateManager.isNullReturnEmpty(inMap.get(WorkFlowAttribute.STATUS));
        if (WorkFlowAttribute.STATUS_FIXED.equals(wfStatus)) {

            //取得结案UUID
            String uuid = certificateManager.isNullReturnEmpty(inMap.get(WorkFlowAttribute.BUSINESS_KEY));

            //固定资产验收交接单
            AssetHandover assetHandover = PersistenceFactory.getInstance().findByPK(AssetHandover.class, uuid);

            AssetCard assetCard = BeanFactory.getObject(AssetCard.class);
            BeanUtil.bean2Bean(assetHandover, assetCard);
            assetCard.setUuid(null);
            PersistenceFactory.getInstance().insert(assetCard);

            assetCard.setAssetHandoverUuid(uuid);
            assetCard.setState("2");
            PersistenceFactory.getInstance().update(assetCard);
        }

        //Do After
        return out;
    }
}
