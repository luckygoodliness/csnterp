package com.csnt.scdp.bizmodules.modules.prm.receipts.action;

import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.prm.receipts.services.intf.ReceiptsManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.*;

/**
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 09:19:57
 */

@Scope("singleton")
@Controller("receipts-add")
@Transactional
public class AddAction extends CommonAddAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);

	@Resource(name = "receipts-manager")
	private ReceiptsManager receiptsManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
        Map out = new HashMap();
        if(receiptsManager.checkMoneyBasedonUnknownReceipts(inMap)){
            out = super.perform(inMap);
			Map  viewdata = (Map)inMap.get("viewdata");
			Map data = (Map)viewdata.get("prmReceiptsDto");
		    if(data.get("isInternal")!=null && ((Long)data.get("isInternal"))==1){
				String sql = " update PRM_RECEIPTS set money_type='' where nvl(is_internal,0)=1 and money_type is not null ";
				DAOMeta daoMeta1 = new DAOMeta(sql,  new ArrayList<>());
				PersistenceFactory.getInstance().updateByNativeSQL(daoMeta1);
			}
        } else {
            throw new BizException("实际收款金额超出待确认收款剩余金额!");
        }
		//Do After
		return out;
	}
}
