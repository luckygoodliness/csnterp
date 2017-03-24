package com.csnt.scdp.bizmodules.modules.prm.receipts.action;

import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.fr.stable.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.prm.receipts.services.intf.ReceiptsManager;

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
 * @timestamp 2015-09-23 09:19:57
 */

@Scope("singleton")
@Controller("receipts-exportxls")
@Transactional
public class ExportXlsAction extends CommonExportXlsAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ExportXlsAction.class);

    @Resource(name = "receipts-manager")
    private ReceiptsManager receiptsManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Long isReceMoney = null;
        Map queryConditions = (Map) inMap.get("queryConditions");
        try {
            isReceMoney = (Long) queryConditions.get("isReceMoney");
        } catch (Exception e) {

        }

        Map queryDistinctMap = new HashMap<>();
        String sql = "";
        if (isReceMoney != null && isReceMoney == 0) {
            queryConditions.remove("isReceMoney");
            sql += String.format(" nvl(pre.ACTUAL_MONEY,0)=0 ");
        } else if (isReceMoney != null && isReceMoney == 1) {
            queryConditions.remove("isReceMoney");
            sql += String.format(" nvl(pre.ACTUAL_MONEY,0)<>0 ");
        }
        if (StringUtils.isNotEmpty(sql)) {
            queryDistinctMap.put("extend", sql);
            inMap.put(CommonAttribute.QUERY_EXTRA_PARAMS, queryDistinctMap);
        } else {
            queryDistinctMap.put("extend", " 1=1 ");
            inMap.put(CommonAttribute.QUERY_EXTRA_PARAMS, queryDistinctMap);
        }
        //Do before
        Map out = super.perform(inMap);
        //Do After
        return out;
    }
}
