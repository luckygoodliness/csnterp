Ext.define("Balanceofcontract.model.ScmContractModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','scmContractCode','contractNature','supplierCode','supplierName','amount','quantity','purchaseType','contractPayType','otherDes','debter','debterDepartment','bank','bankId','totalValue','payType','isUrgent','debtDate','state','effectiveDate','stampDate','contractState','isVirtual','paid','invoiceAmount','remark','companyCode','projectId','departmentCode','officeId','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);