Ext.define("Responsibleproject.model.ResponsibleProjectModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','principal','principalDesc','responsibleProject','projectCode','responsibleProjectDesc','remark','companyCode','projectId','departmentCode','createBy','createTime','updateBy','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0},'seqNo']
	}
);