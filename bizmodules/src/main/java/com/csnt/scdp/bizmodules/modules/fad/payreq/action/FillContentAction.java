package com.csnt.scdp.bizmodules.modules.fad.payreq.action;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.entity.fad.FadPayReqH;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadPayReqHAttribute;
import com.csnt.scdp.bizmodules.modules.common.intf.CommonServiceManager;
import com.csnt.scdp.bizmodules.modules.fad.payreq.dto.FadPayReqCDto;
import com.csnt.scdp.bizmodules.modules.fad.payreq.dto.FadPayReqHDto;
import com.csnt.scdp.bizmodules.modules.fad.payreq.services.intf.PayreqManager;
import com.csnt.scdp.framework.commonaction.crud.CommonLoadAction;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.ListUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fisher on 2015/11/9.
 */
@Scope("singleton")
@Controller("payreq-fill")
@Transactional
public class FillContentAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);

    @Resource(name = "payreq-manager")
    private PayreqManager payreqManager;


    @Resource(name = "erp-common-service-manager")
    private CommonServiceManager commonServiceManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map result = new HashMap<>();

        String year = inMap.get("year").toString();
        String month = inMap.get("month").toString();

        //取最新已处理的月度支付申请
        FadPayReqH latestPayReqH = payreqManager.getLatestHandledPayReqH();
        if (latestPayReqH != null) {
            Integer y = new Integer(year);
            Integer m = new Integer(month);
            Integer ml = new Integer(latestPayReqH.getMonth());
            if (latestPayReqH.getYear().compareTo(y) < 0 ||
                    (latestPayReqH.getYear().compareTo(y) == 0 && ml.compareTo(m) <= 0)) {
                List<FadPayReqH> fadPayReqHList = getExistSavedBill(year, month);
                if (ListUtil.isNotEmpty(fadPayReqHList)) {
                    result.put("uuid", fadPayReqHList.get(0).getUuid());
                }
            } else {
                result.put("reqWarnMsg", "申请月度已过时！");
            }
        } else {
            List<FadPayReqH> fadPayReqHList = getExistSavedBill(year, month);
            if (ListUtil.isNotEmpty(fadPayReqHList)) {
                result.put("uuid", fadPayReqHList.get(0).getUuid());
            }
        }
        return result;
    }

    /**
     * 获取当年当月新增状态的单据
     */
    public List<FadPayReqH> getExistSavedBill(String year, String month) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        Integer y = new Integer(year);
        mapConditions.put(FadPayReqHAttribute.YEAR, y);
        mapConditions.put(FadPayReqHAttribute.MONTH, month);
        mapConditions.put(FadPayReqHAttribute.STATE, BillStateAttribute.CMD_BILL_STATE_NEW);
        List<FadPayReqH> fadPayReqHList = payreqManager.getFadPayReqHbyCondition(mapConditions);
        return fadPayReqHList;
    }
}
