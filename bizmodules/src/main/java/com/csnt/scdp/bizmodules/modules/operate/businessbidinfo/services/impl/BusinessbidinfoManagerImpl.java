package com.csnt.scdp.bizmodules.modules.operate.businessbidinfo.services.impl;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.entity.fad.FadCashReq;
import com.csnt.scdp.bizmodules.entity.operate.OperateBusinessBidInfo;
import com.csnt.scdp.bizmodules.entity.prm.PrmContractC;
import com.csnt.scdp.bizmodules.entity.prm.PrmCustomer;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCashReqAttribute;
import com.csnt.scdp.bizmodules.entityattributes.nonprm.NonProjectExtraItemAttribute;
import com.csnt.scdp.bizmodules.entityattributes.operate.OperateBusinessBidInfoAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmAbstractContractAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmContractAttribute;
import com.csnt.scdp.bizmodules.modules.operate.businessbidinfo.dto.OperateBusinessBidInfoDto;
import com.csnt.scdp.bizmodules.modules.operate.businessbidinfo.services.intf.BusinessbidinfoManager;
import com.csnt.scdp.framework.bo.QueryCondition;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.dto.BasePojo;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:  BusinessbidinfoManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-28 18:18:49
 */

@Scope("singleton")
@Service("businessbidinfo-manager")
public class BusinessbidinfoManagerImpl implements BusinessbidinfoManager {

    @Override
    public OperateBusinessBidInfo findByUuid(String uuid) {
        if (StringUtil.isEmpty(uuid)) {
            return null;
        }
        return PersistenceFactory.getInstance().findByPK(OperateBusinessBidInfo.class, uuid);
    }

    @Override
    public List<FadCashReq> getObjByProjectId(String projectId) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(FadCashReqAttribute.PROJECT_ID, projectId);
        List<FadCashReq> lstObj = PersistenceFactory.getInstance().
                findByAnyFields(FadCashReq.class, mapConditions, null);
        return lstObj;
    }

    @Override
    public void sendMessage(BasePojo bp) {
        String sql = "SELECT * FROM V_USER_ROLES T WHERE ROLE_NAME IN('运管部经营主管','运管部主任')";
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
        String msgType = ScdpMsgAttribute.MSG_TYPE_IMSG;
        String templateNo = "OPERATE_BUSINESS_BID_INFO";
        MsgSendHelper.sendMsg(BeanUtil.bean2Map(bp), msgType, templateNo, receiptsMeta);
    }

    @Override
    public List<PrmContractC> findContractbyBidInfoId(String infoId) {
        Map contractC = new HashMap();
        contractC.put(PrmAbstractContractAttribute.OPERATE_BUSINESS_BID_INFO_ID, infoId);
        return PersistenceFactory.getInstance().findByAnyFields(PrmContractC.class, contractC, null);
    }

    @Override
    public List<PrmContractC> findContractbyPrmContractId(String contractId) {
        Map contractC = new HashMap();
        contractC.put(PrmContractAttribute.PRM_CONTRACT_ID, contractId);
        return PersistenceFactory.getInstance().findByAnyFields(PrmContractC.class, contractC, null);
    }

    @Override
    public List<OperateBusinessBidInfo> getUnhandledInfoes() {
        QueryCondition condition1 = new QueryCondition();
        List states = new ArrayList<String>();
        states.add(BillStateAttribute.FAD_BILL_STATE_NEW);
        states.add(BillStateAttribute.CMD_BILL_STATE_REJECTED);
        condition1.setField(OperateBusinessBidInfoAttribute.STATE);
        condition1.setOperator("in");
        condition1.setValueList(states);
        List<QueryCondition> lstCondition = new ArrayList<QueryCondition>();
        lstCondition.add(condition1);
        List<OperateBusinessBidInfo> info = PersistenceFactory.getInstance().findByAnyFields(OperateBusinessBidInfo.class, lstCondition, null);
        if (ListUtil.isNotEmpty(info)) {
            info = info.stream().filter(t -> t.getBod() != null && StringUtil.isEmpty(t.getBidResult())).collect(Collectors.toList());
        }
        return info;
    }

    @Override
    public void loadExtraDescField(OperateBusinessBidInfoDto dto) {
        //设置客户
        String customerId = dto.getCustomerId();
        if (StringUtil.isNotEmpty(customerId)) {
            PrmCustomer customer = PersistenceFactory.getInstance().findByPK(PrmCustomer.class, customerId);
            if (customer != null) {
                dto.setCustomerIdDesc(customer.getCustomerName());
            }
        }

        //设置部门
        String officeCode = dto.getContractorOffice();
        if (StringUtil.isNotEmpty(officeCode)) {
            dto.setContractorOfficeDesc(OrgHelper.getOrgNameByCode(officeCode));
        }
    }

    @Override
    public void forwardChangePrmContractCFromBidInfoDto(OperateBusinessBidInfoDto info) {
        String InfoUuid = info.getUuid();
        List<PrmContractC> prmContractCList = findContractbyBidInfoId(InfoUuid);
        if (ListUtil.isNotEmpty(prmContractCList)) {
            prmContractCList.stream().filter(t -> t.getState().equals(BillStateAttribute.FAD_BILL_STATE_NEW)).forEach(t -> {
                //对
                if (t.getProjectSourceType().equals("2")) {
                    t.setProjectName(info.getProjectName());
                } else {
                    t.setContractName(info.getContractName());
                    t.setContractSignDate(info.getContractSignDate());
                    t.setContractSignMoney(info.getContractSignMoney());
                    t.setContractDuration(info.getContractDuration());
                    t.setProjectScale(info.getProjectScale());
                }
                t.setBuildRegion(info.getBuildRegion());
                t.setCustomerId(info.getCustomerId());
                t.setDesignerId(info.getDesignerId());
                t.setManagementId(info.getManagementId());
                t.setContractorOffice(info.getContractorOffice());
                PersistenceFactory.getInstance().update(t);
            });
        }
    }

    @Override
    public boolean validateUniqueField(OperateBusinessBidInfoDto dto, Map<String, Object> checkFields, boolean unchangePass) {
        List<OperateBusinessBidInfo> existObj = PersistenceFactory.getInstance().findByAnyFields(OperateBusinessBidInfo.class, checkFields, null);
        if (unchangePass) {
            if (existObj.size() > 1) {
                return existObj.stream().anyMatch(t -> t.getUuid().equals(dto.getUuid()));
            } else {
                return existObj.size() < 2;
            }
        } else {
            return existObj.size() < 2;
        }
    }
}