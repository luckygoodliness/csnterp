Ext.define("Invoice.model.FadInvoiceTravelModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','fadInvoiceId','type','invoiceNum','invoiceMoney','approvedMoney','companyCode','projectId','departmentCode','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);