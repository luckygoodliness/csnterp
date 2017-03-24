package com.csnt.scdp.bizmodules.modules.fad.cashreq.services.impl;

import com.atomikos.icatch.SysException;
import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.attributes.OriginalFormTypeAttribute;
import com.csnt.scdp.bizmodules.entity.cdm.CdmFileRelation;
import com.csnt.scdp.bizmodules.entity.fad.FadCashReq;
import com.csnt.scdp.bizmodules.entity.fad.FadPayReqC;
import com.csnt.scdp.bizmodules.entity.fad.FadPayReqH;
import com.csnt.scdp.bizmodules.entity.nonprm.SubjectSubject;
import com.csnt.scdp.bizmodules.entity.operate.OperateBusinessBidInfo;
import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entity.scm.ScmContract;
import com.csnt.scdp.bizmodules.entityattributes.cdm.CdmFileRelationAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCashReqAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadPayReqHAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmContractAttribute;
import com.csnt.scdp.bizmodules.modules.cdm.cdmfile.dto.CdmFileRelationDto;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpUserHelper;
import com.csnt.scdp.bizmodules.modules.fad.cashreq.dto.FadCashReqDto;
import com.csnt.scdp.bizmodules.modules.fad.cashreq.services.intf.CashreqManager;
import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
import com.csnt.scdp.bizmodules.modules.fad.payreq.dto.FadPayReqCDto;
import com.csnt.scdp.bizmodules.modules.nonprm.subjectsubject.services.intf.SubjectsubjectManager;
import com.csnt.scdp.bizmodules.modules.prm.projectmain.helper.ProjectMainHelper;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.helper.UserHelper;
import com.csnt.scdp.framework.util.*;
import com.csnt.scdp.sysmodules.entityattributes.ScdpMsgAttribute;
import com.csnt.scdp.sysmodules.helper.MsgSendHelper;
import com.csnt.scdp.sysmodules.helper.NumberingHelper;
import com.csnt.scdp.sysmodules.modules.sys.msgcenter.dto.ReceiptsMeta;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description:  CashreqManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-22 09:40:50
 */

@Scope("singleton")
@Service("cashreq-manager")
public class CashreqManagerImpl implements CashreqManager {
    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Resource(name = "subjectsubject-manager")
    private SubjectsubjectManager subjectsubjectManager;

    @Override
    public void generateScmCashreqByScmContract(List<ScmContract> lst) {
        for (ScmContract contract : lst) {
            String contractPayType = contract.getContractPayType();
            List<String> invoiceReqNos = getUnFinishedFadInvoice(contract.getUuid());
            if (invoiceReqNos.size() > 0) {
                throw new BizException("该请款涉及的发票" + invoiceReqNos.get(0) + "未处理完！无法创建采购请款！");
            } else {
                if (ScmContractAttribute.CONTRACT_PAY_TYPE_VALUE_1.equals(contractPayType) ||
                        ScmContractAttribute.CONTRACT_PAY_TYPE_VALUE_2.equals(contractPayType) ||
                        ScmContractAttribute.CONTRACT_PAY_TYPE_VALUE_5.equals(contractPayType)) {
                    String uuid = UUIDUtil.getUUID();
                    FadCashReqDto cashReq = new FadCashReqDto();
                    cashReq.setUuid(uuid);
                    cashReq.setContractPayType(contractPayType);
                    cashReq.setSupplierId(contract.getSupplierCode());
                    cashReq.setSupplierName(contract.getSupplierName());
                    cashReq.setProjectId(contract.getProjectId());
                    cashReq.setMoney(contract.getTotalValue());
                    cashReq.setPayStyle(contract.getPayType());
                    cashReq.setSquareDate(contract.getDebtDate());
                    cashReq.setIsUrgent(contract.getIsUrgent());
                    cashReq.setRemark(contract.getRemark());
                    cashReq.setPurchaseContractId(contract.getUuid());
                    cashReq.setReqType(FadCashReqAttribute.REQ_TYPE_VALUE_0);
                    cashReq.setPackageUuid(contract.getPurchasePackageId());
                    cashReq.setIsProject(contract.getIsProject());
                    cashReq.setEditflag("+");
                    if (contract.getIsProject() == null || contract.getIsProject() == 0) {
                        cashReq.setSubjectCode(contract.getSubjectCode());
                        cashReq.setFadSubjectCode(contract.getSubjectCode());
                        List<SubjectSubject> subjects = subjectsubjectManager.getObjByFSCode(contract.getSubjectCode());
                        if (ListUtil.isNotEmpty(subjects)) {
                            cashReq.setSubjectName(subjects.get(0).getFinancialSubject());
                        }
                    } else {
                        PrmProjectMain pro = PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, contract.getProjectId());
                        if (pro != null) {
                            cashReq.setFadSubjectCode(pro.getProjectCode());
                            cashReq.setProjectName(pro.getProjectName());
                        }
                    }

                    cashReq.setBudgetCUuid(contract.getBugdetId());
                    cashReq.setState(BillStateAttribute.FAD_BILL_STATE_APPROVED);
                    cashReq.setIsIndividual(FadCashReqAttribute.IS_INDIVIDUAL_0);
                    cashReq.setStaffId(contract.getOperatorId());
                    cashReq.setOfficeId(contract.getOfficeId());
                    cashReq.setOperatorId(contract.getOperatorId());
                    cashReq.setOperatorName(contract.getOperatorName());
                    cashReq.setOperateTime(Calendar.getInstance().getTime());
                    cashReq.setApplyDate(Calendar.getInstance().getTime());
                    cashReq.setIsAdvancePayment(new Integer("1"));
                    cashReq.setRemark("合同预付款自动生成");
                    cashReq.setModel(contract.getOtherDes());
                    if (ScmContractAttribute.CONTRACT_PAY_TYPE_VALUE_1.equals(contractPayType)) {
                        cashReq.setPayeeName(contract.getPayeeName());
                        cashReq.setPayeeBankName(contract.getBank());
                        cashReq.setPayeeAccount(contract.getBankId());
                    } else if (ScmContractAttribute.CONTRACT_PAY_TYPE_VALUE_2.equals(contractPayType)) {
                        cashReq.setStaffId(contract.getDebter());
                        cashReq.setOfficeId(contract.getDebterDepartment());
                    } else if (ScmContractAttribute.CONTRACT_PAY_TYPE_VALUE_5.equals(contractPayType)) {
                        cashReq.setPayeeName(contract.getPayeeName());
                        cashReq.setPayeeBankName(contract.getBank());
                        cashReq.setPayeeAccount(contract.getBankId());
                        cashReq.setElectricCommercialStore(contract.getElectricCommercialStore());
                        cashReq.setJdName(contract.getJdName());
                        cashReq.setJdPassword(contract.getJdPassword());
                        cashReq.setJdOrderNo(contract.getJdOrderNo());
                        cashReq.setJdOrderDate(contract.getJdOrderDate());
                        cashReq.setJdLastDate(contract.getJdLastDate());

//                Map<String, Object> queryMap = new HashMap();
//                String userId = UserHelper.getUserId();
//                queryMap.put("dataId", contract.getUuid());
//                List<CdmFileRelationDto> cdmFileRelationList = PersistenceFactory.getInstance().findByAnyFields(CdmFileRelationDto.class,queryMap,null);
                        String strSql = "SELECT *" +
                                "  FROM (SELECT *" +
                                "          FROM CDM_FILE_RELATION S" +
                                "         WHERE S.DATA_ID = '" + contract.getUuid() + "'" +
                                "           AND S.FILE_CLASSIFY = 'SCM_CONTRACT'" +
                                "         ORDER BY S.CREATE_TIME DESC)" +
                                " WHERE ROWNUM = 1";
                        DAOMeta daoMeta = new DAOMeta(strSql);
                        List<Map<String, Object>> cdmFileRelationList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                        CdmFileRelationDto cdmFileRelationDto = (CdmFileRelationDto) BeanUtil.map2Dto(cdmFileRelationList.get(0), CdmFileRelationDto.class);
                        cdmFileRelationDto.setUuid("");
                        cdmFileRelationDto.setDataId("");
                        cdmFileRelationDto.setEditflag("+");
                        cdmFileRelationDto.setCdmFileType("NONPRM_CASH_DEPOSIT");
                        cdmFileRelationDto.setTblVersion("");
                        List<CdmFileRelationDto> cdmFileRelationDtoList = new ArrayList<>();
                        cdmFileRelationDtoList.add(cdmFileRelationDto);
                        cashReq.setCdmFileRelationDto(cdmFileRelationDtoList);
                    }
                    String runningNo = NumberingHelper.getNumbering("REQ_NO", null);//流水号
                    cashReq.setRunningNo(runningNo);
                    DtoHelper.CascadeSave(cashReq);//数据保存

                    //如果是商城支付发送消息

                    String sql = "SELECT * FROM V_USER_ROLES T WHERE ROLE_NAME = '会计'";
                    if (ScmContractAttribute.CONTRACT_PAY_TYPE_VALUE_5.equals(contractPayType)) {
                        sql = "SELECT * FROM V_USER_ROLES T WHERE ROLE_NAME IN('出纳','会计')";
                    }
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
                    String templateNo = "FAD_CASH_REQ_MESSAGE";
                    Map map = BeanUtil.bean2Map(cashReq);
                    map.put("msgTitel", "合同请款");
                    if (ScmContractAttribute.CONTRACT_PAY_TYPE_VALUE_5.equals(contractPayType)) {
                        map.put("msgTitel", "商城采购待付款");
                    }
                    MsgSendHelper.sendMsg(map, msgType, templateNo, receiptsMeta);
                }
            }
        }
    }


    @Override
    public String generateScmCashreqByScmContractandAmount(ScmContract contract, BigDecimal amount, Integer advanced, String msg, FadPayReqC reqc) {
        String contractPayType = contract.getContractPayType();
        FadCashReq cashReq = null;
        cashReq = new FadCashReq();
        cashReq.setContractPayType(contractPayType);
        cashReq.setSupplierId(contract.getSupplierCode());
        cashReq.setSupplierName(contract.getSupplierName());
        cashReq.setProjectId(contract.getProjectId());
        cashReq.setMoney(amount);
        cashReq.setPayStyle(contract.getPayType());
        cashReq.setSquareDate(contract.getDebtDate());
        cashReq.setIsUrgent(contract.getIsUrgent());
        cashReq.setRemark(contract.getRemark());
        cashReq.setPurchaseContractId(contract.getUuid());
        cashReq.setReqType(FadCashReqAttribute.REQ_TYPE_VALUE_0);
        cashReq.setPackageUuid(contract.getPurchasePackageId());
        cashReq.setIsProject(contract.getIsProject());
        if (contract.getIsProject() == null || contract.getIsProject() == 0) {
            cashReq.setFadSubjectCode(contract.getSubjectCode());
            cashReq.setSubjectCode(contract.getSubjectCode());
            List<SubjectSubject> subjects = subjectsubjectManager.getObjByFSCode(contract.getSubjectCode());
            if (ListUtil.isNotEmpty(subjects)) {
                cashReq.setSubjectName(subjects.get(0).getFinancialSubject());
            }
        } else {
            PrmProjectMain pro = PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, contract.getProjectId());
            if (pro != null) {
                cashReq.setFadSubjectCode(pro.getProjectCode());
                cashReq.setProjectName(pro.getProjectName());
            }
        }

        cashReq.setBudgetCUuid(contract.getBugdetId());
        cashReq.setState(BillStateAttribute.FAD_BILL_STATE_APPROVED);
        cashReq.setIsIndividual(FadCashReqAttribute.IS_INDIVIDUAL_0);
        cashReq.setOfficeId(contract.getOfficeId());
        if (reqc != null) {
            cashReq.setFadPayReqCUuid(reqc.getUuid());
            cashReq.setStaffId(reqc.getCreateBy());
            cashReq.setOperatorId(reqc.getCreateBy());
            String name = ErpUserHelper.getUserNameById(reqc.getCreateBy());
            cashReq.setOperatorName(name);
        } else {
            cashReq.setStaffId(contract.getDebter());
            cashReq.setOperatorId(contract.getOperatorId());
            cashReq.setOperatorName(contract.getOperatorName());
        }
        cashReq.setOperateTime(Calendar.getInstance().getTime());
        cashReq.setApplyDate(Calendar.getInstance().getTime());
        cashReq.setIsAdvancePayment(advanced);
        cashReq.setRemark(msg);
        if (ScmContractAttribute.CONTRACT_PAY_TYPE_VALUE_1.equals(contractPayType)) {
            cashReq.setPayeeName(contract.getPayeeName());
            cashReq.setPayeeBankName(contract.getBank());
            cashReq.setPayeeAccount(contract.getBankId());
        }
        String runningNo = NumberingHelper.getNumbering("REQ_NO", null);//流水号
        cashReq.setRunningNo(runningNo);
        Object obj = saveFadCashReq(cashReq);
        return (String) obj;
    }

    @Override
    public String generateScmCashreqByPayReqCDto(FadPayReqCDto t) {
        String uuid = null;
        if (Arrays.binarySearch(BillStateAttribute.getFadBillAuditState(), t.getState()) >= 0) {
            if (t.getCashReqValue() == null || t.getCashReqValue().compareTo(BigDecimal.ZERO) == 0) {
                BigDecimal needPayMoney = t.getScmContractNeedToPay();//应付账款
                BigDecimal baseMoney = MathUtil.compareTo(MathUtil.sub(t.getScmContractAmount(), t.getScmContractPaidMoney()), t.getAuditMoney()) > 0 ?
                        t.getAuditMoney() : MathUtil.sub(t.getScmContractAmount(), t.getScmContractPaidMoney());
                BigDecimal payableMoney = baseMoney.subtract(needPayMoney);

                FadPayReqC req = getPayReqCByUuid(t.getUuid());
                //remark:存在应付账款的话，临时支付或者月度支付时，点请款，只能请减掉应付账款后的金额
                if (payableMoney.compareTo(BigDecimal.ZERO) > 0) {
                    FadPayReqH fadPayReqH = PersistenceFactory.getInstance().findByPK(FadPayReqH.class, t.getPuuid());
                    String msg = t.getSeqNo() + "";
                    if (fadPayReqH != null) {
                        if (FadPayReqHAttribute.REQ_TYPE_MONTH.equals(fadPayReqH.getReqType())) {
                            msg = "月度支付申请：" + fadPayReqH.getReqno() + "；序号：" + msg + "；系统自动生成";
                        } else {
                            msg = "临时支付申请：" + fadPayReqH.getReqno() + "；序号：" + msg + "；系统自动生成";
                        }
                    }

                    ScmContract scmContract = PersistenceFactory.getInstance().findByPK(ScmContract.class, t.getScmContractId());
                    uuid = generateScmCashreqByScmContractandAmount(scmContract, payableMoney, new Integer("2"), msg, req);
                    if (StringUtil.isNotEmpty(uuid)) {
                        //修改需求请款金额和应付账款
                        req.setCashReqValue(payableMoney);
                        updatePayReqC(req);
                    }
                }
            }
        }
        return uuid;
    }

    @Override
    public FadPayReqC getPayReqCByUuid(String uuid) {
        return PersistenceFactory.getInstance().findByPK(FadPayReqC.class, uuid);
    }

    private void updatePayReqC(FadPayReqC fadPayReqC) {
        PersistenceFactory.getInstance().update(fadPayReqC);
    }

    @Override
    public boolean checkWorkAction(FadCashReq fadCashReq, String actionType) {
        boolean rtnFlag = false;
        if (fadCashReq != null) {
            switch (actionType) {
                case "submit":
                    BigDecimal remainBudget = getRemainBudget(fadCashReq);
                    rtnFlag = (remainBudget.subtract(fadCashReq.getMoney())).compareTo(BigDecimal.ZERO) >= 0;
                    break;
            }
        }
        return rtnFlag;
    }

    @Override
    public BigDecimal getRemainBudget(FadCashReq fadCashReq) {
        BigDecimal rtn = BigDecimal.ZERO;
        if (fadCashReq != null) {
            DAOMeta daoMeta = new DAOMeta();
            String sql;
            String remainBudget = "0";
            if (FadCashReqAttribute.REQ_TYPE_VALUE_0.equals(fadCashReq.getReqType()) &&
                    StringUtil.isNotEmpty(fadCashReq.getPurchaseContractId())) {
                sql = " select * from VW_SCM_CONTRACT_EXECUTE t\n" +
                        " where t.UUID ='" + fadCashReq.getPurchaseContractId() + "'";
                daoMeta.setStrSql(sql);
                List lstObjInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                if (ListUtil.isNotEmpty(lstObjInfo)) {
                    remainBudget = ((Map) lstObjInfo.get(0)).get("scmUnlockedMoney").toString();
                }
            } else {
                if (StringUtil.isNotEmpty(fadCashReq.getProjectId()) &&
                        StringUtil.isNotEmpty(fadCashReq.getBudgetCUuid())) {
                    sql = "select t.*\n" +
                            " from   vw_prm_budget t\n" +
                            " where  t.package_id is null\n" +
                            " and    t.budget_id ='" + fadCashReq.getBudgetCUuid() + "'";
                } else {
                    sql = "select *\n" +
                            " from   vw_non_budget_execute t\n" +
                            " where  t.uuid='" + fadCashReq.getBudgetCUuid() + "'";
                }
                daoMeta.setStrSql(sql);
                List lstObjInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                if (ListUtil.isNotEmpty(lstObjInfo)) {
                    remainBudget = ((Map) lstObjInfo.get(0)).get("remainBudget").toString();
                    if (fadCashReq.getIsProject() != null && fadCashReq.getIsProject() == 0) {
                        String budgetYear = ((Map) lstObjInfo.get(0)).get("year").toString();
                        int curYear = Calendar.getInstance().get(Calendar.YEAR);
                        if (!budgetYear.equals(String.valueOf(curYear))) {
//                        throw new BizException("非当年的预算无法提交申请！");
                            remainBudget = "0";
                        }
                    }
                }
            }
            rtn = new BigDecimal(remainBudget);
        }
        return rtn;
    }

    @Override
    public FadCashReq getFadCashReqbyUuid(String uuid) {
        return PersistenceFactory.getInstance().findByPK(FadCashReq.class, uuid);
    }

    @Override
    public void updateFadCashReq(FadCashReq fadCashReq) {
        PersistenceFactory.getInstance().update(fadCashReq);
    }

    private Object saveFadCashReq(FadCashReq fadCashReq) {
        if (FadCashReqAttribute.REQ_TYPE_VALUE_0.equals(fadCashReq.getReqType())) {
            List<String> invoiceReqNos = getUnFinishedFadInvoice(fadCashReq.getPurchaseContractId());
            if (invoiceReqNos.size() > 0) {
                throw new BizException("该请款涉及的发票" + invoiceReqNos.get(0) + "未处理完！无法创建采购请款！");
            } else {
                return PersistenceFactory.getInstance().insert(fadCashReq);
            }
        } else {
            return PersistenceFactory.getInstance().insert(fadCashReq);
        }
    }

    @Override
    public List<String> getUnFinishedFadInvoice(String contractCode) {
        DAOMeta daoMeta = new DAOMeta();
        String sql = "SELECT *\n" +
                "  FROM FAD_INVOICE T\n" +
                " WHERE T.STATE IN ('0', '1', '2', '8')\n" +
                "   AND T.UUID IN\n" +
                "       (SELECT FAD_INVOICE_ID\n" +
                "          FROM SCM_CONTRACT_INVOICE\n" +
                "         WHERE SCM_CONTRACT_ID = '" + contractCode + "')";
        daoMeta.setStrSql(sql);

        return PersistenceFactory.getInstance().findByNativeSQL(daoMeta).stream()
                .map(x -> x.get("invoiceReqNo").toString())
                .collect(Collectors.toList());
    }

    @Override
    public List<FadCashReq> getFadCashReqByBidInfoUuid(String InfoUuid) {
        List<FadCashReq> rtn = new ArrayList<>();
        if (StringUtil.isNotEmpty(InfoUuid)) {
            Map<String, Object> mapConditions = new HashMap<String, Object>();
            mapConditions.put(FadCashReqAttribute.OPERATE_BUSINESS_BID_INFO_ID, InfoUuid);
            rtn = PersistenceFactory.getInstance().findByAnyFields(FadCashReq.class, mapConditions, null)
                    .stream().filter(t ->
                            !BillStateAttribute.CMD_BILL_STATE_DISABLED.equals(t.getState()))
                    .collect(Collectors.toList());
        }
        return rtn;
    }

    @Override
    public void sendMsg(String uuid, String payeeName, String type) {
        String sql = "SELECT * FROM V_USER_ROLES T WHERE ROLE_NAME ='出纳'";
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
        String templateNo = null;
        if (type.equals("noDepositCustomer")) {
            templateNo = "NC_DEPOSIT_INFO_CHECKED";
        } else {
            templateNo = "CUSTOMER_INFO_CHECKED";
        }
        Map map = new HashMap<>();
        map.put("uuid", uuid);
        map.put("payeeName", payeeName);
        MsgSendHelper.sendMsg(map, msgType, templateNo, receiptsMeta);

    }

    @Override
    public Map validateCashReqDepositBeforeWorkFlowCommitOfStart(String uuid) {
        Map rtn = new HashMap();
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(CdmFileRelationAttribute.DATA_ID, uuid);
        List files = PersistenceFactory.getInstance().findByAnyFields(CdmFileRelation.class, mapConditions, null);
        if (files.size() < 1) {
            rtn.put(CommonAttribute.SUCCESS, false);
            rtn.put(CommonAttribute.MESSAGE, "附件信息不能为空，请上传附件信息！");
        } else {
            rtn.put(CommonAttribute.SUCCESS, true);
        }
        return rtn;
    }
}