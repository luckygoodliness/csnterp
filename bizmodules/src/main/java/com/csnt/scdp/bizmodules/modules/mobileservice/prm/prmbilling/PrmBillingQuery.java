package com.csnt.scdp.bizmodules.modules.mobileservice.prm.prmbilling;

import com.csnt.scdp.bizmodules.attributes.ErpMobileDtoAttribute;
import com.csnt.scdp.bizmodules.attributes.ErpMobileModulePathAttribute;
import com.csnt.scdp.bizmodules.attributes.ErpMobileTerminalAttribute;
import com.csnt.scdp.bizmodules.entity.prm.PrmContract;
import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entityattributes.fad.FmCurrencyAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmBillingAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmBillingDetailAttribute;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpI18NHelper;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpUserHelper;
import com.csnt.scdp.bizmodules.modules.mobileservice.ERPMobileTerminalBaseVariableCollectionAction;
import com.csnt.scdp.bizmodules.modules.mobileservice.intf.MobileTerminalQuery;
import com.csnt.scdp.bizmodules.modules.prm.prmbilling.dto.PrmBillingDetailDto;
import com.csnt.scdp.bizmodules.modules.prm.prmbilling.dto.PrmBillingDto;
import com.csnt.scdp.bizmodules.modules.prm.prmbilling.services.intf.PrmbillingManager;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.DateUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MathUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.FmCurrency;
import com.csnt.scdp.sysmodules.helper.FMCodeHelper;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by lijx on 2016/9/13.
 */
@Scope("singleton")
@Controller("mobile-terminal-query-prm_invoice")
@Transactional
public class PrmBillingQuery  extends ERPMobileTerminalBaseVariableCollectionAction {
    @Resource(name = "mobile-terminal-query")
    private MobileTerminalQuery mobileTerminalQuery;
    @Resource(name = "prmbilling-manager")
    private PrmbillingManager prmbillingManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map out = new LinkedHashMap<>();
        Map itemMap = new LinkedHashMap<>();
        Map extraGridMap = new LinkedHashMap<>();
        inMap.put("dto", ErpMobileDtoAttribute.definitionKey2Dto.get("Prm_Invoice_Dto"));
        Map basePojoMap = super.perform(inMap);
        String workFlowDefinitionKey = (String) inMap.get(WorkFlowAttribute.WORKFLOW_DEFINITION_KEY);
        BasePojo basePojo = (BasePojo) basePojoMap.get(ErpMobileTerminalAttribute.BASE_POJO);
        PrmBillingDto prmBillingDto = (PrmBillingDto) basePojo;

        PrmProjectMain prmProjectMain = PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, prmBillingDto.getPrmProjectMainId());

        PrmContract prmContract = null;
        if(StringUtil.isNotEmpty(prmBillingDto.getPrmContractId()))
            prmContract = PersistenceFactory.getInstance().findByPK(PrmContract.class, prmBillingDto.getPrmContractId());

        Map paramMap = new HashMap<>();
        paramMap.put(FmCurrencyAttribute.CURRENCY_CODE, prmBillingDto.getInvoiceCurrency());
        List<FmCurrency> listInvCurrency = PersistenceFactory.getInstance().findByAnyFields(FmCurrency.class, paramMap, null);
        paramMap.put(FmCurrencyAttribute.CURRENCY_CODE, prmBillingDto.getOriginalCurrency());
        List<FmCurrency> listOrigCurrency = PersistenceFactory.getInstance().findByAnyFields(FmCurrency.class, paramMap, null);

        //主表
        Map hostMap = new LinkedHashMap<>();
        hostMap.put(ErpI18NHelper.englishToChinese(PrmBillingAttribute.PROJECT_NAME,
                ErpMobileModulePathAttribute.PRM_INVOICE_MODULEPATH), prmProjectMain!=null?prmProjectMain.getProjectName():null);//项目名称
        hostMap.put(ErpI18NHelper.englishToChinese(PrmBillingAttribute.PROJECT_CODE,
                ErpMobileModulePathAttribute.PRM_INVOICE_MODULEPATH), prmProjectMain!=null?prmProjectMain.getProjectCode():null);//项目代号
        hostMap.put(ErpI18NHelper.englishToChinese(PrmBillingAttribute.PRM_CONTRACT_ID,
                ErpMobileModulePathAttribute.PRM_INVOICE_MODULEPATH), prmContract!=null?prmContract.getContractName():null);//合同名称
        hostMap.put(ErpI18NHelper.englishToChinese(PrmBillingAttribute.CUSTOMER_NAME,
                ErpMobileModulePathAttribute.PRM_INVOICE_MODULEPATH), prmBillingDto.getCustomerName());//业主单位
        hostMap.put(ErpI18NHelper.englishToChinese(PrmBillingAttribute.CUSTOMER_INVOICE_NAME,
                ErpMobileModulePathAttribute.PRM_INVOICE_MODULEPATH), prmBillingDto.getCustomerInvoiceName());//业主发票抬头
        hostMap.put(ErpI18NHelper.englishToChinese(PrmBillingAttribute.CUSTOMER_INVOICE_NAME_EN,
                ErpMobileModulePathAttribute.PRM_INVOICE_MODULEPATH), prmBillingDto.getCustomerInvoiceNameEn());//业主发票英文抬头
        hostMap.put(ErpI18NHelper.englishToChinese(PrmBillingAttribute.INVOICE_TYPE,
                ErpMobileModulePathAttribute.PRM_INVOICE_MODULEPATH), FMCodeHelper.getDescByCode(prmBillingDto.getInvoiceType(), "FAD_INVOICE_TYPE") );//票据类型
        hostMap.put(ErpI18NHelper.englishToChinese(PrmBillingAttribute.TAX_NO,
                ErpMobileModulePathAttribute.PRM_INVOICE_MODULEPATH), prmBillingDto.getTaxNo());//纳税号
        hostMap.put(ErpI18NHelper.englishToChinese(PrmBillingAttribute.BANK_NAME,
                ErpMobileModulePathAttribute.PRM_INVOICE_MODULEPATH), prmBillingDto.getBankName());//开户银行名称
        hostMap.put(ErpI18NHelper.englishToChinese(PrmBillingAttribute.BANK_ACCOUNT,
                ErpMobileModulePathAttribute.PRM_INVOICE_MODULEPATH), prmBillingDto.getBankAccount());//开户银行账号
        hostMap.put(ErpI18NHelper.englishToChinese(PrmBillingAttribute.CUSTOMER_LOCATION,
                ErpMobileModulePathAttribute.PRM_INVOICE_MODULEPATH), prmBillingDto.getCustomerLocation());//业主单位地址
        hostMap.put(ErpI18NHelper.englishToChinese(PrmBillingAttribute.INVOICE_CURRENCY,
                ErpMobileModulePathAttribute.PRM_INVOICE_MODULEPATH), ListUtil.isNotEmpty(listInvCurrency)? listInvCurrency.get(0).getCurrencyDesc():null );//开票币种
        hostMap.put(ErpI18NHelper.englishToChinese(PrmBillingAttribute.ORIGINAL_CURRENCY,
                ErpMobileModulePathAttribute.PRM_INVOICE_MODULEPATH), ListUtil.isNotEmpty(listOrigCurrency)? listOrigCurrency.get(0).getCurrencyDesc():null );//原币种
        hostMap.put(ErpI18NHelper.englishToChinese(PrmBillingAttribute.PHONE,
                ErpMobileModulePathAttribute.PRM_INVOICE_MODULEPATH), prmBillingDto.getPhone());//联系电话
        hostMap.put(ErpI18NHelper.englishToChinese(PrmBillingAttribute.INVOICE_MONEY,
                ErpMobileModulePathAttribute.PRM_INVOICE_MODULEPATH), MathUtil.formatDecimalToString(prmBillingDto.getInvoiceMoney(), "##,##0.00"));//开票金额
        hostMap.put(ErpI18NHelper.englishToChinese(PrmBillingAttribute.ORIGINAL_MONEY,
                ErpMobileModulePathAttribute.PRM_INVOICE_MODULEPATH), MathUtil.formatDecimalToString(prmBillingDto.getOriginalMoney(), "##,##0.00"));//原币种金额
        hostMap.put(ErpI18NHelper.englishToChinese(PrmBillingAttribute.EXCHANGE_RATE,
                ErpMobileModulePathAttribute.PRM_INVOICE_MODULEPATH), MathUtil.formatDecimalToString(prmBillingDto.getExchangeRate(), "##,##0.00"));//开票币种对原币汇率
        hostMap.put(ErpI18NHelper.englishToChinese(PrmBillingAttribute.TAX_RATE,
                ErpMobileModulePathAttribute.PRM_INVOICE_MODULEPATH), prmBillingDto.getTaxRate()==null?null:FMCodeHelper.getDescByCode(prmBillingDto.getTaxRate().toString(), "FAD_TAX_RATE"));//税率
        hostMap.put(ErpI18NHelper.englishToChinese(PrmBillingAttribute.NETMONEY,
                ErpMobileModulePathAttribute.PRM_INVOICE_MODULEPATH), MathUtil.formatDecimalToString(prmBillingDto.getNetMoney(), "##,##0.00"));//不含税发票金额
        hostMap.put(ErpI18NHelper.englishToChinese(PrmBillingAttribute.TAX_MONEY,
                ErpMobileModulePathAttribute.PRM_INVOICE_MODULEPATH), MathUtil.formatDecimalToString(prmBillingDto.getTaxMoney(), "##,##0.00"));//税额
        hostMap.put(ErpI18NHelper.englishToChinese(PrmBillingAttribute.REQ_PERSON,
                ErpMobileModulePathAttribute.PRM_INVOICE_MODULEPATH), ErpUserHelper.findUserByUserId(prmBillingDto.getReqPerson())==null?null: ErpUserHelper.findUserByUserId(prmBillingDto.getReqPerson()).getUserName());//申请人
        hostMap.put(ErpI18NHelper.englishToChinese(PrmBillingAttribute.REQ_OFFICE,
                ErpMobileModulePathAttribute.PRM_INVOICE_MODULEPATH), OrgHelper.getOrgNameByCode(prmBillingDto.getReqOffice()));//申请部门
        hostMap.put(ErpI18NHelper.englishToChinese(PrmBillingAttribute.STATE,
                ErpMobileModulePathAttribute.PRM_INVOICE_MODULEPATH), FMCodeHelper.getDescByCode(prmBillingDto.getState(), "CDM_BILL_STATE"));//流程状态
        hostMap.put(ErpI18NHelper.englishToChinese(PrmBillingAttribute.LOCATION_TYPE,
                ErpMobileModulePathAttribute.PRM_INVOICE_MODULEPATH), prmBillingDto.getLocationType());//地点类型
        hostMap.put(ErpI18NHelper.englishToChinese(PrmBillingAttribute.PAYMENT_TYPE,
                ErpMobileModulePathAttribute.PRM_INVOICE_MODULEPATH), prmBillingDto.getPaymentType());//缴费类型
        hostMap.put(ErpI18NHelper.englishToChinese(PrmBillingAttribute.EXPECT_RECEIVE_DATE,
                ErpMobileModulePathAttribute.PRM_INVOICE_MODULEPATH), DateUtil.formatDate(prmBillingDto.getExpectReceiveDate(), "yyyy-MM-dd"));//预计收款时间
        hostMap.put(ErpI18NHelper.englishToChinese(PrmBillingAttribute.INVOICE_NO,
                ErpMobileModulePathAttribute.PRM_INVOICE_MODULEPATH), prmBillingDto.getInvoiceNo());//发票号
        hostMap.put(ErpI18NHelper.englishToChinese(PrmBillingAttribute.INVOICE_DATE,
                ErpMobileModulePathAttribute.PRM_INVOICE_MODULEPATH), DateUtil.formatDate(prmBillingDto.getInvoiceDate(), "yyyy-MM-dd"));//开票日期
        hostMap.put(ErpI18NHelper.englishToChinese(PrmBillingAttribute.REMARK,
                ErpMobileModulePathAttribute.PRM_INVOICE_MODULEPATH), prmBillingDto.getRemark());//备注

        itemMap.putAll(hostMap);
        //子表
        extraGridMap.put(ErpMobileTerminalAttribute.EXTRA_GRID, this.collectPurchasePackageVariable(prmBillingDto));
        itemMap.putAll(extraGridMap);

        out.put(ErpMobileTerminalAttribute.ITEM, itemMap);

        //样例，部门code，如果工作流不是自定义的code，则不需要。。。
        out.put(ErpMobileTerminalAttribute.PROCESS_DEPT_CODE, prmBillingDto.getReqOffice());

        return out;
    }

    private Map collectPurchasePackageVariable(PrmBillingDto prmBillingDto) {
        Map resultMap = new HashMap<>();
        List<Map> list = new ArrayList<>();
        List<PrmBillingDetailDto> prmBillingDetailDtoList = prmBillingDto.getPrmBillingDetailDto();
        if (ListUtil.isNotEmpty(prmBillingDetailDtoList)) {
            for (PrmBillingDetailDto billingDetailDto : prmBillingDetailDtoList) {
                Map map = new LinkedHashMap<>();
                map.put(ErpI18NHelper.englishToChinese(PrmBillingDetailAttribute.CONTENT,
                        ErpMobileModulePathAttribute.PRM_INVOICE_MODULEPATH), billingDetailDto.getContent());//开票中文内容
                map.put(ErpI18NHelper.englishToChinese(PrmBillingDetailAttribute.CONTENT_EN,
                        ErpMobileModulePathAttribute.PRM_INVOICE_MODULEPATH), billingDetailDto.getContentEn());//开票英文内容
                map.put(ErpI18NHelper.englishToChinese(PrmBillingDetailAttribute.UNIT,
                        ErpMobileModulePathAttribute.PRM_INVOICE_MODULEPATH), billingDetailDto.getUnit());//计量单位
                map.put(ErpI18NHelper.englishToChinese(PrmBillingDetailAttribute.AMOUNT,
                        ErpMobileModulePathAttribute.PRM_INVOICE_MODULEPATH), MathUtil.formatDecimalToString(billingDetailDto.getAmount(), "##,##0.00"));//数量
                map.put(ErpI18NHelper.englishToChinese(PrmBillingDetailAttribute.PRICE,
                        ErpMobileModulePathAttribute.PRM_INVOICE_MODULEPATH), MathUtil.formatDecimalToString(billingDetailDto.getPrice(), "##,##0.00"));//单价
                map.put(ErpI18NHelper.englishToChinese(PrmBillingDetailAttribute.TOTAL_VALUE,
                        ErpMobileModulePathAttribute.PRM_INVOICE_MODULEPATH), MathUtil.formatDecimalToString(billingDetailDto.getTotalValue(), "##,##0.00"));//原币金额

                list.add(map);
            }
        }
        resultMap.put("开票明细", list);
        return resultMap;
    }
}
