package com.csnt.scdp.bizmodules.modules.workflow.message;

import com.csnt.scdp.bizmodules.attributes.ErpWorkFlowAttribute;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.schedule.job.BaseQuartzJob;
import com.csnt.scdp.framework.util.DateUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.sysmodules.entityattributes.ScdpMsgTemplateAttribute;
import com.csnt.scdp.sysmodules.helper.MsgSendHelper;
import com.csnt.scdp.sysmodules.modules.sys.msgcenter.dto.ReceiptsMeta;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description:  QuartzTestJob
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author lixiang
 * @version 1.0
 */

@DisallowConcurrentExecution
public class UnHandledTaskReminderJob extends BaseQuartzJob {
    private static ILogTracer tracer = LogTracerFactory.getInstance(UnHandledTaskReminderJob.class);

    @Override
    protected String executeInternalBiz(JobExecutionContext context) {
        //currently, weixin expires time is 2 hours, so advise refresh token's interval is 1 hour
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        if (dataMap != null) {
            String msgType = dataMap.getString(ScdpMsgTemplateAttribute.MSG_TYPE);
            String templateNo = dataMap.getString(ScdpMsgTemplateAttribute.MSG_TMPLNO);
            //step1: query all tasks, refer to dashboard query
            List<Map<String, Object>> lstAllTaskMap = getAllTasks(dataMap);
            if (ListUtil.isEmpty(lstAllTaskMap)) {
                return "SUCCESS";
            }
            //step2: group by business key and definition key
            Map<String, Map<String, List<Map<String, Object>>>> allTaskMap = lstAllTaskMap.stream().collect(Collectors.groupingBy(
                    x -> (String) x.get(WorkFlowAttribute.DEFINTION_KEY_LOW),
                    Collectors.groupingBy(y -> (String) y.get(WorkFlowAttribute.BUSINESS_KEY))));
            //step3: query user id by roles
            Map<String, List<String>> roleUserIdMap = ErpWorkFlowHelper.getAllRoleUserIds();
            //step4: get task name, and send to user
            Set<String> definitionKeys = allTaskMap.keySet();
            Map<String, List<Map<String, Object>>> userSendMessageMap = new HashMap<>();
            for (String definitionKey : definitionKeys) {
                Map<String, List<Map<String, Object>>> taskListMap = allTaskMap.get(definitionKey);
                Set<String> uuids = taskListMap.keySet();
                for (String uuid : uuids) {
                    List<Map<String, Object>> lstTaskMap = taskListMap.get(uuid);
                    List<String> lstUserId = ErpWorkFlowHelper.parseAllAssignee(lstTaskMap, roleUserIdMap);
                    if (lstUserId.size() > 0) {
                        String taskName = ErpWorkFlowHelper.getTaskName(definitionKey, uuid, lstTaskMap.get(0));
                        Map<String, Object> messageMap = lstTaskMap.get(0);
                        messageMap.put(ErpWorkFlowAttribute.ERP_TASK_NAME, taskName);
                        for (String userId : lstUserId) {
                            if (userSendMessageMap.containsKey(userId)) {
                                userSendMessageMap.get(userId).add(messageMap);
                            } else {
                                List<Map<String, Object>> lstTaskMessage = new ArrayList<>();
                                lstTaskMessage.add(messageMap);
                                userSendMessageMap.put(userId, lstTaskMessage);
                            }
                        }
                    }
                }
            }
            Set<String> userIdSet = userSendMessageMap.keySet();
            for (String userId : userIdSet) {
                ReceiptsMeta receiptsMeta = new ReceiptsMeta();
                List<String> _lst = new ArrayList<>();
                _lst.add(userId);
                receiptsMeta.setLstSendToUserId(_lst);
                Map inputMap = new HashMap();
                inputMap.put(ErpWorkFlowAttribute.UNHANDLED_DAYS, dataMap.get(ErpWorkFlowAttribute.UNHANDLED_DAYS));
                inputMap.put(ErpWorkFlowAttribute.ERP_TASKS, userSendMessageMap.get(userId));
                MsgSendHelper.sendMsg(inputMap, msgType, templateNo, receiptsMeta);
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    tracer.error(e.getMessage());
                }
            }
        }
        return "SUCCESS";
    }


    private List<Map<String, Object>> getAllTasks(JobDataMap dataMap) {
        try {
            int days = Integer.valueOf(String.valueOf(dataMap.get(ErpWorkFlowAttribute.UNHANDLED_DAYS)));
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.add(GregorianCalendar.DAY_OF_YEAR, (days - 1) * -1);
            Date startDate = DateUtil.parseDate(DateUtil.formatDate(calendar.getTime(), "yyyy-MM-dd"), "yyyy-MM-dd");
            return ErpWorkFlowHelper.getAllUnhandledTasks(startDate);
        } catch (Exception e) {
            tracer.error(e.getMessage());
            return null;
        }
    }
}