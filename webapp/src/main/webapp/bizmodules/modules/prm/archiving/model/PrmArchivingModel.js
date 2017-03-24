Ext.define("Archiving.model.PrmArchivingModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','prmProjectMainId','archivingDate','archivingReqDate','state','companyCode','projectId','departmentCode','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);