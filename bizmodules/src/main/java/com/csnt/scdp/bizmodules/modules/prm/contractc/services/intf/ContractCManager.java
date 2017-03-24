package com.csnt.scdp.bizmodules.modules.prm.contractc.services.intf;

import com.csnt.scdp.bizmodules.entity.prm.PrmContractC;
import com.csnt.scdp.bizmodules.modules.prm.contractc.dto.PrmContractCDto;
import com.csnt.scdp.bizmodules.modules.prm.projectmain.dto.PrmProjectMainDto;

import java.util.List;
import java.util.Map;

/**
 * Description:  ContractManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 09:57:20
 */
public interface ContractCManager {

    PrmContractC findContractHeaderOnly(String uuid);

    /**
     * 字符串值处理
     *
     * @param value 字符串值
     * @return 字符串值为null时返回"",否则返回字符串值
     */
    String isNullReturnEmpty(Object value);

    void checkContractUnique(PrmContractCDto contractCDto);

    List<PrmContractC> getContractCbyContractIdandContractStatus(String contractId, String contractStatus);

    //返回该状态的合同变更或新增记录
    List<PrmContractC> getContractCbyContractIdandContractStatus(String contractUuid, String contractStatus, List<String> states);

    Map<String, List<PrmContractC>> getContractCbyMainDto(PrmProjectMainDto cdto);

    void loadExtraDescField(PrmContractCDto contractDto);

    void invokeApproveAction(String uuid);

    public void sendMsg(String uuid, String projectNamee);

    void sendMsgForCustomer(String contractName, String customerId, String customerName);

    public Map validatePrmContractCBeforeWorkFlowCommit(String uuid);
}