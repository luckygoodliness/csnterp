Ext.define("Supplierinfochange.model.ScmSukpplierKeyCModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','puuid','scmSupplierId','keyNaem','remark','companyCode','projectId','departmentCode','createBy','createTime','updateBy','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0},'seqNo']
	}
);