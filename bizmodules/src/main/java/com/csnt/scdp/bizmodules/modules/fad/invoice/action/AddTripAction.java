package com.csnt.scdp.bizmodules.modules.fad.invoice.action;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCashReqInvoiceAttribute;
import com.csnt.scdp.bizmodules.modules.fad.invoice.services.intf.InvoiceManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.CommonAddAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.helper.NumberingHelper;
import org.apache.struts2.components.Date;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

/**
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-25 14:13:12
 */

@Scope("singleton")
@Controller("tripinvoice-add")
@Transactional
public class AddTripAction extends CommonAddAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(AddTripAction.class);

    @Resource(name = "invoice-manager")
    private InvoiceManager invoiceManager;

    public void validation(Map inMap) {
        String dtoClass = (String) inMap.get(CommonAttribute.DTO_CLASS_NAME);
        Map viewMap = (Map) inMap.get(CommonAttribute.VIEW_DATA);
        String mainTabName = StringUtil.capFirst(BeanFactory.getClass(dtoClass).getSimpleName(), false);
        Map mapInput = (Map) viewMap.get(mainTabName);

//        BigDecimal invoiceTotalValue = new BigDecimal((mapInput.get("invoiceTotalValue")).toString());
        if (mapInput != null) {
//            TRAVEL_CHARGE
//                    差旅费
//            invoiceManager.checkBudgetIsEnough(isPrm,String budgetId, expensesMoney);
            List<Map> fadCashReqInvoiceList = (List) mapInput.get("fadCashReqInvoiceDto");
            for (Map s : fadCashReqInvoiceList) {
                BigDecimal clearanceMoney = new BigDecimal((s.get("clearanceMoney")).toString());//核销金额
                String fadCashReqId = s.get("fadCashReqId").toString();//核销金额
                String str = invoiceManager.fadCashReqInvoiceCheckAdd(fadCashReqId, clearanceMoney);
                if (str.equals("2")) {
                    throw new BizException("找不到对应请款！", new HashMap());
                } else if (!str.equals("1")) {
                    throw new BizException("流水号" + str + "请款已录入的核销金额已经超过请款总额，请重新分配！", new HashMap());
                }
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
//        boolean isPrm = false;
        Map budgetMap = new HashMap();

        String budgetId = "";
        String budgetCode = "";
        String budgetName = "";
//        BigDecimal expensesMoney =new BigDecimal(mapInput.get("expensesMoney").toString());
//        BigDecimal totalClearanceMoney =new BigDecimal(0.00);
        BigDecimal payCash = new BigDecimal(mapInput.get("payCash").toString());
        if (mapInput.get("fadCashReqInvoiceDto") != null) {
            List<Map> fadCashReqInvoiceList = (List) mapInput.get("fadCashReqInvoiceDto");
            for (Map s : fadCashReqInvoiceList) {
                s.put("clearanceType", FadCashReqInvoiceAttribute.CLEARANCE_TYPE_INVOICE);
//                totalClearanceMoney=totalClearanceMoney.add(new BigDecimal(s.get("clearanceMoney").toString()));
            }
        }
//        expensesMoney=expensesMoney.subtract(totalClearanceMoney);
        if (mapInput.get("prmProjectMainId") != null) {//项目
//            isPrm=true;
            String prmProjectMainId = mapInput.get("prmProjectMainId").toString();
            List budgetlist = invoiceManager.getPrmTripBudget(prmProjectMainId);
            if (!budgetlist.isEmpty() && budgetlist.size() != 0) {
                budgetMap = (Map) budgetlist.get(0);
                BigDecimal balance = new BigDecimal((budgetMap.get("remainBudget")).toString());//可用额度
                if ("1".equals(budgetMap.get("isPurchase").toString())) {
                    throw new BizException("该项目差旅必须走采购流程！", new HashMap());
                }
                if (payCash.compareTo(balance) == 1) {
                    throw new BizException("对应费用预算额度不足！", new HashMap());
                }
            } else {
                throw new BizException("目前没有分配对应费用预算！", new HashMap());
            }
            budgetId = (budgetMap.get("budgetId")).toString();
            budgetCode = (budgetMap.get("budgetCode")).toString();
            budgetName = (budgetMap.get("budgetName")).toString();
        } else {//非项目
            String officeCode = mapInput.get("officeId").toString();
            String fadYear = mapInput.get("fadYear").toString();
            int fadYearInt = Integer.parseInt(fadYear);
            Calendar a = Calendar.getInstance();
            int year = a.get(Calendar.YEAR);
            if (fadYearInt < year && payCash.compareTo(new BigDecimal(0.00)) == 1) {
                throw new BizException("不能使用往年预算，往年报销报现金额必须为0！", new HashMap());
            }
//            budgetMap = invoiceManager.getNonTripBudget(officeId);
            List budgetlist = invoiceManager.getNonTripBudget(officeCode, fadYear);
            if (!budgetlist.isEmpty() && budgetlist.size() != 0) {
                budgetMap = (Map) budgetlist.get(0);
                BigDecimal balance = new BigDecimal((budgetMap.get("remainBudget")).toString());//可用额度
                if (payCash.compareTo(balance) == 1) {
                    throw new BizException("对应费用预算额度不足！", new HashMap());
                }
            } else {
                throw new BizException("目前没有分配对应费用预算！", new HashMap());
            }
            budgetId = (budgetMap.get("uuid")).toString();
            budgetCode = (budgetMap.get("subjectCode")).toString();
            budgetName = (budgetMap.get("financialSubject")).toString();
            mapInput.put("fadSubjectCode", budgetCode);
//            invoiceManager.checkBudgetIsEnough(isPrm,budgetId,expensesMoney);
        }
        mapInput.put("subjectId", budgetId);
        mapInput.put("subjectCode", budgetCode);
        mapInput.put("subjectName", budgetName);
        //发票登记生成黏贴单号
        String reqNo = NumberingHelper.getNumbering("FAD_INVOICE_REQ_NO", null);
        mapInput.put("invoiceReqNo", reqNo);
        mapInput.put("state", BillStateAttribute.FAD_BILL_STATE_NEW);
        mapInput.put("directPayment", 1);
//        if (mapInput.get("fadCashReqInvoiceDto") != null) {
//            List<Map> scmContractInvoiceList = (List) mapInput.get("fadCashReqInvoiceDto");
//            for (Map s : scmContractInvoiceList) {
//                s.put("clearanceType", FadCashReqInvoiceAttribute.CLEARANCE_TYPE_INVOICE);
//            }
//        }
        Map out = super.perform(inMap);
        out.put("invoiceReqNo", reqNo);
        return out;
    }
}
