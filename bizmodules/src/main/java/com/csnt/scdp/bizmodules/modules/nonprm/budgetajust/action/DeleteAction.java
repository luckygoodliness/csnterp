package com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.action;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetAjustC;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetAjustCD;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetAjustH;
import com.csnt.scdp.bizmodules.entity.nonprm.SubjectSubject;
import com.csnt.scdp.bizmodules.modules.common.action.ErpDeleteAction;
import com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.services.intf.BudgetajustManager;
import com.csnt.scdp.bizmodules.modules.nonprm.subjectsubject.services.intf.SubjectsubjectManager;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.ListUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  DeleteAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-25 16:43:12
 */

@Scope("singleton")
@Controller("budgetajust-delete")
@Transactional
public class DeleteAction extends ErpDeleteAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(DeleteAction.class);

	@Resource(name = "budgetajust-manager")
	private BudgetajustManager budgetajustManager;

    @Resource(name = "subjectsubject-manager")
    private SubjectsubjectManager subjectsubjectManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {

        List lstUuids = (List) super.getDeleteUuids(inMap);
        if (ListUtil.isNotEmpty(lstUuids)){
            for (int i=0; i<lstUuids.size(); i++){
                String budgetAjustHUuid = (String)lstUuids.get(i);
                NonProjectBudgetAjustH objBudgetAjustH = PersistenceFactory.getInstance().findByPK(NonProjectBudgetAjustH.class, budgetAjustHUuid);
                String state = objBudgetAjustH.getState();
                if (!(BillStateAttribute.CMD_BILL_STATE_NEW.equals(state))&&!(BillStateAttribute.CMD_BILL_STATE_REJECTED.equals(state))){
                    throw new BizException("只能删除新增或退回状态的单据!", new HashMap());
                }
                //根据hid查找对象
                List<NonProjectBudgetAjustC> lstBudgetAjustC= budgetajustManager.getObjByHid(budgetAjustHUuid);
                if (ListUtil.isNotEmpty(lstBudgetAjustC)){
                    for (int j=0; j<lstBudgetAjustC.size(); j++){
                        NonProjectBudgetAjustC budgetAjustC = lstBudgetAjustC.get(j);
                        String fsCode = budgetAjustC.getFinancialSubjectCode();
                        SubjectSubject obj = PersistenceFactory.getInstance()
                                .findByPK(SubjectSubject.class, fsCode);

                        String subjectName = obj.getFinancialSubject();
                        if ("管理费用".equals(subjectName) || "固定资产添置".equals(subjectName)){
                            String cid = budgetAjustC.getUuid();
                            List<NonProjectBudgetAjustCD> lstBudgetAjustCD = budgetajustManager.getObjsByCid(cid);
                            if (ListUtil.isNotEmpty(lstBudgetAjustCD)){
                                for (int x=0; x<lstBudgetAjustCD.size(); x++){
                                    NonProjectBudgetAjustCD budgetAjustCD = lstBudgetAjustCD.get(x);
                                    budgetajustManager.delete(budgetAjustCD);
                                }
                            }
                        }
                    }
                }
            }
        }

		//Do before
		Map out = super.perform(inMap);
		//Do After
		return out;
	}
}
