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
 * Created by Administrator on 2015/11/12.
 */
@Scope("singleton")
@Controller("finalestimate-check")
@Transactional
public class CheckAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(CheckAction.class);

    @Resource(name = "finalestimate-manager")
    private FinalestimateManager finalestimateManager;

    @Override
    //计算项目收款里的已审核状态下的实际收款总额
    public Map perform(Map inMap) throws BizException, SysException {
        List lstError = finalestimateManager.checkBeforeSave(inMap);
        //完工结算不提示 --结算比例超过80%，提示采购合同是否完成结算
        Map prmFinalEstimateMap = (Map) inMap.get("prmFinalEstimateDto");
        String squareType = (String) prmFinalEstimateMap.get("squareType");
        List lstInfo = null;
        if (!squareType.equals("1")) {
            lstInfo = finalestimateManager.validateDataNonForce(inMap);
        }
        Map out = new HashMap();
        if (ListUtil.isNotEmpty(lstError)) {
            out.put("errorList", lstError);
        }
        if (ListUtil.isNotEmpty(lstInfo)) {
            out.put("infoList", lstInfo);
        }
        return out;
    }
}
