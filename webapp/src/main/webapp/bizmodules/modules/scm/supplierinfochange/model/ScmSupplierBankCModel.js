Ext.define("Supplierinfochange.model.ScmSupplierBankCModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','puuid','scmSupplierId','bankName','accountNo','bankAddress','remark','companyCode','projectId','departmentCode','createBy','createTime','updateBy','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0},'isEffect','seqNo']
	}
);