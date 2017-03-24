package com.csnt.scdp.bizmodules.modules.workflow.prm.finalestimate;

import com.csnt.scdp.bizmodules.entity.prm.PrmFinalEstimate;
import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmFinalEstimateAttribute;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MapUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("prm_final_estimates-after-cancel")
@Transactional

public class PrmFinalEstimateProcessAfterCancelAction extends ERPWorkFlowBaseVariableCollectionAction {

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapResult = new HashMap();
        String uuid = (String) inMap.get("businessKey");
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        PrmFinalEstimate prmFinalEstimate = pcm.findByPK(PrmFinalEstimate.class, uuid);
        if (null != prmFinalEstimate) {
            String squareType = prmFinalEstimate.getSquareType();
            //完工结算检查税金是否修正过了
            String prmProjectMainId = prmFinalEstimate.getPrmProjectMainId();
            if ("1".equals(squareType)) {
                if (Boolean.TRUE.equals(MapUtil.getByKeyPath(inMap, "work_flow_setup_var.wf_clearSquareDate"))) {
                    prmFinalEstimate.setSquareDate(null);
                    pcm.update(prmFinalEstimate);
                    PrmProjectMain projectMain = pcm.findByPK(PrmProjectMain.class, prmProjectMainId);
                    if (projectMain != null) {
                        Map condition = new HashMap();
                        condition.put(PrmFinalEstimateAttribute.PRM_PROJECT_MAIN_ID, prmProjectMainId);
                        List<PrmFinalEstimate> lstFinalEstimate = PersistenceFactory.getInstance().findByAnyFields(
                                PrmFinalEstimate.class, condition, null);
                        if (ListUtil.isNotEmpty(lstFinalEstimate) && lstFinalEstimate.size() > 1) {
                            projectMain.setState("0");
                        } else {
                            projectMain.setState(null);
                        }
                        pcm.update(projectMain);
                    }
                }
            } else if ("3".equals(squareType)) {
                if (Boolean.TRUE.equals(MapUtil.getByKeyPath(inMap, "work_flow_setup_var.wf_clearSquareDate"))) {
                    prmFinalEstimate.setSquareDate(null);
                    pcm.update(prmFinalEstimate);
                    PrmProjectMain projectMain = pcm.findByPK(PrmProjectMain.class, prmProjectMainId);
                    if (projectMain != null) {
                        projectMain.setState(null);
                        pcm.update(projectMain);
                    }
                }
            }

        }

        return mapResult;
    }
}
