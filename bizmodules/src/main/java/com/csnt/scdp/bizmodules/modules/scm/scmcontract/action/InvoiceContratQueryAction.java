package com.csnt.scdp.bizmodules.modules.scm.scmcontract.action;

import com.csnt.scdp.bizmodules.modules.scm.scmcontract.services.intf.ScmcontractManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.CommonQueryAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:  QueryAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 15:32:42
 */

@Scope("singleton")
@Controller("invoicecontract-query")
@Transactional
public class InvoiceContratQueryAction extends CommonQueryAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(InvoiceContratQueryAction.class);

    @Resource(name = "scmcontract-manager")
    private ScmcontractManager scmcontractManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        StringBuffer selfSql = new StringBuffer();
        selfSql.append(" 1=1 ");
        //Do before
        Map condMap = (Map) inMap.get("queryConditions");
        Object printFalg = condMap.get("supplierCodeS");
        condMap.remove("supplierCodeS");
//        if ("1".equals(StringUtil.replaceNull(printFalg).toString())) {
//            selfSql.append(" and ivhdr.print_num>0 ");
//        } else if ("0".equals(StringUtil.replaceNull(printFalg).toString())) {
//            selfSql.append(" and (ivhdr.print_num=0 or ivhdr.print_num is null)");
//        }
        if (StringUtil.isNotEmpty(printFalg)) {
            selfSql.append(" and t.uuid not in (" + printFalg + ")");
        }
        Map map = new HashMap();
        map.put("selfconditions", selfSql.toString());
        inMap.put(CommonAttribute.QUERY_EXTRA_PARAMS, map);
        Map out = super.perform(inMap);
        //Do After
        return out;
    }
}
