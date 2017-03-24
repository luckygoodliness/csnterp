package com.csnt.scdp.bizmodules.modules.fad.report.action;

import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
import com.csnt.scdp.framework.commonaction.CommonReportQueryAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shenyx on 2016/1/27.
 */
@Scope("singleton")
@Controller("manager_dep-get")
@Transactional
public class ManagerDepGetAction extends CommonReportQueryAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ManagerDepGetAction.class);
    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map result = new HashMap<>();

        String orgCode = certificateManager.isNullReturnEmpty(inMap.get("orgCode"));

        if (orgCode.length() > 0) {
            String sql = "SELECT\n" +
                    "1\n" +
                    "FROM\n" +
                    "SCDP_ORG O\n" +
                    "LEFT JOIN SCDP_EXPAND_ROW R\n" +
                    "ON\n" +
                    "O.UUID=R.DATA_UUID\n" +
                    "LEFT JOIN SCDP_EXPAND_COLUMN T\n" +
                    "ON\n" +
                    "R.EXPAND_ID= T.UUID\n" +
                    "WHERE\n" +
                    "T.EXPAND_TYPE = 'ORG'\n" +
                    "AND\n" +
                    "R.EXPAND_VALUE='管理部'\n" +
                    "AND\n" +
                    "O.ORG_CODE='" + orgCode + "'";

            DAOMeta daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                result.put("isManagerDep", "1");
            } else {
                result.put("isManagerDep", "0");
            }
        } else {
            result.put("isManagerDep", "1");
        }
        return result;
    }
}
