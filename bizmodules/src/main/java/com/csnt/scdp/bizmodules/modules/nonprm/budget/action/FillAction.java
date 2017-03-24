package com.csnt.scdp.bizmodules.modules.nonprm.budget.action;

import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetC;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetH;
import com.csnt.scdp.bizmodules.entity.nonprm.SubjectSubject;
import com.csnt.scdp.bizmodules.entityattributes.nonprm.NonProjectSubjectSubjectAttribute;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.dto.NonProjectBudgetCDto;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.dto.NonProjectBudgetHDto;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.services.intf.BudgetManager;
import com.csnt.scdp.bizmodules.modules.nonprm.subjectsubject.services.intf.SubjectsubjectManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.CommonLoadAction;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
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
import com.csnt.scdp.framework.util.MapUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpOrg;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Reese on 2015/10/14.
 */
@Scope("singleton")
@Controller("budget-fill")
@Transactional
public class FillAction  implements IAction {

    private static ILogTracer tracer = LogTracerFactory.getInstance(FillAction.class);
    @Resource(name = "budget-manager")
    private BudgetManager budgetManager;

    @Resource(name = "subjectsubject-manager")
    private SubjectsubjectManager subjectsubjectManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map result = new HashMap<>();
        //获取部门code
        String orgCode = (String) inMap.get("orgCode");
        String year = (String) inMap.get("year");
        int y = Integer.parseInt(year);
        int lasY = y - 1;
        String lastYear = Integer.toString(lasY);
        //根据部门code获取费用代码
        List<SubjectSubject> lstFSCodes = subjectsubjectManager.getObjsByOfficeId(orgCode);
        List<NonProjectBudgetCDto> lstBudgetC = new ArrayList<NonProjectBudgetCDto>();
        if (ListUtil.isNotEmpty(lstFSCodes)) {
            List<String> subjectCodeList = budgetManager.getPassedNonProjectBudgetC(orgCode, year).
                    stream().map(t -> t.getFinancialSubjectCode()).collect(Collectors.toList());
            if (ListUtil.isNotEmpty(subjectCodeList)) {
                List<SubjectSubject> lstFSCodeneedFilter = lstFSCodes.stream().
                        filter(t -> subjectCodeList.contains(t.getUuid())).collect(Collectors.toList());
                lstFSCodes.removeAll(lstFSCodeneedFilter);
            }
            Map<String, Map> budgetExecuteInfoLastTmp = budgetManager.getNonProjectBudgetExecuteInfoByOfficeIdAndYear(orgCode, lastYear);
            Map<String, Map> budgetExecuteInfoLastYear = new HashMap<String, Map>();
            if (MapUtil.isNotEmpty(budgetExecuteInfoLastTmp)) {
                budgetExecuteInfoLastTmp.values().forEach(t -> {
                    if (t.get("financialSubjectCode") != null && !budgetExecuteInfoLastYear.containsKey(t.get("financialSubjectCode"))) {
                        budgetExecuteInfoLastYear.put((String) t.get("financialSubjectCode"), t);
                    }
                });
            }

            lstFSCodes.forEach(t -> {
                NonProjectBudgetCDto cdto = new NonProjectBudgetCDto();
                cdto.setFinancialSubjectCode(t.getUuid());
                cdto.setSubjectCode(t.getFinancialSubjectCode());
                cdto.setSubjectName(t.getFinancialSubject());
                cdto.setSeqNo(t.getSeqNo());
                if (MapUtil.isNotEmpty(budgetExecuteInfoLastYear) && budgetExecuteInfoLastYear.containsKey(t.getUuid())) {
                    cdto.setLastYearAssigned((BigDecimal) budgetExecuteInfoLastYear.get(t.getUuid()).get("thisYearAssigned"));
                    cdto.setLastYearChanged((BigDecimal) budgetExecuteInfoLastYear.get(t.getUuid()).get("thisYearChanged"));
                    cdto.setLastYearOccured((BigDecimal) budgetExecuteInfoLastYear.get(t.getUuid()).get("happenedMoney"));
                }
                lstBudgetC.add(cdto);
            });
        }
        result.put("lstBudgetC", lstBudgetC);
        return result;
    }

}
