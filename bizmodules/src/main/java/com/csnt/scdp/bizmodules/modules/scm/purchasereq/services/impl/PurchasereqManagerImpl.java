package com.csnt.scdp.bizmodules.modules.scm.purchasereq.services.impl;

import com.csnt.scdp.bizmodules.entity.prm.PrmPurchaseReqDetail;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmPurchaseReqAttribute;
import com.csnt.scdp.bizmodules.modules.scm.purchasereq.services.intf.PurchasereqManager;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.UserHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MapUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Description:  PurchasereqManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 14:46:35
 */

@Scope("singleton")
@Service("purchasereq-manager")
public class PurchasereqManagerImpl implements PurchasereqManager {
    public String processSearchCondition(DAOMeta daoMeta, Map inMap) {
        String sql = daoMeta.getStrSql();
        Map<String, Object> condMap = (Map) inMap.get("queryConditions");
        if (MapUtil.isEmpty(condMap)) {
            sql = sql.replace("${conditions}", " 1=1 ");
            sql = sql.replace("${selfconditions}", " exists (select 1 from prm_purchase_req_detail pp " +
                    "where pp.scm_purchase_req_id = scm.uuid " +
                    "and (pp.scm_contract_code is null or " +
                    "(pp.scm_contract_code not like 'L%' " +
                    "and pp.scm_contract_code not like 'S%' " +
                    "and pp.scm_contract_code not like 'W%' " +
                    "and pp.scm_contract_code not like 'Z%'))) ");
        } else {
            String selfSql = " 1=1 ";
            String state = (String) condMap.get("state");
//            if (StringUtil.isNotEmpty(state)) {
//                if ("1".equals(state.toString())) {
//                    selfSql += " and exists (select uuid from prm_purchase_req_detail prmdetail where prmdetail.scm_purchase_req_id = scm.uuid and scm_contract_id is null and nvl(isfallback,0) =0) ";
//                } else if ("2".equals(state.toString())) {
//                    selfSql += " and exists (select uuid from prm_purchase_req_detail prmdetail where  prmdetail.scm_purchase_req_id = scm.uuid and (prmdetail.scm_contract_id is not null or nvl(prmdetail.isfallback,0) =1))";
//                } else if ("3".equals(state.toString())) {
//                    selfSql += " and exists (select uuid from prm_purchase_req_detail prmdetail where prmdetail.scm_purchase_req_id = scm.uuid and isfallback =1)";
//                }
//            }
            if (StringUtil.isNotEmpty(state)) {
                if ("1".equals(state.toString())) {
                    selfSql += " and exists (select uuid from prm_purchase_req_detail prmdetail " +
                            "where prmdetail.scm_purchase_req_id = scm.uuid " +
                            "and scm_contract_id is null " +
                            "and nvl(isfallback,0) =0) ";
                } else if ("2".equals(state.toString())) {
                    selfSql += " and exists (select uuid from prm_purchase_req_detail prmdetail " +
                            "where  prmdetail.scm_purchase_req_id = scm.uuid " +
                            "and (prmdetail.scm_contract_id is not null or nvl(prmdetail.isfallback,0) =1) " +
                            "and scm_contract_code not like 'L%' and scm_contract_code not like 'S%' and scm_contract_code not like 'W%' and scm_contract_code not like 'Z%')";
                } else if ("3".equals(state.toString())) {
                    selfSql += " and exists (select uuid from prm_purchase_req_detail prmdetail " +
                            "where prmdetail.scm_purchase_req_id = scm.uuid and isfallback =1)";
                }
            } else {
                selfSql = " exists (select 1 from prm_purchase_req_detail pp " +
                        "where pp.scm_purchase_req_id = scm.uuid " +
                        "and (pp.scm_contract_code is null or " +
                        "(pp.scm_contract_code not like 'L%' " +
                        "and pp.scm_contract_code not like 'S%' " +
                        "and pp.scm_contract_code not like 'W%' " +
                        "and pp.scm_contract_code not like 'Z%')))";
            }

            sql = sql.replace("${selfconditions}", selfSql);
            String conditionSql = " 1=1 ";
            for (String key : condMap.keySet()) {
                if (StringUtil.isNotEmpty(condMap.get(key))) {
                    if ("prmProjectMainId".equals(key)) {
                        conditionSql = conditionSql + " and scm.PRM_PROJECT_MAIN_ID = '" + condMap.get(key) + "'";
                    } else if ("purchaseReqNo".equals(key)) {
                        conditionSql = conditionSql + " and scm.PURCHASE_REQ_NO like '" + condMap.get(key) + "%'";
                    } else if ("officeId".equals(key)) {
//                        conditionSql = conditionSql + " and scm.OFFICE_ID = '" + condMap.get(key) + "'";
                        String officeId = (String) condMap.get(key);
                        if (StringUtil.isNotEmpty(officeId)) {
                            String[] officeIds = officeId.split("\\|");
                            List list = Arrays.asList(officeIds);
                            String tmpOffice = StringUtil.joinForSqlIn(list, ",");
                            conditionSql = conditionSql + " and scm.office_id  in (" + tmpOffice + ")";
                        }
                    }
                }
            }
            sql = sql.replace("${conditions}", conditionSql);
        }
        String userid = UserHelper.getUserId();
        sql = sql + " and scm.PRINCIPAL ='" + userid + "' ORDER BY SCM.UPDATE_TIME DESC";
//        Map<String, Object> map = new HashMap<String, Object>();
//        for (String key : map.keySet()){
//
//        }
        return sql;


    }

    public List<Map<String, Object>> getPurchaseReqDetailByProjectId(String prmProjectMainId) {
        String sql = "select * from VW_PURCHASE_PLAN_DETAIL_LOCK " +
                " where prm_project_main_id =? ";
        List lstParam = new ArrayList<>();
        lstParam.add(prmProjectMainId);
        DAOMeta meta = new DAOMeta(sql, lstParam, false);
        List<Map<String, Object>> lstMap = PersistenceFactory.getInstance().findByNativeSQL(meta);
        return lstMap;
    }

    public Map addScalarMapForWorkFlowCommentLoad() {
        Map scalarMap = new HashMap();
        //因为activiti自带的表里面字段类型和hibernate的表里不一致，所以要进行一次转换
        scalarMap.put(ScmPurchaseReqAttribute.START_USER_ID_, StandardBasicTypes.STRING);
        scalarMap.put(ScmPurchaseReqAttribute.PURCHASE_REQ_NO_DATABASE, StandardBasicTypes.STRING);
        return scalarMap;
    }

    public boolean isContract(List<PrmPurchaseReqDetail> prmPurchaseReqDetails, String uuid) {
        boolean isContract = false;
        if (ListUtil.isNotEmpty(prmPurchaseReqDetails)) {
            for (PrmPurchaseReqDetail prmPurchaseReqDetail : prmPurchaseReqDetails) {
                Integer isFallBack = prmPurchaseReqDetail.getIsfallback();
                if (isFallBack == null || isFallBack.compareTo(Integer.valueOf("1")) != 0) {
                    isContract = true;
                }
            }
        }
        return isContract;
    }
}