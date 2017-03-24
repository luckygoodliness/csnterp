Ext.define("Balanceofcontract.model.ScmContractPayModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','scmContractId','swiftNumber','amount','payType','payClass','payee','payTime','isDebt','writeOff','voucherNo','principal','companyCode','createBy','createTime','updateBy','updateTime','locTimezone','projectId','departmentCode','createOffice','updateOffice','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);