package com.csnt.scdp.bizmodules.modules.prm.contract.services.impl;

import com.csnt.scdp.bizmodules.attributes.MessageKeyAttribute;
import com.csnt.scdp.bizmodules.entity.operate.OperateBusinessBidInfo;
import com.csnt.scdp.bizmodules.entity.prm.PrmContract;
import com.csnt.scdp.bizmodules.entity.prm.PrmCustomer;
import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entity.prm.PrmPurchaseReq;
import com.csnt.scdp.bizmodules.modules.operate.businessbidinfo.services.intf.BusinessbidinfoManager;
import com.csnt.scdp.bizmodules.modules.prm.contract.dto.PrmContractDto;
import com.csnt.scdp.bizmodules.modules.prm.contract.services.intf.ContractManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.helper.MessageHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@Service("contract-manager")
public class ContractManagerImpl implements ContractManager {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ContractManagerImpl.class);

    @Resource(name = "businessbidinfo-manager")
    private BusinessbidinfoManager operateBusinessManager;

    @Override
    public PrmContract findContractHeaderOnly(String uuid) {
        if (StringUtil.isNotEmpty(uuid)) {
            return PersistenceFactory.getInstance().findByPK(PrmContract.class, uuid);
        }
        return null;
    }

    @Override
    public void checkContractUnique(String contractNo, String contractName) {
        List lstParam = new ArrayList();
        lstParam.add(contractNo);
        lstParam.add(contractName);
        DAOMeta daoMeta = DAOHelper.getDAO("contract-dao", "validate_unique", lstParam);
        daoMeta.setNeedFilter(false);
        List lstContract = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(lstContract)) {
            throw new BizException(MessageHelper.getMessage(MessageKeyAttribute.MSG_PRM_CONTRACT_UNIQUE_WARN));
        }
    }

    @Override
    public void loadExtraDescField(PrmContractDto contractDto) {

        if (contractDto == null) {
            return;
        }
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        String operateBusinessUuid = contractDto.getOperateBusinessBidInfoId();
        OperateBusinessBidInfo operateBusinessBidInfo = operateBusinessManager.findByUuid(operateBusinessUuid);
        if (operateBusinessBidInfo != null) {
            contractDto.setOperateBusinessBidInfoIdDesc(operateBusinessBidInfo.getProjectName());
        }

        if (StringUtil.isNotEmpty(contractDto.getContractorOffice())) {
            contractDto.setContractorOfficeDesc(OrgHelper.getOrgNameByCode(contractDto.getContractorOffice()));
        }

        String projectManagerId = contractDto.getProjectManager();
        String generalEngineerId = contractDto.getGeneralEngineer();
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
                        contractDto.setProjectManagerDesc(user.getUserName());
                    }
                    if (user.getUserId().equals(generalEngineerId)) {
                        contractDto.setGeneralEngineerDesc(user.getUserName());
                    }
                }
            }
        }

        String customerUuid = contractDto.getCustomerId();
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
                        contractDto.setCustomerIdDesc(party.getCustomerName());
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

        String innerPurchaseReqId = contractDto.getInnerPurchaseReqId();
        if (StringUtil.isNotEmpty(innerPurchaseReqId)) {
            PrmPurchaseReq prmPurchaseReq = pcm.findByPK(PrmPurchaseReq.class, innerPurchaseReqId);
            if (prmPurchaseReq != null && StringUtil.isNotEmpty(prmPurchaseReq.getPrmProjectMainId())) {
                PrmProjectMain prmProjectMain = pcm.findByPK(PrmProjectMain.class, prmPurchaseReq.getPrmProjectMainId());
                if (prmProjectMain != null) {
                    contractDto.setInnerPurchaseReqIdDesc(prmProjectMain.getProjectName());
                }
            }
        }

    }
}
