package com.csnt.scdp.bizmodules.modules.scm.purchasereq.action;

import com.csnt.scdp.bizmodules.modules.scm.purchasereq.services.intf.PurchasereqManager;
import com.csnt.scdp.bizmodules.modules.scm.scmcontract.dto.ScmContractDto;
import com.csnt.scdp.framework.bo.QueryCondition;
import com.csnt.scdp.framework.commonaction.crud.CommonQueryAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Description:  QueryAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 14:46:34
 */

@Scope("singleton")
@Controller("scmcontract-detail-query")
@Transactional
public class QueryScmContractDetailAction extends CommonQueryAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(QueryScmContractDetailAction.class);

    @Resource(name = "purchasereq-manager")
    private PurchasereqManager purchasereqManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        String scmPurchaseReqUuid = (String) inMap.get("scmPurchaseReqUuid");
        Map map = new HashMap();
        if (StringUtil.isNotEmpty(scmPurchaseReqUuid)) {
            String sql = "select distinct scm_contract_id from prm_purchase_req_detail where scm_contract_id is not null and scm_purchase_req_id = ?";
            List lstParam = new ArrayList();
            lstParam.add(scmPurchaseReqUuid);
            DAOMeta daoMeta = new DAOMeta(sql, lstParam);
            List lstResult = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            List lstScmContractUuid = new ArrayList();
            if (ListUtil.isNotEmpty(lstResult)) {
                for (Iterator<Map> it = lstResult.iterator(); it.hasNext(); ) {
                    Map mapResult = it.next();
                    lstScmContractUuid.add(mapResult.get("scmContractId"));
                }
                QueryCondition queryCondition = new QueryCondition("uuid", "in", null);
                queryCondition.setValueList(lstScmContractUuid);
                QueryCondition queryCondition1 = new QueryCondition("state", "in", null);
                List lstStateValue = new ArrayList();
                lstStateValue.add("0");
                lstStateValue.add("5");
                queryCondition1.setValueList(lstStateValue);
//                QueryCondition queryCondition2 = new QueryCondition("is_virtual", "=", null);
//                List lstVirtual = new ArrayList();
//                lstVirtual.add(1);
//                queryCondition2.setValueList(lstVirtual);
                List lstParam1 = new ArrayList();
                lstParam1.add(queryCondition);
                lstParam1.add(queryCondition1);
//                lstParam1.add(queryCondition2);
                List list = DtoHelper.findByAnyFields(ScmContractDto.class, lstParam1, "uuid");
                map.put("scmContractDto", list);
            }
        }
        return map;
    }

}
