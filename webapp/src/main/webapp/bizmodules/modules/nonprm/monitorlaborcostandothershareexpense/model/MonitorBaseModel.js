Ext.define("Monitorlaborcostandothershareexpense.model.MonitorBaseModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','officeId','companyCode','projectId','departmentCode','createBy','createTime','updateBy','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0},'seqNo']
	}
);