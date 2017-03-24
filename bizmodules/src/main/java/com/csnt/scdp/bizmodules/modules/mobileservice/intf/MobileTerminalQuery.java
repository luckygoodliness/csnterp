package com.csnt.scdp.bizmodules.modules.mobileservice.intf;

import com.csnt.scdp.bizmodules.modules.cdm.cdmfile.dto.CdmFileRelationDto;
import com.csnt.scdp.framework.core.workflow.intf.IWorkFlow;

import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2016/8/16.
 */
public interface MobileTerminalQuery {

    public Map mobileTerminalVariableCollectionAction(String businessKey, String workFlowDefinitionKey, String taskId, String type);

    public List erpMobileLoadParamToClient(Map inMap);

    public Map erpMobileLoadAssignees(Map inMap);

    public Map collectAssignees(Map inMap);

    public Map collectComments(Map inMap);

    public Map getOnlineUserDeptCode(String workFlowDefinitionKey);

    public Map getTaskParams(Map inMap);

    public Map getAttachmentsInfo(List<CdmFileRelationDto> cdmFileRelationDtolist);
}
