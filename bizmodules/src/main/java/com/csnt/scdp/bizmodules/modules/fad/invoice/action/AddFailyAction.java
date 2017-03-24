package com.csnt.scdp.bizmodules.modules.fad.invoice.action;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.entity.fad.FadInvoice;
import com.csnt.scdp.bizmodules.entity.scm.ScmSupplier;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCashReqInvoiceAttribute;
import com.csnt.scdp.bizmodules.modules.fad.invoice.dto.FadInvoiceDto;
import com.csnt.scdp.bizmodules.modules.fad.invoice.services.intf.InvoiceManager;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.services.intf.SupplierinforManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.CommonAddAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.helper.NumberingHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
@Controller("failyinvoice-add")
@Transactional
public class AddFailyAction extends CommonAddAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(AddFailyAction.class);

    @Resource(name = "invoice-manager")
    private InvoiceManager invoiceManager;

    @Resource(name = "supplierinfor-manager")
    private SupplierinforManager supplierinforManager;

    public void validation(Map inMap) {

        String dtoClass = (String) inMap.get(CommonAttribute.DTO_CLASS_NAME);
        Map viewMap = (Map) inMap.get(CommonAttribute.VIEW_DATA);
        String mainTabName = StringUtil.capFirst(BeanFactory.getClass(dtoClass).getSimpleName(), false);
        Map mapInput = (Map) viewMap.get(mainTabName);
//        BigDecimal invoiceTotalValue = new BigDecimal((mapInput.get("invoiceTotalValue")).toString());
        if (mapInput != null) {
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

        boolean isPrm = false;
        String budgetId = mapInput.get("subjectId").toString();
        BigDecimal payCash = new BigDecimal(mapInput.get("payCash").toString());
        if (mapInput.get("prmProjectMainId") != null) {
            isPrm = true;
        } else {
            String fadYear = mapInput.get("fadYear").toString();
            int fadYearInt = Integer.parseInt(fadYear);
            Calendar a = Calendar.getInstance();
            int year = a.get(Calendar.YEAR);
            if (fadYearInt < year && payCash.compareTo(new BigDecimal(0.00)) == 1) {
                throw new BizException("不能使用往年预算，往年报销报现金额必须为0！", new HashMap());
            }
        }
//        BigDecimal expensesMoney =new BigDecimal(mapInput.get("expensesMoney").toString());
//        BigDecimal totalClearanceMoney =new BigDecimal(0.00);
        if (mapInput.get("fadCashReqInvoiceDto") != null) {
            List<Map> fadCashReqInvoiceList = (List) mapInput.get("fadCashReqInvoiceDto");
            for (Map s : fadCashReqInvoiceList) {
                s.put("clearanceType", FadCashReqInvoiceAttribute.CLEARANCE_TYPE_INVOICE);
//                totalClearanceMoney=totalClearanceMoney.add(new BigDecimal(s.get("clearanceMoney").toString()));
            }
        }
//        expensesMoney=expensesMoney.subtract(totalClearanceMoney);
        invoiceManager.checkBudgetIsEnough(isPrm, budgetId, payCash);

        //合同发票登记生成黏贴单号
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

    protected void beforeAction(BasePojo dtoObj) {
        FadInvoiceDto fadInvoiceDto = (FadInvoiceDto) dtoObj;
        //判断银行账户是否在供方库中存在，若存在判断收款单位与库中是否一致，不一致 则返回报错
        List<ScmSupplier> ScmSuppliers = supplierinforManager.getBankBelongedScmSupplier(fadInvoiceDto.getBankId());
        if (ListUtil.isNotEmpty(ScmSuppliers)) {
            List<ScmSupplier> tmp = ScmSuppliers.stream().filter(t -> t.getCompleteName().equals(fadInvoiceDto.getSupplierName())).collect(Collectors.toList());
            if (ListUtil.isEmpty(tmp)) {
                throw new BizException("该银行账户已在供应商：" + ScmSuppliers.get(0).getCompleteName() + " 使用，无法保存！");
            }
        }

        invoiceManager.isAddPackage(fadInvoiceDto.getSubjectId());
    }
}
