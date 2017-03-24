package com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.action;

import com.csnt.scdp.bizmodules.entity.nonprm.*;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.services.intf.BudgetManager;
import com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.dto.NonProjectBudgetAjustCDDto;
import com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.dto.NonProjectBudgetAjustCDto;
import com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.services.intf.BudgetajustManager;
import com.csnt.scdp.bizmodules.modules.nonprm.subjectsubject.services.intf.SubjectsubjectManager;
import com.csnt.scdp.framework.commonaction.crud.CommonLoadAction;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MapUtil;
import org.apache.commons.collections.MapUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
@Controller("budgetajust-fill")
@Transactional
public class FillAction implements IAction {

    private static ILogTracer tracer = LogTracerFactory.getInstance(FillAction.class);

    @Resource(name = "budgetajust-manager")
    private BudgetajustManager budgetajustManager;

    @Resource(name = "budget-manager")
    private BudgetManager budgetManager;

    @Resource(name = "subjectsubject-manager")
    private SubjectsubjectManager subjectsubjectManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {

        Map out = new HashMap<>();
        //获取部门id
        String orgCode = (String) inMap.get("orgCode");
        String year = (String) inMap.get("year");

        List<NonProjectBudgetAjustH> budgetAjustHs = budgetajustManager.getObjeByOrgCodeAndYear(orgCode, year,null);
        if (ListUtil.isNotEmpty(budgetAjustHs)) {
            if (budgetAjustHs.stream().anyMatch((t -> "0".equals(t.getState()) || "1".equals(t.getState())))) {
                throw new BizException("该部门当年存在未处理完的调整,无法再做调整", new HashMap<>());
            }
        }

        List<NonProjectBudgetAjustCDto> lstObjsBudgetAjustCDto = new ArrayList<NonProjectBudgetAjustCDto>();
        List<NonProjectBudgetAjustCDDto> lstObjsBudgetAjustCD = new ArrayList<NonProjectBudgetAjustCDDto>();
        List<NonProjectBudgetAjustCDDto> lstObjsBudgetAjustCD2 = new ArrayList<NonProjectBudgetAjustCDDto>();

        Map<String, NonProjectBudgetC> mapBudgetCs = budgetManager.getPassedNonProjectBudgetC(orgCode, year)
                .stream().collect(Collectors.toMap(NonProjectBudgetC::getFinancialSubjectCode, t -> (t)));

        Map<String, SubjectSubject> subjectSubjects = subjectsubjectManager.getObjsByOfficeId(orgCode)
                .stream().collect(Collectors.toMap(SubjectSubject::getUuid, t -> (t)));

        if (MapUtil.isEmpty(mapBudgetCs)) {
            if (MapUtil.isNotEmpty(subjectSubjects)) {
                subjectSubjects.values().forEach(t -> {
                    NonProjectBudgetAjustCDto ajustCDto = new NonProjectBudgetAjustCDto();
                    ajustCDto.setFinancialSubjectCode(t.getUuid());
                    ajustCDto.setSubjectCode(t.getFinancialSubjectCode());
                    ajustCDto.setSubjectName(t.getFinancialSubject());
                    ajustCDto.setSeqNo(t.getSeqNo());
                    lstObjsBudgetAjustCDto.add(ajustCDto);
                });
            }
        } else {
            //在预算调整时,获取新增的非项目预算明细
            Map<String, SubjectSubject> newdSubjectSubjects = subjectSubjects.values().stream()
                    .filter(t -> !mapBudgetCs.containsKey(t.getUuid())).collect(Collectors.toMap(SubjectSubject::getUuid, t -> (t)));
            mapBudgetCs.values().forEach(t -> {
                NonProjectBudgetAjustCDto ajustCDto = new NonProjectBudgetAjustCDto();
                ajustCDto.setFinancialSubjectCode(t.getFinancialSubjectCode());
                ajustCDto.setOrrigalBudgetAssigned(getBudgetAmount(t));
                if (subjectSubjects.containsKey(t.getFinancialSubjectCode())) {
                    ajustCDto.setSubjectCode(subjectSubjects.get(t.getFinancialSubjectCode()).getFinancialSubjectCode());
                    ajustCDto.setSubjectName(subjectSubjects.get(t.getFinancialSubjectCode()).getFinancialSubject());
                    ajustCDto.setSeqNo(t.getSeqNo());
                }
                lstObjsBudgetAjustCDto.add(ajustCDto);
                if ("管理费用".equals(ajustCDto.getSubjectName())) {
                    String uuid = t.getUuid();
                    //根据cid查找对象
                    List<NonProjectBudgetCD> lstBudgetCD = budgetManager.getObjsByCid(uuid);
                    if (ListUtil.isNotEmpty(lstBudgetCD)) {
                        for (int j = 0; j < lstBudgetCD.size(); j++) {
                            NonProjectBudgetAjustCDDto cdo = new NonProjectBudgetAjustCDDto();
                            BeanUtil.bean2Bean(lstBudgetCD.get(j), cdo);
                            BigDecimal totalValue = lstBudgetCD.get(j).getTotalValue();
                            if (totalValue == null) {
                                BigDecimal amount = lstBudgetCD.get(j).getAmount() == null ? BigDecimal.ZERO : lstBudgetCD.get(j).getAmount();
                                BigDecimal price = lstBudgetCD.get(j).getPrice() == null ? BigDecimal.ZERO : lstBudgetCD.get(j).getPrice();
                                totalValue = amount.multiply(price);
                            }
                            cdo.setOrrigalBudgetAssigned(totalValue);
                            cdo.setAppliedAmount(lstBudgetCD.get(j).getAmount());
                            cdo.setChangedValie(BigDecimal.ZERO);
                            cdo.setBudgetCdUuid(lstBudgetCD.get(j).getUuid());
                            lstObjsBudgetAjustCD.add(cdo);
                        }
                    }
                }
                if ("固定资产添置".equals(ajustCDto.getSubjectName())) {
                    String uuid = t.getUuid();
                    //根据cid查找对象
                    List<NonProjectBudgetCD> lstBudgetCD = budgetManager.getObjsByCid(uuid);
                    if (ListUtil.isNotEmpty(lstBudgetCD)) {
                        for (int j = 0; j < lstBudgetCD.size(); j++) {
                            NonProjectBudgetAjustCDDto cdo = new NonProjectBudgetAjustCDDto();
                            BeanUtil.bean2Bean(lstBudgetCD.get(j), cdo);
                            cdo.setOrrigalBudgetAssigned(lstBudgetCD.get(j).getTotalValue());
                            BigDecimal totalValue = lstBudgetCD.get(j).getTotalValue();
                            if (totalValue == null) {
                                BigDecimal amount = lstBudgetCD.get(j).getAmount() == null ? BigDecimal.ZERO : lstBudgetCD.get(j).getAmount();
                                BigDecimal price = lstBudgetCD.get(j).getPrice() == null ? BigDecimal.ZERO : lstBudgetCD.get(j).getPrice();
                                totalValue = amount.multiply(price);
                            }
                            cdo.setOrrigalBudgetAssigned(totalValue);

                            cdo.setAppliedAmount(lstBudgetCD.get(j).getAmount());
                            cdo.setChangedValie(BigDecimal.ZERO);
                            cdo.setBudgetCdUuid(lstBudgetCD.get(j).getUuid());
                            lstObjsBudgetAjustCD2.add(cdo);
                        }
                    }
                }
            });

            newdSubjectSubjects.values().forEach(t -> {
                NonProjectBudgetAjustCDto ajustCDto = new NonProjectBudgetAjustCDto();
                ajustCDto.setFinancialSubjectCode(t.getUuid());
                ajustCDto.setSubjectCode(t.getFinancialSubjectCode());
                ajustCDto.setSubjectName(t.getFinancialSubject());
                ajustCDto.setSeqNo(t.getSeqNo());
                lstObjsBudgetAjustCDto.add(ajustCDto);
            });
        }
        out.put("lstObjsBudgetAjustC", lstObjsBudgetAjustCDto);
        out.put("lstObjsBudgetAjustCD", lstObjsBudgetAjustCD);
        out.put("lstObjsBudgetAjustCD2", lstObjsBudgetAjustCD2);
        return out;
    }

    private BigDecimal getBudgetAmount(NonProjectBudgetC nonProjectBudgetC) {
        if (nonProjectBudgetC == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal thisYearAssigned = nonProjectBudgetC.getThisYearAssigned() == null ? BigDecimal.ZERO : nonProjectBudgetC.getThisYearAssigned();
        BigDecimal thisYearChanged = nonProjectBudgetC.getThisYearChanged() == null ? BigDecimal.ZERO : nonProjectBudgetC.getThisYearChanged();
        return thisYearAssigned.add(thisYearChanged);

    }
}