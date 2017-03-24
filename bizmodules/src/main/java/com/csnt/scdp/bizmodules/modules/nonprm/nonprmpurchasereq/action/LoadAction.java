package com.csnt.scdp.bizmodules.modules.nonprm.nonprmpurchasereq.action;

import com.csnt.scdp.bizmodules.modules.common.action.ErpLoadAction;
import com.csnt.scdp.bizmodules.modules.nonprm.nonprmpurchasereq.services.intf.NonprmpurchasereqManager;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.apache.commons.collections.MapUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  LoadAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-11-19 14:00:14
 */

@Scope("singleton")
@Controller("nonprmpurchasereq-load")
@Transactional
public class LoadAction extends ErpLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

    @Resource(name = "nonprmpurchasereq-manager")
    private NonprmpurchasereqManager nonprmpurchasereqManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        String uuid = (String) inMap.get("uuid");

        Map map = new HashMap();
        map.put("selfconditions", " REQ.UUID = '" + uuid + "'");
        map.put("selfconditions2", " 1=1 ");
        DAOMeta daoMeta = DAOHelper.getDAO("nonprmpurchasereq-dao", "common_query", new ArrayList(), map);
        List<Map<String, Object>> lstChange = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(lstChange)) {
            ((Map) out.get("prmPurchaseReqDto")).put("stateNm", lstChange.get(0).get("stateNm"));
            ((Map) out.get("prmPurchaseReqDto")).put("financialSubjectCode", lstChange.get(0).get("financialSubjectCode"));
            ((Map) out.get("prmPurchaseReqDto")).put("financialSubject", lstChange.get(0).get("financialSubject"));

            ((Map) out.get("prmPurchaseReqDto")).put("checkRemainMoney", lstChange.get(0).get("remainMoney"));
            ((Map) out.get("prmPurchaseReqDto")).put("departmentCodeDesc", OrgHelper.getOrgNameByCode((String) lstChange.get(0).get("departmentCode")));
        }

        ArrayList<Map> detailList = (ArrayList<Map>) ((Map) out.get("prmPurchaseReqDto")).get("prmPurchaseReqDetailDto");
        if (ListUtil.isNotEmpty(detailList)) {
            for (Map mapTemp : detailList) {
                if (mapTemp.get("technicalDrawing") != null && mapTemp.get("technicalDrawing") != "") {
                    mapTemp.put("isUploaded", "有");
                }
            }
        }

        Map mapCond = new HashMap();
        mapCond.put("qryCondition", uuid);
        daoMeta = DAOHelper.getDAO("nonprmpurchasereq-dao", "query_load_reqdetail", null, mapCond);
        daoMeta.setNeedFilter(false);
        lstChange = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);

        if (ListUtil.isNotEmpty(lstChange) && ListUtil.isNotEmpty(detailList)) {
            for (Map mapTemp : detailList) {
                for (Map tmp : lstChange) {
                    if (((Map) tmp).get("uuid").toString().equals(mapTemp.get("uuid").toString())) {
                        mapTemp.put("planAmount", tmp.get("remainAmount"));
                        mapTemp.put("purchaseBudgetMoney", tmp.get("remainPrice"));
                        mapTemp.put("contractState", tmp.get("contractState"));
                        mapTemp.put("undertaker", tmp.get("undertaker"));
                        mapTemp.put("price", tmp.get("price"));
                        break;
                    }
                }
            }
        }
        //Do After
        return out;
    }
}
