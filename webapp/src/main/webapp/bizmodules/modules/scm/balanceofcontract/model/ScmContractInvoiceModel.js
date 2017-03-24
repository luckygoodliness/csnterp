Ext.define("Balanceofcontract.model.ScmContractInvoiceModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','scmContractId','fadInvoiceId','amount','companyCode','createBy','createTime','updateBy','updateTime','locTimezone','projectId','departmentCode','createOffice','updateOffice','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);