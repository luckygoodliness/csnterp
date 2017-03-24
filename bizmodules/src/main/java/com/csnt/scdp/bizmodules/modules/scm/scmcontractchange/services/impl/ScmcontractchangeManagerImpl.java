package com.csnt.scdp.bizmodules.modules.scm.scmcontractchange.services.impl;

import com.csnt.scdp.bizmodules.entity.scm.ScmContract;
import com.csnt.scdp.bizmodules.entity.scm.ScmContractChange;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmContractAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmContractChangeAttribute;
import com.csnt.scdp.bizmodules.modules.scm.scmcontractchange.services.intf.ScmcontractchangeManager;
import com.csnt.scdp.bizmodules.util.UseInMap;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MapUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  ScmcontractchangeManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 18:01:19
 */

@Scope("singleton")
@Service("scmcontractchange-manager")
public class ScmcontractchangeManagerImpl implements ScmcontractchangeManager {

    @Override
    public void initMapForAdd(Map inMap) {
        Map mapForAdd = UseInMap.getMapForAdd(inMap);
        mapForAdd.put(ScmContractChangeAttribute.STATE, ScmContractChangeAttribute.STATE_VALUE_ADD);
    }

    @Override
    public BigDecimal getRemainBudget(String scmContractId) {
        DAOMeta daoMeta= new DAOMeta();
        String sql = "SELECT SC.UUID,"+
        " DECODE(SC.IS_PROJECT,"+
        " 1,"+
        " (SELECT SUM(PPPD.BUDGET_PRICE *"+
        " NVL(PPRD.HANDLE_AMOUNT, PPRD.AMOUNT))"+
        " FROM PRM_PURCHASE_REQ_DETAIL PPRD"+
        " LEFT JOIN PRM_PURCHASE_PLAN_DETAIL PPPD ON PPRD.PRM_PURCHASE_PLAN_DETAIL_ID = PPPD.UUID"+
        " WHERE SC.UUID = PPRD.SCM_CONTRACT_ID) - SC.AMOUNT,"+
        " (SELECT V.REMAIN_BUDGET"+
        " FROM VW_NON_BUDGET_EXECUTE V"+
        " WHERE V.UUID = SC.BUGDET_ID)) AS REMAIN_BUDGET"+
        " FROM SCM_CONTRACT SC"+
        " WHERE SC.UUID = '"+scmContractId+"'";
        daoMeta.setStrSql(sql);
        List<Map<String,Object>> lstContractInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(lstContractInfo)) {
            BigDecimal remainBudget = new BigDecimal(((Map) (lstContractInfo.get(0))).get("remainBudget").toString());
            return remainBudget;
        }else {
            return BigDecimal.ZERO;
        }
    }

    @Override
    public void checkMoney(Map inMap) {
        //1.参数定义
        Map mapInput = UseInMap.getMapForAdd(inMap);
        String scmContractId = "";
        String contractNature = "";
        String scmContractChangeUuid = (String) mapInput.get(CommonAttribute.UUID);
        BigDecimal originalValue = new BigDecimal(0);
        BigDecimal newValue = new BigDecimal(0);
        if (!StringUtil.isEmpty(mapInput.get(ScmContractChangeAttribute.NEW_VALUE))) {
            newValue = new BigDecimal(mapInput.get(ScmContractChangeAttribute.NEW_VALUE) + "");
        }
        if (!StringUtil.isEmpty(mapInput.get(ScmContractChangeAttribute.ORIGINAL_VALUE))) {
            originalValue = new BigDecimal(mapInput.get(ScmContractChangeAttribute.ORIGINAL_VALUE) + "");
        }
        //2.如果是修改数据
        if (mapInput.get(CommonAttribute.EDITFLAG).equals("*")) {
            ScmContractChange scmContractChange = PersistenceFactory.getInstance().findByPK(ScmContractChange.class, scmContractChangeUuid);
            if (scmContractChange != null) {
                scmContractId = scmContractChange.getScmContractId();
                originalValue = scmContractChange.getOriginalValue();
            } else {
                throw new BizException("未找到对应合同变更记录", new HashMap());
            }
            //如果是新增数据
        } else if (mapInput.get(CommonAttribute.EDITFLAG).equals("+")) {
            scmContractId = (String) mapInput.get(ScmContractChangeAttribute.SCM_CONTRACT_ID);
            if (StringUtil.isEmpty(scmContractId)) {
                throw new BizException("未选择合同", new HashMap());
            }
        }
        //3.根据合同ID找到对应的合同信息
        if (!StringUtil.isEmpty(scmContractId)) {
            ScmContract contractInfo = PersistenceFactory.getInstance().findByPK(ScmContract.class, scmContractId);
            if (contractInfo == null) {
                throw new BizException("未找到该合同！");
            }
            contractNature = contractInfo.getContractNature();
        } else {
            throw new BizException("合同ID异常", new HashMap());
        }
        //4.如果是合同性质是采购，变更前金额不应小于变更后金额
        int compareResult = originalValue.compareTo(newValue);
        if (!StringUtil.isEmpty(contractNature)) {
            if (contractNature.equals(ScmContractAttribute.CONTRACT_NATURE_VALUE_0)) {
                if (compareResult < 1) {
                    throw new BizException("采购合同变更后金额不能大于变更前金额", new HashMap());
                }
            } else if (originalValue.compareTo(newValue) < 1) {
                DAOMeta daoMeta = new DAOMeta();
//                scmContractId = (String) mapInput.get(ScmContractChangeAttribute.SCM_CONTRACT_ID);
//        BigDecimal newValue = new BigDecimal(0);
                String sql = "SELECT SUM(V.REMAIN_BUDGET) as balance" +
                        " FROM VW_PRM_BUDGET V" +
                        " WHERE (V.REMAIN_BUDGET < 0 OR" +
                        " V.PACKAGE_ID = (SELECT SC.PURCHASE_PACKAGE_ID" +
                        " FROM SCM_CONTRACT SC" +
                        " WHERE SC.UUID = '" + scmContractId + "') OR EXISTS" +
                        " (SELECT *" +
                        " FROM PRM_PURCHASE_PACKAGE P" +
                        " WHERE P.UUID = (SELECT SC.PURCHASE_PACKAGE_ID" +
                        " FROM SCM_CONTRACT SC" +
                        " WHERE SC.UUID = '" + scmContractId + "')" +
                        " AND P.PACKAGE_STATE = 4))" +
                        " AND V.PROJECT_MAIN_ID =(SELECT SC.PROJECT_ID" +
                        " FROM SCM_CONTRACT SC" +
                        " WHERE SC.UUID = '" + scmContractId + "')";
                daoMeta.setStrSql(sql);
                List lstContractInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                if (!lstContractInfo.isEmpty() && lstContractInfo.size() != 0) {
                    Object obj = ((Map) lstContractInfo.get(0)).get("balance");
                    BigDecimal balance = BigDecimal.ZERO;
                    if (obj != null) {
                        balance = new BigDecimal(obj.toString());//合同未登记发票总额
                    }
                    if (balance.compareTo(newValue.subtract(originalValue)) < 0) {
                        throw new BizException("变更金额过大,项目可用预算不足", new HashMap());
                    }
                }
            }
        } else {
            throw new BizException("合同类型异常", new HashMap());
        }
    }

    @Override
    public Map getContractInfo(String scmContractId) {
//        DAOMeta daoMeta = DAOHelper.getDAO("scmcontract-dao", "common_query", null);
        Map map = new HashMap<>();
        DAOMeta daoMeta = new DAOMeta();
        String sql = " select sc.*,DECODE(NVL(SC.IS_PROJECT, 0), 0, SC.SUBJECT_CODE, P.PROJECT_CODE) AS fad_Subject_Code," +
                "                               NVL(P.PROJECT_NAME," +
                "                                   (SELECT N.FINANCIAL_SUBJECT " +
                "                                      FROM NON_PROJECT_SUBJECT_SUBJECT N " +
                "                                     WHERE N.FINANCIAL_SUBJECT_CODE = SC.SUBJECT_CODE)) AS fad_Subject_NAME " +
                " from scm_contract sc LEFT JOIN PRM_PROJECT_MAIN P ON SC.PROJECT_ID = P.UUID where sc.uuid= '" + scmContractId + "' ";
        daoMeta.setStrSql(sql);
        List lstContractInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (!lstContractInfo.isEmpty() && lstContractInfo.size() != 0) {
            map = (Map) (lstContractInfo.get(0));
        }
        return map;
    }

    @Override
    public void updateStateToSubmit(String uuid) {
        DAOMeta daoMeta = new DAOMeta();
        String sql = "UPDATE SCM_CONTRACT_CHANGE SET STATE='" + ScmContractChangeAttribute.STATE_VALUE_SUBMIT + "' WHERE UUID='" + uuid + "'";
        daoMeta.setStrSql(sql);
        Integer count = PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
        if (count != 1) {
            throw new BizException("合同提交失败", new HashMap());
        }
    }

    @Override
    public void updateStateToApproved(String uuid, String scmContractId) {
        BigDecimal newValue = new BigDecimal(0);
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        //1.更新合同变更表的状态为通过
        ScmContractChange scmContractChange = pcm.findByPK(ScmContractChange.class, uuid);
        if (scmContractChange != null) {
            scmContractChange.setState(ScmContractChangeAttribute.STATE_VALUE_APPROVED);
            String temp = scmContractChange.getNewValue() + "";
            if (!StringUtil.isEmpty(temp)) {
                newValue = new BigDecimal(temp);
            }
            pcm.update(scmContractChange);
        } else {
            throw new BizException("未找到该合同变更记录", new HashMap());
        }
        //2.更新合同表的合同总额为新合同总额，更新合同表的状态为已结算
        ScmContract scmContract = pcm.findByPK(ScmContract.class, scmContractId);
        if (scmContract != null) {
            scmContract.setAmount(newValue);
            if (!scmContractChange.getCloseChange().isEmpty() && "1".equals(scmContractChange.getCloseChange())) {
                scmContract.setIsClosed(ScmContractAttribute.IS_CLOSED_1);
            }
            pcm.update(scmContract);
        } else {
            throw new BizException("未找到对应的合同记录", new HashMap());
        }
    }

    @Override
    public boolean checkIfHaveApprovedRecord(Map inMap) {
        boolean result = false;
        List uuids = (List) inMap.get("uuids");
        String uuidStr = "";
        if (ListUtil.isNotEmpty(uuids)) {
            for (Object uuid : uuids) {
                uuidStr += uuid + "','";
            }
            if (!"".equals(uuidStr)) {
                uuidStr = uuidStr.substring(0, uuidStr.length() - 3);
            }
        } else if (MapUtil.isNotEmpty((Map) inMap.get(CommonAttribute.VIEW_DATA))) {
            Map map = UseInMap.getMapForAdd(inMap);
            String uuid = (String) map.get(CommonAttribute.UUID);
            if (StringUtil.isNotEmpty()) {
                uuidStr = uuid;
            }
        }
        if (StringUtil.isNotEmpty(uuidStr)) {
            List paramList = new ArrayList<>();
            paramList.add(uuidStr);
            DAOMeta daoMeta = DAOHelper.getDAO("scmcontractchange-dao", "if_have_approved_record_query", paramList);
            List list = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            if (ListUtil.isNotEmpty(list)) {
                result = true;
            }
        }
        return result;
    }
}