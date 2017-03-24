package com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.action;

import com.csnt.scdp.bizmodules.entity.prm.PrmPurchaseReqDetail;
import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.services.intf.PrmpurchasereqManager;
import com.csnt.scdp.framework.bo.QueryCondition;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
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
 * @timestamp 2015-09-26 13:30:43
 */

@Scope("singleton")
@Controller("prmpurchasereq-signproject")
@Transactional
public class SignProjectAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(SignProjectAction.class);

    @Resource(name = "prmpurchasereq-manager")
    private PrmpurchasereqManager prmpurchasereqManager;

    @Override
    public Map perform(Map map) throws BizException, SysException {

        Map inMap = new HashMap<>();
        String prmPurchaseReqDetailUuid = (String) map.get("prmPurchaseReqDetailUuids");
        List prmPurchaseReqDetailUuidsList = Arrays.asList(prmPurchaseReqDetailUuid.split(","));
        String signProjectUuid = (String) map.get("signProjectUuid");
        String signType = (String) map.get("signType");
        QueryCondition condition = new QueryCondition();
        condition.setField("UUID");
        condition.setOperator("in");
        condition.setValueList(prmPurchaseReqDetailUuidsList);
        List<QueryCondition> listCondition = new ArrayList<QueryCondition>();
        listCondition.add(condition);
        if ("1".equals(signType)) {
            List<PrmPurchaseReqDetail> list = PersistenceFactory.getInstance().findByAnyFields(PrmPurchaseReqDetail.class, listCondition, null);
            for (PrmPurchaseReqDetail prmPurchaseReqDetail : list) {
                prmPurchaseReqDetail.setStampProjectUuid(signProjectUuid);
                prmPurchaseReqDetail.setIsStamp("1");
            }
            PersistenceFactory.getInstance().batchUpdate(list);

        } else {
            List<PrmPurchaseReqDetail> list = PersistenceFactory.getInstance().findByAnyFields(PrmPurchaseReqDetail.class, listCondition, null);
            for (PrmPurchaseReqDetail prmPurchaseReqDetail : list) {
                prmPurchaseReqDetail.setStampProjectUuid(null);
            }
            PersistenceFactory.getInstance().batchUpdate(list);
        }
        return inMap;
    }
}
