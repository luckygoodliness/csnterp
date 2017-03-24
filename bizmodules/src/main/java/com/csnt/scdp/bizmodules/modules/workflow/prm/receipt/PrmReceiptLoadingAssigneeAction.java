package com.csnt.scdp.bizmodules.modules.workflow.prm.receipt;

import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
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
@Controller("workflow-load-user-prm_receipt")
@Transactional

public class PrmReceiptLoadingAssigneeAction implements IAction {

//    private static ILogTracer tracer = LogTracerFactory.getInstance(WorkFlowCustomizedLoadingAssigneeAction.class);

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map out = new HashMap();
        String uuid = (String) inMap.get("businessKey");
        String sql = "select su.user_id,\n" +
                "       su.user_name,\n" +
                "       su.org_uuid,\n" +
                "       su.uuid,\n" +
                "       sr.role_name,\n" +
                "       so.org_name,\n" +
                "       so.org_code\n" +
                "  from prm_receipts pr\n" +
                "  left join prm_contract pc\n" +
                "    on pc.uuid = pr.prm_contract_id\n" +
                "  left join prm_purchase_req ppr\n" +
                "    on ppr.uuid = pc.inner_purchase_req_id\n" +
                "  left join prm_member_detail_p pmdp\n" +
                "    on pmdp.prm_project_main_id = ppr.prm_project_main_id\n" +
                "  left join scdp_role sr\n" +
                "    on sr.uuid = pmdp.post\n" +
                "  left join scdp_user su\n" +
                "    on su.user_id = pmdp.staff_id\n" +
                "  left join scdp_org so\n" +
                "    on su.org_uuid = so.uuid\n" +
                " where pr.uuid = ? and sr.role_name= ?";

        DAOMeta daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        List condition = new ArrayList<>();
        condition.add(uuid);
        condition.add("项目经理");
        daoMeta.setLstParams(condition);
        daoMeta.setNeedFilter(false);
        List lst = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        out.put("root", lst);
        out.put("totalProperty", lst == null ? 0 : lst.size());
        return out;
    }
}
