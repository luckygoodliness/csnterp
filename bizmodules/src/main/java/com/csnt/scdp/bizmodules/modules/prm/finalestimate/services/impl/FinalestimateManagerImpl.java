package com.csnt.scdp.bizmodules.modules.prm.finalestimate.services.impl;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.attributes.PrmBudgetCodes;
import com.csnt.scdp.bizmodules.entity.prm.PrmBudgetDetail;
import com.csnt.scdp.bizmodules.entity.prm.PrmFinalEstimate;
import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmFinalEstimateAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainCAttribute;
import com.csnt.scdp.bizmodules.modules.prm.finalestimate.dto.PrmFinalEstimateDto;
import com.csnt.scdp.bizmodules.modules.prm.finalestimate.dto.PrmFinalProjectInfoDto;
import com.csnt.scdp.bizmodules.modules.prm.finalestimate.services.intf.FinalestimateManager;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MathUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  FinalestimateManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 20:02:44
 */

@Scope("singleton")
@Service("finalestimate-manager")
public class FinalestimateManagerImpl implements FinalestimateManager {
    //获取项目归档数据
    @Override
    public List getPrmArchivingForProjectMainId(String prmProjectMainId) {
        DAOMeta daoMeta = new DAOMeta();
        String sql = "select 1\n" +
                "from prm_archiving p\n" +
                "where 1=1  and p.state= '2' \n" +
                "and  p.prm_project_main_id='" + prmProjectMainId + "'";
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        List lstObjInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        return lstObjInfo;
    }


    @Override
    public void setExtraData(PrmFinalEstimateDto dto) {
        //1.设置项目名的显示
        String prmProjectMainId = dto.getPrmProjectMainId();
        PrmProjectMain prmProjectMain = PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, prmProjectMainId);
        if (prmProjectMain != null) {
            dto.setPrmProjectMainIdDesc(prmProjectMain.getProjectName());
            dto.setProjectCode(prmProjectMain.getProjectCode());
        }
        //2.设置部门
        String departmentCode = dto.getDepartmentCode();
        if (StringUtil.isNotEmpty(departmentCode)) {
            dto.setDepartmentCodeDesc(OrgHelper.getOrgNameByCode(departmentCode));
        }
        //加载项目信息
        PrmFinalProjectInfoDto projectInfoDto = getFinalProjectInfo(prmProjectMainId, dto.getSquareType(), dto.getState());
        dto.setPrmFinalProjectInfoDto(projectInfoDto);
        projectInfoDto.setCostCorrection(dto.getCostCorrection());

        //设置结算比例
        BigDecimal alreadySquareMoney = projectInfoDto.getSquareMoneySum();
        if (!BillStateAttribute.CMD_BILL_STATE_APPROVED.equals(dto.getState())) {
            alreadySquareMoney = MathUtil.add(alreadySquareMoney, dto.getSquareProjectMoney());
        }
        BigDecimal projectMoney = projectInfoDto.getTotalSquareMoney();
        if (!MathUtil.isNullOrZero(projectMoney)) {
            BigDecimal div = MathUtil.div(alreadySquareMoney.multiply(BigDecimal.valueOf(100)), projectMoney);
            dto.setSquareProportion(div);
        } else {
            dto.setSquareProportion(BigDecimal.ZERO);
        }
    }

    @Override
    //获取项目收款里的实际收款
    public BigDecimal getProjectReceiptsMoney(String prmProjectMainId) {
        BigDecimal actualMoney = BigDecimal.ZERO;
        DAOMeta daoMeta = new DAOMeta();
        String sql = "SELECT SUM(ACTUAL_MONEY) ACTUAL_MONEY FROM PRM_RECEIPTS WHERE PRM_PROJECT_MAIN_ID = ? AND " +
                "STATE in ('2','4','8') ";
        List lstParam = new ArrayList();
        lstParam.add(prmProjectMainId);
        daoMeta.setStrSql(sql);
        daoMeta.setLstParams(lstParam);
        daoMeta.setNeedFilter(false);
        List lstObjInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(lstObjInfo)) {
            if (((Map) lstObjInfo.get(0)).get("actualMoney") != null) {
                actualMoney = (BigDecimal) ((Map) lstObjInfo.get(0)).get("actualMoney");
            }
        }
        return actualMoney;
    }

    @Override
    //获取项目的进项税总值
    public BigDecimal getProjectReceiptsTaxMoney(String prmProjectMainId) {
        BigDecimal taxMoney = BigDecimal.ZERO;
        if (StringUtil.isNotEmpty(prmProjectMainId)) {
            List lstParam = new ArrayList();
            lstParam.add(prmProjectMainId);
            DAOMeta daoMeta = DAOHelper.getDAO("finalestimate-dao", "get_tax_money", lstParam);
            daoMeta.setNeedFilter(false);
            List lstResult = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            if (ListUtil.isNotEmpty(lstResult) && ((Map) lstResult.get(0)).get("taxMoney") != null) {
                taxMoney = (BigDecimal) ((Map) lstResult.get(0)).get("taxMoney");
            }
        }
        return taxMoney;
    }

    public List<PrmFinalEstimate> getPrmFinalEstimatesByPrmUuid(String prmUuid) {
        Map mapCondition = new HashMap();
        mapCondition.put(PrmProjectMainCAttribute.PRM_PROJECT_MAIN_ID, prmUuid);
        return PersistenceFactory.getInstance().findByAnyFields(PrmFinalEstimate.class,
                mapCondition, null);
    }

    /**
     * 获取采购合同，运行费报销
     * 完成成本（采购合同,外协合同，内委合同，运行费报销，印花税）之和
     *
     * @param prmProjectMainId
     * @return
     */
    @Override
    public BigDecimal getLockedBudget(String prmProjectMainId, BigDecimal totalSquareMoney, BigDecimal projectActualMoney) {
        BigDecimal lockedBudget = BigDecimal.ZERO;
        if (StringUtil.isNotEmpty(prmProjectMainId)) {
            List lstParam = new ArrayList();
            lstParam.add(prmProjectMainId);
            lstParam.add(prmProjectMainId);
            lstParam.add(prmProjectMainId);
            lstParam.add(prmProjectMainId);
            lstParam.add(prmProjectMainId);
            DAOMeta daoMeta = DAOHelper.getDAO("finalestimate-dao", "get_prm_cost_sum", lstParam);
            daoMeta.setNeedFilter(false);
            List lstResult = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            if (ListUtil.isNotEmpty(lstResult) && ((Map) lstResult.get(0)).get("lockedBudget") != null) {
                lockedBudget = (BigDecimal) ((Map) lstResult.get(0)).get("lockedBudget");
            }
            //load tax
            Map mapCondition = new HashMap();
            mapCondition.put(PrmProjectMainCAttribute.PRM_PROJECT_MAIN_ID, prmProjectMainId);
            List<PrmBudgetDetail> lstBudgetDetail = PersistenceFactory.getInstance().findByAnyFields(PrmBudgetDetail.class,
                    mapCondition, null);
            if (ListUtil.isNotEmpty(lstBudgetDetail)) {
                for (PrmBudgetDetail budgetDetail : lstBudgetDetail) {
                    if (PrmBudgetCodes.STAMP_TAX.equals(budgetDetail.getBudgetCode())) {
                        lockedBudget = MathUtil.add(lockedBudget, budgetDetail.getCostControlMoney());
                    } else if (PrmBudgetCodes.TAX_NO_STAMP.equals(budgetDetail.getBudgetCode())) {
                        BigDecimal costControlMoney = budgetDetail.getCostControlMoney();
                        BigDecimal realCostControlMoney = BigDecimal.ZERO;
                        if (!MathUtil.isNullOrZero(totalSquareMoney) && !MathUtil.isNullOrZero(projectActualMoney)) {
                            realCostControlMoney = MathUtil.div(MathUtil.mul(costControlMoney, projectActualMoney), totalSquareMoney, 2);
                        }

                        lockedBudget = MathUtil.add(lockedBudget, realCostControlMoney);
                    }
                }
            }
        }
        return lockedBudget;
    }

    /**
     * 获取采购合同，运行费报销
     * 完成成本（采购发票,外协发票，内委合同，运行费报销，印花税）之和
     *
     * @param prmProjectMainId
     * @return
     */
    @Override
    public BigDecimal getBilledCostSum(String prmProjectMainId, BigDecimal totalSquareMoney, BigDecimal projectActualMoney) {
        BigDecimal lockedBudget = BigDecimal.ZERO;
        if (StringUtil.isNotEmpty(prmProjectMainId)) {
            List lstParam = new ArrayList();
            lstParam.add(prmProjectMainId);
            lstParam.add(prmProjectMainId);
            lstParam.add(prmProjectMainId);
            lstParam.add(prmProjectMainId);
            DAOMeta daoMeta = DAOHelper.getDAO("finalestimate-dao", "get_prm_cost_sum2", lstParam);
            daoMeta.setNeedFilter(false);
            List lstResult = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            if (ListUtil.isNotEmpty(lstResult) && ((Map) lstResult.get(0)).get("lockedBudget") != null) {
                lockedBudget = (BigDecimal) ((Map) lstResult.get(0)).get("lockedBudget");
            }
            //load tax
            Map mapCondition = new HashMap();
            mapCondition.put(PrmProjectMainCAttribute.PRM_PROJECT_MAIN_ID, prmProjectMainId);
            List<PrmBudgetDetail> lstBudgetDetail = PersistenceFactory.getInstance().findByAnyFields(PrmBudgetDetail.class,
                    mapCondition, null);
            if (ListUtil.isNotEmpty(lstBudgetDetail)) {
                for (PrmBudgetDetail budgetDetail : lstBudgetDetail) {
                    if (PrmBudgetCodes.STAMP_TAX.equals(budgetDetail.getBudgetCode())) {
                        lockedBudget = MathUtil.add(lockedBudget, budgetDetail.getCostControlMoney());
                    } else if (PrmBudgetCodes.TAX_NO_STAMP.equals(budgetDetail.getBudgetCode())) {
                        BigDecimal costControlMoney = budgetDetail.getCostControlMoney();
                        BigDecimal realCostControlMoney = BigDecimal.ZERO;
                        if (!MathUtil.isNullOrZero(totalSquareMoney) && !MathUtil.isNullOrZero(projectActualMoney)) {
                            realCostControlMoney = MathUtil.div(MathUtil.mul(costControlMoney, projectActualMoney), totalSquareMoney, 2);
                        }

                        lockedBudget = MathUtil.add(lockedBudget, realCostControlMoney);
                    }
                }
            }
        }
        return lockedBudget;
    }

    /**
     * 获取印花税
     *
     * @param prmProjectMainId
     * @return
     */
    @Override
    public BigDecimal getStampTaxMoney(String prmProjectMainId) {
        BigDecimal stampTaxMoney = BigDecimal.ZERO;
        if (StringUtil.isNotEmpty(prmProjectMainId)) {
            List lstParam = new ArrayList();
            lstParam.add(prmProjectMainId);
            DAOMeta daoMeta = DAOHelper.getDAO("finalestimate-dao", "get_stamp_tax", lstParam);
            daoMeta.setNeedFilter(false);
            List lstRst = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            if (ListUtil.isNotEmpty(lstRst)) {
                stampTaxMoney = (BigDecimal) ((Map) lstRst.get(0)).get("costControlMoney");
            }
        }
        return stampTaxMoney;
    }

    @Override
    //保存之前进行数据检验
    public List checkBeforeSave(Map dataMap) {
        Map prmFinalEstimateMap = (Map) dataMap.get("prmFinalEstimateDto");
        PrmFinalEstimateDto prmFinalEstimateDto = (PrmFinalEstimateDto) BeanUtil.map2Dto(prmFinalEstimateMap, PrmFinalEstimateDto.class);
        String prmProjectMainId = prmFinalEstimateDto.getPrmProjectMainId();
        String uuid = prmFinalEstimateDto.getUuid();
        List lstError = new ArrayList();
        BigDecimal stampTaxMoney = getStampTaxMoney(prmProjectMainId);
        BigDecimal squareCostSum = BigDecimal.ZERO;
        BigDecimal squareCost = BigDecimal.ZERO;
        if (StringUtil.isNotEmpty(prmFinalEstimateMap.get("squareCost"))) {
            squareCost = new BigDecimal(prmFinalEstimateMap.get("squareCost").toString());
        }
        if (StringUtil.isNotEmpty(prmFinalEstimateMap.get("squareCostSum"))) {
            squareCostSum = new BigDecimal(prmFinalEstimateMap.get("squareCostSum").toString());
        }
        if (!MathUtil.isNullOrZero(squareCostSum)) {
            if (MathUtil.compareTo(squareCost, stampTaxMoney) < 0) {
                lstError.add("结算成本要大于该项目的印花税，印花税为" + stampTaxMoney + "元！");
            }
        }
        return lstError;
    }

    //获取项目结算的相关信息
    public PrmFinalProjectInfoDto getFinalProjectInfo(String prmProjectMainId, String squareType, String state) {
        PrmFinalProjectInfoDto projectInfoDto = new PrmFinalProjectInfoDto();

        //历史结算求和
        List paramLst = new ArrayList();
        paramLst.add(prmProjectMainId);
        DAOMeta daoMeta = DAOHelper.getDAO("finalestimate-dao", "get_approved_final_history", paramLst);
        daoMeta.setNeedFilter(false);

        List<Map<String, Object>> lstRet = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        //已结算运行额之和
        BigDecimal squareProjectMoneySum = BigDecimal.ZERO;
        //已结算成本之和
        BigDecimal squareCostSum = BigDecimal.ZERO;
        //已结算税金之和
        BigDecimal raxSum = BigDecimal.ZERO;
        //已结算利润之和
        BigDecimal squareGrossProfitSum = BigDecimal.ZERO;
        BigDecimal taxCorrectionSum = BigDecimal.ZERO;
        //进项税总值
        BigDecimal taxMoneySum = BigDecimal.ZERO;
        if (ListUtil.isNotEmpty(lstRet)) {
            for (Map<String, Object> map : lstRet) {
                String _squareType = (String) map.get(PrmFinalEstimateAttribute.SQUARE_TYPE);
                squareProjectMoneySum = MathUtil.add(squareProjectMoneySum, (BigDecimal) map.get
                        (PrmFinalEstimateAttribute.SQUARE_PROJECT_MONEY));
                squareCostSum = MathUtil.add(squareCostSum, (BigDecimal) map.get(PrmFinalEstimateAttribute.SQUARE_COST));
                squareGrossProfitSum = MathUtil.add(squareGrossProfitSum, (BigDecimal) map.get(PrmFinalEstimateAttribute
                        .SQUARE_GROSS_PROFIT));
                raxSum = MathUtil.add(raxSum, (BigDecimal) map.get(PrmFinalEstimateAttribute.RAX));
                Object taxCorrection = map.get(PrmFinalEstimateAttribute.TAX_CORRECTION);
                raxSum = MathUtil.add(raxSum, (BigDecimal) taxCorrection);
                taxCorrectionSum = MathUtil.add(taxCorrectionSum, (BigDecimal) taxCorrection);
            }
        }
        projectInfoDto.setSquareMoneySum(squareProjectMoneySum);
        projectInfoDto.setSquareCostSum(squareCostSum);
        projectInfoDto.setSquareGrossProfitSum(squareGrossProfitSum);
        projectInfoDto.setRaxSum(raxSum);

        BigDecimal totalSquareMoney = BigDecimal.ZERO;
        BigDecimal plannedCost = BigDecimal.ZERO;
        BigDecimal plannedProfit = BigDecimal.ZERO;
        BigDecimal plannedTax = BigDecimal.ZERO;
        Map mapCondition = new HashMap();
        mapCondition.put(PrmProjectMainCAttribute.PRM_PROJECT_MAIN_ID, prmProjectMainId);
        List<PrmBudgetDetail> lstBudgetDetail = PersistenceFactory.getInstance().findByAnyFields(PrmBudgetDetail.class,
                mapCondition, null);
        if (ListUtil.isNotEmpty(lstBudgetDetail)) {
            for (PrmBudgetDetail budgetDetail : lstBudgetDetail) {
                if (PrmBudgetCodes.PROJECT_MONEY.equals(budgetDetail.getBudgetCode())
                        && budgetDetail.getContractMoney() != null) {
                    totalSquareMoney = budgetDetail.getContractMoney();
                } else if (PrmBudgetCodes.ESTIMATED_COST.equals(budgetDetail.getBudgetCode())
                        && budgetDetail.getCostControlMoney() != null) {
                    plannedCost = budgetDetail.getCostControlMoney();
                } else if (PrmBudgetCodes.PLANNED_PROFIT.equals(budgetDetail.getBudgetCode())
                        && budgetDetail.getCostControlMoney() != null) {
                    plannedProfit = budgetDetail.getCostControlMoney();
                } else if (PrmBudgetCodes.TAX_NO_STAMP.equals(budgetDetail.getBudgetCode())
                        && budgetDetail.getCostControlMoney() != null) {
                    plannedTax = budgetDetail.getCostControlMoney();
                }
            }
        }
        //总运行额
        projectInfoDto.setTotalSquareMoney(totalSquareMoney);
        //总成本
        projectInfoDto.setPlannedCost(plannedCost);
        //总利润
        projectInfoDto.setPlannedProfit(plannedProfit);
        //计划总税金
        projectInfoDto.setPlannedTax(plannedTax);
        //所有的收款（项目收款求和）
        BigDecimal projectActualMoney = this.getProjectReceiptsMoney(prmProjectMainId);
        projectInfoDto.setProjectActualMoneySum(projectActualMoney);

        //计划利润率（计划利润/计划运行额）百分比数
        BigDecimal projectPercent = BigDecimal.ZERO;
        if (!MathUtil.isNullOrZero(totalSquareMoney)) {
            projectPercent = MathUtil.div(plannedProfit.multiply(BigDecimal.valueOf(100)), totalSquareMoney, 2);
        }
        projectInfoDto.setPlannedProfitPercent(projectPercent);
        //在册运行额
        projectInfoDto.setRegisteredMoney(MathUtil.sub(totalSquareMoney, projectInfoDto.getSquareMoneySum()));
        //在册收款额
        projectInfoDto.setRegisteredReceiveMoney(MathUtil.sub(projectActualMoney, projectInfoDto.getSquareMoneySum()));
        //已发生成本,已发生金额+修正税金
        BigDecimal lockedBudget = MathUtil.add(this.getLockedBudget(prmProjectMainId, totalSquareMoney, projectActualMoney), taxCorrectionSum);
        BigDecimal billedCostSum = MathUtil.add(this.getBilledCostSum(prmProjectMainId, totalSquareMoney, projectActualMoney), taxCorrectionSum);
        projectInfoDto.setLockedBudgetSum(lockedBudget);
        projectInfoDto.setBilledCostSum(billedCostSum);
        if ("1".equals(squareType) && BillStateAttribute.CMD_BILL_STATE_APPROVED.equals(state)) {
            projectInfoDto.setRegisteredCost(BigDecimal.ZERO);
            projectInfoDto.setRegisteredProfit(BigDecimal.ZERO);
        } else {
            //在册成本
            projectInfoDto.setRegisteredCost(MathUtil.sub(plannedCost, projectInfoDto.getSquareCostSum()));
            //在册利润
            projectInfoDto.setRegisteredProfit(MathUtil.sub(plannedProfit, projectInfoDto.getSquareGrossProfitSum()));
        }
        //进项税总值
        taxMoneySum = this.getProjectReceiptsTaxMoney(prmProjectMainId);
        projectInfoDto.setTaxMoneySum(taxMoneySum);

        return projectInfoDto;
    }

    @Override
    public List<String> validateDataNonForce(Map dataMap) {
        Map prmFinalEstimateMap = (Map) dataMap.get("prmFinalEstimateDto");
        PrmFinalEstimateDto prmFinalEstimateDto = (PrmFinalEstimateDto) BeanUtil.map2Dto(prmFinalEstimateMap, PrmFinalEstimateDto.class);
        String prmProjectMainId = prmFinalEstimateDto.getPrmProjectMainId();
        List<String> lstInfo = new ArrayList();
        if (StringUtil.isNotEmpty(prmProjectMainId)) {
            BigDecimal squareProportion = BigDecimal.ZERO;
            if (StringUtil.isNotEmpty(prmFinalEstimateMap.get("squareProportion"))) {
                squareProportion = new BigDecimal(prmFinalEstimateMap.get("squareProportion").toString());
            }
            if (squareProportion.compareTo(new BigDecimal(80)) > 0) {
                List paramLst = new ArrayList();
                paramLst.add(prmProjectMainId);
                DAOMeta daoMeta = DAOHelper.getDAO("finalestimate-dao", "get_contract_unclosed", paramLst);
                daoMeta.setNeedFilter(false);
                List lstRet = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                if (ListUtil.isNotEmpty(lstRet)) {
                    lstInfo.add("阶段结算（比例超过80%）提示：采购合同还没有完全结束！");
                } else {
                    lstInfo.add("阶段结算（比例超过80%）提示：采购合同已经完全结束！");
                }
            }
        }

        return lstInfo;
    }


}