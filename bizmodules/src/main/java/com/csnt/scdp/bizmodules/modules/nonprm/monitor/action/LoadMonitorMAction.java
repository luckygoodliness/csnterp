package com.csnt.scdp.bizmodules.modules.nonprm.monitor.action;

import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectExtraItem;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectIncome;
import com.csnt.scdp.bizmodules.modules.nonprm.income.services.intf.IncomeManager;
import com.csnt.scdp.bizmodules.modules.nonprm.monitor.services.intf.MonitorManager;
import com.csnt.scdp.framework.commonaction.crud.CommonLoadAction;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  LoadAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-27 15:12:59
 */

@Scope("singleton")
@Controller("monitorm-load-actual")
@Transactional
public class LoadMonitorMAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadMonitorMAction.class);

    @Resource(name = "monitor-manager")
    private MonitorManager monitorManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = new HashMap();

        Object yearTmp = inMap.get("year");
        if (yearTmp != null) {
            String year = yearTmp.toString();
            if (StringUtil.isNotEmpty(year)) {
                monitorManager.initialMonitorData(year);
            }
        }
        List lstValuesOther = monitorManager.getNonPrmIncomeByYear(inMap);
        List lstValueCon = monitorManager.getOperateAgreementByYear(inMap);

        out.put("operateAgreementDto", lstValueCon);
        out.put("otherNoPrmOutDto", lstValuesOther);//其他非项目支出
        //Do After
        return out;
    }
}
