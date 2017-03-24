Ext.define("Responsiblesubject.model.ResponsibleSubjectModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','principal','subjectCode','remark','companyCode','projectId','departmentCode','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);