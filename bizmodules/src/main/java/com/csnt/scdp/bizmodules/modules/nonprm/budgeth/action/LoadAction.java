package com.csnt.scdp.bizmodules.modules.nonprm.budgeth.action;

import com.csnt.scdp.bizmodules.modules.common.action.ErpLoadAction;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.dto.NonProjectBudgetCDDto;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.dto.NonProjectBudgetCDto;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.dto.NonProjectBudgetHDto;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.services.intf.BudgetManager;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description:  LoadAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-24 13:22:12
 */

@Scope("singleton")
@Controller("budgeth-load")
@Transactional
public class LoadAction extends ErpLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

    @Resource(name = "budget-manager")
    private BudgetManager budgetManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map out = super.perform(inMap);
        Map dtoMap = (Map) out.get("nonProjectBudgetHDto");
        if (dtoMap != null) {
            String orgCode = (String) dtoMap.get("officeId");
            if(orgCode!=null){
                String orgName = OrgHelper.getOrgNameByCode(orgCode);
                dtoMap.put("officeIdDesc", orgName);
            }
        }
        return out;
    }

    @Override
    protected void afterAction(BasePojo dtoObj) {
        NonProjectBudgetHDto hdto = (NonProjectBudgetHDto) dtoObj;

        List<NonProjectBudgetCDto> lstCdto = hdto.getNonProjectBudgetCDto();
        hdto.setNonProjectBudgetCDDto(new ArrayList<NonProjectBudgetCDDto>());
        hdto.setNonProjectBudgetCD2Dto(new ArrayList<NonProjectBudgetCDDto>());
        Map<String, Map> budgetExecuteInfo = budgetManager.getNonProjectBudgetExecuteInfoByOfficeIdAndYear(hdto.getOfficeId(), hdto.getYear());
        if (ListUtil.isNotEmpty(lstCdto)) {
            lstCdto.forEach(t -> {
                if (budgetExecuteInfo.containsKey(t.getUuid())) {
                    t.setSubjectCode((String) budgetExecuteInfo.get(t.getUuid()).get("subjectCode"));
                    t.setSubjectName((String) budgetExecuteInfo.get(t.getUuid()).get("financialSubject"));
                    t.setThisYearOccured((BigDecimal) budgetExecuteInfo.get(t.getUuid()).get("invoiceTotalMoney"));
                    t.setThisYearLocked((BigDecimal) budgetExecuteInfo.get(t.getUuid()).get("lockedBudget"));
                }

                if ("管理费用".equals(t.getSubjectName())) {
                    String cid = t.getUuid();
                    List<NonProjectBudgetCDDto> lstBudgetCD = (List) budgetManager.getObjsByCid(cid);
                    hdto.setNonProjectBudgetCDDto(lstBudgetCD);
                }
                if ("固定资产添置".equals(t.getSubjectName())) {
                    String cid = t.getUuid();
                    List<NonProjectBudgetCDDto> lstBudgetCD2 = (List) budgetManager.getObjsByCid(cid);
                    hdto.setNonProjectBudgetCD2Dto(lstBudgetCD2);
                }
            });
        }
    }
}
