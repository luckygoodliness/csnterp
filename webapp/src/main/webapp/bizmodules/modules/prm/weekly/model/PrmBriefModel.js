Ext.define("Weekly.model.PrmBriefModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','prmProjectMainId','prmWeeklyId','type','content','state','companyCode','projectId','departmentCode','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);