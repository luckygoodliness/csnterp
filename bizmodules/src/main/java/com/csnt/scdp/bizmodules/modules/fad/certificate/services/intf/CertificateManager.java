package com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf;

import com.csnt.scdp.bizmodules.modules.fad.certificate.dto.FadCertificateDetailDto;
import com.csnt.scdp.bizmodules.modules.fad.certificate.dto.FadCertificateDto;

import java.util.List;
import java.util.Map;

/**
 * Description:  CertificateManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-10-16 10:07:48
 */
public interface CertificateManager {

    /**
     * 凭证合并行界定符
     */
    public static String MergeSplitSymbol = "、";

    public static String NCCodeDataSymbol = "[$nc_code]";

    public static String WSFadCertificateXML = "<?xml version='1.0' encoding='GBK'?>"
            + "<ufinterface roottag='voucher' billtype='gl' replace='Y' receiver='9137A00' sender='ZHKJ' isexchange='Y' filename='voucher.xml' proc='add' operation='req'>"
            + "     <voucher id='[$fad_certificate_uuid]'>"
            + "             <voucher_head>"
            //+ "                 <!--公司 ，固定-->"
            + "                 <company>9137A00</company>"
            //+ "                 <!--凭证类别，固定-->"
            + "                 <voucher_type>[$voucher_type]</voucher_type>"
            //+ "                 <!--会计年度，不能为空， 最大长度为4,类型为:java.lang.String-->"
            + "                 <fiscal_year>[$fiscal_year]</fiscal_year>"
            //+ "                 <!--会计期间，不能为空， 最大长度为2,类型为:java.lang.String-->"
            + "                 <accounting_period>[$accounting_period]</accounting_period>"
            //+ "                 <!--凭证号，固定0，由系统自动生成当前凭证号 -->"
            + "                 <voucher_id>0</voucher_id>"
            //+ "                 <!--附单据数，不能为空，最大长度为4,类型为:int  -->"
            + "                 <attachment_number>[$attachment_number]</attachment_number>"
            //+ "                 <!--制单日期，不能为空, 最大长度为10,类型为:Date -->"
            + "                 <prepareddate>[$prepareddate]</prepareddate>"
            //+ "                 <!--制单人，固定, 最大长度为30,类型为: java.lang.String -->"
            + "                 <enter>[$enter]</enter>"
            //+ "                 <!--出纳，可以为空, 最大长度为30,类型为: java.lang.String -->"
            + "                 <cashier>[$cashier]</cashier>"
            //+ "                 <!--是否已签字，不能为空，默认为N, 最大长度为1,类型为:Boolean -->"
            + "                 <signature>[$signature]</signature>"
            //+ "                 <!--审核人，可以为空, 最大长度为30,类型为: java.lang.String -->"
            + "                 <checker>[$checker]</checker>"
            //+ "                 <!--记账日期，可以为空, 最大长度为10,类型为:Date -->"
            + "                 <posting_date>[$posting_date]</posting_date>"
            //+ "                 <!--记账人，可以为空, 最大长度为30,类型为: java.lang.String -->"
            + "                 <posting_person>[$posting_person]</posting_person>"
            //+ "                 <!--来源系统，不能为空,固定值-->"
            + "                 <voucher_making_system>GL</voucher_making_system>"
            //+ "                 <!--备注，可以为空，最大长度为100,类型为: java.lang.String -->"
            + "                 <memo1>[$memo1]</memo1>"
            + "                 <memo2>[$memo2]</memo2>"
            //+ "                 <!--收付款对象编码，不能为空，最大长度为100,类型为: java.lang.String -->"
            + "                 <reserve1>[$reserve1]</reserve1>"
            //+ "                 <!--收付款对象名称，不能为空，最大长度为100,类型为: java.lang.String -->"
            + "                 <reserve2>[$reserve2]</reserve2>"
            + "             </voucher_head>"
            + "             <voucher_body>"
            //+ "                 <!--分录明细-->"
            + "               [$entry]"
            + "             </voucher_body>"
            + "     </voucher>"
            + "</ufinterface>";

    public static String WSFadCertificateDetailXML = ""
            //+ "<!--分录明细-->"
            + "<entry>"
            //+ "     <!--分录号，不能为空，最大长度为4,类型为:int -->"
            + "     <entry_id>[$entry_id]</entry_id>"
            //+ "     <!--科目，不能为空, 最大长度为30,类型为: java.lang.String -->"
            + "     <account_code>[$account_code]</account_code>"
            //+ "     <!--摘要，不能为空, 最大长度为100,类型为: java.lang.String -->"
            + "     <abstract>[$abstract]</abstract>"
            //+ "     <!--结算方式，可以为空, 最大长度为30,类型为: java.lang.String -->"
            + "     <settlement>[$settlement]</settlement>"
            //+ "     <!--票据号，可以为空, 最大长度为20,类型为: java.lang.String -->"
            + "     <document_id>[$document_id]</document_id>"
            //+ "     <!--票据日期，可以为空, 最大长度为10,类型为: Date -->"
            + "     <document_date>[$document_date]</document_date>"
            //+ "     <!--币种，不能为空, 最大长度为30,类型为: java.lang.String -->"
            + "     <currency>[$currency]</currency>"
            //+ "     <!--单价，可以为空, 最大长度为20,类型为:numric(20,8) -->"
            + "     <unit_price>[$unit_price]</unit_price>"
            //+ "     <!--汇率1，主辅币核算时使用，折辅汇率，可以为空, 最大长度为20,类型为:numric(20,8)-->"
            + "     <exchange_rate1>[$exchange_rate1]</exchange_rate1>"
            //+ "     <!--汇率2，折本汇率，可以为空, 最大长度为20,类型为:numric(20,8)-->"
            + "     <exchange_rate2>[$exchange_rate2]</exchange_rate2>"
            //+ "     <!--借方数量，可以为空, 最大长度为20,类型为:numric(20,8)-->"
            + "     <debit_quantity>[$debit_quantity]</debit_quantity>"
            //+ "     <!--原币借方发生额，不能为空, 最大长度为20,类型为:numric(20,2)-->"
            + "     <primary_debit_amount>[$primary_debit_amount]</primary_debit_amount>"
            //+ "     <!--辅币借方发生额，可以为空, 最大长度为20,类型为:numric(20,8)-->"
            + "     <secondary_debit_amount>[$secondary_debit_amount]</secondary_debit_amount>"
            //+ "     <!--本币借方发生额，不能为空, 最大长度为20,类型为:numric(20,2)-->"
            + "     <natural_debit_currency>[$natural_debit_currency]</natural_debit_currency>"
            //+ "     <!--贷方数量，可以为空, 最大长度为20,类型为:numric(20,8)-->"
            + "     <credit_quantity>[$credit_quantity]</credit_quantity>"
            //+ "     <!--原币贷方发生额，不能为空, 最大长度为20,类型为:numric(20,8)-->"
            + "     <primary_credit_amount>[$primary_credit_amount]</primary_credit_amount>"
            //+ "     <!--辅币贷方发生额，可以为空, 最大长度为20,类型为:numric(20,8)-->"
            + "     <secondary_credit_amount>[$secondary_credit_amount]</secondary_credit_amount>"
            //+ "     <!--本币贷方发生额，不能为空, 最大长度为20,类型为:numric(20,8)-->"
            + "     <natural_credit_currency>[$natural_credit_currency]</natural_credit_currency>"
            //+ "     <!--原始单据类型，可以为空, 最大长度为30,类型为: java.lang.String -->"
            + "     <bill_type>[$bill_type]</bill_type>"
            //+ "     <!--原始单据编号， 可以为空, 最大长度为20,类型为: java.lang.String -->"
            + "     <bill_id>[$bill_id]</bill_id>"
            //+ "     <!--原始单据日期，可以为空, 最大长度为10,类型为:Date-->"
            + "     <bill_date>[$bill_date]</bill_date>"
            //+ "     <!--辅助核算项目-->"
            + "     [$auxiliary_accounting]"
            + "</entry>";

    public static String WSFadCertificateAccountXML = ""
            //+"<!--辅助核算项目 按项目或部门人员核算?-->"
            + "<auxiliary_accounting>"
            + "      <item name='[$account_no]'>[$account_info_code]</item>"
            + "</auxiliary_accounting>";

    /**
     * 字符串值处理
     *
     * @param value 字符串值
     * @return 字符串值为""时返回"0",否则返回字符串值
     */
    String isEmptyReturnZero(String value);

    /**
     * 字符串值处理
     *
     * @param value 字符串值
     * @return 字符串值与数字0相等时返回"",否则返回字符串值
     */
    String isZeroReturnEmpty(String value);

    /**
     * 字符串值处理
     *
     * @param value 字符串值
     * @return 字符串值为null时返回"",否则返回字符串值
     */
    String isNullReturnEmpty(Object value);

    /**
     * 字符串值处理
     *
     * @param value 数字整型值
     * @return 数字整型值为null时返回"",否则返回字符串值
     */
    String isNullReturnEmpty(Integer value);

    /**
     * 日期验证
     *
     * @param dateStr       日期字符串
     * @param dateFormatStr 用于效验【日期字符串】的【日期格式字符串】
     * @param state         主表凭证的state(流程状态:0为【未发送NC】,1为【已发送NC】,2为【作废】)
     */
    void certificateCheckToDayValidDate(String dateStr, String dateFormatStr, String state);

    String nvl(Object value1, Object value2);

    String AccountNoNCSet(String AccountNo);

    String SplitAccountInfoCodeForScdpUser(String accountInfoCode);
    /**
     * 数字整型值判定
     *
     * @param value 字符串值
     * @return 字符串值转换数字整型值成功返回true, 否则返回false
     */
    //Boolean isInteger(String value);

    /**
     * uuid是否对应ncCode判定
     *
     * @param value 字符串值
     * @return uuid对应ncCode成功返回true, 否则返回false
     */
    Boolean isUuidBelongNcCode(String value);

    /**
     * 字符串值处理,消除字符串值的重复行
     *
     * @param values 字符串值
     * @param SP     行界定符
     * @return 消除重复行的字符串值
     */
    String StringDistinct(String values, String SP);

    /**
     * 字符串值处理,消除发送NC字符串值的禁忌符号
     *
     * @param value 字符串值
     * @return 消除重复行的字符串值
     */
    String sendNcStringFilter(String value);

    /**
     * in/out处理,同档LoadAction中转换【certificate-edit-layout】out的各项code、id到name
     *
     * @param outMap out到前台的【certificate-edit-layout】集合
     */
    void setExtraData(Map outMap);

    /**
     * 根据businessId(业务表uuid)、
     * originalFormType
     * (
     * 业务表内部类型:
     * 1.个人请款、
     * 2.采购合同请款、
     * 3.保证金请款、
     * 4.无请款报销、
     * 5.有请款报销、
     * 6.无请款采购合同、
     * 7.有请款采购合同、
     * 8.采购合同成本确认、
     * 9.采购合同支付(20000以下)、
     * 10.采购合同支付、
     * 11.还款、
     * 12.收款
     * 13.开票
     * )
     * 设定
     * fad_invoice(发票)、
     * fad_cash_req(请款)、
     * fad_pay_req_c(支付)、
     * fad_cash_clearance(还款)、
     * prm_receipts(收款)
     * prm_billing(开票)
     * 业务表的state(流程状态)到【已发送NC】(state值为4)或【作废】(state值为3)
     *
     * @param fadCertificateUuid   主表凭证uuid
     * @param businessId           业务表uuid
     * @param originalFormType     业务表内部类型
     * @param state                主表凭证的state(流程状态:0为【未发送NC】,1为【已发送NC】,2为【作废】)
     * @param certifiBusinessState 红冲凭证情况下其原凭证相关业务表的state预设定(流程状态:0为【草稿】,1为【提交】,2为【审核通过】,3为【作废】,4为【已发送NC】,8为【已生成凭证】)
     * @param BusinessState        业务表的state预设定(流程状态:0为【草稿】,1为【提交】,2为【审核通过】,3为【作废】,4为【已发送NC】,8为【已生成凭证】)
     * @return 主表凭证的state为【未发送NC】时,则不更新业务表的state返回false,
     * 主表凭证的state为【已发送NC】时,且主表凭证不是红冲凭证,则更新业务表的state到【已发送NC】返回true
     * 主表凭证的state为【已发送NC】时,且主表凭证是红冲凭证,则更新业务表的state到【作废】返回true
     * 主表凭证的state为【已发送NC】时,根据业务表uuid无法找到业务表时抛出异常
     * 主表凭证的state为【作废】时,且主表凭证不是红冲凭证,则更新业务表的state到【作废】返回true
     * 主表凭证的state为【作废】时,且主表凭证是红冲凭证,则更新业务表的state到【已发送NC】返回true
     * 主表凭证的state为【作废】时,根据业务表uuid无法找到业务表时抛出异常
     */
    Boolean updateStateToSent(String fadCertificateUuid, String businessId, String originalFormType, String state, String certifiBusinessState, String BusinessState, Map mResult);

    /**
     * 根据业务表uuid、
     * 业务表外部类型:
     * 0.FAD_INVOICE(发票)
     * 1.FAD_CASH_REQ(请款)
     * 2.FAD_PAY_REQ_C(支付)
     * 3.FAD_CASH_CLEARANCE(还款)
     * 4.PRM_RECEIPTS(收款)
     * 5.PRM_BILLING(开票)
     * <p>
     * 业务表内部类型:
     * 1.个人请款、
     * 2.采购合同请款、
     * 3.保证金请款、
     * 4.无请款报销、
     * 5.有请款报销、
     * 6.无请款采购合同、
     * 7.有请款采购合同、
     * 8.采购合同成本确认、
     * 9.采购合同支付(20000以下)、
     * 10.采购合同支付、
     * 11.还款、
     * 12.收款
     * 13.开票
     * 生成相应凭证
     *
     * @param businessId              业务表uuid
     * @param originalFormTypeForeign 业务表外部类型
     * @return 凭证生成成功返回主表凭证uuid, 否则返回""或抛出异常
     */
    String javaBeanToCertificate(String businessId, String originalFormTypeForeign);

    String getFadCertificateUuidByBusinessId(String businessId);

    /**
     * 根据业务表uuid、业务表外部类型判定并取得业务表内部类型
     *
     * @param businessId              业务表uuid
     * @param originalFormTypeForeign 业务表外部类型
     * @return 返回业务表内部类型, 不能判定则返回""
     */
    String getOriginalFormType(String businessId, String originalFormTypeForeign);

    /**
     * 根据业务表内部类型判定并取得业务表外部类型
     *
     * @param originalFormType 业务表内部类型
     * @return 返回业务表外部类型, 不能判定则返回""
     */
    String getOriginalFormTypeForeign(String originalFormType);

    /**
     * 根据发票业务表uuid判断发票相关采购合同是否外协
     *
     * @param businessId 发票业务表uuid
     * @return 发票相关采购合同是外协返回true, 否则返回false
     */
    Boolean isContractWaixie(String businessId);

    /**
     * 根据业务表uuid为子表凭证摘要的科目设定相关辅助核算项到分录辅助核算
     *
     * @param businessId 业务表uuid
     */
    void certificateDetailSetDetailForBusinessId(String businessId, List fadCertificateList);

    /**
     * 根据业务表uuid为子表凭证摘要的科目设定相关辅助核算项到分录辅助核算
     *
     * @param businessId 业务表uuid
     */
    void certificateDetailSetAccountForBusinessId(String businessId, List fadCertificateList);

    /**
     * 根据业务表uuid将子表凭证摘要的原始单据编号、摘要(包含项目非项目中海财务内部自定义科目号)设定到主表凭证
     *
     * @param businessId 业务表uuid
     */
    void certificateMergeOriginalFormCodeAbstractsFromCertificateDetail(String businessId, List fadCertificateList);

    /**
     * 根据主表凭证uuid将子表凭证摘要的原始单据编号、摘要(包含项目非项目中海财务内部自定义科目号)设定到主表凭证
     *
     * @param fadCertificateUuid 主表凭证uuid
     */
    void certificateMergeOriginalFormCodeAbstractsFromCertificateDetailByFadCertificateUuid(String fadCertificateUuid);

    /**
     * 根据【非项目凭证设定规则】更新【业务表uuid】相关【非项目凭证】
     *
     * @param businessId 业务表uuid
     */
    void nonProjectSetRuleToUpdateCertificateSetDetailSubject(String businessId, String originalFormTypeForeign, String originalFormType, List fadCertificateList);

    /**
     * 根据【项目凭证设定规则】更新【业务表uuid】相关【项目凭证】
     *
     * @param businessId 业务表uuid
     */
    void projectSetRuleToUpdateCertificateSetDetailSubject(String businessId, String originalFormTypeForeign, String originalFormType, List fadCertificateList);

    /**
     * 根据【项目凭证运行费设定规则】更新【业务表uuid】相关【项目凭证】
     *
     * @param businessId 业务表uuid
     */
    void projectRunSetRuleToUpdateCertificateSetDetailSubject(String businessId, String originalFormTypeForeign, String originalFormType, List fadCertificateList);

    /**
     * 根据【非项目凭证设定规则】更新【业务表uuid】相关【非项目凭证】之【辅助核算】
     *
     * @param businessId 业务表uuid
     */
    void nonProjectSetRuleToUpdateCertificateSetAccountRtfree(String businessId, String originalFormTypeForeign, String originalFormType, List fadCertificateList);

    /**
     * 根据【项目凭证设定规则】更新【业务表uuid】相关【项目凭证】之【辅助核算】
     *
     * @param businessId 业务表uuid
     */
    void projectSetRuleToUpdateCertificateSetAccountRtfree(String businessId, String originalFormTypeForeign, String originalFormType, List fadCertificateList);

    /**
     * 根据【项目凭证运行费设定规则】更新【业务表uuid】相关【项目凭证】之【辅助核算】
     *
     * @param businessId 业务表uuid
     */
    void projectRunSetRuleToUpdateCertificateSetAccountRtfree(String businessId, String originalFormTypeForeign, String originalFormType, List fadCertificateList);


    /**
     * 根据业务表uuid设定凭证物理字段
     *
     * @param businessId 业务表uuid
     */
    void certificateSetSystemUpdataField(String businessId, List fadCertificateList);

    /**
     * 根据业务表uuid判断原凭证借贷金额是否小于等于0,小于等于0则抛出异常以回滚本次所有相关更新
     *
     * @param businessId 业务表uuid
     */
    void ifCertificateLocalZeroThrowException(String businessId);

    /**
     * 根据业务表uuid判断是否重复生成凭证,重复生成凭证则抛出异常以回滚本次所有相关更新
     *
     * @param businessId 业务表uuid
     */
    void ifCertificateAlreadyExistThrowException(String businessId);

    /**
     * 生成主表凭证->请款
     *
     * @param businessId       业务表uuid
     * @param originalFormType 业务表内部类型
     */
    void createCertificateFromBusinessReqHead(String businessId, String originalFormType);

    /**
     * 生成子表凭证摘要->个人请款(1日常2差旅)
     *
     * @param businessId       业务表uuid
     * @param originalFormType 业务表内部类型
     */
    void createCertificateFromBusinessPersonalReq(String businessId, String originalFormType);

    /**
     * 生成子表凭证摘要->材料费或外包商请款(0采购合同)
     *
     * @param businessId       业务表uuid
     * @param originalFormType 业务表内部类型
     */
    void createCertificateFromBusinessPurchaseContractReq(String businessId, String originalFormType);

    /**
     * 生成子表凭证摘要->保证金请款(3保证金)
     *
     * @param businessId       业务表uuid
     * @param originalFormType 业务表内部类型
     */
    void createCertificateFromBusinessBondReq(String businessId, String originalFormType);

    /**
     * 生成子表凭证摘要->周转金请款(4周转金)
     *
     * @param businessId       业务表uuid
     * @param originalFormType 业务表内部类型
     */
    void createCertificateFromRevolvingReq(String businessId, String originalFormType);

    /**
     * 生成主表凭证->报销与采购合同发票
     *
     * @param businessId       业务表uuid
     * @param originalFormType 业务表内部类型
     */
    void createCertificateFromBusinessInvoiceHead(String businessId, String originalFormType);

    /**
     * 生成子表凭证摘要->无请款报销
     *
     * @param businessId       业务表uuid
     * @param originalFormType 业务表内部类型
     */
    void createCertificateFromBusinessNoPaymentReimbursementInvoice(String businessId, String originalFormType);

    /**
     * 生成子表凭证摘要->有请款报销
     *
     * @param businessId       业务表uuid
     * @param originalFormType 业务表内部类型
     */
    void createCertificateFromBusinessPaymentReimbursementInvoice(String businessId, String originalFormType);

    /**
     * 生成子表凭证摘要->无请款采购合同
     *
     * @param businessId       业务表uuid
     * @param originalFormType 业务表内部类型
     */
    void createCertificateFromBusinessNoPaymentPurchaseContractInvoice(String businessId, String originalFormType);

    void createCertificateFromBusinessNoPaymentPurchaseContractInvoiceWaixie(String businessId, String originalFormType);

    void createCertificateFromBusinessNoPaymentPurchaseContractInvoiceMaterial(String businessId, String originalFormType);

    /**
     * 生成子表凭证摘要->有请款采购合同应付
     *
     * @param businessId       业务表uuid
     * @param originalFormType 业务表内部类型
     */
    void createCertificateFromBusinessPaymentPurchaseContractPayableInvoice(String businessId, String originalFormType);

    void createCertificateFromBusinessPaymentPurchaseContractPayableInvoiceWaixie(String businessId, String originalFormType);

    void createCertificateFromBusinessPaymentPurchaseContractPayableInvoiceMaterial(String businessId, String originalFormType);

    /**
     * 生成子表凭证摘要->有请款采购合同预付
     *
     * @param businessId       业务表uuid
     * @param originalFormType 业务表内部类型
     */
    void createCertificateFromBusinessPaymentPurchaseContractPaidInvoice(String businessId, String originalFormType);

    void createCertificateFromBusinessPaymentPurchaseContractPaidInvoiceWaixie(String businessId, String originalFormType);

    void createCertificateFromBusinessPaymentPurchaseContractPaidInvoiceMaterial(String businessId, String originalFormType);

    /**
     * 生成子表凭证摘要->采购合同成本确认
     *
     * @param businessId       业务表uuid
     * @param originalFormType 业务表内部类型
     */
    void createCertificateFromBusinessPurchaseContractCostConfirmationInvoice(String businessId, String originalFormType);

    void createCertificateFromBusinessPurchaseContractCostConfirmationInvoiceMaterial(String businessId, String originalFormType);

    /**
     * 生成子表凭证摘要->采购合同支付20000以下
     *
     * @param businessId       业务表uuid
     * @param originalFormType 业务表内部类型
     */
    void createCertificateFromBusinessPurchaseContractPaymentLessThan20000Invoice(String businessId, String originalFormType);

    void createCertificateFromBusinessPurchaseContractPaymentLessThan20000InvoiceMaterial(String businessId, String originalFormType);

    /**
     * 生成主表凭证、子表凭证摘要->采购合同支付
     *
     * @param businessId       业务表uuid
     * @param originalFormType 业务表内部类型
     */
    void createCertificateFromBusinessPurchaseContractPayment(String businessId, String originalFormType);

    /**
     * 生成主表凭证、子表凭证摘要->还款
     *
     * @param businessId       业务表uuid
     * @param originalFormType 业务表内部类型
     */
    void createCertificateFromBusinessPayBackTheMoney(String businessId, String originalFormType);

    /**
     * 生成主表凭证、子表凭证摘要->收款
     *
     * @param businessId       业务表uuid
     * @param originalFormType 业务表内部类型
     */
    void createCertificateFromBusinessReceivables(String businessId, String originalFormType);

    /**
     * 生成主表凭证、子表凭证摘要->收款(待确认)
     *
     * @param businessId       业务表uuid
     * @param originalFormType 业务表内部类型
     */
    void createCertificateFromBusinessReceivablesPendingConfirmation(String businessId, String originalFormType);

    /**
     * 生成主表凭证、子表凭证摘要->收款(调整)
     *
     * @param businessId       业务表uuid
     * @param originalFormType 业务表内部类型
     */
    void createCertificateFromBusinessReceivablesAdjustment(String businessId, String originalFormType);

    /**
     * 生成主表凭证、子表凭证摘要->开票
     *
     * @param businessId       业务表uuid
     * @param originalFormType 业务表内部类型
     */
    void createCertificateFromBusinessBill(String businessId, String originalFormType);

    /**
     * 凭证合并处理
     *
     * @param fadCertificateGridSelectionUuidList 须合并的凭证列表
     * @return 合并成功返回"OK",否则返回errorMsg(异常信息)
     */
    String certificateMerge(List fadCertificateGridSelectionUuidList);

    /**
     * 合并凭证复原处理
     *
     * @param fadCertificateUuid 须复原的主表凭证uuid
     * @return 复原成功返回"OK",否则返回errorMsg(异常信息)
     */
    String certificateMergeRestore(String fadCertificateUuid);

    /**
     * 凭证拆分处理
     *
     * @param fadCertificateUuid    须拆分的主表凭证uuid
     * @param certificateSplitCount 拆分份数
     * @return 拆分成功返回"OK",否则返回errorMsg(异常信息)
     */
    String certificateSplit(String fadCertificateUuid, Integer certificateSplitCount);

    /**
     * 拆分凭证复原处理
     *
     * @param fadCertificateUuid 须复原的主表凭证uuid
     * @return 复原成功返回"OK",否则返回errorMsg(异常信息)
     */
    String certificateSplitRestore(String fadCertificateUuid);

    /**
     * 主表凭证逻辑删除处理
     *
     * @param fadCertificate 主表凭证dto
     */
    void certificateSetVoid(FadCertificateDto fadCertificate, Integer isVoid);

    /**
     * 主表凭证逻辑删除批量处理
     *
     * @param fadCertificate            主表凭证dto
     * @param fadCertificateLst1        主表凭证dto集合
     * @param fadCertificateDetailLst2  子表凭证摘要dto集合
     * @param fadCertificateAccountLst3 分录辅助核算dto集合
     */
    void certificateSetVoid(FadCertificateDto fadCertificate, List fadCertificateLst1, List fadCertificateDetailLst2, List fadCertificateAccountLst3, Integer isVoid);

    /**
     * 子表凭证摘要新增处理
     *
     * @param fadCertificateLst1 主表凭证dto集合
     */
    void certificateSetSon(List fadCertificateLst1);

    /**
     * 子表凭证摘要逻辑删除处理
     *
     * @param fadCertificateDetail 子表凭证摘要dto
     */
    void certificateDetailSetVoid(FadCertificateDetailDto fadCertificateDetail, Integer isVoid);

    /**
     * 子表凭证摘要逻辑删除批量处理
     *
     * @param fadCertificateDetail      子表凭证摘要dto
     * @param fadCertificateDetailLst2  子表凭证摘要dto集合
     * @param fadCertificateAccountLst3 分录辅助核算dto集合
     */
    void certificateDetailSetVoid(FadCertificateDetailDto fadCertificateDetail, List fadCertificateDetailLst2, List fadCertificateAccountLst3, Integer isVoid);

    /**
     * 分录辅助核算新增处理
     *
     * @param fadCertificateDetailLst2 子表凭证摘要dto集合
     */
    void certificateDetailSetSon(List fadCertificateDetailLst2);

    /**
     * 设定子表凭证摘要的分录号、SEQ_NO
     *
     * @param fadCertificateDetailLst2 子表凭证摘要dto集合
     */
    void certificateDetailSetOrder(List fadCertificateDetailLst2);

    /**
     * 主表凭证合并分录同类项处理
     *
     * @param fadCertificateUuid 须合并分录同类项的主表凭证uuid
     * @return 合并成功返回"OK",否则返回errorMsg(异常信息)
     */
    String certificateDetailMerge(String fadCertificateUuid);

    /**
     * 验证合并拆分凭证与其原凭证借贷金额是否相符,在凭证合并、拆分、发送时验证,金额不相符则抛出异常
     *
     * @param fadCertificate 主表凭证dto
     */
    void certificateCheckMoney(FadCertificateDto fadCertificate);

    /**
     * 主表凭证时间戳验证
     *
     * @param fadCertificateUuid 须时间戳验证的主表凭证uuid
     * @param tblVersion         时间戳
     */
    void certificateCheckTimeStamp(String fadCertificateUuid, String tblVersion);

    /**
     * 表时间戳验证
     *
     * @param tableName    须时间戳验证的表名
     * @param businessName 须时间戳验证的业务名
     * @param uuid         须时间戳验证的表uuid
     * @param tblVersion   时间戳
     */
    void CheckTimeStamp(String tableName, String businessName, String uuid, String tblVersion);

    /**
     * 凭证红冲处理
     *
     * @param fadCertificateUuid 须红冲的主表凭证uuid
     * @return 红冲成功返回"OK",否则返回errorMsg(异常信息)
     */
    String certificateDeficit(String fadCertificateUuid);

    Map certificateSendNC(String fadCertificateUuid, String makerUuid);

    Map certificateToLogicVoid(String fadCertificateUuid, String makerUuid);

    Map certificateToLogicVoidAlone(String fadCertificateUuid, String makerUuid);
}