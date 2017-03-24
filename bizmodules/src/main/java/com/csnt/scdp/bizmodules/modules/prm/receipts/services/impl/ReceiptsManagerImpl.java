package com.csnt.scdp.bizmodules.modules.prm.receipts.services.impl;

import com.csnt.scdp.bizmodules.attributes.CommonAttribute;
import com.csnt.scdp.bizmodules.entity.prm.*;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmReceiptsAttribute;
import com.csnt.scdp.bizmodules.modules.prm.receipts.dto.PrmReceiptsDto;
import com.csnt.scdp.bizmodules.modules.prm.receipts.dto.PrmReceiptsScmInvoiceDto;
import com.csnt.scdp.bizmodules.modules.prm.receipts.services.intf.ReceiptsManager;
import com.csnt.scdp.framework.bo.QueryCondition;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.DateUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entityattributes.ScdpMsgAttribute;
import com.csnt.scdp.sysmodules.helper.MsgSendHelper;
import com.csnt.scdp.sysmodules.modules.sys.msgcenter.dto.ReceiptsMeta;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description:  ReceiptsManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 09:20:00
 */

@Scope("singleton")
@Service("receipts-manager")
public class ReceiptsManagerImpl implements ReceiptsManager {

    public boolean checkMoneyBasedonUnknownReceipts(Map inMap) {
        Map viewData = (Map) inMap.get(CommonAttribute.VIEW_DATA);
        Map prmReceiptsDto = (Map) viewData.get(PrmReceiptsAttribute.PRM_RECEIPTS_DTO);
        String unKnownReceiptUuid = (String) prmReceiptsDto.get(PrmReceiptsAttribute.PRM_UNKNOWN_RECEIPTS_ID);
        String receiptUuid = (String) prmReceiptsDto.get(CommonAttribute.UUID);

        Object actualMoney = prmReceiptsDto.get(PrmReceiptsAttribute.ACTUAL_MONEY);
        if (actualMoney != null) {
            if (StringUtil.isEmpty(unKnownReceiptUuid) && StringUtil.isNotEmpty(receiptUuid)) {
                PrmReceiptsDto receipt = (PrmReceiptsDto) DtoHelper.findDtoByPK(PrmReceiptsDto.class, receiptUuid);
                unKnownReceiptUuid = receipt.getPrmUnknownReceiptsId();
            }

            if (StringUtil.isNotEmpty(unKnownReceiptUuid)) {
                BigDecimal orgMoney = queryUnknownReceiptsMoney(unKnownReceiptUuid);
                BigDecimal usedMoney = queryTotalUsedMoney(unKnownReceiptUuid, receiptUuid);
                if (orgMoney.compareTo(new BigDecimal(actualMoney.toString()).add(usedMoney)) < 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private BigDecimal queryUnknownReceiptsMoney(String uuid) {

        String sql = "select money from prm_unknown_receipts pre where pre.uuid = ?";
        DAOMeta daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        List lstParam = new ArrayList();
        lstParam.add(uuid);
        daoMeta.setLstParams(lstParam);
        List lstUnknowReceipts = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(lstUnknowReceipts)) {
            Map mapResult = (Map) lstUnknowReceipts.get(0);
            BigDecimal money = (BigDecimal) mapResult.get("money");
            return money == null ? new BigDecimal(0) : money;
        }
        return new BigDecimal(0);
    }

    private BigDecimal queryTotalUsedMoney(String unknownReceiptUuid, String receiptUuid) {

        String sql = "select sum(actual_money) money from prm_receipts pre where pre.prm_unknown_receipts_id = ? ";
        DAOMeta daoMeta = new DAOMeta();
        List lstParam = new ArrayList();
        lstParam.add(unknownReceiptUuid);
        if (StringUtil.isNotEmpty(receiptUuid)) {
            sql = sql + " and pre.uuid <> ?";
            lstParam.add(receiptUuid);
        }
        daoMeta.setStrSql(sql);
        daoMeta.setLstParams(lstParam);
        List lstUnknowReceipts = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(lstUnknowReceipts)) {
            Map mapResult = (Map) lstUnknowReceipts.get(0);
            BigDecimal money = (BigDecimal) mapResult.get("money");
            return money == null ? new BigDecimal(0) : money;
        }
        return new BigDecimal(0);
    }

    @Override
    public Map<String, List<PrmReceipts>> loadPrmReceiptsByUUids(List prmUUids) {
        QueryCondition condition = new QueryCondition();
        condition.setField(PrmReceiptsAttribute.PRM_PROJECT_MAIN_ID);
        condition.setOperator("in");
        condition.setValueList(prmUUids);
        List<QueryCondition> lstCondition = new ArrayList<QueryCondition>();
        lstCondition.add(condition);
        lstCondition.add(new QueryCondition(PrmReceiptsAttribute.STATE, "=", "2"));
        List<PrmReceipts> pr = PersistenceFactory.getInstance().findByAnyFields(PrmReceipts.class, lstCondition, null);

        if (ListUtil.isNotEmpty(pr)) {
            return pr.stream().filter(p -> StringUtil.isNotEmpty(p.getPrmProjectMainId())).collect(Collectors.groupingBy(PrmReceipts::getPrmProjectMainId));
        } else {
            return new HashMap<String, List<PrmReceipts>>();
        }
    }

    @Override
    public Map<String, Map> loadPrmAmountAndReceiptsByUUids(List prmUUids) {
        Map<String, Map> rtnMap = new HashMap<String, Map>();
        if (ListUtil.isNotEmpty(prmUUids)) {
            String sqlIn = StringUtil.joinForSqlIn(prmUUids, ",");
            String sql = "select * from vw_prm_receipt t where t.uuid in (" + sqlIn + ")";
            DAOMeta daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);

            PersistenceFactory.getInstance().findByNativeSQL(daoMeta).forEach(t -> {
                rtnMap.put(((Map) t).get("uuid").toString(), t);
            });
        }
        return rtnMap;
    }

    /**
     * 根据合同主键获取内委信息
     *
     * @param prmContractId
     * @return
     */
    @Override
    public Map loadInternalInfo(String prmContractId) {
        Map outMap = new HashMap();
        String internalOffice = "";
        Integer isInternal = 0;
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();

//        PrmProjectMain prmProjectMainOri = pcm.findByPK(PrmProjectMain.class, prmProjectMainId);
        PrmContract prmContract = null;
//        if (prmProjectMainOri != null && StringUtil.isNotEmpty(prmProjectMainOri.getPrmContractId())) {
//            prmContract = pcm.findByPK(PrmContract.class, prmProjectMainOri.getPrmContractId());
//        }
        if (StringUtil.isNotEmpty(prmContractId)) {
            prmContract = pcm.findByPK(PrmContract.class, prmContractId);
        }
        if (prmContract != null) {
            String innerPurchaseReqId = prmContract.getInnerPurchaseReqId();
            if (StringUtil.isNotEmpty(innerPurchaseReqId)) {
                PrmPurchaseReq prmPurchaseReq = pcm.findByPK(PrmPurchaseReq.class, innerPurchaseReqId);
                if (prmPurchaseReq != null && StringUtil.isNotEmpty(prmPurchaseReq.getPrmProjectMainId())) {
                    PrmProjectMain prmProjectMain = pcm.findByPK(PrmProjectMain.class, prmPurchaseReq.getPrmProjectMainId());
                    if (prmProjectMain != null) {
                        internalOffice = prmProjectMain.getContractorOffice();
                        isInternal = 1;
                    }
                }
            }
        }
        outMap.put("internalOffice", internalOffice);
        outMap.put("isInternal", isInternal);
        return outMap;
    }

    @Override
    public void sendMsg(String uuid, String projectCode, BigDecimal squareProjectMoney) {
        String sql = "SELECT * FROM V_USER_ROLES T WHERE ROLE_NAME ='内部报表管理员'";
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
        String templateNo = "PRM_RECEIPT_REPORT";
        Map map = new HashMap<>();
        map.put("uuid", uuid);
        map.put("projectCode", projectCode);
        map.put("squareProjectMoney", squareProjectMoney);
        MsgSendHelper.sendMsg(map, msgType, templateNo, receiptsMeta);

    }

    @Override
    public Map getReceiptsScmInvoiceFields(String prmContractId) {
        String sql = "select SCES.SCM_CONTRACT_CODE,\n" +
                "       (select so.org_name\n" +
                "          from scdp_org so\n" +
                "         where so.org_code = sces.OFFICE_ID) as OFFICE_ID_DESC,\n" +
                "       PPM.PROJECT_NAME,\n" +
                "       PPM.PROJECT_CODE,\n" +
                "      sces.SCM_NEED_PAY_MONEY,\n" +
                "      sces.SCM_NEED_PAY_MONEY_LOCK\n" +
                "  from VW_SCM_CONTRACT_EXECUTE_SIMPLE SCES\n" +
                "  LEFT JOIN prm_project_main PPM\n" +
                "    ON PPM.UUID = SCES.PROJECT_ID\n" +
                " where SCES.PROJECT_ID IS NOT NULL " +
                "  and UPPer(sces.UUID)=UPPer(?) ";
        List lstParams = new ArrayList<>();
        lstParams.add(prmContractId);
        DAOMeta daoMeta = new DAOMeta(sql, lstParams);
        daoMeta.setNeedFilter(false);
        List list = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (list != null && list.size() > 0) {
            return (Map) list.get(0);
        }
        return new HashMap<>();
    }

    @Override
    public Map validatePrmReceiptBeforeWorkFlowCommit(String uuid) {
        Map rtn = new HashMap();
        BasePojo basePojo = DtoHelper.findDtoByPK(PrmReceiptsDto.class, uuid);
        PrmReceiptsDto prmReceiptsDto = (PrmReceiptsDto) basePojo;
        Date actualDate = prmReceiptsDto.getActualDate();
        BigDecimal actualMoney = prmReceiptsDto.getActualMoney();
        StringBuffer errorMsg = new StringBuffer();
        if (actualDate == null || actualMoney == null) {
            errorMsg.append("实际收款金额和实际到款时间不能为空；");
        }
        String moneyType = prmReceiptsDto.getMoneyType();
        String isInternal = String.valueOf(prmReceiptsDto.getIsInternal());
        if (StringUtil.isEmpty(moneyType) && !"1".equals(isInternal)) {
            errorMsg.append("付款方式不能为空；");
        }

        if (StringUtil.isNotEmpty(moneyType) && moneyType.equals("HEDGE")) {
            BigDecimal hedgeMoneySum = BigDecimal.ZERO;
            List<PrmReceiptsScmInvoiceDto> prmReceiptsScmInvoiceDtos = prmReceiptsDto.getPrmReceiptsScmInvoiceDto();
            this.loadExtraDescField(prmReceiptsScmInvoiceDtos);
            for (PrmReceiptsScmInvoiceDto prmReceiptsScmInvoiceDto : prmReceiptsScmInvoiceDtos) {
                BigDecimal hedgeMoney = prmReceiptsScmInvoiceDto.getHedgeMoney();
                BigDecimal needPayMoneyLock = prmReceiptsScmInvoiceDto.getNeedPayMoneyLock();
                if (hedgeMoney == null || hedgeMoney.compareTo(BigDecimal.ZERO) < 1 || (needPayMoneyLock != null && hedgeMoney.compareTo(needPayMoneyLock) < 1)) {
                    errorMsg.append("对冲金额不能为空，要大于零，且不能大于可对冲应付账款");
                } else {
                    hedgeMoneySum = hedgeMoneySum.add(hedgeMoney);
                }
            }
            if (actualDate != null && hedgeMoneySum.compareTo(actualMoney) == 0) {
                errorMsg.append("对冲金额合计与实际收款金额不一致");
            }
        }

        if (StringUtil.isNotEmpty(errorMsg)) {
            rtn.put(com.csnt.scdp.framework.attributes.CommonAttribute.SUCCESS, false);
            rtn.put(com.csnt.scdp.framework.attributes.CommonAttribute.MESSAGE, errorMsg.toString());
        } else {
            rtn.put(com.csnt.scdp.framework.attributes.CommonAttribute.SUCCESS, true);
        }
        return rtn;
    }

    @Override
    public void loadExtraDescField(List<PrmReceiptsScmInvoiceDto> receiptsScmInvoiceDtoList) {
        if (ListUtil.isNotEmpty(receiptsScmInvoiceDtoList)) {
            for (PrmReceiptsScmInvoiceDto receiptsScmInvoiceDto : receiptsScmInvoiceDtoList) {
                Map receiptsScmInvoiceFields = this.getReceiptsScmInvoiceFields(receiptsScmInvoiceDto.getScmContractUuid());
                receiptsScmInvoiceDto.setProjectCode(receiptsScmInvoiceFields.get("projectCode") == null ? null : (String) receiptsScmInvoiceFields.get("projectCode"));
                receiptsScmInvoiceDto.setProjectName(receiptsScmInvoiceFields.get("projectName") == null ? null : (String) receiptsScmInvoiceFields.get("projectName"));
                receiptsScmInvoiceDto.setScmContractUuidDesc(receiptsScmInvoiceFields.get("scmContractCode") == null ? null : (String) receiptsScmInvoiceFields.get("scmContractCode"));
                receiptsScmInvoiceDto.setNeedPayMoney(receiptsScmInvoiceFields.get("scmNeedPayMoney") == null ? null : (BigDecimal) receiptsScmInvoiceFields.get("scmNeedPayMoney"));
                receiptsScmInvoiceDto.setNeedPayMoneyLock(receiptsScmInvoiceFields.get("scmNeedPayMoneyLock") == null ? null : (BigDecimal) receiptsScmInvoiceFields.get("scmNeedPayMoneyLock"));
                receiptsScmInvoiceDto.setOfficeIdDesc(receiptsScmInvoiceFields.get("officeIdDesc") == null ? null : (String) receiptsScmInvoiceFields.get("officeIdDesc"));
            }
        }
    }


}