package com.csnt.scdp.bizmodules.modules.nonprm.nonprmpurchasereq.action;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:  ModifyAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-11-19 14:00:14
 */

@Scope("singleton")
@Controller("nonprmpurchasereq-modify")
@Transactional
public class ModifyAction extends CommonModifyAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(ModifyAction.class);

	@Resource(name = "nonprmpurchasereq-manager")
	private NonprmpurchasereqManager nonprmpurchasereqManager;

    @Resource(name = "prmpurchasereq-manager")
    private PrmpurchasereqManager prmpurchasereqManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
        HashMap viewData = ((HashMap)inMap.get("viewdata"));
        HashMap reqDto = (HashMap)viewData.get("prmPurchaseReqDto");
        ArrayList dDtoList = (ArrayList)reqDto.get("prmPurchaseReqDetailDto");

        String consignee = reqDto.get("addrConsignee") == null ? "" : reqDto.get("addrConsignee").toString();
        String arriveLocation = reqDto.get("addrArriveLocation") == null ? "" : reqDto.get("addrArriveLocation").toString();
        String remark = reqDto.get("addrRemark") == null ? "" : reqDto.get("addrRemark").toString();
        String contactWay = reqDto.get("addrContactWay") == null ? "" : reqDto.get("addrContactWay").toString();

        for(Object dob : dDtoList)
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
