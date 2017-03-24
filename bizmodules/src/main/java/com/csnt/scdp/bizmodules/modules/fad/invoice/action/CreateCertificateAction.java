package com.csnt.scdp.bizmodules.modules.fad.invoice.action;

import com.csnt.scdp.bizmodules.attributes.OriginalFormTypeAttribute;
import com.csnt.scdp.bizmodules.entity.fad.FadInvoice;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadInvoiceAttribute;
import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
import com.csnt.scdp.bizmodules.modules.fad.invoice.services.intf.InvoiceManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 15:32:42
 */

@Scope("singleton")
@Controller("invoice-createcertificate")
@Transactional
public class CreateCertificateAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(CreateCertificateAction.class);

    @Resource(name = "invoice-manager")
    private InvoiceManager invoiceManager;

    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map rsMap = new HashMap();
        String uuid = (String) inMap.get("uuid");
        String expensesType = (String) inMap.get("expensesType");
        if ("0".equals(expensesType)) {
            invoiceManager.checkForCreateCertificate(uuid);//校验是否存在对应采购请款未发送NC,存在刚不能发送
            invoiceManager.contractInvoiceWrittenOff("0", uuid);//采购发票生成凭证之前重新生成核销数据
        }
        String fadCertificateUuid = certificateManager.javaBeanToCertificate(uuid, OriginalFormTypeAttribute.FAD_INVOICE);
        if (StringUtil.isEmpty(fadCertificateUuid)) {
            throw new BizException("凭证生成失败");
        } else {
            rsMap.put("fadCertificateUuid", fadCertificateUuid);
        }
        invoiceManager.changeStateToCertificated(uuid);
        return rsMap;
    }
}
