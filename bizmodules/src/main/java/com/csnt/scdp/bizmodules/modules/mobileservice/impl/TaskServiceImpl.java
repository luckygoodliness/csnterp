package com.csnt.scdp.bizmodules.modules.mobileservice.impl;

import com.csnt.scdp.bizmodules.modules.mobileservice.intf.MobileTerminalComplete;
import com.csnt.scdp.bizmodules.modules.mobileservice.intf.MobileTerminalQuery;
import com.csnt.scdp.sysmodules.bo.WorkflowTaskBean;
import com.csnt.scdp.sysmodules.modules.sys.workflow.services.intf.TaskService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by lenovo on 2016/8/19.
 */

@Service
@Primary
public class TaskServiceImpl implements TaskService {
    @Resource(name = "mobile-terminal-query")
    private MobileTerminalQuery mobileTerminalQuery;
    @Resource(name = "mobile-terminal-complete")
    private MobileTerminalComplete mobileTerminalComplete;

    @Override
    public Map getTask(String businessKey, String definitionKey, String id, String type) {
        return mobileTerminalQuery.mobileTerminalVariableCollectionAction(businessKey, definitionKey, id, type);
    }

    @Override
    public Map submitTask(WorkflowTaskBean bean) {
        return mobileTerminalComplete.mobileTerminalCompleteAction(bean);
    }
}
