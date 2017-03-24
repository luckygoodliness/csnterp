package com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.action;

import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetAjustCD;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetAjustH;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetCD;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.dto.NonProjectBudgetCDDto;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.dto.NonProjectBudgetCDto;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.dto.NonProjectBudgetHDto;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.services.intf.BudgetManager;
import com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.dto.NonProjectBudgetAjustCDDto;
import com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.dto.NonProjectBudgetAjustCDto;
import com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.dto.NonProjectBudgetAjustHDto;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MapUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.services.intf.BudgetajustManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

/**
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-25 16:43:12
 */

@Scope("singleton")
@Controller("budgetajust-add")
@Transactional
public class AddAction extends CommonAddAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);

    @Resource(name = "budgetajust-manager")
    private BudgetajustManager budgetajustManager;

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
        NonProjectBudgetAjustHDto budgetAjustHDto = (NonProjectBudgetAjustHDto) dtoObj;
        List<NonProjectBudgetAjustH> budgetAjustHs = budgetajustManager.getObjeByOrgCodeAndYear(budgetAjustHDto.getOfficeId(), budgetAjustHDto.getYear(), null);
        if (ListUtil.isNotEmpty(budgetAjustHs)) {
            if (budgetAjustHs.stream().anyMatch((t -> "0".equals(t.getState()) || "1".equals(t.getState())))) {
                throw new BizException("该部门当年存在未处理完的调整,无法再做调整", new HashMap<>());
            }
        }

        List<NonProjectBudgetAjustCDto> lstBudgetAjustCDto = budgetAjustHDto.getNonProjectBudgetAjustCDto();
        if (ListUtil.isEmpty(lstBudgetAjustCDto)) {
            throw new BizException("数据为空，无法保存", new HashMap());
        } else {
            Map<String, Map> BudgetView = budgetajustManager.getNonBudgetExecute(budgetAjustHDto.getYear(), budgetAjustHDto.getOfficeId());
            lstBudgetAjustCDto.forEach(c -> {
                String subCode = c.getSubjectCode();
                BigDecimal orrigalBudgetAssigned = c.getOrrigalBudgetAssigned() == null ? BigDecimal.ZERO : c.getOrrigalBudgetAssigned();
                BigDecimal changedBudget = c.getBudgetChanged() == null ? BigDecimal.ZERO : c.getBudgetChanged();
                BigDecimal realBudget = orrigalBudgetAssigned.add(changedBudget);
                if (BigDecimal.ZERO.compareTo(realBudget) > 0) {
                    throw new BizException("变更后预算不能小于0！", new HashMap<>());
                }
                if (MapUtil.isNotEmpty(BudgetView) && BudgetView.containsKey(subCode)) {
                    Map budget = BudgetView.get(subCode);
                    BigDecimal lockedBudget = (BigDecimal) budget.get("lockedBudget");
                    lockedBudget = lockedBudget == null ? BigDecimal.ZERO : lockedBudget;
                    if (lockedBudget.compareTo(realBudget) > 0) {
                        throw new BizException("变更后预算不能小于预算已发生金额！", new HashMap());
                    }
                }
            });
        }
    }

    @Override
    protected void afterAction(BasePojo dtoObj) {
        NonProjectBudgetAjustHDto budgetAjustHDto = (NonProjectBudgetAjustHDto) dtoObj;
        List<NonProjectBudgetAjustCDto> lstBudgetAjustCDto = budgetAjustHDto.getNonProjectBudgetAjustCDto();
        if (ListUtil.isEmpty(lstBudgetAjustCDto)) {
            throw new BizException("数据为空，无法保存", new HashMap());
        } else {
            saveContentDetail(budgetAjustHDto.getNonProjectBudgetAjustCDDto(), "管理费用", lstBudgetAjustCDto);
            saveContentDetail(budgetAjustHDto.getNonProjectBudgetAjustCD2Dto(), "固定资产添置", lstBudgetAjustCDto);
        }
    }

    private void saveContentDetail(List<NonProjectBudgetAjustCDDto> detail, String subjectName, List<NonProjectBudgetAjustCDto> lstBudgetAjustCDto) {
        if (ListUtil.isNotEmpty(detail) && StringUtil.isNotEmpty(subjectName) && ListUtil.isNotEmpty(lstBudgetAjustCDto)) {
            Optional<NonProjectBudgetAjustCDto> budgetAjustC = lstBudgetAjustCDto.stream().filter(t -> subjectName.equals(t.getSubjectName())).findFirst();
            if (budgetAjustC.isPresent()) {
                List<BasePojo> dtoAddObjs = new ArrayList<BasePojo>();
                detail.forEach(t -> {
                    t.setCid(budgetAjustC.get().getUuid());
                    dtoAddObjs.add(t);
                });
                DtoHelper.batchAdd(dtoAddObjs, NonProjectBudgetAjustCD.class);
            }
        }
    }
}
