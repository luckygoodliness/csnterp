package com.csnt.scdp.bizmodules.modules.nonprm.budget.action;

import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetC;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetCD;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetH;
import com.csnt.scdp.bizmodules.entity.nonprm.SubjectSubject;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.dto.NonProjectBudgetCDDto;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.dto.NonProjectBudgetCDto;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.dto.NonProjectBudgetHDto;
import com.csnt.scdp.bizmodules.modules.nonprm.budgeth.services.intf.BudgethManager;
import com.csnt.scdp.bizmodules.modules.nonprm.subjectsubject.services.intf.SubjectsubjectManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.ObjectUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpOrg;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.services.intf.BudgetManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
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
 * @timestamp 2015-09-24 13:22:12
 */

@Scope("singleton")
@Controller("budget-add")
@Transactional
public class AddAction extends CommonAddAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);

    @Resource(name = "budget-manager")
    private BudgetManager budgetManager;

    protected void beforeAction(BasePojo dtoObj) {
    }
    @Override
    public Map perform(Map inMap) throws BizException, SysException {

        Map out = super.perform(inMap);
        //Do After
        return out;
    }

    @Override
    protected void afterAction(BasePojo dtoObj) {

        NonProjectBudgetHDto budgetHDto = (NonProjectBudgetHDto) dtoObj;
        String hid = budgetHDto.getUuid();
        List<NonProjectBudgetCDto> lstBudgetCDto = (List) budgetManager.getNonProjectBudgetCDetailsByHid(hid);
        if (ListUtil.isEmpty(lstBudgetCDto)){
            throw new BizException("数据为空，无法保存", new HashMap());
        }else {
            if (budgetHDto != null) {
                List<NonProjectBudgetCDto> lstCDto = budgetHDto.getNonProjectBudgetCDto();
                if (ListUtil.isNotEmpty(lstCDto)) {
                    for (int y = 0; y < lstCDto.size(); y++) {
                        String subjectName = lstCDto.get(y).getSubjectName();
                        if ("管理费用".equals(subjectName) || "固定资产添置".equals(subjectName)) {
                            String cid = lstCDto.get(y).getUuid();
                            List<NonProjectBudgetCDDto> lstCDDto = "管理费用".equals(subjectName) ?
                                    budgetHDto.getNonProjectBudgetCDDto() : budgetHDto.getNonProjectBudgetCD2Dto();
                            if (ListUtil.isNotEmpty(lstCDDto)) {
                                for (int i = 0; i < lstCDDto.size(); i++) {
                                    NonProjectBudgetCDDto budgetCDDto = lstCDDto.get(i);
                                    NonProjectBudgetCD budgetCD = new NonProjectBudgetCD();
                                    budgetCD.setItem(budgetCDDto.getItem());
                                    budgetCD.setAmount(budgetCDDto.getAmount());
                                    budgetCD.setPrice(budgetCDDto.getPrice());
                                    budgetCD.setTotalValue(budgetCDDto.getTotalValue());
                                    budgetCD.setCid(cid);
                                    budgetManager.save(budgetCD);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}






