package com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.action;

import com.csnt.scdp.bizmodules.entityattributes.prm.PrmPurchaseReqAttribute;
import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.dto.PrmPurchaseReqDetailDto;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.services.intf.PrmpurchasereqManager;

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
 * @timestamp 2015-09-26 15:30:52
 */

@Scope("singleton")
@Controller("prmpurchasereq-add")
@Transactional
public class AddAction extends CommonAddAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);

	@Resource(name = "prmpurchasereq-manager")
	private PrmpurchasereqManager prmpurchasereqManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
//        prmpurchasereqManager.checkExcessBudget(inMap);

        HashMap viewData = ((HashMap)inMap.get("viewdata"));
//        HashMap addDto = (HashMap)viewData.get("prmPurchaseReqDetailDtoAddress");
//        String consignee = addDto.get("consignee").toString();
//        String arriveLocation = addDto.get("arriveLocation").toString();
//        String remark = addDto.get("remark").toString();
//        String contactWay = addDto.get("contactWay").toString();

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
        rdto.put("isProject", "1");

        ArrayList dDto = (ArrayList)rdto.get("prmPurchaseReqDetailDto");
        for(Object dob : dDto)
        {
            HashMap dm = (HashMap)dob;
            dm.put("consignee",consignee);
            dm.put("arriveLocation",arriveLocation);
            dm.put("remark",remark);
            dm.put("contactWay",contactWay);
        }

        prmpurchasereqManager.updateCdmFileRelation(inMap,"PRMPURCHASEREQ");

		Map out = super.perform(inMap);
		//Do After
		return out;
	}
}
