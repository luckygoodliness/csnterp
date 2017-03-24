package com.csnt.scdp.bizmodules.modules.fad.fadinterestrate.services.impl;

import com.csnt.scdp.bizmodules.entity.fad.FadInterestRate;
import com.csnt.scdp.bizmodules.modules.fad.fadinterestrate.services.intf.FadinterestrateManager;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Description:  FadinterestrateManagerImpl
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-08-30 10:37:36
 */

@Scope("singleton")
@Service("fadinterestrate-manager")
public class FadinterestrateManagerImpl implements FadinterestrateManager {
    public List<FadInterestRate> getRateListOrderByValidityDateFrom() {
        List<FadInterestRate> result = PersistenceFactory.getInstance().findByAnyFields(FadInterestRate.class, new HashMap(), "validityDateFrom", null);
        return result;
    }
    public List<Map<String, Object>> getReqClearList(String fadCashReqId) {
        String sql = " select *\n" +
                "   from (select t.clearance_money as clearance_money,t.fad_cash_req_id,trunc(f.certificate_create_time, 'dd') as clearance_date\n" +
                "           from fad_invoice f\n" +
                "           left join fad_cash_req_invoice t on f.uuid = t.fad_invoice_id\n" +
                "           where f.state='4'\n"+
                "         union all\n" +
                "         select f.total_money as clearance_money,t.fad_cash_req_id,trunc(f.certificate_create_time, 'dd') as clearance_date\n" +
                "           from fad_cash_clearance f\n" +
                "           left join fad_cash_req_invoice t on f.uuid = t.fad_invoice_id\n" +
                "           where f.state='4') b\n" +
                "  where b.fad_cash_req_id =?\n" +
                "  order by b.clearance_date asc";
        List param = new ArrayList<>();
        param.add(fadCashReqId);
        DAOMeta dao = new DAOMeta(sql, param);
        dao.setNeedFilter(false);
        List<Map<String, Object>> result = PersistenceFactory.getInstance().findByNativeSQL(dao);

        return result;
    }
}