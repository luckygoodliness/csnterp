Ext.define("Invoice.model.ScmContractInvoiceModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','fadSubjectCode','projectName','subjectName','officeName','scmContractId','scmContractCode','fadInvoiceMoneyL','fadInvoiceMoney','contractSubjectCode','fadInvoiceId','amount','contractTotalValue','companyCode','createBy','createTime','updateBy','updateTime','locTimezone','projectId','departmentCode','createOffice','updateOffice','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);