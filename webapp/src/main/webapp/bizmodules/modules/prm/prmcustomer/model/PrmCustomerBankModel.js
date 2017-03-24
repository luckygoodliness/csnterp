Ext.define("Prmcustomer.model.PrmCustomerBankModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','prmCustomerId','bankCode','bankName','bankNumber','companyCode','projectId','departmentCode','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);