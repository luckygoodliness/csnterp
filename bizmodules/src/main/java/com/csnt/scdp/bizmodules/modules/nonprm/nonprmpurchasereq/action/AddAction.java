package com.csnt.scdp.bizmodules.modules.nonprm.nonprmpurchasereq.action;

import com.csnt.scdp.bizmodules.entityattributes.prm.PrmPurchaseReqAttribute;
import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.services.intf.PrmpurchasereqManager;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.nonprm.nonprmpurchasereq.services.intf.NonprmpurchasereqManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-11-19 14:00:14
 */

@Scope("singleton")
@Controller("nonprmpurchasereq-add")
@Transactional
public class AddAction extends CommonAddAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);

	@Resource(name = "nonprmpurchasereq-manager")
	private NonprmpurchasereqManager nonprmpurchasereqManager;

    @Resource(name = "prmpurchasereq-manager")
    private PrmpurchasereqManager prmpurchasereqManager;
	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
        HashMap viewData = ((HashMap)inMap.get("viewdata"));
        HashMap rdto = (HashMap)viewData.get("prmPurchaseReqDto");

        String consignee = rdto.get("addrConsignee").toString();
        String arriveLocation = rdto.get("addrArriveLocation").toString();
        String remark = rdto.get("addrRemark").toString();
        String contactWay = rdto.get("addrContactWay").toString();

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String str = df.format(now);

        rdto.put("purchaseReqNo","CGSQ" + str);
        rdto.put("state", PrmPurchaseReqAttribute.STATE_VALUE_0);
        rdto.put("isProject", "0");

        ArrayList dDto = (ArrayList)rdto.get("prmPurchaseReqDetailDto");
        for(Object dob : dDto)
        {
            HashMap dm = (HashMap)dob;
            dm.put("consignee",consignee);
            dm.put("arriveLocation",arriveLocation);
            dm.put("remark",remark);
            dm.put("contactWay",contactWay);
        }

        prmpurchasereqManager.updateCdmFileRelation(inMap,"NONPRMPURCHASEREQ");

        Map out = super.perform(inMap);
		//Do After
		return out;
	}
}
