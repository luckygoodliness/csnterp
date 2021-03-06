package com.csnt.scdp.bizmodules.modules.fad.cashclearance.action;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCashReqInvoiceAttribute;
import com.csnt.scdp.bizmodules.modules.fad.invoice.services.intf.InvoiceManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.helper.NumberingHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.fad.cashclearance.services.intf.CashclearanceManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
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
 * @timestamp 2015-11-08 18:01:30
 */

@Scope("singleton")
@Controller("cashclearance-modify")
@Transactional
public class ModifyAction extends CommonModifyAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(ModifyAction.class);

	@Resource(name = "cashclearance-manager")
	private CashclearanceManager cashclearanceManager;
    @Resource(name = "invoice-manager")
    private InvoiceManager invoiceManager;

    public void validation(Map inMap) {
        String dtoClass = (String) inMap.get(CommonAttribute.DTO_CLASS_NAME);
        Map viewMap = (Map) inMap.get(CommonAttribute.VIEW_DATA);
        String mainTabName = StringUtil.capFirst(BeanFactory.getClass(dtoClass).getSimpleName(), false);
        Map mapInput = (Map) viewMap.get(mainTabName);
        if(mapInput!=null){
            List<Map> fadCashReqInvoiceList = (List)mapInput.get("fadCashReqInvoiceDto");
            for(Map s:fadCashReqInvoiceList){
                BigDecimal amount;
                String editflag =s.get("editflag").toString();
                String str ="1";
                switch (StringUtil.replaceNull(editflag)) {
                    case "+":
                    case "":
                        BigDecimal clearanceMoney=new BigDecimal((s.get("clearanceMoney")).toString());//核销金额
                        String fadCashReqId = s.get("fadCashReqId").toString();//核销金额
                        str =invoiceManager.fadCashReqInvoiceCheckAdd(fadCashReqId,clearanceMoney);
                        break;
                    case "-":
                        break;
                    case "*":
                        String uuid = s.get("uuid").toString();
                        amount=new BigDecimal((s.get("clearanceMoney")).toString());
                        str =invoiceManager.fadCashReqInvoiceCheckModify(uuid,amount);
                    case "#":
                        break;
                }
                if(str.equals("2")){
                    throw new BizException("找不到对应请款！", new HashMap());
                }else if(!str.equals("1")){
                    throw new BizException("流水号"+str+"请款核销金额已经超过请款总额，请重新分配！", new HashMap());
                }
//                countInvoiceTotalValue = countInvoiceTotalValue.add(amount);
            }
        }
    }
	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
        validation(inMap);
        String dtoClass = (String) inMap.get(CommonAttribute.DTO_CLASS_NAME);
        Map viewMap = (Map) inMap.get(CommonAttribute.VIEW_DATA);
        String mainTabName = StringUtil.capFirst(BeanFactory.getClass(dtoClass).getSimpleName(), false);
        Map mapInput = (Map) viewMap.get(mainTabName);
        if (mapInput.get("fadCashReqInvoiceDto") != null) {
            List<Map> scmContractInvoiceList = (List) mapInput.get("fadCashReqInvoiceDto");
            for (Map s : scmContractInvoiceList) {
                s.put("clearanceType", FadCashReqInvoiceAttribute.CLEARANCE_TYPE_CASH);
            }
        }
		Map out = super.perform(inMap);
		//Do After
		return out;
	}
}
