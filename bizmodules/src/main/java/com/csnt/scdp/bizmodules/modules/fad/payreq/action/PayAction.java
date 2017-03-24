package com.csnt.scdp.bizmodules.modules.fad.payreq.action;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.attributes.OriginalFormTypeAttribute;
import com.csnt.scdp.bizmodules.entity.fad.FadPayReqC;
import com.csnt.scdp.bizmodules.entity.fad.FadPayReqH;
import com.csnt.scdp.bizmodules.modules.fad.cashreq.services.intf.CashreqManager;
import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
import com.csnt.scdp.bizmodules.modules.fad.payreq.dto.FadPayReqCDto;
import com.csnt.scdp.bizmodules.modules.fad.payreq.services.intf.PayreqManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
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
@Controller("payreq-pay")
@Transactional
public class PayAction implements IAction {

    private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);

    @Resource(name = "payreq-manager")
    private PayreqManager payreqManager;

    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Resource(name = "cashreq-manager")
    private CashreqManager cashreqManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map rsMap = new HashMap();
        String uuid = (String) inMap.get("uuid");
        String reqRelated = (String) inMap.get("reqRelated");
        FadPayReqH fadPayReqH = payreqManager.getFadPayReqHbyUuid(uuid);
        if (fadPayReqH != null && BillStateAttribute.CMD_BILL_STATE_APPROVED.equals(fadPayReqH.getState())) {
            List payData = (ArrayList) inMap.get("payData");
            List<FadPayReqC> lstPayReqC = payreqManager.getFadPayReqCbyUuids(payData);
            if (ListUtil.isNotEmpty(lstPayReqC)) {
                List<FadPayReqCDto> lstPayReqCDto = new ArrayList<FadPayReqCDto>();
                lstPayReqC.forEach(t -> {
                    if (BillStateAttribute.FAD_BILL_STATE_APPROVED.equals(t.getState())) {
                        FadPayReqCDto cDto = new FadPayReqCDto();
                        BeanUtil.bean2Bean(t, cDto);
                        lstPayReqCDto.add(cDto);
                    }
                });
                payreqManager.setObjectPlusInfo(lstPayReqCDto);
                //遍历进行采购费用申请
                if ("yes".equals(reqRelated)) {
                    lstPayReqCDto.forEach(t -> {
                        cashreqManager.generateScmCashreqByPayReqCDto(t);
                    });
                }
                //遍历进行进行支付操作，生成凭证
                lstPayReqCDto.forEach(t -> {
                            if (BillStateAttribute.FAD_BILL_STATE_APPROVED.equals(t.getState()) && t.getAuditMoney() != null) {
                                BigDecimal cashReqValue = t.getCashReqValue() == null ? BigDecimal.ZERO : t.getCashReqValue();//本次请款金额

                                BigDecimal canPayMoney = t.getScmContractNeedToPay();//应付账款
                                BigDecimal needPayMoney = t.getAuditMoney().subtract(cashReqValue);//本次还需支付金额

                                FadPayReqC req = cashreqManager.getPayReqCByUuid(t.getUuid());

                                if (needPayMoney.compareTo(canPayMoney) <= 0) {
                                    req.setAccountsPayable(needPayMoney);
                                    payreqManager.update(req);
                                } else {
                                    req.setAccountsPayable(canPayMoney);
                                    payreqManager.update(req);
                                }
                                if (req.getAccountsPayable() != null && req.getAccountsPayable().compareTo(BigDecimal.ZERO) > 0) {
                                    String fadCertificateUuid = certificateManager.javaBeanToCertificate(t.getUuid(), OriginalFormTypeAttribute.FAD_PAY_REQ_C);
                                    rsMap.put("fadCertificateUuid", fadCertificateUuid);

                                    req.setState(BillStateAttribute.FAD_BILL_STATE_CERTIFICATED);
                                    req.setCertificateCreateTime(new Date());
                                    payreqManager.update(req);
                                }
                            }
                        }
                );
            }
        }
        return rsMap;
    }
}
