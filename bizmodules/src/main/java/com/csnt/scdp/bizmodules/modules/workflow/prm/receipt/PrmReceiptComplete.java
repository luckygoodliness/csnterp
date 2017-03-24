package com.csnt.scdp.bizmodules.modules.workflow.prm.receipt;

import com.csnt.scdp.bizmodules.entity.prm.PrmReceipts;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.prm.receipts.dto.PrmReceiptsDto;
import com.csnt.scdp.bizmodules.modules.prm.receipts.dto.PrmReceiptsScmInvoiceDto;
import com.csnt.scdp.bizmodules.modules.scm.balanceofcontract.dto.ScmContractDto;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("prm_receipt-complete")
@Transactional

public class PrmReceiptComplete extends PrmReceiptStart {

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapVar = super.perform(inMap);
        String businessKey = (String) inMap.get(WorkFlowAttribute.BUSINESS_KEY);
        PrmReceipts pr = PersistenceFactory.getInstance().findByPK(PrmReceipts.class, businessKey);
        mapVar.put("moneyType", pr.getMoneyType());
        //通过项目收款主表ID去找子表的合同ID，通过合同ID去找合同归属部门
        PrmReceiptsDto prmReceiptsDto = (PrmReceiptsDto)DtoHelper.findDtoByPK(PrmReceiptsDto.class, businessKey);
        List<PrmReceiptsScmInvoiceDto> prmReceiptsScmInvoiceDtoList = prmReceiptsDto.getPrmReceiptsScmInvoiceDto();
        List assigneeList = new ArrayList<>();
        if(ListUtil.isNotEmpty(prmReceiptsScmInvoiceDtoList)) {
            for(PrmReceiptsScmInvoiceDto prmReceiptsScmInvoiceDto : prmReceiptsScmInvoiceDtoList) {
                ScmContractDto scmContractDto = (ScmContractDto)DtoHelper.findDtoByPK(ScmContractDto.class, prmReceiptsScmInvoiceDto.getScmContractUuid());
                String officeId = scmContractDto.getOfficeId();
                if (StringUtil.isNotEmpty(officeId)) {
                    String orgUuid = OrgHelper.getOrgIdByCode(officeId);
                    Map userMap = ErpWorkFlowHelper.loadBusinessDivisionInfo(orgUuid, officeId);
                    assigneeList.add(userMap.get("事业部总经理"));
                }
            }
        }

        mapVar.put("assigneeList",assigneeList);
        return mapVar;
    }
}
