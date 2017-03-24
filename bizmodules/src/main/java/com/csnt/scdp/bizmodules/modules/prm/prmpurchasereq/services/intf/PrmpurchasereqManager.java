package com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.services.intf;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Description:  PrmpurchasereqManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 15:30:53
 */
public interface PrmpurchasereqManager {
    void cleanContract(Map inMap);

//    void setExtraData(Map out);

    /**
     * 1.提交
     *
     * @param uuid
     */
    void submit(String uuid);

    /**
     * 1.审核
     *
     * @param uuid
     */
    void audit(String uuid);

    void checkExcessBudget(Map inMap);

    /**
     * 更新ERP文件管理表
     *
     * @param inMap
     */
    void updateCdmFileRelation(Map inMap, String moduleName);

    /**
     * 更新包状态
     *
     * @param packageId
     */
    boolean updatePackageState(String packageId);

    /**
     * @param inMap
     */
    void deleteAllCdmFileRelation(Map inMap);

    BigDecimal getTotalAmountByReqId(String purchaseReqId);

    public void sendMsg(String uuid, String purchaseReqNo, String deptCode);

    /**
     * 校验申请数量是否超额
     *
     * @param uuid     业务数据uuid
     * @param checkFor 数据来源0、采购申请（项目）1、采购变更申请
     */
    void checkPurchase(String uuid, String checkFor);

    /**
     * @param uuid   申请单uuid
     * @param reason 作废原因
     * @return result
     */
    String abolish(String uuid, String reason);

    /**
     * 更新预算明细标识
     */
    boolean updateBudget(Map inMap);
}