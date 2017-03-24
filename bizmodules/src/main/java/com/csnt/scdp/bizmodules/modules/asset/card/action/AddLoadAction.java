package com.csnt.scdp.bizmodules.modules.asset.card.action;

import com.csnt.scdp.bizmodules.entity.asset.AssetCard;
import com.csnt.scdp.bizmodules.modules.asset.card.dto.AssetCardDto;
import com.csnt.scdp.bizmodules.modules.asset.card.services.intf.CardManager;
import com.csnt.scdp.bizmodules.modules.asset.check.dto.AssetAnnualCheckDto;
import com.csnt.scdp.bizmodules.modules.asset.maintain.dto.AssetMaintainDto;
import com.csnt.scdp.bizmodules.modules.asset.transfer.dto.AssetTransferDto;
import com.csnt.scdp.bizmodules.modules.common.action.ErpLoadAction;
import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.DateUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpOrg;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.entityattributes.ScdpOrgAttribute;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Description:  LoadAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-22 10:18:39
 */

@Scope("singleton")
@Controller("card-addLoad")
@Transactional
public class AddLoadAction extends ErpLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

    @Resource(name = "card-manager")
    private CardManager cardManager;

    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {

        //Do before
        Map out = super.perform(inMap);
        Map dtoMap = (Map) out.get("assetHandoverDto");
        if (dtoMap != null) {
            String orgCode = certificateManager.isNullReturnEmpty(dtoMap.get("officeId"));
            if (certificateManager.isNullReturnEmpty(orgCode).length() > 0) {
                String orgName = certificateManager.isNullReturnEmpty(OrgHelper.getOrgNameByCode(orgCode));
                dtoMap.put("officeIdDesc", orgName);
            }

            String userId = certificateManager.isNullReturnEmpty(dtoMap.get("liablePerson"));
            Map<String, Object> scdpUserConditionsMap = new HashMap<String, Object>();
            scdpUserConditionsMap.put(ScdpUserAttribute.USER_ID, userId);
            List<ScdpUser> scdpUserList = certificateManager.isNullReturnEmpty(userId).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, scdpUserConditionsMap, null) : new ArrayList<>();
            if (scdpUserList.size() > 0) {
                ScdpUser scdpUser = (ScdpUser) scdpUserList.get(0);
                String userName = certificateManager.isNullReturnEmpty(scdpUser.getUserName());
                dtoMap.put("liablePersonDesc", userName);
            }
        }

        //Do After
        return out;
    }
}
