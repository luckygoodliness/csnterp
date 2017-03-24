package com.csnt.scdp.bizmodules.modules.fad.invoice.services.intf;

import com.csnt.scdp.bizmodules.entity.fad.FadCashClearance;
import com.csnt.scdp.bizmodules.entity.fad.FadInvoice;
import com.csnt.scdp.bizmodules.modules.fad.invoice.dto.FadInvoiceDto;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Description:  InvoiceManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-25 14:13:13
 */
public interface InvoiceManager {

    public List<Map> getCashReqInfo(List<String> fadCashReqIdList);

    public String contractInvoiceCheckAdd(String contractId, BigDecimal value);

    public String contractInvoiceCheckModify(String uuid, BigDecimal value);

    public void checkLXHTSupplierName(String uuid);

    public String fadCashReqInvoiceCheckAdd(String fadCashReqId, BigDecimal value);

    public String fadCashReqInvoiceCheckModify(String detailUuid, BigDecimal value);
    // 存储过程核销
    public void contractInvoiceWrittenOff(String action, String id);
    //JAVA核销
    public void contractInvoiceClearance(FadInvoiceDto dto);


    /**
     * 获取项目差旅预算ID
     *
     * @param projectMainId 项目UUID
     * @return true超标false没超
     */
    public List getPrmTripBudget(String projectMainId);
    public void checkPrmTripBudget(String projectMainId,BigDecimal payCash);

    /**
     * 获取项目差旅预算ID
     *
     * @param officeCode 部门ID
     * @return true超标false没超
     */
    public List getNonTripBudget(String officeCode,String fadYear);
    public void checkNonTripBudget(String officeCode,BigDecimal payCash,String fadYear);

    /**
     * 校验预算是否超标
     *
     * @param isPrm    false、非项目，true、项目
     * @param budgetId 预算的UUID
     * @param value    报销金额
     */
    public void checkBudgetIsEnough(boolean isPrm, String budgetId, BigDecimal value);

    /**
     * 采购发票生成凭证前校验：是否存在该发票涉及合同还未入账的请款或现在核销，如果存在则不能生成凭证
     *@param uuid 预算的发票UUID
     */
    public void checkForCreateCertificate(String uuid);

    /**
     * @param invoiceId 发票的UUID
     * @return 发票信息
     */
    public FadInvoice getObjectById(String invoiceId);

    /**
     * 根据UUID组，获取发票集合，Key为对应的UUID
     *
     * @param lstUuid
     * @return
     */
    public Map<String, FadInvoice> getObjectsByIds(List lstUuid);


    Map<String, FadCashClearance> getFadCashClearanceByIds(List lstUuid);

    //根据UUID更改FAD_INVOIE的流程状态(字典FAD_BILL_STATE)

    /**
     * 把FAD_INVOICE表流程状态更新为1、提交
     *
     * @param uuid
     * @return
     */
    public boolean changeStateToSubmitted(String uuid);

    /**
     * 把FAD_INVOICE表流程状态更新为2、通过
     *
     * @param uuid
     * @return
     */
    public boolean changeStateToApproved(String uuid);

    /**
     * 把FAD_INVOICE表流程状态更新为3、作废
     *
     * @param uuid
     * @return
     */
    public boolean changeStateToDisabled(String uuid);

    /**
     * 把FAD_INVOICE表流程状态更新为8、已生成凭证
     *
     * @param uuid
     * @return
     */
    public boolean changeStateToCertificated(String uuid);

    /**
     * 把FAD_INVOICE表流程状态更新为4、发送
     *
     * @param uuid
     * @return
     */
    public boolean changeStateToSended(String uuid);

    public void isAddPackage(String subjectId);
}