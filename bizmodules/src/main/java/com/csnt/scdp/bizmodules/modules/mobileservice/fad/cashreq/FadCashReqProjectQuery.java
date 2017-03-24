package com.csnt.scdp.bizmodules.modules.mobileservice.fad.cashreq;

import com.atomikos.icatch.SysException;
import com.csnt.scdp.bizmodules.attributes.ErpMobileModulePathAttribute;
import com.csnt.scdp.bizmodules.attributes.ErpMobileTerminalAttribute;
import com.csnt.scdp.bizmodules.entity.fad.FadCashReq;
import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entity.scm.ScmContract;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCashReqAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCashReqInvoiceAttribute;
import com.csnt.scdp.bizmodules.modules.cdm.cdmfile.dto.CdmFileRelationDto;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpCodeHelper;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpI18NHelper;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpUserHelper;
import com.csnt.scdp.bizmodules.modules.fad.cashreq.dto.FadCashReqDto;
import com.csnt.scdp.bizmodules.modules.fad.invoice.dto.FadCashReqInvoiceDto;
import com.csnt.scdp.bizmodules.modules.mobileservice.ERPMobileTerminalBaseVariableCollectionAction;
import com.csnt.scdp.bizmodules.modules.mobileservice.intf.MobileTerminalQuery;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.DateUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MathUtil;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.helper.FMCodeHelper;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import com.fasterxml.jackson.databind.deser.Deserializers;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.beans.PersistenceDelegate;
import java.util.*;

/**
 * Created by xuwm on 2016/9/12.
 */
@Scope("singleton")
@Controller("mobile-terminal-query-prm_cash_request")
@Transactional
public class FadCashReqProjectQuery extends ERPMobileTerminalBaseVariableCollectionAction {
    @Resource(name="mobile-terminal-query")
    private MobileTerminalQuery mobileTerminalQuery;


    @Override
    public Map perform(Map inMap) throws BizException,SysException{
        Map out=new LinkedHashMap<>();//返回结果
        Map itemMap=new LinkedHashMap<>();//主表数据
        Map extraGridMap=new LinkedHashMap<>();//子表数据

        Map basePojoMap=super.perform(inMap);
        String workFlowDefinitionKey=(String)inMap.get(WorkFlowAttribute.WORKFLOW_DEFINITION_KEY);
        BasePojo basePojo=(BasePojo)basePojoMap.get(ErpMobileTerminalAttribute.BASE_POJO);
        FadCashReqDto fadCashReqDto=(FadCashReqDto)basePojo;

        String projectId=fadCashReqDto.getProjectId();
        if(StringUtils.isNotBlank(projectId)){
            PrmProjectMain obj= PersistenceFactory.getInstance().findByPK(PrmProjectMain.class,projectId);
            if(obj!=null){
                fadCashReqDto.setProjectName(obj.getProjectName());
                fadCashReqDto.setPrmContractorOffice(obj.getContractorOffice());
            }
        }

        String purchaseContractId=fadCashReqDto.getPurchaseContractId();
        if(StringUtils.isNotBlank(purchaseContractId)){
            ScmContract obj=PersistenceFactory.getInstance().findByPK(ScmContract.class,purchaseContractId);
            if(obj!=null){
                fadCashReqDto.setPurchaseContractIdPlus(obj.getScmContractCode());
            }
        }

        String orgCode =fadCashReqDto.getOfficeId();
        if(orgCode!=null){
            String orgName= OrgHelper.getOrgNameByCode(orgCode);
            fadCashReqDto.setOfficeIdDesc(orgName);
        }

        String userId=fadCashReqDto.getStaffId();
        ScdpUser user= ErpUserHelper.findUserByUserId(userId);
        if(user!=null){
            String userName=user.getUserName();
            fadCashReqDto.setStaffIdDesc(userName);
        }

        //主表
        Map hostMap=new LinkedHashMap<>();
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.RUNNING_NO, ErpMobileModulePathAttribute.PRM_CASH_REQUEST_MODULEPATH),fadCashReqDto.getRunningNo());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.MONEY,ErpMobileModulePathAttribute.PRM_CASH_REQUEST_MODULEPATH), MathUtil.formatDecimalToString(fadCashReqDto.getMoney(), "##,##0.00"));
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.IS_URGENT,ErpMobileModulePathAttribute.PRM_CASH_REQUEST_MODULEPATH), "1".equals(fadCashReqDto.getIsUrgent())?"是":"否");
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.STATE,ErpMobileModulePathAttribute.PRM_CASH_REQUEST_MODULEPATH),FMCodeHelper.getDescByCode(fadCashReqDto.getState(), "FAD_BILL_STATE"));
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.PROJECT_CODE,ErpMobileModulePathAttribute.PRM_CASH_REQUEST_MODULEPATH),fadCashReqDto.getFadSubjectCode());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.SUBJECT_NAME,ErpMobileModulePathAttribute.PRM_CASH_REQUEST_MODULEPATH),fadCashReqDto.getSubjectName());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.PROJECT_NAME,ErpMobileModulePathAttribute.PRM_CASH_REQUEST_MODULEPATH),fadCashReqDto.getProjectName());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.PAY_STYLE,ErpMobileModulePathAttribute.PRM_CASH_REQUEST_MODULEPATH),FMCodeHelper.getDescByCode(fadCashReqDto.getPayStyle(), "FAD_PAYWAY"));
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.APPLY_DATE,ErpMobileModulePathAttribute.PRM_CASH_REQUEST_MODULEPATH), DateUtil.formatDate(fadCashReqDto.getApplyDate(), "yyyy-MM-dd"));
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.SQUARE_DATE,ErpMobileModulePathAttribute.PRM_CASH_REQUEST_MODULEPATH), DateUtil.formatDate(fadCashReqDto.getSquareDate(), "yyyy-MM-dd"));
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.PRECLEAR_DATE,ErpMobileModulePathAttribute.PRM_CASH_REQUEST_MODULEPATH), DateUtil.formatDate(fadCashReqDto.getPreclearDate(), "yyyy-MM-dd"));
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.STAFF_ID,ErpMobileModulePathAttribute.PRM_CASH_REQUEST_MODULEPATH),fadCashReqDto.getStaffIdDesc());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.OFFICE_ID,ErpMobileModulePathAttribute.PRM_CASH_REQUEST_MODULEPATH),fadCashReqDto.getOfficeIdDesc());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.PAYEE_NAME,ErpMobileModulePathAttribute.PRM_CASH_REQUEST_MODULEPATH),fadCashReqDto.getPayeeName());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.PAYEE_BANK_NAME,ErpMobileModulePathAttribute.PRM_CASH_REQUEST_MODULEPATH),fadCashReqDto.getPayeeBankName());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.PAYEE_ACCOUNT,ErpMobileModulePathAttribute.PRM_CASH_REQUEST_MODULEPATH),fadCashReqDto.getPayeeAccount());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.MODEL,ErpMobileModulePathAttribute.PRM_CASH_REQUEST_MODULEPATH),fadCashReqDto.getModel());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.OTHER,ErpMobileModulePathAttribute.PRM_CASH_REQUEST_MODULEPATH),fadCashReqDto.getOther());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.REMARK,ErpMobileModulePathAttribute.PRM_CASH_REQUEST_MODULEPATH),fadCashReqDto.getRemark());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.OPERATOR_NAME,ErpMobileModulePathAttribute.PRM_CASH_REQUEST_MODULEPATH),fadCashReqDto.getOperatorName());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.OPERATE_TIME,ErpMobileModulePathAttribute.PRM_CASH_REQUEST_MODULEPATH), DateUtil.formatDate(fadCashReqDto.getOperateTime(), "yyyy-MM-dd"));

        itemMap.putAll(hostMap);
        //核销信息子表
        extraGridMap.put(ErpMobileTerminalAttribute.EXTRA_GRID,new HashMap());
        itemMap.putAll(extraGridMap);

        out.put(ErpMobileTerminalAttribute.ITEM,itemMap);

        out.putAll(this.collectAttachemtVariable(fadCashReqDto));

        out.put(ErpMobileTerminalAttribute.PROCESS_DEPT_CODE,fadCashReqDto.getPrmContractorOffice());
        return out;
    }

//    private Map collectInvoiceVariable(FadCashReqDto fadCashReqDto){
//        Map resultMap = new HashMap<>();
//        List<Map> fadCashReqInvoiceInfo = new ArrayList<>();
//        List<FadCashReqInvoiceDto> fadCashReqInvoiceDtoList = fadCashReqDto.getFadCashReqInvoiceDto();
//        if (ListUtil.isNotEmpty(fadCashReqInvoiceDtoList)) {
//            for (FadCashReqInvoiceDto fadCashReqInvoiceDto : fadCashReqInvoiceDtoList) {
//                Map map = new LinkedHashMap<>();
//                map.put(ErpI18NHelper.englishToChinese(FadCashReqInvoiceAttribute.INVOICE_RUNNING_NO, ErpMobileModulePathAttribute.PRM_CASH_REQUEST_MODULEPATH), fadCashReqInvoiceDto.getInvoiceRunningNo());//流水号
//                map.put(ErpI18NHelper.englishToChinese(FadCashReqInvoiceAttribute.STATE, ErpMobileModulePathAttribute.PRM_CASH_REQUEST_MODULEPATH), fadCashReqInvoiceDto.getState());
//                map.put(ErpI18NHelper.englishToChinese(FadCashReqInvoiceAttribute.CLEARANCE_TYPE_NAME, ErpMobileModulePathAttribute.PRM_CASH_REQUEST_MODULEPATH), fadCashReqInvoiceDto.getClearanceTypeName());
//                map.put(ErpI18NHelper.englishToChinese(FadCashReqInvoiceAttribute.INVOICE_NO, ErpMobileModulePathAttribute.PRM_CASH_REQUEST_MODULEPATH), fadCashReqInvoiceDto.getInvoiceNo());
//                map.put(ErpI18NHelper.englishToChinese(FadCashReqInvoiceAttribute.CLEARANCE_MONEY, ErpMobileModulePathAttribute.PRM_CASH_REQUEST_MODULEPATH), fadCashReqInvoiceDto.getClearanceMoney());
//                fadCashReqInvoiceInfo.add(map);
//            }
//
//        }
//        //todo 这些国际化需要以后再加
//        resultMap.put("核销信息", fadCashReqInvoiceInfo);
//        return resultMap;
//
//    }

    private Map collectAttachemtVariable(FadCashReqDto fadCashReqDto){
        Map resultMap = new HashMap<>();
        List<Map> attachmentInfo = new ArrayList<Map>();
        List<CdmFileRelationDto> cdmFileRelationDtoList = fadCashReqDto.getCdmFileRelationDto();
        resultMap.putAll(mobileTerminalQuery.getAttachmentsInfo(cdmFileRelationDtoList));
        return resultMap;
    }
}
