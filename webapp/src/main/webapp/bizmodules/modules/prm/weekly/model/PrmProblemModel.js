Ext.define("Weekly.model.PrmProblemModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','prmWeeklyId','postPersonName','providerName','prmProjectMainId','postDate','problemType','description','postPerson','proposalSolution','proposalDate','provider','remark','state','companyCode','projectId','departmentCode','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);