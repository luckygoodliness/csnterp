package com.csnt.scdp.bizmodules.modules.scm.scmoverduereceivables.services.impl;

import com.csnt.scdp.bizmodules.entity.scm.ScmContract;
import com.csnt.scdp.bizmodules.entity.scm.ScmSupplier;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmOverdueReceivablesAttribute;
import com.csnt.scdp.bizmodules.modules.scm.scmoverduereceivables.services.intf.ScmoverduereceivablesManager;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Description:  ScmoverduereceivablesManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-27 19:55:27
 */

@Scope("singleton")
@Service("scmoverduereceivables-manager")
public class ScmoverduereceivablesManagerImpl implements ScmoverduereceivablesManager {
    @Override
    public void setExtraData(Map outMap) {
        Map dtoMap = (Map) outMap.get("scmsupplierDto");
        if (dtoMap != null) {
            //1.设置供应商名的显示
            String supplierName = "";
            String supplier = (String) dtoMap.get(ScmOverdueReceivablesAttribute.SUPPLIER);
            ScmSupplier scmSupplier = PersistenceFactory.getInstance().findByPK(ScmSupplier.class, supplier);
            if (scmSupplier != null) {
                supplierName = scmSupplier.getCompleteName();
            }

            dtoMap.put(ScmOverdueReceivablesAttribute.SUPPLIER_NAME, supplierName);
            //2.设置合同号的显示
            String scmContractName = "";
            String scmContract = (String) dtoMap.get(ScmOverdueReceivablesAttribute.SCM_CONTRACT);
            ScmContract scmContractObj = PersistenceFactory.getInstance().findByPK(ScmContract.class, scmContract);
            if (scmContractObj != null) {
                scmContractName = scmContractObj.getScmContractCode();
            }
            dtoMap.put(ScmOverdueReceivablesAttribute.SCM_CONTRACT_NAME, scmContractName);
        }
    }
}