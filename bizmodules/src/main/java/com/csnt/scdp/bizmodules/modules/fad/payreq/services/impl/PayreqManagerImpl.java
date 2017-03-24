package com.csnt.scdp.bizmodules.modules.fad.payreq.services.impl;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.entity.fad.FadPayReqC;
import com.csnt.scdp.bizmodules.entity.fad.FadPayReqH;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadPayReqCAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadPayReqHAttribute;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpUserHelper;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.fad.payreq.dto.FadPayReqCDto;
import com.csnt.scdp.bizmodules.modules.fad.payreq.dto.FadPayReqHDto;
import com.csnt.scdp.bizmodules.modules.fad.payreq.services.intf.PayreqManager;
import com.csnt.scdp.bizmodules.modules.scm.scmcontract.services.intf.ScmcontractManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.bo.QueryCondition;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.UserHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpRole;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.entityattributes.ScdpMsgAttribute;
import com.csnt.scdp.sysmodules.helper.MsgSendHelper;
import com.csnt.scdp.sysmodules.helper.NumberingHelper;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import com.csnt.scdp.sysmodules.helper.UserDtoHelper;
import com.csnt.scdp.sysmodules.modules.sys.msgcenter.dto.ReceiptsMeta;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description:  PayreqManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 17:42:25
 */

@Scope("singleton")
@Service("payreq-manager")
public class PayreqManagerImpl implements PayreqManager {

    @Resource(name = "scmcontract-manager")
    private ScmcontractManager scmcontractManager;

    /**
     * 设置支付明细的合同相关字段
     *
     * @param lst 格式 "'111','eee'" ,若uuids为空，则不加过滤
     * @return 结果
     */
    @Override
    public void setObjectPlusInfo(List<FadPayReqCDto> lst) {
        if (lst != null && lst.size() > 0) {
            String uuids = "";
            for (FadPayReqCDto dto : lst) {
                uuids += "'" + dto.getScmContractId() + "',";
            }
            uuids = uuids.length() > 0 ? uuids.substring(0, uuids.length() - 1) : "";
            if (StringUtils.isNotBlank(uuids)) {
                Map<String, Map> rtnMap = scmcontractManager.getObjectsById(uuids);

                List<FadPayReqCDto> lstCDto = lst.stream()
                        .filter(p -> (StringUtils.isNotBlank(p.getScmContractId()))).collect(Collectors.toList());
                for (int i = 0; i < lstCDto.size(); i++) {
                    if (StringUtil.isEmpty(lstCDto.get(i).getState())) {
                        lstCDto.get(i).setState("0");
                    }
                    if (rtnMap.containsKey(lstCDto.get(i).getScmContractId())) {
                        Map contract = rtnMap.get(lstCDto.get(i).getScmContractId());
                        lstCDto.get(i).setScmContractCode((String) (contract.get("scmContractCode")));
                        lstCDto.get(i).setSupplierName((String) (contract.get("supplierName")));
                        lstCDto.get(i).setProjectMainName((String) (contract.get("projectName")));
                        lstCDto.get(i).setProjectCode((String) (contract.get("projectCode")));
                        lstCDto.get(i).setOrgName((String) (contract.get("orgName")));

                        if (contract.get("amount") != null) {
                            BigDecimal amount = new BigDecimal(contract.get("amount").toString());
                            lstCDto.get(i).setScmContractAmount(amount);
                        }

                        if (contract.get("scmPaidMoney") != null) {
                            BigDecimal paidMoney = new BigDecimal(contract.get("scmPaidMoney").toString());
                            lstCDto.get(i).setScmContractPaidMoney(paidMoney);
                        }

                        if (contract.get("checkedMoney") != null) {
                            BigDecimal checkedMoney = new BigDecimal(contract.get("checkedMoney").toString());
                            lstCDto.get(i).setScmContractCheckedAmount(checkedMoney);
                        }
                        if (contract.get("scmNeedPayMoney") != null) {
                            BigDecimal scmContractNeedToPay = new BigDecimal(contract.get("scmNeedPayMoney").toString());
                            lstCDto.get(i).setScmContractNeedToPay(scmContractNeedToPay);
                        }

                        if (contract.get("fadInvoiceMoney") != null) {
                            BigDecimal fadInvoiceMoney = new BigDecimal(contract.get("fadInvoiceMoney").toString());
                            lstCDto.get(i).setScmContractFadInvoiceMoney(fadInvoiceMoney);
                        }
                        if (contract.get("supplierBlack") != null) {
                            Integer supplierBlack = new Integer(contract.get("supplierBlack").toString());
                            lstCDto.get(i).setIsBlackList(supplierBlack);
                        }
                    }
                }
            }
        }
    }


    public void setObjectPlusInfo(FadPayReqHDto hdto) {
        if (hdto != null && hdto.getFadPayReqCDto() != null && hdto.getFadPayReqCDto().size() > 0) {
            String uuidsOrSql = "SELECT SCM_CONTRACT_ID\n" +
                    "          FROM FAD_PAY_REQ_C C\n" +
                    "         WHERE C.PUUID = '" + hdto.getUuid() + "'";
            if (StringUtils.isNotBlank(uuidsOrSql)) {
                Map<String, Map> rtnMap = scmcontractManager.getObjectsById(uuidsOrSql);

                List<FadPayReqCDto> lstCDto = hdto.getFadPayReqCDto().stream()
                        .filter(p -> (StringUtils.isNotBlank(p.getScmContractId())) && rtnMap.containsKey(p.getScmContractId())
                        ).collect(Collectors.toList());
                String uuidsOrSql2 = "SELECT DISTINCT SUPPLIER_ID\n" +
                        "          FROM FAD_PAY_REQ_C C\n" +
                        "         WHERE C.PUUID = '" + hdto.getUuid() + "'";
                Map<String, Map> rtnSupMap = getScmSupplierPayInfo(uuidsOrSql2);

                hdto.setFadPayReqCDto(lstCDto);

                for (int i = 0; i < lstCDto.size(); i++) {
                    if (StringUtil.isEmpty(lstCDto.get(i).getState())) {
                        lstCDto.get(i).setState("0");
                    }
                    if (rtnMap.containsKey(lstCDto.get(i).getScmContractId())) {
                        Map contract = rtnMap.get(lstCDto.get(i).getScmContractId());
                        lstCDto.get(i).setScmContractCode((String) (contract.get("scmContractCode")));
                        lstCDto.get(i).setContractNature((String) (contract.get("contractNature")));
                        lstCDto.get(i).setSupplierName((String) (contract.get("supplierName")));
                        lstCDto.get(i).setProjectMainName((String) (contract.get("projectName")));
                        lstCDto.get(i).setProjectCode((String) (contract.get("projectCode")));
                        lstCDto.get(i).setOrgName((String) (contract.get("orgName")));

                        if (contract.get("amount") != null) {
                            BigDecimal amount = new BigDecimal(contract.get("amount").toString());
                            lstCDto.get(i).setScmContractAmount(amount);
                        }

                        if (contract.get("scmPaidMoney") != null) {
                            BigDecimal paidMoney = new BigDecimal(contract.get("scmPaidMoney").toString());
                            lstCDto.get(i).setScmContractPaidMoney(paidMoney);
                        }

                        if (contract.get("scmNeedPayMoney") != null) {
                            BigDecimal scmContractNeedToPay = new BigDecimal(contract.get("scmNeedPayMoney").toString());
                            lstCDto.get(i).setScmContractNeedToPay(scmContractNeedToPay);
                        }

                        if (contract.get("checkedMoney") != null) {
                            BigDecimal checkedMoney = new BigDecimal(contract.get("checkedMoney").toString());
                            lstCDto.get(i).setScmContractCheckedAmount(checkedMoney);
                        }

                        if (contract.get("fadInvoiceMoney") != null) {
                            BigDecimal fadInvoiceMoney = new BigDecimal(contract.get("fadInvoiceMoney").toString());
                            lstCDto.get(i).setScmContractFadInvoiceMoney(fadInvoiceMoney);
                        }
                        if (contract.get("supplierBlack") != null) {
                            Integer supplierBlack = new Integer(contract.get("supplierBlack").toString());
                            lstCDto.get(i).setIsBlackList(supplierBlack);
                        }
                        if (contract.get("invoiceScmNeedCheck") != null) {
                            BigDecimal invoiceScmNeedCheck = new BigDecimal(contract.get("invoiceScmNeedCheck").toString());
                            lstCDto.get(i).setInvoiceScmNeedCheck(invoiceScmNeedCheck);
                        }
                    }
                    if (rtnSupMap.containsKey(lstCDto.get(i).getSupplierId())) {
                        Map supPayInfo = rtnSupMap.get(lstCDto.get(i).getSupplierId());
                        if (supPayInfo.get("supNeedPayMoney") != null) {
                            BigDecimal supNeedPayMoney = new BigDecimal(supPayInfo.get("supNeedPayMoney").toString());
                            lstCDto.get(i).setScmContractSupplierUnPaidMoney(supNeedPayMoney);
                        }
                    }
                }
            }
        }
    }

    /**
     * 设置支付明细的合同相关字段
     *
     * @param filter 过滤条件
     * @return 结果
     */
    @Override
    public void addContract2Object(List<FadPayReqCDto> lst, String filter) {
        StringBuilder uuids = new StringBuilder();

        if (lst == null) {
            return;
        }
        if (lst.size() > 0) {
            lst.forEach(n -> uuids.append("'" + n.getScmContractId() + "',"));
        }
        if (StringUtils.isNotBlank(uuids)) {
            String us = uuids.toString();
            us = us.length() > 0 ? us.substring(0, us.length() - 1) : "";
            filter += "  and t.uuid not in (" + us + ") ";
        }
        //以上  排除在lst中以为的数据
        Map<String, Map> rtnMap = scmcontractManager.getObjectsById("", filter);

        for (Map.Entry<String, Map> entry : rtnMap.entrySet()) {
            FadPayReqCDto obj = new FadPayReqCDto();
            lst.add(obj);
            obj.setSeqNo(lst.size());
            obj.setState("0");

            obj.setSupplierId((String) (entry.getValue().get("supplierCode")));
            obj.setProjectMainId((String) (entry.getValue().get("projectId")));

            if (entry.getValue().get("paidMoney") != null) {
                BigDecimal paidMoney = new BigDecimal(entry.getValue().get("paidMoney").toString());
                obj.setScmContractPaidMoney(paidMoney);
            }

            if (entry.getValue().get("amount") != null) {
                BigDecimal amount = new BigDecimal(entry.getValue().get("amount").toString());
                obj.setScmContractAmount(amount);
            }

            if (entry.getValue().get("scmContractCode") != null)
                obj.setScmContractCode(entry.getValue().get("scmContractCode").toString());

            if (entry.getValue().get("supplierName") != null)
                obj.setSupplierName(entry.getValue().get("supplierName").toString());

            if (entry.getValue().get("projectName") != null)
                obj.setProjectMainName(entry.getValue().get("projectName").toString());

            if (entry.getValue().get("orgName") != null)
                obj.setOrgName(entry.getValue().get("orgName").toString());

            if (entry.getValue().get("paidMoney") != null) {
                BigDecimal paidMoney = new BigDecimal(entry.getValue().get("paidMoney").toString());
                obj.setScmContractPaidMoney(paidMoney);
            }

            if (entry.getValue().get("checkedMoney") != null) {
                BigDecimal checkedMoney = new BigDecimal(entry.getValue().get("checkedMoney").toString());
                obj.setScmContractCheckedAmount(checkedMoney);
            }
            if (entry.getValue().get("fadInvoiceMoney") != null) {
                BigDecimal fadInvoiceMoney = new BigDecimal(entry.getValue().get("fadInvoiceMoney").toString());
                obj.setScmContractFadInvoiceMoney(fadInvoiceMoney);
            }
        }
    }

    @Override
    public List<FadPayReqH> getFadPayReqHbyCondition(Map<String, Object> mapConditions) {
        List<FadPayReqH> lstFadPayReqH = PersistenceFactory.getInstance().
                findByAnyFields(FadPayReqH.class, mapConditions, null);
        return lstFadPayReqH;
    }

    @Override
    public FadPayReqH getLatestHandledPayReqH() {

        FadPayReqH fadPayReqH = null;
        DAOMeta daoMeta = new DAOMeta();
        String sql = "select t.*\n" +
                " from   fad_pay_req_h t\n" +
                " where  t.year || t.month =\n" +
                "       (select max(t.year || t.month)\n" +
                "        from   fad_pay_req_h t\n" +
                "        where  t.req_type = 1\n" +
                "        and  t.state in ('1','2','6') and  (t.is_void = 0 or t.is_void is null))";

        daoMeta.setStrSql(sql);

        List lstObjInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(lstObjInfo)) {
            String uuid = ((Map) lstObjInfo.get(0)).get("uuid").toString();
            fadPayReqH = PersistenceFactory.getInstance().findByPK(FadPayReqH.class, uuid);
        }
        return fadPayReqH;
    }

    @Override
    public FadPayReqH getFadPayReqHbyUuid(String uuid) {
        return PersistenceFactory.getInstance().findByPK(FadPayReqH.class, uuid);
    }

    @Override
    public List<FadPayReqC> getFadPayReqCbyUuids(List uuid) {
        QueryCondition condition1 = new QueryCondition();
        condition1.setField(CommonAttribute.UUID);
        condition1.setOperator("in");
        condition1.setValueList(uuid);
        List<QueryCondition> lstCondition = new ArrayList<QueryCondition>();
        lstCondition.add(condition1);
        return PersistenceFactory.getInstance().findByAnyFields(FadPayReqC.class, lstCondition, null);

    }

    @Override
    public List<FadPayReqC> getFadPayReqCbyPuuid(String puuid) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(FadPayReqCAttribute.PUUID, puuid);
        List<FadPayReqC> lstFadPayReqC = PersistenceFactory.getInstance().
                findByAnyFields(FadPayReqC.class, mapConditions, null);
        return lstFadPayReqC;
    }

    @Override
    public void updateFadPayReqHBillState(String uuid, String state) {
        FadPayReqH fadPayReqH = PersistenceFactory.getInstance().findByPK(FadPayReqH.class, uuid);
        fadPayReqH.setState(state);
        PersistenceFactory.getInstance().update(fadPayReqH);
    }

    @Override
    public void update(FadPayReqC fadPayReqC) {
        PersistenceFactory.getInstance().update(fadPayReqC);
    }

    @Override
    public List<FadPayReqCDto> filterFadPayReqCbyRight(List<FadPayReqCDto> lst, Boolean needFilter, String userId) {
        if (ListUtil.isNotEmpty(lst) && needFilter) {
            //部门经理和部门主任可以看到本部门所有数据
            if (ErpUserHelper.isSelfBizDivisionVp(userId) || ErpUserHelper.isSelfManageDivisionVp(userId)) {
                ScdpUser userInfo = ErpUserHelper.getScdpUserByUserId(userId);
                if (userInfo != null) {
                    String orgName = OrgHelper.getOrgNameById(userInfo.getOrgUuid());
                    return lst.stream().filter(t ->
                            (orgName.equals(t.getOrgName()))).collect(Collectors.toList());
                } else {
                    return new ArrayList<FadPayReqCDto>();
                }
            } else {
                List<String> projectMainId = ErpUserHelper.getChargedPrmProjectMainByUserId(userId);
//                return ListUtil.isEmpty(projectMainId) ? new ArrayList<FadPayReqCDto>() :
                return lst.stream().filter(t -> projectMainId.contains(t.getProjectMainId()) ||
                        userId.equals(t.getCreateBy())).collect(Collectors.toList());
            }
        }
        return lst;
    }

    @Override
    public void closePreviousMonthReq(FadPayReqH fadPayReqh) {
        if (fadPayReqh != null) {
            String date = fadPayReqh.getYear().toString() + fadPayReqh.getMonth();
            DAOMeta daoMeta = new DAOMeta();
            String sql = "UPDATE FAD_PAY_REQ_H H\n" +
                    "   SET H.STATE = 4\n" +
                    " WHERE H.REQ_TYPE = '1'\n" +
                    "   AND H.STATE <> '4'\n" +
                    "   AND TO_CHAR(H.YEAR || H.MONTH) < TO_CHAR('" + date + "')";
            daoMeta.setStrSql(sql);
            PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);

//            sql = "  UPDATE FAD_PAY_REQ_C C\n" +
//                    "     SET C.STATE = '3'\n" +
//                    "   WHERE C.STATE in ('1','2','7')" +
//                    "     AND C.PUUID IN (SELECT H.UUID\n" +
//                    "                       FROM FAD_PAY_REQ_H H\n" +
//                    "                      WHERE H.REQ_TYPE = '1'\n" +
//                    "                        AND H.STATE = '4')";
//            daoMeta.setStrSql(sql);
//            PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
        }
    }

    /**
     * 单据审核通过后，修改支付明细状态，修改为通过状态，只有通过状态的支付明细才能请款和支付
     *
     * @param fadPayReqh 支付申请表头
     */
    @Override
    public void endorseMonthReqChildren(FadPayReqH fadPayReqh) {
        if (fadPayReqh != null) {
            String uuidsOrSql = "SELECT SCM_CONTRACT_ID\n" +
                    "          FROM FAD_PAY_REQ_C C\n" +
                    "         WHERE C.PUUID = '" + fadPayReqh.getUuid() + "'";
            Map<String, Map> rtnMap = scmcontractManager.getObjectsById(uuidsOrSql);
            getFadPayReqCbyPuuid(fadPayReqh.getUuid()).forEach(t -> {
                Map contract = rtnMap.get(t.getScmContractId());
                BigDecimal scmPaidMoney = new BigDecimal("0");
                if (contract != null && contract.get("scmPaidMoney") != null) {
                    scmPaidMoney = new BigDecimal(contract.get("scmPaidMoney").toString());
                }
                if (fadPayReqh.getReqType() == null || FadPayReqHAttribute.REQ_TYPE_TEMP.equals(fadPayReqh.getReqType())) {
                    if (BillStateAttribute.FAD_BILL_STATE_SUBMITTED.equals(t.getState()) || BillStateAttribute.FAD_BILL_STATE_NEW.equals(t.getState())) {
                        t.setState(BillStateAttribute.FAD_BILL_STATE_APPROVED);
                        t.setScmContractThenPaid(scmPaidMoney);
                        update(t);
                    }
                } else {
                    if (BillStateAttribute.FAD_BILL_STATE_SUBMITTED.equals(t.getState())) {
                        t.setState(BillStateAttribute.FAD_BILL_STATE_APPROVED);
                        t.setScmContractThenPaid(scmPaidMoney);
                        update(t);
                    }
                }
            });
        }
    }

    @Override
    public Map<String, Map> getFadCertificateNo(FadPayReqH fadPayReqh) {
        Map<String, Map> rtn = new HashMap<String, Map>();
        if (fadPayReqh != null) {
            DAOMeta daoMeta = new DAOMeta();
            String uuidsOrSql = "SELECT BUSINESS_ID," +
                    "       LISTAGG(T.CERTIFICATE_NO, '||') WITHIN GROUP(ORDER BY BUSINESS_ID) AS CERTIFICATE_NO" +
                    "  FROM FAD_CERTIFICATE T" +
                    " WHERE T.BUSINESS_ID IN" +
                    "       (SELECT C.UUID" +
                    "          FROM FAD_PAY_REQ_C C" +
                    "         WHERE C.PUUID = '" + fadPayReqh.getUuid() + "')" +
                    "   AND CERTIFICATE_NO IS NOT NULL" +
                    " GROUP BY T.BUSINESS_ID";
            daoMeta.setStrSql(uuidsOrSql);
            daoMeta.setNeedFilter(false);
            List lstObjInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            if (ListUtil.isNotEmpty(lstObjInfo)) {
                lstObjInfo.forEach(n -> {
                            rtn.put(((Map) n).get("businessId").toString(), (Map) n);
                        }
                );
            }
        }
        return rtn;
    }

    @Override
    public Map<String, Map> getScmSupplierPayInfo(String uuidsOrSql) {
        Map<String, Map> rtn = new HashMap<String, Map>();
        String append = " ";
        if (StringUtils.isNotBlank(uuidsOrSql)) {
            append = " and t.supplier_code in (" + uuidsOrSql + ")";
        }
        DAOMeta daoMeta = new DAOMeta();
        String sql = "select supplier_code," +
                "       sum(t.scm_need_pay_money) as sup_need_pay_money," +
                "       sum(t.scm_paid_money) as sup_paid_money," +
                "       sum(t.scm_unpaid_money) as sup_unpaid_money," +
                "       sum(amount) as sup_amount" +
                "  from vw_scm_contract_execute t" +
                " where t.supplier_code is not null " + append +
                " group by t.supplier_code";
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        List lstObjInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(lstObjInfo)) {
            lstObjInfo.forEach(n -> {
                        rtn.put(((Map) n).get("supplierCode").toString(), (Map) n);
                    }
            );
        }
        return rtn;
    }

    @Override
    public void sendMsg(String uuid, String month) {
        String sql = "SELECT * FROM V_USER_ROLES T WHERE ROLE_NAME ='事业部分管支付领导'";
        DAOMeta daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        List<Map<String, Object>> lstUserInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        List<String> userIdLst = new ArrayList<String>();
        ReceiptsMeta receiptsMeta = new ReceiptsMeta();
        if (ListUtil.isNotEmpty(lstUserInfo)) {
            for (int i = 0; i < lstUserInfo.size(); i++) {
                String userId = (String) lstUserInfo.get(i).get("userId");
                userIdLst.add(userId);
            }
            receiptsMeta.setLstSendToUserId(userIdLst);
        }
        String msgType = ScdpMsgAttribute.MSG_TYPE_IMSG;
        String templateNo = "SCM_MONTH_PAYMENT_REQUEST";
        Map map = new HashMap<>();
        map.put("uuid", uuid);
        map.put("month", month);
        MsgSendHelper.sendMsg(map, msgType, templateNo, receiptsMeta);

    }

    @Override
    public Integer closeObsoletedPayment(int type, int obsoleteMonths, int obsoleteDays) {
        Integer updateCount = Integer.parseInt("0");
        if (obsoleteDays > -1) {
            String sql = "SELECT * 　\n" +
                    "  FROM (SELECT H.UUID,\n" +
                    "               H.STATE,\n" +
                    "               H.REQNO,\n" +
                    "               H.REQ_TYPE,\n" +
                    "               H.IS_VOID,\n" +
                    "               NVL(S.END_TIME_, H.UPDATE_TIME) AS AUDIT_TIME\n" +
                    "          FROM FAD_PAY_REQ_H H\n" +
                    "          LEFT JOIN (SELECT *\n" +
                    "                      FROM (SELECT ROW_NUMBER() OVER(PARTITION BY T.BUSINESS_KEY_ ORDER BY T.START_TIME_ DESC) RN,\n" +
                    "                                   T.START_TIME_,\n" +
                    "                                   T.END_TIME_,\n" +
                    "                                   T.BUSINESS_KEY_\n" +
                    "                              FROM ACT_HI_PROCINST T)\n" +
                    "                     WHERE RN = 1) S\n" +
                    "            ON H.UUID = S.BUSINESS_KEY_\n" +
                    "         WHERE H.STATE = '2'\n" +
                    "           AND (H.IS_VOID = 0 OR H.IS_VOID IS NULL))";
            if (FadPayReqHAttribute.REQ_TYPE_MONTH == type || FadPayReqHAttribute.REQ_TYPE_TEMP == type) {
                sql = sql + " WHERE  REQ_TYPE = '" + type + "'";
            } else if (FadPayReqHAttribute.REQ_TYPE_ALL == type) {
                sql = sql + " WHERE  1=1 ";
            } else {
                throw new BizException("调用方法支付类型错误！");
            }
            sql = sql + " AND TRUNC(ADD_MONTHS(AUDIT_TIME," + obsoleteMonths + ")+" + obsoleteDays + ") < TRUNC(SYSDATE)";
            DAOMeta daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            List lstObjInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            if (ListUtil.isNotEmpty(lstObjInfo)) {
                List ids = Arrays.asList(lstObjInfo.stream().map(t -> ((Map) t).get("uuid")).toArray());
                String uuids = StringUtil.joinForSqlIn(ids, ",");

                String updateSql = " UPDATE FAD_PAY_REQ_H H " +
                        "   SET H.STATE ='" + BillStateAttribute.CMD_BILL_STATE_CLOSED + "'" +
                        "   WHERE UUID IN (" + uuids + ") and H.STATE='2'";
                daoMeta.setStrSql(updateSql);
                updateCount = PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
            }
        }
        return updateCount;
    }
}

