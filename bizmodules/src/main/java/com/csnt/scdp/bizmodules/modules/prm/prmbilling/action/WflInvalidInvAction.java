package com.csnt.scdp.bizmodules.modules.prm.prmbilling.action;

import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.sysmodules.modules.sys.workflow.action.WorkFlowVariableProcessAction;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lijx on 2016/8/22.
 */
@Scope("singleton")
@Controller("prm_invalid-after-fixed")
@Transactional
public class WflInvalidInvAction extends WorkFlowVariableProcessAction {

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map map = new HashedMap();
        String businessKey = (String) inMap.get(WorkFlowAttribute.BUSINESS_KEY);

        String sql = "update PRM_BILLING set state='3' where uuid=?";
        String sql1 = "update PRM_BILLING p set p.state='3' where p.uuid = " +
                " (select p1.repeal_Uuid from PRM_BILLING p1 where p1.uuid=? )";
        List lstParams = new ArrayList<>();
        lstParams.add(businessKey);
        DAOMeta daoMeta = new DAOMeta(sql, lstParams);
        DAOMeta daoMeta1 = new DAOMeta(sql1, lstParams);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta1);
        return map;
    }
}
