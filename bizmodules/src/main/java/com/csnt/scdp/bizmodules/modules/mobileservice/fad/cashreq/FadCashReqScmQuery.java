package com.csnt.scdp.bizmodules.modules.mobileservice.fad.cashreq;

import com.csnt.scdp.bizmodules.attributes.ErpMobileDtoAttribute;
import com.csnt.scdp.bizmodules.attributes.ErpMobileModulePathAttribute;
import com.csnt.scdp.bizmodules.attributes.ErpMobileTerminalAttribute;
import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entity.scm.ScmContract;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCashReqAttribute;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpI18NHelper;
import com.csnt.scdp.bizmodules.modules.common.impl.CommonServiceManagerImpl;
import com.csnt.scdp.bizmodules.modules.common.intf.CommonServiceManager;
import com.csnt.scdp.bizmodules.modules.fad.cashreq.action.LoadAction;
import com.csnt.scdp.bizmodules.modules.fad.cashreq.dto.FadCashReqDto;
import com.csnt.scdp.bizmodules.modules.mobileservice.ERPMobileTerminalBaseVariableCollectionAction;
import com.csnt.scdp.bizmodules.modules.mobileservice.intf.MobileTerminalQuery;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.dto.BasePojo;
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
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiezf on 2016/9/12.
 * 采购请款
 */
@Scope("singleton")
@Controller("mobile-terminal-query-scm_purchasepaymentrequestcontract")
@Transactional
public class FadCashReqScmQuery extends ERPMobileTerminalBaseVariableCollectionAction {

    @Resource(name = "mobile-terminal-query")
    private MobileTerminalQuery mobileTerminalQuery;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {

        Map out = new LinkedHashMap<>();
        Map itemMap = new LinkedHashMap<>();
        Map extraGridMap = new LinkedHashMap<>();
        inMap.put("uuid",inMap.get("businessKey"));
        inMap.put("dtoClass", ErpMobileDtoAttribute.definitionKey2Dto.get("Scm_PurchasePaymentRequestContract_Dto"));

        Map basePojoMap = super.perform(inMap);
        BasePojo basePojo = (BasePojo) basePojoMap.get(ErpMobileTerminalAttribute.BASE_POJO);
        FadCashReqDto fadCashReqDto = (FadCashReqDto) basePojo;
        Map hostMap = new LinkedHashMap<>();

        //填充项目名称
        String projectId = fadCashReqDto.getProjectId();
        if (StringUtils.isNotBlank(projectId)) {
            PrmProjectMain obj = PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, projectId);
            if (obj != null) {
                fadCashReqDto.setProjectName(obj.getProjectName());
                fadCashReqDto.setPrmContractorOffice(obj.getContractorOffice());
            }
        }
        //填充部门
        String orgCode = fadCashReqDto.getOfficeId();
        String orgName = OrgHelper.getOrgNameByCode(orgCode);
        fadCashReqDto.setOfficeIdDesc(orgName);
        //填充用户名
        CommonServiceManager commonServiceManager = new CommonServiceManagerImpl();
        String staffId = fadCashReqDto.getStaffId();
        if (StringUtils.isNotBlank(staffId)) {
            List<ScdpUser> obj = commonServiceManager.findUserByUserId(staffId);
            if (ListUtil.isNotEmpty(obj)) {
                String name = obj.get(0).getUserName();
                fadCashReqDto.setStaffIdDesc(name);
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

        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.MONEY, ErpMobileModulePathAttribute.FAD_CASHREQ_MODULEPATH), MathUtil.formatDecimalToString(fadCashReqDto.getMoney(), "##,##0.00"));
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.IS_URGENT, ErpMobileModulePathAttribute.FAD_CASHREQ_MODULEPATH), fadCashReqDto.getIsUrgent() == 1 ? "是" : "否");
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.PURCHASE_CONTRACT_ID, ErpMobileModulePathAttribute.FAD_CASHREQ_MODULEPATH), fadCashReqDto.getPurchaseContractIdPlus());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.PROJECT_CODE, ErpMobileModulePathAttribute.FAD_CASHREQ_MODULEPATH), fadCashReqDto.getFadSubjectCode());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.SUPPLIER_NAME, ErpMobileModulePathAttribute.FAD_CASHREQ_MODULEPATH), fadCashReqDto.getSupplierName());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.PROJECT_NAME, ErpMobileModulePathAttribute.FAD_CASHREQ_MODULEPATH), fadCashReqDto.getProjectName());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.SUBJECT_CODE, ErpMobileModulePathAttribute.FAD_CASHREQ_MODULEPATH), fadCashReqDto.getSubjectCode());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.SUBJECT_NAME, ErpMobileModulePathAttribute.FAD_CASHREQ_MODULEPATH), fadCashReqDto.getSubjectName());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.PAY_STYLE, ErpMobileModulePathAttribute.FAD_CASHREQ_MODULEPATH), FMCodeHelper.getDescByCode(fadCashReqDto.getPayStyle(), "FAD_PAYWAY"));
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.APPLY_DATE, ErpMobileModulePathAttribute.FAD_CASHREQ_MODULEPATH), DateUtil.formatDate(fadCashReqDto.getApplyDate(), "yyyy-MM-dd"));
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.SQUARE_DATE, ErpMobileModulePathAttribute.FAD_CASHREQ_MODULEPATH), DateUtil.formatDate(fadCashReqDto.getSquareDate(), "yyyy-MM-dd"));
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.PRECLEAR_DATE, ErpMobileModulePathAttribute.FAD_CASHREQ_MODULEPATH), DateUtil.formatDate(fadCashReqDto.getPreclearDate(), "yyyy-MM-dd"));
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.STAFF_ID, ErpMobileModulePathAttribute.FAD_CASHREQ_MODULEPATH), fadCashReqDto.getStaffIdDesc());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.OFFICE_ID, ErpMobileModulePathAttribute.FAD_CASHREQ_MODULEPATH), fadCashReqDto.getOfficeIdDesc());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.PAYEE_NAME, ErpMobileModulePathAttribute.FAD_CASHREQ_MODULEPATH), fadCashReqDto.getPayeeName());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.PAYEE_BANK_NAME, ErpMobileModulePathAttribute.FAD_CASHREQ_MODULEPATH), fadCashReqDto.getPayeeBankName());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.PAYEE_ACCOUNT, ErpMobileModulePathAttribute.FAD_CASHREQ_MODULEPATH), fadCashReqDto.getPayeeAccount());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.MODEL, ErpMobileModulePathAttribute.FAD_CASHREQ_MODULEPATH), fadCashReqDto.getModel());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.REMARK, ErpMobileModulePathAttribute.FAD_CASHREQ_MODULEPATH), fadCashReqDto.getRemark());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.OPERATOR_NAME, ErpMobileModulePathAttribute.FAD_CASHREQ_MODULEPATH), fadCashReqDto.getOperatorName());
        hostMap.put(ErpI18NHelper.englishToChinese(FadCashReqAttribute.OPERATE_TIME, ErpMobileModulePathAttribute.FAD_CASHREQ_MODULEPATH), DateUtil.formatDate(fadCashReqDto.getOperateTime(), "yyyy-MM-dd"));
        itemMap.putAll(hostMap);

        extraGridMap.put(ErpMobileTerminalAttribute.EXTRA_GRID, new HashMap<>());
        itemMap.putAll(extraGridMap);

        //核销信息子表
        itemMap.putAll(extraGridMap);

        out.put(ErpMobileTerminalAttribute.ITEM, itemMap);

        return out;
    }

}
