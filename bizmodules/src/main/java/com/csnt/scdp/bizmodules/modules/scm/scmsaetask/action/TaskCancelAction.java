package com.csnt.scdp.bizmodules.modules.scm.scmsaetask.action;

import com.csnt.scdp.bizmodules.modules.scm.scmsaetask.dto.ScmSaeTaskDto;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.helper.DtoHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.Map;

/**
 * Created by linzp on 2016/9/2.
 */
@Scope("singleton")
@Controller("scmsaetask-taskcancel")
@Transactional
public class TaskCancelAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(TaskCancelAction.class);

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        String scmSaeTaskId = inMap.get("scmSaeTaskId").toString();
        ScmSaeTaskDto scmSaeTaskDto = (ScmSaeTaskDto)DtoHelper.findDtoByPK(ScmSaeTaskDto.class,scmSaeTaskId);
        scmSaeTaskDto.setState("0");// 未提交
        scmSaeTaskDto.setEditflag("*");

        DtoHelper.CascadeSave(scmSaeTaskDto);
        return null;
    }
}
