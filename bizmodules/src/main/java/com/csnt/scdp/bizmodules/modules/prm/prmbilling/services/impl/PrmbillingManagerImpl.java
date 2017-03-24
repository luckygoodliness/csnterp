package com.csnt.scdp.bizmodules.modules.prm.prmbilling.services.impl;

import com.csnt.scdp.bizmodules.entity.prm.PrmBilling;
import com.csnt.scdp.bizmodules.entity.prm.PrmCustomer;
import com.csnt.scdp.bizmodules.entity.prm.PrmCustomerBank;
import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entityattributes.fad.FmCurrencyAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmBillingAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmCustomerAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmCustomerBankAttribute;
import com.csnt.scdp.bizmodules.modules.prm.prmbilling.services.intf.PrmbillingManager;
import com.csnt.scdp.bizmodules.modules.prm.prmcustomer.dto.PrmCustomerBankDto;
import com.csnt.scdp.bizmodules.modules.prm.prmcustomer.dto.PrmCustomerDto;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.framework.util.UUIDUtil;
import com.csnt.scdp.sysmodules.entity.FmCurrency;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;
import com.csnt.scdp.sysmodules.helper.NumberingHelper;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:  PrmbillingManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 17:35:07
 */

@Scope("singleton")
@Service("prmbilling-manager")
public class PrmbillingManagerImpl implements PrmbillingManager {

    @Override
    public String isEmptyReturnZero(String value) {
        if (value.trim().length() == 0) {
            return "0";
        } else {
            return value.trim();
        }
    }

    @Override
    public String isZeroReturnEmpty(String value) {
        try {
            if (new BigDecimal(value).compareTo(new BigDecimal("0")) == 0) {
                return "";
            } else {
                return value.trim();
            }
        } catch (Exception e) {
            return value.trim();
        }
    }

    @Override
    public String isNullReturnEmpty(Object value) {
        if (value == null || value.equals("null")) {
            return "";
        } else {
            return value.toString().trim();
        }
    }

    @Override
    public String isNullReturnEmpty(Integer value) {
        if (value == null) {
            return "";
        } else {
            return value.toString().trim();
        }
    }

    @Override
    public void billingCheckTimeStamp(String prmBillingUuid, String tblVersion) {
        try {
            if (isNullReturnEmpty(prmBillingUuid).length() > 0 && isNullReturnEmpty(tblVersion).length() > 0) {
                String sql = "UPDATE PRM_BILLING SET TBL_VERSION = '" + UUIDUtil.getUUID() + "' WHERE UUID = '" + prmBillingUuid + "' AND TBL_VERSION = '" + tblVersion + "'";
                DAOMeta daoMeta = new DAOMeta();
                daoMeta.setStrSql(sql);
                daoMeta.setNeedFilter(false);
                if (!(PersistenceFactory.getInstance().updateByNativeSQL(daoMeta) > 0)) {
                    throw new BizException("开票时间戳验证:开票过期(已被其他用户修改过),操作无效,请刷新后重新尝试!");
                }
            } else {
                throw new BizException("开票时间戳验证:初始条件设定不足!");
            }
        } catch (Exception e) {
            throw new BizException("开票时间戳异常:占用超时!");
        }
    }

    @Override
    public void setExtraData(Map outMap) {
        Map dtoMap = (Map) outMap.get("prmBillingDto");
        if (dtoMap != null) {
            //1.设置项目名的显示
            String projectName = "";
            String projectCode = "";
            Integer isPreProject = 0;
            String prmProjectMainId = (String) dtoMap.get("prmProjectMainId");
            PrmProjectMain prmProjectMain = PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, prmProjectMainId);
            if (prmProjectMain != null) {
                projectName = prmProjectMain.getProjectName();
                projectCode = prmProjectMain.getProjectCode();
                isPreProject = prmProjectMain.getIsPreProject();
            }
            dtoMap.put(PrmBillingAttribute.PROJECT_NAME, projectName);
            dtoMap.put(PrmBillingAttribute.PROJECT_CODE, projectCode);
            dtoMap.put(PrmBillingAttribute.IS_PRE_PROJECT, isPreProject);
            //设置申请人姓名的显示
            String reqPersonName = "";
            String reqPerson = (String) dtoMap.get("reqPerson");
            if (reqPerson != null) {
                Map paramMap = new HashMap<>();
                paramMap.put(ScdpUserAttribute.USER_ID, reqPerson);
                List<ScdpUser> list = PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, paramMap, null);
                if (ListUtil.isNotEmpty(list)) {
                    reqPersonName = list.get(0).getUserName();
                }
            }
            dtoMap.put(PrmBillingAttribute.REQ_PERSON_NAME, reqPersonName);
            //设置业主单位名的显示
            String customer = "";
            String customerName = (String) dtoMap.get("customerName");
            if (StringUtil.isNotEmpty(customerName)) {
                PrmCustomer prmCustomer = PersistenceFactory.getInstance().findByPK(PrmCustomer.class, customerName);
                if (prmCustomer != null) {
                    customer = prmCustomer.getCustomerName();
                }
                dtoMap.put(PrmBillingAttribute.CUSTOMER, customer);
            }
            //设置开票币种的显示
            String invoiceCurrencyName = "";
            String invoiceCurrency = (String) dtoMap.get("invoiceCurrency");
            if (invoiceCurrency != null) {
                Map paramMap = new HashMap<>();
                paramMap.put(FmCurrencyAttribute.CURRENCY_CODE, invoiceCurrency);
                List<FmCurrency> list = PersistenceFactory.getInstance().findByAnyFields(FmCurrency.class, paramMap, null);
                if (ListUtil.isNotEmpty(list)) {
                    invoiceCurrencyName = list.get(0).getCurrencyDesc();
                }
            }
            dtoMap.put(PrmBillingAttribute.INVOICE_CURRENCY_NAME, invoiceCurrencyName);
            //设置原币种的显示
            String originalCurrencyName = "";
            String originalCurrency = (String) dtoMap.get("originalCurrency");
            if (originalCurrency != null) {
                Map paramMap = new HashMap<>();
                paramMap.put(FmCurrencyAttribute.CURRENCY_CODE, originalCurrency);
                List<FmCurrency> list = PersistenceFactory.getInstance().findByAnyFields(FmCurrency.class, paramMap, null);
                if (ListUtil.isNotEmpty(list)) {
                    originalCurrencyName = list.get(0).getCurrencyDesc();
                }
            }
            dtoMap.put(PrmBillingAttribute.ORIGINAL_CURRENCY_NAME, originalCurrencyName);
            //设置部门
            String reqOffice = (String) dtoMap.get("reqOffice");
            if (StringUtil.isNotEmpty(reqOffice)) {
                dtoMap.put("reqOfficeDesc", OrgHelper.getOrgNameByCode(reqOffice));
            }
        }
    }

    //根据合同id获取数据
    @Override
    public PrmBilling getCustomerForUUID(String uuid) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        PrmCustomer prmCustomer = pcm.findByPK(PrmCustomer.class, uuid);
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(PrmCustomerBankAttribute.PRM_CUSTOMER_ID, uuid);
        List<PrmCustomerBank> lstPrmCustomerBank = pcm.findByAnyFields(PrmCustomerBank.class, mapConditions, null);
        if (prmCustomer != null) {
            PrmBilling prmBilling = new PrmBilling();
            prmBilling.setPhone(prmCustomer.getCustomerTel());
            prmBilling.setCustomerInvoiceName(prmCustomer.getCustomerName());
            prmBilling.setCustomerLocation(prmCustomer.getCustomerAddress());
            prmBilling.setTaxNo(prmCustomer.getTaxNo());

            if (ListUtil.isNotEmpty(lstPrmCustomerBank)) {
                if (StringUtil.isNotEmpty(lstPrmCustomerBank.get(0).getBankName())) {
                    prmBilling.setBankName(lstPrmCustomerBank.get(0).getBankName());
                }
                if (StringUtil.isNotEmpty(lstPrmCustomerBank.get(0).getBankNumber())) {
                    prmBilling.setBankAccount(lstPrmCustomerBank.get(0).getBankNumber());
                }
            }
            return prmBilling;
        }
        return null;
    }

    @Override
    public Map getCustomerLatestBankInfo(Map inMap) {
        Map out = new HashMap<>();
        String customer = (String) inMap.get("supplierCode");
        if (StringUtil.isNotEmpty(customer)) {
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            Map paramMap = new HashMap<>();
            paramMap.put(PrmCustomerBankAttribute.PRM_CUSTOMER_ID, customer);
            List prmCustomerBankList = pcm.findByAnyFields(PrmCustomerBank.class, paramMap, com.csnt.scdp.framework.codegenerator.attributes.CommonAttribute.SYS_COLUMN_CREATE_TIME + " DESC");
            if (ListUtil.isNotEmpty(prmCustomerBankList)) {
                String bankId = ((PrmCustomerBank) prmCustomerBankList.get(0)).getBankNumber();
                String bankName = ((PrmCustomerBank) prmCustomerBankList.get(0)).getBankName();
                out.put("bankId", bankId);
                out.put("bankName", bankName);
            }
        }
        return out;
    }

    @Override
    public void updateCustomerAfterBilling(String billingUuid) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        PrmBilling prmBilling = pcm.findByPK(PrmBilling.class, billingUuid);
        String customerInvoiceId = prmBilling.getCustomerInvoiceId();
        String customerInvoiceName = prmBilling.getCustomerInvoiceName();
        String bankName = prmBilling.getBankName();
        String bankAccount = prmBilling.getBankAccount();
        String taxNo = prmBilling.getTaxNo();
        String customerLocation = prmBilling.getCustomerLocation();
        String phone = prmBilling.getPhone();
//        StringBuffer sb = new StringBuffer();
        if (StringUtil.isEmpty(customerInvoiceName)) {
//            sb.append("业主发票抬头，");
            throw new BizException("业主发票抬头不能为空！");
        }
//        if (StringUtil.isEmpty(bankName)) {
//            sb.append("开户银行名称，");
//        }
//        if (StringUtil.isEmpty(bankAccount)) {
//            sb.append("开户银行账号，");
//        }
//        if (StringUtil.isEmpty(taxNo)) {
//            sb.append("纳税号，");
//        }
//        if (sb.toString().length() < 1) {
//
//        }
        String prmCustomerUuid = "";
        Map conditionMap = new HashMap<>();
        conditionMap.put(PrmCustomerAttribute.CUSTOMER_NAME, customerInvoiceName);
        List list = DtoHelper.findByAnyFields(PrmCustomerDto.class, conditionMap, null);
        if (ListUtil.isNotEmpty(list)) {//如果已经存在该客户
            PrmCustomerDto prmCustomerDto = (PrmCustomerDto) list.get(0);
            prmCustomerUuid = prmCustomerDto.getUuid();
            //更新taxNo纳税号
            if (StringUtil.isNotEmpty(taxNo)) {
                if (StringUtil.isEmpty(prmCustomerDto.getTaxNo())) {
                    String sql = "UPDATE PRM_CUSTOMER P SET P.TAX_NO ='" + taxNo + "' WHERE P.UUID='" + prmCustomerUuid + "' AND P.TAX_NO IS NULL";
                    DAOMeta daoMeta = new DAOMeta(sql);
                    pcm.updateByNativeSQL(daoMeta);
                }
            }
            if (StringUtil.isNotEmpty(customerLocation)) {
                if (StringUtil.isEmpty(prmCustomerDto.getCustomerAddress())) {
                    String sql = "UPDATE PRM_CUSTOMER P SET P.CUSTOMER_ADDRESS ='" + customerLocation + "' WHERE P.UUID='" + prmCustomerUuid + "' AND P.CUSTOMER_ADDRESS IS NULL";
                    DAOMeta daoMeta = new DAOMeta(sql);
                    pcm.updateByNativeSQL(daoMeta);
                }
            }
            if (StringUtil.isNotEmpty(phone)) {
                if (StringUtil.isEmpty(prmCustomerDto.getCustomerTel())) {
                    String sql = "UPDATE PRM_CUSTOMER P SET P.CUSTOMER_TEL ='" + phone + "' WHERE P.UUID='" + prmCustomerUuid + "' AND P.CUSTOMER_TEL IS NULL";
                    DAOMeta daoMeta = new DAOMeta(sql);
                    pcm.updateByNativeSQL(daoMeta);
                }
            }

            //更新银行账号
            if (StringUtil.isNotEmpty(bankAccount) && StringUtil.isNotEmpty(bankName)) {
                Boolean isNewBankNo = true;
                if (ListUtil.isNotEmpty(prmCustomerDto.getPrmCustomerBankDto())) {
                    List<PrmCustomerBankDto> prmCustomerBankDtoList = prmCustomerDto.getPrmCustomerBankDto();
                    for (PrmCustomerBankDto prmCustomerBankDto : prmCustomerBankDtoList) {
                        if (StringUtil.isNotEmpty(prmCustomerBankDto.getBankNumber()) && bankAccount.equals(prmCustomerBankDto.getBankNumber())) {
                            isNewBankNo = false;
                            break;
                        }
                    }
                }
                if (isNewBankNo) {
                    PrmCustomerBank PrmCustomerBank = new PrmCustomerBank();
                    PrmCustomerBank.setBankName(bankName.trim());
                    PrmCustomerBank.setBankNumber(bankAccount.trim());
                    PrmCustomerBank.setPrmCustomerId(prmCustomerUuid);
                    pcm.insert(PrmCustomerBank);
                }
            }
        } else {//新客户
            prmCustomerUuid = UUIDUtil.getUUID();
            PrmCustomerDto prmCustomerDto = new PrmCustomerDto();
            prmCustomerDto.setUuid(prmCustomerUuid);
            prmCustomerDto.setCustomerName(customerInvoiceName);
            prmCustomerDto.setTaxNo(taxNo);
            prmCustomerDto.setCustomerAddress(customerLocation);
            prmCustomerDto.setCustomerTel(phone);
            if (StringUtil.isNotEmpty(bankAccount) && StringUtil.isNotEmpty(bankName)) {
                PrmCustomerBankDto PrmCustomerBankDto = new PrmCustomerBankDto();
                PrmCustomerBankDto.setBankName(bankName);
                PrmCustomerBankDto.setBankNumber(bankAccount);
                PrmCustomerBankDto.setEditflag("+");
                List bankList = new ArrayList<>();
                bankList.add(PrmCustomerBankDto);
                prmCustomerDto.setPrmCustomerBankDto(bankList);
            }
            prmCustomerDto.setEditflag("+");
            DtoHelper.CascadeSave(prmCustomerDto);
        }
        if (StringUtil.isNotEmpty(prmCustomerUuid) && StringUtil.isEmpty(customerInvoiceId)) {
            prmBilling.setCustomerInvoiceId(prmCustomerUuid);
            pcm.update(prmBilling);
        }
    }

    // M3_C11_F1_增加消息推送
    @Override
    public List<Map<String, Object>> getTreasurers() {
        String sql = "select * from v_user_roles t where role_name = '会计'";
        DAOMeta daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        List<Map<String, Object>> lstUserInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        return lstUserInfo;
    }

    @Override
    public String getProjectCode(String uuid) {
        String sql = "select pm.project_code\n" +
                "from prm_billing pb\n" +
                "left join prm_project_main pm\n" +
                "on pb.prm_project_main_id = pm.uuid\n" +
                "where pb.uuid = '" + uuid + "'";
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        DAOMeta daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        List<Map<String, Object>> projectInfos = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        Map<String, Object> result = projectInfos.get(0);
        return (String) result.get("projectCode");
    }

    @Override
    public void validaBankNo(String customerName, String bankNo) {
        if (StringUtil.isNotEmpty(bankNo) && StringUtil.isNotEmpty(customerName)) {
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            Map conditionMap = new HashMap<>();
            conditionMap.put(PrmCustomerBankAttribute.BANK_NUMBER, bankNo.trim());
            List<PrmCustomerBank> customerBankList = pcm.findByAnyFields(PrmCustomerBank.class, conditionMap, null);
            if (ListUtil.isNotEmpty(customerBankList)) {
                String customerId = customerBankList.get(0).getPrmCustomerId();
                PrmCustomer PrmCustomer = pcm.findByPK(PrmCustomer.class, customerId);
                if (!PrmCustomer.getCustomerName().trim().equals(customerName)) {
                    throw new BizException("该银行账号已经被客户" + PrmCustomer.getCustomerName() + "使用！");
                }
            }
        }
    }
}