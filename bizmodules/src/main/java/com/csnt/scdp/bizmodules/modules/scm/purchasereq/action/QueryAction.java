package com.csnt.scdp.bizmodules.modules.scm.purchasereq.action;

import com.csnt.scdp.bizmodules.modules.scm.purchasereq.services.intf.PurchasereqManager;
import com.csnt.scdp.framework.commonaction.crud.CommonQueryAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.helper.DAOHelper;
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
@Controller("purchasereq-query")
@Transactional
public class QueryAction extends CommonQueryAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(QueryAction.class);

    @Resource(name = "purchasereq-manager")
    private PurchasereqManager purchasereqManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
//		Map out = super.perform(inMap);
        //Do After
        List lstParam = new ArrayList<>();
        DAOMeta daoMeta = DAOHelper.getDAO("purchasereq-dao", "common_query", lstParam);
        daoMeta.setStrSql(purchasereqManager.processSearchCondition(daoMeta, inMap));
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        List lstReturn = new ArrayList();
        List<Map<String, Object>> listPurchaseReq = pcm.findByNativeSQL(daoMeta);

        if (ListUtil.isNotEmpty(listPurchaseReq)) {
            List lstUuid = new ArrayList();
            listPurchaseReq.forEach(scmPurchaseReq -> {
                lstUuid.add(scmPurchaseReq.get("uuid"));
            });
            DAOMeta daoMeta1 = DAOHelper.getDAO("purchasereq-dao", "detail_info_query", new ArrayList());
            String sql = daoMeta1.getStrSql();
            sql = sql.replace("${selfconditions}", " dt.scm_purchase_req_id in (" + StringUtil.joinForSqlIn(lstUuid, ",") + ")");
            daoMeta1.setStrSql(sql);
            List lstDetailDto = pcm.findByNativeSQL(daoMeta1);
            if (ListUtil.isNotEmpty(lstDetailDto)) {
                lstDetailDto.forEach(detail -> {
                    String scmPurchaseReqId = (String) ((Map) detail).get("scmPurchaseReqId");
                    for (Iterator it = listPurchaseReq.iterator(); it.hasNext(); ) {
                        Map scmPurchaseReq = (Map) it.next();
                        if (scmPurchaseReqId.equals(scmPurchaseReq.get("uuid"))) {
                            if (scmPurchaseReq.containsKey("prmPurchaseReqDetailDto")) {
                                List lstDetail = (List) scmPurchaseReq.get("prmPurchaseReqDetailDto");
                                lstDetail.add(detail);
                            } else {
                                List lstDetail = new ArrayList();
                                lstDetail.add(detail);
                                scmPurchaseReq.put("prmPurchaseReqDetailDto", lstDetail);
                            }
                            break;
                        }
                    }
                });
            }
        }
        Map map = new HashMap();
        map.put("scmPurchaseReqDto", listPurchaseReq);
        return map;
    }
}
