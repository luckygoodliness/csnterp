package com.csnt.scdp.bizmodules.modules.workflow.prm.purchaseplan;

import com.csnt.scdp.bizmodules.entity.prm.PrmPurchasePackage;
import com.csnt.scdp.bizmodules.entity.prm.PrmPurchasePlan;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmPurchasePackageAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmPurchasePlanAttribute;
import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.action.LoadAction;
import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.dto.PrmPurchasePackageDto;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.sysmodules.entity.ScdpOrg;
import com.csnt.scdp.sysmodules.entityattributes.ScdpOrgAttribute;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("prm_purchase_plan-after-fixed")
@Transactional

public class PrmPurchasePlanAfterCompleteAction extends PrmPurchasePlanAfterStart {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        updateChangeMoney(inMap);
        Map mapResult=new HashMap();
//        mapResult=collectReturnResult(inMap);
        inMap.put(WorkFlowAttribute.SKIP_COMMON_PROCESS_AFTER_ACTION, true);
        super.updateState(inMap);
        return mapResult;
    }




}
