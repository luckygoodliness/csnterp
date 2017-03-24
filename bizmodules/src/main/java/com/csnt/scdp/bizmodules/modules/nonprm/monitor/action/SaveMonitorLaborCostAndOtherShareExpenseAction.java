package com.csnt.scdp.bizmodules.modules.nonprm.monitor.action;

import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectIncome;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectIncomeIn;
import com.csnt.scdp.bizmodules.modules.nonprm.income.services.intf.IncomeManager;
import com.csnt.scdp.bizmodules.modules.nonprm.monitor.services.intf.MonitorManager;
import com.csnt.scdp.bizmodules.modules.nonprm.monitorlaborcostandothershareexpense.dto.MonitorLaborCostDto;
import com.csnt.scdp.bizmodules.modules.nonprm.monitorlaborcostandothershareexpense.dto.MonitorOtherShareDto;
import com.csnt.scdp.bizmodules.entity.nonprm.MonitorLaborCost;
import com.csnt.scdp.bizmodules.entity.nonprm.MonitorOtherShare;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-27 15:12:59
 */

@Scope("singleton")
@Controller("monitorlaborcostandothershareexpense-save-actual")
@Transactional
public class SaveMonitorLaborCostAndOtherShareExpenseAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(AddMonAction.class);

    @Resource(name = "monitor-manager")
    private MonitorManager monitorManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = new HashMap();

        String tblVersion = monitorManager.isNullReturnEmpty(inMap.get("tblVersion"));
        String officeId = monitorManager.isNullReturnEmpty(inMap.get("officeId"));
        String year = monitorManager.isNullReturnEmpty(inMap.get("year"));
        String month = monitorManager.isNullReturnEmpty(inMap.get("month"));
        monitorManager.monitorBaseCheckTimeStamp(officeId, tblVersion);

        String sql = "";
        DAOMeta daoMeta = null;

        sql = "DELETE FROM MONITOR_LABOR_COST WHERE OFFICE_ID = '" + officeId + "' AND YEAR = '" + year + "' AND MONTH = '" + month + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        sql = "DELETE FROM MONITOR_OTHER_SHARE WHERE OFFICE_ID = '" + officeId + "' AND YEAR = '" + year + "' AND MONTH = '" + month + "'";
        daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

        List lstMonitorLaborCost = (ArrayList) inMap.get("monitorLaborCostArray");
        List lstMonitorOtherShare = (ArrayList) inMap.get("monitorOtherShareArray");

        List monitorLaborCostLstForInsert = new ArrayList<>();
        if (ListUtil.isNotEmpty(lstMonitorLaborCost)) {
            for (Object obj : lstMonitorLaborCost) {
                Map map = (Map) obj;
                MonitorLaborCostDto monitorLaborCostDto = (MonitorLaborCostDto) BeanUtil.map2Dto(map, MonitorLaborCostDto.class);
                monitorLaborCostDto.setUuid(null);
                monitorLaborCostDto.setOfficeId(officeId);
                monitorLaborCostDto.setYear(Integer.parseInt(year));
                monitorLaborCostDto.setMonth(Integer.parseInt(month));
                monitorLaborCostLstForInsert.add(monitorLaborCostDto);
            }
            DtoHelper.batchAdd(monitorLaborCostLstForInsert, MonitorLaborCost.class);
        }

        List monitorOtherShareLstForInsert = new ArrayList<>();
        if (ListUtil.isNotEmpty(lstMonitorOtherShare)) {
            for (Object obj : lstMonitorOtherShare) {
                Map map = (Map) obj;
                MonitorOtherShareDto monitorOtherShareDto = (MonitorOtherShareDto) BeanUtil.map2Dto(map, MonitorOtherShareDto.class);
                monitorOtherShareDto.setUuid(null);
                monitorOtherShareDto.setOfficeId(officeId);
                monitorOtherShareDto.setYear(Integer.parseInt(year));
                monitorOtherShareDto.setMonth(Integer.parseInt(month));
                monitorOtherShareLstForInsert.add(monitorOtherShareDto);
            }
            DtoHelper.batchAdd(monitorOtherShareLstForInsert, MonitorOtherShare.class);
        }

        out.put("info", "success");
        //Do After
        return out;
    }
}
