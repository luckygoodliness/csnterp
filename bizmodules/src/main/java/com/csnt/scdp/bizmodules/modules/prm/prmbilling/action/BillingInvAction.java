package com.csnt.scdp.bizmodules.modules.prm.prmbilling.action;

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
 * Created by lijx on 2016/8/17.
 */
@Scope("singleton")
@Controller("prmbilling-billingInv")
@Transactional
public class BillingInvAction  implements IAction {
    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map out = new HashMap();
        String uuids =(String) inMap.get("uuids");
        String sql = "update PRM_BILLING t\n" +
                "   set t.invoice_no = (case\n" +
                "                        when nvl(t.invoice_no, 'null') = 'null' then\n" +
                "                         '已开票'\n" +
                "                        else\n" +
                "                         t.invoice_no\n" +
                "                      end),\n" +
                "       t.invoice_date = (case\n" +
                "                          when t.invoice_date is null then\n" +
                "                           sysdate\n" +
                "                          else\n" +
                "                           t.invoice_date\n" +
                "                        end)\n" +
                " where t.uuid in ("+uuids+")";
        List lstParams =  new ArrayList<>();
//        lstParams.add(uuids);
        DAOMeta daoMeta1 = new DAOMeta(sql,lstParams);
        boolean success =  PersistenceFactory.getInstance().updateByNativeSQL(daoMeta1)>0;
        out.put("success",success);
        return out;
    }
}
