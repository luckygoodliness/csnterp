package com.csnt.scdp.bizmodules.modules.workflow;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.csnt.scdp.bizmodules.attributes.ErpMobileDtoAttribute;
import com.csnt.scdp.bizmodules.attributes.ErpWorkFlowAttribute;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.helper.DBHelper;
import com.csnt.scdp.framework.helper.SysconfigHelper;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.JsonUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.helper.OnlineUserHelper;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Wuhd on 2015/11/9.
 */
@Scope("singleton")
@Controller("work-flow-common-after-process-action")
@Transactional

public class WorkFlowUpdateStateAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(WorkFlowUpdateStateAction.class);

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map map = new HashedMap();
        Boolean skipThisAction = (Boolean) inMap.get(WorkFlowAttribute.SKIP_COMMON_PROCESS_AFTER_ACTION);
        if (skipThisAction != null && skipThisAction) {
            return map;
        }
        map.putAll(updateState(inMap));

        String workFlowDefinitionKey = (String) inMap.get(WorkFlowAttribute.WORKFLOW_DEFINITION_KEY);
        if (ErpMobileDtoAttribute.definitionKey2Dto.containsKey(workFlowDefinitionKey + "_Dto")) {
            ErpWorkFlowHelper.pushMsgToMobile(inMap);
        }
        return map;
    }

    private Map updateState(Map inMap) {
        Map map = new HashedMap();
        String businessKey = (String) inMap.get(WorkFlowAttribute.BUSINESS_KEY);
        String dto = (String) inMap.get(WorkFlowAttribute.DTO);
        String tableName = BeanUtil.getTableNameByPojoClass(BeanUtil.getPojoClassByDto(BeanFactory.getClass(dto))).toLowerCase();
        String subMethod = (String) inMap.get("subMethod");
        Integer state = -1;
        if ("complete".equals(subMethod)) {
            String status = (String) inMap.get("status");
            if ("PROCESSING".equals(status)) {
                state = 1;//已提交
            } else if ("FIXED".equals(status)) {
                state = 2;//已审核
            }
        } else if ("reject".equals(subMethod)) {
            Boolean closed = (Boolean) inMap.get("wf_status_closed");
            if (closed != null && closed) {
                state = 5;//如果回到的是第一步，状态改成已退回
            } else {
                state = 9;//如果回到的不是第一步，状态改成已提交（退回）
            }
            List userList = (List) inMap.get(WorkFlowAttribute.WF_REJECTION_MSG_RECEIVERS_BETWEEN_CURR_AND_BACK2);
            if (ListUtil.isNotEmpty(userList)) {
                ErpWorkFlowHelper.sendMsgToRollback(inMap, userList);
            }

        } else if ("start".equals(subMethod)) {
            state = 1;//已提交
        } else if ("cancel".equals(subMethod)) {
            Boolean closed = (Boolean) inMap.get("wf_status_closed");
            if (closed != null && closed) {
                state = 0;//如果回到的是第一步，状态改成新增
            } else {
                state = 1;//如果回到的不是第一步，状态改成提交
            }
        }
        if (StringUtil.isNotEmpty(tableName) && state != -1) {
            List lstColumns = DBHelper.getTableMetaFromDB(tableName);

            if (ListUtil.isNotEmpty(lstColumns)) {
                for (Iterator it = lstColumns.iterator(); it.hasNext(); ) {
                    Map mapColumn = (Map) it.next();
                    if ("state".toUpperCase().equals(mapColumn.get("COLUMN_NAME"))) {
                        String sql = "update " + tableName + " set state = ? where uuid = ?";
                        DAOMeta daoMeta = new DAOMeta();
                        daoMeta.setStrSql(sql);
                        List lstParam = new ArrayList();
                        lstParam.add(state);
                        lstParam.add(businessKey);
                        daoMeta.setLstParams(lstParam);
                        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
                        pcm.updateByNativeSQL(daoMeta);
                        map.put(ErpWorkFlowAttribute.STATE, state);
                        break;
                    }
                }
            }
        }
        return map;
    }


}
