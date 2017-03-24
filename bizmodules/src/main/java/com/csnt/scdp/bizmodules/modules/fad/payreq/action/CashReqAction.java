package com.csnt.scdp.bizmodules.modules.fad.payreq.action;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.entity.fad.FadPayReqC;
import com.csnt.scdp.bizmodules.entity.fad.FadPayReqH;
import com.csnt.scdp.bizmodules.entity.scm.ScmContract;
import com.csnt.scdp.bizmodules.modules.fad.cashreq.services.intf.CashreqManager;
import com.csnt.scdp.bizmodules.modules.fad.payreq.dto.FadPayReqCDto;
import com.csnt.scdp.bizmodules.modules.fad.payreq.services.intf.PayreqManager;
import com.csnt.scdp.bizmodules.modules.scm.scmcontract.services.intf.ScmcontractManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.BeanUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by fisher on 2015/11/9.
 */
@Scope("singleton")
@Controller("payreq-cashreq")
@Transactional
public class CashReqAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);

    @Resource(name = "payreq-manager")
    private PayreqManager payreqManager;

    @Resource(name = "cashreq-manager")
    private CashreqManager cashreqManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map rsMap = new HashMap();
        String uuid = (String) inMap.get("uuid");
        FadPayReqH fadPayReqH = payreqManager.getFadPayReqHbyUuid(uuid);
        if (fadPayReqH != null && BillStateAttribute.CMD_BILL_STATE_APPROVED.equals(fadPayReqH.getState())) {
            List reqData = (ArrayList) inMap.get("reqData");
            List<FadPayReqC> lstPayReqC = payreqManager.getFadPayReqCbyUuids(reqData);

            List<FadPayReqCDto> lstPayReqCDto = new ArrayList<FadPayReqCDto>();
            lstPayReqC.forEach(t -> {
                if (Arrays.binarySearch(BillStateAttribute.getFadBillAuditState(), t.getState()) >= 0) {
                    FadPayReqCDto cDto = new FadPayReqCDto();
                    BeanUtil.bean2Bean(t, cDto);
                    lstPayReqCDto.add(cDto);
                }
            });

            payreqManager.setObjectPlusInfo(lstPayReqCDto);
            lstPayReqCDto.forEach(t ->
            {
                cashreqManager.generateScmCashreqByPayReqCDto(t);
            });
        }
        return rsMap;
    }
}
