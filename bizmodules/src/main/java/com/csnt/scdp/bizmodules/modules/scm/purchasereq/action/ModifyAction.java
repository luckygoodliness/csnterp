package com.csnt.scdp.bizmodules.modules.scm.purchasereq.action;

import com.csnt.scdp.bizmodules.entity.prm.PrmPurchaseReqDetail;
import com.csnt.scdp.bizmodules.entity.scm.ScmPurchaseReq;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.scm.purchasereq.services.intf.PurchasereqManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Map;

/**
 * Description:  ModifyAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 14:46:34
 */

@Scope("singleton")
@Controller("purchasereq-modify")
@Transactional
public class ModifyAction extends CommonModifyAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(ModifyAction.class);

	@Resource(name = "purchasereq-manager")
	private PurchasereqManager purchasereqManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
//		Map out = super.perform(inMap);
		//Do After
        String contractId = inMap.get("contractId").toString();
        String uuids = inMap.get("uuids").toString();
        String[] us = uuids.split(",");
        for(int i=0;i<us.length;i++){
            updateDetail(contractId,us[i]);
        }
		return null;
	}

    public void updateDetail(String contractId,String uuid){
        PrmPurchaseReqDetail sp = PersistenceFactory.getInstance().findByPK(PrmPurchaseReqDetail.class,uuid);
        sp.setScmContractId(contractId);
        PersistenceFactory.getInstance().update(sp);
    }
}
