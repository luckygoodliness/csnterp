package com.csnt.scdp.bizmodules.modules.workflow.prm.finalestimate;

import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.sysmodules.helper.FMCodeHelper;
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
@Controller("workflow-load-user-prm_final_estimates")
@Transactional

public class FinalEstimateLoadingAssigneeAction implements IAction {

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map out = new HashMap();
        String deptCode =(String) inMap.get("processDeptCode");
        String accountManager= FMCodeHelper.getDescByCode(deptCode,"ACCOUNT_MANAGER");
        String sql="select su.user_id,\n" +
                "                su.user_name,\n" +
                "                su.org_uuid,\n" +
                "                su.uuid,\n" +
                "                v.role_name,\n" +
                "                so.org_name,\n" +
                "                so.org_code\n" +
                "           from scdp_user su\n" +
                "           left join scdp_org so\n" +
                "             on so.uuid = su.org_uuid\n" +
                "           left join v_user_roles v\n" +
                "             on v.user_id = su.user_id        \n" +
                "          where su.user_id = ?\n" +
                "            and v.role_name = ?";

        DAOMeta daoMeta=new DAOMeta();
        daoMeta.setStrSql(sql);
        List condition=new ArrayList<>();
        condition.add(accountManager);
        condition.add("内部核算管理员");
        daoMeta.setLstParams(condition);
        daoMeta.setNeedFilter(false);
        List lst=PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        out.put("root",lst);
        out.put("totalProperty",lst==null?0:lst.size());
        return out;
    }
}
