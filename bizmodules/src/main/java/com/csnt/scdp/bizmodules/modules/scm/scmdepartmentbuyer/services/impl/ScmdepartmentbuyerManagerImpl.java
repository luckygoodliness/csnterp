package com.csnt.scdp.bizmodules.modules.scm.scmdepartmentbuyer.services.impl;

import com.csnt.scdp.bizmodules.modules.scm.scmdepartmentbuyer.services.intf.ScmdepartmentbuyerManager;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  ScmdepartmentbuyerManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-11-23 11:06:51
 */

@Scope("singleton")
@Service("scmdepartmentbuyer-manager")
public class ScmdepartmentbuyerManagerImpl implements ScmdepartmentbuyerManager {
    @Override
    public void setExtraData(Map outMap) {
        Map dtoMap = (Map) outMap.get("scmDepartmentBuyerDto");
        if (dtoMap != null) {
            //1.设置负责人的显示
            String buyerName = "";
            String buyer = (String) dtoMap.get("buyer");
            if (StringUtil.isNotEmpty(buyer)) {
                Map paramMap = new HashMap<>();
                paramMap.put(ScdpUserAttribute.USER_ID, buyer);
                List<ScdpUser> list = PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, paramMap, null);
                if (ListUtil.isNotEmpty(list)) {
                    buyerName = list.get(0).getUserName();
                }
            }
            dtoMap.put("buyerName", buyerName);
            dtoMap.put("officeCodeDesc", OrgHelper.getOrgNameByCode((String) dtoMap.get("officeCode")));
        }
    }
}