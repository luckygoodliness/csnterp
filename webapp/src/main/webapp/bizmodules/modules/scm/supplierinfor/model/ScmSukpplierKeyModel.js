Ext.define("Supplierinfor.model.ScmSukpplierKeyModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','scmSupplierId','keyNaem','remark','companyCode','projectId','departmentCode','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);