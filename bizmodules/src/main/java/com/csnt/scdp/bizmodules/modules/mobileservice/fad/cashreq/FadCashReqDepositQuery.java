package com.csnt.scdp.bizmodules.modules.mobileservice.fad.cashreq;

import com.csnt.scdp.bizmodules.attributes.ErpMobileModulePathAttribute;
import com.csnt.scdp.bizmodules.attributes.ErpMobileTerminalAttribute;
import com.csnt.scdp.bizmodules.entity.operate.OperateBusinessBidInfo;
import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entity.scm.ScmContract;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCashReqAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCashReqInvoiceAttribute;
import com.csnt.scdp.bizmodules.modules.cdm.cdmfile.dto.CdmFileRelationDto;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpI18NHelper;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpUserHelper;
import com.csnt.scdp.bizmodules.modules.fad.cashreq.dto.FadCashReqDto;
import com.csnt.scdp.bizmodules.modules.mobileservice.ERPMobileTerminalBaseVariableCollectionAction;
import com.csnt.scdp.bizmodules.modules.mobileservice.intf.MobileTerminalQuery;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.bizmodules.modules.fad.invoice.dto.FadCashReqInvoiceDto;
import com.csnt.scdp.framework.helper.SysconfigHelper;
import com.csnt.scdp.framework.util.DateUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MathUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.helper.FMCodeHelper;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lenovo on 2016/8/16.
 */

@Scope("singleton")
@Controller("mobile-terminal-query-non_prm_cashreq_deposit")
@Transactional
public class FadCashReqDepositQuery extends ERPMobileTerminalBaseVariableCollectionAction {

    @Resource(name = "mobile-terminal-query")
    private MobileTerminalQuery mobileTerminalQuery;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map out = new LinkedHashMap<>();
        Map itemMap = new LinkedHashMap<>();
        Map extraGridMap = new LinkedHashMap<>();
        Map basePojoMap = super.perform(inMap);
        String workFlowDefinitionKey = (String) inMap.get(WorkFlowAttribute.WORKFLOW_DEFINITION_KEY);
        BasePojo basePojo = (BasePojo) basePojoMap.get(ErpMobileTerminalAttribute.BASE_POJO);
        FadCashReqDto fadCashReqDto = (FadCashReqDto) basePojo;

        String infoId = fadCashReqDto.getOperateBusinessBidInfoId();
        if (StringUtils.isNotBlank(infoId)) {
            OperateBusinessBidInfo bid = PersistenceFactory.getInstance().findByPK(OperateBusinessBidInfo.class, infoId);
            if (bid != null) {
                fadCashReqDto.setOperateBusinessBidInfoIdDesc(bid.getProjectName());
            }
        }
        String projectId = fadCashReqDto.getProjectId();
        if (StringUtils.isNotBlank(projectId)) {
            PrmProjectMain obj = PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, projectId);
            if (obj != null) {
                fadCashReqDto.setProjectName(obj.getProjectName());
                fadCashReqDto.setPrmContractorOffice(obj.getContractorOffice());
            }
        }
        //填充合同
        String purchaseContractId = fadCashReqDto.getPurchaseContractId();
        if (StringUtils.isNotBlank(purchaseContractId)) {
            ScmContract obj = PersistenceFactory.getInstance().findByPK(ScmContract.class, purchaseContractId);
            if (obj != null) {
                fadCashReqDto.setPurchaseContractIdPlus(obj.getScmContractCode());
            }
        }


        String orgCode = fadCashReqDto.getOfficeId();
        if (orgCode != null) {
            String orgName = OrgHelper.getOrgNameByCode(orgCode);
            fadCashReqDto.setOfficeIdDesc(orgName);
        }

        String userId = fadCashReqDto.getStaffId();
        ScdpUser user = ErpUserHelper.findUserByUserId(userId);
        if (user != null) {
            String userName = user.getUserName();
            fadCashReqDto.setStaffIdDesc(userName);

        }
        String stateDesc = FMCodeHelper.getDescByCode(fadCashReqDto.getState(), "CDM_BILL_STATE");
        fadCashReqDto.setStateDesc(stateDesc);

        String payStyleDesc = FMCodeHelper.getDescByCode(fadCashReqDto.getPayStyle(), "FAD_PAYWAY");
        fadCashReqDto.setPayStyleDesc(payStyleDesc);

        //主表
        Map hostMap = new LinkedHashMap<>();
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.RUNNING_NO, ErpMobileModulePathAttribute.NON_PRM_CASHREQ_DEPOSIT_MODULEPATH), fadCashReqDto.getRunningNo());//流水号
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.MONEY, ErpMobileModulePathAttribute.NON_PRM_CASHREQ_DEPOSIT_MODULEPATH), MathUtil.formatDecimalToString(fadCashReqDto.getMoney(), "##,##0.00"));//请款金额
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.IS_URGENT, ErpMobileModulePathAttribute.NON_PRM_CASHREQ_DEPOSIT_MODULEPATH), "1".equals(fadCashReqDto.getIsUrgent()) ? "是" : "否");//是否加急
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.STATE, ErpMobileModulePathAttribute.NON_PRM_CASHREQ_DEPOSIT_MODULEPATH), fadCashReqDto.getStateDesc());//状态
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.OPERATE_BUSINESS_BID_INFO_ID, ErpMobileModulePathAttribute.NON_PRM_CASHREQ_DEPOSIT_MODULEPATH), fadCashReqDto.getOperateBusinessBidInfoIdDesc());//投标信息
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.FAD_SUBJECT_CODE, ErpMobileModulePathAttribute.NON_PRM_CASHREQ_DEPOSIT_MODULEPATH), fadCashReqDto.getFadSubjectCode());//项目代号
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.PROJECT_NAME, ErpMobileModulePathAttribute.NON_PRM_CASHREQ_DEPOSIT_MODULEPATH), fadCashReqDto.getProjectName());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.SUBJECT_NAME, ErpMobileModulePathAttribute.NON_PRM_CASHREQ_DEPOSIT_MODULEPATH), fadCashReqDto.getSubjectName());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.DEPOSIT_TYPE, ErpMobileModulePathAttribute.NON_PRM_CASHREQ_DEPOSIT_MODULEPATH), fadCashReqDto.getDepositType());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.PAY_STYLE, ErpMobileModulePathAttribute.NON_PRM_CASHREQ_DEPOSIT_MODULEPATH), fadCashReqDto.getPayStyleDesc());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.APPLY_DATE, ErpMobileModulePathAttribute.NON_PRM_CASHREQ_DEPOSIT_MODULEPATH), DateUtil.formatDate(fadCashReqDto.getApplyDate(), "yyyy-MM-dd"));
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.SQUARE_DATE, ErpMobileModulePathAttribute.NON_PRM_CASHREQ_DEPOSIT_MODULEPATH), DateUtil.formatDate(fadCashReqDto.getSquareDate(), "yyyy-MM-dd"));
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.STAFF_ID, ErpMobileModulePathAttribute.NON_PRM_CASHREQ_DEPOSIT_MODULEPATH), fadCashReqDto.getStaffIdDesc());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.OFFICE_ID, ErpMobileModulePathAttribute.NON_PRM_CASHREQ_DEPOSIT_MODULEPATH), fadCashReqDto.getOfficeIdDesc());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.FAD_SUBJECT_CODE, ErpMobileModulePathAttribute.NON_PRM_CASHREQ_DEPOSIT_MODULEPATH), fadCashReqDto.getFadSubjectCode());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.PAYEE_NAME, ErpMobileModulePathAttribute.NON_PRM_CASHREQ_DEPOSIT_MODULEPATH), fadCashReqDto.getPayeeName());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.MODEL, ErpMobileModulePathAttribute.NON_PRM_CASHREQ_DEPOSIT_MODULEPATH), fadCashReqDto.getModel());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.PAYEE_ACCOUNT, ErpMobileModulePathAttribute.NON_PRM_CASHREQ_DEPOSIT_MODULEPATH), fadCashReqDto.getPayeeAccount());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.OTHER, ErpMobileModulePathAttribute.NON_PRM_CASHREQ_DEPOSIT_MODULEPATH), fadCashReqDto.getOther());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.OPERATOR_NAME, ErpMobileModulePathAttribute.NON_PRM_CASHREQ_DEPOSIT_MODULEPATH), fadCashReqDto.getOperatorName());

        itemMap.putAll(hostMap);


        extraGridMap.put(ErpMobileTerminalAttribute.EXTRA_GRID, this.collectInvoiceVariable(fadCashReqDto));


        //核销信息子表
        itemMap.putAll(extraGridMap);


        out.put(ErpMobileTerminalAttribute.ITEM, itemMap);


        //附件表
        out.putAll(this.collectAttachemtVariable(fadCashReqDto));


        //样例，部门code，如果工作流不是自定义的code，则不需要。。。
        out.put(ErpMobileTerminalAttribute.PROCESS_DEPT_CODE, "");
        return out;
    }

    private Map collectInvoiceVariable(FadCashReqDto fadCashReqDto) {
        Map resultMap = new HashMap<>();
        List<Map> fadCashReqInvoiceInfo = new ArrayList<>();
        List<FadCashReqInvoiceDto> fadCashReqInvoiceDtoList = fadCashReqDto.getFadCashReqInvoiceDto();
        if (ListUtil.isNotEmpty(fadCashReqInvoiceDtoList)) {
            for (FadCashReqInvoiceDto fadCashReqInvoiceDto : fadCashReqInvoiceDtoList) {
                Map map = new LinkedHashMap<>();
                map.put(ErpI18NHelper.englishToChinese(FadCashReqInvoiceAttribute.INVOICE_RUNNING_NO, ErpMobileModulePathAttribute.NON_PRM_CASHREQ_DEPOSIT_MODULEPATH), fadCashReqInvoiceDto.getInvoiceRunningNo());//流水号
                map.put(ErpI18NHelper.englishToChinese(FadCashReqInvoiceAttribute.STATE, ErpMobileModulePathAttribute.NON_PRM_CASHREQ_DEPOSIT_MODULEPATH), fadCashReqInvoiceDto.getState());
                map.put(ErpI18NHelper.englishToChinese(FadCashReqInvoiceAttribute.CLEARANCE_TYPE_NAME, ErpMobileModulePathAttribute.NON_PRM_CASHREQ_DEPOSIT_MODULEPATH), fadCashReqInvoiceDto.getClearanceTypeName());
                map.put(ErpI18NHelper.englishToChinese(FadCashReqInvoiceAttribute.INVOICE_NO, ErpMobileModulePathAttribute.NON_PRM_CASHREQ_DEPOSIT_MODULEPATH), fadCashReqInvoiceDto.getInvoiceNo());
                map.put(ErpI18NHelper.englishToChinese(FadCashReqInvoiceAttribute.CLEARANCE_MONEY, ErpMobileModulePathAttribute.NON_PRM_CASHREQ_DEPOSIT_MODULEPATH), fadCashReqInvoiceDto.getClearanceMoney());
                fadCashReqInvoiceInfo.add(map);
            }

        }
        //todo 这些国际化需要以后再加
        resultMap.put("核销信息", fadCashReqInvoiceInfo);
        return resultMap;
    }

    private Map collectAttachemtVariable(FadCashReqDto fadCashReqDto) {

        Map resultMap = new HashMap<>();
        List<Map> attachmentInfo = new ArrayList<Map>();
        List<CdmFileRelationDto> cdmFileRelationDtoList = fadCashReqDto.getCdmFileRelationDto();
        resultMap.putAll(mobileTerminalQuery.getAttachmentsInfo(cdmFileRelationDtoList));
        return resultMap;
    }

}
