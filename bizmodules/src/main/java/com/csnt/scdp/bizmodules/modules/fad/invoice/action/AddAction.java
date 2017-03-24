package com.csnt.scdp.bizmodules.modules.fad.invoice.action;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.entity.scm.ScmSupplier;
import com.csnt.scdp.bizmodules.modules.fad.invoice.dto.FadInvoiceDto;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.services.intf.SupplierinforManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.*;
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
import com.csnt.scdp.bizmodules.modules.fad.invoice.services.intf.InvoiceManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
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
@Controller("invoice-add")
@Transactional
public class AddAction extends CommonAddAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);

    @Resource(name = "invoice-manager")
    private InvoiceManager invoiceManager;
    @Resource(name = "supplierinfor-manager")
    private SupplierinforManager supplierinforManager;

    public void validation(Map inMap) {

        String dtoClass = (String) inMap.get(CommonAttribute.DTO_CLASS_NAME);
        Map viewMap = (Map) inMap.get(CommonAttribute.VIEW_DATA);
        String mainTabName = StringUtil.capFirst(BeanFactory.getClass(dtoClass).getSimpleName(), false);
        Map mapInput = (Map) viewMap.get(mainTabName);
        BigDecimal invoiceTotalValue = new BigDecimal((mapInput.get("invoiceTotalValue")).toString());
        BigDecimal countInvoiceTotalValue = new BigDecimal(0.00);
        if (mapInput != null) {
            List<Map> scmContractInvoiceList = (List) mapInput.get("scmContractInvoiceDto");
            for (Map s : scmContractInvoiceList) {
                BigDecimal amount = new BigDecimal((s.get("amount")).toString());
                String scmContractId = s.get("scmContractId").toString();
                String str = invoiceManager.contractInvoiceCheckAdd(scmContractId, amount);
                if (str.equals("2")) {
                    throw new BizException("找不到合同！", new HashMap());
                } else if (!str.equals("1")) {
                    throw new BizException("合同" + str + "发票总额已经超过合同总额，请重新分配！", new HashMap());
                }
                countInvoiceTotalValue = countInvoiceTotalValue.add(amount);
            }
        }
        if (invoiceTotalValue.compareTo(countInvoiceTotalValue) == 0) {
            return;
        } else {
            throw new BizException("所分配金额与发票总金额不等，请重新分配！", new HashMap());
        }
    }

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        validation(inMap);//合同发票校验
        String dtoClass = (String) inMap.get(CommonAttribute.DTO_CLASS_NAME);
        Map viewMap = (Map) inMap.get(CommonAttribute.VIEW_DATA);
        String mainTabName = StringUtil.capFirst(BeanFactory.getClass(dtoClass).getSimpleName(), false);
        Map mapInput = (Map) viewMap.get(mainTabName);
        //合同发票登记生成黏贴单号
        String reqNo = NumberingHelper.getNumbering("FAD_INVOICE_REQ_NO", null);
        mapInput.put("invoiceReqNo", reqNo);
        mapInput.put("state", BillStateAttribute.FAD_BILL_STATE_NEW);
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
    }

    @Override
    // 存储过程核销
    protected void afterAction(BasePojo dtoObj) {
        String id = (((FadInvoiceDto) dtoObj).getUuid());
        invoiceManager.contractInvoiceWrittenOff("0", id);
    }
    //JAVA核销
//    protected void afterAction(BasePojo dtoObj) {
//        FadInvoiceDto dto  = (FadInvoiceDto) dtoObj;
//        invoiceManager.contractInvoiceClearance(dto);
//    }
}
