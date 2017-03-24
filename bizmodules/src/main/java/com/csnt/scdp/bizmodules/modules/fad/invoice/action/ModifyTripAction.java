package com.csnt.scdp.bizmodules.modules.fad.invoice.action;

import com.csnt.scdp.bizmodules.entityattributes.fad.FadCashReqInvoiceAttribute;
import com.csnt.scdp.bizmodules.modules.fad.invoice.dto.FadInvoiceDto;
import com.csnt.scdp.bizmodules.modules.fad.invoice.services.intf.InvoiceManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.CommonModifyAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Calendar;
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
 * @timestamp 2015-09-25 14:13:12
 */

@Scope("singleton")
@Controller("tripinvoice-modify")
@Transactional
public class ModifyTripAction extends CommonModifyAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ModifyTripAction.class);

    @Resource(name = "invoice-manager")
    private InvoiceManager invoiceManager;

    public void validation(Map inMap) {

        String dtoClass = (String) inMap.get(CommonAttribute.DTO_CLASS_NAME);
        Map viewMap = (Map) inMap.get(CommonAttribute.VIEW_DATA);
        String mainTabName = StringUtil.capFirst(BeanFactory.getClass(dtoClass).getSimpleName(), false);
        Map mapInput = (Map) viewMap.get(mainTabName);
        if (mapInput != null) {
            List<Map> fadCashReqInvoiceList = (List) mapInput.get("fadCashReqInvoiceDto");
            for (Map s : fadCashReqInvoiceList) {
                BigDecimal amount;
                String editflag = s.get("editflag").toString();
                String str = "1";
                switch (StringUtil.replaceNull(editflag)) {
                    case "+":
                    case "":
                        BigDecimal clearanceMoney = new BigDecimal((s.get("clearanceMoney")).toString());//核销金额
                        String fadCashReqId = s.get("fadCashReqId").toString();//核销金额
                        str = invoiceManager.fadCashReqInvoiceCheckAdd(fadCashReqId, clearanceMoney);
                        break;
                    case "-":
                        break;
                    case "*":
                        String uuid = s.get("uuid").toString();
                        amount = new BigDecimal((s.get("clearanceMoney")).toString());
                        str = invoiceManager.fadCashReqInvoiceCheckModify(uuid, amount);
                    case "#":
                        break;
                }
                if (str.equals("2")) {
                    throw new BizException("找不到对应请款！", new HashMap());
                } else if (!str.equals("1")) {
                    throw new BizException("流水号" + str + "请款核销金额已经超过请款总额，请重新分配！", new HashMap());
                }
//                countInvoiceTotalValue = countInvoiceTotalValue.add(amount);
            }
        }
    }

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        validation(inMap);
        String dtoClass = (String) inMap.get(CommonAttribute.DTO_CLASS_NAME);
        Map viewMap = (Map) inMap.get(CommonAttribute.VIEW_DATA);
        String mainTabName = StringUtil.capFirst(BeanFactory.getClass(dtoClass).getSimpleName(), false);
        Map mapInput = (Map) viewMap.get(mainTabName);
//        String uuid = (String) mapInput.get("uuid");
//        String prmProjectMainId = (String) mapInput.get("prmProjectMainId");
//        String officeId = (String) mapInput.get("officeId");
//        FadInvoiceDto fadInvoiceDto = PersistenceFactory.getInstance().findByPK(FadInvoiceDto.class, uuid);
//        if (StringUtil.isEmpty(prmProjectMainId)&&StringUtil.isNotEmpty(fadInvoiceDto.getPrmProjectMainId())) {
//            prmProjectMainId = fadInvoiceDto.getPrmProjectMainId();
//        }
//        if (StringUtil.isEmpty(officeId)&&StringUtil.isNotEmpty(fadInvoiceDto.getOfficeId())) {
//            officeId = fadInvoiceDto.getOfficeId();
//        }
//        if(StringUtil.isNotEmpty(fadInvoiceDto.getOfficeId())){
//
//        }
//        String state =
//        //toDo 修改时如果状态等0时校验预算是否足够！！！！
//        if (0 == 1) {
//            BigDecimal payCash = new BigDecimal(mapInput.get("payCash").toString());
//            fadInvoiceDto.getPrmProjectMainId()
//        }

        if (mapInput.get("fadCashReqInvoiceDto") != null) {
            List<Map> scmContractInvoiceList = (List) mapInput.get("fadCashReqInvoiceDto");
            for (Map s : scmContractInvoiceList) {
                s.put("clearanceType", FadCashReqInvoiceAttribute.CLEARANCE_TYPE_INVOICE);
            }
        }
        Map out = super.perform(inMap);
        //Do After
        return out;
    }
    protected void afterAction(BasePojo dtoObj) {
        FadInvoiceDto fadInvoiceDto = (FadInvoiceDto)dtoObj;

        if("0".equals(fadInvoiceDto.getState())){//只在提交之前做校验，因为提交后已经本数据已经在占用额度中，并且提交之前不可以改大额度
            BigDecimal payCash = fadInvoiceDto.getPayCash();
            if(StringUtil.isNotEmpty(fadInvoiceDto.getPrmProjectMainId())){
                invoiceManager.checkPrmTripBudget(fadInvoiceDto.getPrmProjectMainId(),payCash);
            }else {
                String fadYear = fadInvoiceDto.getFadYear();
                int fadYearInt = Integer.parseInt(fadYear);
                Calendar a = Calendar.getInstance();
                int year = a.get(Calendar.YEAR);
                if (fadYearInt < year && payCash.compareTo(new BigDecimal(0.00)) == 1) {
                    throw new BizException("不能使用往年预算，往年报销报现金额必须为0！", new HashMap());
                }
                invoiceManager.checkNonTripBudget(fadInvoiceDto.getOfficeId(), payCash, fadInvoiceDto.getFadYear());
            }
        }
    }
}
