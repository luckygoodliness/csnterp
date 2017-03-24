package com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.action;

import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.services.intf.PrmpurchasereqManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  ModifyAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 15:30:52
 */

@Scope("singleton")
@Controller("prmpurchasereq-modify")
@Transactional
public class ModifyAction extends CommonModifyAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(ModifyAction.class);

	@Resource(name = "prmpurchasereq-manager")
	private PrmpurchasereqManager prmpurchasereqManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
//        prmpurchasereqManager.checkExcessBudget(inMap);

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

        prmpurchasereqManager.updateCdmFileRelation(inMap,"PRMPURCHASEREQ");

        Map out = super.perform(inMap);
		//Do After
		return out;
	}


}
