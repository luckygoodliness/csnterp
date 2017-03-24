package com.csnt.scdp.bizmodules.modules.workflow.prm.purchasereq;

import com.csnt.scdp.bizmodules.attributes.ErpWorkFlowAttribute;
import com.csnt.scdp.bizmodules.entity.prm.PrmPurchaseReq;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.workflow.intf.IWorkFlow;
import com.csnt.scdp.framework.util.ListUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.persistence.Persistence;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2016/2/23.
 */
@Scope("singleton")
@Controller("workflow-load-user-prm_purchase_request")
@Transactional

public class PrmPurchaseRequestLoadingAssigneeAction implements IAction {

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map out = new HashMap();
        String uuid = (String) inMap.get("businessKey");
        String sql = "select su.user_id,su.user_name,su.org_uuid,su.uuid,sr.role_name,so.org_name,so.org_code\n" +
                "  from prm_member_detail_p pmdp\n" +
                "  left join scdp_role sr\n" +
                "    on sr.uuid = pmdp.post  left join scdp_user  su on su.user_id=pmdp.staff_id left join scdp_org so on su.org_uuid=so.uuid\n" +
                " where pmdp.prm_project_main_id in\n" +
                "       (select ppr.prm_project_main_id\n" +
                "          from prm_purchase_req ppr\n" +
                "         where ppr.uuid = ?) and sr.role_name=?";

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
