Ext.define("Archiving.model.PrmArchivingDetailModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','prmArchivingId','volumeName','amount','pageNumber','type','companyCode','projectId','departmentCode','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);