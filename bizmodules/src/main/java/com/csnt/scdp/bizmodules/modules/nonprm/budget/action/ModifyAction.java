package com.csnt.scdp.bizmodules.modules.nonprm.budget.action;

import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetC;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetCD;
import com.csnt.scdp.bizmodules.entity.nonprm.SubjectSubject;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.dto.NonProjectBudgetCDDto;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.dto.NonProjectBudgetHDto;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.services.intf.BudgetManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
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
@Controller("budget-modify")
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
    protected void afterAction(BasePojo dtoObj) {
        NonProjectBudgetHDto budgetHDto = (NonProjectBudgetHDto) dtoObj;
        if (budgetHDto != null) {
            List<NonProjectBudgetC> lstCDto = budgetManager.getNonProjectBudgetCDetailsByHid(((NonProjectBudgetHDto) dtoObj).getUuid());
            List<NonProjectBudgetCDDto> lstCDDto = budgetHDto.getNonProjectBudgetCDDto();
            List<NonProjectBudgetCDDto> lstCD2Dto = budgetHDto.getNonProjectBudgetCD2Dto();
            updateCDInfo(lstCDto, lstCDDto, "管理费用");
            updateCDInfo(lstCDto, lstCD2Dto, "固定资产添置");
        }
    }

    private void updateCDInfo(List<NonProjectBudgetC> lstC, List<NonProjectBudgetCDDto> lstCDDto, String subjectName) {
        String manageCid = null;
        for (NonProjectBudgetC manageBudgetC : lstC) {
            SubjectSubject sub = PersistenceFactory.getInstance()
                    .findByPK(SubjectSubject.class, manageBudgetC.getFinancialSubjectCode());
            if (sub.getFinancialSubject().equals(subjectName)) {
                manageCid = manageBudgetC.getUuid();
                break;
            }
        }
        if (StringUtil.isEmpty(manageCid)) {
            lstCDDto.stream().forEach(t -> {
                budgetManager.remove(t.getUuid());
            });
        } else {
            String oo = manageCid;
            List<BasePojo> dtoAddObjs = new ArrayList<BasePojo>();
            List<BasePojo> dtoUpdateObjs = new ArrayList<BasePojo>();
            List<BasePojo> dtoDelObjs = new ArrayList<BasePojo>();
            lstCDDto.stream().forEach(t -> {
                switch (StringUtil.replaceNull(t.getEditflag())) {
                    case "+":
                    case "":
                        t.setCid(oo);
                        dtoAddObjs.add(t);
                        break;
                    case "-":
                        dtoDelObjs.add(t);
                        break;
                    case "*":
                        dtoUpdateObjs.add(t);
                        break;
                    case "#":
                        break;
                }
            });
            DtoHelper.batchAdd(dtoAddObjs, NonProjectBudgetCD.class);
            DtoHelper.batchDel(dtoDelObjs, NonProjectBudgetCDDto.class);
            DtoHelper.batchUpdate(dtoUpdateObjs, NonProjectBudgetCD.class);
        }
    }
}
