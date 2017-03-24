package com.csnt.scdp.bizmodules.modules.mobileservice.fad.payreq;

import com.csnt.scdp.bizmodules.attributes.ErpMobileDtoAttribute;
import com.csnt.scdp.bizmodules.attributes.ErpMobileModulePathAttribute;
import com.csnt.scdp.bizmodules.attributes.ErpMobileTerminalAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadPayReqCAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadPayReqHAttribute;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpI18NHelper;
import com.csnt.scdp.bizmodules.modules.fad.payreq.dto.FadPayReqCDto;
import com.csnt.scdp.bizmodules.modules.fad.payreq.dto.FadPayReqHDto;
import com.csnt.scdp.bizmodules.modules.fad.payreq.services.intf.PayreqManager;
import com.csnt.scdp.bizmodules.modules.mobileservice.ERPMobileTerminalBaseVariableCollectionAction;
import com.csnt.scdp.bizmodules.modules.mobileservice.intf.MobileTerminalQuery;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MathUtil;
import com.csnt.scdp.sysmodules.helper.FMCodeHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by xuwm on 2016/9/14.
 */
@Scope("singleton")
@Controller("mobile-terminal-query-scm_purchasepaymentrequestmonthpayment")
@Transactional
public class PayreqprojectmonthQuery extends ERPMobileTerminalBaseVariableCollectionAction {

        @Resource(name="mobile-terminal-query")
        private MobileTerminalQuery mobileTerminalQuery;
        @Resource(name = "payreq-manager")
        private PayreqManager payreqManager;
        public Map perform(Map inMap) throws BizException,SysException {
            Map out = new LinkedHashMap<>();
            Map itemMap = new LinkedHashMap<>();
            Map extraGridMap = new LinkedHashMap<>();

            inMap.put("dto", ErpMobileDtoAttribute.definitionKey2Dto.get("Scm_PurchasePaymentRequestMonthPayment_Dto"));
            Map basePojoMap = super.perform(inMap);
            String workFlowDefinitionKey = (String) inMap.get(WorkFlowAttribute.WORKFLOW_DEFINITION_KEY);
            BasePojo basePojo = (BasePojo) basePojoMap.get(ErpMobileTerminalAttribute.BASE_POJO);
            FadPayReqHDto fadPayReqHDto=(FadPayReqHDto) basePojo;
            payreqManager.setObjectPlusInfo(fadPayReqHDto);

            Map hostMap=new LinkedHashMap<>();
            hostMap.put(ErpI18NHelper.englishToChinese(FadPayReqHAttribute.REQNO,ErpMobileModulePathAttribute.SCM_PURCHASEPAYMENTREQUESTMONTHPAYMENT_MODULEPATH),fadPayReqHDto.getReqno());
            hostMap.put(ErpI18NHelper.englishToChinese(FadPayReqHAttribute.YEAR,ErpMobileModulePathAttribute.SCM_PURCHASEPAYMENTREQUESTMONTHPAYMENT_MODULEPATH),fadPayReqHDto.getYear());
            hostMap.put(ErpI18NHelper.englishToChinese(FadPayReqHAttribute.MONTH,ErpMobileModulePathAttribute.SCM_PURCHASEPAYMENTREQUESTMONTHPAYMENT_MODULEPATH),fadPayReqHDto.getMonth());
            hostMap.put(ErpI18NHelper.englishToChinese(FadPayReqHAttribute.STATE,ErpMobileModulePathAttribute.SCM_PURCHASEPAYMENTREQUESTMONTHPAYMENT_MODULEPATH),FMCodeHelper.getDescByCode(fadPayReqHDto.getState(), "CDM_BILL_STATE"));
            itemMap.putAll(hostMap);

            extraGridMap.put(ErpMobileTerminalAttribute.EXTRA_GRID,this.collectProjectTmpVariable(fadPayReqHDto));
            itemMap.putAll(extraGridMap);
            out.put(ErpMobileTerminalAttribute.ITEM,itemMap);
            return out;
        }
    private Map collectProjectTmpVariable(FadPayReqHDto fadPayReqHDto){
        Map resultMap = new HashMap<>();
        List<Map> payreqProjectMonthInfo=new ArrayList<>();
        List<FadPayReqCDto> FadPayReqCDtoList=fadPayReqHDto.getFadPayReqCDto();
        if(ListUtil.isNotEmpty(FadPayReqCDtoList)) {
            for(FadPayReqCDto fadPayReqCDto:FadPayReqCDtoList) {
                Map map=new LinkedHashMap<>();
                map.put(ErpI18NHelper.englishToChinese(FadPayReqCAttribute.SEQ_NO, ErpMobileModulePathAttribute.SCM_PURCHASEPAYMENTREQUESTMONTHPAYMENT_MODULEPATH), fadPayReqCDto.getSeqNo());
                map.put(ErpI18NHelper.englishToChinese(FadPayReqCAttribute.ORGNAME, ErpMobileModulePathAttribute.SCM_PURCHASEPAYMENTREQUESTMONTHPAYMENT_MODULEPATH), fadPayReqCDto.getOrgName());//部门
                map.put(ErpI18NHelper.englishToChinese(FadPayReqCAttribute.SUPPLIER_ID, ErpMobileModulePathAttribute.SCM_PURCHASEPAYMENTREQUESTMONTHPAYMENT_MODULEPATH), fadPayReqCDto.getSupplierName());//供应商
                map.put(ErpI18NHelper.englishToChinese(FadPayReqCAttribute.PROJECT_MAIN_ID, ErpMobileModulePathAttribute.SCM_PURCHASEPAYMENTREQUESTMONTHPAYMENT_MODULEPATH), fadPayReqCDto.getProjectMainName());//项目名称
                map.put(ErpI18NHelper.englishToChinese(FadPayReqCAttribute.PROJECTCODE, ErpMobileModulePathAttribute.SCM_PURCHASEPAYMENTREQUESTMONTHPAYMENT_MODULEPATH), fadPayReqCDto.getProjectCode());
                map.put(ErpI18NHelper.englishToChinese(FadPayReqCAttribute.AUDIT_MONEY, ErpMobileModulePathAttribute.SCM_PURCHASEPAYMENTREQUESTMONTHPAYMENT_MODULEPATH), MathUtil.formatDecimalToString(fadPayReqCDto.getAuditMoney(), "##,##0.00"));//会议终审金额(元)
                map.put(ErpI18NHelper.englishToChinese(FadPayReqCAttribute.AUDITMONEYRATE, ErpMobileModulePathAttribute.SCM_PURCHASEPAYMENTREQUESTMONTHPAYMENT_MODULEPATH), MathUtil.formatDecimalToString(fadPayReqCDto.getAuditMoneyRate(), "##,##0.00"));//会议终审比例(%)
                map.put(ErpI18NHelper.englishToChinese(FadPayReqCAttribute.PRMRECEIPTRATE, ErpMobileModulePathAttribute.SCM_PURCHASEPAYMENTREQUESTMONTHPAYMENT_MODULEPATH), MathUtil.formatDecimalToString(fadPayReqCDto.getPrmReceiptRate(), "##,##0.00"));//项目收款比例(%)
                map.put(ErpI18NHelper.englishToChinese(FadPayReqCAttribute.REQ_MONEY, ErpMobileModulePathAttribute.SCM_PURCHASEPAYMENTREQUESTMONTHPAYMENT_MODULEPATH), MathUtil.formatDecimalToString(fadPayReqCDto.getReqMoney(), "##,##0.00"));//本期申请支付金额(元)
                map.put(ErpI18NHelper.englishToChinese(FadPayReqCAttribute.REQMONEYRATE, ErpMobileModulePathAttribute.SCM_PURCHASEPAYMENTREQUESTMONTHPAYMENT_MODULEPATH), MathUtil.formatDecimalToString(fadPayReqCDto.getReqMoneyRate(), "##,##0.00"));//申请支付比例(%)
                map.put(ErpI18NHelper.englishToChinese(FadPayReqCAttribute.APPROVE_MONEY, ErpMobileModulePathAttribute.SCM_PURCHASEPAYMENTREQUESTMONTHPAYMENT_MODULEPATH), MathUtil.formatDecimalToString(fadPayReqCDto.getApproveMoney(), "##,##0.00"));//部门确认金额(元)
                map.put(ErpI18NHelper.englishToChinese(FadPayReqCAttribute.APPROVE_MONEY_RATE, ErpMobileModulePathAttribute.SCM_PURCHASEPAYMENTREQUESTMONTHPAYMENT_MODULEPATH), MathUtil.formatDecimalToString(fadPayReqCDto.getApproveMoneyRate(), "##,##0.00"));//确认比例(%)
                map.put(ErpI18NHelper.englishToChinese(FadPayReqCAttribute.PURCHASE_MANAGER_MONEY, ErpMobileModulePathAttribute.SCM_PURCHASEPAYMENTREQUESTMONTHPAYMENT_MODULEPATH), MathUtil.formatDecimalToString(fadPayReqCDto.getPurchaseManagerMoney(), "##,##0.00"));//采购经理申请金额(元)
                map.put(ErpI18NHelper.englishToChinese(FadPayReqCAttribute.PURCHASE_MANAGER_MONEYRATE, ErpMobileModulePathAttribute.SCM_PURCHASEPAYMENTREQUESTMONTHPAYMENT_MODULEPATH), MathUtil.formatDecimalToString(fadPayReqCDto.getPurchaseManagerMoneyRate(), "##,##0.00"));//申请比例(%)
                map.put(ErpI18NHelper.englishToChinese(FadPayReqCAttribute.PURCHASE_CHIEF_MONEY, ErpMobileModulePathAttribute.SCM_PURCHASEPAYMENTREQUESTMONTHPAYMENT_MODULEPATH), MathUtil.formatDecimalToString(fadPayReqCDto.getPurchaseChiefMoney(), "##,##0.00"));//供应链部初审(元)
                map.put(ErpI18NHelper.englishToChinese(FadPayReqCAttribute.PURCHASE_CHIEF_MONEYRATE, ErpMobileModulePathAttribute.SCM_PURCHASEPAYMENTREQUESTMONTHPAYMENT_MODULEPATH), MathUtil.formatDecimalToString(fadPayReqCDto.getPurchaseChiefMoneyRate(), "##,##0.00"));//初审比例(%)
                map.put(ErpI18NHelper.englishToChinese(FadPayReqCAttribute.STATE, ErpMobileModulePathAttribute.SCM_PURCHASEPAYMENTREQUESTMONTHPAYMENT_MODULEPATH), FMCodeHelper.getDescByCode(fadPayReqCDto.getState(), "FAD_BILL_STATE"));
                payreqProjectMonthInfo.add(map);
            }
        }
        resultMap.put("月度支付明细",payreqProjectMonthInfo);
        return resultMap;
    }
}
