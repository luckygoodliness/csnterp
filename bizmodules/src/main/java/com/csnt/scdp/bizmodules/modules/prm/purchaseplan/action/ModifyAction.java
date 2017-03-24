package com.csnt.scdp.bizmodules.modules.prm.purchaseplan.action;

import com.csnt.scdp.bizmodules.entity.prm.*;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainCAttribute;
import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.dto.PrmPurchasePackageDto;
import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.dto.PrmPurchasePlanDetailDto;
import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.dto.PrmPurchasePlanDto;
import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.services.intf.PurchaseplanManager;
import com.csnt.scdp.framework.commonaction.crud.CommonModifyAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.helper.UserHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import oracle.jdbc.driver.DatabaseError;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

/**
 * Description:  ModifyAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 19:04:01
 */

@Scope("singleton")
@Controller("purchaseplan-modify")
@Transactional
public class ModifyAction extends CommonModifyAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ModifyAction.class);

    @Resource(name = "purchaseplan-manager")
    private PurchaseplanManager purchaseplanManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        if (false) {
            Map m = DtoHelper.getDtoMap(inMap);
            if (m.get("prmPurchasePlanDetailDto") != null) {
                List list = new ArrayList<>();
                list = (ArrayList) m.get("prmPurchasePlanDetailDto");
                if (ListUtil.isNotEmpty(list)) {
                    double originalMoney = 0d;
                    double tempMoney = 0d;
                    String budgetId = null;
                    String type = null;
                    List<Map> ids = new ArrayList<Map>();
                    for (int i = 0; i < list.size(); i++) {
                        Map dto = (Map) list.get(i);
                        Map tempMap = new HashMap<>();
                        //预算金额修改
                        if (dto.get("purchaseBudgetMoney") != null) {
                            String uuid = dto.get("uuid").toString();
                            PrmPurchasePlanDetailDto detail = PersistenceFactory.getInstance().findByPK(PrmPurchasePlanDetailDto.class, uuid);
                            if (budgetId == null && detail != null) {
                                budgetId = detail.getPrmBudgetRefId();
                                tempMap.put("budgetId", detail.getPrmBudgetRefId());
                                tempMap.put("type", detail.getPrmBudgetType().toUpperCase());
                                tempMap.put("purchaseUuid", dto.get("uuid").toString());

                                type = detail.getPrmBudgetType().toUpperCase();
                            }
                            //已分包
                            if (detail.getSubPackageNo() != null) {
                                //累加获得修改后的拆项合计
                                tempMoney += (Double) dto.get("purchaseBudgetMoney");
                            }
                        }
                    }
                    if (tempMoney != 0) {
                        String sql = "select sum(t.purchase_budget_money) from PRM_PURCHASE_PLAN_DETAIL t  where t.prm_budget_ref_id=(select prm_budget_ref_id from prm_purchase_plan_detail p where  p.uuid='" + "" + "') and t.uuid not in ('8a91833c50fe63060150fe9187840000') group by t.prm_budget_ref_id";
                        if (budgetId != null) {
                            if (type.equals("PRINCIPAL")) {
                                originalMoney = PersistenceFactory.getInstance().findByPK(PrmBudgetPrincipal.class, budgetId).getSchemingTotalValue().doubleValue();
                            } else if (type.equals("ACCESSORY")) {
                                originalMoney = PersistenceFactory.getInstance().findByPK(PrmBudgetAccessory.class, budgetId).getTotalValue().doubleValue();
                            } else {
                                originalMoney = PersistenceFactory.getInstance().findByPK(PrmBudgetOutsource.class, budgetId).getTotalValue().doubleValue();
                            }
                            if (tempMoney > originalMoney) {
                                throw new BizException("拆项合计不能大于立项预算");
                            }
                        }
                    }
                }
            }
        }
        Map out = super.perform(inMap);
        //Do After
        return out;
    }

    public void beforeAction(BasePojo dtoObj) {
        PrmPurchasePlanDto prmPurchasePlanDto = (PrmPurchasePlanDto) dtoObj;
        purchaseplanManager.addReviseRecord(dtoObj);
        purchaseplanManager.isApplyForReimb(prmPurchasePlanDto);
        List<PrmPurchasePackageDto> lstPurchasePackage = prmPurchasePlanDto.getPrmPurchasePackageDto();
        if (ListUtil.isNotEmpty(lstPurchasePackage)) {
            for (int i = 0; i < lstPurchasePackage.size(); i++) {
                PrmPurchasePackageDto prmPurchasePackageDto = lstPurchasePackage.get(i);
                BigDecimal pendingMoney = prmPurchasePackageDto.getPendingMoney();
                String purchasePackageId = prmPurchasePackageDto.getUuid();
                String prmProjectMainId = prmPurchasePackageDto.getPrmProjectMainId();
                String packageName = prmPurchasePackageDto.getPackageName();
                DAOMeta daoMeta = new DAOMeta();
                daoMeta.setStrSql("select vp.locked_budget from vw_prm_pur_lock vp " +
                        "where vp.prm_project_main_id = ? and vp.purchase_package_id = ?");
                List param = new ArrayList();
                param.add(prmProjectMainId);
                param.add(purchasePackageId);
                daoMeta.setLstParams(param);
                List lstRst = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                if (ListUtil.isNotEmpty(lstRst)) {
                    String lockedMoneyStr = StringUtil.replaceNull(((Map) lstRst.get(0)).get("lockedBudget"));
                    BigDecimal lockedMoney = new BigDecimal(lockedMoneyStr);
                    if (BigDecimal.ZERO.compareTo(lockedMoney) < 0) {
                        if (pendingMoney.compareTo(lockedMoney) < 0) {
                            throw new BizException("包名为" + packageName + "的采购预算总价小于包锁住金额！");
                        }
                    }
                }
            }
        }
    }


}
