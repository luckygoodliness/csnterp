package com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.action;

import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.services.intf.PrmpurchasereqManager;
import com.csnt.scdp.framework.commonaction.crud.CommonQueryAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  QueryAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 15:30:52
 */

@Scope("singleton")
@Controller("prmpurchasereq-planquery")
@Transactional
public class QueryPlanAction extends CommonQueryAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(QueryPlanAction.class);

    @Resource(name = "prmpurchasereq-manager")
    private PrmpurchasereqManager prmpurchasereqManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {

        StringBuffer selfSql = new StringBuffer();
        Map condMap = (Map) inMap.get("queryConditions");
        List duuids = (List) condMap.get("duuids");
        String uuidCon = "(";
        for (int i = 0; i < duuids.size(); i++){
            String uuid = (String) duuids.get(i);
            if (i == duuids.size() - 1){
                uuidCon += "'" + uuid + "')";
            }else {
                uuidCon += "'" + uuid + "',";
            }
        }

        if (StringUtil.isNotEmpty(uuidCon)){
            selfSql.append(" AND PL.UUID in " + uuidCon);
        }

        Map out = new HashMap();
        Map cond = new HashMap();
        cond.put("qryCondition", selfSql.toString());
        DAOMeta daoMeta = DAOHelper.getDAO("prmpurchasereq-dao", "query_load_plan", null, cond);
        List<Map<String, Object>> lstChange = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        //M3_C5_F2_页面调整
        if (ListUtil.isNotEmpty(lstChange)) {
            out.put ("PrmPurchasePlanDetailDto",lstChange);
            out.put("hasDat",true);
        }else {
            out.put("hasDat",false);
        }
        return out;
    }
}
