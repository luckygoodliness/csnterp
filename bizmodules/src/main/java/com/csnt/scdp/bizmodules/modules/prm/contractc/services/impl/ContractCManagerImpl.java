package com.csnt.scdp.bizmodules.modules.prm.contractc.services.impl;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.attributes.MessageKeyAttribute;
import com.csnt.scdp.bizmodules.entity.cdm.CdmFileRelation;
import com.csnt.scdp.bizmodules.entity.operate.OperateBusinessBidInfo;
import com.csnt.scdp.bizmodules.entity.prm.PrmContract;
import com.csnt.scdp.bizmodules.entity.prm.PrmContractC;
import com.csnt.scdp.bizmodules.entity.prm.PrmCustomer;
import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entity.prm.PrmPurchaseReq;
import com.csnt.scdp.bizmodules.entityattributes.cdm.CdmFileRelationAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmContractAttribute;
import com.csnt.scdp.bizmodules.modules.operate.businessbidinfo.services.intf.BusinessbidinfoManager;
import com.csnt.scdp.bizmodules.modules.prm.prmexamperiod.services.intf.PrmExamPeriodManager;
import com.csnt.scdp.bizmodules.modules.prm.contractc.dto.PrmContractCDto;
import com.csnt.scdp.bizmodules.modules.prm.contractc.services.intf.ContractCManager;
import com.csnt.scdp.bizmodules.modules.prm.prmcustomer.services.intf.PrmcustomerManager;
import com.csnt.scdp.bizmodules.modules.prm.projectmain.dto.PrmProjectMainDto;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.bo.QueryCondition;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.helper.MessageHelper;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.entityattributes.ScdpMsgAttribute;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;
import com.csnt.scdp.sysmodules.helper.MsgSendHelper;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import com.csnt.scdp.sysmodules.modules.sys.msgcenter.dto.ReceiptsMeta;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:  ContractManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 09:57:20
 */

@Scope("singleton")
@Service("contract-c-manager")
public class ContractCManagerImpl implements ContractCManager {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ContractCManagerImpl.class);

    @Resource(name = "businessbidinfo-manager")
    private BusinessbidinfoManager operateBusinessManager;

    @Override
    public String isNullReturnEmpty(Object value) {
        if (value == null || value.equals("null")) {
            return "";
        } else {
            return value.toString().trim();
        }
    }

    @Override
    public PrmContractC findContractHeaderOnly(String uuid) {
        if (StringUtil.isNotEmpty(uuid)) {
            return PersistenceFactory.getInstance().findByPK(PrmContractC.class, uuid);
        }
        return null;
    }

    @Override
    public void checkContractUnique(PrmContractCDto contractCDto) {
        List lstParam = new ArrayList();
        lstParam.add(contractCDto.getContractStatus());
        lstParam.add(contractCDto.getContractNo());
        lstParam.add(contractCDto.getContractName());
        DAOMeta daoMeta = DAOHelper.getDAO("contract-c-dao", "validate_unique", lstParam);
        daoMeta.setNeedFilter(false);
        List<String> lstContract = PersistenceFactory.getInstance().findByNativeSQL(daoMeta).stream().map(t -> ((String) t.get("uuid"))).collect(Collectors.toList());
        if (ListUtil.isNotEmpty(lstContract)) {
            if (StringUtil.isNotEmpty(contractCDto.getUuid())) {
                lstContract = lstContract.stream().filter(t -> (!t.equals(contractCDto.getUuid()))).collect(Collectors.toList());
            }
            if (ListUtil.isNotEmpty(lstContract)) {
                throw new BizException(MessageHelper.getMessage(MessageKeyAttribute.MSG_PRM_CONTRACT_UNIQUE_WARN));
            }
        }

        List lstParam1 = new ArrayList();
        lstParam1.add(contractCDto.getContractNo());
        lstParam1.add(contractCDto.getContractName());
        daoMeta = DAOHelper.getDAO("contract-dao", "validate_unique", lstParam1);
        daoMeta.setNeedFilter(false);
        List lstContract2 = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(lstContract)) {
            throw new BizException(MessageHelper.getMessage(MessageKeyAttribute.MSG_PRM_CONTRACT_UNIQUE_WARN));
        }
    }

    @Override
    public void loadExtraDescField(PrmContractCDto contractCDto) {

        if (contractCDto == null) {
            return;
        }
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        String operateBusinessUuid = contractCDto.getOperateBusinessBidInfoId();
        OperateBusinessBidInfo operateBusinessBidInfo = operateBusinessManager.findByUuid(operateBusinessUuid);
        if (operateBusinessBidInfo != null) {
            contractCDto.setOperateBusinessBidInfoIdDesc(operateBusinessBidInfo.getProjectName());
        }

        if (StringUtil.isNotEmpty(contractCDto.getContractorOffice())) {
            contractCDto.setContractorOfficeDesc(OrgHelper.getOrgNameByCode(contractCDto.getContractorOffice()));
        }

        String projectManagerId = contractCDto.getProjectManager();
        String generalEngineerId = contractCDto.getGeneralEngineer();
        List<String> lstUserId = new ArrayList<>();
        if (StringUtil.isNotEmpty(projectManagerId)) {
            lstUserId.add(projectManagerId);
        }
        if (StringUtil.isNotEmpty(generalEngineerId) && !lstUserId.contains(generalEngineerId)) {
            lstUserId.add(generalEngineerId);
        }
        if (ListUtil.isNotEmpty(lstUserId)) {
            Map mapUserId = new HashMap();
            mapUserId.put(ScdpUserAttribute.USER_ID, lstUserId);
            List<ScdpUser> lstUser = pcm.findByAnyFields(ScdpUser.class, mapUserId, null);
            if (ListUtil.isNotEmpty(lstUser)) {
                for (ScdpUser user : lstUser) {
                    if (user.getUserId().equals(projectManagerId)) {
                        contractCDto.setProjectManagerDesc(user.getUserName());
                    }
                    if (user.getUserId().equals(generalEngineerId)) {
                        contractCDto.setGeneralEngineerDesc(user.getUserName());
                    }
                }
            }
        }

        String customerUuid = contractCDto.getCustomerId();
//		String designerId = contractDto.getDesignerId();
//		String managementId = contractDto.getManagementId();
        List<String> lstPartyId = new ArrayList<>();
        if (StringUtil.isNotEmpty(customerUuid)) {
            lstPartyId.add(customerUuid);
        }
//		if (StringUtil.isNotEmpty(designerId)) {
//			lstPartyId.add(designerId);
//		}
//		if (StringUtil.isNotEmpty(managementId)) {
//			lstPartyId.add(managementId);
//		}

        if (ListUtil.isNotEmpty(lstPartyId)) {
            Map mapPartyId = new HashMap();
            mapPartyId.put(CommonAttribute.UUID, lstPartyId);
            List<PrmCustomer> lstParty = pcm.findByAnyFields(PrmCustomer.class, mapPartyId, null);
            if (ListUtil.isNotEmpty(lstParty)) {
                for (PrmCustomer party : lstParty) {
                    if (party.getUuid().equals(customerUuid)) {
                        contractCDto.setCustomerIdDesc(party.getCustomerName());
                    }
//					if (party.getCustomerCode().equals(designerId)) {
//						contractDto.setDesignerIdDesc(party.getCustomerName());
//					}
//					if (party.getCustomerCode().equals(managementId)) {
//						contractDto.setManagementIdDesc(party.getCustomerName());
//					}
                }
            }
        }
        //填充变更前金额
        String contractId = contractCDto.getPrmContractId();
        if (StringUtil.isNotEmpty(contractId)) {
            if (contractCDto.getAuditTime() == null) {
                PrmContract prmContract = pcm.findByPK(PrmContract.class, contractId);
                if (prmContract != null) {
                    contractCDto.setContractLastMoney(prmContract.getContractNowMoney());
                }
            } else {
                Map mapCondition = new HashMap();
                mapCondition.put(PrmContractAttribute.PRM_CONTRACT_ID, contractId);
                List<PrmContractC> lstC = pcm.findByAnyFields(PrmContractC.class, mapCondition, null);
                if (ListUtil.isNotEmpty(lstC)) {
                    lstC = lstC.stream().filter(t -> BillStateAttribute.FAD_BILL_STATE_APPROVED.equals(t.getState()) && t.getAuditTime() != null && t.getAuditTime().compareTo(contractCDto.getAuditTime()) < 0).collect(Collectors.toList());
                    if (ListUtil.isNotEmpty(lstC)) {
                        PrmContractC c = lstC.stream().max((x, y) -> x.getAuditTime().compareTo(y.getAuditTime())).get();
                        if (c.getContractNowMoney() == null) {
                            contractCDto.setContractLastMoney(c.getContractSignMoney());
                        } else {
                            contractCDto.setContractLastMoney(c.getContractNowMoney());
                        }
                    }
                }
            }
        }
        String innerPurchaseReqId = contractCDto.getInnerPurchaseReqId();
        if (StringUtil.isNotEmpty(innerPurchaseReqId)) {
            PrmPurchaseReq prmPurchaseReq = pcm.findByPK(PrmPurchaseReq.class, innerPurchaseReqId);
            if (prmPurchaseReq != null && StringUtil.isNotEmpty(prmPurchaseReq.getPrmProjectMainId())) {
                PrmProjectMain prmProjectMain = pcm.findByPK(PrmProjectMain.class, prmPurchaseReq.getPrmProjectMainId());
                if (prmProjectMain != null) {
                    contractCDto.setInnerPurchaseReqIdDesc(prmProjectMain.getProjectName());
                }
            }
        }
    }

    @Override
    public void invokeApproveAction(String uuid) {
        PrmExamPeriodManager prmExamPeriodManager = BeanFactory.getBean("prmexamperiod-manager".toLowerCase());
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        PrmContractC mainChangeEntity = pcm.findByPK(PrmContractC.class, uuid);
        if (mainChangeEntity == null) {
            return;
        }
        Date auditDate = new Date();
        mainChangeEntity.setAuditTime(auditDate);

        Date examDate = prmExamPeriodManager.getExamDateFromAppointedDate(auditDate);
        mainChangeEntity.setExamDate(examDate);

        //若没有业主单位，审批通过，自动添加到客户管理，同时更新合同
        if (StringUtil.isNotEmpty(mainChangeEntity.getCustomerId())) {
            PrmCustomer customer = PersistenceFactory.getInstance().findByPK(PrmCustomer.class, mainChangeEntity.getCustomerId());
            if (customer == null) {
                PrmcustomerManager prmcustomerManager = BeanFactory.getBean("prmcustomer-manager");
                String customerName = mainChangeEntity.getCustomerId();
                String customerId = prmcustomerManager.createPrmcustomerByName(customerName);
                mainChangeEntity.setCustomerId(customerId);
                //send msg
                sendMsgForCustomer(mainChangeEntity.getContractName(), customerId, customerName);
            }
        }

        String mainUuid = mainChangeEntity.getPrmContractId();
        boolean isInsert = false;
        if (StringUtil.isEmpty(mainUuid)) {
            //do insert
            isInsert = true;
            PrmContract mainEntity = new PrmContract();
            BeanUtil.bean2Bean(mainChangeEntity, mainEntity);
            mainEntity.setUuid(null);
            mainEntity.setExamDate(null);
            mainEntity.setState(BillStateAttribute.CMD_BILL_STATE_APPROVED);
            mainEntity.setContractNowMoney(mainEntity.getContractSignMoney());
            pcm.insert(mainEntity);
            mainEntity = pcm.findByPK(PrmContract.class, mainEntity.getUuid());
            mainEntity.setCreateBy(mainChangeEntity.getCreateBy());
            mainEntity.setCreateTime(mainChangeEntity.getCreateTime());
            pcm.update(mainEntity);
            //write the main uuid to main change header
            mainUuid = mainEntity.getUuid();
            mainChangeEntity.setPrmContractId(mainUuid);
            pcm.update(mainChangeEntity);
        } else {
            //sync header
            PrmContract mainEntity = pcm.findByPK(PrmContract.class, mainUuid);
            String oldState = mainEntity.getState();
            String oldCreateBy = mainEntity.getCreateBy();
            Date oldCreateTime = mainEntity.getCreateTime();
            BeanUtil.bean2Bean(mainChangeEntity, mainEntity);
            mainEntity.setUuid(mainUuid);
            mainChangeEntity.setState(oldState);
            mainChangeEntity.setCreateBy(oldCreateBy);
            mainChangeEntity.setCreateTime(oldCreateTime);
            pcm.update(mainEntity);

//            mainChangeEntity.setState(oldState);
//            pcm.update(mainChangeEntity);
        }

        //sync file
        Map mapCondition = new HashMap();
        mapCondition.put(CdmFileRelationAttribute.DATA_ID, uuid);
        List<CdmFileRelation> lstCurrFile = pcm.findByAnyFields(CdmFileRelation.class, mapCondition, null);

        mapCondition.clear();
        mapCondition.put(CdmFileRelationAttribute.DATA_ID, mainUuid);
        List<CdmFileRelation> lstExistFile = pcm.findByAnyFields(CdmFileRelation.class, mapCondition, null);

        if (lstCurrFile == null) {
            lstCurrFile = new ArrayList<>();
        }
        if (lstExistFile == null) {
            lstExistFile = new ArrayList<>();
        }
        List<CdmFileRelation> lstInsert = new ArrayList<>();
        List<CdmFileRelation> lstDelete = new ArrayList<>();
        List<CdmFileRelation> lstUpdate = new ArrayList<>();
        for (CdmFileRelation curr : lstCurrFile) {
            boolean found = false;
            for (CdmFileRelation exist : lstExistFile) {
                if (curr.getFileId().equals(exist)) {
                    found = true;
                    exist.setRemark(curr.getRemark());
                    lstUpdate.add(exist);
                    break;
                }
            }
            if (!found) {
                CdmFileRelation newFile = new CdmFileRelation();
                BeanUtil.bean2Bean(curr, newFile);
                newFile.setDataId(mainUuid);
                lstInsert.add(newFile);
            }
        }

        for (CdmFileRelation exist : lstExistFile) {
            boolean found = false;
            for (CdmFileRelation curr : lstCurrFile) {
                if (curr.getFileId().equals(exist)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                lstDelete.add(exist);
            }
        }

        if (ListUtil.isNotEmpty(lstDelete)) {
            pcm.batchDelete(lstDelete);
        }
        if (ListUtil.isNotEmpty(lstUpdate)) {
            pcm.batchUpdate(lstUpdate);
        }
        if (ListUtil.isNotEmpty(lstInsert)) {
            pcm.batchInsert(lstInsert);
        }
    }

    //重大项目发送消息
    @Override
    public void sendMsg(String uuid, String projectName) {
        //发送消息
        String sql = "SELECT * FROM V_USER_ROLES T WHERE ROLE_NAME IN('供应链部主任','供应链部采购计划主管')";
        DAOMeta daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        List<Map<String, Object>> lstUserInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        List<String> userIdLst = new ArrayList<>();
        ReceiptsMeta receiptsMeta = new ReceiptsMeta();
        if (ListUtil.isNotEmpty(lstUserInfo)) {
            for (int i = 0; i < lstUserInfo.size(); i++) {
                String userId = (String) lstUserInfo.get(i).get("userId");
                userIdLst.add(userId);
            }
            receiptsMeta.setLstSendToUserId(userIdLst);
        }

        Map map = new HashMap<>();
        map.put("uuid", uuid);
        map.put("projectName", projectName);
        String msgType = ScdpMsgAttribute.MSG_TYPE_IMSG;
        String templateNo = "PRM_PROJECT_MAIN_MAJOR";
        MsgSendHelper.sendMsg(map, msgType, templateNo, receiptsMeta);

    }


    //重大项目发送消息
    @Override
    public void sendMsgForCustomer(String contractName, String customerId, String customerName) {
        //发送消息
        String sql = "SELECT * FROM V_USER_ROLES T WHERE ROLE_NAME IN('运管部经营主管')";
        DAOMeta daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        List<Map<String, Object>> lstUserInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        List<String> userIdLst = new ArrayList<>();
        ReceiptsMeta receiptsMeta = new ReceiptsMeta();
        if (ListUtil.isNotEmpty(lstUserInfo)) {
            for (int i = 0; i < lstUserInfo.size(); i++) {
                String userId = (String) lstUserInfo.get(i).get("userId");
                userIdLst.add(userId);
            }
            receiptsMeta.setLstSendToUserId(userIdLst);
        }

        Map map = new HashMap<>();
        map.put("contractName", contractName);
        map.put("customerId", customerId);
        map.put("customerName", customerName);
        String msgType = ScdpMsgAttribute.MSG_TYPE_IMSG;
        String templateNo = "PRM_CONTRACT_CUSTOMER_NEW";
        MsgSendHelper.sendMsg(map, msgType, templateNo, receiptsMeta);

    }

    @Override
    public List<PrmContractC> getContractCbyContractIdandContractStatus(String contractId, String contractStatus, List<String> states) {
        List<PrmContractC> r = getContractCbyContractIdandContractStatus(contractId, contractStatus);
        if (ListUtil.isNotEmpty(r)) {
            r = r.stream().filter(t -> states.contains(t.getState())).collect(Collectors.toList());
        }
        return r;
    }

    //根据合同ID和合同状态返回变更或新增记录
    @Override
    public List<PrmContractC> getContractCbyContractIdandContractStatus(String contractId, String contractStatus) {
        List<PrmContractC> out = null;
        if (StringUtil.isNotEmpty(contractId)) {
            Map<String, Object> mapConditions = new HashMap<String, Object>();
            mapConditions.put(PrmContractAttribute.PRM_CONTRACT_ID, contractId);
            mapConditions.put(PrmContractAttribute.CONTRACT_STATUS, contractStatus);
            out = PersistenceFactory.getInstance().findByAnyFields(PrmContractC.class, mapConditions, null);
        }
        return out;
    }

    @Override
    public Map<String, List<PrmContractC>> getContractCbyMainDto(PrmProjectMainDto cdto) {
        Map<String, List<PrmContractC>> rtn = new HashMap<String, List<PrmContractC>>();
        if (cdto != null && StringUtil.isNotEmpty(cdto.getUuid())) {
            String sql = " SELECT MAX(AUDIT_TIME) as AUDIT_TIME\n" +
                    "  FROM PRM_PROJECT_MAIN_C T\n" +
                    " WHERE T.STATE = '2'\n" +
                    "   AND AUDIT_TIME IS NOT NULL\n" +
                    "   AND T.PRM_PROJECT_MAIN_ID = ?";
            List lstParam = new ArrayList();
            lstParam.add(cdto.getUuid());
            DAOMeta daoMeta = new DAOMeta(sql, lstParam, false);
            daoMeta.setNeedFilter(false);
            List<Map<String, Object>> lastTime = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            if (lastTime.get(0).get("auditTime") != null) {
                Date lastAuditTime = (Date) lastTime.get(0).get("auditTime");
                sql = "select prm_contract_id from Prm_Contract_Detail t where t.prm_project_main_id = ?";
                lstParam.clear();
                lstParam.add(cdto.getUuid());
                daoMeta = new DAOMeta(sql, lstParam, false);
                daoMeta.setNeedFilter(false);
                List<Object> prmContractIdLst = PersistenceFactory.getInstance().findByNativeSQL(daoMeta)
                        .stream().map(x -> x.get("prmContractId"))
                        .collect(Collectors.toList());
                if (ListUtil.isNotEmpty(prmContractIdLst)) {
                    QueryCondition condition1 = new QueryCondition();
                    condition1.setField(PrmContractAttribute.PRM_CONTRACT_ID);
                    condition1.setOperator("in");
                    condition1.setValueList(prmContractIdLst);
                    List<QueryCondition> lstCondition = new ArrayList<QueryCondition>();
                    lstCondition.add(condition1);
                    List<PrmContractC> list = PersistenceFactory.getInstance().findByAnyFields(PrmContractC.class, lstCondition, null);
                    rtn = list.stream().filter(t -> t.getAuditTime() != null && t.getAuditTime().compareTo(lastAuditTime) > 0).collect(Collectors.groupingBy(PrmContractC::getPrmContractId));
                }
            }
        }
        return rtn;
    }

    @Override
    public Map validatePrmContractCBeforeWorkFlowCommit(String uuid) {
        Map rtn = new HashMap();
        PrmContractC prmContractC = PersistenceFactory.getInstance().findByPK(PrmContractC.class, uuid);
        String taxType = prmContractC.getPrmCodeType();
        if (StringUtil.isEmpty(taxType)) {
            rtn.put(CommonAttribute.SUCCESS, false);
            rtn.put(CommonAttribute.MESSAGE, "提交前，请补充代号类型！");
        } else {
            rtn.put(CommonAttribute.SUCCESS, true);
        }
        return rtn;
    }
}
