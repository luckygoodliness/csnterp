package com.csnt.scdp.bizmodules.modules.nonprm.nonprmpurchasereq.action;

import com.csnt.scdp.bizmodules.modules.nonprm.nonprmpurchasereq.services.intf.NonprmpurchasereqManager;
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
 * @timestamp 2015-11-19 14:00:14
 */

@Scope("singleton")
@Controller("nonprmbudget-query")
@Transactional
public class QuerynonprmBudgetAction extends CommonQueryAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(QuerynonprmBudgetAction.class);

	@Resource(name = "nonprmpurchasereq-manager")
	private NonprmpurchasereqManager nonprmpurchasereqManager;


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        //Do After
        return out;
    }
}
