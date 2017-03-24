package com.csnt.scdp.bizmodules.modules.workflow.scm.scmnewcontractchange;

import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2016/2/23.
 */
@Scope("singleton")
@Controller("workflow-load-user-scm_purchase_change")
@Transactional

public class ScmNewContractLoadingAssigneeAction implements IAction {

//    private static ILogTracer tracer = LogTracerFactory.getInstance(WorkFlowCustomizedLoadingAssigneeAction.class);

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map out = new HashMap();
        String uuid = (String) inMap.get("businessKey");
        String sql1 = "SELECT SC.CREATE_BY,SCC.OFFICE_ID\n" +
                      "FROM SCM_CONTRACT_CHANGE SCC\n" +
                      "LEFT JOIN SCM_CONTRACT SC ON SCC.SCM_CONTRACT_ID = SC.UUID\n" +
                      "WHERE SCC.UUID = '"+uuid+"'";
        DAOMeta daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql1);
        daoMeta.setNeedFilter(false);
        List<Map<String, Object>> contractInfoLst = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        Map<String, Object> contractInfo = contractInfoLst.get(0);
        String createBy = contractInfo.get("createBy").toString();
        String officeId = contractInfo.get("officeId").toString();
        String sql2 = "SELECT VURN.user_id,\n" +
                "       VURN.user_name,\n" +
                "       VURN.ORG_UUID,\n" +
                "       VURN.user_uuid AS UUID,\n" +
                "       VURN.role_name,\n" +
                "       VURN.org_name,\n" +
                "       VURN.org_code\n" +
                "  FROM V_USER_ROLES_N VURN\n" +
                " WHERE VURN.role_name = '供应链部采购经理'\n" +
                " AND VURN.user_id = '"+createBy+"'";
        daoMeta.setStrSql(sql2);
        List result = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if(result.size() > 0) {
            out.put("root", result);
            out.put("totalProperty", result == null ? 0 : result.size());
        } else {
            String sql3 = "select vurn.user_id,\n" +
                    "         vurn.user_name,\n" +
                    "         vurn.org_uuid,\n" +
                    "         vurn.user_uuid as uuid,\n" +
                    "         vurn.role_name,\n" +
                    "         vurn.org_name,\n" +
                    "         vurn.org_code\n" +
                    "    from v_user_roles_n vurn\n" +
                    "   where vurn.role_name = '供应链部采购经理'\n" +
                    "     and vurn.user_id in (SELECT TT.principal\n" +
                    "                            FROM (select srd.principal\n" +
                    "                                    from scm_responsible_department srd\n" +
                    "                                   where srd.responsible_department = '"+officeId+"'\n" +
                    "                                   order by srd.create_time desc) TT\n" +
                    "                           WHERE ROWNUM = 1)";
            daoMeta.setStrSql(sql3);
            List res = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            if(res.size() > 0) {
                out.put("root", res);
                out.put("totalProperty", res == null ? 0 : res.size());
            } else {
                String sql4 = "select vurn.user_id,\n" +
                        "         vurn.user_name,\n" +
                        "         vurn.org_uuid,\n" +
                        "         vurn.user_uuid as uuid,\n" +
                        "         vurn.role_name,\n" +
                        "         vurn.org_name,\n" +
                        "         vurn.org_code\n" +
                        "    from v_user_roles_n vurn\n" +
                        "   where vurn.role_name = '供应链部采购经理'";
                daoMeta.setStrSql(sql4);
                List lst = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                out.put("root", lst);
                out.put("totalProperty", lst == null ? 0 : lst.size());
            }
        }

        return out;
    }
}
