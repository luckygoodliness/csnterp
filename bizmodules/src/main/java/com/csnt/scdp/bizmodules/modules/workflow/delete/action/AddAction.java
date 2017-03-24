package com.csnt.scdp.bizmodules.modules.workflow.delete.action;

import ch.qos.logback.core.util.StringCollectionUtil;
import com.csnt.scdp.bizmodules.entity.workflow.WorkFlowDelete;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.workflow.delete.dto.WorkFlowDeleteDto;
import com.csnt.scdp.bizmodules.modules.workflow.delete.services.intf.WorkflowdeleteManager;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.framework.util.UUIDUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Description:  AddAction
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-09-24 14:56:54
 */

@Scope("singleton")
@Controller("workflowdelete-add")
@Transactional
public class AddAction extends CommonAddAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);

    @Resource(name = "workflowdelete-manager")
    private WorkflowdeleteManager workflowdeleteManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        //Do After
        return out;
    }

    @Override
    protected void beforeAction(BasePojo dtoObj) {
        WorkFlowDeleteDto workFlowDeleteDto = (WorkFlowDeleteDto) dtoObj;
        String businessKey = workFlowDeleteDto.getBusinessKey();
        if (StringUtil.isNotEmpty(businessKey)) {
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            //历史表
            String sql = "select s.proc_inst_id_,s.end_time_ from act_hi_procinst  s where s.BUSINESS_KEY_= ? order by s.start_time_ desc";
            DAOMeta daoMeta1 = new DAOMeta();
            daoMeta1.setStrSql(sql);
            List condition1 = new ArrayList<>();
            condition1.add(businessKey);
            daoMeta1.setLstParams(condition1);
            daoMeta1.setNeedFilter(false);
            List<Map<String, Object>> result1 = pcm.findByNativeSQL(daoMeta1, ErpWorkFlowHelper.addScalarMapForProcinst());
            String procInstId = null;
            if (ListUtil.isNotEmpty(result1)) {
                Map procMap = result1.get(0);
                procInstId = (String) procMap.get("procInstId");
            } else {
                throw new BizException("不存在工作流");
            }

            String newBusinessKey = UUIDUtil.getUUID();
            String sql2 = "update act_hi_procinst s set s.business_key_=? , s.end_time_=? where s.proc_inst_id_=?";
            DAOMeta daoMeta2 = new DAOMeta();
            daoMeta2.setStrSql(sql2);
            daoMeta2.setNeedFilter(false);
            List condition2 = new ArrayList<>();
            condition2.add(newBusinessKey);
            condition2.add(new Date());
            condition2.add(procInstId);
            daoMeta2.setLstParams(condition2);
            pcm.updateByNativeSQL(daoMeta2);

            String sql3 = "update act_ru_execution s set s.business_key_ = ? where s.proc_inst_id_=?";
            DAOMeta daoMeta3 = new DAOMeta();
            daoMeta3.setStrSql(sql3);
            List condition3 = new ArrayList<>();
            condition3.add(newBusinessKey);
            condition3.add(procInstId);
            daoMeta3.setLstParams(condition3);
            daoMeta3.setNeedFilter(false);
            pcm.updateByNativeSQL(daoMeta3);

            String sql7 = "select s.assignee_ from act_ru_task  s where s.proc_inst_id_= ?";
            DAOMeta daoMeta7 = new DAOMeta();
            daoMeta7.setStrSql(sql7);
            List condition7 = new ArrayList<>();
            condition7.add(procInstId);
            daoMeta7.setLstParams(condition7);
            daoMeta7.setNeedFilter(false);
            List<Map<String, Object>> result7 = pcm.findByNativeSQL(daoMeta7, ErpWorkFlowHelper.addScalarMapForRuTask());
            String ruAssignee = null;
            if (ListUtil.isNotEmpty(result7)) {
                Map ruTaskMap = result7.get(0);
                ruAssignee = (String) ruTaskMap.get("assignee");
            }

            String sql4 = "update act_ru_task s set s.assignee_ = ? where s.proc_inst_id_=?";
            DAOMeta daoMeta4 = new DAOMeta();
            daoMeta4.setStrSql(sql4);
            List condition4 = new ArrayList<>();
            condition4.add(newBusinessKey);
            condition4.add(procInstId);
            daoMeta4.setLstParams(condition4);
            daoMeta4.setNeedFilter(false);
            pcm.updateByNativeSQL(daoMeta4);

            String sql5 = "select s.assignee_,s.name_ from act_hi_taskinst s where s.proc_inst_id_=? order by s.start_time_";
            DAOMeta daoMeta5 = new DAOMeta();
            daoMeta5.setStrSql(sql5);
            List condition5 = new ArrayList<>();
            condition5.add(procInstId);
            daoMeta5.setLstParams(condition5);
            daoMeta5.setNeedFilter(false);
            List<Map<String, Object>> result5 = pcm.findByNativeSQL(daoMeta5, ErpWorkFlowHelper.addScalarMapForTaskinst());
            StringBuffer assigneeList = new StringBuffer();
            if (ListUtil.isNotEmpty(result5)) {
                for (Map result : result5) {
                    String name = (String) result.get("name");
                    String assignee = (String) result.get("assignee");
                    if (StringUtil.isNotEmpty(name)) {
                        assigneeList.append(name);
                    } else {
                        assigneeList.append("null");
                    }
                    assigneeList.append(":");

                    if (StringUtil.isNotEmpty(assignee)) {
                        assigneeList.append(assignee);
                    } else {
                        assigneeList.append("null");
                    }
                    assigneeList.append(";");
                }
            }

            String sql6 = "update act_hi_taskinst s set s.assignee_ = ? where s.proc_inst_id_=?";
            DAOMeta daoMeta6 = new DAOMeta();
            daoMeta6.setStrSql(sql6);
            List condition6 = new ArrayList<>();
            condition6.add(newBusinessKey);
            condition6.add(procInstId);
            daoMeta6.setLstParams(condition6);
            daoMeta6.setNeedFilter(false);
            pcm.updateByNativeSQL(daoMeta6);

            workFlowDeleteDto.setNewBusinessKey(newBusinessKey);
            workFlowDeleteDto.setRuAssignee(ruAssignee);
            if (assigneeList != null) {
                workFlowDeleteDto.setAssigneeRecord(assigneeList.toString());
            }

        }
    }
}
