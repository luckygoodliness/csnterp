package com.csnt.scdp.bizmodules.modules.common.action;

import com.csnt.scdp.bizmodules.attributes.ErpCommonAttribute;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.commonaction.crud.CommonLoadAction;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:  ErpLoadAction
 * Copyright: © 2015 Aardwolf Studio. All rights reserved.
 * Company:CSNT中海网络科技股份有限公司
 *
 * @author liujingyuan
 * @version 1.0
 */

public class ErpLoadAction extends CommonLoadAction {
    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        String uuid = getEntityUuid(inMap);
        String definitionKey = getWorkflowDefinitionKey(inMap);
        inMap.put(CommonAttribute.UUID, uuid);
        Map out = super.perform(inMap);
        Map wfInMap = new HashMap<>();
        if (StringUtil.isNotEmpty(definitionKey)) {
            String dtoClass = (String) inMap.get(CommonAttribute.DTO_CLASS_NAME);
            wfInMap.put(WorkFlowAttribute.BUSINESS_KEY, uuid);
            wfInMap.put(WorkFlowAttribute.WORKFLOW_DEFINITION_KEY, definitionKey);
            wfInMap.put(WorkFlowAttribute.DTO, dtoClass);
            IAction action = BeanFactory.getBean("workflow-init-query-action");
            Map wfOutMap = action.perform(wfInMap);
            out.put(ErpCommonAttribute.ERP_WF_INIT_PARAMS, wfOutMap);
        }
        return out;
    }

    protected String getEntityUuid(Map inMap) {
        String uuid = null;
        Object pkObj = inMap.get(CommonAttribute.UUID);
        if (pkObj instanceof String) {
            uuid = (String) pkObj;
        } else if (pkObj instanceof Map) {
            uuid = (String) ((Map) pkObj).get(CommonAttribute.UUID);
        }
        return uuid;
    }


    protected String getWorkflowDefinitionKey(Map inMap) {
        String definitionKey = null;
        Object pkObj = inMap.get(CommonAttribute.UUID);
        if (pkObj instanceof Map) {
            definitionKey = (String) ((Map) pkObj).get(WorkFlowAttribute.WORKFLOW_DEFINITION_KEY);
        }
        return definitionKey;
    }
}

