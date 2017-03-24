package com.csnt.scdp.bizmodules.modules.scm.requestallot.action;

import com.csnt.scdp.bizmodules.modules.scm.requestallot.services.intf.RequestallotManager;
import com.csnt.scdp.framework.commonaction.crud.CommonQueryAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Map;

/**
 * Description:  QueryAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-10-22 22:18:10
 */

@Scope("singleton")
@Controller("requestallot-query")
@Transactional
public class QueryAction extends CommonQueryAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(QueryAction.class);

    @Resource(name = "requestallot-manager")
    private RequestallotManager requestallotManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        //1.为申请单分配查询页面设置额外的查询条件（当负责人不填时，只查询负责人为空的数据）
        requestallotManager.setExtraQueryConditions(inMap);
        Map out = super.perform(inMap);
        //Do After
        return out;
    }
}
