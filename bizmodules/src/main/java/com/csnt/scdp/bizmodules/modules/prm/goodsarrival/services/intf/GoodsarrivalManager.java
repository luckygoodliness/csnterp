package com.csnt.scdp.bizmodules.modules.prm.goodsarrival.services.intf;

import java.util.List;
import java.util.Map;

/**
 * Description:  GoodsarrivalManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-10-15 12:30:07
 */
public interface GoodsarrivalManager {
    //根据uuid获取合同明细表数据
    public List getScmContractDetailForUUID(String uuid);

    //1.额外设置需要返回的数据
    void setExtraData(Map outMap);

    /**
     * 更新结算状态
     *
     * @param uuids
     */
    void updateIsClosed(String uuids);

    /**
     * 把运费更新到到货确认表中
     *
     * @param contractId
     */
    void confirmFreight(String contractId, String name);
}