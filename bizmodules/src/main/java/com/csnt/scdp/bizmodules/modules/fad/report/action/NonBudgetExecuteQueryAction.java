package com.csnt.scdp.bizmodules.modules.fad.report.action;

import com.csnt.scdp.framework.commonaction.CommonReportQueryAction;
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
 * Created by shenyx on 2016/1/27.
 */
@Scope("singleton")
@Controller("non_fad_cashreq_detail-query")
@Transactional
public class NonBudgetExecuteQueryAction extends CommonReportQueryAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(NonBudgetExecuteQueryAction.class);

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map out = super.perform(inMap);
        //Do After
        return out;
    }
}
