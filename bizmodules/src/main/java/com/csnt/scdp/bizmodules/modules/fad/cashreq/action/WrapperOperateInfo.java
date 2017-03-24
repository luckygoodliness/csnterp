package com.csnt.scdp.bizmodules.modules.fad.cashreq.action;

import com.csnt.scdp.bizmodules.attributes.CommonAttribute;
import com.csnt.scdp.bizmodules.attributes.MessageKeyAttribute;
import com.csnt.scdp.bizmodules.entity.fad.FadCashReq;
import com.csnt.scdp.bizmodules.entity.operate.OperateBusinessBidInfo;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCashReqAttribute;
import com.csnt.scdp.bizmodules.entityattributes.operate.OperateBusinessBidInfoAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmContractAttribute;
import com.csnt.scdp.bizmodules.modules.fad.cashreq.services.intf.CashreqManager;
import com.csnt.scdp.bizmodules.modules.prm.contractc.services.intf.ContractCManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2016/6/30.
 */
@Scope("singleton")
@Controller("fad-deposit-wrapper-operate-info")
@Transactional
public class WrapperOperateInfo implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(WrapperOperateInfo.class);
    @Resource(name = "cashreq-manager")
    private CashreqManager cashreqManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        String uuid = (String) inMap.get(CommonAttribute.UUID);
        String operateBusinessBidInfoId = (String) inMap.get(FadCashReqAttribute.OPERATE_BUSINESS_BID_INFO_ID);
        OperateBusinessBidInfo bidInfo = PersistenceFactory.getInstance().findByPK(OperateBusinessBidInfo.class, operateBusinessBidInfoId);
        String msg = beforeAction(uuid, operateBusinessBidInfoId);
        Map out = new HashMap<>();
        out.put("errorInfo", msg);
        out.put(FadCashReqAttribute.OPERATE_BUSINESS_BID_INFO, bidInfo);
        return out;
    }

    private String beforeAction(String uuid, String bidInfoUuid) {
        //检查是否存在未作废的投标信息的保证金
        String rtn = "";
        List<FadCashReq> cash = cashreqManager.getFadCashReqByBidInfoUuid(bidInfoUuid).stream().filter(t -> !t.getUuid().equals(uuid)).collect(Collectors.toList());
        if (ListUtil.isNotEmpty(cash)) {
            String content = cash.stream().map(FadCashReq::getRunningNo).collect(Collectors.joining(","));
            rtn = "改投标信息收集已经与关联以下保证金已经关联：" + content + " ,无法再做请款！";
        }
        return rtn;
    }
}
