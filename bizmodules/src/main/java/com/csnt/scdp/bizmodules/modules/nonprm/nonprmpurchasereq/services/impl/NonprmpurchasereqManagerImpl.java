package com.csnt.scdp.bizmodules.modules.nonprm.nonprmpurchasereq.services.impl;

import com.csnt.scdp.bizmodules.entity.prm.PrmPurchaseReq;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmPurchaseReqAttribute;
import com.csnt.scdp.bizmodules.modules.nonprm.nonprmpurchasereq.services.intf.NonprmpurchasereqManager;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  NonprmpurchasereqManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-11-19 14:00:26
 */

@Scope("singleton")
@Service("nonprmpurchasereq-manager")
public class NonprmpurchasereqManagerImpl implements NonprmpurchasereqManager {

    public void submit(String uuid) {
        PrmPurchaseReq rs = PersistenceFactory.getInstance().findByPK(PrmPurchaseReq.class, uuid);
        if (rs != null) {
            rs.setState(PrmPurchaseReqAttribute.STATE_VALUE_1);
            PersistenceFactory.getInstance().update(rs);
        } else {
            throw new BizException("记录不存在");
        }
    }

    public void audit(String isPro, String uuid) {
        List lastParam = new ArrayList();
        lastParam.add(isPro);
        lastParam.add(uuid);
        String message = (PersistenceFactory.getInstance().getDataFromProcOrFunc("call PKG_SCM.SP_PURCHASE_PRM_TO_SCM(?,?,?)", lastParam, PersistenceCrudManager.RETURN_DATA_TYPE.VARCHAR)).toString();
        Util.println(message);
    }


    //校验预算是否超过
    public boolean checkExcessBudget(Map inMap) {
        String uuid = (String) inMap.get(WorkFlowAttribute.BUSINESS_KEY);

        PrmPurchaseReq prmPurchaseReq = PersistenceFactory.getInstance().findByPK(PrmPurchaseReq.class, uuid);

        boolean bRtn = true;
        if (prmPurchaseReq != null && StringUtil.isNotEmpty(prmPurchaseReq.getBugdetId())) {
            bRtn = getCheckResult(prmPurchaseReq);
        }

        return bRtn;
    }

    private boolean getCheckResult(PrmPurchaseReq prmPurchaseReq) {
        boolean rtn = true;
        DAOMeta daoMeta;
        String sql;
        String remainBudget = "0";
        String reqBudget = "0";
        List lstObjInfo;
        if (prmPurchaseReq.getSubjectCode().endsWith("-固")) {
            Map mapCond = new HashMap();
            mapCond.put("qryCondition", prmPurchaseReq.getUuid());
            daoMeta = DAOHelper.getDAO("nonprmpurchasereq-dao", "query_check_fixed_assets", null, mapCond);
            daoMeta.setNeedFilter(false);
            lstObjInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            if (ListUtil.isNotEmpty(lstObjInfo)) {
                for (Object ob : lstObjInfo) {
                    remainBudget = ((Map) ob).get("remain").toString();
                    if (new BigDecimal(remainBudget).compareTo(BigDecimal.ZERO) > 0) {
                        rtn = false;
                        break;
                    }
                }
            }
        } else {
            daoMeta = new DAOMeta();
            sql = " SELECT * FROM VW_NON_BUDGET_EXECUTE t " +
                    " WHERE T.UUID ='" + prmPurchaseReq.getBugdetId() + "'";
            daoMeta.setStrSql(sql);
            lstObjInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            if (ListUtil.isNotEmpty(lstObjInfo)) {
                remainBudget = ((Map) lstObjInfo.get(0)).get("remainBudget").toString();
            }

            sql = "SELECT " +
                    " NVL(SUM(RD.AMOUNT * RD.EXPECTED_PRICE),'0') TOTAL " +
                    " FROM PRM_PURCHASE_REQ_DETAIL RD " +
                    " WHERE RD.PRM_PURCHASE_REQ_ID ='" + prmPurchaseReq.getUuid() + "'";
            daoMeta.setStrSql(sql);
            lstObjInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            if (ListUtil.isNotEmpty(lstObjInfo)) {
                reqBudget = ((Map) lstObjInfo.get(0)).get("total").toString();
            }

            if ((new BigDecimal(reqBudget).subtract(new BigDecimal(remainBudget))).compareTo(BigDecimal.ZERO) > 0) {
                rtn = false;
            }
        }
        return rtn;
    }
}