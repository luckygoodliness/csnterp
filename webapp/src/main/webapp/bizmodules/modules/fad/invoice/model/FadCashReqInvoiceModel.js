Ext.define("Invoice.model.FadCashReqInvoiceModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'invoiceNo', 'fadCashReqId', 'cashContract', 'cashVoucher', 'runningNo',
            'invoiceRunningNo', 'clearanceTypeName','state',
            'fadInvoiceId', 'reqMoney', 'clearanceMoney','clearancedMoney', 'clearanceType', 'companyCode', 'projectId',
            'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice',
            'updateTime', 'locTimezone', 'tblVersion', {name: 'isVoid', defaultValue: 0}]
    }
);