Ext.define("Weekly.model.PrmProgressModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','prmProjectMainId','prmWeeklyId','projectProgress','progressDeviationReason','receiptsDeviationReason','management','state','companyCode','projectId','departmentCode','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);