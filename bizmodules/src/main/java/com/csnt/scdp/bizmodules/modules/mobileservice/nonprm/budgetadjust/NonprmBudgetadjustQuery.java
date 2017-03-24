package com.csnt.scdp.bizmodules.modules.mobileservice.nonprm.budgetadjust;

import com.csnt.scdp.bizmodules.attributes.ErpMobileModulePathAttribute;
import com.csnt.scdp.bizmodules.attributes.ErpMobileTerminalAttribute;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetAjustCD;
import com.csnt.scdp.bizmodules.entity.nonprm.SubjectSubject;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCashReqAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCashReqInvoiceAttribute;
import com.csnt.scdp.bizmodules.entityattributes.nonprm.NonProjectBudgetAjustCAttribute;
import com.csnt.scdp.bizmodules.entityattributes.nonprm.NonProjectBudgetAjustCDAttribute;
import com.csnt.scdp.bizmodules.entityattributes.nonprm.NonProjectBudgetAjustHAttribute;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpI18NHelper;
import com.csnt.scdp.bizmodules.modules.fad.cashreq.dto.FadCashReqDto;
import com.csnt.scdp.bizmodules.modules.fad.invoice.dto.FadCashReqInvoiceDto;
import com.csnt.scdp.bizmodules.modules.mobileservice.ERPMobileTerminalBaseVariableCollectionAction;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.services.impl.BudgetManagerImpl;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.services.intf.BudgetManager;
import com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.dto.NonProjectBudgetAjustCDDto;
import com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.dto.NonProjectBudgetAjustCDto;
import com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.dto.NonProjectBudgetAjustHDto;
import com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.services.impl.BudgetajustManagerImpl;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MathUtil;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by xiezf on 2016/9/13.
 * 非项目预算变更
 * NONPRM_BUDGET_ADJUST_MODULEPATH
 */
@Scope("singleton")
@Controller("mobile-terminal-query-non_prm_budget_adjustment")
@Transactional
public class NonprmBudgetadjustQuery extends ERPMobileTerminalBaseVariableCollectionAction {

    @Override
    public Map perform(Map inMap) throws BizException, SysException {

        Map out = new LinkedHashMap<>();
        Map itemMap = new LinkedHashMap<>();
        Map extraGridMap = new LinkedHashMap<>();

        Map basePojoMap = super.perform(inMap);
        BasePojo basePojo = (BasePojo) basePojoMap.get(ErpMobileTerminalAttribute.BASE_POJO);
        NonProjectBudgetAjustHDto nonProjectBudgetAjustHDto = (NonProjectBudgetAjustHDto) basePojo;

        BudgetajustManagerImpl budgetajustManager = new BudgetajustManagerImpl();
        List lstAjustCdto = nonProjectBudgetAjustHDto.getNonProjectBudgetAjustCDto();
        nonProjectBudgetAjustHDto.setNonProjectBudgetAjustCDDto(new ArrayList<NonProjectBudgetAjustCDDto>());
        nonProjectBudgetAjustHDto.setNonProjectBudgetAjustCD2Dto(new ArrayList<NonProjectBudgetAjustCDDto>());
        if (ListUtil.isNotEmpty(lstAjustCdto)) {
            for (int i = 0; i < lstAjustCdto.size(); i++) {
                NonProjectBudgetAjustCDto budgetAjustCDto = (NonProjectBudgetAjustCDto) lstAjustCdto.get(i);
                //获取费用代码 存入数据库的是SubjectSubject表的uuid
                String fsCode = budgetAjustCDto.getFinancialSubjectCode();
                //fsCode 作为 SubjectSubject模块的 uuid查找费用科目代号和费用名称
                SubjectSubject sub = PersistenceFactory.getInstance()
                        .findByPK(SubjectSubject.class, fsCode);
                if (sub != null) {
                    String financialName = sub.getFinancialSubject();
                    String financialSubjectCode = sub.getFinancialSubjectCode();
                    budgetAjustCDto.setSubjectCode(financialSubjectCode);
                    budgetAjustCDto.setSubjectName(financialName);
                    if ("管理费用".equals(financialName)) {
                        String cid = budgetAjustCDto.getUuid();
                        List<NonProjectBudgetAjustCDDto> lstBudgetAjustCD = (List) budgetajustManager.getObjsByCid(cid);
                        nonProjectBudgetAjustHDto.setNonProjectBudgetAjustCDDto(lstBudgetAjustCD);
                    }
                    if ("固定资产添置".equals(financialName)) {
                        String cid = budgetAjustCDto.getUuid();
                        List<NonProjectBudgetAjustCDDto> lstBudgetAjustCD2 = (List) budgetajustManager.getObjsByCid(cid);
                        nonProjectBudgetAjustHDto.setNonProjectBudgetAjustCD2Dto(lstBudgetAjustCD2);
                    }
                }
            }
        }
        //主表
        Map hostMap = new LinkedHashMap<>();
        hostMap.put(ErpI18NHelper.englishToChinese(NonProjectBudgetAjustHAttribute.YEAR, ErpMobileModulePathAttribute.NONPRM_BUDGET_ADJUST_MODULEPATH), nonProjectBudgetAjustHDto.getYear());
        hostMap.put(ErpI18NHelper.englishToChinese(NonProjectBudgetAjustHAttribute.OFFICE_ID, ErpMobileModulePathAttribute.NONPRM_BUDGET_ADJUST_MODULEPATH),OrgHelper.getOrgNameByCode(nonProjectBudgetAjustHDto.getOfficeId()));
        itemMap.putAll(hostMap);

        //填充子表 nonProjectBudgetAjustCDto  nonProjectBudgetAjustCD2Dto
        extraGridMap.put(ErpMobileTerminalAttribute.EXTRA_GRID, this.collectProjectBudgetAjustCDto(nonProjectBudgetAjustHDto));

        itemMap.putAll(extraGridMap);
        out.put(ErpMobileTerminalAttribute.ITEM, itemMap);

        out.put(ErpMobileTerminalAttribute.PROCESS_DEPT_CODE, nonProjectBudgetAjustHDto.getOfficeId());

        return out;
    }

    private Map collectProjectBudgetAjustCDto(NonProjectBudgetAjustHDto nonProjectBudgetAjustHDto){
        Map resultMap = new HashMap<>();
        List<Map> nonProjectBudgetAjustCDtoInfo = new ArrayList<>();
        List<NonProjectBudgetAjustCDto> nonProjectBudgetAjustCDtoList = nonProjectBudgetAjustHDto.getNonProjectBudgetAjustCDto();
        if (ListUtil.isNotEmpty(nonProjectBudgetAjustCDtoList)) {
            for (NonProjectBudgetAjustCDto nonProjectBudgetAjustCDto : nonProjectBudgetAjustCDtoList) {
                Map map = new LinkedHashMap<>();
                map.put(ErpI18NHelper.englishToChinese(NonProjectBudgetAjustCAttribute.FINANCIAL_SUBJECT_CODE, ErpMobileModulePathAttribute.NONPRM_BUDGET_ADJUST_MODULEPATH), nonProjectBudgetAjustCDto.getSubjectCode());
                map.put(ErpI18NHelper.englishToChinese(NonProjectBudgetAjustCAttribute.SUBJECT_NAME, ErpMobileModulePathAttribute.NONPRM_BUDGET_ADJUST_MODULEPATH), nonProjectBudgetAjustCDto.getSubjectName());
                map.put(ErpI18NHelper.englishToChinese(NonProjectBudgetAjustCAttribute.ORRIGAL_BUDGET_ASSIGNED, ErpMobileModulePathAttribute.NONPRM_BUDGET_ADJUST_MODULEPATH), MathUtil.formatDecimalToString(nonProjectBudgetAjustCDto.getOrrigalBudgetAssigned(), "##,##0.00"));
                map.put(ErpI18NHelper.englishToChinese(NonProjectBudgetAjustCAttribute.BUDGET_CHANGED, ErpMobileModulePathAttribute.NONPRM_BUDGET_ADJUST_MODULEPATH), MathUtil.formatDecimalToString(nonProjectBudgetAjustCDto.getBudgetChanged(), "##,##0.00"));
                map.put(ErpI18NHelper.englishToChinese(NonProjectBudgetAjustCAttribute.AFTER_ADJUST_MONEY, ErpMobileModulePathAttribute.NONPRM_BUDGET_ADJUST_MODULEPATH), MathUtil.formatDecimalToString(nonProjectBudgetAjustCDto.getAfterAdjustMoney(), "##,##0.00"));
                nonProjectBudgetAjustCDtoInfo.add(map);
            }

        }
        resultMap.put("预算调整", nonProjectBudgetAjustCDtoInfo);
        List<Map> nonProjectBudgetAjustCD2DtoInfo = new ArrayList<>();
        List<NonProjectBudgetAjustCDDto> nonProjectBudgetAjustCD2DtoList = nonProjectBudgetAjustHDto.getNonProjectBudgetAjustCD2Dto();
        if (ListUtil.isNotEmpty(nonProjectBudgetAjustCD2DtoList)) {
            for (NonProjectBudgetAjustCD nonProjectBudgetAjustCD2Dto : nonProjectBudgetAjustCD2DtoList) {
                Map map = new LinkedHashMap<>();
                map.put(ErpI18NHelper.englishToChinese(NonProjectBudgetAjustCDAttribute.ITEM, ErpMobileModulePathAttribute.NONPRM_BUDGET_ADJUST_MODULEPATH), nonProjectBudgetAjustCD2Dto.getItem());
                map.put(ErpI18NHelper.englishToChinese(NonProjectBudgetAjustCDAttribute.ORRIGAL_BUDGET_ASSIGNED, ErpMobileModulePathAttribute.NONPRM_BUDGET_ADJUST_MODULEPATH), MathUtil.formatDecimalToString(nonProjectBudgetAjustCD2Dto.getOrrigalBudgetAssigned(), "##,##0.00"));
                map.put(ErpI18NHelper.englishToChinese(NonProjectBudgetAjustCDAttribute.APPLIED_AMOUNT, ErpMobileModulePathAttribute.NONPRM_BUDGET_ADJUST_MODULEPATH), MathUtil.formatDecimalToString(nonProjectBudgetAjustCD2Dto.getAppliedAmount(), "##,##0.00"));
                map.put(ErpI18NHelper.englishToChinese(NonProjectBudgetAjustCDAttribute.PRICE, ErpMobileModulePathAttribute.NONPRM_BUDGET_ADJUST_MODULEPATH), MathUtil.formatDecimalToString(nonProjectBudgetAjustCD2Dto.getPrice(), "##,##0.00"));
                map.put(ErpI18NHelper.englishToChinese(NonProjectBudgetAjustCDAttribute.CHANGED_VALIE, ErpMobileModulePathAttribute.NONPRM_BUDGET_ADJUST_MODULEPATH), MathUtil.formatDecimalToString(nonProjectBudgetAjustCD2Dto.getChangedValie(), "##,##0.00"));
                nonProjectBudgetAjustCD2DtoInfo.add(map);
            }
        }
        resultMap.put("固定资产添置", nonProjectBudgetAjustCD2DtoInfo);
        return resultMap;
    }
}
