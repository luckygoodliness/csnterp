package com.csnt.scdp.bizmodules.modules.mobileservice.nonprm.budget;

import com.csnt.scdp.bizmodules.attributes.ErpMobileModulePathAttribute;
import com.csnt.scdp.bizmodules.attributes.ErpMobileTerminalAttribute;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetCD;
import com.csnt.scdp.bizmodules.entity.nonprm.SubjectSubject;
import com.csnt.scdp.bizmodules.entityattributes.nonprm.*;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpI18NHelper;
import com.csnt.scdp.bizmodules.modules.mobileservice.ERPMobileTerminalBaseVariableCollectionAction;
import com.csnt.scdp.bizmodules.modules.mobileservice.intf.MobileTerminalQuery;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.dto.NonProjectBudgetCDDto;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.dto.NonProjectBudgetCDto;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.dto.NonProjectBudgetHDto;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.services.impl.BudgetManagerImpl;
import com.csnt.scdp.bizmodules.modules.prm.prmbilling.dto.PrmBillingDto;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MathUtil;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by xuwm on 2016/9/13.
 */
@Scope("singleton")
@Controller("mobile-terminal-query-non_prm_budget_plan_apply")
@Transactional
public class NonPrmBudgetQuery extends ERPMobileTerminalBaseVariableCollectionAction {
    @Resource(name="mobile-terminal-query")
    private MobileTerminalQuery mobileTerminalQuery;

    public Map perform(Map inMap) throws BizException,SysException {
        Map out = new LinkedHashMap<>();
        Map itemMap = new LinkedHashMap<>();
        Map extraGridMap = new LinkedHashMap<>();

        Map basePojoMap = super.perform(inMap);
        String workFlowDefinitionKey = (String) inMap.get(WorkFlowAttribute.WORKFLOW_DEFINITION_KEY);
        BasePojo basePojo = (BasePojo) basePojoMap.get(ErpMobileTerminalAttribute.BASE_POJO);
        NonProjectBudgetHDto nonProjectBudgetHDto = (NonProjectBudgetHDto) basePojo;

        BudgetManagerImpl budgetManager = new BudgetManagerImpl();
        List lstbudgetCdto = nonProjectBudgetHDto.getNonProjectBudgetCDto();
        nonProjectBudgetHDto.setNonProjectBudgetCDDto(new ArrayList<NonProjectBudgetCDDto>());
        nonProjectBudgetHDto.setNonProjectBudgetCD2Dto(new ArrayList<NonProjectBudgetCDDto>());
        if (ListUtil.isNotEmpty(lstbudgetCdto)) {
            for (int i = 0; i < lstbudgetCdto.size(); i++) {
                NonProjectBudgetCDto budgetCDto = (NonProjectBudgetCDto) lstbudgetCdto.get(i);
                //获取费用代码 存入数据库的是SubjectSubject表的uuid
                String fsCode = budgetCDto.getFinancialSubjectCode();
                //fsCode 作为 SubjectSubject模块的 uuid查找费用科目代号和费用名称
                SubjectSubject sub = PersistenceFactory.getInstance()
                        .findByPK(SubjectSubject.class, fsCode);
                if (sub != null) {
                    String financialName = sub.getFinancialSubject();
                    String financialSubjectCode = sub.getFinancialSubjectCode();
                    budgetCDto.setSubjectCode(financialSubjectCode);
                    budgetCDto.setSubjectName(financialName);
                    if ("管理费用".equals(financialName)) {
                        String cid = budgetCDto.getUuid();
                        List<NonProjectBudgetCDDto> lstBudgetCD = (List) budgetManager.getObjsByCid(cid);
                        nonProjectBudgetHDto.setNonProjectBudgetCDDto(lstBudgetCD);
                    }
                    if ("固定资产添置".equals(financialName)) {
                        String cid = budgetCDto.getUuid();
                        List<NonProjectBudgetCDDto> lstBudgetCD2 = (List) budgetManager.getObjsByCid(cid);
                        nonProjectBudgetHDto.setNonProjectBudgetCD2Dto(lstBudgetCD2);
                    }
                }
            }
        }

        Map hostMap=new LinkedHashMap<>();
        hostMap.put(ErpI18NHelper.englishToChinese(NonProjectBudgetHAttribute.YEAR, ErpMobileModulePathAttribute.NON_PRM_BUDGET_PLAN_APPLY_MODULEPATH),nonProjectBudgetHDto.getYear());
        hostMap.put(ErpI18NHelper.englishToChinese(NonProjectBudgetHAttribute.OFFICE_ID,ErpMobileModulePathAttribute.NON_PRM_BUDGET_PLAN_APPLY_MODULEPATH), OrgHelper.getOrgNameByCode(nonProjectBudgetHDto.getOfficeId()));
        itemMap.putAll(hostMap);

        //子表
        extraGridMap.put(ErpMobileTerminalAttribute.EXTRA_GRID, this.collectBudgetVariable(nonProjectBudgetHDto));

        itemMap.putAll(extraGridMap);

        out.put(ErpMobileTerminalAttribute.ITEM,itemMap);

        out.put(ErpMobileTerminalAttribute.PROCESS_DEPT_CODE,nonProjectBudgetHDto.getOfficeId());

        return out;
    }

    private Map collectBudgetVariable(NonProjectBudgetHDto nonProjectBudgetHDto){
        Map resultMap=new LinkedHashMap<>();

        List<Map> nonProjectBudgetCInfo=new ArrayList<>();
        List<NonProjectBudgetCDto> nonProjectBudgetCDtoList=nonProjectBudgetHDto.getNonProjectBudgetCDto();
        if(ListUtil.isNotEmpty(nonProjectBudgetCDtoList)){
            for(NonProjectBudgetCDto nonProjectBudgetCDto:nonProjectBudgetCDtoList){
                Map map=new LinkedHashMap<>();
                map.put(ErpI18NHelper.englishToChinese(NonProjectBudgetCAttribute.FINANCIAL_SUBJECT_CODE,ErpMobileModulePathAttribute.NON_PRM_BUDGET_PLAN_APPLY_MODULEPATH),nonProjectBudgetCDto.getSubjectCode());
                map.put(ErpI18NHelper.englishToChinese(NonProjectBudgetCAttribute.SUBJECT_NAME,ErpMobileModulePathAttribute.NON_PRM_BUDGET_PLAN_APPLY_MODULEPATH),nonProjectBudgetCDto.getSubjectName());
                map.put(ErpI18NHelper.englishToChinese(NonProjectBudgetCAttribute.THIS_YEAR_APPLYED,ErpMobileModulePathAttribute.NON_PRM_BUDGET_PLAN_APPLY_MODULEPATH),MathUtil.formatDecimalToString(nonProjectBudgetCDto.getThisYearApplyed(), "##,##0.00"));
                nonProjectBudgetCInfo.add(map);
            }
        }

        resultMap.put("预算申请",nonProjectBudgetCInfo);

        List<Map> nonProjectBudgetCDInfo=new ArrayList<Map>();
        List<NonProjectBudgetCDDto> nonProjectBudgetCD2List=nonProjectBudgetHDto.getNonProjectBudgetCD2Dto();
        if(ListUtil.isNotEmpty(nonProjectBudgetCD2List)){
            for(NonProjectBudgetCD nonProjectBudgetCDDto:nonProjectBudgetCD2List){
                Map map=new LinkedHashMap<>();
                map.put(ErpI18NHelper.englishToChinese(NonProjectBudgetCDAttribute.ITEM,ErpMobileModulePathAttribute.NON_PRM_BUDGET_PLAN_APPLY_MODULEPATH),nonProjectBudgetCDDto.getItem());
                map.put(ErpI18NHelper.englishToChinese(NonProjectBudgetCDAttribute.AMOUNT,ErpMobileModulePathAttribute.NON_PRM_BUDGET_PLAN_APPLY_MODULEPATH), MathUtil.formatDecimalToString(nonProjectBudgetCDDto.getAmount(), "##,##0.00"));
                map.put(ErpI18NHelper.englishToChinese(NonProjectBudgetCDAttribute.PRICE,ErpMobileModulePathAttribute.NON_PRM_BUDGET_PLAN_APPLY_MODULEPATH), MathUtil.formatDecimalToString(nonProjectBudgetCDDto.getPrice(), "##,##0.00"));
                map.put(ErpI18NHelper.englishToChinese(NonProjectBudgetCDAttribute.TOTAL_VALUE,ErpMobileModulePathAttribute.NON_PRM_BUDGET_PLAN_APPLY_MODULEPATH), MathUtil.formatDecimalToString(nonProjectBudgetCDDto.getTotalValue(), "##,##0.00"));
                map.put(ErpI18NHelper.englishToChinese(NonProjectBudgetCDAttribute.DESCP,ErpMobileModulePathAttribute.NON_PRM_BUDGET_PLAN_APPLY_MODULEPATH),nonProjectBudgetCDDto.getDescp());
                nonProjectBudgetCDInfo.add(map);
            }
        }
        resultMap.put("固定资产配置",nonProjectBudgetCDInfo);

        return resultMap;
    }

}
