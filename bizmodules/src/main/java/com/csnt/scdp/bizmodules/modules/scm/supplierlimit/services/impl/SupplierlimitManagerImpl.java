package com.csnt.scdp.bizmodules.modules.scm.supplierlimit.services.impl;

import com.csnt.scdp.bizmodules.entity.scm.ScmSupplierLimit;
import com.csnt.scdp.bizmodules.modules.scm.supplierlimit.services.intf.SupplierlimitManager;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.util.ListUtil;
import com.fr.third.org.apache.poi.hssf.record.formula.functions.Int;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Description:  SupplierlimitManagerImpl
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-07-19 15:14:12
 */

@Scope("singleton")
@Service("supplierlimit-manager")
public class SupplierlimitManagerImpl implements SupplierlimitManager {

    public List<Map<String, Object>> selectCurAmount(String uuid) {
        DAOMeta daoMeta = new DAOMeta();
        String reqSql = "SELECT SC.SUPPLIER_CODE," +
                " SUM(SC.AMOUNT) AS CUR_AMOUNT," +
                " COUNT(1) AS CUR_VOLUME" +
                " FROM SCM_CONTRACT SC" +
                " WHERE SC.STATE = 2" +
                " and exists (select 1 FROM SCM_SUPPLIER_LIMIT_DETAIL D WHERE D.SCM_SUPPLIER_LIMIT_ID='"+uuid+"' AND SC.SUPPLIER_CODE=D.SCM_SUPPLIER_ID)" +
                " AND SC.SUPPLIER_CODE IS NOT NULL" +
                " GROUP BY SC.SUPPLIER_CODE";
        daoMeta.setStrSql(reqSql);
        List<Map<String, Object>> reqList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        return reqList;
    }

    public List<Map<String, Object>> selectTime(String beginTime, String endTime) {
        DAOMeta daoMeta = new DAOMeta();
        String reqSql = "select * from scm_supplier_limit s where (to_date('" +beginTime+"','yyyy-mm-dd')BETWEEN  s.begin_time and s.end_time )" +
                "or(to_date('"+ endTime+ "','yyyy-mm-dd')BETWEEN  s.begin_time and s.end_time ) or " +
                "(to_date('"+beginTime+"','yyyy-mm-dd')<= s.begin_time and s.end_time <=to_date('"+endTime+"','yyyy-mm-dd')) ";
        daoMeta.setStrSql(reqSql);
        List<Map<String, Object>> reqList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        return reqList;
    }

    public List<ScmSupplierLimit> selectYear(Integer year) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        List<ScmSupplierLimit> scmSupplierList = new ArrayList<>();
            Map mapContractId = new HashMap();
            mapContractId.put("year", year);
            scmSupplierList = pcm.findByAnyFields(ScmSupplierLimit.class, mapContractId, null);
        return scmSupplierList;
    }


}