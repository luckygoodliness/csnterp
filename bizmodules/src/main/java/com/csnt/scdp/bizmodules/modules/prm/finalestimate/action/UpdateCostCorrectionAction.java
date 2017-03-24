package com.csnt.scdp.bizmodules.modules.prm.finalestimate.action;

import com.csnt.scdp.bizmodules.entity.prm.PrmFinalEstimate;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmFinalEstimateAttribute;
import com.csnt.scdp.bizmodules.modules.prm.finalestimate.services.intf.FinalestimateManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:  LoadAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 20:02:44
 */

@Scope("singleton")
@Controller("final-estimate-update-cost-correction")
@Transactional
public class UpdateCostCorrectionAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(UpdateCostCorrectionAction.class);

    @Resource(name = "finalestimate-manager")
    private FinalestimateManager finalestimateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before

        String uuid = (String) inMap.get(CommonAttribute.UUID);
        BigDecimal costCorrection = new BigDecimal(String.valueOf(inMap.get(PrmFinalEstimateAttribute.COST_CORRECTION)));
        BigDecimal costCorrectionOrig = new BigDecimal(String.valueOf(inMap.get("costCorrectionOrig")));
        BigDecimal squareGrossProfit = new BigDecimal(String.valueOf(inMap.get(PrmFinalEstimateAttribute.SQUARE_GROSS_PROFIT)));
        PrmFinalEstimate finalEstimate = PersistenceFactory.getInstance().findByPK(PrmFinalEstimate.class, uuid);
        BigDecimal squareGrossProfitNew=squareGrossProfit.subtract(costCorrection).add(costCorrectionOrig) ;

        if (finalEstimate != null) {
            finalEstimate.setCostCorrection(costCorrection);
            finalEstimate.setSquareGrossProfit(squareGrossProfitNew);
            PersistenceFactory.getInstance().update(finalEstimate);
        }
        //Do After
        return new HashMap();
    }
}
