package com.csnt.scdp.bizmodules.modules.nonprm.income.action;

import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectIncome;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectIncomeIn;
import com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.dto.NonProjectBudgetAjustHDto;
import com.csnt.scdp.bizmodules.modules.nonprm.income.dto.NonProjectIncome2Dto;
import com.csnt.scdp.bizmodules.modules.nonprm.income.dto.NonProjectIncomeBalanceDto;
import com.csnt.scdp.bizmodules.modules.nonprm.income.dto.NonProjectIncomeDto;
import com.csnt.scdp.bizmodules.modules.nonprm.income.dto.NonProjectIncomeInDto;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.nonprm.income.services.intf.IncomeManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

/**
 * Description:  ModifyAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-24 17:37:27
 */

@Scope("singleton")
@Controller("income-modify")
@Transactional
public class ModifyAction extends CommonModifyAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ModifyAction.class);

    @Resource(name = "income-manager")
    private IncomeManager incomeManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map outMap = new HashMap<>();
        Map map = DtoHelper.getDtoMap(inMap);
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        String year = (String) map.get("uuid");
        //根据年份查找数据库中是否有当年数据记录
        List<NonProjectIncomeInDto> lstIncomeInDto = (List) map.get("nonProjectIncomeInDto");
        if (ListUtil.isNotEmpty(lstIncomeInDto)) {
            for (int i = 0; i < lstIncomeInDto.size(); i++) {
                Map incomeIn = (Map) lstIncomeInDto.get(i);
                String uuid = (String) incomeIn.get("uuid");
                NonProjectIncomeIn comeIn = pcm.findByPK(NonProjectIncomeIn.class, uuid);
                if (incomeIn.get("appliedValue") != null) {
                    comeIn.setAppliedValue(new BigDecimal(incomeIn.get("appliedValue").toString()));
                }
                if (incomeIn.get("firstInstance") != null) {
                    comeIn.setFirstInstance(new BigDecimal(incomeIn.get("firstInstance").toString()));
                }
                if (incomeIn.get("assignedValue") != null) {
                    comeIn.setAssignedValue(new BigDecimal(incomeIn.get("assignedValue").toString()));
                }
                pcm.update(comeIn);
            }
        }

        List<NonProjectIncomeDto> lstIncomeDto = (List<NonProjectIncomeDto>) map.get("nonProjectIncomeDto");
        if (ListUtil.isNotEmpty(lstIncomeDto)){
            for (int i=0; i<lstIncomeDto.size(); i++){
                Map incomeIn = (Map) lstIncomeDto.get(i);
                String uuid = (String) incomeIn.get("uuid");
                NonProjectIncome income = pcm.findByPK(NonProjectIncome.class, uuid);
                if (incomeIn.get("appliedValue") != null) {
                    income.setAppliedValue(new BigDecimal(incomeIn.get("appliedValue").toString()));
                }
                if (incomeIn.get("firstInstance") != null) {
                    income.setFirstInstance(new BigDecimal(incomeIn.get("firstInstance").toString()));
                }
                if (incomeIn.get("assignedValue") != null) {
                    income.setAssignedValue(new BigDecimal(incomeIn.get("assignedValue").toString()));
                }
                if (incomeIn.get("occurredValue") != null) {
                    income.setOccurredValue(new BigDecimal(incomeIn.get("occurredValue").toString()));
                }
//                if (StringUtil.isNotEmpty(incomeIn.get("appliedValue").toString())) {
//                    income.setAppliedValue(new BigDecimal(incomeIn.get("appliedValue").toString()));
//                }
//                if (StringUtil.isNotEmpty(incomeIn.get("firstInstance").toString())) {
//                    income.setFirstInstance(new BigDecimal(incomeIn.get("firstInstance").toString()));
//                }
//                if (StringUtil.isNotEmpty(incomeIn.get("assignedValue").toString())) {
//                    income.setAssignedValue(new BigDecimal(incomeIn.get("assignedValue").toString()));
//                }
//                if (StringUtil.isNotEmpty(incomeIn.get("occurredValue").toString())) {
//                    income.setOccurredValue(new BigDecimal(incomeIn.get("occurredValue").toString()));
//                }
                pcm.update(income);
            }
        }

        return outMap;
    }
}
