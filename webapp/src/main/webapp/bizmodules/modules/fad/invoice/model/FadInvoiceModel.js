Ext.define("Invoice.model.FadInvoiceModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'isWriteOff','payee','fadProjectMainId', 'projectCode', 'bank', 'bankId', 'invoiceCode', 'expensesType', 'subjectId', 'subjectCode', 'fadSubjectCode', 'subjectName', 'buildTime', 'invoiceNo', 'invoiceNum', 'invoiceTotalValue', 'expensesMoney', 'supplierId', 'supplierName', 'taxRegistrationNo', 'invoiceReqNo', 'invoiceType', 'payStyle', 'taxRate', 'taxRateDesc', 'renderPerson', 'tripLocation', 'tripBeginDate', 'tripEndDate', 'tripDays', 'tripReason', 'state', 'remark', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'officeId', 'officeName', 'officeIdDesc', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', 'scmContractCode','directPayment','fadYear','fadMonth', {
            name: 'isVoid',
            defaultValue: 0
        }]
    }
);