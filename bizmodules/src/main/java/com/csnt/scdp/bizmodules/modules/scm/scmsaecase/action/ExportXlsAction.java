package com.csnt.scdp.bizmodules.modules.scm.scmsaecase.action;

import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.scm.scmsaecase.services.intf.ScmsaecaseManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:  ExportXlsAction
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-08-16 15:17:56
 */

@Scope("singleton")
@Controller("scmsaecase-exportxls")
@Transactional
public class ExportXlsAction extends CommonExportXlsAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(ExportXlsAction.class);

	@Resource(name = "scmsaecase-manager")
	private ScmsaecaseManager scmsaecaseManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
        Map queryExtraParams= (Map) inMap.get(CommonAttribute.QUERY_EXTRA_PARAMS);
        if(queryExtraParams==null){
            queryExtraParams=new HashMap();
            inMap.put(CommonAttribute.QUERY_EXTRA_PARAMS,queryExtraParams);
        }
        StringBuffer sb=new StringBuffer();
        String selfConditions= (String) queryExtraParams.get("selfconditions");
        if(StringUtil.isEmpty(selfConditions)){
            sb.append(" 1=1 ");
        }else{
            sb.append(selfConditions);
        }
        queryExtraParams.put("selfconditions",sb.toString());

        Map out = super.perform(inMap);
		//Do After
		return out;
	}
}
