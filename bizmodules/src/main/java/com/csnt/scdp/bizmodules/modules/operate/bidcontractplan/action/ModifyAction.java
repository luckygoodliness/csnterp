package com.csnt.scdp.bizmodules.modules.operate.bidcontractplan.action;

import com.csnt.scdp.bizmodules.entity.operate.OperateBusinessBidInfo;
import com.csnt.scdp.bizmodules.entity.prm.PrmContractC;
import com.csnt.scdp.bizmodules.entityattributes.operate.OperateBusinessBidInfoAttribute;
import com.csnt.scdp.bizmodules.modules.operate.bidcontractplan.services.intf.BidcontractplanManager;
import com.csnt.scdp.bizmodules.modules.operate.businessbidinfo.dto.OperateBusinessBidInfoDto;
import com.csnt.scdp.bizmodules.modules.operate.businessbidinfo.services.intf.BusinessbidinfoManager;
import com.csnt.scdp.bizmodules.util.UseInMap;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.CommonModifyAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entityattributes.ScdpMsgAttribute;
import com.csnt.scdp.sysmodules.helper.MsgSendHelper;
import com.csnt.scdp.sysmodules.modules.sys.msgcenter.dto.ReceiptsMeta;
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
 * @timestamp 2015-11-05 10:26:28
 */

@Scope("singleton")
@Controller("bidcontractplan-modify")
@Transactional
public class ModifyAction extends CommonModifyAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ModifyAction.class);

    @Resource(name = "businessbidinfo-manager")
    private BusinessbidinfoManager businessbidinfoManager;
    @Resource(name = "bidcontractplan-manager")
    private BidcontractplanManager bidcontractplanManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        //Do After
        return out;
    }

    @Override
    protected void beforeAction(BasePojo dtoObj) {
        OperateBusinessBidInfoDto operateBusinessBidInfo = (OperateBusinessBidInfoDto) dtoObj;

        Map<String, Object> checkFields = new HashMap<String, Object>();
        checkFields.put(OperateBusinessBidInfoAttribute.CONTRACT_NAME, operateBusinessBidInfo.getContractName());
        if (!businessbidinfoManager.validateUniqueField(operateBusinessBidInfo, checkFields, false)) {
            throw new BizException("数据校验失败！[合同名称] 已存在！");
        }

        List<PrmContractC> prmContractcs = businessbidinfoManager.findContractbyBidInfoId(operateBusinessBidInfo.getUuid());
        if (ListUtil.isNotEmpty(prmContractcs)) {
            //已关联处理过的合同新增，不能修改合同协议草案
            String[] validStates = {"1", "2", "9"};
            Long validCount = prmContractcs.stream().filter(t ->
                            Arrays.binarySearch(validStates, t.getState()) >= 0
            ).count();
            if (validCount > 0) {
                throw new BizException("该合同协议草案已在合同新增中被引用，且新增合同已生效或在流程中，无法修改!");
            }
        }
    }

    @Override
    protected void afterAction(BasePojo dtoObj) {
        bidcontractplanManager.sendMessage(dtoObj);
        businessbidinfoManager.forwardChangePrmContractCFromBidInfoDto((OperateBusinessBidInfoDto) dtoObj);
    }
}
