package com.csnt.scdp.bizmodules.modules.fad.invoice.services.impl;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.attributes.CommonAttribute;
import com.csnt.scdp.bizmodules.attributes.MessageKeyAttribute;
import com.csnt.scdp.bizmodules.entity.fad.*;
import com.csnt.scdp.bizmodules.entity.prm.PrmPurchasePlanDetail;
import com.csnt.scdp.bizmodules.entity.scm.ScmContractInvoice;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCashReqAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCashReqInvoiceAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadInvoiceAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmPurchasePlanDetailAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmContractInvoiceAttribute;
import com.csnt.scdp.bizmodules.modules.fad.cashreq.dto.FadCashReqDto;
import com.csnt.scdp.bizmodules.modules.fad.invoice.dto.FadCashReqInvoiceDto;
import com.csnt.scdp.bizmodules.modules.fad.invoice.dto.FadInvoiceDto;
import com.csnt.scdp.bizmodules.modules.fad.invoice.dto.ScmContractInvoiceDto;
import com.csnt.scdp.bizmodules.modules.fad.invoice.services.intf.InvoiceManager;
import com.csnt.scdp.bizmodules.modules.fad.payreq.dto.FadPayReqCDto;
import com.csnt.scdp.framework.bo.QueryCondition;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.w3c.dom.ls.LSException;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description:  InvoiceManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-25 14:13:13
 */

@Scope("singleton")
@Service("invoice-manager")
public class InvoiceManagerImpl implements InvoiceManager {

    public List<Map> getCashReqInfo(List<String> fadCashReqIdList) {
        List<Map> cashReqList = new ArrayList();
        if (ListUtil.isNotEmpty(fadCashReqIdList)) {
            StringBuffer sb = new StringBuffer();
            sb.append("'*'");
            for (String s : fadCashReqIdList) {
                sb.append(",'" + s + "'");
            }
            DAOMeta daoMeta = DAOHelper.getDAO("invoice-dao", "cash_Req_query", null);
            String sql = daoMeta.getStrSql();
            sql = sql + " where uuid in(" + sb.toString() + ") ";
            daoMeta.setStrSql(sql);
            List lstContractCashReqInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            if (ListUtil.isNotEmpty(lstContractCashReqInfo)) {
                for (Object m : lstContractCashReqInfo) {
                    cashReqList.add((Map) m);
                }
            }
        }
        return cashReqList;
    }

    /*新增时校验合同分配是否超额
    contractId:为表scm_contract_invoice新增数据合同ID
     */
    public String contractInvoiceCheckAdd(String contractId, BigDecimal value) {

        DAOMeta daoMeta = new DAOMeta();
        String sql = "";
        sql = " select t.SCM_CONTRACT_CODE as code," +
                " nvl(amount,0)-" +
                "nvl((" +
                "select sum(amount) " +
                " from scm_contract_invoice d " +
                " where d.scm_contract_id = t.uuid " +
                " AND (EXISTS (SELECT 1 FROM FAD_INVOICE F WHERE F.UUID = D.FAD_INVOICE_ID AND F.STATE <> '3') " +
                " OR EXISTS (SELECT 1 FROM FAD_CASH_CLEARANCE F WHERE F.UUID = D.FAD_INVOICE_ID AND F.STATE <> '3')" +
                ")),0) as balance " +
                " from scm_contract t where t.uuid = '" + contractId + "' ";
        daoMeta.setStrSql(sql);
        String result = "";
        List lstContractInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (!lstContractInfo.isEmpty() && lstContractInfo.size() != 0) {
            BigDecimal balance = new BigDecimal((((Map) lstContractInfo.get(0)).get("balance")).toString());//合同未登记发票总额
            if (value.compareTo(balance) == 1) {
                result = (((Map) lstContractInfo.get(0)).get("code")).toString();
            } else {
                result = "1";
            }
        } else {
            result = "2";
        }
        return result;//校验通过返回1，否则返回不合格合同，找不到该合同返回“2”
    }

    /*修改时校验合同分配是否超额
     * uuid:为表scm_contract_invoice修改数据ID
     */
    public String contractInvoiceCheckModify(String uuid, BigDecimal value) {

        DAOMeta daoMeta = new DAOMeta();
        String sql = "";
        sql = " select t.SCM_CONTRACT_CODE as code," +
                "nvl(amount,0)-" +
                "nvl((" +
                "select sum(amount) from scm_contract_invoice d" +
                " where d.scm_contract_id = t.uuid" +
                " and d.uuid !='" + uuid + "'" +
                " AND (EXISTS (SELECT 1 FROM FAD_INVOICE F WHERE F.UUID = D.FAD_INVOICE_ID AND F.STATE <> '3') " +
                " OR EXISTS (SELECT 1 FROM FAD_CASH_CLEARANCE F WHERE F.UUID = D.FAD_INVOICE_ID AND F.STATE <> '3')" +
                ")),0) as balance from scm_contract t where t.uuid = (select scm_contract_id from scm_contract_invoice d where  d.uuid ='" + uuid + "') ";
        daoMeta.setStrSql(sql);
        List lstContractInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        String result = "1";

        if (!lstContractInfo.isEmpty() && lstContractInfo.size() != 0) {
            BigDecimal balance = new BigDecimal((((Map) lstContractInfo.get(0)).get("balance")).toString());//合同未登记发票总额
            if (value.compareTo(balance) == 1) {
                result = (((Map) lstContractInfo.get(0)).get("code")).toString();
            } else {
                result = "1";
            }
        } else {
            result = "2";
        }
        return result;//校验通过返回1，否则返回不合格合同，找不到该合同返回“2”
    }


    /*提交时校验
*零星发票分配录入发票时，对于同一个零星合同，后面录入的发票的供应商必须与第一次录入的供应商保持一致。
*/
    public void checkLXHTSupplierName(String uuid) {
        DAOMeta daoMeta = new DAOMeta();
        String sql = "SELECT *" +
                " FROM (SELECT O.SCM_CONTRACT_CODE, O.INVOICE_REQ_NO, O.SUPPLIER_NAME" +
                "        FROM (SELECT FI.UUID," +
                "                FI.INVOICE_REQ_NO," +
                "                FI.SUPPLIER_NAME," +
                "                SCI.SCM_CONTRACT_ID," +
                "                SC.SCM_CONTRACT_CODE" +
                "                FROM FAD_INVOICE FI" +
                "                LEFT JOIN SCM_CONTRACT_INVOICE SCI ON FI.UUID =" +
                "                        SCI.FAD_INVOICE_ID" +
                "                LEFT JOIN SCM_CONTRACT SC ON SCI.SCM_CONTRACT_ID = SC.UUID" +
                "                WHERE FI.EXPENSES_TYPE = 0" +
                "                AND SC.PURCHASE_TYPES = '00'" +
                "                AND FI.UUID = '"+uuid+"'" +
                "                AND FI.SUPPLIER_NAME IS NOT NULL) T" +
                "        LEFT JOIN (SELECT FI.UUID," +
                "                FI.INVOICE_REQ_NO," +
                "                FI.SUPPLIER_NAME," +
                "                SCI.SCM_CONTRACT_ID," +
                "                SC.SCM_CONTRACT_CODE" +
                "                FROM FAD_INVOICE FI" +
                "                LEFT JOIN SCM_CONTRACT_INVOICE SCI ON FI.UUID =" +
                "                        SCI.FAD_INVOICE_ID" +
                "                LEFT JOIN SCM_CONTRACT SC ON SCI.SCM_CONTRACT_ID =" +
                "                        SC.UUID" +
                "                WHERE FI.EXPENSES_TYPE = 0" +
                "                AND SC.PURCHASE_TYPES = '00'" +
                "                AND FI.UUID != '"+uuid+"'" +
                "               AND FI.SUPPLIER_NAME IS NOT NULL) O ON T.SCM_CONTRACT_CODE =" +
                "                O.SCM_CONTRACT_CODE" +
                "        AND T.SUPPLIER_NAME !=" +
                "                O.SUPPLIER_NAME) R" +
                " WHERE R.SCM_CONTRACT_CODE IS NOT NULL";
        daoMeta.setStrSql(sql);
        List mergeList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);

        if (ListUtil.isNotEmpty(mergeList)) {
            String scmContractCode=""+(((Map) mergeList.get(0)).get("scmContractCode"));
            String invoiceReqNo=""+(((Map) mergeList.get(0)).get("invoiceReqNo"));
            String supplierName=""+(((Map) mergeList.get(0)).get("supplierName"));
            throw new BizException("提交失败！零星合同“"+scmContractCode+"”在流水号“"+invoiceReqNo+"”的发票中录入的供应商名为“"+supplierName+"”，两者必需一致。", new HashMap());
        }

    }


    //********************************************校验核销金额是否超额↓*********************************************************//
    /*新增时校验核销金额是否超额
    *fadCashReqId:为表fad_cash_req_invoice新增数据请款ID
    *
    */
    public String fadCashReqInvoiceCheckAdd(String fadCashReqId, BigDecimal value) {

        DAOMeta daoMeta = new DAOMeta();
        String sql = "";
        sql = " select t.uuid,t.running_No," +
                "nvl(t.money,0)-" +
                "nvl((" +
                "select sum(d.clearance_money) from fad_cash_req_invoice d" +
                " where d.fad_cash_req_id=t.uuid" +
                " AND (EXISTS (SELECT 1 FROM FAD_INVOICE F WHERE F.UUID = D.FAD_INVOICE_ID AND F.STATE <> '3') " +
                " OR EXISTS (SELECT 1 FROM FAD_CASH_CLEARANCE F WHERE F.UUID = D.FAD_INVOICE_ID AND F.STATE <> '3')" +
                ")),0) as balance from fad_cash_req t where t.uuid = '" + fadCashReqId + "' ";
        daoMeta.setStrSql(sql);
        //加进保证金请款以后，是可以跨部门的
        daoMeta.setNeedFilter(false);
        String result = "";
        List fadCashReqInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (!fadCashReqInfo.isEmpty() && fadCashReqInfo.size() != 0) {
            BigDecimal balance = new BigDecimal((((Map) fadCashReqInfo.get(0)).get("balance")).toString());//可用额度
            if (value.compareTo(balance) == 1) {
                result = (((Map) fadCashReqInfo.get(0)).get("runningNo")).toString();
            } else {
                result = "1";
            }
        } else {
            result = "2";
        }
        return result;//校验通过返回1，否则返回超额请款流水号，找不到该请款返回“2”
    }

    /*修改时校验核销金额是否超额
    *fadCashReqId:为表fad_cash_req_invoice修改数据uuid
    */
    public String fadCashReqInvoiceCheckModify(String detailUuid, BigDecimal value) {

        DAOMeta daoMeta = new DAOMeta();
        String sql = "";
        sql = "select uuid,running_No," +
                " nvl(money,0)-" +
                " nvl((select sum(d.clearance_money)" +
                " from fad_cash_req_invoice d" +
                " where d.fad_cash_req_id = t.uuid" +
                " and d.uuid <> '" + detailUuid + "'" +
                " AND (EXISTS (SELECT 1 FROM FAD_INVOICE F WHERE F.UUID = D.FAD_INVOICE_ID AND F.STATE <> '3') " +
                " OR EXISTS (SELECT 1 FROM FAD_CASH_CLEARANCE F WHERE F.UUID = D.FAD_INVOICE_ID AND F.STATE <> '3')" +
                ")),0) as balance" +
                " from fad_cash_req t" +
                " where uuid = (select fad_cash_req_id" +
                " from fad_cash_req_invoice i" +
                " where i.uuid = '" + detailUuid + "') ";
        daoMeta.setStrSql(sql);
        //加进保证金请款以后，是可以跨部门的
        daoMeta.setNeedFilter(false);
        String result = "";
        List fadCashReqInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (!fadCashReqInfo.isEmpty() && fadCashReqInfo.size() != 0) {
            BigDecimal balance = new BigDecimal((((Map) fadCashReqInfo.get(0)).get("balance")).toString());//可用额度
            if (value.compareTo(balance) == 1) {
                result = (((Map) fadCashReqInfo.get(0)).get("runningNo")).toString();
            } else {
                result = "1";
            }
        } else {
            result = "2";
        }
        return result;//校验通过返回1，否则返回超额请款流水号，找不到该请款返回“2”
    }

    //********************************************校验核销金额是否超额↑*********************************************************//
//********************************************项目差旅↓*********************************************************//
    public List getPrmTripBudget(String projectMainId) {//获取项目差旅预算使用情况
        DAOMeta daoMeta = new DAOMeta();
        String sql = "SELECT v.budget_Id,v.budget_code,v.budget_name,V.REMAIN_BUDGET ,V.IS_PURCHASE FROM VW_PRM_BUDGET v where v.budget_code = 'TRAVEL_CHARGE' and v.project_Main_Id ='" + projectMainId + "'";

        daoMeta.setStrSql(sql);
        List fadCashReqInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        return fadCashReqInfo;
    }

    public void checkPrmTripBudget(String projectMainId, BigDecimal payCash) {//校验项目差旅预算是否足够
        DAOMeta daoMeta = new DAOMeta();
        String sql = "SELECT v.budget_Id,v.budget_code,v.budget_name,V.REMAIN_BUDGET FROM VW_PRM_BUDGET v where v.budget_code = 'TRAVEL_CHARGE' and v.project_Main_Id ='" + projectMainId + "'";
        daoMeta.setStrSql(sql);
        List fadCashReqInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (!fadCashReqInfo.isEmpty() && fadCashReqInfo.size() != 0) {
            Map budgetMap = (Map) fadCashReqInfo.get(0);
            BigDecimal balance = new BigDecimal((budgetMap.get("remainBudget")).toString());//可用额度
            if (payCash.compareTo(balance) == 1) {
                throw new BizException("对应费用预算额度不足！", new HashMap());
            }
        } else {
            throw new BizException("目前没有分配对应费用预算！", new HashMap());
        }
    }

    //********************************************项目差旅↑***********************************************************//
    //********************************************非项目差旅↓*********************************************************//
    public List getNonTripBudget(String officeCode, String fadYear) {//获取非项目差旅预算使用情况
        DAOMeta daoMeta = new DAOMeta();
//        Map budgetMap = new HashMap();
        String sql = "select * from  VW_NON_BUDGET_EXECUTE v where v.FINANCIAL_SUBJECT ='差旅费' and v.OFFICE_ID ='" + officeCode + "' and v.year = '" + fadYear + "'";
        daoMeta.setStrSql(sql);
        List fadCashReqInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        return fadCashReqInfo;
    }

    public void checkNonTripBudget(String officeCode, BigDecimal payCash, String fadYear) {//校验非项目郑州预算是否足够
        DAOMeta daoMeta = new DAOMeta();
//        Map budgetMap = new HashMap();
        String sql = "select * from  VW_NON_BUDGET_EXECUTE v where v.FINANCIAL_SUBJECT ='差旅费' and v.OFFICE_ID ='" + officeCode + "' and v.year = '" + fadYear + "'";
        daoMeta.setStrSql(sql);
        List fadCashReqInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (!fadCashReqInfo.isEmpty() && fadCashReqInfo.size() != 0) {
            Map budgetMap = (Map) fadCashReqInfo.get(0);
            BigDecimal balance = new BigDecimal((budgetMap.get("remainBudget")).toString());//可用额度
            if (payCash.compareTo(balance) == 1) {
                throw new BizException("对应费用预算额度不足！", new HashMap());
            }
        } else {
            throw new BizException("目前没有分配对应费用预算！", new HashMap());
        }
    }

    //********************************************非项目差旅↑*********************************************************//
    //********************************************日常报销↓*********************************************************//
    public void checkBudgetIsEnough(boolean isPrm, String budgetId, BigDecimal value) {
        DAOMeta daoMeta = new DAOMeta();
        String sql = "";
        if (isPrm) {
            sql = "SELECT V.REMAIN_BUDGET AS balance FROM VW_PRM_BUDGET v where v.BUDGET_ID ='" + budgetId + "'";
        } else {
            sql = "SELECT V.REMAIN_BUDGET AS balance FROM VW_NON_BUDGET_EXECUTE v where v.uuid ='" + budgetId + "'";
        }
        daoMeta.setStrSql(sql);
//        String result = "";
        List fadCashReqInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (!fadCashReqInfo.isEmpty() && fadCashReqInfo.size() != 0) {
            BigDecimal balance = new BigDecimal((((Map) fadCashReqInfo.get(0)).get("balance")).toString());//可用额度
            if (value.compareTo(balance) == 1) {
                throw new BizException("对应费用预算额度不足！", new HashMap());
            }
        } else {
            throw new BizException("目前没有分配对应费用预算！", new HashMap());
        }
    }

    //********************************************日常报销↑*********************************************************//
    //********************************************校验是否存在对应采购请款未发送NC↓*********************************************************//
    public void checkForCreateCertificate(String uuid) {
        DAOMeta daoMeta = new DAOMeta();
        String reqSql = "SELECT (SELECT SC.SCM_CONTRACT_CODE" +
                " FROM SCM_CONTRACT SC" +
                " WHERE SC.UUID = PURCHASE_CONTRACT_ID) SCM_CONTRACT_CODE," +
                " RUNNING_NO" +
                " FROM FAD_CASH_REQ FCR" +
                " WHERE FCR.REQ_TYPE = '0'" +
                " AND FCR.STATE IN ('0', '1', '2', '8')" +
                " AND FCR.PURCHASE_CONTRACT_ID IN" +
                " (SELECT SCI.SCM_CONTRACT_ID" +
                " FROM FAD_INVOICE FI" +
                " LEFT JOIN SCM_CONTRACT_INVOICE SCI ON FI.UUID = SCI.FAD_INVOICE_ID" +
                " WHERE FI.UUID = '" + uuid + "')";
        daoMeta.setStrSql(reqSql);
        List<Map<String, Object>> reqList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(reqList)) {
            StringBuffer sb = new StringBuffer();
            for (Map req : reqList) {
                sb.append(req.get("scmContractCode").toString() + "合同存在未发送NC请款，流水号：" + req.get("runningNo").toString() + "</br>");
            }
            sb.append("不能生成凭证！");
            throw new BizException(sb.toString(), new HashMap());
        }
    }

    //********************************************校验是否存在对应采购请款未发送NC↑*********************************************************//
    @Override
    public FadInvoice getObjectById(String invoiceId) {
        return PersistenceFactory.getInstance().findByPK(FadInvoice.class, invoiceId);
    }

    @Override
    public Map<String, FadInvoice> getObjectsByIds(List lstUuid) {
        Map<String, FadInvoice> outMap = new HashMap();
        if (ListUtil.isNotEmpty(lstUuid)) {
            QueryCondition condition1 = new QueryCondition();
            condition1.setField(FadInvoiceAttribute.UUID);
            condition1.setOperator("in");
            condition1.setValueList(lstUuid);
            List<QueryCondition> lstCondition = new ArrayList<QueryCondition>();
            lstCondition.add(condition1);
            List<FadInvoice> objs = PersistenceFactory.getInstance().findByAnyFields(FadInvoice.class, lstCondition, null);
            if (objs != null && objs.size() > 0) {
                objs.forEach(n -> {
                    outMap.put(n.getUuid(), n);
                });
            }
            return outMap;
        } else {
            return outMap;
        }
    }

    @Override
    public Map<String, FadCashClearance> getFadCashClearanceByIds(List lstUuid) {
        if (ListUtil.isNotEmpty(lstUuid)) {
            QueryCondition condition1 = new QueryCondition();
            condition1.setField(CommonAttribute.UUID);
            condition1.setOperator("in");
            condition1.setValueList(lstUuid);
            List<QueryCondition> lstCondition = new ArrayList<QueryCondition>();
            lstCondition.add(condition1);
            return PersistenceFactory.getInstance().findByAnyFields(FadCashClearance.class, lstCondition, null).stream().
                    collect(Collectors.toMap(FadCashClearance::getUuid, p -> p));
        } else {
            return new HashMap<String, FadCashClearance>();
        }
    }

    //提交
    @Override
    public boolean changeStateToSubmitted(String uuid) {
        FadInvoice fadInvoice = PersistenceFactory.getInstance().findByPK(FadInvoice.class, uuid);
        if (fadInvoice != null) {
            fadInvoice.setState(BillStateAttribute.FAD_BILL_STATE_SUBMITTED);
            PersistenceFactory.getInstance().update(fadInvoice);
        } else {
            throw new BizException(MessageKeyAttribute.ERP_NO_RECORD_SELECTED, new HashMap());
        }
        return true;
    }

    //审核
    @Override
    public boolean changeStateToApproved(String uuid) {
        FadInvoice fadInvoice = PersistenceFactory.getInstance().findByPK(FadInvoice.class, uuid);
        if (fadInvoice != null) {
            fadInvoice.setState(BillStateAttribute.FAD_BILL_STATE_APPROVED);
            PersistenceFactory.getInstance().update(fadInvoice);
        } else {
            throw new BizException(MessageKeyAttribute.ERP_NO_RECORD_SELECTED, new HashMap());
        }
        return true;
    }

    //作废
    @Override
    public boolean changeStateToDisabled(String uuid) {
        FadInvoice fadInvoice = PersistenceFactory.getInstance().findByPK(FadInvoice.class, uuid);
        if (fadInvoice != null) {
            fadInvoice.setState(BillStateAttribute.FAD_BILL_STATE_DISABLED);
            PersistenceFactory.getInstance().update(fadInvoice);
        } else {
            throw new BizException(MessageKeyAttribute.ERP_NO_RECORD_SELECTED, new HashMap());
        }
        return true;
    }

    //已生成凭证
    @Override
    public boolean changeStateToCertificated(String uuid) {
        FadInvoice fadInvoice = PersistenceFactory.getInstance().findByPK(FadInvoice.class, uuid);
        if (fadInvoice != null) {
            fadInvoice.setState(BillStateAttribute.FAD_BILL_STATE_CERTIFICATED);
            fadInvoice.setCertificateCreateTime(new Date());
            PersistenceFactory.getInstance().update(fadInvoice);
        } else {
            throw new BizException(MessageKeyAttribute.ERP_NO_RECORD_SELECTED, new HashMap());
        }
        return true;
    }

    //已发送NC
    @Override
    public boolean changeStateToSended(String uuid) {
        FadInvoice fadInvoice = PersistenceFactory.getInstance().findByPK(FadInvoice.class, uuid);
        if (fadInvoice != null) {
            fadInvoice.setState(BillStateAttribute.FAD_BILL_STATE_SENT);
            PersistenceFactory.getInstance().update(fadInvoice);
        } else {
            throw new BizException(MessageKeyAttribute.ERP_NO_RECORD_SELECTED, new HashMap());
        }
        return true;
    }


    // 存储过程核销
    public void contractInvoiceWrittenOff(String action, String fadInvoiceId) {
        List lastParam = new ArrayList();
        lastParam.add(action);
        lastParam.add(fadInvoiceId);
        String message = (PersistenceFactory.getInstance().getDataFromProcOrFunc("call PKG_FAD.SP_INVOICE_WRITTEN_OFF(?,?,?)", lastParam, PersistenceCrudManager.RETURN_DATA_TYPE.VARCHAR)).toString();
        Util.println(message);
    }

    //JAVA核销
    public void contractInvoiceClearance(FadInvoiceDto dto) {

//        List<String> UUids = dto.getFadCashReqInvoiceDto().stream().map(t -> t.getUuid()).collect(Collectors.toList());
//        DtoHelper.casecadeDelete(FadCashReqInvoiceDto.class, UUids, false);
//
//        dto.getFadCashReqInvoiceDto().clear();
//        List<ScmContractInvoiceDto> scmContractInvoiceDtoList = dto.getScmContractInvoiceDto();
//
//        if (!ListUtil.isEmpty(scmContractInvoiceDtoList)) {
//            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
//            Map mapContractId = new HashMap();
//
//            for (ScmContractInvoiceDto d : scmContractInvoiceDtoList) {
//                mapContractId.put(FadCashReqAttribute.PURCHASE_CONTRACT_ID, d.getScmContractId());
//            }
//            List<FadCashReq> fadCashReqs = pcm.findByAnyFields(FadCashReq.class, mapContractId, null).stream().collect(Collectors.toList());
//
//            Map<String, List<FadCashReq>> fadPayReqCGroup = fadCashReqs.stream().collect(Collectors.groupingBy(FadCashReq::getPurchaseContractId));
//
//            Map fadCashReqId = new HashMap();
//            fadCashReqs.forEach(f -> fadCashReqId.put(FadCashReqInvoiceAttribute.FAD_CASH_REQ_ID, f.getUuid()));
//
//            List<FadCashReqInvoice> fadCashReqInvoices = pcm.findByAnyFields(FadCashReqInvoice.class, fadCashReqId, null);
//
//
//            Map<String, Double> fadCashReqCleared = fadCashReqInvoices.stream().
//                    collect(Collectors.groupingBy(FadCashReqInvoice::getFadCashReqId, Collectors.summingDouble(p -> p.getClearanceMoney().doubleValue())));
//
//            //所有相关请款没有核销明细
//            Map<String, BigDecimal> fadCashReqUnCleared = new HashMap<String, BigDecimal>();
//
//            fadCashReqs.forEach(t -> {
//                if (fadCashReqCleared.containsKey(t.getUuid())) {
//                    double unClearedMoney = t.getMoney().doubleValue() - fadCashReqCleared.get(t.getUuid()).doubleValue();
//                    if (unClearedMoney > 0) {
//                        fadCashReqUnCleared.put(t.getUuid(), new BigDecimal(unClearedMoney));
//                    }
//                }
//            });
//
//            scmContractInvoiceDtoList.forEach(t -> {
//                BigDecimal amount = new BigDecimal(t.getAmount().toString());
//                List<FadCashReq> fad = fadPayReqCGroup.get(t.getUuid());
//                for (FadCashReq req : fad) {
//                    if (amount.compareTo(BigDecimal.ZERO) > 0 && fadCashReqUnCleared.containsKey(req.getUuid())) {
//                        BigDecimal unCleared = fadCashReqUnCleared.get(req.getUuid());
//                        FadCashReqInvoiceDto invoice = new FadCashReqInvoiceDto();
//                        invoice.setFadInvoiceId(dto.getUuid());
//                        invoice.setFadCashReqId(req.getUuid());
//                        invoice.setClearanceType("0");
//                        invoice.setClearanceMoney(amount.compareTo(unCleared) > 0 ? unCleared : amount);
//                        invoice.setEditflag("+");
//
//                        amount = amount.subtract(unCleared);
//                        dto.getFadCashReqInvoiceDto().add(invoice);
//                    }
//                }
//            });
//        }
    }

    @Override
    public void isAddPackage(String subjectId) {
        if (StringUtil.isNotEmpty(subjectId)) {
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            Map conditon = new HashMap<>();
            conditon.put(PrmPurchasePlanDetailAttribute.PRM_BUDGET_REF_ID, subjectId);
            List<PrmPurchasePlanDetail> prmPurchasePlanDetails = pcm.findByAnyFields(PrmPurchasePlanDetail.class, conditon, null);
            if (ListUtil.isNotEmpty(prmPurchasePlanDetails)) {
                PrmPurchasePlanDetail prmPurchasePlanDetail = prmPurchasePlanDetails.get(0);
                if ("RUN".equals(prmPurchasePlanDetail.getPrmBudgetType()) && prmPurchasePlanDetail.getSubPackageNo() != null) {
                    throw new BizException(prmPurchasePlanDetail.getName() + "费用已加入采购计划包，不能进行日常报销");
                }
            }
        }
    }
}