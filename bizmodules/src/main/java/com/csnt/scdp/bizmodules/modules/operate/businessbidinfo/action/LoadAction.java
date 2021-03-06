package com.csnt.scdp.bizmodules.modules.operate.businessbidinfo.action;

import com.csnt.scdp.bizmodules.entity.fad.FadCashReq;
import com.csnt.scdp.bizmodules.entity.prm.PrmContractC;
import com.csnt.scdp.bizmodules.entity.prm.PrmCustomer;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmAbstractContractAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmContractAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmCustomerAttribute;
import com.csnt.scdp.bizmodules.modules.common.action.ErpLoadAction;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpUserHelper;
import com.csnt.scdp.bizmodules.modules.fad.cashreq.services.intf.CashreqManager;
import com.csnt.scdp.bizmodules.modules.operate.businessbidinfo.dto.OperateBusinessBidInfoDto;
import com.csnt.scdp.bizmodules.modules.operate.businessbidinfo.services.intf.BusinessbidinfoManager;
import com.csnt.scdp.framework.bo.QueryCondition;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.UserHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description:  LoadAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-28 18:18:49
 */

@Scope("singleton")
@Controller("businessbidinfo-load")
@Transactional
public class LoadAction extends ErpLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

    @Resource(name = "businessbidinfo-manager")
    private BusinessbidinfoManager businessbidinfoManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        //Do After
        Map dtoMap = (Map) out.get("operateBusinessBidInfoDto");
        return out;
    }

    @Override
    protected void afterAction(BasePojo dtoObj) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        OperateBusinessBidInfoDto operateBusinessBidInfo = (OperateBusinessBidInfoDto) dtoObj;
        if (operateBusinessBidInfo == null) {
            return;
        }

        //设置客户
        String customerId = operateBusinessBidInfo.getCustomerId();
        if (StringUtil.isNotEmpty(customerId)) {
            PrmCustomer customer = pcm.findByPK(PrmCustomer.class, customerId);
            if (customer != null) {
                operateBusinessBidInfo.setCustomerIdDesc(customer.getCustomerName());
            }
        }

        //设置部门
        String officeCode = operateBusinessBidInfo.getContractorOffice();
        if (StringUtil.isNotEmpty(officeCode)) {
            operateBusinessBidInfo.setContractorOfficeDesc(OrgHelper.getOrgNameByCode(officeCode));
        }
        //设置经办人
        String operateBy = operateBusinessBidInfo.getOperateBy();
        if (StringUtil.isNotEmpty(operateBy)) {
            operateBusinessBidInfo.setOperateByName(ErpUserHelper.getUserNameById(operateBy));
        }

        //不能修改>不能改金额>不能删除
        //若该投标信息是否可以修改删除
        operateBusinessBidInfo.setCanBeDeleted(1);
        operateBusinessBidInfo.setCanBeModified(1);
        operateBusinessBidInfo.setCanMoneyBeModified(1);
        List<PrmContractC> prmContractcs = businessbidinfoManager.findContractbyBidInfoId(operateBusinessBidInfo.getUuid());
        if (ListUtil.isNotEmpty(prmContractcs)) {
            operateBusinessBidInfo.setCanBeDeleted(0);
            String[] validStates = {"1", "2", "9"};
            Long validCount = prmContractcs.stream().filter(t ->
                            Arrays.binarySearch(validStates, t.getState()) >= 0
            ).count();
            if (validCount > 0) {
                operateBusinessBidInfo.setCanBeModified(0);
            } else {
                operateBusinessBidInfo.setCanBeModified(1);
            }
        } else {
            operateBusinessBidInfo.setCanBeDeleted(1);
            operateBusinessBidInfo.setCanBeModified(1);
        }
        //若投标信息能够修改，判断是否和保证金情况向关联
        if (operateBusinessBidInfo.getCanBeModified() == 1) {
            CashreqManager cashreqManager = BeanFactory.getBean("cashreq-manager");
            List<FadCashReq> cash = cashreqManager.getFadCashReqByBidInfoUuid(operateBusinessBidInfo.getUuid());
            if (ListUtil.isNotEmpty(cash)) {
                operateBusinessBidInfo.setCanMoneyBeModified(0);
                operateBusinessBidInfo.setCanBeDeleted(0);
            }
        }
    }
}
