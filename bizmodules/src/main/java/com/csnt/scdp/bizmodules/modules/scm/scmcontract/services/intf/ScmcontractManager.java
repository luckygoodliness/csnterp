package com.csnt.scdp.bizmodules.modules.scm.scmcontract.services.intf;

import com.csnt.scdp.bizmodules.entity.scm.ScmContract;
import com.csnt.scdp.bizmodules.modules.scm.scmcontract.dto.ScmContractDto;

import java.util.List;
import java.util.Map;

/**
 * Description:  ScmcontractManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 15:32:43
 */
public interface ScmcontractManager {
    /**
     * 1.额外设置需要返回的数据
     *
     * @param outMap 传入的参数集
     */
    void setExtraData(Map outMap);

    /**
     * 2.直接生效
     *
     * @param uuid 合同主键ID
     */
    ScmContract directEffect(String uuid);

    /**
     * 3.取消生效
     *
     * @param uuid
     */
    void cancelEffect(String uuid);

    /**
     * 4.提交专员
     *
     * @param uuid
     */
    void submitToPersion(String uuid);

    /**
     * 5.提交审核
     *
     * @param uuid
     */
    void submitToApprove(String uuid);

    /**
     * 6.审核通过
     *
     * @param uuid
     */
    ScmContract approved(String uuid);

    /**
     * 7.合同废止
     *
     * @param uuid
     */
    void contractRevocation(String uuid, String fallbackReason);

    /**
     * 获取某供应商最新有效的银行信息
     *
     * @param inMap
     * @return
     */
    Map getSupplierLatestBankInfo(Map inMap);

    /**
     * 判断是否可以使用直接生效和取消生效
     *
     * @return
     */
    void validateIfCanDirect(String uuid);

    /**
     * 8.删除之前的判断，只能删除合同状态为（0合同谈判1合同编辑2合同明细导入）的记录
     *
     * @param uuids
     */
    void beforeDelete(List uuids);

    /**
     * 合同总额不能超过申请明细意向总金额(申请明细数据*意向单价)！
     *
     * @param inMap
     */
    void checkAmount(Map inMap);

    /**
     * 修改之前的判断，只能修改合同状态为（0合同谈判1合同编辑2合同明细导入）的记录
     *
     * @param inMap
     */
    void beforeModify(Map inMap);

    /**
     * 结算合同，即把合同状态修改为6
     *
     * @param inMap
     */
    void balance(Map inMap);

    /**
     * 根据UUID组，获取合同集合，Key为对应的UUID
     *
     * @param lstUuid
     * @return 合同集
     */
    public Map<String, ScmContract> getScmContractByIds(List lstUuid);

    /**
     * 根据UUID组，获取合同集合，Key为对应的UUID
     *
     * @param uuids 格式 "'111','eee'" ,若uuids为空，则不加过滤,filter 为过滤条件
     * @return 合同集
     */
    public Map<String, Map> getObjectsById(String uuids, String filter);

    public Map<String, Map> getObjectsById(String uuids);

    /**
     * 合同金额拆分
     * 把合同金额根据金额比例平均拆分到采购申请明细中
     * @param uuid
     * @return
     */
    void allotContractMoney(String uuid);
    /**
     * 合同明细补差价
     * @param uuid
     * @return
     */
    void clearingBalance(ScmContractDto scmContractDto);
}