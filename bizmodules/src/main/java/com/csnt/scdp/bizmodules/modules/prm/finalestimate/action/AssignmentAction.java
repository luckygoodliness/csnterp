package com.csnt.scdp.bizmodules.modules.prm.finalestimate.action;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.entity.prm.PrmBilling;
import com.csnt.scdp.bizmodules.entity.prm.PrmFinalEstimate;
import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmFinalEstimateAttribute;
import com.csnt.scdp.bizmodules.modules.prm.finalestimate.dto.PrmFinalEstimateDto;
import com.csnt.scdp.bizmodules.modules.prm.finalestimate.dto.PrmFinalProjectInfoDto;
import com.csnt.scdp.bizmodules.modules.prm.finalestimate.services.intf.FinalestimateManager;
import com.csnt.scdp.bizmodules.modules.prm.projectmain.services.intf.ProjectmainManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MathUtil;
import com.csnt.scdp.framework.util.ObjectUtil;
import com.csnt.scdp.framework.util.StringUtil;
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

/**
 * Created by Administrator on 2015/10/13.
 */
@Scope("singleton")
@Controller("finalestimate-assignment")
@Transactional
public class AssignmentAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(AssignmentAction.class);

    @Resource(name = "finalestimate-manager")
    private FinalestimateManager finalestimateManager;

    @Override
    //根据项目id在归档表中存在已审核数据，为1，否则为0
    public Map perform(Map inMap) throws BizException, SysException {
        Map out = new HashMap<>();
        String prmProjectMainId = (String) inMap.get(PrmFinalEstimateAttribute.PRM_PROJECT_MAIN_ID);
        String squareType = (String) inMap.get(PrmFinalEstimateAttribute.SQUARE_TYPE);
        List<Map> archiveList = finalestimateManager.getPrmArchivingForProjectMainId(prmProjectMainId);
        Integer isArchiving = ListUtil.isEmpty(archiveList) ? 0 : 1;
        List<String> lstError = new ArrayList<>();
        List<String> lstInfo = new ArrayList<>();
        String title = "阶段结算:";
        if ("3".equals(squareType)) {
            title = "项目中止：";
        } else if ("1".equals(squareType)) {
            title = "完工结算：";
        }

        if ("3".equals(squareType)) {
            //项目中止
            Map condition = new HashMap();
            condition.put(PrmFinalEstimateAttribute.PRM_PROJECT_MAIN_ID, prmProjectMainId);
            List<PrmFinalEstimate> lstFinalEstimate = PersistenceFactory.getInstance().findByAnyFields(
                    PrmFinalEstimate.class, condition, null);
            if (ListUtil.isNotEmpty(lstFinalEstimate)) {
                lstError.add(title + "项目已做过结算，不能进行中止！");
                out.put("errors", lstError);
                return out;
            }
        }

        if ("1".equals(squareType) || "3".equals(squareType)) {
            List paramLst = new ArrayList();
            paramLst.add(prmProjectMainId);

            //校验是否所有采购申请单已经处理
            DAOMeta daoMeta = DAOHelper.getDAO("finalestimate-dao", "get_purchaseReq_state", paramLst);
            daoMeta.setNeedFilter(false);
            List lstRet = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            if (ListUtil.isNotEmpty(lstRet)) {
                for (Object o : lstRet) {
                    Map m = (Map) o;
                    lstError.add("采购申请单：" + m.get("purchaseReqNo") + m.get("des"));
                }
            }
            //检查项目变更是否存在
            daoMeta = DAOHelper.getDAO("finalestimate-dao", "prm_unapproved_project_c", paramLst);
            daoMeta.setNeedFilter(false);
            lstRet = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            if (ListUtil.isNotEmpty(lstRet)) {
                lstError.add(title + "存在没有审核通过的项目变更！");
            }

            //校验是否所有合同已经被关闭了
            daoMeta = DAOHelper.getDAO("finalestimate-dao", "get_contract_unclosed", paramLst);
            daoMeta.setNeedFilter(false);
            lstRet = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            if (ListUtil.isNotEmpty(lstRet)) {
                lstError.add(title + "存在没有结算的采购合同！");
            }
            //校验是否存在处理中的合同变更
            daoMeta = DAOHelper.getDAO("finalestimate-dao", "get_contract_change", paramLst);
            daoMeta.setNeedFilter(false);
            lstRet = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            if (ListUtil.isNotEmpty(lstRet)) {
                lstError.add(title + "存在处理中的采购合同变更！");
            }

            //内委合同的项目是否已经完工结算
            daoMeta = DAOHelper.getDAO("finalestimate-dao", "get_unsquared_inner_project", paramLst);
            daoMeta.setNeedFilter(false);
            lstRet = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            if (ListUtil.isNotEmpty(lstRet)) {
                List<String> lstInnerContractName = new ArrayList<>();
                for (Map<String, Object> rowMap : (List<Map<String, Object>>) lstRet) {
                    String innerContractName = (String) rowMap.get("contractName");
                    BigDecimal contractMoney = (BigDecimal) rowMap.get("contractSignMoney");
                    BigDecimal receiptsMoney = (BigDecimal) rowMap.get("receiptsMoney");
                    if (!MathUtil.isNullOrZero(contractMoney)) {
                        if (StringUtil.isEmpty(innerContractName) || MathUtil.compareTo(contractMoney, receiptsMoney) != 0) {
                            lstInnerContractName.add(innerContractName);
                        }
                    }
                }
                if (ListUtil.isNotEmpty(lstInnerContractName)) {
                    lstError.add(title + "存在内委合同项目的运行额与收款金额不匹配！合同名称如下：");
                    for (String innerContractName : lstInnerContractName) {
                        lstError.add(innerContractName);
                    }
                }
            }
            //校验所有的报销，运行费报销是否已经被审核通过了，
            daoMeta = DAOHelper.getDAO("finalestimate-dao", "get_unapproved_claim", paramLst);
            daoMeta.setNeedFilter(false);
            lstRet = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            if (ListUtil.isNotEmpty(lstRet)) {
                lstError.add(title + "存在没有审核通过项目日常报销或者差旅报销！");
            }
            //所有请款是否已经核销了
            daoMeta = DAOHelper.getDAO("finalestimate-dao", "get_uncleared_cash_req", paramLst);
            daoMeta.setNeedFilter(false);
            lstRet = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            if (ListUtil.isNotEmpty(lstRet)) {
                lstError.add(title + "存在没有完成（未审核通过或者未核销完）的费用申请！");
            }

            //校验 是否已归档
            if (!Integer.valueOf(1).equals(isArchiving)) {
                lstError.add(title + "项目未归档！");
            }
        }
        PrmFinalProjectInfoDto projectInfoDto = finalestimateManager.getFinalProjectInfo(prmProjectMainId, squareType, null);
        PrmFinalEstimateDto finalEstimateDto = new PrmFinalEstimateDto();
        finalEstimateDto.setPrmProjectMainId(prmProjectMainId);
        PrmProjectMain projectMain = PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, prmProjectMainId);
        finalEstimateDto.setPrmProjectMainIdDesc(projectMain.getProjectName());
        finalEstimateDto.setProjectCode(projectMain.getProjectCode());
        finalEstimateDto.setDepartmentCode(projectMain.getContractorOffice());
        finalEstimateDto.setDepartmentCodeDesc(OrgHelper.getOrgNameByCode(projectMain.getContractorOffice()));
        finalEstimateDto.setSquareType(squareType);
        finalEstimateDto.setIsArchiving(isArchiving);
        finalEstimateDto.setPrmFinalProjectInfoDto(projectInfoDto);

        ProjectmainManager projectmainManager = com.csnt.scdp.framework.core.spring.BeanFactory.getBean
                ("projectmain-manager");
        if ("1".equals(squareType)) {
            //完工结算
            BigDecimal squareMoney = MathUtil.sub(projectInfoDto.getTotalSquareMoney(), projectInfoDto
                    .getSquareMoneySum());
            BigDecimal squareCost = MathUtil.sub(projectInfoDto.getLockedBudgetSum(), projectInfoDto.getSquareCostSum());
            BigDecimal squareProfit = MathUtil.sub(squareMoney, squareCost);
            BigDecimal squareTax = MathUtil.sub(projectInfoDto.getPlannedTax(), projectInfoDto.getRaxSum());
            finalEstimateDto.setSquareProjectMoney(squareMoney);
            finalEstimateDto.setSquareCost(squareCost);
            finalEstimateDto.setSquareGrossProfit(squareProfit);
            finalEstimateDto.setRax(squareTax);
            finalEstimateDto.setSquareProportion(BigDecimal.valueOf(100));

            //校验是否所有合同已经被关闭了
            List paramLst = new ArrayList();
            paramLst.add(prmProjectMainId);

            DAOMeta daoMeta = DAOHelper.getDAO("finalestimate-dao", "get_contract_unrelative", paramLst);
            daoMeta.setNeedFilter(false);
            List lstRet = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            if (ListUtil.isNotEmpty(lstRet)) {
                lstError.add(title + "存在没有关联合同的采购申请！采购申请单号如下：");
                for (Object o : lstRet) {
                    Map m = (Map) o;
                    lstError.add((String) m.get("purchaseReqNo"));
                }
            }

            //如果项目代号以研开头或者第7-8位是KY的，允许项目收款额不等于运行额
            String projectCode = projectMain.getProjectCode();
            if (StringUtil.isNotEmpty(projectCode) && projectCode.length() > 8 && !projectCode.startsWith("研") && !projectCode.substring(6, 8).equals("KY")) {
                if (!ObjectUtil.beSame(projectInfoDto.getTotalSquareMoney(), projectInfoDto.getProjectActualMoneySum())) {
                    lstError.add(title + "项目收款额(" + projectInfoDto.getProjectActualMoneySum() + ") " +
                            "不等于项目运行额(" + projectInfoDto.getTotalSquareMoney() + ")！");
                }
            }

            //如果项目总收款额与总开票金额不相等，给出用户提示
            Map condition = new HashMap<>();
            condition.put(PrmFinalEstimateAttribute.PRM_PROJECT_MAIN_ID, prmProjectMainId);
            List<PrmBilling> prmBillings = PersistenceFactory.getInstance().findByAnyFields(PrmBilling.class, condition, null);
            List<BigDecimal> invoiceMoneys = new ArrayList<>();
            BigDecimal invoiceMoneySum = BigDecimal.ZERO;
            if (ListUtil.isNotEmpty(prmBillings)) {
                prmBillings.stream().filter(x -> "0".equals(x.getBillType().toString()) && ("2".equals(x.getState())
                        || "8".equals(x.getState()) || "4".equals(x.getState()))).forEach(x -> {
                    invoiceMoneys.add(x.getInvoiceMoney());
                });
            }
            if (ListUtil.isNotEmpty(invoiceMoneys)) {
                for (BigDecimal invoiceMoney : invoiceMoneys) {
                    invoiceMoneySum = invoiceMoneySum.add(invoiceMoney);
                }
            }
            if (invoiceMoneySum.setScale(2,BigDecimal.ROUND_DOWN).compareTo(projectInfoDto.getProjectActualMoneySum().setScale(2, BigDecimal.ROUND_DOWN)) != 0) {
                lstInfo.add(title + "项目总收款额不等于总开票金额");
            }

            if (ListUtil.isNotEmpty(lstError)) {
                out.put("errors", lstError);
                return out;
            }
            if (ListUtil.isNotEmpty(lstInfo)) {
                out.put("infoMsg", lstInfo);
            }
        } else if ("3".equals(squareType)) {
            //中止
            BigDecimal squareMoney = projectInfoDto.getProjectActualMoneySum();
            BigDecimal squareCost = projectInfoDto.getLockedBudgetSum();
            BigDecimal squareProfit = MathUtil.sub(squareMoney, squareCost);
            BigDecimal squareTax = projectInfoDto.getPlannedTax();
            finalEstimateDto.setSquareProjectMoney(squareMoney);
            finalEstimateDto.setSquareCost(squareCost);
            finalEstimateDto.setSquareGrossProfit(squareProfit);
            finalEstimateDto.setRax(squareTax);
            if (MathUtil.isNullOrZero(projectInfoDto.getTotalSquareMoney())) {
                BigDecimal percentage = MathUtil.div(
                        MathUtil.mul(squareMoney, BigDecimal.valueOf(100)),
                        projectInfoDto.getTotalSquareMoney());
                finalEstimateDto.setSquareProportion(percentage);
            } else {
                finalEstimateDto.setSquareProportion(BigDecimal.ZERO);
            }
        } else if ("0".equals(squareType)) {
            //阶段结算
            BigDecimal availableSquareMoney1 = MathUtil.sub(projectInfoDto.getProjectActualMoneySum(), projectInfoDto
                    .getSquareMoneySum());
            BigDecimal availableCost1 = MathUtil.sub(projectInfoDto.getLockedBudgetSum(), projectInfoDto
                    .getSquareCostSum());
            BigDecimal availableCost2 = MathUtil.sub(projectInfoDto.getBilledCostSum(), projectInfoDto.getSquareCostSum());
            //validate the receipt money should be more or equal billed cost
            if (availableSquareMoney1.compareTo(availableCost2) < 0
                    || MathUtil.compareTo(projectInfoDto.getProjectActualMoneySum(), projectInfoDto.getBilledCostSum())
                    < 0) {
                lstError.add(title + "在册收款额不能小于未结算的实际总开票成本！");
                out.put("errors", lstError);
                return out;

            } else if (MathUtil.compareTo(MathUtil.mul(projectInfoDto.getProjectActualMoneySum(), projectInfoDto.getPlannedCost()),
                    MathUtil.mul(projectInfoDto.getLockedBudgetSum(), projectInfoDto.getTotalSquareMoney())) > 0) {
                //收款*（1-k）>发生成本（合同）
                BigDecimal squareMoney = MathUtil.sub(
                        MathUtil.div(MathUtil.mul(projectInfoDto.getLockedBudgetSum(),
                                        projectInfoDto.getTotalSquareMoney(), 4),
                                projectInfoDto.getPlannedCost()
                        )
                        , projectInfoDto.getSquareMoneySum()
                );
                BigDecimal squareCost = MathUtil.sub(projectInfoDto.getLockedBudgetSum(), projectInfoDto
                        .getSquareCostSum());
                BigDecimal squareProfit = MathUtil.sub(squareMoney, squareCost);
                finalEstimateDto.setSquareProjectMoney(squareMoney);
                finalEstimateDto.setSquareCost(squareCost);
                finalEstimateDto.setSquareGrossProfit(squareProfit);
            } else if (MathUtil.compareTo(MathUtil.mul(projectInfoDto.getProjectActualMoneySum(), projectInfoDto.getPlannedCost()),
                    MathUtil.mul(projectInfoDto.getBilledCostSum(), projectInfoDto.getTotalSquareMoney())) > 0) {
                //开票成本<=收款*（1-k）<=发生成本（合同）
                BigDecimal squareMoney = MathUtil.sub(projectInfoDto.getProjectActualMoneySum(), projectInfoDto
                        .getSquareMoneySum());
                BigDecimal squareCost = BigDecimal.ZERO;
                if (!MathUtil.isNullOrZero(projectInfoDto.getTotalSquareMoney())) {
                    squareCost = MathUtil.sub(MathUtil.div(
                                    MathUtil.mul(projectInfoDto.getProjectActualMoneySum(),
                                            projectInfoDto.getPlannedCost(), 4),
                                    projectInfoDto.getTotalSquareMoney()
                            ),
                            projectInfoDto.getSquareCostSum()
                    );
                }
                BigDecimal squareProfit = MathUtil.sub(squareMoney, squareCost);
                finalEstimateDto.setSquareProjectMoney(squareMoney);
                finalEstimateDto.setSquareCost(squareCost);
                finalEstimateDto.setSquareGrossProfit(squareProfit);
            } else {
                //收款*（1-k）<发票&&收款>=发生成本（发票）
                BigDecimal squareMoney = MathUtil.sub(projectInfoDto.getProjectActualMoneySum(), projectInfoDto
                        .getSquareMoneySum());
                BigDecimal squareCost = MathUtil.sub(projectInfoDto.getBilledCostSum(), projectInfoDto.getSquareCostSum());
                BigDecimal squareProfit = MathUtil.sub(squareMoney, squareCost);
                finalEstimateDto.setSquareProjectMoney(squareMoney);
                finalEstimateDto.setSquareCost(squareCost);
                finalEstimateDto.setSquareGrossProfit(squareProfit);
            }
            BigDecimal squareProportion = BigDecimal.valueOf(0);
            if (!MathUtil.isNullOrZero(projectInfoDto.getTotalSquareMoney())) {
                squareProportion = MathUtil.div(MathUtil.mul(
                        MathUtil.add(projectInfoDto.getSquareMoneySum(), finalEstimateDto.getSquareProjectMoney())
                        , BigDecimal.valueOf(100))
                        , projectInfoDto.getTotalSquareMoney());
            }
            finalEstimateDto.setSquareProportion(squareProportion);
            if (!MathUtil.isNullOrZero(projectInfoDto.getTotalSquareMoney())) {
                BigDecimal squareTax = MathUtil.sub(MathUtil.div(
                        MathUtil.mul(projectInfoDto.getProjectActualMoneySum(), projectInfoDto.getPlannedTax(), 4),
                        projectInfoDto.getTotalSquareMoney()), projectInfoDto.getRaxSum());
                finalEstimateDto.setRax(squareTax);
            } else {
                finalEstimateDto.setRax(BigDecimal.ZERO);
            }
        }
        finalEstimateDto.setState(BillStateAttribute.CMD_BILL_STATE_NEW);
        out.put("prmFinalEstimateDto", finalEstimateDto);
        return out;
    }
}
