package com.csnt.scdp.bizmodules.modules.prm.purchaseplan.action;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.attributes.ErpCommonAttribute;
import com.csnt.scdp.bizmodules.attributes.PrmBudgetCodes;
import com.csnt.scdp.bizmodules.entity.prm.*;
import com.csnt.scdp.bizmodules.entity.scm.ScmMaterialClass;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmBudgetPrincipalAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmMaterialClassAttribute;
import com.csnt.scdp.bizmodules.modules.common.action.ErpLoadAction;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpUserHelper;
import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.dto.PrmPurchasePackageDto;
import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.dto.PrmPurchasePlanDetailDto;
import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.dto.PrmPurchasePlanDto;
import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.services.intf.PurchaseplanManager;
import com.csnt.scdp.bizmodules.modules.scm.purchasereq.services.intf.PurchasereqManager;
import com.csnt.scdp.framework.bo.QueryCondition;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MathUtil;
import com.csnt.scdp.framework.util.ObjectUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:  LoadAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 19:04:01
 */

@Scope("singleton")
@Controller("purchaseplan-replacepackage")
@Transactional
public class ReplacePackage implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ReplacePackage.class);

    @Resource(name = "purchaseplan-manager")
    private PurchaseplanManager purchaseplanManager;

    @Resource(name = "purchasereq-manager")
    private PurchasereqManager purchasereqManager;


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map result = new HashMap<>();
        String newPackageId = (String) inMap.get("newPackageId");
        String oldPackageId = (String) inMap.get("oldPackageId");
        String purchaseDetailuuid = (String) inMap.get("purchaseDetailuuid");
        String changeType = (String) inMap.get("changeType");
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        Map condition = new HashMap<>();
        condition.put("prmPurchasePlanDetailId", purchaseDetailuuid);
        List<PrmPurchaseReqDetail> prmPurchaseReqDetails = pcm.findByAnyFields(PrmPurchaseReqDetail.class, condition, null);
        if (purchasereqManager.isContract(prmPurchaseReqDetails, purchaseDetailuuid)) {
            throw new BizException("该明细已经进行采购申请，不能调包");
        }

        PrmPurchasePlanDetail prmPurchasePlanDetail = pcm.findByPK(PrmPurchasePlanDetail.class, purchaseDetailuuid);

        if (changeType.equals("0")) {
            //调包
            PrmPurchasePackage oldPrmPurchasePackage = pcm.findByPK(PrmPurchasePackage.class, oldPackageId);
            PrmPurchasePackage newPrmPurchasePackage = pcm.findByPK(PrmPurchasePackage.class, newPackageId);
            //必须先减包再加包
            purchaseplanManager.subPurchaseDetailFromPackage(prmPurchasePlanDetail, oldPrmPurchasePackage);
            String res = purchaseplanManager.addPurchaseDetailToPackage(prmPurchasePlanDetail, newPrmPurchasePackage);
            if(StringUtil.isNotEmpty(res)){
                throw new BizException(res);
            }
        } else if (changeType.equals("1")) {
            //减包
            PrmPurchasePackage oldPrmPurchasePackage = pcm.findByPK(PrmPurchasePackage.class, oldPackageId);
            purchaseplanManager.subPurchaseDetailFromPackage(prmPurchasePlanDetail, oldPrmPurchasePackage);
        } else if (changeType.equals("2")) {
            //加包
            PrmPurchasePackage newPrmPurchasePackage = pcm.findByPK(PrmPurchasePackage.class, newPackageId);
            String res = purchaseplanManager.addPurchaseDetailToPackage(prmPurchasePlanDetail, newPrmPurchasePackage);
            if(StringUtil.isNotEmpty(res)){
                throw new BizException(res);
            }
        }
        return result;
    }


}
