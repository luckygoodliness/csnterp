package com.csnt.scdp.bizmodules.modules.prm.progress.action;

import com.csnt.scdp.bizmodules.modules.prm.progress.services.intf.ProgressManager;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2015/11/14.
 */
@Scope("singleton")
@Controller("progress-change")
@Transactional
public class ChangeAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ChangeAction.class);

    @Resource(name = "progress-manager")
    private ProgressManager progressManager;

    @Override
    //根据uuid带出监理单位
    public Map perform(Map inMap) throws BizException, SysException {
        String uuid = (String) inMap.get("uuid");
        Map out = new HashMap<>();
        List<Map> Management = progressManager.getManagementIdByUUID(uuid);
        if (ListUtil.isNotEmpty(Management)) {
            for (int i = 0; i < Management.size(); i++) {
                if (Management.get(i).get("managementId") != null) {
                    String managementId = (Management.get(i).get("managementId")).toString();
                    if (managementId != null) {
                        out.put("management", managementId);
                    }
                }
            }
        }
        return out;
    }
}
