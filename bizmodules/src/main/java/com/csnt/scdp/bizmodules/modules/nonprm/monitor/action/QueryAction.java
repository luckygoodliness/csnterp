package com.csnt.scdp.bizmodules.modules.nonprm.monitor.action;

import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectExtraItem;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectIncome;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectIncomeIn;
import com.csnt.scdp.bizmodules.modules.nonprm.income.services.intf.IncomeManager;
import com.csnt.scdp.framework.commonaction.CommonReportQueryAction;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.DateUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.nonprm.monitor.services.intf.MonitorManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Description:  QueryAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-27 15:12:59
 */

@Scope("singleton")
@Controller("monitor-query")
@Transactional
public class QueryAction extends CommonReportQueryAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(QueryAction.class);

    @Resource(name = "monitor-manager")
    private MonitorManager monitorManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {

        Map queryConditions = (Map) inMap.get("queryConditions");
        Object yearTmp = queryConditions.get("year");
        if (yearTmp != null) {
            String year = yearTmp.toString();
            if (StringUtil.isNotEmpty(year)) {
                monitorManager.initialMonitorData(year);
            }
        }
        Map out = super.perform(inMap);
        //Do After
        return out;
    }
}
