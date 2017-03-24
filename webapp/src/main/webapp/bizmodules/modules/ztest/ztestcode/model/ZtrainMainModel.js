Ext.define("Ztestcode.model.ZtrainMainModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','code','name','phone','email','cusomterUuid','companyCode','projectId','departmentCode','createBy','createTime','updateBy','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0},'seqNo','state']
	}
);