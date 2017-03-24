package com.csnt.scdp.bizmodules.modules.prm.receipts.action;

import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lijx on 2016/9/6.
 */
@Scope("singleton")
@Controller("receipts-billingisnull")
@Transactional
public class CountBillingContractIsNullAction implements IAction {
    @Override
    public Map perform(Map map) throws BizException, SysException {
        String prmProjectMainId = (String)map.get("prmProjectMainId");
        String sql = " select (select nvl(count(1), 0)\n" +
                "          from prm_billing t\n" +
                "         where t.BILL_TYPE = 0\n" +
                "           and t.state <>'3' \n" +
                "           and t.prm_project_main_id = ? \n" +
                "           and t.prm_contract_id is null) +\n" +
                "       (select nvl(count(1), 0)\n" +
                "          from prm_receipts t\n" +
                "         where t.prm_project_main_id = ? \n" +
                "           and t.prm_contract_id is null) as result " +
                "  from dual";
        List list = new ArrayList<>();
        list.add(prmProjectMainId);
        list.add(prmProjectMainId);
        DAOMeta daoMeta1 = new DAOMeta(sql, list);
        List<Map<String,Object>> lst = PersistenceFactory.getInstance().findByNativeSQL(daoMeta1);
        if(lst!=null && lst.size()>0)
            return lst.get(0);
        return null;
    }
}
