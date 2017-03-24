Ext.define("Supplierinfor.model.ScmSupplierBankModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','scmSupplierId','bankName','accountNo','bankAddress','remark','companyCode','projectId','departmentCode','createBy','createOffice','createTime','isEffect','seqNo','updateBy','updateOffice','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);