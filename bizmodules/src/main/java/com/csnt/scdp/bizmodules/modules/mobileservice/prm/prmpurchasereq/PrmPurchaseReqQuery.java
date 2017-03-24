package com.csnt.scdp.bizmodules.modules.mobileservice.prm.prmpurchasereq;

import com.csnt.scdp.bizmodules.attributes.ErpMobileDtoAttribute;
import com.csnt.scdp.bizmodules.attributes.ErpMobileModulePathAttribute;
import com.csnt.scdp.bizmodules.attributes.ErpMobileTerminalAttribute;
import com.csnt.scdp.bizmodules.entity.prm.PrmPurchaseReqDetail;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmPurchaseReqAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmPurchaseReqDetailAttribute;
import com.csnt.scdp.bizmodules.modules.cdm.cdmfile.dto.CdmFileRelationDto;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpI18NHelper;
import com.csnt.scdp.bizmodules.modules.mobileservice.ERPMobileTerminalBaseVariableCollectionAction;
import com.csnt.scdp.bizmodules.modules.mobileservice.intf.MobileTerminalQuery;
import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.dto.PrmPurchaseReqDetailDto;
import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.dto.PrmPurchaseReqDto;
import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.dto.PrmPurchasePackageDto;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MathUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.helper.FMCodeHelper;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by lijx on 2016/9/13.
 */
@Scope("singleton")
@Controller("mobile-terminal-query-prm_purchase_request")
@Transactional
public class PrmPurchaseReqQuery extends ERPMobileTerminalBaseVariableCollectionAction {
    @Resource(name = "mobile-terminal-query")
    private MobileTerminalQuery mobileTerminalQuery;


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map out = new LinkedHashMap<>();
        Map itemMap = new LinkedHashMap<>();
        Map extraGridMap = new LinkedHashMap<>();
        inMap.put("dto", ErpMobileDtoAttribute.definitionKey2Dto.get("Prm_Purchase_Request_Dto"));
        Map basePojoMap = super.perform(inMap);
        String workFlowDefinitionKey = (String) inMap.get(WorkFlowAttribute.WORKFLOW_DEFINITION_KEY);
        BasePojo basePojo = (BasePojo) basePojoMap.get(ErpMobileTerminalAttribute.BASE_POJO);
        PrmPurchaseReqDto prmPurchaseReqDto = (PrmPurchaseReqDto) basePojo;

        double totalMoney = 0;
        List<PrmPurchaseReqDetailDto> purchaseReqDetailLst =  prmPurchaseReqDto.getPrmPurchaseReqDetailDto();
        for(PrmPurchaseReqDetailDto dto : purchaseReqDetailLst){
            totalMoney += (dto.getExpectedPrice().doubleValue() * dto.getAmount().doubleValue());
        }
        Map map = new HashMap();
        map.put("selfconditions", " REQ.UUID = '" + prmPurchaseReqDto.getUuid() + "'");
        DAOMeta daoMeta = DAOHelper.getDAO("prmpurchasereq-dao", "common_query", new ArrayList(), map);
        List<Map<String, Object>> lstChange = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(lstChange)) {
            prmPurchaseReqDto.setProjectName((String) lstChange.get(0).get("projectName"));
        }

        //主表
        Map hostMap = new LinkedHashMap<>();
        hostMap.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqAttribute.PURCHASE_REQ_NO,
                ErpMobileModulePathAttribute.PRM_PURCHASE_REQUEST_MODULEPATH), prmPurchaseReqDto.getPurchaseReqNo());//申请单号
        hostMap.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqAttribute.IS_INNER,
                ErpMobileModulePathAttribute.PRM_PURCHASE_REQUEST_MODULEPATH), FMCodeHelper.getDescByCode(prmPurchaseReqDto.getIsInner()==null?"0":prmPurchaseReqDto.getIsInner().toString(), "IS_PRE_PROJECT"));//是否内委
        hostMap.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqAttribute.INNER_SUPPLIER,
                ErpMobileModulePathAttribute.PRM_PURCHASE_REQUEST_MODULEPATH), StringUtil.isEmpty(prmPurchaseReqDto.getInnerSupplier()) ? null : OrgHelper.getOrgNameByCode(prmPurchaseReqDto.getInnerSupplier()));//内委部门
        hostMap.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqAttribute.TOTAL_MONEY,
                ErpMobileModulePathAttribute.PRM_PURCHASE_REQUEST_MODULEPATH), MathUtil.formatDecimalToString(new BigDecimal(totalMoney), "##,##0.00"));//合计
        hostMap.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqAttribute.DEPARTMENT_CODE,
                ErpMobileModulePathAttribute.PRM_PURCHASE_REQUEST_MODULEPATH), StringUtil.isEmpty(prmPurchaseReqDto.getDepartmentCode())? null : OrgHelper.getOrgNameByCode(prmPurchaseReqDto.getDepartmentCode()));//所属部门
        hostMap.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqAttribute.PROJECT_NAME,
                ErpMobileModulePathAttribute.PRM_PURCHASE_REQUEST_MODULEPATH),  prmPurchaseReqDto.getProjectName());//所属项目

        itemMap.putAll(hostMap);

        //子表
        extraGridMap.put(ErpMobileTerminalAttribute.EXTRA_GRID,this.collectPurchaseReqDetailVariable(prmPurchaseReqDto));
        itemMap.putAll(extraGridMap);

        out.put(ErpMobileTerminalAttribute.ITEM, itemMap);

        //附件表
        out.putAll(this.collectAttachemtVariable(prmPurchaseReqDto));

        //样例，部门code，如果工作流不是自定义的code，则不需要。。。
        out.put(ErpMobileTerminalAttribute.PROCESS_DEPT_CODE,prmPurchaseReqDto.getOfficeId());


        return out;
    }
    private Map collectPurchaseReqDetailVariable( PrmPurchaseReqDto prmPurchaseReqDto) {
        Map resultMap = new HashMap<>();
        List<Map> list = new ArrayList<>();
        List<PrmPurchaseReqDetailDto> prmPurchaseReqDetailDtoList = prmPurchaseReqDto.getPrmPurchaseReqDetailDto();

        Map mapCond = new HashMap();
        mapCond.put("qryCondition", prmPurchaseReqDto.getUuid());
        DAOMeta daoMeta = DAOHelper.getDAO("prmpurchasereq-dao","query_load_reqdetail",null,mapCond);
        List<Map<String, Object>> lstChange = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(lstChange)) {
            for (PrmPurchaseReqDetailDto purchaseReqDetailDto : prmPurchaseReqDetailDtoList) {
                for (Map tmp : lstChange) {
                    if (((Map) tmp).get("uuid").toString().equals(purchaseReqDetailDto.getUuid())) {
                        purchaseReqDetailDto.setPackageName((String) tmp.get("packageName"));
                        purchaseReqDetailDto.setPlanAmount((BigDecimal) tmp.get("planAmount"));
                        break;
                    }
                }
            }
        }

        if (ListUtil.isNotEmpty(prmPurchaseReqDetailDtoList)) {
            for (PrmPurchaseReqDetailDto purchaseReqDetailDto : prmPurchaseReqDetailDtoList) {
                Map map = new LinkedHashMap<>();
                map.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqDetailAttribute.PACKAGE_NAME,
                        ErpMobileModulePathAttribute.PRM_PURCHASE_REQUEST_MODULEPATH), purchaseReqDetailDto.getPackageName());//包名
                map.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqDetailAttribute.SERIAL_NUMBER,
                        ErpMobileModulePathAttribute.PRM_PURCHASE_REQUEST_MODULEPATH), purchaseReqDetailDto.getSerialNumber());//序号
                map.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqDetailAttribute.PURCHASE_BUDGET_MONEY,
                        ErpMobileModulePathAttribute.PRM_PURCHASE_REQUEST_MODULEPATH), MathUtil.formatDecimalToString(purchaseReqDetailDto.getPurchaseBudgetMoney(), "##,##0.00"));//采购预算
                map.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqDetailAttribute.PLAN_AMOUNT,
                        ErpMobileModulePathAttribute.PRM_PURCHASE_REQUEST_MODULEPATH), MathUtil.formatDecimalToString(purchaseReqDetailDto.getPlanAmount(), "##,##0.00"));//可申请数量
                map.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqDetailAttribute.NAME,
                        ErpMobileModulePathAttribute.PRM_PURCHASE_REQUEST_MODULEPATH), purchaseReqDetailDto.getName());//名称
                map.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqDetailAttribute.MODEL,
                        ErpMobileModulePathAttribute.PRM_PURCHASE_REQUEST_MODULEPATH), purchaseReqDetailDto.getModel());//型号
                map.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqDetailAttribute.FACTORY,
                        ErpMobileModulePathAttribute.PRM_PURCHASE_REQUEST_MODULEPATH), purchaseReqDetailDto.getFactory());//厂家
                map.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqDetailAttribute.AMOUNT,
                        ErpMobileModulePathAttribute.PRM_PURCHASE_REQUEST_MODULEPATH), MathUtil.formatDecimalToString(purchaseReqDetailDto.getAmount(), "##,##0.00"));//数量
                map.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqDetailAttribute.BUDGET_PRICE,
                        ErpMobileModulePathAttribute.PRM_PURCHASE_REQUEST_MODULEPATH), MathUtil.formatDecimalToString(purchaseReqDetailDto.getBudgetPrice(), "##,##0.00"));//预算单价
                map.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqDetailAttribute.EXPECTED_PRICE,
                        ErpMobileModulePathAttribute.PRM_PURCHASE_REQUEST_MODULEPATH), MathUtil.formatDecimalToString(purchaseReqDetailDto.getExpectedPrice(), "##,##0.00"));//意向单价
                map.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqDetailAttribute.UNIT,
                        ErpMobileModulePathAttribute.PRM_PURCHASE_REQUEST_MODULEPATH), purchaseReqDetailDto.getUnit());//单位
                map.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqDetailAttribute.SUB_TOTAL,
                        ErpMobileModulePathAttribute.PRM_PURCHASE_REQUEST_MODULEPATH), MathUtil.formatDecimalToString(purchaseReqDetailDto.getAmount().multiply(purchaseReqDetailDto.getExpectedPrice()), "##,##0.00"));//小计

                list.add(map);
            }
        }

        resultMap.put("采购申请明细", list);
        return resultMap;
    }

    private Map collectAttachemtVariable(PrmPurchaseReqDto prmPurchaseReqDto) {
        Map resultMap = new HashMap<>();
        List<Map> attachmentInfo = new ArrayList<Map>();
        List<CdmFileRelationDto> cdmFileRelationDtoList = prmPurchaseReqDto.getCdmFileRelationDto();
        resultMap.putAll(mobileTerminalQuery.getAttachmentsInfo(cdmFileRelationDtoList));
        return resultMap;
    }
}
