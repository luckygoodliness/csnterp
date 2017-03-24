package com.csnt.scdp.bizmodules.modules.fad.certificate.action;

import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 15:32:42
 */

@Scope("singleton")
@Controller("certificate-getAnyValue")
@Transactional
public class GetAnyValueAction implements IAction {

    private static ILogTracer tracer = LogTracerFactory.getInstance(GetAnyValueAction.class);
    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map result = new HashMap();

        String field = certificateManager.isNullReturnEmpty(inMap.get("field"));
        String sql = certificateManager.isNullReturnEmpty(inMap.get("sql"));
        if (field.length() > 0 && sql.length() > 0) {
            DAOMeta daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            List anyValueMapList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            String value = "";
            if (anyValueMapList.size() > 0) {
                Map anyValueMap = (Map) anyValueMapList.get(0);
                value = certificateManager.isNullReturnEmpty(anyValueMap.get(field));
            }
            result.put("value", value);
        } else {
            result.put("value", "");
        }
        return result;
    }
}
