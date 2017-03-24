package com.csnt.scdp.bizmodules.modules.operate.businessbidinfo.services.intf;

import com.csnt.scdp.bizmodules.entity.fad.FadCashReq;
import com.csnt.scdp.bizmodules.entity.operate.OperateBusinessBidInfo;
import com.csnt.scdp.bizmodules.entity.prm.PrmContractC;
import com.csnt.scdp.bizmodules.modules.operate.businessbidinfo.dto.OperateBusinessBidInfoDto;
import com.csnt.scdp.framework.dto.BasePojo;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Description:  BusinessbidinfoManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-28 18:18:49
 */
public interface BusinessbidinfoManager {

    OperateBusinessBidInfo findByUuid(String uuid);

    /**
     * 根据projectId查找projectName
     *
     * @param projectId 项目id
     * @return 此id对应的对象
     */
    public List<FadCashReq> getObjByProjectId(String projectId);

    public void sendMessage(BasePojo bp);

    public List<PrmContractC> findContractbyBidInfoId(String infoId);

    public void forwardChangePrmContractCFromBidInfoDto(OperateBusinessBidInfoDto infoId);

    public List<PrmContractC> findContractbyPrmContractId(String infoId);

    //取新增未处理完的招标信息收集
    public List<OperateBusinessBidInfo> getUnhandledInfoes();

    public void loadExtraDescField(OperateBusinessBidInfoDto dto);

    boolean validateUniqueField(OperateBusinessBidInfoDto dto, Map<String, Object> CheckFields, boolean unchangePass);
}