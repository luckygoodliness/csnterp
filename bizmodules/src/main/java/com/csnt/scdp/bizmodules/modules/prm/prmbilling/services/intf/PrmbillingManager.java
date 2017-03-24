package com.csnt.scdp.bizmodules.modules.prm.prmbilling.services.intf;

import com.csnt.scdp.bizmodules.entity.prm.PrmBilling;

import java.util.List;
import java.util.Map;

/**
 * Description:  PrmbillingManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 17:35:07
 */
public interface PrmbillingManager {

    /**
     * 字符串值处理
     *
     * @param value 字符串值
     * @return 字符串值为""时返回"0",否则返回字符串值
     */
    String isEmptyReturnZero(String value);

    /**
     * 字符串值处理
     *
     * @param value 字符串值
     * @return 字符串值与数字0相等时返回"",否则返回字符串值
     */
    String isZeroReturnEmpty(String value);

    /**
     * 字符串值处理
     *
     * @param value 字符串值
     * @return 字符串值为null时返回"",否则返回字符串值
     */
    String isNullReturnEmpty(Object value);

    /**
     * 字符串值处理
     *
     * @param value 数字整型值
     * @return 数字整型值为null时返回"",否则返回字符串值
     */
    String isNullReturnEmpty(Integer value);

    /**
     * 主表凭证时间戳验证
     *
     * @param prmBillingUuid 须时间戳验证的主表开票uuid
     * @param tblVersion     时间戳
     */
    void billingCheckTimeStamp(String prmBillingUuid, String tblVersion);

    //额外设置需要返回的数据
    void setExtraData(Map outMap);

    //根据合同id获取数据
    public PrmBilling getCustomerForUUID(String uuid);

    /**
     * 获取某客户最新的银行信息
     *
     * @param inMap
     * @return
     */
    Map getCustomerLatestBankInfo(Map inMap);

    /**
     * 校验银行账号是否可用
     * @param customerName
     * @param bankNo
     */
    void validaBankNo(String customerName,String bankNo);

    /**
     * 开票申请审核后更新客户信息
     */
    void updateCustomerAfterBilling(String billingUuid);

    // M3_C11_F1_增加消息推送
    List<Map<String, Object>> getTreasurers();

    // M3_C11_F1_增加消息推送
    String getProjectCode(String uuid);
}