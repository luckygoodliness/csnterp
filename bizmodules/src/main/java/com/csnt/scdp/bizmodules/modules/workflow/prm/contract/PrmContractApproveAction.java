package com.csnt.scdp.bizmodules.modules.workflow.prm.contract;

import com.csnt.scdp.bizmodules.entity.prm.PrmContract;
import com.csnt.scdp.bizmodules.entity.prm.PrmContractC;
import com.csnt.scdp.bizmodules.entity.prm.PrmCustomer;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmContractAttribute;
import com.csnt.scdp.bizmodules.modules.prm.contract.dto.PrmContractDto;
import com.csnt.scdp.bizmodules.modules.prm.contractc.dto.PrmContractCDto;
import com.csnt.scdp.bizmodules.modules.prm.contractc.services.intf.ContractCManager;
import com.csnt.scdp.bizmodules.modules.prm.prmcustomer.services.intf.PrmcustomerManager;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
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
 * @timestamp 2015-09-27 15:28:19
 */

@Scope("singleton")
@Controller("prm_contract_management-after-complete")
public class PrmContractApproveAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(PrmContractApproveAction.class);

    @Resource(name = "contract-c-manager")
    private ContractCManager contractCManager;

    @Resource(name = "prmcustomer-manager")
    private PrmcustomerManager prmcustomerManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = new HashMap();
        String uuid = (String) inMap.get(WorkFlowAttribute.BUSINESS_KEY);
        PrmContractC prmContractC = PersistenceFactory.getInstance().findByPK(PrmContractC.class, uuid);

        String wfStatus = (String) inMap.get(WorkFlowAttribute.STATUS);
        if (WorkFlowAttribute.STATUS_FIXED.equals(wfStatus)) {
            contractCManager.invokeApproveAction(uuid);
            PrmContractCDto prmContractCDto = (PrmContractCDto) DtoHelper.findDtoByPK(PrmContractCDto.class, uuid);
            String prmContractId = prmContractCDto.getPrmContractId();
            if (StringUtil.isNotEmpty(prmContractId)) {
                if (Integer.valueOf(1).equals(prmContractCDto.getIsMajorProject())) {
                    contractCManager.sendMsg(prmContractId, prmContractCDto.getContractName());
                }
            }

            //若没有业主单位，审批通过，自动添加到客户管理，同时更新合同
            if (StringUtil.isNotEmpty(prmContractC.getCustomerId())) {
                PrmCustomer customer = PersistenceFactory.getInstance().findByPK(PrmCustomer.class, prmContractC.getCustomerId());
                if (customer == null) {
                    String customerName = prmContractC.getCustomerId();
                    String customerId = prmcustomerManager.createPrmcustomerByName(customerName);
                    prmContractC.setCustomerId(customerId);
                    //send message to 运管部经营主管
                    contractCManager.sendMsgForCustomer(prmContractC.getContractName(), customerId, customerName);
                }
            }
        }

        out.put(PrmContractAttribute.PRM_CONTRACT_C_DTO, PersistenceFactory.getInstance().findByPK
                (PrmContractC.class, uuid));
        //Do After
        return out;
    }

}
