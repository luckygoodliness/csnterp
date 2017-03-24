package com.csnt.scdp.bizmodules.modules.fad.certificate.action;

import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:  ExportXlsAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-10-16 10:07:48
 */

@Scope("singleton")
@Controller("certificate-exportxls")
@Transactional
public class ExportXlsAction extends CommonExportXlsAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ExportXlsAction.class);

    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        if (certificateManager.isNullReturnEmpty(inMap.get("layoutFile")).equals("certificate-query-layout.xml")) {
            StringBuffer condSql = new StringBuffer();
            condSql.append(" 1=1 ");
            Map condMap = (Map) inMap.get("queryConditions");
            String debtorSearch = certificateManager.isNullReturnEmpty(condMap.get("debtorSearch"));
            String debtorSymbol = certificateManager.isNullReturnEmpty(condMap.get("debtorSymbol"));
            condMap.remove("debtorSearch");
            condMap.remove("debtorSymbol");

            if (debtorSearch.length() > 0 && debtorSymbol.length() > 0) {
                if (debtorSymbol.equals("0")) {
                    condSql.append(" and debtor = " + debtorSearch);
                }
                if (debtorSymbol.equals("1")) {
                    condSql.append(" and debtor >= " + debtorSearch);
                }
                if (debtorSymbol.equals("2")) {
                    condSql.append(" and debtor <= " + debtorSearch);
                }
            } else if (debtorSearch.length() > 0) {
                condSql.append(" and to_char(debtor) like '%" + debtorSearch + "%'");
            }

            Map condmap = new HashMap();
            condmap.put("selfconditions", condSql.toString());
            inMap.put(CommonAttribute.QUERY_EXTRA_PARAMS, condmap);
        }
        Map out = super.perform(inMap);
        //Do After
        return out;
    }
}
