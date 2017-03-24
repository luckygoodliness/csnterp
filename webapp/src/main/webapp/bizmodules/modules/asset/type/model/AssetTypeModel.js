Ext.define("Type.model.AssetTypeModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','code','name','isenabled','upgradeCode','descp','companyCode','companyName','departmentCode','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime',{name: 'isVoid', defaultValue: 0},'locTimezone','tblVersion']
	}
);