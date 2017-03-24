package com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.action;

import com.csnt.scdp.bizmodules.attributes.MessageKeyAttribute;
import com.csnt.scdp.bizmodules.attributes.PrmBudgetCodes;
import com.csnt.scdp.bizmodules.entity.prm.*;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmBudgetDetailAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmBudgetDetailCAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainCAttribute;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmBudgetDetailCDto;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmBudgetRunCDto;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmContractDetailCDto;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmProjectMainCDto;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.services.intf.PrmprojectmaincManager;
import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.services.intf.PurchaseplanManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.helper.MessageHelper;
import com.csnt.scdp.framework.util.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-27 15:28:19
 */

@Scope("singleton")
@Controller("prmprojectmainc2-validate-before-submit")
@Transactional
public class ValidationBeforeSubmit2 implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ValidationBeforeSubmit2.class);

    @Resource(name = "prmprojectmainc-manager")
    private PrmprojectmaincManager prmprojectmaincManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = new HashMap();
        List<String> lstMessage = new ArrayList<>();

        String uuid = (String) inMap.get(CommonAttribute.UUID);
        PrmProjectMainCDto prmProjectMainCDto = (PrmProjectMainCDto) DtoHelper.findDtoByPK(PrmProjectMainCDto.class, uuid);
        //validate contract now money
        List<PrmContractDetailCDto> lstContractDetail = prmProjectMainCDto.getPrmContractDetailCDto();
        BigDecimal contractMoney = BigDecimal.ZERO;
        if (ListUtil.isNotEmpty(lstContractDetail)) {
            DoubleSummaryStatistics stats = lstContractDetail.stream().mapToDouble(x -> x.getContractNowMoney()
                    .doubleValue()).summaryStatistics();
            contractMoney = BigDecimal.valueOf(stats.getSum()).setScale(2,BigDecimal.ROUND_HALF_UP);
        }
        Optional<PrmBudgetDetailCDto> contractTotal = prmProjectMainCDto.getPrmBudgetDetailCDto()
                .stream().filter(t -> "CONTRACT_TOTAL".equals(t.getBudgetCode())).findAny();
        if (contractTotal.isPresent()) {
            PrmBudgetDetailCDto contractTotalDetail = contractTotal.get();
            if (MathUtil.compareTo(contractMoney, contractTotalDetail.getContractMoney()) != 0) {
                lstMessage.add("预算明细中的合同合计与项目合同的签约额不相同！");
            }
        } else {
            lstMessage.add("预算明细中无合同合计金额！");
        }

        //主材、辅材、外协、运行费总额不能大于锁定金额
        lstMessage.addAll(validateBudgetLockedAmountMoney(prmProjectMainCDto));
        //校验运行费不能大于占用预算
        lstMessage.addAll(validateBudgetRunLockedAmountMoney(prmProjectMainCDto));
        //check serial number
        lstMessage.addAll(prmprojectmaincManager.validateSerialNumber(prmProjectMainCDto));
        //校验主材、辅材、外协拆分明细是否存在大于原拆分项的情况
        lstMessage.addAll(prmprojectmaincManager.validateSplitItemTotalValue(prmProjectMainCDto));
        //校验主材、辅材、外协修改后的金额不能超出采购申请的金额
        lstMessage.addAll(validateLockedAmountMoney(prmProjectMainCDto));

        //Do After
        out.put(CommonAttribute.DATAROOT, lstMessage);
        return out;
    }

    private List<String> validateBudgetLockedAmountMoney(PrmProjectMainCDto prmProjectMainCDto) {
        List<String> lstMessage = new ArrayList<>();

        String prmProjectMainId = prmProjectMainCDto.getPrmProjectMainId();
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        List lstParam = new ArrayList();
        lstParam.add(prmProjectMainId);
        DAOMeta daoMeta = DAOHelper.getDAO("prmprojectmainc-dao", "prm-actual-budget", lstParam);
        daoMeta.setNeedFilter(false);
        List<Map<String, Object>> lstActualBudget = pcm.findByNativeSQL(daoMeta);
        Map<String, String> budgetCodes = new HashMap();
        budgetCodes.put(PrmProjectMainAttribute.OUTSOURCE, "外协");
        budgetCodes.put(PrmProjectMainAttribute.PRINCIPAL, "主材");
        budgetCodes.put(PrmProjectMainAttribute.ACCESSORY, "辅材");
        budgetCodes.put("RUN", "运行费");

        if (ListUtil.isNotEmpty(lstActualBudget)) {
            Map<String, Double> budgetLocked = lstActualBudget.stream().map(t -> {
                PrmBudgetDetailC c = new PrmBudgetDetailC();
                String budgetCode = (String) t.get("budgetFirstType");
                c.setBudgetCode(budgetCode);
                BigDecimal costControlMoney = (BigDecimal) t.get("lockedBudget");
                c.setCostControlMoney(costControlMoney);
                return c;
            }).collect(Collectors.groupingBy(PrmBudgetDetailC::getBudgetCode, Collectors.summingDouble(t -> t.getCostControlMoney().doubleValue())));
            if (MapUtil.isNotEmpty(budgetLocked)) {
                prmProjectMainCDto.getPrmBudgetDetailCDto().stream().forEach(t -> {
                    if (PrmProjectMainAttribute.OUTSOURCE.equals(t.getBudgetCode())
                            || PrmProjectMainAttribute.PRINCIPAL.equals(t.getBudgetCode())
                            || PrmProjectMainAttribute.ACCESSORY.equals(t.getBudgetCode())
                            || "RUN".equals(t.getBudgetCode())) {
                        if (budgetLocked.containsKey(t.getBudgetCode())) {
                            if (t.getCostControlMoney().doubleValue() < budgetLocked.get(t.getBudgetCode())) {
                                lstMessage.add("立项预算明细变更" + budgetCodes.get(t.getBudgetCode()) + " 金额不能小于占用金额" + budgetLocked.get(t.getBudgetCode()));
                            }
                        }
                    }
                });
            }
        }

        return lstMessage;
    }

    private List<String> validateLockedAmountMoney(PrmProjectMainCDto prmProjectMainCDto) {
        List<String> lstMessage = new ArrayList<>();
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        PurchaseplanManager purchaseplanManager = BeanFactory.getBean("purchaseplan-manager");
        String projectUuid = prmProjectMainCDto.getPrmProjectMainId();
        Map<String, Map<String, Map<String, Object>>> purchaseInfoMap = purchaseplanManager.getPurchasePlanDetailInfo
                (projectUuid);
        Map condition = new HashMap();
        condition.put(PrmBudgetDetailAttribute.PRM_PROJECT_MAIN_ID, projectUuid);
        //外协
        List<PrmBudgetOutsource> lstOutsource = pcm.findByAnyFields(PrmBudgetOutsource.class, condition, null);
        if (ListUtil.isNotEmpty(prmProjectMainCDto.getPrmBudgetOutsourceCDto())) {
            prmProjectMainCDto.getPrmBudgetOutsourceCDto().stream()
                    .filter(x -> StringUtil.isEmpty(x.getSplitFromUuid()) && StringUtil.isNotEmpty(x.getLastUuid()))
                    .filter(x -> lstOutsource.stream()
                            .filter(y -> y.getUuid().equals(x.getLastUuid())
                                    && (MathUtil.compareTo(y.getAmount(), x.getAmount()) != 0
                                    || MathUtil.compareTo(y.getTotalValue(), x.getTotalValue()) != 0))
                            .findAny().isPresent())
                    .filter(x -> purchaseInfoMap.get(PrmBudgetCodes.OUTSOURCE).get(x.getLastUuid()) != null)
                    .forEach(x -> {
                        Map<String, Object> purchaseDetail = purchaseInfoMap.get(PrmBudgetCodes.OUTSOURCE).get(x.getLastUuid());
                        if (MathUtil.compareTo(x.getTotalValue(), (BigDecimal) purchaseDetail.get("lockedMoney")) < 0
                                || MathUtil.compareTo(x.getAmount(), (BigDecimal) purchaseDetail.get("lockedAmount")) < 0) {
                            lstMessage.add("外协明细：序号" + x.getSerialNumber() + " 数量和金额不能小于已采购数量和金额！");
                        }
                    });
        }
        //主材
        List<PrmBudgetPrincipal> lstPrincipal = pcm.findByAnyFields(PrmBudgetPrincipal.class, condition, null);
        if (ListUtil.isNotEmpty(prmProjectMainCDto.getPrmBudgetPrincipalCDto())) {
            prmProjectMainCDto.getPrmBudgetPrincipalCDto().stream()
                    .filter(x -> StringUtil.isEmpty(x.getSplitFromUuid()) && StringUtil.isNotEmpty(x.getLastUuid()))
                    .filter(x -> lstPrincipal.stream()
                            .filter(y -> y.getUuid().equals(x.getLastUuid())
                                    && (MathUtil.compareTo(y.getAmount(), x.getAmount()) != 0
                                    || MathUtil.compareTo(y.getSchemingTotalValue(), x.getSchemingTotalValue()) != 0))
                            .findAny().isPresent())
                    .filter(x -> purchaseInfoMap.get(PrmBudgetCodes.PRINCIPAL).get(x.getLastUuid()) != null)
                    .forEach(x -> {
                        Map<String, Object> purchaseDetail = purchaseInfoMap.get(PrmBudgetCodes.PRINCIPAL).get(x.getLastUuid());
                        if (MathUtil.compareTo(x.getSchemingTotalValue(), (BigDecimal) purchaseDetail.get("lockedMoney")) < 0
                                || MathUtil.compareTo(x.getAmount(), (BigDecimal) purchaseDetail.get("lockedAmount")) < 0) {
                            lstMessage.add("主材明细：序号" + x.getSerialNumber() + " 计划数量和计划金额不能小于已采购数量和金额！");
                        }
                    });
        }
        //辅材
        List<PrmBudgetAccessory> lstAccessory = pcm.findByAnyFields(PrmBudgetAccessory.class, condition, null);
        if (ListUtil.isNotEmpty(prmProjectMainCDto.getPrmBudgetAccessoryCDto())) {
            prmProjectMainCDto.getPrmBudgetAccessoryCDto().stream()
                    .filter(x -> StringUtil.isEmpty(x.getSplitFromUuid()) && StringUtil.isNotEmpty(x.getLastUuid()))
                    .filter(x -> lstAccessory.stream()
                            .filter(y -> y.getUuid().equals(x.getLastUuid())
                                    && (MathUtil.compareTo(y.getAmount(), x.getAmount()) != 0
                                    || MathUtil.compareTo(y.getTotalValue(), x.getTotalValue()) != 0))
                            .findAny().isPresent())
                    .filter(x -> purchaseInfoMap.get(PrmBudgetCodes.ACCESSORY).get(x.getLastUuid()) != null)
                    .forEach(x -> {
                        Map<String, Object> purchaseDetail = purchaseInfoMap.get(PrmBudgetCodes.ACCESSORY).get(x.getLastUuid());
                        if (MathUtil.compareTo(x.getTotalValue(), (BigDecimal) purchaseDetail.get("lockedMoney")) < 0
                                || MathUtil.compareTo(x.getAmount(), (BigDecimal) purchaseDetail.get("lockedAmount")) < 0) {
                            lstMessage.add("辅材明细：序号" + x.getSerialNumber() + " 数量和金额不能小于已采购数量和金额！");
                        }
                    });
        }
        return lstMessage;
    }

    private List<String> validateBudgetRunLockedAmountMoney(PrmProjectMainCDto prmProjectMainCDto) {
        List<String> lstMessage = new ArrayList<>();

        String prmProjectMainId = prmProjectMainCDto.getPrmProjectMainId();
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        List lstParam = new ArrayList();
        lstParam.add(prmProjectMainId);
        DAOMeta daoMeta = DAOHelper.getDAO("prmprojectmainc-dao", "prm-actual-budget", lstParam);
        daoMeta.setNeedFilter(false);
        List<Map<String, Object>> lstActualBudget = pcm.findByNativeSQL(daoMeta);

        if (ListUtil.isNotEmpty(lstActualBudget)) {
            Map condition = new HashMap();
            condition.put(PrmBudgetDetailAttribute.PRM_PROJECT_MAIN_ID, prmProjectMainId);
            List<PrmBudgetRun> lstRun = pcm.findByAnyFields(PrmBudgetRun.class, condition, null);
            Map<String, PrmBudgetRun> mapRun = lstRun.stream()
                    .filter(t -> t.getTotalValue() != null)
                    .collect(Collectors.toMap(PrmBudgetRun::getUuid, p -> p));
            //只对本次变更的运行费明细进行校验
            List<PrmBudgetRunCDto> lstBudgetRun = prmProjectMainCDto.getPrmBudgetRunCDto().stream()
                    .filter(t -> t.getTotalValue() == null ||
                            (mapRun.containsKey(t.getLastUuid()) &&
                                    mapRun.get(t.getLastUuid()).getTotalValue().compareTo(t.getTotalValue()) != 0))
                    .collect(Collectors.toList());

            if (ListUtil.isNotEmpty(lstBudgetRun)) {
                for (Map<String, Object> map : lstActualBudget) {
                    String budgetCode = (String) map.get(PrmBudgetDetailCAttribute.BUDGET_CODE);
                    String budgetName = (String) map.get(PrmBudgetDetailCAttribute.BUDGET_NAME);
                    BigDecimal lockedBudget = (BigDecimal) map.get(PrmBudgetDetailCAttribute.LOCKED_BUDGET);
                    lockedBudget = (lockedBudget != null) ? lockedBudget : BigDecimal.ZERO;

                    for (PrmBudgetRunC budget : lstBudgetRun) {
                        if (budgetCode.equals(budget.getFinancialSubjectCode())) {
                            BigDecimal costMoney = (budget.getTotalValue() != null) ? budget.getTotalValue() : BigDecimal.ZERO;
                            if (costMoney.compareTo(lockedBudget) < 0) {
                                Map params = new HashMap<>();
                                params.put(PrmBudgetDetailCAttribute.BUDGET_NAME, budgetName);
                                params.put(PrmBudgetDetailCAttribute.LOCKED_BUDGET, lockedBudget);
                                lstMessage.add(MessageHelper.getMessage(MessageKeyAttribute.MSG_PRM_CHANGE_BUDGET_AMOUNT_LESS, params));
                            }
                        }
                    }
                }
            }
        }
        return lstMessage;
    }
}
