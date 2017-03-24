package com.csnt.scdp.bizmodules.modules.prm.contractc.action;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.entity.prm.PrmContractC;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmContractAttribute;
import com.csnt.scdp.bizmodules.modules.prm.contractc.dto.PrmContractCDto;
import com.csnt.scdp.bizmodules.modules.prm.contractc.services.intf.ContractCManager;
import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.services.intf.PrmpurchasereqManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.CommonAddAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.ObjectUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 09:57:20
 */

@Scope("singleton")
@Controller("contract-c-add")
@Transactional
public class AddAction extends CommonAddAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);

    @Resource(name = "contract-c-manager")
    private ContractCManager contractCManager;


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        //Do After
        return out;
    }

    @Override
    protected void beforeAction(BasePojo dtoObj) {
        PrmContractCDto dto = (PrmContractCDto) dtoObj;
        if (dto == null) {
            return;
        }
        String contractStatus = dto.getContractStatus();
        if (PrmContractAttribute.CONTRACT_STATUS_NEW.equals(contractStatus)) {
            if (StringUtil.isEmpty(dto.getProjectSourceType())) {
                throw new BizException("项目来源不能为空！");
            }

            contractCManager.checkContractUnique(dto);
            String innerPurchaseReqId = dto.getInnerPurchaseReqId();
            if (StringUtil.isNotEmpty(innerPurchaseReqId)) {
                BigDecimal contractMoney = dto.getContractSignMoney();
                PrmpurchasereqManager purchaseManager = BeanFactory.getBean("prmpurchasereq-manager");
                BigDecimal reqAmount = purchaseManager.getTotalAmountByReqId(innerPurchaseReqId);
                if (contractMoney.compareTo(reqAmount) > 0) {
                    throw new BizException("内委合同金额不能大于内委申请额！");
                }
            }
        } else if (PrmContractAttribute.CONTRACT_STATUS_REVISE.equals(contractStatus)) {
            String contractId = dto.getPrmContractId();
            if (StringUtil.isNotEmpty(contractId)) {
                //判断是否有工作流中的变更记录
                List<String> states = new ArrayList<String>();
                states.add(BillStateAttribute.FAD_BILL_STATE_NEW);
                states.add(BillStateAttribute.FAD_BILL_STATE_SUBMITTED);
                states.add(BillStateAttribute.FAD_BILL_STATE_SUBMITTED_REJECTED);
                List<PrmContractC> rtn = contractCManager.getContractCbyContractIdandContractStatus(contractId, PrmContractAttribute.CONTRACT_STATUS_REVISE, states);
                if (ListUtil.isNotEmpty(rtn)) {
                    throw new BizException("存在新增或者审批中的合同变更数据，无法新增！");
                }
            }
        }
    }
}
