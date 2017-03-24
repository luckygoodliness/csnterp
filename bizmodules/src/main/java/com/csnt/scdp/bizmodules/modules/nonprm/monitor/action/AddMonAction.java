package com.csnt.scdp.bizmodules.modules.nonprm.monitor.action;

import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectIncome;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectIncomeIn;
import com.csnt.scdp.bizmodules.modules.nonprm.income.services.intf.IncomeManager;
import com.csnt.scdp.bizmodules.modules.nonprm.monitor.services.intf.MonitorManager;
import com.csnt.scdp.bizmodules.modules.nonprm.monitorm.dto.NonProjectIncomeMonDto;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
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
@Controller("monitor-month-add")
@Transactional
public class AddMonAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(AddMonAction.class);

    @Resource(name = "monitor-manager")
    private MonitorManager monitorManager;

    @Resource(name = "income-manager")
    private IncomeManager incomeManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = new HashMap();
        List lstOperateAgreement = (ArrayList) inMap.get("operateAgreementArray");
        List lstOtherNonPrm = (ArrayList) inMap.get("otherNoPrmOutArray");

        if (ListUtil.isNotEmpty(lstOperateAgreement)) {
            for (Object obj : lstOperateAgreement) {
                Map map = (Map) obj;
                NonProjectIncomeMonDto nonProjectMonDto = (NonProjectIncomeMonDto) BeanUtil.map2Dto(map, NonProjectIncomeMonDto.class);
                if (StringUtil.isNotEmpty(nonProjectMonDto.getUuid())) {
                    nonProjectMonDto.setEditflag("*");
                } else {
                    nonProjectMonDto.setEditflag("+");
                }
                if (nonProjectMonDto.getOccurredValue() != null) {
                    nonProjectMonDto.setOccurredValue(nonProjectMonDto.getOccurredValue().multiply(new BigDecimal("10000")));
                }
                DtoHelper.CascadeSave(nonProjectMonDto);

                NonProjectIncomeIn nonProjectIncomeIn = incomeManager.getNonProjectIncomeInByUUid(nonProjectMonDto.getPuuid());

                BigDecimal assignedValue = nonProjectMonDto.getAssignedValue() == null ? nonProjectMonDto.getAssignedValue() : nonProjectMonDto.getAssignedValue().multiply(new BigDecimal("10000"));
                nonProjectIncomeIn.setAssignedValue(assignedValue);
                incomeManager.save(nonProjectIncomeIn);
            }
        }
        if (ListUtil.isNotEmpty(lstOtherNonPrm)) {
            for (Object obj : lstOtherNonPrm) {
                Map map = (Map) obj;
                NonProjectIncomeMonDto nonProjectMonDto = (NonProjectIncomeMonDto) BeanUtil.map2Dto(map, NonProjectIncomeMonDto.class);
                if (StringUtil.isNotEmpty(nonProjectMonDto.getUuid())) {
                    nonProjectMonDto.setEditflag("*");
                } else {
                    nonProjectMonDto.setEditflag("+");
                }
                if (nonProjectMonDto.getOccurredValue() != null) {
                    nonProjectMonDto.setOccurredValue(nonProjectMonDto.getOccurredValue().multiply(new BigDecimal("10000")));
                }
                DtoHelper.CascadeSave(nonProjectMonDto);

                NonProjectIncome nonProjectIncome = incomeManager.getNonProjectIncomeByUUid(nonProjectMonDto.getPuuid());

                BigDecimal assignedValue = nonProjectMonDto.getAssignedValue() == null ? nonProjectMonDto.getAssignedValue() : nonProjectMonDto.getAssignedValue().multiply(new BigDecimal("10000"));
                nonProjectIncome.setAssignedValue(assignedValue);
                incomeManager.save(nonProjectIncome);
            }
        }

        out.put("info", "success");
        //Do After
        return out;
    }
}
