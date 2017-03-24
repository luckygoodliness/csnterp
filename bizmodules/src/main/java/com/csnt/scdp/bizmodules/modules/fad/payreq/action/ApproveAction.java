package com.csnt.scdp.bizmodules.modules.fad.payreq.action;

/**
 * Created by fisher on 2015/11/9.
 */

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.attributes.ErpWorkFlowAttribute;
import com.csnt.scdp.bizmodules.entity.fad.FadPayReqC;
import com.csnt.scdp.bizmodules.entity.fad.FadPayReqH;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadPayReqCAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadPayReqHAttribute;
import com.csnt.scdp.bizmodules.modules.fad.payreq.dto.FadPayReqCDto;
import com.csnt.scdp.bizmodules.modules.fad.payreq.services.intf.PayreqManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Scope("singleton")
@Controller("payreq-approve")
@Transactional
public class ApproveAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);

    @Resource(name = "payreq-manager")
    private PayreqManager payreqManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map rsMap = new HashMap();
        String action = (String) inMap.get("action");
        String uuid = (String) inMap.get("uuid");
        List fadUUids = (List) inMap.get("fadUUids");
        if (StringUtil.isNotEmpty(action)) {
            FadPayReqH reqH = payreqManager.getFadPayReqHbyUuid(uuid);
            switch (action) {
                case "lock":
                    if (!BillStateAttribute.CMD_BILL_STATE_NEW.equals(reqH.getState())) {
                        rsMap.put("msg", "支付申请未处于新增状态，无法锁定！");
                    } else {
                        List<FadPayReqC> recList = payreqManager.getFadPayReqCbyPuuid(uuid);
                        //针对供应链数据，凡是处于待确认状态的数据，状态更新为已提交
                        recList.forEach(t -> {
                            if (BillStateAttribute.FAD_BILL_STATE_CHECKING.equals(t.getState()) && t.getDepartmentCode().equals(ErpWorkFlowAttribute.CSNT_GYLB)) {
                                t.setState(BillStateAttribute.FAD_BILL_STATE_SUBMITTED);
                                payreqManager.update(t);
                            }
                        });

                        recList.stream().filter(t -> !BillStateAttribute.FAD_BILL_STATE_SUBMITTED.equals(t.getState())).forEach(t -> {
                            t.setAuditMoney(BigDecimal.ZERO);
                            t.setPurchaseChiefMoney(BigDecimal.ZERO);
                            payreqManager.update(t);
                        });

                        Map<String, List<FadPayReqC>> fadPayReqCGroup = recList.stream().
                                filter(t -> BillStateAttribute.FAD_BILL_STATE_SUBMITTED.equals(t.getState())).collect(Collectors.groupingBy(FadPayReqC::getScmContractId));
                        Iterator iter = fadPayReqCGroup.entrySet().iterator();

                        while (iter.hasNext()) {
                            Map.Entry entry = (Map.Entry) iter.next();
                            List<FadPayReqC> val = (List<FadPayReqC>) entry.getValue();
                            if (val.size() > 0) {
                                Optional<FadPayReqC> reqc1 = val.stream().filter(t -> t.getPurchaseManagerMoney() != null).findFirst();
                                Optional<FadPayReqC> reqc2 = val.stream().filter(t -> t.getApproveMoney() != null).findFirst();
                                if (!reqc2.isPresent()) {
                                    reqc2 = val.stream().filter(t -> t.getReqMoney() != null).findFirst();
                                }
                                if (!reqc1.isPresent()) {
                                    FadPayReqC f = reqc2.get();
                                    val.forEach(t -> {
                                        if (!t.getUuid().equals(f.getUuid())) {
                                            t.setState(BillStateAttribute.FAD_BILL_STATE_DISABLED);
                                            t.setPurchaseChiefMoney(BigDecimal.ZERO);
                                            t.setAuditMoney(BigDecimal.ZERO);
                                            payreqManager.update(t);
                                        }
                                    });
                                } else {
                                    FadPayReqC fscm = reqc1.get();
                                    if (reqc2.isPresent()) {
                                        FadPayReqC f2 = reqc2.get();
                                        fscm.setReqMoney(f2.getReqMoney());
                                        fscm.setApproveMoney(f2.getApproveMoney());
                                        fscm.setScmContractExpectPaid(f2.getScmContractExpectPaid());
                                        payreqManager.update(fscm);
                                    }
                                    val.forEach(t -> {
                                        if (!t.getUuid().equals(fscm.getUuid())) {
                                            t.setState(BillStateAttribute.FAD_BILL_STATE_DISABLED);
                                            t.setPurchaseChiefMoney(BigDecimal.ZERO);
                                            t.setAuditMoney(BigDecimal.ZERO);
                                            payreqManager.update(t);
                                        }
                                    });
                                }
                            }
                        }
                        payreqManager.updateFadPayReqHBillState(uuid, BillStateAttribute.CMD_BILL_STATE_LOCKED);
                    }
                    break;
                case "check":
                    if (!BillStateAttribute.CMD_BILL_STATE_NEW.equals(reqH.getState())) {
                        rsMap.put("msg", "支付申请未处于新增状态，无法确认！");
                    } else {
                        payreqManager.getFadPayReqCbyUuids(fadUUids).forEach(t -> {
                            if (BillStateAttribute.FAD_BILL_STATE_CHECKING.equals(t.getState())) {
                                t.setState(BillStateAttribute.FAD_BILL_STATE_SUBMITTED);
                                payreqManager.update(t);
                            }
                        });
                    }
                    break;
                case "commit":
                    if (!BillStateAttribute.CMD_BILL_STATE_NEW.equals(reqH.getState())) {
                        rsMap.put("msg", "支付申请未处于新增状态，无法提交！");
                    } else {
                        payreqManager.getFadPayReqCbyUuids(fadUUids).forEach(t -> {
                            if (BillStateAttribute.FAD_BILL_STATE_NEW.equals(t.getState())) {
                                t.setState(BillStateAttribute.FAD_BILL_STATE_CHECKING);
                                payreqManager.update(t);
                            }
                        });
                    }
                    break;
                case "back":
                    if (!BillStateAttribute.CMD_BILL_STATE_NEW.equals(reqH.getState())) {
                        rsMap.put("msg", "支付申请未处于新增状态，无法撤回！");
                    } else {
                        payreqManager.getFadPayReqCbyUuids(fadUUids).forEach(t -> {
                            if (BillStateAttribute.FAD_BILL_STATE_CHECKING.equals(t.getState())) {
                                t.setState(BillStateAttribute.FAD_BILL_STATE_NEW);
                                payreqManager.update(t);
                            }
                        });
                    }
                    break;
                case "backDept":
                    if (!BillStateAttribute.CMD_BILL_STATE_NEW.equals(reqH.getState())) {
                        rsMap.put("msg", "支付申请未处于新增状态，无法退回部门！");
                    } else {
                        payreqManager.getFadPayReqCbyUuids(fadUUids).forEach(t -> {
                            if (BillStateAttribute.CMD_BILL_STATE_SUBMITTED.equals(t.getState())) {
                                t.setState(BillStateAttribute.FAD_BILL_STATE_CHECKING);
                                payreqManager.update(t);
                            }
                        });
                    }
                    break;
            }
        }
        return rsMap;
    }
}
