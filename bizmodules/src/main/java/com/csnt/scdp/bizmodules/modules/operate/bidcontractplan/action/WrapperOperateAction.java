package com.csnt.scdp.bizmodules.modules.operate.bidcontractplan.action;

import com.csnt.scdp.bizmodules.entityattributes.operate.OperateBusinessBidInfoAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmContractAttribute;
import com.csnt.scdp.bizmodules.modules.operate.bidcontractplan.services.intf.BidcontractplanManager;
import com.csnt.scdp.bizmodules.modules.operate.businessbidinfo.dto.OperateBusinessBidInfoDto;
import com.csnt.scdp.bizmodules.modules.operate.businessbidinfo.services.intf.BusinessbidinfoManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:  QueryAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 09:57:20
 */

@Scope("singleton")
@Controller("prm-contract-wrapper-operate-info")
@Transactional
public class WrapperOperateAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(WrapperOperateAction.class);

    @Resource(name = "businessbidinfo-manager")
    private BusinessbidinfoManager businessbidinfoManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = new HashMap();
        String operateUuid = (String) inMap.get(CommonAttribute.UUID);
        if (StringUtil.isNotEmpty(operateUuid)) {
            OperateBusinessBidInfoDto operateInfoDto = (OperateBusinessBidInfoDto) DtoHelper.findDtoByPK(OperateBusinessBidInfoDto.class, operateUuid);

            businessbidinfoManager.loadExtraDescField(operateInfoDto);

            if (operateInfoDto != null) {
                out.put(OperateBusinessBidInfoAttribute.PROJECT_TYPE, operateInfoDto.getProjectType());
                out.put(PrmContractAttribute.PROJECT_NAME, operateInfoDto.getProjectName());
                out.put(PrmContractAttribute.CONTRACTOR_OFFICE, operateInfoDto.getContractorOffice());
                out.put(PrmContractAttribute.CONTRACTOR_OFFICE_DESC, operateInfoDto.getContractorOfficeDesc());
                out.put(PrmContractAttribute.CUSTOMER_ID, operateInfoDto.getCustomerId());
                out.put(PrmContractAttribute.CUSTOMER_ID_DESC, operateInfoDto.getCustomerIdDesc());
                if ("2".equals(operateInfoDto.getProjectType())) {
                    out.put(PrmContractAttribute.CONTRACT_NAME, operateInfoDto.getContractName());
                    out.put(PrmContractAttribute.CONTRACT_NO, operateInfoDto.getContractNo());
                    out.put(PrmContractAttribute.PROJECT_MANAGER, operateInfoDto.getProjectManager());
                    out.put(PrmContractAttribute.PROJECT_MANAGER_DESC, operateInfoDto.getProjectManagerDesc());
                    out.put(PrmContractAttribute.GENERAL_ENGINEER, operateInfoDto.getGeneralEngineer());
                    out.put(PrmContractAttribute.GENERAL_ENGINEER_DESC, operateInfoDto.getGeneralEngineerDesc());
                    out.put(PrmContractAttribute.DESIGNER_ID, operateInfoDto.getDesignerId());
                    out.put(PrmContractAttribute.DESIGNER_ID_DESC, operateInfoDto.getDesignerIdDesc());
                    out.put(PrmContractAttribute.MANAGEMENT_ID, operateInfoDto.getManagementId());
                    out.put(PrmContractAttribute.MANAGEMENT_ID_DESC, operateInfoDto.getManagementIdDesc());
                    out.put(PrmContractAttribute.CONTRACT_SIGN_MONEY, operateInfoDto.getContractSignMoney());
                    out.put(PrmContractAttribute.SUCCESS_BID_DATE, operateInfoDto.getSuccessBidDate());
                    out.put(PrmContractAttribute.CONTRACT_SIGN_DATE, operateInfoDto.getContractSignDate());
                    out.put(PrmContractAttribute.CONTRACT_START_DATE, operateInfoDto.getContractStartDate());
                    out.put(PrmContractAttribute.CONTRACT_END_DATE, operateInfoDto.getContractEndDate());
                    out.put(PrmContractAttribute.CONTRACT_DURATION, operateInfoDto.getContractDuration());
                    out.put(PrmContractAttribute.DEFECTS_LIABILITY_PERIODS, operateInfoDto.getDefectsLiabilityPeriods());
                    out.put(PrmContractAttribute.PREOPERATION, operateInfoDto.getPreoperation());
                    out.put(PrmContractAttribute.PROJECT_SCALE, operateInfoDto.getProjectScale());
                    out.put(PrmContractAttribute.COUNTRY_CODE, operateInfoDto.getCountryCode());
                    out.put(PrmContractAttribute.BUILD_REGION, operateInfoDto.getBuildRegion());
                    out.put(PrmContractAttribute.PROJECT_SOURCE_TYPE, operateInfoDto.getProjectSourceType());
                    out.put(PrmContractAttribute.TAX_REGION, operateInfoDto.getTaxRegion());
                    out.put(PrmContractAttribute.TAX_TYPE, operateInfoDto.getTaxType());

                    out.put(PrmContractAttribute.CDM_FILE_RELATION_DTO, operateInfoDto.getCdmFileRelationDto());
                }
            }
        }
        //Do After
        return out;
    }
}
