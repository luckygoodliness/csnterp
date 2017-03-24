package com.csnt.scdp.bizmodules.modules.workflow.common.impl;

import com.csnt.scdp.bizmodules.attributes.ErpWorkFlowAttribute;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.workflow.common.intf.ErpWorkflowManager;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entityattributes.ScdpMsgAttribute;
import com.csnt.scdp.sysmodules.helper.MsgSendHelper;
import com.csnt.scdp.sysmodules.modules.sys.msgcenter.dto.ReceiptsMeta;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  ErpWorkflowManagerImpl
 * Copyright: © 2015 Aardwolf Studio. All rights reserved.
 * Company:CSNT中海网络科技股份有限公司
 *
 * @author liujingyuan
 * @version 1.0
 */
@Scope("singleton")
@Service("erp-workflow-manager-common")
public class ErpWorkflowManagerImpl implements ErpWorkflowManager {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ErpWorkflowManagerImpl.class);

    @Override
    public void start(Map contextMap) {
        contextMap.remove(WorkFlowAttribute.TASK_ID);
        sendCommonMessage(contextMap);

    }

    @Override
    public void complete(Map contextMap) {
        contextMap.remove(WorkFlowAttribute.TASK_ID);
        sendCommonMessage(contextMap);

    }

    @Override
    public void assign(Map contextMap) {
        sendCommonMessage(contextMap);

    }

    @Override
    public void reject(Map contextMap) {
        contextMap.remove(WorkFlowAttribute.TASK_ID);
        sendCommonMessage(contextMap);
    }

    @Override
    public void cancel(Map contextMap) {

    }

    protected void sendCommonMessage(Map contextMap) {
        String taskId = (String) contextMap.get(WorkFlowAttribute.TASK_ID);
        String businessKey = (String) contextMap.get(WorkFlowAttribute.BUSINESS_KEY);
        String definitionKey = (String) contextMap.get(WorkFlowAttribute.WORKFLOW_DEFINITION_KEY);
        Object priority = contextMap.get(WorkFlowAttribute.PRIORITY_KEY_LOW);
        if (StringUtil.isEmpty(priority) || Integer.valueOf(priority.toString()) >= 50) {
            //only handle the priority <50
            return;
        }
        String assignees = (String) contextMap.get(WorkFlowAttribute.ASSIGNEE_LOWER);
        List<Map<String, Object>> lstTaskMap = null;
        if (StringUtil.isNotEmpty(taskId)) {
            //find user by taskid
            lstTaskMap = ErpWorkFlowHelper.getTaskByTaskId(taskId);
        } else if (StringUtil.isNotEmpty(definitionKey) && StringUtil.isNotEmpty(businessKey)) {
            //find user by business key and definition key
            lstTaskMap = ErpWorkFlowHelper.getTasksByBusinessKey(definitionKey, businessKey);
        }
        if (ListUtil.isEmpty(lstTaskMap)) {
            return;
        }

        ReceiptsMeta receiptsMeta = new ReceiptsMeta();
        List<String> lstUserId = ErpWorkFlowHelper.parseAllAssignee(lstTaskMap, ErpWorkFlowHelper.getAllRoleUserIds());
        receiptsMeta.setLstSendToUserId(lstUserId);
        Map messageMap = new HashMap();
        String content = ErpWorkFlowHelper.getTaskName(definitionKey, businessKey, lstTaskMap.get(0));
        messageMap.putAll(lstTaskMap.get(0));
        messageMap.put(ErpWorkFlowAttribute.ERP_TASK_NAME, content);
        MsgSendHelper.sendMsg(messageMap, ScdpMsgAttribute.MSG_TYPE_WEIXIN, ErpWorkFlowAttribute
                        .HIGH_PRIORITY_TASK_TEMPLATE_NO,
                receiptsMeta);
    }
}

