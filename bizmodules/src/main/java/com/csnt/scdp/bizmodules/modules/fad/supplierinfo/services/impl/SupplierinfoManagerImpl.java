package com.csnt.scdp.bizmodules.modules.fad.supplierinfo.services.impl;

import com.csnt.scdp.bizmodules.attributes.CommonAttribute;
import com.csnt.scdp.bizmodules.modules.fad.supplierinfo.services.intf.SupplierinfoManager;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description:  SupplierinfoManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-12-10 13:50:46
 */

@Scope("singleton")
@Service("supplierinfo-manager")
public class SupplierinfoManagerImpl implements SupplierinfoManager {

    @Override
    public void validateUnique(String uuid, String completeName, String editFlag) {
        String valSql = "SELECT S1.UUID FROM SCM_SUPPLIER S1 WHERE S1.COMPLETE_NAME = ? " +
                "UNION ALL SELECT S2.UUID FROM FAD_SUPPLIER S2 WHERE S2.COMPLETE_NAME = ?";
        DAOMeta daoMeta = new DAOMeta();
        List paramLst = new ArrayList();
        paramLst.add(completeName);
        paramLst.add(completeName);
        daoMeta.setStrSql(valSql);
        daoMeta.setLstParams(paramLst);

        List list = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if(list.size()>0){
            if("+".equals(editFlag)){
                throw new BizException("公司全称为"+completeName+"的SUPPLIER已存在！");
            }
            if("*".equals(editFlag)){
                Map map = (Map)list.get(0);
                String uuidDb = StringUtil.replaceNull(map.get(CommonAttribute.UUID));
                if(uuidDb.equals(uuid)){
                    return;
                }else{
                    throw new BizException("公司全称为"+completeName+"的SUPPLIER已存在！");
                }
            }
        }
    }
}