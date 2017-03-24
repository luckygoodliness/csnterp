package com.csnt.scdp.bizmodules.modules.fad.invoice.action;

import com.csnt.scdp.bizmodules.entityattributes.fad.FadInvoiceAttribute;
import com.csnt.scdp.bizmodules.modules.fad.invoice.dto.FadInvoiceDto;
import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
import com.csnt.scdp.bizmodules.modules.fad.invoice.services.intf.InvoiceManager;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
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
@Controller("invoice-batchworkflowcommit")
@Transactional
public class BatchWorkFlowCommitAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(BatchWorkFlowCommitAction.class);

    @Resource(name = "invoice-manager")
    private InvoiceManager invoiceManager;

    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map result = new HashMap<>();
        List fadInvoiceGridSelectionUuidList = (List) inMap.get("fadInvoiceGridSelectionUuidList");
        List fadInvoiceGridSelectionInvoiceReqNoList = (List) inMap.get("fadInvoiceGridSelectionInvoiceReqNoList");
        List fadInvoiceGridSelectionTblVersionList = (List) inMap.get("fadInvoiceGridSelectionTblVersionList");
        String errMessages = "";

        for (int i = 0; i < fadInvoiceGridSelectionUuidList.size(); i++) {
            String fadInvoiceUuid = certificateManager.isNullReturnEmpty(fadInvoiceGridSelectionUuidList.get(i));
            String invoiceReqNo = certificateManager.isNullReturnEmpty(fadInvoiceGridSelectionInvoiceReqNoList.get(i));
            String tblVersion = certificateManager.isNullReturnEmpty(fadInvoiceGridSelectionTblVersionList.get(i));
            certificateManager.CheckTimeStamp("FAD_INVOICE", "发票", fadInvoiceUuid, tblVersion);

            try {
                IAction action = BeanFactory.getBean("workflow-complete-action".toLowerCase());
                Map inMapForWorkFlow = new HashMap();
                inMapForWorkFlow.put(WorkFlowAttribute.BUSINESS_KEY, fadInvoiceUuid);
                inMapForWorkFlow.put(WorkFlowAttribute.WORKFLOW_DEFINITION_KEY, "Scm_Contract_Invoice");
                inMapForWorkFlow.put(WorkFlowAttribute.DTO, "com.csnt.scdp.bizmodules.modules.fad.invoice.dto.FadInvoiceDto");
                inMapForWorkFlow.put(WorkFlowAttribute.MENU_CODE, "SCMCONTRACTINVOICE");
                action.perform(inMapForWorkFlow);
            } catch (Exception e) {
                String errMessage = "单据【" + certificateManager.nvl(invoiceReqNo, fadInvoiceUuid) + "】" + e.getMessage();
                if (errMessage.length() > 0) {
                    if (errMessage.split("\n").length > 1) {
                        errMessage = certificateManager.isNullReturnEmpty(errMessage.split("\n")[0]) + certificateManager.isNullReturnEmpty(errMessage.split("\n")[1]);
                    }
                    errMessages = (errMessages.length() > 0) ? errMessages + "\n\n" + errMessage : "\n\n" + errMessage;
                }
            }
        }
        result.put("errorMsg", errMessages);
        return result;
    }
}
