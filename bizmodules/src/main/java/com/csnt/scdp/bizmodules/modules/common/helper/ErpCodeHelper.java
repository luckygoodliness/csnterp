package com.csnt.scdp.bizmodules.modules.common.helper;

import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;

import java.util.List;

/**
 * Created by huangming on 2015-11-03.
 */
public class ErpCodeHelper {
    public static List findByCodeType(String codeType) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        DAOMeta daoMeta = new DAOMeta();
        String sql = "select * from scdp_code where code_type = '" + codeType + "'";
        daoMeta.setStrSql(sql);
        List lstscdpCode = pcm.findByNativeSQL(daoMeta);
        return lstscdpCode;
    }

    public static List findByCodeTypeAndCodeDesc(String codeType, String codeDesc) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        DAOMeta daoMeta = new DAOMeta();
        String sql = "select * from scdp_code where code_type = '" + codeType + "' and code_desc = '" + codeDesc + "'";
        daoMeta.setStrSql(sql);
        List lstscdpCode = pcm.findByNativeSQL(daoMeta);
        return lstscdpCode;
    }

    public static List findByCodeTypeAndSysCode(String codeType, String sysCode) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        DAOMeta daoMeta = new DAOMeta();
        String sql = "select * from scdp_code where code_type = '" + codeType + "' and sys_code = '" + sysCode + "'";
        daoMeta.setStrSql(sql);
        List lstscdpCode = pcm.findByNativeSQL(daoMeta);
        return lstscdpCode;
    }
}
