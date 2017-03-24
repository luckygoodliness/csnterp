package com.csnt.scdp.bizmodules.modules.prm.problem.action;

import com.csnt.scdp.bizmodules.entity.prm.PrmGoodsArrival;
import com.csnt.scdp.bizmodules.entity.prm.PrmProblemFeedback;
import com.csnt.scdp.bizmodules.modules.prm.problem.services.intf.ProblemManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Map;

/**
 * Created by lenove on 2015/11/15.
 */
@Scope("singleton")
@Controller("problem-deal")
@Transactional
public class DealAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(DealAction.class);

    @Resource(name = "problem-manager")
    private ProblemManager problemManager;

    @Override
    //让弹出框写的值进数据库
    public Map perform(Map inMap) throws BizException, SysException {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        String feedbacks = (String) inMap.get("feedbacks");
        String dealPersons = (String) inMap.get("dealPersons");
        String name = (String) inMap.get("name");
        String uuid = (String) inMap.get("uuid");
        PrmProblemFeedback prmProblemFeedback = new PrmProblemFeedback();
        if (prmProblemFeedback != null && StringUtil.isNotEmpty(uuid)) {
            prmProblemFeedback.setPrmProblemId(uuid);
            prmProblemFeedback.setFeedback(feedbacks);
            prmProblemFeedback.setDealPerson(dealPersons);
            prmProblemFeedback.setCreateBy(name);
            pcm.insert(prmProblemFeedback);
        }
        return null;
    }
}
