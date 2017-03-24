package com.csnt.scdp.bizmodules.modules.workflow.message;

import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.schedule.job.BaseQuartzJob;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.sysmodules.entityattributes.ScdpMsgAttribute;
import com.csnt.scdp.sysmodules.entityattributes.ScdpMsgTemplateAttribute;
import com.csnt.scdp.sysmodules.helper.MsgSendHelper;
import com.csnt.scdp.sysmodules.modules.sys.msgcenter.dto.ReceiptsMeta;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import java.util.*;

/**
 * Created by linzp on 2016/8/10.
 */
public class ProjectReceiptsReminderJob extends BaseQuartzJob {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ProjectReceiptsReminderJob.class);

    @Override
    protected String executeInternalBiz(JobExecutionContext context) {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        if (dataMap != null) {
            // 获取所有 项目收款
            String sql = "SELECT T.*, M.PROJECT_CODE　\n" +
                    "  FROM (SELECT *\n" +
                    "          FROM PRM_RECEIPTS PR\n" +
                    "         WHERE PR.STATE = '1'\n" +
                    "           AND IS_INTERNAL != 1) T\n" +
                    "  LEFT JOIN PRM_PROJECT_MAIN M\n" +
                    "    ON T.PRM_PROJECT_MAIN_ID = M.UUID";// 流程状态 已提交 内委的不需要
            DAOMeta daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            List<Map<String, Object>> receiptsInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            String msgType = dataMap.getString(ScdpMsgTemplateAttribute.MSG_TYPE);
            String templateNo = dataMap.getString(ScdpMsgTemplateAttribute.MSG_TMPLNO);
            receiptsInfo.forEach(record -> {
                Date estimateDate = (Date) record.get("estimateDate");
                Date now = new Date();
                if (estimateDate != null && now.after(estimateDate)) {
                    // 获取申请人
                    List<String> userIdLst = new ArrayList<>();
                    ReceiptsMeta receiptsMeta = new ReceiptsMeta();
                    String userId = (String) record.get("createBy");
                    userIdLst.add(userId);
                    receiptsMeta.setLstSendToUserId(userIdLst);

                    Map map = new HashMap<>();
                    map.put("projectCode", (String) record.get("projectCode"));
                    map.put("uuid", (String) record.get("uuid"));
                    MsgSendHelper.sendMsg(map, msgType, templateNo, receiptsMeta);
                }
            });
        }
        return "SUCCESS";
    }
}
