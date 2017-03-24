package com.csnt.scdp.bizmodules.modules.scm.scmsaetask.action;

import com.csnt.scdp.framework.commonaction.crud.CommonModifyAction;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.helper.DtoHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.scm.scmsaetask.dto.ScmSaeTaskDto;

import javax.transaction.Transactional;
import java.util.Map;

/**
 * Created by linzp on 2016/9/2.
 */
@Scope("singleton")
@Controller("scmsaetask-taskcommit")
@Transactional
public class TaskCommitAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(TaskCommitAction.class);

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        String scmSaeTaskId = inMap.get("scmSaeTaskId").toString();
        ScmSaeTaskDto scmSaeTaskDto = (ScmSaeTaskDto)DtoHelper.findDtoByPK(ScmSaeTaskDto.class,scmSaeTaskId);
        scmSaeTaskDto.setState("1");// 已提交
        scmSaeTaskDto.setEditflag("*");

        DtoHelper.CascadeSave(scmSaeTaskDto);
        return null;
    }
}
