package com.csnt.scdp.bizmodules.modules.mobileservice.prm.receipts;

import com.csnt.scdp.bizmodules.attributes.ErpMobileDtoAttribute;
import com.csnt.scdp.bizmodules.attributes.ErpMobileModulePathAttribute;
import com.csnt.scdp.bizmodules.attributes.ErpMobileTerminalAttribute;
import com.csnt.scdp.bizmodules.entity.prm.PrmContract;
import com.csnt.scdp.bizmodules.entity.prm.PrmCustomer;
import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entity.scm.ScmContract;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmCustomerAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmReceiptsAttribute;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpI18NHelper;
import com.csnt.scdp.bizmodules.modules.fad.cashreq.dto.FadCashReqDto;
import com.csnt.scdp.bizmodules.modules.mobileservice.ERPMobileTerminalBaseVariableCollectionAction;
import com.csnt.scdp.bizmodules.modules.mobileservice.intf.MobileTerminalQuery;
import com.csnt.scdp.bizmodules.modules.prm.receipts.dto.PrmReceiptsDto;
import com.csnt.scdp.bizmodules.modules.prm.receipts.services.intf.ReceiptsManager;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.DateUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MathUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.helper.FMCodeHelper;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by xuwm on 2016/9/13.
 */
@Scope("singleton")
@Controller("mobile-terminal-query-prm_receipt")
@Transactional
public class PrmReceiptsQuery extends ERPMobileTerminalBaseVariableCollectionAction{
    @Resource(name="mobile-terminal-query")
    private MobileTerminalQuery mobileTerminalQuery;

    @Override
    public Map perform(Map inMap) throws BizException,SysException{
        Map out=new LinkedHashMap<>();
        Map itemMap=new LinkedHashMap<>();
        Map extraGridMap=new LinkedHashMap<>();
        inMap.put("dto", ErpMobileDtoAttribute.definitionKey2Dto.get("Prm_Receipt_Dto"));
        Map basePojoMap=super.perform(inMap);
        String workFlowDefinitionKey=(String)inMap.get(WorkFlowAttribute.WORKFLOW_DEFINITION_KEY);
        BasePojo basePojo=(BasePojo)basePojoMap.get(ErpMobileTerminalAttribute.BASE_POJO);
        PrmReceiptsDto prmReceiptsDto=(PrmReceiptsDto)basePojo;

        PrmProjectMain prmProjectMain = PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, prmReceiptsDto.getPrmProjectMainId());

        PrmContract prmContract = null;
        if(StringUtil.isNotEmpty(prmReceiptsDto.getPrmContractId()))
            prmContract = PersistenceFactory.getInstance().findByPK(PrmContract.class, prmReceiptsDto.getPrmContractId());

        List lstPartyId = new ArrayList();
        String payer = (String) prmReceiptsDto.getPayer();
        if (StringUtil.isNotEmpty(payer)) {
            lstPartyId.add(payer);
        }
        String customerId = (String) prmReceiptsDto.getCustomerId();
        if (StringUtil.isNotEmpty(customerId)) {
            lstPartyId.add(customerId);
        }

        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        Map mapPartyId = new HashMap();
        Map hostMap=new LinkedHashMap<>();
        if (ListUtil.isNotEmpty(lstPartyId)) {
            mapPartyId.put(PrmCustomerAttribute.UUID, lstPartyId);
            hostMap.put(ErpI18NHelper.englishToChinese(PrmReceiptsAttribute.PROJECT_ID, ErpMobileModulePathAttribute.PRM_RECEIPT_MODULEPATH),prmProjectMain!=null?prmProjectMain.getProjectName():null);//所属项目
            hostMap.put(ErpI18NHelper.englishToChinese(PrmReceiptsAttribute.PROJECT_CODE, ErpMobileModulePathAttribute.PRM_RECEIPT_MODULEPATH), prmProjectMain != null ? prmProjectMain.getProjectCode() : null);//项目代号
            hostMap.put(ErpI18NHelper.englishToChinese(PrmReceiptsAttribute.PRM_CONTRACT_ID,ErpMobileModulePathAttribute.PRM_RECEIPT_MODULEPATH),prmContract!=null?prmContract.getProjectName():null);//合同名称
            List<PrmCustomer> lstParty = pcm.findByAnyFields(PrmCustomer.class, mapPartyId, null);
            if (ListUtil.isNotEmpty(lstParty)) {
                for (PrmCustomer party : lstParty) {
                    if (party.getUuid().equals(customerId)) {
                        hostMap.put(ErpI18NHelper.englishToChinese(PrmReceiptsAttribute.CUSTOMER_ID, ErpMobileModulePathAttribute.PRM_RECEIPT_MODULEPATH), party.getCustomerName());
                    }
                    if (party.getUuid().equals(payer)) {
                        hostMap.put(ErpI18NHelper.englishToChinese(PrmReceiptsAttribute.PAYER,ErpMobileModulePathAttribute.PRM_RECEIPT_MODULEPATH), party.getCustomerName());
//                        prmReceiptsDto.put("payerDesc", party.getCustomerName());
                    }
                }
            }
        }
        hostMap.put(ErpI18NHelper.englishToChinese(PrmReceiptsAttribute.ESTIMATE_MONEY,ErpMobileModulePathAttribute.PRM_RECEIPT_MODULEPATH), MathUtil.formatDecimalToString(prmReceiptsDto.getEstimateMoney(), "##,##0.00"));//预计收款金额
        hostMap.put(ErpI18NHelper.englishToChinese(PrmReceiptsAttribute.ACTUAL_MONEY,ErpMobileModulePathAttribute.PRM_RECEIPT_MODULEPATH), MathUtil.formatDecimalToString(prmReceiptsDto.getActualMoney(), "##,##0.00"));//实际收款金额
        hostMap.put(ErpI18NHelper.englishToChinese(PrmReceiptsAttribute.MONEY_TYPE,ErpMobileModulePathAttribute.PRM_RECEIPT_MODULEPATH),FMCodeHelper.getDescByCode(prmReceiptsDto.getMoneyType(), "MONEY_TYPE"));
        hostMap.put(ErpI18NHelper.englishToChinese(PrmReceiptsAttribute.PRM_UNKNOWN_RECEIPTS_ID,ErpMobileModulePathAttribute.PRM_RECEIPT_MODULEPATH),prmReceiptsDto.getPrmUnknownReceiptsId());
        hostMap.put(ErpI18NHelper.englishToChinese(PrmReceiptsAttribute.ESTIMATE_DATE,ErpMobileModulePathAttribute.PRM_RECEIPT_MODULEPATH), DateUtil.formatDate(prmReceiptsDto.getEstimateDate(), "yyyy-MM-dd"));//预计到款时间
        hostMap.put(ErpI18NHelper.englishToChinese(PrmReceiptsAttribute.ACTUAL_DATE,ErpMobileModulePathAttribute.PRM_RECEIPT_MODULEPATH), DateUtil.formatDate(prmReceiptsDto.getActualDate(), "yyyy-MM-dd"));//实际到款时间
        hostMap.put(ErpI18NHelper.englishToChinese(PrmReceiptsAttribute.STATE,ErpMobileModulePathAttribute.PRM_RECEIPT_MODULEPATH), FMCodeHelper.getDescByCode(prmReceiptsDto.getState(), "CDM_BILL_STATE"));//流程状态
        hostMap.put(ErpI18NHelper.englishToChinese(PrmReceiptsAttribute.DEPARTMENT_CODE,ErpMobileModulePathAttribute.PRM_RECEIPT_MODULEPATH), OrgHelper.getOrgNameByCode(prmReceiptsDto.getDepartmentCode()));//所属部门
        hostMap.put(ErpI18NHelper.englishToChinese(PrmReceiptsAttribute.IS_INTERNAL,ErpMobileModulePathAttribute.PRM_RECEIPT_MODULEPATH), "1".equals(prmReceiptsDto.getIsInternal())?"是":"否");
        hostMap.put(ErpI18NHelper.englishToChinese(PrmReceiptsAttribute.INTERNAL_OFFICE,ErpMobileModulePathAttribute.PRM_RECEIPT_MODULEPATH),null);//委托部门

        itemMap.putAll(hostMap);

        out.put(ErpMobileTerminalAttribute.EXTRA_GRID,new HashMap());
        itemMap.putAll(extraGridMap);

        out.put(ErpMobileTerminalAttribute.ITEM,itemMap);
        out.put(ErpMobileTerminalAttribute.PROCESS_DEPT_CODE,prmReceiptsDto.getDepartmentCode());
        return out;


    }
}
