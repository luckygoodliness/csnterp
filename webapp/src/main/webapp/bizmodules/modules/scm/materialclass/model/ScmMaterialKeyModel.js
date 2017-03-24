Ext.define("Materialclass.model.ScmMaterialKeyModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','scmMaterialClassId','keyName','remark','companyCode','projectId','departmentCode','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);