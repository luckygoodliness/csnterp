package com.csnt.scdp.bizmodules.modules.nonprm.monitor.action;

import com.csnt.scdp.bizmodules.entity.fad.FadCertificate;
import com.csnt.scdp.bizmodules.entity.nonprm.MonitorBase;
import com.csnt.scdp.bizmodules.modules.nonprm.monitor.services.intf.MonitorManager;
import com.csnt.scdp.bizmodules.modules.nonprm.monitorlaborcostandothershareexpense.dto.MonitorBaseDto;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;
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
@Controller("monitorlaborcostandothershareexpense-load-actual")
@Transactional
public class LoadMonitorLaborCostAndOtherShareExpenseAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadMonitorMAction.class);

    @Resource(name = "monitor-manager")
    private MonitorManager monitorManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = new HashMap();

        String tblVersion = "";
        String officeId = monitorManager.isNullReturnEmpty(inMap.get("officeId"));
        if (!(officeId.length() > 0)) {
            throw new BizException("月度实际发生录入时，请先填写查询条件的【部门】、【年】、【月】！");
        } else {
            Map<String, Object> monitorBaseConditionsMap = new HashMap<String, Object>();
            monitorBaseConditionsMap.put("officeId", monitorManager.isNullReturnEmpty(inMap.get("officeId")));
            ;
            List monitorBaseList = monitorManager.isNullReturnEmpty(inMap.get("officeId")).length() > 0 ? DtoHelper.findByAnyFields(MonitorBaseDto.class, monitorBaseConditionsMap, "seq_no asc") : new ArrayList<>();
            if (!(monitorBaseList.size() > 0)) {
                MonitorBaseDto monitorBaseInsert = BeanFactory.getObject(MonitorBaseDto.class);
                monitorBaseInsert.setUuid(null);
                monitorBaseInsert.setOfficeId(officeId);
                monitorBaseList.add(monitorBaseInsert);

                try {
                    DtoHelper.batchAdd(monitorBaseList, MonitorBase.class);
                } catch (Exception e) {
                }
                monitorBaseList = monitorManager.isNullReturnEmpty(inMap.get("officeId")).length() > 0 ? DtoHelper.findByAnyFields(MonitorBaseDto.class, monitorBaseConditionsMap, "seq_no asc") : new ArrayList<>();
            }

            //String monitorBaseUuid = "1";
            //MonitorBase monitorBase = PersistenceFactory.getInstance().findByPK(MonitorBase.class, monitorBaseUuid);
            tblVersion = monitorManager.isNullReturnEmpty(((MonitorBase) monitorBaseList.get(0)).getTblVersion());

            List lstMonitorLaborCost = monitorManager.getMonitorLaborCost(inMap);
            if (!(lstMonitorLaborCost.size() > 0)) {
                lstMonitorLaborCost = monitorManager.getMonitorLaborCostM(inMap);
            }
            List lstMonitorOtherShare = monitorManager.getMonitorOtherShare(inMap);
            if (!(lstMonitorOtherShare.size() > 0)) {
                lstMonitorOtherShare = monitorManager.getMonitorOtherShareM(inMap);
            }

            out.put("tblVersion", tblVersion);
            out.put("monitorLaborCostDto", lstMonitorLaborCost);
            out.put("monitorOtherShareDto", lstMonitorOtherShare);
            //Do After
            return out;
        }
    }
}
