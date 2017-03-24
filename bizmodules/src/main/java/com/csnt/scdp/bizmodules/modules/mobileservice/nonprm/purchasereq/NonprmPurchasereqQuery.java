package com.csnt.scdp.bizmodules.modules.mobileservice.nonprm.purchasereq;

import com.csnt.scdp.bizmodules.attributes.ErpMobileDtoAttribute;
import com.csnt.scdp.bizmodules.attributes.ErpMobileModulePathAttribute;
import com.csnt.scdp.bizmodules.attributes.ErpMobileTerminalAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmPurchaseReqAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmPurchaseReqDetailAttribute;
import com.csnt.scdp.bizmodules.modules.cdm.cdmfile.dto.CdmFileRelationDto;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpI18NHelper;
import com.csnt.scdp.bizmodules.modules.mobileservice.ERPMobileTerminalBaseVariableCollectionAction;
import com.csnt.scdp.bizmodules.modules.mobileservice.intf.MobileTerminalQuery;
import com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.dto.NonProjectBudgetAjustCDto;
import com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.dto.NonProjectBudgetAjustHDto;
import com.csnt.scdp.bizmodules.modules.nonprm.nonprmpurchasereq.action.LoadAction;
import com.csnt.scdp.bizmodules.modules.nonprm.nonprmpurchasereq.dto.PrmPurchaseReqDetailDto;
import com.csnt.scdp.bizmodules.modules.nonprm.nonprmpurchasereq.dto.PrmPurchaseReqDto;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.util.*;
import com.csnt.scdp.sysmodules.helper.FMCodeHelper;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by xiezf on 2016/9/11.
 * 采购申请（非项目）
 */
@Scope("singleton")
@Controller("mobile-terminal-query-non_prm_purchase_request")
@Transactional
public class NonprmPurchasereqQuery extends ERPMobileTerminalBaseVariableCollectionAction {

    @Resource(name = "mobile-terminal-query")
    private MobileTerminalQuery mobileTerminalQuery;
    private BigDecimal totalMoney;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {

        Map out = new LinkedHashMap<>();
        Map itemMap = new LinkedHashMap<>();
        Map extraGridMap = new LinkedHashMap<>();

        Map basePojoMap = super.perform(inMap);
        BasePojo basePojo = (BasePojo) basePojoMap.get(ErpMobileTerminalAttribute.BASE_POJO);
        PrmPurchaseReqDto prmPurchaseReqDto = (PrmPurchaseReqDto) basePojo;
        totalMoney = BigDecimal.valueOf(0);

        String departmentCode = "";
        String uuid = prmPurchaseReqDto.getUuid();
        Map map = new HashMap();
        map.put("selfconditions", " REQ.UUID = '" + uuid + "'");
        map.put("selfconditions2", " 1=1 ");
        DAOMeta daoMeta = DAOHelper.getDAO("nonprmpurchasereq-dao", "common_query", new ArrayList(), map);
        List<Map<String, Object>> lstChange = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(lstChange)) {
            prmPurchaseReqDto.setFinancialSubject(lstChange.get(0).get("financialSubject").toString());
            prmPurchaseReqDto.setFinancialSubjectCode(lstChange.get(0).get("financialSubjectCode").toString());
            departmentCode = OrgHelper.getOrgNameByCode((String) lstChange.get(0).get("departmentCode"));
        }

        //主表
        Map hostMap = new LinkedHashMap<>();
        hostMap.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqAttribute.PRUCHASE_LEVEL, ErpMobileModulePathAttribute.NONPRM_PUR_CHASEREQ_MODULEPATH), FMCodeHelper.getDescByCode(prmPurchaseReqDto.getPurchaseLevel(), "PRM_PURCHASE_LEVEL") );//采购级别
        hostMap.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqAttribute.DEPARTMENT_CODE, ErpMobileModulePathAttribute.NONPRM_PUR_CHASEREQ_MODULEPATH),departmentCode);//所属部门
        hostMap.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqAttribute.FINANCIAL_SUBJECT_CODE, ErpMobileModulePathAttribute.NONPRM_PUR_CHASEREQ_MODULEPATH), prmPurchaseReqDto.getFinancialSubjectCode());//费用科目
        hostMap.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqAttribute.FINANCIAL_SUBJECT, ErpMobileModulePathAttribute.NONPRM_PUR_CHASEREQ_MODULEPATH), prmPurchaseReqDto.getFinancialSubject());//费用名称

        extraGridMap.put(ErpMobileTerminalAttribute.EXTRA_GRID, this.collectGridDetailVariable(prmPurchaseReqDto));
        hostMap.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqAttribute.TOTAL_MONEY, ErpMobileModulePathAttribute.NONPRM_PUR_CHASEREQ_MODULEPATH), MathUtil.formatDecimalToString(totalMoney, "##,##0.00"));//合计(元)

        itemMap.putAll(hostMap);
        itemMap.putAll(extraGridMap);

        out.put(ErpMobileTerminalAttribute.ITEM, itemMap);
        //附件表
        out.putAll(this.collectAttachemtVariable(prmPurchaseReqDto));

        out.put(ErpMobileTerminalAttribute.PROCESS_DEPT_CODE, prmPurchaseReqDto.getDepartmentCode());

        return out;
    }
    //填充附件
    private Map collectAttachemtVariable(PrmPurchaseReqDto prmPurchaseReqDto) {
        Map resultMap = new HashMap<>();
        List<Map> attachmentInfo = new ArrayList<Map>();
        List<CdmFileRelationDto> cdmFileRelationDtoList = prmPurchaseReqDto.getCdmFileRelationDto();
        resultMap.putAll(mobileTerminalQuery.getAttachmentsInfo(cdmFileRelationDtoList));
        return resultMap;
    }

    //填充子表
    private Map collectGridDetailVariable(PrmPurchaseReqDto prmPurchaseReqDto) {
        Map resultMap = new HashMap<>();
        List<Map> prmPurchaseReqDetailDtoInfo = new ArrayList<>();
        List<PrmPurchaseReqDetailDto> prmPurchaseReqDetailDtoList = prmPurchaseReqDto.getPrmPurchaseReqDetailDto();

        Map mapCond = new HashMap();
        mapCond.put("qryCondition", prmPurchaseReqDto.getUuid());
        DAOMeta daoMeta = DAOHelper.getDAO("nonprmpurchasereq-dao", "query_load_reqdetail", null, mapCond);
        daoMeta.setNeedFilter(false);
        List<Map<String, Object>> lstChange = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);

        if (ListUtil.isNotEmpty(prmPurchaseReqDetailDtoList)) {
            for (PrmPurchaseReqDetailDto prmPurchaseReqDetailDto : prmPurchaseReqDetailDtoList) {
                Map map = new LinkedHashMap<>();

                if (ListUtil.isNotEmpty(lstChange)){
                    for (Map tmp : lstChange) {
                        if (((Map) tmp).get("uuid").toString().equals(prmPurchaseReqDetailDto.getUuid())) {
                            BigDecimal remainPrice = tmp.get("remainPrice")==null?null:new BigDecimal(tmp.get("remainPrice").toString());
                            map.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqDetailAttribute.PURCHASE_BUDGET_MONEY, ErpMobileModulePathAttribute.NONPRM_PUR_CHASEREQ_MODULEPATH), MathUtil.formatDecimalToString(remainPrice, "##,##0.00"));//可申请金额
                            BigDecimal remainAmount = tmp.get("remainAmount")==null?null:new BigDecimal(tmp.get("remainAmount").toString());
                            map.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqDetailAttribute.PLAN_AMOUNT, ErpMobileModulePathAttribute.NONPRM_PUR_CHASEREQ_MODULEPATH), MathUtil.formatDecimalToString(remainAmount, "##,##0.00"));//可申请数量
                            BigDecimal price = tmp.get("price")==null?null:new BigDecimal(tmp.get("price").toString());
                            map.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqDetailAttribute.PRICE, ErpMobileModulePathAttribute.NONPRM_PUR_CHASEREQ_MODULEPATH), MathUtil.formatDecimalToString(price, "##,##0.00"));//单价
                            break;
                        }
                    }
                }
                map.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqDetailAttribute.NAME, ErpMobileModulePathAttribute.NONPRM_PUR_CHASEREQ_MODULEPATH), prmPurchaseReqDetailDto.getName());
                map.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqDetailAttribute.MODEL, ErpMobileModulePathAttribute.NONPRM_PUR_CHASEREQ_MODULEPATH), prmPurchaseReqDetailDto.getModel());
                map.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqDetailAttribute.AMOUNT, ErpMobileModulePathAttribute.NONPRM_PUR_CHASEREQ_MODULEPATH), MathUtil.formatDecimalToString(prmPurchaseReqDetailDto.getAmount(), "##,##0.00"));
                map.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqDetailAttribute.EXPECTED_PRICE, ErpMobileModulePathAttribute.NONPRM_PUR_CHASEREQ_MODULEPATH), MathUtil.formatDecimalToString(prmPurchaseReqDetailDto.getExpectedPrice(), "##,##0.00"));
                map.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqDetailAttribute.UNIT, ErpMobileModulePathAttribute.NONPRM_PUR_CHASEREQ_MODULEPATH), prmPurchaseReqDetailDto.getUnit());
                //如果数量和单价为空则返回0
                String amount = StringUtil.replaceNull(prmPurchaseReqDetailDto.getAmount(),"0");
                String price = StringUtil.replaceNull(prmPurchaseReqDetailDto.getExpectedPrice(),"0");
                BigDecimal subTotal = BigDecimal.valueOf(Long.valueOf(amount)).multiply(BigDecimal.valueOf(Long.valueOf(price)));
                map.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqDetailAttribute.SUB_TOTAL, ErpMobileModulePathAttribute.NONPRM_PUR_CHASEREQ_MODULEPATH), MathUtil.formatDecimalToString(subTotal, "##,##0.00"));
                map.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqDetailAttribute.SUPPLIER_ID, ErpMobileModulePathAttribute.NONPRM_PUR_CHASEREQ_MODULEPATH), prmPurchaseReqDetailDto.getSupplierId());
                map.put(ErpI18NHelper.englishToChinese(PrmPurchaseReqDetailAttribute.ARRIVE_DATE, ErpMobileModulePathAttribute.NONPRM_PUR_CHASEREQ_MODULEPATH), DateUtil.formatDate(prmPurchaseReqDetailDto.getArriveDate(), "yyyy-MM-dd"));
                prmPurchaseReqDetailDtoInfo.add(map);
                totalMoney = totalMoney.add(subTotal);
            }
        }
        resultMap.put("采购包明细", prmPurchaseReqDetailDtoInfo);
        return resultMap;
    }
}
