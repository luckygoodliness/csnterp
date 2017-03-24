package com.csnt.scdp.bizmodules.modules.workflow.prm.finalestimate;

import com.csnt.scdp.bizmodules.entity.prm.PrmFinalEstimate;
import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.modules.prm.prmexamperiod.services.intf.PrmExamPeriodManager;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.util.DateUtil;
import com.csnt.scdp.framework.util.MapUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("prm_final_estimates-after-processing")
@Transactional

public class PrmFinalEstimateProcessAfterProcessAction extends ERPWorkFlowBaseVariableCollectionAction {

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapResult = new HashMap();
        String uuid = (String) inMap.get("businessKey");
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        PrmFinalEstimate prmFinalEstimate = pcm.findByPK(PrmFinalEstimate.class, uuid);
        if (null != prmFinalEstimate) {
            PrmExamPeriodManager prmExamPeriodManager = BeanFactory.getBean("prmexamperiod-manager".toLowerCase());

            String squareType = prmFinalEstimate.getSquareType();
            //完工结算检查税金是否修正过了
            if ("1".equals(squareType)) {
                if (Boolean.TRUE.equals(MapUtil.getByKeyPath(inMap, "work_flow_setup_var.wf_updateSquareDate"))) {
                    prmFinalEstimate.setSquareDate(DateUtil.parseDate(DateUtil.formatDate(new Date(), "yyyy-MM-dd"),
                            "yyyy-MM-dd"));

                    Date examDate = prmExamPeriodManager.getExamDateFromAppointedDate(new Date());
                    prmFinalEstimate.setExamDate(examDate);

                    pcm.update(prmFinalEstimate);
                    PrmProjectMain projectMain = pcm.findByPK(PrmProjectMain.class, prmFinalEstimate.getPrmProjectMainId
                            ());
                    if (projectMain != null) {
                        projectMain.setState("1");
                        pcm.update(projectMain);
                    }
                }
            } else if ("3".equals(squareType)) {
                if (Boolean.TRUE.equals(MapUtil.getByKeyPath(inMap, "work_flow_setup_var.wf_updateSquareDate"))) {
                    prmFinalEstimate.setSquareDate(DateUtil.parseDate(DateUtil.formatDate(new Date(), "yyyy-MM-dd"),
                            "yyyy-MM-dd"));

                    Date examDate = prmExamPeriodManager.getExamDateFromAppointedDate(new Date());
                    prmFinalEstimate.setExamDate(examDate);

                    pcm.update(prmFinalEstimate);
                    PrmProjectMain projectMain = pcm.findByPK(PrmProjectMain.class, prmFinalEstimate.getPrmProjectMainId
                            ());
                    if (projectMain != null) {
                        projectMain.setState("3");
                        pcm.update(projectMain);
                    }
                }
            }

        }

        return mapResult;
    }
}
