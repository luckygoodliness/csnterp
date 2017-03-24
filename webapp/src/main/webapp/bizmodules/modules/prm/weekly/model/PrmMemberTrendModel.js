Ext.define("Weekly.model.PrmMemberTrendModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','prmProjectMainId','staffName','prmWeeklyId','staffId','beginDate','endDate','percent','state','companyCode','projectId','departmentCode','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);