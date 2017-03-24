package com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.action;

import com.csnt.scdp.bizmodules.entity.nonprm.*;
import com.csnt.scdp.bizmodules.entityattributes.nonprm.NonProjectSubjectSubjectAttribute;
import com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.dto.NonProjectBudgetAjustCDDto;
import com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.dto.NonProjectBudgetAjustCDto;
import com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.dto.NonProjectBudgetAjustHDto;
import com.csnt.scdp.bizmodules.modules.nonprm.subjectsubject.services.intf.SubjectsubjectManager;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.BeanUtil;
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
import java.util.stream.Collectors;

/**
 * Description:  ModifyAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-25 16:43:12
 */

@Scope("singleton")
@Controller("budgetajust-modify")
@Transactional
public class ModifyAction extends CommonModifyAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ModifyAction.class);

    @Resource(name = "budgetajust-manager")
    private BudgetajustManager budgetajustManager;

    @Resource(name = "subjectsubject-manager")
    private SubjectsubjectManager subjectsubjectManager;

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
        if (budgetAjustHDto != null) {
            String adjustYear = budgetAjustHDto.getYear();
            Calendar date = Calendar.getInstance();
            String currentYear = Integer.toString(date.get(Calendar.YEAR));
            if (!(currentYear.equals(adjustYear))) {
                throw new BizException("只能调整当年预算！");
            }
            Map<String, Map> BudgetView = budgetajustManager.getNonBudgetExecute(budgetAjustHDto.getYear(), budgetAjustHDto.getOfficeId());
            budgetAjustHDto.getNonProjectBudgetAjustCDto().forEach(c -> {
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
            List<SubjectSubject> subject = subjectsubjectManager.getObjsByOfficeId(budgetAjustHDto.getOfficeId());
            if (ListUtil.isNotEmpty(subject)) {
                Map<String, SubjectSubject> subjectIdMap = subject.stream().collect(Collectors.toMap(SubjectSubject::getUuid, (p) -> p));
                lstBudgetAjustCDto.stream().forEach(t -> {
                    if (subjectIdMap.containsKey(t.getFinancialSubjectCode())) {
                     t.setSubjectName(subjectIdMap.get(t.getFinancialSubjectCode()).getFinancialSubject());
                    }
                });
                saveContentDetail(budgetAjustHDto.getNonProjectBudgetAjustCDDto(), "管理费用", lstBudgetAjustCDto);
                saveContentDetail(budgetAjustHDto.getNonProjectBudgetAjustCD2Dto(), "固定资产添置", lstBudgetAjustCDto);
            }
        }
    }

    private void saveContentDetail(List<NonProjectBudgetAjustCDDto> detail, String subjectName, List<NonProjectBudgetAjustCDto> lstBudgetAjustCDto) {
        if (ListUtil.isNotEmpty(detail) && StringUtil.isNotEmpty(subjectName) && ListUtil.isNotEmpty(lstBudgetAjustCDto)) {
            List<BasePojo> dtoAddObjs = new ArrayList<BasePojo>();
            List<BasePojo> dtoUpdateObjs = new ArrayList<BasePojo>();
            List<BasePojo> dtoDelObjs = new ArrayList<BasePojo>();
            Optional<NonProjectBudgetAjustCDto> budgetAjustC = lstBudgetAjustCDto.stream().filter(t -> subjectName.equals(t.getSubjectName())).findFirst();
            if (budgetAjustC.isPresent()) {
                detail.forEach(t -> {
                    switch (StringUtil.replaceNull(t.getEditflag())) {
                        case "+":
                        case "":
                            t.setCid(budgetAjustC.get().getUuid());
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
            } else {
                detail.stream().forEach(t -> {
                    dtoDelObjs.add(t);
                });
            }
            DtoHelper.batchAdd(dtoAddObjs, NonProjectBudgetAjustCD.class);
            DtoHelper.batchDel(dtoDelObjs, NonProjectBudgetAjustCDDto.class);
            DtoHelper.batchUpdate(dtoUpdateObjs, NonProjectBudgetAjustCD.class);
        }
    }
}
