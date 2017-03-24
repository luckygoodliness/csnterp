Ext.define("Cashclearance.model.FadCashReqInvoiceModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','fadCashReqId','fadInvoiceId','clearanceMoney','clearanceType','companyCode','projectId','departmentCode','createBy','createTime','updateBy','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0},'seqNo','state','runningNo']
	}
);