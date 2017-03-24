package com.csnt.scdp.bizmodules.modules.fad.cashreq.action;

import com.csnt.scdp.bizmodules.entity.scm.ScmSupplier;
import com.csnt.scdp.bizmodules.modules.fad.cashreq.dto.FadCashReqDto;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.services.intf.SupplierinforManager;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.ListUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.fad.cashreq.services.intf.CashreqManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:  ModifyAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-22 09:40:50
 */

@Scope("singleton")
@Controller("cashreq-modify")
@Transactional
public class ModifyAction extends CommonModifyAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ModifyAction.class);

    @Resource(name = "cashreq-manager")
    private CashreqManager cashreqManager;

    @Resource(name = "supplierinfor-manager")
    private SupplierinforManager supplierinforManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        //Do After
        return out;
    }

    protected void beforeAction(BasePojo dtoObj) {
        FadCashReqDto fadCashReqDto = (FadCashReqDto) dtoObj;
        //判断银行账户是否在供方库中存在，若存在判断收款单位与库中是否一致，不一致 则返回报错
        List<ScmSupplier> ScmSuppliers = supplierinforManager.getBankBelongedScmSupplier(fadCashReqDto.getPayeeAccount());
        if (ListUtil.isNotEmpty(ScmSuppliers)) {
            List<ScmSupplier> tmp = ScmSuppliers.stream().filter(t -> t.getCompleteName().equals(fadCashReqDto.getPayeeName())).collect(Collectors.toList());
            if (ListUtil.isEmpty(tmp)) {
                throw new BizException("该银行账户已在供应商：" + ScmSuppliers.get(0).getCompleteName() + " 使用，无法保存！");
            }
        }
    }
}
