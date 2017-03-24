package com.csnt.scdp.bizmodules.modules.common.action;

import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.commonaction.crud.CommonDeleteAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.StringUtil;

import java.sql.Timestamp;
import java.util.*;

/**
 * Description:  ErpDeleteAction
 * Copyright: © 2015 Aardwolf Studio. All rights reserved.
 * Company:CSNT中海网络科技股份有限公司
 *
 * @author liujingyuan
 * @version 1.0
 */
public class ErpDeleteAction extends CommonDeleteAction {
    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map outMap = new HashMap();

        Object uuidsObj = inMap.get(CommonAttribute.UUIDS);
        if (uuidsObj instanceof List) {
            outMap = super.perform(inMap);
        } else if (uuidsObj instanceof Map) {
            List lstUuids = (List) ((Map) uuidsObj).get(CommonAttribute.UUIDS);
            String definitionKey = (String) ((Map) uuidsObj).get(WorkFlowAttribute.WORKFLOW_DEFINITION_KEY);
            inMap.put(CommonAttribute.UUIDS, lstUuids);
            outMap = super.perform(inMap);
            resolveWorkflow(definitionKey, lstUuids);
        }
        return outMap;
    }

    private void resolveWorkflow(String definitionKey, List lstUuids) {
        List lstParam = new ArrayList<>();
        lstParam.add(new Timestamp(new Date().getTime()));
        lstParam.add("DELETE");
        String sqlHisTask = "update act_hi_taskinst set end_time_ =?, delete_reason_=?" +
                " where end_time_ is null and  proc_inst_id_ in" +
                "(select proc_inst_id_ from act_ru_execution " +
                "where business_key_ in (" + StringUtil.joinForSqlIn(lstUuids, ",") + ") " +
                "and proc_def_id_ like '" + definitionKey + ":%') ";

        String sqlActRun = "delete from act_ru_task where proc_inst_id_ in " +
                "( select proc_inst_id_ from act_hi_procinst " +
                "where  business_key_ in (" + StringUtil.joinForSqlIn(lstUuids, ",") + ") " +
                "and proc_def_id_ like '" + definitionKey + ":%' " +
                "and end_time_ is null)";

        String sqlHisProc = "update act_hi_procinst set end_time_ =?, delete_reason_=?" +
                "where business_key_ in (" + StringUtil.joinForSqlIn(lstUuids, ",") + ") " +
                "and proc_def_id_ like '" + definitionKey + ":%' " +
                "and end_time_ is null";

//        String sqlExecution="delete from act_ru_execution " +
//                "where business_key_ in ("+ StringUtil.joinForSqlIn(lstUuids,",")+") " +
//                "and proc_def_id_ like '"+definitionKey+":%'";

        DAOMeta daoMeta1 = new DAOMeta(sqlHisTask, lstParam);
        DAOMeta daoMeta2 = new DAOMeta(sqlActRun, null);
        DAOMeta daoMeta3 = new DAOMeta(sqlHisProc, lstParam);
//        DAOMeta daoMeta4=new DAOMeta(sqlExecution,null);

        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta1);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta2);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta3);
//        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta4);
    }

    protected Object getDeleteUuids(Map inMap) {
        Object uuidsObj = inMap.get(CommonAttribute.UUIDS);
        if (uuidsObj instanceof Map) {
            List lstUuids = (List) ((Map) uuidsObj).get(CommonAttribute.UUIDS);
            return lstUuids;
        }
        return uuidsObj;
    }
}

