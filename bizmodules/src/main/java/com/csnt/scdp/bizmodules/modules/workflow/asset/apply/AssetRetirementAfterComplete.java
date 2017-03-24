package com.csnt.scdp.bizmodules.modules.workflow.asset.apply;

import com.csnt.scdp.bizmodules.entity.asset.AssetDiscardApply;
import com.csnt.scdp.bizmodules.entity.asset.AssetCard;
import com.csnt.scdp.bizmodules.modules.asset.apply.services.intf.ApplyManager;
import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/19.
 */
@Scope("singleton")
@Controller("asset_retirement-after-complete")
public class AssetRetirementAfterComplete implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(AssetRetirementAfterComplete.class);

    @Resource(name = "apply-manager")
    private ApplyManager applyManager;

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

            //资产设备报废申请单
            AssetDiscardApply assetDiscardApply = PersistenceFactory.getInstance().findByPK(AssetDiscardApply.class, uuid);
            AssetCard assetCard = PersistenceFactory.getInstance().findByPK(AssetCard.class, certificateManager.isNullReturnEmpty(assetDiscardApply.getCardUuid()));

            assetCard.setNetValue(assetDiscardApply.getNetValue());
            assetCard.setStatus("P");

            PersistenceFactory.getInstance().update(assetCard);

            /*Map<String, Object> cdmFileRelationConditionsMap = new HashMap<String, Object>();
            cdmFileRelationConditionsMap.put(CdmFileRelationAttribute.DATA_ID, certificateManager.isNullReturnEmpty(assetDiscardApply.getUuid()));
            List<CdmFileRelation> cdmFileRelationList = PersistenceFactory.getInstance().findByAnyFields(CdmFileRelation.class, cdmFileRelationConditionsMap, null);
            for (int i = 0; i < cdmFileRelationList.size(); i++) {
                CdmFileRelation cdmFileRelation_g = (CdmFileRelation) cdmFileRelationList.get(i);
                CdmFileRelation cdmFileRelation_s = BeanFactory.getObject(CdmFileRelation.class);
                BeanUtil.bean2Bean(cdmFileRelation_g, cdmFileRelation_s);
                cdmFileRelation_s.setUuid(null);
                cdmFileRelation_s.setDataId(certificateManager.isNullReturnEmpty(assetDiscardApply.getCardUuid()));
                PersistenceFactory.getInstance().insert(cdmFileRelation_s);
            }*/
        }

        //Do After
        return out;
    }
}
