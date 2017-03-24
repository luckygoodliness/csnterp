Ext.define("Invoice.model.FadInvoiceSubsidyModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','fadInvoiceId','prmProjectName','tripLocation','tripDays','standard','money','companyCode','projectId','departmentCode','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);