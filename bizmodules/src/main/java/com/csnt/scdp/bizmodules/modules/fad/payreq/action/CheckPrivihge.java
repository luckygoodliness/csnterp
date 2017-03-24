package com.csnt.scdp.bizmodules.modules.fad.payreq.action;

import com.csnt.scdp.bizmodules.modules.common.helper.ErpUserHelper;
import com.csnt.scdp.bizmodules.modules.fad.payreq.services.intf.PayreqManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import org.springframework.stereotype.Controller;
import org.springframework.context.annotation.Scope;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fisher on 2015/11/16.
 */
@Scope("singleton")
@Controller("payreq-checkprivihge")
@Transactional
public class CheckPrivihge implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(DeleteAction.class);

    @Resource(name = "payreq-manager")
    private PayreqManager payreqManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = new HashMap();
        List<String> reqData = (ArrayList) inMap.get("reqData");
        String userId = inMap.get("userId").toString();
        Map rtnDataMap = new HashMap();
        reqData.forEach(t -> {
                    switch (t) {
                        case "isBizVp":
                            rtnDataMap.put(t, ErpUserHelper.isSelfBizDivisionVp(userId));
                            break;
                    }
                }
        );

        out.put(reqData, rtnDataMap);
        //Do After
        return out;
    }
}
