package com.csnt.scdp.bizmodules.modules.mobileservice.prm.purchaseplan;

import com.csnt.scdp.bizmodules.attributes.*;
import com.csnt.scdp.bizmodules.entity.prm.*;
import com.csnt.scdp.bizmodules.entity.scm.ScmMaterialClass;
import com.csnt.scdp.bizmodules.entityattributes.prm.*;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmMaterialClassAttribute;
import com.csnt.scdp.bizmodules.modules.cdm.cdmfile.dto.CdmFileRelationDto;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpI18NHelper;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpUserHelper;
import com.csnt.scdp.bizmodules.modules.mobileservice.ERPMobileTerminalBaseVariableCollectionAction;
import com.csnt.scdp.bizmodules.modules.mobileservice.intf.MobileTerminalQuery;
import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.dto.PrmPurchasePackageDto;
import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.dto.PrmPurchasePlanDto;
import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.services.intf.PurchaseplanManager;
import com.csnt.scdp.bizmodules.modules.scm.purchasereq.services.intf.PurchasereqManager;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MathUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.helper.FMCodeHelper;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by lijx on 2016/9/13.
 */
@Scope("singleton")
@Controller("mobile-terminal-query-prm_purchase_plan")
@Transactional
public class PurchasePlanQuery  extends ERPMobileTerminalBaseVariableCollectionAction {
    @Resource(name = "mobile-terminal-query")
    private MobileTerminalQuery mobileTerminalQuery;

    @Resource(name = "purchaseplan-manager")
    private PurchaseplanManager purchaseplanManager;

    @Resource(name = "purchasereq-manager")
    private PurchasereqManager purchasereqManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map out = new LinkedHashMap<>();
        Map itemMap = new LinkedHashMap<>();
        Map extraGridMap = new LinkedHashMap<>();
        inMap.put("dto", ErpMobileDtoAttribute.definitionKey2Dto.get("Prm_Purchase_Plan_Dto"));
        Map basePojoMap = super.perform(inMap);
        String workFlowDefinitionKey = (String) inMap.get(WorkFlowAttribute.WORKFLOW_DEFINITION_KEY);
        BasePojo basePojo = (BasePojo) basePojoMap.get(ErpMobileTerminalAttribute.BASE_POJO);
        PrmPurchasePlanDto prmPurchasePlanDto = (PrmPurchasePlanDto) basePojo;

        loadExtraDescField(prmPurchasePlanDto);

        //主表
        Map hostMap = new LinkedHashMap<>();
        hostMap.put(ErpI18NHelper.englishToChinese(PrmPurchasePlanAttribute.PROJECT_NAME,
                ErpMobileModulePathAttribute.PRM_PURCHASE_PLAN_MODULEPATH), prmPurchasePlanDto.getProjectName());//项目名称
        hostMap.put(ErpI18NHelper.englishToChinese(PrmProjectMainAttribute.DEPARTMENT,
                ErpMobileModulePathAttribute.PRM_PURCHASE_PLAN_MODULEPATH), prmPurchasePlanDto.getContractorOfficeDesc());//承担部门
        hostMap.put(ErpI18NHelper.englishToChinese(PrmPurchasePlanAttribute.MANAGER,
                ErpMobileModulePathAttribute.PRM_PURCHASE_PLAN_MODULEPATH), prmPurchasePlanDto.getManager());//项目负责人
        hostMap.put(ErpI18NHelper.englishToChinese(PrmProjectMainAttribute.PROJECT_CODE,
                ErpMobileModulePathAttribute.PRM_PURCHASE_PLAN_MODULEPATH), prmPurchasePlanDto.getProjectCode());//项目代号
        hostMap.put(ErpI18NHelper.englishToChinese(PrmPurchasePlanAttribute.CUSTOMER,
                ErpMobileModulePathAttribute.PRM_PURCHASE_PLAN_MODULEPATH), prmPurchasePlanDto.getCustomer());//业主单位
        hostMap.put(ErpI18NHelper.englishToChinese(PrmProjectMainAttribute.PROJECT_MONEY,
                ErpMobileModulePathAttribute.PRM_PURCHASE_PLAN_MODULEPATH), MathUtil.formatDecimalToString(prmPurchasePlanDto.getProjectMoney(), "##,##0.00"));//合同金额
        hostMap.put(ErpI18NHelper.englishToChinese(PrmProjectMainAttribute.STATE,
                ErpMobileModulePathAttribute.PRM_PURCHASE_PLAN_MODULEPATH),  FMCodeHelper.getDescByCode(prmPurchasePlanDto.getPurchasePlanState(), "CDM_BILL_STATE"));//流程状态
        hostMap.put(ErpI18NHelper.englishToChinese(PrmProjectMainAttribute.IS_PRE_PROJECT,
                ErpMobileModulePathAttribute.PRM_PURCHASE_PLAN_MODULEPATH), FMCodeHelper.getDescByCode(prmPurchasePlanDto.getIsPreProject()==null?"0":prmPurchasePlanDto.getIsPreProject().toString(), "IS_PRE_PROJECT"));//预立项

        itemMap.putAll(hostMap);

        //子表
        extraGridMap.put(ErpMobileTerminalAttribute.EXTRA_GRID,this.collectPurchasePackageVariable(prmPurchasePlanDto) );
        itemMap.putAll(extraGridMap);

        out.put(ErpMobileTerminalAttribute.ITEM, itemMap);

        //附件表
        out.putAll(this.collectAttachemtVariable(prmPurchasePlanDto));

        //样例，部门code，如果工作流不是自定义的code，则不需要。。。
        out.put(ErpMobileTerminalAttribute.PROCESS_DEPT_CODE,prmPurchasePlanDto.getContractorOffice());
        return out;
    }

    private void loadExtraDescField(PrmPurchasePlanDto prmProjectMain){
        prmProjectMain.setPrmProjectMainId(prmProjectMain.getUuid());
        if (prmProjectMain == null) {
            return;
        }
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        //******set project information start
        if (StringUtil.isNotEmpty(prmProjectMain.getProjectManager())) {
            ScdpUser managerUser = ErpUserHelper.getScdpUserByUserId(prmProjectMain.getProjectManager());
            if (managerUser != null) {
                prmProjectMain.setManager(managerUser.getUserName());
            }
        }
        if (StringUtil.isNotEmpty(prmProjectMain.getContractorOffice())) {
            prmProjectMain.setContractorOfficeDesc(OrgHelper.getOrgNameByCode(prmProjectMain.getContractorOffice()));
        }
        if (StringUtil.isNotEmpty(prmProjectMain.getCustomerId())) {
            String custName = purchaseplanManager.getCustmerNameByUuid(prmProjectMain.getCustomerId());
            prmProjectMain.setCustomer(custName);
        }
        //****set project information end

        List<PrmPurchasePackageDto> lstPurchasePackage = prmProjectMain.getPrmPurchasePackageDto();
        if (ListUtil.isNotEmpty(lstPurchasePackage)) {
            List<String> lstMaterialClassCode = lstPurchasePackage.stream().filter(x -> StringUtil.isNotEmpty(x
                    .getMaterialClassCode())).map
                    (x -> x.getMaterialClassCode()).distinct().collect(Collectors.toList());
            if (ListUtil.isNotEmpty(lstMaterialClassCode)) {
                Map condition = new HashMap<>();
                condition.put(ScmMaterialClassAttribute.CODE, lstMaterialClassCode);
                List<ScmMaterialClass> lstMaterialClass = pcm.findByAnyFields(ScmMaterialClass.class, condition, null);
                if (ListUtil.isNotEmpty(lstMaterialClass)) {
                    for (ScmMaterialClass materialClass : lstMaterialClass) {
                        for (PrmPurchasePackageDto purchasePackage : lstPurchasePackage) {
                            if (materialClass.getCode().equals(purchasePackage.getMaterialClassCode())) {
                                purchasePackage.setMaterialClassCodeDesc(materialClass.getName());
                            }
                        }
                    }
                }
            }
        }
    }

    private Map collectPurchasePackageVariable( PrmPurchasePlanDto prmPurchasePlanDto) {
        Map resultMap = new HashMap<>();
        List<Map> list = new ArrayList<>();
        List<PrmPurchasePackageDto> prmPurchasePackageDtoList = prmPurchasePlanDto.getPrmPurchasePackageDto();
        if (ListUtil.isNotEmpty(prmPurchasePackageDtoList)) {
            for (PrmPurchasePackageDto purchasePackageDto : prmPurchasePackageDtoList) {
                Map map = new LinkedHashMap<>();
                map.put(ErpI18NHelper.englishToChinese(PrmPurchasePackageAttribute.PACKAGE_NAME,
                        ErpMobileModulePathAttribute.PRM_PURCHASE_PLAN_MODULEPATH), purchasePackageDto.getPackageName());//包名
                map.put(ErpI18NHelper.englishToChinese(PrmPurchasePackageAttribute.PACKAGE_BUDGET_MONEY,
                        ErpMobileModulePathAttribute.PRM_PURCHASE_PLAN_MODULEPATH), MathUtil.formatDecimalToString(purchasePackageDto.getPackageBudgetMoney(), "##,##0.00"));//包预算金额
                map.put(ErpI18NHelper.englishToChinese(PrmPurchasePackageAttribute.MATERIAL_CLASS_CODE,
                        ErpMobileModulePathAttribute.PRM_PURCHASE_PLAN_MODULEPATH), purchasePackageDto.getMaterialClassCodeDesc());//物料科目
                map.put(ErpI18NHelper.englishToChinese(PrmPurchasePackageAttribute.PURCHASE_LEVEL ,
                        ErpMobileModulePathAttribute.PRM_PURCHASE_PLAN_MODULEPATH),FMCodeHelper.getDescByCode(purchasePackageDto.getPurchaseLevel(), "PRM_PURCHASE_LEVEL"));//采购级别

                list.add(map);
            }
        }
        resultMap.put("采购包", list);
        return resultMap;
    }

    private Map collectAttachemtVariable(PrmPurchasePlanDto prmPurchasePlanDto) {

        Map resultMap = new HashMap<>();
        List<Map> attachmentInfo = new ArrayList<Map>();
        List<CdmFileRelationDto> cdmFileRelationDtoList = prmPurchasePlanDto.getCdmFileRelationDto();
        resultMap.putAll(mobileTerminalQuery.getAttachmentsInfo(cdmFileRelationDtoList));
        return resultMap;
    }
}
