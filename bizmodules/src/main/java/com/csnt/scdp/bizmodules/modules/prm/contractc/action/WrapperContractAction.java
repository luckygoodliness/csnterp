package com.csnt.scdp.bizmodules.modules.prm.contractc.action;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmContractAttribute;
import com.csnt.scdp.bizmodules.modules.prm.contract.dto.PrmContractDto;
import com.csnt.scdp.bizmodules.modules.prm.contractc.dto.PrmContractCDto;
import com.csnt.scdp.bizmodules.modules.prm.contractc.services.intf.ContractCManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.BeanUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:  LoadAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 09:57:20
 */

@Scope("singleton")
@Controller("wrapper-contract-to-revise")
@Transactional
public class WrapperContractAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(WrapperContractAction.class);

    @Resource(name = "contract-c-manager")
    private ContractCManager contractManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        String uuid = (String) inMap.get(CommonAttribute.UUID);

        PrmContractDto contractDto = (PrmContractDto) DtoHelper.findDtoByPK(PrmContractDto.class, uuid);
        PrmContractCDto contractCDto = new PrmContractCDto();
        if (contractCDto != null) {
            BeanUtil.bean2Bean(contractDto, contractCDto);
            contractCDto.setPrmContractId(uuid);
            contractCDto.setContractStatus(PrmContractAttribute.CONTRACT_STATUS_REVISE);
            contractCDto.setContractLastMoney(contractDto.getContractNowMoney());
            contractCDto.setState(BillStateAttribute.CMD_BILL_STATE_NEW);
            contractManager.loadExtraDescField(contractCDto);
        }
        Map out = new HashMap<>();
        out.put(PrmContractAttribute.PRM_CONTRACT_C_DTO, contractCDto);
        return out;
    }
}
