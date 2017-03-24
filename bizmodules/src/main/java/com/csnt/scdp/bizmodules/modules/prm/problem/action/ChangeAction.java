package com.csnt.scdp.bizmodules.modules.prm.problem.action;

import com.csnt.scdp.bizmodules.entity.prm.PrmProblem;
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
 * Created by John on 2015/11/16.
 */
@Scope("singleton")
@Controller("problem-change")
@Transactional
public class ChangeAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ChangeAction.class);

    @Resource(name = "problem-manager")
    private ProblemManager problemManager;

    @Override
    //关闭时把状态设为关闭
    public Map perform(Map inMap) throws BizException, SysException {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        String uuid = (String) inMap.get("uuid");
        PrmProblem prmProblem = problemManager.getPrmProblemForUUID(uuid);
        if (prmProblem != null) {
            prmProblem.setState("4");
            pcm.update(prmProblem);
        }
        return null;
    }
}
