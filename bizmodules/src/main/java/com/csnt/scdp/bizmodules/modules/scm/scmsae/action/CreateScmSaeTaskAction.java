package com.csnt.scdp.bizmodules.modules.scm.scmsae.action;

import com.csnt.scdp.bizmodules.modules.scm.scmsae.services.intf.ScmsaeManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by linzp on 2016/8/30.
 */
@Scope("singleton")
@Controller("scmsaetask-createscmsaetask")
@Transactional
public class CreateScmSaeTaskAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(CreateScmSaeTaskAction.class);

    @Resource(name = "scmsae-manager")
    private ScmsaeManager scmsaeManager;

    @Override
    public Map perform(Map map) throws BizException, SysException {
        Map rsMap = new HashMap();
        String scmSaeId = map.get("scmSaeId").toString();
        String beginTime =  map.get("beginTime").toString();
        beginTime = beginTime.substring(0, beginTime.indexOf('T'));
        String endTime   =  map.get("endTime").toString();
        endTime = endTime.substring(0, endTime.indexOf('T'));
        String remark   = map.get("remark").toString();

        String info = scmsaeManager.createScmSaeTask(scmSaeId, beginTime, endTime, remark);
        String message = "";
        if("TRUE".equals(info)) {
            message = "生成成功！";
        } else {
            message = "<p>生成失败,系统报错！<p/>"+info;
        }
        rsMap.put("message", message);
        return rsMap;
    }
}
