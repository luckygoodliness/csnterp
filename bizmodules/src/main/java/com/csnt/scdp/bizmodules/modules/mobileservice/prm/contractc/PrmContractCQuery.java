package com.csnt.scdp.bizmodules.modules.mobileservice.prm.contractc;

import com.csnt.scdp.bizmodules.attributes.ErpMobileDtoAttribute;
import com.csnt.scdp.bizmodules.attributes.ErpMobileModulePathAttribute;
import com.csnt.scdp.bizmodules.attributes.ErpMobileTerminalAttribute;
import com.csnt.scdp.bizmodules.entity.prm.PrmContractC;
import com.csnt.scdp.bizmodules.entity.prm.PrmCustomer;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCashReqAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmContractCAttribute;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpI18NHelper;
import com.csnt.scdp.bizmodules.modules.mobileservice.ERPMobileTerminalBaseVariableCollectionAction;
import com.csnt.scdp.bizmodules.modules.mobileservice.intf.MobileTerminalQuery;
import com.csnt.scdp.bizmodules.modules.prm.contractc.dto.PrmContractCDto;
import com.csnt.scdp.bizmodules.modules.prm.contractc.services.intf.ContractCManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.util.DateUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MathUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.helper.FMCodeHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by lijx on 2016/9/9.
 */
@Scope("singleton")
@Controller("mobile-terminal-query-prm_contract_management")
@Transactional
public class PrmContractCQuery extends ERPMobileTerminalBaseVariableCollectionAction {

    @Resource(name = "mobile-terminal-query")
    private MobileTerminalQuery mobileTerminalQuery;
    @Resource(name = "contract-c-manager")
    private ContractCManager contractManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map out = new LinkedHashMap<>();
        Map itemMap = new LinkedHashMap<>();
        Map extraGridMap = new LinkedHashMap<>();
        inMap.put("dto", ErpMobileDtoAttribute.definitionKey2Dto.get("Prm_Contract_Management_Dto"));
        Map basePojoMap = super.perform(inMap);
        String workFlowDefinitionKey = (String) inMap.get(WorkFlowAttribute.WORKFLOW_DEFINITION_KEY);
        BasePojo basePojo = (BasePojo) basePojoMap.get(ErpMobileTerminalAttribute.BASE_POJO);
        PrmContractCDto prmContractCDto = (PrmContractCDto) basePojo;

        contractManager.loadExtraDescField(prmContractCDto);

        String prmCodeType = "PRM_CODE_TYPE";
        if(StringUtil.isNotEmpty(prmContractCDto.getTaxType()))
            prmCodeType = prmCodeType + "_" + prmContractCDto.getTaxType();
        else
            prmCodeType = prmCodeType + "_PP";

        //主表
        Map hostMap = new LinkedHashMap<>();
        hostMap.put(ErpI18NHelper.englishToChinese(PrmContractCAttribute.STATE, ErpMobileModulePathAttribute.PRM_CONTRACT_MANAGEMENT_MODULEPATH),  FMCodeHelper.getDescByCode(prmContractCDto.getState(), "CDM_BILL_STATE"));//流程状态
        hostMap.put(ErpI18NHelper.englishToChinese(PrmContractCAttribute.CONTRACT_NAME, ErpMobileModulePathAttribute.PRM_CONTRACT_MANAGEMENT_MODULEPATH), prmContractCDto.getContractName());//合同名称
        hostMap.put(ErpI18NHelper.englishToChinese(PrmContractCAttribute.CUSTOMER_ID, ErpMobileModulePathAttribute.PRM_CONTRACT_MANAGEMENT_MODULEPATH), prmContractCDto.getCustomerIdDesc());//业主单位
        hostMap.put(ErpI18NHelper.englishToChinese(PrmContractCAttribute.PRM_CODE_TYPE, ErpMobileModulePathAttribute.PRM_CONTRACT_MANAGEMENT_MODULEPATH),  FMCodeHelper.getDescByCode(prmContractCDto.getPrmCodeType(), prmCodeType));//代号类型
        hostMap.put(ErpI18NHelper.englishToChinese(PrmContractCAttribute.PROFIT_RATE, ErpMobileModulePathAttribute.PRM_CONTRACT_MANAGEMENT_MODULEPATH), prmContractCDto.getProfitRate());//预计利润率
        hostMap.put(ErpI18NHelper.englishToChinese(PrmContractCAttribute.CONTRACTOR_OFFICE, ErpMobileModulePathAttribute.PRM_CONTRACT_MANAGEMENT_MODULEPATH), prmContractCDto.getContractorOfficeDesc());//承担部门
        hostMap.put(ErpI18NHelper.englishToChinese(PrmContractCAttribute.CONTRACT_SIGN_MONEY, ErpMobileModulePathAttribute.PRM_CONTRACT_MANAGEMENT_MODULEPATH), MathUtil.formatDecimalToString(prmContractCDto.getContractSignMoney(), "##,##0.00"));//合同签约额
        hostMap.put(ErpI18NHelper.englishToChinese(PrmContractCAttribute.CONTRACT_SIGN_DATE, ErpMobileModulePathAttribute.PRM_CONTRACT_MANAGEMENT_MODULEPATH), DateUtil.formatDate(prmContractCDto.getContractSignDate(), "yyyy-MM-dd"));//合同签订日期
        hostMap.put(ErpI18NHelper.englishToChinese(PrmContractCAttribute.PROJECT_SOURCE_TYPE, ErpMobileModulePathAttribute.PRM_CONTRACT_MANAGEMENT_MODULEPATH), FMCodeHelper.getDescByCode(prmContractCDto.getProjectSourceType(), "CDM_PROJECT_SOURCE_TYPE"));//项目来源
        hostMap.put(ErpI18NHelper.englishToChinese(PrmContractCAttribute.CONTRACT_DURATION, ErpMobileModulePathAttribute.PRM_CONTRACT_MANAGEMENT_MODULEPATH), prmContractCDto.getContractDuration());//合同工期

        itemMap.putAll(hostMap);
        //子表
        extraGridMap.put(ErpMobileTerminalAttribute.EXTRA_GRID, new HashMap());
        itemMap.putAll(extraGridMap);

        out.put(ErpMobileTerminalAttribute.ITEM, itemMap);

        //样例，部门code，如果工作流不是自定义的code，则不需要。。。
        out.put(ErpMobileTerminalAttribute.PROCESS_DEPT_CODE, prmContractCDto.getContractorOffice());

        return out;
    }
}
