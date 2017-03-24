package com.csnt.scdp.bizmodules.modules.scm.scmcontract.action;

import com.csnt.scdp.bizmodules.entity.scm.ScmContract;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCashReqAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmContractAttribute;
import com.csnt.scdp.bizmodules.modules.fad.cashreq.services.impl.CashreqManagerImpl;
import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.services.intf.PrmpurchasereqManager;
import com.csnt.scdp.bizmodules.modules.scm.scmcontract.services.intf.ScmcontractManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 15:32:42
 */

@Scope("singleton")
@Controller("scmcontract-approved")
@Transactional
public class ApprovedAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ApprovedAction.class);

    @Resource(name = "scmcontract-manager")
    private ScmcontractManager scmcontractManager;

    @Resource(name = "cashreq-manager")
    private CashreqManagerImpl cashreqManagerImpl;

    @Resource(name = "prmpurchasereq-manager")
    private PrmpurchasereqManager prmpurchasereqManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        String uuid = (String) inMap.get(CommonAttribute.UUID);
        if (StringUtil.isEmpty(uuid)) {
            throw new BizException("UUID为空");
        }
        Map rsMap = new HashMap();
        ScmContract scmContract = scmcontractManager.approved(uuid);
        //如果包号不为空，则更新包号
        String purchasePackageId = scmContract.getPurchasePackageId();
        if (StringUtil.isNotEmpty(purchasePackageId)) {
            prmpurchasereqManager.updatePackageState(purchasePackageId);
        }
        //1.生成采购请款单，（如果是个人报销和其他，则不是生成采购请款单）
        String scmContractPayType = scmContract.getContractPayType();
        if (!ScmContractAttribute.CONTRACT_PAY_TYPE_VALUE_3.equals(scmContractPayType) || ScmContractAttribute.CONTRACT_PAY_TYPE_VALUE_4.equals(scmContractPayType)) {
            String purchaseContractId = scmContract.getScmContractCode();
            List<ScmContract> list = new ArrayList<ScmContract>();
            list.add(scmContract);
            cashreqManagerImpl.generateScmCashreqByScmContract(list);
            rsMap.put(FadCashReqAttribute.PURCHASE_CONTRACT_ID, purchaseContractId);
        }
        return rsMap;
    }
}
