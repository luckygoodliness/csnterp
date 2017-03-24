package com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.action;

import com.csnt.scdp.bizmodules.attributes.PrmBudgetCodes;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmBudgetDetailCDto;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmContractDetailCDto;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmProjectMainCDto;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.services.intf.PrmprojectmaincManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MathUtil;
import com.csnt.scdp.framework.util.ObjectUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

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
@Controller("prmprojectmainc-validate-before-submit")
@Transactional
public class ValidationBeforeSubmit implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ValidationBeforeSubmit.class);

    @Resource(name = "prmprojectmainc-manager")
    private PrmprojectmaincManager prmprojectmaincManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = new HashMap();
        String uuid = (String) inMap.get(CommonAttribute.UUID);
        PrmProjectMainCDto prmProjectMainCDto = (PrmProjectMainCDto) DtoHelper.findDtoByPK(PrmProjectMainCDto.class, uuid);
        List lstMessage = new ArrayList<>();
        //check contract
        List<PrmContractDetailCDto> lstContractDetail = prmProjectMainCDto.getPrmContractDetailCDto();
        BigDecimal contractMoney = BigDecimal.ZERO;
        if (ListUtil.isNotEmpty(lstContractDetail)) {
            DoubleSummaryStatistics stats = lstContractDetail.stream().mapToDouble(x -> x.getContractNowMoney()
                    .doubleValue()).summaryStatistics();
            contractMoney = BigDecimal.valueOf(stats.getSum()).setScale(2,BigDecimal.ROUND_HALF_UP);
        }
        Optional<PrmBudgetDetailCDto> contractTotal = prmProjectMainCDto.getPrmBudgetDetailCDto()
                .stream().filter(t -> "CONTRACT_TOTAL".equals(t.getBudgetCode())).findAny();
        if (contractTotal.isPresent()) {
            PrmBudgetDetailCDto contractTotalDetail = contractTotal.get();
            if (MathUtil.compareTo(contractMoney, contractTotalDetail.getContractMoney()) != 0) {
                lstMessage.add("预算明细中的合同合计与项目合同的签约额不相同！");
            }
        } else {
            lstMessage.add("预算明细中无合同合计金额！");
        }

        out.put(CommonAttribute.DATAROOT, lstMessage);
        //Do After
        return out;
    }

}
