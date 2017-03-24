package com.csnt.scdp.bizmodules.modules.mobileservice.impl;


import com.csnt.scdp.bizmodules.attributes.ErpMobileDtoAttribute;
import com.csnt.scdp.bizmodules.attributes.ErpMobileTerminalAttribute;
import com.csnt.scdp.bizmodules.modules.cdm.cdmfile.dto.CdmFileRelationDto;
import com.csnt.scdp.bizmodules.modules.mobileservice.intf.MobileTerminalQuery;
import com.csnt.scdp.bizmodules.modules.workflow.message.UnHandledTaskReminderJob;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.bo.QueryCondition;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.core.workflow.intf.IWorkFlow;
import com.csnt.scdp.framework.helper.SysconfigHelper;
import com.csnt.scdp.framework.helper.UserHelper;
import com.csnt.scdp.framework.util.ArrayUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MapUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpFileManager;
import com.csnt.scdp.sysmodules.helper.FMCodeHelper;
import com.csnt.scdp.sysmodules.helper.FileManagerHelper;
import com.csnt.scdp.sysmodules.modules.sys.msgcenter.services.intf.MsgcenterManager;
import com.csnt.scdp.sysmodules.modules.sys.workflow.services.intf.WorkFlowManager;
import org.apache.struts2.json.JSONUtil;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.Persistence;
import javax.transaction.Transactional;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Service("mobile-terminal-query")
@Transactional
public class MobileTerminalQueryImpl implements MobileTerminalQuery {
    private static ILogTracer tracer = LogTracerFactory.getInstance(UnHandledTaskReminderJob.class);

    @Resource(name = "workflow-manager")
    private WorkFlowManager workFlowManager;
    @Resource(name = "msgcenter-manager-impl")
    private MsgcenterManager msgcenterManager;

    @Override
    public Map mobileTerminalVariableCollectionAction(String businessKey, String workFlowDefinitionKey, String taskId, String type) {

        Map mapResult = new LinkedHashMap<>();
        Map inMap = new HashMap<>();

        if (StringUtil.isNotEmpty(workFlowDefinitionKey) && StringUtil.isNotEmpty(businessKey) && StringUtil.isNotEmpty(taskId)) {
            String controllId = "mobile-terminal-query-" + workFlowDefinitionKey.toLowerCase();
            IAction action = null;
            try {
                inMap.put(WorkFlowAttribute.BUSINESS_KEY, businessKey);
                inMap.put(WorkFlowAttribute.WORKFLOW_DEFINITION_KEY, workFlowDefinitionKey);
                inMap.put(WorkFlowAttribute.TASK_ID, taskId);
                inMap.put(ErpMobileTerminalAttribute.QUERY_TYPE, type);
                String dto = ErpMobileDtoAttribute.definitionKey2Dto.get(workFlowDefinitionKey + "_Dto");
                inMap.put(WorkFlowAttribute.DTO, dto);
                String menuCode = ErpMobileDtoAttribute.definitionKey2Dto.get(workFlowDefinitionKey + "_MenuCode");
                inMap.put(WorkFlowAttribute.MENU_CODE, menuCode);

                action = BeanFactory.getBean(controllId.toLowerCase());
                mapResult = action.perform(inMap);
                String processDeptCode = (String) mapResult.get(ErpMobileTerminalAttribute.PROCESS_DEPT_CODE);
                if (StringUtil.isEmpty(processDeptCode)) {
                    processDeptCode = UserHelper.getOrgCode();
                } else {
                    mapResult.remove(ErpMobileTerminalAttribute.PROCESS_DEPT_CODE);
                }
                //如果没有附件，传空list到移动端
                if (mapResult.get("attachments") == null) {
                    List attachments = new ArrayList<>();
                    mapResult.put("attachments", attachments);
                }
                //如果没有附件，传空list到移动端
                if (mapResult.get("reports") == null) {
                    List reports = new ArrayList<>();
                    mapResult.put("reports", reports);
                }

                inMap.put(ErpMobileTerminalAttribute.PROCESS_DEPT_CODE, processDeptCode);

                mapResult.putAll(this.collectAssignees(inMap));

                mapResult.putAll(this.collectComments(inMap));

                mapResult.putAll(this.getTaskParams(inMap));


            } catch (NoSuchBeanDefinitionException e) {
                mapResult.put(ErpMobileTerminalAttribute.ITEM, "");
                tracer.error("The module is not completed");
                return mapResult;
            }
        } else {
            tracer.error("The parameters(businessKey,workFlowDefinitionKey,taskId) is empty");
            return mapResult;
        }


        try {
            System.out.println(JSONUtil.serialize(mapResult));
        } catch (Exception e) {

        }
        return mapResult;

    }

    @Override
    public List erpMobileLoadParamToClient(Map inMap) {
        List result;
        IWorkFlow workFlowImpl = BeanFactory.getBean(CommonAttribute.WORK_FLOW_IMPL);
        String businessKey = (String) inMap.get(WorkFlowAttribute.BUSINESS_KEY);
        String definitionKey = (String) inMap.get(WorkFlowAttribute.WORKFLOW_DEFINITION_KEY);
        String taskId = (String) inMap.get(WorkFlowAttribute.TASK_ID);
        if (StringUtil.isEmpty(taskId)) {
            //loadStartNode为true代表第一步
            result = workFlowManager.loadParamToClient(inMap, true, definitionKey, null, workFlowImpl, WorkFlowAttribute.WF_SUBMETHOD_START);
        } else {
            result = workFlowManager.loadParamToClient(inMap, false, definitionKey, taskId, workFlowImpl, WorkFlowAttribute.WF_SUBMETHOD_COMPLETE);
        }
        return result;
    }

    @Override
    public Map erpMobileLoadAssignees(Map inMap) {
        Map retMap = new HashMap<>();
        Map info = new HashMap<>();
        List formDataList = erpMobileLoadParamToClient(inMap);
        Map userFilters = new HashMap<>();
        String userFilter = null;
        if (ListUtil.isNotEmpty(formDataList)) {
            for (Object formData : formDataList) {
                String userFormData = (String) formData;
                if (userFormData.contains("userFilter")) {
                    userFilter = userFormData.substring(userFormData.indexOf("=") + 1, userFormData.length());
                } else if (userFormData.contains("wf_disable_assignee_choose=1")||userFormData.contains("wf_goto_end=1")) {
                    return retMap;
                } else if (userFormData.contains("workflow-load-user-")) {
                    //如果是自定义提交人，需要传入下面两个参数
                    info.put("businessKey", (String) inMap.get("businessKey"));
                    info.put("workFlowDefinitionKey", (String) inMap.get("workFlowDefinitionKey"));
                }
            }
        }
        userFilters.put("userFilter", userFilter);
        Map deptCodeInfo = new HashMap<>();
        deptCodeInfo.put(ErpMobileTerminalAttribute.PROCESS_DEPT_CODE, (String) inMap.get(ErpMobileTerminalAttribute.PROCESS_DEPT_CODE));
        info.putAll(userFilters);
        info.putAll(deptCodeInfo);
        Map queryConditions = new HashMap<>();
        queryConditions.put("queryConditions", info);
        retMap = msgcenterManager.loadUser(queryConditions);
        return retMap;
    }

    @Override
    public Map collectAssignees(Map inMap) {
        Map assignees = new LinkedHashMap<>();
        List<Map> retMapList = new ArrayList<Map>();
        String queryType = (String) inMap.get(ErpMobileTerminalAttribute.QUERY_TYPE);
        if (StringUtil.isNotEmpty(queryType) && queryType.equals("UNFIXED")) {
            Map userInfoMap = this.erpMobileLoadAssignees(inMap);
            if (MapUtil.isNotEmpty(userInfoMap)) {
                List<Map> userList = (List) userInfoMap.get("root");
                if (ListUtil.isNotEmpty(userList)) {
                    for (Map userInfo : userList) {
                        Map retMap = new HashMap<>();
                        String userName = (String) userInfo.get("userName");
                        String orgName = (String) userInfo.get("orgName");
                        String userId = (String) userInfo.get("userId");
                        retMap.put("userId", userId);
                        retMap.put("userName", userName);
                        retMap.put("orgName", orgName);
                        retMapList.add(retMap);
                    }
                }
            }
        }
        assignees.put("persons", retMapList);
        return assignees;
    }

    @Override
    public Map collectComments(Map inMap) {
        List<Map> retMapList = new ArrayList<>();
        String commentAnnotation = "workflow-query-comment-action";
        IAction action = BeanFactory.getBean(commentAnnotation);
        Map map = action.perform(inMap);
        if (MapUtil.isNotEmpty(map)) {
            List<Map> commentInfoList = (List) map.get("comment");
            if (ListUtil.isNotEmpty(commentInfoList)) {
                for (Map commentInfo : commentInfoList) {
                    Map commentInfoMap = new LinkedHashMap<>();
                    String taskName = (String) commentInfo.get("taskName");
                    String assignee = (String) commentInfo.get("assignee");
                    String deleteReason = FMCodeHelper.getDescByCode((String) commentInfo.get("deleteReason"), "WF_DELETE_REASON");
                    Date startTime = (Date) commentInfo.get("starttime");
                    Date endTime = (Date) commentInfo.get("endtime");
                    String startTimeStandard = null;
                    String endTimeStandard = null;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    if (null != startTime) {
                        startTimeStandard = sdf.format(startTime);
                    }
                    if (null != endTime) {
                        endTimeStandard = sdf.format(endTime);
                    }
                    commentInfoMap.put("节点名称", taskName);
                    commentInfoMap.put("操作人", assignee);
                    commentInfoMap.put("操作", deleteReason);
                    commentInfoMap.put("到达时间", startTimeStandard);
                    commentInfoMap.put("结束时间", endTimeStandard);
                    retMapList.add(commentInfoMap);
                }
            }
        }
        Map comment = new LinkedHashMap<>();
        comment.put("history", retMapList);
        return comment;
    }

    @Override
    public Map getOnlineUserDeptCode(String workFlowDefinitionKey) {
        Map out = new HashMap<>();
        if (StringUtil.isEmpty(workFlowDefinitionKey)) {
            return null;
        }
        String isProcessDeptCode = ErpMobileDtoAttribute.definitionKey2Dto.get(workFlowDefinitionKey + "_DeptCode");
        if (StringUtil.isEmpty(isProcessDeptCode)) {
            String deptCode = UserHelper.getOrgCode();
            out.put(ErpMobileTerminalAttribute.PROCESS_DEPT_CODE, deptCode);
        }
        return out;
    }

    @Override
    public Map getTaskParams(Map inMap) {
        String workFlowDefinitionKey = (String) inMap.get(WorkFlowAttribute.WORKFLOW_DEFINITION_KEY);
        String businessKey = (String) inMap.get(WorkFlowAttribute.BUSINESS_KEY);
        String dto = (String) inMap.get(WorkFlowAttribute.DTO);
        String menuCode = (String) inMap.get(WorkFlowAttribute.MENU_CODE);
        String taskId = (String) inMap.get(WorkFlowAttribute.TASK_ID);
        String processDeptCode = (String) inMap.get(ErpMobileTerminalAttribute.PROCESS_DEPT_CODE);
        //todo 系统额外的参数，从界面上取到的现在在applyController用到，后期处理
        String variable = "";

        Map resultMap = new LinkedHashMap<>();
        Map taskParamsMap = new LinkedHashMap<>();
        taskParamsMap.put(WorkFlowAttribute.BUSINESS_KEY, businessKey);
        taskParamsMap.put("definitionKey", workFlowDefinitionKey);
        taskParamsMap.put(WorkFlowAttribute.DTO, dto);
        taskParamsMap.put(WorkFlowAttribute.MENU_CODE, menuCode);
        taskParamsMap.put(WorkFlowAttribute.TASK_ID, taskId);
        taskParamsMap.put(ErpMobileTerminalAttribute.PROCESS_DEPT_CODE, processDeptCode);
        taskParamsMap.put("variable", variable);
        resultMap.put("taskParams", taskParamsMap);

        return resultMap;
    }

    @Override
    public Map getAttachmentsInfo(List<CdmFileRelationDto> cdmFileRelationDtolist) {
        String previewUrl = SysconfigHelper.getProperty("file_preview_url");

        String[] previewFileTypes = {"doc", "xls", "ppt", "docx", "xlsx", "pptx", "txt"};
        String[] picFileTypes = {"pdf", "jpg", "jpeg", "gif", "png", "bmp"};


        Map attachments = new HashMap<>();
        List<LinkedHashMap> attachmentInfo = new ArrayList<LinkedHashMap>();
        List fileIdList = new ArrayList<>();
        if (ListUtil.isNotEmpty(cdmFileRelationDtolist)) {
            for (CdmFileRelationDto cdmFileRelationDto : cdmFileRelationDtolist) {
                fileIdList.add(cdmFileRelationDto.getFileId());
            }
        }
        if (ListUtil.isNotEmpty(fileIdList)) {
            QueryCondition condition = new QueryCondition();
            condition.setField("uuid");
            condition.setOperator("in");
            condition.setValueList(fileIdList);
            List<QueryCondition> queryConditions = new ArrayList<>();
            queryConditions.add(condition);
            List<ScdpFileManager> scdpFileManagers = PersistenceFactory.getInstance().findByAnyFields(ScdpFileManager.class, queryConditions, null);

            String url = null;
            if (ListUtil.isNotEmpty(scdpFileManagers)) {
                for (ScdpFileManager scdpFileManager : scdpFileManagers) {
                    LinkedHashMap attachmentMap = new LinkedHashMap<>();
                    String fileType = scdpFileManager.getFileType().toLowerCase();
                    String downloadUrl = FileManagerHelper.getFileDownloadUrl(scdpFileManager.getUuid());
                    String trueUrl = downloadUrl.substring(0, downloadUrl.indexOf("filename") - 1);
                    if (StringUtil.isNotEmpty(fileType) && ArrayUtil.contains(previewFileTypes, fileType)) {
                        url = previewUrl + "?ft=" + fileType + "&url=" + URLEncoder.encode(trueUrl);
                        attachmentMap.put(ErpMobileTerminalAttribute.IS_DIRECT, false);
                    }

                    if (StringUtil.isNotEmpty(fileType) && ArrayUtil.contains(picFileTypes, fileType)) {
                        url = downloadUrl.substring(0, downloadUrl.lastIndexOf("filename") - 1);
                        attachmentMap.put(ErpMobileTerminalAttribute.IS_DIRECT, true);
                    }
                    if ("pdf".equals(fileType)) {
                        attachmentMap.put("pdf_preview_url", previewUrl + "?ft=" + fileType + "&url=" + URLEncoder.encode(trueUrl));
                    }

                    attachmentMap.put(ErpMobileTerminalAttribute.TITLE, scdpFileManager.getFileName());
                    attachmentMap.put(ErpMobileTerminalAttribute.TYPE, scdpFileManager.getFileType());
                    attachmentMap.put(ErpMobileTerminalAttribute.URL, url);
                    attachmentInfo.add(attachmentMap);
                }
            }
        }
        attachments.put("attachments", attachmentInfo);
        return attachments;
    }


}
