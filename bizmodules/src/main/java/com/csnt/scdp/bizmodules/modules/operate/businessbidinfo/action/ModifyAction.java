package com.csnt.scdp.bizmodules.modules.operate.businessbidinfo.action;

import com.csnt.scdp.bizmodules.entity.prm.PrmContractC;
import com.csnt.scdp.bizmodules.entityattributes.operate.OperateBusinessBidInfoAttribute;
import com.csnt.scdp.bizmodules.modules.operate.businessbidinfo.dto.OperateBusinessBidInfoDto;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.sysmodules.entityattributes.ScdpMsgAttribute;
import com.csnt.scdp.sysmodules.helper.MsgSendHelper;
import com.csnt.scdp.sysmodules.modules.sys.msgcenter.dto.ReceiptsMeta;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.operate.businessbidinfo.services.intf.BusinessbidinfoManager;

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
 * @timestamp 2015-09-28 18:18:49
 */

@Scope("singleton")
@Controller("businessbidinfo-modify")
@Transactional
public class ModifyAction extends CommonModifyAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ModifyAction.class);

    @Resource(name = "businessbidinfo-manager")
    private BusinessbidinfoManager businessbidinfoManager;

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
        checkFields.put(OperateBusinessBidInfoAttribute.PROJECT_NAME, operateBusinessBidInfo.getProjectName());
        if (!businessbidinfoManager.validateUniqueField(operateBusinessBidInfo, checkFields, false)) {
            throw new BizException("数据校验失败！[项目名称] 已存在！");
        }

        List<PrmContractC> prmContractcs = businessbidinfoManager.findContractbyBidInfoId(operateBusinessBidInfo.getUuid());
        if (ListUtil.isNotEmpty(prmContractcs)) {
            //已关联合同新增， 投标结果只能是排名第一、排名第二、排名第三
            String[] result = {"1", "2", "3"};
            if (Arrays.binarySearch(result, operateBusinessBidInfo.getBidResult()) < 0) {
                throw new BizException("该投标信息已在合同新增中被引用，投标结果修改错误!");
            }
            //已关联处理过的合同新增，不能修改投标信息
            String[] validStates = {"1", "2", "9"};
            Long validCount = prmContractcs.stream().filter(t ->
                            Arrays.binarySearch(validStates, t.getState()) >= 0
            ).count();
            if (validCount > 0) {
                throw new BizException("该投标信息已在合同新增中被引用，且新增合同已生效或在流程中，无法修改!");
            }
        }
    }

    @Override
    protected void afterAction(BasePojo dtoObj) {
        businessbidinfoManager.sendMessage(dtoObj);
        businessbidinfoManager.forwardChangePrmContractCFromBidInfoDto((OperateBusinessBidInfoDto) dtoObj);
    }
}
