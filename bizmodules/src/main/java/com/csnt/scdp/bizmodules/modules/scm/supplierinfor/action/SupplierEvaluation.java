package com.csnt.scdp.bizmodules.modules.scm.supplierinfor.action;

import com.csnt.scdp.bizmodules.modules.scm.scmsupplierlimitchange.services.intf.ScmsupplierlimitchangeManager;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.dto.ScmSupplierEvaluationDto;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 15:32:42
 */

@Scope("singleton")
@Controller("supplierinfor-supplierevaluation")
@Transactional
public class SupplierEvaluation implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(SupplierEvaluation.class);

    @Resource(name = "scmsupplierlimitchange-manager")
    private ScmsupplierlimitchangeManager scmsupplierlimitchangeManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map rsMap = new HashMap();
        String uuid = (String) inMap.get("uuid");
        ScmSupplierEvaluationDto scmSupplierEvaluationDto = new ScmSupplierEvaluationDto();
        scmSupplierEvaluationDto.setScmSupplierId(uuid);
        if (StringUtil.isNotEmpty(inMap.get("price"))) {
            Integer price = Integer.valueOf("0" + inMap.get("price"));
            scmSupplierEvaluationDto.setPrice(price);
        }
        if (StringUtil.isNotEmpty(inMap.get("business"))) {
            Integer business = Integer.valueOf("0" + inMap.get("business"));
            scmSupplierEvaluationDto.setBusiness(business);
        }
        if (StringUtil.isNotEmpty(inMap.get("personQuality"))) {
            Integer personQuality = Integer.valueOf("0" + inMap.get("personQuality"));
            scmSupplierEvaluationDto.setPersonQuality(personQuality);
        }
        if (StringUtil.isNotEmpty(inMap.get("organizingCapability"))) {
            Integer organizingCapability = Integer.valueOf("0" + inMap.get("organizingCapability"));
            scmSupplierEvaluationDto.setOrganizingCapability(organizingCapability);
        }
        if (StringUtil.isNotEmpty(inMap.get("compliance"))) {
            Integer compliance = Integer.valueOf("0" + inMap.get("compliance"));
            scmSupplierEvaluationDto.setCompliance(compliance);
        }
        if (StringUtil.isNotEmpty(inMap.get("securityManagement"))) {
            Integer securityManagement = Integer.valueOf("0" + inMap.get("securityManagement"));
            scmSupplierEvaluationDto.setSecurityManagement(securityManagement);
        }
        if (StringUtil.isNotEmpty(inMap.get("finalEstimate"))) {
            Integer finalEstimate = Integer.valueOf("0" + inMap.get("finalEstimate"));
            scmSupplierEvaluationDto.setFinalEstimate(finalEstimate);
        }
        if (StringUtil.isNotEmpty(inMap.get("arrivalTime"))) {
            Integer arrivalTime = Integer.valueOf("0" + inMap.get("arrivalTime"));
            scmSupplierEvaluationDto.setArrivalTime(arrivalTime);
        }
        if (StringUtil.isNotEmpty(inMap.get("equipmentQuality"))) {
            Integer equipmentQuality = Integer.valueOf("0" + inMap.get("equipmentQuality"));
            scmSupplierEvaluationDto.setEquipmentQuality(equipmentQuality);
        }
        if (StringUtil.isNotEmpty(inMap.get("service"))) {
            Integer service = Integer.valueOf("0" + inMap.get("service"));
            scmSupplierEvaluationDto.setService(service);
        }
        if (StringUtil.isNotEmpty(inMap.get("comprehensive"))) {
            Integer comprehensive = Integer.valueOf("0" + inMap.get("comprehensive"));
            scmSupplierEvaluationDto.setComprehensive(comprehensive);
        }
        if (StringUtil.isNotEmpty(inMap.get("remark"))) {
            String remark = (String) inMap.get("remark");
            scmSupplierEvaluationDto.setRemark(remark);
        }
        //M3_C17_F1_供方评价
        if (StringUtil.isNotEmpty(inMap.get("evaluateFrom"))) {
            String evaluateFrom = (String) inMap.get("evaluateFrom");
            scmSupplierEvaluationDto.setEvaluateFrom(evaluateFrom);
        }
        if (StringUtil.isNotEmpty(inMap.get("scmContractId"))) {
            String scmContractId = (String) inMap.get("scmContractId");
            scmSupplierEvaluationDto.setScmContractId(scmContractId);
        }
        scmSupplierEvaluationDto.setEditflag("+");
        DtoHelper.CascadeSave(scmSupplierEvaluationDto);
        return null;
    }
}
