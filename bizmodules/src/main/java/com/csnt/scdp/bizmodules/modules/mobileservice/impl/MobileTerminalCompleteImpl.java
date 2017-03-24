package com.csnt.scdp.bizmodules.modules.mobileservice.impl;

import com.csnt.scdp.bizmodules.attributes.ErpMobileDtoAttribute;
import com.csnt.scdp.bizmodules.attributes.ErpMobileTerminalAttribute;
import com.csnt.scdp.bizmodules.modules.mobileservice.intf.MobileTerminalComplete;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.helper.UserHelper;
import com.csnt.scdp.framework.util.MapUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.bo.WorkflowTaskBean;
import com.csnt.scdp.sysmodules.modules.sys.workflow.services.intf.WorkFlowManager;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by lenovo on 2016/8/23.
 */
@Scope("singleton")
@Service("mobile-terminal-complete")
@Transactional
public class MobileTerminalCompleteImpl implements MobileTerminalComplete {

    @Resource(name = "workflow-manager")
    private WorkFlowManager workFlowManager;

    @Override
    public Map mobileTerminalCompleteAction(WorkflowTaskBean bean) throws BizException, SysException {
        //提交方法
        String opType = bean.getOpType();
        String businessKey = bean.getBusinessKey();
        String workFlowDefinitionKey = bean.getDefinitionKey();
        String taskId = bean.getTaskId();
        String processDeptCode = bean.getProcessDeptCode();
        String dto = bean.getDto();
        String menuCode = bean.getMenuCode();
        String comment = bean.getComment();
        String assignee = bean.getAssignee();

        Map inMap = new HashMap<>();
        Map businessMap = new HashMap<>();
        Map mapResult = new LinkedHashMap<>();

        inMap.put(WorkFlowAttribute.BUSINESS_KEY, businessKey);
        inMap.put(WorkFlowAttribute.TASK_ID, taskId);
        inMap.put(WorkFlowAttribute.WORKFLOW_DEFINITION_KEY, workFlowDefinitionKey);
        inMap.put(ErpMobileTerminalAttribute.PROCESS_DEPT_CODE, processDeptCode);
        inMap.put(WorkFlowAttribute.COMMENT, comment);
        inMap.put(WorkFlowAttribute.MENU_CODE, menuCode);
        inMap.put(WorkFlowAttribute.DTO, dto);
        inMap.put(WorkFlowAttribute.ASSIGNEE_LOWER, assignee);
        inMap.put(WorkFlowAttribute.TERMINAL_TYPE, ErpMobileTerminalAttribute.MOBILE_TYPE);

        if (StringUtil.isNotEmpty(workFlowDefinitionKey) && StringUtil.isNotEmpty(businessKey)) {
            if (opType.equals("1")) {//提交

                if (StringUtil.isEmpty(taskId)) {
                    mapResult = this.mobileTerminalStartSubmit(inMap);
                } else {
                    mapResult = this.mobileTerminalCompleteSubmit(inMap);
                }

            } else if (opType.equals("2")) {//转发

                mapResult = this.mobileTerminalAssigneeSubmit(inMap);

            } else if (opType.equals("3")) {

                mapResult = this.mobileTerminalRejectSubmit(inMap);

            } else {
                mapResult.put("result", "2");
                mapResult.put("message", "提交类型（提交，转发，退回）有误");
            }


        }

        return mapResult;
    }


    @Override
    public Map mobileTerminalStartSubmit(Map inMap) {
        Map businessMap = new HashMap<>();
        Map mapResult = new LinkedHashMap<>();
        String taskId = (String) inMap.get(WorkFlowAttribute.TASK_ID);
        String workFlowDefinitionKey = (String) inMap.get(WorkFlowAttribute.WORKFLOW_DEFINITION_KEY);

        if (StringUtil.isEmpty(taskId)) {
            //taskId为空代表新增
            String controllId = "mobile-terminal-start-" + workFlowDefinitionKey.toLowerCase();
            IAction action = null;
            try {
                action = BeanFactory.getBean(controllId.toLowerCase());
                businessMap = action.perform(inMap);
            } catch (NoSuchBeanDefinitionException ignore) {
            }

            if (MapUtil.isEmpty(businessMap) || (MapUtil.isNotEmpty(businessMap) && businessMap.containsKey("result") && (boolean) businessMap.get("result"))) {
                workFlowManager.completeWorkflow(inMap);
                mapResult.put("result", "0");
                mapResult.put("message", "提交成功");
            } else {
                mapResult.put("result", "1");
                mapResult.put("message", "数据校验不通过");
            }
        }

        return mapResult;
    }

    @Override
    public Map mobileTerminalCompleteSubmit(Map inMap) {
        Map businessMap = new HashMap<>();
        Map mapResult = new LinkedHashMap<>();
        String taskId = (String) inMap.get(WorkFlowAttribute.TASK_ID);
        String workFlowDefinitionKey = (String) inMap.get(WorkFlowAttribute.WORKFLOW_DEFINITION_KEY);

        if (StringUtil.isNotEmpty(taskId)) {
            //taskId为空代表新增
            String controllId = "mobile-terminal-complete-" + workFlowDefinitionKey.toLowerCase();
            IAction action = null;
            try {
                action = BeanFactory.getBean(controllId.toLowerCase());
                businessMap = action.perform(inMap);
            } catch (NoSuchBeanDefinitionException ignore) {
            }

            if (MapUtil.isEmpty(businessMap) || (MapUtil.isNotEmpty(businessMap) && businessMap.containsKey("result") && (boolean) businessMap.get("result"))) {
                workFlowManager.completeWorkflow(inMap);
                mapResult.put("result", "0");
                mapResult.put("message", "提交成功");
            } else if(MapUtil.isNotEmpty(businessMap)&&businessMap.containsKey("message")){
                mapResult.put("result", "1");
                mapResult.put("message", businessMap.get("message"));
            }else{
                mapResult.put("result", "1");
                mapResult.put("message", "数据校验不通过");
            }
        }

        return mapResult;
    }

    @Override
    public Map mobileTerminalAssigneeSubmit(Map inMap) {
        Map businessMap = new HashMap<>();
        Map mapResult = new LinkedHashMap<>();
        String taskId = (String) inMap.get(WorkFlowAttribute.TASK_ID);
        String workFlowDefinitionKey = (String) inMap.get(WorkFlowAttribute.WORKFLOW_DEFINITION_KEY);
        String assignees = (String) inMap.get(WorkFlowAttribute.ASSIGNEE_LOWER);
        if (StringUtil.isEmpty(taskId)) {
            mapResult.put("result", "4");
            mapResult.put("message", "taskId为空");
            return mapResult;
        }
        if (StringUtil.isNotEmpty(assignees)) {
            workFlowManager.assignWorkflow(inMap);
            mapResult.put("result", "0");
            mapResult.put("message", "转发成功");
        } else {
            mapResult.put("result", "3");
            mapResult.put("message", "请选择提交人");
        }
        return mapResult;
    }

    @Override
    public Map mobileTerminalRejectSubmit(Map inMap) {
        inMap.put("wf_reject", "1");
        //默认退回到上一步
        List lstAdditionalFormData = new ArrayList<>();
        lstAdditionalFormData.add("wf_rollback_one_step=1");
        inMap.put("lstAdditionalFormData", lstAdditionalFormData);


        Map businessMap = new HashMap<>();
        Map mapResult = new LinkedHashMap<>();
        String taskId = (String) inMap.get(WorkFlowAttribute.TASK_ID);
        String workFlowDefinitionKey = (String) inMap.get(WorkFlowAttribute.WORKFLOW_DEFINITION_KEY);
//        String assignees = (String) inMap.get(WorkFlowAttribute.ASSIGNEE_LOWER);
        if (StringUtil.isEmpty(taskId)) {
            mapResult.put("result", "4");
            mapResult.put("message", "taskId为空");
            return mapResult;
        }
        workFlowManager.rejectWorkflow(inMap);
        mapResult.put("result", "0");
        mapResult.put("message", "退回成功");

        return mapResult;
    }


}
