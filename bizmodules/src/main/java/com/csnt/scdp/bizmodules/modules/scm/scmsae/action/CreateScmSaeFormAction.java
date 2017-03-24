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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by linzp on 2016/8/30.
 */
@Scope("singleton")
@Controller("scmsaeform-createscmsaeform")
@Transactional
public class CreateScmSaeFormAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(CreateScmSaeFormAction.class);

    @Resource(name = "scmsae-manager")
    private ScmsaeManager scmsaeManager;

    @Override
    public Map perform(Map map) throws BizException, SysException {
        Map rsMap = new HashMap();
        String scmSaeId = map.get("scmSaeId").toString();
        String info = scmsaeManager.createScmSaeForm(scmSaeId);
        String message = "";
        if("TRUE".equals(info)) {
            message = "生成成功！";
        } else {
            message = "<p>生成失败，系统报错！<p/>"+info;
        }
        rsMap.put("message", message);
        return rsMap;
    }
}
