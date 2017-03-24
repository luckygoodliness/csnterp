package com.csnt.scdp.bizmodules.modules.erpsys.erpoffice.action;

/**
 * Created by fisher on 2015/11/4.
 */

import com.csnt.scdp.bizmodules.modules.common.helper.ErpOrgHelper;
import com.csnt.scdp.framework.commonaction.crud.CommonQueryAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.helper.UserHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.Map;


@Scope("singleton")
@Controller("erpofficemajor-query")
@Transactional
public class QueryMajorAction extends CommonQueryAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(QueryMajorAction.class);

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        String userId = (String) inMap.get("userId");
        String officeId = UserHelper.getOrgUuid();
        return ErpOrgHelper.getDeptMajorProjectMoney();
    }
}