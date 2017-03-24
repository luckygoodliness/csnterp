package com.csnt.scdp.bizmodules.modules.fad.cashreq.action;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.entity.fad.FadCashReq;
import com.csnt.scdp.bizmodules.entity.scm.ScmSupplier;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCashReqAttribute;
import com.csnt.scdp.bizmodules.modules.fad.cashreq.dto.FadCashReqDto;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.services.intf.SupplierinforManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.helper.NumberingHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.fad.cashreq.services.intf.CashreqManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-22 09:40:50
 */

@Scope("singleton")
@Controller("cashreq-add")
@Transactional
public class AddAction extends CommonAddAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);

    @Resource(name = "cashreq-manager")
    private CashreqManager cashreqManager;

    @Resource(name = "supplierinfor-manager")
    private SupplierinforManager supplierinforManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        String dtoClass = (String) inMap.get(CommonAttribute.DTO_CLASS_NAME);
        Map viewMap = (Map) inMap.get(CommonAttribute.VIEW_DATA);

        String mainTabName = StringUtil.capFirst(BeanFactory.getClass(dtoClass).getSimpleName(), false);

        Map mapInput = (Map) viewMap.get(mainTabName);
        String reqNo = NumberingHelper.getNumbering("REQ_NO", null);
        mapInput.put("runningNo", reqNo);
        mapInput.put("state", BillStateAttribute.FAD_BILL_STATE_NEW);
        Map out = super.perform(inMap);
        //Do After
        out.put("runningNo", reqNo);
        return out;
    }

    protected void beforeAction(BasePojo dtoObj) {
        FadCashReqDto fadCashReqDto = (FadCashReqDto) dtoObj;
        if (FadCashReqAttribute.REQ_TYPE_VALUE_0.equals(fadCashReqDto.getReqType())) {
            if (StringUtil.isEmpty(fadCashReqDto.getPurchaseContractId())) {
                throw new BizException("该采购请款的合同编号为空，保存失败！请联系管理员！");
            } else {
                List<String> invoiceReqNos = cashreqManager.getUnFinishedFadInvoice(fadCashReqDto.getPurchaseContractId());
                if (invoiceReqNos.size() > 0) {
                    throw new BizException("该请款涉及的发票" + invoiceReqNos.get(0) + "未处理完！无法新增请款！");
                }
            }
        }
        //判断银行账户是否在供方库中存在，若存在判断收款单位与库中是否一致，不一致 则返回报错
        List<ScmSupplier> ScmSuppliers = supplierinforManager.getBankBelongedScmSupplier(fadCashReqDto.getPayeeAccount());
        if (ListUtil.isNotEmpty(ScmSuppliers)) {
            List<ScmSupplier> tmp = ScmSuppliers.stream().filter(t -> t.getCompleteName().equals(fadCashReqDto.getPayeeName())).collect(Collectors.toList());
            if (ListUtil.isEmpty(tmp)) {
                throw new BizException("该银行账户已在供应商：" + ScmSuppliers.get(0).getCompleteName() + " 使用，无法保存！");
            }
        }

        List<FadCashReq> cash = cashreqManager.getFadCashReqByBidInfoUuid(fadCashReqDto.getOperateBusinessBidInfoId());
        if (ListUtil.isNotEmpty(cash)) {
            String content = cash.stream().map(FadCashReq::getRunningNo).collect(Collectors.joining(","));
            throw new BizException("改投标信息收集已经与关联以下保证金已经关联：" + content + " ,无法再做请款！");
        }
    }
}
