package com.csnt.scdp.bizmodules.modules.scm.supplierinfor.services.intf;

import com.csnt.scdp.bizmodules.entity.scm.ScmSupplier;

import java.util.List;
import java.util.Map;

/**
 * Description:  SupplierinforManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-24 16:02:01
 */
public interface SupplierinforManager {
    /**
     * 设置额外需要显示的数据
     *
     * @param out
     */
    void setExtraData(Map out);

    /**
     * 使供方失效
     *
     * @param inMap
     */
    void cancelEffectSupplier(Map inMap);

    /**
     * 根据供应商、开户银行、银行账户维护供应商信息
     *
     * @param supplierName 供应商名称
     * @param bank         开户银行
     * @param bankId       银行账户
     * @param flag         0.合格供方（仅供应链部改）：年度合格供方，有名单
     *                     1.普通供方（仅供应链部改）：有合同的供应商，S/w,有资质
     *                     2.零星供方（仅供应链部改）：无合同的供应商，L，无资质，零星报销
     *                     3.报销供方（财务退回---事业部改；供应链部改）：非项目/项目的报销的客商
     *                     4.其他（财务退回---事业部改；供应链部改）：保证金类的请款（属于上家）的客商（当财务/事业部提供资质，供应链部就可以改为普通供方）
     */
    String generateSupplierByFlag(String supplierName, String bank, String bankId, String flag);
    /**
     * 根据收单单位银行账户返回收款单位
     *
     * @param bankId
     * @return
     */
    List<ScmSupplier> getBankBelongedScmSupplier(String bankId);
    void fadSupplierMappingChange(String uuid,String completeName);
    void supplierNameValidate(String completeName,String simpleName,String supplierCode);
    void supplierNameValidateMouseLeave(String completeName, String simpleName,String supplierCode);
}