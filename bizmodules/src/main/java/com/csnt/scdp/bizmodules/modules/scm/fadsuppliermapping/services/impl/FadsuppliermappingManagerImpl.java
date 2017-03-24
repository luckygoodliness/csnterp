package com.csnt.scdp.bizmodules.modules.scm.fadsuppliermapping.services.impl;

import com.csnt.scdp.bizmodules.entity.scm.FadSupplierMapping;
import com.csnt.scdp.bizmodules.entity.scm.ScmSupplier;
import com.csnt.scdp.bizmodules.modules.scm.fadsuppliermapping.dto.FadSupplierMappingDto;
import com.csnt.scdp.bizmodules.modules.scm.fadsuppliermapping.services.intf.FadsuppliermappingManager;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.ListUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  FadsuppliermappingManagerImpl
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-08-04 15:22:39
 */

@Scope("singleton")
@Service("fadsuppliermapping-manager")
public class FadsuppliermappingManagerImpl implements FadsuppliermappingManager {

    public void fadSupplierMappingChange(String supplierFromName, String supplierToUuid) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        Map conditionMap = new HashMap<>();
        conditionMap.put("completeName", supplierFromName);
        List<ScmSupplier> scmSupplier = pcm.findByAnyFields(ScmSupplier.class, conditionMap, null);
        if (ListUtil.isNotEmpty(scmSupplier)) {
            Map conditionMapTwo = new HashMap<>();
            conditionMapTwo.put("supplierToUuid", scmSupplier.get(0).getUuid());
            List<FadSupplierMapping> fadSupplierMapping = pcm.findByAnyFields(FadSupplierMapping.class, conditionMapTwo, null);
            for (int i = 0; i < fadSupplierMapping.size(); i++) {
                fadSupplierMapping.get(i).setSupplierToUuid(supplierToUuid);
            }
        }
    }

}