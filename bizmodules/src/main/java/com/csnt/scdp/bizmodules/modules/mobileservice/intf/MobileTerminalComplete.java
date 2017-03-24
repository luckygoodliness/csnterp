package com.csnt.scdp.bizmodules.modules.mobileservice.intf;

import com.csnt.scdp.sysmodules.bo.WorkflowTaskBean;

import java.util.Map;

/**
 * Created by lenovo on 2016/8/23.
 */
public interface MobileTerminalComplete {

    public Map mobileTerminalCompleteAction(WorkflowTaskBean bean);

    public Map mobileTerminalStartSubmit(Map inMap);

    public Map mobileTerminalCompleteSubmit(Map inMap);

    public Map mobileTerminalAssigneeSubmit(Map inMap);

    public Map mobileTerminalRejectSubmit(Map inMap);

}
