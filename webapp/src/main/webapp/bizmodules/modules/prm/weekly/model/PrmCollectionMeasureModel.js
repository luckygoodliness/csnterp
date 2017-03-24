Ext.define("Weekly.model.PrmCollectionMeasureModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','prmProjectMainId','principalName','prmWeeklyId','periods','report','principal','measureMoney','sign','actualReceiptsDate','remark','state','companyCode','projectId','departmentCode','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);