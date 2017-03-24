package com.csnt.scdp.bizmodules.modules.nonprm.budget.action;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetC;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetCD;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetH;
import com.csnt.scdp.bizmodules.entity.nonprm.SubjectSubject;
import com.csnt.scdp.bizmodules.modules.common.action.ErpDeleteAction;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.services.intf.BudgetManager;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MathUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Description:  DeleteAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-24 13:22:12
 */

@Scope("singleton")
@Controller("budget-delete")
@Transactional
public class DeleteAction extends ErpDeleteAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(DeleteAction.class);

    @Resource(name = "budget-manager")
    private BudgetManager budgetManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {

        beforeAction(inMap);
        //Do before
        Map out = super.perform(inMap);
        //Do After
        return out;
    }

    private void beforeAction(Map inMap) {
        List lstUuids = (List) super.getDeleteUuids(inMap);
        if (ListUtil.isNotEmpty(lstUuids)) {
            for (int i = 0; i < lstUuids.size(); i++) {
                String budgetHUuid = (String) lstUuids.get(i);
                NonProjectBudgetH objBudgetH = PersistenceFactory.getInstance().findByPK(NonProjectBudgetH.class, budgetHUuid);
                String state = objBudgetH.getState();
                if (!BillStateAttribute.CMD_BILL_STATE_NEW.equals(state) && !BillStateAttribute.CMD_BILL_STATE_REJECTED.equals(state)) {
                    throw new BizException("只能删除新增或退回状态的单据！", new HashMap());
                } else {
                    //根据hid查找对象
                    List<NonProjectBudgetC> lstBudgetC = budgetManager.getNonProjectBudgetCDetailsByHid(budgetHUuid);
                    if (ListUtil.isNotEmpty(lstBudgetC)) {
                        Optional<NonProjectBudgetC> preappropriatedBudgetC = lstBudgetC.stream().filter(t -> !MathUtil.isNullOrZero(t.getThisYearPreappropriation())).findFirst();
                        if (preappropriatedBudgetC.isPresent()) {
                            throw new BizException("存在预算明细已做预拨款，无法删除！请先联系财务处理！");
                        } else {
                            for (int j = 0; j < lstBudgetC.size(); j++) {
                                NonProjectBudgetC budgetC = lstBudgetC.get(j);
                                String fsCode = budgetC.getFinancialSubjectCode();
                                if (StringUtil.isNotEmpty(fsCode)) {
                                    SubjectSubject obj = PersistenceFactory.getInstance()
                                            .findByPK(SubjectSubject.class, fsCode);
                                    String subjectName = obj.getFinancialSubject();
                                    if ("管理费用".equals(subjectName) || "固定资产添置".equals(subjectName)) {
                                        String cid = budgetC.getUuid();
                                        List<NonProjectBudgetCD> lstBudgetCD = budgetManager.getObjsByCid(cid);
                                        if (ListUtil.isNotEmpty(lstBudgetCD)) {
                                            for (int x = 0; x < lstBudgetCD.size(); x++) {
                                                NonProjectBudgetCD budgetCD = lstBudgetCD.get(x);
                                                budgetManager.delete(budgetCD);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
