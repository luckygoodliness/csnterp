package com.csnt.scdp.bizmodules.modules.prm.finalestimate.services.intf;

import com.csnt.scdp.bizmodules.entity.prm.PrmFinalEstimate;
import com.csnt.scdp.bizmodules.modules.prm.finalestimate.dto.PrmFinalEstimateDto;
import com.csnt.scdp.bizmodules.modules.prm.finalestimate.dto.PrmFinalProjectInfoDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Description:  FinalestimateManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 20:02:44
 */
public interface FinalestimateManager {
    public List getPrmArchivingForProjectMainId(String prmProjectMainId);

    //1.额外设置需要返回的数据
    void setExtraData(PrmFinalEstimateDto dto);

    //获取项目收款数据
    public BigDecimal getProjectReceiptsMoney(String prmProjectMainId);

    //获取已发生成本
    BigDecimal getLockedBudget(String prmProjectMainId, BigDecimal totalSquareMoney, BigDecimal projectActualMoney);

    //获取已发生成本（开票）
    BigDecimal getBilledCostSum(String prmProjectMainId, BigDecimal totalSquareMoney, BigDecimal projectActualMoney);

    /**
     * 获取印花税
     *
     * @param prmProjectMainId
     * @return
     */
    BigDecimal getStampTaxMoney(String prmProjectMainId);

    //保存之前进行数据检验
    List checkBeforeSave(Map dataMap);

    PrmFinalProjectInfoDto getFinalProjectInfo(String prmProjectMainId, String squareType, String state);

    //非强制性校验,结算比例超过80%，提示采购合同是否完成结算
    List<String> validateDataNonForce(Map inMap);

    //获取项目的进项税总值
    public BigDecimal getProjectReceiptsTaxMoney(String prmProjectMainId);

    //获取项目箱管的结算信息
    List<PrmFinalEstimate> getPrmFinalEstimatesByPrmUuid(String prmUuid);
}