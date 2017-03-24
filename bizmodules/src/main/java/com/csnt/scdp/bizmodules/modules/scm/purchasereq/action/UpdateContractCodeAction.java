package com.csnt.scdp.bizmodules.modules.scm.purchasereq.action;

import com.csnt.scdp.bizmodules.entity.prm.PrmPurchaseReqDetail;
import com.csnt.scdp.bizmodules.entity.scm.ScmContract;
import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.services.intf.PrmpurchasereqManager;
import com.csnt.scdp.bizmodules.modules.scm.scmcontract.dto.PrmPurchaseReqDetailDto;
import com.csnt.scdp.bizmodules.modules.scm.scmcontract.dto.ScmContractDto;
import com.csnt.scdp.framework.commonaction.crud.CommonModifyAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.ListUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Description:  ModifyAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 15:30:52
 */

@Scope("singleton")
@Controller("update-contract-code")
@Transactional
public class UpdateContractCodeAction extends CommonModifyAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(UpdateContractCodeAction.class);

    @Resource(name = "prmpurchasereq-manager")
    private PrmpurchasereqManager prmpurchasereqManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = new HashMap();
        String uuid = (String) inMap.get("uuid");
        String scmContractCode = (String) inMap.get("scmContractCode");

        ScmContractDto contract = (ScmContractDto) DtoHelper.findDtoByPK(ScmContractDto.class, uuid);
        contract.setScmContractCode(scmContractCode);
        List lstPrmPurchaseReqDetails = contract.getPrmPurchaseReqDetailDto();
        if (ListUtil.isNotEmpty(lstPrmPurchaseReqDetails)) {
            for (Iterator<PrmPurchaseReqDetailDto> it = lstPrmPurchaseReqDetails.iterator(); it.hasNext(); ) {
                PrmPurchaseReqDetailDto prmPurchaseReqDetailDto = it.next();
                prmPurchaseReqDetailDto.setScmContractCode(scmContractCode);
            }
        }
        List lstContract = new ArrayList();
        lstContract.add(contract);
        DtoHelper.batchUpdate(lstPrmPurchaseReqDetails, PrmPurchaseReqDetail.class);
        DtoHelper.batchUpdate(lstContract, ScmContract.class);
        return out;
    }
}
