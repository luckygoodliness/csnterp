package com.csnt.scdp.bizmodules.modules.fad.invoice.action;

import com.csnt.scdp.bizmodules.entityattributes.fad.FadInvoiceAttribute;
import com.csnt.scdp.bizmodules.modules.fad.invoice.dto.FadInvoiceDto;
import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
import com.csnt.scdp.bizmodules.modules.fad.invoice.services.intf.InvoiceManager;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.framework.util.UUIDUtil;
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
@Controller("invoice-getmaxtaxrate")
@Transactional
public class GetMaxTaxRateAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(GetMaxTaxRateAction.class);

    @Resource(name = "invoice-manager")
    private InvoiceManager invoiceManager;

    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map result = new HashMap<>();
        List selectedRecordsSelectionUuidList = (List) inMap.get("selectedRecordsSelectionUuidList");

        String maxTaxRate = "";
        String maxTaxRateDesc = "";
        for (int i = 0; i < selectedRecordsSelectionUuidList.size(); i++) {
            String scmContractUuid = certificateManager.isNullReturnEmpty(selectedRecordsSelectionUuidList.get(i));
            String sql = "SELECT\n" +
                    "NVL(GET_DESCP_TAX(PRM_PROJECT_MAIN.PRM_CODE_TYPE,FAD_BASE_SUBJECT.DESCP), 0) / 100 TAX_RATE,\n" +
                    "(SELECT CODE_DESC FROM SCDP_CODE SC WHERE SC.CODE_TYPE = 'FAD_TAX_RATE' AND SC.SYS_CODE = NVL(GET_DESCP_TAX(PRM_PROJECT_MAIN.PRM_CODE_TYPE,FAD_BASE_SUBJECT.DESCP), 0) / 100 AND SC.CODE_DESC <> '免税') AS TAX_RATE_DESC\n" +
                    "FROM SCM_CONTRACT,PRM_PROJECT_MAIN,FAD_BASE_SUBJECT\n" +
                    "WHERE\n" +
                    "SCM_CONTRACT.PROJECT_ID = PRM_PROJECT_MAIN.UUID\n" +
                    "AND\n" +
                    "SCM_CONTRACT.SUBJECT_CODE = FAD_BASE_SUBJECT.SUBJECT_NO\n" +
                    "AND\n" +
                    "SCM_CONTRACT.UUID = '" + scmContractUuid + "'";

            DAOMeta daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            List projectTaxRateResult = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            if (projectTaxRateResult.size() > 0 && Double.valueOf(certificateManager.isNullReturnEmpty(((Map) projectTaxRateResult.get(0)).get("taxRate"))) > Double.valueOf(certificateManager.nvl(maxTaxRate, "0"))) {
                maxTaxRate = certificateManager.isNullReturnEmpty(((Map) projectTaxRateResult.get(0)).get("taxRate"));
                maxTaxRateDesc = certificateManager.isNullReturnEmpty(((Map) projectTaxRateResult.get(0)).get("taxRateDesc"));
            }
        }
        result.put("maxTaxRate", maxTaxRate);
        result.put("maxTaxRateDesc", maxTaxRateDesc);
        return result;
    }
}
