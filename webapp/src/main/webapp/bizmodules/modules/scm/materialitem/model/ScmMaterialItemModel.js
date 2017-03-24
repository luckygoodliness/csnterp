Ext.define("Materialitem.model.ScmMaterialItemModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','code','name',{name: 'available', defaultValue: 0},'remark','companyCode','projectId','departmentCode','createBy','createTime','updateBy','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0},'seqNo']
	}
);