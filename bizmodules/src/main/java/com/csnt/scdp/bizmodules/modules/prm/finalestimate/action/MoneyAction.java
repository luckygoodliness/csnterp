package com.csnt.scdp.bizmodules.modules.prm.finalestimate.action;

import com.csnt.scdp.bizmodules.modules.prm.finalestimate.services.intf.FinalestimateManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenove on 2015/11/14.
 */
@Scope("singleton")
@Controller("finalestimate-money")
@Transactional
public class MoneyAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(MoneyAction.class);

    @Resource(name = "finalestimate-manager")
    private FinalestimateManager finalestimateManager;

    @Override
    //获取立项预算的计划利润
    public Map perform(Map inMap) throws BizException, SysException {
        BigDecimal zero = BigDecimal.ZERO;
        String prmProjectMainId = (String) inMap.get("prmProjectMainId");
        Map out = new HashMap<>();
//        List<Map> costControlMoneyList = finalestimateManager.getCostControlMoneyByPrmProjectMainId(prmProjectMainId);
//        if (ListUtil.isNotEmpty(costControlMoneyList)) {
//            for (int i = 0; i < costControlMoneyList.size(); i++) {
//                if (costControlMoneyList.get(i).get("costControlMoney") != null) {
//                    BigDecimal costControlMoney = new BigDecimal(costControlMoneyList.get(i).get("costControlMoney").toString());
//                    out.put("costControlMoney", costControlMoney);
//                } else {
//                    out.put("costControlMoney", zero);
//                }
//
//            }
//        }

        return out;
    }
}
