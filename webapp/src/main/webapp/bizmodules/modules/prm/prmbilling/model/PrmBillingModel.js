Ext.define("Prmbilling.model.PrmBillingModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'prmProjectMainId', 'isPreProject', 'cdmBillStateCombo','remark', 'fadInvoiceCombo', 'fadTaxRateCombo', 'customerId', 'customerName', 'customerInvoiceName', 'customerInvoiceNameEn', 'customerLocation', 'exchangeRate', 'invoiceCurrency', 'originalCurrency', 'originalMoney', 'phone', 'taxNo', 'bankName', 'bankAccount', 'invoiceMoney', 'reqPerson', 'reqOffice', 'maker', 'invoiceType', 'taxRate', 'state', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', 'taxRateName', {
            name: 'isVoid',
            defaultValue: 0
        }, 'netMoney', 'taxMoney', 'reqOfficeDesc', 'locationType', 'paymentType', 'expectReceiveDate', 'customerInvoiceId', 'prmContractId','billType','repealUuid','reason']
    }
);