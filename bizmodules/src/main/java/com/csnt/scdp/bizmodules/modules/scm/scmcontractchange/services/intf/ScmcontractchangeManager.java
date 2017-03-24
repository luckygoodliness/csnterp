package com.csnt.scdp.bizmodules.modules.scm.scmcontractchange.services.intf;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Description:  ScmcontractchangeManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 18:01:19
 */
public interface ScmcontractchangeManager {
    /**
     * 初始化新增的数据
     *
     * @param inMap
     */
    void initMapForAdd(Map inMap);

    /**
     * 检查金额（如果是采购，则变更前金额应大于变更后金额）
     *
     * @param inMap
     */
    void checkMoney(Map inMap);

    /**
     * 根据合同ID获取合同信息
     *
     * @param scmContractId
     * @return
     */
    Map getContractInfo(String scmContractId);

    /**
     * 根据主键更行状态为submit
     *
     * @param uuid
     */
    void updateStateToSubmit(String uuid);

    /**
     * 根据主键更新状态为approved，同时把价格回写到合同表（SCM_CONTRACT）
     *
     * @param uuid
     * @param scmContractId
     */
    void updateStateToApproved(String uuid, String scmContractId);

    /**
     * 删除时判断是否有审核通过的数据，有则不让删除
     *
     * @param inMap
     */
    boolean checkIfHaveApprovedRecord(Map inMap);

    /**
     * 获取合同变更可用额度
     *
     * @param scmContractId
     */
    BigDecimal getRemainBudget(String scmContractId);
}