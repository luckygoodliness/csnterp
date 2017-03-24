package com.csnt.scdp.bizmodules.modules.prm.purchaseplan.services.intf;

import com.csnt.scdp.bizmodules.entity.fad.FadInvoice;
import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entity.prm.PrmPurchasePackage;
import com.csnt.scdp.bizmodules.entity.prm.PrmPurchasePlanDetail;
import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.dto.PrmPurchasePlanDto;
import com.csnt.scdp.framework.dto.BasePojo;

import java.util.List;
import java.util.Map;

/**
 * Description:  PurchaseplanManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 19:04:01
 */
public interface PurchaseplanManager {

    /**
     * 根据主表projectMainId查询已立项项目
     *
     * @param id
     * @return
     */
    PrmProjectMain findBudgetByProjectMainId(String id);

    /**
     * 根据部门code查询部门名称
     *
     * @param code
     * @return
     */
    String getDepartmentNameByCode(String code);

    /**
     * 根据Uuid查询业主（客户）名称
     *
     * @param uuid
     * @return
     */
    String getCustmerNameByUuid(String uuid);

    /**
     * 根据立项预算类别返回对应类的Class
     *
     * @param type
     * @return
     */
    Class getBudgetClassByType(String type);

    void setPackageDetailRefId(Map inMap, String uuid);

    List<String> queryExistedSerialNo(String projectMainId, List<String> lstSerialNo, String budgetType);

    Map<String, Map<String, Map<String, Object>>> getPurchasePlanDetailInfo(String mainUuid);

    public Map getInvoiceInfoByMainId(String prmProjectMainId);

    public void addReviseRecord(BasePojo dtoObj);

    public Map getRunMaterialInfo(List classCode);

    public String addPurchaseDetailToPackage(PrmPurchasePlanDetail prmPurchasePlanDetail, PrmPurchasePackage prmPurchasePackage);

    public String subPurchaseDetailFromPackage(PrmPurchasePlanDetail prmPurchasePlanDetail, PrmPurchasePackage prmPurchasePackage);

    public void isApplyForReimb(PrmPurchasePlanDto prmPurchasePlanDto);

    public boolean hasPartPackage(String purchasePlanUuid);

    public void partPackageRun(String purchasePlanUuid, String type);

    public void partPackageRun(String purchasePlanUuid, List hadReimb);
}
