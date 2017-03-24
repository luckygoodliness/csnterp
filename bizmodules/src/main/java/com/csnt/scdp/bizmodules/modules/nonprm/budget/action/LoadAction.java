package com.csnt.scdp.bizmodules.modules.nonprm.budget.action;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetC;
import com.csnt.scdp.bizmodules.entity.nonprm.SubjectSubject;
import com.csnt.scdp.bizmodules.modules.common.action.ErpLoadAction;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.dto.NonProjectBudgetCDDto;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.dto.NonProjectBudgetCDto;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.dto.NonProjectBudgetHDto;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.services.intf.BudgetManager;
import com.csnt.scdp.bizmodules.modules.nonprm.subjectsubject.services.intf.SubjectsubjectManager;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MapUtil;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
@Controller("budget-load")
@Transactional
public class LoadAction extends ErpLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

    @Resource(name = "budget-manager")
    private BudgetManager budgetManager;

    @Resource(name = "subjectsubject-manager")
    private SubjectsubjectManager subjectsubjectManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map out = super.perform(inMap);
        Map dtoMap = (Map) out.get("nonProjectBudgetHDto");
        if (dtoMap != null) {
            String orgCode = (String) dtoMap.get("officeId");
            if (orgCode != null) {
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
        Map<String, SubjectSubject> subjectSubjectMap = subjectsubjectManager.getObjsByOfficeId(hdto.getOfficeId())
                .stream().collect(Collectors.toMap(SubjectSubject::getUuid, t -> (t)));

        //将新加的预算科目加到预算明细中
        if (!hdto.getState().equals(BillStateAttribute.FAD_BILL_STATE_APPROVED)) {
            List<String> lstCdtoSubjectCode = lstCdto.stream().map(NonProjectBudgetCDto::getFinancialSubjectCode).collect(Collectors.toList());
            subjectSubjectMap.values().forEach(t -> {
                if (ListUtil.isEmpty(lstCdtoSubjectCode) || !lstCdtoSubjectCode.contains(t.getUuid())) {
                    NonProjectBudgetC cdto = new NonProjectBudgetC();
                    cdto.setFinancialSubjectCode(t.getUuid());
                    cdto.setSeqNo(t.getSeqNo());
                    cdto.setHid(hdto.getUuid());
                    budgetManager.saveNonProjectBudgetC(cdto);
                }
            });
        }

        Map<String, Map> budgetExecuteInfo = budgetManager.getNonProjectBudgetExecuteInfoByOfficeIdAndYear(hdto.getOfficeId(), hdto.getYear());

        Integer lastYear = Integer.valueOf(hdto.getYear()) - 1;
        Map<String, Map> budgetExecuteInfoLastTmp = budgetManager.getNonProjectBudgetExecuteInfoByOfficeIdAndYear(hdto.getOfficeId(), lastYear.toString());
        Map<String, Map> budgetExecuteInfoLastYear = new HashMap<String, Map>();
        if (MapUtil.isNotEmpty(budgetExecuteInfoLastTmp)) {
            budgetExecuteInfoLastTmp.values().forEach(t -> {
                if (t.get("financialSubjectCode") != null && !budgetExecuteInfoLastYear.containsKey(t.get("financialSubjectCode"))) {
                    budgetExecuteInfoLastYear.put((String) t.get("financialSubjectCode"), t);
                }
            });
        }

        if (ListUtil.isNotEmpty(lstCdto)) {
            lstCdto.forEach(t -> {
                if (budgetExecuteInfo.containsKey(t.getUuid())) {
                    t.setThisYearOccured((BigDecimal) budgetExecuteInfo.get(t.getUuid()).get("happenedMoney"));
                    t.setThisYearLocked((BigDecimal) budgetExecuteInfo.get(t.getUuid()).get("lockedBudget"));
                    t.setSubjectCode((String) budgetExecuteInfo.get(t.getUuid()).get("subjectCode"));
                    t.setSubjectName((String) budgetExecuteInfo.get(t.getUuid()).get("financialSubject"));
                }
                if (MapUtil.isNotEmpty(budgetExecuteInfoLastYear) && budgetExecuteInfoLastYear.containsKey(t.getFinancialSubjectCode())) {
                    t.setLastYearAssigned((BigDecimal) budgetExecuteInfoLastYear.get(t.getFinancialSubjectCode()).get("thisYearAssigned"));
                    t.setLastYearChanged((BigDecimal) budgetExecuteInfoLastYear.get(t.getFinancialSubjectCode()).get("thisYearChanged"));
                    t.setLastYearOccured((BigDecimal) budgetExecuteInfoLastYear.get(t.getFinancialSubjectCode()).get("happenedMoney"));
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
