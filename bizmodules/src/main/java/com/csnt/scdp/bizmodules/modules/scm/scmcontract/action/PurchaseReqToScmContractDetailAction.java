package com.csnt.scdp.bizmodules.modules.scm.scmcontract.action;

import com.csnt.scdp.bizmodules.entity.prm.PrmPurchaseReqDetail;
import com.csnt.scdp.bizmodules.entity.scm.ScmContractDetail;
import com.csnt.scdp.bizmodules.modules.scm.scmcontract.services.intf.ScmcontractManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.CommonAddAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
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
@Controller("purchasereq-to-scmcontractdetail-add")
@Transactional
public class PurchaseReqToScmContractDetailAction extends CommonAddAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(PurchaseReqToScmContractDetailAction.class);

    @Resource(name = "scmcontract-manager")
    private ScmcontractManager scmcontractManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        String uuid = (String) inMap.get(CommonAttribute.UUID);
        if (StringUtil.isEmpty(uuid)) {
            throw new BizException("未选择记录！");
        }
        //1.获取到PrmPurchaseReq中的记录（询价单）
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        List paramList = new ArrayList<>();
        paramList.add(uuid);
        List<PrmPurchaseReqDetail> prmPurchaseReqDetail = pcm.findByAnyFields(PrmPurchaseReqDetail.class, paramList, null);
        if (ListUtil.isNotEmpty(prmPurchaseReqDetail)) {
            List<ScmContractDetail> scmContractDetailList = new ArrayList<>();
            for (PrmPurchaseReqDetail p : prmPurchaseReqDetail) {
                ScmContractDetail scmContractDetail = new ScmContractDetail();
                scmContractDetail.setScmContractId(uuid);
                scmContractDetail.setMaterialName(p.getName());
                scmContractDetail.setModel(p.getModel());
//                scmContractDetail.setUnits(p.);
                scmContractDetail.setAmount(p.getAmount());
//                scmContractDetail.setunit

            }
        } else {
            throw new BizException("该合同无询价单！");
        }
        Map out = super.perform(inMap);
        return out;
    }
}
