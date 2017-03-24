Ext.define("Prmblacklistmonth.model.PrmBlacklistMonthModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','scmSupplierId','monthFrom','monthTo','prmProjectMainId','complainDepartment','complainContract','complainant','reason','remark','companyCode','projectId','departmentCode','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);