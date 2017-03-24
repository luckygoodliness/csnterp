package com.csnt.scdp.bizmodules.modules.workflow.scm.contractinvoice;

import com.csnt.scdp.bizmodules.modules.fad.invoice.dto.FadInvoiceDto;
import com.csnt.scdp.bizmodules.modules.fad.invoice.dto.ScmContractInvoiceDto;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.services.intf.SupplierinforManager;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("scm_contract_invoice-after-fixed")
@Transactional

public class ScmContractInvoiceAfterCompleteAction extends ERPWorkFlowBaseVariableCollectionAction {
    @Resource(name = "supplierinfor-manager")
    private SupplierinforManager supplierinforManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapResult = new HashMap();

        String uuid = (String) inMap.get("businessKey");
        FadInvoiceDto fadInvoiceDto = (FadInvoiceDto) DtoHelper.findDtoByPK(FadInvoiceDto.class, uuid);
        String newUuid = supplierinforManager.generateSupplierByFlag(fadInvoiceDto.getSupplierName(), fadInvoiceDto.getBank(), fadInvoiceDto.getBankId(), "2");//
        if (StringUtil.isNotEmpty(newUuid) && StringUtil.isEmpty(fadInvoiceDto.getSupplierId())) {
            fadInvoiceDto.setSupplierId(newUuid);
        }
        if (ListUtil.isNotEmpty(fadInvoiceDto.getScmContractInvoiceDto())) {
            List<ScmContractInvoiceDto> list = fadInvoiceDto.getScmContractInvoiceDto();
            String supplierId = fadInvoiceDto.getSupplierId();
            String supplierName = fadInvoiceDto.getSupplierName();
            String scmContractId = "";
            for (ScmContractInvoiceDto dto : list) {
                scmContractId = dto.getScmContractId();
                //更新供应商为空的采购合同
                String sql = "UPDATE SCM_CONTRACT S SET S.SUPPLIER_CODE = '" + supplierId + "',S.SUPPLIER_NAME = '" + supplierName + "' WHERE S.SUPPLIER_CODE IS NULL AND S.UUID = '" + scmContractId + "'";
                PersistenceCrudManager pcm = PersistenceFactory.getInstance();
                DAOMeta daoMeta = new DAOMeta(sql);
                pcm.updateByNativeSQL(daoMeta);
            }
        }
        return mapResult;
    }
}
