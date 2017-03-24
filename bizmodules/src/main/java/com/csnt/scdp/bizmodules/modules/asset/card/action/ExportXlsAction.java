package com.csnt.scdp.bizmodules.modules.asset.card.action;

import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.asset.card.services.intf.CardManager;

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
 * @timestamp 2015-09-22 10:18:39
 */

@Scope("singleton")
@Controller("card-exportxls")
@Transactional
public class ExportXlsAction extends CommonExportXlsAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ExportXlsAction.class);

    @Resource(name = "card-manager")
    private CardManager cardManager;

    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {

        //Do before
        if (
                certificateManager.isNullReturnEmpty(inMap.get("layoutFile")).equals("card-query-layout.xml")
                        ||
                        certificateManager.isNullReturnEmpty(inMap.get("layoutFile")).equals("jcard-query-layout.xml")
                        ||
                        certificateManager.isNullReturnEmpty(inMap.get("layoutFile")).equals("jcard2-query-layout.xml")
                ) {
            Map queryConditions = (Map) inMap.get("queryConditions");
            String cardNotUse = StringUtil.replaceNull(queryConditions.get("cardNotUse"));
            Map<String, String> extraMap = new HashMap<String, String>();
            if ("1".equals(cardNotUse)) {
                extraMap.put("cardNotUse", " a.status<>'P' and a.state = 2 ");
            } else {
                extraMap.put("cardNotUse", " 1=1");
            }
            queryConditions.remove("cardNotUse");
            inMap.put(CommonAttribute.QUERY_EXTRA_PARAMS, extraMap);
        }

        //Do After
        Map out = super.perform(inMap);
        return out;
    }
}
