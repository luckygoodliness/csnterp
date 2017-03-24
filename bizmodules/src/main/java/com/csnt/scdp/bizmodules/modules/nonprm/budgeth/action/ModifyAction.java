package com.csnt.scdp.bizmodules.modules.nonprm.budgeth.action;

import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetAppro;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetC;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.dto.NonProjectBudgetCDto;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.dto.NonProjectBudgetHDto;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.services.intf.BudgetManager;
import com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.services.intf.BudgetajustManager;
import com.csnt.scdp.bizmodules.modules.nonprm.subjectsubject.services.intf.SubjectsubjectManager;
import com.csnt.scdp.framework.commonaction.crud.CommonModifyAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MapUtil;
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
 * Description:  ModifyAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-24 13:22:12
 */

@Scope("singleton")
@Controller("budgeth-modify")
@Transactional
public class ModifyAction extends CommonModifyAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ModifyAction.class);

    @Resource(name = "budget-manager")
    private BudgetManager budgetManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        //Do After
        return out;
    }

    @Override
    protected void beforeAction(BasePojo dtoObj) {
        NonProjectBudgetHDto budgetHDto = (NonProjectBudgetHDto) dtoObj;
        List<NonProjectBudgetCDto> lstBudgetCDto = budgetHDto.getNonProjectBudgetCDto();
        if (ListUtil.isNotEmpty(lstBudgetCDto)) {
            Map<String, Map> BudgetView = budgetManager.getNonProjectBudgetExecuteInfoByOfficeIdAndYear(budgetHDto.getOfficeId(), budgetHDto.getYear());
            if (MapUtil.isNotEmpty(BudgetView)) {
                lstBudgetCDto.forEach(t -> {
                    BigDecimal thisYearAssigned = t.getThisYearAssigned() == null ? BigDecimal.ZERO : t.getThisYearAssigned();
                    BigDecimal thisYearChanged = t.getThisYearChanged() == null ? BigDecimal.ZERO : t.getThisYearChanged();
                    BigDecimal thisYearLocked = BigDecimal.ZERO;
                    if (BudgetView.containsKey(t.getUuid())) {
                        thisYearLocked = (BigDecimal) BudgetView.get(t.getUuid()).get("lockedBudget");
                        thisYearLocked = thisYearLocked == null ? BigDecimal.ZERO : thisYearLocked;
                    }
                    BigDecimal realAmount = thisYearAssigned.add(thisYearChanged);
                    BigDecimal thisYearAppropriation = t.getThisYearAppropriation() == null ? BigDecimal.ZERO : t.getThisYearAppropriation();

                    if (thisYearAppropriation.compareTo(realAmount) > 0) {
                        throw new BizException("拨款金额不能大于当年下达总额！");
                    }
                    if (thisYearAppropriation.compareTo(thisYearLocked) < 0) {
                        throw new BizException("拨款金额不能小于当年占用金额！");
                    }

                    NonProjectBudgetC budgetC = PersistenceFactory.getInstance()
                            .findByPK(NonProjectBudgetC.class, t.getUuid());

                    NonProjectBudgetAppro approDto = new NonProjectBudgetAppro();
                    approDto.setAppropriationBefore(budgetC.getThisYearAppropriation());
                    approDto.setAppropriationAfter(thisYearAppropriation);
                    approDto.setBudgetCuuid(t.getUuid());
                    if (BudgetView.containsKey(t.getUuid())) {
                        approDto.setBudgetSubjectName((String)BudgetView.get(t.getUuid()).get("financialSubject"));
                        approDto.setBudgetSubjectCode((String)BudgetView.get(t.getUuid()).get("subjectCode"));
                    }
                    budgetManager.save(approDto);
                });
            }
        }
    }
}
